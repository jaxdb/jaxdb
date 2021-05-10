/* Copyright (c) 2015 JAX-DB
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import org.jaxdb.jsql.type.Table;

final class expression {
  abstract static class OneArg<T,A> extends type.Entity<T> {
    protected final A a;

    OneArg(final A a) {
      this.a = Objects.requireNonNull(a);
    }

    @Override
    final Table table() {
      return ((Subject)a).table();
    }
  }

  abstract static class TwoArg<T,A,B> extends OneArg<T,A> {
    protected final B b;

    TwoArg(final A a, final B b) {
      super(a);
      this.b = b;
    }
  }

  static final class Count extends OneArg<Long,type.Entity<?>> {
    final boolean distinct;

    Count(final type.Entity<?> column, final boolean distinct) {
      super(column);
      this.distinct = distinct;
    }

    @Override
    Object evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("COUNT(?) cannot be evaluated outside the DB");
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Numeric extends TwoArg<Number,kind.Numeric<?>,kind.Numeric<?>> {
    final operator.Arithmetic operator;

    Numeric(final operator.Arithmetic operator, final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
      this.operator = operator;
    }

    Numeric(final operator.Arithmetic operator, final Number a, final kind.Numeric<?> b) {
      super((type.Numeric<?>)type.DataType.wrap(a), b);
      this.operator = operator;
    }

    Numeric(final operator.Arithmetic operator, final kind.Numeric<?> a, final Number b) {
      super(a, (type.Numeric<?>)type.DataType.wrap(b));
      this.operator = operator;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation);
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

      return operator.evaluate(a, b);
    }
  }

  static class Temporal extends TwoArg<java.time.temporal.Temporal,type.Temporal<?>,Interval> {
    final operator.ArithmeticPlusMinus operator;

    Temporal(final operator.ArithmeticPlusMinus operator, final type.Temporal<?> a, final Interval b) {
      super(a, b);
      this.operator = operator;
    }

    @Override
    java.time.temporal.Temporal evaluate(final java.util.Set<Evaluable> visited) {
      if (a == null || b == null)
        return null;

      final java.time.temporal.Temporal temp = a.evaluate(visited);
      if (temp == null)
        return null;

      if (temp instanceof LocalDateTime)
        return operator.evaluate((LocalDateTime)temp, b);

      if (temp instanceof LocalDate)
        return operator.evaluate((LocalDate)temp, b);

      if (temp instanceof LocalTime)
        return operator.evaluate((LocalTime)temp, b);

      throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Temporal type");
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Interval interval = a instanceof type.TIME ? b.toTimeInterval() : a instanceof type.DATE ? b.toDateInterval() : b;
      if (interval == null)
        Compiler.compile(a, compilation, isExpression);
      else
        compilation.compiler.compile(this, compilation);
    }
  }

  abstract static class String<A> extends OneArg<java.lang.String,A> {
    String(final A a) {
      super(a);
    }
  }

  static final class ChangeCase extends String<kind.DataType<?>> {
    final operator.String1 operator;

    ChangeCase(final operator.String1 operator, final kind.DataType<?> a) {
      super(a);
      this.operator = operator;
    }

    ChangeCase(final operator.String1 operator, final CharSequence a) {
      super(type.DataType.wrap(a));
      this.operator = operator;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation);
    }

    @Override
    final java.lang.String evaluate(final java.util.Set<Evaluable> visited) {
      return a == null || !(a instanceof Evaluable) ? null : operator.evaluate((java.lang.String)((Evaluable)a).evaluate(visited));
    }
  }

  static final class Concat extends String<kind.DataType<?>[]> {
    Concat(final kind.DataType<?> a, final kind.DataType<?> b) {
      super(new kind.DataType[] {a, b});
    }

    Concat(final kind.DataType<?> a, final kind.DataType<?> b, final CharSequence c) {
      super(new kind.DataType[] {a, b, type.DataType.wrap(c)});
    }

    Concat(final kind.DataType<?> a, final CharSequence b) {
      super(new kind.DataType[] {a, type.DataType.wrap(b)});
    }

    Concat(final kind.DataType<?> a, final CharSequence b, final kind.DataType<?> c) {
      super(new kind.DataType[] {a, type.DataType.wrap(b), c});
    }

    Concat(final kind.DataType<?> a, final CharSequence b, final kind.DataType<?> c, final CharSequence d) {
      super(new kind.DataType[] {a, type.DataType.wrap(b), c, type.DataType.wrap(d)});
    }

    Concat(final CharSequence a, final kind.DataType<?> b) {
      super(new kind.DataType[] {type.DataType.wrap(a), b});
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final kind.DataType<?> c) {
      super(new kind.DataType[] {type.DataType.wrap(a), b, c});
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c) {
      super(new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c)});
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final kind.DataType<?> c, final CharSequence d) {
      super(new kind.DataType[] {type.DataType.wrap(a), b, c, type.DataType.wrap(d)});
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c, final kind.DataType<?> d) {
      super(new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d});
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c, final kind.DataType<?> d, final CharSequence e) {
      super(new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d, type.DataType.wrap(e)});
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation);
    }

    @Override
    final java.lang.String evaluate(final java.util.Set<Evaluable> visited) {
      final StringBuilder builder = new StringBuilder();
      for (final kind.DataType<?> arg : a) {
        if (!(arg instanceof Evaluable))
          return null;

        builder.append(((Evaluable)arg).evaluate(visited));
      }

      return builder.toString();
    }
  }

  static final class Set extends OneArg<Object,type.Entity<?>> {
    final java.lang.String function;
    final boolean distinct;

    Set(final java.lang.String function, final type.Entity<?> entity, final boolean distinct) {
      super(entity);
      this.function = function;
      this.distinct = distinct;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation);
    }

    @Override
    Object evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("SetFunction cannot be evaluated outside the DB");
    }
  }

  private expression() {
  }
}