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

import org.jaxdb.vendor.DBVendor;

// CREATE DATABASE jaxdb;
// CREATE USER jaxdb IDENTIFIED BY 'jaxdb';
// GRANT ALL ON jaxdb.* TO 'jaxdb'@'%';
public class MySQL extends Vendor {
  public MySQL() {
    // NOTE: for some reason, "127.0.0.1" works if you tunnel the local 3306 port to a remote machine, and "localhost" fails to connect
    this("com.mysql.cj.jdbc.Driver", "jdbc:mysql://127.0.0.1:13306/jaxdb?user=jaxdb&password=jaxdb&useSSL=false&serverTimezone=UTC");
  }

  public MySQL(final String driverClassName, final String url) {
    super(driverClassName, url);
  }

  @Override
  public void close() {
  }

  @Override
  public DBVendor getDBVendor() {
    return DBVendor.MY_SQL;
  }
}