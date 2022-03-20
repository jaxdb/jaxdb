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

import java.util.Iterator;
import java.util.Map;

import org.jaxdb.jsql.data.Except;
import org.jaxdb.jsql.data.Merge;

@SuppressWarnings("rawtypes")
public class TableCache<T extends data.Table> implements Notification.InsertListener<T>, Notification.UpdateListener<T>, Notification.UpgradeListener<T>, Notification.DeleteListener<T> {
  protected final Map<Key<?>,data.Table<?>> keyToTable;

  public TableCache(final Map<Key<?>,data.Table<?>> keyToTable) {
    this.keyToTable = assertNotNull(keyToTable);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onInsert(final T row) {
    assertNotNull(row);

    row.reset(Except.PRIMARY_KEY_FOR_UPDATE);
    return (T)keyToTable.put(row.getKey(), row);
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onUpdate(final T row) {
    assertNotNull(row);

    T entity = (T)keyToTable.putIfAbsent(row.getKey(), row);
    if (entity == null)
      entity = row;
    else
      entity.merge(row, Merge.KEYS);

    entity.reset(Except.PRIMARY_KEY_FOR_UPDATE);
    return entity;
  }

  private boolean isUpToDate(final T entity, final T update) {
    assertNotNull(entity);
    assertNotNull(update);

    for (final data.Column<?> cu : update.getColumns()) {
      if (!cu.primary && cu.wasSet()) {
        final data.Column<?> ce = entity.getColumn(cu.getName());
        if (!ce.equals(cu))
          return false;
      }
    }

    return true;
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
        final data.Column<?> column = entity.getColumn(entry.getKey());
        if (column == null)
          throw new IllegalArgumentException("Table " + row.getName() + " does not have column \"" + entry.getKey() + "\"");

        if (entry.getValue() == null ? column.get() != null : column.get() == null || !entry.getValue().equals(column.get().toString()))
          return isUpToDate(entity, row) ? entity : null;
      }
    }

    entity.merge(row, Merge.KEYS);
    entity.reset(Except.PRIMARY_KEY_FOR_UPDATE);
    return entity;
  }

  @Override
  @SuppressWarnings("unchecked")
  public T onDelete(final T row) {
    assertNotNull(row);
    return (T)keyToTable.remove(row.getKey());
  }

  protected void delete(final Class<? extends data.Table> table) {
    assertNotNull(table);
    final Iterator<Map.Entry<Key<?>,data.Table<?>>> iterator = keyToTable.entrySet().iterator();
    while (iterator.hasNext()) {
      final Map.Entry<Key<?>,data.Table<?>> entry = iterator.next();
      if (entry.getValue().getClass() == table)
        iterator.remove();
    }
  }
}