/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb.jsql;

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

interface kind {
  interface ApproxNumeric<T extends Number> extends Numeric<T> {
  }

  interface ARRAY<T> extends DataType<T> {
  }

  interface BIGINT extends ExactNumeric<Long> {
    interface UNSIGNED extends ExactNumeric<BigInteger>, Numeric.UNSIGNED {
    }
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
    interface UNSIGNED extends ApproxNumeric<BigDecimal>, Numeric.UNSIGNED {
    }
  }

  interface DOUBLE extends ApproxNumeric<Double> {
    interface UNSIGNED extends ApproxNumeric<Double>, Numeric.UNSIGNED {
    }
  }

  interface ENUM<T extends Enum<?>> extends Textual<T> {
  }

  interface Entity<T> extends Subject<T> {
  }

  interface ExactNumeric<T extends Number> extends Numeric<T> {
  }

  interface FLOAT extends ApproxNumeric<Float> {
    interface UNSIGNED extends ApproxNumeric<Float>, Numeric.UNSIGNED {
    }
  }

  interface INT extends ExactNumeric<Integer> {
    interface UNSIGNED extends ExactNumeric<Long>, Numeric.UNSIGNED {
    }
  }

  interface LargeObject<T extends Closeable> extends DataType<T> {
  }

  interface Numeric<T extends Number> extends DataType<T> {
    interface UNSIGNED {
    }
  }

  interface SMALLINT extends ExactNumeric<Short> {
    interface UNSIGNED extends ExactNumeric<Integer>, Numeric.UNSIGNED {
    }
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
    interface UNSIGNED extends ExactNumeric<Short>, Numeric.UNSIGNED {
    }
  }
}