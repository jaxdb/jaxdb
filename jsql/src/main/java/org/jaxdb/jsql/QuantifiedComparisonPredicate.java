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
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Set;

import org.jaxdb.jsql.keyword.Select;

class QuantifiedComparisonPredicate<V extends Serializable> extends data.Entity<V> {
  final String qualifier;
  final Subject subQuery;

  QuantifiedComparisonPredicate(final String qualifier, final Select.untyped.SELECT<?> subQuery) {
    super(false);
    this.qualifier = qualifier;
    this.subQuery = (Subject)subQuery;
  }

  @Override
  final data.Table<?> getTable() {
    return subQuery.getTable();
  }

  @Override
  data.Column<?> getColumn() {
    return subQuery.getColumn();
  }

  @Override
  final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    compilation.compiler.compileQuantifiedComparisonPredicate(this, compilation);
  }

  @Override
  Serializable evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException("QuantifiedComparisonPredicate cannot be evaluated outside the DB");
  }
}