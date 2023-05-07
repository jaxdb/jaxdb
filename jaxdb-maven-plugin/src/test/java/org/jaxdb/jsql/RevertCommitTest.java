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

import org.junit.Test;
import org.libj.io.UnsynchronizedStringReader;

public class RevertCommitTest {
  public static <V>void test(final data.Column<V> t, final V v1, final V v2, final V v3) {
    final String name = t.getClass().getSimpleName();
    assertTrue(name, t.isNull());
    assertNull(name, t.get());
    assertFalse(name, t.wasSet());

    assertTrue(name, t.set(v1));
    assertFalse(name, t.set(v1));

    assertTrue(name, t.changed);
    assertFalse(name, t.set(v1));
    assertFalse(name, t.isNull());
    assertTrue(name, t.wasSet());
    assertEquals(name, v1, t.get());

    t.revert();
    assertFalse(name, t.changed);
    assertTrue(name, t.isNull());
    assertNull(name, t.get());
    assertFalse(name, t.wasSet());

    assertTrue(name, t.set(v1));
    assertTrue(name, t.changed);
    assertFalse(name, t.set(v1));
    assertFalse(name, t.isNull());
    assertTrue(name, t.wasSet());
    assertTrue(name, t.wasSet());
    assertEquals(name, v1, t.get());

    t._commitEntity$();

    t.revert();
    assertFalse(name, t.changed);
    assertFalse(name, t.isNull());
    assertTrue(name, t.wasSet());
    assertEquals(name, v1, t.get());

    assertTrue(name, t.set(v2));
    assertFalse(name, t.set(v2));
    if (v3 != null) // BLOB and CLOB are not supported
      assertFalse(name, t.set(v3));

    assertTrue(name, t.changed);
    assertFalse(name, t.set(v2));
    assertFalse(name, t.isNull());
    assertTrue(name, t.wasSet());
    assertEquals(name, v2, t.get());

    t.revert();
    assertFalse(name, t.changed);
    assertFalse(name, t.isNull());
    assertTrue(name, t.wasSet());
    assertEquals(name, v1, t.get());
  }

  @Test
  public void testRevertCommit() {
    test(new data.BIGINT(), 1L, 2L, new Long(2));
    test(new data.BINARY(2), new byte[] {1, 2}, new byte[] {3, 4}, new byte[] {3, 4});
    test(new data.BLOB(), new ByteArrayInputStream(new byte[] {1, 2}), new ByteArrayInputStream(new byte[] {3, 4}), null);
    test(new data.BOOLEAN(), false, true, new Boolean(true));
    test(new data.CHAR(), "one", "two", new String("two"));
    test(new data.CLOB(), new UnsynchronizedStringReader("one"), new UnsynchronizedStringReader("two"), null);
    test(new data.DATE(), LocalDate.MIN, LocalDate.MAX, LocalDate.of(Year.MAX_VALUE, 12, 31));
    test(new data.DATETIME(), LocalDateTime.MIN, LocalDateTime.MAX, LocalDateTime.of(LocalDate.MAX, LocalTime.MAX));
    test(new data.DECIMAL(), BigDecimal.ZERO, BigDecimal.ONE, new BigDecimal("1.0"));
    test(new data.DOUBLE(), 0d, 1d, new Double(1));
    final types.Type t = new types.Type();
    test(t.enumType, types.Type.EnumType.ZERO, types.Type.EnumType.ONE, types.Type.EnumType.ONE);
    test(new data.FLOAT(), 0f, 1f, new Float(1));
    test(new data.INT(), 0, 1, new Integer(1));
    test(new data.SMALLINT(), (short)0, (short)1, new Short((short)1));
    test(new data.TIME(), LocalTime.MIN, LocalTime.of(12, 0), LocalTime.of(12, 0));
    test(new data.TINYINT(), (byte)0, (byte)1, new Byte((byte)1));
  }
}