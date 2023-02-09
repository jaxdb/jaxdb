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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DbVendor;
import org.libj.io.Readers;
import org.libj.io.Streams;
import org.libj.io.UnsynchronizedStringReader;

final class PostgreSQLCompiler extends Compiler {
  PostgreSQLCompiler() {
    super(DbVendor.POSTGRE_SQL);
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      try {
        final StringBuilder b = new StringBuilder("BEGIN;");
        b.append("SELECT pg_advisory_xact_lock(2142616474639426746);\n");
        b.append("CREATE OR REPLACE FUNCTION MODULUS(dividend double precision, divisor double precision) RETURNS numeric AS $$\n");
        b.append("DECLARE\n");
        b.append("  factor double precision;\n");
        b.append("  result double precision;\n");
        b.append("BEGIN\n");
        b.append("  factor := dividend / divisor;\n");
        b.append("  IF factor < 0 THEN\n");
        b.append("    factor := CEIL(factor);\n");
        b.append("  ELSE\n");
        b.append("    factor := FLOOR(factor);\n");
        b.append("  END IF;\n");
        b.append("  RETURN dividend - divisor * factor;\n");
        b.append("END;\n");
        b.append("$$ LANGUAGE plpgsql;\n");
        b.append("END;");
        statement.execute(b.toString());
      }
      catch (final SQLException e) {
        if (!"X0Y68".equals(e.getSQLState())) // FUNCTION '*' already exists
          throw e;
      }

      try {
        final StringBuilder b = new StringBuilder("BEGIN;\n");
        b.append("SELECT pg_advisory_xact_lock(2142616474639426746);\n");
        b.append("CREATE OR REPLACE FUNCTION LOG2(num numeric) RETURNS numeric AS $$\n");
        b.append("DECLARE\n");
        b.append("  result double precision;\n");
        b.append("BEGIN\n");
        b.append("  RETURN LOG(2, num);\n");
        b.append("END;\n");
        b.append("$$ LANGUAGE plpgsql;\n");
        b.append("END;");
        statement.execute(b.toString());
      }
      catch (final SQLException e) {
        if (!"X0Y68".equals(e.getSQLState())) // FUNCTION '*' already exists
          throw e;
      }

      try {
        final StringBuilder b = new StringBuilder("BEGIN;\n");
        b.append("SELECT pg_advisory_xact_lock(2142616474639426746);\n");
        b.append("CREATE OR REPLACE FUNCTION LOG10(num numeric) RETURNS numeric AS $$\n");
        b.append("DECLARE\n");
        b.append("  result double precision;\n");
        b.append("BEGIN\n");
        b.append("  RETURN LOG(10, num);\n");
        b.append("END;\n");
        b.append("$$ LANGUAGE plpgsql;\n");
        b.append("END;");
        statement.execute(b.toString());
      }
      catch (final SQLException e) {
        if (!"X0Y68".equals(e.getSQLState())) // FUNCTION '*' already exists
          throw e;
      }
    }
  }

  @Override
  void setSession(final Connection connection, final Statement statement, final String sessionId) throws SQLException {
    final String sql = sessionId == null ? "SET SESSION jaxdb.session_id = DEFAULT" : "SET SESSION jaxdb.session_id = '" + sessionId + "'";
    if (statement instanceof PreparedStatement) {
      try (final PreparedStatement sessionStatement = connection.prepareStatement(sql)) {
        sessionStatement.execute();
      }
    }
    else {
      statement.execute(sql);
    }
  }

  @Override
  StringBuilder translateEnum(final StringBuilder b, final data.ENUM<?> from, final data.ENUM<?> to) {
    return b.append("::text::").append(to.type().getAnnotation(EntityEnum.Type.class).value());
  }

  @Override
  void compileCaseElse(final data.Column<?> variable, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("CASE ");
    if (variable instanceof data.ENUM && _else instanceof data.CHAR)
      toChar((data.ENUM<?>)variable, compilation);
    else
      variable.compile(compilation, true);
  }

  @Override
  void compileWhenThenElse(final Subject when, final data.Column<?> then, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    final Class<?> conditionClass = when instanceof Predicate ? ((Predicate)when).column.getClass() : when.getClass();
    if ((when instanceof data.ENUM || then instanceof data.ENUM) && (conditionClass != then.getClass() || _else instanceof data.CHAR)) {
      compilation.sql.append(" WHEN ");
      if (when instanceof data.ENUM)
        toChar((data.ENUM<?>)when, compilation);
      else
        when.compile(compilation, true);

      compilation.sql.append(" THEN ");
      if (then instanceof data.ENUM)
        toChar((data.ENUM<?>)then, compilation);
      else
        then.compile(compilation, true);
    }
    else {
      super.compileWhenThenElse(when, then, _else, compilation);
    }
  }

  @Override
  void compileElse(final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append(" ELSE ");
//    if (_else instanceof CaseImpl.CHAR.ELSE && _else.value instanceof type.ENUM)
    if (_else instanceof data.ENUM)
      toChar((data.ENUM<?>)_else, compilation);
    else
      _else.compile(compilation, true);
    compilation.sql.append(" END");
  }

  @Override
  void compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("CONCAT(");
    for (int i = 0, i$ = expression.a.length; i < i$; ++i) { // [A]
      final Subject arg = toSubject(expression.a[i]);
      if (i > 0)
        compilation.sql.append(", ");

      arg.compile(compilation, true);
      compilation.sql.append("::text");
    }
    compilation.sql.append(')');
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
    compilation.sql.append("((");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(") ");
    compilation.sql.append(o);
    compilation.sql.append(" (");
    compilation.sql.append("INTERVAL '");
    final ArrayList<TemporalUnit> units = b.getUnits();
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.sql.append(' ');

      final long component;
      final String unitString;
      if (unit == Interval.Unit.MICROS) {
        component = b.get(unit);
        unitString = "MICROSECOND";
      }
      else if (unit == Interval.Unit.MILLIS) {
        component = b.get(unit);
        unitString = "MILLISECOND";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = b.get(unit) * 3;
        unitString = "MONTH";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = b.get(unit) * 100;
        unitString = "YEARS";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = b.get(unit) * 1000;
        unitString = "YEARS";
      }
      else {
        component = b.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }

      compilation.sql.append(component).append(' ').append(unitString);
    }

    compilation.sql.append('\'');
    compilation.sql.append("))");
  }

  @Override
  StringBuilder getPreparedStatementMark(final StringBuilder b, final data.Column<?> column) {
    b.append('?');
    if (column instanceof data.ENUM) {
      b.append("::");
      q(b, column.type().getAnnotation(EntityEnum.Type.class).value());
    }

    return b;
  }

  private static void toChar(final data.ENUM<?> column, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("CAST(");
    column.compile(compilation, true);
    compilation.sql.append(" AS CHAR(").append(column.length()).append("))");
  }

  @Override
  final void compilePredicate(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    if (predicate.a.getClass() == predicate.b.getClass() || (!(predicate.a instanceof data.ENUM) && !(predicate.b instanceof data.ENUM))) {
      super.compilePredicate(predicate, compilation);
    }
    else {
      if (predicate.a instanceof data.ENUM)
        toChar((data.ENUM<?>)predicate.a, compilation);
      else
        predicate.a.compile(compilation, true);

      compilation.sql.append(' ').append(predicate.operator).append(' ');
      if (predicate.b instanceof data.ENUM)
        toChar((data.ENUM<?>)predicate.b, compilation);
      else
        predicate.b.compile(compilation, true);
    }
  }

  @Override
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("MODULUS(");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(", ");
    toSubject(b).compile(compilation, true);
    compilation.sql.append(')');
  }

  private static void compileCastNumeric(final Subject dateType, final Compilation compilation) throws IOException, SQLException {
    if (dateType instanceof data.ApproxNumeric) {
      compilation.sql.append("CAST(");
      dateType.compile(compilation, true);
      compilation.sql.append(" AS NUMERIC)");
    }
    else {
      dateType.compile(compilation, true);
    }
  }

  private static void compileLog(final String sqlFunction, final Subject a, final Subject b, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append(sqlFunction).append('(');
    compileCastNumeric(a, compilation);

    if (b != null) {
      compilation.sql.append(", ");
      compileCastNumeric(b, compilation);
    }

    compilation.sql.append(')');
  }

  @Override
  void compileLn(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileLog("LN", toSubject(a), null, compilation);
  }

  @Override
  void compileLog(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileLog("LOG", toSubject(a), toSubject(b), compilation);
  }

  @Override
  void compileLog2(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileLog("LOG2", toSubject(a), null, compilation);
  }

  @Override
  void compileLog10(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileLog("LOG10", toSubject(a), null, compilation);
  }

  @Override
  void compileRound(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("ROUND(");
    if (b instanceof data.Numeric<?> && !((data.Numeric<?>)b).isNull() && ((data.Numeric<?>)b).get().intValue() == 0) {
      toSubject(a).compile(compilation, true);
    }
    else {
      compileCastNumeric(toSubject(a), compilation);
      compilation.sql.append(", ");
      toSubject(b).compile(compilation, true);
    }
    compilation.sql.append(')');
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
  void setParameter(final data.BLOB column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws IOException, SQLException {
    try (final InputStream in = column.getForUpdateWhereGetOld(isForUpdateWhere)) {
      if (in != null)
        statement.setBytes(parameterIndex, Streams.readBytes(in));
      else
        statement.setNull(parameterIndex, Types.BINARY);
    }
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
  boolean aliasInForUpdate() {
    return false;
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    if (select != null)
      compileInsertSelect(columns, select, false, compilation);
    else
      compileInsert(columns, false, compilation);

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
        final data.Column column = columns[i];
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

  private String getNames(final Column<?>[] autos) {
    final StringBuilder b = new StringBuilder();
    for (int i = 0, i$ = autos.length; i < i$; ++i) { // [A]
      if (i > 0)
        b.append(", ");

      q(b, autos[i].name);
    }

    return b.toString();
  }

  @Override
  StringBuilder prepareSqlReturning(final StringBuilder sql, final Column<?>[] autos) {
    return super.prepareSqlReturning(sql.append(" RETURNING ").append(getNames(autos)), autos);
  }
}