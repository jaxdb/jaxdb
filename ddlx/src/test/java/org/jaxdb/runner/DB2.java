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

import org.jaxdb.vendor.DbVendor;

import com.ibm.db2.jcc.DB2BaseDataSource;

public class DB2 extends Vendor {
  public DB2() {
    this("com.ibm.db2.jcc.DB2Driver", "jdbc:db2://localhost:50001/jaxdb:user=jaxdb;password=jaxdb;traceLevel=" + DB2BaseDataSource.TRACE_ALL + ";");
  }

  public DB2(final String driverClassName, final String url) {
    super(driverClassName, url);
  }

  @Override
  public void close() {
  }

  @Override
  public DbVendor getDbVendor() {
    return DbVendor.DB2;
  }
}