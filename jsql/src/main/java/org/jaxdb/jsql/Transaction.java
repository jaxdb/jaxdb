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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.function.Consumer;

import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Assertions;
import org.libj.sql.exception.SQLExceptions;
import org.libj.sql.exception.SQLInvalidSchemaNameException;

public class Transaction implements AutoCloseable {
  public enum Event {
    EXECUTE,
    COMMIT,
    ROLLBACK,
    CLOSE
  }

  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private DBVendor vendor;
  private boolean closed;

  private Connection connection;
  private Boolean isPrepared;
  private Connector connector;

  private ArrayList<Consumer<Event>> listeners;

  public Transaction(final Class<? extends Schema> schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  public Transaction(final Class<? extends Schema> schema) {
    this(schema, null);
  }

  public Class<? extends Schema> getSchemaClass() {
    return schema;
  }

  public String getDataSourceId() {
    return dataSourceId;
  }

  public DBVendor getVendor() throws IOException, SQLException {
    return vendor == null ? vendor = DBVendor.valueOf(getConnection().getMetaData()) : vendor;
  }

  public Connection getConnection() throws IOException, SQLException {
    if (connection != null)
      return connection;

    try {
      connection = getConnector().getConnection();
      connection.setAutoCommit(false);
      return connection;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  protected boolean isPrepared() throws SQLInvalidSchemaNameException {
    return isPrepared == null ? isPrepared = getConnector().isPrepared() : isPrepared;
  }

  protected Connector getConnector() throws SQLInvalidSchemaNameException {
    if (connector != null)
      return connector;

    connector = Database.getConnector(schema, dataSourceId);
    if (connector == null)
      throw new SQLInvalidSchemaNameException("No " + ConnectionFactory.class.getName() + " registered for " + (schema == null ? null : schema.getName()) + ", id: " + dataSourceId);

    return connector;
  }

  private void notifyListeners(final Event event) {
    if (listeners != null) {
      for (final Consumer<Event> listener : listeners)
        listener.accept(event);

      listeners.clear();
    }
  }

  protected void addListener(final Consumer<Event> listener) {
    if (listeners == null)
      listeners = new ArrayList<>();

    listeners.add(Assertions.assertNotNull(listener));
  }

  public boolean commit() throws SQLException {
    if (connection == null)
      return false;

    try {
      connection.commit();
      notifyListeners(Event.COMMIT);
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
      notifyListeners(Event.ROLLBACK);
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
      notifyListeners(Event.ROLLBACK);
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
    notifyListeners(Event.CLOSE);

    listeners = null;
    connector = null;
    if (connection == null)
      return;

    try {
      connection.close();
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
    finally {
      connection = null;
    }
  }
}