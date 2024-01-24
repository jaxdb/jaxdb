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

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.Temporal;

import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.UUIDs;

public interface GenerateOn<T> extends Serializable {
  static final GenerateOn<Number> AUTO_GENERATED = new GenerateOn<Number>() {
    @Override
    public void generate(final data.Column<? super Number> column, final DbVendor vendor) {
      throw new UnsupportedOperationException();
    }
  };

  public static final GenerateOn<Number> INCREMENT = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final data.Column<? super Number> column, final DbVendor vendor) {
      final data.Column<? extends Number> numberType = (data.Column<? extends Number>)column;
      if (numberType instanceof data.TINYINT) {
        final data.TINYINT integer = (data.TINYINT)numberType;
        final exp.TINYINT a = ADD(integer, (byte)1);
        final byte max = integer.max() != null ? integer.max() : vendor.getDialect().maxTinyint();
        integer.ref = integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max);
      }
      else if (numberType instanceof data.SMALLINT) {
        final data.SMALLINT integer = (data.SMALLINT)numberType;
        final exp.SMALLINT a = ADD(integer, (short)1);
        final short max = integer.max() != null ? integer.max() : vendor.getDialect().maxSmallint();
        integer.ref = integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max);
      }
      else if (numberType instanceof data.INT) {
        final data.INT integer = (data.INT)numberType;
        final exp.INT a = ADD(integer, 1);
        final int max = integer.max() != null ? integer.max() : vendor.getDialect().maxInt();
        integer.ref = integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max);
      }
      else if (numberType instanceof data.BIGINT) {
        final data.BIGINT integer = (data.BIGINT)numberType;
        final exp.BIGINT a = ADD(integer, 1);
        final long max = integer.max() != null ? integer.max() : vendor.getDialect().maxBigint();
        integer.ref = integer.min() != null ? ADD(MOD(SUB(a, integer.min()), max - integer.min() + 1), integer.min()) : MOD(a, max);
      }
      // FIXME: Support FLOAT, DOUBLE, and DECIMAL?
      // else if (numberType instanceof type.FLOAT)
      // ((type.FLOAT)numberType).ref = ADD((type.FLOAT)numberType, 1f);
      // else if (numberType instanceof type.DOUBLE)
      // ((type.DOUBLE)numberType).ref = ADD((type.DOUBLE)numberType, 1f);
      // else if (numberType instanceof type.DECIMAL)
      // ((type.DECIMAL)numberType).ref = ADD((type.DECIMAL)numberType, 1f);
      else {
        throw new UnsupportedOperationException("Unsupported type: " + numberType.getClass().getName());
      }
    }
  };

  public static final GenerateOn<Temporal> TIMESTAMP = new GenerateOn<Temporal>() {
    @Override
    @SuppressWarnings("rawtypes")
    public void generate(final data.Column column, final DbVendor vendor) {
      if (column instanceof data.DATE)
        ((data.DATE)column).set(LocalDate.now(), SetBy.SYSTEM);
      else if (column instanceof data.TIME)
        ((data.TIME)column).set(LocalTime.now(), SetBy.SYSTEM);
      else if (column instanceof data.DATETIME)
        ((data.DATETIME)column).set(LocalDateTime.now(), SetBy.SYSTEM);
      else
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_MINUTES = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final data.Column<? super Number> column, final DbVendor vendor) {
      final data.Column<? extends Number> numberType = (data.Column<? extends Number>)column;
      final int ts = (int)(System.currentTimeMillis() / 60000);
      if (numberType instanceof data.INT)
        ((data.INT)numberType).setValue(ts);
      else if (numberType instanceof data.BIGINT)
        ((data.BIGINT)numberType).setValue(ts, null);
      else
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_SECONDS = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final data.Column<? super Number> column, final DbVendor vendor) {
      final data.Column<? extends Number> numberType = (data.Column<? extends Number>)column;
      final int ts = (int)(System.currentTimeMillis() / 1000);
      if (numberType instanceof data.INT)
        ((data.INT)numberType).setValue(ts);
      else if (numberType instanceof data.BIGINT)
        ((data.BIGINT)numberType).setValue(ts, null);
      else
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
    }
  };

  public static final GenerateOn<Number> EPOCH_MILLIS = new GenerateOn<Number>() {
    @Override
    @SuppressWarnings("unchecked")
    public void generate(final data.Column<? super Number> column, final DbVendor vendor) {
      final data.Column<? extends Number> numberType = (data.Column<? extends Number>)column;
      final long ts = System.currentTimeMillis();
      if (numberType instanceof data.INT)
        throw new IllegalArgumentException("Signed INT type does not support TIMESTAMP with millisecond precision");

      if (numberType instanceof data.BIGINT)
        ((data.BIGINT)numberType).setValue(ts);
      else
        throw new UnsupportedOperationException("Unsupported type: " + column.getClass().getName());
    }
  };

  public static final GenerateOn<String> UUID = new GenerateOn<String>() {
    @Override
    public void generate(final data.Column<? super String> column, final DbVendor vendor) {
      final data.Textual<? super String> textualType = (data.Textual<? super String>)column;
      final java.util.UUID uuid = java.util.UUID.randomUUID();
      textualType.set(textualType.length() == 32 ? UUIDs.toString32(uuid) : uuid.toString(), SetBy.SYSTEM);
    }
  };

  public void generate(data.Column<? super T> column, DbVendor vendor);
}