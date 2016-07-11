/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde.datatype;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xdl.DBVendor;
import org.safris.xdb.xde.DataType;
import org.safris.xdb.xde.Entity;

public final class Blob extends DataType<byte[]> {
  protected static final int sqlType = Types.BLOB;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final byte[] value) throws SQLException {
    statement.setBinaryStream(parameterIndex, new ByteArrayInputStream(value));
  }

  public Blob(final Entity owner, final String specName, final String name, final byte[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<byte[]> generateOnInsert, final GenerateOn<byte[]> generateOnUpdate) {
    super(sqlType, byte[].class, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected Blob(final Blob column) {
    super(column);
  }

  @Override
  protected String getPreparedStatementMark(final DBVendor vendor) {
    return "?";
  }

  @Override
  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  @Override
  protected byte[] get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getBytes(columnIndex);
  }
}