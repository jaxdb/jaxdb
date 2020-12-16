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
import java.util.Set;

final class LikePredicate extends Predicate {
  final boolean positive;
  final CharSequence pattern;

  LikePredicate(final kind.Textual<?> dataType, final boolean positive, final CharSequence pattern) {
    super(dataType);
    this.positive = positive;
    this.pattern = pattern;
  }

  @Override
  String evaluate(final Set<Evaluable> visited) {
    if (dataType == null || pattern == null || !(dataType instanceof Evaluable))
      return null;

    final type.Textual<?> a = (type.Textual<?>)((Evaluable)dataType).evaluate(visited);
    if (a.isNull())
      return null;

    final String value = a.get().toString();
    return value.matches(pattern.toString().replace("%", ".*")) == positive ? value : null;
  }

  @Override
  final void compile(final Compilation compilation) throws IOException {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }
}