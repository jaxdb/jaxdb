/* Copyright (c) 2021 JAX-DB
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
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.jaxdb.jsql.Database.OnConnectPreLoad;
import org.libj.util.Interval;

public final class Notification<T extends data.Table> {
  public abstract static class Action implements Comparable<Action>, Serializable {
    abstract <T extends data.Table>void action(String sessionId, long timestamp, Notification.Listener<T> listener, Map<String,String> keyForUpdate, T row);

    final <T extends data.Table>void invoke(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
      if (!listenerClass.isInstance(listener))
        throw new UnsupportedOperationException("Unsupported action: " + name);

      action(sessionId, timestamp, listener, keyForUpdate, row);
    }

    public static final class SELECT extends Action {
      private SELECT() {
        super("SELECT", "SELECT", (byte)0, Notification.SelectListener.class);
      }

      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        throw new UnsupportedOperationException();
      }

      <T extends data.Table>void onSelect(final Notification.Listener<T> listener, final T row, final boolean addKey) {
        if (!super.listenerClass.isInstance(listener))
          throw new UnsupportedOperationException("Unsupported action: " + super.name);

        ((SelectListener<T>)listener).onSelect(row, addKey);
      }

      @SuppressWarnings("unchecked")
      <T extends data.Table>void onSelectRange(final Notification.Listener<T> listener, final T table, final Interval<data.Key> ... intervals) {
        if (!super.listenerClass.isInstance(listener))
          throw new UnsupportedOperationException("Unsupported action: " + super.name);

        ((SelectListener<T>)listener).onSelectRange(table, intervals);
      }
    }

    public static final class INSERT extends Action {
      private INSERT() {
        super("INSERT", "INSERT", (byte)0, Notification.InsertListener.class);
      }

      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((InsertListener<T>)listener).onInsert(sessionId, timestamp, row);
      }
    }

    public abstract static class UP extends Action {
      private static final byte ordinal = 1;

      private UP(final String name) {
        super(name, "UPDATE", ordinal, Notification.UpdateListener.class);
      }

      private UP() {
        super("UP", "UPDATE", ordinal, Notification.UpdateListener.class);
      }
    }

    // NOTE: UPDATE and UPGRADE have the same ordinal, so that they cannot both specified alongside each other
    public static final class UPDATE extends UP {
      private UPDATE() {
        super("UPDATE");
      }

      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((UpdateListener<T>)listener).onUpdate(sessionId, timestamp, row, null);
      }
    }

    public static final class UPGRADE extends UP {
      private UPGRADE() {
        super("UPGRADE");
      }

      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((UpdateListener<T>)listener).onUpdate(sessionId, timestamp, row, keyForUpdate);
      }
    }

    public static final class DELETE extends Action {
      private DELETE() {
        super("DELETE", "DELETE", (byte)2, Notification.DeleteListener.class);
      }

      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((DeleteListener<T>)listener).onDelete(sessionId, timestamp, row);
      }
    }

    public static final SELECT SELECT;
    public static final INSERT INSERT;
    static final UP UP = new UP() {
      @Override
      <T extends data.Table>void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        throw new UnsupportedOperationException();
      }
    };
    public static final UPDATE UPDATE;
    public static final UPGRADE UPGRADE;
    public static final DELETE DELETE;

    private static final Action[] values = {
      SELECT = new SELECT(),
      INSERT = new INSERT(),
      UPDATE = new UPDATE(),
      UPGRADE = new UPGRADE(),
      DELETE = new DELETE()
    };

    public static Action valueOf(final String name) {
      return "SELECT".equals(name) ? SELECT : "INSERT".equals(name) ? INSERT : "UPDATE".equals(name) ? UPDATE : "UPGRADE".equals(name) ? UPGRADE : "DELETE".equals(name) ? DELETE : null;
    }

    public static Action[] values() {
      return values;
    }

    private final byte ordinal;
    private final String name;
    private final String sql;
    @SuppressWarnings("rawtypes")
    private final Class<? extends Notification.Listener> listenerClass;

    @SuppressWarnings("rawtypes")
    private Action(final String name, final String sql, final byte ordinal, final Class<? extends Notification.Listener> listenerClass) {
      this.ordinal = ordinal;
      this.name = name;
      this.sql = sql;
      this.listenerClass = listenerClass;
    }

    public byte ordinal() {
      return ordinal;
    }

    @Override
    public int compareTo(final Action o) {
      return name.compareTo(o.name);
    }

    public String toSql() {
      return sql;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public interface DefaultListener<T extends data.Table> extends SelectListener<T>, InsertListener<T>, UpdateListener<T>, DeleteListener<T> {
  }

  public interface SelectListener<T extends data.Table> extends Listener<T> {
    void onSelect(T row, boolean addKey);
    @SuppressWarnings("unchecked")
    void onSelectRange(T row, Interval<data.Key> ... intervals);
  }

  @FunctionalInterface
  public interface InsertListener<T extends data.Table> extends Listener<T> {
    T onInsert(String sessionId, long timestamp, T row);
  }

  @FunctionalInterface
  public interface UpdateListener<T extends data.Table> extends Listener<T> {
    T onUpdate(String sessionId, long timestamp, T row, Map<String,String> keyForUpdate);
  }

  @FunctionalInterface
  public interface DeleteListener<T extends data.Table> extends Listener<T> {
    T onDelete(String sessionId, long timestamp, T row);
  }

  public interface Listener<T extends data.Table> {
    /**
     * Called when a new {@link Connection} is established for the context of a {@link data.Table}.
     *
     * @param connection The {@link Connection}.
     * @param table The {@link data.Table}.
     * @param onConnectPreLoad The {@link OnConnectPreLoad}.
     * @throws IOException If an I/O error has occurred.
     * @throws SQLException If a SQL error has occurred.
     */
    default void onConnect(Connection connection, T table, OnConnectPreLoad onConnectPreLoad) throws IOException, SQLException {
    }

    /**
     * Called when an unhandled failure is encountered.
     *
     * @param sessionId The session ID.
     * @param timestamp The timestamp (in microseconds) of the NOTIFY invocation.
     * @param table The {@link data.Table}.
     * @param e The unhandled {@link Exception}.
     */
    default void onFailure(String sessionId, long timestamp, T table, Exception e) {
    }
  }

  private final Notification.Listener<T> listener;
  private final Action action;
  private final Map<String,String> keyForUpdate;
  private final String sessionId;
  private final long timestamp;
  private final T row;

  Notification(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Action action, final Map<String,String> keyForUpdate, final T row) {
    this.sessionId = sessionId;
    this.timestamp = timestamp;
    this.listener = listener;
    this.action = action;
    this.keyForUpdate = keyForUpdate;
    this.row = row;
  }

  void invoke() {
    action.invoke(sessionId, timestamp, listener, keyForUpdate, row);
  }
}