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

public interface type {
  abstract static interface Key extends Serializable {
    Object value(int i);
    Column column(int i);
    int length();
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