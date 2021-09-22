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

import static org.jaxdb.jsql.Notification.Action.*;
import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UPDATE;
import org.libj.lang.ObjectUtil;
import org.libj.util.CollectionUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TableCache extends RowCache {
  private static final Logger logger = LoggerFactory.getLogger(TableCache.class);
  private static final Comparator<data.Table<?>> tableNameComparator = (o1, o2) -> o1.getName().compareTo(o2.getName());

  /**
   * Creates a new {@link TableCache} with the provided {@link Connector} over
   * which a {@link org.jaxdb.jsql.Notification.Listener Notification.Listener}
   * will receive notifications of {@link INSERT}, {@link UPDATE} and
   * {@link DELETE} actions to be handled by this instance initialized with the
   * given {@code rows}.
   *
   * @param connector The {@link Connector}.
   * @param rows The list of rows to initialize the cache.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   * @throws IllegalArgumentException If {@code connector} is null, or
   *           {@code rows} is null or empty.
   */
  @SuppressWarnings({"rawtypes", "unchecked"})
  public TableCache(final Connector connector, final List<data.Table<?>> rows) throws IOException, SQLException {
    super(new ConcurrentHashMap<>());
    assertNotNull(connector);
    assertNotEmpty(rows);
    for (final data.Table row : rows)
      insert(row);

    rows.sort(tableNameComparator);
    final int len = CollectionUtil.dedupe(rows, tableNameComparator);
    final data.Table<?>[] tables = new data.Table<?>[len];
    for (int i = 0; i < len; ++i)
      tables[i] = rows.get(i);

    connector.addNotificationListener(INSERT, UPDATE, DELETE, (final Action action, final data.Table<?> row) -> {
      final data.Table<?> value = handle(action, row);
      if (logger.isDebugEnabled())
        logger.debug(getClass().getSimpleName() + ".handle(" + action + "," + row + ") -> " + ObjectUtil.simpleIdentityString(value) + ": " + value);
    }, tables);
  }
}