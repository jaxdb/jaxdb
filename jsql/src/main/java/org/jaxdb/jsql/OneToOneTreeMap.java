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

import java.util.NavigableMap;
import java.util.TreeMap;

public class OneToOneTreeMap<V extends data.Table> extends TreeCacheMap<V> implements OneToOneMap<V> {
  @SuppressWarnings({"rawtypes", "unchecked"})
  static final OneToOneTreeMap EMPTY = new OneToOneTreeMap(null, null, new TreeMap<>());

  OneToOneTreeMap(final data.Table table, final String name) {
    super(table, name);
  }

  private OneToOneTreeMap(final data.Table table, final String name, final NavigableMap<data.Key,V> map) {
    super(table, name, map);
  }

  @Override
  TreeCacheMap<V> newInstance(final data.Table table, final String name, final NavigableMap<data.Key,V> map) {
    return new OneToOneTreeMap<>(table, name, map);
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }
}