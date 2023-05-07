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

import java.util.NavigableMap;

import org.mapdb.BTreeMap;

public class OneToManyTreeMap<V extends data.Table> extends NavigableCacheMap<NavigableCacheMap<V>> implements OneToManyMap<NavigableCacheMap<V>> {
  OneToManyTreeMap(final data.Table table) {
    super(table, (BTreeMap<data.Key,NavigableCacheMap<V>>)db.treeMap(String.valueOf(System.identityHashCode(this))).counterEnable().create());
  }

  @Override
  public NavigableCacheMap<V> superGet(final data.Key key) {
    return map.get(key);
  }

  @Override
  public NavigableCacheMap<V> get(final Object key) {
    final NavigableCacheMap<V> subMap = map.get(key);
    return subMap != null ? subMap : EMPTY;
  }

  @SuppressWarnings("unchecked")
  void add(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    NavigableCacheMap<V> valueMap = map.get(key);
    if (valueMap == null)
      map.put(key, valueMap = new NavigableCacheMap<>(table, (NavigableMap<data.Key,V>)db.treeMap(name + ":" + key).counterEnable().create()));

    valueMap.put(value.getKey().immutable(), value);
  }

  void remove(final type.Key key, final V value) {
    final NavigableCacheMap<V> subMap = map.get(key);
    if (subMap != null)
      subMap.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final NavigableCacheMap<V> subMap = map.get(key);
    if (subMap != null)
      subMap.remove(value.getKeyOld());
  }
}