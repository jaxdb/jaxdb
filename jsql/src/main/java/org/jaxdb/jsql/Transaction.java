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
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;

import org.libj.sql.exception.SQLExceptions;

public class Transaction implements AutoCloseable {
  public enum Event {
    EXECUTE,
    COMMIT,
    ROLLBACK
  }

  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private boolean closed;

  private Connection connection;
  private ArrayList<Consumer<Event>> listeners;

  public Transaction(final Class<? extends Schema> schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  public Transaction(final Class<? extends Schema> schema) {
    this(schema, null);
  }

  public Connection getConnection() throws SQLException {
    if (connection != null)
      return connection;

    try {
      return this.connection = Objects.requireNonNull(Schema.getConnection(schema, dataSourceId, false));
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  public Class<? extends Schema> getSchemaClass() {
    return this.schema;
  }

  public String getDataSourceId() {
    return this.dataSourceId;
  }

  private void notifyListeners(final Event event) {
    if (this.listeners != null) {
      for (final Consumer<Event> listener : this.listeners)
        listener.accept(event);

      this.listeners.clear();
    }
  }

  protected void addListener(final Consumer<Event> listener) {
    Objects.requireNonNull(listener);
    if (this.listeners == null)
      this.listeners = new ArrayList<>();

    this.listeners.add(listener);
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