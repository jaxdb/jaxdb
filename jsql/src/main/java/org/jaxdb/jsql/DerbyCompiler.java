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
import java.sql.Date;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jaxdb.jsql.Command.Select.untyped;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DbVendor;
import org.libj.math.SafeMath;
import org.libj.sql.DateTimes;

final class DerbyCompiler extends Compiler {
  public static final class Function {
    public static Double power(final Double a, final Double b) {
      return a == null || b == null ? null : StrictMath.pow(a, b);
    }

    public static Double round(final Double a, final Integer b) {
      return a == null || b == null ? null : SafeMath.round(a, b);
    }

    public static Double mod(final Double a, final Double b) {
      return a == null || b == null ? null : a % b;
    }

    public static Double log(final Double a, final Double b) {
      return a == null || b == null ? null : SafeMath.log(a, b);
    }

    public static Double log2(final Double a) {
      return a == null ? null : SafeMath.log2(a);
    }

    public static Timestamp now() {
      return new Timestamp(System.currentTimeMillis());
    }

    public static Date add(final Date date, final String string) {
      return Date.valueOf(Interval.valueOf(string).addTo(date.toLocalDate()));
    }

    public static Date sub(final Date date, final String string) {
      return Date.valueOf(Interval.valueOf(string).subtractFrom(date.toLocalDate()));
    }

    public static Time add(final Time time, final String string) {
      return DateTimes.toTime(Interval.valueOf(string).addTo(time.toLocalTime()));
    }

    public static Time sub(final Time time, final String string) {
      return DateTimes.toTime(Interval.valueOf(string).subtractFrom(time.toLocalTime()));
    }

    public static Timestamp add(final Timestamp timestamp, final String string) {
      return Interval.valueOf(string).addTo(timestamp);
    }

    public static Timestamp sub(final Timestamp timestamp, final String string) {
      return Interval.valueOf(string).subtractFrom(timestamp);
    }

    private Function() {
    }
  }

