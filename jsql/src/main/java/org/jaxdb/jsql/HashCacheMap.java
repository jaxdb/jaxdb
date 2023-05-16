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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mapdb.HTreeMap;

public abstract class HashCacheMap<V> extends CacheMap<V> {
  final String name;
  final Map<data.Key,V> map;
  final HashSet<type.Key> mask = new HashSet<>();

  @SuppressWarnings("unchecked")
  HashCacheMap(final data.Table table, final String name) {
    this(table, name, (HTreeMap<data.Key,V>)db.hashMap(name).counterEnable().create());
  }

  HashCacheMap(final data.Table table, final String name, final Map<data.Key,V> map) {
    super(table);
    this.name = name;
    this.map = map;
  }

  abstract HashCacheMap<V> newInstance(data.Table table, String name, Map<data.Key,V> map);

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

  @Override
  final V superGet(final data.Key key) {
    return map.get(key);
  }

  @Override
  final V superPut(final data.Key key, final V value) {
    mask.add(key);
    return map.put(key, value);
  }

  @Override
  final V superRemove(final type.Key key) {
    mask.remove(key);
    return map.remove(key);
  }

  @Override
  final V superRemoveOld(final type.Key key) {
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
    return map.isEmpty();
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