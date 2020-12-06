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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.libj.math.BigInt;

final class operator {
  abstract static class Generic {
    private final java.lang.String symbol;

    Generic(final java.lang.String symbol) {
      this.symbol = symbol;
    }

    @Override
    public java.lang.String toString() {
      return symbol;
    }
  }

  abstract static class ArithmeticPlusMinus extends Arithmetic {
    ArithmeticPlusMinus(final java.lang.String symbol) {
      super(symbol);
    }

    abstract LocalDate evaluate(LocalDate a, Interval b);
    abstract LocalDateTime evaluate(LocalDateTime a, Interval b);
    abstract LocalTime evaluate(LocalTime a, Interval b);
  }

  abstract static class Arithmetic extends Generic {
    static final ArithmeticPlusMinus PLUS = new ArithmeticPlusMinus("+") {
      @Override
      float evaluate(final float a, final float b) {
        return a + b;
      }

      @Override
      double evaluate(final double a, final double b) {
        return a + b;
      }

      @Override
      Number evaluate(final byte a, final byte b) {
        return a + b;
      }

      @Override
      Number evaluate(final short a, final short b) {
        return a + b;
      }

      @Override
      Number evaluate(final int a, final int b) {
        return a + b;
      }

      @Override
      Number evaluate(final long a, final long b) {
        return a + b;
      }

      @Override
      Number evaluate(final BigInt a, final BigInt b) {
        return a.clone().add(b);
      }

      @Override
      Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.add(b);
      }

      @Override
      LocalDate evaluate(final LocalDate a, final Interval b) {
        return b.addTo(a);
      }

      @Override
      LocalDateTime evaluate(final LocalDateTime a, final Interval b) {
        return b.addTo(a);
      }

      @Override
      LocalTime evaluate(final LocalTime a, final Interval b) {
        return b.addTo(a);
      }
    };

    static final ArithmeticPlusMinus MINUS = new ArithmeticPlusMinus("-") {
      @Override
      float evaluate(final float a, final float b) {
        return a - b;
      }

      @Override
      double evaluate(final double a, final double b) {
        return a - b;
      }

      @Override
      Number evaluate(final byte a, final byte b) {
        return a - b;
      }

      @Override
      Number evaluate(final short a, final short b) {
        return a - b;
      }

      @Override
      Number evaluate(final int a, final int b) {
        return a - b;
      }

      @Override
      Number evaluate(final long a, final long b) {
        return a - b;
      }

      @Override
      Number evaluate(final BigInt a, final BigInt b) {
        return a.clone().sub(b);
      }

      @Override
      Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.subtract(b);
      }

      @Override
      LocalDate evaluate(final LocalDate a, final Interval b) {
        return b.subtractFrom(a);
      }

      @Override
      LocalDateTime evaluate(final LocalDateTime a, final Interval b) {
        return b.subtractFrom(a);
      }

