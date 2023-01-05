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
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Documented;
import org.libj.lang.Strings;
import org.libj.math.BigInt;
import org.openjax.xml.datatype.HexBinary;
import org.w3.www._2001.XMLSchema;

final class GeneratorUtil {
  static final Object THIS = new Object();
  static final Object MUTABLE = new Object();
  static final Object PRIMARY_KEY = new Object();
  static final Object KEY_FOR_UPDATE = new Object();
  static final Object COMMIT_UPDATE = new Object();

  static String getDoc(final $Documented documented, final int depth, final char start, final char end) {
    final XMLSchema.yAA.$String documentation = documented.getDocumentation();
    if (documentation == null)
      return "";

    final String doc = documentation.text().trim();
    if (doc.length() == 0)
      return "";

    final String indent = Strings.repeat(" ", depth * 2);
    final StringBuilder out = new StringBuilder();
    if (start != '\0')
      out.append(start);

    out.append(indent).append("/** ").append(doc).append(" */");
    if (end != '\0')
      out.append(end);

    return out.toString();
  }

  @SuppressWarnings("rawtypes")
  static String compile(final Object object, final Class<? extends data.Column> type) {
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