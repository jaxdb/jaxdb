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

import org.mapdb.DB;
import org.mapdb.DBMaker;

abstract class RelationMap<V> implements Map<data.Key,V> {
  static final DB db = DBMaker.heapDB().make();

  data.Table table;

  RelationMap(final data.Table table) {
    this.table = table;
  }

  public abstract boolean containsKey(final data.Key key);

  V superGet(final data.Key key) {
    return get(key);
  }

  final V removeOld(final type.Key key) {
    return remove(key);
  }

  @Override
  public final boolean remove(final Object key, final Object value) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final V remove(final Object key) {
    throw new UnsupportedOperationException();
  }

  @Override
  public final Object clone() {
    throw new UnsupportedOperationException();
  }
}