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

import java.util.Map;

public class OneToManyHashMap<V extends data.Table> extends HashCacheMap<Map<data.Key,V>> implements OneToManyMap<Map<data.Key,V>> {
  OneToManyHashMap(final data.Table table) {
    super(table);
  }

  private OneToManyHashMap(final data.Table table, final Map<data.Key,Map<data.Key,V>> map) {
    super(table, table.getSchema(), map);
  }

  @Override
  HashCacheMap<Map<data.Key,V>> newInstance(final data.Table table, final Map<data.Key,Map<data.Key,V>> map) {
    return new OneToManyHashMap<>(table, map);
  }

  @Override
  public final OneToOneHashMap<V> get(final Object key) {
    final OneToOneHashMap<V> v = (OneToOneHashMap<V>)map.get(key);
    return v != null ? v : OneToOneHashMap.EMPTY;
  }

  final void add$(final data.Key key, final V value) {
    // mask.add(key); Do not add key, because this is only 1 value being added of many

    OneToOneHashMap<V> v = (OneToOneHashMap<V>)map.get(key);
    if (v == null)
      map.put(key, v = new OneToOneHashMap<>(table));

    v.put$(value.getKey(), value);
  }

  final void remove$(final data.Key key, final V value) {
    final OneToOneHashMap<V> v = (OneToOneHashMap<V>)map.get(key);
    if (v != null)
      v.remove$(value.getKey());
  }

  final void remove$Old(final data.Key key, final V value) {
    final OneToOneHashMap<V> v = (OneToOneHashMap<V>)map.get(key);
    if (v != null)
      v.remove$(value.getKeyOld());
  }
}