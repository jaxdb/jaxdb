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

public final class Double extends DataType<java.lang.Double> {
  protected static final int sqlType = Types.DOUBLE;

  protected static java.lang.Double get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final double value = resultSet.getDouble(columnIndex);
    return resultSet.wasNull() ? null : value;
  }

  protected static void set(final PreparedStatement statement, final int parameterIndex, final java.lang.Double value) throws SQLException {
    if (value != null)
      statement.setDouble(parameterIndex, value);
    else
      statement.setNull(parameterIndex, sqlType);
  }

  public final int precision;
  public int decimal;
  public final boolean unsigned;
  public final java.lang.Double min;
  public final java.lang.Double max;

  public Double(final Entity owner, final String specName, final String name, final java.lang.Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<java.lang.Double> generateOnInsert, final GenerateOn<java.lang.Double> generateOnUpdate, final int precision, final int decimal, final boolean unsigned, final java.lang.Double min, final java.lang.Double max) {
    super(sqlType, java.lang.Double.class, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }

  protected Double(final Double copy) {
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