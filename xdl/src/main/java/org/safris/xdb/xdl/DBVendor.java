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

package org.safris.xdb.xdl;

import org.safris.xdb.xdl.spec.DerbySQLSpec;
import org.safris.xdb.xdl.spec.MySQLSpec;
import org.safris.xdb.xdl.spec.PostgreSQLSpec;
import org.safris.xdb.xdl.spec.SQLSpec;

public enum DBVendor {
  DERBY("Derby", new DerbySQLSpec()),
  MY_SQL("MySQL", new MySQLSpec()),
  POSTGRE_SQL("PostgreSQL", new PostgreSQLSpec());

  public static DBVendor parse(final String value) {
    for (final DBVendor vendor : DBVendor.values())
      if (vendor.name.equals(value))
        return vendor;

    return null;
  }

  private final String name;
  private final SQLSpec sqlSpec;

  private DBVendor(final String name, final SQLSpec sqlSpec) {
    this.name = name;
    this.sqlSpec = sqlSpec;
  }

  public SQLSpec getSQLSpec() {
    return sqlSpec;
  }

  @Override
  public String toString() {
    return name;
  }
}