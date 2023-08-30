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

package org.jaxdb.jsql;

import org.libj.util.function.IntBooleanConsumer;
import org.libj.util.function.Throwing;

@FunctionalInterface
public interface ThrowingIntBooleanConsumer<E extends Throwable> extends IntBooleanConsumer {
  @Override
  default void accept(final int v1, final boolean v2) {
    try {
      acceptThrows(v1, v2);
    }
    catch (final Throwable e) {
      Throwing.rethrow(e);
    }
  }

  void acceptThrows(int v1, boolean v2) throws E;
}