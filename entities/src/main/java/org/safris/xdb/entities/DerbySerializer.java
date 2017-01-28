/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Set;

import org.safris.commons.lang.Classes;
import org.safris.commons.math.Functions;
import org.safris.xdb.entities.Interval.Unit;
import org.safris.xdb.entities.Select.GROUP_BY;
import org.safris.xdb.entities.Select.SELECT;
import org.safris.xdb.schema.DBVendor;

final class DerbySerializer extends Serializer {
  public static final class Function {
    public static double mod(final double a, final double b) {
      return a % b;
    }

    public static double log(final double a, final double b) {
      return Math.log(a) / Math.log(b);
    }

    public static double log2(final double a) {
      return Math.log(a) / 0.6931471805599453;
    }

    public static Date add(final Date date, final String string) {
      return add(date, Interval.valueOf(string), 1);
    }

    public static Date add(final Time time, final String string) {
      return add(time, Interval.valueOf(string), 1);
    }

    public static Date add(final Timestamp timestamp, final String string) {
      return add(timestamp, Interval.valueOf(string), 1);
    }

    public static Date sub(final Date date, final String string) {
      return add(date, Interval.valueOf(string), -1);
    }

    public static Date sub(final Time time, final String string) {
      return add(time, Interval.valueOf(string), -1);
    }

    public static Date sub(final Timestamp timestamp, final String string) {
      return add(timestamp, Interval.valueOf(string), -1);
    }

    private static Date add(final java.util.Date date, final Interval interval, final int sign) {
      final Calendar calendar = Calendar.getInstance();
      calendar.setTime(date);
      for (final Unit unit : interval.getUnits())
        calendar.add(unit.getCalendarField(), sign * unit.getFieldScale() * interval.getComponent(unit));

      return new Date(calendar.getTimeInMillis());
    }

    private Function() {
    }
  }

  @Override
  protected DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      statement.execute("CREATE FUNCTION LOG(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".log'");
      statement.execute("CREATE FUNCTION LOG2(a DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".log2'");
      statement.execute("CREATE FUNCTION POWER(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Math.class.getName() + ".pow'");
      statement.execute("CREATE FUNCTION ROUND(a DOUBLE, b INT) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Classes.getStrictName(Functions.class) + ".round'");
      statement.execute("CREATE FUNCTION DMOD(a DOUBLE, b DOUBLE) RETURNS DOUBLE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".mod'");
      statement.execute("CREATE FUNCTION DATE_ADD(a DATE, b VARCHAR(255)) RETURNS DATE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      statement.execute("CREATE FUNCTION DATE_SUB(a DATE, b VARCHAR(255)) RETURNS DATE PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
      statement.execute("CREATE FUNCTION TIME_ADD(a TIME, b VARCHAR(255)) RETURNS TIME PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      statement.execute("CREATE FUNCTION TIME_SUB(a TIME, b VARCHAR(255)) RETURNS TIME PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
      statement.execute("CREATE FUNCTION TIMESTAMP_ADD(a TIMESTAMP, b VARCHAR(255)) RETURNS TIMESTAMP PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".add'");
      statement.execute("CREATE FUNCTION TIMESTAMP_SUB(a TIMESTAMP, b VARCHAR(255)) RETURNS TIMESTAMP PARAMETER STYLE JAVA NO SQL LANGUAGE JAVA EXTERNAL NAME '" + Function.class.getName() + ".sub'");
    }
  }

  @Override
  protected void serialize(final Interval interval, final Serialization serialization) {
    final Set<Unit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    // FIXME:...
    if (units.size() > 1)
      throw new UnsupportedOperationException("FIXME: units.size() > 1");

    for (final Unit unit : units)
      clause.append(" ").append(interval.getComponent(unit)).append(" " + unit.name());

    serialization.append("'").append(clause.substring(1)).append("'");
  }

  @Override
  protected void serialize(final TemporalExpression<?> expression, final Serialization serialization) throws IOException {
    if (expression.a instanceof data.Date)
      serialization.append("DATE");
    else if (expression.a instanceof data.Time)
      serialization.append("TIME");
    else if (expression.a instanceof data.DateTime)
      serialization.append("TIMESTAMP");
    else
      throw new UnsupportedOperationException("Unexpected temporal type: " + expression.a.getClass());

    serialization.append(expression.operator == Operator.PLUS ? "_ADD(" : "_SUB(");
    expression.a.serialize(serialization);
    serialization.append(", ");
    expression.b.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final NumericExpression<?> expression, final Serialization serialization) throws IOException {
    serialization.append("(");
    expression.a.serialize(serialization);
    serialization.append(" ").append(expression.operator.toString()).append(" ");
    expression.b.serialize(serialization);
    for (int i = 0; i < expression.args.length; i++) {
      final Serializable arg = expression.args[i];
      serialization.append(" ").append(expression.operator.toString()).append(" ");
      arg.serialize(serialization);
    }
    serialization.append(")");
  }

  @Override
  protected void serialize(final function.numeric.Mod<? extends Number> function, final Serialization serialization) throws IOException {
    serialization.append("DMOD(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final Select.GROUP_BY<?> groupBy, final Serialization serialization) throws IOException {
    if (groupBy != null) {
      final SelectCommand command = (SelectCommand)serialization.command;
      groupBy.subjects.addAll(command.select().getEntitiesWithOwners());
      super.serialize(groupBy, serialization);
    }
  }

  @Override
  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void serialize(final Select.HAVING<?> having, final Serialization serialization) throws IOException {
    if (having != null) {
      final SELECT<?> select = (SELECT<?>)Keyword.getParentRoot(having.parent());
      final SelectCommand command = (SelectCommand)serialization.command;
      if (command.groupBy() == null) {
        final GROUP_BY<?> groupBy = new GROUP_BY(null, select.getEntitiesWithOwners());
        serialize(groupBy, serialization);
      }

      serialization.append(" HAVING ");
      having.condition.serialize(serialization);
    }
  }

  @Override
  protected void serialize(final Select.LIMIT<?> limit, final Select.OFFSET<?> offset, final Serialization serialization) {
    if (limit != null) {
      if (offset != null)
        serialization.append(" OFFSET ").append(offset.rows).append(" ROWS");

      serialization.append(" FETCH NEXT ").append(limit.rows).append(" ROWS ONLY");
    }
  }
}