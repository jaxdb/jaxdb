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

package org.safris.rdb.jsql;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;

import org.safris.rdb.vendor.DBVendor;
import org.safris.rdb.vendor.Dialect;

final class OracleSerializer extends Serializer {
  private static Constructor<?> INTERVALDS;

  private static Object newINTERVALDS(final String s) {
    try {
      return (INTERVALDS == null ? INTERVALDS = Class.forName("oracle.sql.INTERVALDS").getConstructor(String.class) : INTERVALDS).newInstance(s);
    }
    catch (final ClassNotFoundException | IllegalAccessException | NoSuchMethodException | SecurityException e) {
      throw new ExceptionInInitializerError(e);
    }
    catch (final InstantiationException | InvocationTargetException e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  protected DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
  }

  @Override
  protected String serialize(final As<?> as) {
    return null;
  }

  @Override
  protected void serialize(final SelectCommand command, final Select.SELECT<?> select, final Serialization serialization) throws IOException {
    if (command.limit() != null) {
      serialization.append("SELECT * FROM (");
      if (command.offset() != null) {
        serialization.append("SELECT ROWNUM rnum3729, r.* FROM (");
        serialization.skipFirstColumn(true);
      }
    }

    super.serialize(command, select, serialization);
  }

  @Override
  protected void serialize(final Select.LIMIT<?> limit, final Select.OFFSET<?> offset, final Serialization serialization) {
    if (limit != null) {
      serialization.append(") r WHERE ROWNUM <= ");
      if (offset != null)
        serialization.append(String.valueOf(limit.rows + offset.rows)).append(") WHERE rnum3729 > ").append(offset.rows);
      else
        serialization.append(String.valueOf(limit.rows));
    }
  }

  @Override
  protected void serialize(final function.numeric.Pi function, final Serialization serialization) {
    serialization.append("ACOS(-1)");
  }

  @Override
  protected void serialize(final function.numeric.Log2 function, final Serialization serialization) throws IOException {
    serialization.append("LOG(2, ");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final function.numeric.Log10 function, final Serialization serialization) throws IOException {
    serialization.append("LOG(10, ");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final TemporalExpression expression, final Serialization serialization) throws IOException {
    expression.a.serialize(serialization);
    serialization.append(" ");
    if (expression.b instanceof Interval) {
      final Interval interval = (Interval)expression.b;
      if (interval.getUnits().size() == 1) {
        serialization.append(expression.operator.toString());
        serialization.append(" ");
        expression.b.serialize(serialization);
      }
      else {
        for (final Interval.Unit unit : interval.getUnits()) {
          serialization.append(expression.operator.toString());
          serialization.append(" ");
          new Interval(interval.getComponent(unit), unit).serialize(serialization);
        }
      }
    }
  }

  @Override
  protected void serialize(final Interval interval, final Serialization serialization) {
    if (interval.getUnits().size() > 1)
      throw new UnsupportedOperationException("Interval classes with only 1 Interval.Unit are supported");

    final Interval.Unit unit = interval.getUnits().iterator().next();
    if (unit == Interval.Unit.MICROS) {
      serialization.append("INTERVAL '").append(BigDecimal.valueOf(interval.getComponent(unit)).divide(BigDecimal.valueOf(1000000l))).append("' SECOND");
    }
    else if (unit == Interval.Unit.MILLIS) {
      serialization.append("INTERVAL '").append(BigDecimal.valueOf(interval.getComponent(unit)).divide(BigDecimal.valueOf(1000l))).append("' SECOND");
    }
    else if (unit == Interval.Unit.WEEKS) {
      serialization.append("INTERVAL '").append(interval.getComponent(unit) * 7).append("' DAY");
    }
    else if (unit == Interval.Unit.QUARTERS) {
      serialization.append("NUMTOYMINTERVAL(").append(interval.getComponent(unit) * 3).append(", 'MONTH')");
    }
    else if (unit == Interval.Unit.DECADES) {
      serialization.append("NUMTOYMINTERVAL(").append(interval.getComponent(unit) * 10).append(", 'YEAR')");
    }
    else if (unit == Interval.Unit.CENTURIES) {
      serialization.append("NUMTOYMINTERVAL(").append(interval.getComponent(unit) * 100).append(", 'YEAR')");
    }
    else if (unit == Interval.Unit.MILLENNIA) {
      serialization.append("NUMTOYMINTERVAL(").append(interval.getComponent(unit) * 1000).append(", 'YEAR')");
    }
    else if (unit.name().endsWith("S")) {
      serialization.append("INTERVAL '").append(interval.getComponent(unit)).append("' ").append(unit.name().substring(0, unit.name().length() - 1));
    }
    else {
      throw new UnsupportedOperationException("Unexpected Interval.Unit: " + unit.name());
    }
  }

  @Override
  protected String serialize(final type.CHAR serializable) {
    final String value = serializable.get().replace("'", "''");
    return value.length() == 0 || value.charAt(0) == ' ' ? "' " + value + "'" : "'" + value + "'";
  }

  @Override
  protected void serialize(final Cast.AS as, final Serialization serialization) throws IOException {
    if (as.cast instanceof type.BINARY) {
      serialization.append("UTL_RAW.CAST_TO_RAW(");
      as.dataType.serialize(serialization);
      serialization.append(")");
    }
    else if (as.cast instanceof type.BLOB) {
      serialization.append("TO_BLOB(");
      as.dataType.serialize(serialization);
      serialization.append(")");
    }
    else if (as.cast instanceof type.CLOB) {
      serialization.append("TO_CLOB(");
      as.dataType.serialize(serialization);
      serialization.append(")");
    }
    else if (as.cast instanceof type.DATE && !(as.dataType instanceof type.DATETIME)) {
      serialization.append("TO_DATE(");
      as.dataType.serialize(serialization);
      serialization.append(", 'YYYY-MM-DD')");
    }
    else if (as.cast instanceof type.DATETIME && !(as.dataType instanceof type.DATETIME)) {
      serialization.append("TO_TIMESTAMP(");
      as.dataType.serialize(serialization);
      serialization.append(", 'YYYY-MM-DD HH24:MI:SS.FF')");
    }
    else if (as.dataType instanceof type.DATETIME && as.cast instanceof type.TIME) {
      serialization.append("CAST(CASE WHEN ");
      as.dataType.serialize(serialization);
      serialization.append(" IS NULL THEN NULL ELSE '+0 ' || TO_CHAR(");
      as.dataType.serialize(serialization);
      serialization.append(", 'HH24:MI:SS.FF') END");
      serialization.append(" AS ").append(as.cast.declare(serialization.vendor)).append(")");
    }
    else if (as.dataType instanceof type.TIME && as.cast instanceof type.CHAR) {
      serialization.append("SUBSTR(CAST(");
      as.dataType.serialize(serialization);
      serialization.append(" AS ").append(new type.CHAR(((type.CHAR)as.cast).length(), true).declare(serialization.vendor)).append("), 10, 18)");
    }
    else {
      serialization.append("CAST(");
      if (as.cast instanceof type.TIME && !(as.dataType instanceof type.TIME))
        serialization.append("'+0 ' || ");

      as.dataType.serialize(serialization);
      serialization.append(" AS ").append(as.cast.declare(serialization.vendor)).append(")");
    }
  }

  @Override
  protected void setParameter(final type.CHAR dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final String value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.length() == 0 || value.charAt(0) == ' ' ? " " + value : value);
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  protected String getParameter(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value != null && value.startsWith(" ") ? value.substring(1) : value;
  }

  @Override
  protected void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setObject(parameterIndex, newINTERVALDS("+0 " + value.format(Dialect.TIME_FORMAT)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  protected LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Object value = resultSet.getObject(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalTime.parse(value.toString().substring(2), Dialect.TIME_FORMAT);
  }

  @Override
  protected void serializeNextSubject(final Subject<?> subject, final int index, final Keyword<?> source, final Serialization serialization) throws IOException {
    if (source instanceof Select.SELECT && (subject instanceof ComparisonPredicate || subject instanceof BooleanTerm)) {
      serialization.append("CASE WHEN ");
      super.serializeNextSubject(subject, index, source, serialization);
      serialization.append(" THEN 1 ELSE 0 END");
    }
    else {
      super.serializeNextSubject(subject, index, source, serialization);
    }

    if (!(source instanceof Select.GROUP_BY) && !(subject instanceof Entity) && subject.wrapper() == null)
      serialization.append(" c" + index);
  }
}