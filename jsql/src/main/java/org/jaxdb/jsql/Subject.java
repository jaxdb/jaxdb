/* Copyright (c) 2016 JAX-DB
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

abstract class Subject implements Cloneable {
  abstract void compile(Compilation compilation, boolean isExpression) throws IOException, SQLException;
  abstract data.Table table();
  abstract data.Column<?> column();

  @SuppressWarnings("unchecked")
  final Class<? extends Schema> schema() {
    final data.Table table = table();
    return table == null ? null : (Class<? extends Schema>)table.getClass().getEnclosingClass();
  }

  @Override
  protected Subject clone() {
    try {
      return (Subject)super.clone();
    }
    catch (final CloneNotSupportedException e) {
      throw new RuntimeException();
    }
  }
}