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

import java.util.Map;

public class OneToManyHashMap<V extends data.Table> extends HashCacheMap<OneToOneHashMap<V>> implements OneToManyMap<OneToOneHashMap<V>> {
  OneToManyHashMap(final data.Table table, final String name) {
    super(table, name);
  }

  private OneToManyHashMap(final data.Table table, final String name, final Map<data.Key,OneToOneHashMap<V>> map) {
    super(table, name, map);
  }

  @Override
  HashCacheMap<OneToOneHashMap<V>> newInstance(final data.Table table, final String name, final Map<data.Key,OneToOneHashMap<V>> map) {
    return new OneToManyHashMap<>(table, name, map);
  }

  @Override
  public final OneToOneHashMap<V> get(final Object key) {
    final OneToOneHashMap<V> v = map.get(key);
    return v != null ? v : OneToOneHashMap.EMPTY;
  }

  final void add(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    OneToOneHashMap<V> v = map.get(key);
    if (v == null)
      map.put(key, v = new OneToOneHashMap<>(table, name + ":" + key));

    v.put(value.getKey().immutable(), value, addKey);
  }

  final void superRemove(final type.Key key, final V value) {
    final OneToOneHashMap<V> v = map.get(key);
    if (v != null)
      v.superRemove(value.getKey());
  }

  final void superRemoveOld(final type.Key key, final V value) {
    final OneToOneHashMap<V> v = map.get(key);
    if (v != null)
      v.superRemove(value.getKeyOld());
  }
}