/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.vendor.DBVendor;
import org.libj.sql.exception.SQLExceptions;
import org.libj.sql.exception.SQLInvalidSchemaNameException;
import org.libj.util.ConcurrentHashSet;

public abstract class Schema {
  private static final ConcurrentHashMap<String,ConcurrentHashSet<Class<? extends Schema>>> initialized = new ConcurrentHashMap<>();

  static DBVendor getDBVendor(final Connection connection) throws SQLException {
    if (connection == null)
      return null;

    try {
      final String url = connection.getMetaData().getURL();
      if (url.contains("jdbc:sqlite"))
        return DBVendor.SQLITE;

      if (url.contains("jdbc:derby"))
        return DBVendor.DERBY;

      if (url.contains("jdbc:mariadb"))
        return DBVendor.MARIA_DB;

      if (url.contains("jdbc:mysql"))
        return DBVendor.MY_SQL;

      if (url.contains("jdbc:oracle"))
        return DBVendor.ORACLE;

      if (url.contains("jdbc:postgresql"))
        return DBVendor.POSTGRE_SQL;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }

    return null;
  }

  static Connection getConnection(final Class<? extends Schema> schema, final String dataSourceId, final boolean autoCommit) throws SQLException {
    final Connector connector = Registry.getConnector(schema, dataSourceId);
    if (connector == null)
      throw new SQLInvalidSchemaNameException("No " + Connector.class.getName() + " registered for " + (schema == null ? null : schema.getName()) + ", id: " + dataSourceId);

    try {
      final Connection connection = connector.getConnection();
      final String url = connection.getMetaData().getURL();
      ConcurrentHashSet<Class<? extends Schema>> schemas = initialized.get(url);
      if (schemas == null) {
        initialized.put(url, schemas = new ConcurrentHashSet<>());
        schemas.add(schema);
        final Compiler compiler = Compiler.getCompiler(getDBVendor(connection));
        compiler.onConnect(connection);
        compiler.onRegister(connection);
        if (!connection.getAutoCommit())
          connection.commit();
      }
      else if (schemas.add(schema)) {
        final Compiler compiler = Compiler.getCompiler(getDBVendor(connection));
        compiler.onRegister(connection);
        if (!connection.getAutoCommit())
          connection.commit();
      }

      connection.setAutoCommit(autoCommit);
      return connection;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }
}