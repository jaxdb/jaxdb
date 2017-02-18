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

package org.safris.xdb.schema.vendor;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.postgresql.Driver;
import org.safris.commons.sql.ConnectionProxy;

@SuppressWarnings("unused")
public class PostgreSQL implements Vendor {
  @Override
  public synchronized void init() throws IOException, SQLException {
//    CREATE USER mycompany WITH PASSWORD 'mycompany';
//    CREATE DATABASE mycompany;
//    GRANT ALL PRIVILEGES ON DATABASE mycompany TO mycompany;
    new Driver();
  }

  @Override
  public Connection getConnection() throws SQLException {
    return new ConnectionProxy(DriverManager.getConnection("jdbc:postgresql://localhost/xdb?user=xdb&password=xdb"));
  }

  @Override
  public void destroy() throws SQLException {
  }
}