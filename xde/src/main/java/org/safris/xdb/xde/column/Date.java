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

import org.joda.time.LocalDate;
import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Table;

public final class Date extends Column<LocalDate> {
  protected static final int sqlType = Types.DATE;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final LocalDate value) throws SQLException {
    statement.setDate(parameterIndex, new java.sql.Date(value.toDate().getTime()));
  }

  public Date(final Table owner, final String csqlName, final String name, final LocalDate _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<LocalDate> generateOnInsert, final GenerateOn<LocalDate> generateOnUpdate) {
    super(sqlType, LocalDate.class, owner, csqlName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected Date(final Date column) {
    super(column);
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  protected LocalDate get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final java.sql.Date value = resultSet.getDate(columnIndex);
    return value == null ? null : new LocalDate(value.getTime());
  }
}