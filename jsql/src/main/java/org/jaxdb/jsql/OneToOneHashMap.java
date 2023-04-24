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

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mapdb.HTreeMap;

public class OneToOneHashMap<V extends data.Table> extends HashMap<data.Key,V> implements OneToOneMap<V> {
  private final String name = String.valueOf(System.identityHashCode(this));
  @SuppressWarnings("unchecked")
  private final HTreeMap<data.Key,V> map = (HTreeMap<data.Key,V>)db.hashMap(name).counterEnable().create();

  private final data.Table table;

  OneToOneHashMap(final data.Table table) {
    this.table = table;
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
  public int size() {
    return map.size();
  }

  @Override
  public boolean isEmpty() {
    return map.isEmpty();
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }

  @Override
  public boolean containsKey(final Object key) {
    return map.containsKey(key);
  }

  @Override
  public V put(final data.Key key, final V value) {
    return map.put(key, value);
  }

  @Override
  public void putAll(final Map<? extends data.Key,? extends V> m) {
    map.putAll(m);
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
  public boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public Set<data.Key> keySet() {
    return map.keySet();
  }

  @Override
  public Collection<V> values() {
    return map.values();
  }

  @Override
  public Set<Entry<data.Key,V>> entrySet() {
    return map.entrySet();
  }

  @Override
  public V getOrDefault(final Object key, V defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public V putIfAbsent(final data.Key key, V value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return map.remove(key, value);
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
  public void forEach(final BiConsumer<? super data.Key,? super V> action) {
    map.forEach(action);
  }

  @Override
  public void replaceAll(final BiFunction<? super data.Key,? super V,? extends V> function) {
    map.replaceAll(function);
  }

  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
  }
}