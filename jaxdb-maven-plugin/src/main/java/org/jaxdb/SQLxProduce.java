/* Copyright (c) 2019 JAX-DB
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

package org.jaxdb;

import org.jaxdb.www.sqlx_0_6.xLygluGCXAA.$Database;

abstract class SQLxProduce extends Produce<JaxDbMojo<?>.Configuration,SQLxProduce,$Database> {
  private static int index;
  static final SQLxProduce[] values = new SQLxProduce[1];

  private SQLxProduce(final String name) {
    super(name, values, index++);
  }

  static final SQLxProduce SQL = new SQLxProduce("sql") {
    @Override
    void execute(final JaxDbMojo<?>.Configuration configuration, final SqlMojo<SQLxProduce,$Database> sqlMojo) throws Exception {
      sqlMojo.executeStaged(configuration);
    }
  };
}