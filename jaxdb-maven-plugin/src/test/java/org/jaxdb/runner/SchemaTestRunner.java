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

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.jsql.Connector;
import org.jaxdb.jsql.Database;
import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.TestCommand;
import org.jaxdb.jsql.Transaction;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.util.ArrayUtil;

public class SchemaTestRunner extends DBTestRunner {
  private static final Map<DBTestRunner.DB,Connector> schemaClassToConnector = Collections.synchronizedMap(new HashMap<>());

  @Target(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface TestSchema {
    Class<? extends Schema> value();
  }

  public SchemaTestRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  @Override
  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && !Transaction.class.isAssignableFrom(method.getMethod().getParameterTypes()[0]) && !Connector.class.isAssignableFrom(method.getMethod().getParameterTypes()[0]))
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(method.getMethod().getParameterTypes(), ',', Class::getSimpleName) + ") must declare no parameters or one parameter of type " + Transaction.class.getName() + " or " + Connector.class.getName()));
  }

  private static Class<? extends Schema> getSchemaClass(final Method method, final TestSchema testSchema) {
    if (testSchema == null)
      throw new RuntimeException("@TestSchema must be specified on Transaction parameter for " + method.getName());

    return testSchema.value();
  }

  @Override
  @SuppressWarnings("resource")
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Object target, Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    final Class<?>[] parameterTypes = method.getParameterTypes();
    TestCommand.Select.beforeInvokeExplosively(method);

    if (parameterTypes.length == 0)
      return frameworkMethod.invokeExplosivelySuper(target);

    final DBTestRunner.Executor executor = frameworkMethod.getExecutor();
    params = new Object[parameterTypes.length];
    Transaction transaction = null;

    final TestSchema testSchema = method.getAnnotation(TestSchema.class);
    for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
      if (Connector.class.isAssignableFrom(parameterTypes[i]))
        params[i] = getConnector(getSchemaClass(method, testSchema), executor);
      else if (Transaction.class.isAssignableFrom(parameterTypes[i]))
        params[i] = transaction = newTransaction(getSchemaClass(method, testSchema), executor);
      else if (org.jaxdb.runner.Vendor.class.isAssignableFrom(parameterTypes[i]))
        params[i] = executor.getVendor();
      else
        throw new UnsupportedOperationException("Unsupported parameter type: " + parameterTypes[i].getName());
    }

    final Object result;

    try {
      result = frameworkMethod.invokeExplosivelySuper(target, params);
    }
    catch (final Throwable t) {
      if (transaction != null) {
        transaction.rollback(t);
        transaction.close();
      }

      throw t;
    }

    if (transaction != null) {
      transaction.rollback();
      transaction.close();
    }

    return result;
  }

  private static Connector getConnector(final Class<? extends Schema> schemaClass, final DBTestRunner.Executor executor) {
    final DB db = executor.getDB();
    Connector connector = schemaClassToConnector.get(db);
    if (connector == null)
      schemaClassToConnector.put(db, connector = Database.get(schemaClass, db.value().getSimpleName()).connect(i -> i != null ? executor.getConnection(i.getLevel()) : executor.getConnection()));

    return connector;
  }

  private static Transaction newTransaction(final Class<? extends Schema> schemaClass, final DBTestRunner.Executor executor) {
    return new Transaction(schemaClass) {
      @Override
      protected Connector getConnector() {
        return SchemaTestRunner.getConnector(schemaClass, executor);
      }
    };
  }
}