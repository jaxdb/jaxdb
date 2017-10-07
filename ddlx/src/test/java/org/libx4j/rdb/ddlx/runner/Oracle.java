/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.ddlx.runner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.TimeZone;

import org.lib4j.sql.ConnectionProxy;
import org.libx4j.rdb.vendor.DBVendor;

public class Oracle implements Vendor {
  @Override
  public DBVendor getDBVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  public synchronized void init() throws IOException, SQLException {
    // NOTE: If TimeZone.setDefault() is not called:
    // NOTE: ORA-00604: error occurred at recursive SQL level 1
    // NOTE: ORA-01882: timezone region not found
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//  ALTER SYSTEM SET open_cursors=10000 SCOPE=BOTH;
//  ALTER SYSTEM SET processes=150 SCOPE=spfile;
//  GRANT ALL PRIVILEGES TO rdb IDENTIFIED BY rdb;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return new ConnectionProxy(DriverManager.getConnection("jdbc:oracle:thin:rdb/rdb@localhost:1521:xe"));
  }

  @Override
  public void destroy() throws SQLException {
  }
}