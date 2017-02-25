/* Copyright (c) 2017 Seva Safris
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

package org.safris.dbb.jsql.generator;

import org.safris.dbb.jsql.DMLGenerator;

public class Args {
  public final Class<?> a;
  public final Class<?> b;

  public Args(final Class<?> a, final Class<?> b) {
    this.a = a;
    this.b = b;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof Args))
      return false;

    final Args that = (Args)obj;
    return a == that.a && b == that.b;
  }

  @Override
  public int hashCode() {
    return a.hashCode() ^ b.hashCode();
  }

  @Override
  public String toString() {
    return "(" + DMLGenerator.getName(a) + ", " + DMLGenerator.getName(b) + ")";
  }
}