  private static void createFunction(final Statement statement, final String function) throws SQLException {
    try {
      statement.execute(function);
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  DerbyCompiler() {
    super(DbVendor.DERBY);
  }

  @Override
  void onConnect(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      statement.execute("SET SCHEMA APP");
    }
  }

  @Override
  void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      createFunction(statement, "CREATE FUNCTION LOG(b DOUBLE, n DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".log'");
      createFunction(statement, "CREATE FUNCTION LOG2(a DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".log2'");
      createFunction(statement, "CREATE FUNCTION POWER(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".power'");
      createFunction(statement, "CREATE FUNCTION ROUND(a DOUBLE, b INT) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".round'");
      createFunction(statement, "CREATE FUNCTION DMOD(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".mod'");
      createFunction(statement, "CREATE FUNCTION NOW() RETURNS TIMESTAMP PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".now'");
      createFunction(statement, "CREATE FUNCTION DATE_ADD(a DATE, b VARCHAR(255)) RETURNS DATE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      createFunction(statement, "CREATE FUNCTION DATE_SUB(a DATE, b VARCHAR(255)) RETURNS DATE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
      createFunction(statement, "CREATE FUNCTION TIME_ADD(a TIME, b VARCHAR(255)) RETURNS TIME PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      createFunction(statement, "CREATE FUNCTION TIME_SUB(a TIME, b VARCHAR(255)) RETURNS TIME PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
      createFunction(statement, "CREATE FUNCTION TIMESTAMP_ADD(a TIMESTAMP, b VARCHAR(255)) RETURNS TIMESTAMP PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      createFunction(statement, "CREATE FUNCTION TIMESTAMP_SUB(a TIMESTAMP, b VARCHAR(255)) RETURNS TIMESTAMP PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
    }
  }

  @Override
  StringBuilder compileColumn(final StringBuilder b, final data.BLOB column, final boolean isForUpdateWhere) throws IOException {
    b.append("CAST (");
    super.compileColumn(b, column, isForUpdateWhere);
    return b.append(" AS BLOB)");
  }

  private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").toFormatter();

  @Override
  StringBuilder compileColumn(final StringBuilder b, final data.TIME column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(TIME_FORMAT.format(column.getForUpdateWhereGetOld(isForUpdateWhere))).append('\'');
  }

  @Override
  boolean supportsPreparedBatch() {
    return false;
  }

  @Override
  boolean supportsReturnGeneratedKeysBatch() {
    return false;
  }

  @Override
  void compileFrom(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from() != null)
      super.compileFrom(select, useAliases, compilation);
    else
      compilation.sql.append(" FROM SYSIBM.SYSDUMMY1");
  }

  @Override
  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    final StringBuilder sql = compilation.sql;
    if (a instanceof data.DATE)
      sql.append("DATE");
    else if (a instanceof data.TIME)
      sql.append("TIME");
    else if (a instanceof data.DATETIME)
      sql.append("TIMESTAMP");
    else
      throw new UnsupportedOperationException("Unsupported temporal type: " + a.getClass().getName());

    sql.append("_").append(o).append('(');
    toSubject(a).compile(compilation, true);
    sql.append(", ");

    final ArrayList<TemporalUnit> units = b.getUnits();
    // FIXME:...
    if (units.size() > 1)
      throw new UnsupportedOperationException("FIXME: units.size() > 1");

    sql.append('\'');
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        sql.append(' ');

      sql.append(b.get(unit)).append(' ').append(unit);
    }

    sql.append("')");
  }

  @Override
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("DMOD(");
    toSubject(a).compile(compilation, true);
    sql.append(", ");
    toSubject(b).compile(compilation, true);
    sql.append(')');
  }

  @Override
  void compileGroupByHaving(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.groupBy == null && select.having != null) {
      final untyped.SELECT<?> command = (untyped.SELECT<?>)compilation.command;
      select.groupBy = command.getEntitiesWithOwners();
    }

    super.compileGroupByHaving(select, useAliases, compilation);
  }

  @Override
  void compileLimitOffset(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.limit != -1) {
      final StringBuilder sql = compilation.sql;
      if (select.offset != -1)
        sql.append(" OFFSET ").append(select.offset).append(" ROWS");

      sql.append(" FETCH NEXT ").append(select.limit).append(" ROWS ONLY");
    }
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
    final StringBuilder sql = compilation.sql;
    sql.append(" OF ");
    final HashSet<data.Column<?>> columns = new HashSet<>(1);
    for (int i = 0, i$ = select.forSubjects.length; i < i$; ++i) { // [A]
      final Subject entity = select.forSubjects[i];
      if (entity instanceof data.Table)
        Collections.addAll(columns, ((data.Table)entity)._column$);
      else if (entity instanceof data.Column)
        columns.add((data.Column<?>)entity);
      else
        throw new UnsupportedOperationException("Unsupported type.Entity: " + entity.getClass().getName());
    }

    if (columns.size() > 0) {
      final Iterator<data.Column<?>> iterator = columns.iterator();
      for (int i = 0; iterator.hasNext(); ++i) { // [I]
        final data.Column<?> column = iterator.next();
        if (i > 0)
          sql.append(", ");

        q(sql, column.name);
      }
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,data.ENUM<?>> translateTypes;
    final StringBuilder sql = compilation.sql;
    sql.append("MERGE INTO ");
    q(sql, columns[0].getTable().getName()).append(" b USING ");
    final List<String> selectColumnNames;
    final Condition<?> matchRefinement;
    boolean modified = false;
    if (select == null) {
      translateTypes = null;
      selectColumnNames = null;
      matchRefinement = null;
      sql.append("SYSIBM.SYSDUMMY1 a ON ");
      for (int i = 0, i$ = onConflict.length; i < i$; ++i) { // [A]
        if (i > 0)
          sql.append(" AND ");

        final data.Column column = onConflict[i];
        sql.append("b.");
        q(sql, column.name).append(column.isNull() ? " IS " : " = ");
        compilation.addParameter(column, false, false);
      }
    }
    else {
      final Command.Select.untyped.SELECT<?> selectCommand = (Command.Select.untyped.SELECT<?>)select;
      if (selectCommand.limit != -1)
        throw new SQLSyntaxErrorException("Derby does not support LIMIT function in MERGE clause");

      if (selectCommand.joins != null)
        throw new SQLSyntaxErrorException("Derby does not support JOIN function in MERGE clause");

      final Compilation selectCompilation = compilation.newSubCompilation(selectCommand);
      selectCommand.translateTypes = translateTypes = new HashMap<>();
      selectCommand.compile(selectCompilation, false);
      q(sql, selectCommand.from()[0].getName()).append(" a ON ");
      selectColumnNames = selectCompilation.getColumnTokens();

      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (column.primary) {
          if (modified)
            sql.append(" AND ");

          sql.append("b.");
          q(sql, column.name).append(" = a.").append(selectColumnNames.get(i));
          modified = true;
        }
      }

      matchRefinement = selectCommand.where();
    }

    if (doUpdate) {
      sql.append(" WHEN MATCHED");
      if (matchRefinement != null) {
        sql.append(" AND ");
        matchRefinement.compile(compilation, false);
      }

      sql.append(" THEN UPDATE SET ");
      modified = false;
      for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
        final data.Column column = columns[i];
        if (selectColumnNames != null || shouldUpdate(column, compilation)) {
          if (modified)
            sql.append(", ");

          sql.append("b.");
          q(sql, column.name).append(" = ");
          if (selectColumnNames != null)
            sql.append(" a.").append(selectColumnNames.get(i));
          else
            compilation.addParameter(column, false, false);

          modified = true;
        }
      }
    }

    sql.append(" WHEN NOT MATCHED");
    if (matchRefinement != null) {
      sql.append(" AND ");
      matchRefinement.compile(compilation, false);
    }

    final ArrayList<data.Column> insertValues = new ArrayList<>();
    final StringBuilder insertNames = new StringBuilder();
    modified = false;
    for (int i = 0, i$ = columns.length; i < i$; ++i) { // [A]
      final data.Column column = columns[i];
      if (select != null || shouldInsert(column, true, compilation)) {
        if (modified)
          insertNames.append(", ");

        insertValues.add(column);
        q(insertNames, column.name);
        if (translateTypes != null && column instanceof data.ENUM<?>)
          translateTypes.put(i, (data.ENUM<?>)column);

        modified = true;
      }
    }

    sql.append(" THEN INSERT (").append(insertNames).append(") VALUES (");
    for (int i = 0, i$ = insertValues.size(); i < i$; ++i) { // [RA]
      final data.Column column = insertValues.get(i);
      if (i > 0)
        sql.append(", ");

      if (selectColumnNames != null)
        sql.append("a.").append(selectColumnNames.get(i));
      else
        compilation.addParameter(column, false, false);
    }

    sql.append(')');
  }
}