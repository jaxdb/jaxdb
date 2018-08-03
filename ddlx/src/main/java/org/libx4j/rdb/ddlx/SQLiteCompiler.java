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

import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Column;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Columns;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Constraints;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Index;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Int;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Integer;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Named;
import org.libx4j.rdb.ddlx_0_9_9.xLzgluGCXYYJc.$Table;
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
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null && !column.getNull$().text() ? "NOT NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    if (!isAutoIncrement(column))
      return null;

    final $Columns primaryKey = table.getConstraints().getPrimaryKey();
    if (primaryKey == null) {
      logger.warn("AUTOINCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    if (primaryKey.getColumn().size() > 1) {
      logger.warn("AUTOINCREMENT is not allowed for tables with composite primary keys -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    for (final $Named primaryColumn : primaryKey.getColumn())
      if (primaryColumn.getName$().text().equals(column.getName$().text()))
        return "PRIMARY KEY";

    logger.warn("AUTOINCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
    return null;
  }

  @Override
  protected String createIntegerColumn(final $Integer column) {
    if (isAutoIncrement(column)) {
      if (!(column instanceof $Int))
        logger.warn("AUTOINCREMENT is only allowed on an INT column type -- Overriding to INT.");

      return "INTEGER";
    }

    return super.createIntegerColumn(column);
  }

  @Override
  protected String blockPrimaryKey(final $Table table, final $Constraints constraints, final Map<String,$Column> columnNameToColumn) throws GeneratorExecutionException {
    final $Columns primaryKey = constraints.getPrimaryKey();
    if (primaryKey != null && primaryKey.getColumn().size() == 1) {
      final $Column column = columnNameToColumn.get(primaryKey.getColumn().get(0).getName$().text());
      if (column instanceof $Integer && isAutoIncrement(($Integer)column))
        return null;
    }

    return super.blockPrimaryKey(table, constraints, columnNameToColumn);
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return " ON " + q(table.getName$().text());
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    if ($Index.Type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by SQLite's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getVendor().getDialect(), columns) + ")");
  }
}