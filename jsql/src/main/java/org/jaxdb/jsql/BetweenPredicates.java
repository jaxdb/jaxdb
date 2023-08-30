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
  abstract static class BetweenPredicate<V> extends ComparisonPredicate<V> {
//    private final Supplier<Segment<V>> toInterval;
    final boolean positive;
    final type.Column<?> column;

    BetweenPredicate(final type.Column<?> column, final Subject a, final Subject b, final boolean positive) {
      super(column, a, b);
      this.column = column;
      this.positive = positive;
//      this.toInterval = null;
    }

    BetweenPredicate(final type.Column<?> column, final V a, final V b, final boolean positive) {
      super(column, data.wrap(a), data.wrap(b));
      this.column = column;
      this.positive = positive;
//      this.toInterval = toInterval(column, a, b, ((data.Column<V>)a).getDiscreteTopology(), positive);
    }

    BetweenPredicate(final V a, final type.Column<?> column, final V b, final boolean positive) {
      super(column, data.wrap(a), data.wrap(b));
      this.column = column;
      this.positive = positive;
//      this.toInterval = toInterval(a, column, b, ((data.Column<V>)a).getDiscreteTopology(), positive);
    }

    BetweenPredicate(final V a, final V b, final type.Column<?> column, final boolean positive) {
      super(column, data.wrap(a), data.wrap(b));
      this.column = column;
      this.positive = positive;
//      this.toInterval = toInterval(a, b, column, ((data.Column<V>)a).getDiscreteTopology(), positive);
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compileBetweenPredicate(this, compilation);
    }

    abstract Subject a();
    abstract Subject b();
  }

  static final class NumericBetweenPredicate<V extends Number> extends BetweenPredicate<V> {
    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      super(column, (Subject)a, (Subject)b, positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final V a, final type.Numeric<?> b, final boolean positive) {
      this(column, data.wrap(a), b, positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final type.Numeric<?> a, final V b, final boolean positive) {
      this(column, a, data.wrap(b), positive);
    }

    NumericBetweenPredicate(final V value, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      this(data.wrap(value), a, b, positive);
    }

    NumericBetweenPredicate(final type.Numeric<?> column, final V a, final V b, final boolean positive) {
      super(column, a, b, positive);
    }

    NumericBetweenPredicate(final V value, final V a, final type.Numeric<?> b, final boolean positive) {
      super(value, a, b, positive);
    }

    NumericBetweenPredicate(final V value, final type.Numeric<?> a, final V b, final boolean positive) {
      super(value, a, b, positive);
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

  static final class TemporalBetweenPredicate<V> extends BetweenPredicate<V> {
    TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      super(column, (Subject)a, (Subject)b, positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final type.Temporal<?> column, final T a, final type.Temporal<?> b, final boolean positive) {
      this(column, (type.Temporal<?>)data.wrap(a), b, positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final type.Temporal<?> column, final type.Temporal<?> a, final T b, final boolean positive) {
      this(column, a, (type.Temporal<?>)data.wrap(b), positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final T value, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      this((type.Temporal<?>)data.wrap(value), a, b, positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final type.Temporal<?> column, final T a, final T b, final boolean positive) {
      super(column, (V)a, (V)b, positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final T value, final T a, final type.Temporal<?> b, final boolean positive) {
      super((V)value, (V)a, b, positive);
    }

    <T extends Temporal>TemporalBetweenPredicate(final T value, final type.Temporal<?> a, final T b, final boolean positive) {
     super((V)value, a,  (V)b, positive);
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

      final Temporal a = (Temporal)((Evaluable)this.a).evaluate(visited);
      final Temporal b = (Temporal)((Evaluable)this.b).evaluate(visited);
      final Temporal c = (Temporal)((Evaluable)this.column).evaluate(visited);
      return Temporals.compare(a, c) <= 0 && Temporals.compare(c, b) >= 0 == positive;
    }
  }

  static final class TimeBetweenPredicate<V extends LocalTime> extends BetweenPredicate<V> {
    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final type.TIME b, final boolean positive) {
      super(column, (Subject)a, (Subject)b, positive);
    }

    TimeBetweenPredicate(final type.TIME column, final V a, final type.TIME b, final boolean positive) {
      this(column, data.wrap(a), b, positive);
    }

    TimeBetweenPredicate(final type.TIME column, final type.TIME a, final V b, final boolean positive) {
      this(column, a, data.wrap(b), positive);
    }

    TimeBetweenPredicate(final V value, final type.TIME a, final type.TIME b, final boolean positive) {
      this(data.wrap(value), a, b, positive);
    }

    TimeBetweenPredicate(final type.TIME column, final V a, final V b, final boolean positive) {
      super(column, a, b, positive);
    }

    TimeBetweenPredicate(final V value, final V a, final type.TIME b, final boolean positive) {
      super(value, a, b, positive);
    }

    TimeBetweenPredicate(final V value, final type.TIME a, final V b, final boolean positive) {
      super(value, a,  b, positive);
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

  static final class TextualBetweenPredicate<V> extends BetweenPredicate<V> {
    TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      super(column, (Subject)a, (Subject)b, positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final type.Textual<?> column, final T a, final type.Textual<?> b, final boolean positive) {
      this(column, (type.Textual<?>)data.wrap(a), b, positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final type.Textual<?> column, final type.Textual<?> a, final T b, final boolean positive) {
      this(column, a, (type.Textual<?>)data.wrap(b), positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final T value, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      this((type.Textual<?>)data.wrap(value), a, b, positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final type.Textual<?> column, final T a, final T b, final boolean positive) {
      super(column, (V)a, (V)b, positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final T value, final T a, final type.Textual<?> b, final boolean positive) {
      super((V)value, (V)a, b, positive);
    }

    <T extends CharSequence>TextualBetweenPredicate(final T value, final type.Textual<?> a, final T b, final boolean positive) {
      super((V)value, a, (V)b, positive);
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