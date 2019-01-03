/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.sqlx;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.Set;

import org.openjax.standard.lang.PackageLoader;
import org.openjax.standard.lang.PackageNotFoundException;
import org.openjax.rdb.ddlx.dt;
import org.openjax.rdb.vendor.DBVendor;

abstract class Compiler {
  private static final Compiler[] compilers = new Compiler[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getContextPackageLoader().loadPackage(Compiler.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Compiler.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Compiler compiler = (Compiler)cls.getDeclaredConstructor().newInstance();
          compilers[compiler.getVendor().ordinal()] = compiler;
        }
      }
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException | IOException | NoSuchMethodException | PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected abstract DBVendor getVendor();

  protected String compile(final dt.BIGINT value) {
    return value.toString();
  }

  protected String compile(final dt.BINARY value) {
    return "X'" + value + "'";
  }

  protected String compile(final dt.BLOB value) {
    return "X'" + value + "'";
  }

  protected String compile(final dt.BOOLEAN value) {
    return value.toString();
  }

  protected String compile(final dt.CHAR value) {
    return "'" + value.get().replace("'", "''") + "'";
  }

  protected String compile(final dt.CLOB value) {
    return "'" + value.get().replace("'", "''") + "'";
  }

  protected String compile(final dt.DATE value) {
    return "'" + value + "'";
  }

  protected String compile(final dt.DATETIME value) {
    return "'" + value + "'";
  }

  protected String compile(final dt.DECIMAL value) {
    return value.toString();
  }

  protected String compile(final dt.DOUBLE value) {
    return value.toString();
  }

  protected String compile(final dt.ENUM value) {
    return "'" + value + "'";
  }

  protected String compile(final dt.FLOAT value) {
    return value.toString();
  }

  protected String compile(final dt.INT value) {
    return value.toString();
  }

  protected String compile(final dt.SMALLINT value) {
    return value.toString();
  }

  protected String compile(final dt.TIME value) {
    return "'" + value + "'";
  }

  protected String compile(final dt.TINYINT value) {
    return value.toString();
  }
}