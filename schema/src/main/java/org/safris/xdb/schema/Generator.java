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
import org.safris.commons.xml.XMLException;
import org.safris.maven.common.Log;
import org.safris.xdb.schema.standard.ReservedWords;
import org.safris.xdb.schema.standard.SQLStandard;
import org.safris.xdb.xds.xe.$xds_bigint;
import org.safris.xdb.xds.xe.$xds_binary;
import org.safris.xdb.xds.xe.$xds_blob;
import org.safris.xdb.xds.xe.$xds_boolean;
import org.safris.xdb.xds.xe.$xds_char;
import org.safris.xdb.xds.xe.$xds_check;
import org.safris.xdb.xds.xe.$xds_clob;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_columns;
import org.safris.xdb.xds.xe.$xds_compliant;
import org.safris.xdb.xds.xe.$xds_constraints;
import org.safris.xdb.xds.xe.$xds_date;
import org.safris.xdb.xds.xe.$xds_dateTime;
import org.safris.xdb.xds.xe.$xds_decimal;
import org.safris.xdb.xds.xe.$xds_enum;
import org.safris.xdb.xds.xe.$xds_float;
import org.safris.xdb.xds.xe.$xds_foreignKey;
import org.safris.xdb.xds.xe.$xds_int;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_smallint;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_tinyint;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.xdb.xds.xe.$xds_time;
import org.safris.xdb.xds.xe.xds_schema;

