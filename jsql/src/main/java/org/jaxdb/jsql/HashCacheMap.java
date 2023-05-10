package org.jaxdb.jsql;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import org.mapdb.HTreeMap;

public class HashCacheMap<V> extends CacheMap<V> {
  final String name;
  final Map<data.Key,V> map;
  private final HashSet<type.Key> mask = new HashSet<>();

  @SuppressWarnings("unchecked")
  HashCacheMap(final data.Table table, final String name) {
    this(table, name, (HTreeMap<data.Key,V>)db.hashMap(name).counterEnable().create());
  }

  private HashCacheMap(final data.Table table, final String name, final Map<data.Key,V> map) {
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

  V put(final data.Key key, final V value, final boolean addKey) {
    return put(key, value);
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
  public boolean containsValue(final Object value) {
    return map.containsValue(value);
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }

  @Override
  public V getOrDefault(final Object key, final V defaultValue) {
    return map.getOrDefault(key, defaultValue);
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