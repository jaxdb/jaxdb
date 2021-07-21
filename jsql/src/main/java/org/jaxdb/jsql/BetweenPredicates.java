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
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.temporal.Temporal;
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
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compileBetweenPredicate(this, compilation);
    }

    abstract Subject a();
    abstract Subject b();
  }

  final static class NumericBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final Number a, final type.Numeric<?> b, final boolean positive) {
      this(column, data.Column.wrap(a), b, positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final Number b, final boolean positive) {
      this(column, a, data.Column.wrap(b), positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final Number a, final Number b, final boolean positive) {
      this(column, data.Column.wrap(a), data.Column.wrap(b), positive);
    }

    NumericBetweenPredicate(final Number value, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      this(data.Column.wrap(value), a, b, positive);
    }

    NumericBetweenPredicate(final Number value, final Number a, final type.Numeric<?> b, final boolean positive) {
      this(data.Column.wrap(value), data.Column.wrap(a), b, positive);
    }

    NumericBetweenPredicate(final Number value, final type.Numeric<?> a, final Number b, final boolean positive) {
      this(data.Column.wrap(value), a,  data.Column.wrap(b), positive);
    }

    NumericBetweenPredicate(final Number value, final Number a, final Number b, final boolean positive) {
      this(data.Column.wrap(value), data.Column.wrap(a),  data.Column.wrap(b), positive);
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

  final static class TemporalBetweenPredicate extends BetweenPredicate {
    private final Evaluable a;
    private final Evaluable b;

    TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Evaluable)a;
      this.b = (Evaluable)b;
    }

    TemporalBetweenPredicate(final type.Temporal<?> column, final java.time.temporal.Temporal a, final type.Temporal<?> b, final boolean positive) {
      this(column, (type.Temporal<?>)data.Column.wrap(a), b, positive);
    }

    TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final java.time.temporal.Temporal b, final boolean positive) {
      this(column, a, (type.Temporal<?>)data.Column.wrap(b), positive);
    }

    TemporalBetweenPredicate(final type.Temporal<?> column, final java.time.temporal.Temporal a, final java.time.temporal.Temporal b, final boolean positive) {
      this(column, (type.Temporal<?>)data.Column.wrap(a), (type.Temporal<?>)data.Column.wrap(b), positive);
    }

    TemporalBetweenPredicate(final java.time.temporal.Temporal value, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      this((type.Temporal<?>)data.Column.wrap(value), a, b, positive);
    }

    TemporalBetweenPredicate(final java.time.temporal.Temporal value, final java.time.temporal.Temporal a, final type.Temporal<?> b, final boolean positive) {
      this((type.Temporal<?>)data.Column.wrap(value), (type.Temporal<?>)data.Column.wrap(a), b, positive);
    }

    TemporalBetweenPredicate(final java.time.temporal.Temporal value, final type.Temporal<?> a, final java.time.temporal.Temporal b, final boolean positive) {
      this((type.Temporal<?>)data.Column.wrap(value), a,  (type.Temporal<?>)data.Column.wrap(b), positive);
    }

    TemporalBetweenPredicate(final java.time.temporal.Temporal value, final java.time.temporal.Temporal a, final java.time.temporal.Temporal b, final boolean positive) {
      this((type.Temporal<?>)data.Column.wrap(value), (type.Temporal<?>)data.Column.wrap(a),  (type.Temporal<?>)data.Column.wrap(b), positive);
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

  final static class TimeBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final type.TIME b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    TimeBetweenPredicate(final type.TIME column, final LocalTime a, final type.TIME b, final boolean positive) {
      this(column, data.Column.wrap(a), b, positive);
    }

    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final LocalTime b, final boolean positive) {
      this(column, a, data.Column.wrap(b), positive);
    }

    TimeBetweenPredicate(final type.TIME column, final LocalTime a, final LocalTime b, final boolean positive) {
      this(column, data.Column.wrap(a), data.Column.wrap(b), positive);
    }

    TimeBetweenPredicate(final LocalTime value, final type.TIME a, final type.TIME b, final boolean positive) {
      this(data.Column.wrap(value), a, b, positive);
    }

    TimeBetweenPredicate(final LocalTime value, final LocalTime a, final type.TIME b, final boolean positive) {
      this(data.Column.wrap(value), data.Column.wrap(a), b, positive);
    }

    TimeBetweenPredicate(final LocalTime value, final type.TIME a, final LocalTime b, final boolean positive) {
      this(data.Column.wrap(value), a,  data.Column.wrap(b), positive);
    }

    TimeBetweenPredicate(final LocalTime value, final LocalTime a, final LocalTime b, final boolean positive) {
      this(data.Column.wrap(value), data.Column.wrap(a),  data.Column.wrap(b), positive);
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

  final static class TextualBetweenPredicate extends BetweenPredicate {
    private final Subject a;
    private final Subject b;

    TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      super(column, positive);
      this.a = (Subject)a;
      this.b = (Subject)b;
    }

    TextualBetweenPredicate(final type.Textual<?> column, final CharSequence a, final type.Textual<?> b, final boolean positive) {
      this(column, (type.Textual<?>)data.Column.wrap(a), b, positive);
    }

    TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final CharSequence b, final boolean positive) {
      this(column, a, (type.Textual<?>)data.Column.wrap(b), positive);
    }

    TextualBetweenPredicate(final type.Textual<?> column, final CharSequence a, final CharSequence b, final boolean positive) {
      this(column, (type.Textual<?>)data.Column.wrap(a), (type.Textual<?>)data.Column.wrap(b), positive);
    }

    TextualBetweenPredicate(final CharSequence value, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      this((type.Textual<?>)data.Column.wrap(value), a, b, positive);
    }

    TextualBetweenPredicate(final CharSequence value, final CharSequence a, final type.Textual<?> b, final boolean positive) {
      this((type.Textual<?>)data.Column.wrap(value), (type.Textual<?>)data.Column.wrap(a), b, positive);
    }

    TextualBetweenPredicate(final CharSequence value, final type.Textual<?> a, final CharSequence b, final boolean positive) {
      this((type.Textual<?>)data.Column.wrap(value), a,  (type.Textual<?>)data.Column.wrap(b), positive);
    }

    TextualBetweenPredicate(final CharSequence value, final CharSequence a, final CharSequence b, final boolean positive) {
      this((type.Textual<?>)data.Column.wrap(value), (type.Textual<?>)data.Column.wrap(a),  (type.Textual<?>)data.Column.wrap(b), positive);
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