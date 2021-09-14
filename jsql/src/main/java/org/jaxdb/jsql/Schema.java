/* Copyright (c) 2014 JAX-DB
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

package org.jaxdb.jsql;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class Schema {
  @SuppressWarnings("unchecked")
  static Schema newSchema(final Class<? extends Schema> schemaClass) {
    try {
      return ((Constructor<Schema>)schemaClass.getDeclaredConstructor()).newInstance();
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new IllegalStateException(e);
    }
    catch (final IllegalAccessException | InstantiationException  | NoSuchMethodException e) {
      throw new IllegalStateException(e);
    }
  }
}