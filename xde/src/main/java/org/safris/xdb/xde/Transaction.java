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