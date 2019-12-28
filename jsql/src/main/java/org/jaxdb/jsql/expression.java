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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

final class expression {
  abstract static class Generic<T> extends type.Subject<T> {
  }

  static final class Count extends Generic<Long> {
    static final Count STAR  = new Count();

    final boolean distinct;
    final kind.DataType<?> column;

    Count(final kind.DataType<?> column, final boolean distinct) {
      this.column = column;
      this.distinct = distinct;
    }

    private Count() {
      this.distinct = false;
      this.column = null;
    }

    @Override
    Object evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("COUNT(?) cannot be evaluated outside the DB");
    }

    @Override
    final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Numeric extends Generic<Number> {
    final operator.Arithmetic operator;
    final kind.Numeric<?> a;
    final kind.Numeric<?> b;

    Numeric(final operator.Arithmetic operator, final kind.Numeric<?> a, final kind.Numeric<?> b) {
      this.operator = operator;
      this.a = a;
      this.b = b;
    }

    Numeric(final operator.Arithmetic operator, final Number a, final kind.Numeric<?> b) {
      this.operator = operator;
      this.a = (type.Numeric<?>)type.DataType.wrap(a);
      this.b = b;
    }

    Numeric(final operator.Arithmetic operator, final kind.Numeric<?> a, final Number b) {
      this.operator = operator;
      this.a = a;
      this.b = (type.Numeric<?>)type.DataType.wrap(b);
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

  static class Temporal extends Generic<java.time.temporal.Temporal> {
    final operator.ArithmeticPlusMinus operator;
    final type.Temporal<?> a;
    final Interval b;

    Temporal(final operator.ArithmeticPlusMinus operator, final type.Temporal<?> a, final Interval b) {
      this.operator = operator;
      this.a = a;
      this.b = b;
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
    final void compile(final Compilation compilation) throws IOException {
      final Interval interval = a instanceof type.TIME ? b.toTimeInterval() : a instanceof type.DATE ? b.toDateInterval() : b;
      if (interval == null)
        Compiler.getCompiler(compilation.vendor).compile(a, compilation);
      else
        Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  abstract static class String extends Generic<java.lang.String> {
  }

  static final class ChangeCase extends String {
    final operator.String1 operator;
    final kind.DataType<?> arg;

    ChangeCase(final operator.String1 operator, final kind.DataType<?> a) {
      this.operator = operator;
      this.arg = a;
    }

    ChangeCase(final operator.String1 operator, final CharSequence a) {
      this.operator = operator;
      this.arg = type.DataType.wrap(a);
    }

    @Override
    final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    final java.lang.String evaluate(final java.util.Set<Evaluable> visited) {
      return arg == null || !(arg instanceof Evaluable) ? null : operator.evaluate((java.lang.String)((Evaluable)arg).evaluate(visited));
    }
  }

  static final class Concat extends String {
    final kind.DataType<?>[] args;

    Concat(final kind.DataType<?> a, final kind.DataType<?> b) {
      this.args = new kind.DataType[] {a, b};
    }

    Concat(final kind.DataType<?> a, final kind.DataType<?> b, final CharSequence c) {
      this.args = new kind.DataType[] {a, b, type.DataType.wrap(c)};
    }

    Concat(final kind.DataType<?> a, final CharSequence b) {
      this.args = new kind.DataType[] {a, type.DataType.wrap(b)};
    }

    Concat(final kind.DataType<?> a, final CharSequence b, final kind.DataType<?> c) {
      this.args = new kind.DataType[] {a, type.DataType.wrap(b), c};
    }

    Concat(final kind.DataType<?> a, final CharSequence b, final kind.DataType<?> c, final CharSequence d) {
      this.args = new kind.DataType[] {a, type.DataType.wrap(b), c, type.DataType.wrap(d)};
    }

    Concat(final CharSequence a, final kind.DataType<?> b) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b};
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final kind.DataType<?> c) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b, c};
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c)};
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final kind.DataType<?> c, final CharSequence d) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b, c, type.DataType.wrap(d)};
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c, final kind.DataType<?> d) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d};
    }

    Concat(final CharSequence a, final kind.DataType<?> b, final CharSequence c, final kind.DataType<?> d, final CharSequence e) {
      this.args = new kind.DataType[] {type.DataType.wrap(a), b, type.DataType.wrap(c), d, type.DataType.wrap(e)};
    }

    @Override
    final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    final java.lang.String evaluate(final java.util.Set<Evaluable> visited) {
      final StringBuilder builder = new StringBuilder();
      for (final kind.DataType<?> arg : args) {
        if (!(arg instanceof Evaluable))
          return null;

        builder.append(((Evaluable)arg).evaluate(visited));
      }

      return builder.toString();
    }
  }

  static final class Set extends Generic<Object> {
    final java.lang.String function;
    final boolean distinct;
    final type.Subject<?> a;

    Set(final java.lang.String function, final type.Subject<?> subject, final boolean distinct) {
      this.function = function;
      this.a = subject;
      this.distinct = distinct;
    }

    @Override
    final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    Object evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("SetFunction cannot be evaluated outside the DB");
    }
  }

  private expression() {
  }
}