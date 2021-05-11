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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.auto;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.VendorRunner;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.runner.VendorSchemaRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
public abstract class AutoTest {
  @VendorRunner.Vendor(Derby.class)
  @VendorRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends AutoTest {
  }

  @VendorRunner.Vendor(MySQL.class)
  @VendorRunner.Vendor(PostgreSQL.class)
  @VendorRunner.Vendor(Oracle.class)
  public static class RegressionTest extends AutoTest {
  }

  private static final int MIN_SECONDARY = 2;
  private static final int MAX_SECONDARY = 5;
  private static final int MIN_TERTIARY = 0;

  @Test
  public void testCharUuid(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    final auto.CharUuid a = new auto.CharUuid();

    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.CharUuid> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertNotNull(rows.nextEntity().primary.get());
    }
  }

  @Test
  public void testTinyintIncrement(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    auto.TinyintIncrement a = new auto.TinyintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    assertEquals(MIN_SECONDARY, a.primary.getAsByte());

    try (final RowIterator<auto.TinyintIncrement> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.primary.getAsByte());
      assertEquals(MIN_SECONDARY, a.secondary.getAsByte());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsByte());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) {
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.TinyintIncrement> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsByte());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsByte());
      }
    }
  }

  @Test
  public void testSmallintIncrement(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    auto.SmallintIncrement a = new auto.SmallintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    assertEquals(MIN_SECONDARY, a.primary.getAsShort());

    try (final RowIterator<auto.SmallintIncrement> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.primary.getAsShort());
      assertEquals(MIN_SECONDARY, a.secondary.getAsShort());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsShort());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) {
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.SmallintIncrement> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsShort());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsShort());
      }
    }
  }

  @Test
  public void testIntIncrement(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    auto.IntIncrement a = new auto.IntIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    assertEquals(MIN_SECONDARY, a.primary.getAsInt());

    try (final RowIterator<auto.IntIncrement> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.primary.getAsInt());
      assertEquals(MIN_SECONDARY, a.secondary.getAsInt());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsInt());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) {
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.IntIncrement> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsInt());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsInt());
      }
    }
  }

  @Test
  public void testIntTimestampMinutes(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.IntTimestampMinutes a = new auto.IntTimestampMinutes();

    DELETE(a)
      .execute(transaction);

    int ts = (int)(System.currentTimeMillis() / 60000);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.IntTimestampMinutes> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.getAsInt());
      assertEquals(ts, a.primary.getAsInt());
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200); // This is moot

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = (int)(System.currentTimeMillis() / 60000);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.IntTimestampMinutes> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.getAsInt());
      }
    }
  }

  @Test
  public void testIntTimestampSeconds(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.IntTimestampSeconds a = new auto.IntTimestampSeconds();

    DELETE(a)
      .execute(transaction);

    int ts = (int)(System.currentTimeMillis() / 1000);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.IntTimestampSeconds> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.getAsInt());
      assertEquals(ts, a.primary.getAsInt());
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = (int)(System.currentTimeMillis() / 1000);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.IntTimestampSeconds> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.getAsInt());
      }
    }
  }

  @Test
  public void testBigintIncrement(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    auto.BigintIncrement a = new auto.BigintIncrement();

    DELETE(a)
      .execute(transaction);

    INSERT(a)
      .execute(transaction);

    assertEquals(MIN_SECONDARY, a.primary.get().intValue());

    try (final RowIterator<auto.BigintIncrement> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(MIN_SECONDARY, a.primary.getAsLong());
      assertEquals(MIN_SECONDARY, a.secondary.getAsLong());
      assertEquals(MIN_TERTIARY, a.tertiary.getAsLong());
    }

    for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) {
      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintIncrement> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.getAsLong());
        assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.getAsLong());
      }
    }
  }

  @Test
  public void testBigintTimestampMinutes(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.BigintTimestampMinutes a = new auto.BigintTimestampMinutes();

    DELETE(a)
      .execute(transaction);

    int ts = (int)(System.currentTimeMillis() / 60000);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.BigintTimestampMinutes> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.getAsLong());
      assertEquals(ts, a.primary.getAsLong());
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200); // This is moot

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = (int)(System.currentTimeMillis() / 60000);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintTimestampMinutes> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.getAsLong());
      }
    }
  }

  @Test
  public void testBigintTimestampSeconds(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.BigintTimestampSeconds a = new auto.BigintTimestampSeconds();

    DELETE(a)
      .execute(transaction);

    int ts = (int)(System.currentTimeMillis() / 1000);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.BigintTimestampSeconds> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.getAsLong());
      assertEquals(ts, a.primary.getAsLong());
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = (int)(System.currentTimeMillis() / 1000);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintTimestampSeconds> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.getAsLong());
      }
    }
  }

  @Test
  public void testBigintTimestampMilliseconds(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.BigintTimestampMilliseconds a = new auto.BigintTimestampMilliseconds();

    DELETE(a)
      .execute(transaction);

    long ts = System.currentTimeMillis();
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.BigintTimestampMilliseconds> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.getAsLong(), 2);
      assertEquals(ts, a.primary.getAsLong(), 2);
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(12);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = System.currentTimeMillis();
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintTimestampMilliseconds> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.getAsLong(), 2);
      }
    }
  }

  @Test
  public void testTimeTimestamp(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.TimeTimestamp a = new auto.TimeTimestamp();

    DELETE(a)
      .execute(transaction);

    LocalTime ts = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.TimeTimestamp> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.get().truncatedTo(ChronoUnit.SECONDS));
      assertEquals(ts, a.primary.get().truncatedTo(ChronoUnit.SECONDS));
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.TimeTimestamp> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.get().truncatedTo(ChronoUnit.SECONDS));
      }
    }
  }

  @Test
  public void testDateTimestamp(@Schema(auto.class) final Transaction transaction) throws IOException, SQLException {
    auto.DateTimestamp a = new auto.DateTimestamp();

    DELETE(a)
      .execute(transaction);

    final LocalDate ts = LocalDate.now();
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.DateTimestamp> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.get());
      assertEquals(ts, a.primary.get());
    }

    final boolean mark = true;
    a.mark.set(mark);

    UPDATE(a)
      .execute(transaction);

    try (final RowIterator<auto.DateTimestamp> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(mark, a.mark.get());
      assertEquals(ts, a.secondary.get());
    }
  }

  @Test
  public void testDatetimeTimestamp(@Schema(auto.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    auto.DatetimeTimestamp a = new auto.DatetimeTimestamp();

    DELETE(a)
      .execute(transaction);

    LocalDateTime ts = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
    INSERT(a)
      .execute(transaction);

    assertFalse(a.primary.isNull());

    try (final RowIterator<auto.DatetimeTimestamp> rows =
      SELECT(a)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      a = rows.nextEntity();
      assertFalse(a.primary.isNull());
      assertEquals(ts, a.primary.get().truncatedTo(ChronoUnit.SECONDS));
      assertEquals(ts, a.primary.get().truncatedTo(ChronoUnit.SECONDS));
    }

    for (int i = 0; i < 3; ++i) {
      Thread.sleep(1200);

      final boolean mark = i % 2 == 0;
      a.mark.set(mark);

      ts = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      UPDATE(a)
        .execute(transaction);

      try (final RowIterator<auto.DatetimeTimestamp> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(mark, a.mark.get());
        assertEquals(ts, a.secondary.get().truncatedTo(ChronoUnit.SECONDS));
      }
    }
  }
}