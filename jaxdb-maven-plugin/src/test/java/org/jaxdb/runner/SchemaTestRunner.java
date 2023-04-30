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
import org.jaxdb.jsql.TestCommand;
import org.jaxdb.jsql.Transaction;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.util.ArrayUtil;

public class SchemaTestRunner extends DBTestRunner {
  private static final Map<DBTestRunner.DB,Connector> schemaClassToConnector = Collections.synchronizedMap(new HashMap<>());

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Schema {
    Class<? extends org.jaxdb.jsql.Schema> value();
  }

  public SchemaTestRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  @Override
  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && !Transaction.class.isAssignableFrom(method.getMethod().getParameterTypes()[0]))
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(method.getMethod().getParameterTypes(), ',', Class::getSimpleName) + ") must declare no parameters or one parameter of type: " + Transaction.class.getName()));
  }

  @Override
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Object target, Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    final Class<?>[] parameterTypes = method.getParameterTypes();

    if (parameterTypes.length == 0) {
      TestCommand.Select.beforeInvokeExplosively(method, null);
      return frameworkMethod.invokeExplosivelySuper(target);
    }

    final DBTestRunner.Executor executor = frameworkMethod.getExecutor();
    params = new Object[parameterTypes.length];
    int transactionArg = -1;
    for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
      if (Transaction.class.isAssignableFrom(parameterTypes[i]))
        transactionArg = i;
      else if (org.jaxdb.runner.Vendor.class.isAssignableFrom(parameterTypes[i]))
        params[i] = executor.getVendor();
      else
        throw new UnsupportedOperationException("Unsupported parameter type: " + parameterTypes[i].getName());
    }

    if (transactionArg == -1)
      throw new RuntimeException("Transaction parameter was not found");

    for (final Annotation annotation : method.getParameterAnnotations()[transactionArg]) // [A]
      if (annotation.annotationType() == Schema.class)
        return invokeInTransaction(((Schema)annotation).value(), executor, params, transactionArg, method, frameworkMethod, target);

    throw new RuntimeException("@Schema must be specified on Transaction parameter for " + frameworkMethod.getMethod().getName());
  }

  private static <S extends org.jaxdb.jsql.Schema>Object invokeInTransaction(final Class<S> schemaClass, final DBTestRunner.Executor executor, final Object[] params, final int transactionArg, final Method method, final VendorFrameworkMethod frameworkMethod, final Object target) throws Throwable {
    try (final Transaction transaction = new PreparedTransaction(executor.getVendor(), schemaClass) {
      @Override
      protected Connector getConnector() {
        Connector connector = schemaClassToConnector.get(executor.getDB());
        if (connector == null) {
          schemaClassToConnector.put(executor.getDB(), connector = new PreparedConnector(schemaClass, executor::getConnection));
          Database.threadLocal(schemaClass).connect(connector);
        }

        return connector;
      }
    }) {
      params[transactionArg] = transaction;
      TestCommand.Select.beforeInvokeExplosively(method, transaction);
      return frameworkMethod.invokeExplosivelySuper(target, params);
    }
  }
}