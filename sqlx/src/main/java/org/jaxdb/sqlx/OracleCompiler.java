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
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.Hexadecimal;

final class OracleCompiler extends Compiler {
  OracleCompiler() {
    super(DbVendor.ORACLE);
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.BINARY value) {
    return b.append("HEXTORAW('").append(new Hexadecimal(value.get())).append("')");
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.BLOB value) {
    return b.append("HEXTORAW('").append(value.get()).append("')");
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.BOOLEAN value) {
    return b.append(value.get() ? "1" : "0");
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.CHAR value) {
    final String string = value.get().replace("'", "''");
    return string.length() == 0 || string.charAt(0) == ' ' ? b.append("' ").append(string).append('\'') : b.append('\'').append(string).append('\'');
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.DATE value) {
    return b.append("TO_DATE('").append(value).append("','YYYY-MM-DD')");
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.DATETIME value) {
    return b.append("TO_TIMESTAMP('").append(value).append("', 'YYYY-MM-DD HH24:MI:SS.FF')");
  }

  @Override
  StringBuilder compile(final StringBuilder b, final dt.TIME value) {
    return b.append("'0 ").append(value).append('\'');
  }

  @Override
  boolean sequenceReset(final Connection connection, final Appendable builder, final String tableName, final String columnName, final long restartWith) throws IOException, SQLException {
    final String sequenceName = getSequenceName(tableName, columnName);
    if (connection != null) {
      try (final CallableStatement statement = connection.prepareCall("{ ? = call reset_sequence(?, ?) }")) {
        statement.registerOutParameter(1, Types.NUMERIC);
        statement.setString(2, sequenceName);
        statement.setLong(3, restartWith);
        statement.executeUpdate();
        return statement.getInt(1) == 1;
      }
    }

    builder.append("\nEXEC DBMS_OUTPUT.PUT_LINE(reset_sequence('").append(sequenceName).append("', ").append(restartWith + "));");
    return true;
  }
}