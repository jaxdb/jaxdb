/* Copyright (c) 2011 JAX-DB
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

package org.jaxdb.ddlx;

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

import javax.xml.transform.TransformerException;

import org.jaxdb.vendor.DBVendor;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.Schema;
import org.libj.lang.PackageLoader;
import org.libj.lang.PackageNotFoundException;
import org.libj.util.ArrayUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public final class Generator {
  static final Logger logger = LoggerFactory.getLogger(Generator.class);

  static {
    try {
      PackageLoader.getContextPackageLoader().loadPackage(Schema.class.getPackage().getName());
    }
    catch (final IOException | PackageNotFoundException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  private static void trapPrintUsage() {
    final String vendors = ArrayUtil.toString(DBVendor.values(), "|");
    System.err.println("Usage: Generator <-d DEST_DIR> <-v VENDOR> <DDLx_FILE>");
    System.err.println();
    System.err.println("Mandatory arguments:");
    System.err.println("  -v <VENDOR>        One of: <" + vendors + ">");
    System.err.println("  -d <DEST_DIR>      Specify the destination directory.");
    System.exit(1);
  }

  public static void main(final String[] args) throws GeneratorExecutionException, IOException, SAXException, TransformerException {
    if (args.length != 5)
      trapPrintUsage();

    DBVendor vendor = null;
    File destDir = null;
    URL schemaUrl = null;
    String sqlFileName = null;
    for (int i = 0, i$ = args.length; i < i$; ++i) { // [A]
      if ("-v".equals(args[i]))
        vendor = DBVendor.valueOf(args[++i]);
      else if ("-d".equals(args[i]))
        destDir = new File(args[++i]).getAbsoluteFile();
      else {
        final File schemaFile = new File(args[i]);
        sqlFileName = schemaFile.getName().substring(0, schemaFile.getName().lastIndexOf('.') + 1) + "sql";
        schemaUrl = schemaFile.getAbsoluteFile().toURI().toURL();
      }
    }

    if (vendor == null || destDir == null || schemaUrl == null) {
      trapPrintUsage();
    }
    else {
      final StatementBatch statementBatch = createDDL(new DDLx(schemaUrl), vendor);
      destDir.mkdirs();
      statementBatch.writeOutput(new File(destDir, sqlFileName));
    }
  }

  public static StatementBatch createDDL(final DDLx ddlx, final DBVendor vendor) throws GeneratorExecutionException {
    return new StatementBatch(new Generator(ddlx).parse(vendor));
  }

  private static String checkNameViolation(String string) {
    string = string.toUpperCase();

    final SQLStandard[] enums = ReservedWords.get(string);
    if (enums == null)
      return null;

    final StringBuilder message = new StringBuilder("The name '").append(string).append("' is reserved word in ").append(enums[0]);

    for (int i = 1, i$ = enums.length; i < i$; ++i) // [A]
      message.append(", ").append(enums[i]);

    message.append('.');
    return message.toString();
  }

  private final DDLx ddlx;

  Generator(final DDLx ddlx) {
    this.ddlx = ddlx;

    if (logger.isWarnEnabled()) {
      final ArrayList<String> errors = getErrors();
      for (int i = 0, i$ = errors.size(); i < i$; ++i) // [RA]
        logger.warn(errors.get(i));
    }
  }

  private ArrayList<String> getErrors() {
    final ArrayList<String> errors = new ArrayList<>();
    final List<$Table> tables = ddlx.getMergedSchema().getTable();
    for (int i = 0, i$ = tables.size(); i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      if (table.getConstraints() == null || table.getConstraints().getPrimaryKey() == null) {
        errors.add("Table `" + table.getName$().text() + "` does not have a primary key.");
      }
      else {
        final List<$Column> columns = table.getColumn();
        for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
          final $Column column = columns.get(j);
          if (ddlx.isPrimary(table, column) && (column.getNull$() == null || column.getNull$().text()))
            errors.add("Primary key column `" + column.getName$().text() + "` on table `" + table.getName$().text() + "` is NULL.");
        }
      }
    }

    return errors;
  }

  private final Map<String,Integer> columnCount = new HashMap<>();

  public Map<String,Integer> getColumnCount() {
    return columnCount;
  }

  static class ColumnRef {
    final $Column column;
    final int index;

    private ColumnRef(final $Column column, final int index) {
      this.column = column;
      this.index = index;
    }
  }

  private static void registerColumns(final $Table table, final Set<? super String> tableNames, final Map<? super String,ColumnRef> columnNameToColumn) throws GeneratorExecutionException {
    final String tableName = table.getName$().text();
    final List<String> violations = new ArrayList<>();
    String nameViolation = checkNameViolation(tableName);
    if (nameViolation != null)
      violations.add(nameViolation);

    if (tableNames.contains(tableName))
      throw new GeneratorExecutionException("Circular table dependency detected: " + tableName);

    tableNames.add(tableName);
    final List<$Column> columns = table.getColumn();
    if (columns != null) {
      for (int c = 0, c$ = columns.size(); c < c$; ++c) { // [RA]
        final $Column column = columns.get(c);
        final String columnName = column.getName$().text();
        nameViolation = checkNameViolation(columnName);
        if (nameViolation != null)
          violations.add(nameViolation);

        final ColumnRef existing = columnNameToColumn.get(columnName);
        if (existing != null)
          throw new GeneratorExecutionException("Duplicate column definition: " + tableName + "." + columnName);

        columnNameToColumn.put(columnName, new ColumnRef(column, c));
      }
    }

    if (violations.size() > 0)
      violations.forEach(logger::warn);
  }

  private LinkedHashSet<CreateStatement> parseTable(final DBVendor vendor, final $Table table, final Set<? super String> tableNames, final Map<String,Map<String,String>> tableNameToEnumToOwner) throws GeneratorExecutionException {
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,ColumnRef> columnNameToColumn = new HashMap<>();
    registerColumns(table, tableNames, columnNameToColumn);

    final Compiler compiler = Compiler.getCompiler(vendor);
    final LinkedHashSet<CreateStatement> statements = new LinkedHashSet<>(compiler.types(table, tableNameToEnumToOwner));
    // FIXME: Redo this whole "CreateStatement" class model
    final LinkedHashSet<CreateStatement> createStatements = new LinkedHashSet<>();

    columnCount.put(table.getName$().text(), table.getColumn() == null ? 0 : table.getColumn().size());
    final CreateStatement createTable = compiler.createTableIfNotExists(createStatements, table, columnNameToColumn, tableNameToEnumToOwner);

    statements.add(createTable);
    statements.addAll(createStatements);

    statements.addAll(compiler.triggers(table));
    statements.addAll(compiler.indexes(table, columnNameToColumn));
    return statements;
  }

  public LinkedHashSet<Statement> parse(final DBVendor vendor) throws GeneratorExecutionException {
    final Map<String,LinkedHashSet<DropStatement>> dropTableStatements = new HashMap<>();
    final Map<String,LinkedHashSet<DropStatement>> dropTypeStatements = new HashMap<>();
    final Map<String,LinkedHashSet<CreateStatement>> createTableStatements = new HashMap<>();

    final Schema normalized = ddlx.getNormalizedSchema();

    // The following code resolves a problem with ENUM types. The DDLx is generated from merged schema, whereby the original owner
    // of the ENUM type is lost. The jSQL, however, is generated from the normalized schema, where the owner of the ENUM type is
    // present. The `tableNameToEnumToOwner` variable is a map for each table linking each table's ENUMs to their original owners.
    final Map<String,$Table> tableNameToTable = new HashMap<>();
    final Map<String,Map<String,String>> tableNameToEnumToOwner = new HashMap<String,Map<String,String>>() {
      @Override
      public Map<String,String> get(final Object key) {
        final String tableName = (String)key;
        Map<String,String> map = super.get(key);
        if (map == null)
          put(tableName, map = new HashMap<>());

        return map;
      }
    };

    List<$Table> tables = normalized.getTable();
    int i$ = tables.size();
    for (int i = 0; i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      tableNameToTable.put(table.getName$().text(), table);
    }

    for (int i = 0; i < i$; ++i) { // [RA]
      $Table table = tables.get(i);
      if (table.getAbstract$().text())
        continue;

      final Map<String,String> colNameToOwnerTable = tableNameToEnumToOwner.get(table.getName$().text());
      do {
        final List<$Column> columns = table.getColumn();
        for (int j = 0, j$ = columns.size(); j < j$; ++j) { // [RA]
          final $Column column = columns.get(j);
          if (column instanceof $Enum)
            colNameToOwnerTable.put(column.getName$().text(), table.getName$().text());
        }

        table = table.getExtends$() != null ? tableNameToTable.get(table.getExtends$().text()) : null;
      }
      while (table != null);
    }

    final Set<String> skipTables = new HashSet<>();
    final Schema merged = ddlx.getMergedSchema();
    tables = merged.getTable();
    i$ = tables.size();
    for (int i = 0; i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      if (table.getSkip$().text()) {
        skipTables.add(table.getName$().text());
      }
      else if (!table.getAbstract$().text()) {
        dropTableStatements.put(table.getName$().text(), Compiler.getCompiler(vendor).dropTable(table));
        dropTypeStatements.put(table.getName$().text(), Compiler.getCompiler(vendor).dropTypes(table, tableNameToEnumToOwner));
      }
    }

    final Set<String> tableNames = new HashSet<>();
    for (int i = 0; i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      if (!table.getAbstract$().text())
        createTableStatements.put(table.getName$().text(), parseTable(vendor, table, tableNames, tableNameToEnumToOwner));
    }

    final LinkedHashSet<Statement> statements = new LinkedHashSet<>();
    final CreateStatement createSchema = Compiler.getCompiler(vendor).createSchemaIfNotExists(merged);
    if (createSchema != null)
      statements.add(createSchema);

    final ListIterator<$Table> listIterator = tables.listIterator(tables.size());
    while (listIterator.hasPrevious()) {
      final $Table table = listIterator.previous();
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName) && !table.getAbstract$().text())
        statements.addAll(dropTableStatements.get(tableName));
    }

    for (int i = 0; i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName) && !table.getAbstract$().text())
        statements.addAll(dropTypeStatements.get(tableName));
    }

    for (int i = 0; i < i$; ++i) { // [RA]
      final $Table table = tables.get(i);
      final String tableName = table.getName$().text();
      if (!skipTables.contains(tableName) && !table.getAbstract$().text())
        statements.addAll(createTableStatements.get(tableName));
    }

    return statements;
  }

  public static boolean isAuto(final $Column column) {
    if (!(column instanceof $Integer))
      return false;

    final $Integer integer = ($Integer)column;
    return integer.getGenerateOnInsert$() != null && $Integer.GenerateOnInsert$.AUTO_5FINCREMENT.text().equals(integer.getGenerateOnInsert$().text());
  }
}