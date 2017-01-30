/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.data;

import java.lang.reflect.Modifier;
import java.util.Set;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.PackageNotFoundException;
import org.safris.xdb.schema.DBVendor;
import org.safris.xdb.xdd.xe.$xdd_binary;
import org.safris.xdb.xdd.xe.$xdd_blob;
import org.safris.xdb.xdd.xe.$xdd_boolean;
import org.safris.xdb.xdd.xe.$xdd_char;
import org.safris.xdb.xdd.xe.$xdd_clob;
import org.safris.xdb.xdd.xe.$xdd_date;
import org.safris.xdb.xdd.xe.$xdd_dateTime;
import org.safris.xdb.xdd.xe.$xdd_decimal;
import org.safris.xdb.xdd.xe.$xdd_enum;
import org.safris.xdb.xdd.xe.$xdd_float;
import org.safris.xdb.xdd.xe.$xdd_integer;
import org.safris.xdb.xdd.xe.$xdd_time;

public abstract class Serializer {
  private static final Serializer[] serializers = new Serializer[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemPackageLoader().loadPackage(Serializer.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Serializer.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Serializer serializer = (Serializer)cls.newInstance();
          serializers[serializer.getVendor().ordinal()] = serializer;
        }
      }
    }
    catch (final PackageNotFoundException | ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static Serializer getSerializer(final DBVendor vendor) {
    final Serializer serializer = serializers[vendor.ordinal()];
    if (serializer == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return serializer;
  }

  protected abstract DBVendor getVendor();

  protected String serialize(final $xdd_char attribute) {
    return "'" + attribute.text().replace("'", "''") + "'";
  }

  protected String serialize(final $xdd_clob attribute) {
    return "'" + attribute.text().replace("'", "''") + "'";
  }

  protected String serialize(final $xdd_binary attribute) {
    return "X'" + attribute.text() + "'";
  }

  protected String serialize(final $xdd_blob attribute) {
    return "X'" + attribute.text() + "'";
  }

  protected String serialize(final $xdd_integer attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $xdd_float attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $xdd_decimal attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $xdd_date attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $xdd_time attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $xdd_dateTime attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $xdd_boolean attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $xdd_enum attribute) {
    return "'" + attribute.text() + "'";
  }
}