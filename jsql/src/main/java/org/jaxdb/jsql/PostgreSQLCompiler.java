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
import java.sql.Statement;
import java.sql.Time;
import java.sql.Types;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DBVendor;
import org.libj.io.Readers;
import org.libj.io.Streams;

final class PostgreSQLCompiler extends Compiler {
  PostgreSQLCompiler() {
    super(DBVendor.POSTGRE_SQL);
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      try {
        final StringBuilder b = new StringBuilder("BEGIN;");
        b.append("SELECT pg_advisory_xact_lock(2142616474639426746);");
        b.append("CREATE OR REPLACE FUNCTION MODULUS(dividend double precision, divisor double precision) RETURNS numeric AS $$");
        b.append("DECLARE");
        b.append("  factor double precision;");
        b.append("  result double precision;");
        b.append("BEGIN");
        b.append("  factor := dividend / divisor;");
        b.append("  IF factor < 0 THEN");
        b.append("    factor := CEIL(factor);");
        b.append("  ELSE");
        b.append("    factor := FLOOR(factor);");
        b.append("  END IF;");
        b.append("  RETURN dividend - divisor * factor;");
        b.append("END;");
        b.append("$$ LANGUAGE plpgsql;");
        b.append("END;");
        statement.execute(b.toString());
      }
      catch (final SQLException e) {
        if (!"X0Y68".equals(e.getSQLState())) // FUNCTION '*' already exists
          throw e;
      }

      try {
        final StringBuilder b = new StringBuilder("BEGIN;");
        b.append("CREATE OR REPLACE FUNCTION LOG2(num numeric) RETURNS numeric AS $$");
        b.append("DECLARE");
        b.append("  result double precision;");
        b.append("BEGIN");
        b.append("  RETURN LOG(2, num);");
        b.append("END;");
        b.append("$$ LANGUAGE plpgsql;");
        b.append("END;");
        statement.execute(b.toString());
      }
      catch (final SQLException e) {
        if (!"X0Y68".equals(e.getSQLState())) // FUNCTION '*' already exists
          throw e;
      }

      try {
        final StringBuilder b = new StringBuilder("BEGIN;");
        b.append("CREATE OR REPLACE FUNCTION LOG10(num numeric) RETURNS numeric AS $$");
        b.append("DECLARE");
        b.append("  result double precision;");
        b.append("BEGIN");
        b.append("  RETURN LOG(10, num);");
        b.append("END;");
        b.append("$$ LANGUAGE plpgsql;");
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
  String translateEnum(final data.ENUM<?> from, final data.ENUM<?> to) {
    return "::text::" + to.type().getAnnotation(EntityEnum.Type.class).value();
  }

  @Override
  void compileCaseElse(final data.Column<?> variable, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CASE ");
    if (variable instanceof data.ENUM && _else instanceof data.CHAR)
      toChar((data.ENUM<?>)variable, compilation);
    else
      variable.compile(compilation, true);
  }

  @Override
  void compileWhenThenElse(final Subject when, final data.Column<?> then, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    final Class<?> conditionClass = when instanceof Predicate ? ((Predicate)when).column.getClass() : when.getClass();
    if ((when instanceof data.ENUM || then instanceof data.ENUM) && (conditionClass != then.getClass() || _else instanceof data.CHAR)) {
      compilation.append(" WHEN ");
      if (when instanceof data.ENUM)
        toChar((data.ENUM<?>)when, compilation);
      else
        when.compile(compilation, true);

      compilation.append(" THEN ");
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
    compilation.append(" ELSE ");
//    if (_else instanceof CaseImpl.CHAR.ELSE && _else.value instanceof type.ENUM)
    if (_else instanceof data.ENUM)
      toChar((data.ENUM<?>)_else, compilation);
    else
      _else.compile(compilation, true);
    compilation.append(" END");
  }

  @Override
  void compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CONCAT(");
    for (int i = 0, i$ = expression.a.length; i < i$; ++i) { // [A]
      final Subject arg = toSubject(expression.a[i]);
      if (i > 0)
        compilation.comma();

      arg.compile(compilation, true);
      compilation.append("::text");
    }
    compilation.append(')');
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
    compilation.append("((");
    toSubject(a).compile(compilation, true);
    compilation.append(") ");
    compilation.append(o);
    compilation.append(" (");
    compilation.append("INTERVAL '");
    final ArrayList<TemporalUnit> units = b.getUnits();
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
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

      compilation.append(component).append(' ').append(unitString);
    }

    compilation.append('\'');
    compilation.append("))");
  }

  @Override
  String getPreparedStatementMark(final data.Column<?> column) {
    return column instanceof data.ENUM ? "?::" + q(column.type().getAnnotation(EntityEnum.Type.class).value()) : "?";
  }

  private static void toChar(final data.ENUM<?> column, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CAST(");
    column.compile(compilation, true);
    compilation.append(" AS CHAR(").append(column.length()).append("))");
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

      compilation.append(' ').append(predicate.operator).append(' ');
      if (predicate.b instanceof data.ENUM)
        toChar((data.ENUM<?>)predicate.b, compilation);
      else
        predicate.b.compile(compilation, true);
    }
  }

  @Override
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.append("MODULUS(");
    toSubject(a).compile(compilation, true);
    compilation.comma();
    toSubject(b).compile(compilation, true);
    compilation.append(')');
  }

  private static void compileCastNumeric(final Subject dateType, final Compilation compilation) throws IOException, SQLException {
    if (dateType instanceof data.ApproxNumeric) {
      compilation.append("CAST(");
      dateType.compile(compilation, true);
      compilation.append(" AS NUMERIC)");
    }
    else {
      dateType.compile(compilation, true);
    }
  }

  private static void compileLog(final String sqlFunction, final Subject a, final Subject b, final Compilation compilation) throws IOException, SQLException {
    compilation.append(sqlFunction).append('(');
    compileCastNumeric(a, compilation);

    if (b != null) {
      compilation.comma();
      compileCastNumeric(b, compilation);
    }

    compilation.append(')');
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
    compilation.append("ROUND(");
    if (b instanceof data.Numeric<?> && !((data.Numeric<?>)b).isNull() && ((data.Numeric<?>)b).get().intValue() == 0) {
      toSubject(a).compile(compilation, true);
    }
    else {
      compileCastNumeric(toSubject(a), compilation);
      compilation.comma();
      toSubject(b).compile(compilation, true);
    }
    compilation.append(')');
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
    return value == null ? null : new StringReader(value);
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

    compilation.append(" ON CONFLICT (");
    for (int i = 0, i$ = onConflict.length; i < i$; ++i) { // [A]
      if (i > 0)
        compilation.comma();

      onConflict[i].compile(compilation, false);
    }

    compilation.append(')');
    if (doUpdate) {
      compilation.append(" DO UPDATE SET ");

      boolean modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (column.primary)
          continue;

        if (select != null) {
          if (modified)
            compilation.comma();

          final String name = q(column.name);
          compilation.append(name).append(" = EXCLUDED.").append(name);
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
    else {
      compilation.append(" DO NOTHING");
    }
  }

  private String getNames(final Column<?>[] autos) {
    final StringBuilder builder = new StringBuilder();
    for (int i = 0, i$ = autos.length; i < i$; ++i) { // [A]
      if (i > 0)
        builder.append(", ");

      builder.append(q(autos[i].name));
    }

    return builder.toString();
  }

  @Override
  String prepareSqlReturning(final String sql, final Column<?>[] autos) {
    return super.prepareSqlReturning(sql + " RETURNING " + getNames(autos), autos);
  }
}