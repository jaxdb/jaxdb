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

public class OneToManyTreeMap<V extends data.Table> extends NavigableCacheMap<NavigableCacheMap<V>> implements OneToManyMap<NavigableCacheMap<V>> {
  OneToManyTreeMap(final data.Table table, final String name) {
    super(table, name);
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

  void add(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    NavigableCacheMap<V> valueMap = map.get(key);
    if (valueMap == null)
      map.put(key, valueMap = new NavigableCacheMap<>(table, name + ":" + key));

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