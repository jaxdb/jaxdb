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

package org.safris.xdb.xdl.spec;

import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.xdl.$xdl_bit;
import org.safris.xdb.xdl.$xdl_blob;
import org.safris.xdb.xdl.$xdl_boolean;
import org.safris.xdb.xdl.$xdl_char;
import org.safris.xdb.xdl.$xdl_column;
import org.safris.xdb.xdl.$xdl_date;
import org.safris.xdb.xdl.$xdl_dateTime;
import org.safris.xdb.xdl.$xdl_decimal;
import org.safris.xdb.xdl.$xdl_enum;
import org.safris.xdb.xdl.$xdl_float;
import org.safris.xdb.xdl.$xdl_integer;
import org.safris.xdb.xdl.$xdl_table;
import org.safris.xdb.xdl.$xdl_time;
import org.safris.xdb.xdl.SQLDataTypes;

public class MySQLSpec extends SQLSpec {
  public List<String> triggers(final $xdl_table table) {
    if (table._triggers() == null)
      return super.triggers(table);

    final String tableName = table._name$().text();
    final List<$xdl_table._triggers._trigger> triggers = table._triggers().get(0)._trigger();
    final List<String> statements = new ArrayList<String>();
    for (final $xdl_table._triggers._trigger trigger : triggers) {
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
          //System.err.println(c);
          if (c != ' ' && c != '\t')
            break;
        }

        //System.err.println("XXXX: " + i + " " + j);
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

  public String type(final $xdl_table table, final $xdl_char type) {
    return (type._variant$().text() ? "VARCHAR" : "CHAR") + "(" + type._length$().text() + ")";
  }

  public String type(final $xdl_table table, final $xdl_bit type) {
    return "BIT(" + type._length$().text() + ")";
  }

  public final String type(final $xdl_table table, final $xdl_blob type) {
    return "BLOB(" + type._length$().text() + ")";
  }

  public String type(final $xdl_table table, final $xdl_integer type) {
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

  public String type(final $xdl_table table, final $xdl_float type) {
    String sql = type._double$().text() ? "DOUBLE" : "FLOAT";
    sql += "(" + type._precision$().text() + ")";

    if (!type._unsigned$().isNull() && type._unsigned$().text())
      sql += " UNSIGNED";

    return sql;
  }

  public String type(final $xdl_table table, final $xdl_decimal type) {
    SQLDataTypes.checkValidNumber(type._name$().text(), type._precision$().text(), type._decimal$().text());
    String sql = "DECIMAL(" + type._precision$().text() + ", " + type._decimal$().text() + ")";
    if (!type._unsigned$().isNull() && type._unsigned$().text())
      sql += " UNSIGNED";

    return sql;
  }

  public String type(final $xdl_table table, final $xdl_date type) {
    return "DATE";
  }

  public String type(final $xdl_table table, final $xdl_time type) {
    return "TIME";
  }

  public String type(final $xdl_table table, final $xdl_dateTime type) {
    return "DATETIME(" + type._precision$().text() + ")";
  }

  public String type(final $xdl_table table, final $xdl_boolean type) {
    return "BOOLEAN";
  }

  public String type(final $xdl_table table, final $xdl_enum type) {
    if (type._values$().isNull())
      return "ENUM()";

    String values = "";
    for (final String value : type._values$().text())
      values += ", '" + SQLDataTypes.toEnumValue(value) + "'";

    return "ENUM(" + values.substring(2) + ")";
  }

  public String $null(final $xdl_table table, final $xdl_column column) {
    return !column._null$().isNull() ? !column._null$().text() ? "NOT NULL" : "NULL" : "";
  }

  public String $autoIncrement(final $xdl_table table, final $xdl_integer column) {
    return !column._generateOnInsert$().isNull() && $xdl_integer._generateOnInsert$.AUTO_5FINCREMENT.text().equals(column._generateOnInsert$().text()) ? $xdl_integer._generateOnInsert$.AUTO_5FINCREMENT.text() : "";
  }
}