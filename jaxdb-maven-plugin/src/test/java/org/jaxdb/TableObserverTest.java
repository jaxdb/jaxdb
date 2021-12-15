/* Copyright (c) 2017 JAX-DB
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.atomic.AtomicInteger;

import org.jaxdb.jsql.JSqlTest;
import org.jaxdb.jsql.TableObserver;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.data.Column;
import org.jaxdb.jsql.types;
import org.junit.Test;

public class TableObserverTest extends JSqlTest {
  private static final types.Type t = new types.Type();
  private static final AtomicInteger observed = new AtomicInteger(0);

  static {
    t.addObserver(new TableObserver() {
      @Override
      public void beforeSetBoolean(final Column<Boolean> column, final boolean changed, final boolean oldNull, final boolean oldValue, final boolean newNull, final boolean newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetByte(final Column<Byte> column, final boolean changed, final boolean oldNull, final byte oldValue, final boolean newNull, final byte newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetShort(final Column<Short> column, final boolean changed, final boolean oldNull, final short oldValue, final boolean newNull, final short newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetInt(final Column<Integer> column, final boolean changed, final boolean oldNull, final int oldValue, final boolean newNull, final int newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetLong(final Column<Long> column, final boolean changed, final boolean oldNull, final long oldValue, final boolean newNull, final long newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetFloat(final Column<Float> column, final boolean changed, final boolean oldNull, final float oldValue, final boolean newNull, final float newValue) {
        observed.incrementAndGet();
      }

      @Override
      public void beforeSetDouble(final Column<Double> column, final boolean changed, final boolean oldNull, final double oldValue, final boolean newNull, final double newValue) {
        observed.incrementAndGet();
      }

      @Override
      public <V> void beforeSetObject(final Column<V> column, final boolean changed, final V oldValue, final V newValue) {
        observed.incrementAndGet();
      }
    });
  }

  @Test
  public void testBigIntObservers() {
    t.bigintType.set(1);
    assertEquals(1, t.bigintType.getAsLong());
    assertEquals(Long.valueOf(1), t.bigintType.get());
    assertFalse(t.bigintType.isNull());
    assertTrue(t.bigintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.bigintType.setNull();
    assertNull(t.bigintType.get());
    assertTrue(t.bigintType.isNull());
    assertTrue(t.bigintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.bigintType.set(Long.valueOf(1));
    assertEquals(1, t.bigintType.getAsLong());
    assertEquals(Long.valueOf(1), t.bigintType.get());
    assertFalse(t.bigintType.isNull());
    assertTrue(t.bigintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.bigintType.set(data.BIGINT.NULL);
    assertNull(t.bigintType.get());
    assertTrue(t.bigintType.isNull());
    assertTrue(t.bigintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.bigintType.set((Long)null);
    assertNull(t.bigintType.get());
    assertTrue(t.bigintType.isNull());
    assertTrue(t.bigintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.bigintType.set(new data.BIGINT(1L));
    assertFalse(t.bigintType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.bigintType.set((data.BIGINT)null);
    assertFalse(t.bigintType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testBinaryObservers() {
    t.binaryType.setNull();
    assertNull(t.binaryType.get());
    assertTrue(t.binaryType.isNull());
    assertTrue(t.binaryType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.binaryType.set(new byte[0]);
    assertTrue(t.binaryType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.binaryType.set(data.BINARY.NULL);
    assertNull(t.binaryType.get());
    assertTrue(t.binaryType.isNull());
    assertTrue(t.binaryType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.binaryType.set((byte[])null);
    assertNull(t.binaryType.get());
    assertTrue(t.binaryType.isNull());
    assertTrue(t.binaryType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.binaryType.set(new data.BINARY(new byte[1]));
    assertFalse(t.binaryType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.binaryType.set((data.BINARY)null);
    assertFalse(t.bigintType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testBlobObservers() {
    t.blobType.setNull();
    assertNull(t.blobType.get());
    assertTrue(t.blobType.isNull());
    assertTrue(t.blobType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.blobType.set(new ByteArrayInputStream(new byte[0]));
    assertTrue(t.blobType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.blobType.set(data.BLOB.NULL);
    assertNull(t.blobType.get());
    assertTrue(t.blobType.isNull());
    assertTrue(t.blobType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.blobType.set((ByteArrayInputStream)null);
    assertNull(t.blobType.get());
    assertTrue(t.blobType.isNull());
    assertTrue(t.blobType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.blobType.set(new data.BLOB(new ByteArrayInputStream(new byte[0])));
    assertFalse(t.blobType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.blobType.set((data.BLOB)null);
    assertFalse(t.blobType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testBooleanObservers() {
    t.booleanType.set(true);
    assertEquals(true, t.booleanType.getAsBoolean());
    assertEquals(Boolean.TRUE, t.booleanType.get());
    assertFalse(t.booleanType.isNull());
    assertTrue(t.booleanType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.booleanType.setNull();
    assertNull(t.booleanType.get());
    assertTrue(t.booleanType.isNull());
    assertTrue(t.booleanType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.booleanType.set(Boolean.TRUE);
    assertEquals(true, t.booleanType.getAsBoolean());
    assertEquals(Boolean.TRUE, t.booleanType.get());
    assertFalse(t.booleanType.isNull());
    assertTrue(t.booleanType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.booleanType.set(data.BOOLEAN.NULL);
    assertNull(t.booleanType.get());
    assertTrue(t.booleanType.isNull());
    assertTrue(t.booleanType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.booleanType.set((Boolean)null);
    assertNull(t.booleanType.get());
    assertTrue(t.booleanType.isNull());
    assertTrue(t.booleanType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.booleanType.set(new data.BOOLEAN(true));
    assertFalse(t.booleanType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.booleanType.set((data.BOOLEAN)null);
    assertFalse(t.booleanType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testCharObservers() {
    t.charType.setNull();
    assertNull(t.charType.get());
    assertTrue(t.charType.isNull());
    assertTrue(t.charType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.charType.set("");
    assertTrue(t.charType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.charType.set(data.CHAR.NULL);
    assertNull(t.charType.get());
    assertTrue(t.charType.isNull());
    assertTrue(t.charType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.charType.set((String)null);
    assertNull(t.charType.get());
    assertTrue(t.charType.isNull());
    assertTrue(t.charType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.charType.set(new data.CHAR(""));
    assertFalse(t.charType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.charType.set((data.CHAR)null);
    assertFalse(t.charType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testDateTimeObservers() {
    t.datetimeType.setNull();
    assertNull(t.datetimeType.get());
    assertTrue(t.datetimeType.isNull());
    assertTrue(t.datetimeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.datetimeType.set(LocalDateTime.now());
    assertTrue(t.datetimeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.datetimeType.set(data.DATETIME.NULL);
    assertNull(t.datetimeType.get());
    assertTrue(t.datetimeType.isNull());
    assertTrue(t.datetimeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.datetimeType.set((LocalDateTime)null);
    assertNull(t.datetimeType.get());
    assertTrue(t.datetimeType.isNull());
    assertTrue(t.datetimeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.datetimeType.set(new data.DATETIME(LocalDateTime.now()));
    assertFalse(t.datetimeType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.datetimeType.set((data.DATETIME)null);
    assertFalse(t.datetimeType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testDateObservers() {
    t.dateType.setNull();
    assertNull(t.dateType.get());
    assertTrue(t.dateType.isNull());
    assertTrue(t.dateType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.dateType.set(LocalDate.now());
    assertTrue(t.dateType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.dateType.set(data.DATE.NULL);
    assertNull(t.dateType.get());
    assertTrue(t.dateType.isNull());
    assertTrue(t.dateType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.dateType.set((LocalDate)null);
    assertNull(t.dateType.get());
    assertTrue(t.dateType.isNull());
    assertTrue(t.dateType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.dateType.set(new data.DATE(LocalDate.now()));
    assertFalse(t.dateType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.dateType.set((data.DATE)null);
    assertFalse(t.dateType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testDecimalObservers() {
    t.decimalType.setNull();
    assertNull(t.decimalType.get());
    assertTrue(t.decimalType.isNull());
    assertTrue(t.decimalType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.decimalType.set(BigDecimal.ONE);
    assertTrue(t.decimalType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.decimalType.set(data.DECIMAL.NULL);
    assertNull(t.decimalType.get());
    assertTrue(t.decimalType.isNull());
    assertTrue(t.decimalType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.decimalType.set((BigDecimal)null);
    assertNull(t.decimalType.get());
    assertTrue(t.decimalType.isNull());
    assertTrue(t.decimalType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.decimalType.set(new data.DECIMAL(BigDecimal.ONE));
    assertFalse(t.decimalType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.decimalType.set((data.DECIMAL)null);
    assertFalse(t.decimalType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testDoubleObservers() {
    t.doubleType.set(1d);
    assertEquals(1d, t.doubleType.getAsDouble(), 0);
    assertEquals(Double.valueOf(1d), t.doubleType.get());
    assertFalse(t.doubleType.isNull());
    assertTrue(t.doubleType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.doubleType.setNull();
    assertNull(t.doubleType.get());
    assertTrue(t.doubleType.isNull());
    assertTrue(t.doubleType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.doubleType.set(Double.valueOf(1d));
    assertEquals(1d, t.doubleType.getAsDouble(), 0);
    assertEquals(Double.valueOf(1d), t.doubleType.get());
    assertFalse(t.doubleType.isNull());
    assertTrue(t.doubleType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.doubleType.set(data.DOUBLE.NULL);
    assertNull(t.doubleType.get());
    assertTrue(t.doubleType.isNull());
    assertTrue(t.doubleType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.doubleType.set((Double)null);
    assertNull(t.doubleType.get());
    assertTrue(t.doubleType.isNull());
    assertTrue(t.doubleType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.doubleType.set(new data.DOUBLE(1d));
    assertFalse(t.doubleType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.doubleType.set((data.DOUBLE)null);
    assertFalse(t.doubleType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testEnumObservers() {
    t.enumType.setNull();
    assertNull(t.enumType.get());
    assertTrue(t.enumType.isNull());
    assertTrue(t.enumType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.enumType.set(types.Type.EnumType.ONE);
    assertEquals(types.Type.EnumType.ONE, t.enumType.get());
    assertFalse(t.enumType.isNull());
    assertTrue(t.enumType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.enumType.set(data.ENUM.NULL);
    assertNull(t.enumType.get());
    assertTrue(t.enumType.isNull());
    assertTrue(t.enumType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.enumType.set((types.Type.EnumType)null);
    assertNull(t.enumType.get());
    assertTrue(t.enumType.isNull());
    assertTrue(t.enumType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.enumType.set(new data.ENUM<>(types.Type.EnumType.ONE));
    assertFalse(t.enumType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.enumType.set((data.ENUM<types.Type.EnumType>)null);
    assertFalse(t.enumType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testFloatObservers() {
    t.floatType.set(1f);
    assertEquals(1d, t.floatType.getAsFloat(), 0);
    assertEquals(Float.valueOf(1f), t.floatType.get());
    assertFalse(t.floatType.isNull());
    assertTrue(t.floatType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.floatType.setNull();
    assertNull(t.floatType.get());
    assertTrue(t.floatType.isNull());
    assertTrue(t.floatType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.floatType.set(Float.valueOf(1f));
    assertEquals(1d, t.floatType.getAsFloat(), 0);
    assertEquals(Float.valueOf(1f), t.floatType.get());
    assertFalse(t.floatType.isNull());
    assertTrue(t.floatType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.floatType.set(data.FLOAT.NULL);
    assertNull(t.floatType.get());
    assertTrue(t.floatType.isNull());
    assertTrue(t.floatType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.floatType.set((Float)null);
    assertNull(t.floatType.get());
    assertTrue(t.floatType.isNull());
    assertTrue(t.floatType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.floatType.set(new data.FLOAT(1f));
    assertFalse(t.floatType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.floatType.set((data.FLOAT)null);
    assertFalse(t.floatType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testIntObservers() {
    t.intType.set(1);
    assertEquals(1, t.intType.getAsInt());
    assertEquals(Integer.valueOf(1), t.intType.get());
    assertFalse(t.intType.isNull());
    assertTrue(t.intType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.intType.setNull();
    assertNull(t.intType.get());
    assertTrue(t.intType.isNull());
    assertTrue(t.intType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.intType.set(Integer.valueOf(1));
    assertEquals(1d, t.intType.getAsInt(), 0);
    assertEquals(Integer.valueOf(1), t.intType.get());
    assertFalse(t.intType.isNull());
    assertTrue(t.intType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.intType.set(data.INT.NULL);
    assertNull(t.intType.get());
    assertTrue(t.intType.isNull());
    assertTrue(t.intType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.intType.set((Integer)null);
    assertNull(t.intType.get());
    assertTrue(t.intType.isNull());
    assertTrue(t.intType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.intType.set(new data.INT(1));
    assertFalse(t.intType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.intType.set((data.INT)null);
    assertFalse(t.intType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testSmallIntObservers() {
    t.smallintType.set((short)1);
    assertEquals((short)1, t.smallintType.getAsShort());
    assertEquals(Short.valueOf((short)1), t.smallintType.get());
    assertFalse(t.smallintType.isNull());
    assertTrue(t.smallintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.smallintType.setNull();
    assertNull(t.smallintType.get());
    assertTrue(t.smallintType.isNull());
    assertTrue(t.smallintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.smallintType.set(Short.valueOf((short)1));
    assertEquals((short)1, t.smallintType.getAsShort());
    assertEquals(Short.valueOf((short)1), t.smallintType.get());
    assertFalse(t.smallintType.isNull());
    assertTrue(t.smallintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.smallintType.set(data.SMALLINT.NULL);
    assertNull(t.smallintType.get());
    assertTrue(t.smallintType.isNull());
    assertTrue(t.smallintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.smallintType.set((Short)null);
    assertNull(t.smallintType.get());
    assertTrue(t.smallintType.isNull());
    assertTrue(t.smallintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.smallintType.set(new data.SMALLINT((short)1));
    assertFalse(t.smallintType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.smallintType.set((data.SMALLINT)null);
    assertFalse(t.smallintType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testTimeObservers() {
    t.timeType.setNull();
    assertNull(t.timeType.get());
    assertTrue(t.timeType.isNull());
    assertTrue(t.timeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.timeType.set(LocalTime.now());
    assertTrue(t.timeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.timeType.set(data.TIME.NULL);
    assertNull(t.timeType.get());
    assertTrue(t.timeType.isNull());
    assertTrue(t.timeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.timeType.set((LocalTime)null);
    assertNull(t.timeType.get());
    assertTrue(t.timeType.isNull());
    assertTrue(t.timeType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.timeType.set(new data.TIME(LocalTime.now()));
    assertFalse(t.timeType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.timeType.set((data.TIME)null);
    assertFalse(t.timeType.reset());
    assertEquals(0, observed.getAndSet(0));
  }

  @Test
  public void testTinyIntObservers() {
    t.tinyintType.set((byte)1);
    assertEquals((byte)1, t.tinyintType.getAsByte());
    assertEquals(Byte.valueOf((byte)1), t.tinyintType.get());
    assertFalse(t.tinyintType.isNull());
    assertTrue(t.tinyintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.tinyintType.setNull();
    assertNull(t.tinyintType.get());
    assertTrue(t.tinyintType.isNull());
    assertTrue(t.tinyintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.tinyintType.set(Byte.valueOf((byte)1));
    assertEquals((byte)1, t.tinyintType.getAsByte());
    assertEquals(Byte.valueOf((byte)1), t.tinyintType.get());
    assertFalse(t.tinyintType.isNull());
    assertTrue(t.tinyintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.tinyintType.set(data.TINYINT.NULL);
    assertNull(t.tinyintType.get());
    assertTrue(t.tinyintType.isNull());
    assertTrue(t.tinyintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.tinyintType.set((Byte)null);
    assertNull(t.tinyintType.get());
    assertTrue(t.tinyintType.isNull());
    assertTrue(t.tinyintType.reset());
    assertEquals(1, observed.getAndSet(0));

    t.tinyintType.set(new data.TINYINT((byte)1));
    assertFalse(t.tinyintType.reset());
    assertEquals(0, observed.getAndSet(0));

    t.tinyintType.set((data.TINYINT)null);
    assertFalse(t.tinyintType.reset());
    assertEquals(0, observed.getAndSet(0));
  }
}