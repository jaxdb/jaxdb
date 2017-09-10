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
    public void generate(final data.DataType<Number> dataType) {
      final data.DataType<? extends Number> numberType = (data.DataType<? extends Number>)dataType;
      if (numberType instanceof data.TINYINT)
        ((data.TINYINT)numberType).set(DML.ADD((data.TINYINT)numberType, (byte)1));
      else if (numberType instanceof data.TINYINT.UNSIGNED)
        ((data.TINYINT.UNSIGNED)numberType).set(DML.ADD((data.TINYINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof data.SMALLINT)
        ((data.SMALLINT)numberType).set(DML.ADD((data.SMALLINT)numberType, (byte)1));
      else if (numberType instanceof data.SMALLINT.UNSIGNED)
        ((data.SMALLINT.UNSIGNED)numberType).set(DML.ADD((data.SMALLINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof data.INT)
        ((data.INT)numberType).set(DML.ADD((data.INT)numberType, (byte)1));
      else if (numberType instanceof data.INT.UNSIGNED)
        ((data.INT.UNSIGNED)numberType).set(DML.ADD((data.INT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof data.BIGINT)
        ((data.BIGINT)numberType).set(DML.ADD((data.BIGINT)numberType, (byte)1));
      else if (numberType instanceof data.BIGINT.UNSIGNED)
        ((data.BIGINT.UNSIGNED)numberType).set(DML.ADD((data.BIGINT.UNSIGNED)numberType, DML.UNSIGNED((byte)1)));
      else if (numberType instanceof data.FLOAT)
        ((data.FLOAT)numberType).set(DML.ADD((data.FLOAT)numberType, 1f));
      else if (numberType instanceof data.FLOAT.UNSIGNED)
        ((data.FLOAT.UNSIGNED)numberType).set(DML.ADD((data.FLOAT.UNSIGNED)numberType, DML.UNSIGNED(1f)));
      else if (numberType instanceof data.DOUBLE)
        ((data.DOUBLE)numberType).set(DML.ADD((data.DOUBLE)numberType, 1f));
      else if (numberType instanceof data.DOUBLE.UNSIGNED)
        ((data.DOUBLE.UNSIGNED)numberType).set(DML.ADD((data.DOUBLE.UNSIGNED)numberType, DML.UNSIGNED(1d)));
      else if (numberType instanceof data.DECIMAL)
        ((data.DECIMAL)numberType).set(DML.ADD((data.DECIMAL)numberType, 1f));
      else if (numberType instanceof data.DECIMAL.UNSIGNED)
        ((data.DECIMAL.UNSIGNED)numberType).set(DML.ADD((data.DECIMAL.UNSIGNED)numberType, DML.UNSIGNED(1d)));
      else
        throw new UnsupportedOperationException("Unsupported type: " + numberType.getClass().getName());
    }
  };

  public static final GenerateOn<Temporal> TIMESTAMP = new GenerateOn<Temporal>() {
    @Override
    @SuppressWarnings("cast")
    public void generate(final data.DataType<Temporal> dataType) {
      final data.DataType<? extends Temporal> temporalType = (data.DataType<? extends Temporal>)dataType;
      if (temporalType instanceof data.DATE)
        dataType.value = LocalDate.now();
      else if (temporalType instanceof data.TIME)
        dataType.value = LocalTime.now();
      else if (temporalType instanceof data.DATETIME)
        dataType.value = LocalDateTime.now();
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<String> UUID = new GenerateOn<String>() {
    @Override
    public void generate(final data.DataType<String> dataType) {
      dataType.value = java.util.UUID.randomUUID().toString();
    }
  };

  public abstract void generate(final data.DataType<T> dataType);
}