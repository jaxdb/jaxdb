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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.libj.util.ConcurrentHashSet;
import org.libj.util.Interval;

public abstract class HashCacheMap<V> extends CacheMap<V> {
  final static class KeyConcurrentHashSet extends ConcurrentHashSet<data.Key> {
    private boolean all;

    @Override
    public boolean add(final data.Key e) {
      return e == data.Key.ALL ? all = true : super.add(e);
    }

    void addAll(final data.Key[] keys) {
      for (final data.Key key : keys) // [A]
        add(key);
    }

    void addAll(final Interval<data.Key>[] keys) {
      for (final Interval<data.Key> key : keys) // [A]
        add((data.Key)key);
    }

    @Override
    public boolean contains(final Object o) {
      return all || super.contains(o);
    }
  }

  final KeyConcurrentHashSet mask = new KeyConcurrentHashSet();
  final Map<data.Key,V> map;

  HashCacheMap(final data.Table table) {
    this(table, table.getSchema(), new ConcurrentHashMap<>());
  }

  HashCacheMap(final data.Table table, final Schema schema, final Map<data.Key,V> map) {
    super(table, schema);
    this.map = map;
  }

  abstract HashCacheMap<V> newInstance(data.Table table, Map<data.Key,V> map);

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
    mask.remove(key);
    return map.remove(key);
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
  public final Set<Map.Entry<data.Key,V>> entrySet() {
    // FIXME: Must disallow Map.Entry.setValue()
    return map.entrySet();
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
  public final boolean isEmpty() {
    return map.size() == 0;
  }

  @Override
  public final Set<data.Key> keySet() {
    return map.keySet();
  }

  @Override
  public final int size() {
    return map.size();
  }

  @Override
  public final String toString() {
    return map.toString();
  }

  @Override
  public final Collection<V> values() {
    return map.values();
  }
}