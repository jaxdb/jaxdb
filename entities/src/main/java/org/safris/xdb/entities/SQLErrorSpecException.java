/* Copyright (c) 2016 Seva Safris
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

package org.safris.xdb.entities;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.safris.commons.lang.Pair;
import org.safris.xdb.schema.DBVendor;

public class SQLErrorSpecException extends SQLException {
  private static final Logger logger = Logger.getLogger(Tables.class.getName());

  private static final long serialVersionUID = 400839108529773414L;
  private static final Map<DBVendor,Map<String,ErrorSpec>> errorSpecs = new HashMap<DBVendor,Map<String,ErrorSpec>>();

  public static class ErrorSpec {
    @SafeVarargs
    public ErrorSpec(final Pair<DBVendor,String> ... specs) {
      for (final Pair<DBVendor,String> spec : specs) {
        Map<String,ErrorSpec> sqlCodeToException = SQLErrorSpecException.errorSpecs.get(spec.a);
        if (sqlCodeToException == null)
          SQLErrorSpecException.errorSpecs.put(spec.a, sqlCodeToException = new HashMap<String,ErrorSpec>());

        sqlCodeToException.put(spec.b, this);
      }
    }
  }

  public static final ErrorSpec CONNECTION_ATTEMPT_FAILED = new ErrorSpec(new Pair<DBVendor,String>(null, "08001"));

  public static final ErrorSpec INVALID_PARAMETER_VALUE = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "22023"));
  public static final ErrorSpec SYNTAX_ERROR = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "42601"));
  public static final ErrorSpec DATATYPE_MISMATCH = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "42804"));
  public static final ErrorSpec UNIQUE_VIOLATION = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "23505"));
  public static final ErrorSpec UNDEFINED_TABLE = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "42P01"));
  public static final ErrorSpec NOT_NULL_VIOLATION = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "23502"));
  public static final ErrorSpec UNDEFINED_COLUMN = new ErrorSpec(new Pair<DBVendor,String>(DBVendor.POSTGRE_SQL, "42703"));

  public static final ErrorSpec UNIMPLEMENTED = new ErrorSpec();

  public static SQLErrorSpecException lookup(final SQLException e, final DBVendor vendor) {
    Map<String,ErrorSpec> sqlCodeToException = errorSpecs.get(vendor);
    if (sqlCodeToException == null) {
      logger.warning("THE VENDOR " + vendor + " DOES NOT EXIST IN THE XDEException library!!!!!!!!!!!!!!! Add it!!!!! http://www.postgresql.org/docs/8.3/static/errcodes-appendix.html");
      return new SQLErrorSpecException(e, UNIMPLEMENTED);
    }

    final ErrorSpec errorSpec = sqlCodeToException.get(e.getSQLState());
    if (errorSpec == null) {
      logger.warning("THE SQLSTATE " + e.getSQLState() + " FOR VENDOR " + vendor + " DOES NOT EXIST IN THE XDEException library!!!!!!!!!!!!!!! Add it!!!!! http://www.postgresql.org/docs/8.3/static/errcodes-appendix.html");
      return new SQLErrorSpecException(e, UNIMPLEMENTED);
    }

    return new SQLErrorSpecException(e, errorSpec);
  }

  public static SQLErrorSpecException lookup(final SQLException e) {
    return lookup(e, null);
  }

  private final ErrorSpec errorSpec;

  private SQLErrorSpecException(final SQLException e, final ErrorSpec errorSpec) {
    super(e.getMessage(), e.getSQLState(), e.getErrorCode(), e.getCause());
    setStackTrace(e.getStackTrace());
    this.errorSpec = errorSpec;
  }

  protected SQLErrorSpecException(final String message) {
    super(message);
    this.errorSpec = null;
  }

  public ErrorSpec getErrorSpec() {
    return errorSpec;
  }
}