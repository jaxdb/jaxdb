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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import org.jaxdb.jsql.keyword.Select;

final class InPredicate extends Predicate {
  final Subject[] values;

  @SafeVarargs
  InPredicate(final type.Column<?> column, final Object ... values) {
    this(column, Arrays.asList(values));
  }

  InPredicate(final type.Column<?> column, final Collection<?> values) {
    super(column);
    final int len = values.size();
    this.values = new data.Column<?>[len];
    final Iterator<?> iterator = values.iterator();
    for (int i = 0; i < len; ++i) // [A]
      this.values[i] = org.jaxdb.jsql.data.wrap(iterator.next());
  }

  InPredicate(final type.Column<?> column, final Select.untyped.SELECT<? extends data.Column<?>> query) {
    super(column);
    this.values = new Subject[] {(Subject)query};
  }

  @Override
  Object evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException("IN cannot be evaluated outside the DB");
  }

  @Override
  final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    compilation.compiler.compileInPredicate(this, compilation);
  }

  @Override
  void collectColumns(final ArrayList<data.Column<?>> list) {
    collectColumn(list, (Subject)column);
  }
}