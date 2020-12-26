/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb.jsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.regex.Pattern;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;

final class SQLiteCompiler extends Compiler {
  private static final Pattern dateTimePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,7})?");

  @Override
  public DBVendor getVendor() {
    return DBVendor.SQLITE;
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
  }

  @Override
  void compile(final Cast.AS as, final Compilation compilation) throws IOException {
    if (as.cast instanceof type.Temporal) {
      compilation.append("STRFTIME(\"");
      if (as.cast instanceof type.DATE) {
        compilation.append("%Y-%m-%d");
      }
      else if (as.cast instanceof type.TIME) {
        compilation.append("%H:%M:");
        compilation.append(((type.TIME)as.cast).precision() > 0 ? "%f" : "%S");
      }
      else if (as.cast instanceof type.DATETIME) {
        compilation.append("%Y-%m-%d %H:%M:");
        compilation.append(((type.DATETIME)as.cast).precision() > 0 ? "%f" : "%S");
      }
      else {
        throw new UnsupportedOperationException("Unsupported type.Temporal type: " + as.cast.getClass());
      }

      compilation.append("\", (");
      compilable(as.dataType).compile(compilation);
      compilation.append("))");
    }
    else {
      super.compile(as, compilation);
    }
  }

  @Override
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    if (expression.a instanceof type.DATE)
      compilation.append("DATE(");
    else if (expression.a instanceof type.TIME)
      compilation.append("TIME(");
    else if (expression.a instanceof type.DATETIME)
      compilation.append("DATETIME(");
    else
      throw new UnsupportedOperationException("Unsupported type: " + expression.a.getClass());

    expression.a.compile(compilation);
    compilation.append(", '").append(expression.operator);
    expression.b.compile(compilation);
    compilation.append("')");
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    for (int i = 0; i < units.size(); ++i) {
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.append(' ');

      final long component;
      final String unitString;
      if (unit == Interval.Unit.WEEKS) {
        component = interval.get(unit) * 7;
        unitString = "DAYS";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = interval.get(unit) * 3;
        unitString = "MONTHS";
      }
      else if (unit == Interval.Unit.DECADES) {
        component = interval.get(unit) * 10;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = interval.get(unit) * 100;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = interval.get(unit) * 1000;
        unitString = "YEAR";
      }
      else {
        component = interval.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }

      compilation.append(component).append(' ').append(unitString);
    }
  }

  @Override
  void compile(final function.Mod function, final Compilation compilation) throws IOException {
    compilation.append('(');
    function.a.compile(compilation);
    compilation.append(" % ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  @Override
  void compile(final function.Ln function, final Compilation compilation) throws IOException {
    compilation.append("LOG(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log function, final Compilation compilation) throws IOException {
    compilation.append("LOG(");
    function.b.compile(compilation);
    compilation.append(") / LOG(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log2 function, final Compilation compilation) throws IOException {
    compilation.append("LOG(");
    function.a.compile(compilation);
    compilation.append(") / 0.6931471805599453");
  }

  @Override
  void setParameter(final type.CLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    try (final Reader in = dataType.get()) {
      if (in != null)
        statement.setString(parameterIndex, Readers.readFully(in));
      else
        statement.setNull(parameterIndex, dataType.sqlType());
    }
  }

  @Override
  Reader getParameter(final type.CLOB clob, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value == null ? null : new StringReader(value);
  }

  @Override
  void setParameter(final type.DATE dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDate value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, Dialect.DATE_FORMAT.format(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDate getParameter(final type.DATE date, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return LocalDate.parse(value, Dialect.DATETIME_FORMAT);

    return LocalDate.parse(value, Dialect.DATE_FORMAT);
  }

  @Override
  void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.format(Dialect.TIME_FORMAT));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalTime getParameter(final type.TIME time, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return LocalTime.parse(value, Dialect.DATETIME_FORMAT);

    return LocalTime.parse(value, Dialect.TIME_FORMAT);
  }

  @Override
  void setParameter(final type.DATETIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.format(Dialect.DATETIME_FORMAT));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDateTime getParameter(final type.DATETIME dateTime, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.parse(value, Dialect.DATETIME_FORMAT);
  }

  @Override
  void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    try (final InputStream in = dataType.get()) {
      if (in != null)
        statement.setBytes(parameterIndex, Streams.readBytes(in));
      else
        statement.setNull(parameterIndex, dataType.sqlType());
    }
  }
}