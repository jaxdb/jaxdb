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
import java.util.ArrayList;
import java.util.List;

import org.libx4j.rdb.ddlx.xAA.$Column;
import org.libx4j.rdb.ddlx.xAA.$Enum;
import org.libx4j.rdb.ddlx.xAA.$Index;
import org.libx4j.rdb.ddlx.xAA.$Integer;
import org.libx4j.rdb.ddlx.xAA.$Named;
import org.libx4j.rdb.ddlx.xAA.$Table;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PostgreSQLCompiler extends Compiler {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLCompiler.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<DropStatement> drops(final $Table table) {
    final List<DropStatement> statements = super.drops(table);
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Enum) {
          statements.add(new DropStatement("DROP TYPE IF EXISTS " + Dialect.getTypeName(table.getName$().text(), (($Enum)column).getName$().text())));
        }
        else if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type))
            statements.add(new DropStatement("DROP SEQUENCE IF EXISTS " + SQLDataTypes.getSequenceName(table, type)));
        }
      }
    }

    return statements;
  }

  @Override
  protected List<CreateStatement> types(final $Table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        if (column instanceof $Enum) {
          final $Enum type = ($Enum)column;
          final StringBuilder sql = new StringBuilder("CREATE TYPE ").append(Dialect.getTypeName(table.getName$().text(), type.getName$().text())).append(" AS ENUM (");
          if (type.getValues$() != null) {
            final List<String> enums = Dialect.parseEnum(type.getValues$().text());
            final StringBuilder builder = new StringBuilder();
            for (final String value : enums)
              builder.append(", '").append(value).append("'");

            sql.append(builder.substring(2));
          }

          statements.add(0, new CreateStatement(sql.append(")").toString()));
        }
        else if (column instanceof $Integer) {
          final $Integer type = ($Integer)column;
          if (isAutoIncrement(type))
            statements.add(0, new CreateStatement("CREATE SEQUENCE " + SQLDataTypes.getSequenceName(table, type)));
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  protected String $null(final $Table table, final $Column column) {
    return column.getNull$() != null ? !column.getNull$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $Table table, final $Integer column) {
    return isAutoIncrement(column) ? "DEFAULT nextval('" + SQLDataTypes.getSequenceName(table, column) + "')" : "";
  }

  @Override
  protected String dropIndexOnClause(final $Table table) {
    return "";
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final $Index.Type$ type, final String tableName, final $Named ... columns) {
    final String uniqueClause;
    if ($Index.Type$.HASH.text().equals(type.text())) {
      if (columns.length > 1) {
        logger.warn("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition.");
        return null;
      }

      if (unique)
        logger.warn("UNIQUE HASH indexes are not supported by PostgreSQL. Creating non-UNIQUE index.");

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    return new CreateStatement("CREATE " + uniqueClause + "INDEX " + indexName + " ON " + tableName + " USING " + type.text() + " (" + SQLDataTypes.csvNames(columns) + ")");
  }
}