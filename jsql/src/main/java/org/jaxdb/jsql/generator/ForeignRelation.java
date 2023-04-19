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

abstract class ForeignRelation extends Relation {
  final IndexType indexTypeForeign;
  private final Columns referenceColumns;

  final Relations<ForeignRelation> reverses = new Relations<>();
  final TableMeta referenceTable;
  final String fieldName;

  final String cacheInstanceNameForeign;
  final String declarationNameForeign;

  ForeignRelation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final TableMeta referenceTable, final Columns referenceColumns, final IndexType indexType, final IndexType indexTypeForeign) {
    super(schemaClassName, sourceTable, tableMeta, columns, indexType);
    this.indexTypeForeign = indexTypeForeign;
    this.referenceTable = referenceTable;
    this.referenceColumns = referenceColumns;

    final StringBuilder foreignName = new StringBuilder();
    if (referenceColumns.size() > 0)
      for (final ColumnMeta columnMeta : referenceColumns) // [S]
        foreignName.append(columnMeta.camelCase).append('$');

    foreignName.setLength(foreignName.length() - 1);
    this.fieldName = columnName + "$" + referenceTable.classCase + "_" + foreignName;

    this.cacheInstanceNameForeign = foreignName + "To" + referenceTable.classCase;
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
    out.append("\n    public final ").append(typeName).append(' ').append(fieldName).append("() {");
    out.append("\n      final org.jaxdb.jsql.RelationMap<").append(declaredName).append("> cache = ").append(declarationNameForeign).append('.').append(cacheInstanceNameForeign).append(';');
    out.append("\n      return cache != null ? cache.superGet(").append(keyClause.replace("{1}", classSimpleName).replace("{2}", "Old")).append(") : null;");
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