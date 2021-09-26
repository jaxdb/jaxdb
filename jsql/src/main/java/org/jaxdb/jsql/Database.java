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

import java.util.IdentityHashMap;

import javax.sql.DataSource;

import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
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
    assertNotNull(dataSource, "dataSource == null");
    return () -> new AuditConnection(dataSource.getConnection());
  }

  @SuppressWarnings("unchecked")
  public static Connector getConnector(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    final Object[] localGlobal = schemaClassToLocalGlobal.get(schemaClass);
    final Database database;
    if (localGlobal[0] != null)
      database = ((ThreadLocal<Database>)localGlobal[0]).get();
    else if (localGlobal[1] != null)
      database = (Database)localGlobal[1];
    else
      database = null;

    if (database == null)
      throw new IllegalArgumentException("Connector for schema=\"" + (schemaClass == null ? null : schemaClass.getName()) + ", dataSourceId=\"" + dataSourceId + "\" does not exist");

    final String schemaClassNameDataSourceId = schemaClass.getName() + "<" + dataSourceId + ">";
    return database.schemaClassNameIdToConnector.get(schemaClassNameDataSourceId);
  }

  public static boolean isPrepared(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    final Connector connector = getConnector(schemaClass, dataSourceId);
    return connector != null && connector.isPrepared();
  }

  private final ConcurrentNullHashMap<String,Connector> schemaClassNameIdToConnector = new ConcurrentNullHashMap<>();
  private final Class<? extends Schema> schemaClass;

  private Database(final Class<? extends Schema> schemaClass) {
    this.schemaClass = schemaClass;
  }

  private Connector connect(final Class<? extends Schema> schemaClass, final ConnectionFactory connectionFactory, final boolean prepared, final String dataSourceId) {
    logm(logger, TRACE, "%?.connect", "%s,%?,%b,%s", this, schemaClass, connectionFactory, prepared, dataSourceId);
    final String schemaClassNameId = schemaClass.getName() + "<" + dataSourceId + ">";
    Connector connector = schemaClassNameIdToConnector.get(schemaClassNameId);
    if (connector == null)
      schemaClassNameIdToConnector.put(schemaClassNameId, connector = new Connector(schemaClass, dataSourceId));

    connector.set(connectionFactory, prepared);
    return connector;
  }

  public Connector connect(final ConnectionFactory connector) {
    return connect(schemaClass, assertNotNull(connector, "connector == null"), false, null);
  }

  public Connector connect(final ConnectionFactory connector, final String dataSourceId) {
    return connect(schemaClass, assertNotNull(connector, "connector == null"), false, dataSourceId);
  }

  public Connector connect(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, null);
  }

  public Connector connect(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), false, dataSourceId);
  }

  public Connector connectPrepared(final ConnectionFactory connector) {
    return connect(schemaClass, assertNotNull(connector, "connector == null"), true, null);
  }

  public Connector connectPrepared(final ConnectionFactory connector, final String dataSourceId) {
    return connect(schemaClass, assertNotNull(connector, "connector == null"), true, dataSourceId);
  }

  public Connector connectPrepared(final DataSource dataSource) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, null);
  }

  public Connector connectPrepared(final DataSource dataSource, final String dataSourceId) {
    return connect(schemaClass, toConnectionFactory(dataSource), true, dataSourceId);
  }
}