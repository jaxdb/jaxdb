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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;

import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$IndexType;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$PrimaryKey;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.Schema;
import org.jaxsb.runtime.BindingList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

final class PostgreSQLCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLCompiler.class);

  PostgreSQLCompiler() {
    super(DbVendor.POSTGRE_SQL);
  }

  @Override
  void init(final Connection connection) {
  }

  @Override
  StringBuilder primaryKey(final StringBuilder b, final Schema.Table table, final String[] columnNames, final $PrimaryKey.Using$ using) {
    return super.primaryKey(b, table, columnNames, null);
  }

  @Override
  LinkedHashSet<DropStatement> dropTypes(final Schema.Table table, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final LinkedHashSet<DropStatement> statements = super.dropTypes(table, tableNameToEnumToOwner);
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Enum) {
          final StringBuilder b = new StringBuilder("DROP TYPE IF EXISTS \"");
          Dialect.getTypeName(b, ($Enum)column, tableNameToEnumToOwner).append('"');
          statements.add(new DropStatement(b.toString()));
        }
        else if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (Generator.isAuto(type))
            statements.add(new DropStatement(q(new StringBuilder("DROP SEQUENCE IF EXISTS "), getSequenceName(table, type)).toString()));
        }
      }
    }

    return statements;
  }

  @Override
  ArrayList<CreateStatement> types(final Schema.Table table, final HashMap<String,String> enumTemplateToValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    final ArrayList<CreateStatement> statements = new ArrayList<>();
    final BindingList<$Column> columns = table.getColumn();
    if (columns != null) {
      StringBuilder sql = null;
      for (int i = 0, i$ = columns.size(); i < i$; ++i) { // [RA]
        final $Column column = columns.get(i);
        if (column instanceof $Enum) {
          final $Enum enumColumn = ($Enum)column;
          if (sql == null)
            sql = new StringBuilder();
          else
            sql.setLength(0);

          sql.append("CREATE TYPE \"");
          Dialect.getTypeName(sql, enumColumn, tableNameToEnumToOwner).append("\" AS ENUM (");
          final String[] enums = Dialect.parseEnum(enumColumn.getValues$() != null ? enumColumn.getValues$().text() : enumTemplateToValues.get(enumColumn.getTemplate$().text()));
          for (int j = 0, j$ = enums.length; j < j$; ++j) { // [RA]
            if (j > 0)
              sql.append(", ");

            sql.append('\'').append(enums[j]).append('\'');
          }

          statements.add(0, new CreateStatement(sql.append(')').toString()));
        }
        else if (column instanceof $Integer) {
          final $Integer integer = ($Integer)column;
          if (Generator.isAuto(integer)) {
            final StringBuilder b = new StringBuilder("CREATE SEQUENCE ");
            q(b, getSequenceName(table, integer));
            final String min = getAttr("min", integer);
            if (min != null)
              b.append(" MINVALUE ").append(min);

            final String max = getAttr("max", integer);
            if (max != null)
              b.append(" MAXVALUE ").append(max);

            final String _default = getAttr("default", integer);
            if (_default != null)
              b.append(" START ").append(_default);

            b.append(" CYCLE");
            statements.add(0, new CreateStatement(b.toString()));
          }
        }
      }
    }

    statements.addAll(super.types(table, enumTemplateToValues, tableNameToEnumToOwner));
    return statements;
  }

  @Override
  StringBuilder $null(final StringBuilder b, final Schema.Table table, final $Column column) {
    if (column.getNull$() != null)
      b.append(column.getNull$().text() ? " NULL" : " NOT NULL");

    return b;
  }

  @Override
  String $autoIncrement(final LinkedHashSet<CreateStatement> alterStatements, final Schema.Table table, final $Integer column) {
    return Generator.isAuto(column) ? "DEFAULT nextval('" + getSequenceName(table, column) + "')" : null; // FIXME: StringBuilder
  }

  @Override
  StringBuilder dropIndexOnClause(final Schema.Table table) {
    return new StringBuilder(0);
  }

  @Override
  CreateStatement createIndex(final boolean unique, final String indexName, final $IndexType type, final String tableName, final $Named ... columns) {
    final String uniqueClause;
    if ($IndexType.HASH.text().equals(type.text())) {
      if (columns.length > 1) {
        if (logger.isWarnEnabled()) { logger.warn("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition."); }
        return null;
      }

      if (unique && logger.isWarnEnabled())
        logger.warn("UNIQUE HASH indexes are not supported by PostgreSQL. Creating non-UNIQUE index.");

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    final StringBuilder b = new StringBuilder("CREATE ").append(uniqueClause).append("INDEX ");
    q(b, indexName).append(" ON ");
    q(b, tableName).append(" USING ").append(type.text()).append(" (").append(SQLDataTypes.csvNames(getDialect(), columns)).append(')');
    return new CreateStatement(b.toString());
  }
}