/* Copyright (c) 2016 Seva Safris
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

import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalUnit;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class Interval extends Serializable {
  public static enum Unit {
    MICROS(ChronoUnit.MICROS, Calendar.MILLISECOND, 1),
    MILLIS(ChronoUnit.MILLIS, Calendar.MILLISECOND, 1),
    SECONDS(ChronoUnit.SECONDS, Calendar.SECOND, 1),
    MINUTES(ChronoUnit.MINUTES, Calendar.MINUTE, 1),
    HOURS(ChronoUnit.HOURS, Calendar.HOUR, 1),
    DAYS(ChronoUnit.DAYS, Calendar.DATE, 1),
    WEEKS(ChronoUnit.WEEKS, Calendar.DATE, 7),
    MONTHS(ChronoUnit.MONTHS, Calendar.MONTH, 1),
    QUARTERS(IsoFields.QUARTER_YEARS, Calendar.MONTH, 3),
    YEARS(ChronoUnit.YEARS, Calendar.YEAR, 1),
    DECADES(ChronoUnit.DECADES, Calendar.YEAR, 10),
    CENTURIES(ChronoUnit.CENTURIES, Calendar.YEAR, 100),
    MILLENNIA(ChronoUnit.MILLENNIA, Calendar.YEAR, 1000);

    private final TemporalUnit unit;
    private final int calendarField;
    private final int fieldScale;

    Unit(final TemporalUnit unit, final int calendarField, final int fieldScale) {
      this.unit = unit;
      this.calendarField = calendarField;
      this.fieldScale = fieldScale;
    }

    public final TemporalUnit unit() {
      return unit;
    }

    protected int getCalendarField() {
      return calendarField;
    }

    protected int getFieldScale() {
      return fieldScale;
    }
  }

  public static Interval valueOf(final String string) {
    if (string == null)
      throw new NullPointerException("interval == null");

    final int index = string.indexOf(' ');
    if (index < 0)
      throw new IllegalArgumentException("Malformed interval " + string);

    final int value = Integer.parseInt(string.substring(0, index));
    final Unit unit = Unit.valueOf(string.substring(index + 1));
    return new Interval(value, unit);
  }

  private final Map<Unit,Integer> intervals = new HashMap<Unit,Integer>();

  public Interval(final int value, final Unit unit) {
    intervals.put(unit, value);
  }

  public Interval and(final int value, final Unit unit) {
    final Integer existing = intervals.get(unit);
    intervals.put(unit, existing != null ? existing + value : value);
    return this;
  }

  public Set<Unit> getUnits() {
    return intervals.keySet();
  }

  public Integer getComponent(final Unit unit) {
    return intervals.get(unit);
  }

  @Override
  protected void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}