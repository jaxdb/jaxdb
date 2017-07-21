/* Copyright (c) 2017 lib4j
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
import java.time.temporal.Temporal;
import java.util.Set;

import org.lib4j.lang.Numbers;
import org.lib4j.util.Temporals;

final class BetweenPredicates {
  protected static abstract class BetweenPredicate<T> extends Predicate<T> {
    protected final boolean positive;

    protected BetweenPredicate(final type.DataType<?> dataType, final boolean positive) {
      super(dataType);
      this.positive = positive;
    }

    protected abstract type.DataType<?> a();
    protected abstract type.DataType<?> b();

    @Override
    protected final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  protected static class NumericBetweenPredicate<T> extends BetweenPredicate<T> {
    protected final type.Numeric<?> a;
    protected final type.Numeric<?> b;

    protected NumericBetweenPredicate(final type.Numeric<?> dataType, final type.Numeric<?> a, final type.Numeric<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = a;
      this.b = b;
    }

    @Override
    protected type.DataType<?> a() {
      return a;
    }

    @Override
    protected type.DataType<?> b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null)
        return null;

      final Number a = this.a.evaluate(visited);
      final Number b = this.b.evaluate(visited);
      final Number c = (Number)this.dataType.evaluate(visited);
      return Numbers.compare(a, c) >= 0 && Numbers.compare(c, b) <= 0 ? positive : !positive;
    }
  }

  protected static class TemporalBetweenPredicate<T> extends BetweenPredicate<T> {
    protected final type.Temporal<?> a;
    protected final type.Temporal<?> b;

    protected TemporalBetweenPredicate(final type.Temporal<?> dataType, final type.Temporal<?> a, final type.Temporal<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = a;
      this.b = b;
    }

    @Override
    protected type.DataType<?> a() {
      return a;
    }

    @Override
    protected type.DataType<?> b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null)
        return null;

      final Temporal a = this.a.evaluate(visited);
      final Temporal b = this.b.evaluate(visited);
      final Temporal c = (Temporal)this.dataType.evaluate(visited);
      return Temporals.compare(a, c) <= 0 && Temporals.compare(c, b) >= 0 ? positive : !positive;
    }
  }

  protected static class TimeBetweenPredicate<T> extends BetweenPredicate<T> {
    protected final type.TIME a;
    protected final type.TIME b;

    protected TimeBetweenPredicate(final type.TIME dataType, final type.TIME a, final type.TIME b, final boolean positive) {
      super(dataType, positive);
      this.a = a;
      this.b = b;
    }

    @Override
    protected type.DataType<?> a() {
      return a;
    }

    @Override
    protected type.DataType<?> b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null)
        return null;

      final Temporal a = this.a.evaluate(visited);
      final Temporal b = this.b.evaluate(visited);
      final Temporal c = (Temporal)this.dataType.evaluate(visited);
      return Temporals.compare(a, c) >= 0 && Temporals.compare(c, b) <= 0 ? positive : !positive;
    }
  }

  protected static class TextualBetweenPredicate<T> extends BetweenPredicate<T> {
    protected final type.Textual<?> a;
    protected final type.Textual<?> b;

    protected TextualBetweenPredicate(final type.Textual<?> dataType, final type.Textual<?> a, final type.Textual<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = a;
      this.b = b;
    }

    @Override
    protected type.DataType<?> a() {
      return a;
    }

    @Override
    protected type.DataType<?> b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null)
        return null;

      final String a = this.a.evaluate(visited);
      final String b = this.b.evaluate(visited);
      final String c = (String)this.dataType.evaluate(visited);
      return a.compareTo(c) >= 0 && c.compareTo(b) <= 0 ? positive : !positive;
    }
  }

  private BetweenPredicates() {
  }
}