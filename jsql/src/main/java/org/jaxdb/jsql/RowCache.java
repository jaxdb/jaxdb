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

import static org.libj.lang.Assertions.*;

import java.sql.Connection;
import java.util.Map;

import org.jaxdb.jsql.data.Table;

@SuppressWarnings("rawtypes")
public class RowCache<T extends data.Table> implements Notification.InsertListener<T>, Notification.UpdateListener<T>, Notification.UpgradeListener<T>, Notification.DeleteListener<T> {
  protected final Map<Key<?>,data.Table<?>> keyToTable;

  public RowCache(final Map<Key<?>,Table<?>> keyToTable) {
    this.keyToTable = assertNotNull(keyToTable);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onInsert(final Connection connection, final T row) {
    assertNotNull(row);
    row.reset(true);
    return (T)keyToTable.put(row.getKey(), row);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onUpdate(final Connection connection, final T row) {
    assertNotNull(row);
    T entity = (T)keyToTable.putIfAbsent(row.getKey(), row);
    if (entity != null)
      entity.merge(row);
    else
      entity = row;

    entity.reset(true);
    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onUpgrade(final Connection connection, final T row, final Map<String,String> keyForUpdate) {
    assertNotNull(row);
    final T entity = (T)keyToTable.get(row.getKey());
    if (entity == null)
      return null;

    if (keyForUpdate != null) {
      for (final Map.Entry<String,String> entry : keyForUpdate.entrySet()) {
        final data.Column<?> value = entity.getColumn(entry.getKey());
        if (value == null)
          throw new IllegalArgumentException("Table " + row.getName() + " does not have column named " + entry.getKey());

        if (!entry.getValue().equals(value.get().toString())) // NOTE: Doing a string equals here, because the map does not parse into Number or Boolean types
          return null;
      }
    }

    entity.merge(row);
    entity.reset(true);
    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onDelete(final Connection connection, final T row) {
    assertNotNull(row);
    return (T)keyToTable.remove(row.getKey());
  }
}