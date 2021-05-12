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
import org.libj.util.ArrayUtil;

final class SQLiteCompiler extends Compiler {
  private static final Pattern dateTimePattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,7})?");

  SQLiteCompiler() {
    super(DBVendor.SQLITE);
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
  }

  @Override
  void compile(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof type.Temporal) {
      compilation.append("STRFTIME(\"");
      if (as.cast instanceof type.DATE) {
        compilation.append("%Y-%m-%d");
      }
      else if (as.cast instanceof type.TIME) {
        compilation.append("%H:%M:");
        final type.TIME time = (type.TIME)as.cast;
        compilation.append(time.precision() == null || time.precision() > 0 ? "%f" : "%S");
      }
      else if (as.cast instanceof type.DATETIME) {
        compilation.append("%Y-%m-%d %H:%M:");
        final type.DATETIME dateTime = (type.DATETIME)as.cast;
        compilation.append(dateTime.precision() == null || dateTime.precision() > 0 ? "%f" : "%S");
      }
      else {
        throw new UnsupportedOperationException("Unsupported type.Temporal type: " + as.cast.getClass().getName());
      }

      compilation.append("\", (");
      toSubject(as.dataType).compile(compilation, true);
      compilation.append("))");
    }
    else {
      super.compile(as, compilation);
    }
  }

  @Override
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException, SQLException {
    if (expression.a instanceof type.DATE)
      compilation.append("DATE(");
    else if (expression.a instanceof type.TIME)
      compilation.append("TIME(");
    else if (expression.a instanceof type.DATETIME)
      compilation.append("DATETIME(");
    else
      throw new UnsupportedOperationException("Unsupported type: " + expression.a.getClass().getName());

    expression.a.compile(compilation, true);
    compilation.append(", '").append(expression.operator);
    expression.b.compile(compilation, true);
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
  void compile(final function.Mod function, final Compilation compilation) throws IOException, SQLException {
    compilation.append('(');
    function.a.compile(compilation, true);
    compilation.append(" % ");
    function.b.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final function.Ln function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("LOG(");
    function.a.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("LOG(");
    function.b.compile(compilation, true);
    compilation.append(") / LOG(");
    function.a.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log2 function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("LOG(");
    function.a.compile(compilation, true);
    compilation.append(") / 0.6931471805599453");
  }

  @Override
  void compileFor(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: Log (once) that this is unsupported.
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
      statement.setString(parameterIndex, Dialect.dateToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDate getParameter(final type.DATE date, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return Dialect.dateTimeFromString(value).toLocalDate();

    return Dialect.dateFromString(value);
  }

  @Override
  void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, Dialect.timeToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalTime getParameter(final type.TIME time, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return Dialect.dateTimeFromString(value).toLocalTime();

    return Dialect.timeFromString(value);
  }

  @Override
  void setParameter(final type.DATETIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, Dialect.dateTimeToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDateTime getParameter(final type.DATETIME dateTime, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return resultSet.wasNull() || value == null ? null : Dialect.dateTimeFromString(value);
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

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void compileInsertOnConflict(final type.DataType<?>[] columns, final Select.untyped.SELECT<?> select, final type.DataType<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    if (select != null) {
      compileInsertSelect(columns, select, false, compilation);
      if (((SelectImpl.untyped.SELECT<?>)select).where == null)
        compilation.append(" WHERE TRUE");
    }
    else {
      compileInsert(columns, false, compilation);
    }

    compilation.append(" ON CONFLICT (");
    for (int i = 0; i < onConflict.length; ++i) {
      if (i > 0)
        compilation.comma();

      onConflict[i].compile(compilation, false);
    }

    compilation.append(')');
    if (doUpdate) {
      compilation.append(" DO UPDATE SET ");

      boolean added = false;
      for (int i = 0; i < columns.length; ++i) {
        final type.DataType column = columns[i];
        if (ArrayUtil.contains(onConflict, column))
          continue;

        if (added)
          compilation.comma();

        final String name = q(column.name);
        if (select != null) {
          compilation.append(name).append(" = EXCLUDED.").append(name);
          added = true;
          continue;
        }

        if ((!column.wasSet() || column.keyForUpdate) && column.generateOnUpdate != null)
          column.generateOnUpdate.generate(column, compilation.vendor);
        else if (!column.wasSet())
          continue;

        evaluateIndirection(column, compilation);
        compilation.append(name).append(" = ");
        compilation.addParameter(column, false);
        added = true;
      }
    }
    else {
      compilation.append(" DO NOTHING");
    }
  }

  @Override
  boolean supportsReturnGeneratedKeysBatch() {
    return false;
  }
}