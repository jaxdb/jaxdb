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

import static org.jaxdb.jsql.DML.SELECT;
import static org.jaxdb.jsql.Notification.Action.*;
import static org.jaxdb.jsql.Notification.Action.DELETE;
import static org.jaxdb.jsql.Notification.Action.INSERT;
import static org.libj.lang.Assertions.*;
import static org.libj.logging.LoggerUtil.*;
import static org.slf4j.event.Level.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import javax.sql.DataSource;

import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.jsql.Notification.DefaultListener;
import org.jaxdb.jsql.RowIterator.Cacheability;
import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;
import org.libj.util.Interval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database extends Notifiable {
  private static final Logger logger = LoggerFactory.getLogger(Database.class);
  private static final IdentityHashMap<Class<? extends Schema>,Object[]> schemaClassToLocalGlobal = new IdentityHashMap<Class<? extends Schema>,Object[]>() {
    @Override
    @SuppressWarnings("unchecked")
    public Object[] get(final Object key) {
      final Class<? extends Schema> schemaClass = (Class<? extends Schema>)key;
      Object[] value = super.get(schemaClass);
      if (value == null)
        super.put(schemaClass, value = new Object[2]);

      return value;
    }
  };

  @SuppressWarnings("unchecked")
  public static Database threadLocal(final Class<? extends Schema> schemaClass) {
    final Object[] localGlobal = schemaClassToLocalGlobal.get(schemaClass);
    final ThreadLocal<Database> threadLocal = (ThreadLocal<Database>)localGlobal[0];
    if (threadLocal != null)
      return threadLocal.get();

    final Database database = new Database(schemaClass);
    localGlobal[0] = new ThreadLocal<Database>() {
      @Override
      protected Database initialValue() {
        return database;
      }
    };

    return database;
  }

  public static Database global(final Class<? extends Schema> schemaClass) {
    final Object[] localGlobal = schemaClassToLocalGlobal.get(schemaClass);
    Database database = (Database)localGlobal[1];
    if (database == null)
      localGlobal[1] = database = new Database(schemaClass);

    return database;
  }

  private static ConnectionFactory toConnectionFactory(final DataSource dataSource) {
    assertNotNull(dataSource, "dataSource is null");
    return () -> AuditConnection.wrapIfDebugEnabled(dataSource.getConnection());
  }

  @SuppressWarnings("unchecked")
  static ConcurrentHashMap<String,Connector> getConnectors(final Class<? extends Schema> schemaClass) {
    final Object[] localGlobal = schemaClassToLocalGlobal.get(assertNotNull(schemaClass));
    final Database database;
    if (localGlobal[0] != null)
      database = ((ThreadLocal<Database>)localGlobal[0]).get();
    else if (localGlobal[1] != null)
      database = (Database)localGlobal[1];
    else
      database = null;

    if (database == null)
      throw new IllegalArgumentException("Connector for schema=\"" + (schemaClass == null ? null : schemaClass.getName()) + " does not exist");

    return database.schemaClassToDataSourceIdToConnector.get(schemaClass);
  }

  static Connector getConnector(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    final ConcurrentHashMap<String,Connector> dataSourceIdToConnector = getConnectors(schemaClass);
    return dataSourceIdToConnector == null ? null : dataSourceIdToConnector.get(dataSourceId);
  }

  static Boolean isPrepared(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    final Connector connector = getConnector(schemaClass, dataSourceId);
    return connector == null ? null : connector.isPrepared();
  }

  private final ConcurrentHashMap<Class<?>,ConcurrentNullHashMap<String,Connector>> schemaClassToDataSourceIdToConnector = new ConcurrentHashMap<>(2);
  private final Class<? extends Schema> schemaClass;
  private Schema schema;

  private Database(final Class<? extends Schema> schemaClass) {
    this.schemaClass = schemaClass;
  }

  private Connector findConnector(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    logm(logger, TRACE, "%?.findConnector", "%s,%s", this, schemaClass, dataSourceId);
    ConcurrentNullHashMap<String,Connector> dataSourceIdToConnector = schemaClassToDataSourceIdToConnector.get(schemaClass);
    Connector connector;
    if (dataSourceIdToConnector == null)
      schemaClassToDataSourceIdToConnector.put(schemaClass, dataSourceIdToConnector = new ConcurrentNullHashMap<>(2));
    else if ((connector = dataSourceIdToConnector.get(dataSourceId)) != null)
      return connector;

    dataSourceIdToConnector.put(dataSourceId, connector = new Connector(schemaClass, dataSourceId));
    return connector;
  }

  private Connector connect(final Class<? extends Schema> schemaClass, final ConnectionFactory connectionFactory, final boolean prepared, final String dataSourceId) {
    final Connector connector = findConnector(schemaClass, dataSourceId);
    connector.set(connectionFactory, prepared);
    return connector;
  }

  public Connector connect(final ConnectionFactory connectionFactory) {
    return connect(schemaClass, connectionFactory, false, null);
  }

  public Connector connect(final ConnectionFactory connectionFactory, final String dataSourceId) {
    return connect(schemaClass, connectionFactory, false, dataSourceId);
  }

  public Connector connect(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, null);
  }

  public Connector connect(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, dataSourceId);
  }

  public Connector connectPrepared(final ConnectionFactory connectionFactory) {
    return connect(schemaClass, connectionFactory, true, null);
  }

  public Connector connectPrepared(final ConnectionFactory connectionFactory, final String dataSourceId) {
    return connect(schemaClass, connectionFactory, true, dataSourceId);
  }

  public Connector connectPrepared(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, null);
  }

  public Connector connectPrepared(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, dataSourceId);
  }

  public Boolean isPrepared(final String dataSourceId) {
    return isPrepared(schemaClass, dataSourceId);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table>boolean addNotificationListener(final Connector connector, final INSERT insert, final Notification.InsertListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), null, null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table>boolean addNotificationListener(final Connector connector, final UP up, final Notification.UpdateListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, assertNotNull(up), null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table>boolean addNotificationListener(final Connector connector, final DELETE delete, final Notification.DeleteListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, null, assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table,L extends Notification.InsertListener<T> & Notification.UpdateListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final UP up, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), assertNotNull(up), null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table,L extends Notification.UpdateListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, assertNotNull(up), assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table,L extends Notification.InsertListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), null, assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table,L extends Notification.InsertListener<T> & Notification.UpdateListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), assertNotNull(up), assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  private <T extends data.Table>boolean addNotificationListener0(final Connector connector, final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    assertNotNull(connector);
    assertNotNull(notificationListener);
    assertNotEmpty(tables);

    for (final T table : tables) // [A]
      table._initCache$();

    if (schema == null)
      schema = Schema.getSchema(schemaClass);

    return connector.addNotificationListener0(insert, up, delete, notificationListener, queue, tables);
  }

  private static final QueryConfig refreshQueryConfig = new QueryConfig.Builder().withCacheability(Cacheability.SELECT_CACHEABLE_ENTITY).build();

  @FunctionalInterface
  public static interface OnConnectPreLoad {
    void accept(data.Table t, Connection u) throws IOException, SQLException;

    public static final OnConnectPreLoad ALL = (final data.Table table, final Connection connection) -> {
      if (!table._mutable$)
        throw new IllegalArgumentException("Table is mutable");

      try (final RowIterator<?> rows =
        SELECT(table).
        FROM(table)
          .execute(connection, refreshQueryConfig)) {
        while (rows.nextRow());
      }
    };

    public static final OnConnectPreLoad NONE = (final data.Table table, final Connection connection) -> {};
  }

  public static class CacheConfig {
    private final Database database;
    private final Connector connector;
    private final DefaultListener<data.Table> notificationListener;
    private final Queue<Notification<data.Table>> queue;
    private final ArrayList<data.Table> tables = new ArrayList<>();

    private CacheConfig(final Database database, final Connector connector, final DefaultListener<data.Table> notificationListener, final Queue<Notification<data.Table>> queue) {
      this.database = database;
      this.connector = connector;
      this.notificationListener = notificationListener;
      this.queue = queue;
    }

    public CacheConfig with(final data.Table ... tables) {
      for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
        final data.Table table = tables[i];
        table.configureCache(OnConnectPreLoad.ALL, Cacheability.SELECT_CACHEABLE_ENTITY);
        this.tables.add(assertNotNull(table));
      }

      return this;
    }

    public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final data.Table ... tables) {
      for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
        final data.Table table = tables[i];
        table.configureCache(assertNotNull(onConnectPreLoad), Cacheability.SELECT_CACHEABLE_ENTITY);
        this.tables.add(assertNotNull(table));
      }

      return this;
    }

    public CacheConfig with(final Cacheability cacheability, final data.Table ... tables) {
      for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
        final data.Table table = tables[i];
        table.configureCache(OnConnectPreLoad.ALL, cacheability);
        this.tables.add(assertNotNull(table));
      }

      return this;
    }

    public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final Cacheability cacheability, final data.Table ... tables) {
      for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
        final data.Table table = tables[i];
        table.configureCache(assertNotNull(onConnectPreLoad), cacheability);
        this.tables.add(assertNotNull(table));
      }

      return this;
    }

    private void commit() throws IOException, SQLException {
      database.cacheConfigured = true;
      database.addNotificationListener(connector, INSERT, UPGRADE, DELETE, notificationListener, queue, tables.toArray(new data.Table[tables.size()]));
    }
  }

  private boolean cacheConfigured = false;

  public void configCache(final Connector connector, final DefaultListener<data.Table> notificationListener, final Queue<Notification<data.Table>> queue, final Consumer<CacheConfig> cacheBuilder) throws IOException, SQLException {
    if (cacheConfigured)
      throw new IllegalStateException("Cache already configured");

    final CacheConfig builder = new CacheConfig(this, connector, notificationListener, queue);
    cacheBuilder.accept(builder);
    builder.commit();
  }

  @Override
  void onConnect(final Connection connection, final data.Table table, final OnConnectPreLoad onConnectPreLoad) throws IOException, SQLException {
    if (schema != null)
      schema.onConnect(connection, table, onConnectPreLoad);
  }

  @Override
  void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
    if (schema != null)
      schema.onFailure(sessionId, timestamp, table, e);
  }

  @Override
  void onSelect(final data.Table row, final boolean addKey) {
    if (schema != null)
      schema.onSelect(row, addKey);
  }

  @Override
  void onSelectRange(final data.Table table, final Interval<type.Key>[] intervals) {
    if (schema != null)
      schema.onSelectRange(table, intervals);
  }

  @Override
  void onInsert(final String sessionId, final long timestamp, final data.Table row) {
    if (schema != null)
      schema.onInsert(sessionId, timestamp, row);
  }

  @Override
  void onUpdate(final String sessionId, final long timestamp, final data.Table row, final Map<String,String> keyForUpdate) {
    if (schema != null)
      schema.onUpdate(sessionId, timestamp, row, keyForUpdate);
  }

  @Override
  void onDelete(final String sessionId, final long timestamp, final data.Table row) {
    if (schema != null)
      schema.onDelete(sessionId, timestamp, row);
  }
}