package org.jaxdb.sqlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.vendor.DBVendor;

public class DB2Compiler extends Compiler {
  DB2Compiler() {
    super(DBVendor.DB2);
  }

  @Override
  String restartWith(final Connection connection, final String tableName, final String columnName, final long restartWith) throws SQLException {
    final String sql = "ALTER TABLE " + getDialect().quoteIdentifier(tableName) + " ALTER COLUMN " + getDialect().quoteIdentifier(columnName) + " RESTART WITH " + restartWith;
    if (connection != null) {
      try (final Statement statement = connection.createStatement()) {
        statement.execute(sql);
      }
    }

    return sql;
  }
}