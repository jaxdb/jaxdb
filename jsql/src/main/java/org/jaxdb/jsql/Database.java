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

import java.sql.Connection;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Database {
  private static final Logger logger = LoggerFactory.getLogger(Database.class);
  static final ConcurrentHashMap<Schema,ConcurrentNullHashMap<String,Database>> schemaClassToLocalGlobal = new ConcurrentHashMap<>();

  public static Database get(final Schema schema, final String dataSourceId) {
    ConcurrentNullHashMap<String,Database> localGlobal = schemaClassToLocalGlobal.get(schema);
    Database database;
    if (localGlobal == null)
      schemaClassToLocalGlobal.put(schema, localGlobal = new ConcurrentNullHashMap<>(2));
    else if ((database = localGlobal.get(dataSourceId)) != null)
      return database;

    localGlobal.put(dataSourceId, database = new Database(schema, dataSourceId));
    return database;
  }

  public static Database get(final Schema schema) {
    return get(schema, null);
  }

  private static ConnectionFactory toConnectionFactory(final DataSource dataSource) {
    assertNotNull(dataSource, "dataSource is null");
    return (final Transaction.Isolation isolation) -> {
      final Connection connection = dataSource.getConnection();
      if (isolation != null)
        connection.setTransactionIsolation(isolation.getLevel());

      return AuditConnection.wrapIfDebugEnabled(connection);
    };
  }

  static Connector getConnector(final Schema schema, final String dataSourceId) {
    final ConcurrentNullHashMap<String,Database> dataSourceIdToConnector = schemaClassToLocalGlobal.get(schema);
    if (dataSourceIdToConnector == null)
      return null;

    final Database database = dataSourceIdToConnector.get(dataSourceId);
    return database == null ? null : database.getConnector();
  }

  final Schema schema;
  final String dataSourceId;
  private Connector connector;

  Database(final Schema schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  Connector getConnector() {
    return connector;
  }

  private Connector connect(final ConnectionFactory connectionFactory, final boolean isPrepared) {
    logm(logger, TRACE, "%?.connect", "%s,%s,%b", this, schema, connectionFactory, isPrepared);
    return connector == null ? connector = newConnector(connectionFactory, isPrepared) : connector;
  }

  Connector newConnector(final ConnectionFactory connectionFactory, final boolean isPrepared) {
    return new Connector(schema, dataSourceId, connectionFactory, isPrepared);
  }

  public Connector connect(final ConnectionFactory connectionFactory) {
    return connect(connectionFactory, false);
  }

  public Connector connect(final DataSource dataSource) {
    return connect(toConnectionFactory(dataSource), false);
  }

  public Connector connectPrepared(final ConnectionFactory connectionFactory) {
    return connect(connectionFactory, true);
  }

  public Connector connectPrepared(final DataSource dataSource) {
    return connect(toConnectionFactory(dataSource), true);
  }

  public boolean isConnected() {
    return connector != null;
  }

  public Boolean isPrepared() {
    return connector == null ? null : connector.isPrepared();
  }
}