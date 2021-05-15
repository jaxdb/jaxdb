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

import org.jaxdb.jsql.SelectImpl.untyped;
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
//      statement.execute("DROP FUNCTION " + function.substring(16, function.indexOf('(')));
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
  String compile(final type.BLOB dataType) throws IOException {
    return "CAST (" + super.compile(dataType) + " AS BLOB)";
  }

  private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").toFormatter();

  @Override
  String compile(final type.TIME dataType) {
    return dataType.isNull() ? "NULL" : "'" +  TIME_FORMAT.format(dataType.get()) + "'";
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
  void compileFrom(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from() != null)
      super.compileFrom(select, useAliases, compilation);
    else
      compilation.append(" FROM SYSIBM.SYSDUMMY1");
  }

  @Override
  void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    // FIXME:...
    if (units.size() > 1)
      throw new UnsupportedOperationException("FIXME: units.size() > 1");

    compilation.append('\'');
    for (int i = 0, len = units.size(); i < len; ++i) {
      final TemporalUnit unit = units.get(i);
      if (i > 0)
        compilation.append(' ');

      compilation.append(interval.get(unit)).append(' ').append(unit);
    }

    compilation.append('\'');
  }

  @Override
  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException, SQLException {
    if (expression.a instanceof type.DATE)
      compilation.append("DATE");
    else if (expression.a instanceof type.TIME)
      compilation.append("TIME");
    else if (expression.a instanceof type.DATETIME)
      compilation.append("TIMESTAMP");
    else
      throw new UnsupportedOperationException("Unsupported temporal type: " + expression.a.getClass().getName());

    compilation.append(expression.operator == operator.Arithmetic.PLUS ? "_ADD(" : "_SUB(");
    expression.a.compile(compilation, true);
    compilation.comma();
    expression.b.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compile(final function.Mod function, final Compilation compilation) throws IOException, SQLException {
    compilation.append("DMOD(");
    function.a.compile(compilation, true);
    compilation.comma();
    function.b.compile(compilation, true);
    compilation.append(')');
  }

  @Override
  void compileGroupByHaving(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.groupBy == null && select.having != null) {
      final untyped.SELECT<?> command = (untyped.SELECT<?>)compilation.command;
      select.groupBy = command.getEntitiesWithOwners();
    }

    super.compileGroupByHaving(select, useAliases, compilation);
  }

  @Override
  void compileLimitOffset(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.limit != -1) {
      if (select.offset != -1)
        compilation.append(" OFFSET ").append(select.offset).append(" ROWS");

      compilation.append(" FETCH NEXT ").append(select.limit).append(" ROWS ONLY");
    }
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
    compilation.append(" OF ");
    final HashSet<type.DataType<?>> columns = new HashSet<>(1);
    for (int i = 0; i < select.forSubjects.length; ++i) {
      final type.Entity<?> entity = select.forSubjects[i];
      if (entity instanceof type.Table)
        Collections.addAll(columns, ((type.Table)entity)._column$);
      else if (entity instanceof type.DataType)
        columns.add((type.DataType<?>)entity);
      else
        throw new UnsupportedOperationException("Unsupported type.Entity: " + entity.getClass().getName());
    }

    final Iterator<type.DataType<?>> iterator = columns.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      final type.DataType<?> column = iterator.next();
      if (i > 0)
        compilation.comma();

      compilation.append(q(column.name));
    }
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compileInsertOnConflict(final type.DataType<?>[] columns, final Select.untyped.SELECT<?> select, final type.DataType<?>[] onConflict, final boolean doUpdate, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,type.ENUM<?>> translateTypes;
    compilation.append("MERGE INTO ").append(q(columns[0].table.name())).append(" b USING ");
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

        final type.DataType column = onConflict[i];
        compilation.append(q(column.name)).append(column.isNull() ? " IS " : " = ");
        compilation.addParameter(column, false);
      }
    }
    else {
      final SelectImpl.untyped.SELECT<?> selectImpl = (SelectImpl.untyped.SELECT<?>)select;
      if (selectImpl.limit != -1)
        throw new SQLSyntaxErrorException("Derby does not support LIMIT function in MERGE clause");

      if (selectImpl.joins != null)
        throw new SQLSyntaxErrorException("Derby does not support JOIN function in MERGE clause");

      final Compilation selectCompilation = compilation.newSubCompilation(selectImpl);
      selectImpl.translateTypes = translateTypes = new HashMap<>();
      selectImpl.compile(selectCompilation, false);
      compilation.append(q(selectImpl.from().get(0).name())).append(" a ON ");
      selectColumnNames = selectCompilation.getColumnTokens();

      for (int i = 0; i < columns.length; ++i) {
        final type.DataType column = columns[i];
        if (column.primary) {
          if (modified)
            compilation.append(" AND ");

          compilation.append("b." + q(column.name)).append(" = a." + selectColumnNames.get(i));
          modified = true;
        }
      }

      matchRefinement = selectImpl.where();
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
        final type.DataType column = columns[i];
        if (selectColumnNames != null || shouldUpdate(column, compilation)) {
          if (modified)
            compilation.append(", ");

          compilation.append("b.").append(q(column.name)).append(" = ");
          if (selectColumnNames != null)
            compilation.append(" a." + selectColumnNames.get(i));
          else
            compilation.addParameter(column, false);

          modified = true;
        }
      }
    }

    compilation.append(" WHEN NOT MATCHED");
    if (matchRefinement != null) {
      compilation.append(" AND ");
      matchRefinement.compile(compilation, false);
    }

    final ArrayList<type.DataType> insertValues = new ArrayList<>();
    final StringBuilder insertNames = new StringBuilder();
    modified = false;
    for (int i = 0; i < columns.length; ++i) {
      final type.DataType column = columns[i];
      if (select != null || shouldInsert(column, true, compilation)) {
        if (modified)
          insertNames.append(COMMA);

        insertValues.add(column);
        insertNames.append(q(column.name));
        if (translateTypes != null && column instanceof type.ENUM<?>)
          translateTypes.put(i, (type.ENUM<?>)column);

        modified = true;
      }
    }

    compilation.append(" THEN INSERT (").append(insertNames).append(") VALUES (");
    for (int i = 0, len = insertValues.size(); i < len; ++i) {
      final type.DataType column = insertValues.get(i);
      if (i > 0)
        compilation.comma();

      if (selectColumnNames != null)
        compilation.append("a." + selectColumnNames.get(i));
      else
        compilation.addParameter(column, false);
    }

    compilation.append(')');
  }
}