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
import java.io.Reader;
import java.io.StringReader;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;

import org.safris.commons.io.Readers;
import org.safris.commons.io.Streams;
import org.safris.rdb.jsql.type.BLOB;
import org.safris.rdb.jsql.type.ENUM;
import org.safris.rdb.vendor.DBVendor;
import org.safris.rdb.vendor.Dialect;

final class PostgreSQLSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      final StringBuilder modulus = new StringBuilder("CREATE OR REPLACE FUNCTION MODULUS(dividend double precision, divisor double precision) RETURNS numeric AS $$");
      modulus.append("DECLARE");
      modulus.append("  factor double precision;");
      modulus.append("  result double precision;");
      modulus.append("BEGIN");
      modulus.append("  factor := dividend / divisor;");
      modulus.append("  IF factor < 0 THEN");
      modulus.append("    factor := CEIL(factor);");
      modulus.append("  ELSE");
      modulus.append("    factor := FLOOR(factor);");
      modulus.append("  END IF;");
      modulus.append("  RETURN dividend - divisor * factor;");
      modulus.append("END;");
      modulus.append("$$ LANGUAGE plpgsql;");
      statement.execute(modulus.toString());

      final StringBuilder log2 = new StringBuilder("CREATE OR REPLACE FUNCTION LOG2(num numeric) RETURNS numeric AS $$");
      log2.append("DECLARE");
      log2.append("  result double precision;");
      log2.append("BEGIN");
      log2.append("  RETURN logger.info(2, num);");
      log2.append("END;");
      log2.append("$$ LANGUAGE plpgsql;");
      statement.execute(log2.toString());

      final StringBuilder log10 = new StringBuilder("CREATE OR REPLACE FUNCTION LOG10(num numeric) RETURNS numeric AS $$");
      log10.append("DECLARE");
      log10.append("  result double precision;");
      log10.append("BEGIN");
      log10.append("  RETURN logger.info(10, num);");
      log10.append("END;");
      log10.append("$$ LANGUAGE plpgsql;");
      statement.execute(log10.toString());
    }
    catch (final SQLException e) {
      if (!"X0Y68".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  protected void serialize(final Case.Simple.CASE<?,?> case_, final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    serialization.append("CASE ");
    if (case_.variable instanceof type.ENUM && _else instanceof Case.CHAR.ELSE)
      toChar((type.ENUM<?>)case_.variable, serialization);
    else
      case_.variable.serialize(serialization);
  }

  @Override
  protected void serialize(final Case.WHEN<?> when, final Case.THEN<?,?> then, final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    final Class<?> conditionClass = when.condition instanceof Predicate ? ((Predicate<?>)when.condition).dataType.getClass() : when.condition.getClass();
    if ((when.condition instanceof type.ENUM || then.value instanceof type.ENUM) && (conditionClass != then.value.getClass() || _else instanceof Case.CHAR.ELSE)) {
      serialization.append(" WHEN ");
      if (when.condition instanceof type.ENUM)
        toChar((type.ENUM<?>)when.condition, serialization);
      else
        when.condition.serialize(serialization);

      serialization.append(" THEN ");
      if (then.value instanceof type.ENUM)
        toChar((type.ENUM<?>)then.value, serialization);
      else
        then.value.serialize(serialization);
    }
    else {
      super.serialize(when, then, _else, serialization);
    }
  }

  @Override
  protected void serialize(final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    serialization.append(" ELSE ");
    if (_else instanceof Case.CHAR.ELSE && _else.value instanceof type.ENUM)
      toChar((type.ENUM<?>)_else.value, serialization);
    else
      _else.value.serialize(serialization);
    serialization.append(" END");
  }

  @Override
  protected void serialize(final StringExpression serializable, final Serialization serialization) throws IOException {
    if (serializable.operator != Operator.CONCAT)
      throw new UnsupportedOperationException("The only supported operator for StringExpression is: ||");

    serialization.append("CONCAT(");
    for (int i = 0; i < serializable.args.length; i++) {
      final Serializable arg = serializable.args[i];
      if (i > 0)
        serialization.append(", ");

      arg.serialize(serialization);
    }
    serialization.append(")");
  }

  @Override
  protected void serialize(final Interval interval, final Serialization serialization) {
    final Set<Interval.Unit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final Interval.Unit unit : units) {
      final Integer component;
      final String unitString;
      if (unit == Interval.Unit.MICROS) {
        component = interval.getComponent(unit);
        unitString = "MICROSECOND";
      }
      else if (unit == Interval.Unit.MILLIS) {
        component = interval.getComponent(unit);
        unitString = "MILLISECOND";
      }
      else if (unit == Interval.Unit.QUARTERS) {
        component = interval.getComponent(unit) * 3;
        unitString = "MONTH";
      }
      else if (unit == Interval.Unit.CENTURIES) {
        component = interval.getComponent(unit) * 100;
        unitString = "YEARS";
      }
      else if (unit == Interval.Unit.MILLENNIA) {
        component = interval.getComponent(unit) * 1000;
        unitString = "YEARS";
      }
      else {
        component = interval.getComponent(unit);
        unitString = unit.name().substring(0, unit.name().length() - 1);
      }

      clause.append(" ").append(component).append(" " + unitString);
    }

    serialization.append("INTERVAL '").append(clause.substring(1)).append("'");
  }

  @Override
  protected String getPreparedStatementMark(final type.DataType<?> dataType) {
    if (dataType instanceof ENUM) {
      final EntityEnum entityEnum = (EntityEnum)dataType.get();
      return "?::" + Dialect.getTypeName(entityEnum.table(), entityEnum.column());
    }

    return "?";
  }

  @Override
  protected String serialize(final BLOB dataType) throws IOException {
    if (dataType.get() == null)
      return "NULL";

    final BigInteger integer = new BigInteger(Streams.readBytes(dataType.get()));
    return "E'\\" + integer.toString(8); // FIXME: This is only half done
  }

  private static void toChar(final type.ENUM<?> dataType, final Serialization serialization) throws IOException {
    serialization.append("CAST(");
    dataType.serialize(serialization);
    serialization.append(" AS CHAR(").append(dataType.length()).append("))");
  }

  @Override
  protected final void serialize(final ComparisonPredicate<?> predicate, final Serialization serialization) throws IOException {
    if (predicate.a.getClass() == predicate.b.getClass() || (!(predicate.a instanceof type.ENUM) && !(predicate.b instanceof type.ENUM))) {
      super.serialize(predicate, serialization);
    }
    else {
      if (predicate.a instanceof type.ENUM)
        toChar((type.ENUM<?>)predicate.a, serialization);
      else
        predicate.a.serialize(serialization);

      serialization.append(" ").append(predicate.operator).append(" ");
      if (predicate.b instanceof type.ENUM)
        toChar((type.ENUM<?>)predicate.b, serialization);
      else
        predicate.b.serialize(serialization);
    }
  }

  @Override
  protected void serialize(final function.numeric.Mod function, final Serialization serialization) throws IOException {
    serialization.append("MODULUS(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
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
  protected void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    if (dataType.get() != null)
      statement.setBytes(parameterIndex, Streams.readBytes(dataType.get()));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }
}