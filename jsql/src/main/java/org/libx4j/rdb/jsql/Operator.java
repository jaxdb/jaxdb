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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.libx4j.rdb.jsql.type.DataType;

final class operator {
  public static abstract class Generic {
    private final java.lang.String symbol;

    protected Generic(final java.lang.String symbol) {
      this.symbol = symbol;
    }

    @Override
    public java.lang.String toString() {
      return symbol;
    }
  }

  static abstract class ArithmeticPlusMinus extends Arithmetic {
    protected ArithmeticPlusMinus(final java.lang.String symbol) {
      super(symbol);
    }

    protected abstract LocalDate evaluate(final LocalDate a, final Interval b);
    protected abstract LocalDateTime evaluate(final LocalDateTime a, final Interval b);
    protected abstract LocalTime evaluate(final LocalTime a, final Interval b);
  }

  static abstract class Arithmetic extends Generic {
    public static final ArithmeticPlusMinus PLUS = new ArithmeticPlusMinus("+") {
      @Override
      protected float evaluate(final float a, final float b) {
        return a + b;
      }

      @Override
      protected double evaluate(final double a, final double b) {
        return a + b;
      }

      @Override
      protected Number evaluate(final byte a, final byte b) {
        return a + b;
      }

      @Override
      protected Number evaluate(final short a, final short b) {
        return a + b;
      }

      @Override
      protected Number evaluate(final int a, final int b) {
        return a + b;
      }

      @Override
      protected Number evaluate(final long a, final long b) {
        return a + b;
      }

      @Override
      protected Number evaluate(final BigInteger a, final BigInteger b) {
        return a.add(b);
      }

      @Override
      protected Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.add(b);
      }

      @Override
      protected LocalDate evaluate(final LocalDate a, final Interval b) {
        return b.addTo(a);
      }

      @Override
      protected LocalDateTime evaluate(final LocalDateTime a, final Interval b) {
        return b.addTo(a);
      }

      @Override
      protected LocalTime evaluate(final LocalTime a, final Interval b) {
        return b.addTo(a);
      }
    };
    public static final ArithmeticPlusMinus MINUS = new ArithmeticPlusMinus("-") {
      @Override
      protected float evaluate(final float a, final float b) {
        return a - b;
      }

      @Override
      protected double evaluate(final double a, final double b) {
        return a - b;
      }

      @Override
      protected Number evaluate(final byte a, final byte b) {
        return a - b;
      }

      @Override
      protected Number evaluate(final short a, final short b) {
        return a - b;
      }

      @Override
      protected Number evaluate(final int a, final int b) {
        return a - b;
      }

      @Override
      protected Number evaluate(final long a, final long b) {
        return a - b;
      }

      @Override
      protected Number evaluate(final BigInteger a, final BigInteger b) {
        return a.subtract(b);
      }

      @Override
      protected Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.subtract(b);
      }

      @Override
      protected LocalDate evaluate(final LocalDate a, final Interval b) {
        return b.subtractFrom(a);
      }

      @Override
      protected LocalDateTime evaluate(final LocalDateTime a, final Interval b) {
        return b.subtractFrom(a);
      }

