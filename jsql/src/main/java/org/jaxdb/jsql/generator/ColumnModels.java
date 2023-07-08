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
import java.util.Iterator;
import java.util.LinkedHashSet;

class ColumnModels extends LinkedHashSet<ColumnModel> {
  static final ColumnModels EMPTY_SET = new ColumnModels(null, 0);
  final TableModel table;
  private String instanceNameForKey;

  ColumnModels(final TableModel table, final Collection<? extends ColumnModel> c) {
    super(c);
    this.table = table;
  }

  ColumnModels(final TableModel table, final int initialCapacity) {
    super(initialCapacity);
    this.table = table;
  }

  ColumnModels(final TableModel table, final ColumnModel columnModel) {
    super(1);
    this.table = table;
    add(columnModel);
  }

  @Override
  public boolean add(final ColumnModel e) {
    if (instanceNameForKey != null)
      throw new IllegalStateException();

    return super.add(e);
  }

  String getInstanceNameForKey() {
    if (instanceNameForKey != null)
      return instanceNameForKey;

    if (size() == 0)
      return instanceNameForKey = "";

    final StringBuilder b = new StringBuilder();
    final Iterator<ColumnModel> iterator = iterator();
    for (int i = 0; iterator.hasNext(); ++i) { // [S]
      if (i > 0)
        b.append('_');

      b.append(iterator.next().camelCase);
    }

    return instanceNameForKey = b.toString();
  }

  String getInstanceNameForCache(final String classCase) {
    return getInstanceNameForCache(getInstanceNameForKey(), classCase);
  }

  static String getInstanceNameForCache(final String instanceNameForKey, final String classCase) {
    return instanceNameForKey + "_TO_" + classCase;
  }
}