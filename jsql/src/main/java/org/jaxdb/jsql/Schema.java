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

import org.jaxdb.jsql.Callbacks.OnNotifies;

public abstract class Schema extends Notifiable {
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

  Listeners<Notification.Listener<?>> listeners;
  Listeners<Notification.InsertListener<?>> insertListeners;
  Listeners<Notification.UpdateListener<?>> updateListeners;
  Listeners<Notification.DeleteListener<?>> deleteListeners;
  ConcurrentHashMap<String,OnNotifies> notifyListeners;

  private static Class<? extends data.Table<?>> getTableClass(final data.Table<?> table) {
    Class<?> c = table.getClass();
    while (c.isAnonymousClass())
      c = c.getSuperclass();

    return (Class<? extends data.Table<?>>)c;
  }

  private class Listeners<K extends Notification.Listener<?>> extends LinkedHashMap<K,LinkedHashSet<Class<? extends data.Table<?>>>> {
    @Override
    public LinkedHashSet<Class<? extends data.Table<?>>> get(final Object key) {
      LinkedHashSet<Class<? extends data.Table<?>>> value = super.get(key);
      if (value == null)
        put((K)key, value = new LinkedHashSet<>());

      return value;
    }

    void add(final K key, final data.Table<?>[] tables) {
      final LinkedHashSet set = get(key);
      for (final data.Table<?> table : tables) // [A]
        set.add(getTableClass(table));
    }
  }

  void addListener(final Notification.Listener<?> listener, final data.Table<?>[] tables) {
    if (listeners == null)
      listeners = new Listeners<>();

    listeners.add(listener, tables);
  }

  void addListener(final Notification.InsertListener<?> listener, final data.Table<?>[] tables) {
    if (insertListeners == null)
      insertListeners = new Listeners<>();

    insertListeners.add(listener, tables);
  }

  void addListener(final Notification.UpdateListener<?> listener, final data.Table<?>[] tables) {
    if (updateListeners == null)
      updateListeners = new Listeners<>();

    updateListeners.add(listener, tables);
  }

  void addListener(final Notification.DeleteListener<?> listener, final data.Table<?>[] tables) {
    if (deleteListeners == null)
      deleteListeners = new Listeners<>();

    deleteListeners.add(listener, tables);
  }

  void awaitNotify(final String sessionId, final OnNotifies listeners) {
    if (notifyListeners == null) {
      synchronized (this) {
        if (notifyListeners == null) {
          notifyListeners = new ConcurrentHashMap<>();
        }
      }
    }

    notifyListeners.put(sessionId, listeners);
  }

  OnNotifies removeSession(final String sessionId) {
    return sessionId == null || notifyListeners == null ? null : notifyListeners.remove(sessionId);
  }

  OnNotifies getSession(final String sessionId) {
    return sessionId == null || notifyListeners == null ? null : notifyListeners.get(sessionId);
  }

  @Override
  void onConnect(final Connection connection, final data.Table<?> table) throws IOException, SQLException {
    if (listeners != null)
      for (final Map.Entry<? extends Notification.Listener,LinkedHashSet<Class<? extends data.Table<?>>>> entry : listeners.entrySet()) // [S]
        if (entry.getValue().contains(table.getClass()))
          entry.getKey().onConnect(connection, table);
  }

  @Override
  void onFailure(final String sessionId, final long timestamp, final data.Table<?> table, final Exception e) {
    if (listeners != null)
      for (final Map.Entry<? extends Notification.Listener,LinkedHashSet<Class<? extends data.Table<?>>>> entry : listeners.entrySet()) // [S]
        if (entry.getValue().contains(table.getClass()))
          entry.getKey().onFailure(sessionId, timestamp, table, e);
  }

  @Override
  void onInsert(final String sessionId, final long timestamp, final data.Table<?> row) {
    if (insertListeners != null)
      for (final Map.Entry<? extends Notification.InsertListener,LinkedHashSet<Class<? extends data.Table<?>>>> entry : insertListeners.entrySet()) // [S]
        if (entry.getValue().contains(row.getClass()))
          entry.getKey().onInsert(sessionId, timestamp, row);
  }

  @Override
  void onUpdate(final String sessionId, final long timestamp, final data.Table<?> row, final Map<String,String> keyForUpdate) {
    if (updateListeners != null)
      for (final Map.Entry<? extends Notification.UpdateListener,LinkedHashSet<Class<? extends data.Table<?>>>> entry : updateListeners.entrySet()) // [S]
        if (entry.getValue().contains(row.getClass()))
          entry.getKey().onUpdate(sessionId, timestamp, row, keyForUpdate);
  }

  @Override
  void onDelete(final String sessionId, final long timestamp, final data.Table<?> row) {
    if (deleteListeners != null)
      for (final Map.Entry<? extends Notification.DeleteListener,LinkedHashSet<Class<? extends data.Table<?>>>> entry : deleteListeners.entrySet()) // [S]
        if (entry.getValue().contains(row.getClass()))
          entry.getKey().onDelete(sessionId, timestamp, row);
  }
}