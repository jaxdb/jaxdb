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

package org.safris.dbx.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.safris.dbx.ddlx.DBVendor;

final class MySQLSerializer extends Serializer {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  protected void onRegister(final Connection connection) throws SQLException {
  }

  @Override
  protected void serialize(final TemporalExpression expression, final Serialization serialization) throws IOException {
    if (expression.operator == Operator.PLUS)
      serialization.append("DATE_ADD(");
    else if (expression.operator == Operator.MINUS)
      serialization.append("DATE_SUB(");
    else
      throw new UnsupportedOperationException("Supported operators for TemporalExpression are only + and -, and this should have been not allowed via strong type semantics " + expression.operator);

    expression.a.serialize(serialization);
    serialization.append(", ");
    expression.b.serialize(serialization);
    serialization.append(")");
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
      if (unit == Interval.Unit.MILLIS) {
        component = interval.getComponent(unit) * 1000;
        unitString = "MICROSECOND";
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
      else if (unit.name().endsWith("S")) {
        component = interval.getComponent(unit);
        unitString = unit.name().substring(0, unit.name().length() - 1);
      }
      else {
        throw new UnsupportedOperationException("Unexpected Interval.Unit: " + unit.name());
      }

      clause.append(" ").append(component).append(" " + unitString);
    }

    serialization.append("INTERVAL ").append(clause.substring(1));
  }

  @Override
  protected void serialize(final Cast.AS as, final Serialization serialization) throws IOException {
    if (as.castAs instanceof type.Temporal || as.castAs instanceof type.Textual || as.castAs instanceof type.BINARY) {
      super.serialize(as, serialization);
    }
    else if (as.castAs instanceof type.DECIMAL) {
      serialization.append("CAST(");
      as.dataType.serialize(serialization);
      final String declaration = as.castAs.declare(serialization.vendor);
      serialization.append(" AS ").append(as.castAs instanceof type.UNSIGNED ? declaration.substring(0, declaration.indexOf(" UNSIGNED")) : declaration).append(")");
    }
    else if (as.castAs instanceof type.ExactNumeric) {
      serialization.append("CAST(");
      as.dataType.serialize(serialization);
      serialization.append(" AS ").append(as.castAs instanceof type.UNSIGNED ? "UNSIGNED" : "SIGNED").append(" INTEGER)");
    }
    else {
      as.dataType.serialize(serialization);
    }
  }
}