/* Copyright (c) 2017 Seva Safris
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

package org.safris.dbb.ddlx.runner;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.safris.commons.sql.ConnectionProxy;

public class Oracle implements Vendor {
  @Override
  public synchronized void init() throws IOException, SQLException {
//  ALTER SYSTEM SET open_cursors=10000 SCOPE=BOTH;
//  ALTER SYSTEM SET processes=150 SCOPE=spfile;
//  GRANT ALL PRIVILEGES TO dbb IDENTIFIED BY dbb;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return new ConnectionProxy(DriverManager.getConnection("jdbc:oracle:thin:dbb/dbb@localhost:1521:xe"));
  }

  @Override
  public void destroy() throws SQLException {
  }
}