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

import static org.jaxdb.jsql.DML.*;
import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.jsql.data.Except;
import org.jaxdb.jsql.keyword.Select.Entity.SELECT;
import org.libj.lang.ObjectUtil;
import org.libj.sql.AuditConnection;
import org.openjax.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class DefaultCache implements Notification.DefaultListener<data.Table<?>> {
  private static final Logger logger = LoggerFactory.getLogger(DefaultCache.class);

  private final Connector connector;

  public DefaultCache(final Connector connector) {
    this.connector = assertNotNull(connector);
  }

  protected DefaultCache() {
    this.connector = null;
  }

  protected Connector getConnector() {
    return connector;
  }

  @SuppressWarnings("unchecked")
  protected static Map<data.Key,data.Table<?>> getCache(final data.Table<?> table) {
    return (Map<data.Key,data.Table<?>>)table.getCache();
  }

  private static data.Table<?> insert(final Map<data.Key,data.Table<?>> cache, final data.Table<?> entity) {
    cache.put(entity.getKey().immutable(), entity);
    entity._commitInsert$();
    entity._commitEntity$();
    return entity;
  }

  @SuppressWarnings("unchecked")
  private static data.Table<?> update(final data.Table<?> entity, final data.Table<?> update) {
    ((data.Table)entity).merge(update);
    entity._commitUpdate$();
    entity._commitEntity$();
    return entity;
  }

  private static boolean isUpToDate(final data.Table<?> entity, final data.Table<?> update) {
    assertNotNull(entity);
    assertNotNull(update);

    for (final data.Column<?> cu : update.getColumns()) { // [A]
      if (!cu.primary && cu.setByCur == SetBy.USER) {
        final data.Column<?> ce = entity.getColumn(cu.getName());
        if (!ce.equals(cu))
          return false;
      }
    }

    return true;
  }

  @Override
  public void onConnect(final Connection connection, final data.Table table) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".onConnect(\"" + table.getName() + "\")");

    if (table._column$.length == 0)
      return;

    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table)
        .execute(connection)) {
      while (rows.nextRow())
        onInsert(null, -1, rows.nextEntity());
    }
  }

  @Override
  public void onFailure(final String sessionId, final long timestamp, final data.Table<?> table, final Exception e) {
    if (sessionId != null) {
      final Schema schema = getConnector().getSchema();
      final OnNotifyCallbackList onNotifyCallbackList = schema.getSession(sessionId);
      if (onNotifyCallbackList != null && onNotifyCallbackList.test(e))
        schema.removeSession(sessionId);
    }
  }

  @Override
  public data.Table<?> onInsert(final String sessionId, final long timestamp, final data.Table<?> row) {
    assertNotNull(row);
    Exception exception = null;
    try {
      if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".onInsert(" + (sessionId != null ? "\"" + sessionId + "\"," + timestamp + "," : "") + "<\"" + row.getName() + "\"|" + ObjectUtil.simpleIdentityString(row) + ">:" + row + ")");

      final Map<data.Key,data.Table<?>> cache = getCache(row);
      final data.Table<?> entity = cache.get(row.getKey());
      if (entity == null)
        return insert(cache, row.clone(false));

      if (entity.equals(row))
        return entity;

      return update(entity, row);
    }
    catch (final Exception e) {
      exception = e;
      throw e;
    }
    finally {
      if (sessionId != null) {
        final Schema schema = getConnector().getSchema();
        final OnNotifyCallbackList onNotifyCallbackList = schema.getSession(sessionId);
        if (onNotifyCallbackList != null && onNotifyCallbackList.test(exception))
          schema.removeSession(sessionId);
      }
    }
  }

  @Override
  public data.Table<?> onUpdate(final String sessionId, final long timestamp, final data.Table<?> row, final Map<String,String> keyForUpdate) {
    assertNotNull(row);
    Exception exception = null;
    try {
      if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".onUpdate(\"" + sessionId + "\"," + timestamp + ",<\"" + row.getName() + "\"|" + ObjectUtil.simpleIdentityString(row) + ">:" + row + "," + JSON.toString(keyForUpdate) + ")");

      final Map<data.Key,data.Table<?>> cache = getCache(row);
      data.Table<?> entity;
      if (row.getKeyOld().equals(row.getKey())) {
        entity = cache.get(row.getKey());
        if (entity == null)
          return keyForUpdate != null ? refreshRow(row) : insert(cache, row.clone(false));
      }
      else {
        entity = cache.remove(row.getKeyOld());
        if (entity != null) {
          cache.put(row.getKey().immutable(), entity);
        }
        else {
          entity = cache.get(row.getKey());
          if (entity == null)
            return keyForUpdate != null ? refreshRow(row) : insert(cache, row.clone(false));
        }
      }

      if (keyForUpdate != null) {
        for (final Map.Entry<String,String> entry : keyForUpdate.entrySet()) { // [S]
          final data.Column<?> column = entity.getColumn(entry.getKey());
          if (column == null)
            throw new IllegalArgumentException("Table " + row.getName() + " does not have column \"" + entry.getKey() + "\"");

          if (entry.getValue() == null ? column.get() != null : column.get() == null || !entry.getValue().equals(column.get().toString()))
            return isUpToDate(entity, row) ? entity : null;
        }
      }

      return update(entity, row);
    }
    catch (final Exception e) {
      exception = e;
      throw e;
    }
    finally {
      if (sessionId != null) {
        final Schema schema = getConnector().getSchema();
        final OnNotifyCallbackList onNotifyCallbackList = schema.getSession(sessionId);
        if (onNotifyCallbackList != null && onNotifyCallbackList.test(exception))
          schema.removeSession(sessionId);
      }
    }
  }

  @Override
  public data.Table<?> onDelete(final String sessionId, final long timestamp, final data.Table<?> row) {
    assertNotNull(row);
    Exception exception = null;
    try {
      if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".onDelete(\"" + sessionId + "\"," + timestamp + ",<\"" + row.getName() + "\"|" + ObjectUtil.simpleIdentityString(row) + ">:" + row + ")");

      final data.Table<?> entity = getCache(row).remove(row.getKey());
      if (entity == null)
        return null;

      entity._commitDelete$();
      entity._commitEntity$();
      return entity;
    }
    catch (final Exception e) {
      exception = e;
      throw e;
    }
    finally {
      if (sessionId != null) {
        final Schema schema = getConnector().getSchema();
        final OnNotifyCallbackList onNotifyCallbackList = schema.getSession(sessionId);
        if (onNotifyCallbackList != null && onNotifyCallbackList.test(exception))
          schema.removeSession(sessionId);
      }
    }
  }

  protected void delete(final data.Table<?> row) {
    final Map<data.Key,data.Table<?>> cache = getCache(row);
    final data.MutableKey key = row.getKey();
    if (key != null)
      cache.remove(key);
    else
      cache.clear();
  }

  /**
   * Refreshes the provided {@link data.Table} by performing a {@code SELECT} to the DB based on the {@code row}'s primary key.
   *
   * @implNote It is important that this method is not called in the same thread at that invoking
   *           {@link Notifier#notify(String,String)}. Doing so may result in a deadlock.
   * @param row The row to refresh.
   * @return The refreshed row.
   */
  @SuppressWarnings("unchecked")
  protected data.Table<?> refreshRow(final data.Table<?> row) {
    // FIXME: This approach ends up mutating the provided row
    row.reset(Except.PRIMARY_KEY);
    selectRow(row);

    data.Table entity = getCache(row).get(row.getKey());
    if (entity == null) {
      getCache(row).put(row.getKey().immutable(), entity = row.clone(false));
      entity._commitInsert$();
    }
    else {
      entity.merge(row);
      entity._commitUpdate$();
    }

    entity._commitEntity$();

    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".refreshRow(<\"" + row.getName() + "\"|" + ObjectUtil.simpleIdentityString(row) + ">:" + row + ") -> " + ObjectUtil.simpleIdentityString(entity) + ": " + entity);

    return entity;
  }

  protected void selectRow(final data.Table row) {
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".selectRow(<\"" + row.getName() + "\"|" + ObjectUtil.simpleIdentityString(row) + ">:" + row + ") -> " + ObjectUtil.simpleIdentityString(row) + ": " + row.toString(true));

    try (final Connection connection = getConnector().getConnection()) {
      try (final RowIterator<?> rows =
        SELECT(row)
          .execute(connection)) {

        if (!rows.nextRow())
          throw new IllegalStateException("Expected a row");

        rows.nextEntity();

        if (rows.nextRow())
          throw new IllegalStateException("Did not expect another row");
      }
      catch (final SQLException e) {
        if (logger.isWarnEnabled()) logger.warn("selectRow(): connection.isClosed() = " + (connection != null ? AuditConnection.isClosed(connection) : "null"), e);
      }
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled()) logger.warn(e.getMessage(), e);
    }
    catch (final IOException ie) {
      throw new UncheckedIOException(ie);
    }
  }

  public void refreshTables(final String sessionId, final long timestamp, final data.Table<?> ... tables) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".refreshTables(\"" + sessionId + "\"," + timestamp + "," + Arrays.stream(tables).map(t -> t.getName()).collect(Collectors.joining(",")) + ")");

    assertNotNull(tables);
    for (final data.Table table : tables) { // [A]
      try (final RowIterator<? extends data.Table> rows =
        SELECT(table).
        FROM(table)
          .execute()) {
        while (rows.nextRow()) {
          onInsert(sessionId, timestamp, rows.nextEntity());
        }
      }
    }
  }

  public void refreshTables(final String sessionId, final long timestamp, final SELECT<?> ... selects) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace(getClass().getSimpleName() + ".refreshTables(\"" + sessionId + "\"," + timestamp + ",[" + selects.length + "])");

    assertNotNull(selects);
    for (final SELECT select : selects) { // [A]
      try (final RowIterator<? extends data.Table> rows =
        select.execute()) {
        while (rows.nextRow())
          onInsert(sessionId, timestamp, rows.nextEntity());
      }
    }
  }
}