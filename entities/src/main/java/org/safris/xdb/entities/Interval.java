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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Interval extends Serializable {
  public static enum Unit {
    MICROS(ChronoUnit.MICROS),
    MILLIS(ChronoUnit.MILLIS),
    SECONDS(ChronoUnit.SECONDS),
    MINUTES(ChronoUnit.MINUTES),
    HOURS(ChronoUnit.HOURS),
    DAYS(ChronoUnit.DAYS),
    WEEKS(ChronoUnit.WEEKS),
    MONTHS(ChronoUnit.MONTHS),
    QUARTERS(IsoFields.QUARTER_YEARS),
    YEARS(ChronoUnit.YEARS),
    DECADES(ChronoUnit.DECADES),
    CENTURIES(ChronoUnit.CENTURIES),
    MILLENNIA(ChronoUnit.MILLENNIA);

    private final TemporalUnit unit;

    Unit(final TemporalUnit unit) {
      this.unit = unit;
    }

    public final TemporalUnit unit() {
      return unit;
    }
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
  protected final void serialize(final Serialization serialization) throws IOException {
    Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
  }
}