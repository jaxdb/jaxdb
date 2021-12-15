/* Copyright (c) 2021 JAX-DB
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

import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.data.Table;

final class expression {
  static data.Table<?> getTable(final type.Entity<?> a, final type.Entity<?> b) {
    final data.Table<?> table = ((Subject)a).getTable();
    return table != null ? table : ((Subject)b).getTable();
  }

  static data.Column<?> getColumn(final type.Entity<?> a, final type.Entity<?> b) {
    final data.Column<?> table = ((Subject)a).getColumn();
    return table != null ? table : ((Subject)b).getColumn();
  }

  abstract static class Expression<T extends type.Column<V>,D extends data.Column<V>,V> extends Evaluable implements exp.Expression<T,D,V> {
    @Override
    public D AS(final D column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    @Override
    Table<?> getTable() {
      return null;
    }
  }

  abstract static class Expression1<O extends operation.Operation1<? super V>,T extends type.Column<V>,D extends data.Column<V>,V> extends Expression<T,D,V> {
    final O o;
    final T a;

    Expression1(final O o, final T a) {
      this.o = o;
      this.a = a;
    }

    @Override
    data.Table<?> getTable() {
      return ((Subject)a).getTable();
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      o.compile(a, compilation);
    }
  }

  abstract static class NumericExpression1<O extends operation.Operation1<? super V>,T extends type.Numeric<V>,D extends data.Numeric<V>,V extends Number> extends Expression1<O,T,D,V> {
    NumericExpression1(final O o, final type.Numeric<?> a) {
      super(o, (T)a);
    }

    @Override
    data.Table<?> getTable() {
      return ((Subject)a).getTable();
    }

    @Override
    Column<?> getColumn() {
      return ((Subject)a).getColumn();
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      o.compile(a, compilation);
    }

    @Override
    final Number evaluate(final java.util.Set<Evaluable> visited) {
      return a instanceof Evaluable ? (Number)o.evaluate((V)((Evaluable)a).evaluate(visited)) : null;
    }
  }

  abstract static class NumericFunction0 extends Evaluable implements exp.Expression<type.Numeric<Number>,data.Numeric<Number>,Number> {
    @Override
    public data.Numeric<Number> AS(final data.Numeric<Number> column) {
      // FIXME: Implement this...
      return null;
    }

    @Override
    final Table<?> getTable() {
      // FIXME: Implement this...
      return null;
    }
  }

  // FIXME: .. do we need these?...
  abstract static class NumericFunction1 extends NumericFunction0 {
    final type.Numeric<?> a;

    NumericFunction1(final type.Numeric<?> a) {
      this.a = a;
    }

    NumericFunction1(final Number a) {
      this.a = (type.Numeric<?>)data.wrap(a);
    }

    @Override
    Number evaluate(final java.util.Set<Evaluable> visited) {
      return this.a instanceof Evaluable ? evaluate((Number)((Evaluable)this.a).evaluate(visited)) : null;
    }

    abstract Number evaluate(Number a);
  }

  abstract static class NumericFunction2 extends NumericFunction0 {
    final type.Numeric<?> a;
    final type.Numeric<?> b;

    NumericFunction2(final type.Numeric<?> a, final type.Numeric<?> b) {
      this.a = a;
      this.b = b;
    }

    NumericFunction2(final Number a, final type.Numeric<?> b) {
      this.a = (type.Numeric<?>)data.wrap(a);
      this.b = b;
    }

    NumericFunction2(final type.Numeric<?> a, final Number b) {
      this.a = a;
      this.b = (type.Numeric<?>)data.wrap(b);
    }

    @Override
    Number evaluate(final java.util.Set<Evaluable> visited) {
      if (!(this.a instanceof Evaluable) || !(this.b instanceof Evaluable))
        return null;

      final Number a = (Number)((Evaluable)this.a).evaluate(visited);
      if (a == null)
        return null;

      final Number b = (Number)((Evaluable)this.b).evaluate(visited);
      if (b == null)
        return null;

      return evaluate(a, b);
    }

    abstract Number evaluate(Number a, Number b);
  }

  abstract static class Temporal extends data.Entity<java.time.temporal.Temporal> {
    // FIXME: Rename this... or redo this weak pattern
    final String function;

    Temporal(final String function) {
      super(false);
      this.function = function;
    }

    @Override
    final Table<?> getTable() {
      return null;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compileTemporal(this, compilation);
    }
  }

  private expression() {
  }
}