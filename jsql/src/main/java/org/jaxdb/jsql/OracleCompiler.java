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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.keyword.Cast;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DbVendor;
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

  OracleCompiler() {
    super(DbVendor.ORACLE);
  }

  @Override
  void onConnect(final Connection connection) {
  }

  @Override
  void onRegister(final Connection connection) {
  }

  @Override
  StringBuilder compileAs(final StringBuilder b, final As<?> as) {
    return b;
  }

  @Override
  void compileSelect(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.limit != -1) {
      compilation.sql.append("SELECT * FROM (");
      if (select.offset != -1) {
        compilation.sql.append("SELECT ROWNUM rnum3729, r.* FROM (");
        compilation.skipFirstColumn(true);
      }
    }

    super.compileSelect(select, useAliases, compilation);
  }

  @Override
  void compileFrom(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from() != null)
      super.compileFrom(select, useAliases, compilation);
    else
      compilation.sql.append(" FROM dual");
  }

  @Override
  void compileLimitOffset(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.limit != -1) {
      compilation.sql.append(") r WHERE ROWNUM <= ");
      if (select.offset != -1)
        compilation.sql.append(String.valueOf(select.limit + select.offset)).append(") WHERE rnum3729 > ").append(select.offset);
      else
        compilation.sql.append(String.valueOf(select.limit));
    }
  }

  @Override
  void compilePi(final Compilation compilation) {
    compilation.sql.append("ACOS(-1)");
  }

  @Override
  void compileLog2(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("LOG(2, ");
    toSubject(a).compile(compilation, true);
    compilation.sql.append(')');
  }

  @Override
  void compileLog10(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("LOG(10, ");
    toSubject(a).compile(compilation, true);
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
    if (b.getUnits().size() != 1)
      throw new UnsupportedOperationException("TODO");

    final List<TemporalUnit> units = b.getUnits();
    Interval.Unit unit = (Interval.Unit)units.get(units.size() - 1);
    final boolean isNumToY = unit == Interval.Unit.MONTHS || unit == Interval.Unit.QUARTERS || unit == Interval.Unit.YEARS || unit == Interval.Unit.DECADES || unit == Interval.Unit.CENTURIES || unit == Interval.Unit.MILLENNIA;

    toSubject(a).compile(compilation, true);
    if (a instanceof type.TIME && isNumToY)
      return;

    compilation.sql.append(' ');
    compilation.sql.append(o);
    compilation.sql.append(' ');

    if (unit == Interval.Unit.MONTHS || unit == Interval.Unit.QUARTERS) {
      compilation.sql.append("NUMTOYMINTERVAL(").append(b.convertTo(Interval.Unit.MONTHS)).append(", 'MONTH')");
    }
    else if (unit == Interval.Unit.YEARS || unit == Interval.Unit.DECADES || unit == Interval.Unit.CENTURIES || unit == Interval.Unit.MILLENNIA) {
      compilation.sql.append("NUMTOYMINTERVAL(").append(b.convertTo(Interval.Unit.YEARS)).append(", 'YEAR')");
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
      compilation.sql.append("INTERVAL '").append(b.convertTo(unit)).append("' ").append(unitString);
    }
  }

  @Override
  StringBuilder compileColumn(final StringBuilder b, final data.CHAR column, final boolean isForUpdateWhere) {
    if (column.getForUpdateWhereIsNullOld(isForUpdateWhere))
      return b.append("NULL");

    final String value = column.getForUpdateWhereGetOld(isForUpdateWhere).replace("'", "''");
    return value.length() == 0 || value.charAt(0) == ' ' ? b.append("' ").append(value).append('\'') : b.append('\'').append(value).append('\'');
  }

  @Override
  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    if (as.cast instanceof type.BINARY) {
      compilation.sql.append("UTL_RAW.CAST_TO_RAW((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("))");
    }
    else if (as.cast instanceof type.BLOB) {
      compilation.sql.append("TO_BLOB((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("))");
    }
    else if (as.cast instanceof type.CLOB) {
      compilation.sql.append("TO_CLOB((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("))");
    }
    else if (as.cast instanceof type.DATE && !(as.column instanceof type.DATETIME)) {
      compilation.sql.append("TO_DATE((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("), 'YYYY-MM-DD')");
    }
    else if (as.cast instanceof type.DATETIME && !(as.column instanceof type.DATETIME)) {
      compilation.sql.append("TO_TIMESTAMP((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("), 'YYYY-MM-DD HH24:MI:SS.FF')");
    }
    else if (as.cast instanceof type.TIME && as.column instanceof type.DATETIME) {
      compilation.sql.append("CAST(CASE WHEN (");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append(") IS NULL THEN NULL ELSE '+0 ' || TO_CHAR((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append("), 'HH24:MI:SS.FF') END");
      compilation.sql.append(" AS ");
      as.cast.declare(compilation.sql, compilation.vendor).append(')');
    }
    else if (as.cast instanceof type.CHAR && as.column instanceof type.TIME) {
      compilation.sql.append("SUBSTR(CAST((");
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append(") AS ");
      new data.CHAR(((data.CHAR)as.cast).length(), true).declare(compilation.sql, compilation.vendor).append("), 10, 18)");
    }
    else {
      compilation.sql.append("CAST((");
      if (as.cast instanceof type.TIME && !(as.column instanceof type.TIME))
        compilation.sql.append("'+0 ' || ");

      compilation.sql.append('(');
      toSubject(as.column).compile(compilation, true);
      compilation.sql.append(")) AS ");
      as.cast.declare(compilation.sql, compilation.vendor).append(')');
    }
  }

  @Override
  void setParameter(final data.CHAR column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final String value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setString(parameterIndex, value.length() == 0 || value.charAt(0) == ' ' ? " " + value : value); // FIXME: StringBuilder
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  @Override
  void updateColumn(final data.CHAR column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = column.get();
    if (value != null)
      resultSet.updateString(columnIndex, value.length() == 0 || value.charAt(0) == ' ' ? " " + value : value); // FIXME: StringBuilder
    else
      resultSet.updateNull(columnIndex);
  }

  @Override
  String getParameter(final data.CHAR column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value != null && value.startsWith(" ") ? value.substring(1) : value;
  }

  @Override
  void setParameter(final data.TIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setObject(parameterIndex, newINTERVALDS("+0 " + Dialect.timeToString(value)));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  @Override
  LocalTime getParameter(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Object value = resultSet.getObject(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    final LocalTime localTime = Dialect.timeFromString(value.toString().substring(value.toString().indexOf(' ') + 1));
    return value.toString().charAt(0) == '-' ? Temporals.subtract(LocalTime.MIDNIGHT, localTime) : localTime;
  }

  @Override
  StringBuilder compileColumn(final StringBuilder b, final data.DATETIME column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append("TO_TIMESTAMP(('").append(Dialect.dateTimeToString(column.getForUpdateWhereGetOld(isForUpdateWhere))).append("'), 'YYYY-MM-DD HH24:MI:SS.FF')");
  }

  @Override
  void compileNextSubject(final Subject subject, final int index, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    if (!isFromGroupBy && (subject instanceof ComparisonPredicate || subject instanceof BooleanTerm || subject instanceof Predicate)) {
      compilation.sql.append("CASE WHEN ");
      super.compileNextSubject(subject, index, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
      compilation.sql.append(" THEN 1 ELSE 0 END");
    }
    else {
      super.compileNextSubject(subject, index, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
    }

    if (!isFromGroupBy && !(subject instanceof data.Table) && (!(subject instanceof data.Entity) || !(((data.Entity<?>)subject).wrapped() instanceof As)))
      compilation.sql.append(" c").append(index);
  }

  @Override
  void compileFor(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: Log (once) that this is unsupported.
    select.forLockStrength = Command.Select.untyped.SELECT.LockStrength.UPDATE;
    select.forLockOption = null;
    super.compileFor(select, compilation);
  }

  @Override
  void compileForOf(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    // FIXME: It seems Oracle does support this.
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,data.ENUM<?>> translateTypes;
    compilation.sql.append("MERGE INTO ");
    q(compilation.sql, columns[0].getTable().getName()).append(" a USING (");
    final List<String> columnNames;
    if (select == null) {
      compilation.sql.append("SELECT ");
      translateTypes = null;
      columnNames = new ArrayList<>();
      boolean modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (shouldInsert(column, true, compilation)) {
          if (modified)
            compilation.sql.append(", ");

          compilation.addParameter(column, false, false);
          final String columnName = q(column.name);
          columnNames.add(columnName);
          compilation.sql.append(" AS ").append(columnName);
          modified = true;
        }
      }

      compilation.sql.append(" FROM dual");
    }
    else {
      final Command.Select.untyped.SELECT<?> selectCommand = (Command.Select.untyped.SELECT<?>)select;
      final Compilation selectCompilation = compilation.newSubCompilation(selectCommand);
      selectCommand.translateTypes = translateTypes = new HashMap<>();
      selectCommand.compile(selectCompilation, false);
      compilation.sql.append(selectCompilation);
      columnNames = selectCompilation.getColumnTokens();
    }

    compilation.sql.append(") b ON (");

    boolean modified = false;
    for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
      final data.Column column = columns[i];
      if (column.primary) {
        if (modified)
          compilation.sql.append(", ");

        compilation.sql.append("a.");
        q(compilation.sql, column.name).append(" = ").append("b.").append(columnNames.get(i));
        modified = true;
      }
    }

    compilation.sql.append(')');
    final StringBuilder insertNames = new StringBuilder();
    final StringBuilder insertValues = new StringBuilder();
    modified = false;
    for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
      final data.Column column = columns[i];
      if (shouldInsert(column, false, compilation)) {
        if (modified) {
          insertNames.append(", ");
          insertValues.append(", ");
        }

        q(insertNames, column.name);
        insertValues.append("b.").append(columnNames.get(i));
        if (translateTypes != null && column instanceof data.ENUM<?>)
          translateTypes.put(i, (data.ENUM<?>)column);

        modified = true;
      }
    }

    if (doUpdate) {
      compilation.sql.append(" WHEN MATCHED THEN UPDATE SET ");
      modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (shouldUpdate(column, compilation)) {
          if (modified)
            compilation.sql.append(", ");

          compilation.sql.append("a.");
          q(compilation.sql, column.name).append(" = b.").append(columnNames.get(i));
          modified = true;
        }
      }
    }

    compilation.sql.append(" WHEN NOT MATCHED THEN INSERT (").append(insertNames).append(") VALUES (").append(insertValues).append(')');
  }

  @Override
  boolean supportsReturnGeneratedKeysBatch() {
    return false;
  }

  private String[] getNames(final Column<?>[] autos) {
    final String[] names = new String[autos.length];
    for (int i = 0, i$ = autos.length; i < i$; ++i) // [A]
      names[i] = q(autos[i].name);

    return names;
  }

  @Override
  PreparedStatement prepareStatementReturning(final Connection connection, final StringBuilder sql, final Column<?>[] autos) throws SQLException {
    return connection.prepareStatement(sql.toString(), getNames(autos));
  }

  @Override
  int executeUpdateReturning(final Statement statement, final StringBuilder sql, final data.Column<?>[] autos) throws SQLException {
    return statement.executeUpdate(sql.toString(), getNames(autos));
  }

  // FIXME: Figure out how to remove this
  private final String q(final CharSequence identifier) {
    return getDialect().quoteIdentifier(new StringBuilder(identifier.length() + 2), identifier).toString();
  }
}