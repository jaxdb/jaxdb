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
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.util.Temporals;

final class OracleCompiler extends Compiler {
  private static Constructor<?> INTERVALDS;

  private static Object newINTERVALDS(final String s) {
    try {
      return (INTERVALDS == null ? INTERVALDS = Class.forName("oracle.sql.INTERVALDS").getConstructor(String.class) : INTERVALDS).newInstance(s);
    }
    catch (final ClassNotFoundException | IllegalAccessException | NoSuchMethodException e) {
      throw new ExceptionInInitializerError(e);
    }
    catch (final InstantiationException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  @Override
  public DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
  }

  @Override
  String compile(final As<?> as) {
    return null;
  }

  @Override
  void compile(final SelectCommand command, final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException {
    if (command.limit() != null) {
      compilation.append("SELECT * FROM (");
      if (command.offset() != null) {
        compilation.append("SELECT ROWNUM rnum3729, r.* FROM (");
        compilation.skipFirstColumn(true);
      }
    }

    super.compile(command, select, compilation);
  }

  @Override
  void compile(final SelectImpl.untyped.FROM<?> from, final Compilation compilation) throws IOException {
    if (from != null)
      super.compile(from, compilation);
    else
      compilation.append(" FROM DUAL");
  }

  @Override
  void compile(final SelectImpl.untyped.LIMIT<?> limit, final SelectImpl.untyped.OFFSET<?> offset, final Compilation compilation) {
    if (limit != null) {
      compilation.append(") r WHERE ROWNUM <= ");
      if (offset != null)
        compilation.append(String.valueOf(limit.rows + offset.rows)).append(") WHERE rnum3729 > ").append(offset.rows);
      else
        compilation.append(String.valueOf(limit.rows));
    }
  }

  @Override
  void compile(final function.Pi function, final Compilation compilation) {
    compilation.append("ACOS(-1)");
  }

  @Override
  void compile(final function.Log2 function, final Compilation compilation) throws IOException {
    compilation.append("LOG(2, ");
    function.a.compile(compilation);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log10 function, final Compilation compilation) throws IOException {
    compilation.append("LOG(10, ");
    function.a.compile(compilation);
    compilation.append(')');
  }

  @Override
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    expression.a.compile(compilation);
    compilation.append(' ');
    final Interval interval = expression.b;
    if (interval.getUnits().size() == 1) {
      compilation.append(expression.operator.toString());
      compilation.append(' ');
      interval.compile(compilation);
    }
    else {
      for (final TemporalUnit unit : interval.getUnits()) {
        compilation.append(expression.operator.toString());
        compilation.append(' ');
        new Interval(interval.get(unit), (Interval.Unit)unit).compile(compilation);
      }
    }
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    // FIXME: This can be fixed with nested interval semantics
    if (interval.getUnits().size() > 1)
      throw new UnsupportedOperationException("Interval classes with only 1 Interval.Unit are supported");

    final TemporalUnit unit = interval.getUnits().iterator().next();
    if (unit == Interval.Unit.MICROS) {
      compilation.append("INTERVAL '").append(BigDecimal.valueOf(interval.get(unit)).divide(BigDecimal.valueOf(1000000L), RoundingMode.HALF_UP)).append("' SECOND");
    }
    else if (unit == Interval.Unit.MILLIS) {
      compilation.append("INTERVAL '").append(BigDecimal.valueOf(interval.get(unit)).divide(BigDecimal.valueOf(1000L), RoundingMode.HALF_UP)).append("' SECOND");
    }
    else if (unit == Interval.Unit.WEEKS) {
      compilation.append("INTERVAL '").append(interval.get(unit) * 7).append("' DAY");
    }
    else if (unit == Interval.Unit.QUARTERS) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.get(unit) * 3).append(", 'MONTH')");
    }
    else if (unit == Interval.Unit.DECADES) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.get(unit) * 10).append(", 'YEAR')");
    }
    else if (unit == Interval.Unit.CENTURIES) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.get(unit) * 100).append(", 'YEAR')");
    }
    else if (unit == Interval.Unit.MILLENNIA) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.get(unit) * 1000).append(", 'YEAR')");
    }
    else if (unit.toString().endsWith("S")) {
      compilation.append("INTERVAL '").append(interval.get(unit)).append("' ").append(unit.toString(), 0, unit.toString().length() - 1);
    }
    else {
      throw new UnsupportedOperationException("Unsupported Interval.Unit: " + unit);
    }
  }

  @Override
  String compile(final type.CHAR dataType) {
    final String value = dataType.get().replace("'", "''");
    return value.length() == 0 || value.charAt(0) == ' ' ? "' " + value + "'" : "'" + value + "'";
  }

  @Override
  void compile(final Cast.AS as, final Compilation compilation) throws IOException {
    if (as.cast instanceof kind.BINARY) {
      compilation.append("UTL_RAW.CAST_TO_RAW((");
      compilable(as.dataType).compile(compilation);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.BLOB) {
      compilation.append("TO_BLOB((");
      compilable(as.dataType).compile(compilation);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.CLOB) {
      compilation.append("TO_CLOB((");
      compilable(as.dataType).compile(compilation);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.DATE && !(as.dataType instanceof kind.DATETIME)) {
      compilation.append("TO_DATE((");
      compilable(as.dataType).compile(compilation);
      compilation.append("), 'YYYY-MM-DD')");
    }
    else if (as.cast instanceof kind.DATETIME && !(as.dataType instanceof kind.DATETIME)) {
      compilation.append("TO_TIMESTAMP((");
      compilable(as.dataType).compile(compilation);
      compilation.append("), 'YYYY-MM-DD HH24:MI:SS.FF')");
    }
    else if (as.cast instanceof kind.TIME && as.dataType instanceof kind.DATETIME) {
      compilation.append("CAST(CASE WHEN (");
      compilable(as.dataType).compile(compilation);
      compilation.append(") IS NULL THEN NULL ELSE '+0 ' || TO_CHAR((");
      compilable(as.dataType).compile(compilation);
      compilation.append("), 'HH24:MI:SS.FF') END");
      compilation.append(" AS ").append(as.cast.declare(compilation.vendor)).append(')');
    }
    else if (as.cast instanceof kind.CHAR && as.dataType instanceof kind.TIME) {
      compilation.append("SUBSTR(CAST((");
      compilable(as.dataType).compile(compilation);
      compilation.append(") AS ").append(new type.CHAR(((type.CHAR)as.cast).length(), true).declare(compilation.vendor)).append("), 10, 18)");
    }
    else {
      compilation.append("CAST((");
      if (as.cast instanceof kind.TIME && !(as.dataType instanceof kind.TIME))
        compilation.append("'+0 ' || ");

      compilation.append('(');
      compilable(as.dataType).compile(compilation);
      compilation.append(")) AS ").append(as.cast.declare(compilation.vendor)).append(')');
    }
  }

  @Override
  void setParameter(final type.CHAR dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final String value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.length() == 0 || value.charAt(0) == ' ' ? " " + value : value);
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  String getParameter(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value != null && value.startsWith(" ") ? value.substring(1) : value;
  }

  @Override
  void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setObject(parameterIndex, newINTERVALDS("+0 " + value.format(Dialect.TIME_FORMAT)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Object value = resultSet.getObject(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    final LocalTime localTime = LocalTime.parse(value.toString().substring(value.toString().indexOf(' ') + 1), Dialect.TIME_FORMAT);
    return value.toString().charAt(0) == '-' ? Temporals.subtract(LocalTime.MIDNIGHT, localTime) : localTime;
  }

  @Override
  void compileNextSubject(final Compilable subject, final int index, final Keyword<?> source, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation) throws IOException {
    if (source instanceof SelectImpl.untyped.SELECT && (subject instanceof ComparisonPredicate || subject instanceof BooleanTerm || subject instanceof Predicate)) {
      compilation.append("CASE WHEN ");
      super.compileNextSubject(subject, index, source, translateTypes, compilation);
      compilation.append(" THEN 1 ELSE 0 END");
    }
    else {
      super.compileNextSubject(subject, index, source, translateTypes, compilation);
    }

    if (!(source instanceof SelectImpl.untyped.GROUP_BY) && !(subject instanceof type.Entity) && (!(subject instanceof type.Subject) || !(((type.Subject<?>)subject).wrapper() instanceof As)))
      compilation.append(" c" + index);
  }
}