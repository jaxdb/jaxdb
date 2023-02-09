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
import java.sql.Statement;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DbVendor;

final class DerbyCompiler extends Compiler {
  DerbyCompiler() {
    super(DbVendor.DERBY);
  }

  @Override
  final StringBuilder compile(final StringBuilder b, final dt.BLOB value) {
    return b.append("CAST(X'").append(value).append("' AS BLOB)");
  }

  @Override
  boolean sequenceReset(final Connection connection, final Appendable builder, final String tableName, final String columnName, final long restartWith) throws IOException, SQLException {
    final StringBuilder sql = new StringBuilder("ALTER TABLE ");
    getDialect().quoteIdentifier(sql, tableName);
    sql.append(" ALTER COLUMN ");
    getDialect().quoteIdentifier(sql, columnName).append(" RESTART WITH ").append(restartWith);
    if (connection != null) {
      try (final Statement statement = connection.createStatement()) {
        return statement.executeUpdate(sql.toString()) != 0;
      }
    }

    builder.append('\n').append(sql).append(';');
    return true;
  }
}