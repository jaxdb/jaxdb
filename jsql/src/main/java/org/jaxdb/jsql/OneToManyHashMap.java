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

public class OneToManyHashMap<V extends data.Table> extends HashCacheMap<Map<data.Key,V>> implements OneToManyMap<Map<data.Key,V>> {
  OneToManyHashMap(final data.Table table, final String name) {
    super(table, name);
  }

  @SuppressWarnings("unchecked")
  void add(final data.Key key, final V value, final boolean addKey) {
    Map<data.Key,V> valueMap = map.get(key);
    if (valueMap == null)
      map.put(key, valueMap = (Map<data.Key,V>)db.hashMap(name + ":" + key).counterEnable().create());

    valueMap.put(value.getKey().immutable(), value);
  }

  void remove(final type.Key key, final V value) {
    final Map<data.Key,V> set = map.get(key);
    if (set != null)
      set.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final Map<data.Key,V> set = map.get(key);
    if (set != null)
      set.remove(value.getKeyOld());
  }
}