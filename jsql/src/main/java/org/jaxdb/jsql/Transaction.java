/* Copyright (c) 2016 JAX-DB
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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

import org.libj.sql.exception.SQLExceptions;

public class Transaction implements AutoCloseable {
  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private final AtomicBoolean inited = new AtomicBoolean(false);
  private volatile boolean closed;

  private Connection connection;

  public Transaction(final Class<? extends Schema> schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  public Transaction(final Class<? extends Schema> schema) {
    this(schema, null);
  }

  public Connection getConnection() throws SQLException {
    if (inited.get())
      return connection;

    synchronized (inited) {
      if (inited.get())
        return connection;

      inited.set(true);
      try {
        return this.connection = Objects.requireNonNull(Schema.getConnection(schema, dataSourceId, false));
      }
      catch (final SQLException e) {
        throw SQLExceptions.toStrongType(e);
      }
    }
  }

  public Class<? extends Schema> getSchemaClass() {
    return this.schema;
  }

  public String getDataSourceId() {
    return this.dataSourceId;
  }

  public boolean commit() throws SQLException {
    if (connection == null)
      return false;

    try {
      connection.commit();
      return true;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  public boolean rollback() throws SQLException {
    if (connection == null)
      return false;

    try {
      connection.rollback();
      return true;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  public boolean rollback(final Throwable t) {
    if (connection == null)
      return false;

    try {
      connection.rollback();
      return true;
    }
    catch (final SQLException e) {
      t.addSuppressed(e);
      return false;
    }
  }

  @Override
  public void close() throws SQLException {
    if (closed)
      return;

    closed = true;
    if (connection == null)
      return;

    try {
      connection.close();
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }
}