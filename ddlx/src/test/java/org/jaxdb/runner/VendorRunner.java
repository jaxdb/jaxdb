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
import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

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
    VendorRunner.Vendor[] value();
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

  private static final Map<VendorRunner.Vendor,VendorRunner.Executor> vendorToExecutor = Collections.synchronizedMap(new HashMap<VendorRunner.Vendor,VendorRunner.Executor>() {
    private static final long serialVersionUID = -5606560108178675473L;

    @Override
    public VendorRunner.Executor get(final Object key) {
      final VendorRunner.Vendor vendor = (VendorRunner.Vendor)key;
      VendorRunner.Executor value = super.get(vendor);
      if (value == null)
        vendorToExecutor.put(vendor, value = new VendorRunner.Executor(vendor));

      return value;
    }
  });

  static class Executor {
    private final VendorRunner.Vendor vendor;
    private final ExecutorService executor;
    final Semaphore sempaphore;
    private final org.jaxdb.runner.Vendor vendorInstance;

    private Executor(final VendorRunner.Vendor vendor) {
      if (vendor.parallel() < 1)
        throw new RuntimeException("@" + VendorRunner.Vendor.class.getSimpleName() + "(parallel=" + vendor.parallel() + ") must be greater than 1");

      this.vendor = vendor;
      this.executor = Executors.newFixedThreadPool(vendor.parallel());
      this.sempaphore = new Semaphore(vendor.parallel());
      this.vendorInstance = org.jaxdb.runner.Vendor.getVendor(vendor.value());
    }

    public VendorRunner.Vendor getVendor() {
      return this.vendor;
    }

    Connection getConnection() throws IOException, SQLException {
      return vendorInstance.getConnection();
    }

    void execute(final Runnable command) {
      executor.execute(command);
    }

    @Override
    public String toString() {
      return vendor.toString() + " [permits=" + sempaphore.availablePermits() + "]";
    }
  }

  static VendorRunner.Executor[] getExecutors(final Class<?> testClass) {
    final VendorRunner.Vendors vendors = Classes.getAnnotationDeep(testClass, VendorRunner.Vendors.class);
    if (vendors != null) {
      final VendorRunner.Executor[] executors = new VendorRunner.Executor[vendors.value().length];
      for (int i = 0; i < executors.length; ++i)
        executors[i] = vendorToExecutor.get(vendors.value()[i]);

      return executors;
    }

    final VendorRunner.Vendor vendor = Classes.getAnnotationDeep(testClass, VendorRunner.Vendor.class);
    if (vendor != null)
      return new VendorRunner.Executor[] {vendorToExecutor.get(vendor)};

    throw new RuntimeException("@" + VendorRunner.Vendor.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());
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
    final VendorRunner.Executor[] executors = getExecutors(testClass);
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
              for (final VendorRunner.Executor executor : executors)
                value.add(new VendorFrameworkMethod(method, executor));
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

  /**
   * Returns the result of invoking this method on {@code target} with
   * parameters {@code params}. {@link InvocationTargetException}s thrown are
   * unwrapped, and their causes rethrown.
   *
   * @param frameworkMethod The {@link VendorFrameworkMethod}.
   * @param vendor The {@link VendorRunner.Vendor}.
   * @param target The target object to be invoked.
   * @param params The parameters to be passed to the invoked method.
   * @return The result of invoking this method on {@code target} with
   *         parameters {@code params}.
   * @throws Throwable If an exception occurs.
   */
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final VendorRunner.Vendor vendor, final Object target, final Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    if (method.getParameterTypes().length == 1) {
      try (final Connection connection = frameworkMethod.getExecutor().getConnection()) {
        logger.info(VendorRunner.toString(method) + " [" + vendor.value().getSimpleName() + "]");
        return frameworkMethod.invokeExplosivelySuper(target, connection);
      }
    }

    logger.info(VendorRunner.toString(method) + " [" + vendor.value().getSimpleName() + "]");
    return frameworkMethod.invokeExplosivelySuper(target);
  }

  protected class VendorFrameworkMethod extends FrameworkMethod {
    private final VendorRunner.Executor executor;

    VendorFrameworkMethod(final Method method, final VendorRunner.Executor executor) {
      super(method);
      this.executor = executor;
    }

    private void runChild(final Statement statement, final Description description, final RunNotifier notifier) {
      if (Classes.isAnnotationPresentDeep(getTestClass().getJavaClass(), Synchronized.class))
        invoke(statement, description, notifier);
      else
        executor.execute(() -> invoke(statement, description, notifier));
    }

    private void invoke(final Statement statement, final Description description, final RunNotifier notifier) {
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

    VendorRunner.Executor getExecutor() {
      return this.executor;
    }

    @Override
    public Object invokeExplosively(final Object target, final Object ... params) throws Throwable {
      return VendorRunner.this.invokeExplosively(this, executor.getVendor(), target, params);
    }

    final Object invokeExplosivelySuper(final Object target, final Object ... params) throws Throwable {
      try {
        executor.sempaphore.acquire();
        return super.invokeExplosively(target, params);
      }
      catch (final Throwable t) {
        final Unsupported unsupported = getMethod().getAnnotation(Unsupported.class);
        if (unsupported != null) {
          for (final Class<? extends org.jaxdb.runner.Vendor> unsupportedVendor : unsupported.value()) {
            if (unsupportedVendor == executor.getVendor().value()) {
              logger.warn("[" + executor.getVendor().value().getSimpleName() + "] does not support " + getMethod().getDeclaringClass().getSimpleName() + "." + VendorRunner.toString(getMethod()));
              return null;
            }
          }
        }

        DeferredLogger.flush();
        logger.error(executor.getVendor().value().getSimpleName());
        throw t instanceof SQLException ? flatten((SQLException)t) : t;
      }
      finally {
        executor.sempaphore.release();
      }
    }

    @Override
    public String getName() {
      return super.getName() + "[" + executor.getVendor().value().getSimpleName() + "]";
    }

    @Override
    public boolean equals(final Object obj) {
      return obj == this;
    }

    @Override
    public String toString() {
      return executor.toString();
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
      ((VendorFrameworkMethod)method).runChild(new Statement() {
        @Override
        public void evaluate() throws Throwable {
          methodBlock(method).evaluate();
        }
      }, description, notifier);
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

        final VendorRunner.Vendors vendors = Classes.getAnnotationDeep(testClass, VendorRunner.Vendors.class);
        if (vendors != null)
          for (final VendorRunner.Vendor vendor : vendors.value())
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