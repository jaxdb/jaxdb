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
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;

final class PostgreSQLCompiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      final StringBuilder modulus = new StringBuilder("CREATE OR REPLACE FUNCTION MODULUS(dividend double precision, divisor double precision) RETURNS numeric AS $$");
      modulus.append("DECLARE");
      modulus.append("  factor double precision;");
      modulus.append("  result double precision;");
      modulus.append("BEGIN");
      modulus.append("  factor := dividend / divisor;");
      modulus.append("  IF factor < 0 THEN");
      modulus.append("    factor := CEIL(factor);");
      modulus.append("  ELSE");
      modulus.append("    factor := FLOOR(factor);");
      modulus.append("  END IF;");
      modulus.append("  RETURN dividend - divisor * factor;");
      modulus.append("END;");
      modulus.append("$$ LANGUAGE plpgsql;");
      statement.execute(modulus.toString());

      final StringBuilder log2 = new StringBuilder("CREATE OR REPLACE FUNCTION LOG2(num numeric) RETURNS numeric AS $$");
      log2.append("DECLARE");
      log2.append("  result double precision;");
      log2.append("BEGIN");
      log2.append("  RETURN LOG(2, num);");
      log2.append("END;");
      log2.append("$$ LANGUAGE plpgsql;");
      statement.execute(log2.toString());

      final StringBuilder log10 = new StringBuilder("CREATE OR REPLACE FUNCTION LOG10(num numeric) RETURNS numeric AS $$");
      log10.append("DECLARE");
      log10.append("  result double precision;");
      log10.append("BEGIN");
      log10.append("  RETURN LOG(10, num);");
      log10.append("END;");
      log10.append("$$ LANGUAGE plpgsql;");
      statement.execute(log10.toString());
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  String translateEnum(final type.ENUM<?> from, final type.ENUM<?> to) {
    final EntityEnum.Spec spec = to.type().getAnnotation(EntityEnum.Spec.class);
    return "::text::" + Dialect.getTypeName(spec.table(), spec.column());
  }

  @Override
  void compile(final CaseImpl.Simple.CASE<?,?> case_, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append("CASE ");
    if (case_.variable instanceof type.ENUM && _else instanceof CaseImpl.CHAR.ELSE)
      toChar((type.ENUM<?>)case_.variable, compilation);
    else
      case_.variable.compile(compilation, true);
  }

  @Override
  void compile(final CaseImpl.WHEN<?> when, final CaseImpl.THEN<?,?> then, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    final Class<?> conditionClass = when.condition instanceof Predicate ? ((Predicate)when.condition).dataType.getClass() : when.condition.getClass();
    if ((when.condition instanceof type.ENUM || then.value instanceof type.ENUM) && (conditionClass != then.value.getClass() || _else instanceof CaseImpl.CHAR.ELSE)) {
      compilation.append(" WHEN ");
      if (when.condition instanceof type.ENUM)
        toChar((type.ENUM<?>)when.condition, compilation);
      else
        when.condition.compile(compilation, true);

      compilation.append(" THEN ");
      if (then.value instanceof type.ENUM)
        toChar((type.ENUM<?>)then.value, compilation);
      else
        then.value.compile(compilation, true);
    }
    else {
      super.compile(when, then, _else, compilation);
    }
  }

  @Override
  void compile(final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append(" ELSE ");
    if (_else instanceof CaseImpl.CHAR.ELSE && _else.value instanceof type.ENUM)
      toChar((type.ENUM<?>)_else.value, compilation);
    else
      _else.value.compile(compilation, true);
    compilation.append(" END");
  }

  @Override
  void compile(final expression.Concat expression, final Compilation compilation) throws IOException {
    compilation.append("CONCAT(");
    for (int i = 0; i < expression.args.length; i++) {
      final Compilable arg = compilable(expression.args[i]);
      if (i > 0)
        compilation.append(", ");

      arg.compile(compilation, true);
    }
    compilation.append(')');
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    compilation.append("INTERVAL '");
    final List<TemporalUnit> units = interval.getUnits();
    for (int i = 0; i < units.size(); ++i) {
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.append(' ');

      final long component;
      final String unitString;
      if (unit == Interval.Unit.MICROS) {
        component = interval.get(unit);
        unitString = "MICROSECOND";
      }
      else if (unit == Interval.Unit.MILLIS) {
        component = interval.get(unit);
        unitString = "MILLISECOND";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = interval.get(unit) * 3;
        unitString = "MONTH";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = interval.get(unit) * 100;
        unitString = "YEARS";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = interval.get(unit) * 1000;
        unitString = "YEARS";
      }
      else {
        component = interval.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }

      compilation.append(component).append(' ').append(unitString);
    }

    compilation.append('\'');
  }

  @Override
  String getPreparedStatementMark(final type.DataType<?> dataType) {
    if (!(dataType instanceof type.ENUM))
      return "?";

    final EntityEnum.Spec spec = dataType.type().getAnnotation(EntityEnum.Spec.class);
    return "?::" + q(Dialect.getTypeName(spec.table(), spec.column()));
  }

  @Override
  String compile(final type.BLOB dataType) throws IOException {
    try (final InputStream in = dataType.get()) {
      if (in == null)
        return "NULL";

      final BigInteger integer = new BigInteger(Streams.readBytes(in));
      return "E'\\" + integer.toString(8); // FIXME: This is only half done
    }
  }

  private static void toChar(final type.ENUM<?> dataType, final Compilation compilation) throws IOException {
    compilation.append("CAST(");
    dataType.compile(compilation, true);
    compilation.append(" AS CHAR(").append(dataType.length()).append("))");
  }

  @Override
  final void compile(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException {
    if (predicate.a.getClass() == predicate.b.getClass() || (!(predicate.a instanceof type.ENUM) && !(predicate.b instanceof type.ENUM))) {
      super.compile(predicate, compilation);
    }
    else {
      if (predicate.a instanceof type.ENUM)
        toChar((type.ENUM<?>)predicate.a, compilation);
      else
        predicate.a.compile(compilation, true);

      compilation.append(' ').append(predicate.operator).append(' ');
      if (predicate.b instanceof type.ENUM)
        toChar((type.ENUM<?>)predicate.b, compilation);
      else
        predicate.b.compile(compilation, true);
    }
  }

  @Override
  void compile(final function.Mod function, final Compilation compilation) throws IOException {
    compilation.append("MODULUS(");
    function.a.compile(compilation, true);
    compilation.append(", ");
    function.b.compile(compilation, true);
    compilation.append(')');
  }

  private static void compileCastNumeric(final Compilable dateType, final Compilation compilation) throws IOException {
    if (dateType instanceof type.ApproxNumeric) {
      compilation.append("CAST(");
      dateType.compile(compilation, true);
      compilation.append(" AS NUMERIC)");
    }
    else {
      dateType.compile(compilation, true);
    }
  }

  private static void compileLog(final String sqlFunction, final function.Generic function, final Compilation compilation) throws IOException {
    compilation.append(sqlFunction).append('(');
    compileCastNumeric(function.a, compilation);

    if (function.b != null) {
      compilation.append(", ");
      compileCastNumeric(function.b, compilation);
    }

    compilation.append(')');
  }

  @Override
  void compile(final function.Ln function, final Compilation compilation) throws IOException {
    compileLog("LN", function, compilation);
  }

  @Override
  void compile(final function.Log function, final Compilation compilation) throws IOException {
    compileLog("LOG", function, compilation);
  }

  @Override
  void compile(final function.Log2 function, final Compilation compilation) throws IOException {
    compileLog("LOG2", function, compilation);
  }

  @Override
  void compile(final function.Log10 function, final Compilation compilation) throws IOException {
    compileLog("LOG10", function, compilation);
  }

  @Override
  void compile(final function.Round function, final Compilation compilation) throws IOException {
    compilation.append("ROUND(");
    if (function.b instanceof type.Numeric<?> && !((type.Numeric<?>)function.b).isNull() && ((type.Numeric<?>)function.b).get().intValue() == 0) {
      function.a.compile(compilation, true);
    }
    else {
      compileCastNumeric(function.a, compilation);
      compilation.append(", ");
      function.b.compile(compilation, true);
    }
    compilation.append(')');
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
  void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    try (final InputStream in = dataType.get()) {
      if (in != null)
        statement.setBytes(parameterIndex, Streams.readBytes(in));
      else
        statement.setNull(parameterIndex, dataType.sqlType());
    }
  }
}