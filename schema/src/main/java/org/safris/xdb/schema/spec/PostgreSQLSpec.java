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

package org.safris.xdb.schema.spec;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.safris.xdb.schema.SQLDataTypes;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_columnCommon;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_index;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;

public final class PostgreSQLSpec extends SQLSpec {
  private static final Logger logger = Logger.getLogger(PostgreSQLSpec.class.getName());

  public static String getTypeName(final String tableName, final String columnName) {
    return "ty_" + tableName + "_" + columnName;
  }

  @Override
  public List<String> drops(final $xds_table table) {
    final List<String> statements = super.drops(table);
    if (table._column() != null) {
      for (final $xds_columnCommon column : table._column()) {
        if (column instanceof $xds_enum) {
          statements.add("DROP TYPE IF EXISTS " + getTypeName(table._name$().text(), (($xds_enum)column)._name$().text()));
        }
        else if (column instanceof $xds_integer) {
          final $xds_integer type = ($xds_integer)column;
          if (!type._generateOnInsert$().isNull() && $xds_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add("DROP SEQUENCE " + SQLDataTypes.getSequenceName(table, type));
        }
      }
    }

    return statements;
  }

  @Override
  public List<String> types(final $xds_table table) {
    final List<String> statements = new ArrayList<String>();
    if (table._column() != null) {
      for (final $xds_columnCommon column : table._column()) {
        if (column instanceof $xds_enum) {
          final $xds_enum type = ($xds_enum)column;
          String sql = "CREATE TYPE " + getTypeName(table._name$().text(), type._name$().text()) + " AS ENUM (";
          if (!type._values$().isNull()) {
            String values = "";
            for (final String value : type._values$().text())
              values += ", '" + SQLDataTypes.toEnumValue(value) + "'";

            sql += values.substring(2);
          }

          sql += ")";
          statements.add(0, sql);
        }
        else if (column instanceof $xds_integer) {
          final $xds_integer type = ($xds_integer)column;
          if (!type._generateOnInsert$().isNull() && $xds_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(type._generateOnInsert$().text()))
            statements.add(0, "CREATE SEQUENCE " + SQLDataTypes.getSequenceName(table, type));
        }
      }
    }

    statements.addAll(super.types(table));
    return statements;
  }

  @Override
  public String type(final $xds_table table, final $xds_char type) {
    return (type._national$().text() ? "N" : "") + (type._varying$().text() ? "VARCHAR" : "CHAR") + "(" + type._length$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_clob type) {
    return (type._national$().text() ? "NCLOB" : "CLOB") + "(" + type._length$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_binary type) {
    return "BIT" + (type._varying$().text() ? " VARYING" : "") + "(" + type._length$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_blob type) {
    return "BLOB" + "(" + type._length$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_integer type) {
    final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), false, type._min$().text(), type._max$().text());
    if (noBytes == 1) // 2^8 = 256
      return "SMALLINT";

    if (noBytes == 2) // 2^16 = 65536
      return "SMALLINT";

    if (noBytes == 3) // 2^24 = 16777216
      return "INTEGER";

    if (noBytes == 4) // 2^32 = 4294967296
      return "INTEGER";

    return "BIGINT";
  }

  @Override
  public String type(final $xds_table table, final $xds_float type) {
    return (type._double$().text() ? "DOUBLE PRECISION" : "REAL") + "(" + type._precision$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_decimal type) {
    SQLDataTypes.checkValidNumber(type._name$().text(), type._precision$().text(), type._decimal$().text());
    return "DECIMAL(" + type._precision$().text() + ", " + type._decimal$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_date type) {
    return "DATE";
  }

  @Override
  public String type(final $xds_table table, final $xds_time type) {
    return "TIME";
  }

  @Override
  public String type(final $xds_table table, final $xds_dateTime type) {
    return "TIMESTAMP(" + type._precision$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_boolean type) {
    return "BOOLEAN";
  }

  @Override
  public String type(final $xds_table table, final $xds_enum type) {
    return getTypeName(table._name$().text(), type._name$().text());
  }

  @Override
  public String $null(final $xds_table table, final $xds_columnCommon column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  public String $autoIncrement(final $xds_table table, final $xds_integer column) {
    return !column._generateOnInsert$().isNull() && $xds_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? "DEFAULT nextval('" + SQLDataTypes.getSequenceName(table, column) + "')" : "";
  }

  @Override
  protected String dropIndexOnClause(final $xds_table table) {
    return "";
  }

  @Override
  protected String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $xds_named ... columns) {
    final String uniqueClause;
    if ($xds_index._type$.HASH.text().equals(type)) {
      if (columns.length > 1) {
        logger.warning("Composite HASH indexes are not supported by PostgreSQL. Skipping index definition.");
        return "";
      }

      if (unique) {
        logger.warning("UNIQUE HASH indexes are not supported by PostgreSQL. Creating index non-UNIQUE index.");
      }

      uniqueClause = "";
    }
    else {
      uniqueClause = unique ? "UNIQUE " : "";
    }

    return "CREATE " + uniqueClause + "INDEX " + indexName + " ON " + tableName + " USING " + type + " (" + SQLDataTypes.csvNames(columns) + ")";
  }
}