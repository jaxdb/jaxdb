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

package org.safris.dbb.ddlx.spec;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.safris.dbb.ddlx.SQLDataTypes;
import org.safris.dbb.ddlx.xe.$ddlx_column;
import org.safris.dbb.ddlx.xe.$ddlx_enum;
import org.safris.dbb.ddlx.xe.$ddlx_index;
import org.safris.dbb.ddlx.xe.$ddlx_integer;
import org.safris.dbb.ddlx.xe.$ddlx_named;
import org.safris.dbb.ddlx.xe.$ddlx_table;
import org.safris.maven.common.Log;

public final class PostgreSQLSpec extends SQLSpec {
  @Override
  public void init(final Connection connection) throws SQLException {
  }

  public static String getTypeName(final String tableName, final String columnName) {
    return "ty_" + tableName + "_" + columnName;
  }

  @Override
  public List<String> drops(final $ddlx_table table) {
    final List<String> statements = super.drops(table);
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_enum) {
          statements.add("DROP TYPE IF EXISTS " + getTypeName(table._name$().text(), (($ddlx_enum)column)._name$().text()));
        }
        else if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add("DROP SEQUENCE IF EXISTS " + SQLDataTypes.getSequenceName(table, type));
        }
      }
    }

    return statements;
  }

  @Override
  public List<String> types(final $ddlx_table table) {
    final List<String> statements = new ArrayList<String>();
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        if (column instanceof $ddlx_enum) {
          final $ddlx_enum type = ($ddlx_enum)column;
          final StringBuilder sql = new StringBuilder("CREATE TYPE ").append(getTypeName(table._name$().text(), type._name$().text())).append(" AS ENUM (");
          if (!type._values$().isNull()) {
            final List<String> enums = parseEnum(type._values$().text());
            final StringBuilder builder = new StringBuilder();
            for (final String value : enums)
              builder.append(", '").append(value).append("'");

            sql.append(builder.substring(2));
          }

          statements.add(0, sql.append(")").toString());
        }
        else if (column instanceof $ddlx_integer) {
          final $ddlx_integer type = ($ddlx_integer)column;
          if (!type._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add(0, "CREATE SEQUENCE " + SQLDataTypes.getSequenceName(table, type));
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  public String $null(final $ddlx_table table, final $ddlx_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  public String $autoIncrement(final $ddlx_table table, final $ddlx_integer column) {
    return !column._generateOnInsert$().isNull() && $ddlx_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? "DEFAULT nextval('" + SQLDataTypes.getSequenceName(table, column) + "')" : "";
  }

  @Override
  public String truncate(final String tableName) {
    return "DELETE FROM " + tableName;
  }

  @Override
  protected String dropIndexOnClause(final $ddlx_table table) {
    return "";
  }

  @Override
  protected String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $ddlx_named ... columns) {
    final String uniqueClause;
    if ($ddlx_index._type$.HASH.text().equals(type)) {
      if (columns.length > 1) {
        Log.warn("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition.");
        return "";
      }

      if (unique) {
        Log.warn("UNIQUE HASH indexes are not supported by PostgreSQL. Creating index non-UNIQUE index.");
      }

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    return "CREATE " + uniqueClause + "INDEX " + indexName + " ON " + tableName + " USING " + type + " (" + SQLDataTypes.csvNames(columns) + ")";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE PRECISION" : "REAL";
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareBinary(final boolean varying, final long length) {
    return "BYTEA";
  }

  @Override
  public String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final long length) {
    return "TEXT";
  }

  @Override
  public String declareBlob(final long length) {
    return "BYTEA";
  }

  @Override
  public String declareDecimal(final short precision, final short scale, final boolean unsigned) {
    SQLDataTypes.checkValidNumber(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "TIMESTAMP(" + precision + ")";
  }

  @Override
  public String declareTime(final short precision) {
    return "TIME";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareInt8(final short precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  public String declareInt16(final short precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    return "INT";
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    return getTypeName(table._name$().text(), type._name$().text());
  }
}