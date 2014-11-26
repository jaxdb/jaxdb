package org.safris.xdb.xde.cql;

import java.sql.Date;
import java.sql.Time;

import org.safris.xdb.xde.Column;

public class LogicalCondition extends Condition {
  private static Object checkRef(final Column<?> obj) {
    return lookupAlias(getOwner(obj), false) == null ? obj.get() : obj;
  }

  public static LogicalCondition EQ(final Boolean a, final Boolean b) {
    return new LogicalCondition("=", a, b);
  }

  public static LogicalCondition EQ(final Column<?> a, final Boolean b) {
    return new LogicalCondition("=", checkRef(a), b);
  }

  public static LogicalCondition EQ(final Boolean a, final Column<?> b) {
    return new LogicalCondition("=", a, checkRef(b));
  }

  public static LogicalCondition EQ(final Time a, final Time b) {
    return new LogicalCondition("=", a, b);
  }

  public static LogicalCondition EQ(final Column<?> a, final Time b) {
    return new LogicalCondition("=", checkRef(a), b);
  }

  public static LogicalCondition EQ(final Time a, final Column<?> b) {
    return new LogicalCondition("=", a, checkRef(b));
  }

  public static LogicalCondition EQ(final Date a, final Date b) {
    return new LogicalCondition("=", a, b);
  }

  public static LogicalCondition EQ(final Column<?> a, final Date b) {
    return new LogicalCondition("=", checkRef(a), b);
  }

  public static LogicalCondition EQ(final Date a, final Column<?> b) {
    return new LogicalCondition("=", a, checkRef(b));
  }

  public static LogicalCondition EQ(final String a, final String b) {
    return new LogicalCondition("=", a, b);
  }

  public static LogicalCondition EQ(final Column<?> a, final String b) {
    return new LogicalCondition("=", checkRef(a), b);
  }

  public static LogicalCondition EQ(final String a, final Column<?> b) {
    return new LogicalCondition("=", a, checkRef(b));
  }

  public static LogicalCondition EQ(final Number a, final Number b) {
    return new LogicalCondition("=", a, b);
  }

  public static LogicalCondition EQ(final Column<?> a, final Number b) {
    return new LogicalCondition("=", checkRef(a), b);
  }

  public static LogicalCondition EQ(final Number a, final Column<?> b) {
    return new LogicalCondition("=", a, checkRef(b));
  }

  public static LogicalCondition EQ(final Column<?> a, final Column<?> b) {
    return new LogicalCondition("=", checkRef(a), checkRef(b));
  }

  private LogicalCondition(final String operator, final Object a, final Object b) {
    super(operator, a, b);
  }
}