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

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.ddlx.runner.VendorRunner;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.auto;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class AutoTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(auto.class)
  @VendorRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends AutoTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(auto.class)
  @VendorRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends AutoTest {
  }

  private static final int MIN_SECONDARY = 2;
  private static final int MAX_SECONDARY = 5;

  private static final int MIN_TERTIARY = 0;

  @Test
  public void testCharUuid() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      final auto.CharUuid a = new auto.CharUuid();
      INSERT(a)
        .execute(transaction);
      try (final RowIterator<auto.CharUuid> rows =
        SELECT(a)
          .execute(transaction)) {
        assertTrue(rows.nextRow());
        assertNotNull(rows.nextEntity().primary.get());
      }
    }
  }

  @Test
  public void testTinyintIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.TinyintIncrement a = new auto.TinyintIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testTinyintUnsignedIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.TinyintUnsignedIncrement a = new auto.TinyintUnsignedIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.TinyintUnsignedIncrement> rows =
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

        try (final RowIterator<auto.TinyintUnsignedIncrement> rows =
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
  }

  @Test
  public void testSmallintIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.SmallintIncrement a = new auto.SmallintIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testSmallintUnsignedIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.SmallintUnsignedIncrement a = new auto.SmallintUnsignedIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.SmallintUnsignedIncrement> rows =
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

        try (final RowIterator<auto.SmallintUnsignedIncrement> rows =
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
  }

  @Test
  public void testIntIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.IntIncrement a = new auto.IntIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testIntTimestampSeconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.IntTimestampSeconds a = new auto.IntTimestampSeconds();

      DELETE(a)
        .execute(transaction);

      int ts = (int)(System.currentTimeMillis() / 1000);
      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testIntUnsignedTimestampSeconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.IntUnsignedTimestampSeconds a = new auto.IntUnsignedTimestampSeconds();

      DELETE(a)
        .execute(transaction);

      int ts = (int)(System.currentTimeMillis() / 1000);
      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.IntUnsignedTimestampSeconds> rows =
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

        try (final RowIterator<auto.IntUnsignedTimestampSeconds> rows =
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
  }

  @Test
  public void testIntUnsignedIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.IntUnsignedIncrement a = new auto.IntUnsignedIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.IntUnsignedIncrement> rows =
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

        try (final RowIterator<auto.IntUnsignedIncrement> rows =
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
  }

  @Test
  public void testBigintIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintIncrement a = new auto.BigintIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testBigintUnsignedIncrement() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintUnsignedIncrement a = new auto.BigintUnsignedIncrement();

      DELETE(a)
        .execute(transaction);

      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintUnsignedIncrement> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(MIN_SECONDARY, a.primary.get().intValue());
        assertEquals(MIN_SECONDARY, a.secondary.get().intValue());
        assertEquals(MIN_TERTIARY, a.tertiary.get().intValue());
      }

      for (int i = MIN_SECONDARY + 1; i < MAX_SECONDARY * 2; ++i) {
        final boolean mark = i % 2 == 0;
        a.mark.set(mark);

        UPDATE(a)
          .execute(transaction);

        try (final RowIterator<auto.BigintUnsignedIncrement> rows =
          SELECT(a)
            .execute(transaction)) {

          assertTrue(rows.nextRow());
          a = rows.nextEntity();
          assertFalse(a.primary.isNull());
          assertEquals(mark, a.mark.get());
          assertEquals(((i - MIN_SECONDARY) % (MAX_SECONDARY + 1 - MIN_SECONDARY)) + MIN_SECONDARY, a.secondary.get().intValue());
          assertEquals(i - MIN_SECONDARY + MIN_TERTIARY, a.tertiary.get().intValue());
        }
      }
    }
  }

  @Test
  public void testBigintTimestampSeconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintTimestampSeconds a = new auto.BigintTimestampSeconds();

      DELETE(a)
        .execute(transaction);

      int ts = (int)(System.currentTimeMillis() / 1000);
      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testBigintUnsignedTimestampSeconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintUnsignedTimestampSeconds a = new auto.BigintUnsignedTimestampSeconds();

      DELETE(a)
        .execute(transaction);

      int ts = (int)(System.currentTimeMillis() / 1000);
      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintUnsignedTimestampSeconds> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(ts, a.primary.get().longValue());
        assertEquals(ts, a.primary.get().longValue());
      }

      for (int i = 0; i < 3; ++i) {
        Thread.sleep(1200);

        final boolean mark = i % 2 == 0;
        a.mark.set(mark);

        ts = (int)(System.currentTimeMillis() / 1000);
        UPDATE(a)
          .execute(transaction);

        try (final RowIterator<auto.BigintUnsignedTimestampSeconds> rows =
          SELECT(a)
            .execute(transaction)) {

          assertTrue(rows.nextRow());
          a = rows.nextEntity();
          assertFalse(a.primary.isNull());
          assertEquals(mark, a.mark.get());
          assertEquals(ts, a.secondary.get().longValue());
        }
      }
    }
  }

  @Test
  public void testBigintTimestampMilliseconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintTimestampMilliseconds a = new auto.BigintTimestampMilliseconds();

      DELETE(a)
        .execute(transaction);

      long ts = System.currentTimeMillis();
      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testBigintUnsignedTimestampMilliseconds() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.BigintUnsignedTimestampMilliseconds a = new auto.BigintUnsignedTimestampMilliseconds();

      DELETE(a)
        .execute(transaction);

      long ts = System.currentTimeMillis();
      INSERT(a)
        .execute(transaction);

      try (final RowIterator<auto.BigintUnsignedTimestampMilliseconds> rows =
        SELECT(a)
          .execute(transaction)) {

        assertTrue(rows.nextRow());
        a = rows.nextEntity();
        assertFalse(a.primary.isNull());
        assertEquals(ts, a.primary.get().longValue(), 2);
        assertEquals(ts, a.primary.get().longValue(), 2);
      }

      for (int i = 0; i < 3; ++i) {
        Thread.sleep(12);

        final boolean mark = i % 2 == 0;
        a.mark.set(mark);

        ts = System.currentTimeMillis();
        UPDATE(a)
          .execute(transaction);

        try (final RowIterator<auto.BigintUnsignedTimestampMilliseconds> rows =
          SELECT(a)
            .execute(transaction)) {

          assertTrue(rows.nextRow());
          a = rows.nextEntity();
          assertFalse(a.primary.isNull());
          assertEquals(mark, a.mark.get());
          assertEquals(ts, a.secondary.get().longValue(), 2);
        }
      }
    }
  }

  @Test
  public void testTimeTimestamp() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.TimeTimestamp a = new auto.TimeTimestamp();

      DELETE(a)
        .execute(transaction);

      LocalTime ts = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testDateTimestamp() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.DateTimestamp a = new auto.DateTimestamp();

      DELETE(a)
        .execute(transaction);

      final LocalDate ts = LocalDate.now();
      INSERT(a)
        .execute(transaction);

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
  }

  @Test
  public void testDatetimeTimestamp() throws InterruptedException, IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(auto.class)) {
      auto.DatetimeTimestamp a = new auto.DatetimeTimestamp();

      DELETE(a)
        .execute(transaction);

      LocalDateTime ts = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
      INSERT(a)
        .execute(transaction);

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
}