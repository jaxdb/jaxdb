/* Copyright (c) 2022 JAX-DB
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

package org.jaxdb.jsql.generator;

import static org.jaxdb.jsql.generator.GeneratorUtil.*;
import static org.libj.lang.Assertions.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.stream.Collectors;

import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.CacheMap;
import org.jaxdb.jsql.GenerateOn;
import org.jaxdb.jsql.Schema;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.generator.IndexType.UNDEFINED;
import org.jaxdb.jsql.generator.Relation.CurOld;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Bigint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Binary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Blob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Boolean;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Char;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Clob;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Columns;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Constraints.PrimaryKey;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Date;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Datetime;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Decimal;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Double;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Float;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyComposite.Column;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$ForeignKeyUnary;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Indexes;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Indexes.Index;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Int;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Integer;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Named;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Schema.Table;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Smallint;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Time;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Tinyint;
import org.jaxdb.www.jsql_0_5.xLygluGCXAA.KeyForUpdate;
import org.jaxsb.runtime.BindingList;
import org.libj.lang.Identifiers;
import org.libj.lang.Strings;
import org.libj.util.MultiLinkedHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3.www._2001.XMLSchema.yAA;

class TableMeta {
  private static final Logger logger = LoggerFactory.getLogger(TableMeta.class);
  private final Set<String> primaryKeyColumnNames;
  private final IndexType primaryKeyIndexType;
  private final Set<String> keyForUpdateColumnNames;

  private final LinkedHashSet<Columns> uniques;
  private final LinkedHashSet<Columns> indexes;
  final Columns primaryKey;
  private final ForeignKeys foreignKeys;

  private final LinkedHashMap<Columns,IndexType> columnsToIndexType = new LinkedHashMap<Columns,IndexType>() {
    @Override
    public IndexType put(final Columns key, IndexType value) {
      final IndexType existing = super.get(key);
      if (existing != null)
        value = existing.merge(value);

      return super.put(key, value);
    }
  };
  private final MultiLinkedHashMap<Columns,Relation,Relations<Relation>> columnsToRelations = new MultiLinkedHashMap<>(Relations::new);

  final SchemaManifest schemaManifest;
  final ArrayList<TableMeta> ancestors = new ArrayList<>();
  final boolean isAbstract;
  final TableMeta superTable;
  final String tableName;
  final String classCase;
  final Table table;
  private final ColumnMeta[] columns;
  private final LinkedHashMap<String,ColumnMeta> columnNameToColumnMeta;

  private int totalAutoCount = 0;
  private int totalAutoOffset = 0;

  private final String classSimpleName;
  final String className;
  final String singletonInstanceName;

  TableMeta(final Table table, final SchemaManifest schemaManifest) throws GeneratorExecutionException {
    this.isAbstract = table.getAbstract$() != null && table.getAbstract$().text();
    if (table.getExtends$() == null) {
      this.superTable = null;
      this.uniques = new LinkedHashSet<>(1);
      this.indexes = new LinkedHashSet<>(1);
      this.foreignKeys = new ForeignKeys(1);
    }
    else {
      this.superTable = assertNotNull(schemaManifest.tableNameToTableMeta.get(table.getExtends$().text()));
      if (!superTable.isAbstract)
        throw new GeneratorExecutionException("Cannot extend non-abstract table");

      this.uniques = new LinkedHashSet<>(superTable.uniques);
      this.indexes = new LinkedHashSet<>(superTable.indexes);
      this.foreignKeys = new ForeignKeys(1);

      this.columnsToIndexType.putAll(superTable.columnsToIndexType);
      this.columnsToRelations.putAll(superTable.columnsToRelations);
    }

    this.table = table;
    this.schemaManifest = schemaManifest;
    this.tableName = table.getName$().text();
    this.classCase = Identifiers.toClassCase(tableName, '$');

    this.classSimpleName = Identifiers.toClassCase(tableName, '$');
    this.className = schemaManifest.schemaClassName + "." + classSimpleName;
    this.singletonInstanceName = classSimpleName + "$";

    final $Constraints constraints = table.getConstraints();
    final PrimaryKey primaryKey;
    if (constraints != null && (primaryKey = constraints.getPrimaryKey()) != null) {
      primaryKeyIndexType = IndexType.of(primaryKey.getUsing$(), true);
      final BindingList<? extends $Named> primaryKeyColumns = primaryKey.getColumn();
      final int noColumns = primaryKeyColumns.size();
      primaryKeyColumnNames = new LinkedHashSet<>(noColumns);
      for (int i = 0; i < noColumns; ++i) // [RA]
        primaryKeyColumnNames.add(assertNotNull(primaryKeyColumns.get(i).getName$().text()));
    }
    else {
      primaryKeyIndexType = null;
      if (superTable != null) {
        primaryKeyColumnNames = superTable.primaryKeyColumnNames;
      }
      else {
        primaryKeyColumnNames = Collections.EMPTY_SET;
      }
    }

    final KeyForUpdate keyForUpdate = table.getJsqlKeyForUpdate();
    if (keyForUpdate != null) {
      final BindingList<? extends $Named> columns = keyForUpdate.getColumn();
      final int noColumns = columns.size();
      this.keyForUpdateColumnNames = new LinkedHashSet<>(noColumns);
      for (int i = 0; i < noColumns; ++i) // [RA]
        keyForUpdateColumnNames.add(columns.get(i).getName$().text());
    }
    else if (superTable != null) {
      keyForUpdateColumnNames = superTable.keyForUpdateColumnNames;
    }
    else {
      keyForUpdateColumnNames = Collections.EMPTY_SET;
    }

    this.columns = getColumnMetas(this, primaryKeyColumnNames, 0);

    this.columnNameToColumnMeta = new LinkedHashMap<>(columns.length);
    for (final ColumnMeta column : columns) // [A]
      columnNameToColumnMeta.put(column.name, column);

    if (constraints != null) {
      final BindingList<$Columns> uniqueColumns = constraints.getUnique();
      if (uniqueColumns != null) {
        for (int i = 0, i$ = uniqueColumns.size(); i < i$; ++i) { // [RA]
          final BindingList<? extends $Named> columns = uniqueColumns.get(i).getColumn();
          final int noColumns = columns.size();
          final Columns uniqueColumnMetas = new Columns(this, noColumns);
          for (int j = 0; j < noColumns; ++j) // [RA]
            uniqueColumnMetas.add(assertNotNull(columnNameToColumnMeta.get(columns.get(j).getName$().text())));

          uniques.add(uniqueColumnMetas);
          columnsToIndexType.put(uniqueColumnMetas, IndexType.of((String)null, true));
        }
      }

      final BindingList<$ForeignKeyComposite> foreignKeyComposites = constraints.getForeignKey();
      if (foreignKeyComposites != null) {
        for (int i = 0, i$ = foreignKeyComposites.size(); i < i$; ++i) { // [RA]
          final $ForeignKeyComposite foreignKeyComposite = foreignKeyComposites.get(i);
          final TableMeta referenceTable = schemaManifest.tableNameToTableMeta.get(foreignKeyComposite.getReferences$().text());
          final Columns columns = new Columns(this, 2);
          final Columns referenceColumns = new Columns(this, 2);
          final BindingList<Column> foreignKeyColumns = foreignKeyComposite.getColumn();
          final int j$ = foreignKeyColumns.size();
          for (int j = 0; j < j$; ++j) { // [RA]
            final $ForeignKeyComposite.Column foreignKeyColumn = foreignKeyColumns.get(j);
            columns.add(assertNotNull(columnNameToColumnMeta.get(foreignKeyColumn.getName$().text())));
            referenceColumns.add(assertNotNull(referenceTable.columnNameToColumnMeta.get(foreignKeyColumn.getReferences$().text())));
          }

          foreignKeys.add(new ForeignKey(this, columns, referenceTable, referenceColumns));
        }
      }
    }

    if (superTable != null && superTable.primaryKey.size() > 0) {
      this.primaryKey = superTable.primaryKey;
    }
    else {
      final int noPrimaryColumns = primaryKeyColumnNames.size();
      if (noPrimaryColumns > 0) {
        this.primaryKey = new Columns(this, noPrimaryColumns);
        for (final String primaryKeyColumnName : primaryKeyColumnNames) // [S]
          this.primaryKey.add(assertNotNull(columnNameToColumnMeta.get(primaryKeyColumnName)));

        columnsToIndexType.put(this.primaryKey, primaryKeyIndexType);
      }
      else {
        this.primaryKey = Columns.EMPTY_SET;
      }
    }

    // FIXME: Should <index> be CACHED?
    final $Indexes indexes = table.getIndexes();
    if (indexes != null) {
      final BindingList<Index> indexColumns = indexes.getIndex();
      if (indexColumns != null) {
        for (int i = 0, i$ = indexColumns.size(); i < i$; ++i) { // [RA]
          final $Indexes.Index indexColumn = indexColumns.get(i);
          final BindingList<? extends $Named> columns = indexColumn.getColumn();
          final int noColumns = columns.size();
          final Columns indexColumnMetas = new Columns(this, 1);
          for (int j = 0; j < noColumns; ++j) // [RA]
            indexColumnMetas.add(assertNotNull(columnNameToColumnMeta.get(columns.get(j).getName$().text())));

          final boolean isUnique = indexColumn.getUnique$() != null && indexColumn.getUnique$().text();
          this.columnsToIndexType.put(indexColumnMetas, IndexType.of(indexColumn.getType$(), isUnique));
          this.indexes.add(indexColumnMetas);
          if (isUnique)
            this.uniques.add(indexColumnMetas);
        }
      }
    }
  }

  void init() throws GeneratorExecutionException {
    for (final ColumnMeta column : columns) { // [A]
      final $Column.Index index = column.column.getIndex();
      if (index != null) {
        final Columns indexColumnMetas = new Columns(column.tableMeta, column);
        this.indexes.add(indexColumnMetas);
        columnsToIndexType.put(indexColumnMetas, IndexType.of(index.getType$(), index.getUnique$().text()));
        if (index.getUnique$() != null && index.getUnique$().text())
          uniques.add(indexColumnMetas);
      }

      final $ForeignKeyUnary foreignKey = column.column.getForeignKey();
      if (foreignKey != null) {
        final TableMeta referenceTable = assertNotNull(schemaManifest.tableNameToTableMeta.get(foreignKey.getReferences$().text()));
        final ColumnMeta referenceColumn = assertNotNull(referenceTable.columnNameToColumnMeta.get(foreignKey.getColumn$().text()));
        foreignKeys.add(new ForeignKey(column.tableMeta, new Columns(column.tableMeta, column), referenceTable, new Columns(referenceColumn.tableMeta, referenceColumn)));
      }
    }

    if (columnsToIndexType.size() > 0) {
      for (final Map.Entry<Columns,IndexType> entry : columnsToIndexType.entrySet()) { // [S]
        if (entry.getValue() instanceof UNDEFINED) {
          if (logger.isWarnEnabled()) logger.warn(tableName + " {" + entry.getKey().stream().map(c -> c.name).collect(Collectors.joining(",")) + "} does not have an explicit INDEX definition. Assuming B-TREE.");
          entry.setValue(entry.getValue().isUnique ? IndexType.BTREE_UNIQUE : IndexType.BTREE);
        }
      }
    }

    // First process the constraints, because the UNIQUE spec may collide with INDEX spec that is missing UNIQUE=true.
    if (primaryKey.size() > 0)
      makeIndexes(primaryKey);

    if (uniques.size() > 0)
      for (final Columns unique : uniques) // [S]
        makeIndexes(unique);

    // FIXME: Should <index> be CACHED?
    if (indexes.size() > 0)
      for (final Columns index : indexes) // [S]
        makeIndexes(index);

    if (foreignKeys.size() > 0)
      for (final ForeignKey foreignKey : foreignKeys) // [S]
        makeForeignRelations(foreignKey);

//    print();
  }

  private void print() {
    System.err.println("Table: " + tableName);
    System.err.println("primaryKeyColumnNames: " + primaryKeyColumnNames);
    System.err.println("keyForUpdateColumnNames: " + keyForUpdateColumnNames);

    System.err.println("uniques: " + uniques);
//    System.err.println(indexes);
    System.err.println("primaryKey: " + primaryKey);
    System.err.println("foreignKeys: " + foreignKeys);

    System.err.println("columnNameToIndexType: " + columnsToIndexType);
    System.err.println("columnNameToRelations: " + columnsToRelations);
  }

  StringBuilder getClassNameOfEnum(final $Enum column) {
    final StringBuilder out = new StringBuilder();
    if (column.getTemplate$() != null && column.getValues$() == null)
      return out.append(schemaManifest.schemaClassName).append('.').append(Identifiers.toClassCase(column.getTemplate$().text(), '$'));

    return out.append(classCase).append('.').append(Identifiers.toClassCase(column.getName$().text(), '$'));
  }

  private ColumnMeta[] getColumnMetas(final TableMeta tableMeta, final Set<String> primaryKeyColumnNames, final int depth) throws GeneratorExecutionException {
    final Table table = tableMeta.table;
    final List<$Column> columns = table.getColumn();
    final int size = columns == null ? 0 : columns.size();
    final ColumnMeta[] columnMetas = tableMeta.superTable == null ? new ColumnMeta[depth + size] : getColumnMetas(tableMeta.superTable, primaryKeyColumnNames, depth + size);
    if (columns != null) {
      final boolean isSuperTable = depth != 0;

      for (int c = 1; c <= size; ++c) { // [RA]
        final int i = size - c;
        final $Column column = columns.get(i);
        final int index = columnMetas.length - depth - c;
        columnMetas[index] = getColumnMeta(index, tableMeta, column, primaryKeyColumnNames);
        if (org.jaxdb.ddlx.Generator.isAuto(column)) {
          ++totalAutoCount;
          if (isSuperTable)
            ++totalAutoOffset;
        }
      }
    }

    return columnMetas;
  }

  private static ColumnMeta getColumnMeta(final int index, final TableMeta tableMeta, final $Column column, final Set<String> primaryKeyColumnNames) throws GeneratorExecutionException {
    final String columnName = column.getName$().text();
    final Class<?> cls = column.getClass().getSuperclass();
    GenerateOn<?> generateOnInsert = null;
    GenerateOn<?> generateOnUpdate = null;

    final boolean isPrimary = primaryKeyColumnNames.contains(columnName);
    final boolean isKeyForUpdate = tableMeta.keyForUpdateColumnNames.contains(columnName);
    final Object[] commonParams = {THIS, MUTABLE, "\"" + column.getName$().text() + "\"", PRIMARY_KEY, KEY_FOR_UPDATE, COMMIT_UPDATE, isNull(column)};
    if (column instanceof $Char) {
      final $Char col = ($Char)column;
      if (col.getSqlxGenerateOnInsert$() != null) {
        if ($Char.GenerateOnInsert$.UUID.text().equals(col.getSqlxGenerateOnInsert$().text())) {
          if (col.getLength$().text() != null && col.getLength$().text() < 32)
            throw new GeneratorExecutionException("CHAR(" + col.getLength$().text() + ") requires minimum precision of 32 for UUID");

          generateOnInsert = GenerateOn.UUID;
        }
        else {
          throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + col.getSqlxGenerateOnInsert$().text());
        }
      }

      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.CHAR.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, col.getLength$() == null ? null : col.getLength$().text(), isVarying(col.getVarying$()));
    }

    if (column instanceof $Clob) {
      final $Clob col = ($Clob)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.CLOB.class, commonParams, null, generateOnInsert, generateOnUpdate, col.getLength$() == null ? null : col.getLength$().text());
    }

    if (column instanceof $Binary) {
      final $Binary col = ($Binary)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.BINARY.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, col.getLength$() == null ? null : col.getLength$().text(), isVarying(col.getVarying$()));
    }

    if (column instanceof $Blob) {
      final $Blob col = ($Blob)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.BLOB.class, commonParams, null, generateOnInsert, generateOnUpdate, col.getLength$() == null ? null : col.getLength$().text());
    }

    if (column instanceof $Integer) {
      final $Integer integerColumn = ($Integer)column;
      if (integerColumn.getGenerateOnInsert$() != null) {
        if ($Integer.GenerateOnInsert$.AUTO_5FINCREMENT.text().equals(integerColumn.getGenerateOnInsert$().text())) {
          generateOnInsert = GenerateOn.AUTO_GENERATED;
        }
        else {
          throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + integerColumn.getGenerateOnInsert$().text());
        }
      }

      if (column instanceof $Tinyint) {
        final $Tinyint integer = ($Tinyint)column;
        if (integer.getSqlxGenerateOnUpdate$() != null) {
          if ($Tinyint.GenerateOnUpdate$.INCREMENT.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (isPrimary)
              throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

            generateOnUpdate = GenerateOn.INCREMENT;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + integer.getSqlxGenerateOnUpdate$().text());
          }
        }

        return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.TINYINT.class, commonParams, integer.getDefault$() == null ? null : integer.getDefault$().text(), generateOnInsert, generateOnUpdate, integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : integer.getMin$().text(), integer.getMax$() == null ? null : integer.getMax$().text());
      }

      if (column instanceof $Smallint) {
        final $Smallint integer = ($Smallint)column;
        if (integer.getSqlxGenerateOnUpdate$() != null) {
          if ($Smallint.GenerateOnUpdate$.INCREMENT.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (isPrimary)
              throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

            generateOnUpdate = GenerateOn.INCREMENT;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + integer.getSqlxGenerateOnUpdate$().text());
          }
        }

        return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.SMALLINT.class, commonParams, integer.getDefault$() == null ? null : integer.getDefault$().text(), generateOnInsert, generateOnUpdate, integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : integer.getMin$().text(), integer.getMax$() == null ? null : integer.getMax$().text());
      }

      if (column instanceof $Int) {
        final $Int integer = ($Int)column;
        if (integer.getSqlxGenerateOnInsert$() != null) {
          if (generateOnInsert != null)
            throw new GeneratorExecutionException("ddlx:generateOnInsert and sqlx:generateOnInsert are mutually exclusive");

          if ($Int.GenerateOnInsert$.EPOCH_5FMINUTES.text().equals(integer.getSqlxGenerateOnInsert$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 8)
              throw new GeneratorExecutionException("INT(" + integer.getPrecision$().text() + ") requires minimum precision of 8 for EPOCH_MINUTES");

            generateOnInsert = GenerateOn.EPOCH_MINUTES;
          }
          else if ($Int.GenerateOnInsert$.EPOCH_5FSECONDS.text().equals(integer.getSqlxGenerateOnInsert$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 10)
              throw new GeneratorExecutionException("INT(" + integer.getPrecision$().text() + ") requires minimum precision of 10 for EPOCH_SECONDS");

            generateOnInsert = GenerateOn.EPOCH_SECONDS;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + integer.getSqlxGenerateOnInsert$().text());
          }
        }

        if (integer.getSqlxGenerateOnUpdate$() != null) {
          if (isPrimary)
            throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

          if ($Int.GenerateOnUpdate$.INCREMENT.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            generateOnUpdate = GenerateOn.INCREMENT;
          }
          else if ($Int.GenerateOnUpdate$.EPOCH_5FMINUTES.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 8)
              throw new GeneratorExecutionException("INT(" + integer.getPrecision$().text() + ") requires minimum precision of 8 for EPOCH_MINUTES");

            generateOnUpdate = GenerateOn.EPOCH_MINUTES;
          }
          else if ($Int.GenerateOnUpdate$.EPOCH_5FSECONDS.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 10)
              throw new GeneratorExecutionException("INT(" + integer.getPrecision$().text() + ") requires minimum precision of 10 for EPOCH_SECONDS");

            generateOnUpdate = GenerateOn.EPOCH_SECONDS;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + integer.getSqlxGenerateOnUpdate$().text());
          }
        }

        return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.INT.class, commonParams, integer.getDefault$() == null ? null : integer.getDefault$().text(), generateOnInsert, generateOnUpdate, integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : integer.getMin$().text(), integer.getMax$() == null ? null : integer.getMax$().text());
      }

      if (column instanceof $Bigint) {
        final $Bigint integer = ($Bigint)column;
        if (integer.getSqlxGenerateOnInsert$() != null) {
          if (generateOnInsert != null)
            throw new GeneratorExecutionException("ddlx:generateOnInsert and sqlx:generateOnInsert are mutually exclusive");

          if ($Bigint.GenerateOnInsert$.EPOCH_5FMINUTES.text().equals(integer.getSqlxGenerateOnInsert$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 8)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 8 for EPOCH_MINUTES");

            generateOnInsert = GenerateOn.EPOCH_MINUTES;
          }
          else if ($Bigint.GenerateOnInsert$.EPOCH_5FSECONDS.text().equals(integer.getSqlxGenerateOnInsert$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 10)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 10 for EPOCH_SECONDS");

            generateOnInsert = GenerateOn.EPOCH_SECONDS;
          }
          else if ($Bigint.GenerateOnInsert$.EPOCH_5FMILLIS.text().equals(integer.getSqlxGenerateOnInsert$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 13)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 13 for EPOCH_MILLIS");

            generateOnInsert = GenerateOn.EPOCH_MILLIS;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + integer.getSqlxGenerateOnInsert$().text());
          }
        }

        if (integer.getSqlxGenerateOnUpdate$() != null) {
          if (isPrimary)
            throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

          if ($Bigint.GenerateOnUpdate$.INCREMENT.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            generateOnUpdate = GenerateOn.INCREMENT;
          }
          else if ($Bigint.GenerateOnUpdate$.EPOCH_5FMINUTES.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 8)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 8 for EPOCH_MINUTES");

            generateOnUpdate = GenerateOn.EPOCH_MINUTES;
          }
          else if ($Bigint.GenerateOnUpdate$.EPOCH_5FSECONDS.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 10)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 10 for EPOCH_SECONDS");

            generateOnUpdate = GenerateOn.EPOCH_SECONDS;
          }
          else if ($Bigint.GenerateOnUpdate$.EPOCH_5FMILLIS.text().equals(integer.getSqlxGenerateOnUpdate$().text())) {
            if (integer.getPrecision$().text() != null && integer.getPrecision$().text() < 13)
              throw new GeneratorExecutionException("BIGINT(" + integer.getPrecision$().text() + ") requires minimum precision of 13 for EPOCH_MILLIS");

            generateOnUpdate = GenerateOn.EPOCH_MILLIS;
          }
          else {
            throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + integer.getSqlxGenerateOnUpdate$().text());
          }
        }

        return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.BIGINT.class, commonParams, integer.getDefault$() == null ? null : integer.getDefault$().text(), generateOnInsert, generateOnUpdate, integer.getPrecision$() == null ? null : integer.getPrecision$().text().intValue(), integer.getMin$() == null ? null : integer.getMin$().text(), integer.getMax$() == null ? null : integer.getMax$().text());
      }
    }

    if (column instanceof $Float) {
      final $Float col = ($Float)column;
      final Number min = col.getMin$() != null ? col.getMin$().text() : null;
      final Number max = col.getMax$() != null ? col.getMax$().text() : null;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.FLOAT.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, min, max);
    }

    if (column instanceof $Double) {
      final $Double col = ($Double)column;
      final Number min = col.getMin$() != null ? col.getMin$().text() : null;
      final Number max = col.getMax$() != null ? col.getMax$().text() : null;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.DOUBLE.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, min, max);
    }

    if (column instanceof $Decimal) {
      final $Decimal col = ($Decimal)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.DECIMAL.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, col.getPrecision$() == null ? null : col.getPrecision$().text().intValue(), col.getScale$() == null ? 0 : col.getScale$().text().intValue(), col.getMin$() == null ? null : col.getMin$().text(), col.getMax$() == null ? null : col.getMax$().text());
    }

    if (column instanceof $Date) {
      final $Date col = ($Date)column;
      if (col.getSqlxGenerateOnInsert$() != null) {
        if ($Date.GenerateOnInsert$.TIMESTAMP.text().equals(col.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;
        else
          throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + col.getSqlxGenerateOnInsert$().text());
      }

      if (col.getSqlxGenerateOnUpdate$() != null) {
        if ($Date.GenerateOnUpdate$.TIMESTAMP.text().equals(col.getSqlxGenerateOnUpdate$().text())) {
          if (isPrimary)
            throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

          generateOnUpdate = GenerateOn.TIMESTAMP;
        }
        else {
          throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + col.getSqlxGenerateOnUpdate$().text());
        }
      }

      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.DATE.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate);
    }

    if (column instanceof $Time) {
      final $Time col = ($Time)column;
      if (col.getSqlxGenerateOnInsert$() != null) {
        if ($Time.GenerateOnInsert$.TIMESTAMP.text().equals(col.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;
        else
          throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + col.getSqlxGenerateOnInsert$().text());
      }

      if (col.getSqlxGenerateOnUpdate$() != null) {
        if ($Time.GenerateOnUpdate$.TIMESTAMP.text().equals(col.getSqlxGenerateOnUpdate$().text())) {
          if (isPrimary)
            throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

          generateOnUpdate = GenerateOn.TIMESTAMP;
        }
        else {
          throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + col.getSqlxGenerateOnUpdate$().text());
        }
      }

      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.TIME.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, col.getPrecision$() == null ? null : col.getPrecision$().text().intValue());
    }

    if (column instanceof $Datetime) {
      final $Datetime col = ($Datetime)column;
      if (col.getSqlxGenerateOnInsert$() != null) {
        if ($Datetime.GenerateOnInsert$.TIMESTAMP.text().equals(col.getSqlxGenerateOnInsert$().text()))
          generateOnInsert = GenerateOn.TIMESTAMP;
        else
          throw new GeneratorExecutionException("Unknown generateOnInsert specification: " + col.getSqlxGenerateOnInsert$().text());
      }

      if (col.getSqlxGenerateOnUpdate$() != null) {
        if ($Datetime.GenerateOnUpdate$.TIMESTAMP.text().equals(col.getSqlxGenerateOnUpdate$().text())) {
          if (isPrimary)
            throw new GeneratorExecutionException("Primary column \"" + tableMeta.table.getName$().text() + "." + column.getName$().text() + "\" cannot specify generateOnUpdate");

          generateOnUpdate = GenerateOn.TIMESTAMP;
        }
        else {
          throw new GeneratorExecutionException("Unknown generateOnUpdate specification: " + col.getSqlxGenerateOnUpdate$().text());
        }
      }

      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.DATETIME.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate, col.getPrecision$() == null ? null : col.getPrecision$().text().intValue());
    }

    if (column instanceof $Boolean) {
      final $Boolean col = ($Boolean)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.BOOLEAN.class, commonParams, col.getDefault$() == null ? null : col.getDefault$().text(), generateOnInsert, generateOnUpdate);
    }

    if (column instanceof $Enum) {
      final $Enum col = ($Enum)column;
      return new ColumnMeta(tableMeta, index, column, isPrimary, isKeyForUpdate, data.ENUM.class, commonParams, col.getDefault$() == null ? null : tableMeta.getClassNameOfEnum(col).append('.').append(Generator.enumStringToEnum(col.getDefault$().text())), generateOnInsert, generateOnUpdate);
    }

    throw new IllegalArgumentException("Unknown class: " + cls);
  }

  private boolean isPrimaryKey(final Columns columns) {
    return primaryKey.equals(columns);
  }

  private boolean isUnique(final Columns columns) {
    return uniques.contains(columns);
  }

  private void makeForeignRelations(final ForeignKey foreignKey) throws GeneratorExecutionException {
    for (int i = 0, i$ = ancestors.size(); i < i$; ++i) { // [RA]
      final TableMeta ancestor = ancestors.get(i);
      final IndexType indexTypeForeign = foreignKey.referenceTable.columnsToIndexType.get(foreignKey.referenceColumns);
      if (indexTypeForeign == null)
        throw new GeneratorExecutionException(tableName + ":{" + foreignKey.columns.stream().map(c -> c.name).collect(Collectors.joining(",")) + "} is referencing privateKey " + foreignKey.referenceTable.tableName + ":{" + foreignKey.referenceColumns.stream().map(c -> c.name).collect(Collectors.joining(",")) + "} which does not have an PRIMARY KEY, UNIQUE, or INDEX definition.");

      final IndexType indexType = columnsToIndexType.getOrDefault(foreignKey.columns, indexTypeForeign.getNonUnique());
      final ForeignRelation forward = makeForeignRelation(foreignKey.table, ancestor, foreignKey.columns, foreignKey.referenceTable, foreignKey.referenceColumns, indexType, indexTypeForeign);
      if (forward.referenceTable.isAbstract)
        continue;

      final ForeignRelation reverse = makeForeignRelation(foreignKey.table, foreignKey.referenceTable, foreignKey.referenceColumns, ancestor, foreignKey.columns, indexTypeForeign, indexType);
      forward.reverses.add(reverse);
      reverse.reverses.add(forward);
      foreignKey.referenceTable.columnsToRelations.getOrNew(foreignKey.referenceColumns).add(reverse);
      ancestor.columnsToRelations.getOrNew(foreignKey.columns).add(forward);
    }
  }

  boolean isRelated(TableMeta thatTable) {
    TableMeta thisTable;
    do {
      thisTable = this;
      do {
        if (thatTable.tableName.equals(thisTable.tableName))
          return true;
      }
      while ((thisTable = thisTable.superTable) != null);
    }
    while ((thatTable = thatTable.superTable) != null);

    return false;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof TableMeta))
      return false;

    final TableMeta that = (TableMeta)obj;
    return tableName.equals(that.tableName);
  }

  @Override
  public int hashCode() {
    return tableName.hashCode();
  }

  @Override
  public String toString() {
    final StringBuilder s = new StringBuilder(tableName).append(": {\n");
    s.append("  \"primaryKey\": ").append(primaryKey).append(",\n");
    s.append("  \"unique\": [");
    if (uniques.size() > 0) {
      for (final Columns unique : uniques) { // [S]
        s.append("\n    [");
        if (unique.size() > 0)
          for (final ColumnMeta columnMeta : unique) // [S]
            s.append(columnMeta.name).append(',');

        s.setCharAt(s.length() - 1, ']');
      }

      s.append("\n  ");
    }

    s.append("]\n");

    s.append("  \"foreignKey\": [");
    if (columnsToRelations.size() > 0) {
      for (final Map.Entry<Columns,Relations<Relation>> entry : columnsToRelations.entrySet()) // [S]
        s.append("\n    ").append(entry.getKey()).append(" -> ").append(entry.getValue());

      s.append("\n  ");
    }

    s.append("]\n");
    return s.append('}').toString();
  }

  private String getConcreteClass(final String declarationName) {
    if (primaryKey.size() == 0)
      return CacheMap.class.getName() + "<" + declarationName + ">";

    return columnsToIndexType.get(primaryKey).getConcreteClass(declarationName);
  }

  String makeTable() {
    final String tableName = table.getName$().text();

    final int noColumnsLocal = table.getColumn() == null ? 0 : table.getColumn().size();
    final int noColumnsTotal = columns.length;

    final StringBuilder out = new StringBuilder();

    out.append("\n  public interface $").append(classSimpleName).append(" {");

    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      final ColumnMeta column = columns[i];
      if (column.column instanceof $Enum) {
        final $Enum enumColumn = ($Enum)column.column;
        if (enumColumn.getTemplate$() == null || enumColumn.getValues$() != null)
          out.append(Generator.declareEnumClass(classCase, enumColumn, 4));
      }
    }

    out.append("\n  }\n");

    final Collection<Relations<Relation>> allRelations = columnsToRelations.values();

    final String ext = superTable == null ? data.Table.class.getCanonicalName() : Identifiers.toClassCase(table.getExtends$().text(), '$');

    final LinkedHashSet<String> keyClauseColumnAssignments = new LinkedHashSet<>();
    final HashSet<String> declared = new HashSet<>();
    if (!isAbstract) {
      out.append("\n  public final ").append(className).append("$ ").append(singletonInstanceName).append(" = new ").append(classSimpleName).append("$();\n");
      out.append("\n  public final class ").append(classSimpleName).append("$ extends ").append(className).append(" {");
      out.append("\n    private ").append(singletonInstanceName).append("() {");
      out.append("\n      super(false, false);");
      out.append("\n    }\n");
      out.append("\n    private boolean cacheSelectEntity;\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    final void setCacheSelectEntity(final boolean cacheSelectEntity) {");
      out.append("\n      this.cacheSelectEntity = cacheSelectEntity;");
      out.append("\n    }\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    final boolean getCacheSelectEntity() {");
      out.append("\n      return cacheSelectEntity;");
      out.append("\n    }\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    final ").append(getConcreteClass(className)).append(" getCache() {");
      out.append("\n      return ").append(primaryKey.size() == 0 ? "null" : "_" + primaryKey.getInstanceNameForCache(classCase) + "Map$").append(';');
      out.append("\n    }\n");

      {
        out.append("\n    // CACHE DECLARE");
        if (allRelations.size() > 0)
          for (final LinkedHashSet<Relation> relations : allRelations) // [C]
            for (final Relation relation : relations) // [S]
              write("\n", relation.writeCacheDeclare(keyClauseColumnAssignments), out, declared);

        if (declared.size() > 0)
          out.append('\n');
      }

      {
        declared.clear();
        out.append("\n    boolean _cacheEnabled$;\n");

        out.append("\n    void _initCache$() {");
        out.append("\n      if (_cacheEnabled$)");
        out.append("\n        return;\n");
        out.append("\n      super._initCache$();");
        out.append("\n      _cacheEnabled$ = true;\n");

        if (allRelations.size() > 0)
          for (final LinkedHashSet<Relation> relations : allRelations) // [C]
            for (final Relation relation : relations) // [S]
              write("\n      ", relation.writeCacheInit(), out, declared);

        if (keyClauseColumnAssignments.size() > 0) {
          out.append('\n');
          for (final String keyClauseColumnAssignment : keyClauseColumnAssignments) // [S]
            out.append("\n      ").append(keyClauseColumnAssignment).append(';');
        }

        out.append("\n    }");
      }

      out.append("\n  };\n");
    }

    out.append(getDoc(table, 1, '\0', '\n', "Table", tableName)); // FIXME: Add "\d foo" -like printout of column info into the table's javadoc
    out.append("\n  public");
    if (isAbstract)
      out.append(" abstract");

    out.append(" class ").append(classSimpleName).append(" extends ").append(ext).append(" implements $").append(classSimpleName);
//    if (!isAbstract)
//      out.append(" implements ").append(type.Table.class.getCanonicalName()).append('<').append(className).append('>');

    out.append(" {");

    final StringBuilder dcl = new StringBuilder();

    if (!isAbstract) {
      {
        out.append("\n    // WRITE DECLARE");
        declared.clear();

        if (allRelations.size() > 0) {
          for (final LinkedHashSet<Relation> relations : allRelations) { // [C]
            for (final Relation relation : relations) { // [S]
              // Only write the declaration if the source of the relation came from this table, or an unrelated table.
              // This avoids sub-tables overriding the same declaration of super-tables.
              if (relation instanceof ForeignRelation) {
                final ForeignRelation foreign = (ForeignRelation)relation;
                if (!foreign.referenceTable.isAbstract)
                  write("\n", foreign.writeDeclaration(classSimpleName), out, declared);
              }
            }
          }
        }

        if (declared.size() > 0)
          out.append('\n');
      }

      {
        declared.clear();

        out.append("\n\n    @").append(Override.class.getName());
        out.append("\n    void _commitSelectAll$() {");
        out.append("\n      if (!").append(singletonInstanceName).append('.').append("_cacheEnabled$)");
        out.append("\n        return;\n");
        out.append("\n      getCache().addKey(").append(data.Key.class.getCanonicalName()).append(".ALL);");
        if (allRelations.size() > 0) {
          for (final LinkedHashSet<Relation> relations : allRelations) { // [C]
            for (final Relation relation : relations) { // [S]
              write("\n      ", relation.writeCacheSelectAll(), out, declared);
            }
          }
        }

        out.append("\n    }\n");

        declared.clear();

        out.append("\n\n    @").append(Override.class.getName());
        out.append("\n    void _commitInsert$() {");
        out.append("\n      if (!").append(singletonInstanceName).append('.').append("_cacheEnabled$)");
        out.append("\n        return;\n");
        // out.append("\n super._commitInsert$();\n");
        if (allRelations.size() > 0) {
          for (final LinkedHashSet<Relation> relations : allRelations) { // [C]
            for (final Relation relation : relations) { // [S]
              write("\n      ", relation.writeCacheInsert(classSimpleName, CurOld.Cur), out, declared);
            }
          }
        }

        out.append("\n    }\n");

        declared.clear();

        out.append("\n    @").append(Override.class.getName());
        out.append("\n    void _commitDelete$() {");
        out.append("\n      if (!").append(singletonInstanceName).append('.').append("_cacheEnabled$)");
        out.append("\n        return;\n");

        // FIXME: Remove the re-addition of all Relation(s) to ArrayList
        final ArrayList<Relation> onChangeRelations = new ArrayList<>(1);
        if (columnsToRelations.size() > 0)
          for (final Map.Entry<Columns,Relations<Relation>> entry : columnsToRelations.entrySet()) // [S]
            onChangeRelations.addAll(entry.getValue());

        if (onChangeRelations.size() > 0) {
          for (int i = 0, i$ = onChangeRelations.size(); i < i$; ++i) { // [RA]
            final Relation onChangeRelation = onChangeRelations.get(i);
            if (onChangeRelation instanceof ForeignRelation) {
              final ForeignRelation relation = (ForeignRelation)onChangeRelation;
              boolean added = false;
              final LinkedHashSet<ForeignRelation> reverses = relation.reverses;
              for (final ForeignRelation reverse : reverses) // [S]
                added |= write("      ", reverse.writeOnChangeReverse(relation.fieldName), out, declared);

              if (added)
                out.append('\n');

              write("\n      ", relation.writeOnChangeForward(), out, declared);
            }
          }

          declared.clear();
          for (int i = 0, i$ = onChangeRelations.size(); i < i$; ++i) { // [RA]
            final Relation onChangeRelation = onChangeRelations.get(i);
            write("\n      ", onChangeRelation.writeOnChangeClearCache(classSimpleName, onChangeRelation.keyClause(), CurOld.Cur), out, declared);
          }
        }
        out.append("\n    }\n");
      }
    }

    dcl.setLength(0);

    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      final ColumnMeta column = columns[i];
      out.append(getDoc(column.column, 2, '\n', '\0', "Column", column.column.getName$().text(), "Primary", column.isPrimary, "KeyForUpdate", column.isKeyForUpdate, "NULL", column.column.getNull$() == null || column.column.getNull$().text())); // FIXME: Add DEFAULT
      out.append("\n    public final ").append(column.declareColumn()).append(";\n");
    }

    if (!isAbstract) {
      out.append("\n    /** Creates a new {@link ").append(className).append("}. */");
      out.append("\n    public ").append(classSimpleName).append("() {");
      out.append("\n      this(true);");
      out.append("\n    }\n");

      // Constructor with primary key columns
      if (primaryKey.size() > 0) {
        final StringBuilder set = new StringBuilder();
        out.append("\n    /** Creates a new {@link ").append(className).append("} with the specified primary key. */");
        out.append("\n    public ").append(classSimpleName).append('(');
        final StringBuilder params = new StringBuilder();
        for (final ColumnMeta columnMeta : columns) { // [A]
          if (columnMeta.isPrimary) {
            params.append("final ").append(columnMeta.rawType).append(' ').append(columnMeta.camelCase).append(", ");
            final String fieldName = Identifiers.toCamelCase(columnMeta.name, '_');
            set.append("      this.").append(fieldName).append(".set(").append(fieldName).append(");\n");
          }
        }

        if (params.length() > 0)
          params.setLength(params.length() - 2);

        out.append(params).append(") {");
        out.append("\n      this();\n");

        if (set.length() > 0) {
          set.setLength(set.length() - 1);
          out.append(set);
        }

        out.append("\n    }\n");
      }

      // Copy constructor
      out.append("\n    /** Creates a new {@link ").append(className).append("} as a copy of the provided {@link ").append(className).append("} instance. */");
      out.append("\n    public ").append(classSimpleName).append("(final ").append(className).append(" copy) {");
      out.append("\n      this(true, copy);");
      out.append("\n    }\n");


      out.append("\n    ").append(classSimpleName).append("(final boolean _mutable$) {");
      out.append("\n      this(_mutable$, false);");
      out.append("\n    }\n");
    }

    final String[] commitUpdates;

    if (isAbstract) {
      commitUpdates = null;
    }
    else {
      commitUpdates = new String[columns.length];
      final StringBuilder ocb = new StringBuilder();
      for (int i = 0; i < columns.length; ++i) { // [A]
        final ColumnMeta columnMeta = columns[i];
          // This section executed onChanged() for each column to clear the foreign Key
  //      Map<String,String> foreignKeyColumns = tableToForeignKeyColumns.get(table);
  //      if (foreignKeyColumns == null) {
  //        tableToForeignKeyColumns.put(table, foreignKeyColumns = new HashMap<>());
  //        for (final ColumnMeta t : columns) { // [?]
  //          if (t.column.getForeignKey() != null) {
  //            final String privateKeyName = "_foreignKey$" + t.instanceCase;
  //            foreignKeyColumns.put(t.name, privateKeyName);
  //          }
  //        }
  //
  //        if (table.getConstraints() != null && table.getConstraints().getForeignKey() != null) {
  //          for (final $ForeignKeyComposite foreignKey : table.getConstraints().getForeignKey()) { // [?]
  //            final StringBuilder foreignKeyName = new StringBuilder();
  //            for (final $ForeignKeyComposite.Column column : foreignKey.getColumn()) { // [?]
  //              final String camelCase = Identifiers.toCamelCase(column.getName$().text(), '_');
  //              foreignKeyName.append(camelCase).append('$');
  //            }
  //
  //            foreignKeyName.setLength(foreignKeyName.length() - 1);
  //            final String privateKeyName = "_foreignKey$" + foreignKeyName;
  //            for (final $ForeignKeyComposite.Column column : foreignKey.getColumn()) { // [?]
  //              foreignKeyColumns.put(column.getName$().text(), privateKeyName);
  //            }
  //          }
  //        }
  //      }

        final ArrayList<Relation> onChangeRelationsForColumn = new ArrayList<>(1);
        if (columnsToRelations.size() > 0)
          for (final Map.Entry<Columns,Relations<Relation>> entry : columnsToRelations.entrySet()) // [S]
            if (entry.getKey().contains(columnMeta))
              onChangeRelationsForColumn.addAll(entry.getValue());

        if (onChangeRelationsForColumn.size() == 0) {
          commitUpdates[i] = null;
          continue;
        }

        final HashSet<String> declared2 = new HashSet<>();

        ocb.append("\n        new ").append(Consumer.class.getName()).append('<').append(className).append(">() {\n          @").append(Override.class.getName());
        ocb.append("\n          public void accept(final ").append(className).append(" self) {");
        ocb.append("\n            if (!").append(singletonInstanceName).append('.').append("_cacheEnabled$)");
        ocb.append("\n              return;\n");
        for (int j = 0, j$ = onChangeRelationsForColumn.size(); j < j$; ++j) { // [RA]
          final Relation onChangeRelation = onChangeRelationsForColumn.get(j);
          if (onChangeRelation instanceof ForeignRelation) {
            final ForeignRelation relation = (ForeignRelation)onChangeRelation;
            boolean added = false;
            final LinkedHashSet<ForeignRelation> reverses = relation.reverses;
            for (final ForeignRelation reverse : reverses) // [S]
              added |= write("\n            ", reverse.writeOnChangeClearCacheForeign(classSimpleName, onChangeRelation.keyClause(), CurOld.Old, CurOld.Cur), ocb, declared2);

            if (added)
              ocb.append('\n');

            write("\n            ", relation.writeOnChangeForward(), ocb, declared2);
          }
        }

        for (int j = 0, j$ = onChangeRelationsForColumn.size(); j < j$; ++j) { // [RA]
          final Relation onChangeRelation = onChangeRelationsForColumn.get(j);
          write("\n            ", onChangeRelation.writeOnChangeClearCache(classSimpleName, onChangeRelation.keyClause(), CurOld.Old), ocb, declared2);
        }

        for (int j = 0, j$ = onChangeRelationsForColumn.size(); j < j$; ++j) { // [RA]
          final Relation onChangeRelation = onChangeRelationsForColumn.get(j);
          write("\n            ", onChangeRelation.writeCacheInsert(classSimpleName, CurOld.Cur), ocb, declared2);
        }

        if (primaryKeyColumnNames.contains(columnMeta.name) && columnsToRelations.size() > 0) {
          for (final Map.Entry<Columns,Relations<Relation>> entry : columnsToRelations.entrySet()) { // [S]
            final LinkedHashSet<Relation> relations = entry.getValue();
            for (final Relation relation : relations) { // [S]
              if (relation instanceof ForeignRelation) {
                final ForeignRelation foreign = (ForeignRelation)relation;

                if (entry.getKey().contains(columnMeta) || relation instanceof ManyToManyRelation) {
                  write("\n            ", foreign.writeOnChangeClearCache(classSimpleName, foreign.keyClause(), CurOld.Old), ocb, declared2);
                  write("\n            ", foreign.writeCacheInsert(classSimpleName, CurOld.Cur), ocb, declared2);
                }

  //                    for (final Foreign reverse : relation.reverses) { // [?]
  //                      if (reverse.referencesColumns.contains(columnMeta))
  //                        write("          ", reverse.writeOnChangeClearCache(classSimpleName, relation.keyClause, CurOld.Old), "\n", ocb, declared);
  //                    }
              }
            }
          }
        }

        ocb.append("\n          }\n        }");

        // FIXME: Wow, what a hack!
        Strings.replace(ocb, classSimpleName + ".this", "self");
        commitUpdates[i] = ocb.toString();
        ocb.setLength(0);
      }
    }

    final StringBuilder init = new StringBuilder();
    newColumnArray(init, noColumnsTotal, primaryKeyColumnNames.isEmpty() && keyForUpdateColumnNames.isEmpty() ? null : i -> {
      if (primaryKeyColumnNames.contains(columns[i].name))
        return data.class.getCanonicalName() + "." + (primaryKeyIndexType instanceof IndexType.HASH ? "HASH" : "BTREE");

      if (keyForUpdateColumnNames.contains(columns[i].name))
        return data.class.getCanonicalName() + ".KEY_FOR_UPDATE";

      return null;
    }).append(", ");
    newColumnArray(init, primaryKey.size()).append(", ");
    newColumnArray(init, keyForUpdateColumnNames.size()).append(", ");
    newColumnArray(init, totalAutoCount);

    final String init0 = init.toString();

    final StringBuilder parameters = new StringBuilder();
    for (int i = 0; i < columns.length; ++i) { // [A]
      final ColumnMeta column = columns[i];
      parameters.append(", final ").append(Consumer.class.getName()).append("<? extends ").append(column.tableMeta.className).append("> ").append(column.instanceCase);
      init.append(", (").append(Consumer.class.getName()).append(")null");
    }

    final StringBuilder arguments = new StringBuilder();
    for (int s = columns.length - noColumnsLocal, i = 0; i < s; ++i) { // [A]
      arguments.append(", ").append(columns[i].instanceCase);
      if (commitUpdates != null && commitUpdates[i] != null)
        arguments.append(" != null ? ").append(columns[i].instanceCase).append(" : ").append(commitUpdates[i]);
    }

    out.append("\n    ").append(classSimpleName).append("(final boolean _mutable$, final boolean _wasSelected$) {");
    out.append("\n      this(_mutable$, _wasSelected$, ").append(init).append(");");
    out.append("\n    }\n");

    out.append("\n    /** Creates a new {@link ").append(className).append("} as a copy of the provided {@link ").append(className).append("} instance. */");
    out.append("\n    ").append(classSimpleName).append("(final boolean _mutable$, final ").append(className).append(" copy) {");
    out.append("\n      this(_mutable$, false, ").append(init0).append(", copy);");
    out.append("\n    }\n\n");

    for (int x = 0; x < 2; ++x) { // [N]
      if (x == 0) {
        out.append("    ").append(classSimpleName).append("(final boolean _mutable$, final boolean _wasSelected$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _column$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _primary$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _keyForUpdate$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _auto$").append(parameters).append(") {\n");
        out.append("      super(_mutable$, _wasSelected$, _column$, _primary$, _keyForUpdate$, _auto$").append(arguments).append(");\n");
      }
      else {
        out.append("    ").append(classSimpleName).append("(final boolean _mutable$, final boolean _wasSelected$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _column$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _primary$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _keyForUpdate$, final ").append(data.Column.class.getCanonicalName()).append("<?>[] _auto$, final ").append(className).append(" copy) {\n");
        out.append("      super(_mutable$, _wasSelected$, _column$, _primary$, _keyForUpdate$, _auto$");
        if (superTable != null)
          out.append(", copy");
        out.append(");\n");
      }

      int primaryIndex = 0;
      int keyForUpdateIndex = 0;
      int autoIndex = totalAutoOffset;
      for (int s = columns.length - noColumnsLocal, i = 0; i < columns.length; ++i) { // [A]
        if (i > s)
          out.append('\n');

        final ColumnMeta columnMeta = columns[i];

        final StringBuilder dec = new StringBuilder();
        if (primaryKeyColumnNames.contains(columnMeta.name))
          dec.append("_primary$[").append(primaryIndex++).append("] = ");

        if (keyForUpdateColumnNames.contains(columnMeta.name))
          dec.append("_keyForUpdate$[").append(keyForUpdateIndex++).append("] = ");

        if (org.jaxdb.ddlx.Generator.isAuto(columnMeta.column))
          dec.append("_auto$[").append(autoIndex++).append("] = ");

        if (i >= s || dec.length() > 0) {
          dec.append("_column$[").append(i).append(']');
          out.append("     ").append(dec);
        }

        if (i < s) {
          if (dec.length() > 0)
            out.append(";\n");
        }
        else {
          out.append(" = this.");

          if (x == 0)
            columnMeta.assignConstructor(out, i, commitUpdates == null ? null : commitUpdates[i]);
          else
            columnMeta.assignCopyConstructor(out);

          out.append(';');
        }
      }

      out.append("\n    }\n\n");
    }

    final StringBuilder buf = new StringBuilder();
    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      final ColumnMeta columnMeta = columns[i];
      if (buf.length() > 0)
        buf.append('\n');

      final String fieldName = Identifiers.toCamelCase(columnMeta.name, '_');
      buf.append("      if (t.").append(fieldName).append(".setByCur != null)\n");
      buf.append("        ").append(fieldName).append(".copy(t.").append(fieldName).append(");\n");
    }

    out.append("    @").append(Override.class.getName()).append('\n');
    out.append("    void _merge$(final ").append(data.Table.class.getCanonicalName()).append(" table) {\n");
    if (superTable != null)
      out.append("      super._merge$(table);\n");

    out.append("      final ").append(className).append(" t = (").append(className).append(")table;\n");
    if (buf.length() > 0)
      out.append(buf);
    out.append("    }\n");

