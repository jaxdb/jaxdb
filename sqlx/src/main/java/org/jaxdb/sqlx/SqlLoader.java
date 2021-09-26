/* Copyright (c) 2021 JAX-DB
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

package org.jaxdb.sqlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorBase;

abstract class SqlLoader extends DBVendorBase {
  final Connection connection;

  SqlLoader(final Connection connection) throws SQLException {
    super(DBVendor.valueOf(connection.getMetaData()));
    this.connection = connection;
  }

  static class TableToColumnToIncrement extends HashMap<String,Map<String,Integer>> {
    @Override
    public Map<String,Integer> get(final Object key) {
      final String str = (String)key;
      Map<String,Integer> value = super.get(str);
      if (value == null)
        super.put(str, value = new HashMap<>(1));

      return value;
    }
  }
}