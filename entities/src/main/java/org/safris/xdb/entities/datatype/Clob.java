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

package org.safris.xdb.entities.datatype;

import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.safris.xdb.entities.DataType;
import org.safris.xdb.entities.Entity;
import org.safris.xdb.entities.GenerateOn;
import org.safris.xdb.schema.DBVendor;

public final class Clob extends DataType<Reader> {
  protected static Reader get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final java.sql.Clob clob = resultSet.getClob(columnIndex);
    return clob == null ? null : clob.getCharacterStream();
  }

  protected static void set(final PreparedStatement statement, final int parameterIndex, final Reader value) throws SQLException {
    if (value != null)
      statement.setClob(parameterIndex, value);
    else
      statement.setNull(parameterIndex, Types.CLOB);
  }

  public final int length;
  public final boolean national;

  public Clob(final Entity owner, final String specName, final String name, final Reader _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final int length, final boolean national) {
    super(national ? Types.NCLOB : Types.CLOB, Reader.class, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    this.length = length;
    this.national = national;
  }

  public Clob() {
    this(null, null, null, null, false, false, true, null, null, Integer.MAX_VALUE, false);
  }

  protected Clob(final Clob copy) {
    super(copy);
    this.length = copy.length;
    this.national = copy.national;
  }

  @Override
  protected String getPreparedStatementMark(final DBVendor vendor) {
    return "?";
  }

  @Override
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  @Override
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    this.value = get(resultSet, columnIndex);
  }
}