/* Copyright (c) 2017 JAX-DB
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

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

import org.libj.util.DiscreteTopology;
import org.libj.util.Interval;

public interface type {
  public abstract static class Key extends Interval<Key> implements Serializable {
    Key() {
      min = this;
    }

    @Override
    public Key getMax() {
      return max == null ? max = next() : max;
    }

    abstract Key next();
    abstract DiscreteTopology<Object[]> topology();

    @Override
    @SuppressWarnings("unchecked")
    public final int compareTo(final Interval<Key> o) {
      final Key key = (Key)o;
      final int i$ = length();
      if (i$ != key.length())
        throw new IllegalArgumentException("this.length() (" + i$ + ") != that.length() (" + key.length() + ")");

      for (int i = 0; i < i$; ++i) { // [RA]
        final Object a = value(i);
        final Object b = key.value(i);
        if (a == null) {
          if (b == null)
            continue;

          return -1;
        }

        if (b == null)
          return 1;

        if (a.getClass() != b.getClass())
          throw new IllegalArgumentException(a.getClass().getName() + " != " + b.getClass().getName());

        final int c = ((Comparable<Object>)a).compareTo(b);
        if (c != 0)
          return c;
      }

      return 0;
    }

    public abstract Object value(int i);
    public abstract Column column(int i);
    public abstract Key immutable();
    abstract int length();

    @Override
    public final int hashCode() {
      int hashCode = 1;
      for (int i = 0, i$ = length(); i < i$; ++i) { // [RA]
        hashCode *= 31;
        final Object value = value(i);
        if (value != null)
          hashCode += value.hashCode();
      }

      return hashCode;
    }

    @Override
    public final boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Key))
        return false;

      final Key that = (Key)obj;
      final int i$ = length();
      if (i$ != that.length())
        return false;

      for (int i = 0; i < i$; ++i) // [RA]
        if (!Objects.equals(value(i), that.value(i)))
          return false;

      return true;
    }

    @Override
    public String toString() {
      final int i$ = length();
      if (i$ == 0)
        return "{}";

      final StringBuilder s = new StringBuilder();
      s.append('{');
      for (int i = 0; i < i$; ++i) // [RA]
        s.append(value(i)).append(',');

      s.setCharAt(s.length() - 1, '}');
      return s.toString();
    }
  }

  public interface ApproxNumeric<V extends Number> extends Numeric<V> {
  }

  public interface ARRAY<V> extends Objective<V[]> {
  }

  public interface BIGINT extends ExactNumeric<Long> {
  }

  public interface BINARY extends Objective<byte[]> {
  }

  public interface BLOB extends LargeObject<InputStream> {
  }

  public interface BOOLEAN extends Primitive<Boolean> {
  }

  public interface CHAR extends Textual<String> {
  }

  public interface CLOB extends LargeObject<Reader> {
  }

  public interface Column<V> extends Entity {
  }

  public interface DATE extends Temporal<LocalDate> {
  }

  public interface DATETIME extends Temporal<LocalDateTime> {
  }

  public interface DECIMAL extends ExactNumeric<BigDecimal> {
  }

  public interface DOUBLE extends ApproxNumeric<Double> {
  }

  public interface ENUM<V extends EntityEnum> extends Textual<V> {
  }

  public interface Table extends Entity {
  }

  public interface ExactNumeric<V extends Number> extends Numeric<V> {
  }

  public interface FLOAT extends ApproxNumeric<Float> {
  }

  public interface INT extends ExactNumeric<Integer> {
  }

  public interface LargeObject<V extends Closeable> extends Objective<V> {
  }

  public interface Numeric<V extends Number> extends Primitive<V> {
  }

  public interface Objective<V> extends Column<V> {
  }

  public interface Primitive<V> extends Column<V> {
  }

  public interface SMALLINT extends ExactNumeric<Short> {
  }

  public interface Entity {
  }

  public interface Temporal<V extends java.time.temporal.Temporal> extends Objective<V> {
  }

  public interface Textual<V extends CharSequence & Comparable<?>> extends Objective<V> {
  }

  public interface TIME extends Temporal<LocalTime> {
  }

  public interface TINYINT extends ExactNumeric<Byte> {
  }
}