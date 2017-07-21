/* Copyright (c) 2014 lib4j
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
import java.util.Set;

import org.libx4j.rdb.vendor.DBVendor;

final class BooleanTerm extends type.BOOLEAN {
  protected final operator.Boolean operator;
  protected final Condition<?> a;
  protected final Condition<?> b;
  protected final Condition<?>[] conditions;

  @SafeVarargs
  protected BooleanTerm(final operator.Boolean operator, final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    this.operator = operator;
    this.a = a;
    this.b = b;
    this.conditions = conditions;
  }

  @Override
  protected Boolean evaluate(final Set<Evaluable> visited) {
    if (a == null || b == null || a.evaluate(visited) == null || b.evaluate(visited) == null)
      return null;

    for (int i = 0; i < conditions.length; i++)
      if (conditions[i] == null)
        return null;

    for (int i = 0; i < conditions.length; i++) {
      final Object evaluated = conditions[i].evaluate(visited);
      if (evaluated == null)
        return null;

      if (!(evaluated instanceof Boolean))
        throw new RuntimeException("!!!!");

      if (!(Boolean)evaluated)
        return Boolean.FALSE;
    }

    return Boolean.TRUE;
  }

  @Override
  protected final String compile(final DBVendor vendor) {
    return operator.toString();
  }

  @Override
  protected final void compile(final Compilation compilation) throws IOException {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }
}