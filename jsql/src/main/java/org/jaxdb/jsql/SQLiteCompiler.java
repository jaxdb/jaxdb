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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.jaxdb.jsql.keyword.Cast;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;
import org.libj.io.UnsynchronizedStringReader;

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
  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof data.Temporal) {
      compilation.sql.append("STRFTIME(\"");
      if (as.cast instanceof data.DATE) {
        compilation.sql.append("%Y-%m-%d");
      }
      else if (as.cast instanceof data.TIME) {
        compilation.sql.append("%H:%M:");
        final data.TIME time = (data.TIME)as.cast;
        compilation.sql.append(time.precision() == null || time.precision() > 0 ? "%f" : "%S");
      }
      else if (as.cast instanceof data.DATETIME) {
        compilation.sql.append("%Y-%m-%d %H:%M:");
        final data.DATETIME dateTime = (data.DATETIME)as.cast;
        compilation.sql.append(dateTime.precision() == null || dateTime.precision() > 0 ? "%f" : "%S");
      }
      else {
        throw new UnsupportedOperationException("Unsupported type.Temporal type: " + as.cast.getClass().getName());
      }

      compilation.sql.append("\", (");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("))");
    }
    else {
      super.compileCast(as, compilation);
    }
  }

  @Override
  void compileIntervalAdd(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "+", b, compilation);
  }

  @Override
  void compileIntervalSub(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "-", b, compilation);
  }

  @Override
  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    if (a instanceof data.DATE)
      compilation.sql.append("DATE(");
    else if (a instanceof data.TIME)
      compilation.sql.append("TIME(");
    else if (a instanceof data.DATETIME)
      compilation.sql.append("DATETIME(");
    else
      throw new UnsupportedOperationException("Unsupported type: " + a.getClass().getName());

    toSubject(a).compile(compilation, true);
    compilation.sql.append(", '").append(o);

    final ArrayList<TemporalUnit> units = b.getUnits();
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
      if (i > 0)
        compilation.sql.append(' ');

      final long component;
      final String unitString;
      final TemporalUnit unit = units.get(i);
      if (unit == Interval.Unit.WEEKS) {
        component = b.get(unit) * 7;
        unitString = "DAYS";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = b.get(unit) * 3;
        unitString = "MONTHS";
      }
      else if (unit == Interval.Unit.DECADES) {
        component = b.get(unit) * 10;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = b.get(unit) * 100;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = b.get(unit) * 1000;
        unitString = "YEAR";
      }
      else {
        component = b.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }

      compilation.sql.append(component).append(' ').append(unitString);
    }

    compilation.sql.append("')");
  }

  @Override
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append('(');
    toSubject(a).compile(compilation, true);
    compilation.sql.append(" % ");
    toSubject(b).compile(compilation, true);
    compilation.sql.append(')');
  }

  @Override
  void compileLn(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("LOG(");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(')');
  }

  @Override
  void compileLog(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("LOG(");
    toSubject(b).compile(compilation, true);
    compilation.sql.append(") / LOG(");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(')');
  }

  @Override
  void compileLog2(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("LOG(");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(") / 0.6931471805599453");
  }

  @Override
  void compileFor(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: Log (once) that this is unsupported.
  }

  @Override
  void setParameter(final data.CLOB column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws IOException, SQLException {
    try (final Reader in = column.getForUpdateWhereGetOld(isForUpdateWhere)) {
      if (in != null)
        statement.setString(parameterIndex, Readers.readFully(in));
      else
        statement.setNull(parameterIndex, column.sqlType());
    }
  }

  @Override
  Reader getParameter(final data.CLOB clob, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value == null ? null : new UnsynchronizedStringReader(value);
  }

  @Override
  void setParameter(final data.DATE column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalDate value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setString(parameterIndex, Dialect.dateToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDate getParameter(final data.DATE date, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return Dialect.dateTimeFromString(value).toLocalDate();

    return Dialect.dateFromString(value);
  }

  @Override
  void setParameter(final data.TIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setString(parameterIndex, Dialect.timeToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalTime getParameter(final data.TIME time, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (dateTimePattern.matcher(value).matches())
      return Dialect.dateTimeFromString(value).toLocalTime();

    return Dialect.timeFromString(value);
  }

  @Override
  void setParameter(final data.DATETIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalDateTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setString(parameterIndex, Dialect.dateTimeToString(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  LocalDateTime getParameter(final data.DATETIME dateTime, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return resultSet.wasNull() || value == null ? null : Dialect.dateTimeFromString(value);
  }

  @Override
  void setParameter(final data.BLOB column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws IOException, SQLException {
    try (final InputStream in = column.getForUpdateWhereGetOld(isForUpdateWhere)) {
      if (in != null)
        statement.setBytes(parameterIndex, Streams.readBytes(in));
      else
        statement.setNull(parameterIndex, column.sqlType());
    }
  }

  @Override
  void compileInsert(final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("INSERT ");
    if (ignore)
      compilation.sql.append("IGNORE ");

    compilation.sql.append("INTO ");
    q(compilation.sql, columns[0].getTable().getName());
    boolean modified = false;
    for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
      final data.Column<?> column = columns[i];
      if (!shouldInsert(column, true, compilation))
        continue;

      if (modified)
        compilation.sql.append(", ");
      else
        compilation.sql.append(" (");

      q(compilation.sql, column.name);
      modified = true;
    }

    if (modified) {
      compilation.sql.append(") VALUES (");
      modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column<?> column = columns[i];
        if (!shouldInsert(column, false, compilation))
          continue;

        if (modified)
          compilation.sql.append(", ");

        compilation.addParameter(column, false, false);
        modified = true;
      }

      compilation.sql.append(')');
    }
    else {
      compilation.sql.append(" DEFAULT VALUES");
    }
  }

  @Override
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    if (select != null) {
      compileInsertSelect(columns, select, false, compilation);
      if (((Command.Select.untyped.SELECT<?>)select).where() == null)
        compilation.sql.append(" WHERE TRUE");
    }
    else {
      compileInsert(columns, false, compilation);
    }

    compilation.sql.append(" ON CONFLICT (");
    for (int i = 0, i$ = onConflict.length; i < i$; ++i) { // [A]
      if (i > 0)
        compilation.sql.append(", ");

      onConflict[i].compile(compilation, false);
    }

    compilation.sql.append(')');
    if (doUpdate) {
      compilation.sql.append(" DO UPDATE SET ");

      boolean modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column<?> column = columns[i];
        if (column.primary)
          continue;

        if (select != null) {
          if (modified)
            compilation.sql.append(", ");

          q(compilation.sql, column.name).append(" = EXCLUDED.");
          q(compilation.sql, column.name);
          modified = true;
        }
        else if (shouldUpdate(column, compilation)) {
          if (modified)
            compilation.sql.append(", ");

          q(compilation.sql, column.name).append(" = ");
          compilation.addParameter(column, false, false);
          modified = true;
        }
      }
    }
    else {
      compilation.sql.append(" DO NOTHING");
    }
  }

  @Override
  boolean supportsReturnGeneratedKeysBatch() {
    return false;
  }
}