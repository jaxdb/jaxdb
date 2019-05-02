/* Copyright (c) 2017 OpenJAX
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

final class CaseCommand extends Command {
  private final Simple.CASE<?,?> simpleCase;
  private final Search.WHEN<?> searchCase;
  private final List<THEN<?,?>> then = new ArrayList<>();
  private ELSE<?> _else;

  protected CaseCommand(final Simple.CASE<?,?> simpleCase) {
    this.simpleCase = simpleCase;
    this.searchCase = null;
  }

  protected CaseCommand(final Search.WHEN<?> searchCase) {
    this.searchCase = searchCase;
    this.simpleCase = null;
  }

  protected List<THEN<?,?>> then() {
    return then;
  }

  protected void add(final THEN<?,?> then) {
    this.then.add(then);
  }

  protected ELSE<?> else_() {
    return _else;
  }

  protected void add(final ELSE<?> _else) {
    if (this._else != null)
      throw new IllegalStateException("Attempted to reassign ELSE");

    this._else = _else;
  }

  @Override
  protected Class<? extends Schema> getSchema() {
    throw new UnsupportedOperationException();
  }

  @Override
  @SuppressWarnings("rawtypes")
  protected void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = Compiler.getCompiler(compilation.vendor);
    if (simpleCase != null)
      compiler.compile(simpleCase, else_(), compilation);
    else if (searchCase != null)
      compiler.compile(searchCase, compilation);
    else
      throw new UnsupportedOperationException("Both simple and search CASEs should not be null");

    for (int i = 0; i < then().size(); i++) {
      final THEN<?,?> then = then().get(i);
      compiler.compile((CaseImpl.WHEN)then.parent(), then, else_(), compilation);
    }

    compiler.compile(else_(), compilation);
  }
}