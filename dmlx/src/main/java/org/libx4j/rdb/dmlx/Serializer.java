/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.dmlx;

import java.lang.reflect.Modifier;
import java.util.Set;

import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.libx4j.rdb.dmlx.xe.$dmlx_binary;
import org.libx4j.rdb.dmlx.xe.$dmlx_blob;
import org.libx4j.rdb.dmlx.xe.$dmlx_boolean;
import org.libx4j.rdb.dmlx.xe.$dmlx_char;
import org.libx4j.rdb.dmlx.xe.$dmlx_clob;
import org.libx4j.rdb.dmlx.xe.$dmlx_date;
import org.libx4j.rdb.dmlx.xe.$dmlx_dateTime;
import org.libx4j.rdb.dmlx.xe.$dmlx_decimal;
import org.libx4j.rdb.dmlx.xe.$dmlx_enum;
import org.libx4j.rdb.dmlx.xe.$dmlx_float;
import org.libx4j.rdb.dmlx.xe.$dmlx_integer;
import org.libx4j.rdb.dmlx.xe.$dmlx_time;
import org.libx4j.rdb.vendor.DBVendor;

abstract class Serializer {
  private static final Serializer[] serializers = new Serializer[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemContextPackageLoader().loadPackage(Serializer.class.getPackage());
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

  protected static Serializer getSerializer(final DBVendor vendor) {
    final Serializer serializer = serializers[vendor.ordinal()];
    if (serializer == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return serializer;
  }

  protected abstract DBVendor getVendor();

  protected String serialize(final $dmlx_boolean attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $dmlx_float attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $dmlx_decimal attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $dmlx_integer attribute) {
    return String.valueOf(attribute.text());
  }

  protected String serialize(final $dmlx_char attribute) {
    return "'" + attribute.text().replace("'", "''") + "'";
  }

  protected String serialize(final $dmlx_clob attribute) {
    return "'" + attribute.text().replace("'", "''") + "'";
  }

  protected String serialize(final $dmlx_binary attribute) {
    return "X'" + attribute.text() + "'";
  }

  protected String serialize(final $dmlx_blob attribute) {
    return "X'" + attribute.text() + "'";
  }

  protected String serialize(final $dmlx_date attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $dmlx_time attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $dmlx_dateTime attribute) {
    return "'" + attribute.text() + "'";
  }

  protected String serialize(final $dmlx_enum attribute) {
    return "'" + attribute.text() + "'";
  }
}