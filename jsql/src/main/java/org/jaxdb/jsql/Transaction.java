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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLRecoverableException;
import java.util.ArrayList;

import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableBatchResult;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
import org.jaxdb.vendor.DBVendor;
import org.libj.sql.exception.SQLExceptions;

public class Transaction implements AutoCloseable {
  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private DBVendor vendor;
  private boolean closed;
  private int totalCount = 0;

  private Connection connection;
  private Boolean isPrepared;
  private Connector connector;

  private Callbacks callbacks;

  public Transaction(final Class<? extends Schema> schema, final String dataSourceId) {
    this.schema = schema;
    this.dataSourceId = dataSourceId;
  }

  public Transaction(final Class<? extends Schema> schema) {
    this(schema, null);
  }

  public Transaction(final Connector connector) {
    this(assertNotNull(connector).getSchema().getClass(), connector.getDataSourceId());
    this.connector = connector;
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

  protected boolean isPrepared() {
    return isPrepared == null ? isPrepared = getConnector().isPrepared() : isPrepared;
  }

  protected Connector getConnector() {
    return connector == null ? connector = Database.getConnector(schema, dataSourceId) : connector;
  }

  protected void addCallbacks(final Callbacks callbacks) {
    if (callbacks != null)
      getCallbacks().merge(callbacks);
  }

  Callbacks getCallbacks() {
    return callbacks == null ? callbacks = new Callbacks() : callbacks;
  }

  private ArrayList<OnNotifyCallbackList> onNotifyCallbackLists;

  protected void onExecute(final String sessionId, final int count) {
    if (count > 0)
      totalCount += count;

    if (callbacks != null)
      callbacks.onExecute(sessionId, count);
  }

  protected void onExecute(final String sessionId, final OnNotifyCallbackList onNotifyCallbackList, final int count) {
    onExecute(sessionId, count);
    if (onNotifyCallbackList != null) {
      if (onNotifyCallbackLists == null)
        onNotifyCallbackLists = new ArrayList<>();

      onNotifyCallbackLists.add(onNotifyCallbackList);
    }
  }

  public NotifiableResult commit() throws SQLException {
    if (connection == null)
      throw new SQLRecoverableException("Closed Connection");

    try {
      connection.commit();
      if (callbacks != null)
        callbacks.onCommit(totalCount);

      return new NotifiableBatchResult(totalCount, onNotifyCallbackLists);
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
    finally {
      if (callbacks != null && callbacks.onCommits != null)
        callbacks.onCommits.clear();
    }
  }

  public void rollback() throws SQLException {
    if (connection == null)
      throw new SQLRecoverableException("Closed Connection");

    try {
      connection.rollback();
      if (callbacks != null)
        callbacks.onRollback();
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
    finally {
      if (callbacks != null)
        callbacks.clear();
    }
  }

  public boolean rollback(final Throwable t) {
    if (connection == null) {
      assertNotNull(t).addSuppressed(new SQLRecoverableException("Closed Connection"));
      return false;
    }

    try {
      connection.rollback();
      if (callbacks != null)
        callbacks.onRollback();

      return true;
    }
    catch (final SQLException e) {
      assertNotNull(t).addSuppressed(e);
      return false;
    }
    finally {
      if (callbacks != null)
        callbacks.clear();
    }
  }

  @Override
  public void close() throws IOException, SQLException {
    if (closed)
      return;

    closed = true;

    if (callbacks != null) {
      callbacks.clear();
      callbacks = null;
    }

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