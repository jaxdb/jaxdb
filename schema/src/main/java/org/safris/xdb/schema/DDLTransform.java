/* Copyright (c) 2011 Seva Safris
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

package org.safris.xdb.schema;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.lang.Arrays;
import org.safris.commons.lang.Numbers;
import org.safris.commons.util.MaskedEnum;
import org.safris.commons.util.TopologicalSort;
import org.safris.commons.xml.XMLException;
import org.safris.maven.common.Log;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_check;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_foreignKey;
import org.safris.xdb.xds.xe.$xds_inherited;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;
import org.safris.xdb.xds.xe.xds_schema;

public final class DDLTransform extends XDLTransformer {
  private static final Map<String,Integer> reservedWords = new HashMap<String,Integer>();

  @SuppressWarnings("unused")
  private static class SQLStandardEnum extends MaskedEnum {
    public static final SQLStandardEnum SQL92 = new SQLStandardEnum(0, "SQL-92");
    public static final SQLStandardEnum SQL99 = new SQLStandardEnum(1, "SQL-99");
    public static final SQLStandardEnum SQL2003 = new SQLStandardEnum(2, "SQL-2003");

    public static SQLStandardEnum[] toArray(final int mask) {
      return MaskedEnum.<SQLStandardEnum>toArray(SQLStandardEnum.class, mask);
    }

    private final String name;

    private SQLStandardEnum(final int index, final String name) {
      super(index);
      this.name = name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  static {
    // reserved words per SQL spec (SQL-92, SQL-99, SQL-2003)
    reservedWords.put("ABSOLUTE", 0b110);
    reservedWords.put("ACTION", 0b110);
    reservedWords.put("ADD", 0b111);
    reservedWords.put("AFTER", 0b010);
    reservedWords.put("ALL", 0b111);
    reservedWords.put("ALLOCATE", 0b111);
    reservedWords.put("ALTER", 0b111);
    reservedWords.put("AND", 0b111);
    reservedWords.put("ANY", 0b111);
    reservedWords.put("ARE", 0b111);
    reservedWords.put("ARRAY", 0b011);
    reservedWords.put("AS", 0b111);
    reservedWords.put("ASC", 0b110);
    reservedWords.put("ASENSITIVE", 0b011);
    reservedWords.put("ASSERTION", 0b110);
    reservedWords.put("ASYMMETRIC", 0b011);
    reservedWords.put("AT", 0b111);
    reservedWords.put("ATOMIC", 0b011);
    reservedWords.put("AUTHORIZATION", 0b111);
    reservedWords.put("AVG", 0b100);
    reservedWords.put("BEFORE", 0b010);
    reservedWords.put("BEGIN", 0b111);
    reservedWords.put("BETWEEN", 0b111);
    reservedWords.put("BIGINT", 0b001);
    reservedWords.put("BINARY", 0b011);
    reservedWords.put("BIT", 0b110);
    reservedWords.put("BIT_LENGTH", 0b100);
    reservedWords.put("BLOB", 0b011);
    reservedWords.put("BOOLEAN", 0b011);
    reservedWords.put("BOTH", 0b111);
    reservedWords.put("BREADTH", 0b010);
    reservedWords.put("BY", 0b111);
    reservedWords.put("CALL", 0b111);
    reservedWords.put("CALLED", 0b001);
    reservedWords.put("CASCADE", 0b110);
    reservedWords.put("CASCADED", 0b111);
    reservedWords.put("CASE", 0b111);
    reservedWords.put("CAST", 0b111);
    reservedWords.put("CATALOG", 0b110);
    reservedWords.put("CHAR", 0b111);
    reservedWords.put("CHAR_LENGTH", 0b100);
    reservedWords.put("CHARACTER", 0b111);
    reservedWords.put("CHARACTER_LENGTH", 0b100);
    reservedWords.put("CHECK", 0b111);
    reservedWords.put("CLOB", 0b011);
    reservedWords.put("CLOSE", 0b111);
    reservedWords.put("COALESCE", 0b100);
    reservedWords.put("COLLATE", 0b111);
    reservedWords.put("COLLATION", 0b110);
    reservedWords.put("COLUMN", 0b111);
    reservedWords.put("COMMIT", 0b111);
    reservedWords.put("CONDITION", 0b111);
    reservedWords.put("CONNECT", 0b111);
    reservedWords.put("CONNECTION", 0b110);
    reservedWords.put("CONSTRAINT", 0b111);
    reservedWords.put("CONSTRAINTS", 0b110);
    reservedWords.put("CONSTRUCTOR", 0b010);
    reservedWords.put("CONTAINS", 0b100);
    reservedWords.put("CONTINUE", 0b111);
    reservedWords.put("CONVERT", 0b100);
    reservedWords.put("CORRESPONDING", 0b111);
    reservedWords.put("COUNT", 0b100);
    reservedWords.put("CREATE", 0b111);
    reservedWords.put("CROSS", 0b111);
    reservedWords.put("CUBE", 0b011);
    reservedWords.put("CURRENT", 0b111);
    reservedWords.put("CURRENT_DATE", 0b111);
    reservedWords.put("CURRENT_DEFAULT_TRANSFORM_GROUP", 0b011);
    reservedWords.put("CURRENT_PATH", 0b111);
    reservedWords.put("CURRENT_ROLE", 0b011);
    reservedWords.put("CURRENT_TIME", 0b111);
    reservedWords.put("CURRENT_TIMESTAMP", 0b111);
    reservedWords.put("CURRENT_TRANSFORM_GROUP_FOR_TYPE", 0b011);
    reservedWords.put("CURRENT_USER", 0b111);
    reservedWords.put("CURSOR", 0b111);
    reservedWords.put("CYCLE", 0b011);
    reservedWords.put("DATA", 0b010);
    reservedWords.put("DATE", 0b111);
    reservedWords.put("DAY", 0b111);
    reservedWords.put("DEALLOCATE", 0b111);
    reservedWords.put("DEC", 0b111);
    reservedWords.put("DECIMAL", 0b111);
    reservedWords.put("DECLARE", 0b111);
    reservedWords.put("DEFAULT", 0b111);
    reservedWords.put("DEFERRABLE", 0b110);
    reservedWords.put("DEFERRED", 0b110);
    reservedWords.put("DELETE", 0b111);
    reservedWords.put("DEPTH", 0b010);
    reservedWords.put("DEREF", 0b011);
    reservedWords.put("DESC", 0b110);
    reservedWords.put("DESCRIBE", 0b111);
    reservedWords.put("DESCRIPTOR", 0b110);
    reservedWords.put("DETERMINISTIC", 0b111);
    reservedWords.put("DIAGNOSTICS", 0b110);
    reservedWords.put("DISCONNECT", 0b111);
    reservedWords.put("DISTINCT", 0b111);
    reservedWords.put("DO", 0b111);
    reservedWords.put("DOMAIN", 0b110);
    reservedWords.put("DOUBLE", 0b111);
    reservedWords.put("DROP", 0b111);
    reservedWords.put("DYNAMIC", 0b011);
    reservedWords.put("EACH", 0b011);
    reservedWords.put("ELEMENT", 0b001);
    reservedWords.put("ELSE", 0b111);
    reservedWords.put("ELSEIF", 0b111);
    reservedWords.put("END", 0b111);
    reservedWords.put("EQUALS", 0b010);
    reservedWords.put("ESCAPE", 0b111);
    reservedWords.put("EXCEPT", 0b111);
    reservedWords.put("EXCEPTION", 0b110);
    reservedWords.put("EXEC", 0b111);
    reservedWords.put("EXECUTE", 0b111);
    reservedWords.put("EXISTS", 0b111);
    reservedWords.put("EXIT", 0b111);
    reservedWords.put("EXTERNAL", 0b111);
    reservedWords.put("EXTRACT", 0b100);
    reservedWords.put("FALSE", 0b111);
    reservedWords.put("FETCH", 0b111);
    reservedWords.put("FILTER", 0b011);
    reservedWords.put("FIRST", 0b110);
    reservedWords.put("FLOAT", 0b111);
    reservedWords.put("FOR", 0b111);
    reservedWords.put("FOREIGN", 0b111);
    reservedWords.put("FOUND", 0b110);
    reservedWords.put("FREE", 0b011);
    reservedWords.put("FROM", 0b111);
    reservedWords.put("FULL", 0b111);
    reservedWords.put("FUNCTION", 0b111);
    reservedWords.put("GENERAL", 0b010);
    reservedWords.put("GET", 0b111);
    reservedWords.put("GLOBAL", 0b111);
    reservedWords.put("GO", 0b110);
    reservedWords.put("GOTO", 0b110);
    reservedWords.put("GRANT", 0b111);
    reservedWords.put("GROUP", 0b111);
    reservedWords.put("GROUPING", 0b011);
    reservedWords.put("HANDLER", 0b111);
    reservedWords.put("HAVING", 0b111);
    reservedWords.put("HOLD", 0b011);
    reservedWords.put("HOUR", 0b111);
    reservedWords.put("IDENTITY", 0b111);
    reservedWords.put("IF", 0b111);
    reservedWords.put("IMMEDIATE", 0b111);
    reservedWords.put("IN", 0b111);
    reservedWords.put("INDICATOR", 0b111);
    reservedWords.put("INITIALLY", 0b110);
    reservedWords.put("INNER", 0b111);
    reservedWords.put("INOUT", 0b111);
    reservedWords.put("INPUT", 0b111);
    reservedWords.put("INSENSITIVE", 0b111);
    reservedWords.put("INSERT", 0b111);
    reservedWords.put("INT", 0b111);
    reservedWords.put("INTEGER", 0b111);
    reservedWords.put("INTERSECT", 0b111);
    reservedWords.put("INTERVAL", 0b111);
    reservedWords.put("INTO", 0b111);
    reservedWords.put("IS", 0b111);
    reservedWords.put("ISOLATION", 0b110);
    reservedWords.put("ITERATE", 0b011);
    reservedWords.put("JOIN", 0b111);
    reservedWords.put("KEY", 0b110);
    reservedWords.put("LANGUAGE", 0b111);
    reservedWords.put("LARGE", 0b011);
    reservedWords.put("LAST", 0b110);
    reservedWords.put("LATERAL", 0b011);
    reservedWords.put("LEADING", 0b111);
    reservedWords.put("LEAVE", 0b111);
    reservedWords.put("LEFT", 0b111);
    reservedWords.put("LEVEL", 0b110);
    reservedWords.put("LIKE", 0b111);
    reservedWords.put("LOCAL", 0b111);
    reservedWords.put("LOCALTIME", 0b011);
    reservedWords.put("LOCALTIMESTAMP", 0b011);
    reservedWords.put("LOCATOR", 0b010);
    reservedWords.put("LOOP", 0b111);
    reservedWords.put("LOWER", 0b100);
    reservedWords.put("MAP", 0b010);
    reservedWords.put("MATCH", 0b111);
    reservedWords.put("MAX", 0b100);
    reservedWords.put("MEMBER", 0b001);
    reservedWords.put("MERGE", 0b001);
    reservedWords.put("METHOD", 0b011);
    reservedWords.put("MIN", 0b100);
    reservedWords.put("MINUTE", 0b111);
    reservedWords.put("MODIFIES", 0b011);
    reservedWords.put("MODULE", 0b111);
    reservedWords.put("MONTH", 0b111);
    reservedWords.put("MULTISET", 0b001);
    reservedWords.put("NAMES", 0b110);
    reservedWords.put("NATIONAL", 0b111);
    reservedWords.put("NATURAL", 0b111);
    reservedWords.put("NCHAR", 0b111);
    reservedWords.put("NCLOB", 0b011);
    reservedWords.put("NEW", 0b011);
    reservedWords.put("NEXT", 0b110);
    reservedWords.put("NO", 0b111);
    reservedWords.put("NONE", 0b011);
    reservedWords.put("NOT", 0b111);
    reservedWords.put("NULL", 0b111);
    reservedWords.put("NULLIF", 0b100);
    reservedWords.put("NUMERIC", 0b111);
    reservedWords.put("OBJECT", 0b010);
    reservedWords.put("OCTET_LENGTH", 0b100);
    reservedWords.put("OF", 0b111);
    reservedWords.put("OLD", 0b011);
    reservedWords.put("ON", 0b111);
    reservedWords.put("ONLY", 0b111);
    reservedWords.put("OPEN", 0b111);
    reservedWords.put("OPTION", 0b110);
    reservedWords.put("OR", 0b111);
    reservedWords.put("ORDER", 0b111);
    reservedWords.put("ORDINALITY", 0b010);
    reservedWords.put("OUT", 0b111);
    reservedWords.put("OUTER", 0b111);
    reservedWords.put("OUTPUT", 0b111);
    reservedWords.put("OVER", 0b011);
    reservedWords.put("OVERLAPS", 0b111);
    reservedWords.put("PAD", 0b110);
    reservedWords.put("PARAMETER", 0b111);
    reservedWords.put("PARTIAL", 0b110);
    reservedWords.put("PARTITION", 0b011);
    reservedWords.put("PATH", 0b110);
    reservedWords.put("POSITION", 0b100);
    reservedWords.put("PRECISION", 0b111);
    reservedWords.put("PREPARE", 0b111);
    reservedWords.put("PRESERVE", 0b110);
    reservedWords.put("PRIMARY", 0b111);
    reservedWords.put("PRIOR", 0b110);
    reservedWords.put("PRIVILEGES", 0b110);
    reservedWords.put("PROCEDURE", 0b111);
    reservedWords.put("PUBLIC", 0b110);
    reservedWords.put("RANGE", 0b011);
    reservedWords.put("READ", 0b110);
    reservedWords.put("READS", 0b011);
    reservedWords.put("REAL", 0b111);
    reservedWords.put("RECURSIVE", 0b011);
    reservedWords.put("REF", 0b011);
    reservedWords.put("REFERENCES", 0b111);
    reservedWords.put("REFERENCING", 0b011);
    reservedWords.put("RELATIVE", 0b110);
    reservedWords.put("RELEASE", 0b011);
    reservedWords.put("REPEAT", 0b111);
    reservedWords.put("RESIGNAL", 0b111);
    reservedWords.put("RESTRICT", 0b110);
    reservedWords.put("RESULT", 0b011);
    reservedWords.put("RETURN", 0b111);
    reservedWords.put("RETURNS", 0b111);
    reservedWords.put("REVOKE", 0b111);
    reservedWords.put("RIGHT", 0b111);
    reservedWords.put("ROLE", 0b010);
    reservedWords.put("ROLLBACK", 0b111);
    reservedWords.put("ROLLUP", 0b011);
    reservedWords.put("ROUTINE", 0b110);
    reservedWords.put("ROW", 0b011);
    reservedWords.put("ROWS", 0b111);
    reservedWords.put("SAVEPOINT", 0b011);
    reservedWords.put("SCHEMA", 0b110);
    reservedWords.put("SCOPE", 0b011);
    reservedWords.put("SCROLL", 0b111);
    reservedWords.put("SEARCH", 0b011);
    reservedWords.put("SECOND", 0b111);
    reservedWords.put("SECTION", 0b110);
    reservedWords.put("SELECT", 0b111);
    reservedWords.put("SENSITIVE", 0b011);
    reservedWords.put("SESSION", 0b110);
    reservedWords.put("SESSION_USER", 0b111);
    reservedWords.put("SET", 0b111);
    reservedWords.put("SETS", 0b010);
    reservedWords.put("SIGNAL", 0b111);
    reservedWords.put("SIMILAR", 0b011);
    reservedWords.put("SIZE", 0b110);
    reservedWords.put("SMALLINT", 0b111);
    reservedWords.put("SOME", 0b111);
    reservedWords.put("SPACE", 0b110);
    reservedWords.put("SPECIFIC", 0b111);
    reservedWords.put("SPECIFICTYPE", 0b011);
    reservedWords.put("SQL", 0b111);
    reservedWords.put("SQLCODE", 0b100);
    reservedWords.put("SQLERROR", 0b100);
    reservedWords.put("SQLEXCEPTION", 0b111);
    reservedWords.put("SQLSTATE", 0b111);
    reservedWords.put("SQLWARNING", 0b111);
    reservedWords.put("START", 0b011);
    reservedWords.put("STATE", 0b010);
    reservedWords.put("STATIC", 0b011);
    reservedWords.put("SUBMULTISET", 0b001);
    reservedWords.put("SUBSTRING", 0b100);
    reservedWords.put("SUM", 0b100);
    reservedWords.put("SYMMETRIC", 0b011);
    reservedWords.put("SYSTEM", 0b011);
    reservedWords.put("SYSTEM_USER", 0b111);
    reservedWords.put("TABLE", 0b111);
    reservedWords.put("TABLESAMPLE", 0b001);
    reservedWords.put("TEMPORARY", 0b110);
    reservedWords.put("THEN", 0b111);
    reservedWords.put("TIME", 0b111);
    reservedWords.put("TIMESTAMP", 0b111);
    reservedWords.put("TIMEZONE_HOUR", 0b111);
    reservedWords.put("TIMEZONE_MINUTE", 0b111);
    reservedWords.put("TO", 0b111);
    reservedWords.put("TRAILING", 0b111);
    reservedWords.put("TRANSACTION", 0b110);
    reservedWords.put("TRANSLATE", 0b100);
    reservedWords.put("TRANSLATION", 0b111);
    reservedWords.put("TREAT", 0b011);
    reservedWords.put("TRIGGER", 0b011);
    reservedWords.put("TRIM", 0b100);
    reservedWords.put("TRUE", 0b111);
    reservedWords.put("UNDER", 0b010);
    reservedWords.put("UNDO", 0b111);
    reservedWords.put("UNION", 0b111);
    reservedWords.put("UNIQUE", 0b111);
    reservedWords.put("UNKNOWN", 0b111);
    reservedWords.put("UNNEST", 0b011);
    reservedWords.put("UNTIL", 0b111);
    reservedWords.put("UPDATE", 0b111);
    reservedWords.put("UPPER", 0b100);
    reservedWords.put("USAGE", 0b110);
    reservedWords.put("USER", 0b111);
    reservedWords.put("USING", 0b111);
    reservedWords.put("VALUE", 0b111);
    reservedWords.put("VALUES", 0b111);
    reservedWords.put("VARCHAR", 0b111);
    reservedWords.put("VARYING", 0b111);
    reservedWords.put("VIEW", 0b110);
    reservedWords.put("WHEN", 0b111);
    reservedWords.put("WHENEVER", 0b111);
    reservedWords.put("WHERE", 0b111);
    reservedWords.put("WHILE", 0b111);
    reservedWords.put("WINDOW", 0b011);
    reservedWords.put("WITH", 0b111);
    reservedWords.put("WITHIN", 0b011);
    reservedWords.put("WITHOUT", 0b011);
    reservedWords.put("WORK", 0b110);
    reservedWords.put("WRITE", 0b110);
    reservedWords.put("YEAR", 0b111);
    reservedWords.put("ZONE", 0b110);
  }

  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      final String vendors = Arrays.toString(DBVendor.values(), "|");
      System.err.println("<" + vendors + "> <XDL_FILE>");
      System.exit(1);
    }

    createDDL(new File(args[1]).toURI().toURL(), DBVendor.parse(args[0]), null);
  }

  public static DDL[] createDDL(final URL url, final DBVendor vendor, final File outDir) throws IOException, XMLException {
    return DDLTransform.createDDL(parseArguments(url, outDir), vendor, outDir);
  }

  public static DDL[] createDDL(final xds_schema database, final DBVendor vendor, final File outDir) {
    final DDLTransform creator = new DDLTransform(database);
    final DDL[] ddls = creator.parse(vendor);
    final StringBuilder sql = new StringBuilder();
    for (int i = ddls.length - 1; i >= 0; --i)
      if (ddls[i].drop != null)
        for (final String drop : ddls[i].drop)
          sql.append(drop).append(";\n");

    if (sql.length() > 0)
      sql.append("\n");

    for (final DDL ddl : ddls)
      for (final String create : ddl.create)
        sql.append(create).append(";\n\n");

    final String out = vendor == DBVendor.DERBY ? "CREATE SCHEMA " + database._name$().text() + ";\n\n" + sql : sql.toString();
    writeOutput(out, outDir != null ? new File(outDir, creator.merged._name$().text() + ".sql") : null);
    return ddls;
  }

  public static DDLTransform transformDDL(final URL url) throws IOException, XMLException {
    final xds_schema database = parseArguments(url, null);
    return new DDLTransform(database);
  }

  private static void checkName(String string) {
    string = string.toUpperCase();

    final Integer mask = reservedWords.get(string);
    if (mask == null)
      return;

    final SQLStandardEnum[] enums = SQLStandardEnum.toArray(mask);
    String message = "The name '" + string + "' is reserved word in " + enums[0];

    for (int i = 1; i < enums.length; i++)
      message += ", " + enums[i];

    message += ".";
    Log.warn(message);
  }

  private DDLTransform(final xds_schema database) {
    super(database);
  }

  private final Map<String,Integer> columnCount = new HashMap<String,Integer>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static String parseColumn(final $xds_table table, final $xds_column column, final DBVendor vendor) {
    final StringBuilder ddl = new StringBuilder();
    ddl.append(column._name$().text()).append(" ");
    if (column instanceof $xds_char) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_char)column));
    }
    else if (column instanceof $xds_clob) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_clob)column));
    }
    else if (column instanceof $xds_binary) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_binary)column));
    }
    else if (column instanceof $xds_blob) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_blob)column));
    }
    else if (column instanceof $xds_integer) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_integer)column));
    }
    else if (column instanceof $xds_float) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_float)column));
    }
    else if (column instanceof $xds_decimal) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_decimal)column));
    }
    else if (column instanceof $xds_date) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_date)column));
    }
    else if (column instanceof $xds_time) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_time)column));
    }
    else if (column instanceof $xds_dateTime) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_dateTime)column));
    }
    else if (column instanceof $xds_boolean) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_boolean)column));
    }
    else if (column instanceof $xds_enum) {
      ddl.append(vendor.getSQLSpec().type(table, ($xds_enum)column));
    }

    final String defaultFragement = vendor.getSQLSpec().$default(table, column);
    if (defaultFragement != null && defaultFragement.length() > 0)
      ddl.append(" DEFAULT ").append(defaultFragement);

    final String nullFragment = vendor.getSQLSpec().$null(table, column);
    if (nullFragment != null && nullFragment.length() > 0)
      ddl.append(" ").append(nullFragment);

    if (column instanceof $xds_integer) {
      final String autoIncrementFragment = vendor.getSQLSpec().$autoIncrement(table, ($xds_integer)column);
      if (autoIncrementFragment != null && autoIncrementFragment.length() > 0)
        ddl.append(" ").append(autoIncrementFragment);
    }

    return ddl.toString();
  }

  private String parseColumns(final DBVendor vendor, final $xds_table table) {
    final StringBuilder ddl = new StringBuilder();
    columnCount.put(table._name$().text(), table._column() != null ? table._column().size() : 0);
    if (table._column() == null)
      return "";

    for (final $xds_column column : table._column())
      if (!(column instanceof $xds_inherited))
        ddl.append(",\n  ").append(parseColumn(table, column, vendor));

    return ddl.substring(2);
  }

  private static String recurseCheckRule(final $xds_check check) {
    final String condition;
    if (check._column().size() == 2)
      condition = check._column(1).text();
    else if (!check._value(0).isNull())
      condition = Numbers.isNumber(check._value(0).text()) ? Numbers.roundInsignificant(check._value(0).text()) : "'" + check._value(0).text() + "'";
    else
      throw new UnsupportedOperationException("Unexpected condition on column '" + check._column(0).text() + "'");

    final String clause = check._column(0).text() + " " + check._operator(0).text() + " " + condition;
    if (!check._and(0).isNull())
      return "(" + clause + " AND " + recurseCheckRule(check._and(0)) + ")";

    if (!check._or(0).isNull())
      return "(" + clause + " OR " + recurseCheckRule(check._or(0)) + ")";

    return clause;
  }

  private String parseConstraints(final DBVendor vendor, final String tableName, final Map<String,$xds_column> columnNameToColumn, final $xds_table table) {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $xds_table._constraints constraints = table._constraints(0);

      // unique constraint
      final List<$xds_table._constraints._unique> uniques = constraints._unique();
      if (uniques != null) {
        String uniqueString = "";
        int uniqueIndex = 1;
        for (final $xds_table._constraints._unique unique : uniques) {
          final List<$xds_named> columns = unique._column();
          String columnsString = "";
          for (final $xds_named column : columns)
            columnsString += ", " + column._name$().text();

          uniqueString += ",\n  CONSTRAINT " + table._name$().text() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      // check constraint
      final List<$xds_check> checks = constraints._check();
      if (checks != null) {
        String checkString = "";
        for (final $xds_check check : checks) {
          final String checkClause = recurseCheckRule(check);
          checkString += ",\n  CHECK " + (checkClause.startsWith("(") ? checkClause : "(" + checkClause + ")");
        }

        contraintsBuffer.append(checkString);
      }

      // primary key constraint
      final $xds_table._constraints._primaryKey primaryKey = constraints._primaryKey(0);
      if (!primaryKey.isNull()) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xds_named primaryColumn : primaryKey._column()) {
          final String primaryKeyColumn = primaryColumn._name$().text();
          final $xds_column column = columnNameToColumn.get(primaryKeyColumn);
          if (column._null$().text()) {
            Log.error("Column " + tableName + "." + column._name$() + " must be NOT NULL to be a PRIMARY KEY.");
            System.exit(1);
          }

          primaryKeyBuffer.append(", ").append(primaryKeyColumn);
        }

        contraintsBuffer.append(",\n  PRIMARY KEY (").append(primaryKeyBuffer.substring(2)).append(")");
      }

      // foreign key constraint
      final List<$xds_table._constraints._foreignKey> foreignKeys = constraints._foreignKey();
      if (foreignKeys != null) {
        for (final $xds_table._constraints._foreignKey foreignKey : foreignKeys) {
          String columns = "";
          String referencedColumns = "";
          for (final $xds_table._constraints._foreignKey._column column : foreignKey._column()) {
            columns += ", " + column._name$().text();
            referencedColumns += ", " + column._column$().text();
          }

          contraintsBuffer.append(",\n  FOREIGN KEY (").append(columns.substring(2));
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          insertDependency(tableName, foreignKey._references$().text());
          contraintsBuffer.append(" (").append(referencedColumns.substring(2)).append(")");
          if (!foreignKey._onDelete$().isNull())
            contraintsBuffer.append(" ON DELETE ").append(foreignKey._onDelete$().text());

          if (vendor != DBVendor.DERBY && !foreignKey._onUpdate$().isNull())
            contraintsBuffer.append(" ON UPDATE ").append(foreignKey._onUpdate$().text());
        }
      }
    }

    if (table._column() != null) {
      for (final $xds_column column : table._column()) {
        if (column._foreignKey() != null) {
          final $xds_foreignKey foreignKey = column._foreignKey(0);
          contraintsBuffer.append(",\n  FOREIGN KEY (").append(column._name$().text());
          contraintsBuffer.append(") REFERENCES ").append(foreignKey._references$().text());
          insertDependency(tableName, foreignKey._references$().text());
          contraintsBuffer.append(" (").append(foreignKey._column$().text()).append(")");
          if (!foreignKey._onDelete$().isNull())
            contraintsBuffer.append(" ON DELETE ").append(foreignKey._onDelete$().text());

          if (vendor != DBVendor.DERBY && !foreignKey._onUpdate$().isNull())
            contraintsBuffer.append(" ON UPDATE ").append(foreignKey._onUpdate$().text());
        }
      }

      // Parse the min & max constraints of numeric types
      for (final $xds_column column : table._column()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $xds_integer) {
          final $xds_integer type = ($xds_integer)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xds_float) {
          final $xds_float type = ($xds_float)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xds_decimal) {
          final $xds_decimal type = ($xds_decimal)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }

        if (minCheck != null)
          minCheck = column._name$().text() + " >= " + minCheck;

        if (maxCheck != null)
          maxCheck = column._name$().text() + " <= " + maxCheck;

        if (minCheck != null) {
          if (maxCheck != null)
            contraintsBuffer.append(",\n  CHECK (" + minCheck + " AND " + maxCheck + ")");
          else
            contraintsBuffer.append(",\n  CHECK (" + minCheck + ")");
        }
        else if (maxCheck != null) {
          contraintsBuffer.append(",\n  CHECK (" + maxCheck + ")");
        }
      }

      // parse the <check/> element per type
      for (final $xds_column column : table._column()) {
        String operator = null;
        String condition = null;
        if (column instanceof $xds_char) {
          final $xds_char type = ($xds_char)column;
          if (!type._check(0).isNull()) {
            operator = $xds_char._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_char._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : null;
            condition = "'" + type._check(0)._condition$().text() + "'";
          }
        }
        else if (column instanceof $xds_integer) {
          final $xds_integer type = ($xds_integer)column;
          if (!type._check(0).isNull()) {
            operator = $xds_integer._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_integer._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_integer._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_integer._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_integer._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_integer._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $xds_float) {
          final $xds_float type = ($xds_float)column;
          if (!type._check(0).isNull()) {
            operator = $xds_float._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_float._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_float._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_float._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_float._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_float._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }
        else if (column instanceof $xds_decimal) {
          final $xds_decimal type = ($xds_decimal)column;
          if (!type._check(0).isNull()) {
            operator = $xds_decimal._check._operator$.eq.text().equals(type._check(0)._operator$().text()) ? "=" : $xds_decimal._check._operator$.ne.text().equals(type._check(0)._operator$().text()) ? "!=" : $xds_decimal._check._operator$.gt.text().equals(type._check(0)._operator$().text()) ? ">" : $xds_decimal._check._operator$.gte.text().equals(type._check(0)._operator$().text()) ? ">=" : $xds_decimal._check._operator$.lt.text().equals(type._check(0)._operator$().text()) ? "<" : $xds_decimal._check._operator$.lte.text().equals(type._check(0)._operator$().text()) ? "<=" : null;
            condition = String.valueOf(type._check(0)._condition$().text());
          }
        }

        if (operator != null) {
          if (condition != null)
            contraintsBuffer.append(",\n  CHECK (" + column._name$().text() + " " + operator + " " + condition + ")");
          else
            throw new UnsupportedOperationException("Unexpected 'null' condition encountered on column '" + column._name$().text());
        }
        else if (condition != null)
          throw new UnsupportedOperationException("Unexpected 'null' operator encountered on column '" + column._name$().text());
      }
    }

    return contraintsBuffer.toString();
  }

  private static void registerColumns(final Set<String> tableNames, final Map<String,$xds_column> columnNameToColumn, final $xds_table table) {
    final String tableName = table._name$().text();
    checkName(tableName);

    if (tableNames.contains(tableName)) {
      Log.error("Circular dependency detected for table: " + tableName);
      System.exit(1);
    }

    tableNames.add(tableName);
    if (table._column() != null) {
      for (final $xds_column column : table._column()) {
        final String columnName = column._name$().text();
        checkName(columnName);
        final $xds_column existing = columnNameToColumn.get(columnName);
        if (existing != null && !(column instanceof $xds_inherited)) {
          Log.error("Duplicate column definition: " + tableName + "." + columnName + " only xsi:type=\"xds:inherited\" is allowed when overriding a column.");
          System.exit(1);
        }

        columnNameToColumn.put(columnName, column);
      }
    }
  }

  private String[] parseTable(final DBVendor vendor, final $xds_table table, final Set<String> tableNames) {
    insertDependency(table._name$().text(), null);
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$xds_column> columnNameToColumn = new HashMap<String,$xds_column>();
    registerColumns(tableNames, columnNameToColumn, table);

    final List<String> statements = new ArrayList<String>();
    statements.addAll(vendor.getSQLSpec().types(table));

    final String tableName = table._name$().text();
    final StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE TABLE ").append(tableName).append(" (\n");
    buffer.append(parseColumns(vendor, table));
    buffer.append(parseConstraints(vendor, tableName, columnNameToColumn, table));
    buffer.append("\n)");

    statements.add(buffer.toString());

    statements.addAll(vendor.getSQLSpec().triggers(table));
    statements.addAll(vendor.getSQLSpec().indexes(table));
    return statements.toArray(new String[statements.size()]);
  }

  private final Map<String,Set<String>> dependencyGraph = new HashMap<String,Set<String>>();
  private List<String> sortedTableOrder;

  public List<String> getSortedTableOrder() {
    return sortedTableOrder;
  }

  private void insertDependency(final String target, final String source) {
    Set<String> dependants = dependencyGraph.get(target);
    if (dependants == null)
      dependencyGraph.put(target, dependants = new HashSet<String>());

    if (source != null)
      dependants.add(source);
  }

  public DDL[] parse(final DBVendor vendor) {
    final boolean createDropStatements = vendor != DBVendor.DERBY;

    final Map<String,String[]> dropStatements = new HashMap<String,String[]>();
    final Map<String,String[]> createTableStatements = new HashMap<String,String[]>();

    final Set<String> skipTables = new HashSet<String>();
    for (final $xds_table table : merged._table()) {
      if (table._skip$().text()) {
        skipTables.add(table._name$().text());
      }
      else if (!table._abstract$().text() && createDropStatements) {
        final List<String> drops = vendor.getSQLSpec().drops(table);
        dropStatements.put(table._name$().text(), drops.toArray(new String[drops.size()]));
      }
    }

    final Set<String> tableNames = new HashSet<String>();
    for (final $xds_table table : merged._table())
      if (!table._abstract$().text())
        createTableStatements.put(table._name$().text(), parseTable(vendor, table, tableNames));

    sortedTableOrder = TopologicalSort.sort(dependencyGraph);
    final List<DDL> ddls = new ArrayList<DDL>();
    for (final String tableName : sortedTableOrder)
      if (!skipTables.contains(tableName))
        ddls.add(new DDL(tableName, dropStatements.get(tableName), createTableStatements.get(tableName)));

    return ddls.toArray(new DDL[ddls.size()]);
  }
}