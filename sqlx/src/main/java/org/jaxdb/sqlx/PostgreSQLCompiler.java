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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;

final class PostgreSQLCompiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  private static String toHexString(final String hex) {
    return "'\\x" + hex + "'";
  }

  @Override
  String compile(final dt.BINARY value) {
    return toHexString(value.get());
  }

  @Override
  String compile(final dt.BLOB value) {
    return toHexString(value.get());
  }

  @Override
  String restartWith(final Connection connection, final String tableName, final String columnName, final long restartWith) throws SQLException {
    final String sql = "ALTER SEQUENCE " + getSequenceName(tableName, columnName) + " RESTART WITH " + restartWith;
    if (connection != null) {
      try (final Statement statement = connection.createStatement()) {
        statement.execute(sql);
      }
    }

    return sql;
  }
}