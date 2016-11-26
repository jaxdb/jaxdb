/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.xde;

import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.joda.time.base.BaseSingleFieldPeriod;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.xdl.DBVendor;

public abstract class Evaluation<T> extends Variable<T> {
  private static BaseSingleFieldPeriod[] normalizeIntervals(final BaseSingleFieldPeriod[] intervals) {
    if (intervals.length == 0)
      throw new IllegalArgumentException("intervals.length == 0");

    final Map<Class<? extends BaseSingleFieldPeriod>,Integer> values = new HashMap<Class<? extends BaseSingleFieldPeriod>,Integer>();
    for (final BaseSingleFieldPeriod interval : intervals) {
      final Integer value = values.get(interval.getClass());
      values.put(interval.getClass(), value != null ? value + interval.getValue(0) : interval.getValue(0));
    }

    int i = 0;
    final BaseSingleFieldPeriod[] normalized = new BaseSingleFieldPeriod[values.size()];
    try {
      for (final Map.Entry<Class<? extends BaseSingleFieldPeriod>,Integer> entry : values.entrySet()) {
        final Method method = entry.getKey().getMethod(entry.getKey().getSimpleName().toLowerCase(), int.class);
        normalized[i++] = (BaseSingleFieldPeriod)method.invoke(null, entry.getValue().intValue());
      }
    }
    catch (final ReflectiveOperationException e) {
      throw new RuntimeException(e);
    }

    return normalized;
  }

  private final Variable<T> a;
  private final Operator<Predicate<?>> operator;
  private final Object b;

  protected Evaluation(final Variable<T> a, final Operator<Predicate<?>> operator, final Object b) {
    super(null);
    this.a = a;
    this.operator = operator;
    this.b = b;
  }

  @Override
  protected void serialize(final Serializable caller, final Serialization serialization) {
    a.serialize(this, serialization);
    serialization.sql.append(" ");
    if (b instanceof BaseSingleFieldPeriod[]) {
      final BaseSingleFieldPeriod[] intervals = normalizeIntervals((BaseSingleFieldPeriod[])b);
      if (serialization.vendor == DBVendor.POSTGRE_SQL || serialization.vendor == DBVendor.MY_SQL) {
        serialization.sql.append(operator).append(" INTERVAL");
        for (final BaseSingleFieldPeriod interval : intervals)
          serialization.sql.append(" '").append(interval.getValue(0)).append(" ").append(interval.getClass().getSimpleName().toLowerCase()).append("'");
      }
      else {
        throw new UnsupportedOperationException();
      }
    }
    else {
      serialization.sql.append(operator).append(" ");
      Keyword.format(this, b, serialization);
    }
  }

  @Override
  protected Entity owner() {
    return null;
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
    DataType.set(statement, parameterIndex, (Class<T>)Classes.getGenericSuperclasses(value.getClass())[0], this.value);
  }

  @Override
  @SuppressWarnings("unchecked")
  protected void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
    this.value = DataType.get((Class<T>)Classes.getGenericSuperclasses(a.getClass())[0], resultSet, columnIndex);
  }
}