/* Copyright (c) 2021 JAX-DB
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

import java.util.Arrays;

import org.jaxdb.jsql.data.Column;

public class Key<T extends data.Table<?>> {
  private final T table;

  public Key(final T table) {
    this.table = assertNotNull(table);
  }

  public T getTable() {
    return this.table;
  }

  @Override
  public int hashCode() {
    return table.hashCode() ^ Arrays.hashCode(table._primary$);
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Key))
      return false;

    final Key<?> that = (Key<?>)obj;
    return table.getClass() == that.table.getClass() && Arrays.equals(table._primary$, that.table._primary$);
  }

  @Override
  public String toString() {
    if (table._primary$.length == 0)
      return "()";

    final StringBuilder s = new StringBuilder();
    s.append('(');
    for (final Column<?> column : table._primary$)
      s.append(column).append(',');

    s.setCharAt(s.length() - 1, ')');
    return s.toString();
  }
}