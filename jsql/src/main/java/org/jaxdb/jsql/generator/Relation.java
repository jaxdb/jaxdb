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

  final Columns columns;
  final TableMeta sourceTable;
  final TableMeta tableMeta;
  final IndexType indexType;
  final String columnName;
  final String keyClauseValues;
  final String keyCondition;
  final String rangeParams;
  final String keyParams;
  final String keyArgs;
  final String rangeArgs;

  Relation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final IndexType indexType) {
    if ("incident_camera".equals(tableMeta.tableName) && !indexType.isBTree())
      System.out.println();
    this.cacheMethodName = columns.getInstanceNameForCache(tableMeta.classCase);
    this.cacheMapFieldName = "_" + cacheMethodName + "Map$";
    this.cacheIndexFieldName = "_" + cacheMethodName + "Index$";
    this.declarationName = schemaClassName + "." + tableMeta.classCase;

    this.sourceTable = sourceTable;
    this.tableMeta = tableMeta;
    this.columns = columns;
    this.indexType = indexType;

    this.columnName = columns.getInstanceNameForKey();

    final StringBuilder keyClauseValues = new StringBuilder();
    final StringBuilder keyCondition = new StringBuilder();
    final StringBuilder keyParams = new StringBuilder();
    final StringBuilder rangeParams = new StringBuilder();
    final StringBuilder keyArgs = new StringBuilder();
    final StringBuilder fromArgs = new StringBuilder();
    final StringBuilder toArgs = new StringBuilder();

    if (columns.size() > 0) {
      for (final ColumnMeta column : columns) { // [S]
        keyClauseValues.append("{1}.this.").append(column.camelCase).append(".get{2}(), ");
        keyCondition.append("{1}.this.").append(column.camelCase).append(".get{2}() != null && ");
        keyParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append(", ");
        rangeParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append("From, final ").append(column.rawType).append(' ').append(column.instanceCase).append("To, ");
        keyArgs.append(column.instanceCase).append(", ");
        fromArgs.append(column.instanceCase).append("From, ");
        toArgs.append(column.instanceCase).append("To, ");
      }
    }

    keyClauseValues.setLength(keyClauseValues.length() - 2);
    this.keyClauseValues = keyClauseValues.toString();

    keyCondition.setLength(keyCondition.length() - 4);
    this.keyCondition = keyCondition.toString();

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

  private final String writeGetRangeMethod(final String returnType) {
    if (!indexType.isBTree())
      return "";

    return
      "\n    public " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheMethodName + "(" + rangeParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + tableMeta.singletonInstanceName + "." + cacheMapFieldName + ".select(" + rangeArgs + ");" +
      "\n    }\n";
  }

  String keyClause() {
    return keyClause(tableMeta.singletonInstanceName + "." + cacheIndexFieldName);
  }

  String keyClause(final String cacheColumnsName) {
    return data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + keyClauseValues + ")";
  }

  void keyClauseColumnAssignments(final LinkedHashSet<String> keyClauseColumnAssignments) {
    if (columns.size() == 0)
      return;

    final StringBuilder keyClauseColumnAssignment = new StringBuilder();
    final boolean isPrimary = columns.equals(tableMeta.primaryKey);
    if (isPrimary) {
      keyClauseColumnAssignment.append(tableMeta.singletonInstanceName).append("._primary$");
      keyClauseColumnAssignments.add(cacheIndexFieldName + " = " + keyClauseColumnAssignment);
    }
    else {
      for (final ColumnMeta column : columns) // [S]
        keyClauseColumnAssignment.append(tableMeta.singletonInstanceName).append("._column$[").append(column.index).append("], ");

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
      "\n    public " + returnType + " " + cacheMethodName + "(" + keyParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + tableMeta.singletonInstanceName + "." + cacheMapFieldName + ".select(" + keyArgs + ");" +
      "\n    }\n" +
      writeGetRangeMethod(returnType) +
      "\n    public " + indexType.getInterfaceClass(returnType) + " " + cacheMethodName + "() throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      " + tableMeta.singletonInstanceName + "." + cacheMapFieldName + ".selectAll();" +
      "\n      return " + tableMeta.singletonInstanceName + "." + cacheMapFieldName + ";" +
      "\n    }";
  }

  final String writeCacheInit() {
    return cacheMapFieldName + " = new " + indexType.getConcreteClass(null) + "<>(this);";
  }

  String writeCacheInsert(final String classSimpleName, final CurOld curOld) {
    final String method = indexType.isUnique ? "superPut" : "superAdd";
    return "if (" + keyCondition.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ") " + tableMeta.singletonInstanceName + "." + cacheMapFieldName + "." + method + "(" + keyClause().replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ", " + classSimpleName + ".this);";
  }

  String writeCacheSelectAll() {
    return tableMeta.singletonInstanceName + "." + cacheMapFieldName + ".addKey(" + data.Key.class.getCanonicalName() + ".ALL);";
  }

  String writeOnChangeClearCache(final String classSimpleName, final String keyClause, final CurOld curOld) {
    return tableMeta.singletonInstanceName + "." + cacheMapFieldName + ".superRemove" + curOld + "(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + (indexType.isUnique ? "" : ", " + classSimpleName + ".this") + ");";
  }

  String writeOnChangeClearCacheForeign(final String classSimpleName, final String keyClause, final CurOld curOld, final CurOld curOld2) {
//    return tableMeta.singletonInstanceName + "." + cacheInstanceName + ".superGetxxx(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ");";
    return null;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Relation))
      return false;

    final Relation that = (Relation)obj;
    return tableMeta.equals(that.tableMeta) && columns.equals(that.columns);
  }

  @Override
  public int hashCode() {
    return tableMeta.hashCode() ^ columns.hashCode();
  }

  @Override
  public String toString() {
    return "Relation: " + declarationName + " " + columnName + ":" + indexType;
  }
}