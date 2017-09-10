/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.lib4j.lang.Arrays;

final class SQLArray<T> implements Array {
  private final data.ARRAY<T> array;

  public SQLArray(final data.ARRAY<T> array) {
    this.array = array;
  }

  @Override
  public String getBaseTypeName() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getBaseType() throws SQLException {
    return array.dataType.sqlType();
  }

  @Override
  public Object getArray() throws SQLException {
    return array.value;
  }

  @Override
  public Object getArray(final Map<String,Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public Object getArray(final long index, final int count) throws SQLException {
    return Arrays.subArray(array.value, (int)(index - 1), (int)(index - 1 + count));
  }

  @Override
  public Object getArray(final long index, final int count, final Map<String,Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet() throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final Map<String,Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final long index, final int count) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public ResultSet getResultSet(final long index, final int count, final Map<String,Class<?>> map) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public void free() throws SQLException {
  }
}
