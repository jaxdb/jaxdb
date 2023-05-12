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

import java.util.Map;

import org.jaxdb.jsql.data;

class OneToManyRelation extends ForeignRelation {
  OneToManyRelation(final String schemaClassName, final TableMeta sourceTable, final TableMeta tableMeta, final Columns columns, final TableMeta referenceTable, final Columns referenceColumns, final IndexType indexType, final IndexType indexTypeForeign) {
    super(schemaClassName, sourceTable, tableMeta, columns, referenceTable, referenceColumns, indexType, indexTypeForeign);
  }

  @Override
  String writeDeclaration(final String classSimpleName) {
    final String typeName = indexTypeForeign.isUnique ? declarationNameForeign : getType();
    final String declaredName = indexTypeForeign.isUnique ? declarationNameForeign : getDeclaredName();

    final StringBuilder out = new StringBuilder();
    out.append("\n    public final ").append(typeName).append(' ').append(fieldName).append("() {");
    out.append("\n      final ").append(Map.class.getName()).append('<').append(data.Key.class.getCanonicalName()).append(',').append(declaredName).append("> cache = ").append(declarationNameForeign).append('.').append(cacheInstanceNameForeign).append(';');
    out.append("\n      return cache == null ? null : cache.get(").append(keyClause.replace("{1}", classSimpleName).replace("{2}", "Old")).append(");");
    out.append("\n    }");
    return out.toString();
  }

  @Override
  String writeOnChangeReverse(final String fieldName) {
//    return "if (" + fieldName + " != null) " + fieldName + "." + this.fieldName + " = null;";
    return null;
  }

  @Override
  String getDeclaredName() {
    return indexTypeForeign.getInterfaceClass(declarationNameForeign);
  }

  @Override
  String getType() {
    return indexTypeForeign.getInterfaceClass(declarationNameForeign);
  }

  @Override
  String getSymbol() {
    return "1:*";
  }
}