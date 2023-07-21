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

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.SortedMap;

import org.jaxdb.jsql.data;

class Relation {
  enum CurOld {
    Cur(""),
    Old("Old");

    private final String str;

    private CurOld(final String str) {
      this.str = str;
    }

    @Override
    public String toString() {
      return str;
    }
  }

  private final String cacheIndexFieldName;
  private final String cacheMethodName;
  final String cacheMapFieldName;
  private final String declarationName;

  private final ColumnModels columns;
  private final TableModel sourceTable;
  final TableModel tableModel;
  final IndexType indexType;
  final String columnName;
  private final String keyClauseNotNullCheck;
  private final String rangeParams;
  private final String keyParams;
  final KeyModels.KeyModel keyModel;

  Relation(final String schemaClassName, final TableModel sourceTable, final TableModel tableModel, final ColumnModels columns, final IndexType indexType, final KeyModels keyModels) {
    this.cacheMethodName = columns.getInstanceNameForCache(tableModel.classCase);
    this.cacheMapFieldName = "_" + cacheMethodName + "Map$";
    this.cacheIndexFieldName = "_" + cacheMethodName + "Index$";
    this.declarationName = schemaClassName + "." + tableModel.classCase;

    this.sourceTable = sourceTable;
    this.tableModel = tableModel;
    this.columns = columns;
    this.indexType = indexType;

    this.columnName = columns.getInstanceNameForKey();

    final StringBuilder keyClauseNotNullCheck = new StringBuilder();
    final StringBuilder keyParams = new StringBuilder();
    final StringBuilder rangeParams = new StringBuilder();

    if (columns.size() > 0) {
      for (final ColumnModel column : columns) { // [S]
        keyClauseNotNullCheck.append("!{1}.this.").append(column.camelCase).append(".isNull{2}() && ");
        keyParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append(", ");
        rangeParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append("From, final ").append(column.rawType).append(' ').append(column.instanceCase).append("To, ");
      }
    }

    keyClauseNotNullCheck.setLength(keyClauseNotNullCheck.length() - 4);
    this.keyClauseNotNullCheck = keyClauseNotNullCheck.toString();

    keyParams.setLength(keyParams.length() - 2);
    this.keyParams = keyParams.toString();

    rangeParams.setLength(rangeParams.length() - 2);
    this.rangeParams = rangeParams.toString();

    keyModel = keyModels.add(this instanceof ForeignRelation, tableModel.singletonInstanceName, columnName, tableModel.classCase, columns, indexType);
  }

  boolean isDeclaredOnSourceTable() {
    return sourceTable == tableModel;
  }

  private String writeGetRangeMethod(final String returnType) {
    if (!indexType.isBTree())
      return "";

    final String rangeArgs = keyModel.keyArgsRange();
    return
      "\n    public " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheMethodName + "_CACHED(" + rangeParams + ") {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".subMap(" + rangeArgs + ");" +
      "\n    }\n" +
      "\n    public " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheMethodName + "_SELECT(" + rangeParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".select(" + rangeArgs + ");" +
      "\n    }\n";
  }

  final String keyClause(final String replace1, final CurOld replace2, final boolean addSelfRef, final HashSet<String> declared, final String comment) {
    return keyModel.keyRefArgsInternal(tableModel.singletonInstanceName, columnName, tableModel.classCase, replace1, replace2, addSelfRef, declared, comment);
  }

  final void keyClauseColumnAssignments(final LinkedHashSet<String> keyClauseColumnAssignments) {
    if (columns.size() == 0)
      return;

    final StringBuilder keyClauseColumnAssignment = new StringBuilder();
    final boolean isPrimary = columns.equals(tableModel.primaryKey);
    if (isPrimary) {
      keyClauseColumnAssignment.append(tableModel.singletonInstanceName).append("._primary$");
      keyClauseColumnAssignments.add(cacheIndexFieldName + " = " + keyClauseColumnAssignment);
    }
    else {
      for (final ColumnModel column : columns) // [S]
        keyClauseColumnAssignment.append(tableModel.singletonInstanceName).append("._column$[").append(column.index).append("], ");

      keyClauseColumnAssignment.setLength(keyClauseColumnAssignment.length() - 2);
      keyClauseColumnAssignments.add(cacheIndexFieldName + " = new " + data.Column.class.getCanonicalName() + "<?>[] {" + keyClauseColumnAssignment + "}");
    }
  }

  final String writeCacheDeclare(final LinkedHashSet<String> keyClauseColumnAssignments, final HashSet<String> declared) {
    keyClauseColumnAssignments(keyClauseColumnAssignments);
    final String keyArgs = keyModel.keyArgsExternal(declared);
    if (keyArgs == null)
      return null;

    final String returnType = indexType.isUnique ? declarationName : indexType.getInterfaceClass(declarationName);
    return
      "\n    private " + data.Column.class.getCanonicalName() + "<?>[] " + cacheIndexFieldName + ";" +
      "\n    " + indexType.getConcreteClass(declarationName) + " " + cacheMapFieldName + ";\n" +
      "\n    public " + returnType + " " + cacheMethodName + "_CACHED(" + keyParams + ") {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".get(" + keyArgs + ");" +
      "\n    }\n" +
      "\n    public " + returnType + " " + cacheMethodName + "_SELECT(" + keyParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".select(" + keyArgs + ");" +
      "\n    }\n" +
      writeGetRangeMethod(returnType) +
      "\n    public " + indexType.getInterfaceClass(returnType) + " " + cacheMethodName + "_CACHED() {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ";" +
      "\n    }\n" +
      "\n    public " + indexType.getInterfaceClass(returnType) + " " + cacheMethodName + "_SELECT() throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".selectAll();" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ";" +
      "\n    }";
  }

  final String writeCacheInit() {
    return cacheMapFieldName + " = new " + indexType.getConcreteClass(null) + "<>(this);";
  }

  final String writeNullCheckClause(final String classSimpleName, final CurOld curOld) {
    return "if (" + keyClauseNotNullCheck.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ") ";
  }

  String writeCacheInsert(final String classSimpleName, final CurOld curOld, final boolean addSelfRef, final HashSet<String> declared, final String comment) {
    final String keyClause = keyClause(classSimpleName, curOld, addSelfRef, declared, comment);
    if (keyClause == null)
      return null;

    final String method = indexType.isUnique ? "put$" : "add$";
    return writeNullCheckClause(classSimpleName, curOld) + tableModel.singletonInstanceName + "." + cacheMapFieldName + "." + method + "(" + keyClause + ", " + classSimpleName + ".this);";
  }

  final String writeCacheSelectAll() {
    return tableModel.singletonInstanceName + "." + cacheMapFieldName + ".addKey(" + data.Key.class.getCanonicalName() + ".ALL);";
  }

  final String writeOnChangeClearCache(final String classSimpleName, final CurOld curOld, final boolean addSelfRef, final HashSet<String> declared, final String comment) {
    final String keyClause = keyClause(classSimpleName, curOld, addSelfRef, declared, comment);
    if (keyClause == null)
      return null;

    return writeNullCheckClause(classSimpleName, curOld) + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".remove$" + curOld + "(" + keyClause + (indexType.isUnique ? "" : ", " + classSimpleName + ".this") + ");";
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Relation))
      return false;

    final Relation that = (Relation)obj;
    return tableModel.equals(that.tableModel) && columns.equals(that.columns);
  }

  @Override
  public int hashCode() {
    return tableModel.hashCode() ^ columns.hashCode();
  }

  @Override
  public String toString() {
    return "Relation: " + declarationName + " " + columnName + ":" + indexType;
  }
}