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

class ManyToManyRelation extends ForeignRelation {
  ManyToManyRelation(final String schemaClassName, final TableModel sourceTable, final TableModel tableModel, final ColumnModels columns, final TableModel referenceTable, final ColumnModels referenceColumns, final IndexType indexType, final IndexType indexTypeForeign) {
    super(schemaClassName, sourceTable, tableModel, columns, referenceTable, referenceColumns, indexType, indexTypeForeign);
  }

  @Override
  String writeCacheInsert(final String classSimpleName, final CurOld curOld) {
    final String method = indexType.isUnique ? "put$" : "add$";
    return writeNullCheckClause(classSimpleName, curOld) + tableModel.singletonInstanceName + "." + cacheMapFieldName + "." + method + "(" + keyClause(cacheIndexFieldNameForeign).replace("{1}", classSimpleName).replace("{2}", curOld.toString()) + ", " + classSimpleName + ".this);";
  }

  @Override
  String getSymbol() {
    return "*:*";
  }
}