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
import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xde.DataType;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xdl.DBVendor;

public final class DateTime extends DataType<LocalDateTime> {
  protected static final int sqlType = Types.TIMESTAMP;

  protected static void set(final PreparedStatement statement, final int parameterIndex, final LocalDateTime value) throws SQLException {
    statement.setTimestamp(parameterIndex, new Timestamp(value.toDate().getTime()));
  }

  public DateTime(final Entity owner, final String csqlName, final String name, final LocalDateTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<LocalDateTime> generateOnInsert, final GenerateOn<LocalDateTime> generateOnUpdate) {
    super(sqlType, LocalDateTime.class, owner, csqlName, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
  }

  protected DateTime(final DateTime column) {
    super(column);
  }

  protected String getPreparedStatementMark(final DBVendor vendor) {
    return "?";
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    set(statement, parameterIndex, get());
  }

  protected LocalDateTime get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    final java.sql.Timestamp value = resultSet.getTimestamp(columnIndex);
    return value == null ? null : new LocalDateTime(value.getTime());
  }
}