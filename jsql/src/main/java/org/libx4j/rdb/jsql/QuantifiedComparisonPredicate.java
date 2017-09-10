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
import java.util.Set;

import org.libx4j.rdb.jsql.Select;

class QuantifiedComparisonPredicate<T> extends data.Subject<T> {
  protected final String qualifier;
  protected final Compilable subQuery;

  protected QuantifiedComparisonPredicate(final String qualifier, final Select.untyped.SELECT<?> subQuery) {
    this.qualifier = qualifier;
    this.subQuery = (Compilable)subQuery;
  }

  @Override
  protected final void compile(final Compilation compilation) throws IOException {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }

  @Override
  protected Object evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException("QuantifiedComparisonPredicate cannot be evaluated outside the DB");
  }
}