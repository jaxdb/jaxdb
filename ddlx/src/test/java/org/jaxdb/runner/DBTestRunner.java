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
import java.util.RandomAccess;
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
import org.libj.lang.Strings;
import org.libj.logging.DeferredLogger;
import org.libj.test.FailFastRunListener;
import org.libj.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

public class DBTestRunner extends BlockJUnit4ClassRunner {
  static final Logger logger = LoggerFactory.getLogger(DBTestRunner.class);

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Config {
    boolean sync() default false;
    boolean deferLog() default false;
    boolean failFast() default false;
    boolean cache() default false;
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Spec {
    int order() default 1;
    int cardinality() default 1;
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  @Repeatable(DBs.class)
  public @interface DB {
    Class<? extends Vendor> value();
    int parallel() default 1;
  }

  @Target(ElementType.TYPE)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface DBs {
    DB[] value();
  }

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Unsupported {
    Class<? extends Vendor>[] value();
  }

  private static final Comparator<FrameworkMethod> orderComparator = (o1, o2) -> {
    final Spec a1 = o1.getAnnotation(Spec.class);
    if (a1 == null)
      return -1;

    final Spec a2 = o2.getAnnotation(Spec.class);
    if (a2 == null)
      return -1;

    return Integer.compare(a1.order(), a2.order());
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
      for (final Class<?> parameterType : method.getParameterTypes()) // [A]
        builder.append(',').append(parameterType.getName());

      builder.setCharAt(start, '(');
    }
    else {
      builder.append('(');
    }

    builder.append(')');
    return builder.toString();
  }

  private static final Map<DB,Executor> vendorToExecutor = Collections.synchronizedMap(new HashMap<DB,Executor>() {
    @Override
    public Executor get(final Object key) {
      final DB db = (DB)key;
      Executor value = super.get(db);
      if (value == null)
        vendorToExecutor.put(db, value = new Executor(db));

      return value;
    }
  });

  static class Executor {
    private final DB db;
    private final ExecutorService executor;
    final Semaphore sempaphore;
    private final Vendor vendorInstance;

    private Executor(final DB db) {
      if (db.parallel() < 1)
        throw new RuntimeException("@" + DB.class.getSimpleName() + "(parallel=" + db.parallel() + ") must be greater than 1");

      this.db = db;
      this.executor = Executors.newFixedThreadPool(db.parallel());
      this.sempaphore = new Semaphore(db.parallel());
      this.vendorInstance = Vendor.getVendor(db.value());
    }

    public DB getDB() {
      return db;
    }

    public Vendor getVendor() {
      return vendorInstance;
    }

    Connection getConnection() throws IOException, SQLException {
      return vendorInstance.getConnection();
    }

    void execute(final Runnable command) {
      executor.execute(command);
    }

    @Override
    public String toString() {
      return db + " [permits=" + sempaphore.availablePermits() + "]";
    }
  }

  private static boolean matches(final DB db, final String[] testDBs) {
    for (final String testDB : testDBs)
      if (testDB.equals(db.value().getSimpleName().toLowerCase()))
        return true;

    return false;
  }

  private static Executor[] getExecutors(final String[] testDBs, final DB[] dbs, final int index, final int depth) {
    if (index == dbs.length)
      return new Executor[depth];

    final DB db = dbs[index];
    if (!matches(db, testDBs))
      return getExecutors(testDBs, dbs, index + 1, depth);

    final Executor[] executors = getExecutors(testDBs, dbs, index + 1, depth + 1);
    executors[depth] = vendorToExecutor.get(db);
    return executors;
  }

  private static String[] getPropertyValues(final String property) {
    if (property == null || property.length() == 0)
      return null;

    final String[] testDBs = Strings.split(property, ',');
    for (int i = 0, i$ = testDBs.length; i < i$; ++i)
      testDBs[i] = testDBs[i].toLowerCase();

    return testDBs;
  }

  private static final String propertyName = "jaxdb.jsql.test.dbs";

  static Executor[] getExecutors(final Class<?> testClass) {
    final DBs dbs$ = Classes.getAnnotationDeep(testClass, DBs.class);
    final String property = System.getProperty(propertyName);
    final String[] testDBs = getPropertyValues(property);
    final Annotation annotation;
    if (dbs$ == null) {
      final DB db = Classes.getAnnotationDeep(testClass, DB.class);
      annotation = db;
      if (db != null && (testDBs == null || matches(db, testDBs)))
        return new Executor[] {vendorToExecutor.get(db)};
    }
    else {
      annotation = dbs$;
      final DB[] dbs = dbs$.value();
      if (testDBs == null) {
        final Executor[] executors = new Executor[dbs.length];
        for (int i = 0, i$ = executors.length; i < i$; ++i)
          executors[i] = vendorToExecutor.get(dbs[i]);

        return executors;
      }

      final Executor[] executors = getExecutors(testDBs, dbs, 0, 0);
      if (executors.length > 0)
        return executors;
    }

    if (annotation == null)
      throw new RuntimeException("@" + DB.class.getSimpleName() + " annotation is required on class " + testClass.getSimpleName());

    throw new RuntimeException("-D" + propertyName + "=" + property + " does not match annotation values: " + annotation);
  }

  private static final Config findConfig(final Class<?> cls) {
    if (cls == null)
      return null;

    Config config = cls.getAnnotation(Config.class);
    if (config != null)
      return config;

    if (cls.getEnclosingClass() != null && (config = findConfig(cls.getEnclosingClass())) != null)
      return config;

    return findConfig(cls.getSuperclass());
  }

  private final boolean sync;
  private final boolean failFast;
  final boolean cache;

  public DBTestRunner(final Class<?> cls) throws InitializationError {
    super(cls);
    final Config config = findConfig(cls);

    final boolean deferLog;
    if (config != null) {
      this.sync = config.sync();
      this.failFast = config.failFast();
      this.cache = config.cache();
      deferLog = config.deferLog();
    }
    else {
      this.sync = false;
      this.failFast = false;
      this.cache = false;
      deferLog = true;
    }

    if (deferLog)
      DeferredLogger.defer(LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME), Level.DEBUG);
  }

