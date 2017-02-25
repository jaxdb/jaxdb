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

package org.safris.dbb.vendor;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class DBVendor {
  private static int index = 0;
  private static final DBVendor[] instances = new DBVendor[3];
  private static final Map<String,DBVendor> map = new HashMap<String,DBVendor>();

  public static final DBVendor DERBY = new DBVendor("Derby", new DerbyDialect());
  public static final DBVendor MY_SQL = new DBVendor("MySQL", new MySQLDialect());
  public static final DBVendor POSTGRE_SQL = new DBVendor("PostgreSQL", new PostgreSQLDialect());

  public static DBVendor[] values() {
    return instances;
  }

  public static DBVendor valueOf(final DatabaseMetaData metaData) throws SQLException {
    final String vendorName = metaData.getDatabaseProductName().toLowerCase();
    for (final DBVendor vendor : DBVendor.values())
      if (vendorName.contains(vendor.name.toLowerCase()))
        return vendor;

    return null;
  }

  public static DBVendor valueOf(final String string) {
    return map.get(string);
  }

  private final String name;
  private final Dialect dialect;
  private final int ordinal;

  private DBVendor(final String name, final Dialect dialect) {
    instances[this.ordinal = index++] = this;
    map.put(name, this);
    this.name = name;
    this.dialect = dialect;
  }

  public int ordinal() {
    return ordinal;
  }

  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public String toString() {
    return name;
  }
}