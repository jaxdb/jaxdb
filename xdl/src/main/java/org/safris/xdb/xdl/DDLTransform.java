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

package org.safris.xdb.xdl;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.util.TopologicalSort;
import org.safris.xdb.xdl.$xdl_tableType._index;
import org.safris.xml.generator.compiler.runtime.BindingList;

public final class DDLTransform extends XDLTransformer {
  private static final String[] reservedWords = new String[] {"ABSOLUTE", "ACTION", "ADD", "AFTER", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATION", "BEFORE", "BEGIN", "BETWEEN", "BINARY", "BIT", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BY", "CALL", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER", "CHECK", "CLOB", "CLOSE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONDITION", "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONSTRUCTOR", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "CYCLE", "DATA", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DEPTH", "DEREF", "DESC", "DESCRIBE", "DESCRIPTOR", "DETERMINISTIC", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELSE", "ELSEIF", "END", "END-EXEC", "EQUALS", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNAL", "FALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FREE", "FULL", "FUNCTION", "GENERAL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "GROUPING", "HANDLE", "HAVING", "HOLD", "HOUR", "IDENTITY", "IF", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INOUT", "INPUT", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEAVE", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATOR", "LOOP", "MAP", "MATCH", "METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTH", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NESTING", "NEW", "NEXT", "NO", "NONE", "NOT", "NULL", "NUMERIC", "OBJECT", "OF", "OLD", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARAMETER", "PARTIAL", "PATH", "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "READS", "REAL", "RECURSIVE", "REDO", "REF", "REFERENCES", "REFERENCING", "RELATIVE", "RELEASE", "REPEAT", "RESIGNAL", "RESTRICT", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROW", "ROWS", "SAVEPOINT", "SCHEMA", "SCROLL", "SEARCH", "SECOND", "SECTION", "SELECT", "SESSION", "SESSION_USER", "SET", "SETS", "SIGNAL", "SIMILAR", "SIZE", "SMALLINT", "SOME", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATE", "STATIC", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATION", "TREAT", "TRIGGER", "TRUE", "UNDER", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL", "UPDATE", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN", "WHENEVER", "WHERE", "WHILE", "WITH", "WITHOUT", "WORK", "WRITE", "YEAR", "ZONE"};

  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("<MySQL|Derby> <XDL_FILE>");
      System.exit(1);
    }

    createDDL(new File(args[1]).toURI().toURL(), "MySQL".equals(args[0]) ? DBVendor.MY_SQL : "Derby".equals(args[0]) ? DBVendor.DERBY : null, null);
  }

  private static String getIndexName(final $xdl_tableType table, final _index index) {
    return getIndexName(table, index, index._column().toArray(new $xdl_namedType[index._column().size()]));
  }

  private static String getIndexName(final $xdl_tableType table, final $xdl_indexType index, final $xdl_namedType ... column) {
    if (index == null || column.length == 0)
      return null;

    String name = "";
    for (final $xdl_namedType c : column)
      name += "_" + c._name$().text();

    return "idx_" + table._name$().text() + name;
  }

  public static DDL[] createDDL(final URL url, final DBVendor vendor, final File outDir) {
    return DDLTransform.createDDL(parseArguments(url, outDir), vendor, outDir);
  }

  public static DDLTransform transformDDL(final URL url) {
    final xdl_database database = parseArguments(url, null);
    try {
      return new DDLTransform(database);
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean checkName(final String string) {
    return Arrays.binarySearch(reservedWords, string.toUpperCase()) < 0;
  }

  public static DDL[] createDDL(final xdl_database database, final DBVendor vendor, final File outDir) {
    try {
      final DDLTransform creator = new DDLTransform(database);
      final DDL[] ddls = creator.parse(vendor);
      String sql = "";
      for (int i = ddls.length - 1; i >= 0; --i)
        if (ddls[i].drop != null)
          for (final String drop : ddls[i].drop)
            sql += "\n" + drop + ";";

      for (final DDL ddl : ddls)
        for (final String create : ddl.create)
          sql += "\n" + create + ";";

      if (vendor == DBVendor.DERBY)
        sql = "\nCREATE SCHEMA " + database._name$().text() + ";\n" + sql;

      writeOutput(sql, outDir != null ? new File(outDir, creator.merged._name$().text() + ".sql") : null);

      return ddls;
    }
    catch (final Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static String toEnumValue(String value) {
    value = value.replaceAll("\\\\\\\\", "\\\\"); // \\ is \
    value = value.replaceAll("\\\\_", " "); // \_ is <space>
    value = value.replaceAll("\\\\", ""); // anything else escaped is itself
    value = value.replace("'", "\\'"); // ' must be \'
    return value;
  }

  private DDLTransform(final xdl_database database) throws Exception {
    super(database);
  }

  private static void checkValidNumber(final String var, final Integer precision, final Integer decimal) {
    if (precision < decimal) {
      System.err.println("[ERROR] ERROR 1427 (42000): For decimal(M,D), M must be >= D (column '" + var + "').");
      System.exit(1);
    }
  }

  private final Map<String,Integer> columnCount = new HashMap<String,Integer>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static String parseColumn(final $xdl_columnType column, DBVendor vendor) {
    String ddl = "";
    String suffix = "";
    final String columnName = column._name$().text();
    ddl += columnName + " ";
    if (column instanceof $xdl_boolean) {
      final $xdl_boolean type = ($xdl_boolean)column;
      ddl += vendor != DBVendor.DERBY ? "BOOL" : "BOOLEAN";
      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();
    }
    else if (column instanceof $xdl_varchar) {
      final $xdl_varchar type = ($xdl_varchar)column;
      ddl += "VARCHAR";
      if (!type._length$().isNull())
        ddl += "(" + type._length$().text() + ")";

      if (!type._default$().isNull())
        ddl += " DEFAULT '" + type._default$().text() + "'";
    }
    else if (column instanceof $xdl_enum) {
      final $xdl_enum type = ($xdl_enum)column;
      ddl += vendor != DBVendor.DERBY ? "ENUM" : "VARCHAR";
      ddl += "(";
      if (vendor == DBVendor.DERBY) {
        int maxLength = 0;
        if (!type._values$().isNull())
          for (final String value : type._values$().text())
            maxLength = Math.max(maxLength, value.length());

        ddl += String.valueOf(maxLength);
      }
      else if (!type._values$().isNull()) {
        String values = "";
        for (final String value : type._values$().text())
          values += ", '" + DDLTransform.toEnumValue(value) + "'";

        ddl += values.substring(2);
      }

      ddl += ")";
      if (!type._default$().isNull())
        ddl += " DEFAULT '" + type._default$().text() + "'";
    }
    else if (column instanceof $xdl_decimal) {
      final $xdl_decimal type = ($xdl_decimal)column;
      ddl += "DECIMAL";
      if (!type._precision$().isNull() && !type._decimal$().isNull()) {
        DDLTransform.checkValidNumber(column._name$().text(), type._precision$().text(), type._decimal$().text());
        ddl += "(" + type._precision$().text() + ", " + type._decimal$().text() + ")";
      }

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();
    }
    else if (column instanceof $xdl_tinyint) {
      final $xdl_tinyint type = ($xdl_tinyint)column;
      ddl += vendor != DBVendor.DERBY ? "TINYINT" : "SMALLINT";
      if (!type._precision$().isNull() && vendor != DBVendor.DERBY)
        ddl += "(" + type._precision$().text() + ")";

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();

      if (!type._generateOnInsert$().isNull() && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text().equals(type._generateOnInsert$().text()))
        suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text();
    }
    else if (column instanceof $xdl_smallint) {
      final $xdl_smallint type = ($xdl_smallint)column;
      ddl += vendor != DBVendor.DERBY ? "SMALLINT" : !type._unsigned$().isNull() && type._unsigned$().text() ? "INTEGER" : "SMALLINT";
      if (!type._precision$().isNull() && vendor != DBVendor.DERBY)
        ddl += "(" + type._precision$().text() + ")";

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();

      if (!type._generateOnInsert$().isNull() && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text().equals(type._generateOnInsert$().text()))
        suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text();
    }
    else if (column instanceof $xdl_mediumint) {
      final $xdl_mediumint type = ($xdl_mediumint)column;
      ddl += vendor != DBVendor.DERBY ? "MEDIUMINT" : "INTEGER";
      if (vendor != DBVendor.DERBY && !type._precision$().isNull())
        ddl += "(" + type._precision$().text() + ")";

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();

      if (!type._generateOnInsert$().isNull() && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text().equals(type._generateOnInsert$().text()))
        suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text();
    }
    else if (column instanceof $xdl_int) {
      final $xdl_int type = ($xdl_int)column;
      ddl += vendor != DBVendor.DERBY ? "INT" : "INTEGER";
      if (!type._precision$().isNull() && vendor != DBVendor.DERBY)
        ddl += "(" + type._precision$().text() + ")";

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();

      if (!type._generateOnInsert$().isNull() && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text().equals(type._generateOnInsert$().text()))
        suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text();
    }
    else if (column instanceof $xdl_bigint) {
      final $xdl_bigint type = ($xdl_bigint)column;
      ddl += "BIGINT";
      if (vendor != DBVendor.DERBY && !type._precision$().isNull())
        ddl += "(" + type._precision$().text() + ")";

      if (!type._unsigned$().isNull() && type._unsigned$().text() && vendor != DBVendor.DERBY)
        ddl += " UNSIGNED";

      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();

      if (!type._generateOnInsert$().isNull() && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text().equals(type._generateOnInsert$().text()))
        suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.text();
    }
    else if (column instanceof $xdl_date) {
      final $xdl_date type = ($xdl_date)column;
      ddl += "DATE";
      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();
    }
    else if (column instanceof $xdl_time) {
      final $xdl_time type = ($xdl_time)column;
      ddl += "TIME";
      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();
    }
    else if (column instanceof $xdl_dateTime) {
      final $xdl_dateTime type = ($xdl_dateTime)column;
      ddl += vendor != DBVendor.DERBY ? "DATETIME" : "TIMESTAMP";
      if (!type._default$().isNull())
        ddl += " DEFAULT " + type._default$().text();
    }
    else if (column instanceof $xdl_blob) {
      ddl += "BLOB";
    }

    if (!column._null$().isNull())
      ddl += !column._null$().text() ? " NOT NULL" : (vendor != DBVendor.DERBY ? " NULL" : "");

    ddl += suffix;
    return ddl;
  }

  private String parseColumns(final DBVendor vendor, final $xdl_tableType table) {
    String ddl = "";
    columnCount.put(table._name$().text(), table._column().size());
    for (final $xdl_columnType column : table._column())
      if (!(column instanceof $xdl_inherited))
        ddl += ",\n  " + parseColumn(column, vendor);

    return ddl.substring(2);
  }

  private String parseConstraints(final DBVendor vendor, final String tableName, final Map<String,$xdl_columnType> columnNameToColumn, final $xdl_tableType table) {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $xdl_tableType._constraints constraints = table._constraints(0);
      String uniqueString = "";
      int uniqueIndex = 1;
      final List<$xdl_tableType._constraints._unique> uniques = constraints._unique();
      if (uniques != null) {
        for (final $xdl_tableType._constraints._unique unique : uniques) {
          final List<$xdl_namedType> columns = unique._column();
          String columnsString = "";
          for (final $xdl_namedType column : columns)
            columnsString += ", " + column._name$().text();

          uniqueString += ",\n  CONSTRAINT " + table._name$().text() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      final $xdl_tableType._constraints._primaryKey primaryKey = constraints._primaryKey(0);
      if (!primaryKey.isNull()) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xdl_namedType primaryColumn : primaryKey._column()) {
          final String primaryKeyColumn = primaryColumn._name$().text();
          final $xdl_columnType column = columnNameToColumn.get(primaryKeyColumn);
          if (column._null$().text()) {
            System.err.println("[ERROR] Column " + tableName + "." + column._name$() + " must be NOT NULL to be a PRIMARY KEY.");
            System.exit(1);
          }

          primaryKeyBuffer.append(", ").append(primaryKeyColumn);
        }

        contraintsBuffer.append(",\n  PRIMARY KEY (").append(primaryKeyBuffer.substring(2)).append(")");
      }
    }

    if (table._column() != null) {
      for (final $xdl_columnType column : table._column()) {
        if (column._foreignKey() != null) {
          final $xdl_foreignKeyType foreignKey = column._foreignKey(0);
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

      if (table._constraints() != null && table._constraints(0)._foreignKey() != null) {
        for (final $xdl_tableType._constraints._foreignKey foreignKey : table._constraints(0)._foreignKey()) {
          String columns = "";
          String referencedColumns = "";
          for (final $xdl_tableType._constraints._foreignKey._column column : foreignKey._column()) {
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

      for (final $xdl_columnType column : table._column()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $xdl_tinyint) {
          final $xdl_tinyint type = ($xdl_tinyint)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xdl_smallint) {
          final $xdl_smallint type = ($xdl_smallint)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xdl_mediumint) {
          final $xdl_mediumint type = ($xdl_mediumint)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xdl_int) {
          final $xdl_int type = ($xdl_int)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xdl_float) {
          final $xdl_float type = ($xdl_float)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }
        else if (column instanceof $xdl_decimal) {
          final $xdl_decimal type = ($xdl_decimal)column;
          minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
          maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
        }

        if (minCheck != null)
          contraintsBuffer.append(",\n  CHECK (" + column._name$().text() + " >= " + minCheck + ")");

        if (maxCheck != null)
          contraintsBuffer.append(",\n  CHECK (" + column._name$().text() + " <= " + maxCheck + ")");
      }
    }

    return contraintsBuffer.toString();
  }

  private static String getTriggerName(final String tableName, final $xdl_tableType._triggers._trigger trigger, final String action) {
    return tableName + "_" + trigger._time$().text().toLowerCase() + "_" + action.toLowerCase();
  }

  private String parseTriggers(final String tableName, final List<$xdl_tableType._triggers._trigger> triggers) {
    String buffer = "";
    for (final $xdl_tableType._triggers._trigger trigger : triggers) {
      for (final String action : trigger._actions$().text()) {
        buffer += "\nDELIMITER |\n";
        buffer += "CREATE TRIGGER " + DDLTransform.getTriggerName(tableName, trigger, action) + " " + trigger._time$().text() + " " + action + " ON " + tableName + "\n";
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
    }

    return buffer.substring(1);
  }

  private void registerColumns(final Set<String> tableNames, final Map<String,$xdl_columnType> columnNameToColumn, final $xdl_tableType table) {
    final String tableName = table._name$().text();
    if (!checkName(tableName)) {
      System.err.println("[ERROR] The name '" + tableName + "' is a SQL reserved word.");
      System.exit(1);
    }

    if (tableNames.contains(tableName)) {
      System.err.println("[ERROR] Circular dependency detected for table: " + tableName);
      System.exit(1);
    }

    tableNames.add(tableName);
    if (table._column() != null) {
      for (final $xdl_columnType column : table._column()) {
        final $xdl_columnType existing = columnNameToColumn.get(checkName(column._name$().text()));
        if (existing != null && !(column instanceof $xdl_inherited)) {
          System.err.println("[ERROR] Duplicate column definition: " + tableName + "." + column._name$().text() + " only xsi:type=\"xdl:inherited\" is allowed when overriding a column.");
          System.exit(1);
        }

        columnNameToColumn.put(column._name$().text(), column);
      }
    }
  }

  private String[] parseTable(final DBVendor vendor, final $xdl_tableType table, final Set<String> tableNames) {
    insertDependency(table._name$().text(), null);
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$xdl_columnType> columnNameToColumn = new HashMap<String,$xdl_columnType>();
    registerColumns(tableNames, columnNameToColumn, table);

    final String tableName = table._name$().text();
    final StringBuffer buffer = new StringBuffer();
    buffer.append("CREATE TABLE ").append(tableName).append(" (\n");
    buffer.append(parseColumns(vendor, table));
    buffer.append(parseConstraints(vendor, tableName, columnNameToColumn, table));
    buffer.append("\n)");

    final List<String> statements = new ArrayList<String>();
    statements.add(buffer.toString());

    if (table._triggers() != null)
      statements.add(parseTriggers(tableName, table._triggers().get(0)._trigger()));

    if (table._index() != null)
      for (final _index index : table._index())
        statements.add("CREATE " + (!index._unique$().isNull() && index._unique$().text() ? "UNIQUE " : "") + "INDEX " + getIndexName(table, index) + " USING " + index._type$().text() + " ON " + table._name$().text() + " (" + csvNames(index._column()) + ")");

    if (table._column() != null)
      for (final $xdl_columnType column : table._column())
        if (column._index() != null)
          statements.add("CREATE " + (!column._index(0)._unique$().isNull() && column._index(0)._unique$().text() ? "UNIQUE " : "") + "INDEX " + getIndexName(table, column._index(0), column) + " USING " + column._index(0)._type$().text() + " ON " + table._name$().text() + " (" + column._name$().text() + ")");

    return statements.toArray(new String[statements.size()]);
  }

  private static String csvNames(final BindingList<$xdl_namedType> names) {
    if (names.size() == 0)
      return "";

    String csv = "";
    for (final $xdl_namedType name : names)
      csv += ", " + name._name$().text();

    return csv.length() > 0 ? csv.substring(2) : csv;
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

  private String[] createDropStatement(final $xdl_tableType table) {
    final List<String> statements = new ArrayList<String>();
    if (table._index() != null)
      for (final _index index : table._index())
        statements.add("DROP INDEX " + getIndexName(table, index) + " ON " + table._name$().text());

    if (table._column() != null)
      for (final $xdl_columnType column : table._column())
        if (column._index() != null)
          statements.add("DROP INDEX " + getIndexName(table, column._index(0), column) + " ON " + table._name$().text());

    if (table._triggers() != null)
      for (final $xdl_tableType._triggers._trigger trigger : table._triggers().get(0)._trigger())
        for (final String action : trigger._actions$().text())
          statements.add("DROP TRIGGER IF EXISTS " + DDLTransform.getTriggerName(table._name$().text(), trigger, action));

    statements.add("DROP TABLE IF EXISTS " + table._name$().text());
    return statements.toArray(new String[statements.size()]);
  }

  public DDL[] parse(final DBVendor vendor) throws Exception {
    final boolean createDropStatements = vendor != DBVendor.DERBY;

    final Map<String,String[]> dropStatements = new HashMap<String,String[]>();
    final Map<String,String[]> createTableStatements = new HashMap<String,String[]>();

    final Set<String> skipTables = new HashSet<String>();
    for (final $xdl_tableType table : merged._table())
      if (table._skip$().text())
        skipTables.add(table._name$().text());
      else if (!table._abstract$().text() && createDropStatements)
        dropStatements.put(table._name$().text(), createDropStatement(table));

    final Set<String> tableNames = new HashSet<String>();
    for (final $xdl_tableType table : merged._table())
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