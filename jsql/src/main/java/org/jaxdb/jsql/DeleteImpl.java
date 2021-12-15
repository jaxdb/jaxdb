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

import org.jaxdb.jsql.Delete._DELETE;
import org.jaxdb.jsql.Notification.Action;

final class DeleteImpl extends Command<data.Column<?>> implements _DELETE {
  private data.Table<?> table;
  private Condition<?> where;

  DeleteImpl(final data.Table<?> table) {
    this.table = table;
  }

  @Override
  public DeleteImpl WHERE(final Condition<?> where) {
    this.where = where;
    return this;
  }

  @Override
  final data.Table<?> getTable() {
    return table;
  }

  @Override
  final data.Column<?> getColumn() {
    return where;
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Compiler compiler = compilation.compiler;
    if (where != null)
      compiler.compileDelete(table, where, compilation);
    else
      compiler.compileDelete(table, compilation);
  }

  @Override
  protected void onCommit(final Connector connector, final Connection connection, final int count) {
    final DatabaseCache databaseCache;
    if (where == null && (databaseCache = connector.getDatabaseCache()) != null && connector.hasNotificationListener(Action.DELETE, databaseCache, table))
      databaseCache.delete(table.getClass());
  }
}