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

import org.jaxdb.jsql.CacheMap;

abstract class ForeignRelation extends Relation {
  final IndexType indexTypeForeign;
  private final Columns referenceColumns;

  final Relations<ForeignRelation> reverses = new Relations<>();
  final TableMeta referenceTable;
  final String fieldName;

  final String cacheIndexFieldNameForeign;
  final String cacheMapFieldNameForeign;
  final String declarationNameForeign;

  ForeignRelation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final TableMeta referenceTable, final Columns referenceColumns, final IndexType indexType, final IndexType indexTypeForeign) {
    super(schemaClassName, sourceTable, tableMeta, columns, indexType);
    this.indexTypeForeign = indexTypeForeign;
    this.referenceTable = referenceTable;
    this.referenceColumns = referenceColumns;

    final String foreignName = referenceColumns.getInstanceNameForKey();
    this.fieldName = columnName + "$" + referenceTable.classCase + "_" + foreignName;

    final String cacheMethodNameForeign = Columns.getInstanceNameForCache(foreignName, referenceTable.classCase);
    this.cacheMapFieldNameForeign = "_" + cacheMethodNameForeign + "Map$";
    this.cacheIndexFieldNameForeign = referenceTable.singletonInstanceName + "._" + cacheMethodNameForeign + "Index$";
    this.declarationNameForeign = schemaClassName + "." + referenceTable.classCase;
  }

  abstract String getType();
  abstract String getDeclaredName();
  abstract String getSymbol();
  abstract String writeOnChangeReverse(String fieldName);

  String writeDeclaration(final String classSimpleName) {
    final String typeName = getType();
    final String declaredName = getDeclaredName();

    final StringBuilder out = new StringBuilder();
    out.append("\n    public final ").append(typeName).append(' ').append(fieldName).append("() throws ").append(IOException.class.getName()).append(", ").append(SQLException.class.getName()).append(" {");
    out.append("\n      final ").append(CacheMap.class.getName()).append('<').append(declaredName).append("> cache = ").append(referenceTable.singletonInstanceName).append('.').append(cacheMapFieldNameForeign).append(';');
    out.append("\n      return cache == null ? null : cache.superSelect(").append(keyClause(cacheIndexFieldNameForeign).replace("{1}", classSimpleName).replace("{2}", "Old")).append(");");
    out.append("\n    }");
    return out.toString();
  }

  final String writeOnChangeForward() {
    return null;
  }

  @Override
  public int hashCode() {
    return super.hashCode() ^ referenceTable.hashCode() ^ referenceColumns.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Relation) || !super.equals(obj))
      return false;

    final ForeignRelation that = (ForeignRelation)obj;
    return referenceTable.equals(that.referenceTable) && referenceColumns.equals(that.referenceColumns);
  }

  @Override
  public final String toString() {
    return referenceTable.tableName + "<" + getSymbol() + ">" + referenceColumns;
  }
}