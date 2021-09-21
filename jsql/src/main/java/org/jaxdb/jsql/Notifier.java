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

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.jaxdb.jsql.Notification.Action;
import org.libj.lang.Assertions;
import org.libj.lang.Throwables;
import org.libj.util.retry.RetryPolicy;
import org.openjax.json.JSON;
import org.openjax.json.JSON.Type;
import org.openjax.json.JSON.TypeMap;
import org.openjax.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.impossibl.postgres.api.jdbc.PGNotificationListener;

abstract class Notifier implements AutoCloseable, ConnectionFactory {
  private static final Logger logger = LoggerFactory.getLogger(Notifier.class);

  private static void clear(final Action[] actions) {
    for (int i = 0; i < actions.length; ++i)
      actions[i] = null;
  }

  private static int size(final Action[] actions) {
    int size = 0;
    for (final Action action : actions)
      if (action != null)
        ++size;

    return size;
  }

  private static boolean remove(final Action[] target, final Action ... actions) {
    boolean changed = false;
    for (final Action action : actions) {
      changed |= target[action.ordinal()] != null;
      target[action.ordinal()] = null;
    }

    return changed;
  }

  private static boolean add(final Action[] target, final Action action) {
    if (action == null)
      return false;

    final boolean changed = target[action.ordinal()] == null;
    target[action.ordinal()] = action;
    return changed;
  }

  private static boolean add(final Action[] target, final Action.INSERT insert, final Action.UPDATE update, final Action.DELETE delete) {
    boolean changed = false;
    changed |= add(target, insert);
    changed |= add(target, update);
    changed |= add(target, delete);
    return changed;
  }

  private static class TableNotifier<T extends data.Table<?>> implements Closeable {
    private static final TypeMap typeMap = new TypeMap()
      .put(Type.NUMBER, String::new)
      .put(Type.BOOLEAN, Boolean::toString);

    private Map<Notification.Listener<T>,Action[]> notificationListenerToActions = new IdentityHashMap<>();
    private Action[] allActions = new Action[3];
    private T table;

    private TableNotifier(final T table) {
      this.table = Assertions.assertNotNull(table);
    }

    private Action[] addNotificationListener(final Notification.Listener<T> notificationListener, final Action.INSERT insert, final Action.UPDATE update, final Action.DELETE delete) {
      logm(logger, TRACE, "%?.addNotificationListener", "Listener@%h,%s,%s,%s", this, notificationListener, insert, update, delete);
      add(allActions, insert, update, delete);

      Action[] actionSet = notificationListenerToActions.get(Assertions.assertNotNull(notificationListener));
      if (actionSet == null) {
        notificationListenerToActions.put(notificationListener, actionSet = new Action[3]);
        add(actionSet, insert, update, delete);
      }
      else if (!add(actionSet, insert, update, delete)) {
        return null;
      }

      return allActions;
    }

    private boolean removeActions(final Action ... actions) {
      if (!remove(allActions, actions))
        return false;

      if (size(allActions) == 0) {
        notificationListenerToActions.clear();
      }
      else {
        for (final Iterator<Map.Entry<Notification.Listener<T>,Action[]>> iterator = notificationListenerToActions.entrySet().iterator(); iterator.hasNext();) {
          final Map.Entry<Notification.Listener<T>,Action[]> entry = iterator.next();
          final Action[] actionSet = entry.getValue();
          remove(actionSet, actions);
          if (size(actionSet) == 0)
            iterator.remove();
        }
      }

      return true;
    }

    @SuppressWarnings("unchecked")
    void notify(final String payload) throws JsonParseException, IOException {
      logm(logger, TRACE, "%?.notify", this, payload);
      final Map<String,Object> json = (Map<String,Object>)JSON.parse(payload, typeMap);
      final Action action = Action.valueOf(((String)json.get("action")).toUpperCase());

      T row = null;
      for (final Map.Entry<Notification.Listener<T>,Action[]> entry : notificationListenerToActions.entrySet()) {
        if (entry.getValue()[action.ordinal()] != null) {
          if (row == null) {
            row = (T)table.clone();
            row.setColumns((Map<String,String>)json.get("data"));
          }

          entry.getKey().notification(action, row);
        }
      }
    }

    private boolean isEmpty() {
      return size(allActions) == 0;
    }

    @Override
    public void close() {
      notificationListenerToActions.clear();
      clear(allActions);

      notificationListenerToActions = null;
      allActions = null;
      table = null;
    }
  }

  private Connection connection;
  protected final ConnectionFactory connectionFactory;

  Notifier(final Connection connection, final ConnectionFactory connectionFactory) {
    logm(logger, TRACE, "%?.<init>", "%?,%?", this, connection, connectionFactory);
    this.connection = Assertions.assertNotNull(connection);
    this.connectionFactory = Assertions.assertNotNull(connectionFactory);
  }

  @Override
  public final Connection getConnection() throws IOException, SQLException {
    logm(logger, TRACE, "%?.getConnection", this);
    return connection.isClosed() ? connection = connectionFactory.getConnection() : connection;
  }

