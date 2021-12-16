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

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.jsql.Select.Entity.SELECT;
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

  public boolean addNotificationListener(final INSERT insert, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, this, tables);
  }

  public boolean addNotificationListener(final UP up, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(up, this, tables);
  }

  public boolean addNotificationListener(final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(delete, this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final UP up, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, up, this, tables);
  }

  public boolean addNotificationListener(final UP up, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(up, delete, this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, delete, this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return getConnector().addNotificationListener(insert, up, delete, this, tables);
  }

  @Override
  public void onConnect(final Connection connection, final data.Table table) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onConnect(\"" + table.getName() + "\")");

    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table)
        .execute()) {
      while (rows.nextRow())
        onInsert(connection, rows.nextEntity());
    }
  }

  @Override
  public data.Table<?> onInsert(final Connection connection, final data.Table row) {
    final data.Table onInsert = super.onInsert(connection, row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onInsert(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(onInsert) + ": " + onInsert);

    return onInsert;
  }

  @Override
  public data.Table onUpdate(final Connection connection, final data.Table row) {
    final data.Table onUpdate = super.onUpdate(connection, row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpdate(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(onUpdate) + ": " + onUpdate);

    return onUpdate;
  }

  @Override
  public data.Table onUpgrade(final Connection connection, final data.Table row, final Map<String,String> keyForUpdate) {
    final data.Table onUpgrade = super.onUpgrade(connection, row, keyForUpdate);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpgrade(\"" + row.getName() + "\"," + row + "," + JSON.toString(keyForUpdate) + ") -> " + ObjectUtil.simpleIdentityString(onUpgrade) + (onUpgrade != null ? ": " + onUpgrade.toString(true) : ""));

    return onUpgrade != null ? onUpgrade : refreshRow(connection, row);
  }

  @Override
  public data.Table onDelete(final Connection connection, final data.Table row) {
    final data.Table<?> deleted = super.onDelete(connection, row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onDelete(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(deleted) + ": " + deleted);

    return deleted;
  }

  protected data.Table selectRow(final Connection connection, data.Table row) throws IOException, SQLException {
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

  @SuppressWarnings("unchecked")
  protected data.Table refreshRow(Connection connection, data.Table row) {
    // FIXME: This approach ends up mutating the provided row
    row.reset(true);
    try {
      row = selectRow(connection == null || connection.isClosed() ? getConnector().getConnection() : connection, row);
    }
    catch (final SQLException e) {
      if (logger.isWarnEnabled())
        logger.warn("refreshRow(): connection.isClosed() = " + AuditConnection.isClosed(connection) + ", trying again with new connection", e);

      try {
        row = selectRow(getConnector().getConnection(), row);
      }
      catch (final SQLException se) {
        se.addSuppressed(e);
        if (logger.isWarnEnabled())
          logger.warn("refreshRow(): Failed connector.getConnection(), aborting", se);

        throw new IllegalStateException("Unrecoverable invocation: TableCache.refreshRow()", se);
      }
      catch (final IOException ie) {
        throw new UncheckedIOException(ie);
      }
    }
    catch (final IOException ie) {
      throw new UncheckedIOException(ie);
    }

    data.Table entity = keyToTable.get(row.getKey());
    if (entity == null) {
      keyToTable.put(row.getKey(), entity = row);
    }
    else {
      entity.merge(row);
    }

    entity.reset(true);

    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".refreshRow(\"" + row.getName() + "\"," + row + ") -> " + ObjectUtil.simpleIdentityString(entity) + ": " + entity);

    return entity;
  }

  public void refreshTables(final Connection connection, final data.Table<?> ... tables) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".refreshTables(" + Arrays.stream(tables).map(t -> t.getName()).collect(Collectors.joining(",")) + ")");

    assertNotNull(tables);
    for (final data.Table<?> table : tables) {
      try (final RowIterator<? extends data.Table> rows =
        SELECT(table).
        FROM(table)
          .execute()) {
        while (rows.nextRow())
          onInsert(connection, rows.nextEntity());
      }
    }
  }

  public void refreshTables(final Connection connection, final SELECT<?> ... selects) throws IOException, SQLException {
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".refreshTables([" + selects.length + "])");

    assertNotNull(selects);
    for (final SELECT select : selects) {
      try (final RowIterator<? extends data.Table> rows =
        select.execute()) {
        while (rows.nextRow())
          onInsert(connection, rows.nextEntity());
      }
    }
  }
}