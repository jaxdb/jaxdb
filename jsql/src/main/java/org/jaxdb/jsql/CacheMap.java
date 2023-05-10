/* Copyright (c) 2022 JAX-DB
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

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.libj.util.Interval;
import org.mapdb.DB;
import org.mapdb.DBMaker;

abstract class CacheMap<V> implements Map<data.Key,V> {
  static final DB db = DBMaker.heapDB().make();

  data.Table table;

  CacheMap(final data.Table table) {
    this.table = table;
  }

  abstract void addKey(type.Key key);
  abstract void addKey(type.Key[] keys);
  public abstract boolean containsKey(final data.Key key);

  private static data.BOOLEAN and(final type.Column<?> c, final Object min, final Object max) {
    return AND(new ComparisonPredicate.Gte<>(c, min), new ComparisonPredicate.Lt<>(c, max));
  }

  static data.BOOLEAN and(final Interval<type.Key> i) {
    data.BOOLEAN and = null;
    final type.Key min = i.getMin();
    final type.Key max = i.getMax();
    and = and(min.column(0), min.value(0), max.value(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, and(min.column(j), min.value(j), max.value(j)));

    return and;
  }

  void select(final data.BOOLEAN condition) throws IOException, SQLException {
    final ConcurrentHashMap<String,Connector> dataSourceIdToConnectors = Database.getConnectors(table.getSchema().getClass());
    final Notifier<?>[] notifiers;
    if (dataSourceIdToConnectors.size() == 0 || (notifiers  = Database.getNotifiers(dataSourceIdToConnectors.values().iterator(), 0)) == null)
      return;

    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table).
      WHERE(condition)
        .execute(dataSourceIdToConnectors.get(null))) {

      while (rows.nextRow()) {
        final data.Table row = rows.nextEntity();
        for (final Notifier<?> notifier : notifiers) // [A]
          notifier.onSelect(row, false);
      }
    }
  }

  final V select(final data.Key key) throws IOException, SQLException {
    if (!containsKey(key)) {
      select(and(key));
      addKey(key);
    }

    return get(key);
  }

  V superGet(final data.Key key) {
    return get(key);
  }

  final V removeOld(final type.Key key) {
    return remove(key);
  }

  @Override
  public final boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V remove(final Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Object clone() {
    throw new UnsupportedOperationException();
  }
}