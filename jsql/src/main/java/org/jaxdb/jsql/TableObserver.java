/* Copyright (c) 2021 JAX-DB
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

import org.jaxdb.jsql.data.Column;

public interface TableObserver {
  default void beforeSetBoolean(final Column<Boolean> column, final boolean changed, final boolean oldNull, final boolean oldValue, final boolean newNull, final boolean newValue) {
  }

  default void beforeSetByte(final Column<Byte> column, final boolean changed, final boolean oldNull, final byte oldValue, final boolean newNull, final byte newValue) {
  }

  default void beforeSetShort(final Column<Short> column, final boolean changed, final boolean oldNull, final short oldValue, final boolean newNull, final short newValue) {
  }

  default void beforeSetInt(final Column<Integer> column, final boolean changed, final boolean oldNull, final int oldValue, final boolean newNull, final int newValue) {
  }

  default void beforeSetLong(final Column<Long> column, final boolean changed, final boolean oldNull, final long oldValue, final boolean newNull, final long newValue) {
  }

  default void beforeSetFloat(final Column<Float> column, final boolean changed, final boolean oldNull, final float oldValue, final boolean newNull, final float newValue) {
  }

  default void beforeSetDouble(final Column<Double> column, final boolean changed, final boolean oldNull, final double oldValue, final boolean newNull, final double newValue) {
  }

  default <V>void beforeSetObject(final Column<V> column, final boolean changed, final V oldValue, final V newValue) {
  }
}