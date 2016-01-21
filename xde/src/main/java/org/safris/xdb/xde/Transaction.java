package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.SQLException;

import org.safris.xdb.xdl.DBVendor;

public class Transaction {
  protected final DBVendor vendor;
  protected final Connection connection;

  public Transaction(final Class<? extends Schema> schema) throws XDEException {
    this.connection = Schema.getConnection(schema);
    this.vendor = Schema.getDBVendor(connection);

    try {
      this.connection.setAutoCommit(false);
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, vendor);
    }
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