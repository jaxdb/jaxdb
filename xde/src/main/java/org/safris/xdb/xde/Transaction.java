/* Copyright (c) 2016 Seva Safris
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

package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction {
  private final Class<? extends Schema> schema;
  private volatile Boolean inited = false;

  private Connection connection;

  public Transaction(final Class<? extends Schema> schema) {
    this.schema = schema;
  }

  protected Connection getConnection() throws XDEException {
    if (!inited) {
      synchronized (inited) {
        if (!inited) {
          this.connection = Schema.getConnection(schema);
          try {
            this.connection.setAutoCommit(false);
          }
          catch (final SQLException e) {
            throw XDEException.lookup(e, connection != null ? Schema.getDBVendor(connection) : null);
          }

          inited = true;
        }
      }
    }

    return connection;
  }

  public void commit() throws XDEException {
    if (connection == null)
      return;

    try {
      connection.commit();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, connection != null ? Schema.getDBVendor(connection) : null);
    }
  }

  public void rollback() throws XDEException {
    if (connection == null)
      return;

    try {
      connection.rollback();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, connection != null ? Schema.getDBVendor(connection) : null);
    }
  }

  public void close() throws XDEException {
    if (connection == null)
      return;

    try {
      connection.close();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, connection != null ? Schema.getDBVendor(connection) : null);
    }
  }
}