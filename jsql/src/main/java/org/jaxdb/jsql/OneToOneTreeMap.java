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

public class OneToOneTreeMap<V extends data.Table> extends NavigableCacheMap<V> implements OneToOneMap<V> {
  OneToOneTreeMap(final data.Table table, final String name) {
    super(table, name);
  }

  final V put(final data.Key key, final V value, final boolean addKey) {
    if (addKey)
      mask.add(key);

    return put(key, value);
  }

  @Override
  public boolean containsKey(final Object key) {
    return containsKey((data.Key)key);
  }
}