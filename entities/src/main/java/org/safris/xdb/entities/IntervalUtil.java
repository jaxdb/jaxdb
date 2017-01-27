/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

import org.safris.xdb.entities.Interval.Unit;

public final class IntervalUtil {
  public static double mod(final double a, final double b) {
    return a % b;
  }

  public static double log(final double a, final double b) {
    return Math.log(a) / Math.log(b);
  }

  public static double log2(final double a) {
    return Math.log(a) / 0.6931471805599453;
  }

  public static Date add(final Date date, final String string) {
    return add(date, Interval.valueOf(string), 1);
  }

  public static Date add(final Time date, final String string) {
    return add(date, Interval.valueOf(string), 1);
  }

  public static Date add(final Timestamp date, final String string) {
    return add(date, Interval.valueOf(string), 1);
  }

  public static Date sub(final Date date, final String string) {
    return add(date, Interval.valueOf(string), -1);
  }

  public static Date sub(final Time date, final String string) {
    return add(date, Interval.valueOf(string), -1);
  }

  public static Date sub(final Timestamp date, final String string) {
    return add(date, Interval.valueOf(string), -1);
  }

  private static Date add(final java.util.Date date, final Interval interval, final int multilpier) {
    final Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    for (final Unit unit : interval.getUnits())
      calendar.add(unit.getCalendarField(), multilpier * unit.getFieldScale() * interval.getComponent(unit));

    return new Date(calendar.getTimeInMillis());
  }

  private IntervalUtil() {
  }
}