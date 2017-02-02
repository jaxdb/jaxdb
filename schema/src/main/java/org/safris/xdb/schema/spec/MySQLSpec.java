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
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;

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

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE" : "FLOAT" + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareBinary(final boolean varying, final int length) {
    return "BIT" + (varying ? " VARYING" : "") + "(" + length + ")";
  }

  @Override
  public String declareChar(final boolean varying, final int length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final int length) {
    return "CLOB(" + length + ")";
  }

  @Override
  public String declareBlob(final int length) {
    return "BLOB" + "(" + length + ")";
  }

  @Override
  public String declareDecimal(final short precision, final short scale, final boolean unsigned) {
    SQLDataTypes.checkValidNumber(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")" + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "DATETIME(" + Math.max(0, precision - 6) + ")";
  }

  @Override
  public String declareTime(final short precision) {
    return "TIME(" + Math.max(0, precision - 6) + ")";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareInt8(final short precision, final boolean unsigned) {
    return "TINYINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt16(final short precision, final boolean unsigned) {
    return "SMALLINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt24(final short precision, final boolean unsigned) {
    return "MEDIUMINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    return "INTEGER(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareEnum(final $xds_table table, final $xds_enum type) {
    if (type._values$().isNull())
      return "ENUM()";

    final List<String> enums = parseEnum(type._values$().text());
    final StringBuilder builder = new StringBuilder("ENUM(");
    for (final String value : enums)
      builder.append(", '").append(value).append("'");

    return builder.append(")").substring(2);
  }
}