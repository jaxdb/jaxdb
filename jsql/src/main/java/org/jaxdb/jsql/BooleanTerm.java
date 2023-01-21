/* Copyright (c) 2014 JAX-DB
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
import java.sql.SQLException;
import java.util.Set;

import org.jaxdb.vendor.DBVendor;

final class BooleanTerm extends data.BOOLEAN {
  final boolean and;
  final Condition<?> a;
  final Condition<?> b;
  final Condition<?>[] conditions;

  @SafeVarargs
  BooleanTerm(final boolean and, final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    this.and = and;
    this.a = a;
    this.b = b;
    this.conditions = conditions;
  }

  @Override
  final Boolean evaluate(final Set<Evaluable> visited) {
    if (a == null || b == null || a.evaluate(visited) == null || b.evaluate(visited) == null)
      return null;

    for (int i = 0, i$ = conditions.length; i < i$; ++i) // [A]
      if (conditions[i] == null)
        return null;

    if (and) {
      for (int i = 0, i$ = conditions.length; i < i$; ++i) { // [A]
        final Object value = conditions[i].evaluate(visited);
        if (value == null)
          return null;

        if (!(value instanceof Boolean))
          throw new AssertionError();

        if (!(Boolean)value)
          return Boolean.FALSE;
      }

      return Boolean.TRUE;
    }

    for (int i = 0, i$ = conditions.length; i < i$; ++i) { // [A]
      final Object value = conditions[i].evaluate(visited);
      if (value == null)
        return null;

      if (!(value instanceof Boolean))
        throw new AssertionError();

      if ((Boolean)value)
        return Boolean.TRUE;
    }

    return Boolean.FALSE;
  }

  @Override
  final StringBuilder compile(final StringBuilder b, final DBVendor vendor, final boolean isForUpdateWhere) {
    return b.append(toString());
  }

  @Override
  final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    compilation.compiler.compileCondition(this, compilation);
  }

  @Override
  public String toString() {
    return and ? "AND" : "OR";
  }
}