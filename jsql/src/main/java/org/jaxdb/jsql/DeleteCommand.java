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

import org.jaxdb.jsql.DeleteImpl.DELETE;
import org.jaxdb.jsql.DeleteImpl.WHERE;

final class DeleteCommand extends Command<DELETE> {
  private WHERE where;

  DeleteCommand(final DELETE delete) {
    super(delete);
  }

  public WHERE where() {
    return where;
  }

  public void add(final WHERE where) {
    this.where = where;
  }

  @Override
  Class<? extends Schema> getSchema() {
    return getKeyword().entity.schema();
  }

  @Override
  void compile(final Compilation compilation) throws IOException {
    final Compiler compiler = compilation.compiler;
    if (where() != null)
      compiler.compile(getKeyword(), where(), compilation);
    else
      compiler.compile(getKeyword(), compilation);
  }
}