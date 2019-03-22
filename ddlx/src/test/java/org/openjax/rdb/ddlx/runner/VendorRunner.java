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

package org.openjax.rdb.ddlx.runner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import org.openjax.standard.logging.DeferredLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class VendorRunner extends BlockJUnit4ClassRunner {
  protected static final Logger logger = LoggerFactory.getLogger(VendorRunner.class);

  static {
    DeferredLogger.defer(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), Level.DEBUG);
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Order {
    int value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Vendor {
    Class<? extends org.openjax.rdb.ddlx.runner.Vendor>[] value();
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Unsupported {
    Class<? extends org.openjax.rdb.ddlx.runner.Vendor>[] value();
  }

  private static Exception flatten(final SQLException e) {
    final StringBuilder builder = new StringBuilder();
    SQLException next = e;
    while ((next = next.getNextException()) != null)
      builder.append('\n').append(next.getMessage());

    final SQLException exception = new SQLException(e.getMessage() + builder);
    exception.setStackTrace(e.getStackTrace());
    return exception;
  }

  public VendorRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  private final Map<Class<? extends org.openjax.rdb.ddlx.runner.Vendor>,org.openjax.rdb.ddlx.runner.Vendor> vendors = new HashMap<>();

  protected org.openjax.rdb.ddlx.runner.Vendor getVendor(final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> vendorClass) throws IllegalAccessException, InstantiationException, InvocationTargetException, IOException, NoSuchMethodException, SQLException {
    org.openjax.rdb.ddlx.runner.Vendor vendor = vendors.get(vendorClass);
    if (vendor == null) {
      vendors.put(vendorClass, vendor = vendorClass.getDeclaredConstructor().newInstance());
      vendor.init();
    }

    return vendor;
  }

  @Override
  protected void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
    final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    for (final FrameworkMethod method : methods) {
      if (!isIgnored(method)) {
        method.validatePublicVoid(isStatic, errors);
        checkParameters(method, errors);
      }
    }
  }

  protected void checkParameters(final FrameworkMethod method, final List<Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != Connection.class)
      errors.add(new Exception("Method " + method.getName() + " must declare no parameters or one parameter of type: " + Connection.class.getName()));
  }

  private final Set<FrameworkMethod> beforeClassMethodsRun = new HashSet<>();

  protected void run(final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> vendorClass, final FrameworkMethod method, final Object test) throws Throwable {
    if (isIgnored(method))
      return;

    if (method.getMethod().getParameterTypes().length > 0) {
      try (final Connection connection = getVendor(vendorClass).getConnection()) {
        logger.info(method.getMethod().getName() + "() " + vendorClass.getSimpleName());
        method.invokeExplosively(test, connection);
      }
    }
    else {
      if (test != null) {
        logger.info(method.getMethod().getName() + "() " + vendorClass.getSimpleName());
        method.invokeExplosively(test);
        return;
      }
      else if (beforeClassMethodsRun.contains(method)) {
        return;
      }

      synchronized (beforeClassMethodsRun) {
        if (beforeClassMethodsRun.contains(method))
          return;

        beforeClassMethodsRun.add(method);
        logger.info(method.getMethod().getName() + "() " + vendorClass.getSimpleName());
        method.invokeExplosively(test);
      }
    }
  }

  private static final Comparator<FrameworkMethod> orderComparator = new Comparator<FrameworkMethod>() {
    @Override
    public int compare(final FrameworkMethod o1, final FrameworkMethod o2) {
      final Order a1 = o1.getAnnotation(Order.class);
      if (a1 == null)
        return -1;

      final Order a2 = o2.getAnnotation(Order.class);
      if (a2 == null)
        return -1;

      return Integer.compare(a1.value(), a2.value());
    }
  };

  @Override
  protected List<FrameworkMethod> computeTestMethods() {
    final List<FrameworkMethod> methods = new ArrayList<>(super.computeTestMethods());
    methods.sort(orderComparator);
    return methods;
  }

  @Override
  protected Statement methodInvoker(final FrameworkMethod method, final Object test) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        DeferredLogger.clear();
        final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> vendorClass = localVendor.get();
        try {
          run(vendorClass, method, test);
        }
        catch (final Throwable e) {
          DeferredLogger.flush();
          final Unsupported unsupported = method.getMethod().getAnnotation(Unsupported.class);
          if (unsupported != null) {
            for (final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> unsupportedVendor : unsupported.value()) {
              if (unsupportedVendor == vendorClass) {
                logger.warn(vendorClass.getSimpleName() + " does not support " + method.getMethod().getDeclaringClass().getSimpleName() + "." + method.getMethod().getName() + "()");
                return;
              }
            }
          }

          logger.error(vendorClass.getSimpleName());
          throw e instanceof SQLException ? flatten((SQLException)e) : e;
        }
      }
    };
  }

  private ThreadLocal<Class<? extends org.openjax.rdb.ddlx.runner.Vendor>> localVendor = new ThreadLocal<>();

  private Statement evaluate(final List<FrameworkMethod> befores, final Object target, final Statement statement) {
    final Statement vendorStatement = new Statement() {
      @Override
      public void evaluate() throws Throwable {
        final Class<?> testClass = getTestClass().getJavaClass();
        final Vendor vendor = testClass.getAnnotation(Vendor.class);
        if (vendor == null)
          throw new Exception("@" + Vendor.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());

        evaluateVendors(vendor.value(), befores, target, statement);
      }
    };

    return befores.isEmpty() ? vendorStatement : new RunBefores(statement, befores, target) {
      @Override
      public void evaluate() throws Throwable {
        vendorStatement.evaluate();
      }
    };
  }

  private void evaluateVendors(final Class<? extends org.openjax.rdb.ddlx.runner.Vendor>[] vendorClasses, final List<FrameworkMethod> befores, final Object target, final Statement statement) throws Throwable {
    for (final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> vendorClass : vendorClasses) {
      localVendor.set(vendorClass);
      if (!befores.isEmpty())
        for (final FrameworkMethod before : befores)
          VendorRunner.this.run(vendorClass, before, target);

      statement.evaluate();
    }
  }

  @Override
  protected Statement withBefores(final FrameworkMethod method, final Object target, final Statement statement) {
    return evaluate(getTestClass().getAnnotatedMethods(Before.class), target, statement);
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
          final Vendor vendorTest = testClass.getAnnotation(Vendor.class);
          final Class<? extends org.openjax.rdb.ddlx.runner.Vendor>[] vendorClasses = vendorTest != null ? vendorTest.value() : null;
          if (vendorClasses != null)
            for (final Class<? extends org.openjax.rdb.ddlx.runner.Vendor> vendorClass : vendorClasses)
              getVendor(vendorClass).destroy();
        }
      }
    });
    super.run(notifier);
  }
}