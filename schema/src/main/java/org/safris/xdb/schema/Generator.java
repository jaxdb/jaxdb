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
import org.safris.commons.xml.XMLException;
import org.safris.maven.common.Log;
import org.safris.xdb.schema.standard.ReservedWords;
import org.safris.xdb.schema.standard.SQLStandard;
import org.safris.xdb.xds.xe.$xds_column;
import org.safris.xdb.xds.xe.$xds_compliant;
import org.safris.xdb.xds.xe.$xds_table;
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
    final StringBuilder builder = new StringBuilder();
    final String createSchema = vendor.getSQLSpec().createSchemaIfNotExists(schema);
    if (createSchema != null)
      builder.append(createSchema).append(";\n\n");

    for (int i = ddls.length - 1; i >= 0; --i)
      if (ddls[i].drop != null)
        for (final String drop : ddls[i].drop)
          builder.append(drop).append(";\n");

    if (builder.length() > 0)
      builder.append("\n");

    for (final Statement ddl : ddls)
      for (final String create : ddl.create)
        builder.append(create).append(";\n\n");

    final String out = builder.toString();
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

    columnCount.put(table._name$().text(), table._column() != null ? table._column().size() : 0);
    final String createTable = vendor.getSQLSpec().createTableIfNotExists(table, columnNameToColumn);

    statements.add(createTable);

    statements.addAll(vendor.getSQLSpec().triggers(table));
    statements.addAll(vendor.getSQLSpec().indexes(table));
    return statements.toArray(new String[statements.size()]);
  }

  private final List<$xds_table> sortedTableOrder;

  public Statement[] parse(final DBVendor vendor) throws GeneratorExecutionException {
    final Map<String,String[]> dropStatements = new HashMap<String,String[]>();
    final Map<String,String[]> createTableStatements = new HashMap<String,String[]>();

    final Set<String> skipTables = new HashSet<String>();
    for (final $xds_table table : merged._table()) {
      if (table._skip$().text()) {
        skipTables.add(table._name$().text());
      }
      else if (!table._abstract$().text()) {
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