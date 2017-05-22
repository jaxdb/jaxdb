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

package org.safris.rdb.jsql;

import java.io.IOException;

import org.safris.rdb.jsql.model.select;
import org.safris.rdb.vendor.DBVendor;

final class ComparisonPredicate<T> extends type.BOOLEAN {
  protected final Operator<ComparisonPredicate<?>> operator;
  protected final Serializable a;
  protected final Serializable b;

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final select.SELECT<?> a, final type.DataType<?> b) {
    this.operator = operator;
    this.a = (Serializable)a;
    this.b = b;
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final select.SELECT<?> a, final T b) {
    this.operator = operator;
    this.a = (Serializable)a;
    this.b = type.DataType.wrap(b);
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final T a, final select.SELECT<?> b) {
    this.operator = operator;
    this.a = type.DataType.wrap(a);
    this.b = (Serializable)b;
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final type.DataType<?> a, final select.SELECT<?> b) {
    this.operator = operator;
    this.a = a;
    this.b = (Serializable)b;
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final type.DataType<?> a, final T b) {
    this.operator = operator;
    this.a = a;
    this.b = type.DataType.wrap(b);
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final type.DataType<?> a, final QuantifiedComparisonPredicate<?> b) {
    this.operator = operator;
    this.a = a;
    this.b = b;
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final T a, final type.DataType<?> b) {
    this.operator = operator;
    this.a = type.DataType.wrap(a);
    this.b = b;
  }

  protected ComparisonPredicate(final Operator<ComparisonPredicate<?>> operator, final type.DataType<?> a, final type.DataType<?> b) {
    this.operator = operator;
    this.a = a;
    this.b = b;
  }

  @Override
  protected final String serialize(final DBVendor vendor) {
    return operator.toString();
  }

  @Override
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}