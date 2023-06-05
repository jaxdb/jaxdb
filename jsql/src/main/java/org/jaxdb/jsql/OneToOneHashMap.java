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

import java.util.HashMap;
import java.util.Map;

public class OneToOneHashMap<V extends data.Table> extends HashCacheMap<V> implements OneToOneMap<V> {
  @SuppressWarnings({"rawtypes", "unchecked"})
  static final OneToOneHashMap EMPTY = new OneToOneHashMap(null, null, new HashMap<>(0));

  OneToOneHashMap(final data.Table table) {
    super(table);
  }

  private OneToOneHashMap(final data.Table table, final Schema schema, final Map<data.Key,V> map) {
    super(table, schema, map);
  }

  @Override
  HashCacheMap<V> newInstance(final data.Table table, final Map<data.Key,V> map) {
    return new OneToOneHashMap<>(table, table.getSchema(), map);
  }

  @Override
  public V get(final Object key) {
    return map.get(key);
  }
}