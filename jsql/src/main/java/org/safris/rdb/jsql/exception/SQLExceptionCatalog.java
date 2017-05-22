/* Copyright (c) 2016 lib4j
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

package org.safris.rdb.jsql.exception;

import java.lang.reflect.Constructor;
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.SQLInvalidAuthorizationSpecException;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.lang.Throwables;

public class SQLExceptionCatalog {
  private static final Map<String,Class<? extends SQLException>> categories = new HashMap<String,Class<? extends SQLException>>();

  // Spec: http://www.contrib.andrew.cmu.edu/~shadow/sql/sql1992.txt
  static {
    categories.put("02", SQLNoDataException.class);
    categories.put("07", SQLDynamicErrorException.class);
    categories.put("08", SQLConnectionException.class);
    categories.put("0A", SQLFeatureNotSupportedException.class);
    categories.put("21", SQLCardinalityException.class);
    categories.put("22", SQLDataException.class);
    categories.put("23", SQLIntegrityConstraintViolationException.class);
    categories.put("24", SQLInvalidCursorStateException.class);
    categories.put("25", SQLInvalidTransactionStateException.class);
    categories.put("26", SQLInvalidStatementNameException.class);
    categories.put("28", SQLInvalidAuthorizationSpecException.class);
    categories.put("2B", SQLDependentPrivilegeDescriptorsException.class);
    categories.put("2C", SQLInvalidCharacterSetNameException.class);
    categories.put("2D", SQLInvalidTransactionTerminationException.class);
    categories.put("2E", SQLInvalidConnectionNameException.class);
    categories.put("33", SQLInvalidDescriptorNameException.class);
    categories.put("34", SQLInvalidCursorNameException.class);
    categories.put("35", SQLInvalidConditionNumberException.class);
    categories.put("3C", SQLAmbiguousCursorNameException.class);
    categories.put("3D", SQLInvalidCatalogNameException.class);
    categories.put("3F", SQLInvalidSchemaNameException.class);
  }

  public static SQLException lookup(final SQLException exception) {
    final String sqlState = exception.getSQLState();
    if (sqlState == null || sqlState.length() < 2)
      return exception;

    final Class<? extends SQLException> category = categories.get(sqlState.substring(0, 2));
    if (category == null || category.isInstance(exception))
      return exception;

    try {
      final Constructor<? extends SQLException> constructor = category.getConstructor(String.class, String.class, int.class);
      final SQLException sqlException = constructor.newInstance(exception.getMessage(), exception.getSQLState(), exception.getErrorCode());
      sqlException.setStackTrace(exception.getStackTrace());
      Throwables.set(sqlException, exception.getCause());
      return sqlException;
    }
    catch (final ReflectiveOperationException e) {
      throw new UnsupportedOperationException("Attempted to instantiate " + category.getName(), e);
    }
  }
}