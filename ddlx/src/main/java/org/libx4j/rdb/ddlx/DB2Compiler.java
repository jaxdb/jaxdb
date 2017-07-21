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

import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
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
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $ddlx_index._type$ type, final String tableName, final $ddlx_named ... columns) {
    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type.text() + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }

  @Override
  protected DropStatement dropTableIfExists(final $ddlx_table table) {
    return new DropStatement("CALL db2perf_quiet_drop('TABLE " + table._name$().text() + "')");
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
  protected String dropIndexOnClause(final $ddlx_table table) {
    return "";
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() && !column._null$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return isAutoIncrement(column) ? "GENERATED ALWAYS AS IDENTITY (START WITH 1 INCREMENT BY 1)" : "";
  }

  @Override
  protected String onUpdate(final $ddlx_foreignKey._onUpdate$ onUpdate) {
    if ($ddlx_foreignKey._onUpdate$.CASCADE.text().equals(onUpdate.text())) {
      logger.warn("ON UPDATE CASCADE is not supported");
      return null;
    }

    return "ON UPDATE " + onUpdate.text();
  }
}