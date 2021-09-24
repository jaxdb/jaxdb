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

import java.util.Map;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.data.Table;

public class RowCache {
  private final Map<Key<?>,data.Table<?>> primaryKeyToTable;

  public RowCache(final Map<Key<?>,Table<?>> primaryKeyToTable) {
    this.primaryKeyToTable = assertNotNull(primaryKeyToTable);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<T>>T insert(final T row) {
    assertNotNull(row);
    return (T)primaryKeyToTable.put(row.getPrimaryKey(), row);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<T>>boolean upgrade(final T row) {
    assertNotNull(row);
    final T entity = (T)primaryKeyToTable.get(row.getPrimaryKey());
    if (entity == null)
      return false;

    entity.merge(row);
    return true;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<T>>T update(final T row) {
    assertNotNull(row);
    final T entity = (T)primaryKeyToTable.putIfAbsent(row.getPrimaryKey(), row);
    if (entity == null)
      return row;

    entity.merge(row);
    return entity;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<T>>T delete(final Key<T> primaryKey) {
    assertNotNull(primaryKey);
    return (T)primaryKeyToTable.remove(primaryKey);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public <T extends data.Table<?>>T handle(final Action action, final T row) {
    final data.Table raw = row;
    if (action == UPDATE)
      return (T)update(raw);

    if (action == UPGRADE)
      return upgrade(raw) ? (T)raw : null;

    if (action == INSERT) {
      insert(raw);
      return null;
    }

    if (action == DELETE)
      return (T)delete(assertNotNull(raw).getPrimaryKey());

    throw new UnsupportedOperationException("Unsupported Action: " + action);
  }
}