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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.jaxdb.vendor.DBVendor;
import org.libj.util.Temporals;

class MySQLCompiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
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
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    final String function;
    if (expression.operator == operator.Arithmetic.PLUS)
      function = "DATE_ADD";
    else if (expression.operator == operator.Arithmetic.MINUS)
      function = "DATE_SUB";
    else
      throw new UnsupportedOperationException("Supported operators for TemporalExpression are only + and -, and this should have been not allowed via strong type semantics " + expression.operator);

    compilation.append(function).append('(');
    expression.a.compile(compilation, true);
    compilation.append(", ");
    expression.b.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    compilation.append("INTERVAL ");
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
        component = interval.get(unit) * 1000;
        unitString = "MICROSECOND";
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
      else if (unit.toString().endsWith("S")) {
        component = interval.get(unit);
        unitString = unit.toString().substring(0, unit.toString().length() - 1);
      }
      else {
        throw new UnsupportedOperationException("Unsupported Interval.Unit: " + unit);
      }

      compilation.append(component).append(' ').append(unitString);
    }
  }

  @Override
  void compile(final Cast.AS as, final Compilation compilation) throws IOException {
    if (as.cast instanceof type.Temporal || as.cast instanceof type.Textual || as.cast instanceof type.BINARY) {
      super.compile(as, compilation);
    }
    else if (as.cast instanceof type.DECIMAL || as.cast instanceof type.DECIMAL.UNSIGNED) {
      compilation.append("CAST((");
      compilable(as.dataType).compile(compilation, true);
      final String declaration = as.cast.declare(compilation.vendor);
      compilation.append(") AS ").append(declaration).append(')');
    }
    else if (as.cast instanceof type.ExactNumeric) {
      compilation.append("CAST((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append(") AS ").append(as.cast instanceof kind.Numeric.UNSIGNED ? "UNSIGNED" : "SIGNED").append(" INTEGER)");
    }
    else {
      compilation.append('(');
      compilable(as.dataType).compile(compilation, true);
      compilation.append(')');
    }
  }

  @Override
  LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    final LocalTime localTime = value.toLocalDateTime().toLocalTime();
    return value.toString().charAt(0) == '-' ? Temporals.subtract(LocalTime.MIDNIGHT, localTime) : localTime;
  }
}