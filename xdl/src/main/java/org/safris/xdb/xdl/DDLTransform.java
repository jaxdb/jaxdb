/*  Copyright Safris Software 2011
 *
 *  This code is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.safris.xdb.xdl;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.safris.commons.util.TopologicalSort;
import org.safris.xml.generator.compiler.runtime.ComplexType;

public class DDLTransform extends XDLTransformer {
  public static void main(final String[] args) throws Exception {
    createDDL(new File(args[0]), null);
  }

  public static void createDDL(final File xdlFile, final File outDir) {
    final xdl_database database = parseArguments(xdlFile, outDir);
    try {
      final DDLTransform creator = new DDLTransform(database);
      final String sql = creator.parse();
      writeOutput(sql, outDir != null ? new File(outDir, creator.merged.get_name$().getText() + ".sql") : null);
    }
    catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private DDLTransform(final xdl_database database) throws Exception {
    super(database);
  }

  private String parseColumns(final $xdl_tableType<? extends ComplexType> table) {
    String ddl = "";
    if (table.get_column() != null) {
      for (final $xdl_columnType<?> column : table.get_column()) {
        if (column instanceof $xdl_inherited)
          continue;

        String suffix = "";
        final String columnName = column.get_name$().getText();
        ddl += ",\n  " + columnName + " ";
        if (column instanceof $xdl_boolean) {
          final $xdl_boolean<?> type = ($xdl_boolean<?>)column;
          ddl += "BOOL";
          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();
        }
        else if (column instanceof $xdl_varchar) {
          final $xdl_varchar<?> type = ($xdl_varchar<?>)column;
          ddl += "VARCHAR";
          if (type.get_length$() != null)
            ddl += "(" + type.get_length$().getText() + ")";

          if (type.get_default$() != null)
            ddl += " DEFAULT '" + type.get_default$().getText() + "'";
        }
        else if (column instanceof $xdl_enum) {
          final $xdl_enum<?> type = ($xdl_enum<?>)column;
          ddl += "ENUM";
          if (type.get_values$() != null) {
            ddl += "(";
            String values = "";
            for (final String value : type.get_values$().getText())
              values += ", '" + value + "'";

            ddl += values.substring(2);
            ddl += ")";
          }

          if (type.get_default$() != null)
            ddl += " DEFAULT '" + type.get_default$().getText() + "'";
        }
        else if (column instanceof $xdl_decimal) {
          final $xdl_decimal<?> type = ($xdl_decimal<?>)column;
          ddl += "DECIMAL";
          if (type.get_precision$() != null && type.get_decimal$() != null)
            ddl += "(" + type.get_precision$().getText() + ", " + type.get_decimal$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();
        }
        else if (column instanceof $xdl_tinyint) {
          final $xdl_tinyint<?> type = ($xdl_tinyint<?>)column;
          ddl += "TINYINT";
          if (type.get_precision$() != null)
            ddl += "(" + type.get_precision$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();

          if (type.get_generateOnInsert$() != null && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText().equals(type.get_generateOnInsert$().getText()))
            suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText();
        }
        else if (column instanceof $xdl_smallint) {
          final $xdl_smallint<?> type = ($xdl_smallint<?>)column;
          ddl += "SMALLINT";
          if (type.get_precision$() != null)
            ddl += "(" + type.get_precision$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();

          if (type.get_generateOnInsert$() != null && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText().equals(type.get_generateOnInsert$().getText()))
            suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText();
        }
        else if (column instanceof $xdl_mediumint) {
          final $xdl_mediumint<?> type = ($xdl_mediumint<?>)column;
          ddl += "MEDIUMINT";
          if (type.get_precision$() != null)
            ddl += "(" + type.get_precision$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();

          if (type.get_generateOnInsert$() != null && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText().equals(type.get_generateOnInsert$().getText()))
            suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText();
        }
        else if (column instanceof $xdl_int) {
          final $xdl_int<?> type = ($xdl_int<?>)column;
          ddl += "INT";
          if (type.get_precision$() != null)
            ddl += "(" + type.get_precision$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();

          if (type.get_generateOnInsert$() != null && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText().equals(type.get_generateOnInsert$().getText()))
            suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText();
        }
        else if (column instanceof $xdl_bigint) {
          final $xdl_bigint<?> type = ($xdl_bigint<?>)column;
          ddl += "BIGINT";
          if (type.get_precision$() != null)
            ddl += "(" + type.get_precision$().getText() + ")";

          if (type.get_unsigned$() != null && type.get_unsigned$().getText())
            ddl += " UNSIGNED";

          if (type.get_zerofill$() != null && type.get_zerofill$().getText())
            ddl += " ZEROFILL";

          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();

          if (type.get_generateOnInsert$() != null && $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText().equals(type.get_generateOnInsert$().getText()))
            suffix += " " + $xdl_tinyint._generateOnInsert$.AUTO__INCREMENT.getText();
        }
        else if (column instanceof $xdl_date) {
          final $xdl_date<?> type = ($xdl_date<?>)column;
          ddl += "DATE";
          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();
        }
        else if (column instanceof $xdl_time) {
          final $xdl_time<?> type = ($xdl_time<?>)column;
          ddl += "TIME";
          if (type.get_default$() != null)
            ddl += " TIME " + type.get_default$().getText();
        }
        else if (column instanceof $xdl_dateTime) {
          final $xdl_dateTime<?> type = ($xdl_dateTime<?>)column;
          ddl += "DATETIME";
          if (type.get_default$() != null)
            ddl += " DEFAULT " + type.get_default$().getText();
        }
        else if (column instanceof $xdl_blob) {
          ddl += "BLOB";
        }

        if (column.get_null$() != null)
          ddl += column.get_null$().getText() ? " NULL" : " NOT NULL";

        ddl += suffix;
      }
    }

    return ddl.substring(2);
  }

  private String parseConstraints(final String tableName, final Map<String,$xdl_columnType<?>> columnNameToColumn, final $xdl_tableType<? extends ComplexType> table) {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table.get_constraints() != null) {
      final $xdl_tableType._constraints constraints = table.get_constraints(0);
      String uniqueString = "";
      int uniqueIndex = 1;
      final List<$xdl_tableType._constraints._unique> uniques = constraints.get_unique();
      if (uniques != null) {
        for (final $xdl_tableType._constraints._unique unique : uniques) {
          final List<$xdl_tableType._constraints._unique._column> columns = unique.get_column();
          String columnsString = "";
          for (final $xdl_tableType._constraints._unique._column column : columns)
            columnsString += ", " + column.get_name$().getText();

          uniqueString += ",\n  CONSTRAINT " + table.get_name$().getText() + "_unique_" + uniqueIndex++ + " UNIQUE (" + columnsString.substring(2) + ")";
        }

        contraintsBuffer.append(uniqueString);
      }

      final $xdl_tableType._constraints._primaryKey primaryKey = constraints.get_primaryKey(0);
      if (primaryKey != null) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xdl_tableType._constraints._primaryKey._column primaryColumn : primaryKey.get_column()) {
          final String primaryKeyColumn = primaryColumn.get_name$().getText();
          final $xdl_columnType<?> column = columnNameToColumn.get(primaryKeyColumn);
          if (column.get_null$().getText()) {
            System.err.println("[ERROR] Column " + tableName + "." + column.get_name$() + " must be NOT NULL to be a PRIMARY KEY.");
            System.exit(1);
          }

          primaryKeyBuffer.append(", ").append(primaryKeyColumn);
        }

        contraintsBuffer.append(",\n  PRIMARY KEY (").append(primaryKeyBuffer.substring(2)).append(")");
      }
    }

    if (table.get_column() != null) {
      for (final $xdl_columnType<?> column : table.get_column()) {
        if (column.get_foreignKey() != null) {
          final $xdl_columnType._foreignKey foreignKey = column.get_foreignKey(0);
          contraintsBuffer.append(",\n  FOREIGN KEY (").append(column.get_name$().getText());
          contraintsBuffer.append(") REFERENCES ").append(foreignKey.get_references$().getText());
          insertDependency(tableName, foreignKey.get_references$().getText());
          contraintsBuffer.append(" (").append(foreignKey.get_column$().getText()).append(")");
          if (foreignKey.get_onUpdate$() != null)
            contraintsBuffer.append(" ON UPDATE ").append(foreignKey.get_onUpdate$().getText());

          if (foreignKey.get_onDelete$() != null)
            contraintsBuffer.append(" ON DELETE ").append(foreignKey.get_onDelete$().getText());
        }
      }

      if (table.get_constraints() != null && table.get_constraints(0).get_foreignKey() != null) {
        for (final $xdl_tableType._constraints._foreignKey foreignKey : table.get_constraints(0).get_foreignKey()) {
          String columns = "";
          String referencedColumns = "";
          for (final $xdl_tableType._constraints._foreignKey._column column : foreignKey.get_column()) {
            columns += ", " + column.get_name$().getText();
            referencedColumns += ", " + column.get_column$().getText();
          }

          contraintsBuffer.append(",\n  FOREIGN KEY (").append(columns.substring(2));
          contraintsBuffer.append(") REFERENCES ").append(foreignKey.get_references$().getText());
          insertDependency(tableName, foreignKey.get_references$().getText());
          contraintsBuffer.append(" (").append(referencedColumns.substring(2)).append(")");
          if (foreignKey.get_onUpdate$() != null)
            contraintsBuffer.append(" ON UPDATE ").append(foreignKey.get_onUpdate$().getText());

          if (foreignKey.get_onDelete$() != null)
            contraintsBuffer.append(" ON DELETE ").append(foreignKey.get_onDelete$().getText());
        }
      }
    }

    return contraintsBuffer.toString();
  }
  
  private static String getTriggerName(final String tableName, final $xdl_tableType._triggers._trigger trigger, final String action) {
    return tableName + "_" + trigger.get_time$().getText().toLowerCase() + "_" + action.toLowerCase();
  }

  private String parseTriggers(final String tableName, final List<$xdl_tableType._triggers._trigger> triggers) {
    String buffer = "";
    for (final $xdl_tableType._triggers._trigger trigger : triggers) {
      for (final String action : trigger.get_actions$().getText()) {
        buffer += "\nDELIMITER |\n";
        buffer += "CREATE TRIGGER " + DDLTransform.getTriggerName(tableName, trigger, action) + " " + trigger.get_time$().getText() + " " + action + " ON " + tableName + "\n";
        buffer += "  FOR EACH ROW\n";
        buffer += "  BEGIN\n";
        
        final String text = trigger.getText().toString();
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
        buffer += "DELIMITER ;";
      }
    }
    
    return buffer.substring(1);
  }

  private void registerColumns(final Set<String> names, final Map<String,$xdl_columnType<?>> columnNameToColumn, final $xdl_tableType<? extends ComplexType> table) {
    final String tableName = table.get_name$().getText();
    if (names.contains(tableName)) {
      System.err.println("[ERROR] Circular dependency detected for table: " + tableName);
      System.exit(1);
    }

    names.add(tableName);
    if (table.get_column() != null) {
      for (final $xdl_columnType<?> column : table.get_column()) {
        final $xdl_columnType<?> existing = columnNameToColumn.get(column.get_name$().getText());
        if (existing != null && !(column instanceof $xdl_inherited)) {
          System.err.println("[ERROR] Duplicate column definition: " + tableName + "." + column.get_name$().getText() + " only xsi:type=\"xdl:inherited\" is alowed when overriding a column.");
          System.exit(1);
        }

        columnNameToColumn.put(column.get_name$().getText(), column);
      }
    }
  }

  private String parseTable(final $xdl_tableType<? extends ComplexType> table) {
    insertDependency(table.get_name$().getText(), null);
    // Next, register the column names to be referencable by the @primaryKey element
    final Map<String,$xdl_columnType<?>> columnNameToColumn = new HashMap<String,$xdl_columnType<?>>();
    final Set<String> names = new HashSet<String>();
    registerColumns(names, columnNameToColumn, table);

    final String tableName = table.get_name$().getText();
    final StringBuffer tableBuffer = new StringBuffer();
    tableBuffer.append("CREATE TABLE ").append(tableName).append(" (\n");
    tableBuffer.append(parseColumns(table));
    tableBuffer.append(parseConstraints(tableName, columnNameToColumn, table));
    tableBuffer.append("\n);");

    if (table.get_triggers() != null)
      tableBuffer.append("\n").append(parseTriggers(tableName, table.get_triggers().get(0).get_trigger()));
    
    return tableBuffer.toString();
  }

  private final Map<String,Set<String>> dependencyGraph = new HashMap<String,Set<String>>();

  private void insertDependency(final String target, final String source) {
    Set<String> dependants = dependencyGraph.get(target);
    if (dependants == null)
      dependencyGraph.put(target, dependants = new HashSet<String>());

    if (source != null)
      dependants.add(source);
  }

  private final Map<String,String> dropStatements = new HashMap<String,String>();
  private final Map<String,String> createStatements = new HashMap<String,String>();

  private String createDropStatement(final $xdl_tableType<? extends ComplexType> table) {
    String buffer = "";
    if (table.get_triggers() != null)
      for (final $xdl_tableType._triggers._trigger trigger : table.get_triggers().get(0).get_trigger())
        for (final String action : trigger.get_actions$().getText())
          buffer += "\nDROP TRIGGER IF EXISTS " + DDLTransform.getTriggerName(table.get_name$().getText(), trigger, action) + ";";

    buffer += "\nDROP TABLE IF EXISTS " + table.get_name$().getText() + ";";
    return buffer.substring(1);
  }

  private String parse() throws Exception {
    final Set<String> skipTables = new HashSet<String>();
    for (final $xdl_tableType<? extends ComplexType> table : merged.get_table()) {
      if (table.get_skip$().getText())
        skipTables.add(table.get_name$().getText());
      else if (!table.get_abstract$().getText())
        dropStatements.put(table.get_name$().getText(), createDropStatement(table));
    }

    for (final $xdl_tableType<? extends ComplexType> table : merged.get_table())
      if (!table.get_abstract$().getText())
        createStatements.put(table.get_name$().getText(), parseTable(table));

    final StringBuffer tablesBuffer = new StringBuffer();
    final List<String> sortedTableOrder = TopologicalSort.sort(dependencyGraph);
    for (int i = sortedTableOrder.size() - 1; 0 <= i; i--)
      if (dropStatements.containsKey(sortedTableOrder.get(i)))
        tablesBuffer.append("\n").append(dropStatements.get(sortedTableOrder.get(i)));

    for (final String tableName : sortedTableOrder)
      if (!skipTables.contains(tableName))
        tablesBuffer.append("\n").append(createStatements.get(tableName));

    return tablesBuffer.substring(1);
  }
}