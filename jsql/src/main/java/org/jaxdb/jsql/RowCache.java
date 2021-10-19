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

import java.util.Map;

import org.jaxdb.jsql.data.Table;

@SuppressWarnings("rawtypes")
public class RowCache<T extends data.Table> implements Notification.Listener<T> {
  protected final Map<Key<?>,data.Table<?>> keyToTable;

  public RowCache(final Map<Key<?>,Table<?>> keyToTable) {
    this.keyToTable = assertNotNull(keyToTable);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onInsert(final T row) {
    assertNotNull(row);
    return (T)keyToTable.put(row.getKey(), row);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onUpdate(final T row) {
    assertNotNull(row);
    final T entity = (T)keyToTable.putIfAbsent(row.getKey(), row);
    if (entity == null)
      return row;

    entity.merge(row);
    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onUpgrade(final T row, final Map<String,String> keyForUpdate) {
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
    return row;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onDelete(final T row) {
    assertNotNull(row);
    return (T)keyToTable.remove(row.getKey());
  }
}