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

package org.safris.xdb.entities;

import java.io.IOException;

import org.safris.xdb.entities.type.Numeric;

final class NumericExpression<T extends Number> extends Expression<T> {
  protected final Operator<NumericExpression<?>> operator;
  protected final Serializable a;
  protected final Serializable b;
  protected final Serializable[] args;

  protected NumericExpression(final Operator<NumericExpression<?>> operator, final Numeric<?> a, final Numeric<?> b, final Numeric<?> ... args) {
    this.operator = operator;
    this.a = a;
    this.b = b;
    this.args = args;
  }

  protected NumericExpression(final Operator<NumericExpression<?>> operator, final Number a, final Numeric<?> b, final Numeric<?> ... args) {
    this.operator = operator;
    this.a = DataType.wrap(a);
    this.b = b;
    this.args = args;
  }

  protected NumericExpression(final Operator<NumericExpression<?>> operator, final Numeric<?> a, final Number b, final Numeric<?> ... args) {
    this.operator = operator;
    this.a = a;
    this.b = DataType.wrap(b);
    this.args = args;
  }

  @Override
  protected void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}