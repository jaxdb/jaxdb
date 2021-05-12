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
import java.sql.SQLException;

import org.jaxdb.jsql.Insert.CONFLICT_ACTION;
import org.jaxdb.jsql.Insert.INSERT;
import org.jaxdb.jsql.Insert.ON_CONFLICT;
import org.jaxdb.jsql.Insert._INSERT;
import org.jaxdb.jsql.type.Table;
import org.libj.util.function.ToBooleanFunction;

final class InsertImpl<T extends type.Entity<?>> extends Command<T> implements _INSERT<T>, ON_CONFLICT {
  private type.Table table;
  private type.DataType<?>[] columns;
  private type.DataType<?>[] primaries;
  final type.DataType<?>[] autos;
  private Select.untyped.SELECT<?> select;
  private type.DataType<?>[] onConflict;
  private boolean doUpdate;

  InsertImpl(final type.Table table) {
    this.table = table;
    this.columns = null;
    this.autos = recurseColumns(table._auto$, c -> !c.wasSet(), 0, 0);
  }

  @SafeVarargs
  InsertImpl(final type.DataType<?> ... columns) {
    this.table = null;
    this.columns = columns;
    final type.Table table = columns[0].table;
    if (table == null)
      throw new IllegalArgumentException("DataType must belong to a Table");

    for (int i = 1; i < columns.length; ++i)
      if (!columns[i].table.equals(table))
        throw new IllegalArgumentException("All columns must belong to the same Table");

    this.primaries = recurseColumns(columns, c -> c.primary, 0, 0);
    this.autos = recurseColumns(columns, c -> !c.wasSet() && c.generateOnInsert == GenerateOn.AUTO_GENERATED, 0, 0);
  }

  private static final type.DataType<?>[] EMPTY = new type.DataType<?>[0];

  private type.DataType<?>[] recurseColumns(final type.DataType<?>[] columns, final ToBooleanFunction<type.DataType<?>> predicate, final int index, final int depth) {
    if (index == columns.length)
      return depth == 0 ? EMPTY : new type.DataType<?>[depth];

    final type.DataType<?> column = columns[index];
    final boolean include = predicate.applyAsBoolean(column);
    final type.DataType<?>[] results = recurseColumns(columns, predicate, index + 1, include ? depth + 1 : depth);
    if (include)
      results[depth] = column;

    return results;
  }

  @Override
  public INSERT<T> VALUES(final Select.untyped.SELECT<?> select) {
    this.select = select;
    return this;
  }

  @Override
  public ON_CONFLICT ON_CONFLICT() {
    if (table != null)
      this.onConflict = table._primary$;
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
  final Table table() {
    if (table != null)
      return table;

    if (columns != null)
      return table = columns[0].table;

    throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final type.DataType<?>[] columns = this.columns != null ? this.columns : table._column$;
    final Compiler compiler = compilation.compiler;
    if (onConflict != null)
      compiler.compileInsertOnConflict(columns, select, onConflict, doUpdate, compilation);
    else if (select != null)
      compiler.compileInsertSelect(columns, select, false, compilation);
    else
      compiler.compileInsert(columns, false, compilation);
  }

  @Override
  public void close() {
    table = null;
    columns = null;
    select = null;
  }
}