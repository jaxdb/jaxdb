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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.jaxdb.jsql.HashCacheMap.KeyConcurrentHashSet;
import org.libj.util.Interval;
import org.openjax.binarytree.ConcurrentIntervalTreeSet;

public abstract class HashTreeCacheMap<V> extends CacheMap<V> implements NavigableMap<data.Key,V> {
  private static data.BOOLEAN where(final Interval<data.Key>[] intervals) {
    data.BOOLEAN or = andRange(intervals[0]);
    for (int i = 1, i$ = intervals.length; i < i$; ++i)
      or = OR(or, andRange(intervals[i]));

    return or;
  }

  final Map<data.Key,V> hashMap;
  final NavigableMap<data.Key,V> treeMap;
  final ConcurrentIntervalTreeSet<data.Key> treeMask = new ConcurrentIntervalTreeSet<>();
  final KeyConcurrentHashSet hashMask = new KeyConcurrentHashSet();

  HashTreeCacheMap(final data.Table table) {
    this(table, table.getSchema(), new ConcurrentHashMap<>(), new ConcurrentSkipListMap<>());
  }

  HashTreeCacheMap(final data.Table table, final Schema schema, final Map<data.Key,V> hashMap, final NavigableMap<data.Key,V> treeMap) {
    super(table, schema);
    this.hashMap = hashMap;
    this.treeMap = treeMap;
  }

  abstract HashTreeCacheMap<V> newInstance(data.Table table, Map<data.Key,V> hashMap, NavigableMap<data.Key,V> treeMap);

  @Override
  final void addKey(final data.Key key) {
    hashMask.add(key);
    treeMask.add(key);
  }

  @Override
  final void addKey(final data.Key[] keys) {
    hashMask.addAll(keys);
    treeMask.addAll(keys);
  }

  @Override
  public final boolean containsKey(final data.Key key) {
    return hashMask.contains(key);
  }

  final Interval<data.Key>[] diffKeys(final data.Key fromKey, final data.Key toKey) {
    return treeMask.difference(new Interval<>(fromKey, toKey));
  }

  @Override
  final V superGet(final data.Key key) {
    return hashMap.get(key);
  }

  @Override
  final V superPut(final data.Key key, final V value) {
    hashMask.add(key);
    treeMask.add(key);
    hashMap.put(key, value);
    return treeMap.put(key, value);
  }

  @Override
  final V superRemove(final data.Key key) {
    hashMask.remove(key);
    treeMask.remove(key);
    hashMap.remove(key);
    return treeMap.remove(key);
  }

  @Override
  final V superRemoveOld(final data.Key key) {
    hashMap.remove(key);
    return treeMap.remove(key);
  }

  @Override
  public final Map.Entry<data.Key,V> ceilingEntry(final data.Key key) {
    return treeMap.ceilingEntry(key);
  }

  @Override
  public final data.Key ceilingKey(final data.Key key) {
    return treeMap.ceilingKey(key);
  }

  @Override
  public final Comparator<? super data.Key> comparator() {
    return treeMap.comparator();
  }

  @Override
  public final V compute(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    // FIXME: Implement this...
    throw new UnsupportedOperationException();
  }

  @Override
  public final V computeIfAbsent(final data.Key key, final Function<? super data.Key,? extends V> mappingFunction) {
    // FIXME: Implement this...
    throw new UnsupportedOperationException();
  }

  @Override
  public final V computeIfPresent(final data.Key key, final BiFunction<? super data.Key,? super V,? extends V> remappingFunction) {
    // FIXME: Implement this...
    throw new UnsupportedOperationException();
  }

  @Override
  public final boolean containsValue(final Object value) {
    return treeMap.containsValue(value);
  }

  @Override
  public final NavigableSet<data.Key> descendingKeySet() {
    return treeMap.descendingKeySet();
  }

  @Override
  public final NavigableMap<data.Key,V> descendingMap() {
    return newInstance(table, hashMap, treeMap.descendingMap());
  }

  @Override
  public final Set<Map.Entry<data.Key,V>> entrySet() {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.entrySet();
  }

  @Override
  public final Map.Entry<data.Key,V> firstEntry() {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.firstEntry();
  }

  @Override
  public final data.Key firstKey() {
    return treeMap.firstKey();
  }

  @Override
  public final Map.Entry<data.Key,V> floorEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.floorEntry(key);
  }

  @Override
  public final data.Key floorKey(final data.Key key) {
    return treeMap.floorKey(key);
  }

  @Override
  public final void forEach(final BiConsumer<? super data.Key,? super V> action) {
    treeMap.forEach(action);
  }

  @Override
  public final V getOrDefault(final Object key, final V defaultValue) {
    return treeMap.getOrDefault(key, defaultValue);
  }

  @Override
  public final SortedMap<data.Key,V> headMap(final data.Key toKey) {
    return treeMap.headMap(toKey);
  }

  @Override
  public final NavigableMap<data.Key,V> headMap(final data.Key toKey, final boolean inclusive) {
    return newInstance(table, hashMap, treeMap.headMap(toKey, inclusive));
  }

  @Override
  public final Map.Entry<data.Key,V> higherEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.higherEntry(key);
  }

  @Override
  public final data.Key higherKey(final data.Key key) {
    return treeMap.higherKey(key);
  }

  @Override
  public final boolean isEmpty() {
    return treeMap.size() == 0;
  }

  @Override
  public final Set<data.Key> keySet() {
    // FIXME: Return UnmodifiableSet
    return treeMap.keySet();
  }

  @Override
  public final Map.Entry<data.Key,V> lastEntry() {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.lastEntry();
  }

  @Override
  public final data.Key lastKey() {
    return treeMap.lastKey();
  }

  @Override
  public final Map.Entry<data.Key,V> lowerEntry(final data.Key key) {
    // FIXME: Must disallow Map.Entry.setValue()
    return treeMap.lowerEntry(key);
  }

  @Override
  public final data.Key lowerKey(final data.Key key) {
    return treeMap.lowerKey(key);
  }

  @Override
  public final NavigableSet<data.Key> navigableKeySet() {
    return treeMap.navigableKeySet();
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
          hashMask.addAll(diff);
          treeMask.addAll(diff);
        }
      }
    }

    return subMap(fromKey, toKey);
  }

  @Override
  public final int size() {
    return treeMap.size();
  }

  @Override
  public final NavigableMap<data.Key,V> subMap(final data.Key fromKey, final boolean fromInclusive, final data.Key toKey, final boolean toInclusive) {
    return newInstance(table, hashMap, treeMap.subMap(fromKey, fromInclusive, toKey, toInclusive));
  }

  @Override
  public final SortedMap<data.Key,V> subMap(final data.Key fromKey, final data.Key toKey) {
    return treeMap.subMap(fromKey, toKey);
  }

  @Override
  public final SortedMap<data.Key,V> tailMap(final data.Key fromKey) {
    return treeMap.tailMap(fromKey);
  }

  @Override
  public final NavigableMap<data.Key,V> tailMap(final data.Key fromKey, final boolean inclusive) {
    return newInstance(table, hashMap, treeMap.tailMap(fromKey, inclusive));
  }

  @Override
  public final Collection<V> values() {
    // FIXME: Return UnmodifiableCollection
    return treeMap.values();
  }
}