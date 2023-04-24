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
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.libj.util.Interval;
import org.mapdb.BTreeMap;
import org.openjax.binarytree.IntervalTreeSet;

public class OneToOneTreeMap<V extends data.Table> extends TreeMap<data.Key,V> implements NavigableRangeMap<data.Key,V>, OneToOneMap<V> {
  private final String name = String.valueOf(System.identityHashCode(this));
  @SuppressWarnings("unchecked")
  private final BTreeMap<data.Key,V> map = (BTreeMap<data.Key,V>)db.treeMap(name).counterEnable().create();
  private final IntervalTreeSet<data.Key> mask = new IntervalTreeSet<>();

  private final data.Table table;

  OneToOneTreeMap(final data.Table table) {
    this.table = table;
  }

  private static Notifier<?>[] getNotifiers(final Iterator<Connector> iterator, final int depth) throws SQLException, IOException {
    if (!iterator.hasNext())
      return depth == 0 ? null : new Notifier<?>[depth];

    final Notifier<?> notifier = iterator.next().getNotifier();
    if (notifier == null)
      return getNotifiers(iterator, depth);

    final Notifier<?>[] notifiers = getNotifiers(iterator, depth + 1);
    notifiers[depth] = notifier;
    return notifiers;
  }

  private static data.BOOLEAN and(final data.Column<?> c, final Serializable min, final Serializable max) {
    return AND(new ComparisonPredicate<>(function.Logical.GTE, c, min), new ComparisonPredicate<>(function.Logical.LT, c, max));
  }

  private static data.BOOLEAN and(final Interval<data.Key> missing) {
    data.BOOLEAN and = null;
    final data.Key min = missing.getMin();
    final data.Key max = missing.getMax();
    and = and(min.columns[0], min.get(0), max.get(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, and(min.columns[j], min.get(j), max.get(j)));

    return and;
  }

  private static data.BOOLEAN where(final Interval<data.Key>[] missings) {
    data.BOOLEAN or = and(missings[0]);
    for (int i = 1, i$ = missings.length; i < i$; ++i)
      or = OR(or, and(missings[i]));

    return or;
  }

  @Override
  public SortedMap<data.Key,V> get(final data.Key from, final data.Key to) throws SQLException, IOException {
    final Interval<data.Key>[] diff = mask.difference(new Interval<>(from, to));

    final ConcurrentHashMap<String,Connector> dataSourceIdToConnectors = Database.getConnectors(table.schemaClass());
    final Notifier<?>[] notifiers = getNotifiers(dataSourceIdToConnectors.values().iterator(), 0);
    if (notifiers == null)
      return null;

    try (final RowIterator<? extends data.Table> rows =
      SELECT(table).
      FROM(table).
      WHERE(where(diff))
        .execute(dataSourceIdToConnectors.get(null))) {
      while (rows.nextRow()) {
        final data.Table row = rows.nextEntity();
        for (final Notifier<?> notifier : notifiers) // [A]
          notifier.onSelect(row);
      }
    }

    return null;
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public int size() {
    return map.size();
  }

  @Override
  public boolean containsKey(final Object key) {
    return map.containsKey(key);
  }

  @Override
  public boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }

  @Override
  public Comparator<? super data.Key> comparator() {
    return map.comparator();
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
  public void putAll(final Map<? extends data.Key,? extends V> map) {
    this.map.putAll(map);
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
  public String toString() {
    return map.toString();
  }

  @Override
  public V put(final data.Key key, V value) {
    return map.put(key, value);
  }

  @Override
  public V remove(final Object key) {
    return map.remove(key);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public java.util.Map.Entry<data.Key,V> firstEntry() {
    return map.firstEntry();
  }

  @Override
  public java.util.Map.Entry<data.Key,V> lastEntry() {
    return map.lastEntry();
  }

  @Override
  public java.util.Map.Entry<data.Key,V> pollFirstEntry() {
    return map.pollFirstEntry();
  }

  @Override
  public java.util.Map.Entry<data.Key,V> pollLastEntry() {
    return map.pollLastEntry();
  }

  @Override
  public java.util.Map.Entry<data.Key,V> lowerEntry(final data.Key key) {
    return map.lowerEntry(key);
  }

  @Override
  public data.Key lowerKey(final data.Key key) {
    return map.lowerKey(key);
  }

  @Override
  public java.util.Map.Entry<data.Key,V> floorEntry(final data.Key key) {
    return map.floorEntry(key);
  }

  @Override
  public data.Key floorKey(final data.Key key) {
    return map.floorKey(key);
  }

  @Override
  public java.util.Map.Entry<data.Key,V> ceilingEntry(final data.Key key) {
    return map.ceilingEntry(key);
  }

  @Override
  public V getOrDefault(final Object key, V defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public data.Key ceilingKey(final data.Key key) {
    return map.ceilingKey(key);
  }

  @Override
  public java.util.Map.Entry<data.Key,V> higherEntry(final data.Key key) {
    return map.higherEntry(key);
  }

  @Override
  public data.Key higherKey(final data.Key key) {
    return map.higherKey(key);
  }

  @Override
  public Set<data.Key> keySet() {
    return map.keySet();
  }

  @Override
  public NavigableSet<data.Key> navigableKeySet() {
    return map.navigableKeySet();
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
  public Set<java.util.Map.Entry<data.Key,V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public NavigableMap<data.Key,V> descendingMap() {
    return map.descendingMap();
  }

  @Override
  public V putIfAbsent(final data.Key key, V value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public NavigableMap<data.Key,V> subMap(final data.Key fromKey, final boolean fromInclusive, final data.Key toKey, final boolean toInclusive) {
    return map.subMap(fromKey, fromInclusive, toKey, toInclusive);
  }

  @Override
  public NavigableMap<data.Key,V> headMap(final data.Key toKey, final boolean inclusive) {
    return map.headMap(toKey, inclusive);
  }

  @Override
  public NavigableMap<data.Key,V> tailMap(final data.Key fromKey, final boolean inclusive) {
    return map.tailMap(fromKey, inclusive);
  }

  @Override
  public SortedMap<data.Key,V> subMap(final data.Key fromKey, final data.Key toKey) {
    return map.subMap(fromKey, toKey);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return map.remove(key, value);
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
  public boolean replace(final data.Key key, V oldValue, V newValue) {
    return map.replace(key, oldValue, newValue);
  }

  @Override
  public V replace(final data.Key key, V value) {
    return map.replace(key, value);
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
  public V merge(final data.Key key, V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return map.merge(key, value, remappingFunction);
  }

  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
  }
}