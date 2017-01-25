/* Copyright (c) 2014 Seva Safris
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

final class BooleanTerm<T> extends BooleanCondition<T> {
  protected final Operator<BooleanTerm<?>> operator;
  protected final Condition<?> a;
  protected final Condition<?> b;
  protected final Condition<?>[] conditions;

  @SafeVarargs
  protected BooleanTerm(final Operator<BooleanTerm<?>> operator, final Condition<?> a, final Condition<?> b, final Condition<?> ... conditions) {
    this.a = a;
    this.b = b;
    this.operator = operator;
    this.conditions = conditions;
  }

  @Override
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}