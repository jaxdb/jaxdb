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

public class OneToManyHashMap<V extends data.Table<?>> extends HashMap<data.Key,Map<data.Key,V>> implements OneToManyMap<Map<data.Key,V>> {
  private static final HashMap EMPTY = new HashMap(0) {
    @Override
    public Object put(final Object key, final Object value) {
      throw new UnsupportedOperationException();
    }
  };

  @Override
  public Map<data.Key,V> superGet(final data.Key key) {
    return super.get(key);
  }

  @Override
  public Map<data.Key,V> get(final Object key) {
    final Map<data.Key,V> v = super.get(key);
    return v != null ? v : EMPTY;
  }

  public void add(final data.Key key, final V value) {
    Map<data.Key,V> set = super.get(key);
    if (set == null)
      super.put(key, set = new HashMap<>());

    set.put(value.getKey().immutable(), value);
  }

  void remove(final type.Key key, final V value) {
    final Map<data.Key,V> set = super.get(key);
    if (set != null)
      set.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final Map<data.Key,V> set = super.get(key);
    if (set != null)
      set.remove(value.getKeyOld());
  }
}