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

import org.libj.lang.ObjectUtil;
import org.openjax.json.JSON;

public final class Notification<T extends data.Table> {
  public abstract static class Action implements Comparable<Action>, Serializable {
    abstract <T extends data.Table> void action(String sessionId, long timestamp, Notification.Listener<T> listener, Map<String,String> keyForUpdate, T row);

    final <T extends data.Table> void invoke(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
      if (!listenerClass.isInstance(listener))
        throw new UnsupportedOperationException("Unsupported action: " + name);

      action(sessionId, timestamp, listener, keyForUpdate, row);
    }

    public static final class INSERT extends Action {
      private INSERT() {
        super("INSERT", "INSERT", (byte)0, Notification.InsertListener.class);
      }

      @Override
      <T extends data.Table> void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((InsertListener<T>)listener).onInsert(sessionId, timestamp, row);
      }
    }

    public static class UP extends Action {
      private static final byte ordinal = 1;

      private UP(final String name) {
        super(name, "UPDATE", ordinal, Notification.UpdateListener.class);
      }

      @Override
      final <T extends data.Table> void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((UpdateListener<T>)listener).onUpdate(sessionId, timestamp, row, keyForUpdate);
      }
    }

    // NOTE: UPDATE and UPGRADE have the same ordinal, so that they cannot both specified in one set of actions
    public static final class UPDATE extends UP {
      private UPDATE() {
        super("UPDATE");
      }
    }

    public static final class UPGRADE extends UP {
      private UPGRADE() {
        super("UPGRADE");
      }
    }

    public static final class DELETE extends Action {
      private DELETE() {
        super("DELETE", "DELETE", (byte)2, Notification.DeleteListener.class);
      }

      @Override
      <T extends data.Table> void action(final String sessionId, final long timestamp, final Notification.Listener<T> listener, final Map<String,String> keyForUpdate, final T row) {
        ((DeleteListener<T>)listener).onDelete(sessionId, timestamp, row);
      }
    }

    static final UP UP = new UP("UP");

    static <T extends data.Table> void onSelect(final Notification.Listener<T> listener, final T row) {
      if (!(listener instanceof Notification.SelectListener))
        throw new UnsupportedOperationException("Unsupported action: SELECT");

      ((SelectListener<T>)listener).onSelect(row);
    }

    public static final INSERT INSERT;
    public static final UPDATE UPDATE;
    public static final UPGRADE UPGRADE;
    public static final DELETE DELETE;

    private static final Action[] values = {
      INSERT = new INSERT(),
      UPDATE = new UPDATE(),
      UPGRADE = new UPGRADE(),
      DELETE = new DELETE()
    };

    public static Action valueOf(final String name) {
      return "INSERT".equals(name) ? INSERT : "UPDATE".equals(name) ? UPDATE : "UPGRADE".equals(name) ? UPGRADE : "DELETE".equals(name) ? DELETE : null;
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

  /**
   * A cache notification {@link Listener} for {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements.
   *
   * @param <T> The {@link data.Table} type parameter to which this {@link Listener} applies.
   */
  public interface DefaultListener<T extends data.Table> extends SelectListener<T>, InsertListener<T>, UpdateListener<T>, DeleteListener<T> {
  }

  /**
   * A cache notification {@link Listener} for {@code SELECT} statements.
   *
   * @param <T> The {@link data.Table} type parameter to which this {@link Listener} applies.
   */
  @FunctionalInterface
  public interface SelectListener<T extends data.Table> extends Listener<T> {
    /**
     * Called when a {@code SELECT} is performed by the caching system to initialize or refresh the cache.
     *
     * @param row The {@link data.Table} which the {@code SELECT} concerns.
     */
    void onSelect(T row);
  }

  /**
   * A cache notification {@link Listener} for {@code INSERT} statements.
   *
   * @param <T> The {@link data.Table} type parameter to which this {@link Listener} applies.
   */
  @FunctionalInterface
  public interface InsertListener<T extends data.Table> extends Listener<T> {
    /**
     * Called when an {@code INSERT} notification is received from the DB for the provided {@link data.Table row}.
     *
     * @param sessionId The id of the SQL session in which the {@code INSERT} was executed.
     * @param timestamp The millisecond timestamp when the DB invoked the notification.
     * @param row The {@link data.Table row} which the {@code INSERT} concerns.
     * @return The {@link data.Table} representing the cached entity of the provided {@link data.Table row}.
     */
    T onInsert(String sessionId, long timestamp, T row);
  }

  /**
   * A cache notification {@link Listener} for {@code UPDATE} statements.
   *
   * @param <T> The {@link data.Table} type parameter to which this {@link Listener} applies.
   */
  @FunctionalInterface
  public interface UpdateListener<T extends data.Table> extends Listener<T> {
    /**
     * Called when an {@code UPDATE} notification is received from the DB for the provided {@link data.Table row}.
     *
     * @param sessionId The id of the SQL session in which the {@code UPDATE} was executed.
     * @param timestamp The millisecond timestamp when the DB invoked the notification.
     * @param row The {@link data.Table row} which the {@code UPDATE} concerns.
     * @param keyForUpdate A map of column names specified in the {@code <jsql:keyForUpdate>} spec of the DDLx schema for the table of
     *          the provided {@link data.Table row} specifying values that must match for the {@code UPDATE} to be applied to the cache
     *          (optimistic locking).
     * @return The {@link data.Table} representing the cached entity of the provided {@link data.Table row}.
     */
    T onUpdate(String sessionId, long timestamp, T row, Map<String,String> keyForUpdate);
  }

  /**
   * A cache notification {@link Listener} for {@code DELETE} statements.
   *
   * @param <T> The {@link data.Table} type parameter to which this {@link Listener} applies.
   */
  @FunctionalInterface
  public interface DeleteListener<T extends data.Table> extends Listener<T> {
    /**
     * Called when an {@code DELETE} notification is received from the DB for the provided {@link data.Table row}.
     *
     * @param sessionId The id of the SQL session in which the {@code DELETE} was executed.
     * @param timestamp The millisecond timestamp when the DB invoked the notification.
     * @param row The {@link data.Table row} which the {@code DELETE} concerns.
     * @return The {@link data.Table} representing the cached entity of the provided {@link data.Table row}.
     */
    T onDelete(String sessionId, long timestamp, T row);
  }

  public interface Listener<T extends data.Table> {
    /**
     * Called when a new {@link Connection} is established for the context of a {@link type.Table$}.
     *
     * @param connection The {@link Connection}.
     * @param table The {@link type.Table$}.
     * @throws IOException If an I/O error has occurred.
     * @throws SQLException If a SQL error has occurred.
     */
    default void onConnect(Connection connection, T table) throws IOException, SQLException {
    }

    /**
     * Called when an unhandled failure is encountered.
     *
     * @param sessionId The session ID.
     * @param timestamp The timestamp (in microseconds) of the NOTIFY invocation.
     * @param table The {@link type.Table$}.
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

  @Override
  public String toString() {
    return "{sessionId:\"" + sessionId + "\",timestamp:" + timestamp + ",listener:\"" + ObjectUtil.simpleIdentityString(listener) + "\",action:\"" + action + "\",keyForUpdate:\"" + JSON.toString(keyForUpdate) + "\",row:" + row + "}";
  }
}