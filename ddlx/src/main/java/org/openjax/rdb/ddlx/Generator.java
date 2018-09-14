/* Copyright (c) 2011 OpenJAX
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

package org.openjax.rdb.ddlx;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;

import org.fastjax.lang.PackageLoader;
import org.fastjax.lang.PackageNotFoundException;
import org.fastjax.util.Arrays;
import org.fastjax.xml.ValidationException;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.$Column;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.$Table;
import org.openjax.rdb.ddlx_0_9_9.xL0gluGCXYYJc.Schema;
import org.openjax.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Generator {
  protected static final Logger logger = LoggerFactory.getLogger(Generator.class);

  static {
    try {
      PackageLoader.getContextPackageLoader().loadPackage(Schema.class.getPackage().getName());
    }
    catch (final PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  public static void main(final String[] args) throws Exception {
    if (args.length != 2) {
      final String vendors = Arrays.toString(DBVendor.values(), "|");
      throw new GeneratorExecutionException("<" + vendors + "> <XDL_FILE>");
    }

    createDDL(new File(args[1]).toURI().toURL(), DBVendor.valueOf(args[0]));
  }

  public static StatementBatch createDDL(final URL url, final DBVendor vendor) throws GeneratorExecutionException, IOException, ValidationException {
    return new StatementBatch(new Generator(DDLxAudit.makeAudit(url)).parse(vendor));
  }

  private static String checkNameViolation(String string) {
    string = string.toUpperCase();

    final SQLStandard[] enums = ReservedWords.get(string);
    if (enums == null)
      return null;

    final StringBuilder message = new StringBuilder("The name '").append(string).append("' is reserved word in ").append(enums[0]);

    for (int i = 1; i < enums.length; i++)
      message.append(", ").append(enums[i]);

    message.append('.');
    return message.toString();
  }

  protected final DDLxAudit audit;
  protected final Schema schema;

  protected Generator(final DDLxAudit audit) {
    this.audit = audit;
    this.schema = Schemas.flatten(audit.schema());

    final List<String> errors = getErrors();
    if (errors != null && errors.size() > 0)
      for (final String error : errors)
        logger.warn(error);
  }

  private List<String> getErrors() {
    final List<String> errors = new ArrayList<>();
    for (final $Table table : schema.getTable()) {
      if (!table.getAbstract$().text()) {
        if (table.getConstraints() == null || table.getConstraints().getPrimaryKey() == null) {
          errors.add("Table `" + table.getName$().text() + "` does not have a primary key.");
        }
        else {
          for (final $Column column : table.getColumn()) {
            if (audit.isPrimary(table, column) && column.getNull$().text())
              errors.add("Primary key column `" + column.getName$().text() + "` on table `" + table.getName$().text() + "` is NULL.");
          }
        }
      }
    }

    return errors;
  }

  private final Map<String,Integer> columnCount = new HashMap<>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  private static void registerColumns(final Set<String> tableNames, final Map<String,$Column> columnNameToColumn, final $Table table, final Schema schema) throws GeneratorExecutionException {
    final String tableName = table.getName$().text();
    final List<String> violations = new ArrayList<>();
    String nameViolation = checkNameViolation(tableName);
    if (nameViolation != null)
      violations.add(nameViolation);

    if (tableNames.contains(tableName))
      throw new GeneratorExecutionException("Circular table dependency detected: " + tableName);

    tableNames.add(tableName);
    if (table.getColumn() != null) {
      for (final $Column column : table.getColumn()) {
        final String columnName = column.getName$().text();
        nameViolation = checkNameViolation(columnName);
        if (nameViolation != null)
          violations.add(nameViolation);

        final $Column existing = columnNameToColumn.get(columnName);
        if (existing != null)
          throw new GeneratorExecutionException("Duplicate column definition: " + tableName + "." + columnName);

        columnNameToColumn.put(columnName, column);
      }
    }

    if (violations.size() > 0)
      violations.stream().forEach(v -> logger.warn(v));
  }

  private LinkedHashSet<CreateStatement> parseTable(final DBVendor vendor, final $Table table, final Set<String> tableNames) throws GeneratorExecutionException {
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$Column> columnNameToColumn = new HashMap<>();
    registerColumns(tableNames, columnNameToColumn, table, schema);

    final Compiler compiler = Compiler.getCompiler(vendor);
    final LinkedHashSet<CreateStatement> statements = new LinkedHashSet<>();
    statements.addAll(compiler.types(table));

    columnCount.put(table.getName$().text(), table.getColumn() != null ? table.getColumn().size() : 0);
    final CreateStatement createTable = compiler.createTableIfNotExists(table, columnNameToColumn);

    statements.add(createTable);

    statements.addAll(compiler.triggers(table));
    statements.addAll(compiler.indexes(table));
    return statements;
  }

  public LinkedHashSet<Statement> parse(final DBVendor vendor) throws GeneratorExecutionException {
    final Map<String,LinkedHashSet<DropStatement>> dropTableStatements = new HashMap<>();
    final Map<String,LinkedHashSet<DropStatement>> dropTypeStatements = new HashMap<>();
    final Map<String,LinkedHashSet<CreateStatement>> createTableStatements = new HashMap<>();

    final Set<String> skipTables = new HashSet<>();

    for (final $Table table : schema.getTable()) {
      if (table.getSkip$().text()) {
        skipTables.add(table.getName$().text());
      }
      else if (!table.getAbstract$().text()) {
        dropTableStatements.put(table.getName$().text(), Compiler.getCompiler(vendor).dropTable(table));
        dropTypeStatements.put(table.getName$().text(), Compiler.getCompiler(vendor).dropTypes(table));
      }
    }

    final Set<String> tableNames = new HashSet<>();
    for (final $Table table : schema.getTable())
      if (!table.getAbstract$().text())
        createTableStatements.put(table.getName$().text(), parseTable(vendor, table, tableNames));

    final LinkedHashSet<Statement> statements = new LinkedHashSet<>();
    final CreateStatement createSchema = Compiler.getCompiler(vendor).createSchemaIfNotExists(audit.schema());
    if (createSchema != null)
      statements.add(createSchema);

    final ListIterator<$Table> listIterator = schema.getTable().listIterator(schema.getTable().size());
    while (listIterator.hasPrevious()) {
      final $Table table = listIterator.previous();
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName))
        statements.addAll(dropTableStatements.get(tableName));
    }

    for (final $Table table : schema.getTable()) {
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName))
        statements.addAll(dropTypeStatements.get(tableName));
    }

    for (final $Table table : schema.getTable()) {
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName))
        statements.addAll(createTableStatements.get(tableName));
    }

    return statements;
  }
}