/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.safris.xdb.xdl.DBVendor;
import org.safris.xdb.xdl.DDL;

public abstract class Schema {
  protected static DBVendor getDBVendor(final Connection connection) throws XDEException {
    try {
      final String url = connection.getMetaData().getURL();
      if (url.contains("jdbc:derby"))
        return DBVendor.DERBY;

      if (url.contains("jdbc:mysql"))
        return DBVendor.MY_SQL;

      if (url.contains("jdbc:postgresql"))
        return DBVendor.POSTGRE_SQL;
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, null);
    }

    return null;
  }

  protected static Connection getConnection(final Class<? extends Schema> schema) throws XDEException {
    final XDEDataSource dataSource = XDERegistry.getDataSource(schema);
    if (dataSource == null)
      throw new XDEException("No XDEDataSource has been registered for " + schema.getName());

    try {
      return dataSource.getConnection();
    }
    catch (final SQLException e) {
      throw XDEException.lookup(e, null);
    }
  }

  protected static void createDDL(final Class<? extends Schema> schema, final Table[] identity) throws SQLException {
    try (
      final Connection connection = getConnection(schema);
      final Statement statement = connection.createStatement();
    ) {
      final DBVendor vendor = getDBVendor(connection);
      for (final org.safris.xdb.xde.Table entity : identity) {
        final String[] sqls = entity.ddl(vendor, DDL.Type.CREATE);
        for (final String sql : sqls)
          statement.execute(sql);
      }
    }
  }

  protected static void dropDDL(final Class<? extends Schema> schema, final Table[] identity) throws SQLException {
    try (
      final Connection connection = getConnection(schema);
      final Statement statement = connection.createStatement();
    ) {
      final DBVendor vendor = getDBVendor(connection);
      for (int i = identity.length - 1; i >= 0; --i) {
        final String[] sqls = identity[i].ddl(vendor, DDL.Type.DROP);
        for (final String sql : sqls)
          statement.execute(sql);
      }
    }
  }
}