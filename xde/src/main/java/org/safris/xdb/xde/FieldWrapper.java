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
    protected DateFormat initialValue() {
      return new SimpleDateFormat("yyyy-MM-dd");
    }
  };

  private static final ThreadLocal<DateFormat> dateTimeFormat = new ThreadLocal<DateFormat>() {
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

  protected Entity entity() {
    return null;
  }

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

  public String toString() {
    return toString(get());
  }

  protected void set(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final Object value = get();
    DataType.set(statement, parameterIndex, value.getClass(), value);
  }

  protected T get(final ResultSet resultSet, final int columnIndex) throws SQLException {
    throw new UnsupportedOperationException("not implemented");
  }
}