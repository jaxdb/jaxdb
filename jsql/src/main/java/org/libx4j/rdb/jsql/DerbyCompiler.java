/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.temporal.TemporalUnit;
import java.util.List;

import org.lib4j.math.Constants;
import org.lib4j.math.SafeMath;
import org.lib4j.sql.DateTimes;
import org.libx4j.rdb.jsql.Select.GROUP_BY;
import org.libx4j.rdb.jsql.Select.SELECT;
import org.libx4j.rdb.vendor.DBVendor;

final class DerbyCompiler extends Compiler {
  public static final class Function {
    public static Double power(final Double a, final Double b) {
      return a == null || b == null ? null : Math.pow(a, b);
    }

    public static Double round(final Double a, final Integer b) {
      return a == null || b == null ? null : SafeMath.round(a, b);
    }

    public static Double mod(final Double a, final Double b) {
      return a == null || b == null ? null : a % b;
    }

    public static Double log(final Double a, final Double b) {
      return a == null || b == null ? null : Math.log(b) / Math.log(a);
    }

    public static Double log2(final Double a) {
      return a == null ? null : Math.log(a) / Constants.LOG_2;
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

  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  private static void createFunction(final Statement statement, final String function) throws SQLException {
    try {
//      statement.execute("DROP FUNCTION " + function.substring(16, function.indexOf("(")));
      statement.execute(function);
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
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
  protected void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    // FIXME:...
    if (units.size() > 1)
      throw new UnsupportedOperationException("FIXME: units.size() > 1");

    for (final TemporalUnit unit : units)
      clause.append(" ").append(interval.get(unit)).append(" " + unit);

    compilation.append("'").append(clause.substring(1)).append("'");
  }

  @Override
  protected void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    if (expression.a instanceof type.DATE)
      compilation.append("DATE");
    else if (expression.a instanceof type.TIME)
      compilation.append("TIME");
    else if (expression.a instanceof type.DATETIME)
      compilation.append("TIMESTAMP");
    else
      throw new UnsupportedOperationException("Unsupported temporal type: " + expression.a.getClass());

    compilation.append(expression.operator == operator.Arithmetic.PLUS ? "_ADD(" : "_SUB(");
    expression.a.compile(compilation);
    compilation.append(", ");
    expression.b.compile(compilation);
    compilation.append(")");
  }

  @Override
  protected void compile(final expression.Numeric expression, final Compilation compilation) throws IOException {
    compilation.append("(");
    expression.a.compile(compilation);
    compilation.append(" ").append(expression.operator.toString()).append(" ");
    expression.b.compile(compilation);
    compilation.append(")");
  }

  @Override
  protected void compile(final function.Mod function, final Compilation compilation) throws IOException {
    compilation.append("DMOD(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void compile(final Select.HAVING<?> having, final Compilation compilation) throws IOException {
    if (having != null) {
      final SELECT<?> select = ((SelectCommand)compilation.command).select();
      final SelectCommand command = (SelectCommand)compilation.command;
      if (command.groupBy() == null) {
        final GROUP_BY<?> groupBy = new GROUP_BY(null, select.getEntitiesWithOwners());
        compile(groupBy, compilation);
      }

      compilation.append(" HAVING ");
      having.condition.compile(compilation);
    }
  }

  @Override
  protected void compile(final Select.LIMIT<?> limit, final Select.OFFSET<?> offset, final Compilation compilation) {
    if (limit != null) {
      if (offset != null)
        compilation.append(" OFFSET ").append(offset.rows).append(" ROWS");

      compilation.append(" FETCH NEXT ").append(limit.rows).append(" ROWS ONLY");
    }
  }
}