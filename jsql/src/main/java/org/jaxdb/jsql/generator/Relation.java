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

  final String cacheIndexFieldName;
  final String cacheMethodName;
  final String cacheMapFieldName;
  final String declarationName;

  final ColumnModels columns;
  final TableModel sourceTable;
  final TableModel tableModel;
  final IndexType indexType;
  final String columnName;
  final String keyClauseNotNullCheck;
  final String keyClauseValues;
  final String rangeParams;
  final String keyParams;
  final String keyArgs;
  final String rangeArgs;

  Relation(final String schemaClassName, final TableModel sourceTable, final TableModel tableModel, final ColumnModels columns, final IndexType indexType) {
    this.cacheMethodName = columns.getInstanceNameForCache(tableModel.classCase);
    this.cacheMapFieldName = "_" + cacheMethodName + "Map$";
    this.cacheIndexFieldName = "_" + cacheMethodName + "Index$";
    this.declarationName = schemaClassName + "." + tableModel.classCase;

    this.sourceTable = sourceTable;
    this.tableModel = tableModel;
    this.columns = columns;
    this.indexType = indexType;

    this.columnName = columns.getInstanceNameForKey();

    final StringBuilder keyClauseValues = new StringBuilder();
    final StringBuilder keyClauseNotNullCheck = new StringBuilder();
    final StringBuilder keyParams = new StringBuilder();
    final StringBuilder rangeParams = new StringBuilder();
    final StringBuilder keyArgs = new StringBuilder();
    final StringBuilder fromArgs = new StringBuilder();
    final StringBuilder toArgs = new StringBuilder();

    if (columns.size() > 0) {
      for (final ColumnModel column : columns) { // [S]
        keyClauseValues.append("{1}.this.").append(column.camelCase).append(".get{2}(), ");
        keyClauseNotNullCheck.append("!{1}.this.").append(column.camelCase).append(".isNull{2}() && ");
        keyParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append(", ");
        rangeParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append("From, final ").append(column.rawType).append(' ').append(column.instanceCase).append("To, ");
        keyArgs.append(column.instanceCase).append(", ");
        fromArgs.append(column.instanceCase).append("From, ");
        toArgs.append(column.instanceCase).append("To, ");
      }
    }

    keyClauseValues.setLength(keyClauseValues.length() - 2);
    this.keyClauseValues = keyClauseValues.toString();

    keyClauseNotNullCheck.setLength(keyClauseNotNullCheck.length() - 4);
    this.keyClauseNotNullCheck = keyClauseNotNullCheck.toString();

    keyParams.setLength(keyParams.length() - 2);
    this.keyParams = keyParams.toString();

    rangeParams.setLength(rangeParams.length() - 2);
    this.rangeParams = rangeParams.toString();

    keyArgs.setLength(keyArgs.length() - 2);
    this.keyArgs = data.Key.class.getCanonicalName() + ".with(" + cacheIndexFieldName + ", " + keyArgs + ")";

    fromArgs.setLength(fromArgs.length() - 2);
    toArgs.setLength(toArgs.length() - 2);
    this.rangeArgs = data.Key.class.getCanonicalName() + ".with(" + cacheIndexFieldName + ", " + fromArgs + "), " + data.Key.class.getCanonicalName() + ".with(" + cacheIndexFieldName + ", " + toArgs + ")";
  }

  private String writeGetRangeMethod(final String returnType) {
    if (!indexType.isBTree())
      return "";

    return
      "\n    public " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheMethodName + "_CACHED(" + rangeParams + ") {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".subMap(" + rangeArgs + ");" +
      "\n    }\n" +
      "\n    public " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheMethodName + "_SELECT(" + rangeParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".select(" + rangeArgs + ");" +
      "\n    }\n";
  }

  final String keyClause() {
    return keyClause(tableModel.singletonInstanceName + "." + cacheIndexFieldName);
  }

  final String keyClause(final String cacheColumnsName) {
    return data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + keyClauseValues + ")";
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

  final String writeCacheDeclare(final LinkedHashSet<String> keyClauseColumnAssignments) {
    keyClauseColumnAssignments(keyClauseColumnAssignments);
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

  String writeCacheInsert(final String classSimpleName, final CurOld curOld) {
    final String method = indexType.isUnique ? "put$" : "add$";
    return writeNullCheckClause(classSimpleName, curOld) + tableModel.singletonInstanceName + "." + cacheMapFieldName + "." + method + "(" + keyClause().replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ", " + classSimpleName + ".this);";
  }

  final String writeCacheSelectAll() {
    return tableModel.singletonInstanceName + "." + cacheMapFieldName + ".addKey(" + data.Key.class.getCanonicalName() + ".ALL);";
  }

  final String writeOnChangeClearCache(final String classSimpleName, final CurOld curOld) {
    return writeNullCheckClause(classSimpleName, curOld) + tableModel.singletonInstanceName + "." + cacheMapFieldName + ".remove$" + curOld + "(" + keyClause().replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + (indexType.isUnique ? "" : ", " + classSimpleName + ".this") + ");";
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