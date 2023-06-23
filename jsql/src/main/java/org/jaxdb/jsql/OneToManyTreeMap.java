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

public class OneToManyTreeMap<V extends data.Table> extends TreeCacheMap<NavigableMap<data.Key,V>> implements OneToManyMap<NavigableMap<data.Key,V>> {
  OneToManyTreeMap(final data.Table table) {
    super(table);
  }

  private OneToManyTreeMap(final data.Table table, final NavigableMap<data.Key,NavigableMap<data.Key,V>> map) {
    super(table, table.getSchema(), map);
  }

  @Override
  TreeCacheMap<NavigableMap<data.Key,V>> newInstance(final data.Table table, final NavigableMap<data.Key,NavigableMap<data.Key,V>> map) {
    return new OneToManyTreeMap<>(table, map);
  }

  @Override
  public final NavigableMap<data.Key,V> get(final Object key) {
    final NavigableMap<data.Key,V> v = map.get(key);
    return v != null ? v : OneToOneTreeMap.EMPTY;
  }

  final void add$(final data.Key key, final V value) {
    // mask.add(key); Do not add key, because this is only 1 value being added of many

    OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)map.get(key);
    if (v == null)
      map.put(key, v = new OneToOneTreeMap<>(table));

    v.put$(value.getKey(), value);
  }

  final void remove$(final data.Key key, final V value) {
    final OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)map.get(key);
    if (v != null)
      v.remove$(value.getKey());
  }

  final void remove$Old(final data.Key key, final V value) {
    final OneToOneTreeMap<V> v = (OneToOneTreeMap<V>)map.get(key);
    if (v != null)
      v.remove$(value.getKeyOld());
  }
}