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
import java.sql.SQLException;
import java.util.Set;

// FIXME: Reimplement ORDER BY
final class OrderingSpec extends Evaluable {
  final boolean ascending;
  final data.Column<?> column;

  OrderingSpec(final boolean ascending, final data.Column<?> column) {
    this.ascending = ascending;
    this.column = column;
  }

  @Override
  data.Table getTable() {
    return column.getTable();
  }

  @Override
  data.Column<?> getColumn() {
    return column;
  }

  @Override
  final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    compilation.compiler.compileOrder(this, compilation);
  }

  @Override
  Object evaluate(final Set<Evaluable> visited) {
    return column.evaluate(visited);
  }
}