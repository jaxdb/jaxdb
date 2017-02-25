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
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.junit.Before;
import org.junit.internal.runners.statements.RunBefores;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.safris.commons.lang.Arrays;
import org.safris.commons.logging.Logging;
import org.safris.maven.common.Log;

public class VendorRunner extends BlockJUnit4ClassRunner {
  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface RunIn {
    Class<? extends Annotation>[] value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Test {
    Class<? extends Vendor>[] value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public static @interface Integration {
    Class<? extends Vendor>[] value();
  }

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

  public VendorRunner(final Class<?> klass) throws InitializationError {
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

  private final Set<FrameworkMethod> beforeClassMethodsRun = new HashSet<FrameworkMethod>();

  protected void run(final Class<? extends Vendor> vendorClass, final FrameworkMethod method, final Object test) throws Throwable {
    final RunIn runIn = method.getMethod().getAnnotation(RunIn.class);
    if (runIn == null || Arrays.contains(runIn.value(), integrationTest ? Integration.class : Test.class)) {
      if (method.getMethod().getParameterTypes().length > 0) {
        try (final Connection connection = getConnection(vendorClass)) {
          Log.info(VendorRunner.class.getSimpleName() + "::" + (integrationTest ? "Integration" : "Test") + "::" + vendorClass.getSimpleName());
          method.invokeExplosively(test, connection);
        }
      }
      else {
        if (test != null) {
          Log.info(VendorRunner.class.getSimpleName() + "::" + (integrationTest ? "Integration" : "Test") + "::" + vendorClass.getSimpleName());
          method.invokeExplosively(test);
          return;
        }
        else if (beforeClassMethodsRun.contains(method))
          return;

        synchronized (beforeClassMethodsRun) {
          if (beforeClassMethodsRun.contains(method))
            return;

          beforeClassMethodsRun.add(method);
          Log.info(VendorRunner.class.getSimpleName() + "::" + (integrationTest ? "Integration" : "Test") + "::" + vendorClass.getSimpleName());
          method.invokeExplosively(test);
        }
      }
    }
  }

  @Override
  protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        final Class<? extends Vendor> vendorClass = localVendor.get();
        try {
          run(vendorClass, method, test);
        }
        catch (final SQLException e) {
          throw flatten(e);
        }
      }
    };
  }

  private ThreadLocal<Class<? extends Vendor>> localVendor = new ThreadLocal<Class<? extends Vendor>>();

  private Statement evaluate(final List<FrameworkMethod> befores, final Object target, final Statement statement, final boolean transverse) {
    final Statement vendorStatement = new Statement() {
      @Override
      public void evaluate() throws Throwable {
        final Class<?> testClass = getTestClass().getJavaClass();
        final Test vendorTest = testClass.getAnnotation(Test.class);
        final Integration vendorIntegration = testClass.getAnnotation(Integration.class);
        if (vendorTest == null && vendorIntegration == null)
          throw new Exception("@" + Test.class.getSimpleName() + " or @" + Integration.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());

        if (!integrationTest && vendorTest != null)
          evaluateVendors(vendorTest.value(), befores, target, statement);

        if (integrationTest && vendorIntegration != null)
          evaluateVendors(vendorIntegration.value(), befores, target, statement);
      }
    };

    return befores.isEmpty() ? vendorStatement : new RunBefores(statement, befores, target) {
      @Override
      public void evaluate() throws Throwable {
        vendorStatement.evaluate();
      }
    };
  }

  private void evaluateVendors(final Class<? extends Vendor>[] vendorClasses, final List<FrameworkMethod> befores, final Object target, final Statement statement) throws Throwable {
    for (final Class<? extends Vendor> vendorClass : vendorClasses) {
      localVendor.set(vendorClass);
      if (!befores.isEmpty())
        for (final FrameworkMethod before : befores)
          VendorRunner.this.run(vendorClass, before, target);

      statement.evaluate();
    }
  }

  @Override
  protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
    return evaluate(getTestClass().getAnnotatedMethods(Before.class), target, statement, false);
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
        if (testClass != null) {
          final Class<? extends Vendor>[] vendorClasses;
          if (integrationTest) {
            final Integration vendorIntegration = testClass.getAnnotation(Integration.class);
            vendorClasses = vendorIntegration != null ? vendorIntegration.value() : null;
          }
          else {
            final Test vendorTest = testClass.getAnnotation(Test.class);
            vendorClasses = vendorTest != null ? vendorTest.value() : null;
          }

          if (vendorClasses != null)
            for (final Class<? extends Vendor> vendorClass : vendorClasses)
              getVendor(vendorClass).destroy();
        }
      }
    });
    super.run(notifier);
  }
}