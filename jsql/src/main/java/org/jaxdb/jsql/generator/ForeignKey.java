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

class ForeignKey {
  final TableModel table;
  final ColumnModels columns;
  final TableModel referenceTable;
  final ColumnModels referenceColumns;

  ForeignKey(final TableModel table, final ColumnModels columns, final TableModel referenceTable, final ColumnModels referenceColumns) {
    this.table = table;
    this.columns = columns;
    this.referenceTable = referenceTable;
    this.referenceColumns = referenceColumns;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof ForeignKey))
      return false;

    final ForeignKey that = (ForeignKey)obj;
    return table.equals(that.table) && columns.equals(that.columns) && referenceTable.equals(that.referenceTable) && referenceColumns.equals(that.referenceColumns);
  }

  @Override
  public int hashCode() {
    return table.hashCode() ^ columns.hashCode() ^ referenceTable.hashCode() ^ referenceColumns.hashCode();
  }

  @Override
  public String toString() {
    return table.tableName + ":" + columns + " -> " + referenceTable.tableName + ":" + referenceColumns;
  }
}