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

package org.jaxdb.jsql.generator;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;

import org.jaxdb.jsql.GenerateOn;
import org.libj.lang.Numbers;

public final class GeneratorUtil {
  public static String compile(final Object[] object) {
    if (object == null)
      return "null";

    final StringBuilder out = new StringBuilder();
    for (final Object item : object)
      out.append(", ").append(compile(item));

    return "new " + object.getClass().getComponentType().getName() + "[] {" + out.substring(2) + "}";
  }

  public static String compile(final Object object) {
    if (object == null)
      return "null";

    if (object instanceof String)
      return "\"" + ((String)object).replace("\"", "\\\"").replace("\n", "\\n") + "\"";

    if (object instanceof Short)
      return "(short)" + object;

    if (object instanceof Float && Numbers.isWhole((float)object))
      return object + "f";

    if (object instanceof Double && Numbers.isWhole((double)object))
      return object + "d";

    if (object instanceof BigInteger)
      return "new " + BigInteger.class.getName() + "(\"" + object + "\")";

    if (object instanceof BigDecimal)
      return "new " + BigDecimal.class.getName() + "(\"" + object + "\")";

    if (object instanceof Long) {
      final Long value = (Long)object;
      return value.intValue() != value.longValue() ? object + "L" : String.valueOf(object);
    }

    if (object instanceof GenerateOn<?>) {
      try {
        final Field[] fields = GenerateOn.class.getDeclaredFields();
        for (final Field field : fields)
          if (field.get(null) == object)
            return GenerateOn.class.getName() + "." + field.getName();

        throw new IllegalStateException("Did not find the desired field");
      }
      catch (final IllegalAccessException e) {
        throw new IllegalStateException(e);
      }
    }

    return String.valueOf(object);
  }

  private GeneratorUtil() {
  }
}