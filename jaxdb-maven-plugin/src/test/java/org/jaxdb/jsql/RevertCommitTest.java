/* Copyright (c) 2022 JAX-DB
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Year;
import java.util.concurrent.atomic.AtomicReference;

import org.jaxdb.jsql.Types.$AbstractType.EnumType;
import org.junit.Test;
import org.libj.io.UnsynchronizedStringReader;

public class RevertCommitTest {
  private static final String CUR = "cur";
  private static final String OLD = "old";
  private static final String UPD = "upd";

  public <V> void test(final data.Column<V> c, final V v1, final V v2, final V v3) {
    final String name = c.getClass().getSimpleName();
    assertTrue(name, c.isNull());
    assertNull(name, c.get());
    assertFalse(name, c.cued());

    assertTrue(name, c.set(v1));
    assertEquals(CUR, callback.toString());
    assertFalse(name, c.set(v1));
    assertEquals(null, callback.toString());

    assertTrue(name, c.changed);
    assertFalse(name, c.set(v1));
    assertEquals(null, callback.toString());
    assertFalse(name, c.isNull());
    assertTrue(name, c.cued());
    assertEquals(name, v1, c.get());

    c.revert();
    assertEquals(CUR, callback.toString());
    assertFalse(name, c.changed);
    assertTrue(name, c.isNull());
    assertNull(name, c.get());
    assertFalse(name, c.cued());

    assertTrue(name, c.set(v1));
    assertEquals(CUR, callback.toString());
    assertTrue(name, c.changed);
    assertFalse(name, c.set(v1));
    assertEquals(null, callback.toString());
    assertFalse(name, c.isNull());
    assertTrue(name, c.cued());
    assertTrue(name, c.cued());
    assertEquals(name, v1, c.get());

    c._commitEntity$();
    assertEquals(OLD, callback.toString());

    c.revert();
    assertEquals(CUR, callback.toString());
    assertFalse(name, c.changed);
    assertFalse(name, c.isNull());
    assertTrue(name, c.cued());
    assertEquals(name, v1, c.get());

    assertTrue(name, c.set(v2));
    assertEquals(CUR, callback.toString());
    assertFalse(name, c.set(v2));
    assertEquals(null, callback.toString());
    if (v3 != null) { // BLOB and CLOB are not supported
      assertFalse(name, c.set(v3));
      assertEquals(null, callback.toString());
    }

    assertTrue(name, c.changed);
    assertFalse(name, c.set(v2));
    assertEquals(null, callback.toString());
    assertFalse(name, c.isNull());
    assertTrue(name, c.cued());
    assertEquals(name, v2, c.get());

    c.revert();
    assertFalse(name, c.changed);
    assertFalse(name, c.isNull());
    assertTrue(name, c.cued());
    assertEquals(name, v1, c.get());
  }

  private final AtomicReference<String> callback = new AtomicReference<String>() {
    @Override
    public String toString() {
      return getAndSet(null);
    }
  };

  private final OnModify<data.Table> onModify = new OnModify<data.Table>() {
    @Override
    public void changeCur(final data.Table table) {
      callback.set(CUR);
    }

    @Override
    public void changeOld(final data.Table table) {
      callback.set(OLD);
    }

    @Override
    public void update(final data.Table table) {
      callback.set(UPD);
    }
  };

  @Test
  public void testRevertCommit() {
    test(new data.BIGINT(onModify), 1L, 2L, new Long(2));
    test(new data.BINARY(2, onModify), new byte[] {1, 2}, new byte[] {3, 4}, new byte[] {3, 4});
    test(new data.BLOB(onModify), new ByteArrayInputStream(new byte[] {1, 2}), new ByteArrayInputStream(new byte[] {3, 4}), null);
    test(new data.BOOLEAN(onModify), false, true, new Boolean(true));
    test(new data.CHAR(onModify), "one", "two", new String("two"));
    test(new data.CLOB(onModify), new UnsynchronizedStringReader("one"), new UnsynchronizedStringReader("two"), null);
    test(new data.DATE(onModify), LocalDate.MIN, LocalDate.MAX, LocalDate.of(Year.MAX_VALUE, 12, 31));
    test(new data.DATETIME(onModify), LocalDateTime.MIN, LocalDateTime.MAX, LocalDateTime.of(LocalDate.MAX, LocalTime.MAX));
    test(new data.DECIMAL(onModify), BigDecimal.ZERO, BigDecimal.ONE, new BigDecimal("1.0"));
    test(new data.DOUBLE(onModify), 0d, 1d, new Double(1));
    test(new data.ENUM<>(EnumType.class, onModify), EnumType.ZERO, EnumType.ONE, EnumType.ONE);
    test(new data.FLOAT(onModify), 0f, 1f, new Float(1));
    test(new data.INT(onModify), 0, 1, new Integer(1));
    test(new data.SMALLINT(onModify), (short)0, (short)1, new Short((short)1));
    test(new data.TIME(onModify), LocalTime.MIN, LocalTime.of(12, 0), LocalTime.of(12, 0));
    test(new data.TINYINT(onModify), (byte)0, (byte)1, new Byte((byte)1));
  }
}