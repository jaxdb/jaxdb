/* Copyright (c) 2016 JAX-DB
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

abstract class Keyword<T extends type.Subject<?>> extends Provision {
  private final Keyword<T> parent;

  Keyword(final Keyword<T> parent) {
    this.parent = parent;
  }

  final Keyword<T> parent() {
    return parent;
  }

  @Override
  final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
    normalize().compile(compilation, isExpression);
  }

  @Override
  Object evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException();
  }

  private Command<?> command;

  final Command<?> normalize() {
    return command == null ? command = buildCommand() : command;
  }

  abstract Command<?> buildCommand();
}