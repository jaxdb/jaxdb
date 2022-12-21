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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jaxdb.jsql.GenerateOn;
import org.jaxdb.jsql.data;
import org.libj.lang.Strings;
import org.libj.math.BigInt;
import org.openjax.xml.datatype.HexBinary;

public final class GeneratorUtil {
  @SuppressWarnings("rawtypes")
  public static String compile(final Object object, final Class<? extends data.Column> type) {
    if (object == null)
      return "null";

    if (object instanceof StringBuilder)
      return object.toString();

    if (object instanceof String) {
      if (type == data.DATETIME.class) {
        final String[] dateTime = Strings.split(((String)object), ' ');
        final String[] date = Strings.split(dateTime[0], '-');
        final String[] time = Strings.split(dateTime[1], ':');
        final int d = time[2].indexOf('.');
        String nanos = null;
        if (d > -1) {
          nanos = String.format("%-9s", time[2].substring(d + 1)).replace(' ', '0');
          time[2] = time[2].substring(0, d);
        }

        return LocalDateTime.class.getName() + ".of(" + date[0] + ", " + date[1] + ", " + date[2] + ", " + time[0] + ", " + time[1] + ", " + time[2] + (nanos != null ? ", " + nanos : "") + ")";
      }

      if (type == data.DATE.class) {
        final String[] date = Strings.split((String)object, '-');
        return LocalDate.class.getName() + ".of(" + date[0] + ", " + date[1] + ", " + date[2] + ")";
      }

      if (type == data.TIME.class) {
        final String[] time = Strings.split(((String)object), ':');
        final int d = time[2].indexOf('.');
        String nanos = null;
        if (d > -1) {
          nanos = String.format("%-9s", time[2].substring(d + 1)).replace(' ', '0');
          time[2] = time[2].substring(0, d);
        }

        return LocalTime.class.getName() + ".of(" + time[0] + ", " + time[1] + ", " + time[2] + (nanos != null ? ", " + nanos : "") + ")";
      }

      return "\"" + ((String)object).replace("\"", "\\\"").replace("\n", "\\n") + "\"";
    }

    if (object instanceof HexBinary) {
      final StringBuilder builder = new StringBuilder("new byte[] ");
      for (final byte b : ((HexBinary)object).getBytes())
        builder.append(String.format(" (byte)0x%02x,", b));

      builder.setCharAt(11, '{');
      builder.setCharAt(builder.length() - 1, '}');
      return builder.toString();
    }

    if (object instanceof Byte)
      return "(byte)" + object;

    if (object instanceof Short)
      return "(short)" + object;

    if (object instanceof Float)
      return object + "f";

    if (object instanceof Double)
      return object + "d";

    if (object instanceof BigInteger)
      return "new " + BigInt.class.getName() + "(\"" + object + "\")";

    if (object instanceof BigDecimal)
      return "new " + BigDecimal.class.getName() + "(\"" + object + "\")";

    if (object instanceof Long)
      return object + "L";

    if (object instanceof GenerateOn<?>) {
      try {
        final Field[] fields = GenerateOn.class.getDeclaredFields();
        for (final Field field : fields) // [A]
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