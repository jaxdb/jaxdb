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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.jaxdb.vendor.DBVendor;
import org.libj.util.Temporals;

class MySQLCompiler extends Compiler {
  MySQLCompiler() {
    super(DBVendor.MY_SQL);
  }

  MySQLCompiler(final DBVendor vendor) {
    super(vendor);
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
  }

  @Override
  void compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CONCAT(");
    for (int i = 0; i < expression.a.length; ++i) {
      final Subject arg = toSubject(expression.a[i]);
      if (i > 0)
        compilation.comma();

      arg.compile(compilation, true);
    }
    compilation.append(')');
  }

  @Override
  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    final boolean isTime = a instanceof type.TIME;
    if (isTime)
      compilation.append("TIME(");

    compilation.append("DATE_" + o).append('(');
    if (isTime)
      compilation.append("TIMESTAMP(");

    toSubject(a).compile(compilation, true);
    if (isTime)
      compilation.append(')');

    compilation.comma();
    compilation.append("INTERVAL ");
    final List<TemporalUnit> units = b.getUnits();
    for (int i = 0, len = units.size(); i < len; ++i) {
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.append(' ');

      final long component;
      final String unitString;
      if (unit == Interval.Unit.MICROS) {
        component = b.get(unit);
        unitString = "MICROSECOND";
      }
      else if (unit == Interval.Unit.MILLIS) {
        component = b.get(unit) * 1000;
        unitString = "MICROSECOND";
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
      else if (unit.toString().endsWith("S")) {
        component = b.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }
      else {
        throw new UnsupportedOperationException("Unsupported Interval.Unit: " + unit);
      }

      compilation.append(component).append(' ').append(unitString);
      compilation.append(')');
    }

    if (isTime)
      compilation.append(')');
  }

  @Override
  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof data.Temporal || as.cast instanceof data.Textual || as.cast instanceof data.BINARY) {
      super.compileCast(as, compilation);
    }
    else if (as.cast instanceof data.DECIMAL) {
      compilation.append("CAST((");
      toSubject(as.column).compile(compilation, true);
      final String declaration = as.cast.declare(compilation.vendor);
      compilation.append(") AS ").append(declaration).append(')');
    }
    else if (as.cast instanceof data.ExactNumeric) {
      compilation.append("CAST((");
      toSubject(as.column).compile(compilation, true);
      compilation.append(") AS ").append("SIGNED INTEGER)");
    }
    else {
      compilation.append('(');
      toSubject(as.column).compile(compilation, true);
      compilation.append(')');
    }
  }

  @Override
  void setParameter(final data.TIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setObject(parameterIndex, value);
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  @Override
  void updateColumn(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalTime value = column.get();
    if (value != null)
      resultSet.updateTime(columnIndex, Time.valueOf(value));
    else
      resultSet.updateNull(columnIndex);
  }

  @Override
  LocalTime getParameter(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalTime value = resultSet.getObject(columnIndex, LocalTime.class);
    if (resultSet.wasNull() || value == null)
      return null;

    return value.toString().charAt(0) == '-' ? Temporals.subtract(LocalTime.MIDNIGHT, value) : value;
  }

  @Override
  void compileFor(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: It seems MySQL 8+? supports this?
    select.forLockOption = null;
    if (select.forLockStrength == null || select.forLockStrength == SelectImpl.untyped.SELECT.LockStrength.UPDATE) {
      super.compileFor(select, compilation);
    }
    else {
      compilation.append(" LOCK IN SHARE MODE");
      if (select.forSubjects != null && select.forSubjects.length > 0)
        compileForOf(select, compilation);

      if (select.forLockOption != null)
        compilation.append(' ').append(select.forLockOption);
    }
  }

  @Override
  void compileForOf(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: It seems MySQL 8+? supports this?
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    final Compilation selectCompilation;
    if (select != null) {
      selectCompilation = compileInsertSelect(columns, select, !doUpdate, compilation);
    }
    else {
      selectCompilation = null;
      compileInsert(columns, !doUpdate, compilation);
    }

    if (doUpdate) {
      compilation.append(" ON DUPLICATE KEY UPDATE ");

      boolean modified = false;
      for (int i = 0; i < columns.length; ++i) {
        final data.Column column = columns[i];
        if (column.primary)
          continue;

        if (selectCompilation != null) {
          if (modified)
            compilation.comma();

          compilation.append(q(column.name)).append(" = ");
          compilation.append("a.").append(selectCompilation.getColumnTokens().get(i));
          modified = true;
        }
        else if (shouldUpdate(column, compilation)) {
          if (modified)
            compilation.comma();

          compilation.append(q(column.name)).append(" = ");
          compilation.addParameter(column, false, false);
          modified = true;
        }
      }
    }
  }
}