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

import static org.jaxdb.jsql.Compilation.Token.*;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.util.ArrayUtil;
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

  OracleCompiler() {
    super(DBVendor.ORACLE);
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
  void compileSelect(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.limit != -1) {
      compilation.append("SELECT * FROM (");
      if (select.offset != -1) {
        compilation.append("SELECT ROWNUM rnum3729, r.* FROM (");
        compilation.skipFirstColumn(true);
      }
    }

    super.compileSelect(select, useAliases, compilation);
  }

  @Override
  void compileFrom(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from != null)
      super.compileFrom(select, useAliases, compilation);
    else
      compilation.append(" FROM DUAL");
  }

  @Override
  void compileLimitOffset(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.limit != -1) {
      compilation.append(") r WHERE ROWNUM <= ");
      if (select.offset != -1)
        compilation.append(String.valueOf(select.limit + select.offset)).append(") WHERE rnum3729 > ").append(select.offset);
      else
        compilation.append(String.valueOf(select.limit));
    }
  }

  @Override
  void compile(final function.Pi function, final Compilation compilation) {
    compilation.append("ACOS(-1)");
  }

  @Override
  void compile(final function.Log2 function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("LOG(2, ");
    function.a.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final function.Log10 function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("LOG(10, ");
    function.a.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException, SQLException {
    expression.a.compile(compilation, true);
    compilation.append(' ');
    final Interval interval = expression.b;
    if (interval.getUnits().size() == 1) {
      compilation.append(expression.operator.toString());
      compilation.append(' ');
      interval.compile(compilation, true);
    }
    else {
      for (final TemporalUnit unit : interval.getUnits()) {
        compilation.append(expression.operator.toString());
        compilation.append(' ');
        new Interval(interval.get(unit), (Interval.Unit)unit).compile(compilation, true);
      }
    }
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    Interval.Unit unit = (Interval.Unit)units.get(units.size() - 1);
    if (unit == Interval.Unit.MONTHS || unit == Interval.Unit.QUARTERS) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.convertTo(Interval.Unit.MONTHS)).append(", 'MONTH')");
    }
    else if (unit == Interval.Unit.YEARS || unit == Interval.Unit.DECADES || unit == Interval.Unit.CENTURIES || unit == Interval.Unit.MILLENNIA) {
      compilation.append("NUMTOYMINTERVAL(").append(interval.convertTo(Interval.Unit.YEARS)).append(", 'YEAR')");
    }
    else {
      if (unit == Interval.Unit.MICROS || unit == Interval.Unit.MILLIS || unit == Interval.Unit.SECONDS)
        unit = Interval.Unit.SECONDS;
      else if (unit == Interval.Unit.WEEKS)
        unit = Interval.Unit.DAYS;
      else if (unit != Interval.Unit.SECONDS && unit != Interval.Unit.MINUTES && unit != Interval.Unit.HOURS && unit != Interval.Unit.DAYS)
        throw new UnsupportedOperationException("Unsupported Interval.Unit: " + unit);

      final StringBuilder unitString = new StringBuilder(unit.toString());
      unitString.setLength(unitString.length() - 1);
      compilation.append("INTERVAL '").append(interval.convertTo(unit)).append("' ").append(unitString);
    }
  }

  @Override
  String compile(final type.CHAR dataType) {
    final String value = dataType.get().replace("'", "''");
    return value.length() == 0 || value.charAt(0) == ' ' ? "' " + value + "'" : "'" + value + "'";
  }

  @Override
  void compile(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof kind.BINARY) {
      compilation.append("UTL_RAW.CAST_TO_RAW((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.BLOB) {
      compilation.append("TO_BLOB((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.CLOB) {
      compilation.append("TO_CLOB((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("))");
    }
    else if (as.cast instanceof kind.DATE && !(as.dataType instanceof kind.DATETIME)) {
      compilation.append("TO_DATE((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("), 'YYYY-MM-DD')");
    }
    else if (as.cast instanceof kind.DATETIME && !(as.dataType instanceof kind.DATETIME)) {
      compilation.append("TO_TIMESTAMP((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("), 'YYYY-MM-DD HH24:MI:SS.FF')");
    }
    else if (as.cast instanceof kind.TIME && as.dataType instanceof kind.DATETIME) {
      compilation.append("CAST(CASE WHEN (");
      compilable(as.dataType).compile(compilation, true);
      compilation.append(") IS NULL THEN NULL ELSE '+0 ' || TO_CHAR((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append("), 'HH24:MI:SS.FF') END");
      compilation.append(" AS ").append(as.cast.declare(compilation.vendor)).append(')');
    }
    else if (as.cast instanceof kind.CHAR && as.dataType instanceof kind.TIME) {
      compilation.append("SUBSTR(CAST((");
      compilable(as.dataType).compile(compilation, true);
      compilation.append(") AS ").append(new type.CHAR(((type.CHAR)as.cast).length(), true).declare(compilation.vendor)).append("), 10, 18)");
    }
    else {
      compilation.append("CAST((");
      if (as.cast instanceof kind.TIME && !(as.dataType instanceof kind.TIME))
        compilation.append("'+0 ' || ");

      compilation.append('(');
      compilable(as.dataType).compile(compilation, true);
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
  void updateColumn(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = dataType.get();
    if (value != null)
      resultSet.updateString(columnIndex, value.length() == 0 || value.charAt(0) == ' ' ? " " + value : value);
    else
      resultSet.updateNull(columnIndex);
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
      statement.setObject(parameterIndex, newINTERVALDS("+0 " + Dialect.timeToString(value)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Object value = resultSet.getObject(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    final LocalTime localTime = Dialect.timeFromString(value.toString().substring(value.toString().indexOf(' ') + 1));
    return value.toString().charAt(0) == '-' ? Temporals.subtract(LocalTime.MIDNIGHT, localTime) : localTime;
  }

  @Override
  String compile(final type.DATETIME dataType) {
    return dataType.isNull() ? "NULL" : "TO_TIMESTAMP(('" + Dialect.dateTimeToString(dataType.get()) + "'), 'YYYY-MM-DD HH24:MI:SS.FF')";
  }

  @Override
  void compileNextSubject(final kind.Subject<?> subject, final int index, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    if (!isFromGroupBy && (subject instanceof ComparisonPredicate || subject instanceof BooleanTerm || subject instanceof Predicate)) {
      compilation.append("CASE WHEN ");
      super.compileNextSubject(subject, index, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
      compilation.append(" THEN 1 ELSE 0 END");
    }
    else {
      super.compileNextSubject(subject, index, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
    }

    if (!isFromGroupBy && !(subject instanceof type.Entity) && (!(subject instanceof type.Subject) || !(((type.Subject<?>)subject).wrapper() instanceof As)))
      compilation.append(" c" + index);
  }

  @Override
  void compileFor(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: Log (once) that this is unsupported.
    select.forLockStrength = SelectImpl.untyped.SELECT.LockStrength.UPDATE;
    select.forLockOption = null;
    super.compileFor(select, compilation);
  }

  @Override
  void compileForOf(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: It seems Oracle does support this.
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  void compileInsertOnConflict(final type.DataType<?>[] columns, final Select.untyped.SELECT<?> select, final type.DataType<?>[] onConflict, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,type.ENUM<?>> translateTypes = new HashMap<>();
    compilation.append("MERGE INTO (");
    compilation.append(q(columns[0].owner.name()));
    compilation.append(") a USING (");
    final List<String> columnNames;
    if (select == null) {
      compilation.append("SELECT ");
      boolean added = false;
      columnNames = new ArrayList<>();
      for (int i = 0; i < columns.length; ++i) {
        final type.DataType column = columns[i];
        if (column.isNull()) {
          if (!column.wasSet() && column.generateOnInsert != null)
            column.generateOnInsert.generate(column, compilation.vendor);
          else
            continue;
        }

        if (added)
          compilation.comma();

        compilation.addParameter(column, false);
        final String columnName = q(column.name);
        columnNames.add(columnName);
        compilation.concat(" " + columnName);
        added = true;
      }

      compilation.append("FROM dual");
    }
    else {
      final SelectImpl.untyped.SELECT<?> selectImpl = (SelectImpl.untyped.SELECT<?>)select;
      final Compilation selectCompilation = compilation.newSubCompilation(selectImpl);
      selectImpl.setTranslateTypes(translateTypes);
      selectImpl.compile(selectCompilation, false);
      compilation.append(selectCompilation);
      columnNames = compilation.getColumnTokens();
    }

    compilation.append(") b ON (");

    boolean added = false;
    for (int i = 0; i < columns.length; ++i) {
      final type.DataType column = columns[i];
      if (ArrayUtil.contains(onConflict, column)) {
        if (added)
          compilation.comma();

        compilation.append("a." + q(column.name)).append(" = ").append("b." + columnNames.get(i));
        added = true;
      }
    }

    added = false;
    compilation.append(") WHEN MATCHED THEN UPDATE SET ");
    final StringBuilder names = new StringBuilder();
    final StringBuilder values = new StringBuilder();
    for (int i = 0; i < columns.length; ++i) {
      final type.DataType column = columns[i];
      if (i > 0) {
        names.append(COMMA);
        values.append(COMMA);
      }

      final String name = q(column.name);
      final String value = "b." + columnNames.get(i);
      names.append(name);
      values.append(value);
      if (!ArrayUtil.contains(onConflict, column)) {
        if (added)
          compilation.comma();

        compilation.append("a." + name).append(" = ").append(value);
        added = true;
      }
    }

    compilation.append(") WHEN NOT MATCHED THEN INSERT (").append(names).append(") VALUES (").append(values);
  }
}