/* Copyright (c) 2022 JAX-DB
 *
 * Permission is hereby granted, final free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mapdb.BTreeMap;

public class OneToManyTreeMap<V extends data.Table> extends NavigableRelationMap<NavigableMap<data.Key,V>> implements OneToManyMap<NavigableMap<data.Key,V>> {
  private final String name = String.valueOf(System.identityHashCode(this));
  @SuppressWarnings("unchecked")
  private final BTreeMap<data.Key,NavigableMap<data.Key,V>> map = (BTreeMap<data.Key,NavigableMap<data.Key,V>>)db.treeMap(name).counterEnable().create();

  OneToManyTreeMap(final data.Table table) {
    super(table);
  }

  @SuppressWarnings("rawtypes")
  private static final TreeMap EMPTY = new TreeMap() {
    @Override
    public Object put(final Object key, final Object value) {
      throw new UnsupportedOperationException();
    }
  };

  @Override
  public NavigableMap<data.Key,V> superGet(final data.Key key) {
    return map.get(key);
  }

  @Override
  public NavigableMap<data.Key,V> get(final Object key) {
    final NavigableMap<data.Key,V> subMap = map.get(key);
    return subMap != null ? subMap : EMPTY;
  }

  @SuppressWarnings("unchecked")
  void add(final data.Key key, final V value, final boolean addKey) {
    NavigableMap<data.Key,V> subMap = map.get(key);
    if (subMap == null)
      map.put(key, subMap = (NavigableMap<data.Key,V>)db.treeMap(name + ":" + key).counterEnable().create());

    subMap.put(value.getKey().immutable(), value);
  }

  void remove(final type.Key key, final V value) {
    final NavigableMap<data.Key,V> subMap = map.get(key);
    if (subMap != null)
      subMap.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final NavigableMap<data.Key,V> subMap = map.get(key);
    if (subMap != null)
      subMap.remove(value.getKeyOld());
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
  public void putAll(final Map<? extends data.Key,? extends NavigableMap<data.Key,V>> map) {
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
  public NavigableMap<data.Key,V> put(final data.Key key, NavigableMap<data.Key,V> value) {
    return map.put(key, value);
  }

  @Override
  public void clear() {
    map.clear();
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> firstEntry() {
    return map.firstEntry();
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> lastEntry() {
    return map.lastEntry();
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> pollFirstEntry() {
    return map.pollFirstEntry();
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> pollLastEntry() {
    return map.pollLastEntry();
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> lowerEntry(final data.Key key) {
    return map.lowerEntry(key);
  }

  @Override
  public data.Key lowerKey(final data.Key key) {
    return map.lowerKey(key);
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> floorEntry(final data.Key key) {
    return map.floorEntry(key);
  }

  @Override
  public data.Key floorKey(final data.Key key) {
    return map.floorKey(key);
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> ceilingEntry(final data.Key key) {
    return map.ceilingEntry(key);
  }

  @Override
  public NavigableMap<data.Key,V> getOrDefault(final Object key, NavigableMap<data.Key,V> defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public data.Key ceilingKey(final data.Key key) {
    return map.ceilingKey(key);
  }

  @Override
  public Map.Entry<data.Key,NavigableMap<data.Key,V>> higherEntry(final data.Key key) {
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
  public Collection<NavigableMap<data.Key,V>> values() {
    return map.values();
  }

  @Override
  public Set<Map.Entry<data.Key,NavigableMap<data.Key,V>>> entrySet() {
    return map.entrySet();
  }

  @Override
  public NavigableMap<data.Key,NavigableMap<data.Key,V>> descendingMap() {
    return map.descendingMap();
  }

  @Override
  public NavigableMap<data.Key,V> putIfAbsent(final data.Key key, NavigableMap<data.Key,V> value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public NavigableMap<data.Key,NavigableMap<data.Key,V>> subMap(final data.Key fromKey, final boolean fromInclusive, final data.Key toKey, final boolean toInclusive) {
    return map.subMap(fromKey, fromInclusive, toKey, toInclusive);
  }

  @Override
  public NavigableMap<data.Key,NavigableMap<data.Key,V>> headMap(final data.Key toKey, final boolean inclusive) {
    return map.headMap(toKey, inclusive);
  }

  @Override
  public NavigableMap<data.Key,NavigableMap<data.Key,V>> tailMap(final data.Key fromKey, final boolean inclusive) {
    return map.tailMap(fromKey, inclusive);
  }

  @Override
  public SortedMap<data.Key,NavigableMap<data.Key,V>> subMap(final data.Key fromKey, final data.Key toKey) {
    return map.subMap(fromKey, toKey);
  }

  @Override
  public SortedMap<data.Key,NavigableMap<data.Key,V>> headMap(final data.Key toKey) {
    return map.headMap(toKey);
  }

  @Override
  public SortedMap<data.Key,NavigableMap<data.Key,V>> tailMap(final data.Key fromKey) {
    return map.tailMap(fromKey);
  }

  @Override
  public boolean replace(final data.Key key, NavigableMap<data.Key,V> oldValue, NavigableMap<data.Key,V> newValue) {
    return map.replace(key, oldValue, newValue);
  }

  @Override
  public NavigableMap<data.Key,V> replace(final data.Key key, NavigableMap<data.Key,V> value) {
    return map.replace(key, value);
  }

  @Override
  public void forEach(final BiConsumer<? super data.Key,? super NavigableMap<data.Key,V>> action) {
    map.forEach(action);
  }

  @Override
  public void replaceAll(final BiFunction<? super data.Key,? super NavigableMap<data.Key,V>,? extends NavigableMap<data.Key,V>> function) {
    map.replaceAll(function);
  }

  @Override
  public NavigableMap<data.Key,V> computeIfAbsent(final data.Key key, final Function<? super data.Key,? extends NavigableMap<data.Key,V>> mappingFunction) {
    return map.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public NavigableMap<data.Key,V> computeIfPresent(final data.Key key, final BiFunction<? super data.Key,? super NavigableMap<data.Key,V>,? extends NavigableMap<data.Key,V>> remappingFunction) {
    return map.computeIfPresent(key, remappingFunction);
  }

  @Override
  public NavigableMap<data.Key,V> compute(final data.Key key, final BiFunction<? super data.Key,? super NavigableMap<data.Key,V>,? extends NavigableMap<data.Key,V>> remappingFunction) {
    return map.compute(key, remappingFunction);
  }

  @Override
  public NavigableMap<data.Key,V> merge(final data.Key key, NavigableMap<data.Key,V> value, final BiFunction<? super NavigableMap<data.Key,V>,? super NavigableMap<data.Key,V>,? extends NavigableMap<data.Key,V>> remappingFunction) {
    return map.merge(key, value, remappingFunction);
  }
}