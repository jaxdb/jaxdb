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
import java.util.ArrayList;
import java.util.Set;
import org.libj.util.Interval;

abstract class BooleanTerm extends data.BOOLEAN {
  final Condition<?> a;
  final Condition<?> b;
  final Condition<?>[] conditions;

  static final class And extends BooleanTerm {
    @SafeVarargs
    And(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
      super(a, b, conditions);
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (a == null || b == null || a.evaluate(visited) == null || b.evaluate(visited) == null)
        return null;

      for (int i = 0, i$ = conditions.length; i < i$; ++i) // [A]
        if (conditions[i] == null)
          return null;

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

    @Override
    public String toString() {
      return "AND";
    }
  }

  static final class Or extends BooleanTerm {
    @SafeVarargs
    Or(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
      super(a, b, conditions);
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (a == null || b == null || a.evaluate(visited) == null || b.evaluate(visited) == null)
        return null;

      for (int i = 0, i$ = conditions.length; i < i$; ++i) // [A]
        if (conditions[i] == null)
          return null;

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
    public String toString() {
      return "OR";
    }
  }

  @SafeVarargs
  private BooleanTerm(final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    super(a.getTable());
    this.a = a;
    this.b = b;
    this.conditions = conditions;
  }

  @Override
  final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
    return b.append(toString());
  }

  @Override
  final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    return compilation.compiler.compileCondition(this, compilation);
  }

  @Override
  void collectColumns(final ArrayList<data.Column<?>> list) {
    a.collectColumns(list);
    b.collectColumns(list);
    for (int i = 0, i$ = conditions.length; i < i$; ++i) // [A]
      conditions[i].collectColumns(list);
  }
}