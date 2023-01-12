/* Copyright (c) 2021 JAX-DB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import static org.jaxdb.jsql.Notification.Action.*;
import static org.libj.logging.LoggerUtil.*;
import static org.slf4j.event.Level.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.vendor.DBVendor;
import org.libj.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;

public class PostgreSQLNotifier extends Notifier<PGNotificationListener> {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLNotifier.class);

  private static final String channelName = "jaxdb_notify";
  private static final String dropAllFunction = channelName + "_drop_all";
  private static final String pgNotifyPageFunction = "pg_notify_page";
  private static final String sessionIdTimestamp = "SELECT CURRENT_SETTING('jaxdb.session_id', 't') INTO _sessionId;\nSELECT (EXTRACT(EPOCH FROM CURRENT_TIMESTAMP) * 1000)::BIGINT INTO _timestamp;\n";

  private static String getFunctionName(final data.Table<?> table, final Action action) {
    return channelName + "_" + table.getName() + "_" + action.toString().toLowerCase();
  }

  // list all LISTEN channels: SELECT * FROM pg_listening_channels()
  // list all triggers: SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE 'jaxdb_notify_%';
  // drop all triggers: SELECT "jaxdb_notify_drop_all"();
  // list all functions: SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name LIKE 'jaxdb_notify_%';
  // drop all functions: list the functions, then execute DROP FUNCTION %;
  private static final String createDropAllFunction =
    "BEGIN;\n" +
    "SELECT pg_advisory_xact_lock(2142616474639426746);\n" +
    "CREATE OR REPLACE FUNCTION " + dropAllFunction + "() RETURNS TEXT AS $$ DECLARE\n" +
    "  triggerNameRecord RECORD;\n" +
    "  triggerTableRecord RECORD;\n" +
    "  functionName TEXT;\n" +
    "BEGIN\n" +
    "  FOR triggerNameRecord IN SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE '" + channelName + "_%' LOOP\n" +
    "    FOR triggerTableRecord IN SELECT DISTINCT(event_object_table) FROM information_schema.triggers WHERE trigger_name = triggerNameRecord.trigger_name LOOP\n" +
    "      RAISE NOTICE 'DROP TRIGGER \"%\" ON \"%\"', triggerNameRecord.trigger_name, triggerTableRecord.event_object_table;\n" +
    "      EXECUTE 'DROP TRIGGER \"' || triggerNameRecord.trigger_name || '\" ON \"' || triggerTableRecord.event_object_table || '\";';\n" +
    "    END LOOP;\n" +
    "  END LOOP;\n" +
    "  FOR functionName IN SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name LIKE '" + channelName + "_%' LOOP\n" +
    "    EXECUTE 'DROP FUNCTION \"' || functionName || '\";';\n" +
    "  END LOOP;\n" +
    "  RETURN 'done';\n" +
    "END;\n" +
    "$$ LANGUAGE plpgsql SECURITY DEFINER;\n" +
    "END;";

  private static final String createPgNotifyPageFunction =
    "BEGIN;\n" +
    "SELECT pg_advisory_xact_lock(2142616474639426746);\n" +
    "CREATE OR REPLACE FUNCTION " + pgNotifyPageFunction + "(channel TEXT, message TEXT) RETURNS INTEGER AS $$ DECLARE\n" +
    "  pages INTEGER;\n" +
    "  hash TEXT;\n" +
    "BEGIN\n" +
    "  SELECT (char_length(message) / 7950) + 1 INTO pages;\n" +
    "  SELECT md5(message) INTO hash;\n" +
    "  FOR page IN 1..pages LOOP\n" +
    "    PERFORM pg_notify(channel, hash || ':' || pages || ':' || page || ':' || substr(message, ((page - 1) * 7950) + 1, 7950));\n" +
    "  END LOOP;\n" +
    "  RETURN 0;\n" +
    "END;\n" +
    "$$ LANGUAGE plpgsql SECURITY DEFINER;\n" +
    "END;";

  private PGNotificationListener listener;
  private final Map<String,StringBuilder> hashToPages = new ConcurrentHashMap<>();

  PostgreSQLNotifier(final Connection connection, final ConnectionFactory connectionFactory) throws SQLException {
    super(DBVendor.POSTGRE_SQL, connection, connectionFactory);
  }

  @Override
  void start(final Connection connection) throws IOException, SQLException {
    logm(logger, TRACE, "%?.start", "%?", this, connection);
    if (isClosed())
      return;

    try (final Statement statement = connection.createStatement()) {
      statement.execute(createDropAllFunction);
      statement.execute(createPgNotifyPageFunction);
    }

    synchronized (connection) {
      reconnect(connection, listener = new PGNotificationListener() {
        @Override
        public void notification(final int processId, final String channelName, String payload) {
          // 1. Parse payload sent by pg_notify_page(channel,payload)

          int j, i = payload.indexOf(':');
          final String hash = payload.substring(0, i);

          j = payload.indexOf(':', ++i);
          final int pages = Integer.parseInt(payload.substring(i, j));

          i = payload.indexOf(':', ++j);
          final int page = Integer.parseInt(payload.substring(j, i));

          if (pages == 1) {
            payload = payload.substring(++i);
          }
          else {
            // 2. Concatenate the pages, assuming they get delivered in order

            StringBuilder builder = hashToPages.get(hash);
            if (builder == null) {
              if (page != 1)
                throw new IllegalStateException("Expected page = 1, but got page = " + page);

              hashToPages.put(hash, builder = new StringBuilder());
            }

            // pg_notify guarantees messages are delivered in order they are sent for a single transaction
            builder.append(payload, ++i, payload.length());

            if (page != pages)
              return;

            // 3. When received last page, invoke this.notify(tableName,payload)

            payload = builder.toString();
            builder.setLength(0);
            hashToPages.remove(hash);
          }

          i = payload.indexOf("\"table\"");
          i = payload.indexOf('"', i + 7);
          final String tableName = payload.substring(++i, payload.indexOf('"', ++i));
          PostgreSQLNotifier.this.notify(tableName, payload);
        }

        @Override
        public void closed() {
          logm(logger, TRACE, "%?.closed", this);
          try {
            connection.unwrap(PGConnection.class).removeNotificationListener(channelName);
            if (!connection.isClosed())
              connection.close();
          }
          catch (final SQLException e) {
            if (logger.isWarnEnabled()) logger.warn("Failed to disconnect listener from PGConnection", e);
          }

          if (isClosed())
            return;

          try {
            reconnect(getConnection(), this);
          }
          catch (final IOException | SQLException e) {
            if (logger.isErrorEnabled()) logger.error("Failed getConnection()", e);
          }
        }
      });
    }
  }

  @Override
  void tryReconnect(final Connection connection, final PGNotificationListener listener) throws SQLException {
    logm(logger, TRACE, "%?.tryReconnect", "%?,%?", this, connection, listener);
    final PGConnection pgConnection = connection.unwrap(PGConnection.class);
    pgConnection.removeNotificationListener(channelName);
    pgConnection.addNotificationListener(channelName, channelName, listener);
  }

  private static String getCreateFunction(final data.Table<?> table, final Action action, final String functionName) {
    logm(logger, TRACE, "PostgreSQLNotifier.getCreateFunction", "%s,%s,%s", table, action, functionName);
    final String tableName = table.getName();
    final boolean hasKeyForUpdate = table._keyForUpdate$.length > 0;

    final StringBuilder sql = new StringBuilder("BEGIN;");

    sql.append("SELECT pg_advisory_xact_lock(2142616474639426746);\n");
    sql.append("CREATE OR REPLACE FUNCTION ").append(functionName).append("() RETURNS TRIGGER AS $$ DECLARE\n");
    sql.append("  _sessionId TEXT;\n");
    sql.append("  _timestamp BIGINT;\n");

    if (action == INSERT) {
      sql.append("BEGIN\n");
      sql.append(sessionIdTimestamp);
      sql.append("  PERFORM ").append(pgNotifyPageFunction).append("('").append(channelName).append("', json_build_object('sessionId', _sessionId, 'timestamp', _timestamp, 'table', '").append(tableName).append("', 'action', 'INSERT', 'cur', row_to_json(NEW))::text);\n");
    }
    else if (action == UPDATE) {
      sql.append("BEGIN\n");
      sql.append(sessionIdTimestamp);
      sql.append("  PERFORM ").append(pgNotifyPageFunction).append("('").append(channelName).append("', json_build_object('sessionId', _sessionId, 'timestamp', _timestamp, 'table', '").append(tableName).append("', 'action', 'UPDATE', 'old', row_to_json(OLD), 'cur', row_to_json(NEW))::text);\n");
    }
    else if (action == UPGRADE) {
      sql.append("  _old JSON;\n");
      sql.append("  _cur JSON;\n");
      sql.append("BEGIN\n");
      sql.append(sessionIdTimestamp);
      sql.append("  _old = row_to_json(OLD);\n");
      sql.append("  SELECT json_object_agg(COALESCE(old_json.key, new_json.key), new_json.value) INTO _cur\n");
      sql.append("  FROM json_each_text(_old) old_json\n");
      sql.append("  FULL OUTER JOIN json_each_text(row_to_json(NEW)) new_json ON new_json.key = old_json.key\n");
      sql.append("  WHERE new_json.value IS DISTINCT FROM old_json.value");

      if (table._primary$.length == 0)
        throw new IllegalArgumentException("Cannot create UPGRADE trigger on table without primary keys");

      for (final data.Column<?> primary : table._primary$) // [A]
        sql.append(" OR new_json.key = '").append(primary.name).append("'");

      sql.append(";\n");

      sql.append("  PERFORM ").append(pgNotifyPageFunction).append("('").append(channelName).append("', json_build_object('sessionId', _sessionId, 'timestamp', _timestamp, 'table', '").append(tableName).append("', 'action', 'UPGRADE'");
      if (hasKeyForUpdate) {
        sql.append(", 'keyForUpdate', json_build_object(");
        for (final data.Column<?> keyForUpdate : table._keyForUpdate$) // [A]
          sql.append('\'').append(keyForUpdate.name).append("',OLD.\"").append(keyForUpdate.name).append("\",");

        sql.setCharAt(sql.length() - 1, ')');
      }

      sql.append(", 'old', _old, 'cur', _cur)::text);\n");
    }
    else if (action == DELETE) {
      sql.append("BEGIN\n");
      sql.append(sessionIdTimestamp);
      sql.append("  PERFORM ").append(pgNotifyPageFunction).append("('").append(channelName).append("', json_build_object('sessionId', _sessionId, 'timestamp', _timestamp, 'table', '").append(tableName).append("', 'action', 'DELETE', 'old', row_to_json(OLD))::text);\n");
    }
    else {
      throw new UnsupportedOperationException("Unsupported Action: " + action);
    }

    sql.append("  RETURN NULL;\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql;\n");
    sql.append("END;");
    return sql.toString();
  }

  private static String getCreateTrigger(final data.Table<?> table, final Action action, final String triggerName) {
    logm(logger, TRACE, "PostgreSQLNotifier.getCreateTrigger", "%s,%s,%s", table, action, triggerName);

    final StringBuilder sql = new StringBuilder();
    sql.append("CREATE TRIGGER \"").append(triggerName).append("\" AFTER ").append(action.toSql()).append(" ON \"").append(table.getName()).append("\" FOR EACH ROW ");
    if (action instanceof UP)
      sql.append("WHEN (OLD.* IS DISTINCT FROM NEW.*) ");

    sql.append("EXECUTE PROCEDURE ").append(triggerName).append("()");
    return sql.toString();
  }

  @Override
  @SuppressWarnings("null")
  void checkCreateTriggers(final Statement statement, final data.Table<?>[] tables, final Action[][] actionSets) throws SQLException {
    if (logger.isTraceEnabled()) logm(logger, TRACE, "%?.checkCreateTriggers", "%?,%s,%s", this, statement, Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new), Arrays.deepToString(actionSets));

    for (int i = 0, $i = tables.length; i < $i; ++i) { // [A]
      final Action[] actionSet = actionSets[i];
      if (actionSet == null)
        continue;

      final data.Table<?> table = tables[i];
      for (final Action action : Action.values()) { // [A]
        if (!ArrayUtil.contains(actionSet, action)) {
          statement.addBatch("DROP TRIGGER IF EXISTS \"" + getFunctionName(table, action) + "\" ON \"" + table.getName() + "\"");
        }
      }
    }

    String[][] functionNameByActions = null;
    StringBuilder selectFunctions = null;
    StringBuilder selectTriggers = null;
    HashSet<String> functionNames = null;
    HashSet<String> triggerNames = null;
    for (int i = 0, $i = tables.length; i < $i; ++i) { // [A]
      final Action[] actionSet = actionSets[i];
      if (actionSet == null)
        continue;

      final data.Table<?> table = tables[i];
      for (int j = 0, $j = actionSet.length; j < $j; ++j) { // [A]
        final Action action = actionSet[j];
        if (action != null) {
          if (functionNameByActions == null) {
            functionNameByActions = new String[tables.length][];
            selectFunctions = new StringBuilder("SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name IN ");
            selectTriggers = new StringBuilder("SELECT trigger_name FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name IN ");
            functionNames = new HashSet<>(tables.length * 3);
            triggerNames = new HashSet<>(functionNames.size());
          }

          String[] functionNameByAction = functionNameByActions[i];
          if (functionNameByAction == null)
            functionNameByAction = functionNameByActions[i] = new String[actionSet.length];

          final String functionName = functionNameByAction[j] = getFunctionName(table, action);
          selectFunctions.append(" '").append(functionName).append("',");
          selectTriggers.append(" '").append(functionName).append("',");
          functionNames.add(functionName);
          triggerNames.add(functionName);
        }
      }
    }

    if (functionNameByActions == null)
      return;

    selectFunctions.setCharAt(136, '(');
    selectFunctions.setCharAt(selectFunctions.length() - 1, ')');

    selectTriggers.setCharAt(104, '(');
    selectTriggers.setCharAt(selectTriggers.length() - 1, ')');

    try (final ResultSet resultSet = statement.executeQuery(selectFunctions.toString())) {
      while (resultSet.next())
        functionNames.remove(resultSet.getString(1));
    }

    try (final ResultSet resultSet = statement.executeQuery(selectTriggers.toString())) {
      while (resultSet.next())
        triggerNames.remove(resultSet.getString(1));
    }

    if (functionNames.size() == 0 && triggerNames.size() == 0)
      return;

    for (int i = 0, $i = functionNameByActions.length; i < $i; ++i) { // [A]
      final String[] functionNameByAction = functionNameByActions[i];
      for (int j = 0, $j = functionNameByAction.length; j < $j; ++j) { // [A]
        final String functionName = functionNameByAction[j];
        if (functionNames.contains(functionName))
          statement.addBatch(getCreateFunction(tables[i], actionSets[i][j], functionName));

        if (triggerNames.contains(functionName))
          statement.addBatch(getCreateTrigger(tables[i], actionSets[i][j], functionName));
      }
    }
  }

  @Override
  void listenTriggers(final Statement statement) throws SQLException {
    if (logger.isTraceEnabled()) logm(logger, TRACE, "%?.listenTriggers", "%?", this, statement.getConnection());

    statement.addBatch("LISTEN \"" + channelName + "\"");
  }

  @Override
  protected void stop() throws SQLException {
    listener.closed();
  }
}