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

import org.libj.lang.Strings;

final class Alias extends Subject {
  final Subject subject;
  final String name;

  Alias(final Subject subject, final int index) {
    this.subject = subject;
    this.name = Strings.getAlpha(index);
  }

  @Override
  data.Table getTable() {
    return subject.getTable();
  }

  @Override
  data.Column<?> getColumn() {
    return subject.getColumn();
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) {
    compilation.sql.append(name);
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof Alias && name.equals(((Alias)obj).name);
  }

  @Override
  public int hashCode() {
    return 31 + name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }
}