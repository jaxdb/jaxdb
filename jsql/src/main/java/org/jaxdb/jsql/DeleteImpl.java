/* Copyright (c) 2015 JAX-DB
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

final class DeleteImpl extends Executable.Command<type.DataType<?>> implements Delete._DELETE, AutoCloseable {
  private type.Entity entity;
  private Condition<?> where;

  DeleteImpl(final type.Entity entity) {
    this.entity = entity;
  }

  @Override
  public DeleteImpl WHERE(final Condition<?> where) {
    this.where = where;
    return this;
  }

  @Override
  final Class<? extends Schema> schema() {
    return entity.schema();
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException {
    final Compiler compiler = compilation.compiler;
    if (where != null)
      compiler.compileDelete(entity, where, compilation);
    else
      compiler.compileDelete(entity, compilation);
  }

  @Override
  public void close() throws Exception {
    entity = null;
    where = null;
  }
}