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

package org.safris.xdb.schema;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.safris.commons.logging.Logging;
import org.safris.maven.common.Log;
import org.safris.xdb.schema.vendor.Vendor;

public class VendorClassRunner extends BlockJUnit4ClassRunner {
  static {
    Logging.setLevel(Level.FINE);
  }

  private static Exception flatten(final SQLException e) {
    final StringBuilder builder = new StringBuilder();
    SQLException next = e;
    while ((next = next.getNextException()) != null)
      builder.append("\n").append(next.getMessage());

    final SQLException exception = new SQLException(e.getMessage() + builder);
    exception.setStackTrace(e.getStackTrace());
    return exception;
  }

  private final boolean integrationTest;

  public VendorClassRunner(final Class<?> klass) throws InitializationError {
    super(klass);
    this.integrationTest = "true".equals(System.getProperty("integrationTest"));
  }

  private final Map<Class<? extends Vendor>,Vendor> vendors = new HashMap<Class<? extends Vendor>,Vendor>();

  private Vendor getVendor(final Class<? extends Vendor> vendorClass) throws IOException, ReflectiveOperationException, SQLException {
    Vendor vendor = vendors.get(vendorClass);
    if (vendor == null) {
      vendors.put(vendorClass, vendor = vendorClass.newInstance());
      vendor.init();
    }

    return vendor;
  }

  protected final Connection getConnection(final Class<? extends Vendor> vendorClass) throws IOException, ReflectiveOperationException, SQLException {
    return getVendor(vendorClass).getConnection();
  }

  @Override
  protected void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
    final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    for (final FrameworkMethod method : methods) {
      method.validatePublicVoid(isStatic, errors);
      if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != Connection.class)
        errors.add(new Exception("Method " + method.getName() + " should have no parameters or a " + Connection.class.getName() + " parameter"));
    }
  }

  protected void run(final Class<? extends Vendor> vendorClass, final FrameworkMethod method, final Object test) throws Throwable {
    try (final Connection connection = getConnection(vendorClass)) {
      if (method.getMethod().getParameterTypes().length > 0)
        method.invokeExplosively(test, connection);
      else
        method.invokeExplosively(test);
    }
  }

  @Override
  protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        final Class<?> testClass = method.getMethod().getDeclaringClass();
        final VendorTest vendorTest = testClass.getAnnotation(VendorTest.class);
        final VendorIntegration vendorIntegration = testClass.getAnnotation(VendorIntegration.class);
        if (vendorTest == null && vendorIntegration == null)
          throw new Exception("@" + VendorTest.class.getSimpleName() + " or @" + VendorIntegration.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());

        if (!integrationTest && vendorTest != null) {
          for (final Class<? extends Vendor> vendorClass : vendorTest.value()) {
            Log.info("Testing vendor: " + vendorClass.getSimpleName());
            try {
              run(vendorClass, method, test);
            }
            catch (final SQLException e) {
              throw flatten(e);
            }
          }
        }

        if (integrationTest && vendorIntegration != null) {
          for (final Class<? extends Vendor> vendorClass : vendorIntegration.value()) {
            Log.info("Testing vendor: " + vendorClass.getSimpleName());
            try {
              run(vendorClass, method, test);
            }
            catch (final SQLException e) {
              throw flatten(e);
            }
          }
        }
      }
    };
  }

  @Override
  public void run(final RunNotifier notifier) {
    notifier.addListener(new RunListener() {
      private Class<?> testClass;

      @Override
      public void testFinished(final Description description) throws Exception {
        testClass = description.getTestClass();
      }

      @Override
      public void testRunFinished(final Result result) throws Exception {
        final Class<? extends Vendor>[] vendorClasses;
        if (integrationTest) {
          final VendorIntegration vendorIntegration = testClass.getAnnotation(VendorIntegration.class);
          vendorClasses = vendorIntegration != null ? vendorIntegration.value() : null;
        }
        else {
          final VendorTest vendorTest = testClass.getAnnotation(VendorTest.class);
          vendorClasses = vendorTest != null ? vendorTest.value() : null;
        }

        if (vendorClasses != null)
          for (final Class<? extends Vendor> vendorClass : vendorClasses)
            getVendor(vendorClass).destroy();
      }
    });
    super.run(notifier);
  }
}