public final class Generator extends BaseGenerator {
  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      final String vendors = Arrays.toString(DBVendor.values(), "|");
      throw new GeneratorExecutionException("<" + vendors + "> <XDL_FILE>");
    }

    createDDL(new File(args[1]).toURI().toURL(), DBVendor.parse(args[0]), null);
  }

  public static String[] createDDL(final URL url, final DBVendor vendor) throws GeneratorExecutionException, IOException, XMLException {
    return Generator.createDDL(url, vendor, null);
  }

  public static String[] createDDL(final URL url, final DBVendor vendor, final File outDir) throws GeneratorExecutionException, IOException, XMLException {
    return Generator.createDDL(parseArguments(url, outDir), vendor, outDir);
  }

  public static String[] createDDL(final xds_schema schema, final DBVendor vendor, final File outDir) throws GeneratorExecutionException {
    final Generator generator = new Generator(schema);
    final Statement[] ddls = generator.parse(vendor);
    final StringBuilder sql = new StringBuilder();
    for (int i = ddls.length - 1; i >= 0; --i)
      if (ddls[i].drop != null)
        for (final String drop : ddls[i].drop)
          sql.append(drop).append(";\n");

    if (sql.length() > 0)
      sql.append("\n");

    for (final Statement ddl : ddls)
      for (final String create : ddl.create)
        sql.append(create).append(";\n\n");

    final String out = vendor == DBVendor.DERBY ? "CREATE SCHEMA " + schema._name$().text() + ";\n\n" + sql : sql.toString();
    writeOutput(out, outDir != null ? new File(outDir, generator.merged._name$().text() + ".sql") : null);
    return out.split("\\s*;\\s*");
  }

  private static String checkNameViolation(String string, final boolean strict) {
    string = string.toUpperCase();

    final SQLStandard[] enums = ReservedWords.get(string);
    if (enums == null)
      return null;

    final StringBuilder message = new StringBuilder("The name '").append(string).append("' is reserved word in ").append(enums[0]);

    for (int i = 1; i < enums.length; i++)
      message.append(", ").append(enums[i]);

    message.append(".");
    return message.toString();
  }

  private Generator(final xds_schema schema) {
    super(schema);
    sortedTableOrder = Schemas.tables(merged);
  }

  private final Map<String,Integer> columnCount = new HashMap<String,Integer>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static String parseColumn(final $xds_table table, final $xds_column column, final DBVendor vendor) {
    final StringBuilder ddl = new StringBuilder();
    ddl.append(column._name$().text()).append(" ");
    if (column instanceof $xds_char) {
      final $xds_char type = ($xds_char)column;
      ddl.append(vendor.getSQLSpec().declareChar(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $xds_clob) {
      final $xds_clob type = ($xds_clob)column;
      ddl.append(vendor.getSQLSpec().declareClob(type._length$().text()));
    }
    else if (column instanceof $xds_binary) {
      final $xds_binary type = ($xds_binary)column;
      ddl.append(vendor.getSQLSpec().declareBinary(type._varying$().text(), type._length$().text()));
    }
    else if (column instanceof $xds_blob) {
      final $xds_blob type = ($xds_blob)column;
      ddl.append(vendor.getSQLSpec().declareBlob(type._length$().text()));
    }
    else if (column instanceof $xds_tinyint) {
      final $xds_tinyint type = ($xds_tinyint)column;
      ddl.append(vendor.getSQLSpec().declareInt8(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_smallint) {
      final $xds_smallint type = ($xds_smallint)column;
      ddl.append(vendor.getSQLSpec().declareInt16(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_int) {
      final $xds_int type = ($xds_int)column;
      ddl.append(vendor.getSQLSpec().declareInt32(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_bigint) {
      final $xds_bigint type = ($xds_bigint)column;
      if (!type._unsigned$().text() && type._precision$().text().intValue() > 19)
        throw new IllegalArgumentException("BIGINT maximum precision [0, 19] exceeded: " + type._precision$().text().intValue());

      ddl.append(vendor.getSQLSpec().declareInt64(type._precision$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_float) {
      final $xds_float type = ($xds_float)column;
      ddl.append(vendor.getSQLSpec().declareFloat(type._double$().text(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_decimal) {
      final $xds_decimal type = ($xds_decimal)column;
      ddl.append(vendor.getSQLSpec().declareDecimal(type._precision$().text().shortValue(), type._scale$().text().shortValue(), type._unsigned$().text()));
    }
    else if (column instanceof $xds_date) {
      ddl.append(vendor.getSQLSpec().declareDate());
    }
    else if (column instanceof $xds_time) {
      final $xds_time type = ($xds_time)column;
      ddl.append(vendor.getSQLSpec().declareTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $xds_dateTime) {
      final $xds_dateTime type = ($xds_dateTime)column;
      ddl.append(vendor.getSQLSpec().declareDateTime(type._precision$().text().shortValue()));
    }
    else if (column instanceof $xds_boolean) {
      ddl.append(vendor.getSQLSpec().declareBoolean());
    }
    else if (column instanceof $xds_enum) {
      final $xds_enum type = ($xds_enum)column;
      ddl.append(vendor.getSQLSpec().declareEnum(table, type));
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

  private static String parseConstraints(final DBVendor vendor, final String tableName, final Map<String,$xds_column> columnNameToColumn, final $xds_table table) throws GeneratorExecutionException {
    final StringBuffer contraintsBuffer = new StringBuffer();
    if (table._constraints() != null) {
      final $xds_constraints constraints = table._constraints(0);

      // unique constraint
      final List<$xds_columns> uniques = constraints._unique();
      if (uniques != null) {
        String uniqueString = "";
        int uniqueIndex = 1;
        for (final $xds_columns unique : uniques) {
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
      final $xds_columns primaryKey = constraints._primaryKey(0);
      if (!primaryKey.isNull()) {
        final StringBuffer primaryKeyBuffer = new StringBuffer();
        for (final $xds_named primaryColumn : primaryKey._column()) {
          final String primaryKeyColumn = primaryColumn._name$().text();
          final $xds_column column = columnNameToColumn.get(primaryKeyColumn);
          if (column._null$().text())
            throw new GeneratorExecutionException("Column " + tableName + "." + column._name$() + " must be NOT NULL to be a PRIMARY KEY.");

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
          if (column instanceof $xds_tinyint) {
            final $xds_tinyint type = ($xds_tinyint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_smallint) {
            final $xds_smallint type = ($xds_smallint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_int) {
            final $xds_int type = ($xds_int)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else if (column instanceof $xds_bigint) {
            final $xds_bigint type = ($xds_bigint)column;
            minCheck = !type._min$().isNull() ? String.valueOf(type._min$().text()) : null;
            maxCheck = !type._max$().isNull() ? String.valueOf(type._max$().text()) : null;
          }
          else {
            throw new UnsupportedOperationException("Unexpected type: " + column.getClass().getName());
          }
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

  private static void registerColumns(final Set<String> tableNames, final Map<String,$xds_column> columnNameToColumn, final $xds_table table, final xds_schema schema) throws GeneratorExecutionException {
    final boolean strict = $xds_compliant._compliance$.strict.text().equals(schema._compliance$().text());
    final String tableName = table._name$().text();
    final List<String> violations = new ArrayList<String>();
    String violation = checkNameViolation(tableName, strict);
    if (violation != null)
      violations.add(violation);

    if (tableNames.contains(tableName))
      throw new GeneratorExecutionException("Circular table dependency detected: " + schema._name$().text() + "." + tableName);

    tableNames.add(tableName);
    if (table._column() != null) {
      for (final $xds_column column : table._column()) {
        final String columnName = column._name$().text();
        violation = checkNameViolation(columnName, strict);
        if (violation != null)
          violations.add(violation);

        final $xds_column existing = columnNameToColumn.get(columnName);
        if (existing != null)
          throw new GeneratorExecutionException("Duplicate column definition: " + schema._name$().text() + "." + tableName + "." + columnName);

        columnNameToColumn.put(columnName, column);
      }
    }

    if (violations.size() > 0) {
      if (strict) {
        final StringBuilder builder = new StringBuilder();
        for (final String v : violations)
          builder.append(" ").append(v);

        throw new GeneratorExecutionException(schema._name$().text() + ": " + builder.substring(1));
      }

      for (final String v : violations)
        Log.warn(v);
    }
  }

  private String[] parseTable(final DBVendor vendor, final $xds_table table, final Set<String> tableNames) throws GeneratorExecutionException {
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$xds_column> columnNameToColumn = new HashMap<String,$xds_column>();
    registerColumns(tableNames, columnNameToColumn, table, merged);

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

  private final List<$xds_table> sortedTableOrder;

  public Statement[] parse(final DBVendor vendor) throws GeneratorExecutionException {
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

    final List<Statement> ddls = new ArrayList<Statement>();
    for (final $xds_table table : sortedTableOrder) {
      final String tableName = table._name$().text();
      if (!skipTables.contains(tableName))
        ddls.add(new Statement(tableName, dropStatements.get(tableName), createTableStatements.get(tableName)));
    }

    return ddls.toArray(new Statement[ddls.size()]);
  }
}