/* Copyright (c) 2023 JAX-DB
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

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.math.BigDecimal;
import java.nio.channels.UnsupportedAddressTypeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.function.Supplier;

import org.jaxdb.jsql.data.Column;
import org.junit.Test;
import org.libj.lang.ObjectUtil;

public class KeyTest {
  private static class MockTable extends data.Table {
    MockTable() {
      super(true, false, new Column[1], new Column[1], new Column[0], new Column[0]);
    }

    @Override
    protected void toString(final boolean wasSetOnly, final StringBuilder s) {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    data.Table singleton() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    data.Table newInstance() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    public int hashCode() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    Schema getSchema() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    public String getName() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    public boolean equals(final Object obj) {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    data.Table clone(final boolean _mutable$) {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    public data.Table clone() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    void _merge$(final data.Table table) {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    String[] _columnName$() {
      throw new UnsupportedAddressTypeException();
    }

    @Override
    byte[] _columnIndex$() {
      throw new UnsupportedAddressTypeException();
    }
  };

  private static <V>void test(final data.Table t, final data.Column<V> c, final V v, final Runnable setNull1, final Runnable setNull2, final Runnable setNull3, final Runnable set1, final Runnable set2, final Runnable set3, final Runnable set4, final Runnable set5, final Runnable set6) {
    assertNull(t._primaryKeyImmutable$);
    assertNull(t._primaryKeyOldImmutable$);

    t._column$[0] = c;
    t._primary$[0] = c;

    assertEquals(c, t.getKey().column(0));
    assertEquals(c, t.getKeyOld().column(0));
    assertEquals(c.get(), t.getKey().value(0));
    assertEquals(c.getOld(), t.getKeyOld().value(0));

    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertFalse(c.set());
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    assertFalse(c.setIfNotNull(null));
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertFalse(c.set());
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    assertFalse(c.setIfNotEqual(null));
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertFalse(c.set());
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    assertFalse(c.setIfNotNullOrEqual(null));
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertFalse(c.set());
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    setNull1.run();
    assertFalse(c.changed);
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertTrue(c.set());
    assertEquals(data.Column.SetBy.USER, c.setByCur);
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    setNull2.run();
    assertFalse(c.changed);
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertTrue(c.set());
    assertEquals(data.Column.SetBy.USER, c.setByCur);
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    setNull3.run();
    assertFalse(c.changed);
    assertTrue(c.isNull());
    assertTrue(c.isNullOld());
    assertTrue(c.set());
    assertEquals(data.Column.SetBy.USER, c.setByCur);
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    for (int i = 0; i < 10; ++i) {
      final boolean isFirst = i == 0;
      test(t, c, v, isFirst, set1);
      test(t, c, v, isFirst, set2);
      test(t, c, v, isFirst, set3);
      test(t, c, v, isFirst, set4);
      test(t, c, v, isFirst, set5);
      test(t, c, v, isFirst, set6);

      c._commitEntity$();
      assertNotNull(t._primaryKeyImmutable$);
      assertNull(t._primaryKeyOldImmutable$);

      assertEquals(c, t.getKey().column(0));
      assertEquals(c, t.getKeyOld().column(0));
      assertEquals(c.get(), t.getKey().value(0));
      assertEquals(c.getOld(), t.getKeyOld().value(0));
    }

    assertEquals(data.Column.SetBy.USER, c.setByCur);
    assertEquals(data.Column.SetBy.USER, c.setByOld);
  }

  private static <V>void test(final data.Table t, final data.Column<V> c, final V v, final boolean isFirst, final Runnable set) {
    if (set == null)
      return;

    if (c.setNull()) {
      assertEquals(c, t.getKey().column(0));
      assertEquals(c, t.getKeyOld().column(0));
      assertEquals(c.get(), t.getKey().value(0));
      assertEquals(c.getOld(), t.getKeyOld().value(0));
    }

    c.reset();
    assertTrue(c.isNull());

    if (isFirst)
      assertTrue(c.isNullOld());
    else
      assertTrue(ObjectUtil.equals(v, c.getOld()));

    assertFalse(c.set());
    assertNull(c.setByCur);
    assertNotNull(t._primaryKeyImmutable$);
    assertNotNull(t._primaryKeyOldImmutable$);

    for (int i = 0; i < 10; ++i) {
      set.run();
      assertFalse(c.isNull());

      if (isFirst)
        assertTrue(c.isNullOld());
      else
        assertTrue(ObjectUtil.equals(v, c.getOld()));

      assertTrue(c.set());
      assertEquals(data.Column.SetBy.USER, c.setByCur);
      assertEquals(i == 0, t._primaryKeyImmutable$ == null);
      assertEquals(i == 0 && isFirst, t._primaryKeyOldImmutable$ == null); // Because SetByOld is null
      assertEquals(c, t.getKey().column(0));
      assertEquals(c, t.getKeyOld().column(0));
      assertEquals(c.get(), t.getKey().value(0));
      assertEquals(c.getOld(), t.getKeyOld().value(0));
      assertNotNull(t._primaryKeyImmutable$);
      assertNotNull(t._primaryKeyOldImmutable$);

      c.setByCur = null;
      assertFalse(c.setIfNotEqual(v));
      assertTrue(ObjectUtil.equals(v, c.get()));

      assertNull(c.setByCur);
      assertFalse(c.set());
      assertNotNull(t._primaryKeyImmutable$);
      assertNotNull(t._primaryKeyOldImmutable$);

      assertFalse(c.setIfNotNullOrEqual(v));
      assertTrue(ObjectUtil.equals(v, c.get()));
      assertNull(c.setByCur);
      assertFalse(c.set());
      assertNotNull(t._primaryKeyImmutable$);
      assertNotNull(t._primaryKeyOldImmutable$);
      c.setByCur = data.Column.SetBy.USER;
    }
  }

  @Test
  public void testBIGINT() {
    final data.Table t = new MockTable();
    final data.BIGINT c = new data.BIGINT(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null, null);
    test(t, c, 1L, () -> c.set(data.BIGINT.NULL), () -> c.set((Long)null), () -> c.set((data.BIGINT)null), () -> c.set(1), () -> c.set((Long)1L), () -> c.setIfNotEqual(1), () -> c.setIfNotEqual((Long)1L), () -> c.setIfNotNull(1L), () -> c.setIfNotNullOrEqual(1L));
  }

  @Test
  public void testBINARY() {
    final data.Table t = new MockTable();
    final data.BINARY c = new data.BINARY(t, true, "test", data.BTREE, false, null, true, null, null, null, 1, false);
    test(t, c, new byte[] {1}, () -> c.set(data.BINARY.NULL), () -> c.set((byte[])null), () -> c.set((data.BINARY)null), null, () -> c.set(new byte[] {1}), null, () -> c.setIfNotEqual(new byte[] {1}), () -> c.setIfNotNull(new byte[] {1}), () -> c.setIfNotNullOrEqual(new byte[] {1}));
  }

  @Test
  public void testCHAR() {
    final data.Table t = new MockTable();
    final data.CHAR c = new data.CHAR(t, true, "test", data.BTREE, false, null, true, null, null, null, 1, false);
    test(t, c, "1", () -> c.set(data.CHAR.NULL), () -> c.set((String)null), () -> c.set((data.CHAR)null), null, () -> c.set("1"), null, () -> c.setIfNotEqual("1"), () -> c.setIfNotNull("1"), () -> c.setIfNotNullOrEqual("1"));
  }

  @Test
  public void testBLOB() {
    final InputStream v = new ByteArrayInputStream(new byte[] {1});
    final data.Table t = new MockTable();
    final data.BLOB c = new data.BLOB(t, true, "test", data.BTREE, false, null, true, null, null, null, 1L);
    test(t, c, v, () -> c.set(data.BLOB.NULL), () -> c.set((InputStream)null), () -> c.set((data.BLOB)null), null, () -> c.set(v), null, () -> c.setIfNotEqual(v), () -> c.setIfNotNull(v), () -> c.setIfNotNullOrEqual(v));
  }

  @Test
  public void testCLOB() {
    final Reader v = new StringReader("1");
    final data.Table t = new MockTable();
    final data.CLOB c = new data.CLOB(t, true, "test", data.BTREE, false, null, true, null, null, null, 1L);
    test(t, c, v, () -> c.set(data.CLOB.NULL), () -> c.set((Reader)null), () -> c.set((data.CLOB)null), null, () -> c.set(v), null, () -> c.setIfNotEqual(v), () -> c.setIfNotNull(v), () -> c.setIfNotNullOrEqual(v));
  }

  @Test
  public void testDATE() {
    final Supplier<LocalDate> s = () -> LocalDate.parse("2018-12-27");
    final data.Table t = new MockTable();
    final data.DATE c = new data.DATE(t, true, "test", data.BTREE, false, null, true, null, null, null);
    test(t, c, s.get(), () -> c.set(data.DATE.NULL), () -> c.set((LocalDate)null), () -> c.set((data.DATE)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testDECIMAL() {
    final Supplier<BigDecimal> s = () -> new BigDecimal("123.456789");
    final data.Table t = new MockTable();
    final data.DECIMAL c = new data.DECIMAL(t, true, "test", data.BTREE, false, null, true, null, null, null, 23, 20, null, null);
    test(t, c, s.get(), () -> c.set(data.DECIMAL.NULL), () -> c.set((BigDecimal)null), () -> c.set((data.DECIMAL)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testDOUBLE() {
    final Supplier<Double> s = () -> new Double("123.456789");
    final data.Table t = new MockTable();
    final data.DOUBLE c = new data.DOUBLE(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null);
    test(t, c, s.get(), () -> c.set(data.DOUBLE.NULL), () -> c.set((Double)null), () -> c.set((data.DOUBLE)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testDATETIME() {
    final Supplier<LocalDateTime> s = () -> LocalDateTime.parse("2018-12-30T19:34:50.63");
    final data.Table t = new MockTable();
    final data.DATETIME c = new data.DATETIME(t, true, "test", data.BTREE, false, null, true, null, null, null, 0);
    test(t, c, s.get(), () -> c.set(data.DATETIME.NULL), () -> c.set((LocalDateTime)null), () -> c.set((data.DATETIME)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testENUM() {
    final Types.Type.EnumType v = Types.Type.EnumType.EIGHT;
    final data.Table t = new MockTable();
    final data.ENUM<Types.Type.EnumType> c = new data.ENUM<>(t, true, "test", data.BTREE, false, null, true, null, null, null, Types.Type.EnumType.values(), Types.Type.EnumType::valueOf);
    test(t, c, v, () -> c.set(data.ENUM.NULL), () -> c.set((Types.Type.EnumType)null), () -> c.set((data.ENUM<Types.Type.EnumType>)null), null, () -> c.set(v), null, () -> c.setIfNotEqual(v), () -> c.setIfNotNull(v), () -> c.setIfNotNullOrEqual(v));
  }

  @Test
  public void testFLOAT() {
    final Supplier<Float> s = () -> new Float("123.456");
    final data.Table t = new MockTable();
    final data.FLOAT c = new data.FLOAT(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null);
    test(t, c, s.get(), () -> c.set(data.FLOAT.NULL), () -> c.set((Float)null), () -> c.set((data.FLOAT)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testINT() {
    final data.Table t = new MockTable();
    final data.INT c = new data.INT(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null, null);
    test(t, c, 1, () -> c.set(data.INT.NULL), () -> c.set((Integer)null), () -> c.set((data.INT)null), () -> c.set(1), () -> c.set((Integer)1), () -> c.setIfNotEqual(1), () -> c.setIfNotEqual((Integer)1), () -> c.setIfNotNull(1), () -> c.setIfNotNullOrEqual(1));
  }

  @Test
  public void testSMALLINT() {
    final data.Table t = new MockTable();
    final data.SMALLINT c = new data.SMALLINT(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null, null);
    test(t, c, (short)1, () -> c.set(data.SMALLINT.NULL), () -> c.set((Short)null), () -> c.set((data.SMALLINT)null), () -> c.set((short)1), () -> c.set((Short)(short)1), () -> c.setIfNotEqual((short)1), () -> c.setIfNotEqual((Short)(short)1), () -> c.setIfNotNull((short)1), () -> c.setIfNotNullOrEqual((short)1));
  }

  @Test
  public void testTIME() {
    final Supplier<LocalTime> s = () -> LocalTime.parse("19:34:50.63");
    final data.Table t = new MockTable();
    final data.TIME c = new data.TIME(t, true, "test", data.BTREE, false, null, true, null, null, null, 0);
    test(t, c, s.get(), () -> c.set(data.TIME.NULL), () -> c.set((LocalTime)null), () -> c.set((data.TIME)null), null, () -> c.set(s.get()), null, () -> c.setIfNotEqual(s.get()), () -> c.setIfNotNull(s.get()), () -> c.setIfNotNullOrEqual(s.get()));
  }

  @Test
  public void testTINYINT() {
    final data.Table t = new MockTable();
    final data.TINYINT c = new data.TINYINT(t, true, "test", data.BTREE, false, null, true, null, null, null, null, null, null);
    test(t, c, (byte)1, () -> c.set(data.TINYINT.NULL), () -> c.set((Byte)null), () -> c.set((data.TINYINT)null), () -> c.set((byte)1), () -> c.set((Byte)(byte)1), () -> c.setIfNotEqual((byte)1), () -> c.setIfNotEqual((Byte)(byte)1), () -> c.setIfNotNull((byte)1), () -> c.setIfNotNullOrEqual((byte)1));
  }
}