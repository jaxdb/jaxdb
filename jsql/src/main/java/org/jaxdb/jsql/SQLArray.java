/* Copyright (c) 2017 JAX-DB
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

import java.sql.Array;
import java.sql.ResultSet;
import java.util.Map;

import org.libj.util.ArrayUtil;

final class SQLArray<T> implements Array {
  private final data.ARRAY<T> array;

  SQLArray(final data.ARRAY<T> array) {
    this.array = array;
  }

  @Override
  public String getBaseTypeName() {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getBaseType() {
    return array.column.sqlType();
  }

  @Override
  public Object getArray() {
    return array.valueCur;
  }

  @Override
  public Object getArray(final Map<String,Class<?>> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getArray(final long index, final int count) {
    return ArrayUtil.subArray(array.valueCur, (int)(index - 1), count);
  }

  @Override
  public Object getArray(final long index, final int count, final Map<String,Class<?>> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final Map<String,Class<?>> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final long index, final int count) {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final long index, final int count, final Map<String,Class<?>> map) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void free() {
  }
}