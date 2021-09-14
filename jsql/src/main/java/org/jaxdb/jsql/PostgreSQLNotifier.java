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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.EnumSet;

import org.jaxdb.jsql.Notification.Action;
import org.libj.lang.Assertions;
import org.libj.util.CollectionUtil;

import com.impossibl.postgres.api.jdbc.PGConnection;
import com.impossibl.postgres.api.jdbc.PGNotificationListener;

public class PostgreSQLNotifier extends Notifier {
  private static final String functionName = "trigger_notify";
  private static final String channelNameFilter = ".*_" + functionName;
  private static final String dropTriggersFunction = "drop_all_" + functionName;

  private static final String createTriggerFunction;
  private static final String createDropTriggersFunction;

  private static String getTriggerName(final String tableName) {
    return tableName + "_" + functionName;
  }

  static {
    final StringBuilder sql = new StringBuilder();
    sql.append("CREATE OR REPLACE FUNCTION " + functionName + "() RETURNS TRIGGER AS $$ DECLARE\n");
    sql.append("  data JSON;\n");
    sql.append("  notification JSON;\n");
    sql.append("BEGIN\n");
    sql.append("  -- Convert the old or new row to JSON, based on the kind of change.\n");
    sql.append("  -- Change = DELETE? -&gt; OLD row\n");
    sql.append("  -- Change = INSERT or UPDATE? -&gt; NEW row\n");
    sql.append("  IF (TG_OP = 'DELETE') THEN\n");
    sql.append("    data = row_to_json(OLD);\n");
    sql.append("  ELSE\n");
    sql.append("    data = row_to_json(NEW);\n");
    sql.append("  END IF;\n");
    sql.append("  -- Contruct the notification as a JSON string.\n");
    sql.append("  notification = json_build_object('table', TG_TABLE_NAME, 'change', TG_OP, 'data', data);\n");
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

  private final ConnectionFactory connectionFactory;
  private final PGNotificationListener listener;

  PostgreSQLNotifier(final ConnectionFactory connectionFactory) {
    this.connectionFactory = Assertions.assertNotNull(connectionFactory);
    this.listener = new PGNotificationListener() {
      @Override
      public void notification(final int processId, final String channelName, final String payload) {
        final String tableName = channelName.substring(0, channelName.length() - functionName.length() - 1);
        PostgreSQLNotifier.this.notify(tableName, payload);
      }

      @Override
      public void closed() {
        try {
          reconnect();
        }
        catch (final IOException | SQLException e) {
          throw new IllegalStateException("Failed to reconnect PGConnection", e);
        }
      }
    };
  }

  @Override
  void start() throws IOException, SQLException {
    reconnect();
    try (final Statement statement = connection.createStatement()) {
      statement.execute(createTriggerFunction);
      statement.execute(createDropTriggersFunction);
    }
  }

  @Override
  public void removeNotificationListeners() throws IOException, SQLException {
    try (final Statement statement = getConnection().createStatement()) {
      statement.execute("UNLISTEN " + functionName);
      statement.execute("SELECT " + dropTriggersFunction + "()");
    }

    connection.close();
  }

  private void reconnect() throws IOException, SQLException {
    if (isClosed())
      return;

    try (final Statement statement = getConnection().createStatement()) {
      statement.execute("LISTEN " + functionName);
    }

    connection.unwrap(PGConnection.class).addNotificationListener(functionName, channelNameFilter, listener);
  }

  private Connection connection;

  @Override
  public Connection getConnection() throws IOException, SQLException {
    return connection == null || connection.isClosed() ? connection = connectionFactory.getConnection() : connection;
  }

  @Override
  void dropTrigger(final Statement statement, final String tableName) throws SQLException {
    statement.execute("DROP TRIGGER IF EXISTS \"" + getTriggerName(tableName) + "\" ON \"" + tableName + "\"");
  }

  @Override
  void createTrigger(final Statement statement, final String tableName, final EnumSet<Action> actionSet) throws SQLException {
    statement.execute("CREATE TRIGGER \"" + getTriggerName(tableName) + "\" AFTER " + CollectionUtil.toString(actionSet, " OR ") + " ON \"" + tableName + "\" FOR EACH ROW EXECUTE PROCEDURE " + functionName + "()");
  }

  @Override
  public void close() throws SQLException {
    super.close();
    if (connection != null)
      connection.close();
  }
}