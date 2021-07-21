/* Copyright (c) 2020 JAX-DB
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

import static org.junit.Assert.*;

import org.junit.Test;

public class NumericTest {
  private static final Class<?>[] numberTypes = {Byte.class, Short.class, Integer.class, Float.class, Double.class, Long.class};

  @Test
  @SuppressWarnings("unchecked")
  public void testValueOf() {
    for (int i = 0; i < numberTypes.length; ++i) {
      for (int j = 0; j < numberTypes.length; ++j) {
        final Class<? extends Number> from = (Class<? extends Number>)numberTypes[i];
        final Class<? extends Number> to = (Class<? extends Number>)numberTypes[j];
        final Number value = data.Numeric.valueOf(111, from);
        assertEquals(value, data.Numeric.valueOf(data.Numeric.valueOf(value, to), from));
      }
    }
  }
}