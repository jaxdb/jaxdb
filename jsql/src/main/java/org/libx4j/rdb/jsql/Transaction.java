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

import org.libx4j.rdb.jsql.exception.SQLExceptionCatalog;

public final class Transaction implements AutoCloseable {
  private final Class<? extends Schema> schema;
  private volatile Boolean inited = false;

  private Connection connection;

  public Transaction(final Class<? extends Schema> schema) {
    this.schema = schema;
  }

  protected Connection getConnection() throws SQLException {
    if (inited)
      return connection;

    synchronized (inited) {
      if (inited)
        return connection;

      this.connection = Schema.getConnection(schema);
      try {
        this.connection.setAutoCommit(false);
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }

      inited = true;
    }

    return connection;
  }

  public void commit() throws SQLException {
    if (connection == null)
      return;

    try {
      connection.commit();
    }
    catch (final SQLException e) {
      throw SQLExceptionCatalog.lookup(e);
    }
  }

  public void rollback() throws SQLException {
    if (connection == null)
      return;

    try {
      connection.rollback();
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