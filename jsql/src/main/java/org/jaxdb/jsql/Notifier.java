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
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.Numbers;
import org.libj.lang.ObjectUtil;
import org.libj.util.ArrayUtil;
import org.libj.util.Interval;
import org.libj.util.function.Throwing;
import org.openjax.json.JSON;
import org.openjax.json.JSON.Type;
import org.openjax.json.JSON.TypeMap;
import org.openjax.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Notifier<L> extends Notifiable implements AutoCloseable, ConnectionFactory {
  private static final Logger logger = LoggerFactory.getLogger(Notifier.class);

  private static final String OLD = "old";
  private static final String CUR = "cur";

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
  private class TableNotifier<T extends data.Table> implements Closeable {
    private final AtomicBoolean isClosed = new AtomicBoolean(false);
    // FIXME: Review this code, because use of IdentityHashMap usually means an OO approach can be used instead.
    // FIXME: In this case, IdentityHashMap is used because Notification.Listener is an interface.
    // FIXME: Maybe convert Notification.Listener to a class, and have it manage its own actions?
    private final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = new IdentityHashMap<>();
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

      if (logger.isDebugEnabled()) logger.debug(table.getName() + " is closed");
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

      if (notificationListenerToActions.size() > 0) {
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
      }

      return true;
    }

    void onConnect(final Connection connection) throws IOException, SQLException {
      if (isClosed.get())
        return;

      if (notificationListenerToActions.size() > 0)
        for (final Notification.Listener<T> listener : notificationListenerToActions.keySet()) // [S]
          listener.onConnect(connection, table);
    }

    void onFailure(final String sessionId, final long timestamp, final Exception e) {
      if (notificationListenerToActions.size() > 0)
        for (final Notification.Listener<T> listener : notificationListenerToActions.keySet()) // [S]
          listener.onFailure(sessionId, timestamp, table, e);
    }

    @SuppressWarnings("unchecked")
    private T toRow(final T table, final Map<String,Object> json, final String oldNew) {
      final T row = (T)table.clone();
      final String[] notFound = row.setColumns(Notifier.this.vendor, (Map<String,String>)json.get(oldNew), data.Column.SetBy.SYSTEM);
      if (notFound != null && logger.isErrorEnabled()) logger.error("Not found columns in \"" + table.getName() + "\": [\"" + ArrayUtil.toString(notFound, "\", \"") + "\"] of " + JSON.toString(json));
      return row;
    }

    @SuppressWarnings({"null", "unchecked"})
    void notify(final String sessionId, final long timestamp, final Map<String,Object> json) throws JsonParseException {
      logm(logger, TRACE, "%?.notify", "%?,%d,%s", this, sessionId, timestamp, json);
      if (isClosed.get())
        return;

      final Action action = Action.valueOf(((String)json.get("action")).toUpperCase());

      boolean inited = false;
      T old = null;
      T cur = null;
      if (notificationListenerToActions.size() > 0) {
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
            if (logger.isErrorEnabled()) logger.error("Unable to parse columns in \"" + table.getName() + "\": " + JSON.toString(json), e);
            continue;
          }

          if (action == Action.INSERT) {
            queue.add(new Notification<>(sessionId, timestamp, entry.getKey(), action, null, cur));
          }
          else if (action == Action.DELETE) {
            queue.add(new Notification<>(sessionId, timestamp, entry.getKey(), action, null, old));
          }
          else {
            ((data.Table)old).merge(cur);
            queue.add(new Notification<>(sessionId, timestamp, entry.getKey(), action, (Map<String,String>)json.get("keyForUpdate"), old));
          }

          synchronized (thread) {
            thread.notify();
          }
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

  private final DbVendor vendor;
  private Connection connection;
  protected final ConnectionFactory connectionFactory;

  Notifier(final DbVendor vendor, final Connection connection, final ConnectionFactory connectionFactory) throws SQLException {
    logm(logger, TRACE, "%?.<init>", "%?,%?", this, connection, connectionFactory);
    this.vendor = assertNotNull(vendor);
    this.connection = assertNotNull(connection);
    this.connectionFactory = assertNotNull(connectionFactory);
    connection.setAutoCommit(true);

    this.thread = new Thread("JAXDB-Notify") {
      private void flushQueues() {
        if (logger.isTraceEnabled()) logger.trace("JAXDB-Notify Thread.flushQueues()");

        final Collection<TableNotifier<?>> tableNotifiers = tableNameToNotifier.values();
        if (tableNotifiers.size() > 0)
          for (final TableNotifier<?> tableNotifier : tableNotifiers) // [C]
            for (Notification<?> notification; (notification = tableNotifier.queue.poll()) != null; notification.invoke()); // [C]
      }

      @Override
      public void run() {
        if (logger.isTraceEnabled()) logger.trace("JAXDB-Notify Thread.run()");

        while (true) {
          while (state.get() != Notifier.State.STARTED) {
            synchronized (state) {
              try {
                if (state.get() != Notifier.State.STARTED)
                  state.wait();
              }
              catch (final InterruptedException e) {
                if (logger.isErrorEnabled()) logger.error("JAXDB-Notify state wait interrupted, continuing", e);
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
            if (logger.isErrorEnabled()) logger.error("Uncaught exception in Notifier.flushQueues()", t);

            setState(Notifier.State.FAILED);
            if (!(t instanceof Exception))
              Throwing.rethrow(t);
          }
        }
      }
    };

    thread.setPriority(Thread.MAX_PRIORITY);
    thread.setDaemon(true);
    thread.start();
  }

  @Override
  public final Connection getConnection() throws IOException, SQLException {
    logm(logger, TRACE, "%?.getConnection", this);
    if (!connection.isClosed())
      return connection;

    connection = connectionFactory.getConnection();
    connection.setAutoCommit(true);

    if (logger.isDebugEnabled()) logger.debug("getConnection(): New connection: " + ObjectUtil.simpleIdentityString(connection));
    return connection;
  }

  @SuppressWarnings("unchecked")
  final void notify(final String tableName, final String payload) {
    final State state = this.state.get();
    logm(logger, TRACE, "%?.notify", "state=%s,%s,%s", this, state, tableName, payload);
    if (state != Notifier.State.STARTED)
      return;

    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(tableName);
    if (tableNotifier == null)
      return;

    String sessionId = null;
    long timestamp = 0;
    try {
      final Map<String,Object> json = (Map<String,Object>)JSON.parse(payload, typeMap);
      sessionId = (String)json.get("sessionId");
      timestamp = Numbers.parseLong((String)json.get("timestamp"), 0L);
      tableNotifier.notify(sessionId, timestamp, json);
    }
    catch (final Exception e) {
      if (logger.isErrorEnabled()) logger.error("Uncaught exception in Notifier.notify()", e);
      setState(Notifier.State.FAILED);
      tableNotifier.onFailure(sessionId, timestamp, e);
    }
  }

  private final AtomicReference<State> state = new AtomicReference<>(Notifier.State.CREATED);

  private void setState(final State state) {
    logm(logger, TRACE, "%?.setState", "%s", this, state);

    this.state.set(state);

    synchronized (this.state) {
      this.state.notifyAll();
    }
  }

  private final HashMap<String,TableNotifier<?>> tableNameToNotifier = new HashMap<String,TableNotifier<?>>() {
    @Override
    public void clear() {
      final Collection<TableNotifier<?>> values = values();
      if (values.size() > 0)
        for (final TableNotifier<?> notifier : values) // [C]
          notifier.close();

      super.clear();
    }
  };

  private void recreateTrigger(final Connection connection, final data.Table[] tables, final Action[][] actionSets) throws SQLException {
    if (logger.isTraceEnabled()) logm(logger, TRACE, "%?.recreateTrigger", "%?,%s,%s", this, connection, Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new), Arrays.deepToString(actionSets));

    try (final Statement statement = connection.createStatement()) {
      checkCreateTriggers(statement, tables, actionSets);
      listenTriggers(statement);
      statement.executeBatch();
    }
  }

  abstract void checkCreateTriggers(Statement statement, data.Table[] tables, Action[][] actionSets) throws SQLException;
  abstract void listenTriggers(Statement statement) throws SQLException;
  abstract void start(Connection connection) throws IOException, SQLException;
  abstract void tryReconnect(Connection connection, L listener) throws SQLException;

  final void reconnect(final Connection connection, final L listener) throws IOException, SQLException {
    logm(logger, TRACE, "%?.reconnect", "%?,%?", this, connection, listener);
    final State state = this.state.get();
    if (state != Notifier.State.CREATED && state != Notifier.State.STARTED) {
      if (logger.isWarnEnabled()) logger.warn("Trying to reconnect when Notifier.state == " + state);
      return;
    }

    try {
      tryReconnect(connection, listener);

      final Collection<TableNotifier<?>> tableNotifiers = tableNameToNotifier.values();
      if (tableNotifiers.size() > 0)
        for (final TableNotifier<?> tableNotifier : tableNotifiers) // [C]
          tableNotifier.onConnect(connection);

      listenTriggers(connection);
    }
    catch (final Exception e) {
      setState(Notifier.State.FAILED);
      throw e;
    }
  }

  protected abstract void stop() throws SQLException;

  boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete) throws IOException, SQLException {
    final Collection<TableNotifier<?>> tableNotifiers = tableNameToNotifier.values();
    final int size = tableNotifiers.size();
    if (size == 0)
      return false;

    final data.Table[] tables = new data.Table[size];
    final Iterator<TableNotifier<?>> iterator = tableNotifiers.iterator();
    for (int i = 0; i < size; ++i) // [I]
      tables[i] = iterator.next().table;

    return removeNotificationListeners(insert, up, delete, tables);
  }

  boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete, final data.Table[] tables) throws IOException, SQLException {
    final Action[][] actionSets = new Action[tables.length][];
    boolean hasChanges = false;
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final data.Table table = tables[i];
      final String tableName = table.getName();
      final TableNotifier<?> tableNotifier = tableNameToNotifier.get(tableName);
      if (tableNotifier == null || !tableNotifier.removeActions(insert, up, delete))
        continue;

      if (tableNotifier.isEmpty())
        tableNameToNotifier.remove(tableName);

      actionSets[i] = tableNotifier.allActions;
      hasChanges = true;
    }

    if (!hasChanges)
      return false;

    try (final Connection connection = connectionFactory.getConnection()) {
      recreateTrigger(connection, tables, actionSets);
    }

    if (tableNameToNotifier.isEmpty()) {
      setState(Notifier.State.STOPPED);
      stop();
      if (connection != null)
        connection.close();
    }

    return true;
  }

  final boolean hasNotificationListener(final INSERT insert, final UP up, final DELETE delete, final data.Table table) {
    if (insert == null && up == null && delete == null)
      throw new IllegalArgumentException("insert == null && up == null && delete == null");

    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getName());
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
  private <T extends data.Table>void addListenerForTables(final Connection connection, final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<? super T> notificationListener, final Queue<Notification<? super T>> queue, final T[] tables) throws SQLException {
    final Action[][] actionSets = new Action[tables.length][];
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final T table = tables[i];
      final String tableName = table.getName();
      TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
      if (tableNotifier == null)
        tableNameToNotifier.put(tableName, tableNotifier = new TableNotifier(table.singleton(), queue));

      // actionSet must not be null here, because we're adding notification listeners for tables
      actionSets[i] = tableNotifier.addNotificationListener(notificationListener, insert, up, delete);
    }

    recreateTrigger(connection, tables, actionSets);
  }

  private void listenTriggers(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      listenTriggers(statement);
      statement.executeBatch();
    }
  }

  @SuppressWarnings("unchecked")
  final <T extends data.Table>boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<? super T> notificationListener, final Queue<Notification<? super T>> queue, final T ... tables) throws IOException, SQLException {
    assertNotNull(notificationListener);
    assertNotEmpty(tables);
    if (insert == null && up == null && delete == null)
      return false;

    if (logger.isTraceEnabled()) logm(logger, TRACE, "%?.addNotificationListener", "%s,%s,%s,Listener@%h,%s", this, insert, up, delete, notificationListener, Arrays.stream(tables).map(data.Table::getName).toArray(String[]::new));

    if (state.get() == Notifier.State.FAILED) {
      if (logger.isWarnEnabled()) logger.warn("Trying to addNotificationListener(...) when Notifier.state == " + state);
      return false;
    }

    if (state.get() == Notifier.State.STOPPED)
      setState(Notifier.State.CREATED);

    if (state.get() != Notifier.State.STARTED) {
      synchronized (state) {
        if (state.get() != Notifier.State.STARTED) {
          // This will be the Connection for PG-JDBC I/O
          final Connection connection = getConnection();

          addListenerForTables(connection, insert, up, delete, notificationListener, queue, tables);

          // Start the Notifier, which also calls the onConnect(data.Table) callback
          connection.setAutoCommit(true);
          start(connection);

          setState(Notifier.State.STARTED);

          // Activate triggers for the tables
          listenTriggers(connection);

          return true;
        }
      }
    }

    // Create a connection that will close, because Notifier is already running on another connection.
    try (final Connection connection = connectionFactory.getConnection()) {
      addListenerForTables(connection, insert, up, delete, notificationListener, queue, tables);

      // If `this.connection != connection` it means the onConnect(data.Table) call
      // was not made in reconnect(), so invoke it directly for each new table.
      for (final T table : tables) // [A]
        notificationListener.onConnect(connection, table);

      // Activate triggers for the tables
      listenTriggers(connection);
    }

    return true;
  }

  final boolean isErrored() {
    return state.get() == Notifier.State.FAILED;
  }

  final boolean isClosed() {
    return state.get() == Notifier.State.STOPPED;
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onConnect(final Connection connection, final data.Table table) throws IOException, SQLException {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.INSERT.ordinal()] != null)
          entry.getKey().onConnect(connection, table);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.INSERT.ordinal()] != null)
          entry.getKey().onFailure(sessionId, timestamp, table, e);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void onSelect(final data.Table row, final boolean addKey) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.SELECT.ordinal()] != null)
          Action.SELECT.onSelect(entry.getKey(), row, addKey);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void onSelectRange(final data.Table table, final Interval<data.Key> ... intervals) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(table.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.SELECT.ordinal()] != null)
          Action.SELECT.onSelectRange(entry.getKey(), table, intervals);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void onInsert(final String sessionId, final long timestamp, final data.Table row) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.INSERT.ordinal()] != null)
          Action.INSERT.invoke(sessionId, timestamp, entry.getKey(), null, row);
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void onUpdate(final String sessionId, final long timestamp, final data.Table row, final Map<String,String> keyForUpdate) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0) {
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) { // [S]
        final Action[] actions = entry.getValue();
        if (actions[Action.UPDATE.ordinal()] != null)
          Action.UPDATE.invoke(sessionId, timestamp, entry.getKey(), null, row);
        else if (actions[Action.UPGRADE.ordinal()] != null)
          Action.UPGRADE.invoke(sessionId, timestamp, entry.getKey(), keyForUpdate, row);
      }
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void onDelete(final String sessionId, final long timestamp, final data.Table row) {
    final TableNotifier<?> tableNotifier = tableNameToNotifier.get(row.getName());
    if (tableNotifier == null)
      return;

    final IdentityHashMap<Notification.Listener,Action[]> notificationListenerToActions = tableNotifier.notificationListenerToActions;
    if (notificationListenerToActions.size() > 0)
      for (final Map.Entry<Notification.Listener,Action[]> entry : notificationListenerToActions.entrySet()) // [S]
        if (entry.getValue()[Action.DELETE.ordinal()] != null)
          Action.DELETE.invoke(sessionId, timestamp, entry.getKey(), null, row);
  }

  @Override
  public final void close() {
    tableNameToNotifier.clear();
    if (connection == null)
      return;

    try {
      connection.close();
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled()) logger.warn(e.getMessage(), e);
    }
  }
}