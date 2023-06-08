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

import org.jaxdb.jsql.data;

class Relation {
  final Columns columns;
  final TableMeta sourceTable;
  final TableMeta tableMeta;
  final IndexType indexType;
  final String columnName;
  final String keyClause;
  final String keyCondition;
  final String keyParams;
  final String keyArgs;

  final String cacheInstanceName;
  final String declarationName;

  Relation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final IndexType indexType) {
    this.sourceTable = sourceTable;
    this.tableMeta = tableMeta;
    this.columns = columns;
    this.indexType = indexType;

    this.columnName = columns.getInstanceNameForKey();

    final StringBuilder keyClause = new StringBuilder();
    final StringBuilder keyCondition = new StringBuilder();
    final StringBuilder keyParams = new StringBuilder();
    final StringBuilder keyArgs = new StringBuilder();
    if (columns.size() > 0) {
      for (final ColumnMeta column : columns) { // [S]
        keyClause.append("{1}.this.").append(column.camelCase).append(".{2}(), ");
        keyCondition.append("{1}.this.").append(column.camelCase).append(".{2}() != null && ");
        keyParams.append("final ").append(column.rawType).append(' ').append(column.instanceCase).append(", ");
        keyArgs.append(column.instanceCase).append(", ");
      }
    }

    keyClause.setLength(keyClause.length() - 2);
    this.keyClause = data.Key.class.getCanonicalName() + ".with(" + keyClause + ")";

    keyCondition.setLength(keyCondition.length() - 4);
    this.keyCondition = keyCondition.toString();

    keyParams.setLength(keyParams.length() - 2);
    this.keyParams = keyParams.toString();

    keyArgs.setLength(keyArgs.length() - 2);
    this.keyArgs = keyArgs.toString();

    this.cacheInstanceName = tableMeta.getInstanceNameForCache(columns);
    this.declarationName = schemaClassName + "." + tableMeta.classCase;
  }

  final String writeCacheDeclare() {
    final String returnType = indexType.unique ? declarationName : indexType.getInterfaceClass().getName() + "<" + data.Key.class.getCanonicalName() + "," + declarationName + ">";
    return
      "\n    static " + indexType.getConcreteClass().getName() + "<" + declarationName + "> " + cacheInstanceName + ";\n" +
      "\n    public static " + returnType + " " + cacheInstanceName + "(" + keyParams + ") {" +
      "\n      return " + declarationName + "." + cacheInstanceName + ".get(" + data.Key.class.getCanonicalName() + ".with(" + keyArgs + "));" +
      "\n    }\n" +
      "\n    public static " + indexType.getInterfaceClass().getName() + "<" + data.Key.class.getCanonicalName() + "," + returnType + "> " + cacheInstanceName + "() {" +
      "\n      return " + declarationName + "." + cacheInstanceName + ";" +
      "\n    }";
  }

  final String writeCacheInit() {
    return cacheInstanceName + " = new " + indexType.getConcreteClass().getName() + "<>();";
  }

  String writeCacheInsert(final String classSimpleName, final String curOld) {
    final String method = indexType.unique ? "put" : "add";
    return "if (" + keyCondition.replace("{1}", classSimpleName).replace("{2}", curOld) + ") " + declarationName + "." + cacheInstanceName + "." + method + "(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld) + ", " + classSimpleName + ".this);";
  }

  String writeOnChangeClearCache(final String classSimpleName, final String keyClause, final String curOld) {
    return declarationName + "." + cacheInstanceName + ".remove" + curOld + "(" + keyClause.replace("{1}", classSimpleName).replace("{2}", "get" + curOld) + (indexType.unique ? "" : ", " + classSimpleName + ".this") + ");";
  }

  String writeOnChangeClearCacheForeign(final String classSimpleName, final String keyClause, final String curOld, final String curOld2) {
//    return declarationName + "." + cacheInstanceName + ".superGetxxx(" + keyClause.replace("{1}", classSimpleName).replace("{2}", curOld) + ");";
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