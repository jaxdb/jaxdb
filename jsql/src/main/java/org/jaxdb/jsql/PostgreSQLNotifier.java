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
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

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
  private static final String dropTriggersFunction = channelName + "_drop_all";

  private static String getFunctionName(final data.Table<?> table, final Action action) {
    return channelName + "_" + table.getName() + "_" + action.toString().toLowerCase();
  }
  // list all LISTEN channels: SELECT * FROM pg_listening_channels()
  // list all triggers: SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE 'jaxdb_notify_%';
  // drop all triggers: SELECT "jaxdb_notify_drop_all"();
  // list all functions: SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name LIKE 'jaxdb_notify_%';
  // drop all functions: list the functions, then execute DROP FUNCTION %;
  private static final String createDropTriggersFunction;

  static {
    final StringBuilder sql = new StringBuilder();
    sql.setLength(0);
    sql.append("CREATE OR REPLACE FUNCTION " + dropTriggersFunction + "() RETURNS text AS $$ DECLARE\n");
    sql.append("  triggerNameRecord RECORD;\n");
    sql.append("  triggerTableRecord RECORD;\n");
    sql.append("BEGIN\n");
    sql.append("  FOR triggerNameRecord IN SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE '" + channelName + "_%' LOOP\n");
    sql.append("    FOR triggerTableRecord IN SELECT DISTINCT(event_object_table) FROM information_schema.triggers WHERE trigger_name = triggerNameRecord.trigger_name LOOP\n");
    sql.append("      RAISE NOTICE 'DROP TRIGGER \"%\" ON \"%\"', triggerNameRecord.trigger_name, triggerTableRecord.event_object_table;\n");
    sql.append("      EXECUTE 'DROP TRIGGER \"' || triggerNameRecord.trigger_name || '\" ON \"' || triggerTableRecord.event_object_table || '\";';\n");
    sql.append("    END LOOP;\n");
    sql.append("  END LOOP;\n");
    sql.append("  RETURN 'done';\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql SECURITY DEFINER;");
    createDropTriggersFunction = sql.toString();
  }

  private PGNotificationListener listener;

  PostgreSQLNotifier(final Connection connection, final ConnectionFactory connectionFactory) throws SQLException {
    super(DBVendor.POSTGRE_SQL, connection, connectionFactory);
  }

  @Override
  void start(final Connection connection) throws IOException, SQLException {
    logm(logger, TRACE, "%?.start", "%?", this, connection);
    if (isClosed())
      return;

    try (final Statement statement = connection.createStatement()) {
      statement.execute(createDropTriggersFunction);
    }

    synchronized (connection) {
      reconnect(connection, listener = new PGNotificationListener() {
        @Override
        public void notification(final int processId, final String channelName, final String payload) {
          int i = payload.indexOf("\"table\"");
          if (i < 0)
            throw new IllegalStateException();

          i = payload.indexOf('"', i + 7);
          if (i < 0)
            throw new IllegalStateException();

          final String tableName = payload.substring(i + 1, payload.indexOf('"', i + 2));
          try {
            PostgreSQLNotifier.this.notify(tableName, payload);
          }
          catch (final IOException e) {
            throw new UncheckedIOException(e);
          }
        }

        @Override
        public void closed() {
          logm(logger, TRACE, "%?.closed", this);
          try {
            connection.unwrap(PGConnection.class).removeNotificationListener(channelName);
          }
          catch (final SQLException e) {
            logger.warn("Failed to disconnect listener from PGConnection", e);
          }

          if (isClosed())
            return;

          try {
            reconnect(getConnection(), this);
          }
          catch (final IOException | SQLException e) {
            throw new IllegalStateException("Failed to reconnect PGConnection", e);
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

  private static boolean isFunctionDefined(final Connection connection, final String functionName) throws SQLException {
    logm(logger, TRACE, "PostgreSQLNotifier.isFunctionDefined", "%?,%s", connection, functionName);
    final String sql = "SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name = '" + functionName + "';";
    try (
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery(sql);
    ) {
      return resultSet.next() && functionName.equals(resultSet.getString(1));
    }
  }

  private static void checkCreateFunction(final Connection connection, final data.Table<?> table, final Action action) throws SQLException {
    logm(logger, TRACE, "PostgreSQLNotifier.checkCreateFunction", "%?,%s,%s", connection, table, action);
    final String functionName = getFunctionName(table, action);
    if (isFunctionDefined(connection, functionName))
      return;

    final String tableName = table.getName();
    final boolean hasKeyForUpdate = table._keyForUpdate$.length > 0;

    final StringBuilder sql = new StringBuilder();

    sql.append("CREATE OR REPLACE FUNCTION ").append(functionName).append("() RETURNS TRIGGER AS $$ DECLARE\n");
    sql.append("  data JSON;\n");
    sql.append("BEGIN\n");

    if (action == INSERT) {
      sql.append("  data = row_to_json(NEW);\n");
      sql.append("  PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'INSERT', 'data', data)::text);\n");
    }
    else if (action == UPDATE) {
      sql.append("  data = row_to_json(NEW);\n");
      sql.append("  PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'UPDATE', 'data', data)::text);\n");
    }
    else if (action == UPGRADE) {
      sql.append("  SELECT json_object_agg(COALESCE(old_json.key, new_json.key), new_json.value) INTO data\n");
      sql.append("  FROM json_each_text(row_to_json(OLD)) old_json\n");
      sql.append("  FULL OUTER JOIN json_each_text(row_to_json(NEW)) new_json ON new_json.key = old_json.key\n");
      sql.append("  WHERE new_json.value IS DISTINCT FROM old_json.value");

      if (table._primary$.length == 0)
        throw new IllegalArgumentException("Cannot create UPGRADE trigger on table without primary keys");

      for (final data.Column<?> primary : table._primary$)
        sql.append(" OR new_json.key = '").append(primary.name).append("'");

      sql.append(";\n");

      sql.append("  PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'UPGRADE'");
      if (hasKeyForUpdate) {
        sql.append(", 'keyForUpdate', json_build_object(");
        for (final data.Column<?> keyForUpdate : table._keyForUpdate$)
          sql.append('\'').append(keyForUpdate.name).append("',OLD.\"").append(keyForUpdate.name).append("\",");

        sql.setCharAt(sql.length() - 1, ')');
      }

      sql.append(", 'data', data)::text);\n");
    }
    else if (action == DELETE) {
      sql.append("  data = row_to_json(OLD);\n");
      sql.append("  PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'DELETE', 'data', data)::text);\n");
    }
    else {
      throw new UnsupportedOperationException("Unsupported Action: " + action);
    }

    sql.append("  RETURN NULL;\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql;\n");

    try (final Statement statement = connection.createStatement()) {
      statement.execute(sql.toString());
    }
  }

  private static boolean isTriggerDefined(final Connection connection, final String triggerName) throws SQLException {
    logm(logger, TRACE, "PostgreSQLNotifier.isTriggerDefined", "%?,%s", connection, triggerName);
    final String sql = "SELECT trigger_name FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name = '" + triggerName + "';";
    try (
      final Statement statement = connection.createStatement();
      final ResultSet resultSet = statement.executeQuery(sql);
    ) {
      return resultSet.next() && triggerName.equals(resultSet.getString(1));
    }
  }

  private static void checkCreateTrigger(final Connection connection, final data.Table<?> table, final Action action) throws SQLException {
    logm(logger, TRACE, "PostgreSQLNotifier.checkCreateTrigger", "%?,%s,%s", connection, table, action);
    final String triggerName = getFunctionName(table, action);
    if (isTriggerDefined(connection, triggerName))
      return;

    final StringBuilder sql = new StringBuilder();
    sql.append("CREATE TRIGGER \"").append(triggerName).append("\" AFTER ").append(action.toSql()).append(" ON \"").append(table.getName()).append("\" FOR EACH ROW ");
    if (action instanceof UP)
      sql.append("WHEN (OLD.* IS DISTINCT FROM NEW.*) ");

    sql.append("EXECUTE PROCEDURE ").append(triggerName).append("()");
    try (final Statement statement = connection.createStatement()) {
      statement.execute(sql.toString());
    }
  }

  private void dropTrigger(final Connection connection, final data.Table<?> table, final Action action) throws SQLException {
    logm(logger, TRACE, "%?.dropTrigger", "%?,%s", this, connection, table.getName());
    try (final Statement statement = connection.createStatement()) {
      statement.execute("DROP TRIGGER IF EXISTS \"" + getFunctionName(table, action) + "\" ON \"" + table.getName() + "\"");
    }
  }

  @Override
  void checkCreateTrigger(final Connection connection, final data.Table<?> table, final Action[] actionSet) throws SQLException {
    logm(logger, TRACE, "%?.checkCreateTrigger", "%?,%s,%s", this, connection, table, actionSet);

    for (final Action action : Action.values())
      if (!ArrayUtil.contains(actionSet, action))
        dropTrigger(connection, table, action);

    for (final Action action : actionSet) {
      if (action != null) {
        checkCreateFunction(connection, table, action);
        checkCreateTrigger(connection, table, action);
      }
    }
  }

  @Override
  void listenTriggers(final Statement statement, final data.Table<?>[] tables) throws SQLException {
    // NOTE: LISTEN is not table-specific
    if (logger.isTraceEnabled())
      logm(logger, TRACE, "%?.listenTriggers", "%?,%s", this, statement.getConnection(), Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new));

    statement.execute("LISTEN \"" + channelName + "\"");
  }

  @Override
  void listenTrigger(final Statement statement, final data.Table<?> table) throws SQLException {
    // NOTE: LISTEN is not table-specific
//  logm(logger, TRACE, "%?.listenTrigger", "%?,%s", this, statement.getConnection(), table.getName());
    statement.execute("LISTEN \"" + channelName + "\"");
  }

  @Override
  void unlistenTriggers(final Statement statement, final data.Table<?>[] tables) throws SQLException {
    // NOTE: LISTEN is not table-specific
    if (logger.isTraceEnabled())
      logm(logger, TRACE, "%?.unlistenTriggers", "%?,%s", this, statement.getConnection(), Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new));
    statement.execute("UNLISTEN \"" + channelName + "\"");
  }

  @Override
  void unlistenTrigger(final Statement statement, final data.Table<?> table) throws SQLException {
    // NOTE: LISTEN is not table-specific, so this is commented out, because other tables may have triggers
//    logm(logger, TRACE, "%?.unlistenTrigger", "%?,%s", this, statement.getConnection(), table.getName());
//    statement.execute("UNLISTEN \"" + channelName + "\"");
  }

  @Override
  protected void stop() throws SQLException {
    listener.closed();
  }
}