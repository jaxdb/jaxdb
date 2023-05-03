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

  final String cacheColumnsName;
  final String cacheInstanceName;
  final String declarationName;

  final Columns columns;
  final TableMeta sourceTable;
  final TableMeta tableMeta;
  final IndexType indexType;
  final String columnName;
  final String keyClauseColumns;
  final String keyClause;
  final String keyCondition;
  final String rangeParams;
  final String keyParams;
  final String keyArgs;
  final String rangeArgs;

  Relation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final IndexType indexType) {
    this.cacheInstanceName = tableMeta.getInstanceNameForCache(columns);
    this.cacheColumnsName = "$" + cacheInstanceName;
    this.declarationName = schemaClassName + "." + tableMeta.classCase;

    this.sourceTable = sourceTable;
    this.tableMeta = tableMeta;
    this.columns = columns;
    this.indexType = indexType;;

    this.columnName = columns.getInstanceNameForKey();

    final StringBuilder keyClauseColumns = new StringBuilder();
    final StringBuilder keyClauseValues = new StringBuilder();
    final StringBuilder keyCondition = new StringBuilder();
    final StringBuilder keyParams = new StringBuilder();
    final StringBuilder rangeParams = new StringBuilder();
    final StringBuilder keyArgs = new StringBuilder();
    final StringBuilder fromArgs = new StringBuilder();
    final StringBuilder toArgs = new StringBuilder();
    if (columns.size() > 0) {
      for (final ColumnMeta column : columns) { // [S]
        keyClauseColumns.append(column.getCanonicalName(null)).append("(), ");
        keyClauseValues.append("{1}.this.").append(column.camelCase).append(".get{2}(), ");
        keyCondition.append("{1}.this.").append(column.camelCase).append(".get{2}() != null && ");
        keyParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append(", ");
        rangeParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append("From, final ").append(column.rawType).append(' ').append(column.instanceCase).append("To, ");
        keyArgs.append(column.instanceCase).append(", ");
        fromArgs.append(column.instanceCase).append("From, ");
        toArgs.append(column.instanceCase).append("To, ");
      }
    }

    keyClauseColumns.setLength(keyClauseColumns.length() - 2);
    this.keyClauseColumns = "private static final " + data.Column.class.getCanonicalName() + "<?>[] " + cacheColumnsName + " = {" + keyClauseColumns + "}";

    keyClauseValues.setLength(keyClauseValues.length() - 2);
    this.keyClause = data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + keyClauseValues + ")";

    keyCondition.setLength(keyCondition.length() - 4);
    this.keyCondition = keyCondition.toString();

    keyParams.setLength(keyParams.length() - 2);
    this.keyParams = keyParams.toString();

    rangeParams.setLength(rangeParams.length() - 2);
    this.rangeParams = rangeParams.toString();

    keyArgs.setLength(keyArgs.length() - 2);
    this.keyArgs = data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + keyArgs + ")";

    fromArgs.setLength(fromArgs.length() - 2);
    toArgs.setLength(toArgs.length() - 2);
    this.rangeArgs = data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + fromArgs + "), " + data.Key.class.getCanonicalName() + ".with(" + cacheColumnsName + ", " + toArgs + ")";
  }

  private final String writeGetRangeMethod(final String returnType) {
    if (!indexType.isBTree())
      return "";

    return
      "\n    public static " + SortedMap.class.getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheInstanceName + "(" + rangeParams + ") throws " + IOException.class.getName() + ", " + SQLException.class.getName() + " {" +
      "\n      return " + declarationName + "." + cacheInstanceName + ".get(" + rangeArgs + ");" +
      "\n    }\n";
  }

  final String writeCacheDeclare() {
    final String returnType = indexType.unique ? declarationName : indexType.getInterfaceClass().getName() + "<" + data.Key.class.getCanonicalName() + "," + declarationName + ">";
    return
      "\n    " + keyClauseColumns + ";" +
      "\n    static " + indexType.getConcreteClass().getName() + "<" + declarationName + "> " + cacheInstanceName + ";\n" +
      "\n    public static " + returnType + " " + cacheInstanceName + "(" + keyParams + ") {" +
      "\n      return " + declarationName + "." + cacheInstanceName + ".get(" + keyArgs + ");" +
      "\n    }\n" +
      writeGetRangeMethod(returnType) +
      "\n    public static " + indexType.getInterfaceClass().getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheInstanceName + "() {" +
      "\n      return " + declarationName + "." + cacheInstanceName + ";" +
      "\n    }";
  }

  final String writeCacheInit() {
    return cacheInstanceName + " = new " + indexType.getConcreteClass().getName() + "<>(this);";
  }

  String writeCacheInsert(final String classSimpleName, final CurOld curOld, final Boolean addKey) {
    final String method = indexType.unique ? "put" : "add";
    return "if (" + keyCondition.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ") " + declarationName + "." + cacheInstanceName + "." + method + "(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ", " + classSimpleName + ".this, " + (addKey == null ? "addKey" : addKey) + ");";
  }

  String writeOnChangeClearCache(final String classSimpleName, final String keyClause, final CurOld curOld) {
    return declarationName + "." + cacheInstanceName + ".remove" + curOld + "(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + (indexType.unique ? "" : ", " + classSimpleName + ".this") + ");";
  }

  String writeOnChangeClearCacheForeign(final String classSimpleName, final String keyClause, final CurOld curOld, final CurOld curOld2) {
//    return declarationName + "." + cacheInstanceName + ".superGetxxx(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ");";
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