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
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Constraints.PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxsb.runtime.BindingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class SQLiteCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(SQLiteCompiler.class);

  SQLiteCompiler() {
    super(DbVendor.SQLITE);
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  StringBuilder $null(final StringBuilder b, final $Table table, final $Column column) {
    if (column.getNull$() != null && !column.getNull$().text())
      b.append(" NOT NULL");

    return b;
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final $Table table, final $Integer column) {
    if (!Generator.isAuto(column))
      return null;

    final PrimaryKey primaryKey;
    if (table.getConstraints() == null || (primaryKey = table.getConstraints().getPrimaryKey()) == null) {
      if (logger.isWarnEnabled()) { logger.warn("AUTO_INCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTO_INCREMENT spec."); }
      return null;
    }

    final BindingList<? extends $Named> primaryColumns = primaryKey.getColumn();
    final int i$ = primaryColumns.size();
    if (i$ > 1) {
      if (logger.isWarnEnabled()) { logger.warn("AUTO_INCREMENT is not allowed for tables with composite primary keys -- Ignoring AUTO_INCREMENT spec."); }
      return null;
    }

    for (int i = 0; i < i$; ++i) { // [RA]
      final $Named primaryColumn = primaryColumns.get(i);
      if (primaryColumn.getName$().text().equals(column.getName$().text())) {
        final String min = getAttr("min", column);
        if (min != null && logger.isWarnEnabled())
          logger.warn("AUTO_INCREMENT does not consider min=\"" + min + "\" -- Ignoring min spec.");

        final String max = getAttr("max", column);
        if (max != null && logger.isWarnEnabled())
          logger.warn("AUTO_INCREMENT does not consider max=\"" + max + "\" -- Ignoring max spec.");

        final String _default = getAttr("default", column);
        if (_default != null && logger.isWarnEnabled())
          logger.warn("AUTO_INCREMENT does not consider default=\"" + _default + "\" -- Ignoring default spec.");

        return "PRIMARY KEY";
      }
    }

    if (logger.isWarnEnabled()) { logger.warn("AUTO_INCREMENT is only allowed on an INT PRIMARY KEY -- Ignoring AUTO_INCREMENT spec."); }
    return null;
  }

  @Override
  StringBuilder primaryKey(final StringBuilder b, final $Table table, final int[] columns, final PrimaryKey.Using$ using) {
    return super.primaryKey(b, table, columns, null);
  }

  @Override
  StringBuilder createIntegerColumn(final StringBuilder b, final $Integer column) {
    if (Generator.isAuto(column)) {
      if (!(column instanceof $Int) && logger.isWarnEnabled())
        logger.warn("AUTOINCREMENT is only allowed on an INT column type -- Overriding to INT.");

      return b.append("INTEGER");
    }

    return super.createIntegerColumn(b, column);
  }

  @Override
  StringBuilder blockPrimaryKey(final StringBuilder b, final $Table table, final $Constraints constraints, final Map<String,ColumnRef> columnNameToColumn) throws GeneratorExecutionException {
    final PrimaryKey primaryKey = constraints.getPrimaryKey();
    if (primaryKey != null && primaryKey.getColumn().size() == 1) {
      final ColumnRef ref = columnNameToColumn.get(primaryKey.getColumn().get(0).getName$().text());
      if (Generator.isAuto(ref.column))
        return null;
    }

    return super.blockPrimaryKey(b, table, constraints, columnNameToColumn);
  }

  @Override
  StringBuilder dropIndexOnClause(final $Table table) {
    return q(new StringBuilder(" ON "), table.getName$().text());
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $IndexType type, final String tableName, final $Named ... columns) {
    if ($IndexType.HASH.text().equals(type.text()) && logger.isWarnEnabled())
      logger.warn("HASH index type specification is not explicitly supported by SQLite's CREATE INDEX syntax. Creating index with default type.");

    final StringBuilder b = new StringBuilder("CREATE ");
    if (unique)
      b.append("UNIQUE ");

    b.append("INDEX ");
    q(b, indexName).append(" ON ");
    q(b, tableName).append(" (").append(SQLDataTypes.csvNames(getDialect(), columns)).append(')');
    return new CreateStatement(b.toString());
  }
}