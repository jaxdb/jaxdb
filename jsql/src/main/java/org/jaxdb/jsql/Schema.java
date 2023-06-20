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

import static org.jaxdb.jsql.Notification.Action.*;
import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.jaxdb.jsql.CacheConfig.OnConnectPreLoad;
import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.Notification.DefaultListener;
import org.libj.lang.ObjectUtil;
import org.libj.sql.AuditConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Schema {
  private static final Logger logger = LoggerFactory.getLogger(Schema.class);

  private static ConnectionFactory toConnectionFactory(final DataSource dataSource) {
    assertNotNull(dataSource, "dataSource is null");
    return (final Transaction.Isolation isolation) -> {
      final Connection connection = dataSource.getConnection();
      if (isolation != null)
        connection.setTransactionIsolation(isolation.getLevel());

      return AuditConnection.wrapIfDebugEnabled(connection);
    };
  }

  private Connector connector;
  private boolean isPrepared;

  public Connector connect(final ConnectionFactory connectionFactory, final boolean isPrepared) {
    this.isPrepared = isPrepared;
    return this.connector = new Connector(this, connectionFactory, isPrepared);
  }

  public Connector connect(final DataSource dataSource, final boolean isPrepared) {
    return connect(toConnectionFactory(dataSource), isPrepared);
  }

  protected Schema() {
  }

  public Connector getConnector() {
    if (connector == null)
      throw new IllegalStateException("Schema \"" + getName() + "\" is not connected");

    return connector;
  }

  public abstract String getName();
  public abstract type.Table$ getTable(String name);
  public abstract type.Table$[] getTables();
  public abstract void setDefaultQueryConfig(QueryConfig queryConfig);

  public boolean isPrepared() {
    return isPrepared;
  }

  private Notifier<?> cacheNotifier;

  Notifier<?> getCacheNotifier() {
    return cacheNotifier;
  }

  <T extends data.Table & type.Table$,L extends Notification.InsertListener<T> & Notification.UpdateListener<T> & Notification.DeleteListener<T>>void initCache(final L notificationListener, final Queue<Notification<T>> queue, final Set<T> tables, final ArrayList<OnConnectPreLoad> onConnectPreLoads) throws IOException, SQLException {
    if (this.cacheNotifier != null)
      throw new IllegalStateException("Cache was already initialized");

    final int len = tables.size();
    final T[] array = (T[])tables.toArray(new data.Table[len]);
    for (int i = 0; i < len; ++i) // [RA]
      array[i]._initCache$();

    connector.addNotificationListener(INSERT, UPGRADE, DELETE, notificationListener, queue, array);
    this.cacheNotifier = connector.getNotifier();

    for (int i = 0; i < len; ++i) { // [RA]
      final OnConnectPreLoad onConnectPreLoad = onConnectPreLoads.get(i);
      if (onConnectPreLoad != null)
        onConnectPreLoad.accept(array[i]);
    }
  }

  public void configCache(final DefaultListener<data.Table> notificationListener, final Queue<Notification<data.Table>> queue, final Consumer<CacheConfig> cacheBuilder) throws IOException, SQLException {
    if (cacheNotifier != null)
      throw new IllegalStateException("Cache already configured");

    final CacheConfig builder = new CacheConfig(this, notificationListener, queue);
    cacheBuilder.accept(builder);
    builder.commit();
  }

  QueryConfig defaultQueryConfig;
  ConcurrentHashMap<String,OnNotifyCallbackList> notifyListeners;

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
}