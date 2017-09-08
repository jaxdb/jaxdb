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
import java.time.LocalTime;
import java.time.temporal.Temporal;
import java.util.Set;

import org.lib4j.lang.Numbers;
import org.lib4j.util.Temporals;

final class BetweenPredicates {
  protected static abstract class BetweenPredicate extends Predicate {
    protected final boolean positive;

    protected BetweenPredicate(final kind.DataType<?> dataType, final boolean positive) {
      super(dataType);
      this.positive = positive;
    }

    protected abstract Compilable a();
    protected abstract Compilable b();

    @Override
    protected final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  protected static class NumericBetweenPredicate extends BetweenPredicate {
    protected final Compilable a;
    protected final Compilable b;

    protected NumericBetweenPredicate(final kind.Numeric<?> dataType, final kind.Numeric<?> a, final kind.Numeric<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    @Override
    protected Compilable a() {
      return a;
    }

    @Override
    protected Compilable b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null || !(dataType instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Number a = (Number)((Evaluable)this.a).evaluate(visited);
      final Number b = (Number)((Evaluable)this.b).evaluate(visited);
      final Number c = (Number)((Evaluable)this.dataType).evaluate(visited);
      return Numbers.compare(a, c) >= 0 && Numbers.compare(c, b) <= 0 ? positive : !positive;
    }
  }

  protected static class TemporalBetweenPredicate extends BetweenPredicate {
    protected final Compilable a;
    protected final Compilable b;

    protected TemporalBetweenPredicate(final kind.Temporal<?> dataType, final kind.Temporal<?> a, final kind.Temporal<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    @Override
    protected Compilable a() {
      return a;
    }

    @Override
    protected Compilable b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null || !(dataType instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Temporal a = (Temporal)((Evaluable)this.a).evaluate(visited);
      final Temporal b = (Temporal)((Evaluable)this.b).evaluate(visited);
      final Temporal c = (Temporal)((Evaluable)this.dataType).evaluate(visited);
      return Temporals.compare(a, c) <= 0 && Temporals.compare(c, b) >= 0 ? positive : !positive;
    }
  }

  protected static class TimeBetweenPredicate extends BetweenPredicate {
    protected final Compilable a;
    protected final Compilable b;

    protected TimeBetweenPredicate(final kind.TIME<LocalTime> dataType, final kind.TIME<LocalTime> a, final kind.TIME<LocalTime> b, final boolean positive) {
      super(dataType, positive);
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    @Override
    protected Compilable a() {
      return a;
    }

    @Override
    protected Compilable b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null || !(dataType instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Temporal a = (Temporal)((Evaluable)this.a).evaluate(visited);
      final Temporal b = (Temporal)((Evaluable)this.b).evaluate(visited);
      final Temporal c = (Temporal)((Evaluable)this.dataType).evaluate(visited);
      return Temporals.compare(a, c) >= 0 && Temporals.compare(c, b) <= 0 ? positive : !positive;
    }
  }

  protected static class TextualBetweenPredicate extends BetweenPredicate {
    protected final Compilable a;
    protected final Compilable b;

    protected TextualBetweenPredicate(final kind.Textual<?> dataType, final kind.Textual<?> a, final kind.Textual<?> b, final boolean positive) {
      super(dataType, positive);
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    @Override
    protected Compilable a() {
      return a;
    }

    @Override
    protected Compilable b() {
      return b;
    }

    @Override
    protected Boolean evaluate(final Set<Evaluable> visited) {
      if (dataType == null || a == null || b == null || !(dataType instanceof Evaluable) || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final String a = (String)((Evaluable)this.a).evaluate(visited);
      final String b = (String)((Evaluable)this.b).evaluate(visited);
      final String c = (String)((Evaluable)this.dataType).evaluate(visited);
      return a.compareTo(c) >= 0 && c.compareTo(b) <= 0 ? positive : !positive;
    }
  }

  private BetweenPredicates() {
  }
}