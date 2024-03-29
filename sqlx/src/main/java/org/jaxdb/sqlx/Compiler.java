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
import java.sql.Connection;
import java.sql.SQLException;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.DbVendorCompiler;

abstract class Compiler extends DbVendorCompiler {
  private static final Compiler[] compilers = {new DB2Compiler(), new DerbyCompiler(), new MariaDBCompiler(), new MySQLCompiler(), new OracleCompiler(), new PostgreSQLCompiler(), new SQLiteCompiler()};

  static Compiler getCompiler(final DbVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected Compiler(final DbVendor vendor) {
    super(vendor);
  }

  StringBuilder compile(final StringBuilder b, final dt.BIGINT value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.BINARY value) {
    return b.append("X'").append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.BLOB value) {
    return b.append("X'").append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.BOOLEAN value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.CHAR value) {
    return b.append('\'').append(value.get().replace("'", "''")).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.CLOB value) {
    return b.append('\'').append(value.get().replace("'", "''")).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.DATE value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.DATETIME value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.DECIMAL value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.DOUBLE value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.ENUM value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.FLOAT value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.INT value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.SMALLINT value) {
    return b.append(value.toString());
  }

  StringBuilder compile(final StringBuilder b, final dt.TIME value) {
    return b.append('\'').append(value).append('\'');
  }

  StringBuilder compile(final StringBuilder b, final dt.TINYINT value) {
    return b.append(value.toString());
  }

  String insert(final String tableName, final StringBuilder columns, final StringBuilder values) {
    final StringBuilder b = new StringBuilder("INSERT INTO ");
    getDialect().quoteIdentifier(b, tableName);
    if (columns.length() == 0)
      return b.toString();

    if (values.length() == 0)
      throw new IllegalStateException();

    b.append(" (").append(columns).append(") VALUES (").append(values).append(')');
    return b.toString();
  }

  abstract boolean sequenceReset(Connection connection, Appendable builder, String tableName, String columnName, long restartWith) throws IOException, SQLException;
}