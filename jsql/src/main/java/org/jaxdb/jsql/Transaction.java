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
import org.jaxdb.vendor.DbVendor;
import org.libj.sql.exception.SQLExceptions;

public class Transaction implements AutoCloseable {
  public enum Isolation {
    READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),
    READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),
    REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),
    SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

    private final int level;

    private Isolation(final int level) {
      this.level = level;
    }

    public int getLevel() {
      return this.level;
    }
  }

  private final Schema schema;
  private final Isolation isolation;
  private DbVendor vendor;
  private boolean closed;
  private int totalCount = 0;

  private Connection connection;
  private Boolean isPrepared;
  private Connector connector;

  private Callbacks callbacks;

  public Transaction(final Schema schema, final Isolation isolation) {
    this.schema = assertNotNull(schema);
    this.isolation = isolation;
  }

  public Transaction(final Schema schema) {
    this(schema, null);
  }

  public Transaction(final Connector connector, final Isolation isolation) {
    this(connector.getSchema(), isolation);
    this.connector = connector;
  }

  public Transaction(final Connector connector) {
    this(connector, null);
  }

  public Schema getSchema() {
    return schema;
  }

  public DbVendor getVendor() throws IOException, SQLException {
    return vendor == null ? vendor = DbVendor.valueOf(getConnection().getMetaData()) : vendor;
  }

  public Connection getConnection() throws IOException, SQLException {
    if (connection != null)
      return connection;

    try {
      connection = getConnector().getConnection(isolation);
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
    return connector == null ? connector = schema.getConnector() : connector;
  }

  protected void addCallbacks(final Callbacks callbacks) {
    if (callbacks != null)
      getCallbacks().merge(callbacks);
  }

  Callbacks getCallbacks() {
    return callbacks == null ? callbacks = new Callbacks() : callbacks;
  }

  private ArrayList<OnNotifyCallbackList> onNotifyCallbackLists;

  protected void incUpdateCount(final int count) {
    if (count > 0)
      totalCount += count;
  }

  protected void onNotify(final OnNotifyCallbackList onNotifyCallbackList) {
    if (onNotifyCallbackList != null) {
      if (onNotifyCallbackLists == null)
        onNotifyCallbackLists = new ArrayList<>();

      onNotifyCallbackLists.add(onNotifyCallbackList);
    }
  }

  public NotifiableBatchResult commit() throws SQLException {
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
  }

  @Override
  public void close() throws IOException, SQLException {
    if (closed)
      return;

    closed = true;

    if (callbacks != null) {
      callbacks.close();
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