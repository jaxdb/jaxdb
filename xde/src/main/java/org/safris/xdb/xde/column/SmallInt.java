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

public final class SmallInt extends Column<Short> {
  protected static final int sqlType = Types.TINYINT;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final Short value) throws SQLException {
    statement.setShort(parameterIndex, value);
  }

  public final int precision;
  public final boolean unsigned;
  public final Short min;
  public final Short max;

  public SmallInt(final Table owner, final String csqlName, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Short min, final Short max) {
    super(sqlType, Short.class, owner, csqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  protected Short get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final short value = resultSet.getShort(columnIndex);
    return resultSet.wasNull() ? null : value;
  }
}