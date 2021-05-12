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

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.vendor.DBVendor;
import org.libj.sql.AuditConnection;

public abstract class Vendor {
  private static final ConcurrentHashMap<Class<? extends org.jaxdb.runner.Vendor>,org.jaxdb.runner.Vendor> vendorsClasses = new ConcurrentHashMap<>();

  static synchronized org.jaxdb.runner.Vendor getVendor(final Class<? extends org.jaxdb.runner.Vendor> vendorClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
    org.jaxdb.runner.Vendor vendor = vendorsClasses.get(vendorClass);
    if (vendor == null)
      vendorsClasses.put(vendorClass, vendor = vendorClass.getDeclaredConstructor().newInstance());

    return vendor;
  }

  private final String url;

  public Vendor(final String url) {
    this.url = url;
    try {
      getDBVendor().loadDriver();
    }
    catch (final ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  public final String getUrl() {
    return url;
  }

  public Connection getConnection() throws IOException, SQLException {
    final String url = getUrl();
    try {
      return new AuditConnection(DriverManager.getConnection(url));
    }
    catch (final SQLException e1) {
      if (!"08001".equals(e1.getSQLState()))
        throw e1;

      try {
        getDBVendor().loadDriver();
      }
      catch (final ClassNotFoundException e2) {
        e1.addSuppressed(e2);
        throw e1;
      }

      try {
        return new AuditConnection(DriverManager.getConnection(url));
      }
      catch (final SQLException e2) {
        e1.addSuppressed(e2);
        throw e1;
      }
    }
  }

  public abstract DBVendor getDBVendor();
  public abstract void destroy() throws IOException, SQLException;
}