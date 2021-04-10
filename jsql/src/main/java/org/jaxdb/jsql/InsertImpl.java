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

import org.jaxdb.jsql.Insert.DO_UPDATE;
import org.jaxdb.jsql.Insert.ON_CONFLICT;

final class InsertImpl<T extends type.Subject<?>> extends Executable.Modify.Command<T> implements Insert._INSERT<T>, Insert.VALUES<T>, Insert.ON_CONFLICT<T>, Insert.DO_UPDATE<T>, AutoCloseable {
  private Class<? extends Schema> schema;
  private type.Entity entity;
  private type.DataType<?>[] columns;
  private Select.untyped.SELECT<?> select;
  private type.DataType<?>[] onConflict;

  InsertImpl(final type.Entity entity) {
    this.entity = entity;
    this.columns = null;
  }

  @SafeVarargs
  InsertImpl(final type.DataType<?> ... columns) {
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

  @Override
  public ON_CONFLICT<T> ON_CONFLICT() {
    this.onConflict = entity._primary$;
    return this;
  }

  @Override
  public DO_UPDATE<T> DO_UPDATE() {
    return this;
  }

  @Override
  final Class<? extends Schema> schema() {
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
    final type.DataType<?>[] columns = this.columns != null ? this.columns : entity._column$;
    final Compiler compiler = compilation.compiler;
    if (onConflict != null)
      compiler.compileInsertOnConflict(columns, select, onConflict, compilation);
    else if (select != null)
      compiler.compileInsertSelect(columns, select, compilation);
    else
      compiler.compileInsert(columns, compilation);

  }

  @Override
  public void close() {
    entity = null;
    columns = null;
    select = null;
  }
}