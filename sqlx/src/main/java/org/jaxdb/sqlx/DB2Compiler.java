package org.jaxdb.sqlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;

public class DB2Compiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.DB2;
  }

  @Override
  String restartWith(final Connection connection, final String tableName, final String columnName, final long restartWith) throws SQLException {
    final Dialect dialect = getVendor().getDialect();
    final String sql = "ALTER TABLE " + dialect.quoteIdentifier(tableName) + " ALTER COLUMN " + dialect.quoteIdentifier(columnName) + " RESTART WITH " + restartWith;
    if (connection != null) {
      try (final Statement statement = connection.createStatement()) {
        statement.execute(sql);
      }
    }

    return sql;
  }
}