package org.jaxdb.sqlx;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;

public class DB2Compiler extends Compiler {
  @Override
  public DBVendor getVendor() {
    return DBVendor.DB2;
  }

  @Override
  String restartWith(final String tableName, final String columnName, final int restartWith) {
    final Dialect dialect = getVendor().getDialect();
    return "ALTER TABLE " + dialect.quoteIdentifier(tableName) + " ALTER COLUMN " + dialect.quoteIdentifier(columnName) + " RESTART WITH " + (restartWith + 1);
  }
}