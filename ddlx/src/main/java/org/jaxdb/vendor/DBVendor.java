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

package org.jaxdb.vendor;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class DBVendor {
  private static int index;
  private static final DBVendor[] instances = new DBVendor[7];
  private static final Map<String,DBVendor> map = new HashMap<>();

  public static final DBVendor DB2 = new DBVendor("DB2", "com.ibm.db2.jcc.DB2Driver", new DB2Dialect());
  public static final DBVendor DERBY = new DBVendor("Derby", "org.apache.derby.jdbc.EmbeddedDriver", new DerbyDialect());
  public static final DBVendor MARIA_DB = new DBVendor("MariaDB", "org.mariadb.jdbc.Driver", new MariaDBDialect());
  public static final DBVendor MY_SQL = new DBVendor("MySQL", "com.mysql.cj.jdbc.Driver", new MySQLDialect());
  public static final DBVendor ORACLE = new DBVendor("Oracle", "oracle.jdbc.driver.OracleDriver", new OracleDialect());
  public static final DBVendor POSTGRE_SQL = new DBVendor("PostgreSQL", "org.postgresql.Driver", new PostgreSQLDialect());
  public static final DBVendor SQLITE = new DBVendor("SQLite", "org.sqlite.JDBC", new SQLiteDialect());

  public static DBVendor[] values() {
    return instances;
  }

  public static DBVendor valueOf(final DatabaseMetaData metaData) throws SQLException {
    final String vendorName = metaData.getDatabaseProductName().toLowerCase();
    for (final DBVendor vendor : DBVendor.values())
      if (vendorName.contains(vendor.name.toLowerCase()))
        return vendor;

    throw new UnsupportedOperationException("Unsupported DB vendor: " + metaData.getDatabaseProductName());
  }

  public static DBVendor valueOf(final String string) {
    return map.get(string);
  }

  private final String name;
  private final String driverClassName;
  private final Dialect dialect;
  private final int ordinal;

  private DBVendor(final String name, final String driverClassName, final Dialect dialect) {
    instances[this.ordinal = index++] = this;
    map.put(name, this);
    this.name = name;
    this.driverClassName = driverClassName;
    this.dialect = dialect;
  }

  public int ordinal() {
    return ordinal;
  }

  public void loadDriver() throws ClassNotFoundException {
    Class.forName(driverClassName);
  }

  public Dialect getDialect() {
    return dialect;
  }

  @Override
  public String toString() {
    return name;
  }
}