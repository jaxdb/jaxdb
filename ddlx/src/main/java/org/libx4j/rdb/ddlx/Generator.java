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
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.lib4j.lang.Arrays;
import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.libx4j.rdb.ddlx.xe.$ddlx_bigint;
import org.libx4j.rdb.ddlx.xe.$ddlx_changeRule;
import org.libx4j.rdb.ddlx.xe.$ddlx_char;
import org.libx4j.rdb.ddlx.xe.$ddlx_check;
import org.libx4j.rdb.ddlx.xe.$ddlx_column;
import org.libx4j.rdb.ddlx.xe.$ddlx_compliant;
import org.libx4j.rdb.ddlx.xe.$ddlx_decimal;
import org.libx4j.rdb.ddlx.xe.$ddlx_double;
import org.libx4j.rdb.ddlx.xe.$ddlx_float;
import org.libx4j.rdb.ddlx.xe.$ddlx_foreignKey;
import org.libx4j.rdb.ddlx.xe.$ddlx_int;
import org.libx4j.rdb.ddlx.xe.$ddlx_named;
import org.libx4j.rdb.ddlx.xe.$ddlx_smallint;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;
import org.libx4j.rdb.ddlx.xe.$ddlx_tinyint;
import org.libx4j.rdb.ddlx.xe.ddlx_schema;
import org.libx4j.rdb.vendor.DBVendor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Generator {
  protected static final Logger logger = LoggerFactory.getLogger(Generator.class);

  static {
    try {
      PackageLoader.getSystemContextPackageLoader().loadPackage(ddlx_schema.class.getPackage().getName());
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

  private static $ddlx_changeRule.Enum toBinding(final short rule) {
    if (rule == 1)
      return null;

    if (rule == 2)
      return $ddlx_changeRule.CASCADE;

    if (rule == 3)
      return $ddlx_changeRule.SET_20NULL;

    if (rule == 4)
      return $ddlx_changeRule.SET_20DEFAULT;

    if (rule == 5)
      return $ddlx_changeRule.RESTRICT;

    throw new UnsupportedOperationException("Unsupported rule: " + rule);
  }

  private static String getType(final short type) {
    return type != 3 ? "HASH" : "BTREE";
  }

  public static ddlx_schema createDDL(final Connection connection) throws SQLException {
    final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
    final Compiler compiler = Compiler.getCompiler(vendor);
    final DatabaseMetaData metaData = connection.getMetaData();
    final ResultSet tableRows = metaData.getTables(null, null, null, new String[] {"TABLE"});
    final ddlx_schema schema = new ddlx_schema();
    final Map<String,List<$ddlx_check>> tableNameToChecks = compiler.getCheckConstraints(connection);
    final Map<String,List<$ddlx_table._constraints._unique>> tableNameToUniques = compiler.getUniqueConstraints(connection);
    final Map<String,$ddlx_table._indexes> tableNameToIndexes = compiler.getIndexes(connection);
    while (tableRows.next()) {
      final String tableName = tableRows.getString(3);
      final $ddlx_table table = new ddlx_schema._table();
      table._name$(new $ddlx_named._name$(tableName.toLowerCase()));
      schema._table(table);

      final ResultSet columnRows = metaData.getColumns(null, null, tableName, null);
      final Map<String,$ddlx_column> nameToColumn = new HashMap<String,$ddlx_column>();
      final Map<String,$ddlx_column> columnNameToColumn = new HashMap<String,$ddlx_column>();
      final Map<Integer,$ddlx_column> columnNumberToColumn = new TreeMap<Integer,$ddlx_column>();
      while (columnRows.next()) {
        final String columnName = columnRows.getString("COLUMN_NAME").toLowerCase();
        final String typeName = columnRows.getString("TYPE_NAME");
        final int columnSize = columnRows.getInt("COLUMN_SIZE");
        final String _default = columnRows.getString("COLUMN_DEF");
        final int index = columnRows.getInt("ORDINAL_POSITION");
        final String nullable = columnRows.getString("IS_NULLABLE");
        final String autoIncrement = columnRows.getString("IS_AUTOINCREMENT");
        final int decimalDigits = columnRows.getInt("DECIMAL_DIGITS");
        final $ddlx_column column = compiler.makeColumn(columnName.toLowerCase(), typeName, columnSize, decimalDigits, _default, nullable.length() == 0 ? null : "YES".equals(nullable), autoIncrement.length() == 0 ? null : "YES".equals(autoIncrement));
        columnNameToColumn.put(columnName, column);
        columnNumberToColumn.put(index, column);
        nameToColumn.put(columnName, column);
      }

      columnNumberToColumn.values().stream().forEach(c -> table._column(c));

      final ResultSet primaryKeyRows = metaData.getPrimaryKeys(null, null, tableName);
      while (primaryKeyRows.next()) {
        final String columnName = primaryKeyRows.getString("COLUMN_NAME").toLowerCase();
        if (table._constraints() == null)
          table._constraints(new $ddlx_table._constraints());

        if (table._constraints(0)._primaryKey() == null)
          table._constraints(0)._primaryKey(new $ddlx_table._constraints._primaryKey());

        final $ddlx_table._constraints._primaryKey._column column = new $ddlx_table._constraints._primaryKey._column();
        column._name$(new $ddlx_table._constraints._primaryKey._column._name$(columnName));
        table._constraints(0)._primaryKey(0)._column(column);
      }

      final List<$ddlx_table._constraints._unique> uniques = tableNameToUniques == null ? null : tableNameToUniques.get(tableName);
      if (uniques != null && uniques.size() > 0) {
        if (table._constraints() == null)
          table._constraints(new $ddlx_table._constraints());

        for (final $ddlx_table._constraints._unique unique : uniques)
          table._constraints(0)._unique(unique);
      }

      final ResultSet indexRows = metaData.getIndexInfo(null, null, tableName, false, true);
      final Map<String,TreeMap<Short,String>> indexNameToIndex = new HashMap<String,TreeMap<Short,String>>();
      final Map<String,String> indexNameToType = new HashMap<String,String>();
      final Map<String,Boolean> indexNameToUnique = new HashMap<String,Boolean>();
      while (indexRows.next()) {
        final String columnName = indexRows.getString("COLUMN_NAME").toLowerCase();
        if (columnName == null)
          continue;

        final String indexName = indexRows.getString("INDEX_NAME").toLowerCase();
        TreeMap<Short,String> indexes = indexNameToIndex.get(indexName);
        if (indexes == null)
          indexNameToIndex.put(indexName, indexes = new TreeMap<Short,String>());

        final short ordinalPosition = indexRows.getShort("ORDINAL_POSITION");
        indexes.put(ordinalPosition, columnName);

        final String type = getType(indexRows.getShort("TYPE"));
        final String currentType = indexNameToType.get(indexName);
        if (currentType == null)
          indexNameToType.put(indexName, type);
        else if (!type.equals(currentType))
          throw new IllegalStateException("Expected " + type + " = " + currentType);

        final boolean unique = !indexRows.getBoolean("NON_UNIQUE");
        final Boolean currentUnique = indexNameToUnique.get(indexName);
        if (currentUnique == null)
          indexNameToUnique.put(indexName, unique);
        else if (unique != currentUnique)
          throw new IllegalStateException("Expected " + unique + " = " + currentType);
      }

      if (tableNameToIndexes != null && tableNameToIndexes.size() > 0) {
        final $ddlx_table._indexes indexes = tableNameToIndexes.get(tableName);
        if (indexes != null)
          table._indexes(indexes);
      }

      final List<$ddlx_check> checks = tableNameToChecks == null ? null : tableNameToChecks.get(tableName);
      if (checks != null && checks.size() > 0)
        for (final $ddlx_check check : checks)
          addCheck(columnNameToColumn.get(check._column(0).text()), check);

      final ResultSet foreignKeyRows = metaData.getImportedKeys(null, null, tableName);
      while (foreignKeyRows.next()) {
        final String primaryTable = foreignKeyRows.getString("PKTABLE_NAME").toLowerCase();
        final String primaryColumn = foreignKeyRows.getString("PKCOLUMN_NAME").toLowerCase();
        final String columnName = foreignKeyRows.getString("FKCOLUMN_NAME").toLowerCase();
        final short updateRule = foreignKeyRows.getShort("UPDATE_RULE");
        final short deleteRule = foreignKeyRows.getShort("DELETE_RULE");
        final $ddlx_foreignKey foreignKey = new $ddlx_column._foreignKey();
        foreignKey._references$(new $ddlx_foreignKey._references$(primaryTable));
        foreignKey._column$(new $ddlx_foreignKey._column$(primaryColumn));

        final $ddlx_changeRule.Enum onUpdate = toBinding(updateRule);
        if (onUpdate != null)
          foreignKey._onUpdate$(new $ddlx_foreignKey._onUpdate$(onUpdate));

        final $ddlx_changeRule.Enum onDelete = toBinding(deleteRule);
        if (onDelete != null)
          foreignKey._onDelete$(new $ddlx_foreignKey._onDelete$(onDelete));

        nameToColumn.get(columnName)._foreignKey(foreignKey);
      }
    }

    return schema;
  }

  private static void addCheck(final $ddlx_column column, final $ddlx_check check) {
    if (column instanceof $ddlx_char)
      dt.CHAR.addCheck(($ddlx_char)column, check);
    else if (column instanceof $ddlx_tinyint)
      dt.TINYINT.addCheck(($ddlx_tinyint)column, check);
    else if (column instanceof $ddlx_smallint)
      dt.SMALLINT.addCheck(($ddlx_smallint)column, check);
    else if (column instanceof $ddlx_int)
      dt.INT.addCheck(($ddlx_int)column, check);
    else if (column instanceof $ddlx_bigint)
      dt.BIGINT.addCheck(($ddlx_bigint)column, check);
    else if (column instanceof $ddlx_float)
      dt.FLOAT.addCheck(($ddlx_float)column, check);
    else if (column instanceof $ddlx_double)
      dt.DOUBLE.addCheck(($ddlx_double)column, check);
    else if (column instanceof $ddlx_decimal)
      dt.DECIMAL.addCheck(($ddlx_decimal)column, check);
    else
      throw new UnsupportedOperationException("Unsupported check for column type: " + column.getClass().getName());
  }

  public static StatementBatch createDDL(final URL url, final DBVendor vendor) throws GeneratorExecutionException, IOException {
    return Generator.createDDL(DDLxAudit.makeAudit(url), vendor);
  }

  private static StatementBatch createDDL(final DDLxAudit audit, final DBVendor vendor) throws GeneratorExecutionException {
    final Generator generator = new Generator(audit);
    final StatementBatch statementBatch = new StatementBatch(generator.parse(vendor));
    return statementBatch;
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

  protected final DDLxAudit audit;
  protected final ddlx_schema schema;

  protected Generator(final DDLxAudit audit) {
    this.audit = audit;
    this.schema = Schemas.flatten(audit.schema());

    final List<String> errors = getErrors();
    if (errors != null && errors.size() > 0)
      for (final String error : errors)
        logger.warn(error);
  }

  private List<String> getErrors() {
    final List<String> errors = new ArrayList<String>();
    for (final $ddlx_table table : schema._table()) {
      if (!table._abstract$().text()) {
        if (table._constraints(0)._primaryKey(0).isNull()) {
          errors.add("Table `" + table._name$().text() + "` does not have a primary key.");
        }
        else {
          for (final $ddlx_column column : table._column()) {
            if (audit.isPrimary(table, column) && column._null$().text())
              errors.add("Primary key column `" + column._name$().text() + "` on table `" + table._name$().text() + "` is NULL.");
          }
        }
      }
    }

    return errors;
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
      throw new GeneratorExecutionException("Circular table dependency detected: " + tableName);

    tableNames.add(tableName);
    if (table._column() != null) {
      for (final $ddlx_column column : table._column()) {
        final String columnName = column._name$().text();
        violation = checkNameViolation(columnName, strict);
        if (violation != null)
          violations.add(violation);

        final $ddlx_column existing = columnNameToColumn.get(columnName);
        if (existing != null)
          throw new GeneratorExecutionException("Duplicate column definition: " + tableName + "." + columnName);

        columnNameToColumn.put(columnName, column);
      }
    }

    if (violations.size() > 0) {
      if (strict) {
        final StringBuilder builder = new StringBuilder();
        for (final String v : violations)
          builder.append(" ").append(v);

        throw new GeneratorExecutionException(builder.substring(1));
      }

      violations.stream().forEach(v -> logger.warn(v));
    }
  }

  private List<CreateStatement> parseTable(final DBVendor vendor, final $ddlx_table table, final Set<String> tableNames) throws GeneratorExecutionException {
    // Next, register the column names to be referenceable by the @primaryKey element
    final Map<String,$ddlx_column> columnNameToColumn = new HashMap<String,$ddlx_column>();
    registerColumns(tableNames, columnNameToColumn, table, schema);

    final Compiler compiler = Compiler.getCompiler(vendor);
    final List<CreateStatement> statements = new ArrayList<CreateStatement>();
    statements.addAll(compiler.types(table));

    columnCount.put(table._name$().text(), table._column() != null ? table._column().size() : 0);
    final CreateStatement createTable = compiler.createTableIfNotExists(table, columnNameToColumn);

    statements.add(createTable);

    statements.addAll(compiler.triggers(table));
    statements.addAll(compiler.indexes(table));
    return statements;
  }

  public List<Statement> parse(final DBVendor vendor) throws GeneratorExecutionException {
    final Map<String,List<DropStatement>> dropStatements = new HashMap<String,List<DropStatement>>();
    final Map<String,List<CreateStatement>> createTableStatements = new HashMap<String,List<CreateStatement>>();

    final Set<String> skipTables = new HashSet<String>();
    final ListIterator<$ddlx_table> listIterator = schema._table().listIterator(schema._table().size());
    while (listIterator.hasPrevious()) {
      final $ddlx_table table = listIterator.previous();
      if (table._skip$().text()) {
        skipTables.add(table._name$().text());
      }
      else if (!table._abstract$().text()) {
        final List<DropStatement> drops = Compiler.getCompiler(vendor).drops(table);
        dropStatements.put(table._name$().text(), drops);
      }
    }

    final Set<String> tableNames = new HashSet<String>();
    for (final $ddlx_table table : schema._table())
      if (!table._abstract$().text())
        createTableStatements.put(table._name$().text(), parseTable(vendor, table, tableNames));

    final List<Statement> statements = new ArrayList<Statement>();
    final CreateStatement createSchema = Compiler.getCompiler(vendor).createSchemaIfNotExists(audit.schema());
    if (createSchema != null)
      statements.add(createSchema);

    for (final $ddlx_table table : schema._table()) {
      final String tableName = table._name$().text();
      if (!skipTables.contains(tableName)) {
        statements.addAll(0, dropStatements.get(tableName));
        statements.addAll(createTableStatements.get(tableName));
      }
    }

    return statements;
  }
}