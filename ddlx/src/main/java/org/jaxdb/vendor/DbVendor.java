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
import java.util.HashMap;
import java.util.function.Supplier;

public final class DbVendor {
  public static final DbVendor DB2;
  public static final DbVendor DERBY;
  public static final DbVendor MARIA_DB;
  public static final DbVendor MY_SQL;
  public static final DbVendor ORACLE;
  public static final DbVendor POSTGRE_SQL;
  public static final DbVendor SQLITE;

  private static byte index = 0;

  private static final DbVendor[] values = {
    DB2 = new DbVendor("DB2", DB2Dialect::new),
    DERBY = new DbVendor("Derby", DerbyDialect::new),
    MARIA_DB = new DbVendor("MariaDB", MariaDBDialect::new),
    MY_SQL = new DbVendor("MySQL", MySQLDialect::new),
    ORACLE = new DbVendor("Oracle", OracleDialect::new),
    POSTGRE_SQL = new DbVendor("PostgreSQL", PostgreSQLDialect::new),
    SQLITE = new DbVendor("SQLite", SQLiteDialect::new)
  };

  private static final String[] keys = new String[values.length];

  static {
    for (int i = 0, i$ = keys.length; i < i$; ++i) // [A]
      keys[i] = values[i].key;
  }

  public static DbVendor valueOf(final String key) {
    final int index = Arrays.binarySearch(keys, key.toLowerCase());
    return index < 0 ? null : values[index];
  }

  private static final HashMap<String,DbVendor> productNameToDbVendor = new HashMap<>(2);

  public static DbVendor valueOf(final DatabaseMetaData metaData) throws SQLException {
    final String databaseProductName = metaData.getDatabaseProductName();
    DbVendor dbVendor = productNameToDbVendor.get(databaseProductName);
    if (dbVendor != null)
      return dbVendor;

    final String vendorName = databaseProductName.toLowerCase();
    for (final DbVendor vendor : DbVendor.values()) { // [A]
      if (vendorName.contains(vendor.key)) {
        productNameToDbVendor.put(databaseProductName, dbVendor = vendor);
        return dbVendor;
      }
    }

    throw new IllegalArgumentException("Unsupported DB vendor: " + databaseProductName);
  }

  public static DbVendor[] values() {
    return values;
  }

  private final byte ordinal;
  private final String name;
  private final String key;
  private final Supplier<Dialect> dialectSupplier;
  private Dialect dialect;

  private DbVendor(final String name, final Supplier<Dialect> dialectSupplier) {
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