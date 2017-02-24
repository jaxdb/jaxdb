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

package org.safris.dbx.jsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;

import org.safris.dbx.ddlx.DBVendor;
import org.safris.dbx.jsql.exception.SQLExceptionCatalog;
import org.safris.dbx.jsql.exception.SQLInvalidSchemaNameException;

public abstract class Schema {
  private static final Set<Class<? extends Schema>> inited = new HashSet<Class<? extends Schema>>();

  protected static DBVendor getDBVendor(final Connection connection) throws SQLException {
    if (connection == null)
      return null;

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
      throw SQLExceptionCatalog.lookup(e);
    }

    return null;
  }

  protected static Connection getConnection(final Class<? extends Schema> schema) throws SQLException {
    final EntityDataSource dataSource = EntityRegistry.getDataSource(schema);
    if (dataSource == null)
      throw new SQLInvalidSchemaNameException("No " + EntityDataSource.class.getSimpleName() + " has been registered for " + schema.getName());

    try {
      final Connection connection = dataSource.getConnection();
      if (!inited.contains(schema)) {
        Serializer.getSerializer(getDBVendor(connection)).onRegister(connection);
        inited.add(schema);
      }

      return connection;
    }
    catch (final SQLException e) {
      throw SQLExceptionCatalog.lookup(e);
    }
  }
}