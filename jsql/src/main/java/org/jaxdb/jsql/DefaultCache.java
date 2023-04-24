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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.keyword.Select.Entity.SELECT;
import org.libj.lang.ObjectUtil;
import org.openjax.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class DefaultCache implements Notification.DefaultListener<data.Table> {
  private static final Logger logger = LoggerFactory.getLogger(DefaultCache.class);

  protected static String log(final String sessionId, final long timestamp) {
    return sessionId + "," + timestamp;
  }

  protected static String log(final data.Table entity) {
    return "<" + ObjectUtil.simpleIdentityString(entity) + ">\"" + entity.getName() + "\":" + entity;
  }

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

  protected static <T extends data.Table>OneToOneMap<T> getCache(final T table) {
    return table.getCache();
  }

  protected void onNotifyCallbacks(final String sessionId, final Exception e) {
    if (sessionId != null) {
      final Schema schema = getConnector().getSchema();
      final OnNotifyCallbackList onNotifyCallbackList = schema.getSession(sessionId);
      if (onNotifyCallbackList != null)
        onNotifyCallbackList.accept(schema, e);
    }
  }

  private static data.Table insert(final data.Table entity) {
    if (logger.isTraceEnabled()) logger.trace("insert(" + log(entity) + ")");
    entity._commitInsert$();
    entity._commitEntity$();
    return entity;
  }

  private static data.Table update(final data.Table entity, final data.Table update) {
    if (logger.isTraceEnabled()) logger.trace("update(" + log(entity) + "," + log(update) + ")");
    entity.merge(update);
    entity._commitUpdate$();
    entity._commitEntity$();
    return entity;
  }

  @Override
  public void onConnect(final Connection connection, final data.Table table) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace("onConnect(" + ObjectUtil.simpleIdentityString(connection) + ",\"" + table.getName() + "\")");
    if (table._column$.length == 0)
      return;

    final OneToOneMap<? extends data.Table> cache = getCache(table);
    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table)
        .execute(connection)) {
      while (rows.nextRow())
        onSelectInsert(cache, null, -1, rows.nextEntity());
    }
  }

  @Override
  public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
    if (logger.isTraceEnabled()) logger.trace("onFailure(" + log(sessionId, timestamp) + ",\"" + table.getName() + "\")", e);
    onNotifyCallbacks(sessionId, e);
  }

  @Override
  public data.Table onSelect(final data.Table row) {
    if (logger.isTraceEnabled()) logger.trace("onSelect(" + log(row) + ")");
    return onSelectInsert(getCache(row), null, -1, row);
  }

  @Override
  public data.Table onInsert(final String sessionId, final long timestamp, final data.Table row) {
    if (logger.isTraceEnabled()) logger.trace("onInsert(" + log(sessionId, timestamp) + "," + log(row) + ")");
    return onSelectInsert(getCache(row), sessionId, timestamp, row);
  }

  protected data.Table onSelectInsert(final OneToOneMap<? extends data.Table> cache, final String sessionId, final long timestamp, final data.Table row) {
    Exception exception = null;
    try {
      final data.Table entity = cache.get(row.getKey());
      if (entity == null)
        return insert(row.clone(false));

      if (entity.equals(row))
        return entity;

      return update(entity, row);
    }
    catch (final Exception e) {
      exception = e;
      throw e;
    }
    finally {
      onNotifyCallbacks(sessionId, exception);
    }
  }

  @Override
  public data.Table onUpdate(final String sessionId, final long timestamp, final data.Table row, final Map<String,String> keyForUpdate) {
    try {
      return onUpdate(getCache(row), sessionId, timestamp, row, keyForUpdate);
    }
    catch (final IOException | SQLException e) {
      if (logger.isErrorEnabled()) logger.error(log(sessionId, timestamp) + "," + log(row) + "," + JSON.toString(keyForUpdate), e);
      return null;
    }
  }

  protected <T extends data.Table>data.Table onUpdate(final OneToOneMap<T> cache, final String sessionId, final long timestamp, final T row, final Map<String,String> keyForUpdate) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace("onUpdate(" + ObjectUtil.simpleIdentityString(cache) + "," + log(sessionId, timestamp) + "," + log(row) + "," + JSON.toString(keyForUpdate) + ")");
    Exception exception = null;
    try {
      final data.MutableKey key = row.getKey();
      final data.MutableKey keyOld = row.getKeyOld();
      T entity;
      if (keyOld.equals(key)) {
        entity = cache.get(key);
        if (entity == null)
          return keyForUpdate != null ? refreshRow(cache, row) : insert(row.clone(false));
      }
      else {
        entity = cache.remove(keyOld);
        if (entity != null) {
          cache.put(key.immutable(), entity);
        }
        else {
          entity = cache.get(key);
          if (entity == null)
            return keyForUpdate != null ? refreshRow(cache, row) : insert(row.clone(false));
        }
      }

      if (keyForUpdate != null) {
        for (final Map.Entry<String,String> entry : keyForUpdate.entrySet()) { // [S]
          final String keyForUpdateKey = entry.getKey();
          final data.Column<?> column = entity.getColumn(keyForUpdateKey);
          if (column == null)
            throw new IllegalArgumentException("Table \"" + row.getName() + "\" does not have column \"" + keyForUpdateKey + "\"");

          final Object columnValue = column.get();
          final String keyForUpdateValue = entry.getValue();
          if (keyForUpdateValue != null ? columnValue == null || !keyForUpdateValue.equals(columnValue.toString()) : columnValue != null)
            return refreshRow(cache, row);
        }
      }

      return update(entity, row);
    }
    catch (final Exception e) {
      exception = e;
      throw e;
    }
    finally {
      onNotifyCallbacks(sessionId, exception);
    }
  }

  @Override
  public data.Table onDelete(final String sessionId, final long timestamp, final data.Table row) {
    if (logger.isTraceEnabled()) logger.trace("onDelete(" + log(sessionId, timestamp) + "," + log(row) + ")");
    Exception exception = null;
    try {
      final data.Table entity = getCache(row).remove(row.getKey());
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
      onNotifyCallbacks(sessionId, exception);
    }
  }

  protected void delete(final data.Table row) {
    if (logger.isTraceEnabled()) logger.trace("delete(" + log(row) + ")");
    final OneToOneMap<? extends data.Table> cache = getCache(row);
    final data.MutableKey key = row.getKey();
    if (key != null)
      cache.remove(key);
    else
      cache.clear();
  }

  /**
   * Refreshes the provided {@link data.Table} by performing a {@code SELECT} to the DB based on the {@code row}'s primary key.
   *
   * @param <T> The type parameter of the {@link data.Table}.
   * @implNote It is important that this method is not called in the same thread at that invoking
   *           {@link Notifier#notify(String,String)}. Doing so may result in a deadlock.
   * @implSpec This method mutates the provided {@code row}.
   * @param cache The cache {@link Map} for the provided {@code row}.
   * @param row The row to refresh.
   * @return The refreshed row.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  protected <T extends data.Table>data.Table refreshRow(final OneToOneMap<T> cache, final T row) throws IOException, SQLException {
    row.reset(data.Except.PRIMARY_KEY);
    selectRow(row);

    final data.MutableKey key = row.getKey();
    T entity = cache.get(key);
    if (entity == null) {
      cache.put(key.immutable(), entity = (T)row.clone(false));
      entity._commitInsert$();
    }
    else {
      entity.merge(row);
      entity._commitUpdate$();
    }

    entity._commitEntity$();

    if (logger.isTraceEnabled()) logger.trace("refreshRow(" + ObjectUtil.simpleIdentityString(cache) + "," + log(row) + "):" + log(entity));
    return entity;
  }

  protected void selectRow(final data.Table row) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace("selectRow(" + log(row) + ")");

    try (
      final Connection connection = getConnector().getConnection();
      final RowIterator<data.Table> rows =
        SELECT(row)
          .execute(connection)) {

        if (!rows.nextRow())
          throw new IllegalStateException("Expected a row");

        rows.nextEntity();

        if (rows.nextRow())
          throw new IllegalStateException("Did not expect another row");
    }
  }

  // Deliberately set to private visibility due to lack of testing
  private void refreshTables(final String sessionId, final long timestamp, final data.Table ... tables) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace("refreshTables(" + log(sessionId, timestamp) + "," + Arrays.stream(tables).map(t -> t.getName()).collect(Collectors.joining(",")) + ")");
    for (final data.Table table : tables) { // [A]
      final OneToOneMap<? extends data.Table> cache = getCache(table);
      try (final RowIterator<? extends data.Table> rows =
        SELECT(table).
        FROM(table)
          .execute()) {
        while (rows.nextRow()) {
          onSelectInsert(cache, sessionId, timestamp, rows.nextEntity());
        }
      }
    }
  }

  // Deliberately set to private visibility due to lack of testing
  private void refreshTables(final String sessionId, final long timestamp, final SELECT<?> ... selects) throws IOException, SQLException {
    if (logger.isTraceEnabled()) logger.trace("refreshTables(" + log(sessionId, timestamp) + ",[" + selects.length + "])");
    for (final SELECT select : selects) { // [A]
      try (final RowIterator<? extends data.Table> rows =
        select.execute()) {
        while (rows.nextRow())
          onInsert(sessionId, timestamp, rows.nextEntity());
      }
    }
  }
}