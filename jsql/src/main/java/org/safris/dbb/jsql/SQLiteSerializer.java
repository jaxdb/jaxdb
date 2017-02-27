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

package org.safris.dbb.jsql;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;

import org.safris.commons.io.Readers;
import org.safris.commons.io.Streams;
import org.safris.dbb.vendor.DBVendor;
import org.safris.dbb.vendor.Dialect;

final class SQLiteSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.SQLITE;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
  }

  @Override
  protected void serialize(final Cast.AS as, final Serialization serialization) throws IOException {
    if (as.cast instanceof type.Temporal) {
      serialization.append("STRFTIME(\"");
      if (as.cast instanceof type.DATE) {
        serialization.append("%Y-%m-%d");
      }
      else if (as.cast instanceof type.TIME) {
        serialization.append("%H:%M:");
        serialization.append(((type.TIME)as.cast).precision() > 0 ? "%f" : "%S");
      }
      else if (as.cast instanceof type.DATETIME) {
        serialization.append("%Y-%m-%d %H:%M:");
        serialization.append(((type.DATETIME)as.cast).precision() > 0 ? "%f" : "%S");
      }
      else {
        throw new UnsupportedOperationException("Unexpected type.Temporal type: " + as.cast.getClass());
      }

      serialization.append("\", ");
      as.dataType.serialize(serialization);
      serialization.append(")");
    }
    else {
      super.serialize(as, serialization);
    }
  }

  @Override
  protected void serialize(final TemporalExpression expression, final Serialization serialization) throws IOException {
    if (expression.a instanceof type.DATE)
      serialization.append("DATE(");
    else if (expression.a instanceof type.TIME)
      serialization.append("TIME(");
    else if (expression.a instanceof type.DATETIME)
      serialization.append("DATETIME(");
    else
      throw new UnsupportedOperationException("Unexpected type: " + expression.a.getClass());

    expression.a.serialize(serialization);
    serialization.append(", '").append(expression.operator);
    expression.b.serialize(serialization);
    serialization.append("')");
  }

  @Override
  protected void serialize(final Interval interval, final Serialization serialization) {
    final Set<Interval.Unit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final Interval.Unit unit : units) {
      final Integer component;
      final String unitString;
      if (unit == Interval.Unit.WEEKS) {
        component = interval.getComponent(unit) * 7;
        unitString = "DAYS";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = interval.getComponent(unit) * 3;
        unitString = "MONTHS";
      }
      else if (unit == Interval.Unit.DECADES) {
        component = interval.getComponent(unit) * 10;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = interval.getComponent(unit) * 100;
        unitString = "YEAR";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = interval.getComponent(unit) * 1000;
        unitString = "YEAR";
      }
      else {
        component = interval.getComponent(unit);
        unitString = unit.name().substring(0, unit.name().length() - 1);
      }

      clause.append(" ").append(component).append(" " + unitString);
    }

    serialization.append(clause.substring(1));
  }

  @Override
  protected void serialize(final function.numeric.Mod function, final Serialization serialization) throws IOException {
    serialization.append("(");
    function.a.serialize(serialization);
    serialization.append(" % ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final function.numeric.Ln function, final Serialization serialization) throws IOException {
    serialization.append("LOG(");
    function.a.serialize(serialization);
    serialization.append(") / LOG(2.7182818284590452354)");
  }

  @Override
  protected void serialize(final function.numeric.Log function, final Serialization serialization) throws IOException {
    serialization.append("LOG(");
    function.b.serialize(serialization);
    serialization.append(") / LOG(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  @Override
  protected void serialize(final function.numeric.Log2 function, final Serialization serialization) throws IOException {
    serialization.append("LOG(");
    function.a.serialize(serialization);
    serialization.append(") / LOG(2)");
  }

  @Override
  protected void setParameter(final type.CLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    if (dataType.get() != null)
      statement.setString(parameterIndex, Readers.readFully(dataType.get()));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @Override
  protected Reader getParameter(final type.CLOB clob, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return value == null ? null : new StringReader(value);
  }

  @Override
  protected void setParameter(final type.DATE dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDate value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, Dialect.DATE_FORMAT.format(value));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  protected LocalDate getParameter(final type.DATE date, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,7})?"))
      return LocalDate.parse(value, Dialect.DATETIME_FORMAT);

    return LocalDate.parse(value, Dialect.DATE_FORMAT);
  }

  @Override
  protected void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.format(Dialect.TIME_FORMAT));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  protected LocalTime getParameter(final type.TIME time, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    if (resultSet.wasNull() || value == null)
      return null;

    if (value.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}:\\d{2}(\\.\\d{1,7})?"))
      return LocalTime.parse(value, Dialect.DATETIME_FORMAT);

    return LocalTime.parse(value, Dialect.TIME_FORMAT);
  }

  @Override
  protected void setParameter(final type.DATETIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      statement.setString(parameterIndex, value.format(Dialect.DATETIME_FORMAT));
    else
      statement.setNull(parameterIndex, Types.VARCHAR);
  }

  @Override
  protected LocalDateTime getParameter(final type.DATETIME dateTime, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final String value = resultSet.getString(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.parse(value, Dialect.DATETIME_FORMAT);
  }

  @Override
  protected void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    if (dataType.get() != null)
      statement.setBytes(parameterIndex, Streams.getBytes(dataType.get()));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }
}