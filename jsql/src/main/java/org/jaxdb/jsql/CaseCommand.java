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
import java.util.ArrayList;
import java.util.List;

import org.jaxdb.jsql.CaseImpl.ELSE;
import org.jaxdb.jsql.CaseImpl.Search;
import org.jaxdb.jsql.CaseImpl.Simple;
import org.jaxdb.jsql.CaseImpl.THEN;

final class CaseCommand extends Command<Keyword<?>> {
  private final List<THEN<?,?>> then = new ArrayList<>();
  private ELSE<?> _else;

  CaseCommand(final Simple.CASE<?,?> simpleCase) {
    super(simpleCase);
  }

  CaseCommand(final Search.WHEN<?> searchCase) {
    super(searchCase);
  }

  List<THEN<?,?>> then() {
    return then;
  }

  void add(final THEN<?,?> then) {
    this.then.add(then);
  }

  ELSE<?> else_() {
    return _else;
  }

  void add(final ELSE<?> _else) {
    if (this._else != null)
      throw new IllegalStateException("Attempted to reassign ELSE");

    this._else = _else;
  }

  @Override
  Class<? extends Schema> getSchema() {
    throw new UnsupportedOperationException();
  }

  @Override
  @SuppressWarnings("rawtypes")
  void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = compilation.compiler;
    final Keyword<?> keyword = getKeyword();
    // FIXME: Use polymorphism
    if (keyword instanceof Simple.CASE<?,?>)
      compiler.compile((Simple.CASE<?,?>)keyword, else_(), compilation);
    else if (keyword instanceof Search.WHEN<?>)
      compiler.compile((Search.WHEN<?>)keyword, compilation);
    else
      throw new UnsupportedOperationException("Both simple and search CASEs should not be null");

    for (int i = 0; i < then().size(); i++) {
      final THEN<?,?> then = then().get(i);
      compiler.compile((CaseImpl.WHEN)then.parent(), then, else_(), compilation);
    }

    compiler.compile(else_(), compilation);
  }
}