/* Copyright (c) 2011 lib4j
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

package org.libx4j.rdb.ddlx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.lib4j.lang.Arrays;
import org.lib4j.xml.XMLException;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_compliant;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Generator extends BaseGenerator {
  protected static final Logger logger = LoggerFactory.getLogger(Generator.class);

  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      final String vendors = Arrays.toString(DBVendor.values(), "|");
      throw new GeneratorExecutionException("<" + vendors + "> <XDL_FILE>");
    }

    createDDL(new File(args[1]).toURI().toURL(), DBVendor.valueOf(args[0]), null);
  }

  public static List<Statement> createDDL(final URL url, final DBVendor vendor, final File outDir) throws GeneratorExecutionException, IOException, XMLException {
    return Generator.createDDL(DDLxAudit.makeAudit(url, outDir), vendor, outDir);
  }

  private static List<Statement> createDDL(final DDLxAudit audit, final DBVendor vendor, final File outDir) throws GeneratorExecutionException {
    final Generator generator = new Generator(audit);
    final List<Statement> statements = generator.parse(vendor);
    writeOutput(statements, outDir != null ? new File(outDir, generator.merged._name$().text() + ".sql") : null);
    return statements;
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

  protected Generator(final DDLxAudit audit) {
    super(audit);
    sortedTableOrder = Schemas.tables(merged);
  }

  private final Map<String,Integer> columnCount = new HashMap<String,Integer>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static void registerColumns(final Set<String> tableNames, final Map<String,$ddlx_column> columnNameToColumn, final $ddlx_table table, final ddlx_schema schema) throws GeneratorExecutionException {
    final boolean strict = $ddlx_compliant._compliance$.strict.text().equals(schema._compliance$().text());
    final String tableName = table._name$().text();
    final List<String> violations = new ArrayList<String>();
    String violation = checkNameViolation(tableName, strict);
    if (violation != null)
      violations.add(violation);

    if (tableNames.contains(tableName))
      throw new GeneratorExecutionException("Circular table dependency detected: " + schema._name$().text() + "." + tableName);

    tableNames.add(tableName);
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        final String columnName = column._name$().text();
        violation = checkNameViolation(columnName, strict);
        if (violation != null)
          violations.add(violation);

        final $ddlx_column existing = columnNameToColumn.get(columnName);
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
        logger.warn(v);
    }
  }

  private List<CreateStatement> parseTable(final DBVendor vendor, final $ddlx_table table, final Set<String> tableNames) throws GeneratorExecutionException {
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$ddlx_column> columnNameToColumn = new HashMap<String,$ddlx_column>();
    registerColumns(tableNames, columnNameToColumn, table, merged);

    final Serializer serializer = Serializer.getSerializer(vendor);
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    statements.addAll(serializer.types(table));

    columnCount.put(table._name$().text(), table._column() != null ? table._column().size() : 0);
    final CreateStatement createTable = serializer.createTableIfNotExists(table, columnNameToColumn);

    statements.add(createTable);

    statements.addAll(serializer.triggers(table));
    statements.addAll(serializer.indexes(table));
    return statements;
  }

  private final List<$ddlx_table> sortedTableOrder;

  public List<Statement> parse(final DBVendor vendor) throws GeneratorExecutionException {
    final Map<String,List<DropStatement>> dropStatements = new HashMap<String,List<DropStatement>>();
    final Map<String,List<CreateStatement>> createTableStatements = new HashMap<String,List<CreateStatement>>();

    final Set<String> skipTables = new HashSet<String>();
    for (final $ddlx_table table : merged._table()) {
      if (table._skip$().text()) {
        skipTables.add(table._name$().text());
      }
      else if (!table._abstract$().text()) {
        final List<DropStatement> drops = Serializer.getSerializer(vendor).drops(table);
        dropStatements.put(table._name$().text(), drops);
      }
    }

    final Set<String> tableNames = new HashSet<String>();
    for (final $ddlx_table table : merged._table())
      if (!table._abstract$().text())
        createTableStatements.put(table._name$().text(), parseTable(vendor, table, tableNames));

    final List<Statement> statements = new ArrayList<Statement>();
    final CreateStatement createSchema = Serializer.getSerializer(vendor).createSchemaIfNotExists(audit.schema());
    if (createSchema != null)
      statements.add(createSchema);

    for (final $ddlx_table table : sortedTableOrder) {
      final String tableName = table._name$().text();
      if (!skipTables.contains(tableName)) {
        statements.addAll(0, dropStatements.get(tableName));
        statements.addAll(createTableStatements.get(tableName));
      }
    }

    return statements;
  }
}