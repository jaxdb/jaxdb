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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.jsql.Connector;
import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.TestCommand;
import org.jaxdb.jsql.TestConnector;
import org.jaxdb.jsql.TestDatabase;
import org.jaxdb.jsql.Transaction;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.util.ArrayUtil;

public class SchemaTestRunner extends DBTestRunner {
  private static final Map<DBTestRunner.DB,Connector> schemaClassToConnector = Collections.synchronizedMap(new HashMap<>());

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
      hasConnector = checkParameterType(errors, method, parameterTypes, parameterType, Connector.class, hasConnector);
      hasSchema = checkParameterType(errors, method, parameterTypes, parameterType, Schema.class, hasSchema);
      hasVendor = checkParameterType(errors, method, parameterTypes, parameterType, Vendor.class, hasVendor);
    }

    if ((hasTransaction || hasConnector) && !hasSchema)
      errors.add(new Exception("Method " + frameworkMethod.getDeclaringClass().getSimpleName() + "." + frameworkMethod.getName() + "(" + ArrayUtil.toString(parameterTypes, ',', Class::getSimpleName) + ") must declare a " + Schema.class.getSimpleName() + " parameter for " + Transaction.class.getSimpleName() + " or " + Connector.class.getSimpleName() + " parameters to be able to be specified"));
  }

  private final HashMap<Class<?>,Schema> schemaClassToSchema = new HashMap<Class<?>,Schema>() {
    @Override
    @SuppressWarnings("unchecked")
    public Schema get(final Object key) {
      Schema value = super.get(key);
      if (value == null) {
        final Class<? extends Schema> cls = (Class<? extends Schema>)key;
        try {
          super.put(cls, value = cls.getConstructor().newInstance());
        }
        catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
      }

      return value;
    }
  };

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

    int connectorParam = -1;
    int transactionParam = -1;
    Schema schema = null;
    for (int i = 0, i$ = parameterTypes.length; i < i$; ++i) { // [A]
      final Class<?> parameterType = parameterTypes[i];
      if (Connector.class.isAssignableFrom(parameterType))
        connectorParam = i;
      else if (Transaction.class.isAssignableFrom(parameterType))
        transactionParam = i;
      else if (Vendor.class.isAssignableFrom(parameterType))
        params[i] = executor.getVendor();
      else if (Schema.class.isAssignableFrom(parameterType))
        params[i] = schema = schemaClassToSchema.get(parameterType);
      else
        throw new UnsupportedOperationException("Unsupported parameter type: " + parameterType.getName());
    }

    if (connectorParam != -1)
      params[connectorParam] = getConnector(schema, executor);

    if (transactionParam != -1)
      params[transactionParam] = transaction = newTransaction(schema, executor);

    TestConnector.called();

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

  private static Connector getConnector(final Schema schema, final DBTestRunner.Executor executor) {
    final DB db = executor.getDB();
    Connector connector = schemaClassToConnector.get(db);
    if (connector == null)
      schemaClassToConnector.put(db, connector = TestDatabase.get(schema, db.value().getSimpleName()).connect(i -> i != null ? executor.getConnection(i.getLevel()) : executor.getConnection()));

    return connector;
  }

  private static Transaction newTransaction(final Schema schema, final DBTestRunner.Executor executor) {
    return new Transaction(schema) {
      @Override
      protected Connector getConnector() {
        return SchemaTestRunner.getConnector(schema, executor);
      }
    };
  }
}