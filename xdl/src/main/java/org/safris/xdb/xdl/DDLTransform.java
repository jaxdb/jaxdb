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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.util.TopologicalSort;
import org.safris.commons.xml.XMLException;

public final class DDLTransform extends XDLTransformer {
  private static final String[] reservedWords = new String[] {"ABSOLUTE", "ACTION", "ADD", "AFTER", "ALL", "ALLOCATE", "ALTER", "AND", "ANY", "ARE", "ARRAY", "AS", "ASC", "ASSERTION", "AT", "AUTHORIZATION", "BEFORE", "BEGIN", "BETWEEN", "BINARY", "BIT", "BLOB", "BOOLEAN", "BOTH", "BREADTH", "BY", "CALL", "CASCADE", "CASCADED", "CASE", "CAST", "CATALOG", "CHAR", "CHARACTER", "CHECK", "CLOB", "CLOSE", "COLLATE", "COLLATION", "COLUMN", "COMMIT", "CONDITION", "CONNECT", "CONNECTION", "CONSTRAINT", "CONSTRAINTS", "CONSTRUCTOR", "CONTINUE", "CORRESPONDING", "CREATE", "CROSS", "CUBE", "CURRENT", "CURRENT_DATE", "CURRENT_DEFAULT_TRANSFORM_GROUP", "CURRENT_TRANSFORM_GROUP_FOR_TYPE", "CURRENT_PATH", "CURRENT_ROLE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR", "CYCLE", "DATA", "DATE", "DAY", "DEALLOCATE", "DEC", "DECIMAL", "DECLARE", "DEFAULT", "DEFERRABLE", "DEFERRED", "DELETE", "DEPTH", "DEREF", "DESC", "DESCRIBE", "DESCRIPTOR", "DETERMINISTIC", "DIAGNOSTICS", "DISCONNECT", "DISTINCT", "DO", "DOMAIN", "DOUBLE", "DROP", "DYNAMIC", "EACH", "ELSE", "ELSEIF", "END", "END-EXEC", "EQUALS", "ESCAPE", "EXCEPT", "EXCEPTION", "EXEC", "EXECUTE", "EXISTS", "EXIT", "EXTERNAL", "FALSE", "FETCH", "FIRST", "FLOAT", "FOR", "FOREIGN", "FOUND", "FROM", "FREE", "FULL", "FUNCTION", "GENERAL", "GET", "GLOBAL", "GO", "GOTO", "GRANT", "GROUP", "GROUPING", "HANDLE", "HAVING", "HOLD", "HOUR", "IDENTITY", "IF", "IMMEDIATE", "IN", "INDICATOR", "INITIALLY", "INNER", "INOUT", "INPUT", "INSERT", "INT", "INTEGER", "INTERSECT", "INTERVAL", "INTO", "IS", "ISOLATION", "JOIN", "KEY", "LANGUAGE", "LARGE", "LAST", "LATERAL", "LEADING", "LEAVE", "LEFT", "LEVEL", "LIKE", "LOCAL", "LOCALTIME", "LOCALTIMESTAMP", "LOCATOR", "LOOP", "MAP", "MATCH", "METHOD", "MINUTE", "MODIFIES", "MODULE", "MONTH", "NAMES", "NATIONAL", "NATURAL", "NCHAR", "NCLOB", "NESTING", "NEW", "NEXT", "NO", "NONE", "NOT", "NULL", "NUMERIC", "OBJECT", "OF", "OLD", "ON", "ONLY", "OPEN", "OPTION", "OR", "ORDER", "ORDINALITY", "OUT", "OUTER", "OUTPUT", "OVERLAPS", "PAD", "PARAMETER", "PARTIAL", "PATH", "PRECISION", "PREPARE", "PRESERVE", "PRIMARY", "PRIOR", "PRIVILEGES", "PROCEDURE", "PUBLIC", "READ", "READS", "REAL", "RECURSIVE", "REDO", "REF", "REFERENCES", "REFERENCING", "RELATIVE", "RELEASE", "REPEAT", "RESIGNAL", "RESTRICT", "RESULT", "RETURN", "RETURNS", "REVOKE", "RIGHT", "ROLE", "ROLLBACK", "ROLLUP", "ROUTINE", "ROW", "ROWS", "SAVEPOINT", "SCHEMA", "SCROLL", "SEARCH", "SECOND", "SECTION", "SELECT", "SESSION", "SESSION_USER", "SET", "SETS", "SIGNAL", "SIMILAR", "SIZE", "SMALLINT", "SOME", "SPACE", "SPECIFIC", "SPECIFICTYPE", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING", "START", "STATE", "STATIC", "SYSTEM_USER", "TABLE", "TEMPORARY", "THEN", "TIME", "TIMESTAMP", "TIMEZONE_HOUR", "TIMEZONE_MINUTE", "TO", "TRAILING", "TRANSACTION", "TRANSLATION", "TREAT", "TRIGGER", "TRUE", "UNDER", "UNDO", "UNION", "UNIQUE", "UNKNOWN", "UNNEST", "UNTIL", "UPDATE", "USAGE", "USER", "USING", "VALUE", "VALUES", "VARCHAR", "VARYING", "VIEW", "WHEN", "WHENEVER", "WHERE", "WHILE", "WITH", "WITHOUT", "WORK", "WRITE", "YEAR", "ZONE"};

  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      System.err.println("<Derby|MySQL|PostgreSQL> <XDL_FILE>");
      System.exit(1);
    }

