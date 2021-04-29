package org.jaxdb.sqlx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.vendor.DBVendor;

public class DB2Compiler extends Compiler {
  DB2Compiler() {
    super(DBVendor.DB2);
  }

  @Override
  boolean restartWith(final Connection connection, final Appendable builder, final String tableName, final String columnName, final long restartWith) throws IOException, SQLException {
    final String sql = "ALTER TABLE " + getDialect().quoteIdentifier(tableName) + " ALTER COLUMN " + getDialect().quoteIdentifier(columnName) + " RESTART WITH " + restartWith;
    if (connection != null) {
      try (final Statement statement = connection.createStatement()) {
        return statement.executeUpdate(sql) != 0;
      }
    }

    builder.append('\n').append(sql).append(';');
    return true;
  }
}