/* Copyright (c) 2017 OpenJAX
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

package org.openjax.maven.plugin.rdb.jsql;

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.Condition;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.rdb.jsql.Interval;
import org.openjax.rdb.jsql.Interval.Unit;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.jsql.types;

public abstract class DateTimeValueExpressionTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends DateTimeValueExpressionTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends DateTimeValueExpressionTest {
  }

  private static void testInterval(final Interval interval) throws IOException, SQLException {
    testInterval(interval, new types.Type(), null, null);
  }

  private static void testInterval(final Interval interval, final Boolean skipTimeAssert) throws IOException, SQLException {
    testInterval(interval, new types.Type(), null, skipTimeAssert);
  }

  private static void testInterval(final Interval interval, types.Type p, final Condition<?> condition) throws IOException, SQLException {
    testInterval(interval, p, condition, null);
  }

  private static void testInterval(final Interval interval, types.Type p, final Condition<?> condition, final Boolean testDate) throws IOException, SQLException {
    final Condition<?> notNull = AND(IS.NOT.NULL(p.datetimeType), IS.NOT.NULL(p.dateType), IS.NOT.NULL(p.timeType));
    try (final Transaction transaction = new Transaction(types.class)) {
      final RowIterator<type.Subject<?>> rows =
        SELECT(
          p,
          ADD(p.datetimeType, interval),
          SUB(p.datetimeType, interval),
          ADD(p.dateType, interval),
          SUB(p.dateType, interval),
          ADD(p.timeType, interval),
          SUB(p.timeType, interval)).
        FROM(p).
        WHERE(condition != null ? AND(condition, notNull) : notNull).
        LIMIT(1).
        execute(transaction);

      assertTrue(rows.nextRow());
      p = (types.Type)rows.nextEntity();

      final types.Type clone = p.clone();

      final LocalDateTime localDateTime1 = ((type.DATETIME)rows.nextEntity()).get();
      final LocalDateTime localDateTime2 = ((type.DATETIME)rows.nextEntity()).get();
      final LocalDate localDate1 = ((type.DATE)rows.nextEntity()).get();
      final LocalDate localDate2 = ((type.DATE)rows.nextEntity()).get();
      if (testDate == null || testDate) {
        assertEquals(p.datetimeType.get().plus(interval), localDateTime1);
        assertEquals(p.datetimeType.get().minus(interval), localDateTime2);
        assertEquals(p.dateType.get().plus(interval), localDate1);
        assertEquals(p.dateType.get().minus(interval), localDate2);
      }

      final LocalTime localTime1 = ((type.TIME)rows.nextEntity()).get();
      final LocalTime localTime2 = ((type.TIME)rows.nextEntity()).get();
      if (testDate == null || !testDate) {
        assertEquals(p.timeType.get().plus(interval), localTime1);
        assertEquals(p.timeType.get().minus(interval), localTime2);
      }

      if (testDate == null || testDate) {
        p.datetimeType.set(ADD(p.datetimeType, interval));
        p.dateType.set(ADD(p.dateType, interval));
      }

      if (testDate == null || !testDate)
        p.timeType.set(ADD(p.timeType, interval));

      assertEquals(1, UPDATE(p).execute(transaction));

      if (testDate == null || testDate) {
        assertEquals(clone.datetimeType.get().plus(interval), p.datetimeType.get());
        assertEquals(clone.dateType.get().plus(interval), p.dateType.get());
      }

      if (testDate == null || !testDate)
        assertEquals(clone.timeType.get().plus(interval), p.timeType.get());

      if (testDate == null || testDate) {
        p.datetimeType.set(SUB(SUB(p.datetimeType, interval), interval));
        p.dateType.set(SUB(SUB(p.dateType, interval), interval));
      }

      if (testDate == null || !testDate)
        p.timeType.set(SUB(SUB(p.timeType, interval), interval));

      assertEquals(1, UPDATE(p).execute(transaction));

      if (testDate == null || testDate) {
        assertEquals(clone.datetimeType.get().minus(interval), p.datetimeType.get());
        assertEquals(clone.dateType.get().minus(interval), p.dateType.get());
      }

      if (testDate == null || !testDate)
        assertEquals(clone.timeType.get().minus(interval), p.timeType.get());

      transaction.rollback();
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testMicrosDate() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.MICROS), true);
  }

  @Test
  @VendorSchemaRunner.Unsupported({Derby.class, SQLite.class, PostgreSQL.class})
  public void testMicrosTime() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.MICROS), false);
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testMillisDate() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.MILLIS), true);
  }

  @Test
  @VendorSchemaRunner.Unsupported({Derby.class, SQLite.class})
  public void testMillisTime() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.MILLIS), false);
  }

  @Test
  public void testSeconds() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.SECONDS));
  }

  @Test
  public void testMinutes() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.MINUTES));
  }

  @Test
  public void testHours() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.HOURS));
  }

  @Test
  public void testDays() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.DAYS));
  }

  @Test
  public void testWeeks() throws IOException, SQLException {
    testInterval(new Interval(2, Unit.WEEKS));
  }

  @Test
  public void testMonths() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(12, Unit.MONTHS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testQuarters() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(4, Unit.QUARTERS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testYears() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(2, Unit.YEARS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testDecades() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(2, Unit.DECADES), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testCenturies() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(2, Unit.CENTURIES), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testMillenia() throws IOException, SQLException {
    final types.Type p = new types.Type();
    testInterval(new Interval(1, Unit.MILLENNIA), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  public void testInWhere() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.INT> rows =
      SELECT(COUNT()).
      FROM(p).
      WHERE(GT(p.shippedDate, ADD(p.requiredDate, new Interval(2, Unit.DAYS)))).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals((Integer)1, rows.nextEntity().get());
    }
  }

  @Test
  @Ignore("Need to implement server-side INTERVAL evaluation")
  public void testINTERVAL() {
  }
}