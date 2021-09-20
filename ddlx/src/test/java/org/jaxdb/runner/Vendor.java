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
  private static final ConcurrentHashMap<Class<? extends Vendor>,Vendor> classToInstance = new ConcurrentHashMap<>();

  static synchronized Vendor getVendor(final Class<? extends Vendor> vendorClass) {
    try {
      Vendor vendor = classToInstance.get(vendorClass);
      if (vendor == null)
        classToInstance.put(vendorClass, vendor = vendorClass.getDeclaredConstructor().newInstance());

      return vendor;
    }
    catch (final IllegalAccessException | InstantiationException | NoSuchMethodException e) {
      throw new RuntimeException(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new RuntimeException(e.getCause());
    }
  }

  private final String driverClassName;
  private final String url;

  public Vendor(final String driverClassName, final String url) {
    this.driverClassName = driverClassName;
    this.url = url;
    try {
      Class.forName(driverClassName);
    }
    catch (final ClassNotFoundException e) {
      throw new IllegalStateException(e);
    }
  }

  public final String getUrl() {
    return url;
  }

  /**
   * Returns the {@link Connection} for this {@link Vendor} instance.
   *
   * @return The {@link Connection} for this {@link Vendor} instance.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  public Connection getConnection() throws IOException, SQLException {
    final String url = getUrl();
    try {
      return new AuditConnection(DriverManager.getConnection(url));
    }
    catch (final SQLException e1) {
      if (!"08001".equals(e1.getSQLState()))
        throw e1;

      try {
        Class.forName(driverClassName);
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

  /**
   * Rolls back the provided {@link Connection}.
   *
   * @param connection The {@link Connection} to roll back.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  public void rollback(final Connection connection) throws IOException, SQLException {
    connection.rollback();
  }

  public abstract DBVendor getDBVendor();
  public abstract void destroy() throws IOException, SQLException;

  @Override
  public int hashCode() {
    return getDBVendor().hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this;
  }
}