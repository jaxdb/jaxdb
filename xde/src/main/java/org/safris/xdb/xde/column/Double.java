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

package org.safris.xdb.xde.column;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Table;

public final class Double extends Column<java.lang.Double> {
  protected static final int sqlType = Types.DOUBLE;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final java.lang.Double value) throws SQLException {
    statement.setDouble(parameterIndex, value);
  }

  public final int precision;
  public int decimal;
  public final boolean unsigned;
  public final java.lang.Double min;
  public final java.lang.Double max;

  public Double(final Table owner, final String csqlName, final String name, final java.lang.Double _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final int decimal, final boolean unsigned, final java.lang.Double min, final java.lang.Double max) {
    super(sqlType, java.lang.Double.class, owner, csqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }

  protected Double(final Double column) {
    super(column);
    this.precision = column.precision;
    this.unsigned = column.unsigned;
    this.min = column.min;
    this.max = column.max;
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  protected java.lang.Double get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final double value = resultSet.getDouble(columnIndex);
    return resultSet.wasNull() ? null : value;
  }
}