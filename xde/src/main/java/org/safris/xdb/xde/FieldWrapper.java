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

package org.safris.xdb.xde;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;

class FieldWrapper<T> extends Field<T> {
  private static final ThreadLocal<DateFormat> dateFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd");
    }
  };

  private static final ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>() {
    @Override
    protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    }
  };

  protected static String toString(final Object obj) {
    if (obj == null)
      return "NULL";

    if (obj instanceof String)
      return "'" + obj + "'";

    if (obj instanceof LocalDate)
      return "'" + dateFormat.get().format(((LocalDate)obj).toDate()) + "'";

    if (obj instanceof LocalDateTime)
      return "'" + dateTimeFormat.get().format(((LocalDateTime)obj).toDate()) + "'";

    return obj.toString();
  }

  public FieldWrapper(final T value) {
    super(value);
  }

  @Override
  protected Entity entity() {
    return null;
  }

  @Override
  protected void serialize(final Serialization serialization) {
    if (get() == null) {
      serialization.sql.append("NULL");
    }
    else if (serialization.statementType == PreparedStatement.class) {
      serialization.addParameter(get());
      serialization.sql.append("?");
    }
    else {
      serialization.sql.append(toString(get()));
    }
  }

  @Override
  public String toString() {
    return toString(get());
  }

  @Override
  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final Object value = get();
    DataType.set(statement, parameterIndex, value.getClass(), value);
  }

  @Override
  protected T get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("not implemented");
  }
}