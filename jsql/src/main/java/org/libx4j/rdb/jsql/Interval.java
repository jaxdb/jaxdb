/* Copyright (c) 2016 lib4j
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

package org.libx4j.rdb.jsql;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.lib4j.util.Temporals;

public final class Interval extends Compilable implements TemporalAmount {
  public static final class Unit implements TemporalUnit {
    private static final Map<String,Unit> units = new HashMap<String,Unit>();

    protected static Unit valueOf(final String string) {
      return units.get(string.toLowerCase());
    }

    public static Unit MICROS = new Unit(ChronoUnit.MICROS);
    public static Unit MILLIS = new Unit(ChronoUnit.MILLIS);
    public static Unit SECONDS = new Unit(ChronoUnit.SECONDS);
    public static Unit MINUTES = new Unit(ChronoUnit.MINUTES);
    public static Unit HOURS = new Unit(ChronoUnit.HOURS);
    public static Unit DAYS = new Unit(ChronoUnit.DAYS);
    public static Unit WEEKS = new Unit(ChronoUnit.WEEKS);
    public static Unit MONTHS = new Unit(ChronoUnit.MONTHS);
    public static Unit QUARTERS = new Unit(IsoFields.QUARTER_YEARS, "QUARTERS");
    public static Unit YEARS = new Unit(ChronoUnit.YEARS);
    public static Unit DECADES = new Unit(ChronoUnit.DECADES);
    public static Unit CENTURIES = new Unit(ChronoUnit.CENTURIES);
    public static Unit MILLENNIA = new Unit(ChronoUnit.MILLENNIA);

    private final TemporalUnit unit;
    private final String name;

    private Unit(final ChronoUnit unit) {
      this(unit, unit.name());
    }

    private Unit(final TemporalUnit unit, final String name) {
      this.unit = unit;
      this.name = name;
      units.put(name.toLowerCase(), this);
    }

    public String name() {
      return name;
    }

    @Override
    public Duration getDuration() {
      return unit.getDuration();
    }

    @Override
    public boolean isDurationEstimated() {
      return unit.isDurationEstimated();
    }

    @Override
    public boolean isDateBased() {
      return unit.isDateBased();
    }

    @Override
    public boolean isTimeBased() {
      return unit.isTimeBased();
    }

    @Override
    public <R extends Temporal>R addTo(final R temporal, final long amount) {
      return unit.addTo(temporal, amount);
    }

    @Override
    public long between(final Temporal temporal1Inclusive, final Temporal temporal2Exclusive) {
      return unit.between(temporal1Inclusive, temporal2Exclusive);
    }

    @Override
    public String toString() {
      return name;
    }
  }

  public static Interval valueOf(final String string) {
    if (string == null)
      throw new NullPointerException("interval == null");

    final int index = string.indexOf(' ');
    if (index < 0)
      throw new IllegalArgumentException("Malformed interval " + string);

    return new Interval(Integer.parseInt(string.substring(0, index)), Unit.valueOf(string.substring(index + 1)));
  }

  private final Map<TemporalUnit,Long> intervals = new LinkedHashMap<TemporalUnit,Long>();

  public Interval(final long value, final Unit unit) {
    if (value != 0)
      intervals.put(unit, value);
  }

  public Interval() {
  }

  public Interval and(final long value, final Unit unit) {
    final Long newValue = intervals.getOrDefault(unit, 0l) + value;
    if (newValue != 0)
      intervals.put(unit, newValue);
    else
      intervals.remove(unit);

    return this;
  }

  @Override
  public long get(final TemporalUnit unit) {
    return intervals.get(unit);
  }

  @Override
  public Temporal addTo(final Temporal temporal) {
    if (temporal instanceof LocalDateTime)
      return addTo((LocalDateTime)temporal);

    if (temporal instanceof LocalDate)
      return addTo((LocalDate)temporal);

    if (temporal instanceof LocalTime)
      return addTo((LocalTime)temporal);

    throw new UnsupportedOperationException("Unsupported Temporal type: " + temporal.getClass().getName());
  }

  @Override
  public Temporal subtractFrom(final Temporal temporal) {
    if (temporal instanceof LocalDateTime)
      return subtractFrom((LocalDateTime)temporal);

    if (temporal instanceof LocalDate)
      return subtractFrom((LocalDate)temporal);

    if (temporal instanceof LocalTime)
      return subtractFrom((LocalTime)temporal);

    throw new UnsupportedOperationException("Unsupported Temporal type: " + temporal.getClass().getName());
  }

  @Override
  public List<TemporalUnit> getUnits() {
    return new ArrayList<TemporalUnit>(intervals.keySet());
  }

  public Interval toDateInterval() {
    final Interval dateInterval = new Interval();
    for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet())
      if (entry.getKey().isDateBased())
        dateInterval.and(entry.getValue(), (Interval.Unit)entry.getKey());

    return dateInterval.intervals.size() == 0 ? null : dateInterval;
  }

  public Interval toTimeInterval() {
    final Interval dateInterval = new Interval();
    for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet())
      if (entry.getKey().isTimeBased())
        dateInterval.and(entry.getValue(), (Interval.Unit)entry.getKey());

    return dateInterval.intervals.size() == 0 ? null : dateInterval;
  }

  private LocalDateTime add(LocalDateTime dateTime, final int sign) {
    for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet())
      dateTime = dateTime.plus(sign * entry.getValue(), entry.getKey());

    return dateTime;
  }

  private LocalDate add(LocalDate date, final int sign) {
    for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet())
      if (entry.getKey().isDateBased())
        date = date.plus(sign * entry.getValue(), entry.getKey());

    return date;
  }

  private LocalTime add(LocalTime time, final int sign) {
    for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet())
      if (entry.getKey().isTimeBased())
        time = time.plus(sign * entry.getValue(), entry.getKey());

    return time;
  }

  private Timestamp add(final Timestamp timestamp, final int sign) {
    return Timestamp.valueOf(add(timestamp.toLocalDateTime(), sign));
  }

  private long add(final long time, final int sign) {
    return Temporals.toEpochMilli(add(Temporals.toLocalDateTime(time), sign));
  }

  private Date add(final Date date, final int sign) {
    return Temporals.toDate(add(Temporals.toLocalDateTime(date), sign));
  }

  public LocalDateTime addTo(final LocalDateTime dateTime) {
    return add(dateTime, 1);
  }

  public LocalDateTime subtractFrom(final LocalDateTime dateTime) {
    return add(dateTime, -1);
  }

  public LocalDate addTo(final LocalDate date) {
    return add(date, 1);
  }

  public LocalDate subtractFrom(final LocalDate date) {
    return add(date, -1);
  }

  public LocalTime addTo(final LocalTime time) {
    return add(time, 1);
  }

  public LocalTime subtractFrom(final LocalTime time) {
    return add(time, -1);
  }

  public Timestamp addTo(final Timestamp timestamp) {
    return add(timestamp, 1);
  }

  public Timestamp subtractFrom(final Timestamp timestamp) {
    return add(timestamp, -1);
  }

  public long addTo(final long time) {
    return add(time, 1);
  }

  public long subtractFrom(final long time) {
    return add(time, -1);
  }

  public Date addTo(final Date date) {
    return add(date, 1);
  }

  public Date subtractFrom(final Date date) {
    return add(date, -1);
  }

  @Override
  protected void compile(final Compilation compilation) {
    Compiler.getCompiler(compilation.vendor).compile(this, compilation);
  }
}