      @Override
      protected LocalTime evaluate(final LocalTime a, final Interval b) {
        return b.subtractFrom(a);
      }
    };
    public static final Arithmetic MULTIPLY = new Arithmetic("*") {
      @Override
      protected float evaluate(final float a, final float b) {
        return a * b;
      }

      @Override
      protected double evaluate(final double a, final double b) {
        return a * b;
      }

      @Override
      protected Number evaluate(final byte a, final byte b) {
        return a * b;
      }

      @Override
      protected Number evaluate(final short a, final short b) {
        return a * b;
      }

      @Override
      protected Number evaluate(final int a, final int b) {
        return a * b;
      }

      @Override
      protected Number evaluate(final long a, final long b) {
        return a * b;
      }

      @Override
      protected Number evaluate(final BigInteger a, final BigInteger b) {
        return a.multiply(b);
      }

      @Override
      protected Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.multiply(b);
      }
    };
    public static final Arithmetic DIVIDE = new Arithmetic("/") {
      @Override
      protected float evaluate(final float a, final float b) {
        return a / b;
      }

      @Override
      protected double evaluate(final double a, final double b) {
        return a / b;
      }

      @Override
      protected Number evaluate(final byte a, final byte b) {
        return a / b;
      }

      @Override
      protected Number evaluate(final short a, final short b) {
        return a / b;
      }

      @Override
      protected Number evaluate(final int a, final int b) {
        return a / b;
      }

      @Override
      protected Number evaluate(final long a, final long b) {
        return a / b;
      }

      @Override
      protected Number evaluate(final BigInteger a, final BigInteger b) {
        return a.divide(b);
      }

      @Override
      protected Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.divide(b);
      }
    };

    protected Arithmetic(final java.lang.String symbol) {
      super(symbol);
    }

    protected Number evaluate(final Number a, final Number b) {
      if (a == null || b == null)
        return null;

      if (a instanceof Float) {
        if (b instanceof Float || b instanceof Byte || b instanceof Short)
          return evaluate(a.floatValue(), b.floatValue());

        if (b instanceof Double || b instanceof Integer || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else if (b instanceof BigInteger)
          bigDecimal = new BigDecimal((BigInteger)b);
        else
          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");

        return evaluate(BigDecimal.valueOf(a.floatValue()), bigDecimal);
      }

      if (a instanceof Double) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else if (b instanceof BigInteger)
          bigDecimal = new BigDecimal((BigInteger)b);
        else
          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");

        return evaluate(BigDecimal.valueOf(a.doubleValue()), bigDecimal);
      }

      if (a instanceof Byte) {
        if (b instanceof Float)
          return evaluate(a.floatValue(), b.floatValue());

        if (b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte)
          return evaluate(a.byteValue(), b.byteValue());

        if (b instanceof Short)
          return evaluate(a.shortValue(), b.shortValue());

        if (b instanceof Integer)
          return evaluate(a.intValue(), b.intValue());

        if (b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        if (b instanceof BigInteger)
          return evaluate(BigInteger.valueOf(a.longValue()), (BigInteger)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Short) {
        if (b instanceof Float)
          return evaluate(a.floatValue(), b.floatValue());

        if (b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte || b instanceof Short)
          return evaluate(a.shortValue(), b.shortValue());

        if (b instanceof Integer)
          return evaluate(a.intValue(), b.intValue());

        if (b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        if (b instanceof BigInteger)
          return evaluate(BigInteger.valueOf(a.longValue()), (BigInteger)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Integer) {
        if (b instanceof Float || b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte || b instanceof Short || b instanceof Integer)
          return evaluate(a.intValue(), b.intValue());

        if (b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        if (b instanceof BigInteger)
          return evaluate(BigInteger.valueOf(a.longValue()), (BigInteger)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Long) {
        if (b instanceof Float || b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        if (b instanceof BigInteger)
          return evaluate(BigInteger.valueOf(a.longValue()), (BigInteger)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof BigDecimal)
          return evaluate((BigDecimal)a, (BigDecimal)b);

        if (b instanceof BigInteger)
          return evaluate((BigDecimal)a, new BigDecimal((BigInteger)b));

        if (b instanceof Float || b instanceof Double)
          return evaluate((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()));

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate((BigDecimal)a, BigDecimal.valueOf(b.longValue()));

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInteger) {
        if (b instanceof BigInteger)
          return evaluate((BigInteger)a, (BigInteger)b);

        if (b instanceof BigDecimal)
          return evaluate(new BigDecimal((BigInteger)a), (BigDecimal)b);

        if (b instanceof Float || b instanceof Double)
          return evaluate((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()));

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate((BigInteger)a, BigInteger.valueOf(b.longValue()));

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
    }

    protected abstract float evaluate(final float a, final float b);
    protected abstract double evaluate(final double a, final double b);
    protected abstract Number evaluate(final byte a, final byte b);
    protected abstract Number evaluate(final short a, final short b);
    protected abstract Number evaluate(final int a, final int b);
    protected abstract Number evaluate(final long a, final long b);
    protected abstract Number evaluate(final BigInteger a, final BigInteger b);
    protected abstract Number evaluate(final BigDecimal a, final BigDecimal b);
  }

  static abstract class Boolean extends Generic {
    public static final Boolean AND = new Boolean("AND") {
      @Override
      protected java.lang.Boolean evaluate(final Condition<java.lang.Boolean> a, final Condition<java.lang.Boolean> b) {
        return a == null || b == null || a.value == null || b.value == null ? null : a.value && b.value;
      }
    };
    public static final Boolean OR = new Boolean("OR") {
      @Override
      protected java.lang.Boolean evaluate(final Condition<java.lang.Boolean> a, final Condition<java.lang.Boolean> b) {
        return a == null || b == null || a.value == null || b.value == null ? null : a.value || b.value;
      }
    };

    protected Boolean(final java.lang.String symbol) {
      super(symbol);
    }

    protected abstract java.lang.Boolean evaluate(final Condition<java.lang.Boolean> a, final Condition<java.lang.Boolean> b);
  }

  static abstract class Logical<D> extends Generic {
    public static final Logical<type.DataType<?>> EQ = new Logical<type.DataType<?>>("=") {
      @Override
      protected java.lang.Boolean evaluate(final DataType<?> a, final DataType<?> b) {
        return a == null || b == null ? null : a.equals(b);
      }
    };
    public static final Logical<Comparable<Object>> LT = new Logical<Comparable<Object>>("<") {
      @Override
      protected java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return a == null || b == null ? null : a.compareTo(b) < 0;
      }
    };
    public static final Logical<Comparable<Object>> LTE = new Logical<Comparable<Object>>("<=") {
      @Override
      protected java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return a != null && a.compareTo(b) <= 0;
      }
    };
    public static final Logical<Comparable<Object>> GT = new Logical<Comparable<Object>>(">") {
      @Override
      protected java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return LT.evaluate(b, a);
      }
    };
    public static final Logical<Comparable<Object>> GTE = new Logical<Comparable<Object>>(">=") {
      @Override
      protected java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return LTE.evaluate(b, a);
      }
    };
    public static final Logical<type.DataType<?>> NE = new Logical<type.DataType<?>>("<>") {
      @Override
      protected java.lang.Boolean evaluate(final type.DataType<?> a, final type.DataType<?> b) {
        return !EQ.evaluate(a, b);
      }
    };

    protected Logical(final java.lang.String symbol) {
      super(symbol);
    }

    protected abstract java.lang.Boolean evaluate(final D a, final D b);
  }

  static abstract class String extends Generic {
    public static final String CONCAT = new String("||") {
      @Override
      protected java.lang.String evaluate(final java.lang.String a, final java.lang.String b) {
        return a == null || b == null ? null : a + b;
      }
    };

    protected String(final java.lang.String symbol) {
      super(symbol);
    }

    protected abstract java.lang.String evaluate(final java.lang.String a, final java.lang.String b);
  }

  public static class Ordering extends Generic {
    public static final Ordering IS = new Ordering("IS");
    public static final Ordering NOT = new Ordering("IS NOT");

    public static final Ordering ASC = new Ordering("ASC");
    public static final Ordering DESC = new Ordering("DESC");

    protected Ordering(final java.lang.String symbol) {
      super(symbol);
    }
  }

  private operator() {
  }
}