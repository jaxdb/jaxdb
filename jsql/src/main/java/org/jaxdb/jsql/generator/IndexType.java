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

package org.jaxdb.jsql.generator;

import static org.libj.lang.Assertions.*;

import java.util.Map;
import java.util.NavigableMap;

import org.jaxdb.jsql.OneToManyHashMap;
import org.jaxdb.jsql.OneToManyHashTreeMap;
import org.jaxdb.jsql.OneToManyTreeMap;
import org.jaxdb.jsql.OneToOneHashMap;
import org.jaxdb.jsql.OneToOneHashTreeMap;
import org.jaxdb.jsql.OneToOneTreeMap;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$IndexType;

@SuppressWarnings("rawtypes")
abstract class IndexType {
  static final IndexType UNDEFINED = new UNDEFINED(false);
  static final IndexType UNDEFINED_UNIQUE = new UNDEFINED(true);
  static final IndexType BTREE = new BTREE(false, OneToManyTreeMap.class);
  static final IndexType BTREE_UNIQUE = new BTREE(true, OneToOneTreeMap.class);
  static final IndexType HASH = new HASH(false, OneToManyHashMap.class);
  static final IndexType HASH_UNIQUE = new HASH(true, OneToOneHashMap.class);
  static final IndexType HASH_BTREE = new HASH_BTREE(false, OneToManyHashTreeMap.class);
  static final IndexType HASH_BTREE_UNIQUE = new HASH_BTREE(true, OneToOneHashTreeMap.class);

  static class UNDEFINED extends IndexType {
    UNDEFINED(final boolean unique) {
      super(unique, null, null);
    }

    @Override
    IndexType getNonUnique() {
      return UNDEFINED;
    }

    @Override
    IndexType getUnique() {
      return UNDEFINED_UNIQUE;
    }

    @Override
    boolean isSameStrategy(final IndexType indexType) {
      return indexType == UNDEFINED || indexType == UNDEFINED_UNIQUE;
    }
  }

  static class BTREE extends IndexType {
    BTREE(final boolean unique, final Class<? extends Map> cls) {
      super(unique, cls, NavigableMap.class);
    }

    @Override
    IndexType getNonUnique() {
      return BTREE;
    }

    @Override
    IndexType getUnique() {
      return BTREE_UNIQUE;
    }

    @Override
    boolean isSameStrategy(final IndexType indexType) {
      return indexType == BTREE || indexType == BTREE_UNIQUE;
    }
  }

  static class HASH extends IndexType {
    HASH(final boolean unique, final Class<? extends Map> cls) {
      super(unique, cls, Map.class);
    }

    @Override
    IndexType getNonUnique() {
      return HASH;
    }

    @Override
    IndexType getUnique() {
      return HASH_UNIQUE;
    }

    @Override
    boolean isSameStrategy(final IndexType indexType) {
      return indexType == HASH || indexType == HASH_UNIQUE;
    }
  }

  static class HASH_BTREE extends IndexType {
    HASH_BTREE(final boolean unique, final Class<? extends Map> cls) {
      super(unique, cls, NavigableMap.class);
    }

    @Override
    IndexType getNonUnique() {
      return HASH_BTREE;
    }

    @Override
    IndexType getUnique() {
      return HASH_BTREE_UNIQUE;
    }

    @Override
    boolean isSameStrategy(final IndexType indexType) {
      return indexType == HASH_BTREE || indexType == HASH_BTREE_UNIQUE;
    }
  }

  final boolean unique;
  private final Class<? extends Map> iface;
  private final Class<? extends Map> cls;

  abstract IndexType getNonUnique();
  abstract IndexType getUnique();
  abstract boolean isSameStrategy(IndexType indexType);

  final IndexType merge(final IndexType indexType) {
    if (assertNotNull(indexType) instanceof UNDEFINED || isSameStrategy(indexType))
      return unique ? getUnique() : this;

    return unique ? HASH_BTREE_UNIQUE : HASH_BTREE;
  }

  Class<? extends Map> getInterfaceClass() {
    if (iface == null)
      throw new IllegalStateException();

    return iface;
  }

  Class<? extends Map> getConcreteClass() {
    if (cls == null)
      throw new IllegalStateException();

    return cls;
  }

  IndexType(final boolean unique, final Class<? extends Map> cls, final Class<? extends Map> iface) {
    this.unique = unique;
    this.iface = iface;
    this.cls = cls;
  }

  static IndexType of(final $IndexType indexType, final boolean unique) {
    return of(indexType == null ? null : indexType.text(), unique);
  }

  static IndexType of(final String indexType, final boolean unique) {
    return indexType == null ? (unique ? UNDEFINED_UNIQUE : UNDEFINED) : $IndexType.HASH.text().equals(indexType) ? (unique ? HASH_UNIQUE : HASH) : (unique ? BTREE_UNIQUE : BTREE);
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + (unique ? "_UNIQUE" : "");
  }
}