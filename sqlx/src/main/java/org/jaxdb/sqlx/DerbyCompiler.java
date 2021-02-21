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

package org.jaxdb.sqlx;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;

final class DerbyCompiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  final String compile(final dt.BLOB value) {
    return "CAST(X'" + value + "' AS BLOB)";
  }

  @Override
  String restartWith(final String tableName, final String columnName, final int restartWith) {
    final Dialect dialect = getVendor().getDialect();
    return "ALTER TABLE " + dialect.quoteIdentifier(tableName) + " ALTER COLUMN " + dialect.quoteIdentifier(columnName) + " RESTART WITH " + (restartWith + 1);
  }
}