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

package org.jaxdb.ddlx;

import org.openjax.ext.util.Enums;

enum SQLStandard {
  SQL92("SQL-92"),
  SQL99("SQL-99"),
  SQL2003("SQL-2003");

  static SQLStandard[] toArray(final int mask) {
    return Enums.Mask.toArray(mask, SQLStandard.values());
  }

  private final String name;

  SQLStandard(final String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return name;
  }
}