/* Copyright (c) 2016 lib4j
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

import org.safris.rdb.jsql.type.Numeric;

abstract class NumericFunction extends Expression<Number> {
  protected final type.DataType<? extends Number> a;
  protected final type.DataType<?> b;

  protected NumericFunction(final type.DataType<? extends Number> a, final Numeric<?> b) {
    this.a = a;
    this.b = b;
  }

  protected NumericFunction(final type.DataType<? extends Number> a, final Number b) {
    this.a = a;
    this.b = type.DataType.wrap(b);
  }

  protected NumericFunction(final Number a, final type.DataType<? extends Number> b) {
    this.a = type.DataType.wrap(a);
    this.b = b;
  }

  protected NumericFunction(final type.DataType<? extends Number> dataType) {
    this(dataType, (Numeric<?>)null);
  }

  protected NumericFunction() {
    this((Numeric<?>)null, (Numeric<?>)null);
  }
}