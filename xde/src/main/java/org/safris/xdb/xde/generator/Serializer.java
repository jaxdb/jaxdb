/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde.generator;

import java.lang.reflect.Field;

import org.safris.commons.lang.Numbers;
import org.safris.xdb.xde.GenerateOn;
import org.safris.xdb.xde.XDERuntimeException;

public final class Serializer {
  public static String serialize(final Object[] object) {
    if (object == null)
      return "null";

    String out = "";
    for (final Object item : object)
      out += ", " + serialize(item);

    return "new " + object.getClass().getComponentType().getName() + "[] {" + out.substring(2) + "}";
  }

  public static String serialize(final Object object) {
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

    if (object instanceof Long)
      return object + "l";

    if (object instanceof GenerateOn<?>) {
      try {
        final Field[] fields = GenerateOn.class.getDeclaredFields();
        for (final Field field : fields)
          if (field.get(null) == object)
            return GenerateOn.class.getName() + "." + field.getName();

        throw new Error("Did not find the desired field");
      }
      catch (final IllegalAccessException e) {
        throw new XDERuntimeException(e);
      }
    }

    return String.valueOf(object);
  }

  private Serializer() {
  }
}