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
import java.util.Queue;
import java.util.stream.Collectors;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.jsql.Select.Entity.SELECT;
import org.jaxdb.jsql.data.Except;
import org.jaxdb.jsql.data.Merge;
import org.jaxdb.jsql.data.Table;
import org.libj.lang.ObjectUtil;
import org.libj.sql.AuditConnection;
import org.openjax.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class DatabaseCache extends TableCache<data.Table> {
  private static final Logger logger = LoggerFactory.getLogger(DatabaseCache.class);

  private final Connector connector;

  /**
   * Creates a new {@link DatabaseCache} with the provided {@link Connector} over which a
   * {@link org.jaxdb.jsql.Notification.Listener Notification.Listener} will receive notifications of {@link Action#INSERT INSERT},
   * {@link Action#UPDATE UPDATE}, {@link Action#UPGRADE UPGRADE} and {@link Action#DELETE DELETE} actions to be handled by this
   * instance initialized with the given {@code rows}.
   *
   * @param primaryKeyToTable The instance of the underlying {@link Map} to be used by the {@link TableCache}.
   * @param connector The {@link Connector}.
   * @throws IllegalArgumentException If {@code connector} is null, or {@code rows} is null or empty.
   */
  public DatabaseCache(final Map<Key<?>,Table<?>> primaryKeyToTable, final Connector connector) {
    super(primaryKeyToTable);
    this.connector = assertNotNull(connector);
  }

  protected DatabaseCache(final Map<Key<?>,Table<?>> primaryKeyToTable) {
    super(primaryKeyToTable);
    this.connector = null;
  }

  protected Connector getConnector() {
    return connector;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final UP up, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(up, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final DELETE delete, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(delete, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final UP up, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, up, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final UP up, final DELETE delete, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(up, delete, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final DELETE delete, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, delete, (TableCache)this, queue, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, up, delete, (TableCache)this, queue, tables);
  }

  @Override
  public void onConnect(final data.Table table) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onConnect(\"" + table.getName() + "\")");

    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table)
        .execute()) {
      while (rows.nextRow())
        onInsert(rows.nextEntity());
    }
  }

  @Override
  public data.Table<?> onInsert(final data.Table row) {
    final data.Table onInsert = super.onInsert(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onInsert(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(onInsert) + ": " + onInsert);

    return onInsert;
  }

  @Override
  public data.Table onUpdate(final data.Table row) {
    final data.Table onUpdate = super.onUpdate(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpdate(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(onUpdate) + ": " + onUpdate);

    return onUpdate;
  }

  @Override
  public data.Table onUpgrade(final data.Table row, final Map<String,String> keyForUpdate) {
    final data.Table onUpgrade = super.onUpgrade(row, keyForUpdate);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpgrade(\"" + row.getName() + "\"," + row + "," + JSON.toString(keyForUpdate) + ") -> " + ObjectUtil.simpleIdentityString(onUpgrade) + (onUpgrade != null ? ": " + onUpgrade.toString(true) : ""));

    if (onUpgrade != null)
      return onUpgrade;

    refreshRow(row);
    return null;
  }

  @Override
  public data.Table onDelete(final data.Table row) {
    final data.Table<?> deleted = super.onDelete(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onDelete(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(deleted) + ": " + deleted);

    return deleted;
  }

  protected data.Table selectRow(final Connection connection, data.Table row) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".selectRow(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(row) + row.toString(true));

    try (final RowIterator<?> rows =
      SELECT(row)
        .execute(connection)) {

      if (!rows.nextRow())
        throw new IllegalStateException("Expected a row");

      row = (data.Table<?>)rows.nextEntity();
      if (rows.nextRow())
        throw new IllegalStateException("Did not expect another row");

      return row;
    }
  }

  /**
   * Refreshes the provided {@link data.Table} by performing a {@code SELECT} to the DB based on the {@code row}'s primary key.
   *
   * @implNote It is important that this method is not called in the same thread at that invoking
   *           {@link Notifier#notify(String,String)}. Doing so may result in a deadlock.
   * @param row The row to refresh.
   * @return The refreshed row.
   */
  @SuppressWarnings({"resource", "unchecked"})
  protected data.Table refreshRow(data.Table row) {
    // FIXME: This approach ends up mutating the provided row
    row.reset(Except.PRIMARY_KEY);
    Connection connection = null;
    try {
      try {
        connection = getConnector().getConnection();
        row = selectRow(connection, row);
      }
      catch (final SQLException e) {
        if (logger.isWarnEnabled())
          logger.warn("refreshRow(): connection.isClosed() = " + (connection != null ? AuditConnection.isClosed(connection) : "null"), e);
      }
      catch (final IOException ie) {
        throw new UncheckedIOException(ie);
      }

      data.Table entity = keyToTable.get(row.getKey());
      if (entity == null)
        keyToTable.put(row.getKey(), entity = row);
      else
        entity.merge(row, Merge.ALL);

      entity.reset(Except.PRIMARY_KEY_FOR_UPDATE);

      if (logger.isDebugEnabled())
        logger.debug(getClass().getSimpleName() + ".refreshRow(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(entity) + ": " + entity);

      return entity;
    }
    finally {
      if (connection != null) {
        try {
          connection.close();
        }
        catch (final SQLException e) {
          if (logger.isWarnEnabled())
            logger.warn(e.getMessage(), e);
        }
      }
    }
  }

  public void refreshTables(final data.Table<?> ... tables) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".refreshTables(" + Arrays.stream(tables).map(t -> t.getName()).collect(Collectors.joining(",")) + ")");

    assertNotNull(tables);
    for (final data.Table<?> table : tables) {
      try (final RowIterator<? extends data.Table> rows =
        SELECT(table).
        FROM(table)
          .execute()) {
        while (rows.nextRow())
          onInsert(rows.nextEntity());
      }
    }
  }

  public void refreshTables(final SELECT<?> ... selects) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".refreshTables([" + selects.length + "])");

    assertNotNull(selects);
    for (final SELECT select : selects) {
      try (final RowIterator<? extends data.Table> rows =
        select.execute()) {
        while (rows.nextRow())
          onInsert(rows.nextEntity());
      }
    }
  }
}