    createDDL(new File(args[1]).toURI().toURL(), DBVendor.parse(args[0]), null);
  }

  public static DDL[] createDDL(final URL url, final DBVendor vendor, final File outDir) throws IOException, XMLException {
    return DDLTransform.createDDL(parseArguments(url, outDir), vendor, outDir);
  }

  public static DDLTransform transformDDL(final URL url) throws IOException, XMLException {
    final xdl_database database = parseArguments(url, null);
    return new DDLTransform(database);
  }

  private static boolean checkName(final String string) {
    return Arrays.binarySearch(reservedWords, string.toUpperCase()) < 0;
  }

  public static DDL[] createDDL(final xdl_database database, final DBVendor vendor, final File outDir) {
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

  private DDLTransform(final xdl_database database) {
    super(database);
  }

  private final Map<String,Integer> columnCount = new HashMap<String,Integer>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static String parseColumn(final $xdl_table table, final $xdl_column column, final DBVendor vendor) {
    final StringBuilder ddl = new StringBuilder();
    ddl.append(column._name$().text()).append(" ");
    if (column instanceof $xdl_char) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_char)column));
    }
    else if (column instanceof $xdl_bit) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_bit)column));
    }
    else if (column instanceof $xdl_blob) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_blob)column));
    }
    else if (column instanceof $xdl_integer) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_integer)column));
    }
    else if (column instanceof $xdl_float) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_float)column));
    }
    else if (column instanceof $xdl_decimal) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_decimal)column));
    }
    else if (column instanceof $xdl_date) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_date)column));
    }
    else if (column instanceof $xdl_time) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_time)column));
    }
    else if (column instanceof $xdl_dateTime) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_dateTime)column));
    }
    else if (column instanceof $xdl_boolean) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_boolean)column));
    }
    else if (column instanceof $xdl_enum) {
      ddl.append(vendor.getSQLSpec().type(table, ($xdl_enum)column));
    }

    final String defaultFragement = vendor.getSQLSpec().$default(table, column);
    if (defaultFragement != null && defaultFragement.length() > 0)
      ddl.append(" DEFAULT ").append(defaultFragement);

    final String nullFragment = vendor.getSQLSpec().$null(table, column);
    if (nullFragment != null && nullFragment.length() > 0)
      ddl.append(" ").append(nullFragment);

    if (column instanceof $xdl_integer) {
      final String autoIncrementFragment = vendor.getSQLSpec().$autoIncrement(table, ($xdl_integer)column);
      if (autoIncrementFragment != null && autoIncrementFragment.length() > 0)
        ddl.append(" ").append(autoIncrementFragment);
    }

    return ddl.toString();
  }

  private String parseColumns(final DBVendor vendor, final $xdl_table table) {
    final StringBuilder ddl = new StringBuilder();
    columnCount.put(table._name$().text(), table._column() != null ? table._column().size() : 0);
    if (table._column() == null)
      return "";

    for (final $xdl_column column : table._column())
      if (!(column instanceof $xdl_inherited))
        ddl.append(",\n  ").append(parseColumn(table, column, vendor));

    return ddl.substring(2);
  }

  private String parseConstraints(final DBVendor vendor, final String tableName, final Map<String,$xdl_column> columnNameToColumn, final $xdl_table table) {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $xdl_table._constraints constraints = table._constraints(0);
      String uniqueString = "";
      int uniqueIndex = 1;
      final List<$xdl_table._constraints._unique> uniques = constraints._unique();
      if (uniques != null) {
        for (final $xdl_table._constraints._unique unique : uniques) {
          final List<$xdl_named> columns = unique._column();
          String columnsString = "";
          for (final $xdl_named column : columns)
            columnsString += ", " + column._name$().text();

          uniqueString += ",\n  CONSTRAINT " + table._name$().text() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      final $xdl_table._constraints._primaryKey primaryKey = constraints._primaryKey(0);
      if (!primaryKey.isNull()) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xdl_named primaryColumn : primaryKey._column()) {
          final String primaryKeyColumn = primaryColumn._name$().text();
          final $xdl_column column = columnNameToColumn.get(primaryKeyColumn);
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
      for (final $xdl_column column : table._column()) {
        if (column._foreignKey() != null) {
          final $xdl_foreignKey foreignKey = column._foreignKey(0);
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
        for (final $xdl_table._constraints._foreignKey foreignKey : table._constraints(0)._foreignKey()) {
          String columns = "";
          String referencedColumns = "";
          for (final $xdl_table._constraints._foreignKey._column column : foreignKey._column()) {
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

      for (final $xdl_column column : table._column()) {
        String minCheck = null;
        String maxCheck = null;
        if (column instanceof $xdl_integer) {
          final $xdl_integer type = ($xdl_integer)column;
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

  private void registerColumns(final Set<String> tableNames, final Map<String,$xdl_column> columnNameToColumn, final $xdl_table table) {
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
      for (final $xdl_column column : table._column()) {
        final $xdl_column existing = columnNameToColumn.get(checkName(column._name$().text()));
        if (existing != null && !(column instanceof $xdl_inherited)) {
          System.err.println("[ERROR] Duplicate column definition: " + tableName + "." + column._name$().text() + " only xsi:type=\"xdl:inherited\" is allowed when overriding a column.");
          System.exit(1);
        }

        columnNameToColumn.put(column._name$().text(), column);
      }
    }
  }

  private String[] parseTable(final DBVendor vendor, final $xdl_table table, final Set<String> tableNames) {
    insertDependency(table._name$().text(), null);
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$xdl_column> columnNameToColumn = new HashMap<String,$xdl_column>();
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
    for (final $xdl_table table : merged._table()) {
      if (table._skip$().text()) {
        skipTables.add(table._name$().text());
      }
      else if (!table._abstract$().text() && createDropStatements) {
        final List<String> drops = vendor.getSQLSpec().drops(table);
        dropStatements.put(table._name$().text(), drops.toArray(new String[drops.size()]));
      }
    }

    final Set<String> tableNames = new HashSet<String>();
    for (final $xdl_table table : merged._table())
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