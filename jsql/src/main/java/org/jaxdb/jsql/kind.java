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

interface kind {
  interface ApproxNumeric<T extends Number> extends Numeric<T> {
  }

  interface ARRAY<T> extends DataType<T> {
  }

  interface BIGINT extends ExactNumeric<Long> {
  }

  interface BINARY extends DataType<byte[]> {
  }

  interface BLOB extends DataType<InputStream> {
  }

  interface BOOLEAN extends DataType<Boolean> {
  }

  interface CHAR extends Textual<String> {
  }

  interface CLOB extends DataType<Reader> {
  }

  interface DataType<T> extends Subject<T> {
  }

  interface DATE extends Temporal<LocalDate> {
  }

  interface DATETIME extends Temporal<LocalDateTime> {
  }

  interface DECIMAL extends ExactNumeric<BigDecimal> {
  }

  interface DOUBLE extends ApproxNumeric<Double> {
  }

  interface ENUM<T extends Enum<?>> extends Textual<T> {
  }

  interface Entity<T> extends Subject<T> {
  }

  interface ExactNumeric<T extends Number> extends Numeric<T> {
  }

  interface FLOAT extends ApproxNumeric<Float> {
  }

  interface INT extends ExactNumeric<Integer> {
  }

  interface LargeObject<T extends Closeable> extends Objective<T> {
  }

  interface Numeric<T extends Number> extends DataType<T> {
  }

  interface Objective<T> extends DataType<T> {
  }

  interface Primitive<T> extends DataType<T> {
  }

  interface SMALLINT extends ExactNumeric<Short> {
  }

  interface Subject<T> {
  }

  interface Temporal<T extends java.time.temporal.Temporal> extends DataType<T> {
  }

  interface Textual<T extends Comparable<?>> extends DataType<T> {
  }

  interface TIME extends Temporal<LocalTime> {
  }

  interface TINYINT extends ExactNumeric<Byte> {
  }
}