  final void notify(final String tableName, final String payload) throws JsonParseException, IOException {
    logm(logger, TRACE, "%?.notify", "%s,%s", this, tableName, payload);
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(tableName);
    if (tableNotifier != null)
      tableNotifier.notify(payload);
  }

  private enum State {
    CREATED,
    STARTED,
    STOPPED
  }

  private final AtomicReference<State> state = new AtomicReference<>(State.CREATED);
  private final Map<String,TableNotifier<?>> tableNameToNotifier = new HashMap<String,TableNotifier<?>>() {
    private static final long serialVersionUID = 4936394412495528225L;

    @Override
    public void clear() {
      for (final TableNotifier<?> notifier : values())
        notifier.close();

      super.clear();
    }
  };

  private void recreateTrigger(final Statement statement, final String tableName, final Action[] actionSet) throws SQLException {
    logm(logger, TRACE, "%?.recreateTrigger", "%?,%s,%s", this, statement, tableName, actionSet);
    dropTrigger(statement, tableName);
    if (size(actionSet) > 0)
      createTrigger(statement, tableName, actionSet);
  }

  abstract void dropTrigger(Statement statement, String tableName) throws SQLException;
  abstract void createTrigger(Statement statement, String tableName, Action[] actionSet) throws SQLException;
  abstract void start(Connection connection) throws IOException, SQLException;
  abstract void tryReconnect(Connection connection, PGNotificationListener listener) throws SQLException;

  private static final RetryPolicy<SQLException> retryPolicy = new RetryPolicy
    .Builder(10)
    .withMaxRetries(100)
    .withBackoffFactor(1.5)
    .build(
      (final Exception e)
        -> e instanceof SQLException,
      (final List<Exception> exceptions, final int attemptNo, final long delayMs)
        -> Throwables.addSuppressed(new SQLException("Retry failure on attempt " + attemptNo + " with final delay of " + delayMs + "ms", exceptions.get(exceptions.size() - 1)), exceptions, exceptions.size() - 2, -1)
    );

  void reconnect(final Connection connection, final PGNotificationListener listener) throws SQLException {
    logm(logger, TRACE, "%?.reconnect", "%?,%?", this, connection, listener);
    retryPolicy.run(() -> tryReconnect(connection, listener));
  }

  protected abstract void stop() throws SQLException;

  public <T extends data.Table<?>>boolean removeNotificationListeners(final Action ... actions) throws IOException, SQLException {
    Assertions.assertNotEmpty(actions);
    boolean changed = false;
    for (final String tableName : tableNameToNotifier.keySet())
      changed |= removeNotificationListeners(tableName, actions);

    return changed;
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final T table, final Action ... actions) throws IOException, SQLException {
    return removeNotificationListeners(Assertions.assertNotNull(table).getName(), Assertions.assertNotEmpty(actions));
  }

  @SuppressWarnings("unchecked")
  private <T extends data.Table<?>>boolean removeNotificationListeners(final String tableName, final Action ... actions) throws IOException, SQLException {
    TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
    if (tableNotifier == null || !tableNotifier.removeActions(actions))
      return false;

    if (tableNotifier.isEmpty())
      tableNameToNotifier.remove(tableName);

    try (
      final Connection connection = connectionFactory.getConnection();
      final Statement statement = connection.createStatement();
    ) {
      recreateTrigger(statement, tableName, tableNotifier.allActions);
    }

    if (tableNameToNotifier.isEmpty()) {
      state.set(State.STOPPED);
      stop();
      if (connection != null)
        connection.close();
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  public final <T extends data.Table<?>>boolean addNotificationListener(final Action.INSERT insert, final Action.UPDATE update, final Action.DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    Assertions.assertNotEmpty(tables);
    Assertions.assertNotNull(notificationListener);
    if (insert == null && update == null && delete == null)
      return false;

    if (logger.isTraceEnabled())
      logm(logger, TRACE, "%?.addNotificationListener", "%s,%s,%s,Listener@%h,%s", this, insert, update, delete, notificationListener, Arrays.stream(tables).map(t -> t.getName()).toArray(String[]::new));

    for (final T table : tables) {
      Assertions.assertNotNull(table);
      final String tableName = table.getName();
      TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
      if (tableNotifier == null)
        tableNameToNotifier.put(tableName, tableNotifier = new TableNotifier<>(table));

      final Action[] actionSet = tableNotifier.addNotificationListener(notificationListener, insert, update, delete);
      if (actionSet == null)
        return false;

      final Connection connection = connectionFactory.getConnection();

      if (state.get() != State.STARTED) {
        synchronized (state) {
          if (state.get() != State.STARTED) {
            state.set(State.STARTED);
            start(connection);
          }
        }
      }

      try (final Statement statement = connection.createStatement()) {
        recreateTrigger(statement, tableName, actionSet);
      }
    }

    return true;
  }

  public final boolean isClosed() {
    return state.get() == State.STOPPED;
  }

  @Override
  public final void close() {
    tableNameToNotifier.clear();
    if (connection != null) {
      try {
        connection.close();
      }
      catch (final SQLException e) {
        logger.warn(e.getMessage(), e);
      }
    }
  }
}