      @Override
      LocalTime evaluate(final LocalTime a, final Interval b) {
        return b.subtractFrom(a);
      }
    };

    static final Arithmetic MULTIPLY = new Arithmetic("*") {
      @Override
      float evaluate(final float a, final float b) {
        return a * b;
      }

      @Override
      double evaluate(final double a, final double b) {
        return a * b;
      }

      @Override
      Number evaluate(final byte a, final byte b) {
        return a * b;
      }

      @Override
      Number evaluate(final short a, final short b) {
        return a * b;
      }

      @Override
      Number evaluate(final int a, final int b) {
        return a * b;
      }

      @Override
      Number evaluate(final long a, final long b) {
        return a * b;
      }

      @Override
      Number evaluate(final BigInt a, final BigInt b) {
        return a.clone().mul(b);
      }

      @Override
      Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.multiply(b);
      }
    };

    static final Arithmetic DIVIDE = new Arithmetic("/") {
      @Override
      float evaluate(final float a, final float b) {
        return a / b;
      }

      @Override
      double evaluate(final double a, final double b) {
        return a / b;
      }

      @Override
      Number evaluate(final byte a, final byte b) {
        return a / b;
      }

      @Override
      Number evaluate(final short a, final short b) {
        return a / b;
      }

      @Override
      Number evaluate(final int a, final int b) {
        return a / b;
      }

      @Override
      Number evaluate(final long a, final long b) {
        return a / b;
      }

      @Override
      Number evaluate(final BigInt a, final BigInt b) {
        return a.clone().div(b);
      }

      @Override
      Number evaluate(final BigDecimal a, final BigDecimal b) {
        return a.divide(b, RoundingMode.HALF_UP);
      }
    };

    Arithmetic(final java.lang.String symbol) {
      super(symbol);
    }

    Number evaluate(final Number a, final Number b) {
      if (a == null || b == null)
        return null;

      if (a instanceof Float) {
        if (b instanceof Float || b instanceof Byte || b instanceof Short || b instanceof Integer)
          return evaluate(a.floatValue(), b.floatValue());

        if (b instanceof Double || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigInt)
          bigDecimal = new BigDecimal(((BigInt)b).toString()); // FIXME: Bad performance
        else if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else
          throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());

        return evaluate(BigDecimal.valueOf(a.floatValue()), bigDecimal);
      }

      if (a instanceof Double) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigInt)
          bigDecimal = new BigDecimal(((BigInt)b).toString()); // FIXME: Bad performance
        else if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else
          throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());

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

        if (b instanceof BigInt)
          return evaluate(new BigInt(a.longValue()), (BigInt)b);

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
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

        if (b instanceof BigInt)
          return evaluate(new BigInt(a.longValue()), (BigInt)b);

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
      }

      if (a instanceof Integer) {
        if (b instanceof Float || b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte || b instanceof Short || b instanceof Integer)
          return evaluate(a.intValue(), b.intValue());

        if (b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigInt)
          return evaluate(new BigInt(a.longValue()), (BigInt)b);

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
      }

      if (a instanceof Long) {
        if (b instanceof Float || b instanceof Double)
          return evaluate(a.doubleValue(), b.doubleValue());

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate(a.longValue(), b.longValue());

        if (b instanceof BigInt)
          return evaluate(new BigInt(a.longValue()), (BigInt)b);

        if (b instanceof BigDecimal)
          return evaluate(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b);

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
      }

      if (a instanceof BigInt) {
        if (b instanceof BigInt)
          return evaluate((BigInt)a, (BigInt)b);

        if (b instanceof BigDecimal)
          return evaluate(((BigInt)a).toBigDecimal(), (BigDecimal)b);

        if (b instanceof Float || b instanceof Double)
          return evaluate(a, BigDecimal.valueOf(b.doubleValue()));

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate((BigInt)a, new BigInt(b.longValue()));

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
      }

      if (a instanceof BigDecimal) {
        if (b instanceof BigInt)
          return evaluate((BigDecimal)a, ((BigInt)b).toBigDecimal());

        if (b instanceof BigDecimal)
          return evaluate((BigDecimal)a, (BigDecimal)b);

        if (b instanceof Float || b instanceof Double)
          return evaluate((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()));

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate((BigDecimal)a, BigDecimal.valueOf(b.longValue()));

        throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
      }

      throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());
    }

    abstract float evaluate(float a, float b);
    abstract double evaluate(double a, double b);
    abstract Number evaluate(byte a, byte b);
    abstract Number evaluate(short a, short b);
    abstract Number evaluate(int a, int b);
    abstract Number evaluate(long a, long b);
    abstract Number evaluate(BigInt a, BigInt b);
    abstract Number evaluate(BigDecimal a, BigDecimal b);
  }

  abstract static class Boolean extends Generic {
    static final Boolean AND = new Boolean("AND") {
      @Override
      java.lang.Boolean evaluate(final Condition<java.lang.Boolean> a, final Condition<java.lang.Boolean> b) {
        return a == null || b == null || a.isNull() || b.isNull() ? null : a.get() && b.get();
      }
    };
    static final Boolean OR = new Boolean("OR") {
      @Override
      java.lang.Boolean evaluate(final Condition<java.lang.Boolean> a, final Condition<java.lang.Boolean> b) {
        return a == null || b == null || a.isNull() || b.isNull() ? null : a.get() || b.get();
      }
    };

    Boolean(final java.lang.String symbol) {
      super(symbol);
    }

    abstract java.lang.Boolean evaluate(Condition<java.lang.Boolean> a, Condition<java.lang.Boolean> b);
  }

  abstract static class Logical<D> extends Generic {
    static final Logical<type.DataType<?>> EQ = new Logical<type.DataType<?>>("=") {
      @Override
      java.lang.Boolean evaluate(final type.DataType<?> a, final type.DataType<?> b) {
        return a == null || b == null ? null : a.equals(b);
      }
    };
    static final Logical<Comparable<Object>> LT = new Logical<Comparable<Object>>("<") {
      @Override
      java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return a == null || b == null ? null : a.compareTo(b) < 0;
      }
    };
    static final Logical<Comparable<Object>> LTE = new Logical<Comparable<Object>>("<=") {
      @Override
      java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return a != null && a.compareTo(b) <= 0;
      }
    };
    static final Logical<Comparable<Object>> GT = new Logical<Comparable<Object>>(">") {
      @Override
      java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return LT.evaluate(b, a);
      }
    };
    static final Logical<Comparable<Object>> GTE = new Logical<Comparable<Object>>(">=") {
      @Override
      java.lang.Boolean evaluate(final Comparable<Object> a, final Comparable<Object> b) {
        return LTE.evaluate(b, a);
      }
    };
    static final Logical<type.DataType<?>> NE = new Logical<type.DataType<?>>("<>") {
      @Override
      java.lang.Boolean evaluate(final type.DataType<?> a, final type.DataType<?> b) {
        return !EQ.evaluate(a, b);
      }
    };

    Logical(final java.lang.String symbol) {
      super(symbol);
    }

    abstract java.lang.Boolean evaluate(D a, D b);
  }

  abstract static class String extends Generic {
    static final String1 LOWER_CASE = new String1("LOWER") {
      @Override
      java.lang.String evaluate(final java.lang.String a) {
        return a == null ? null : a.toLowerCase();
      }
    };

    static final String1 UPPER_CASE = new String1("UPPER") {
      @Override
      java.lang.String evaluate(final java.lang.String a) {
        return a == null ? null : a.toUpperCase();
      }
    };

    String(final java.lang.String symbol) {
      super(symbol);
    }
  }

  abstract static class String1 extends String {
    String1(final java.lang.String symbol) {
      super(symbol);
    }

    abstract java.lang.String evaluate(java.lang.String a);
  }

  abstract static class StringN extends String {
    StringN(final java.lang.String symbol) {
      super(symbol);
    }

    abstract java.lang.String evaluate(java.lang.String ... strings);
  }

  static class Ordering extends Generic {
    static final Ordering IS = new Ordering("IS");
    static final Ordering NOT = new Ordering("IS NOT");

    static final Ordering ASC = new Ordering("ASC");
    static final Ordering DESC = new Ordering("DESC");

    Ordering(final java.lang.String symbol) {
      super(symbol);
    }
  }

  private operator() {
  }
}