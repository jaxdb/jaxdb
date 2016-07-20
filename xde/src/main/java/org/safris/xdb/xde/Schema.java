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

import org.safris.xdb.xdl.DBVendor;

public abstract class Schema {
  protected static DBVendor getDBVendor(final Connection connection) throws SQLErrorSpecException {
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
      throw SQLErrorSpecException.lookup(e, null);
    }

    return null;
  }

  protected static Connection getConnection(final Class<? extends Schema> schema) throws SQLErrorSpecException {
    final EntityDataSource dataSource = EntityRegistry.getDataSource(schema);
    if (dataSource == null)
      throw new SQLErrorSpecException("No XDEDataSource has been registered for " + schema.getName());

    try {
      return dataSource.getConnection();
    }
    catch (final SQLException e) {
      throw SQLErrorSpecException.lookup(e, null);
    }
  }
}