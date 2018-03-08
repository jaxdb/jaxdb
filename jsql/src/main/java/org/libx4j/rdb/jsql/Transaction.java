/* Copyright (c) 2016 lib4j
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

package org.libx4j.rdb.jsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicBoolean;

import org.lib4j.sql.exception.SQLExceptionCatalog;

public final class Transaction implements AutoCloseable {
  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private final AtomicBoolean inited = new AtomicBoolean(false);

  private Connection connection;

  public Transaction(final Class<? extends Schema> schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  public Transaction(final Class<? extends Schema> schema) {
    this(schema, null);
  }

  protected Connection getConnection() throws SQLException {
    if (inited.get())
      return connection;

    synchronized (inited) {
      if (inited.get())
        return connection;

      this.connection = Schema.getConnection(schema, dataSourceId);
      try {
        this.connection.setAutoCommit(false);
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }

      inited.set(true);
    }

    return connection;
  }

  protected String getDataSourceId() {
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
      throw SQLExceptionCatalog.lookup(e);
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
      throw SQLExceptionCatalog.lookup(e);
    }
  }

  @Override
  public void close() throws SQLException {
    if (connection == null)
      return;

    try {
      connection.close();
    }
    catch (final SQLException e) {
      throw SQLExceptionCatalog.lookup(e);
    }
  }
}