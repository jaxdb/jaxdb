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
import org.jaxdb.jsql.Transaction;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.libj.sql.exception.SQLInvalidSchemaNameException;
import org.libj.util.ArrayUtil;

public class VendorSchemaRunner extends VendorRunner {
  private static final Map<VendorRunner.Vendor,Connector> schemaClassToConnector = Collections.synchronizedMap(new HashMap<>());

  @Target(ElementType.PARAMETER)
  @Retention(RetentionPolicy.RUNTIME)
  public @interface Schema {
    Class<? extends org.jaxdb.jsql.Schema> value();
  }

  public VendorSchemaRunner(final Class<?> cls) throws InitializationError {
    super(cls);
  }

  @Override
  protected void checkParameters(final FrameworkMethod method, final List<? super Throwable> errors) {
    if (method.getMethod().getParameterTypes().length > 0 && method.getMethod().getParameterTypes()[0] != Transaction.class)
      errors.add(new Exception("Method " + method.getDeclaringClass().getSimpleName() + "." + method.getName() + "(" + ArrayUtil.toString(method.getMethod().getParameterTypes(), ',', Class::getSimpleName) + ") must declare no parameters or one parameter of type: " + Transaction.class.getName()));
  }

  @Override
  protected Object invokeExplosively(final VendorFrameworkMethod frameworkMethod, final Vendor vendor, final Object target, final Object ... params) throws Throwable {
    final Method method = frameworkMethod.getMethod();
    if (method.getParameterTypes().length == 0)
      return frameworkMethod.invokeExplosivelySuper(target);

    final Annotation[] annotations = method.getParameterAnnotations()[0];
    final VendorRunner.Executor executor = frameworkMethod.getExecutor();
    for (final Annotation annotation : annotations) {
      if (annotation.annotationType() == Schema.class) {
        final Class<? extends org.jaxdb.jsql.Schema> schemaClass = ((Schema)annotation).value();
        try (final Transaction transaction = new PreparedTransaction(schemaClass) {
          @Override
          protected Connector getConnector() throws SQLInvalidSchemaNameException {
            Connector connector = schemaClassToConnector.get(executor.getVendor());
            if (connector == null)
              schemaClassToConnector.put(executor.getVendor(), connector = new PreparedConnector(schemaClass, () -> executor.getConnection()));

            return connector;
          }
        }) {
          return frameworkMethod.invokeExplosivelySuper(target, transaction);
        }
      }
    }

    throw new RuntimeException("@Schema must be specified on Transaction parameter");
  }
}