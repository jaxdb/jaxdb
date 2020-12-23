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
import java.io.UncheckedIOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.List;

import org.jaxdb.ddlx.runner.VendorRunner;
import org.jaxdb.jsql.Registry;
import org.jaxdb.vendor.DBVendor;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.lang.Classes;
import org.libj.util.ArrayUtil;

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
      errors.add(new Exception("Method " + method.getName() + " must declare no parameters or one parameter of type: " + DBVendor.class.getName()));
  }

  @Override
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Vendor vendor, final Object target, final Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    Schema schema = method.getAnnotation(Schema.class);
    if (schema == null)
      schema = Classes.getAnnotationDeep(getTestClass().getJavaClass(), Schema.class);

    final org.jaxdb.ddlx.runner.Vendor vendorInstance = getVendor(vendor.value());
    if (schema != null) {
      for (final Class<? extends org.jaxdb.jsql.Schema> schemaClass : ArrayUtil.concat(schema.value(), (Class<? extends org.jaxdb.jsql.Schema>)null)) {
        Registry.threadLocal().registerPrepared(schemaClass, () -> {
          try {
            return vendorInstance.getConnection();
          }
          catch (final IOException e) {
            throw new UncheckedIOException(e);
          }
        });
      }
    }

    return method.getParameterTypes().length == 1 ? frameworkMethod.invokeExplosivelySuper(target, vendorInstance.getDBVendor()) : frameworkMethod.invokeExplosivelySuper(target);
  }
}