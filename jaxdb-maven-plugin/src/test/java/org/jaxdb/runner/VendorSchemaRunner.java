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
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.List;

import org.jaxdb.ddlx.runner.VendorRunner;
import org.jaxdb.jsql.Registry;
import org.jaxdb.vendor.DBVendor;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.util.ArrayUtil;
import org.libj.util.function.Throwing;

public class VendorSchemaRunner extends VendorRunner {
  @Target({ElementType.TYPE, ElementType.METHOD})
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Schema {
    Class<? extends org.jaxdb.jsql.Schema>[] value();
  }

  public VendorSchemaRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  @Override
  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != DBVendor.class)
      errors.add(new Exception("Method " + method.getName() + " accepts a " + DBVendor.class.getName() + " parameter"));
  }

  @Override
  protected void run(final Class<? extends org.jaxdb.ddlx.runner.Vendor> vendorClass, final FrameworkMethod method, final Object test) throws Throwable {
    Schema entityClass = method.getMethod().getAnnotation(Schema.class);
    if (entityClass == null)
      entityClass = getTestClass().getJavaClass().getAnnotation(Schema.class);

    final org.jaxdb.ddlx.runner.Vendor vendor = getVendor(vendorClass);
    if (entityClass != null) {
      for (final Class<? extends org.jaxdb.jsql.Schema> schemaClass : ArrayUtil.concat(entityClass.value(), (Class<? extends org.jaxdb.jsql.Schema>)null)) {
        Registry.registerPrepared(schemaClass, () -> {
          try {
            return vendor.getConnection();
          }
          catch (final IOException e) {
            Throwing.rethrow(e);
            return null;
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