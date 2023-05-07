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
import java.util.ArrayList;

import org.jaxdb.jsql.keyword.Cast;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DbVendor;
import org.libj.util.Temporals;

class MySQLCompiler extends Compiler {
  MySQLCompiler() {
    super(DbVendor.MY_SQL);
  }

  MySQLCompiler(final DbVendor vendor) {
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
    final StringBuilder sql = compilation.sql;
    sql.append("CONCAT(");
    for (int i = 0, i$ = expression.a.length; i < i$; ++i) { // [A]
      final Subject arg = toSubject(expression.a[i]);
      if (i > 0)
        sql.append(", ");

      arg.compile(compilation, true);
    }
    sql.append(')');
  }

  @Override
  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    final boolean isTime = a instanceof type.TIME;
    final StringBuilder sql = compilation.sql;
    if (isTime)
      sql.append("TIME(");

    sql.append("DATE_").append(o).append('(');
    if (isTime)
      sql.append("TIMESTAMP(");

    toSubject(a).compile(compilation, true);
    if (isTime)
      sql.append(')');

    sql.append(", ");
    sql.append("INTERVAL ");
    final ArrayList<TemporalUnit> units = b.getUnits();
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        sql.append(' ');

      final long component;
      final String unitString;
      if (unit == Interval.Unit.MICROS) {
        component = b.get(unit);
        unitString = "MICROSECOND ";
      }
      else if (unit == Interval.Unit.MILLIS) {
        component = b.get(unit) * 1000;
        unitString = "MICROSECOND ";
      }
      else if (unit == Interval.Unit.DECADES) {
        component = b.get(unit) * 10;
        unitString = "YEAR ";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = b.get(unit) * 100;
        unitString = "YEAR ";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = b.get(unit) * 1000;
        unitString = "YEAR ";
      }
      else {
        final String string = unit.toString();
        if (string.endsWith("S")) {
          component = b.get(unit);
          unitString = string;
        }
        else {
          throw new UnsupportedOperationException("Unsupported Interval.Unit: " + unit);
        }
      }

      sql.append(component).append(' ').append(unitString, 0, unitString.length() - 1);
      sql.append(')');
    }

    if (isTime)
      sql.append(')');
  }

  @Override
  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof data.Temporal || as.cast instanceof data.Textual || as.cast instanceof data.BINARY) {
      super.compileCast(as, compilation);
      return;
    }

    final StringBuilder sql = compilation.sql;
    if (as.cast instanceof data.DECIMAL) {
      sql.append("CAST((");
      toSubject(as.column).compile(compilation, true);
      sql.append(") AS ");
      as.cast.declare(sql, compilation.vendor).append(')');
    }
    else if (as.cast instanceof data.ExactNumeric) {
      sql.append("CAST((");
      toSubject(as.column).compile(compilation, true);
      sql.append(") AS ").append("SIGNED INTEGER)");
    }
    else {
      sql.append('(');
      toSubject(as.column).compile(compilation, true);
      sql.append(')');
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
  void compileFor(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: It seems MySQL 8+? supports this?
    select.forLockOption = null;
    if (select.forLockStrength == null || select.forLockStrength == Command.Select.untyped.SELECT.LockStrength.UPDATE) {
      super.compileFor(select, compilation);
      return;
    }

    final StringBuilder sql = compilation.sql;
    sql.append(" LOCK IN SHARE MODE");
    if (select.forSubjects != null && select.forSubjects.length > 0)
      compileForOf(select, compilation);

    if (select.forLockOption != null)
      sql.append(' ').append(select.forLockOption);
  }

  @Override
  void compileForOf(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
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
      final StringBuilder sql = compilation.sql;
      sql.append(" ON DUPLICATE KEY UPDATE ");

      boolean modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (column.primaryIndexType != null)
          continue;

        if (selectCompilation != null) {
          if (modified)
            sql.append(", ");

          q(sql, column.name).append(" = ");
          sql.append("a.").append(selectCompilation.getColumnTokens().get(i));
          modified = true;
        }
        else if (shouldUpdate(column, compilation)) {
          if (modified)
            sql.append(", ");

          q(sql, column.name).append(" = ");
          compilation.addParameter(column, false, false);
          modified = true;
        }
      }
    }
  }
}