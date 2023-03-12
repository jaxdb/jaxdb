/* Copyright (c) 2022 JAX-DB
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mapdb.HTreeMap;

public class OneToManyHashMap<V extends data.Table<?>> extends HashMap<data.Key,Map<data.Key,V>> implements OneToManyMap<Map<data.Key,V>> {
  private final String name = String.valueOf(System.identityHashCode(this));
  @SuppressWarnings("unchecked")
  private final HTreeMap<data.Key,Map<data.Key,V>> map = (HTreeMap<data.Key,Map<data.Key,V>>)db.hashMap(name).counterEnable().create();

  @SuppressWarnings("rawtypes")
  private static final HashMap EMPTY = new HashMap(0) {
    @Override
    public Object put(final Object key, final Object value) {
      throw new UnsupportedOperationException();
    }
  };

  @Override
  public Map<data.Key,V> superGet(final data.Key key) {
    return map.get(key);
  }

  @Override
  public Map<data.Key,V> get(final Object key) {
    final Map<data.Key,V> v = map.get(key);
    return v != null ? v : EMPTY;
  }

  @SuppressWarnings("unchecked")
  public void add(final data.Key key, final V value) {
    Map<data.Key,V> subMap = map.get(key);
    if (subMap == null)
      map.put(key, subMap = (Map<data.Key,V>)db.hashMap(name + ":" + key).counterEnable().create());

    subMap.put(value.getKey().immutable(), value);
  }

  void remove(final type.Key key, final V value) {
    final Map<data.Key,V> set = map.get(key);
    if (set != null)
      set.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final Map<data.Key,V> set = map.get(key);
    if (set != null)
      set.remove(value.getKeyOld());
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
  public boolean containsKey(final Object key) {
    return map.containsKey(key);
  }

  @Override
  public Map<data.Key,V> put(final data.Key key, Map<data.Key,V> value) {
    return map.put(key, value);
  }

  @Override
  public void putAll(final Map<? extends data.Key,? extends Map<data.Key,V>> m) {
    map.putAll(m);
  }

  @Override
  public Map<data.Key,V> remove(final Object key) {
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
  public Collection<Map<data.Key,V>> values() {
    return map.values();
  }

  @Override
  public Set<Entry<data.Key,Map<data.Key,V>>> entrySet() {
    return map.entrySet();
  }

  @Override
  public Map<data.Key,V> getOrDefault(final Object key, Map<data.Key,V> defaultValue) {
    return map.getOrDefault(key, defaultValue);
  }

  @Override
  public Map<data.Key,V> putIfAbsent(final data.Key key, Map<data.Key,V> value) {
    return map.putIfAbsent(key, value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    return map.remove(key, value);
  }

  @Override
  public boolean replace(final data.Key key, Map<data.Key,V> oldValue, Map<data.Key,V> newValue) {
    return map.replace(key, oldValue, newValue);
  }

  @Override
  public Map<data.Key,V> replace(final data.Key key, Map<data.Key,V> value) {
    return map.replace(key, value);
  }

  @Override
  public Map<data.Key,V> computeIfAbsent(final data.Key key, final Function<? super data.Key,? extends Map<data.Key,V>> mappingFunction) {
    return map.computeIfAbsent(key, mappingFunction);
  }

  @Override
  public Map<data.Key,V> computeIfPresent(final data.Key key, final BiFunction<? super data.Key,? super Map<data.Key,V>,? extends Map<data.Key,V>> remappingFunction) {
    return map.computeIfPresent(key, remappingFunction);
  }

  @Override
  public Map<data.Key,V> compute(final data.Key key, final BiFunction<? super data.Key,? super Map<data.Key,V>,? extends Map<data.Key,V>> remappingFunction) {
    return map.compute(key, remappingFunction);
  }

  @Override
  public Map<data.Key,V> merge(final data.Key key, Map<data.Key,V> value, final BiFunction<? super Map<data.Key,V>,? super Map<data.Key,V>,? extends Map<data.Key,V>> remappingFunction) {
    return map.merge(key, value, remappingFunction);
  }

  @Override
  public void forEach(final BiConsumer<? super data.Key,? super Map<data.Key,V>> action) {
    map.forEach(action);
  }

  @Override
  public void replaceAll(final BiFunction<? super data.Key,? super Map<data.Key,V>,? extends Map<data.Key,V>> function) {
    map.replaceAll(function);
  }

  @Override
  public Object clone() {
    throw new UnsupportedOperationException();
  }
}