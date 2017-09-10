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

import java.io.Closeable;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

interface type {
  public interface ApproxNumeric<T extends Number> extends Numeric<T> {
  }

  public interface ARRAY<T> extends DataType<T> {
  }

  public interface BIGINT extends ExactNumeric<Long> {
    public interface UNSIGNED extends ExactNumeric<BigInteger>, Numeric.UNSIGNED {
    }
  }

  public interface BINARY extends DataType<byte[]> {
  }

  public interface BLOB extends DataType<InputStream> {
  }

  public interface BOOLEAN extends DataType<Boolean> {
  }

  public interface CHAR extends Textual<String> {
  }

  public interface CLOB extends DataType<Reader> {
  }

  public interface DataType<T> extends Subject<T> {
  }

  public interface DATE extends Temporal<LocalDate> {
  }

  public interface DATETIME extends Temporal<LocalDateTime> {
  }

  public interface DECIMAL extends ExactNumeric<BigDecimal> {
    public interface UNSIGNED extends ApproxNumeric<BigDecimal>, Numeric.UNSIGNED {
    }
  }

  public interface DOUBLE extends ApproxNumeric<Double> {
    public interface UNSIGNED extends ApproxNumeric<Double>, Numeric.UNSIGNED {
    }
  }

  public interface ENUM<T extends Enum<?>> extends Textual<T> {
  }

  public interface Entity<T> extends Subject<T> {
  }

  public interface ExactNumeric<T extends Number> extends Numeric<T> {
  }

  public interface FLOAT extends ApproxNumeric<Float> {
    public interface UNSIGNED extends ApproxNumeric<Float>, Numeric.UNSIGNED {
    }
  }

  public interface INT extends ExactNumeric<Integer> {
    public interface UNSIGNED extends ExactNumeric<Long>, Numeric.UNSIGNED {
    }
  }

  public interface LargeObject<T extends Closeable> extends DataType<T> {
  }

  public interface Numeric<T extends Number> extends DataType<T> {
    public static interface UNSIGNED {
    }
  }

  public interface SMALLINT extends ExactNumeric<Short> {
    public interface UNSIGNED extends ExactNumeric<Integer>, Numeric.UNSIGNED {
    }
  }

  public interface Subject<T> {
  }

  public interface Temporal<T extends java.time.temporal.Temporal> extends DataType<T> {
  }

  public interface Textual<T extends Comparable<?>> extends DataType<T> {
  }

  public interface TIME extends Temporal<LocalTime> {
  }

  public interface TINYINT extends ExactNumeric<Byte> {
    public interface UNSIGNED extends ExactNumeric<Short>, Numeric.UNSIGNED {
    }
  }
}