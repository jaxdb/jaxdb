/* Copyright (c) 2015 JAX-DB
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

package org.jaxdb.ddlx;

import java.sql.Connection;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxdb.ddlx.Generator.ColumnRef;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SQLiteCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(SQLiteCompiler.class);

  SQLiteCompiler() {
    super(DBVendor.SQLITE);
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  String $null(final $Table table, final $Column column) {
    return column.getNull$() != null && !column.getNull$().text() ? "NOT NULL" : "";
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    if (!Generator.isAuto(column))
      return null;

    final $Columns primaryKey;
    if (table.getConstraints() == null || (primaryKey = table.getConstraints().getPrimaryKey()) == null) {
      logger.warn("AUTO_INCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTO_INCREMENT spec.");
      return null;
    }

    if (primaryKey.getColumn().size() > 1) {
      logger.warn("AUTO_INCREMENT is not allowed for tables with composite primary keys -- Ignoring AUTO_INCREMENT spec.");
      return null;
    }

    for (final $Named primaryColumn : primaryKey.getColumn()) {
      if (primaryColumn.getName$().text().equals(column.getName$().text())) {
        final String min = getAttr("min", column);
        if (min != null)
          logger.warn("AUTO_INCREMENT does not consider min=\"" + min + "\" -- Ignoring min spec.");

        final String max = getAttr("max", column);
        if (max != null)
          logger.warn("AUTO_INCREMENT does not consider max=\"" + max + "\" -- Ignoring max spec.");

        final String _default = getAttr("default", column);
        if (_default != null)
          logger.warn("AUTO_INCREMENT does not consider default=\"" + _default + "\" -- Ignoring default spec.");

        return "PRIMARY KEY";
      }
    }

    logger.warn("AUTO_INCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTO_INCREMENT spec.");
    return null;
  }

  @Override
  String createIntegerColumn(final $Integer column) {
    if (Generator.isAuto(column)) {
      if (!(column instanceof $Int))
        logger.warn("AUTOINCREMENT is only allowed on an INT column type -- Overriding to INT.");

      return "INTEGER";
    }

    return super.createIntegerColumn(column);
  }

  @Override
  String blockPrimaryKey(final $Table table, final $Constraints constraints, final Map<String,ColumnRef> columnNameToColumn) throws GeneratorExecutionException {
    final $Columns primaryKey = constraints.getPrimaryKey();
    if (primaryKey != null && primaryKey.getColumn().size() == 1) {
      final ColumnRef ref = columnNameToColumn.get(primaryKey.getColumn().get(0).getName$().text());
      if (Generator.isAuto(ref.column))
        return null;
    }

    return super.blockPrimaryKey(table, constraints, columnNameToColumn);
  }

  @Override
  String dropIndexOnClause(final $Table table) {
    return " ON " + q(table.getName$().text());
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    if ($Index.Type$.HASH.text().equals(type.text()))
      logger.warn("HASH index type specification is not explicitly supported by SQLite's CREATE INDEX syntax. Creating index with default type.");

    return new CreateStatement("CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + q(indexName) + " ON " + q(tableName) + " (" + SQLDataTypes.csvNames(getDialect(), columns) + ")");
  }
}