  private static boolean runsTopToBottom(final Class<? extends Annotation> annotation) {
    return annotation.equals(Before.class) || annotation.equals(BeforeClass.class);
  }

  private static String getMethodKey(final Method method) {
    return method.getName() + Arrays.toString(method.getParameterTypes());
  }

  @Override
  protected TestClass createTestClass(final Class<?> testClass) {
    final Executor[] executors = getExecutors(testClass);
    return new TestClass(testClass) {
      @Override
      protected void scanAnnotatedMembers(final Map<Class<? extends Annotation>,List<FrameworkMethod>> methodsForAnnotations, final Map<Class<? extends Annotation>,List<FrameworkField>> fieldsForAnnotations) {
        super.scanAnnotatedMembers(methodsForAnnotations, fieldsForAnnotations);
        if (methodsForAnnotations.size() > 0) {
          for (final Map.Entry<Class<? extends Annotation>,List<FrameworkMethod>> entry : methodsForAnnotations.entrySet()) { // [S]
            final LinkedHashMap<String,Method> deduplicate = new LinkedHashMap<>();
            final Class<? extends Annotation> key = entry.getKey();
            final List<FrameworkMethod> value = entry.getValue();
            if (!(value instanceof RandomAccess))
              throw new IllegalStateException();

            if (runsTopToBottom(key)) {
              for (int i = value.size() - 1; i >= 0; --i) { // [RA]
                final Method method = value.get(i).getMethod();
                deduplicate.put(getMethodKey(method), method);
              }
            }
            else {
              for (int i = 0, i$ = value.size(); i < i$; ++i) { // [RA]
                final Method method = value.get(i).getMethod();
                deduplicate.put(getMethodKey(method), method);
              }
            }

            value.clear();
            if (deduplicate.size() > 0) {
              for (final Iterator<Method> iterator = deduplicate.values().iterator(); iterator.hasNext();) { // [I]
                Method method = iterator.next();
                // See if `method` is masked by an override that is @Ignore(ed)
                method = Classes.getDeclaredMethodDeep(testClass, method.getName(), method.getParameterTypes());
                if (method.isAnnotationPresent(key) && !method.isAnnotationPresent(Ignore.class) && !method.getDeclaringClass().isAnnotationPresent(Ignore.class))
                  for (final Executor executor : executors) // [A]
                    value.add(new VendorFrameworkMethod(method, executor));
              }

              value.sort(orderComparator);
            }
          }
        }
      }
    };
  }

