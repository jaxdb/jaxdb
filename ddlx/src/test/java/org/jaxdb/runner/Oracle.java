/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb.runner;

import java.util.TimeZone;

import org.jaxdb.vendor.DbVendor;

public class Oracle extends Vendor {
  public Oracle() {
    super("oracle.jdbc.driver.OracleDriver", "jdbc:oracle:thin:jaxdb/jaxdb@localhost:11521:xe");
    // NOTE: If TimeZone.setDefault() is not called:
    // NOTE: ORA-00604: error occurred at recursive SQL level 1
    // NOTE: ORA-01882: timezone region not found
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    // ALTER SYSTEM SET open_cursors=10000 SCOPE=BOTH;
    // ALTER SYSTEM SET processes=150 SCOPE=spfile;
    // GRANT ALL PRIVILEGES TO jaxdb IDENTIFIED BY jaxdb;
  }

  @Override
  public void close() {
  }

  @Override
  public DbVendor getDbVendor() {
    return DbVendor.ORACLE;
  }
}