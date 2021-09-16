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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.jaxdb.jsql.Notification.Action;
import org.libj.lang.Assertions;
import org.openjax.json.JSON;
import org.openjax.json.JsonParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class Notifier implements AutoCloseable, ConnectionFactory {
  private static final Logger logger = LoggerFactory.getLogger(Notifier.class);

  private static class TableNotifier<T extends data.Table> {
    private final T table;
    private final IdentityHashMap<Notification.Listener<T>,EnumSet<Action>> notificationListenerToActions = new IdentityHashMap<>();
    private final EnumSet<Action> allActions = EnumSet.noneOf(Action.class);

    private TableNotifier(final T table) {
      this.table = Assertions.assertNotNull(table);
    }

    private EnumSet<Action> addNotificationListener(final Notification.Listener<T> notificationListener, final Action ... actions) {
      Collections.addAll(allActions, Assertions.assertNotEmpty(actions));
      EnumSet<Action> actionSet = notificationListenerToActions.get(Assertions.assertNotNull(notificationListener));
      if (actionSet == null)
        notificationListenerToActions.put(notificationListener, actionSet = EnumSet.of(actions[0], actions));
      else if (!Collections.addAll(actionSet, actions))
        return null;

      return actionSet;
    }

    private boolean removeActions(final Action ... actions) {
      boolean changed = false;
      for (final Action action : actions)
        changed |= allActions.remove(action);

      if (!changed)
        return false;

      for (final Iterator<Map.Entry<Notification.Listener<T>,EnumSet<Action>>> iterator = notificationListenerToActions.entrySet().iterator(); iterator.hasNext();) {
        final Map.Entry<Notification.Listener<T>,EnumSet<Action>> entry = iterator.next();
        final EnumSet<Action> actionSet = entry.getValue();
        for (final Action action : actions)
          actionSet.remove(action);

        if (actionSet.size() == 0)
          iterator.remove();
      }

      return changed;
    }

    @SuppressWarnings("unchecked")
    void notify(final String payload) throws JsonParseException, IOException {
      final Map<String,Object> json = (Map<String,Object>)JSON.parse(payload);
      final Action action = Action.valueOf(((String)json.get("action")).toUpperCase());

      T row = null;
      for (final Map.Entry<Notification.Listener<T>,EnumSet<Action>> entry : notificationListenerToActions.entrySet()) {
        if (entry.getValue().contains(action)) {
          if (row == null) {
            row = (T)table.clone();
            row.parseJson((Map<String,Object>)json.get("data"));
          }

          entry.getKey().notification(row);
        }
      }
    }
  }

  void notify(final String tableName, final String payload) throws JsonParseException, IOException {
    if (logger.isDebugEnabled())
      logger.debug("Notifier: tableName=" + tableName + ", payload=" + payload);

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
  private final ConcurrentHashMap<String,TableNotifier<?>> tableNameToNotifier = new ConcurrentHashMap<>();

  private void recreateTrigger(final Statement statement, final String tableName, final EnumSet<Action> actionSet) throws SQLException {
    dropTrigger(statement, tableName);
    createTrigger(statement, tableName, actionSet);
  }

  abstract void start() throws IOException, SQLException;
  abstract void dropTrigger(final Statement statement, final String tableName) throws SQLException;
  abstract void createTrigger(final Statement statement, final String tableName, final EnumSet<Action> actionSet) throws SQLException;

  public abstract void removeNotificationListeners() throws IOException, SQLException;

  public <T extends data.Table>boolean removeNotificationListeners(final Action ... actions) throws IOException, SQLException {
    Assertions.assertNotNull(actions);
    boolean changed = false;
    for (final String tableName : tableNameToNotifier.keySet())
      changed |= removeNotificationListeners(tableName, actions);

    return changed;
  }

  public <T extends data.Table>boolean removeNotificationListeners(final T table, final Action ... actions) throws IOException, SQLException {
    return removeNotificationListeners(Assertions.assertNotNull(table).getName(), Assertions.assertNotNull(actions));
  }

  @SuppressWarnings("unchecked")
  private <T extends data.Table>boolean removeNotificationListeners(final String tableName, final Action ... actions) throws IOException, SQLException {
    Assertions.assertNotNull(tableName);
    Assertions.assertNotNull(actions);
    TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
    if (tableNotifier == null || !tableNotifier.removeActions(actions))
      return false;

    if (tableNotifier.allActions.size() == 0) {
      tableNameToNotifier.remove(tableName);
      return true;
    }

    try (final Statement statement = getConnection().createStatement()) {
      recreateTrigger(statement, tableName, tableNotifier.allActions);
    }

    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table>boolean addNotificationListener(final T table, final Notification.Listener<T> notificationListener, final Action ... actions) throws IOException, SQLException {
    Assertions.assertNotNull(table);
    Assertions.assertNotNull(notificationListener);
    Assertions.assertNotEmpty(actions);

    if (state.get() == State.CREATED) {
      synchronized (state) {
        if (state.get() == State.CREATED) {
          state.set(State.STARTED);
          start();
        }
      }
    }

    final String tableName = table.getName();
    TableNotifier<T> tableNotifier = (TableNotifier<T>)tableNameToNotifier.get(tableName);
    if (tableNotifier == null)
      tableNameToNotifier.put(tableName, tableNotifier = new TableNotifier<>(table));

    final EnumSet<Action> actionSet = tableNotifier.addNotificationListener(notificationListener, actions);
    if (actionSet == null)
      return false;

    try (final Statement statement = getConnection().createStatement()) {
      dropTrigger(statement, tableName);
      createTrigger(statement, tableName, actionSet);
    }

    return true;
  }

  public boolean isClosed() {
    return state.get() == State.STOPPED;
  }

  @Override
  public void close() throws SQLException {
    state.set(State.STOPPED);
  }
}