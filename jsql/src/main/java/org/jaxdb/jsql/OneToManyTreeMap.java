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

public class OneToManyTreeMap<V extends data.Table> extends TreeCacheMap<OneToOneTreeMap<V>> implements OneToManyMap<OneToOneTreeMap<V>> {
  OneToManyTreeMap(final data.Table table, final String name) {
    super(table, name);
  }

  private OneToManyTreeMap(final data.Table table, final String name, final NavigableMap<data.Key,OneToOneTreeMap<V>> map) {
    super(table, name, map);
  }

  @Override
  TreeCacheMap<OneToOneTreeMap<V>> newInstance(final data.Table table, final String name, final NavigableMap<data.Key,OneToOneTreeMap<V>> map) {
    return new OneToManyTreeMap<>(table, name, map);
  }

  @Override
  public final OneToOneTreeMap<V> get(final Object key) {
    final OneToOneTreeMap<V> v = map.get(key);
    return v != null ? v : OneToOneTreeMap.EMPTY;
  }

  final void add(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    OneToOneTreeMap<V> v = map.get(key);
    if (v == null)
      map.put(key, v = new OneToOneTreeMap<>(table, name + ":" + key));

    v.put(value.getKey().immutable(), value, addKey);
  }

  final void superRemove(final type.Key key, final V value) {
    final OneToOneTreeMap<V> v = map.get(key);
    if (v != null)
      v.superRemove(value.getKey());
  }

  final void superRemoveOld(final type.Key key, final V value) {
    final OneToOneTreeMap<V> v = map.get(key);
    if (v != null)
      v.superRemove(value.getKeyOld());
  }
}