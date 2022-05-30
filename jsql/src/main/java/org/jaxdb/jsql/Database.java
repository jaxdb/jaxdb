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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Queue;

import javax.sql.DataSource;

import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;
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
    return () -> new AuditConnection(dataSource.getConnection());
  }

  @SuppressWarnings("unchecked")
  static Connector getConnector(final Class<? extends Schema> schemaClass, final String dataSourceId) {
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

    final String schemaClassNameDataSourceId = schemaClass.getName() + "<" + dataSourceId + ">";
    return database.schemaClassNameIdToConnector.get(schemaClassNameDataSourceId);
  }

  static boolean isPrepared(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    final Connector connector = getConnector(schemaClass, dataSourceId);
    return connector != null && connector.isPrepared();
  }

  private final ConcurrentNullHashMap<String,Connector> schemaClassNameIdToConnector = new ConcurrentNullHashMap<>();
  private final Class<? extends Schema> schemaClass;
  private Schema schema;

  private Database(final Class<? extends Schema> schemaClass) {
    this.schemaClass = schemaClass;
  }

  private Connector connect(final Class<? extends Schema> schemaClass, final ConnectionFactory connectionFactory, final boolean prepared, final String dataSourceId) {
    logm(logger, TRACE, "%?.connect", "%s,%?,%b,%s", this, schemaClass, connectionFactory, prepared, dataSourceId);
    final String schemaClassNameDataSourceId = schemaClass.getName() + "<" + dataSourceId + ">";
    Connector connector = schemaClassNameIdToConnector.get(schemaClassNameDataSourceId);
    if (connector == null)
      schemaClassNameIdToConnector.put(schemaClassNameDataSourceId, connector = new Connector(schemaClass, dataSourceId));

    connector.set(connectionFactory, prepared);
    return connector;
  }

  public Connector connect(final ConnectionFactory connectionFactory) {
    return connect(schemaClass, assertNotNull(connectionFactory, "connectionFactory is null"), false, null);
  }

  public Connector connect(final ConnectionFactory connectionFactory, final String dataSourceId) {
    return connect(schemaClass, assertNotNull(connectionFactory, "connectionFactory is null"), false, dataSourceId);
  }

  public Connector connect(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, null);
  }

  public Connector connect(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, dataSourceId);
  }

  public Connector connectPrepared(final ConnectionFactory connectionFactory) {
    return connect(schemaClass, assertNotNull(connectionFactory, "connectionFactory is null"), true, null);
  }

  public Connector connectPrepared(final ConnectionFactory connectionFactory, final String dataSourceId) {
    return connect(schemaClass, assertNotNull(connectionFactory, "connectionFactory is null"), true, dataSourceId);
  }

  public Connector connectPrepared(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, null);
  }

  public Connector connectPrepared(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, dataSourceId);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final Connector connector, final INSERT insert, final Notification.InsertListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), null, null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final Connector connector, final UP up, final Notification.UpdateListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, assertNotNull(up), null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final Connector connector, final DELETE delete, final Notification.DeleteListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, null, assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener(notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>,L extends Notification.InsertListener<T> & Notification.UpdateListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final UP up, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), assertNotNull(up), null, notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener((Notification.InsertListener<?>)notificationListener, tables);
    schema.addListener((Notification.UpdateListener<?>)notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>,L extends Notification.UpdateListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, null, assertNotNull(up), assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener((Notification.UpdateListener<?>)notificationListener, tables);
    schema.addListener((Notification.DeleteListener<?>)notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>,L extends Notification.InsertListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), null, assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener((Notification.InsertListener<?>)notificationListener, tables);
    schema.addListener((Notification.DeleteListener<?>)notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>,L extends Notification.InsertListener<T> & Notification.UpdateListener<T> & Notification.DeleteListener<T>>boolean addNotificationListener(final Connector connector, final INSERT insert, final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    final boolean added = addNotificationListener0(connector, assertNotNull(insert), assertNotNull(up), assertNotNull(delete), notificationListener, queue, tables);
    if (!added)
      return false;

    schema.addListener((Notification.InsertListener<?>)notificationListener, tables);
    schema.addListener((Notification.UpdateListener<?>)notificationListener, tables);
    schema.addListener((Notification.DeleteListener<?>)notificationListener, tables);
    return true;
  }

  @SuppressWarnings("unchecked")
  private <T extends data.Table<?>>boolean addNotificationListener0(final Connector connector, final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    assertNotNull(connector);
    assertNotNull(notificationListener);
    assertNotEmpty(tables);

    for (final data.Table<?> table : tables)
      table._initCache$();

    if (schema == null)
      schema = Schema.getSchema(schemaClass);

    return connector.addNotificationListener0(insert, up, delete, notificationListener, queue, tables);
  }

  @Override
  void onConnect(final Connection connection, final data.Table<?> table) throws IOException, SQLException {
    if (schema != null)
      schema.onConnect(connection, table);
  }

  @Override
  void onFailure(final data.Table<?> table, final Throwable t) {
    if (schema != null)
      schema.onFailure(table, t);
  }

  @Override
  void onInsert(final data.Table<?> row) {
    if (schema != null)
      schema.onInsert(row);
  }

  @Override
  void onUpdate(final data.Table<?> row, final Map<String,String> keyForUpdate) {
    if (schema != null)
      schema.onUpdate(row, keyForUpdate);
  }

  @Override
  void onDelete(final data.Table<?> row) {
    if (schema != null)
      schema.onDelete(row);
  }
}