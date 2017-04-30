/* Copyright (c) 2015 Seva Safris
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

package org.safris.rdb.ddlx;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import org.safris.rdb.ddlx.xe.$ddlx_column;
import org.safris.rdb.ddlx.xe.$ddlx_columns;
import org.safris.rdb.ddlx.xe.$ddlx_constraints;
import org.safris.rdb.ddlx.xe.$ddlx_int;
import org.safris.rdb.ddlx.xe.$ddlx_integer;
import org.safris.rdb.ddlx.xe.$ddlx_named;
import org.safris.rdb.ddlx.xe.$ddlx_table;
import org.safris.rdb.vendor.DBVendor;
import org.safris.maven.common.Log;

final class SQLiteSerializer extends Serializer {
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
    if (column._generateOnInsert$().isNull() || !$ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()))
      return null;

    final $ddlx_constraints constraints = table._constraints(0);
    final $ddlx_columns primaryKey = constraints._primaryKey(0);
    if (primaryKey.isNull()) {
      Log.warn("AUTOINCREMENT is only allowed on an INTEGER PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    if (primaryKey._column().size() > 1) {
      Log.warn("AUTOINCREMENT is not allowed for tables with composite primary keys -- Ignoring AUTOINCREMENT spec.");
      return null;
    }

    for (final $ddlx_named primaryColumn : primaryKey._column()) {
      if (primaryColumn._name$().text().equals(column._name$().text())) {
        if (!(column instanceof $ddlx_int)) {
          Log.warn("AUTOINCREMENT is only allowed on an INTEGER PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
          return null;
        }

        return "PRIMARY KEY";
      }
    }

    Log.warn("AUTOINCREMENT is only allowed on an INTEGER PRIMARY KEY -- Ignoring AUTOINCREMENT spec.");
    return null;
  }

  @Override
  protected String blockPrimaryKey(final $ddlx_table table, final $ddlx_constraints constraints, final Map<String,$ddlx_column> columnNameToColumn) throws GeneratorExecutionException {
    final $ddlx_columns primaryKey = constraints._primaryKey(0);
    if (!primaryKey.isNull() && primaryKey._column().size() == 1) {
      final $ddlx_named primaryColumn = primaryKey._column().get(0);
      final $ddlx_column column = columnNameToColumn.get(primaryColumn._name$().text());
      if (column instanceof $ddlx_int) {
        final $ddlx_int intColumn = ($ddlx_int)column;
        if (!intColumn._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(intColumn._generateOnInsert$().text()))
          return null;
      }
    }

    return super.blockPrimaryKey(table, constraints, columnNameToColumn);
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $ddlx_named ... columns) {
    return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")";
  }
}