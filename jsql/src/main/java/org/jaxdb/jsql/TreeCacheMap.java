/* Copyright (c) 2023 JAX-DB
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
import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.libj.util.Interval;
import org.openjax.binarytree.ConcurrentIntervalTreeSet;

public abstract class TreeCacheMap<V> extends CacheMap<V> implements NavigableMap<data.Key,V> {
  private static data.BOOLEAN where(final Interval<data.Key>[] intervals) {
    data.BOOLEAN or = andRange(intervals[0]);
    for (int i = 1, i$ = intervals.length; i < i$; ++i) // [A]
      or = OR(or, andRange(intervals[i]));

    return or;
  }

  final NavigableMap<data.Key,V> map;
  final ConcurrentIntervalTreeSet<data.Key> mask = new ConcurrentIntervalTreeSet<>();

  TreeCacheMap(final data.Table table) {
    this(table, table.getSchema(), new ConcurrentSkipListMap<>());
  }

  TreeCacheMap(final data.Table table, final Schema schema, final NavigableMap<data.Key,V> map) {
    super(table, schema);
    this.map = map;
  }

  abstract TreeCacheMap<V> newInstance(data.Table table, NavigableMap<data.Key,V> map);

  @Override
  final void addKey(final data.Key key) {
    mask.add(key);
  }

  @Override
  final void addKey(final data.Key[] keys) {
    mask.addAll(keys);
  }

  @Override
  public final boolean containsKey(final data.Key key) {
    return mask.contains(key);
  }

  final Interval<data.Key>[] diffKeys(final data.Key fromKey, final data.Key toKey) {
    return mask.difference(new Interval<>(fromKey, toKey));
  }

  @Override
  final V get$(final data.Key key) {
    return map.get(key);
  }

  @Override
  final V put$(final data.Key key, final V value) {
    mask.add(key);
    return map.put(key, value);
  }

  @Override
  final V remove$(final data.Key key) {
    mask.remove(key);
    return map.remove(key);
  }

  @Override
  final V remove$Old(final data.Key key) {
    return map.remove(key);
  }

  @Override
  public final Map.Entry<data.Key,V> ceilingEntry(final data.Key key) {
    return map.ceilingEntry(key);
  }

  @Override
  public final data.Key ceilingKey(final data.Key key) {
    return map.ceilingKey(key);
  }

  @Override
  public final Comparator<? super data.Key> comparator() {
    return map.comparator();
  }

  @Override
  public final V compute(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    return map.compute(key, remappingFunction);
  }

  @Override
  public final V computeIfAbsent(final data.Key key, final Function<? super data.Key,? extends V> mappingFunction) {
    return map.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public final V computeIfPresent(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    return map.computeIfPresent(key, remappingFunction);
  }

  @Override
  public final boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public final NavigableSet<data.Key> descendingKeySet() {
    return map.descendingKeySet();
  }

  @Override
  public final NavigableMap<data.Key,V> descendingMap() {
    return newInstance(table, map.descendingMap());
  }

  @Override
  public final Set<Map.Entry<data.Key,V>> entrySet() {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.entrySet();
  }

  @Override
  public final Map.Entry<data.Key,V> firstEntry() {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.firstEntry();
  }

  @Override
  public final data.Key firstKey() {
    return map.firstKey();
  }

  @Override
  public final Map.Entry<data.Key,V> floorEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.floorEntry(key);
  }

  @Override
  public final data.Key floorKey(final data.Key key) {
    return map.floorKey(key);
  }

  @Override
  public final void forEach(final BiConsumer<? super data.Key,? super V> action) {
    map.forEach(action);
  }

  @Override
  public final V getOrDefault(final Object key, final V defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public final SortedMap<data.Key,V> headMap(final data.Key toKey) {
    return map.headMap(toKey);
  }

  @Override
  public final NavigableMap<data.Key,V> headMap(final data.Key toKey, final boolean inclusive) {
    return newInstance(table, map.headMap(toKey, inclusive));
  }

  @Override
  public final Map.Entry<data.Key,V> higherEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.higherEntry(key);
  }

  @Override
  public final data.Key higherKey(final data.Key key) {
    return map.higherKey(key);
  }

  @Override
  public final boolean isEmpty() {
    return map.size() == 0;
  }

  @Override
  public final Set<data.Key> keySet() {
    // FIXME: Return UnmodifiableSet
    return map.keySet();
  }

  @Override
  public final Map.Entry<data.Key,V> lastEntry() {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.lastEntry();
  }

  @Override
  public final data.Key lastKey() {
    return map.lastKey();
  }

  @Override
  public final Map.Entry<data.Key,V> lowerEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.lowerEntry(key);
  }

  @Override
  public final data.Key lowerKey(final data.Key key) {
    return map.lowerKey(key);
  }

  @Override
  public final NavigableSet<data.Key> navigableKeySet() {
    return map.navigableKeySet();
  }

  @Override
  public final Map.Entry<data.Key,V> pollFirstEntry() {
    // FIXME: Implement this...
    // FIXME: Must disallow Map.Entry.setValue()
    throw new UnsupportedOperationException();
  }

  @Override
  public final Map.Entry<data.Key,V> pollLastEntry() {
    // FIXME: Implement this...
    // FIXME: Must disallow Map.Entry.setValue()
    throw new UnsupportedOperationException();
  }

  final SortedMap<data.Key,V> select(final data.Key fromKey, final data.Key toKey) throws IOException, SQLException {
    if (fromKey.length() > 1)
      throw new UnsupportedOperationException("Composite keys are not yet supported");

    Interval<data.Key>[] diff = diffKeys(fromKey, toKey);
    if (diff.length > 0) {
      synchronized (this) {
        diff = diffKeys(fromKey, toKey);
        if (diff.length > 0) {
          select(where(diff));
          mask.addAll(diff);
        }
      }
    }

    return subMap(fromKey, toKey);
  }

  @Override
  public final int size() {
    return map.size();
  }

  @Override
  public final NavigableMap<data.Key,V> subMap(final data.Key fromKey, final boolean fromInclusive, final data.Key toKey, final boolean toInclusive) {
    return newInstance(table, map.subMap(fromKey, fromInclusive, toKey, toInclusive));
  }

  @Override
  public final SortedMap<data.Key,V> subMap(final data.Key fromKey, final data.Key toKey) {
    return map.subMap(fromKey, toKey);
  }

  @Override
  public final SortedMap<data.Key,V> tailMap(final data.Key fromKey) {
    return map.tailMap(fromKey);
  }

  @Override
  public final NavigableMap<data.Key,V> tailMap(final data.Key fromKey, final boolean inclusive) {
    return newInstance(table, map.tailMap(fromKey, inclusive));
  }

  @Override
  public final Collection<V> values() {
    // FIXME: Return UnmodifiableCollection
    return map.values();
  }
}