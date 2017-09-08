/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import org.lib4j.lang.Strings;

final class Alias extends Compilable {
  protected final String name;

  public Alias(final int index) {
    this.name = Strings.getAlpha(index);
  }

  @Override
  protected final void compile(final Compilation compilation) {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }

  @Override
  public boolean equals(final Object obj) {
    return this == obj || obj instanceof Alias && name.equals(((Alias)obj).name);
  }

  @Override
  public int hashCode() {
    return 199 * name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }
}