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
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

// FIXME: Evaluate is broken here
abstract class ComparisonPredicate<V extends Serializable> extends data.BOOLEAN {
  final Subject a;
  final Subject b;

  private abstract static class LogicalPredicate<V extends Serializable> extends ComparisonPredicate<V> {
    LogicalPredicate(final type.Column<?> a, final V b) {
      super(a, b);
    }

    LogicalPredicate(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    LogicalPredicate(final V a, final type.Column<?> b) {
      super(a, b);
    }

    LogicalPredicate(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }
  }

  static class Eq<V extends Serializable> extends LogicalPredicate<V> {
    Eq(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Eq(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Eq(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Eq(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append('=');
    }
  }

  static class Ne<V extends Serializable> extends LogicalPredicate<V> {
    Ne(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Ne(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Ne(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Ne(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append("!=");
    }
  }

  static class Gt<V extends Serializable> extends LogicalPredicate<V> {
    Gt(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Gt(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Gt(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Gt(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append('>');
    }
  }

  static class Lt<V extends Serializable> extends LogicalPredicate<V> {
    Lt(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Lt(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Lt(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Lt(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append('<');
    }
  }

  static class Gte<V extends Serializable> extends LogicalPredicate<V> {
    Gte(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Gte(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Gte(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Gte(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append(">=");
    }
  }

  static class Lte<V extends Serializable> extends LogicalPredicate<V> {
    Lte(final type.Column<?> a, final V b) {
      super(a, b);
    }

    Lte(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
      super(a, b);
    }

    Lte(final V a, final type.Column<?> b) {
      super(a, b);
    }

    Lte(final type.Column<?> a, final type.Column<?> b) {
      super(a, b);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return b.append("<=");
    }
  }

  private ComparisonPredicate(final type.Column<?> a, final V b) {
    super(((Subject)a).getTable());
    this.a = (Subject)a;
    this.b = org.jaxdb.jsql.data.wrap(b);
  }

  private ComparisonPredicate(final type.Column<?> a, final QuantifiedComparisonPredicate<?> b) {
    super(((Subject)a).getTable());
    this.a = (Subject)a;
    this.b = b;
  }

  private ComparisonPredicate(final V a, final type.Column<?> b) {
    super(((Subject)b).getTable());
    this.a = org.jaxdb.jsql.data.wrap(a);
    this.b = (Subject)b;
  }

  private ComparisonPredicate(final type.Column<?> a, final type.Column<?> b) {
    super(((Subject)a).getTable());
    this.a = (Subject)a;
    this.b = (Subject)b;
  }

  ComparisonPredicate(final type.Column<?> column, final Subject a, final Subject b) {
    super(((Subject)column).getTable());
    this.a = a;
    this.b = b;
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    compilation.compiler.compilePredicate(this, compilation);
  }

  @Override
  final void collectColumns(final ArrayList<data.Column<?>> list) {
    list.add(a.getColumn());
    list.add(b.getColumn());
  }
}