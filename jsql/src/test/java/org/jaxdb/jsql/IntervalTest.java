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

package org.jaxdb.jsql;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.jaxdb.jsql.Interval.Unit;
import org.junit.Test;
import org.libj.util.Dates;
import org.libj.util.SimpleDateFormats;
import org.libj.util.Temporals;

public class IntervalTest {
  private static final Date date = Date.from(LocalDateTime.parse("2000-01-01T00:00:00", DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.systemDefault()).toInstant());
  private static final long time = date.getTime();
  private static final LocalDateTime localTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);
  private static final LocalDate localDate = LocalDate.of(2000, 1, 1);
  private static final LocalDateTime localDateTime = LocalDateTime.of(2000, 1, 1, 0, 0, 0);

  private static long add(final long time, final int value, final Interval.Unit unit) {
    LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(time), ZoneId.systemDefault());
    localDateTime = localDateTime.plus(value, unit);
    return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
  }

  private static String formatMessage(final Object obj, final Interval.Unit unit, final int i) {
    return obj + (i < 0 ? " - " + (-i) : " + " + i) + " " + unit;
  }

  public void test(final Interval.Unit unit, final int min, final int max) {
    for (int i = min; i < max; ++i) { // [N]
      if (unit != Interval.Unit.MICROS) {
        // long
        assertEquals(formatMessage(date, unit, i), SimpleDateFormats.newDate(add(date.getTime(), i, unit), SimpleDateFormats.ISO_8601), new Interval(i, unit).addTo(date));
        assertEquals(formatMessage(date, unit, i), SimpleDateFormats.newDate(add(date.getTime(), -i, unit), SimpleDateFormats.ISO_8601), new Interval(i, unit).subtractFrom(date));

        // Date
        assertEquals(formatMessage(date.getTime(), unit, i), add(date.getTime(), i, unit), new Interval(i, unit).addTo(time));
        assertEquals(formatMessage(date.getTime(), unit, i), add(date.getTime(), -i, unit), new Interval(i, unit).subtractFrom(time));
      }

      // LocalTime
      assertEquals(formatMessage(localTime.toLocalTime(), unit, i), localTime.plus(i, unit).toLocalTime(), new Interval(i, unit).addTo(localTime.toLocalTime()));
      assertEquals(formatMessage(localTime.toLocalTime(), unit, i), localTime.minus(i, unit).toLocalTime(), new Interval(i, unit).subtractFrom(localTime.toLocalTime()));

      // LocalDate
      if (unit.isDateBased()) {
        assertEquals(formatMessage(localDate, unit, i), localDate.plus(i, unit), new Interval(i, unit).addTo(localDate));
        assertEquals(formatMessage(localDate, unit, i), localDate.minus(i, unit), new Interval(i, unit).subtractFrom(localDate));
      }

      // LocalDateTime
      assertEquals(formatMessage(localDateTime, unit, i), localDateTime.plus(i, unit), new Interval(i, unit).addTo(localDateTime));
      assertEquals(formatMessage(localDateTime, unit, i), localDateTime.minus(i, unit), new Interval(i, unit).subtractFrom(localDateTime));
    }
  }

  @Test
  public void test() {
    test(Interval.Unit.MICROS, -Temporals.MICROS_IN_MILLI, Temporals.MICROS_IN_MILLI);
    test(Interval.Unit.MILLIS, -Dates.MILLISECONDS_IN_SECOND, Dates.MILLISECONDS_IN_SECOND);
    test(Interval.Unit.SECONDS, -Dates.SECONDS_IN_MINUTE, Dates.SECONDS_IN_MINUTE);
    test(Interval.Unit.MINUTES, -Dates.MINUTES_IN_HOUR, Dates.MINUTES_IN_HOUR);
    test(Interval.Unit.HOURS, -Dates.HOURS_IN_DAY, Dates.HOURS_IN_DAY);
    test(Interval.Unit.DAYS, -Dates.DAYS_IN_WEEK, Dates.DAYS_IN_WEEK);
    test(Interval.Unit.WEEKS, -4, 4);
    test(Interval.Unit.MONTHS, -12, 12);
    test(Interval.Unit.QUARTERS, -4, 4);
    test(Interval.Unit.YEARS, -100, 100);
    test(Interval.Unit.CENTURIES, -10, 10);
    test(Interval.Unit.MILLENNIA, -10, 10);
  }

  @Test
  public void testConvertTo() {
    final Interval interval = new Interval(100, Unit.SECONDS);
    assertEquals(new BigDecimal(100000), interval.convertTo(Unit.MILLIS));

    interval.and(125, Unit.MILLIS);
    assertEquals(new BigDecimal(100125), interval.convertTo(Unit.MILLIS));
    assertEquals(new BigDecimal(100125000), interval.convertTo(Unit.MICROS));
    assertEquals(new BigDecimal("100.125"), interval.convertTo(Unit.SECONDS));

    interval.and(3, Unit.HOURS);
    assertEquals(new BigDecimal(10900.125), interval.convertTo(Unit.SECONDS));
    assertEquals(new BigDecimal(10900.125).divide(BigDecimal.valueOf(60), MathContext.DECIMAL64), interval.convertTo(Unit.MINUTES));
  }
}