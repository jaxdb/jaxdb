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

package org.jaxdb.sqlx;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorBase;
import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;

abstract class Compiler extends DBVendorBase {
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
    catch (final IllegalAccessException | InstantiationException | IOException | NoSuchMethodException | PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
    catch (final InvocationTargetException e) {
      if (e.getCause() instanceof RuntimeException)
        throw (RuntimeException)e.getCause();

      throw new ExceptionInInitializerError(e.getCause());
    }
  }

  static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected Compiler(final DBVendor vendor) {
    super(vendor);
  }

  String compile(final dt.BIGINT value) {
    return value.toString();
  }

  String compile(final dt.BINARY value) {
    return "X'" + value + "'";
  }

  String compile(final dt.BLOB value) {
    return "X'" + value + "'";
  }

  String compile(final dt.BOOLEAN value) {
    return value.toString();
  }

  String compile(final dt.CHAR value) {
    return "'" + value.get().replace("'", "''") + "'";
  }

  String compile(final dt.CLOB value) {
    return "'" + value.get().replace("'", "''") + "'";
  }

  String compile(final dt.DATE value) {
    return "'" + value + "'";
  }

  String compile(final dt.DATETIME value) {
    return "'" + value + "'";
  }

  String compile(final dt.DECIMAL value) {
    return value.toString();
  }

  String compile(final dt.DOUBLE value) {
    return value.toString();
  }

  String compile(final dt.ENUM value) {
    return "'" + value + "'";
  }

  String compile(final dt.FLOAT value) {
    return value.toString();
  }

  String compile(final dt.INT value) {
    return value.toString();
  }

  String compile(final dt.SMALLINT value) {
    return value.toString();
  }

  String compile(final dt.TIME value) {
    return "'" + value + "'";
  }

  String compile(final dt.TINYINT value) {
    return value.toString();
  }

  String insert(final String tableName, final StringBuilder columns, final StringBuilder values) {
    final StringBuilder builder = new StringBuilder("INSERT INTO ").append(getDialect().quoteIdentifier(tableName));
    builder.append(" (").append(columns).append(") VALUES (").append(values).append(')');
    return builder.toString();
  }

  abstract boolean sequenceReset(Connection connection, Appendable builder, String tableName, String columnName, long restartWith) throws IOException, SQLException;
}