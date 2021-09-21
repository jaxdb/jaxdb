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

import static org.libj.logging.LoggerUtil.*;
import static org.slf4j.event.Level.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.jsql.Notification.Action;
import org.libj.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;

public class PostgreSQLNotifier extends Notifier {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLNotifier.class);

  private static final String functionName = "trigger_notify";
  private static final String channelNameFilter = ".*_" + functionName;
  private static final String dropTriggersFunction = "drop_all_" + functionName;

  private static String getTriggerName(final String tableName) {
    return tableName + "_" + functionName;
  }

  private static final String createTriggerFunction;
  private static final String createDropTriggersFunction;

  static {
    final StringBuilder sql = new StringBuilder();
    sql.append("CREATE OR REPLACE FUNCTION " + functionName + "() RETURNS TRIGGER AS $$ DECLARE\n");
    sql.append("  data JSON;\n");
    sql.append("  notification JSON;\n");
    sql.append("BEGIN\n");
    sql.append("  -- Convert the old or new row to JSON, based on the kind of action.\n");
    sql.append("  -- Action = DELETE? -&gt; OLD row\n");
    sql.append("  -- Action = INSERT or UPDATE? -&gt; NEW row\n");
    sql.append("  IF (TG_OP = 'DELETE') THEN\n");
    sql.append("    data = row_to_json(OLD);\n");
    sql.append("  ELSE\n");
    sql.append("    data = row_to_json(NEW);\n");
    sql.append("  END IF;\n");
    sql.append("  -- Contruct the notification as a JSON string.\n");
    sql.append("  notification = json_build_object('table', TG_TABLE_NAME, 'action', TG_OP, 'data', data);\n");
    sql.append("  -- Execute pg_notify(channel, notification)\n");
    sql.append("  PERFORM pg_notify('" + functionName + "', notification::text);\n");
    sql.append("   \n");
    sql.append("  -- Result is ignored since this is an AFTER trigger\n");
    sql.append("  RETURN NULL;\n");
    sql.append("END;\n");
    sql.append("$$ LANGUAGE plpgsql;");
    createTriggerFunction = sql.toString();

    sql.setLength(0);
    sql.append("CREATE OR REPLACE FUNCTION " + dropTriggersFunction + "() RETURNS text AS $$ DECLARE\n");
    sql.append("  triggerNameRecord RECORD;\n");
    sql.append("  triggerTableRecord RECORD;\n");
    sql.append("BEGIN\n");
    sql.append("  FOR triggerNameRecord IN SELECT DISTINCT(trigger_name) FROM information_schema.triggers WHERE trigger_schema = 'public' AND trigger_name LIKE '%_" + functionName + "' LOOP\n");
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

  private void reconnect(final Connection connection, final PGNotificationListener listener) throws SQLException {
    logm(logger, TRACE, "%?.reconnect", "%?,%?", this, connection, listener);
    final PGConnection pgConnection = connection.unwrap(PGConnection.class);
    try (final Statement statement = connection.createStatement()) {
      pgConnection.removeNotificationListener(functionName);
      pgConnection.addNotificationListener(functionName, null, listener); // FIXME:?!? channelNameFilter?

      statement.execute("UNLISTEN " + functionName);
      statement.execute("LISTEN " + functionName);
    }
    catch (final Exception e) {
      pgConnection.removeNotificationListener(functionName);
      throw e;
    }
  }

  private PGNotificationListener listener;

  PostgreSQLNotifier(final Connection connection, final ConnectionFactory connectionFactory) {
    super(connection, connectionFactory);
  }

  @Override
  void start(final Connection connection) throws IOException, SQLException {
    logm(logger, TRACE, "%?.start", "%?", this, connection);
    if (isClosed())
      return;

    try (final Statement statement = connection.createStatement()) {
      statement.execute(createTriggerFunction);
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
            connection.unwrap(PGConnection.class).removeNotificationListener(functionName);
          }
          catch (final SQLException e) {
            logger.warn("Failed to remove listener from PGConnection", e);
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
  void dropTrigger(final Statement statement, final String tableName) throws SQLException {
    logm(logger, TRACE, "%?.dropTrigger", "%?,%s", this, statement, tableName);
    statement.execute("DROP TRIGGER IF EXISTS \"" + getTriggerName(tableName) + "\" ON \"" + tableName + "\"");
  }

  @Override
  void createTrigger(final Statement statement, final String tableName, final Action[] actionSet) throws SQLException {
    logm(logger, TRACE, "%?.createTrigger", "%?,%s,%s", this, statement, tableName, actionSet);
    statement.execute("CREATE TRIGGER \"" + getTriggerName(tableName) + "\" AFTER " + ArrayUtil.toString(actionSet, " OR ") + " ON \"" + tableName + "\" FOR EACH ROW EXECUTE PROCEDURE " + functionName + "()");
  }

  @Override
  protected void stop() throws SQLException {
    listener.closed();
  }
}