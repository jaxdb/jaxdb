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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;

import org.joda.time.LocalDateTime;
import org.joda.time.base.BaseLocal;
import org.safris.xdb.schema.DBVendor;
import org.safris.xdb.xde.DataType;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xde.GenerateOn;

public final class DateTime extends DataType<LocalDateTime> {
  protected static final int sqlType = Types.TIMESTAMP;

  protected static LocalDateTime get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final java.sql.Timestamp value = resultSet.getTimestamp(columnIndex);
    return value == null ? null : new LocalDateTime(value.getTime());
  }

  protected static void set(final PreparedStatement statement, final int parameterIndex, final LocalDateTime value) throws SQLException {
    if (value != null)
      statement.setTimestamp(parameterIndex, new Timestamp(value.toDate().getTime()));
    else
      statement.setNull(parameterIndex, sqlType);
  }

  public DateTime(final Entity owner, final String specName, final String name, final LocalDateTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<BaseLocal> generateOnInsert, final GenerateOn<BaseLocal> generateOnUpdate) {
    super(sqlType, LocalDateTime.class, owner, specName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected DateTime(final DateTime copy) {
    super(copy);
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