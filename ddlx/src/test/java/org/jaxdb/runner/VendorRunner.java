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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.internal.AssumptionViolatedException;
import org.junit.internal.runners.model.EachTestNotifier;
import org.junit.runner.Description;
import org.junit.runner.Result;
import org.junit.runner.notification.RunListener;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkField;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.junit.runners.model.Statement;
import org.junit.runners.model.TestClass;
import org.libj.lang.Classes;
import org.libj.logging.DeferredLogger;
import org.libj.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class VendorRunner extends BlockJUnit4ClassRunner {
  static final Logger logger = LoggerFactory.getLogger(VendorRunner.class);

  static {
    DeferredLogger.defer(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), Level.DEBUG);
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Synchronized {
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Order {
    int value();
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Repeatable(Vendors.class)
  public @interface Vendor {
    Class<? extends org.jaxdb.runner.Vendor> value();
    int parallel() default 1;
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Vendors {
    Vendor[] value();
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Unsupported {
    Class<? extends org.jaxdb.runner.Vendor>[] value();
  }

  private static final Comparator<FrameworkMethod> orderComparator = (o1, o2) -> {
    final Order a1 = o1.getAnnotation(Order.class);
    if (a1 == null)
      return -1;

    final Order a2 = o2.getAnnotation(Order.class);
    if (a2 == null)
      return -1;

    return Integer.compare(a1.value(), a2.value());
  };

  private static Exception flatten(final SQLException e) {
    final StringBuilder builder = new StringBuilder();
    SQLException next = e;
    while ((next = next.getNextException()) != null)
      builder.append('\n').append(next.getMessage());

    final SQLException exception = new SQLException(e.getMessage() + builder);
    exception.setStackTrace(e.getStackTrace());
    return exception;
  }

  static class VendorExecutor {
    private final Vendor vendor;
    private final ExecutorService executor;
    private final boolean sync;

    private VendorExecutor(final Vendor vendor, final boolean sync) {
      if (sync && vendor.parallel() != 1)
        throw new RuntimeException("@" + Vendor.class.getSimpleName() + "{parallel=" + vendor.parallel() + "} must be 1 with @Synchronized");

      if (vendor.parallel() < 1)
        throw new RuntimeException("@" + Vendor.class.getSimpleName() + "{parallel=" + vendor.parallel() + "} must be greater than 1");

      this.vendor = vendor;
      this.executor = Executors.newFixedThreadPool(vendor.parallel());
      this.sync = sync;
    }
  }

  private static VendorExecutor[] getVendorExecutors(final Class<?> testClass) {
    final Vendors vendors = Classes.getAnnotationDeep(testClass, Vendors.class);
    final boolean sync = Classes.isAnnotationPresentDeep(testClass, Synchronized.class);
    if (vendors != null) {
      final VendorExecutor[] executors = new VendorExecutor[vendors.value().length];
      for (int i = 0; i < executors.length; ++i)
        executors[i] = new VendorExecutor(vendors.value()[i], sync);

      return executors;
    }

    final Vendor vendor = Classes.getAnnotationDeep(testClass, Vendor.class);
    if (vendor != null)
      return new VendorExecutor[] {new VendorExecutor(vendor, sync)};

    throw new RuntimeException("@" + Vendor.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());
  }

  private static String toString(final Method method) {
    final StringBuilder builder = new StringBuilder(method.getName());
    final int start = builder.length();
    if (method.getParameterTypes().length > 0) {
      for (final Class<?> parameterType : method.getParameterTypes())
        builder.append(',').append(parameterType.getName());

      builder.setCharAt(start, '(');
    }
    else {
      builder.append('(');
    }

    builder.append(')');
    return builder.toString();
  }

  public VendorRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  private static boolean runsTopToBottom(final Class<? extends Annotation> annotation) {
    return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
  }

  private static String getMethodKey(final Method method) {
    return method.getName() + Arrays.toString(method.getParameterTypes());
  }

  @Override
  protected TestClass createTestClass(final Class<?> testClass) {
    final VendorExecutor[] vendorExecutors = getVendorExecutors(testClass);
    return new TestClass(testClass) {
      @Override
      protected void scanAnnotatedMembers(final Map<Class<? extends Annotation>,List<FrameworkMethod>> methodsForAnnotations, final Map<Class<? extends Annotation>,List<FrameworkField>> fieldsForAnnotations) {
        super.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        for (final Map.Entry<Class<? extends Annotation>,List<FrameworkMethod>> entry : methodsForAnnotations.entrySet()) {
          final LinkedHashMap<String,Method> deduplicate = new LinkedHashMap<>();
          final Class<? extends Annotation> key = entry.getKey();
          final List<FrameworkMethod> value = entry.getValue();
          if (runsTopToBottom(key)) {
            for (int i = value.size() - 1; i >= 0; --i) {
              final Method method = value.get(i).getMethod();
              deduplicate.put(getMethodKey(method), method);
            }
          }
          else {
            for (int i = 0, len = value.size(); i < len; ++i) {
              final Method method = value.get(i).getMethod();
              deduplicate.put(getMethodKey(method), method);
            }
          }

          value.clear();
          for (final Iterator<Method> iterator = deduplicate.values().iterator(); iterator.hasNext();) {
            Method method = iterator.next();
            // See if `method` is masked by an override that is @Ignore(ed)
            method = Classes.getDeclaredMethodDeep(testClass, method.getName(), method.getParameterTypes());
            if (method.isAnnotationPresent(key) && !method.isAnnotationPresent(Ignore.class) && !method.getDeclaringClass().isAnnotationPresent(Ignore.class))
              for (final VendorExecutor vendorExecutor : vendorExecutors)
                value.add(new VendorFrameworkMethod(method, vendorExecutor));
          }

          value.sort(orderComparator);
        }
      }
    };
  }

  @Override
  protected final void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
    final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    for (final FrameworkMethod method : methods) {
      if (!isIgnored(method)) {
        method.validatePublicVoid(isStatic, errors);
        checkParameters(method, errors);
      }
    }
  }

  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != Connection.class)
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(method.getMethod().getParameterTypes(), ',', Class::getName) + ") must declare no parameters or one parameter of type: " + Connection.class.getName()));
  }

  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Vendor vendor, final Object target, final Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    if (method.getParameterTypes().length == 1) {
      try (final Connection connection = org.jaxdb.runner.Vendor.getVendor(vendor.value()).getConnection()) {
        logger.info(VendorRunner.toString(method) + " [" + vendor.value().getSimpleName() + "]");
        return frameworkMethod.invokeExplosivelySuper(target, connection);
      }
    }

    logger.info(VendorRunner.toString(method) + " [" + vendor.value().getSimpleName() + "]");
    return frameworkMethod.invokeExplosivelySuper(target);
  }

  protected class VendorFrameworkMethod extends FrameworkMethod {
    private final VendorExecutor vendorExecutor;

    public VendorFrameworkMethod(final Method method, final VendorExecutor vendorExecutor) {
      super(method);
      this.vendorExecutor = vendorExecutor;
    }

    @Override
    public Object invokeExplosively(final Object target, final Object ... params) throws Throwable {
      return VendorRunner.this.invokeExplosively(this, vendorExecutor.vendor, target, params);
    }

    public Object invokeExplosivelySuper(final Object target, final Object ... params) throws Throwable {
      try {
        return super.invokeExplosively(target, params);
      }
      catch (final Throwable t) {
        final Unsupported unsupported = getMethod().getAnnotation(Unsupported.class);
        if (unsupported != null) {
          for (final Class<? extends org.jaxdb.runner.Vendor> unsupportedVendor : unsupported.value()) {
            if (unsupportedVendor == vendorExecutor.vendor.value()) {
              logger.warn("[" + vendorExecutor.vendor.value().getSimpleName() + "] does not support " + getMethod().getDeclaringClass().getSimpleName() + "." + VendorRunner.toString(getMethod()));
              return null;
            }
          }
        }

        DeferredLogger.flush();
        logger.error(vendorExecutor.vendor.value().getSimpleName());
        throw t instanceof SQLException ? flatten((SQLException)t) : t;
      }
    }

    @Override
    public String getName() {
      return super.getName() + "[" + vendorExecutor.vendor.value().getSimpleName() + "]";
    }

    @Override
    public boolean equals(final Object obj) {
      return obj == this;
    }
  }

  private CountDownLatch latch;

  @Override
  protected List<FrameworkMethod> getChildren() {
    final List<FrameworkMethod> children = super.getChildren();
    latch = new CountDownLatch(children.size());
    return children;
  }

  @Override
  protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
    final Description description = describeChild(method);
    if (isIgnored(method)) {
      notifier.fireTestIgnored(description);
      latch.countDown();
    }
    else {
      final Statement statement = new Statement() {
        @Override
        public void evaluate() throws Throwable {
          methodBlock(method).evaluate();
        }
      };

      final VendorFrameworkMethod vendorMethod = (VendorFrameworkMethod)method;
      if (vendorMethod.vendorExecutor.sync)
        runChild(statement, description, notifier);
      else
        vendorMethod.vendorExecutor.executor.execute(() -> runChild(statement, description, notifier));
    }
  }

  private void runChild(final Statement statement, final Description description, final RunNotifier notifier) {
    final EachTestNotifier eachNotifier = new EachTestNotifier(notifier, description);
    eachNotifier.fireTestStarted();
    try {
      statement.evaluate();
    }
    catch (final AssumptionViolatedException e) {
      eachNotifier.addFailedAssumption(e);
    }
    catch (final Throwable e) {
      eachNotifier.addFailure(e);
    }
    finally {
      eachNotifier.fireTestFinished();
      latch.countDown();
    }
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
        if (testClass == null)
          return;

        final Vendors vendors = Classes.getAnnotationDeep(testClass, Vendors.class);
        if (vendors != null)
          for (final Vendor vendor : vendors.value())
            org.jaxdb.runner.Vendor.getVendor(vendor.value()).destroy();
      }
    });

    super.run(notifier);
    try {
      latch.await();
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
  }
}