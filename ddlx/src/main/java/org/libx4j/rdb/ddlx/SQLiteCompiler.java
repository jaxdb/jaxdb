/* Copyright (c) 2015 lib4j
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
import java.util.Map;

import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_columns;
import org.libx4j.rdb.ddlx.xe.$ddlx_constraints;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SQLiteCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(SQLiteCompiler.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.SQLITE;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() && !column._null$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    if (!isAutoIncrement(column))
      return null;

    final $ddlx_columns primaryKey = table._constraints(0)._primaryKey(0);
    if (primaryKey.isNull()) {
      logger.warn("AUTOINCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    if (primaryKey._column().size() > 1) {
      logger.warn("AUTOINCREMENT is not allowed for tables with composite primary keys -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    for (final $ddlx_named primaryColumn : primaryKey._column())
      if (primaryColumn._name$().text().equals(column._name$().text()))
        return "PRIMARY KEY";

    logger.warn("AUTOINCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
    return null;
  }

  @Override
  protected String createIntegerColumn(final $ddlx_integer column) {
    if (isAutoIncrement(column)) {
      if (!(column instanceof $ddlx_int))
        logger.warn("AUTOINCREMENT is only allowed on an INT column type -- Overriding to INT.");

      return "INTEGER";
    }

    return super.createIntegerColumn(column);
  }

  @Override
  protected String blockPrimaryKey(final $ddlx_table table, final $ddlx_constraints constraints, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    final $ddlx_columns primaryKey = constraints._primaryKey(0);
    if (!primaryKey.isNull() && primaryKey._column().size() == 1) {
      final $ddlx_column column = columnNameToColumn.get(primaryKey._column().get(0)._name$().text());
      if (column instanceof $ddlx_integer && isAutoIncrement(($ddlx_integer)column))
        return null;
    }

    return super.blockPrimaryKey(table, constraints, columnNameToColumn);
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $ddlx_index._type$ type, final String tableName, final $ddlx_named ... columns) {
    if ($ddlx_index._type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by SQLite's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")");
  }
}