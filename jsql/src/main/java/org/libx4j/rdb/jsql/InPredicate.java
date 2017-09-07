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
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.libx4j.rdb.jsql.model.kind;
import org.libx4j.rdb.jsql.model.select;

final class InPredicate extends Predicate {
  protected final boolean positive;
  protected final Compilable[] values;

  @SafeVarargs
  protected InPredicate(final kind.DataType<?> dataType, final boolean positive, final Object ... values) {
    this(dataType, positive, Arrays.asList(values));
  }

  protected InPredicate(final kind.DataType<?> dataType, final boolean positive, final Collection<?> values) {
    super(dataType);
    this.positive = positive;
    final Iterator<?> iterator = values.iterator();
    this.values = new type.DataType<?>[values.size()];
    for (int i = 0; iterator.hasNext(); i++)
      this.values[i] = org.libx4j.rdb.jsql.type.DataType.wrap(iterator.next());
  }

  protected InPredicate(final kind.DataType<?> dataType, final boolean positive, final select.untyped.SELECT<? extends type.DataType<?>> query) {
    super(dataType);
    this.positive = positive;
    this.values = new Compilable[] {(Compilable)query};
  }

  @Override
  protected Object evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException("IN cannot be evaluated outside the DB");
  }

  @Override
  protected final void compile(final Compilation compilation) throws IOException {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }
}