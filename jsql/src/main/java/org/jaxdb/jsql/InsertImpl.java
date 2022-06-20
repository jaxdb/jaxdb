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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jaxdb.jsql.Insert.CONFLICT_ACTION;
import org.jaxdb.jsql.Insert.INSERT;
import org.jaxdb.jsql.Insert.ON_CONFLICT;
import org.jaxdb.jsql.Insert._INSERT;
import org.jaxdb.jsql.data.Column.SetBy;
import org.libj.util.function.ToBooleanFunction;

final class InsertImpl<D extends data.Entity<?>> extends Command.Modify<D,Executable.Modify.Insert> implements _INSERT<D>, ON_CONFLICT {
  private data.Table<?> entity;
  private data.Column<?>[] columns;
  private data.Column<?>[] primaries;
  final data.Column<?>[] autos;
  private Select.untyped.SELECT<?> select;
  private data.Column<?>[] onConflict;
  private boolean doUpdate;

  InsertImpl(final data.Table<?> entity) {
    this.entity = entity;
    this.columns = null;
    this.autos = recurseColumns(entity._auto$, c -> c.setByCur != SetBy.USER, 0, 0);
  }

  @SafeVarargs
  InsertImpl(final data.Column<?> ... columns) {
    this.entity = null;
    this.columns = columns;
    final data.Table<?> table = assertNotNull(columns[0].getTable(), "Column must belong to a Table");
    for (int i = 1; i < columns.length; ++i)
      if (!columns[i].getTable().equals(table))
        throw new IllegalArgumentException("All columns must belong to the same Table");

    this.primaries = recurseColumns(columns, c -> c.primary, 0, 0);
    this.autos = recurseColumns(columns, c -> c.setByCur != SetBy.USER && c.generateOnInsert == GenerateOn.AUTO_GENERATED, 0, 0);
  }

  private static final data.Column<?>[] EMPTY = {};

  private data.Column<?>[] recurseColumns(final data.Column<?>[] columns, final ToBooleanFunction<data.Column<?>> predicate, final int index, final int depth) {
    if (index == columns.length)
      return depth == 0 ? EMPTY : new data.Column<?>[depth];

    final data.Column<?> column = columns[index];
    final boolean include = predicate.applyAsBoolean(column);
    final data.Column<?>[] results = recurseColumns(columns, predicate, index + 1, include ? depth + 1 : depth);
    if (include)
      results[depth] = column;

    return results;
  }

  @Override
  public INSERT<D> VALUES(final Select.untyped.SELECT<?> select) {
    this.select = select;
    return this;
  }

  @Override
  public ON_CONFLICT ON_CONFLICT() {
    if (entity != null)
      this.onConflict = entity._primary$;
    else if (primaries != null)
      this.onConflict = primaries;
    else
      throw new IllegalArgumentException("ON CONFLICT requires primary columns in the INSERT clause");

    return this;
  }

  @Override
  public CONFLICT_ACTION DO_UPDATE() {
    this.doUpdate = true;
    return this;
  }

  @Override
  public CONFLICT_ACTION DO_NOTHING() {
    this.doUpdate = false;
    return this;
  }

  @Override
  final data.Table<?> getTable() {
    if (entity != null)
      return entity;

    if (columns != null)
      return entity = columns[0].getTable();

    throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
  }

  @Override
  final data.Column<?> getColumn() {
    throw new UnsupportedOperationException();
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final data.Column<?>[] columns = this.columns != null ? this.columns : entity._column$;
    final Compiler compiler = compilation.compiler;
    if (onConflict != null)
      compiler.compileInsertOnConflict(columns, select, onConflict, doUpdate, compilation);
    else if (select != null)
      compiler.compileInsertSelect(columns, select, false, compilation);
    else
      compiler.compileInsert(columns, false, compilation);

//    compilation.concat(';');
  }

  @Override
  void onCommit(final Connector connector, final Connection connection) {
//    if (count == 1 && select == null) {
//      connector.getSchema().onInsert(null, entity);
      entity._commitEntity$();
//    }
  }
}