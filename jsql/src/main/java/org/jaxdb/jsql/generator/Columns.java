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

import java.util.Collection;
import java.util.LinkedHashSet;

class Columns extends LinkedHashSet<ColumnMeta> {
  static final Columns EMPTY_SET = new Columns(null, 0);
  final TableMeta table;

  Columns(final TableMeta table, final Collection<? extends ColumnMeta> c) {
    super(c);
    this.table = table;
  }

  Columns(final TableMeta table, final int initialCapacity) {
    super(initialCapacity);
    this.table = table;
  }

  Columns(final TableMeta table, final ColumnMeta columnMeta) {
    super(1);
    this.table = table;
    add(columnMeta);
  }

  String getInstanceNameForKey() {
    final StringBuilder columnName = new StringBuilder();
    if (size() > 0)
      for (final ColumnMeta columnMeta : this) // [S]
        columnName.append(columnMeta.camelCase).append('$');

    if (columnName.length() > 0)
      columnName.setLength(columnName.length() - 1);

    return columnName.toString();
  }
}