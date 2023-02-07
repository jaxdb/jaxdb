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

import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.data.Key;
import org.libj.lang.ObjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class OneToOneHashMap<V extends data.Table<?>> extends ConcurrentHashMap<data.Key,V> implements OneToOneMap<V> {
  private static final Logger logger = LoggerFactory.getLogger(OneToOneHashMap.class);

  @Override
  public V put(final Key key, final V value) {
    if (logger.isTraceEnabled()) logger.trace(OneToOneHashMap.class.getSimpleName() + ".put(" + key + ",<\"" + value.getName() + "\"|" + ObjectUtil.simpleIdentityString(value) + ">:" + value + ")");
    return super.put(key, value);
  }

//  @Override
//  public V put(final data.Key key, final V value) {
//    System.err.println(key + " PUT " + value);
//    return super.put(key, value);
//  }
//
//  @Override
//  public V get(final Object key) {
//    final V v = super.get(key);
//    System.err.println(key + " GET " + v);
//    return v;
//  }
}