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

import org.jaxdb.jsql.UpdateImpl.SET;
import org.jaxdb.jsql.UpdateImpl.UPDATE;
import org.jaxdb.jsql.UpdateImpl.WHERE;

final class UpdateCommand extends Command<UPDATE> {
  private List<SET> set;
  private WHERE where;

  UpdateCommand(final UPDATE update) {
    super(update);
  }

  List<SET> set() {
    return set;
  }

  void add(final SET set) {
    if (this.set == null)
      this.set = new ArrayList<>();

    this.set.add(set);
  }

  WHERE where() {
    return where;
  }

  void add(final WHERE where) {
    this.where = where;
  }

  @Override
  Class<? extends Schema> getSchema() {
    return getKeyword().entity.schema();
  }

  @Override
  void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = Compiler.getCompiler(compilation.vendor);
    final UPDATE update = getKeyword();
    if (set() != null)
      compiler.compile(update, set(), where(), compilation);
    else
      compiler.compile(update, compilation);
  }
}