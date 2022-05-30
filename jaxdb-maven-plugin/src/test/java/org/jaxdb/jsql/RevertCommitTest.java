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
import java.io.StringReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Test;

public class RevertCommitTest {
  public static <V>void test(final data.Column<V> t, final V v1, final V v2) {
    assertTrue(t.isNull());
    assertNull(t.get());
    assertFalse(t.wasSet());

    t.set(v1);
    assertTrue(t.changed);
    assertFalse(t.isNull());
    assertTrue(t.wasSet());
    assertEquals(v1, t.get());

    t.revert();
    assertFalse(t.changed);
    assertTrue(t.isNull());
    assertNull(t.get());
    assertFalse(t.wasSet());

    t.set(v1);
    assertTrue(t.changed);
    assertFalse(t.isNull());
    assertTrue(t.wasSet());
    assertTrue(t.wasSet());
    assertEquals(v1, t.get());

    t._commitEntity$();

    t.revert();
    assertFalse(t.changed);
    assertFalse(t.isNull());
    assertTrue(t.wasSet());
    assertEquals(v1, t.get());

    t.set(v2);
    assertTrue(t.changed);
    assertFalse(t.isNull());
    assertTrue(t.wasSet());
    assertEquals(v2, t.get());

    t.revert();
    assertFalse(t.changed);
    assertFalse(t.isNull());
    assertTrue(t.wasSet());
    assertEquals(v1, t.get());
  }

  @Test
  public void testRevertCommit() {
    test(new data.BIGINT(), 1L, 2L);
    test(new data.BINARY(2), new byte[] {1, 2}, new byte[] {3, 4});
    test(new data.BLOB(), new ByteArrayInputStream(new byte[] {1, 2}), new ByteArrayInputStream(new byte[] {3, 4}));
    test(new data.BOOLEAN(), false, true);
    test(new data.CHAR(), "one", "two");
    test(new data.CLOB(), new StringReader("one"), new StringReader("two"));
    test(new data.DATE(), LocalDate.MIN, LocalDate.MAX);
    test(new data.DATETIME(), LocalDateTime.MIN, LocalDateTime.MAX);
    test(new data.DECIMAL(), BigDecimal.ZERO, BigDecimal.ONE);
    test(new data.DOUBLE(), 0d, 1d);
    final types.Type t = new types.Type();
    test(t.enumType, types.Type.EnumType.ZERO, types.Type.EnumType.ONE);
    test(new data.FLOAT(), 0f, 1f);
    test(new data.INT(), 0, 1);
    test(new data.SMALLINT(), (short)0, (short)1);
    test(new data.TIME(), LocalTime.MIN, LocalTime.MAX);
    test(new data.TINYINT(), (byte)0, (byte)1);
  }
}
