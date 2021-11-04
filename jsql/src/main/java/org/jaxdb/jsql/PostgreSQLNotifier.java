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

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;

public class PostgreSQLNotifier extends Notifier<PGNotificationListener> {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLNotifier.class);

  private static final String channelName = "jaxdb_notify";
  private static final String dropTriggersFunction = "drop_all_" + channelName;

  private static String getFunctionName(final data.Table<?> table, final boolean firstSecond) {
    return table.getName() + "_" + channelName + (firstSecond ? "_1" : "_2");
  }

  private static final String createDropTriggersFunction;

  static {
    final StringBuilder sql = new StringBuilder();
    sql.setLength(0);
    sql.append("CREATE OR REPLACE FUNCTION " + dropTriggersFunction + "() RETURNS text AS $$ DECLARE\n");
    sql.append("  triggerNameRecord RECORD;\n");
    sql.append("  triggerTableRecord RECORD;\n");
    sql.append("BEGIN\n");
    sql.append("  FOR triggerNameRecord IN SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE '%_" + channelName + "' LOOP\n");
    sql.append("    FOR triggerTableRecord IN SELECT DISTINCT(event_object_table) FROM information_schema.triggers WHERE trigger_name = triggerNameRecord.trigger_name LOOP\n");
    sql.append("      RAISE NOTICE 'DROP TRIGGER % ON %', triggerNameRecord.trigger_name, triggerTableRecord.event_object_table;\n");
    sql.append("      EXECUTE 'DROP TRIGGER ' || triggerNameRecord.trigger_name || ' ON ' || triggerTableRecord.event_object_table || ';';\n");
    sql.append("    END LOOP;\n");
    sql.append("  END LOOP;\n");
    sql.append("  RETURN 'done';\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql SECURITY DEFINER;");
    createDropTriggersFunction = sql.toString();
  }

  private PGNotificationListener listener;

  PostgreSQLNotifier(final Connection connection, final ConnectionFactory connectionFactory) {
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
    try (final Statement statement = connection.createStatement()) {
      pgConnection.removeNotificationListener(channelName);
      pgConnection.addNotificationListener(channelName, channelName, listener);
    }
    catch (final Exception e) {
      pgConnection.removeNotificationListener(channelName);
      throw e;
    }
  }

  @Override
  void dropTrigger(final Statement statement, final data.Table<?> table) throws SQLException {
    logm(logger, TRACE, "%?.dropTrigger", "%?,%s", this, statement.getConnection(), table.getName());
    statement.execute("DROP TRIGGER IF EXISTS \"" + getFunctionName(table, true) + "\" ON \"" + table.getName() + "\"");
    statement.execute("DROP TRIGGER IF EXISTS \"" + getFunctionName(table, false) + "\" ON \"" + table.getName() + "\"");
  }

  private static boolean isFunctionDefined(final Statement statement, final String functionName) throws SQLException {
    final String sql = "SELECT routine_name FROM information_schema.routines WHERE routine_type = 'FUNCTION' AND specific_schema = 'public' AND routine_name = '" + functionName + "';";
    try (final ResultSet resultSet = statement.executeQuery(sql)) {
      return resultSet.next() && functionName.equals(resultSet.getString(1));
    }
  }

  private static void checkCreateFunction(final Statement statement, final data.Table<?> table, final boolean isUpdate) throws SQLException {
    logm(logger, TRACE, "PostgreSQLNotifier.checkCreateFunction", "%?,%s,%s", statement.getConnection(), table, isUpdate);
    final String functionName = getFunctionName(table, isUpdate);
    if (isFunctionDefined(statement, functionName))
      return;

    final String tableName = table.getName();
    final boolean hasKeyForUpdate = table._keyForUpdate$.length > 0;

    final StringBuilder sql = new StringBuilder();

    sql.append("CREATE OR REPLACE FUNCTION ").append(functionName).append("() RETURNS TRIGGER AS $$ DECLARE\n");
    sql.append("  data JSON;\n");
    sql.append("BEGIN\n");
    sql.append("  IF (TG_OP = 'UPDATE') THEN\n");

    if (isUpdate) { // UPDATE
      sql.append("    data = row_to_json(NEW);\n");
      sql.append("    PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'UPDATE', 'data', data)::text);\n");
    }
    else { // UPGRADE
      sql.append("    SELECT json_object_agg(COALESCE(old_json.key, new_json.key), new_json.value) INTO data\n");
      sql.append("    FROM json_each_text(row_to_json(OLD)) old_json\n");
      sql.append("    FULL OUTER JOIN json_each_text(row_to_json(NEW)) new_json ON new_json.key = old_json.key\n");
      sql.append("    WHERE new_json.value IS DISTINCT FROM old_json.value");

      if (table._primary$.length == 0)
        throw new IllegalArgumentException("Cannot create UPGRADE trigger on table without primary keys");

      for (final data.Column<?> primary : table._primary$)
        sql.append(" OR new_json.key = '").append(primary.name).append("'");

      sql.append(";\n");

      sql.append("    PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'UPGRADE'");
      if (hasKeyForUpdate) {
        sql.append(", 'keyForUpdate', json_build_object(");
        for (final data.Column<?> keyForUpdate : table._keyForUpdate$)
          sql.append('\'').append(keyForUpdate.name).append("',OLD.\"").append(keyForUpdate.name).append("\",");

        sql.setCharAt(sql.length() - 1, ')');
      }

      sql.append(", 'data', data)::text);\n");
    }

    sql.append("  ELSIF (TG_OP = 'INSERT') THEN\n");
    sql.append("    data = row_to_json(NEW);\n");
    sql.append("    PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'INSERT', 'data', data)::text);\n");
    sql.append("  ELSIF (TG_OP = 'DELETE') THEN\n");
    sql.append("    data = row_to_json(OLD);\n");
    sql.append("    PERFORM pg_notify('").append(channelName).append("', json_build_object('table', '").append(tableName).append("', 'action', 'DELETE', 'data', data)::text);\n");
    sql.append("  END IF;\n");
    sql.append("  RETURN NULL;\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql;\n");

    statement.execute(sql.toString());
  }

  @Override
  void createTrigger(final Statement statement, final data.Table<?> table, final Action[] actionSet) throws SQLException {
    logm(logger, TRACE, "%?.createTrigger", "%?,%s,%s", this, statement.getConnection(), table, actionSet);

    final boolean isUpdate = actionSet[Action.UPDATE.ordinal()] == UPDATE;
    checkCreateFunction(statement, table, isUpdate);

    final String functionName = getFunctionName(table, isUpdate);
    final String insertAndDeleteTriggerName = getFunctionName(table, true);
    final String updateOrUpgradeTriggerName = getFunctionName(table, false);

    final StringBuilder sql = new StringBuilder();
    sql.append("CREATE TRIGGER \"").append(insertAndDeleteTriggerName).append("\" AFTER");
    final int len = sql.length();
    for (final Action action : actionSet) {
      if (action instanceof UP)
        statement.execute("CREATE TRIGGER \"" + updateOrUpgradeTriggerName + "\" AFTER UPDATE ON \"" + table.getName() + "\" FOR EACH ROW WHEN (OLD.* IS DISTINCT FROM NEW.*) EXECUTE PROCEDURE " + functionName + "()");
      else if (action != null)
        sql.append(' ').append(action).append(" OR");
    }

    if (sql.length() == len)
      return;

    sql.setCharAt(sql.length() - 1, 'N');
    sql.append(" \"").append(table.getName()).append("\" FOR EACH ROW EXECUTE PROCEDURE ").append(functionName).append("()");
    statement.execute(sql.toString());
  }

  @Override
  void listenTrigger(final Statement statement, final data.Table<?> table) throws SQLException {
    logm(logger, TRACE, "%?.listenTrigger", "%?,%s", this, statement.getConnection(), table.getName());
    statement.execute("LISTEN " + channelName);
  }

  @Override
  void unlistenTrigger(final Statement statement, final data.Table<?> table) throws SQLException {
    logm(logger, TRACE, "%?.unlistenTrigger", "%?,%s", this, statement.getConnection(), table.getName());
    statement.execute("UNLISTEN " + channelName);
  }

  @Override
  protected void stop() throws SQLException {
    listener.closed();
  }
}