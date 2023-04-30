/* Copyright (c) 2014 JAX-DB
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.data.Key;
import org.libj.lang.ObjectUtil;
import org.libj.util.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Schema extends Notifiable {
  private static final Logger logger = LoggerFactory.getLogger(Schema.class);
  private static final IdentityHashMap<Class<? extends Schema>,Schema> instances = new IdentityHashMap<>();

  Schema() {
    instances.put(getClass(), this);
  }

  static Schema getSchema(final Class<? extends Schema> schemaClass) {
    final Schema schema = instances.get(assertNotNull(schemaClass));
    if (schema == null) {
      try {
        Class.forName(schemaClass.getName());
      }
      catch (final ClassNotFoundException e) {
      }
    }

    return instances.get(assertNotNull(schemaClass));
  }

  QueryConfig defaultQueryConfig;
  Listeners<Notification.Listener<?>> listeners;
  Listeners<Notification.SelectListener<?>> selectListeners;
  Listeners<Notification.InsertListener<?>> insertListeners;
  Listeners<Notification.UpdateListener<?>> updateListeners;
  Listeners<Notification.DeleteListener<?>> deleteListeners;
  ConcurrentHashMap<String,OnNotifyCallbackList> notifyListeners;

  @SuppressWarnings("unchecked")
  private static Class<? extends data.Table> getTableClass(final data.Table table) {
    Class<?> c = table.getClass();
    while (c.isAnonymousClass())
      c = c.getSuperclass();

    return (Class<? extends data.Table>)c;
  }

  private class Listeners<K extends Notification.Listener<?>> extends LinkedHashMap<K,LinkedHashSet<Class<? extends data.Table>>> {
    @Override
    @SuppressWarnings("unchecked")
    public LinkedHashSet<Class<? extends data.Table>> get(final Object key) {
      LinkedHashSet<Class<? extends data.Table>> value = super.get(key);
      if (value == null)
        put((K)key, value = new LinkedHashSet<>());

      return value;
    }

    void add(final K key, final data.Table[] tables) {
      final LinkedHashSet<Class<? extends data.Table>> set = get(key);
      for (final data.Table table : tables) // [A]
        set.add(getTableClass(table));
    }
  }

  void addListener(final Notification.Listener<?> listener, final data.Table[] tables) {
    if (listeners == null)
      listeners = new Listeners<>();

    listeners.add(listener, tables);
  }

  void addListener(final Notification.SelectListener<?> listener, final data.Table[] tables) {
    if (selectListeners == null)
      selectListeners = new Listeners<>();

    selectListeners.add(listener, tables);
  }

  void addListener(final Notification.InsertListener<?> listener, final data.Table[] tables) {
    if (insertListeners == null)
      insertListeners = new Listeners<>();

    insertListeners.add(listener, tables);
  }

  void addListener(final Notification.UpdateListener<?> listener, final data.Table[] tables) {
    if (updateListeners == null)
      updateListeners = new Listeners<>();

    updateListeners.add(listener, tables);
  }

  void addListener(final Notification.DeleteListener<?> listener, final data.Table[] tables) {
    if (deleteListeners == null)
      deleteListeners = new Listeners<>();

    deleteListeners.add(listener, tables);
  }

  void awaitNotify(final String sessionId, final OnNotifyCallbackList onNotifyCallbackList) {
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".awaitNotify(" + sessionId + "," + ObjectUtil.simpleIdentityString(onNotifyCallbackList) + ")");

    if (notifyListeners == null) {
      synchronized (this) {
        if (notifyListeners == null) {
          notifyListeners = new ConcurrentHashMap<>();
        }
      }
    }

    notifyListeners.put(sessionId, onNotifyCallbackList);
  }

  void removeSession(final String sessionId) {
    final OnNotifyCallbackList onNotifyCallbackList = notifyListeners.remove(sessionId);
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".removeSession(" + sessionId + "): " + ObjectUtil.simpleIdentityString(onNotifyCallbackList));
  }

  OnNotifyCallbackList getSession(final String sessionId) {
    final OnNotifyCallbackList onNotifyCallbackList = notifyListeners == null ? null : notifyListeners.get(sessionId);
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".getSession(" + sessionId + "): " + ObjectUtil.simpleIdentityString(onNotifyCallbackList));
    return onNotifyCallbackList;
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onConnect(final Connection connection, final data.Table table) throws IOException, SQLException {
    if (listeners == null)
      return;

    final Class<?> tableClass = table.getClass();
    for (final Map.Entry<? extends Notification.Listener,LinkedHashSet<Class<? extends data.Table>>> entry : listeners.entrySet()) // [S]
      if (entry.getValue().contains(tableClass))
        entry.getKey().onConnect(connection, table);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
    if (listeners == null)
      return;

    final Class<?> tableClass = table.getClass();
    for (final Map.Entry<? extends Notification.Listener,LinkedHashSet<Class<? extends data.Table>>> entry : listeners.entrySet()) // [S]
      if (entry.getValue().contains(tableClass))
        entry.getKey().onFailure(sessionId, timestamp, table, e);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onSelect(final data.Table row, final boolean addRange) {
    if (selectListeners == null)
      return;

    final Class<?> rowClass = row.getClass();
    for (final Map.Entry<? extends Notification.SelectListener,LinkedHashSet<Class<? extends data.Table>>> entry : selectListeners.entrySet()) // [S]
      if (entry.getValue().contains(rowClass))
        entry.getKey().onSelect(row, addRange);
  }

  @Override
  void onSelectRange(final data.Table table, Interval<Key>[] intervals) {
    if (selectListeners == null)
      return;

    final Class<?> rowClass = table.getClass();
    for (final Map.Entry<? extends Notification.SelectListener,LinkedHashSet<Class<? extends data.Table>>> entry : selectListeners.entrySet()) // [S]
      if (entry.getValue().contains(rowClass))
        entry.getKey().onSelectRange(table, intervals);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onInsert(final String sessionId, final long timestamp, final data.Table row) {
    if (insertListeners == null)
      return;

    final Class<?> rowClass = row.getClass();
    for (final Map.Entry<? extends Notification.InsertListener,LinkedHashSet<Class<? extends data.Table>>> entry : insertListeners.entrySet()) // [S]
      if (entry.getValue().contains(rowClass))
        entry.getKey().onInsert(sessionId, timestamp, row);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onUpdate(final String sessionId, final long timestamp, final data.Table row, final Map<String,String> keyForUpdate) {
    if (updateListeners == null)
      return;

    final Class<?> rowClass = row.getClass();
    for (final Map.Entry<? extends Notification.UpdateListener,LinkedHashSet<Class<? extends data.Table>>> entry : updateListeners.entrySet()) // [S]
      if (entry.getValue().contains(rowClass))
        entry.getKey().onUpdate(sessionId, timestamp, row, keyForUpdate);
  }

  @Override
  @SuppressWarnings("rawtypes")
  void onDelete(final String sessionId, final long timestamp, final data.Table row) {
    if (deleteListeners == null)
      return;

    final Class<?> rowClass = row.getClass();
    for (final Map.Entry<? extends Notification.DeleteListener,LinkedHashSet<Class<? extends data.Table>>> entry : deleteListeners.entrySet()) // [S]
      if (entry.getValue().contains(rowClass))
        entry.getKey().onDelete(sessionId, timestamp, row);
  }
}