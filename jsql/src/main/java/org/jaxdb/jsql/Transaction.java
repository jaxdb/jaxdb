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
import java.util.Collection;
import java.util.List;

import org.jaxdb.jsql.Listener.OnCommit;
import org.jaxdb.jsql.Listener.OnExecute;
import org.jaxdb.jsql.Listener.OnNotifies;
import org.jaxdb.jsql.Listener.OnNotifyListener;
import org.jaxdb.jsql.Listener.OnRollback;
import org.jaxdb.vendor.DBVendor;
import org.libj.sql.exception.SQLExceptions;

public class Transaction implements AutoCloseable {
  public static abstract class Event<L,T> {
    public static final Event<OnExecute,List<OnExecute>> EXECUTE;
    public static final Event<OnCommit,List<OnCommit>> COMMIT;
    public static final Event<OnRollback,List<OnRollback>> ROLLBACK;
    public static final Event<OnNotifyListener,OnNotifies> NOTIFY;

    private static byte index = 0;

    private static final Event<?,?>[] values = {
      EXECUTE = new Event<OnExecute,List<OnExecute>>("EXECUTE") {
        @Override
        void add(final Listener listeners, final String sessionId, final OnExecute listener) {
          listeners.getExecute().add(assertNotNull(listener));
        }

        @Override
        void notify(final Listener listeners, final String sessionId, final Throwable t, final int count) {
          if (listeners.execute != null)
            for (final OnExecute listener : listeners.execute)
              listener.accept(count);
        }
      },
      COMMIT = new Event<OnCommit,List<OnCommit>>("COMMIT") {
        @Override
        void add(final Listener listeners, final String sessionId, final OnCommit listener) {
          listeners.getCommit().add(assertNotNull(listener));
        }

        @Override
        void notify(final Listener listeners, final String sessionId, final Throwable t, final int count) {
          if (listeners.commit != null)
            for (final OnCommit listener : listeners.commit)
              listener.accept(count);
        }
      },
      ROLLBACK = new Event<OnRollback,List<OnRollback>>("ROLLBACK") {
        @Override
        void add(final Listener listeners, final String sessionId, final OnRollback listener) {
          listeners.getRollback().add(assertNotNull(listener));
        }

        @Override
        void notify(final Listener listeners, final String sessionId, final Throwable t, final int count) {
          if (listeners.rollback != null)
            for (final OnRollback listener : listeners.rollback)
              listener.run();
        }
      },
      NOTIFY = new Event<OnNotifyListener,OnNotifies>("NOTIFY") {
        @Override
        void add(final Listener listeners, final String sessionId, final OnNotifyListener listener) {
          listeners.getNotify().add(sessionId, listener);
        }

        @Override
        void notify(final Listener listeners, final String sessionId, final Throwable t, final int count) {
          if (listeners.notify != null) {
            final Collection<OnNotifyListener> sessionListeners = listeners.notify.remove(sessionId);
            if (sessionListeners != null)
              for (final OnNotifyListener listener : sessionListeners)
                listener.accept(t);
          }
        }
      }
    };

    public static Event<?,?>[] values() {
      return values;
    }

    private final byte ordinal;
    private final String name;

    private Event(final String name) {
      this.ordinal = index++;
      this.name = name;
    }

    abstract void add(Listener listeners, String sessionId, L listener);
    abstract void notify(Listener listeners, String sessionId, Throwable t, int count);

    public byte ordinal() {
      return ordinal;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  private final Class<? extends Schema> schema;
  private final String dataSourceId;
  private DBVendor vendor;
  private boolean closed;
  private int totalCount = 0;

  private Connection connection;
  private Boolean isPrepared;
  private Connector connector;

  private Listener listeners;

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

  protected void setListeners(final Listener listeners) {
    this.listeners = listeners;
  }

  protected void purgeListeners() {
    if (listeners != null)
      listeners.clear();
  }

  Listener getListeners() {
    return listeners == null ? listeners = new Listener() : listeners;
  }

  protected void onExecute(final String sessionId, final int count) {
    totalCount += count;
    if (listeners != null)
      listeners.onExecute(sessionId, count);
  }

  private void onCommit() throws SQLException {
    if (listeners != null) {
      listeners.onCommit(totalCount);
      if (listeners.notify != null)
        for (final OnNotifies onNotifies : listeners.notify.values())
          onNotifies.await();
    }
  }

  private void onRollback() {
    if (listeners != null)
      listeners.onRollback();
  }

  public boolean commit() throws SQLException {
    if (connection == null)
      return false;

    try {
      connection.commit();
      onCommit();
      return true;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
    finally {
      purgeListeners();
      totalCount = 0;
    }
  }

  public boolean rollback() throws SQLException {
    if (connection == null)
      return false;

    try {
      connection.rollback();
      onRollback();
      return true;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
    finally {
      purgeListeners();
      totalCount = 0;
    }
  }

  public boolean rollback(final Throwable t) {
    if (connection == null)
      return false;

    try {
      connection.rollback();
      onRollback();
      return true;
    }
    catch (final SQLException e) {
      assertNotNull(t).addSuppressed(e);
      return false;
    }
    finally {
      purgeListeners();
      totalCount = 0;
    }
  }

  @Override
  public void close() throws IOException, SQLException {
    if (closed)
      return;

    closed = true;
    purgeListeners();
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
      totalCount = 0;
    }
  }
}