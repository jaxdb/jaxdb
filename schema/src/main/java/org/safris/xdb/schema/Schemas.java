/* Copyright (c) 2017 Seva Safris
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