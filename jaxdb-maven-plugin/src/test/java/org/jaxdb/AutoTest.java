/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb;

import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.jaxdb.jsql.Auto;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class AutoTest {
  @DB(Derby.class)
  @DB(SQLite.class)
  public static class IntegrationTest extends AutoTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends AutoTest {
  }

  private static final int MIN_SECONDARY = 2;
  private static final int MAX_SECONDARY = 5;
  private static final int MIN_TERTIARY = 0;

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testCharUuid(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    final Auto.CharUuid a = auto.new CharUuid();

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.CharUuid> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertNotNull(rows.nextEntity().primary.get());
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testTinyintIncrement(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    Auto.TinyintIncrement a = auto.new TinyintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    try (final RowIterator<Auto.TinyintIncrement> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.secondary.getAsByte());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsByte());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) { // [N]
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.TinyintIncrement> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsByte());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsByte());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testSmallintIncrement(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    Auto.SmallintIncrement a = auto.new SmallintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    try (final RowIterator<Auto.SmallintIncrement> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.secondary.getAsShort());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsShort());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) { // [N]
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.SmallintIncrement> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsShort());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsShort());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testIntIncrement(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    Auto.IntIncrement a = auto.new IntIncrement();

    DELETE(a)
      .execute(transaction);

    transaction.commit();

    INSERT(a)
      .execute(transaction);

    try (final RowIterator<Auto.IntIncrement> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.secondary.getAsInt());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsInt());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) { // [N]
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.IntIncrement> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsInt());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsInt());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testIntTimestampMinutes(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.IntTimestampMinutes a = auto.new IntTimestampMinutes();

    DELETE(a)
      .execute(transaction);

    int expected = (int)(System.currentTimeMillis() / 60000);

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.IntTimestampMinutes> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.getAsInt());
      assertEquals(expected, a.primary.getAsInt());
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200); // This is moot

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = (int)(System.currentTimeMillis() / 60000);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.IntTimestampMinutes> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(expected, a.secondary.getAsInt());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testIntTimestampSeconds(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.IntTimestampSeconds a = auto.new IntTimestampSeconds();

    DELETE(a)
      .execute(transaction);

    int expected = (int)(System.currentTimeMillis() / 1000);

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.IntTimestampSeconds> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.getAsInt());
      assertEquals(expected, a.primary.getAsInt());
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = (int)(System.currentTimeMillis() / 1000);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.IntTimestampSeconds> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(expected, a.secondary.getAsInt());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testBigintIncrement(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    Auto.BigintIncrement a = auto.new BigintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    try (final RowIterator<Auto.BigintIncrement> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.secondary.getAsLong());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsLong());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) { // [N]
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.BigintIncrement> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsLong());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsLong());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testBigintTimestampMinutes(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.BigintTimestampMinutes a = auto.new BigintTimestampMinutes();

    DELETE(a)
      .execute(transaction);

    int expected = (int)(System.currentTimeMillis() / 60000);

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.BigintTimestampMinutes> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.getAsLong());
      assertEquals(expected, a.primary.getAsLong());
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200); // This is moot

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = (int)(System.currentTimeMillis() / 60000);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.BigintTimestampMinutes> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(expected, a.secondary.getAsLong());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testBigintTimestampSeconds(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.BigintTimestampSeconds a = auto.new BigintTimestampSeconds();

    DELETE(a)
      .execute(transaction);

    int expected = (int)(System.currentTimeMillis() / 1000);

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.BigintTimestampSeconds> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.getAsLong(), 1);
      assertEquals(expected, a.primary.getAsLong(), 1);
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = (int)(System.currentTimeMillis() / 1000);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.BigintTimestampSeconds> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(expected, a.secondary.getAsLong());
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testBigintTimestampMilliseconds(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.BigintTimestampMilliseconds a = auto.new BigintTimestampMilliseconds();

    DELETE(a)
      .execute(transaction);

    long expected = System.currentTimeMillis();
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.BigintTimestampMilliseconds> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.getAsLong(), 2);
      assertEquals(expected, a.primary.getAsLong(), 2);
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(12);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = System.currentTimeMillis();

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.BigintTimestampMilliseconds> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(expected, a.secondary.getAsLong(), 2);
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testTimeTimestamp(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.TimeTimestamp a = auto.new TimeTimestamp();

    DELETE(a)
      .execute(transaction);

    LocalTime expected = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.TimeTimestamp> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertTrue(ChronoUnit.SECONDS.between(expected, a.primary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
      assertTrue(ChronoUnit.SECONDS.between(expected, a.primary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.TimeTimestamp> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertTrue(ChronoUnit.SECONDS.between(expected, a.secondary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
      }
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testDateTimestamp(final Auto auto, final Transaction transaction) throws IOException, SQLException {
    Auto.DateTimestamp a = auto.new DateTimestamp();

    DELETE(a)
      .execute(transaction);

    final LocalDate expected = LocalDate.now();
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.DateTimestamp> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(expected, a.primary.get());
      assertEquals(expected, a.primary.get());
    }

    final boolean mark = true;
    a.mark.set(mark);

    UPDATE(a)
      .execute(transaction);

    try (final RowIterator<Auto.DateTimestamp> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertEquals(mark, a.mark.get());
      assertEquals(expected, a.secondary.get());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testDatetimeTimestamp(final Auto auto, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    Auto.DatetimeTimestamp a = auto.new DatetimeTimestamp();

    DELETE(a)
      .execute(transaction);

    LocalDateTime expected = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<Auto.DatetimeTimestamp> rows =

      SELECT(a).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertFalse(a.primary.isNull());
      assertTrue(ChronoUnit.SECONDS.between(expected, a.primary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
      assertTrue(ChronoUnit.SECONDS.between(expected, a.primary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
    }

    for (int i = 0; i < 3; ++i) { // [N]
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      expected = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<Auto.DatetimeTimestamp> rows =

        SELECT(a).
        LIMIT(1)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(rows.nextRow());

        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertTrue(ChronoUnit.SECONDS.between(expected, a.secondary.get().truncatedTo(ChronoUnit.SECONDS)) <= 1);
      }
    }
  }
}