/* Copyright (c) 2023 JAX-DB
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

class ForeignKeys extends LinkedHashSet<ForeignKey> {
  ForeignKeys() {
    super();
  }

  ForeignKeys(final Collection<? extends ForeignKey> c) {
    super(c);
  }

  ForeignKeys(final int initialCapacity, final float loadFactor) {
    super(initialCapacity, loadFactor);
  }

  ForeignKeys(final int initialCapacity) {
    super(initialCapacity);
  }

  @Override
  public String toString() {
    if (size() == 0)
      return "[]";

    final StringBuilder b = new StringBuilder("{\n");
    final Iterator<ForeignKey> iterator = iterator();
    for (int i = 0; iterator.hasNext(); ++i) { // [C]
      if (i > 0)
        b.append(",\n");

      b.append("  ").append(iterator.next());
    }

    b.append("\n}");
    return b.toString();
  }
}