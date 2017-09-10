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

import org.libx4j.rdb.vendor.DBVendor;

final class ComparisonPredicate<T> extends data.BOOLEAN {
  protected final operator.Logical<?> operator;
  protected final Compilable a;
  protected final Compilable b;

  protected ComparisonPredicate(final operator.Logical<?> operator, final type.DataType<?> a, final T b) {
    this.operator = operator;
    this.a = Compiler.compilable(a);
    this.b = org.libx4j.rdb.jsql.data.DataType.wrap(b);
  }

  protected ComparisonPredicate(final operator.Logical<?> operator, final type.DataType<?> a, final QuantifiedComparisonPredicate<?> b) {
    this.operator = operator;
    this.a = Compiler.compilable(a);
    this.b = b;
  }

  protected ComparisonPredicate(final operator.Logical<?> operator, final T a, final type.DataType<?> b) {
    this.operator = operator;
    this.a = org.libx4j.rdb.jsql.data.DataType.wrap(a);
    this.b = Compiler.compilable(b);
  }

  protected ComparisonPredicate(final operator.Logical<?> operator, final type.DataType<?> a, final type.DataType<?> b) {
    this.operator = operator;
    this.a = Compiler.compilable(a);
    this.b = Compiler.compilable(b);
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