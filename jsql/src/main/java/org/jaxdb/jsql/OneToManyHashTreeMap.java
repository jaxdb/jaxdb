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
import java.util.NavigableMap;

public class OneToManyHashTreeMap<V extends data.Table> extends HashTreeCacheMap<NavigableMap<data.Key,V>> implements OneToManyMap<NavigableMap<data.Key,V>> {
  OneToManyHashTreeMap(final data.Table table) {
    super(table);
  }

  private OneToManyHashTreeMap(final data.Table table, final Map<data.Key,NavigableMap<data.Key,V>> hashMap, final NavigableMap<data.Key,NavigableMap<data.Key,V>> treeMap) {
    super(table, table.getSchema(), hashMap, treeMap);
  }

  @Override
  HashTreeCacheMap<NavigableMap<data.Key,V>> newInstance(final data.Table table, final Map<data.Key,NavigableMap<data.Key,V>> hashMap, final NavigableMap<data.Key,NavigableMap<data.Key,V>> treeMap) {
    return new OneToManyHashTreeMap<>(table, hashMap, treeMap);
  }

  @Override
  public final NavigableMap<data.Key,V> get(final Object key) {
    final NavigableMap<data.Key,V> v = hashMap.get(key);
    return v != null ? v : OneToOneTreeMap.EMPTY;
  }

  final void add$(final data.Key key, final V value) {
    // mask.add(key); Do not add key, because this is only 1 value being added of many

    OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)hashMap.get(key);
    if (v == null)
      hashMap.put(key, v = new OneToOneTreeMap<>(table));

    v.put$(value.getKey(), value);

    v = (OneToOneTreeMap<V>)treeMap.get(key);
    if (v == null)
      treeMap.put(key, v = new OneToOneTreeMap<>(table));

    v.put$(value.getKey(), value);
  }

  final void remove$(final data.Key key, final V value) {
    OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)hashMap.get(key);
    if (v != null)
      v.remove$(value.getKey());

    v = (OneToOneTreeMap<V>)treeMap.get(key);
    if (v != null)
      v.remove$(value.getKey());
  }

  final void remove$Old(final data.Key key, final V value) {
    OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)hashMap.get(key);
    if (v != null)
      v.remove$(value.getKeyOld());

    v = (OneToOneTreeMap<V>)treeMap.get(key);
    if (v != null)
      v.remove$(value.getKeyOld());
  }
}