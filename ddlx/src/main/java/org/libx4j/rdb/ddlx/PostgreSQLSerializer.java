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

import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_index;
import org.libx4j.rdb.ddlx.xe.$ddlx_integer;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PostgreSQLSerializer extends Serializer {
  private static final Logger logger = LoggerFactory.getLogger(PostgreSQLSerializer.class);

  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  protected void init(final Connection connection) throws SQLException {
  }

  @Override
  protected List<DropStatement> drops(final $ddlx_table table) {
    final List<DropStatement> statements = super.drops(table);
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_enum) {
          statements.add(new DropStatement("DROP TYPE IF EXISTS " + Dialect.getTypeName(table._name$().text(), (($ddlx_enum)column)._name$().text())));
        }
        else if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add(new DropStatement("DROP SEQUENCE IF EXISTS " + SQLDataTypes.getSequenceName(table, type)));
        }
      }
    }

    return statements;
  }

  @Override
  protected List<CreateStatement> types(final $ddlx_table table) {
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_enum) {
          final $ddlx_enum type = ($ddlx_enum)column;
          final StringBuilder sql = new StringBuilder("CREATE TYPE ").append(Dialect.getTypeName(table._name$().text(), type._name$().text())).append(" AS ENUM (");
          if (!type._values$().isNull()) {
            final List<String> enums = Dialect.parseEnum(type._values$().text());
            final StringBuilder builder = new StringBuilder();
            for (final String value : enums)
              builder.append(", '").append(value).append("'");

            sql.append(builder.substring(2));
          }

          statements.add(0, new CreateStatement(sql.append(")").toString()));
        }
        else if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add(0, new CreateStatement("CREATE SEQUENCE " + SQLDataTypes.getSequenceName(table, type)));
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  protected String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  protected String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return !column._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? "DEFAULT nextval('" + SQLDataTypes.getSequenceName(table, column) + "')" : "";
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return "";
  }

  @Override
  protected CreateStatement createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $ddlx_named ... columns) {
    final String uniqueClause;
    if ($ddlx_index._type$.HASH.text().equals(type)) {
      if (columns.length > 1) {
        logger.warn("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition.");
        return null;
      }

      if (unique) {
        logger.warn("UNIQUE HASH indexes are not supported by PostgreSQL. Creating index non-UNIQUE index.");
      }

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    return new CreateStatement("CREATE " + uniqueClause + "INDEX " + indexName + " ON " + tableName + " USING " + type + " (" + SQLDataTypes.csvNames(columns) + ")");
  }
}