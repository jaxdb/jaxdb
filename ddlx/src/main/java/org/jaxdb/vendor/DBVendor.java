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
import java.util.Arrays;
import java.util.function.Supplier;

import org.libj.lang.Strings;

public final class DBVendor {
  public static final DBVendor DB2;
  public static final DBVendor DERBY;
  public static final DBVendor MARIA_DB;
  public static final DBVendor MY_SQL;
  public static final DBVendor ORACLE;
  public static final DBVendor POSTGRE_SQL;
  public static final DBVendor SQLITE;

  private static byte index = 0;

  // FIXME: Driver class name should not be here.
  private static final DBVendor[] values = {
    DB2 = new DBVendor("DB2", DB2Dialect::new),
    DERBY = new DBVendor("Derby", DerbyDialect::new),
    MARIA_DB = new DBVendor("MariaDB", MariaDBDialect::new),
    MY_SQL = new DBVendor("MySQL", MySQLDialect::new),
    ORACLE = new DBVendor("Oracle", OracleDialect::new),
    POSTGRE_SQL = new DBVendor("PostgreSQL", PostgreSQLDialect::new),
    SQLITE = new DBVendor("SQLite", SQLiteDialect::new)
  };

  private static final String[] keys = new String[values.length];

  static {
    for (int i = 0; i < keys.length; ++i) // [A]
      keys[i] = values[i].key;
  }

  public static DBVendor valueOf(final String key) {
    final int index = Arrays.binarySearch(keys, key.toLowerCase());
    return index < 0 ? null : values[index];
  }

  public static DBVendor valueOf(final DatabaseMetaData metaData) throws SQLException {
    final String vendorName = metaData.getDatabaseProductName().toLowerCase();
    for (final DBVendor vendor : DBVendor.values())
      if (Strings.containsIgnoreCase(vendorName, vendor.name))
        return vendor;

    throw new IllegalArgumentException("Unsupported DB vendor: " + metaData.getDatabaseProductName());
  }

  public static DBVendor[] values() {
    return values;
  }

  private final byte ordinal;
  private final String name;
  private final String key;
  private final Supplier<Dialect> dialectSupplier;
  private Dialect dialect;

  private DBVendor(final String name, final Supplier<Dialect> dialectSupplier) {
    this.ordinal = index++;
    this.name = name;
    this.key = name.toLowerCase();
    this.dialectSupplier = dialectSupplier;
  }

  public byte ordinal() {
    return ordinal;
  }

  public Dialect getDialect() {
    return dialect == null ? dialect = dialectSupplier.get() : dialect;
  }

  @Override
  public String toString() {
    return name;
  }
}