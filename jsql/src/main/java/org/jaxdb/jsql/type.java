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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public interface type {
  public abstract static class Key implements Comparable<type.Key> {
    @Override
    public int compareTo(final type.Key o) {
      if (length() != o.length())
        throw new IllegalArgumentException();

      for (int i = 0; i < length(); ++i) {
        final Object a = get(i);
        final Object b = o.get(i);
        if (a.getClass() != b.getClass())
          throw new IllegalArgumentException();

        if (!(a instanceof Comparable))
          throw new UnsupportedOperationException("Unsupported BTREE for: " + a.getClass().getName());

        final int c = ((Comparable)a).compareTo(b);
        if (c != 0)
          return c;
      }

      return 0;
    }

    public abstract Object get(int i);
    abstract int length();
    public abstract Key immutable();

    @Override
    public int hashCode() {
      int hashCode = 1;
      for (int i = 0, len = length(); i < len; ++i) {
        final Object value = get(i);
        hashCode = 31 * hashCode + (value == null ? 0 : value.hashCode());
      }

      return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Key))
        return false;

      final Key that = (Key)obj;
      final int len = length();
      if (len != that.length())
        return false;

      for (int i = 0; i < len; ++i)
        if (!Objects.equals(get(i), that.get(i)))
          return false;

      return true;
    }

    @Override
    public String toString() {
      final int len = length();
      if (len == 0)
        return "{}";

      final StringBuilder s = new StringBuilder();
      s.append('{');
      for (int i = 0; i < len; ++i)
        s.append(get(i)).append(',');

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

  public interface Column<V> extends Entity<V> {
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

  public interface Table<T extends Table<T>> extends Entity<T> {
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

  public interface Entity<V> {
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