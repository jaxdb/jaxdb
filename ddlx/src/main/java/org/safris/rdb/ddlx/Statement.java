/* Copyright (c) 2017 lib4j
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

package org.safris.rdb.ddlx;

public abstract class Statement {
  private final String sql;

  public Statement(final String sql) {
    this.sql = sql;
    if (sql == null)
      throw new NullPointerException("sql == null");
  }

  public String getSql() {
    return this.sql;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Statement))
      return false;

    final Statement that = (Statement)obj;
    return sql.equals(that.sql);
  }

  @Override
  public int hashCode() {
    return sql.hashCode() ^ 7;
  }

  @Override
  public String toString() {
    return sql;
  }
}