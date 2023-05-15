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
import java.nio.channels.UnsupportedAddressTypeException;
import java.sql.SQLException;
import java.util.Map;
import java.util.function.BiFunction;

import org.libj.util.Interval;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public abstract class CacheMap<V> implements Map<data.Key,V> {
  static final DB db = DBMaker.heapDB().make();

  data.Table table;

  CacheMap(final data.Table table) {
    this.table = table;
  }

  abstract void addKey(type.Key key);
  abstract void addKey(type.Key[] keys);
  public abstract boolean containsKey(final data.Key key);
  abstract V superRemove(type.Key key);
  abstract V superRemoveOld(type.Key key);
  abstract V superGet(data.Key key);
  abstract V superPut(data.Key key, V value);

  private static data.BOOLEAN andRange(final type.Column<?> c, final Object min, final Object max) {
    return AND(new ComparisonPredicate.Gte<>(c, min), new ComparisonPredicate.Lt<>(c, max));
  }

  static data.BOOLEAN andRange(final Interval<type.Key> i) {
    final type.Key min = i.getMin();
    final type.Key max = i.getMax();
    data.BOOLEAN and = andRange(min.column(0), min.value(0), max.value(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, andRange(min.column(j), min.value(j), max.value(j)));

    return and;
  }

  private static data.BOOLEAN eq(final type.Column<?> c, final Object v) {
    return new ComparisonPredicate.Eq<>(c, v);
  }

  static data.BOOLEAN andEq(final Interval<type.Key> i) {
    final type.Key min = i.getMin();
    data.BOOLEAN and = eq(min.column(0), min.value(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, eq(min.column(j), min.value(j)));

    return and;
  }

  @Override
  public final void clear() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Object clone() {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean containsKey(final Object key) {
    return containsKey((data.Key)key);
  }

  @Override
  public final V merge(final data.Key key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    throw new UnsupportedAddressTypeException();
  }

  @Override
  public final V put(final data.Key key, final V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void putAll(final Map<? extends data.Key,? extends V> m) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V putIfAbsent(final data.Key key, final V value) {
    throw new UnsupportedOperationException();
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
  public final V replace(final data.Key key, final V value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean replace(final data.Key key, final V oldValue, final V newValue) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final void replaceAll(final BiFunction<? super data.Key,? super V,? extends V> function) {
    throw new UnsupportedOperationException();
  }

  final void select(final data.BOOLEAN condition) throws IOException, SQLException {
    final Class<? extends Schema> schemaClass = table.getSchema().getClass();
    final Database database = Database.global(schemaClass);
    final Notifier<?> notifier = database.getCacheNotifier();
    if (notifier == null)
      return;

    final Connector defaultConnector = database.getConnectors(schemaClass).get(null);
    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table).
      WHERE(condition)
        .execute(defaultConnector)) {

      while (rows.nextRow())
        notifier.onSelect(rows.nextEntity());
    }
  }

  V select(final data.Key key) throws IOException, SQLException {
    if (!containsKey(key)) {
      select(andEq(key));
      addKey(key);
    }

    return get(key);
  }

  final V selectMany(final data.Key key) throws IOException, SQLException {
    if (!containsKey(data.Key.ALL)) {
      select(andEq(key));
      addKey(data.Key.ALL);
    }

    return get(key);
  }

  final V superSelect(final data.Key key) throws IOException, SQLException {
    if (!containsKey(key)) {
      select(andEq(key));
      addKey(key);
    }

    return superGet(key);
  }
}