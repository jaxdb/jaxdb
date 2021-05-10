/* Copyright (c) 2016 JAX-DB
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

import static org.jaxdb.jsql.DML.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import org.jaxdb.jsql.type.DataType;
import org.jaxdb.vendor.DBVendor;
import org.libj.lang.UUIDs;

public interface GenerateOn<T> {
  static final GenerateOn<Number> AUTO_GENERATED = new GenerateOn<Number>() {
    @Override
    public void generate(final DataType<? super Number> dataType, final DBVendor vendor) {
      throw new UnsupportedOperationException();
    }
  };

  public static final GenerateOn<Number> INCREMENT = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final type.DataType<? super Number> dataType, final DBVendor vendor) {
      final type.DataType<? extends Number> numberType = (type.DataType<? extends Number>)dataType;
      if (numberType instanceof type.TINYINT) {
        final type.TINYINT integer = (type.TINYINT)numberType;
        final type.TINYINT a = ADD(integer, (byte)1);
        final byte max = integer.max() != null ? integer.max() : vendor.getDialect().maxTinyint();
        integer.set(integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max));
      }
      else if (numberType instanceof type.SMALLINT) {
        final type.SMALLINT integer = (type.SMALLINT)numberType;
        final type.SMALLINT a = ADD(integer, (short)1);
        final short max = integer.max() != null ? integer.max() : vendor.getDialect().maxSmallint();
        integer.set(integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max));
      }
      else if (numberType instanceof type.INT) {
        final type.INT integer = (type.INT)numberType;
        final type.INT a = ADD(integer, 1);
        final int max = integer.max() != null ? integer.max() : vendor.getDialect().maxInt();
        integer.set(integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max));
      }
      else if (numberType instanceof type.BIGINT) {
        final type.BIGINT integer = (type.BIGINT)numberType;
        final type.BIGINT a = ADD(integer, 1);
        final long max = integer.max() != null ? integer.max() : vendor.getDialect().maxBigint();
        integer.set(integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max));
      }
      // FIXME: Support FLOAT, DOUBLE, and DECIMAL?
//      else if (numberType instanceof type.FLOAT)
//        ((type.FLOAT)numberType).set(ADD((type.FLOAT)numberType, 1f));
//      else if (numberType instanceof type.DOUBLE)
//        ((type.DOUBLE)numberType).set(ADD((type.DOUBLE)numberType, 1f));
//      else if (numberType instanceof type.DECIMAL)
//        ((type.DECIMAL)numberType).set(ADD((type.DECIMAL)numberType, 1f));
      else {
        throw new UnsupportedOperationException("Unsupported type: " + numberType.getClass().getName());
      }
    }
  };

  public static final GenerateOn<Temporal> TIMESTAMP = new GenerateOn<Temporal>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final type.DataType<? super Temporal> dataType, final DBVendor vendor) {
      final type.DataType<? extends Temporal> temporalType = (type.DataType<? extends Temporal>)dataType;
      if (temporalType instanceof type.DATE)
        ((type.DATE)temporalType).value = LocalDate.now();
      else if (temporalType instanceof type.TIME)
        ((type.TIME)temporalType).value = LocalTime.now();
      else if (temporalType instanceof type.DATETIME)
        ((type.DATETIME)temporalType).value = LocalDateTime.now();
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_MINUTES = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final type.DataType<? super Number> dataType, final DBVendor vendor) {
      final type.DataType<? extends Number> numberType = (type.DataType<? extends Number>)dataType;
      final int ts = (int)(System.currentTimeMillis() / 60000);
      if (numberType instanceof type.INT)
        ((type.INT)numberType).setValue(ts);
      else if (numberType instanceof type.BIGINT)
        ((type.BIGINT)numberType).setValue(ts);
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_SECONDS = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final type.DataType<? super Number> dataType, final DBVendor vendor) {
      final type.DataType<? extends Number> numberType = (type.DataType<? extends Number>)dataType;
      final int ts = (int)(System.currentTimeMillis() / 1000);
      if (numberType instanceof type.INT)
        ((type.INT)numberType).setValue(ts);
      else if (numberType instanceof type.BIGINT)
        ((type.BIGINT)numberType).setValue(ts);
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_MILLIS = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final type.DataType<? super Number> dataType, final DBVendor vendor) {
      final type.DataType<? extends Number> numberType = (type.DataType<? extends Number>)dataType;
      final long ts = System.currentTimeMillis();
      if (numberType instanceof type.INT)
        throw new IllegalArgumentException("Signed INT type does not support TIMESTAMP with millisecond precision");

      if (numberType instanceof type.BIGINT)
        ((type.BIGINT)numberType).setValue(ts);
      else
        throw new UnsupportedOperationException("Unsupported type: " + dataType.getClass().getName());
    }
  };

  public static final GenerateOn<String> UUID = new GenerateOn<String>() {
    @Override
    public void generate(final type.DataType<? super String> dataType, final DBVendor vendor) {
      final type.Textual<? super String> textualType = (type.Textual<? super String>)dataType;
      final java.util.UUID uuid = java.util.UUID.randomUUID();
      textualType.value = textualType.length() == 32 ? UUIDs.toString32(uuid) : uuid.toString();
    }
  };

  public void generate(type.DataType<? super T> dataType, DBVendor vendor);
}