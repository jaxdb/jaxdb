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