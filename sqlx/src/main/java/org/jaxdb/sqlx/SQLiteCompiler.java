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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;

final class SQLiteCompiler extends Compiler {
  SQLiteCompiler() {
    super(DBVendor.SQLITE);
  }

  @Override
  String compile(final dt.BOOLEAN value) {
    return value.get() ? "1" : "0";
  }

  @Override
  boolean restartWith(final Connection connection, final Appendable builder, final String tableName, final String columnName, final long restartWith) throws IOException, SQLException {
    return true;
  }
}