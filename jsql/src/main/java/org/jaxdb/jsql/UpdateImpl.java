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
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.jaxdb.jsql.Update.SET;

final class UpdateImpl extends Command.Modify<data.Column<?>,Executable.Modify.Update> implements SET {
  private data.Table<?> entity;
  private List<Subject> sets;
  private Condition<?> where;

  UpdateImpl(final data.Table<?> entity) {
    this.entity = entity;
  }

  private void initSets() {
    if (sets == null)
      sets = new ArrayList<>();
  }

  @Override
  public final <T>UpdateImpl SET(final data.Column<? extends T> column, final type.Column<? extends T> to) {
    return set(column, to);
  }

  @Override
  public final <T>UpdateImpl SET(final data.Column<T> column, final T to) {
    return set(column, to);
  }

  @Override
  public UpdateImpl WHERE(final Condition<?> condition) {
    this.where = condition;
    return this;
  }

  private <T>UpdateImpl set(final data.Column<T> column, final T to) {
    initSets();
    sets.add(column);
    // FIXME: data.ENUM.NULL
    sets.add(to == null ? null : data.wrap(to));
    return this;
  }

  private <T>UpdateImpl set(final data.Column<? extends T> column, final type.Column<? extends T> to) {
    initSets();
    sets.add(column);
    sets.add((Subject)to);
    return this;
  }

  @Override
  final data.Table<?> getTable() {
    return entity;
  }

  @Override
  final data.Column<?> getColumn() {
    throw new UnsupportedOperationException();
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Compiler compiler = compilation.compiler;
    if (sets != null)
      compiler.compileUpdate(entity, sets, where, compilation);
    else
      compiler.compileUpdate(entity, compilation);
  }

  @Override
  void onCommit(final Connector connector, final Connection connection) {
//    if (count == 1 && sets == null) {
//      connector.getSchema().onUpdate(null, entity, null);
      entity._commitEntity$();
//    }
  }
}