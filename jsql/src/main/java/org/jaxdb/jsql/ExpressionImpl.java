/* Copyright (c) 2017 JAX-DB
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// FIXME: The return types need to be examined
final class ExpressionImpl {
  static class TemporalAdd<T extends type.Temporal<V>,D extends data.Temporal<V>,V extends java.time.temporal.Temporal & Serializable> extends TemporalOperation<T,D,V> {
    TemporalAdd(final T a, final Interval b) {
      super(a, b);
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Interval interval = a instanceof type.TIME ? b.toTimeInterval() : a instanceof type.DATE ? b.toDateInterval() : b;
      return interval == null ? ((data.Column<?>)a).compile(compilation, isExpression) : compilation.compiler.compileIntervalAdd(a, b, compilation);
    }

    @Override
    LocalTime evaluate(final LocalTime a) {
      return this.b.toTimeInterval().addTo(a);
    }

    @Override
    LocalDate evaluate(final LocalDate a) {
      return this.b.toDateInterval().addTo(a);
    }

    @Override
    LocalDateTime evaluate(final LocalDateTime a) {
      return this.b.addTo(a);
    }
  }

  static class TemporalSub<T extends type.Temporal<V>,D extends data.Temporal<V>,V extends java.time.temporal.Temporal & Serializable> extends TemporalOperation<T,D,V> {
    TemporalSub(final T a, final Interval b) {
      super(a, b);
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Interval interval = a instanceof type.TIME ? b.toTimeInterval() : a instanceof type.DATE ? b.toDateInterval() : b;
      return interval == null ? ((data.Column<?>)a).compile(compilation, isExpression) : compilation.compiler.compileIntervalSub(a, b, compilation);
    }

    @Override
    LocalTime evaluate(final LocalTime a) {
      return this.b.toTimeInterval().subtractFrom(a);
    }

    @Override
    LocalDate evaluate(final LocalDate a) {
      return this.b.toDateInterval().subtractFrom(a);
    }

    @Override
    LocalDateTime evaluate(final LocalDateTime a) {
      return this.b.subtractFrom(a);
    }
  }

  abstract static class TemporalOperation<T extends type.Temporal<V>,D extends data.Temporal<V>,V extends java.time.temporal.Temporal & Serializable> extends expression.Expression<T,D,V> {
    // FIXME: Hmm.... had to define (b) here to be NOT required to be the same as D... look at OperationImpl
    final T a;
    final Interval b;

    TemporalOperation(final T a, final Interval b) {
      this.a = a;
      this.b = b;
    }

    @Override
    data.Column<?> getColumn() {
      return ((Subject)a).getColumn();
    }

    @Override
    Serializable evaluate(final java.util.Set<Evaluable> visited) {
      if (a == null || b == null)
        return null;

      final java.time.temporal.Temporal temp = (java.time.temporal.Temporal)((Evaluable)a).evaluate(visited);
      if (temp == null)
        return null;

      if (temp instanceof LocalTime)
        return evaluate((LocalTime)temp);

      if (temp instanceof LocalDate)
        return evaluate((LocalDate)temp);

      if (temp instanceof LocalDateTime)
        return evaluate((LocalDateTime)temp);

      throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Temporal type");
    }

    abstract LocalTime evaluate(LocalTime a);
    abstract LocalDate evaluate(LocalDate a);
    abstract LocalDateTime evaluate(LocalDateTime a);
  }

  static final class ChangeCase extends expression.Expression1<function.String1,type.CHAR,data.CHAR,String> implements exp.CHAR {
    ChangeCase(final function.String1 o, final type.CHAR a) {
      super(o, a);
    }

    ChangeCase(final function.String1 o, final String a) {
      super(o, (type.CHAR)data.wrap(a));
    }

    @Override
    data.Column<?> getColumn() {
      return new data.CHAR();
    }

    @Override
    final String evaluate(final java.util.Set<Evaluable> visited) {
      return a == null || !(a instanceof Evaluable) ? null : o.evaluate((String)((Evaluable)a).evaluate(visited));
    }
  }

  static final class Substring extends expression.Expression3<operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer>,type.CHAR,type.INT,type.INT,data.CHAR,String> implements exp.CHAR {
    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final type.CHAR a, final type.INT b, final type.INT c) {
      super(o, a, b, c);
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final type.CHAR a, final type.INT b, final Integer c) {
      super(o, a, b, (type.INT)data.wrap(c));
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final type.CHAR a, final Integer b, final type.INT c) {
      super(o, a, (type.INT)data.wrap(b), c);
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final type.CHAR a, final Integer b, final Integer c) {
      super(o, a, (type.INT)data.wrap(b), (type.INT)data.wrap(c));
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final String a, final type.INT b, final type.INT c) {
      super(o, (type.CHAR)data.wrap(a), b, c);
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final String a, final type.INT b, final Integer c) {
      super(o, (type.CHAR)data.wrap(a), b, (type.INT)data.wrap(c));
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final String a, final Integer b, final type.INT c) {
      super(o, (type.CHAR)data.wrap(a), (type.INT)data.wrap(b), c);
    }

    Substring(final operation.Operation3<java.lang.String,java.lang.Integer,java.lang.Integer> o, final String a, final Integer b, final Integer c) {
      super(o, (type.CHAR)data.wrap(a), (type.INT)data.wrap(b), (type.INT)data.wrap(c));
    }

    @Override
    data.Column<?> getColumn() {
      return new data.CHAR();
    }

    @Override
    final String evaluate(final java.util.Set<Evaluable> visited) {
      return a == null || !(a instanceof Evaluable) ? null : o.evaluate((String)((Evaluable)a).evaluate(visited), (Integer)((Evaluable)b).evaluate(visited), (Integer)((Evaluable)c).evaluate(visited));
    }
  }

  static final class Length extends expression.Expression<type.CHAR,data.CHAR,String> implements exp.CHAR {
    static final operation.Operation1<String,Integer> o = function.String.LENGTH;
    final type.Textual<?> a;

    Length(final type.ENUM<?> a) {
      this.a = a;
    }

    Length(final type.CHAR a) {
      this.a = a;
    }

    Length(final String a) {
      this.a = (type.CHAR)data.wrap(a);
    }

    @Override
    data.Table getTable() {
      return ((Subject)a).getTable();
    }

    @Override
    data.Column<?> getColumn() {
      return new data.CHAR();
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return o.compile(a, compilation);
    }

    @Override
    final Integer evaluate(final java.util.Set<Evaluable> visited) {
      return a == null || !(a instanceof Evaluable) ? null : o.evaluate((String)((Evaluable)a).evaluate(visited));
    }
  }

  static final class Concat extends expression.Expression<type.CHAR,data.CHAR,String> implements exp.CHAR {
    static final operation.Operation o = function.String.CONCAT;
    final type.Textual<?>[] a;
    final data.Table table;

    Concat(final type.Textual<?> a, final type.Textual<?> b) {
      this.a = new type.Textual<?>[] {a, b};
      this.table = expression.getTable(a, b);
    }

    <T extends CharSequence & Serializable>Concat(final type.Textual<?> a, final type.Textual<?> b, final T c) {
      this.a = new type.Textual<?>[] {a, b, data.wrap(c)};
      this.table = expression.getTable(a, b);
    }

    <T extends CharSequence & Serializable>Concat(final type.Textual<?> a, final T b) {
      this.a = new type.Textual<?>[] {a, data.wrap(b)};
      this.table = ((Subject)a).getTable();
    }

    <T extends CharSequence & Serializable>Concat(final type.Textual<?> a, final T b, final type.Textual<?> c) {
      this.a = new type.Textual<?>[] {a, data.wrap(b), c};
      this.table = expression.getTable(a, c);
    }

    <T extends CharSequence & Serializable>Concat(final type.Textual<?> a, final T b, final type.Textual<?> c, final T d) {
      this.a = new type.Textual<?>[] {a, data.wrap(b), c, data.wrap(d)};
      this.table = expression.getTable(a, c);
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b) {
      this.a = new type.Textual<?>[] {data.wrap(a), b};
      this.table = ((Subject)b).getTable();
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b, final type.Textual<?> c) {
      this.a = new type.Textual<?>[] {data.wrap(a), b, c};
      this.table = ((Subject)b).getTable();
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b, final T c) {
      this.a = new type.Textual<?>[] {data.wrap(a), b, data.wrap(c)};
      this.table = ((Subject)b).getTable();
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b, final type.Textual<?> c, final T d) {
      this.a = new type.Textual<?>[] {data.wrap(a), b, c, data.wrap(d)};
      this.table = expression.getTable(b, c);
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b, final T c, final type.Textual<?> d) {
      this.a = new type.Textual<?>[] {data.wrap(a), b, data.wrap(c), d};
      this.table = expression.getTable(b, d);
    }

    <T extends CharSequence & Serializable>Concat(final T a, final type.Textual<?> b, final T c, final type.Textual<?> d, final T e) {
      this.a = new type.Textual<?>[] {data.wrap(a), b, data.wrap(c), d, data.wrap(e)};
      this.table = expression.getTable(b, d);
    }

    @Override
    data.Table getTable() {
      return table;
    }

    @Override
    data.Column<?> getColumn() {
      return new data.CHAR();
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return compilation.compiler.compile(this, compilation);
    }

    @Override
    final String evaluate(final java.util.Set<Evaluable> visited) {
      final StringBuilder builder = new StringBuilder();
      for (final type.Column<?> arg : a) { // [A]
        if (!(arg instanceof Evaluable))
          return null;

        builder.append(((Evaluable)arg).evaluate(visited));
      }

      return builder.toString();
    }
  }

  static final class Count extends expression.Expression<type.BIGINT,data.BIGINT,Long> implements exp.BIGINT {
    final data.Entity a;
    final boolean distinct;

    Count(final data.Entity a, final boolean distinct) {
      this.a = a;
      this.distinct = distinct;
    }

    @Override
    data.Table getTable() {
      return ((Subject)a).getTable();
    }

    @Override
    data.Column<?> getColumn() {
      return new data.BIGINT();
    }

    @Override
    Serializable evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("COUNT(?) cannot be evaluated outside the DB");
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return compilation.compiler.compileCount(a, distinct, compilation);
    }
  }

  static final class Set<T extends type.Column<V>,D extends data.Column<V>,V extends Serializable> extends expression.Expression<T,D,V> {
    final function.Set o;
    final T a;
    final boolean distinct;

    Set(final function.Set o, final T a, final boolean distinct) {
      this.o = o;
      this.a = a;
      this.distinct = distinct;
    }

    @Override
    data.Column<?> getColumn() {
      return ((Subject)a).getColumn();
    }

    @Override
    boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return o.compile(a, distinct, compilation);
    }

    @Override
    Number evaluate(final java.util.Set<Evaluable> visited) {
      throw new UnsupportedOperationException("SetFunction cannot be evaluated outside the DB");
    }
  }

  private ExpressionImpl() {
  }
}