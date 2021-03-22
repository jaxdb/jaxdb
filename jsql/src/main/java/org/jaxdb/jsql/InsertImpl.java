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

final class InsertImpl<T extends type.Subject<?>> extends Executable.Keyword<T> implements Insert._INSERT<T>, Insert.VALUES<T>, AutoCloseable {
  private type.Entity entity;
  private type.DataType<?>[] columns;
  private Select.untyped.SELECT<?> select;

  InsertImpl(final type.Entity entity) {
    super(null);
    this.entity = entity;
    this.columns = null;
  }

  @SafeVarargs
  InsertImpl(final type.DataType<?> ... columns) {
    super(null);
    this.entity = null;
    this.columns = columns;
    final type.Entity entity = columns[0].owner;
    if (entity == null)
      throw new IllegalArgumentException("DataType must belong to an Entity");

    for (int i = 1; i < columns.length; ++i)
      if (!columns[i].owner.equals(entity))
        throw new IllegalArgumentException("All columns must belong to the same Entity");
  }

  @Override
  public Insert.VALUES<T> VALUES(final Select.untyped.SELECT<?> select) {
    this.select = select;
    return this;
  }

  // FIXME: Remove this Command construct
  @Override
  final Command<?> buildCommand() {
    return new Command<InsertImpl<?>>(this) {
      private Class<? extends Schema> schema;

      @Override
      Class<? extends Schema> getSchema() {
        if (schema != null)
          return schema;

        if (entity != null)
          return schema = entity.schema();

        if (columns != null)
          return schema = columns[0].owner.schema();

        throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException {
        InsertImpl.this.compile(compilation, isExpression);
      }
    };
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException {
    final Compiler compiler = compilation.compiler;
    if (select != null)
      compiler.compile(entity, columns, select, compilation);
    else
      compiler.compile(entity, columns, compilation);
  }

  @Override
  public void close() {
    entity = null;
    columns = null;
    select = null;
  }
}