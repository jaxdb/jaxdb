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
import java.sql.SQLException;
import java.util.Map;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.jsql.Select.Entity.SELECT;
import org.jaxdb.jsql.data.Table;
import org.libj.lang.ObjectUtil;
import org.openjax.json.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("rawtypes")
public class TableCache extends RowCache<data.Table> {
  private static final Logger logger = LoggerFactory.getLogger(TableCache.class);

  protected final Connector connector;

  /**
   * Creates a new {@link TableCache} with the provided {@link Connector} over
   * which a {@link org.jaxdb.jsql.Notification.Listener Notification.Listener}
   * will receive notifications of {@link Action#INSERT INSERT},
   * {@link Action#UPDATE UPDATE}, {@link Action#UPGRADE UPGRADE} and
   * {@link Action#DELETE DELETE} actions to be handled by this instance
   * initialized with the given {@code rows}.
   *
   * @param primaryKeyToTable The instance of the underlying {@link Map} to be
   *          used by the {@link RowCache}.
   * @param connector The {@link Connector}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   * @throws IllegalArgumentException If {@code connector} is null, or
   *           {@code rows} is null or empty.
   */
  public TableCache(final Map<Key<?>,Table<?>> primaryKeyToTable, final Connector connector) throws IOException, SQLException {
    super(primaryKeyToTable);
    this.connector = assertNotNull(connector);
  }

  public boolean addNotificationListener(final INSERT insert, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener(assertNotNull(insert), null, null, this, tables);
  }

  public boolean addNotificationListener(final UP up, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(null, assertNotNull(up), null, this, tables);
  }

  public boolean addNotificationListener(final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(null, null, assertNotNull(delete), this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final UP up, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(assertNotNull(insert), assertNotNull(up), null, this, tables);
  }

  public boolean addNotificationListener(final UP up, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(null, assertNotNull(up), assertNotNull(delete), this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(assertNotNull(insert), null, assertNotNull(delete), this, tables);
  }

  public boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final data.Table<?> ... tables) throws IOException, SQLException {
    return connector.addNotificationListener0(assertNotNull(insert), assertNotNull(up), assertNotNull(delete), this, tables);
  }

  @Override
  public data.Table<?> onInsert(final data.Table row) {
    final data.Table onInsert = super.onInsert(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onInsert(" + row + ") -> " + ObjectUtil.simpleIdentityString(onInsert) + ": " + onInsert);

    return onInsert;
  }

  @Override
  public data.Table onUpdate(final data.Table row) {
    final data.Table onUpdate = super.onUpdate(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpdate(" + row + ") -> " + ObjectUtil.simpleIdentityString(onUpdate) + ": " + onUpdate);

    return onUpdate;
  }

  @Override
  public data.Table onUpgrade(final data.Table row, final Map<String,String> keyForUpdate) {
    final data.Table onUpgrade = super.onUpgrade(row, keyForUpdate);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onUpgrade(" + row + "," + JSON.toString(keyForUpdate) + ") -> " + ObjectUtil.simpleIdentityString(onUpgrade) + (onUpgrade != null ? ": " + onUpgrade.toString(true) : ""));

    return onUpgrade != null ? onUpgrade : refreshRow(row);
  }

  @Override
  public data.Table onDelete(final data.Table row) {
    final data.Table<?> deleted = super.onDelete(row);
    if (logger.isDebugEnabled())
      logger.debug(getClass().getSimpleName() + ".onDelete(" + row + ") -> " + ObjectUtil.simpleIdentityString(deleted) + ": " + deleted);

    return deleted;
  }

  @SuppressWarnings("unchecked")
  protected data.Table refreshRow(data.Table row) {
    // FIXME: This approach ends up mutating the provided row
    for (final data.Column<?> column : row._column$) {
      if (column.wasSet) {
        if (!column.primary)
          column.wasSet = false;
      }
      else if (column.primary) {
        throw new IllegalArgumentException("Expected primary column to be set: " + column.name);
      }
    }

    try (final RowIterator<?> rows =
      SELECT(row)
        .execute(connector)) {

      if (!rows.nextRow())
        throw new IllegalStateException("Expected a row");

      row = (data.Table<?>)rows.nextEntity();
      if (rows.nextRow())
        throw new IllegalStateException("Did not expect another row");
    }
    catch (final IOException | SQLException e) {
      logger.error("Failed SELECT in refresh()", e);
    }

    final data.Table entity = keyToTable.get(row.getKey());
    if (entity != null) {
      entity.merge(row);
      return entity;
    }

    keyToTable.put(row.getKey(), row);
    return row;
  }

  public void refreshTables(final data.Table<?> ... tables) throws IOException, SQLException {
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