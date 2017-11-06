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

package org.libx4j.rdb.ddlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$Column;
import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$ForeignKey;
import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$Index;
import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$Integer;
import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$Named;
import org.libx4j.rdb.ddlx.xIEcGGcJdtCXcCFzw5sg.$Table;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class DB2Compiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(DB2Compiler.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.DB2;
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type.text() + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }

  @Override
  protected DropStatement dropTableIfExists(final $Table table) {
    return new DropStatement("CALL db2perf_quiet_drop('TABLE " + table.getName$().text() + "')");
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
    try (final Statement statement = connection.createStatement()) {
      statement.execute("CREATE PROCEDURE db2perf_quiet_drop(IN statement VARCHAR(1000)) LANGUAGE SQL BEGIN DECLARE SQLSTATE CHAR(5); DECLARE NotThere CONDITION FOR SQLSTATE '42704'; DECLARE NotThereSig CONDITION FOR SQLSTATE '42883'; DECLARE EXIT HANDLER FOR NotThere, NotThereSig SET SQLSTATE = ' '; SET statement = 'DROP ' || statement; EXECUTE IMMEDIATE statement; END");
    }
    catch (final SQLException e) {
      if (e.getErrorCode() != -454 || !"42723".equals(e.getSQLState()))
        throw e;
    }
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return "";
  }

  @Override
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null && !column.getNull$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    return isAutoIncrement(column) ? "GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1)" : "";
  }

  @Override
  protected String onUpdate(final $ForeignKey.OnUpdate$ onUpdate) {
    if ($ForeignKey.OnUpdate$.CASCADE.text().equals(onUpdate.text())) {
      logger.warn("ON UPDATE CASCADE is not supported");
      return null;
    }

    return "ON UPDATE " + onUpdate.text();
  }
}