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
import org.jaxdb.vendor.DBVendor;
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
    super(DBVendor.DERBY);
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
  String compileColumn(final data.BLOB column, final boolean isForUpdateWhere) throws IOException {
    return "CAST (" + super.compileColumn(column, isForUpdateWhere) + " AS BLOB)";
  }

  private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").toFormatter();

  @Override
  String compileColumn(final data.TIME column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? "NULL" : "'" +  TIME_FORMAT.format(column.getForUpdateWhereGetOld(isForUpdateWhere)) + "'";
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
      compilation.append(" FROM SYSIBM.SYSDUMMY1");
  }

  @Override
  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    if (a instanceof data.DATE)
      compilation.append("DATE");
    else if (a instanceof data.TIME)
      compilation.append("TIME");
    else if (a instanceof data.DATETIME)
      compilation.append("TIMESTAMP");
    else
      throw new UnsupportedOperationException("Unsupported temporal type: " + a.getClass().getName());

    compilation.append("_" + o).append('(');
    toSubject(a).compile(compilation, true);
    compilation.comma();

    final List<TemporalUnit> units = b.getUnits();
    // FIXME:...
    if (units.size() > 1)
      throw new UnsupportedOperationException("FIXME: units.size() > 1");

    compilation.append('\'');
    for (int i = 0, len = units.size(); i < len; ++i) {
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.append(' ');

      compilation.append(b.get(unit)).append(' ').append(unit);
    }

    compilation.append('\'');
    compilation.append(')');
  }

  @Override
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.append("DMOD(");
    toSubject(a).compile(compilation, true);
    compilation.comma();
    toSubject(b).compile(compilation, true);
    compilation.append(')');
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
      if (select.offset != -1)
        compilation.append(" OFFSET ").append(select.offset).append(" ROWS");

      compilation.append(" FETCH NEXT ").append(select.limit).append(" ROWS ONLY");
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
    compilation.append(" OF ");
    final HashSet<data.Column<?>> columns = new HashSet<>(1);
    for (int i = 0; i < select.forSubjects.length; ++i) {
      final data.Entity<?> entity = select.forSubjects[i];
      if (entity instanceof data.Table)
        Collections.addAll(columns, ((data.Table<?>)entity)._column$);
      else if (entity instanceof data.Column)
        columns.add((data.Column<?>)entity);
      else
        throw new UnsupportedOperationException("Unsupported type.Entity: " + entity.getClass().getName());
    }

    final Iterator<data.Column<?>> iterator = columns.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final data.Column<?> column = iterator.next();
      if (i > 0)
        compilation.comma();

      compilation.append(q(column.name));
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final data.Column<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,data.ENUM<?>> translateTypes;
    compilation.append("MERGE INTO ").append(q(columns[0].getTable().getName())).append(" b USING ");
    final List<String> selectColumnNames;
    final Condition<?> matchRefinement;
    boolean modified = false;
    if (select == null) {
      translateTypes = null;
      selectColumnNames = null;
      matchRefinement = null;
      compilation.append("SYSIBM.SYSDUMMY1 a ON ");
      for (int i = 0; i < onConflict.length; ++i) {
        if (i > 0)
          compilation.append(" AND ");

        final data.Column column = onConflict[i];
        compilation.append("b.").append(q(column.name)).append(column.isNull() ? " IS " : " = ");
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
      compilation.append(q(selectCommand.from()[0].getName())).append(" a ON ");
      selectColumnNames = selectCompilation.getColumnTokens();

      for (int i = 0; i < columns.length; ++i) {
        final data.Column column = columns[i];
        if (column.primary) {
          if (modified)
            compilation.append(" AND ");

          compilation.append("b." + q(column.name)).append(" = a." + selectColumnNames.get(i));
          modified = true;
        }
      }

      matchRefinement = selectCommand.where();
    }

    if (doUpdate) {
      compilation.append(" WHEN MATCHED");
      if (matchRefinement != null) {
        compilation.append(" AND ");
        matchRefinement.compile(compilation, false);
      }

      compilation.append(" THEN UPDATE SET ");
      modified = false;
      for (int i = 0; i < columns.length; ++i) {
        final data.Column column = columns[i];
        if (selectColumnNames != null || shouldUpdate(column, compilation)) {
          if (modified)
            compilation.append(", ");

          compilation.append("b.").append(q(column.name)).append(" = ");
          if (selectColumnNames != null)
            compilation.append(" a." + selectColumnNames.get(i));
          else
            compilation.addParameter(column, false, false);

          modified = true;
        }
      }
    }

    compilation.append(" WHEN NOT MATCHED");
    if (matchRefinement != null) {
      compilation.append(" AND ");
      matchRefinement.compile(compilation, false);
    }

    final ArrayList<data.Column> insertValues = new ArrayList<>();
    final StringBuilder insertNames = new StringBuilder();
    modified = false;
    for (int i = 0; i < columns.length; ++i) {
      final data.Column column = columns[i];
      if (select != null || shouldInsert(column, true, compilation)) {
        if (modified)
          insertNames.append(COMMA);

        insertValues.add(column);
        insertNames.append(q(column.name));
        if (translateTypes != null && column instanceof data.ENUM<?>)
          translateTypes.put(i, (data.ENUM<?>)column);

        modified = true;
      }
    }

    compilation.append(" THEN INSERT (").append(insertNames).append(") VALUES (");
    for (int i = 0, len = insertValues.size(); i < len; ++i) {
      final data.Column column = insertValues.get(i);
      if (i > 0)
        compilation.comma();

      if (selectColumnNames != null)
        compilation.append("a." + selectColumnNames.get(i));
      else
        compilation.addParameter(column, false, false);
    }

    compilation.append(')');
  }
}