package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.SQLException;

import org.safris.xdb.xdl.DBVendor;

public class Transaction {
  private final Class<? extends Schema> schema;
  private volatile Boolean inited = false;

  private DBVendor vendor;
  private Connection connection;

  public Transaction(final Class<? extends Schema> schema) {
    this.schema = schema;
  }

  protected Connection getConnection() throws XDEException {
    if (!inited) {
      synchronized (inited) {
        if (!inited) {
          this.connection = Schema.getConnection(schema);
          this.vendor = Schema.getDBVendor(connection);
          try {
            this.connection.setAutoCommit(false);
          }
          catch (final SQLException e) {
            throw XDEException.lookup(e, vendor);
          }

          inited = true;
        }
      }
    }

    return connection;
  }

  public void commit() throws XDEException {
    try {
      connection.commit();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, vendor);
    }
  }

  public void rollback() throws XDEException {
    try {
      connection.rollback();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, vendor);
    }
  }

  public void close() throws XDEException {
    try {
      connection.close();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, vendor);
    }
  }
}