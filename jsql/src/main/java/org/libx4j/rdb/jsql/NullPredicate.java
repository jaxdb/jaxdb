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

final class NullPredicate extends Predicate {
  protected final boolean positive;

  protected NullPredicate(final kind.DataType<?> dataType, final boolean positive) {
    super(dataType);
    this.positive = positive;
  }

  @Override
  protected Boolean evaluate(final Set<Evaluable> visited) {
    return (positive ? get() == null : get() != null) ? Boolean.TRUE : Boolean.FALSE;
  }

  @Override
  protected final void compile(final Compilation compilation) throws IOException {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }
}