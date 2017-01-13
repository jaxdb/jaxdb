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

import org.safris.xdb.schema.SQLDataTypes;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;

public final class MySQLSpec extends SQLSpec {
  @Override
  public List<String> triggers(final $xds_table table) {
    if (table._triggers() == null)
      return super.triggers(table);

    final String tableName = table._name$().text();
    final List<$xds_table._triggers._trigger> triggers = table._triggers().get(0)._trigger();
    final List<String> statements = new ArrayList<String>();
    for (final $xds_table._triggers._trigger trigger : triggers) {
      String buffer = "";
      for (final String action : trigger._actions$().text()) {
        buffer += "DELIMITER |\n";
        buffer += "CREATE TRIGGER " + SQLDataTypes.getTriggerName(tableName, trigger, action) + " " + trigger._time$().text() + " " + action + " ON " + tableName + "\n";
        buffer += "  FOR EACH ROW\n";
        buffer += "  BEGIN\n";

        final String text = trigger.text().toString();
        // FIXME: This does not work because the whitespace is trimmed before we can check it
        int i = 0, j = -1;
        while (i < text.length()) {
          char c = text.charAt(i++);
          if (c == '\n' || c == '\r')
            continue;

          j++;
          if (c != ' ' && c != '\t')
            break;
        }

        buffer += "    " + text.trim().replace("\n" + text.substring(0, j), "\n    ") + "\n";
        buffer += "  END;\n";
        buffer += "|\n";
        buffer += "DELIMITER";
      }

      statements.add(buffer.toString());
    }

    statements.addAll(super.triggers(table));
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
    final int noBytes = SQLDataTypes.getNumericByteCount(type._precision$().text(), type._unsigned$().text(), type._min$().text(), type._max$().text());
    String sql = "";
    if (noBytes == 1) // 2^8 = 256
      sql += "TINYINT";
    else if (noBytes == 2) // 2^16 = 65536
      sql += "SMALLINT";
    else if (noBytes == 3) // 2^24 = 16777216
      sql += "MEDIUMINT";
    else if (noBytes == 4) // 2^32 = 4294967296
      sql += "INTEGER";
    else
      sql += "BIGINT";

    if (!type._precision$().isNull())
      sql += "(" + type._precision$().text() + ")";

    if (!type._unsigned$().isNull() && type._unsigned$().text())
      sql += " UNSIGNED";

    return sql;
  }

  @Override
  public String type(final $xds_table table, final $xds_float type) {
    String sql = type._double$().text() ? "DOUBLE" : "FLOAT";
    sql += "(" + type._precision$().text() + ")";

    if (!type._unsigned$().isNull() && type._unsigned$().text())
      sql += " UNSIGNED";

    return sql;
  }

  @Override
  public String type(final $xds_table table, final $xds_decimal type) {
    SQLDataTypes.checkValidNumber(type._name$().text(), type._precision$().text(), type._decimal$().text());
    String sql = "DECIMAL(" + type._precision$().text() + ", " + type._decimal$().text() + ")";
    if (!type._unsigned$().isNull() && type._unsigned$().text())
      sql += " UNSIGNED";

    return sql;
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
    return "DATETIME(" + type._precision$().text() + ")";
  }

  @Override
  public String type(final $xds_table table, final $xds_boolean type) {
    return "BOOLEAN";
  }

  @Override
  public String type(final $xds_table table, final $xds_enum type) {
    if (type._values$().isNull())
      return "ENUM()";

    final List<String> enums = parseEnum(type._values$().text());
    final StringBuilder builder = new StringBuilder("ENUM(");
    for (final String value : enums)
      builder.append(", '").append(value).append("'");

    return builder.append(")").substring(2);
  }

  @Override
  public String $null(final $xds_table table, final $xds_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  @Override
  public String $autoIncrement(final $xds_table table, final $xds_integer column) {
    return !column._generateOnInsert$().isNull() && $xds_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? $xds_integer._generateOnInsert$.AUTO_5FINCREMENT.text() : "";
  }

  @Override
  protected String dropIndexOnClause(final $xds_table table) {
    return " ON " + table._name$().text();
  }

  @Override
  protected String createIndex(final boolean unique, final String indexName, final String type, final String tableName, final $xds_named ... columns) {
    return "CREATE " + (unique ? "UNIQUE " : "") + "INDEX " + indexName + " USING " + type + " ON " + tableName + " (" + SQLDataTypes.csvNames(columns) + ")";
  }
}