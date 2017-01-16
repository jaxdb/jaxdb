/* Copyright (c) 2016 Seva Safris
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

package org.safris.xdb.entities;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

class VariableWrapper<T> extends Variable<T> {
  private static final DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
  private static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

  protected static String toString(final Object obj) {
    if (obj == null)
      return "NULL";

    if (obj instanceof Object[]) {
      final StringBuilder buffer = new StringBuilder("(");
      final Object[] arr = (Object[])obj;
      if (arr.length > 0) {
        buffer.append(toString(arr[0]));
        for (int i = 1; i < arr.length; i++)
          buffer.append(", ").append(toString(arr[0]));
      }

      buffer.append(")");
      return buffer.toString();
    }

    if (obj instanceof String)
      return "'" + obj + "'";

    if (obj instanceof LocalDate)
      return "'" + dateFormat.format((LocalDate)obj) + "'";

    if (obj instanceof LocalDateTime)
      return "'" + dateTimeFormat.format((LocalDateTime)obj) + "'";

    return obj.toString();
  }

  protected VariableWrapper(final T value) {
    super(value);
  }

  @Override
  protected Entity owner() {
    return null;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    serialization.addCaller(this);
    if (get() == null) {
      serialization.append("NULL");
    }
    else if (serialization.statementType == PreparedStatement.class) {
      serialization.addParameter(this);
      serialization.append("?");
    }
    else {
      serialization.append(toString(get()));
    }
  }

  @Override
  protected String serialize() {
    return toString(get());
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    DataType.set(statement, parameterIndex, (Class<T>)value.getClass(), get());
  }

  @Override
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException();
  }

  @Override
  public String toString() {
    return serialize();
  }
}