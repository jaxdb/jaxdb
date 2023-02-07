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

public class OneToManyTreeMap<V extends data.Table<?>> extends TreeMap<data.Key,NavigableMap<data.Key,V>> implements OneToManyMap<NavigableMap<data.Key,V>> {
  private static final TreeMap EMPTY = new TreeMap() {
    @Override
    public Object put(final Object key, final Object value) {
      throw new UnsupportedOperationException();
    }
  };

  @Override
  public NavigableMap<data.Key,V> superGet(final data.Key key) {
    return super.get(key);
  }

  @Override
  public NavigableMap<data.Key,V> get(final Object key) {
    final NavigableMap<data.Key,V> v = super.get(key);
    return v != null ? v : EMPTY;
  }

  public void add(final data.Key key, final V value) {
    NavigableMap<data.Key,V> set = super.get(key);
    if (set == null)
      super.put(key, set = new TreeMap<>());

    set.put(value.getKey().immutable(), value);
  }

  @Override
  public boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  void remove(final type.Key key, final V value) {
    final NavigableMap<data.Key,V> set = super.get(key);
    if (set != null)
      set.remove(value.getKey());
  }

  void removeOld(final type.Key key, final V value) {
    final NavigableMap<data.Key,V> set = super.get(key);
    if (set != null)
      set.remove(value.getKeyOld());
  }
}