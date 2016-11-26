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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.safris.xdb.entities.DataType;
import org.safris.xdb.entities.Entity;
import org.safris.xdb.entities.GenerateOn;
import org.safris.xdb.schema.DBVendor;

public final class Float extends DataType<java.lang.Float> {
  protected static final int sqlType = Types.FLOAT;

  protected static java.lang.Float get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final float value = resultSet.getFloat(columnIndex);
    return resultSet.wasNull() ? null : value;
  }

  protected static void set(final PreparedStatement statement, final int parameterIndex, final java.lang.Float value) throws SQLException {
    if (value != null)
      statement.setFloat(parameterIndex, value);
    else
      statement.setNull(parameterIndex, sqlType);
  }

  public final int precision;
  public int decimal;
  public final boolean unsigned;
  public final java.lang.Float min;
  public final java.lang.Float max;

  public Float(final Entity owner, final String specName, final String name, final java.lang.Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<java.lang.Float> generateOnInsert, final GenerateOn<java.lang.Float> generateOnUpdate, final int precision, final int decimal, final boolean unsigned, final java.lang.Float min, final java.lang.Float max) {
    super(sqlType, java.lang.Float.class, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }

  protected Float(final Float copy) {
    super(copy);
    this.precision = copy.precision;
    this.unsigned = copy.unsigned;
    this.min = copy.min;
    this.max = copy.max;
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