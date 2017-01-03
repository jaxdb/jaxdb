package org.safris.xdb.schema;

import java.sql.Connection;
import java.sql.SQLException;

import org.safris.xdb.xds.xe.xds_schema;

public final class DDL {
  public static void create(final xds_schema schema, final Connection connection) throws GeneratorExecutionException, SQLException {
    final DBVendor vendor = DBVendor.parse(connection.getMetaData());
    final java.sql.Statement statement = connection.createStatement();
    final String[] sqls = Generator.createDDL(schema, vendor, null);
    for (final String sql : sqls)
      statement.addBatch(sql);

    statement.executeBatch();
  }

  private DDL() {
  }
}