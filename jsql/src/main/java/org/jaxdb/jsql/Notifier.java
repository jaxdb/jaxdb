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

import static org.libj.lang.Assertions.*;
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
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.jsql.data.Table;
import org.jaxdb.vendor.DBVendor;
import org.libj.lang.ObjectUtil;
import org.libj.util.function.Throwing;
import org.openjax.json.JSON;
import org.openjax.json.JSON.Type;
import org.openjax.json.JSON.TypeMap;
import org.openjax.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Notifier<L> extends Notifiable implements AutoCloseable, ConnectionFactory {
  private static final Logger logger = LoggerFactory.getLogger(Notifier.class);

  private static String OLD = "old";
  private static String CUR = "cur";

  private enum State {
    CREATED,
    STARTED,
    STOPPED,
    FAILED
  }

  private static void clear(final Action[] actions) {
    for (int i = 0, i$ = actions.length; i < i$; ++i) // [A]
      actions[i] = null;
  }

  private static int size(final Action[] actions) {
    int size = 0;
    for (final Action action : actions) // [A]
      if (action != null)
        ++size;

    return size;
  }

  private static boolean contains(final Action[] target, final Action action) {
    return action != null && target[action.ordinal()] != null;
  }

  private static boolean remove(final Action[] target, final Action action) {
    if (action == null || target[action.ordinal()] == null)
      return false;

    target[action.ordinal()] = null;
    return true;
  }

  private static boolean add(final Action[] target, final Action action) {
    if (action == null)
      return false;

    final boolean changed = target[action.ordinal()] == null;
    target[action.ordinal()] = action;
    return changed;
  }

  private static boolean add(final Action[] target, final INSERT insert, final UP up, final DELETE delete) {
    final Action current = target[Action.UPDATE.ordinal()];
    if (current != null && up != null && current != up)
      throw new IllegalArgumentException("UpdateListener and UpgradeListener cannot be used simultaneously for notifications on a particular table");

    boolean changed = false;
    changed |= add(target, insert);
    changed |= add(target, up);
    changed |= add(target, delete);
    return changed;
  }

  private static final TypeMap typeMap = new TypeMap()
    .put(Type.NUMBER, String::new)
    .put(Type.BOOLEAN, Boolean::toString);

  private final Thread thread;

  @SuppressWarnings("rawtypes")
  private class TableNotifier<T extends data.Table<?>> implements Closeable {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    private final Map<Notification.Listener,Action[]> notificationListenerToActions = new IdentityHashMap<>();
    private final Action[] allActions = new Action[3];

    private final T table;
    private final Queue<Notification<T>> queue;

    private TableNotifier(final T table, final Queue<Notification<T>> queue) {
      this.table = assertNotNull(table);
      this.queue = assertNotNull(queue);
    }

    private boolean isClosed() {
      if (!isClosed.get())
        return false;

      if (logger.isDebugEnabled())
        logger.debug(getClass().getSimpleName() + "-" + table.getName() + " is closed");

      return true;
    }

    private Action[] addNotificationListener(final Notification.Listener<? super T> notificationListener, final INSERT insert, final UP up, final DELETE delete) {
      logm(logger, TRACE, "%?[%s].addNotificationListener", "Listener@%h,%s,%s,%s", this, table.getName(), notificationListener, insert, up, delete);
      if (isClosed())
        return null;

      add(allActions, insert, up, delete);

      Action[] actionSet = notificationListenerToActions.get(assertNotNull(notificationListener));
      if (actionSet == null) {
        notificationListenerToActions.put(notificationListener, actionSet = new Action[3]);
        add(actionSet, insert, up, delete);
      }
      else if (!add(actionSet, insert, up, delete)) {
        return null;
      }

      return allActions;
    }

    private boolean removeActions(final INSERT insert, final UP up, final DELETE delete) {
      if (isClosed())
        return false;

      if (!remove(allActions, insert) && !remove(allActions, up) && !remove(allActions, delete))
        return false;

      if (size(allActions) == 0) {
        notificationListenerToActions.clear();
      }
      else {
        for (final Iterator<Map.Entry<Notification.Listener,Action[]>> iterator = notificationListenerToActions.entrySet().iterator(); iterator.hasNext();) { // [I]
          final Map.Entry<Notification.Listener,Action[]> entry = iterator.next();
          final Action[] actions = entry.getValue();
          remove(actions, insert);
          remove(actions, up);
          remove(actions, delete);
          if (size(actions) == 0)
            iterator.remove();
        }
      }

      return true;
    }

    void onConnect(final Connection connection) throws IOException, SQLException {
      if (isClosed.get())
        return;

      for (final Notification.Listener<T> listener : notificationListenerToActions.keySet()) // [S]
        listener.onConnect(connection, table);
    }

    void onFailure(final String sessionId, final T table, final Throwable t) {
      for (final Notification.Listener<T> listener : notificationListenerToActions.keySet()) // [S]
        listener.onFailure(sessionId, table, t);
    }

    @SuppressWarnings("unchecked")
    private T toRow(final T table, final Map<String,Object> json, final String oldNew) {
      final T row = (T)table.clone();
      final List<String> notFound = row.setColumns(Notifier.this.vendor, (Map<String,String>)json.get(oldNew), SetBy.SYSTEM);
      if (notFound != null) {
        if (logger.isErrorEnabled())
          logger.error("Not found columns in \"" + table.getName() + "\": \"" + notFound.stream().collect(Collectors.joining("\", \"")) + "\" of " + JSON.toString(json.get("data")));
      }

      return row;
    }

    @SuppressWarnings({"null", "unchecked"})
    void notify(final String sessionId, final Map<String,Object> json) throws JsonParseException {
      logm(logger, TRACE, "%?.notify", this, sessionId, json);
      if (isClosed.get())
        return;

      final Action action = Action.valueOf(((String)json.get("action")).toUpperCase());

      boolean inited = false;
      T old = null;
      T cur = null;
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) { // [S]
        if (entry.getValue()[action.ordinal()] == null)
          continue;

        try {
          if (!inited) {
            inited = true;
            if (action == Action.INSERT) {
              cur = toRow(table, json, CUR);
            }
            else if (action == Action.DELETE) {
              old = toRow(table, json, OLD);
            }
            else {
              old = toRow(table, json, OLD);
              cur = toRow(table, json, CUR);
            }
          }
        }
        catch (final Exception e) {
          if (logger.isErrorEnabled())
            logger.error("Unable to parse columns in \"" + table.getName() + "\": " + JSON.toString(json), e);

          continue;
        }

        if (action == Action.INSERT) {
          queue.add(new Notification<>(sessionId, entry.getKey(), action, null, cur));
        }
        else if (action == Action.DELETE) {
          queue.add(new Notification<>(sessionId, entry.getKey(), action, null, old));
        }
        else {
          ((data.Table)old).merge(cur);
          queue.add(new Notification<>(sessionId, entry.getKey(), action, (Map<String,String>)json.get("keyForUpdate"), old));
        }

        synchronized (thread) {
          thread.notify();
        }
      }
    }

    private boolean isEmpty() {
      return size(allActions) == 0;
    }

    @Override
    public void close() {
      if (isClosed.get())
        return;

      isClosed.set(true);
      notificationListenerToActions.clear();
      clear(allActions);
    }
  }

  private final DBVendor vendor;
  private Connection connection;
  protected final ConnectionFactory connectionFactory;

  Notifier(final DBVendor vendor, final Connection connection, final ConnectionFactory connectionFactory) throws SQLException {
    logm(logger, TRACE, "%?.<init>", "%?,%?", this, connection, connectionFactory);
    this.vendor = assertNotNull(vendor);
    this.connection = assertNotNull(connection);
    this.connectionFactory = assertNotNull(connectionFactory);
    connection.setAutoCommit(true);

    this.thread = new Thread("JAXDB-Notify") {
      private void flushQueues() {
        if (logger.isTraceEnabled())
          logger.trace("JAXDB-Notify Thread.flushQueues()");

        for (final TableNotifier<?> tableNotifier : tableNameToNotifier.values()) // [C]
          while (tableNotifier.queue.size() > 0)
            tableNotifier.queue.poll().invoke();
      }

      @Override
      public void run() {
        if (logger.isTraceEnabled())
          logger.trace("JAXDB-Notify Thread.run()");

        while (true) {
          while (state.get() != Notifier.State.STARTED) {
            synchronized (state) {
              try {
                if (state.get() != Notifier.State.STARTED)
                  state.wait();
              }
              catch (final InterruptedException e) {
                if (logger.isErrorEnabled())
                  logger.error("JAXDB-Notify state wait interrupted, continuing", e);
              }
            }
          }

          try {
            flushQueues();
            synchronized (thread) {
              flushQueues();
              thread.wait();
            }
          }
          catch (final Throwable t) {
            if (logger.isErrorEnabled())
              logger.error("Uncaught exception in Notifier.flushQueues()", t);

            if (!(t instanceof Exception))
              Throwing.rethrow(t);

            setState(Notifier.State.FAILED);
//            tableNotifier.onFailure(sessionId, tableNotifier.table, t);
          }
        }
      }
    };

    thread.start();
  }

  @Override
  public final Connection getConnection() throws IOException, SQLException {
    logm(logger, TRACE, "%?.getConnection", this);
    if (!connection.isClosed())
      return connection;

    connection = connectionFactory.getConnection();
    connection.setAutoCommit(true);

    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".getConnection(): New connection: " + ObjectUtil.simpleIdentityString(connection));

    return connection;
  }

  final void notify(final String tableName, final String payload) {
    final State state = this.state.get();
    logm(logger, TRACE, "%?.notify", "state=%b,%s,%s", this, state, tableName, payload);
    if (state != State.STARTED)
      return;

    final TableNotifier tableNotifier = tableNameToNotifier.get(tableName);
    if (tableNotifier == null)
      return;

    final Map<String,Object> json = (Map<String,Object>)JSON.parse(payload, typeMap);
    final String sessionId = (String)json.get("sessionId");

    try {
      tableNotifier.notify(sessionId, json);
    }
    catch (final Throwable t) {
      if (logger.isErrorEnabled())
        logger.error("Uncaught exception in Notifier.notify()", t);

      setState(State.FAILED);
      tableNotifier.onFailure(sessionId, tableNotifier.table, t);
      if (!(t instanceof Exception))
        Throwing.rethrow(t);
    }
  }

  private final AtomicReference<State> state = new AtomicReference<>(State.CREATED);

  private void setState(final State state) {
    logm(logger, TRACE, "%?.setState", "%s", this, state);

    this.state.set(state);

    synchronized (this.state) {
      this.state.notifyAll();
    }
  }

  private final Map<String,TableNotifier<?>> tableNameToNotifier = new HashMap<String,TableNotifier<?>>() {
    @Override
    public void clear() {
      for (final TableNotifier<?> notifier : values()) // [C]
        notifier.close();

      super.clear();
    }
  };

  private void recreateTrigger(final Connection connection, final data.Table<?> table, final Action[] actionSet) throws SQLException {
    logm(logger, TRACE, "%?.recreateTrigger", "%?,%s,%s", this, connection, table.getName(), actionSet);
    if (size(actionSet) > 0) {
      checkCreateTrigger(connection, table, actionSet);
      try (final Statement statement = connection.createStatement()) {
        listenTrigger(statement, table);
      }
    }
    else {
      try (final Statement statement = connection.createStatement()) {
        unlistenTrigger(statement, table);
      }
    }
  }

  abstract void checkCreateTrigger(Connection connection, data.Table<?> table, Action[] actionSet) throws SQLException;
  abstract void unlistenTriggers(Statement statement, data.Table<?>[] tables) throws SQLException;
  abstract void unlistenTrigger(Statement statement, data.Table<?> table) throws SQLException;
  abstract void listenTriggers(Statement statement, data.Table<?>[] tables) throws SQLException;
  abstract void listenTrigger(Statement statement, data.Table<?> table) throws SQLException;
  abstract void start(Connection connection) throws IOException, SQLException;
  abstract void tryReconnect(Connection connection, L listener) throws SQLException;

  private data.Table<?>[] getNotifierTables() {
    final data.Table<?>[] tables = new data.Table<?>[tableNameToNotifier.values().size()];
    final Iterator<TableNotifier<?>> iterator = tableNameToNotifier.values().iterator();
    for (int i = 0, i$ = tables.length; i < i$; ++i) // [A]
      tables[i] = iterator.next().table;

    return tables;
  }

  final void reconnect(final Connection connection, final L listener) throws IOException, SQLException {
    logm(logger, TRACE, "%?.reconnect", "%?,%?", this, connection, listener);
    final State state = this.state.get();
    if (state != State.CREATED && state != State.STARTED) {
      if (logger.isWarnEnabled())
        logger.warn("Trying to reconnect when Notifier.state == " + state);

      return;
    }

    try {
      tryReconnect(connection, listener);
      try (final Statement statement = connection.createStatement()) {
        listenTriggers(statement, getNotifierTables());
      }

      for (final TableNotifier<?> tableNotifier : tableNameToNotifier.values()) // [C]
        tableNotifier.onConnect(connection);
    }
    catch (final Exception e) {
      setState(Notifier.State.FAILED);
      throw e;
    }
  }

  protected abstract void stop() throws SQLException;

  <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete) throws IOException, SQLException {
    boolean changed = false;
    for (final TableNotifier<?> tableNotifier : tableNameToNotifier.values()) // [C]
      changed |= removeNotificationListeners(insert, up, delete, tableNotifier.table);

    return changed;
  }

  @SuppressWarnings("unchecked")
  <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete, final T table) throws IOException, SQLException {
    final String tableName = table.getName();
    TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
    if (tableNotifier == null || !tableNotifier.removeActions(insert, up, delete))
      return false;

    if (tableNotifier.isEmpty())
      tableNameToNotifier.remove(tableName);

    try (final Connection connection = connectionFactory.getConnection()) {
      recreateTrigger(connection, table, tableNotifier.allActions);
    }

    if (tableNameToNotifier.isEmpty()) {
      setState(State.STOPPED);
      stop();
      if (connection != null)
        connection.close();
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  final <T extends data.Table<?>>boolean hasNotificationListener(final INSERT insert, final UP up, final DELETE delete, final T table) {
    assertNotNull(table);
    if (insert == null && up == null && delete == null)
      throw new IllegalArgumentException("insert == null && up == null && delete == null");

    final TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(table.getName());
    if (tableNotifier == null)
      return false;

    final Action[] actions = tableNotifier.allActions;
    if (insert != null && !contains(actions, insert))
      return false;

    if (up != null && !contains(actions, up))
      return false;

    if (delete != null && !contains(actions, delete))
      return false;

    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private <T extends data.Table<?>>void addListenerForTables(final Connection connection, final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<? super T> notificationListener, final Queue<Notification<? super T>> queue, final T[] tables) throws SQLException {
    // FIXME: Share a single Statement
    for (final T table : tables) { // [A]
      final String tableName = table.getName();
      TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
      if (tableNotifier == null)
        tableNameToNotifier.put(tableName, tableNotifier = new TableNotifier(table.singleton(), queue));

      // actionSet must not be null here, because we're adding notification listeners for tables
      final Action[] actionSet = tableNotifier.addNotificationListener(notificationListener, insert, up, delete);
      if (actionSet != null)
        recreateTrigger(connection, table, actionSet);
    }
  }

  private <T extends data.Table<?>>void listenTriggers(final Connection connection, final T[] tables) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      listenTriggers(statement, tables);
    }
  }

  private static <T extends data.Table<?>>void invokeOnConnect(final Notification.Listener<T> notificationListener, final Connection connection, final T[] tables) throws IOException, SQLException {
    for (final T table : tables) // [A]
      notificationListener.onConnect(connection, table);
  }

  @SuppressWarnings("unchecked")
  final <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<? super T> notificationListener, final Queue<Notification<? super T>> queue, final T ... tables) throws IOException, SQLException {
    assertNotNull(notificationListener);
    assertNotEmpty(tables);
    if (insert == null && up == null && delete == null)
      return false;

    if (logger.isTraceEnabled())
      logm(logger, TRACE, "%?.addNotificationListener", "%s,%s,%s,Listener@%h,%s", this, insert, up, delete, notificationListener, Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new));

    if (state.get() == State.FAILED) {
      if (logger.isWarnEnabled())
        logger.warn("Trying to addNotificationListener(...) when Notifier.state == " + state);

      return false;
    }

    if (state.get() == State.STOPPED)
      setState(State.CREATED);

    if (state.get() != State.STARTED) {
      synchronized (state) {
        if (state.get() != State.STARTED) {
          // This will be the Connection for PG-JDBC I/O
          final Connection connection = getConnection();

          addListenerForTables(connection, insert, up, delete, notificationListener, queue, tables);

          // Start the Notifier, which also calls the onConnect(data.Table) callback
          connection.setAutoCommit(true);
          start(connection);

          setState(State.STARTED);

          // Activate triggers for the tables
          listenTriggers(connection, tables);

          return true;
        }
      }
    }

    // Create a connection that will close, because Notifier is already running on another connection.
    try (final Connection connection = connectionFactory.getConnection()) {
      addListenerForTables(connection, insert, up, delete, notificationListener, queue, tables);

      // If `this.connection != connection` it means the onConnect(data.Table) call
      // was not made in reconnect(), so invoke it directly for each new table.
      invokeOnConnect(notificationListener, connection, tables);

      // Activate triggers for the tables
      listenTriggers(connection, tables);
    }

    return true;
  }

  final boolean isErrored() {
    return state.get() == State.FAILED;
  }

  final boolean isClosed() {
    return state.get() == State.STOPPED;
  }

  @Override
  void onConnect(final Connection connection, final data.Table<?> table) throws IOException, SQLException {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getTable().getName());
    if (tableNotifier == null)
      return;

    for (final Map.Entry<Notification.Listener,Action[]> entry : tableNotifier.notificationListenerToActions.entrySet()) // [S]
      if (entry.getValue()[Action.INSERT.ordinal()] != null)
        entry.getKey().onConnect(connection, table);
  }

  @Override
  void onFailure(final String sessionId, final Table<?> table, final Throwable t) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getTable().getName());
    if (tableNotifier == null)
      return;

    for (final Map.Entry<Notification.Listener,Action[]> entry : tableNotifier.notificationListenerToActions.entrySet()) // [S]
      if (entry.getValue()[Action.INSERT.ordinal()] != null)
        entry.getKey().onFailure(sessionId, table, t);
  }

  @Override
  void onInsert(final String sessionId, final data.Table<?> row) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getTable().getName());
    if (tableNotifier == null)
      return;

    for (final Map.Entry<Notification.Listener,Action[]> entry : tableNotifier.notificationListenerToActions.entrySet()) // [S]
      if (entry.getValue()[Action.INSERT.ordinal()] != null)
        Notification.invoke(sessionId, entry.getKey(), Action.INSERT, null, row);
  }

  @Override
  void onUpdate(final String sessionId, final data.Table<?> row, final Map<String,String> keyForUpdate) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getTable().getName());
    if (tableNotifier == null)
      return;

    for (final Map.Entry<Notification.Listener,Action[]> entry : tableNotifier.notificationListenerToActions.entrySet()) // [S]
      if (entry.getValue()[Action.UPDATE.ordinal()] != null)
        Notification.invoke(sessionId, entry.getKey(), Action.UPDATE, null, row);
      else if (entry.getValue()[Action.UPGRADE.ordinal()] != null)
        Notification.invoke(sessionId, entry.getKey(), Action.UPGRADE, keyForUpdate, row);
  }

  @Override
  void onDelete(final String sessionId, final data.Table<?> row) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getTable().getName());
    if (tableNotifier == null)
      return;

    for (final Map.Entry<Notification.Listener,Action[]> entry : tableNotifier.notificationListenerToActions.entrySet()) // [S]
      if (entry.getValue()[Action.DELETE.ordinal()] != null)
        Notification.invoke(sessionId, entry.getKey(), Action.DELETE, null, row);
  }

  @Override
  public final void close() {
    tableNameToNotifier.clear();
    if (connection == null)
      return;

    try (final Statement statement = connection.createStatement()) {
      unlistenTriggers(statement, getNotifierTables());
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled())
        logger.warn(e.getMessage(), e);
    }

    try {
      connection.close();
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled())
        logger.warn(e.getMessage(), e);
    }
  }
}