//    out.append("    @").append(Override.class.getName()).append('\n');
//    out.append("    protected ").append(TableCache.class.getName()).append(" getRowCache() {\n");
//    out.append("      return ").append(schemaClassSimpleName).append(".getRowCache();\n");
//    out.append("    }\n\n");

    if (isAbstract) {
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    abstract ").append(className).append(" clone(final boolean _mutable$);\n");
      out.append("\n    public abstract ").append(className).append(" clone();");
    }
    else {
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    ").append(className).append(" clone(final boolean _mutable$) {");
      out.append("\n      return new ").append(className).append("(_mutable$, this);");
      out.append("\n    }\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    public ").append(className).append(" clone() {");
      out.append("\n      return clone(true);");
      out.append("\n    }");
    }

    buf.setLength(0);
    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      final ColumnMeta columnMeta = columns[i];
      buf.append("\n      if (this.").append(columnMeta.instanceCase).append(".isNull() ? !that.").append(columnMeta.instanceCase).append(".isNull() : !").append(columnMeta.getEqualsClause()).append(')');
      buf.append("\n        return false;\n");
    }

    out.append('\n');
    out.append("\n    @").append(Override.class.getName());
    out.append("\n    public boolean equals(final ").append(Object.class.getName()).append(" obj) {");
    out.append("\n      if (obj == this)");
    out.append("\n        return true;\n");
    out.append("\n      if (!(obj instanceof ").append(className).append("))");
    out.append("\n        return false;\n");
    if (superTable != null) {
      out.append("\n      if (!super.equals(obj))");
      out.append("\n        return false;\n");
    }
    if (buf.length() > 0) {
      out.append("\n      final ").append(className).append(" that = (").append(className).append(")obj;");
      out.append(buf);
    }
    out.append("\n      return true;");
    out.append("\n    }\n\n");

    buf.setLength(0);
    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      final ColumnMeta columnMeta = columns[i];
      buf.append("\n      if (!this.").append(columnMeta.instanceCase).append(".isNull())");
      buf.append("\n        hashCode = 31 * hashCode + this.").append(columnMeta.instanceCase).append(".get().hashCode();\n");
    }

    out.append("    @").append(Override.class.getName()).append('\n');
    out.append("    public int hashCode() {\n");
    if (superTable != null)
      out.append("      int hashCode = super.hashCode();");
    else
      out.append("      int hashCode = ").append(tableName.hashCode()).append(';');
    if (buf.length() > 0)
      out.append(buf);
    out.append("\n      return hashCode;");
    out.append("\n    }\n\n");

    buf.setLength(0);
    boolean ifClause = true;
    for (int s = columns.length - noColumnsLocal, i = s; i < columns.length; ++i) { // [A]
      if (ifClause)
        buf.append('\n');
      final ColumnMeta columnMeta = columns[i];
      ifClause = !columnMeta.isPrimary && !columnMeta.isKeyForUpdate;
      if (ifClause)
        buf.append("      if (!wasSetOnly || this.").append(columnMeta.instanceCase).append(".setByCur != null)\n  ");

      buf.append("      this.").append(columnMeta.instanceCase).append(".toJson(s.append(\",\\\"").append(columnMeta.name).append("\\\":\"));\n");
    }

    out.append("    @").append(Override.class.getName()).append('\n');
    out.append("    protected void toString(final boolean wasSetOnly, final ").append(StringBuilder.class.getName()).append(" s) {");
    if (superTable != null)
      out.append("\n      super.toString(wasSetOnly, s);\n");

    if (buf.length() > 0)
      out.append(buf);

    out.append("    }\n");

    if (!isAbstract) {
      declared.clear();

      out.append("\n    @").append(Override.class.getName());
      out.append("\n    public ").append(String.class.getName()).append(" getName() {");
      out.append("\n      return \"").append(tableName).append("\";");
      out.append("\n    }\n");

      out.append("\n    private final ").append(String.class.getName()).append("[] _columnName$ = {");
      for (int i = 0; i < noColumnsTotal; ++i) // [A]
        columns[i].column.text(String.valueOf(i)); // FIXME: Hacking this to record what is the index of each column

      final ArrayList<$Column> sortedColumns = new ArrayList<>();
      for (final ColumnMeta columnMeta : columns) // [A]
        sortedColumns.add(columnMeta.column);
      sortedColumns.sort(Generator.namedComparator);

      for (int i = 0, i$ = sortedColumns.size(); i < i$; ++i) { // [RA]
        if (i > 0)
          out.append(", ");

        out.append('"').append(sortedColumns.get(i).getName$().text()).append('"');
      }
      out.append("};\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    ").append(String.class.getName()).append("[] _columnName$() {");
      out.append("\n      return _columnName$;");
      out.append("\n    }\n");

      out.append("\n    private final byte[] _columnIndex$ = {");
      final int noColumns = sortedColumns.size();
      if (noColumns > 0) {
        for (int i = 0; i < noColumns; ++i) { // [RA]
          if (i > 0)
            out.append(", ");

          out.append(sortedColumns.get(i).text());
        }
      }
      out.append("};\n");
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    byte[] _columnIndex$() {");
      out.append("\n      return _columnIndex$;");
      out.append("\n    }\n");

      out.append("\n    @").append(Override.class.getName());
      out.append("\n    ").append(className).append(" newInstance() {");
      out.append("\n      return new ").append(className).append("(true, true);");
      out.append("\n    }\n");

      out.append("\n    @").append(Override.class.getName());
      out.append("\n    ").append(className).append(" singleton() {");
      out.append("\n      return ").append(singletonInstanceName).append(';');
      out.append("\n    }\n");
    }

    if (superTable == null) {
      out.append("\n    @").append(Override.class.getName());
      out.append("\n    final ").append(Schema.class.getName()).append(" getSchema() {");
      out.append("\n      return ").append(schemaManifest.schemaClassSimpleName).append(".this;");
      out.append("\n    }\n");
    }

    out.append("  }");

    return out.toString();
  }

  void makeIndexes(final Columns columns) {
    for (int i = 0, i$ = ancestors.size(); i < i$; ++i) { // [RA]
      final TableMeta ancestor = ancestors.get(i);
      if (!ancestor.isAbstract) {
        ancestor.columnsToRelations.getOrNew(columns).add(new Relation(schemaManifest.schemaClassName, columns.table, ancestor, columns, assertNotNull(columnsToIndexType.get(columns))));
      }
    }
  }

  private ForeignRelation makeForeignRelation(final TableMeta sourceTable, final TableMeta table, final Columns columns, final TableMeta referenceTable, final Columns referenceColumns, final IndexType indexType, final IndexType indexTypeForeign) {
    final boolean isPrimary = table.isPrimaryKey(columns);
    final boolean isUnique = isPrimary || table.isUnique(columns);
    final boolean isReferencesUnique = referenceTable.isPrimaryKey(referenceColumns) || referenceTable.isUnique(referenceColumns);

    if (isUnique && isReferencesUnique)
      return new OneToOneRelation(schemaManifest.schemaClassName, sourceTable, table, columns, referenceTable, referenceColumns, indexType, indexTypeForeign);

    if (isUnique && !isReferencesUnique)
      return new OneToManyRelation(schemaManifest.schemaClassName, sourceTable, table, columns, referenceTable, referenceColumns, indexType, indexTypeForeign);

    if (!isUnique && isReferencesUnique)
      return new ManyToManyRelation(schemaManifest.schemaClassName, sourceTable, table, columns, referenceTable, referenceColumns, indexType, indexTypeForeign);

    throw new UnsupportedOperationException("Is this even possible?");
  }

  private static boolean isNull(final $Column column) {
    return column.getNull$() == null || column.getNull$().text();
  }

  private static boolean isVarying(final yAA.$Boolean varying) {
    return varying != null && varying.text();
  }

  private static StringBuilder newColumnArray(final StringBuilder out, final int len, final IntFunction<String> predicate) {
    if (predicate == null)
      return newColumnArray(out, len);

    if (len == 0)
      return out.append("empty");

    out.append("new ").append(data.Column.class.getCanonicalName()).append("[] {");
    for (int i = 0; i < len; ++i) { // [A]
      final String marker = predicate.apply(i);
      if (i > 0)
        out.append(", ");

      out.append(marker);
    }

    return out.append('}');
  }

  private static StringBuilder newColumnArray(final StringBuilder out, final int len) {
    return len == 0 ? out.append("empty") : out.append("new ").append(data.Column.class.getCanonicalName()).append('[').append(len).append(']');
  }

  private static boolean write(final String prefix, final String clause, final StringBuilder out, final Set<String> declared) {
    if (clause == null || !declared.add(clause))
      return false;

    out.append(prefix).append(clause);
    return true;
  }
}