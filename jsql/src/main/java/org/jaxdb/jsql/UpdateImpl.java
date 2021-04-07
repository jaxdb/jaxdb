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
import java.util.ArrayList;
import java.util.List;

final class UpdateImpl extends Executable.Modify.Command<type.DataType<?>> implements Update.SET, AutoCloseable {
  private type.Entity entity;
  private List<Compilable> sets;
  private Condition<?> where;

  UpdateImpl(final type.Entity entity) {
    this.entity = entity;
  }

  private void initSets() {
    if (sets == null)
      sets = new ArrayList<>();
  }

  @Override
  public final <T>UpdateImpl SET(final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
    return set(column, to);
  }

  @Override
  public final <T>UpdateImpl SET(final type.DataType<T> column, final T to) {
    return set(column, to);
  }

  @Override
  public UpdateImpl WHERE(final Condition<?> condition) {
    return where(condition);
  }

  private UpdateImpl where(final Condition<?> where) {
    this.where = where;
    return this;
  }

  private <T>UpdateImpl set(final type.DataType<T> column, final T to) {
    initSets();
    sets.add(column);
    sets.add(type.DataType.wrap(to));
    return this;
  }

  private <T>UpdateImpl set(final type.DataType<? extends T> column, final Case.CASE<? extends T> to) {
    initSets();
    sets.add(column);
    sets.add((Compilable)to);
    return this;
  }

  private <T>UpdateImpl set(final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
    initSets();
    sets.add(column);
    sets.add(to);
    return this;
  }

  @Override
  final Class<? extends Schema> schema() {
    return entity.schema();
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException {
    final Compiler compiler = compilation.compiler;
    if (sets != null)
      compiler.compileUpdate(entity, sets, where, compilation);
    else
      compiler.compileUpdate(entity, compilation);
  }

  @Override
  public void close() {
    entity = null;
    where = null;
    if (sets != null) {
      sets.clear();
      sets = null;
    }
  }
}