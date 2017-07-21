/* Copyright (c) 2016 lib4j
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

public abstract class GenerateOn<T> {
  public static final GenerateOn<Number> INCREMENT = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("cast")
    public void generate(final type.DataType<Number> dataType) {
      final type.DataType<? extends Number> numberType = (type.DataType<? extends Number>)dataType;
      if (numberType instanceof type.TINYINT)
        ((type.TINYINT)numberType).set(DML.ADD((type.TINYINT)numberType, (byte)1));
      else if (numberType instanceof type.TINYINT.UNSIGNED)
        ((type.TINYINT.UNSIGNED)numberType).set(DML.ADD((type.TINYINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof type.SMALLINT)
        ((type.SMALLINT)numberType).set(DML.ADD((type.SMALLINT)numberType, (byte)1));
      else if (numberType instanceof type.SMALLINT.UNSIGNED)
        ((type.SMALLINT.UNSIGNED)numberType).set(DML.ADD((type.SMALLINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof type.INT)
        ((type.INT)numberType).set(DML.ADD((type.INT)numberType, (byte)1));
      else if (numberType instanceof type.INT.UNSIGNED)
        ((type.INT.UNSIGNED)numberType).set(DML.ADD((type.INT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof type.BIGINT)
        ((type.BIGINT)numberType).set(DML.ADD((type.BIGINT)numberType, (byte)1));
      else if (numberType instanceof type.BIGINT.UNSIGNED)
        ((type.BIGINT.UNSIGNED)numberType).set(DML.ADD((type.BIGINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof type.FLOAT)
        ((type.FLOAT)numberType).set(DML.ADD((type.FLOAT)numberType, 1f));
      else if (numberType instanceof type.FLOAT.UNSIGNED)
        ((type.FLOAT.UNSIGNED)numberType).set(DML.ADD((type.FLOAT.UNSIGNED)numberType, DML.UNSIGNED(1f)));
      else if (numberType instanceof type.DOUBLE)
        ((type.DOUBLE)numberType).set(DML.ADD((type.DOUBLE)numberType, 1f));
      else if (numberType instanceof type.DOUBLE.UNSIGNED)
        ((type.DOUBLE.UNSIGNED)numberType).set(DML.ADD((type.DOUBLE.UNSIGNED)numberType, DML.UNSIGNED(1d)));
      else if (numberType instanceof type.DECIMAL)
        ((type.DECIMAL)numberType).set(DML.ADD((type.DECIMAL)numberType, 1f));
      else if (numberType instanceof type.DECIMAL.UNSIGNED)
        ((type.DECIMAL.UNSIGNED)numberType).set(DML.ADD((type.DECIMAL.UNSIGNED)numberType, DML.UNSIGNED(1d)));
      else
        throw new UnsupportedOperationException("Unsupported type: " + numberType.getClass().getName());
    }
  };

  public static final GenerateOn<Temporal> TIMESTAMP = new GenerateOn<Temporal>() {
    @Override
    @SuppressWarnings("cast")
    public void generate(final type.DataType<Temporal> dataType) {
      final type.DataType<? extends Temporal> temporalType = (type.DataType<? extends Temporal>)dataType;
      if (temporalType instanceof type.DATE)
        dataType.value = LocalDate.now();
      else if (temporalType instanceof type.TIME)
        dataType.value = LocalTime.now();
      else if (temporalType instanceof type.DATETIME)
        dataType.value = LocalDateTime.now();
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<String> UUID = new GenerateOn<String>() {
    @Override
    public void generate(final type.DataType<String> dataType) {
      dataType.value = java.util.UUID.randomUUID().toString();
    }
  };

  public abstract void generate(final type.DataType<T> dataType);
}