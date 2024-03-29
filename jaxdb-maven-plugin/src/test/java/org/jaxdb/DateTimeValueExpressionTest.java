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

import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.Condition;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.Interval;
import org.jaxdb.jsql.Interval.Unit;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class DateTimeValueExpressionTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends DateTimeValueExpressionTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends DateTimeValueExpressionTest {
  }

  private static void testInterval(final Types types, final Transaction transaction, final Interval interval) throws IOException, SQLException {
    testInterval(transaction, interval, types.Type$, null, null);
  }

  private static void testInterval(final Types types, final Transaction transaction, final Interval interval, final Boolean testDate) throws IOException, SQLException {
    testInterval(transaction, interval, types.Type$, null, testDate);
  }

  private static void testInterval(final Transaction transaction, final Interval interval, final Types.Type p, final Condition<?> condition) throws IOException, SQLException {
    testInterval(transaction, interval, p, condition, null);
  }

  private static void testInterval(final Transaction transaction, final Interval interval, Types.Type p, final Condition<?> condition, final Boolean testDate) throws IOException, SQLException {
    final Condition<?> notNull = AND(NOT(IS.NULL(p.datetimeType)), IS.NOT.NULL(p.dateType), NOT(IS.NULL(p.timeType)));
    try (
      final RowIterator<type.Entity> rows =
        SELECT(
          p,
          ADD(p.datetimeType, interval),
          SUB(p.datetimeType, interval),
          ADD(p.dateType, interval),
          SUB(p.dateType, interval),
          ADD(p.timeType, interval),
          SUB(p.timeType, interval))
          .FROM(p)
          .WHERE(condition != null ? AND(condition, notNull) : notNull)
          .LIMIT(1)
          .execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      p = (Types.Type)rows.nextEntity();

      final Types.Type clone = p.clone();

      final LocalDateTime localDateTime1 = ((data.DATETIME)rows.nextEntity()).get();
      final LocalDateTime localDateTime2 = ((data.DATETIME)rows.nextEntity()).get();
      final LocalDate localDate1 = ((data.DATE)rows.nextEntity()).get();
      final LocalDate localDate2 = ((data.DATE)rows.nextEntity()).get();
      if (testDate == null || testDate) {
        assertEquals(p.datetimeType.get() + " + " + interval, p.datetimeType.get().plus(interval), localDateTime1);
        assertEquals(p.datetimeType.get() + " - " + interval, p.datetimeType.get().minus(interval), localDateTime2);
        assertEquals(p.dateType.get() + " + " + interval, p.dateType.get().plus(interval), localDate1);
        assertEquals(p.dateType.get() + " - " + interval, p.dateType.get().minus(interval), localDate2);
      }

      final LocalTime localTime1 = ((data.TIME)rows.nextEntity()).get();
      final LocalTime localTime2 = ((data.TIME)rows.nextEntity()).get();
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

      assertEquals(1,
        UPDATE(p)
          .execute(transaction)
          .getCount());

      if (testDate == null || testDate) {
        assertEquals(clone.datetimeType.get().plus(interval), p.datetimeType.get());
        assertEquals(clone.dateType.get().plus(interval), p.dateType.get());
      }

      if (testDate == null || !testDate)
        assertEquals(clone.timeType.get().plus(interval), p.timeType.get());

      if (testDate == null || testDate) {
        p.datetimeType.set(SUB(p.datetimeType, interval));
        p.dateType.set(SUB(p.dateType, interval));
      }

      if (testDate == null || !testDate)
        p.timeType.set(SUB(p.timeType, interval));

      assertEquals(1,
        UPDATE(p)
          .execute(transaction)
          .getCount());

      if (testDate == null || testDate) {
        assertEquals(clone.datetimeType.get(), p.datetimeType.get());
        assertEquals(clone.dateType.get(), p.dateType.get());
      }

      if (testDate == null || !testDate)
        assertEquals(clone.timeType.get(), p.timeType.get());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported(SQLite.class)
  public void testMicrosDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.MICROS), true);
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported({Derby.class, SQLite.class, PostgreSQL.class})
  public void testMicrosTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.MICROS), false);
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported(SQLite.class)
  public void testMillisDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.MILLIS), true);
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported({Derby.class, SQLite.class})
  public void testMillisTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.MILLIS), false);
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testSeconds(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.SECONDS));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testMinutes(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.MINUTES));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testHours(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.HOURS));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testDays(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.DAYS));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testWeeks(final Types types, final Transaction transaction) throws IOException, SQLException {
    testInterval(types, transaction, new Interval(2, Unit.WEEKS));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testMonths(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(12, Unit.MONTHS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testQuarters(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(4, Unit.QUARTERS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testYears(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(2, Unit.YEARS), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testDecades(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(2, Unit.DECADES), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testCenturies(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(2, Unit.CENTURIES), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported(PostgreSQL.class)
  public void testMillenia(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type p = types.Type$;
    testInterval(transaction, new Interval(1, Unit.MILLENNIA), p, AND(GT(p.datetimeType, LocalDateTime.parse("2000-01-01T00:00:00")), LT(p.datetimeType, LocalDateTime.parse("2100-01-01T00:00:00"))));
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testInWhere(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .WHERE(GT(p.shippedDate, ADD(p.requiredDate, new Interval(2, Unit.DAYS))))
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(1, rows.nextEntity().getAsLong());
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @Ignore("Need to implement server-side INTERVAL evaluation")
  public void testINTERVAL() {
  }
}