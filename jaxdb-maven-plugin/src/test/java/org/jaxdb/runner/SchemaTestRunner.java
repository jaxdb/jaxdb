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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.TestCommand;
import org.jaxdb.jsql.TestConnectionFactory;
import org.jaxdb.jsql.Transaction;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.util.ArrayUtil;

public class SchemaTestRunner extends DBTestRunner {
  public SchemaTestRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  private static boolean checkParameterType(final List<? super Throwable> errors, final Method method, final Class<?>[] parameterTypes, final Class<?> parameterType, final Class<?> expectedType, final boolean has) {
    if (!expectedType.isAssignableFrom(parameterType))
      return has;

    if (has)
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(parameterTypes, ',', Class::getSimpleName) + ") can only declare one paramter of type " + expectedType.getName()));

    return true;
  }

  @Override
  protected void checkParameters(final FrameworkMethod frameworkMethod, final List<? super Throwable> errors) {
    final Method method = frameworkMethod.getMethod();
    final Class<?>[] parameterTypes = method.getParameterTypes();
    boolean hasTransaction = false;
    boolean hasConnector = false;
    boolean hasSchema = false;
    boolean hasVendor = false;
    for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
      final Class<?> parameterType = parameterTypes[i];
      hasTransaction = checkParameterType(errors, method, parameterTypes, parameterType, Transaction.class, hasTransaction);
      hasSchema = checkParameterType(errors, method, parameterTypes, parameterType, Schema.class, hasSchema);
      hasVendor = checkParameterType(errors, method, parameterTypes, parameterType, Vendor.class, hasVendor);
    }

    if ((hasTransaction || hasConnector) && !hasSchema)
      errors.add(new Exception("Method " + frameworkMethod.getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName() + "(" + ArrayUtil.toString(parameterTypes, ',', Class::getSimpleName) + ") must declare a " + Schema.class.getSimpleName() + " parameter for a " + Transaction.class.getSimpleName() + " parameter to be able to be specified"));
  }

  private final HashMap<Class<?>,HashMap<DB,Schema>> schemaClassToDbToSchema = new HashMap<>();

  @SuppressWarnings("unchecked")
  private Schema getSchema(final Class<?> schemaClass, final Executor executor) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    final DB db = executor.getDB();
    HashMap<DB,Schema> dbToSchema = schemaClassToDbToSchema.get(schemaClass);
    Schema schema;
    if (dbToSchema == null)
      schemaClassToDbToSchema.put(schemaClass, dbToSchema = new HashMap<>());
    else if ((schema = dbToSchema.get(db)) != null)
      return schema;

    dbToSchema.put(db, schema = ((Class<? extends Schema>)schemaClass).getConstructor().newInstance());
    schema.connect(new TestConnectionFactory() {
      @Override
      public Connection getTestConnection(final Transaction.Isolation isolation) throws IOException, SQLException {
        return isolation != null ? executor.getConnection(isolation.getLevel()) : executor.getConnection();
      }
    }, isPrepared());
    return schema;
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

    int transactionParam = -1;
    Schema schema = null;
    for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
      final Class<?> parameterType = parameterTypes[i];
      if (Transaction.class.isAssignableFrom(parameterType))
        transactionParam = i;
      else if (Vendor.class.isAssignableFrom(parameterType))
        params[i] = executor.getVendor();
      else if (Schema.class.isAssignableFrom(parameterType))
        params[i] = schema = getSchema(parameterType, executor);
      else
        throw new UnsupportedOperationException("Unsupported parameter type: " + parameterType.getName());
    }

    if (transactionParam != -1)
      params[transactionParam] = transaction = new Transaction(schema);

    TestConnectionFactory.called();

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
}