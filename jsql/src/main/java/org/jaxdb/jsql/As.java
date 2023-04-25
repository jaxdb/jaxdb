/* Copyright (c) 2017 JAX-DB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, variable to the following conditions:
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

import org.jaxdb.jsql.keyword.Keyword;

final class As<V extends Serializable> extends data.Entity {
  private final Evaluable parent;
  private final data.Entity variable;
  private final boolean explicit;

  As(final Keyword parent, final type.Entity variable, final boolean explicit) {
    super(false);
    this.parent = parent;
    this.variable = (data.Entity)variable;
    this.explicit = explicit;
  }

  As(final Evaluable parent, final type.Entity variable) {
    super(false);
    this.parent = parent;
    this.variable = (data.Entity)variable;
    this.explicit = true;
  }

  Evaluable parent() {
    return parent;
  }

  data.Entity getVariable() {
    return variable;
  }

  boolean isExplicit() {
    return this.explicit;
  }

  @Override
  data.Table getTable() {
    return parent.getTable();
  }

  @Override
  data.Column<?> getColumn() {
    return parent.getColumn();
  }

  @Override
  boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    return compilation.compiler.compileAs(this, compilation, isExpression);
  }

  @Override
  Serializable evaluate(final Set<Evaluable> visited) {
    return variable.evaluate(visited);
  }
}