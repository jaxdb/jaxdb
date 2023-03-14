/* Copyright (c) 2016 JAX-DB
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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
import java.sql.SQLException;
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
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.libj.lang.BigDecimals;
import org.libj.math.BigInt;
import org.libj.util.ArrayUtil;
import org.libj.util.Temporals;

public final class Interval extends data.Entity implements TemporalAmount {
  public static final class Unit implements Comparable<Unit>, TemporalUnit {
    public static final Unit MICROS;
    public static final Unit MILLIS;
    public static final Unit SECONDS;
    public static final Unit MINUTES;
    public static final Unit HOURS;
    public static final Unit DAYS;
    public static final Unit WEEKS;
    public static final Unit MONTHS;
    public static final Unit QUARTERS;
    public static final Unit YEARS;
    public static final Unit DECADES;
    public static final Unit CENTURIES;
    public static final Unit MILLENNIA;

    private static byte index = 0;

    private static final Unit[] values = {
      MICROS = new Unit(ChronoUnit.MICROS, 1, false),
      MILLIS = new Unit(ChronoUnit.MILLIS, 1000, false),
      SECONDS = new Unit(ChronoUnit.SECONDS, 1000000, false),
      MINUTES = new Unit(ChronoUnit.MINUTES, 60000000, false),
      HOURS = new Unit(ChronoUnit.HOURS, 3600000000L, false),
      DAYS = new Unit(ChronoUnit.DAYS, 86400000000L, false),
      WEEKS = new Unit(ChronoUnit.WEEKS, 604800000000L, false),
      MONTHS = new Unit(ChronoUnit.MONTHS, 2629746000000L, true),
      QUARTERS = new Unit(IsoFields.QUARTER_YEARS, "QUARTERS", 7889238000000L, true),
      YEARS = new Unit(ChronoUnit.YEARS, 31556952000000L, true),
      DECADES = new Unit(ChronoUnit.DECADES, 315569520000000L, true),
      CENTURIES = new Unit(ChronoUnit.CENTURIES, 3155695200000000L, true),
      MILLENNIA = new Unit(ChronoUnit.MILLENNIA, 31556952000000000L, true)
    };

    private static Unit[] units = new Unit[values.length];
    private static String[] names = new String[values.length];

    static {
      for (int i = 0, i$ = units.length; i < i$; ++i) { // [A]
        units[i] = values[i];
        names[i] = values[i].name.toLowerCase();
      }

      ArrayUtil.sort(units, names, String::compareTo);
    }

    public static Unit[] values() {
      return values;
    }

    public static Unit valueOf(final String name) {
      final int index = Arrays.binarySearch(names, name.toLowerCase());
      return index < 0 ? null : units[index];
    }

    private final byte ordinal;
    private final TemporalUnit unit;
    private final long micros;
    private final boolean isEstimate;
    private final String name;

    private Unit(final ChronoUnit unit, final long micros, final boolean isEstimate) {
      this(unit, unit.name(), micros, isEstimate);
    }

    private Unit(final TemporalUnit unit, final String name, final long micros, final boolean isEstimate) {
      this.ordinal = index++;
      this.unit = unit;
      this.name = name;
      this.micros = micros;
      this.isEstimate = isEstimate;
    }

    public byte ordinal() {
      return ordinal;
    }

    @Override
    public int compareTo(final Unit o) {
      return o == null ? 1 : o.micros == micros ? 0 : o.micros < micros ? 1 : -1;
    }

    @Override
    public Duration getDuration() {
      return unit.getDuration();
    }

    @Override
    public boolean isDurationEstimated() {
      return isEstimate;
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
    final int space = string.indexOf(' ');
    if (space < 0)
      throw new IllegalArgumentException("Malformed interval " + string);

    return new Interval(Integer.parseInt(string.substring(0, space)), Unit.valueOf(string.substring(space + 1)));
  }

  private final TreeMap<TemporalUnit,Long> intervals = new TreeMap<>();

  public Interval(final long value, final Unit unit) {
    super(false);
    if (value != 0)
      intervals.put(assertNotNull(unit), value);
  }

  public Interval() {
    super(false);
  }

  public Interval and(final long value, final TemporalUnit unit) {
    final long newValue = intervals.getOrDefault(unit, 0L) + value;
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
  public ArrayList<TemporalUnit> getUnits() {
    return new ArrayList<>(intervals.keySet());
  }

  public Interval toDateInterval() {
    final Interval dateInterval = new Interval();
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
        if (entry.getKey().isDateBased())
          dateInterval.and(entry.getValue(), entry.getKey());

    return dateInterval;
  }

  public Interval toTimeInterval() {
    final Interval dateInterval = new Interval();
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
        if (entry.getKey().isTimeBased())
          dateInterval.and(entry.getValue(), entry.getKey());

    return dateInterval;
  }

  private LocalDateTime add(LocalDateTime dateTime, final int sign) {
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
        dateTime = dateTime.plus(sign * entry.getValue(), entry.getKey());

    return dateTime;
  }

  private LocalDate add(LocalDate date, final int sign) {
    long remainder = 0;
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
        if (entry.getKey().isDateBased())
          date = date.plus(sign * entry.getValue(), entry.getKey());
        else
          remainder += entry.getValue();

    if (sign == 1)
      return date;

    return remainder > 0 ? date.minus(1, Unit.DAYS) : date;
  }

  private LocalTime add(LocalTime time, final int sign) {
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
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
  final data.Table getTable() {
    return null;
  }

  @Override
  data.Column<?> getColumn() {
    return null;
  }

  @Override
  void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    // FIXME: Does this ever get called?
    compilation.compiler.compileInterval(null, null, this, compilation);
  }

  public BigDecimal convertTo(final TemporalUnit unit) {
    // FIXME: Decouple from Unit to TemporalUnit
    final int[] micros = BigInt.valueOf(0);
    if (intervals.size() > 0)
      for (final Map.Entry<TemporalUnit,Long> entry : intervals.entrySet()) // [S]
        BigInt.add(micros, BigInt.mul(BigInt.valueOf(entry.getValue()), ((Unit)entry.getKey()).micros));

    return BigInt.toBigDecimal(micros).divide(BigDecimals.intern(((Unit)unit).micros), MathContext.DECIMAL64);
  }

  @Override
  public String toString() {
    if (intervals.size() == 0)
      return "";

    final StringBuilder builder = new StringBuilder();
    final Iterator<Map.Entry<TemporalUnit,Long>> iterator = intervals.entrySet().iterator();
    for (int i = 0; iterator.hasNext(); ++i) { // [I]
      if (i > 0)
        builder.append(' ');

      final Map.Entry<TemporalUnit,Long> entry = iterator.next();
      builder.append(entry.getValue()).append(entry.getKey());
    }

    return builder.toString();
  }

  @Override
  Serializable evaluate(final Set<Evaluable> visited) {
    throw new UnsupportedOperationException();
  }
}