/* Copyright (c) 2017 OpenJAX
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

package org.openjax.maven.plugin.rdb.jsql.runner;

import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.fastjax.util.Arrays;
import org.openjax.rdb.ddlx.runner.Vendor;
import org.openjax.rdb.ddlx.runner.VendorRunner;
import org.openjax.rdb.jsql.Connector;
import org.openjax.rdb.jsql.Registry;
import org.openjax.rdb.vendor.DBVendor;

public class VendorSchemaRunner extends VendorRunner {
  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Schema {
    Class<? extends org.openjax.rdb.jsql.Schema>[] value();
  }

  public VendorSchemaRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  @Override
  protected void checkParameters(final FrameworkMethod method, final List<Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != DBVendor.class)
      errors.add(new Exception("Method " + method.getName() + " accepts a " + DBVendor.class.getName() + " parameter"));
  }

  @Override
  protected void run(final Class<? extends Vendor> vendorClass, final FrameworkMethod method, final Object test) throws Throwable {
    Schema entityClass = method.getMethod().getAnnotation(Schema.class);
    if (entityClass == null)
      entityClass = method.getMethod().getDeclaringClass().getAnnotation(Schema.class);

    final Vendor vendor = getVendor(vendorClass);
    if (entityClass != null) {
      for (final Class<? extends org.openjax.rdb.jsql.Schema> schemaClass : Arrays.concat(entityClass.value(), (Class<? extends org.openjax.rdb.jsql.Schema>)null)) {
        Registry.registerPrepared(schemaClass, new Connector() {
          @Override
          public Connection getConnection() throws SQLException {
            try {
              return vendor.getConnection();
            }
            catch (final IOException e) {
              throw new SQLException(e);
            }
          }
        });
      }
    }

    if (method.getMethod().getParameterTypes().length == 1)
      method.invokeExplosively(test, vendor.getDBVendor());
    else
      method.invokeExplosively(test);
  }
}