package org.jaxdb.sqlx;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.vendor.DbVendor;

public class DB2Compiler extends Compiler {
  DB2Compiler() {
    super(DbVendor.DB2);
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