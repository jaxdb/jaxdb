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
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.libj.util.Interval;
import org.mapdb.BTreeMap;
import org.openjax.binarytree.IntervalTreeSet;

public class TreeCacheMap<V> extends CacheMap<V> implements NavigableMap<data.Key,V> {
  @SuppressWarnings("rawtypes")
  static final TreeCacheMap EMPTY = new TreeCacheMap(null, null);

  private static data.BOOLEAN where(final Interval<type.Key>[] intervals) {
    data.BOOLEAN or = and(intervals[0]);
    for (int i = 1, i$ = intervals.length; i < i$; ++i)
      or = OR(or, and(intervals[i]));

    return or;
  }

  final String name;
  final NavigableMap<data.Key,V> map;
  final IntervalTreeSet<type.Key> mask = new IntervalTreeSet<>();

  @SuppressWarnings("unchecked")
  TreeCacheMap(final data.Table table, final String name) {
    this(table, name, (BTreeMap<data.Key,V>)db.treeMap(name).counterEnable().create());
  }

  private TreeCacheMap(final data.Table table, final String name, final NavigableMap<data.Key,V> map) {
    super(table);
    this.name = name;
    this.map = map;
  }

  @Override
  final void addKey(final type.Key key) {
    mask.add(key);
  }

  @Override
  final void addKey(final type.Key[] keys) {
    for (final type.Key key : keys)
      mask.add(key);
  }

  @Override
  public final boolean containsKey(final data.Key key) {
    return mask.contains(key);
  }

  public Interval<type.Key>[] diffKeys(final data.Key fromKey, final data.Key toKey) {
    return mask.difference(new Interval<>(fromKey, toKey));
  }

  final SortedMap<data.Key,V> select(final data.Key fromKey, final data.Key toKey) throws IOException, SQLException {
    final Interval<type.Key>[] diff = diffKeys(fromKey, toKey);
    if (diff.length > 0) {
      select(where(diff));
      mask.addAll(diff);
    }

    return subMap(fromKey, toKey);
  }

  @Override
  public Entry<data.Key,V> lowerEntry(final data.Key key) {
    return map.lowerEntry(key);
  }

  @Override
  public data.Key lowerKey(final data.Key key) {
    return map.lowerKey(key);
  }

  @Override
  public Entry<data.Key,V> floorEntry(final data.Key key) {
    return map.floorEntry(key);
  }

  @Override
  public Comparator<? super data.Key> comparator() {
    return map.comparator();
  }

  @Override
  public data.Key floorKey(final data.Key key) {
    return map.floorKey(key);
  }

  @Override
  public Entry<data.Key,V> ceilingEntry(final data.Key key) {
    return map.ceilingEntry(key);
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public boolean containsKey(final Object key) {
    return map.containsKey(key);
  }

  @Override
  public data.Key ceilingKey(final data.Key key) {
    return map.ceilingKey(key);
  }

  @Override
  public Entry<data.Key,V> higherEntry(final data.Key key) {
    return map.higherEntry(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public data.Key higherKey(final data.Key key) {
    return map.higherKey(key);
  }

  @Override
  public Entry<data.Key,V> firstEntry() {
    return map.firstEntry();
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }

  @Override
  public Entry<data.Key,V> lastEntry() {
    return map.lastEntry();
  }

  @Override
  public Entry<data.Key,V> pollFirstEntry() {
    return map.pollFirstEntry();
  }

  @Override
  public Entry<data.Key,V> pollLastEntry() {
    return map.pollLastEntry();
  }

  @Override
  public NavigableMap<data.Key,V> descendingMap() {
    return new TreeCacheMap<>(table, name, map.descendingMap());
  }

  @Override
  public V put(final data.Key key, final V value) {
    return map.put(key, value);
  }

  @Override
  public data.Key firstKey() {
    return map.firstKey();
  }

  @Override
  public data.Key lastKey() {
    return map.lastKey();
  }

  @Override
  public NavigableSet<data.Key> navigableKeySet() {
    return map.navigableKeySet();
  }

  @Override
  public Set<data.Key> keySet() {
    return map.keySet();
  }

  @Override
  public NavigableSet<data.Key> descendingKeySet() {
    return map.descendingKeySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public NavigableMap<data.Key,V> subMap(final data.Key fromKey, final boolean fromInclusive, final data.Key toKey, final boolean toInclusive) {
    return new TreeCacheMap<>(table, name, map.subMap(fromKey, fromInclusive, toKey, toInclusive));
  }

  @Override
  public Set<Entry<data.Key,V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public void putAll(final Map<? extends data.Key,? extends V> m) {
    // FIXME: Can be optimized for maps with masks
    for (final Map.Entry<? extends data.Key,? extends V> entry : map.entrySet())
      this.map.put(entry.getKey(), entry.getValue());
  }

  @Override
  public NavigableMap<data.Key,V> headMap(final data.Key toKey, final boolean inclusive) {
    return new TreeCacheMap<>(table, name, map.headMap(toKey, inclusive));
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public NavigableMap<data.Key,V> tailMap(final data.Key fromKey, final boolean inclusive) {
    return new TreeCacheMap<>(table, name, map.tailMap(fromKey, inclusive));
  }

  @Override
  public SortedMap<data.Key,V> subMap(final data.Key fromKey, final data.Key toKey) {
    return map.subMap(fromKey, toKey);
  }

  @Override
  public SortedMap<data.Key,V> headMap(final data.Key toKey) {
    return map.headMap(toKey);
  }

  @Override
  public SortedMap<data.Key,V> tailMap(final data.Key fromKey) {
    return map.tailMap(fromKey);
  }

  @Override
  public boolean equals(final Object o) {
    return map.equals(o);
  }

  @Override
  public int hashCode() {
    return map.hashCode();
  }

  @Override
  public V getOrDefault(final Object key, final V defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public void forEach(final BiConsumer<? super data.Key,? super V> action) {
    map.forEach(action);
  }

  @Override
  public void replaceAll(final BiFunction<? super data.Key,? super V,? extends V> function) {
    map.replaceAll(function);
  }

  @Override
  public V putIfAbsent(final data.Key key, final V value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public boolean replace(final data.Key key, final V oldValue, final V newValue) {
    return map.replace(key, oldValue, newValue);
  }

  @Override
  public V replace(final data.Key key, final V value) {
    return map.replace(key, value);
  }

  @Override
  public V computeIfAbsent(final data.Key key, final Function<? super data.Key,? extends V> mappingFunction) {
    return map.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public V computeIfPresent(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    return map.computeIfPresent(key, remappingFunction);
  }

  @Override
  public V compute(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    return map.compute(key, remappingFunction);
  }

  @Override
  public V merge(final data.Key key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return map.merge(key, value, remappingFunction);
  }
}