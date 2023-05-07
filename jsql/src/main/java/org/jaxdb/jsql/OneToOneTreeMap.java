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

  void addKey(final Interval<data.Key>[] intervals) {
    mask.addAll(intervals);
  }

  V put(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(new Interval<>(key, key.next()));

    return put(key, value);
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
    return AND(new ComparisonPredicate.Gte<>(c, min), new ComparisonPredicate.Lt<>(c, max));
  }

  private static data.BOOLEAN and(final Interval<data.Key> i) {
    data.BOOLEAN and = null;
    final data.Key min = i.getMin();
    final data.Key max = i.getMax();
    and = and(min.column(0), min.value(0), max.value(0));
    for (int j = 1, i$ = min.length(); j < i$; ++j)
      and = AND(and, and(min.column(j), min.value(j), max.value(j)));

    return and;
  }

  private static data.BOOLEAN where(final Interval<data.Key>[] missings) {
    data.BOOLEAN or = and(missings[0]);
    for (int i = 1, i$ = missings.length; i < i$; ++i)
      or = OR(or, and(missings[i]));

    return or;
  }

  @Override
  public SortedMap<data.Key,V> get(final data.Key fromKey, final data.Key toKey) throws IOException, SQLException {
    final Interval<data.Key>[] diff = mask.difference(new Interval<>(fromKey, toKey));
    if (diff.length > 0) {
      final ConcurrentHashMap<String,Connector> dataSourceIdToConnectors = Database.getConnectors(table.getSchema().getClass());
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
            notifier.onSelect(row, false);
        }

        for (final Notifier<?> notifier : notifiers) // [A]
          notifier.onSelectRange(table, diff);
      }
    }

    return subMap(fromKey, toKey);
  }

  @Override
  public boolean containsKey(final data.Key key) {
    return mask.contains(key);
  }

  @Override
  public boolean containsKey(final Object key) {
    return containsKey((data.Key)key);
  }

  @Override
  public boolean containsValue(final V value) {
    return map.containsValue(value);
  }

  @Override
  @SuppressWarnings("unchecked")
  public boolean containsValue(final Object value) {
    return containsValue((V)value);
  }

  @Override
  public V get(final data.Key key) {
    return map.get(key);
  }

  public Iterator<Map.Entry<data.Key,V>> entryIterator(final Interval<data.Key> interval) {
    return map.entryIterator(interval.getMin(), true, interval.getMax(), false);
  }

  @Override
  public Interval<data.Key>[] diffKeys(final Interval<data.Key>[] rangeIntervals) {
    return diffKeys(rangeIntervals, rangeIntervals.length, 0, 0);
  }

  private static final Interval[] empty = {};

  private Interval<data.Key>[] diffKeys(final Interval<data.Key>[] rangeIntervals, final int length, final int index, final int depth) {
    if (index == length)
      return depth == 0 ? empty : new Interval[depth];

    final Interval<data.Key> interval = rangeIntervals[index];
    final Interval<data.Key>[] diff = mask.difference(interval);
    final int len = diff.length;
    final Interval<data.Key>[] allDiff = diffKeys(rangeIntervals, length, index + 1, depth + len);
    System.arraycopy(allDiff, 0, diff, depth, len);
    return allDiff;
  }

  @Override
  public V get(final Object key) {
    return get((data.Key)key);
  }

  @Override
  public V getOrDefault(final data.Key key, final V defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public V getOrDefault(final Object key, final V defaultValue) {
    return getOrDefault((data.Key)key, defaultValue);
  }

  @Override
  public V put(final data.Key key, final V value) {
    return map.put(key, value);
  }

  @Override
  public void putAll(final Map<? extends data.Key,? extends V> map) {
    // FIXME: Can be optimized for maps with masks
    for (final Map.Entry<? extends data.Key,? extends V> entry : map.entrySet())
      this.map.put(entry.getKey(), entry.getValue());
  }

  @Override
  public V putIfAbsent(final data.Key key, final V value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public V remove(final Object key) {
    throw new UnsupportedOperationException();
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
  public void replaceAll(final BiFunction<? super data.Key,? super V,? extends V> function) {
    map.replaceAll(function);
  }

  @Override
  public void clear() {
    map.clear();
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
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<data.Key> keySet() {
    return map.keySet();
  }

  @Override
  public Set<Map.Entry<data.Key,V>> entrySet() {
    return map.entrySet();
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
  public void forEach(final BiConsumer<? super data.Key,? super V> action) {
    map.forEach(action);
  }

  @Override
  public V merge(final data.Key key, final V value, final BiFunction<? super V,? super V,? extends V> remappingFunction) {
    return map.merge(key, value, remappingFunction);
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
  public Map.Entry<data.Key,V> firstEntry() {
    return map.firstEntry();
  }

  @Override
  public Map.Entry<data.Key,V> lastEntry() {
    return map.lastEntry();
  }

  @Override
  public Map.Entry<data.Key,V> pollFirstEntry() {
    return map.pollFirstEntry();
  }

  @Override
  public Map.Entry<data.Key,V> pollLastEntry() {
    return map.pollLastEntry();
  }

  @Override
  public Map.Entry<data.Key,V> lowerEntry(final data.Key key) {
    return map.lowerEntry(key);
  }

  @Override
  public data.Key lowerKey(final data.Key key) {
    return map.lowerKey(key);
  }

  @Override
  public Map.Entry<data.Key,V> floorEntry(final data.Key key) {
    return map.floorEntry(key);
  }

  @Override
  public data.Key floorKey(final data.Key key) {
    return map.floorKey(key);
  }

  @Override
  public Map.Entry<data.Key,V> ceilingEntry(final data.Key key) {
    return map.ceilingEntry(key);
  }

  @Override
  public data.Key ceilingKey(final data.Key key) {
    return map.ceilingKey(key);
  }

  @Override
  public Map.Entry<data.Key,V> higherEntry(final data.Key key) {
    return map.higherEntry(key);
  }

  @Override
  public data.Key higherKey(final data.Key key) {
    return map.higherKey(key);
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
  public NavigableMap<data.Key,V> descendingMap() {
    return map.descendingMap();
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
  public SortedMap<data.Key,V> headMap(final data.Key toKey) {
    return map.headMap(toKey);
  }

  @Override
  public SortedMap<data.Key,V> tailMap(final data.Key fromKey) {
    return map.tailMap(fromKey);
  }

  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
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
}