  @Override
  protected final void validatePublicVoidNoArgMethods(final Class<? extends Annotation> annotation, final boolean isStatic, final List<Throwable> errors) {
    final List<FrameworkMethod> methods = getTestClass().getAnnotatedMethods(annotation);
    if (!(methods instanceof RandomAccess))
      throw new IllegalStateException();

    for (int i = 0, i$ = methods.size(); i < i$; ++i) { // [RA]
      final FrameworkMethod method = methods.get(i);
      if (!isIgnored(method)) {
        method.validatePublicVoid(isStatic, errors);
        checkParameters(method, errors);
      }
    }
  }

  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && !Connection.class.isAssignableFrom(method.getMethod().getParameterTypes()[0]))
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(method.getMethod().getParameterTypes(), ',', Class::getName) + ") must declare no parameters or one parameter of type: " + Connection.class.getName()));
  }

  /**
   * Returns the result of invoking this method on {@code target} with parameters {@code params}. {@link InvocationTargetException}s
   * thrown are unwrapped, and their causes rethrown.
   *
   * @param frameworkMethod The {@link VendorFrameworkMethod}.
   * @param target The target object to be invoked.
   * @param params The parameters to be passed to the invoked method.
   * @return The result of invoking this method on {@code target} with parameters {@code params}.
   * @throws Throwable If an exception occurs.
   */
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Object target, final Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    final Class<? extends Vendor> vendor = frameworkMethod.getExecutor().getDB().value();
    if (method.getParameterTypes().length == 1) {
      try (final Connection connection = frameworkMethod.getExecutor().getConnection()) {
        if (logger.isInfoEnabled()) logger.info(toString(method) + " [" + vendor.getSimpleName() + "]");
        return frameworkMethod.invokeExplosivelySuper(target, connection);
      }
    }

    if (logger.isInfoEnabled()) logger.info(toString(method) + " [" + vendor.getSimpleName() + "]");
    return frameworkMethod.invokeExplosivelySuper(target);
  }

  protected class VendorFrameworkMethod extends FrameworkMethod {
    private final Executor executor;

    VendorFrameworkMethod(final Method method, final Executor executor) {
      super(method);
      this.executor = executor;
    }

    private void runChild(final Statement statement, final Description description, final RunNotifier notifier) {
      final Spec spec = getMethod().getAnnotation(Spec.class);
      final int cardinality = spec == null ? 1 : spec.cardinality();
      for (int c = 0; c < cardinality; ++c) { // [N]
        if (sync)
          invoke(statement, description, notifier);
        else
          executor.execute(() -> invoke(statement, description, notifier));
      }
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

    Executor getExecutor() {
      return executor;
    }

    @Override
    public Object invokeExplosively(final Object target, final Object ... params) throws Throwable {
      return DBTestRunner.this.invokeExplosively(this, target, params);
    }

    final Object invokeExplosivelySuper(final Object target, final Object ... params) throws Throwable {
      try {
        executor.sempaphore.acquire();
        return super.invokeExplosively(target, params);
      }
      catch (final Throwable t) {
        final Unsupported unsupported = getMethod().getAnnotation(Unsupported.class);
        final DB db = executor.getDB();
        if (unsupported != null) {
          for (final Class<? extends Vendor> unsupportedVendor : unsupported.value()) { // [A]
            if (unsupportedVendor == db.value()) {
              if (logger.isWarnEnabled()) logger.warn("[" + db.value().getSimpleName() + "] does not support " + getMethod().getDeclaringClass().getSimpleName() + "." + DBTestRunner.toString(getMethod()));
              return null;
            }
          }
        }

        DeferredLogger.flush();
        if (logger.isErrorEnabled()) logger.error("[ERROR] " + db.value().getSimpleName());
        throw t instanceof SQLException ? flatten((SQLException)t) : t;
      }
      finally {
        executor.sempaphore.release();
      }
    }

    @Override
    public String getName() {
      return super.getName() + "[" + executor.getDB().value().getSimpleName() + "]";
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
  public void run(final RunNotifier notifier) {
    final Description description = getDescription();

    int numTests = 0;
    for (final Description child : description.getChildren()) { // [L]
      final Spec spec = child.getAnnotation(Spec.class);
      numTests += spec == null ? 1 : spec.cardinality();
    }

    latch = new CountDownLatch(numTests);

    notifier.addListener(new RunListener() {
      private Class<?> testClass;

      @Override
      public void testFinished(final Description description) throws Exception {
        testClass = description.getTestClass();
      }

      @Override
      @SuppressWarnings("resource")
      public void testRunFinished(final Result result) throws Exception {
        if (testClass == null)
          return;

        final DBs dbs = Classes.getAnnotationDeep(testClass, DBs.class);
        if (dbs != null)
          for (final DB db : dbs.value()) // [A]
            Vendor.getVendor(db.value()).close();
      }
    });

    if (failFast)
      notifier.addListener(new FailFastRunListener(notifier));

    super.run(notifier);
    try {
      latch.await();
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
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
}