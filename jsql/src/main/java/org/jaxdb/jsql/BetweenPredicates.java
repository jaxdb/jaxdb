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
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.ArrayList;
import java.util.Set;

import org.libj.lang.Numbers;
import org.libj.util.Temporals;

final class BetweenPredicates {
  abstract static class BetweenPredicate extends Predicate {
    final boolean positive;

    BetweenPredicate(final type.Column<?> column, final boolean positive) {
      super(column);
      this.positive = positive;
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return compilation.compiler.compileBetweenPredicate(this, compilation);
    }

    abstract Subject a();
    abstract Subject b();

    @Override
    void collectColumns(final ArrayList<data.Column<?>> list) {
      list.add(a().getColumn());
      list.add(b().getColumn());
    }
  }

  static final class NumericBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final Number a, final type.Numeric<?> b, final boolean positive) {
      this(column, data.wrap(a), b, positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final Number b, final boolean positive) {
      this(column, a, data.wrap(b), positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final Number a, final Number b, final boolean positive) {
      this(column, data.wrap(a), data.wrap(b), positive);
    }

    NumericBetweenPredicate(final Number value, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      this(data.wrap(value), a, b, positive);
    }

    NumericBetweenPredicate(final Number value, final Number a, final type.Numeric<?> b, final boolean positive) {
      this(data.wrap(value), data.wrap(a), b, positive);
    }

    NumericBetweenPredicate(final Number value, final type.Numeric<?> a, final Number b, final boolean positive) {
      this(data.wrap(value), a,  data.wrap(b), positive);
    }

    NumericBetweenPredicate(final Number value, final Number a, final Number b, final boolean positive) {
      this(data.wrap(value), data.wrap(a),  data.wrap(b), positive);
    }

    @Override
    Subject a() {
      return a;
    }

    @Override
    Subject b() {
      return b;
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (column == null || a == null || b == null || !(column instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Number a = (Number)((Evaluable)this.a).evaluate(visited);
      final Number b = (Number)((Evaluable)this.b).evaluate(visited);
      final Number c = (Number)((Evaluable)this.column).evaluate(visited);
      return Numbers.compare(a, c) >= 0 && Numbers.compare(c, b) <= 0 == positive;
    }
  }

  static final class TemporalBetweenPredicate extends BetweenPredicate {
    private final Evaluable a;
    private final Evaluable b;

    TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Evaluable)a;
      this.b = (Evaluable)b;
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final type.Temporal<?> column, final T a, final type.Temporal<?> b, final boolean positive) {
      this(column, (type.Temporal<?>)data.wrap(a), b, positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final T b, final boolean positive) {
      this(column, a, (type.Temporal<?>)data.wrap(b), positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final type.Temporal<?> column, final T a, final T b, final boolean positive) {
      this(column, (type.Temporal<?>)data.wrap(a), (type.Temporal<?>)data.wrap(b), positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final T value, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      this((type.Temporal<?>)data.wrap(value), a, b, positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final T value, final T a, final type.Temporal<?> b, final boolean positive) {
      this((type.Temporal<?>)data.wrap(value), (type.Temporal<?>)data.wrap(a), b, positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final T value, final type.Temporal<?> a, final T b, final boolean positive) {
      this((type.Temporal<?>)data.wrap(value), a,  (type.Temporal<?>)data.wrap(b), positive);
    }

    <T extends java.time.temporal.Temporal & Serializable>TemporalBetweenPredicate(final T value, final T a, final T b, final boolean positive) {
      this((type.Temporal<?>)data.wrap(value), (type.Temporal<?>)data.wrap(a),  (type.Temporal<?>)data.wrap(b), positive);
    }

    @Override
    Subject a() {
      return a;
    }

    @Override
    Subject b() {
      return b;
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (column == null || a == null || b == null || !(column instanceof Evaluable))
        return null;

      final Temporal a = (Temporal)this.a.evaluate(visited);
      final Temporal b = (Temporal)this.b.evaluate(visited);
      final Temporal c = (Temporal)((Evaluable)this.column).evaluate(visited);
      return Temporals.compare(a, c) <= 0 && Temporals.compare(c, b) >= 0 == positive;
    }
  }

  static final class TimeBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final type.TIME b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    TimeBetweenPredicate(final type.TIME column, final LocalTime a, final type.TIME b, final boolean positive) {
      this(column, data.wrap(a), b, positive);
    }

    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final LocalTime b, final boolean positive) {
      this(column, a, data.wrap(b), positive);
    }

    TimeBetweenPredicate(final type.TIME column, final LocalTime a, final LocalTime b, final boolean positive) {
      this(column, data.wrap(a), data.wrap(b), positive);
    }

    TimeBetweenPredicate(final LocalTime value, final type.TIME a, final type.TIME b, final boolean positive) {
      this(data.wrap(value), a, b, positive);
    }

    TimeBetweenPredicate(final LocalTime value, final LocalTime a, final type.TIME b, final boolean positive) {
      this(data.wrap(value), data.wrap(a), b, positive);
    }

    TimeBetweenPredicate(final LocalTime value, final type.TIME a, final LocalTime b, final boolean positive) {
      this(data.wrap(value), a,  data.wrap(b), positive);
    }

    TimeBetweenPredicate(final LocalTime value, final LocalTime a, final LocalTime b, final boolean positive) {
      this(data.wrap(value), data.wrap(a),  data.wrap(b), positive);
    }

    @Override
    Subject a() {
      return a;
    }

    @Override
    Subject b() {
      return b;
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (column == null || a == null || b == null || !(column instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Temporal a = (Temporal)((Evaluable)this.a).evaluate(visited);
      final Temporal b = (Temporal)((Evaluable)this.b).evaluate(visited);
      final Temporal c = (Temporal)((Evaluable)this.column).evaluate(visited);
      return Temporals.compare(a, c) >= 0 && Temporals.compare(c, b) <= 0 == positive;
    }
  }

  static final class TextualBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final type.Textual<?> column, final T a, final type.Textual<?> b, final boolean positive) {
      this(column, (type.Textual<?>)data.wrap(a), b, positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final T b, final boolean positive) {
      this(column, a, (type.Textual<?>)data.wrap(b), positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final type.Textual<?> column, final T a, final T b, final boolean positive) {
      this(column, (type.Textual<?>)data.wrap(a), (type.Textual<?>)data.wrap(b), positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final T value, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      this((type.Textual<?>)data.wrap(value), a, b, positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final T value, final T a, final type.Textual<?> b, final boolean positive) {
      this((type.Textual<?>)data.wrap(value), (type.Textual<?>)data.wrap(a), b, positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final T value, final type.Textual<?> a, final T b, final boolean positive) {
      this((type.Textual<?>)data.wrap(value), a,  (type.Textual<?>)data.wrap(b), positive);
    }

    <T extends CharSequence & Serializable>TextualBetweenPredicate(final T value, final T a, final T b, final boolean positive) {
      this((type.Textual<?>)data.wrap(value), (type.Textual<?>)data.wrap(a),  (type.Textual<?>)data.wrap(b), positive);
    }

    @Override
    Subject a() {
      return a;
    }

    @Override
    Subject b() {
      return b;
    }

    @Override
    Boolean evaluate(final Set<Evaluable> visited) {
      if (column == null || a == null || b == null || !(column instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final String a = (String)((Evaluable)this.a).evaluate(visited);
      final String b = (String)((Evaluable)this.b).evaluate(visited);
      final String c = (String)((Evaluable)this.column).evaluate(visited);
      return a.compareTo(c) >= 0 && c.compareTo(b) <= 0  == positive;
    }
  }

  private BetweenPredicates() {
  }
}