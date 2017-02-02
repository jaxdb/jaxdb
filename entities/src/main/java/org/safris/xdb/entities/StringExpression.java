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

final class StringExpression extends Expression<String> {
  protected final Operator<StringExpression> operator;
  protected final Serializable[] args;

  protected StringExpression(final Operator<StringExpression> operator, final type.DataType<?> a, final type.DataType<?> b) {
    this.operator = operator;
    this.args = new Serializable[] {a, b};
  }

  protected StringExpression(final Operator<StringExpression> operator, final type.DataType<?> a, final type.DataType<?> b, final CharSequence c) {
    this.operator = operator;
    this.args = new Serializable[] {a, b, type.DataType.wrap(c)};
  }

  protected StringExpression(final Operator<StringExpression> operator, final type.DataType<?> a, final CharSequence b) {
    this.operator = operator;
    this.args = new Serializable[] {a, type.DataType.wrap(b)};
  }

  protected StringExpression(final Operator<StringExpression> operator, final type.DataType<?> a, final CharSequence b, final type.DataType<?> c) {
    this.operator = operator;
    this.args = new Serializable[] {a, type.DataType.wrap(b), c};
  }

  protected StringExpression(final Operator<StringExpression> operator, final type.DataType<?> a, final CharSequence b, final type.DataType<?> c, final CharSequence d) {
    this.operator = operator;
    this.args = new Serializable[] {a, type.DataType.wrap(b), c, type.DataType.wrap(d)};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b, final type.DataType<?> c) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b, c};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b, final CharSequence c) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b, type.DataType.wrap(c)};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b, final type.DataType<?> c, final CharSequence d) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b, c, type.DataType.wrap(d)};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b, final CharSequence c, final type.DataType<?> d) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d};
  }

  protected StringExpression(final Operator<StringExpression> operator, final CharSequence a, final type.DataType<?> b, final CharSequence c, final type.DataType<?> d, final CharSequence e) {
    this.operator = operator;
    this.args = new Serializable[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d, type.DataType.wrap(e)};
  }

  @Override
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}