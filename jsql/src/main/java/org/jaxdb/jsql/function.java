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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.libj.math.BigInt;
import org.libj.math.SafeMath;
import org.libj.util.ArrayUtil;

final class function {
  // FIXME: Is this the right MathContext?
  private static final MathContext mc = new MathContext(65, RoundingMode.DOWN);

  static final class Function implements operation.Operation {
    static final class PI extends expression.Expression<type.DOUBLE,data.DOUBLE,Double> implements exp.DOUBLE {
      @Override
      data.Column<?> getColumn() {
        return new data.DOUBLE();
      }

      @Override
      Number evaluate(final java.util.Set<Evaluable> visited) {
        return Math.PI;
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        compilation.compiler.compilePi(compilation);
      }
    }

    static final class NOW extends expression.Expression<type.DATETIME,data.DATETIME,LocalDateTime> implements exp.DATETIME {
      @Override
      data.Column<?> getColumn() {
        return new data.DATETIME();
      }

      @Override
      LocalDateTime evaluate(final java.util.Set<Evaluable> visited) {
        return LocalDateTime.now();
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        compilation.compiler.compileNow(compilation);
      }
    }
  }

  abstract static class Function1 implements operation.Operation1<Number,Number> {
    static final Function1 ABS = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAbs(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return SafeMath.abs(a.floatValue());

        if (a instanceof Double)
          return SafeMath.abs(a.doubleValue());

        if (a instanceof Byte || a instanceof Short || a instanceof Integer)
          return SafeMath.abs(a.intValue());

        if (a instanceof Long)
          return SafeMath.abs(a.longValue());

        if (a instanceof BigInt)
          return ((BigInt)a).clone().abs();

        if (a instanceof BigDecimal)
          return SafeMath.abs((BigDecimal)a);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 SIGN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSign(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return SafeMath.signum(a.floatValue());

        if (a instanceof Byte)
          return (byte)SafeMath.signum(a.floatValue());

        if (a instanceof Short)
          return (short)SafeMath.signum(a.floatValue());

        if (a instanceof Double)
          return SafeMath.signum(a.doubleValue());

        if (a instanceof Integer)
          return (int)SafeMath.signum(a.doubleValue());

        if (a instanceof Long)
          return (long)SafeMath.signum(a.doubleValue());

        if (a instanceof BigInt)
          return new BigInt(((BigInt)a).signum());

        if (a instanceof BigDecimal)
          return BigDecimal.valueOf(((BigDecimal)a).signum());

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 FLOOR = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileFloor(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInt)
          return a;

        if (a instanceof Float)
          return SafeMath.floor(a.floatValue());

        if (a instanceof Double)
          return SafeMath.floor(a.doubleValue());

        if (a instanceof BigDecimal)
          return SafeMath.floor((BigDecimal)a);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 CEIL = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileCeil(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInt)
          return a;

        if (a instanceof Float)
          return SafeMath.ceil(a.floatValue());

        if (a instanceof Double)
          return SafeMath.ceil(a.doubleValue());

        if (a instanceof BigDecimal)
          return SafeMath.ceil((BigDecimal)a);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 DEGREES = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileDegrees(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        // FIXME: Not sure if casting back to a's type is correct here.
        if (a instanceof Float)
          return (float)SafeMath.toDegrees(a.doubleValue());

        if (a instanceof Double)
          return Math.toDegrees(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.toDegrees(a.floatValue());

        if (a instanceof Short)
          return (short)SafeMath.toDegrees(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.toDegrees(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.toDegrees(a.longValue());

        if (a instanceof BigInt)
          return SafeMath.toDegrees(((BigInt)a).toBigDecimal(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.toDegrees(((BigDecimal)a), mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 RADIANS = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileRadians(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        // FIXME: Not sure if casting back to a's type is correct here.
        if (a instanceof Float)
          return (float)SafeMath.toRadians(a.doubleValue());

        if (a instanceof Double)
          return Math.toRadians(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.toRadians(a.floatValue());

        if (a instanceof Short)
          return (short)SafeMath.toRadians(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.toRadians(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.toRadians(a.longValue());

        if (a instanceof BigInt)
          return SafeMath.toRadians(((BigInt)a).toBigDecimal(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.toRadians(((BigDecimal)a), mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 SQRT = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSqrt(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        // FIXME: Not sure if casting back to a's type is correct here.
        if (a instanceof Float)
          return (float)SafeMath.sqrt(a.floatValue());

        if (a instanceof Double)
          return SafeMath.sqrt(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.sqrt(a.floatValue());

        if (a instanceof Short)
          return (short)SafeMath.sqrt(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.sqrt(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.sqrt(a.longValue());

        if (a instanceof BigInt)
          return ((BigInt)a).sqrt();

        if (a instanceof BigDecimal)
          return SafeMath.sqrt((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 SIN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSin(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.sin(a.floatValue());

        if (a instanceof Double)
          return SafeMath.sin(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.sin(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.sin(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.sin(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.sin(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.sin(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.sin((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 ASIN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAsin(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.asin(a.floatValue());

        if (a instanceof Double)
          return SafeMath.asin(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.asin(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.asin(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.asin(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.asin(a.longValue());

        // FIXME: Need to figure out what should be the return type of these functions
        // FIXME: Probably: Highest possible precision type given input types
        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.asin(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.asin((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 COS = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileCos(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.cos(a.floatValue());

        if (a instanceof Double)
          return SafeMath.cos(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.cos(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.cos(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.cos(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.cos(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.cos(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.cos((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 ACOS = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAcos(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.acos(a.floatValue());

        if (a instanceof Double)
          return SafeMath.acos(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.acos(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.acos(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.acos(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.acos(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.acos(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.acos((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 TAN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileTan(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.tan(a.floatValue());

        if (a instanceof Double)
          return SafeMath.tan(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.tan(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.tan(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.tan(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.tan(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.tan(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.tan((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 ATAN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAtan(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.atan(a.floatValue());

        if (a instanceof Double)
          return SafeMath.atan(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.atan(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.atan(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.atan(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.atan(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.atan(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.atan((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 EXP = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileExp(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float)
          return (float)SafeMath.exp(a.floatValue());

        if (a instanceof Double)
          return SafeMath.exp(a.doubleValue());

        if (a instanceof Byte)
          return (byte)SafeMath.exp(a.byteValue());

        if (a instanceof Short)
          return (short)SafeMath.exp(a.shortValue());

        if (a instanceof Integer)
          return (int)SafeMath.exp(a.intValue());

        if (a instanceof Long)
          return (long)SafeMath.exp(a.longValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.exp(((BigInt)a).toBigInteger(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.exp((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 LN = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLn(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
          return SafeMath.log(a.floatValue());

        if (a instanceof BigInt) // FIXME: Implement efficient alternative
          return SafeMath.log(((BigInt)a).toBigDecimal(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.log((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 LOG2 = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLog2(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
          return SafeMath.log2(a.doubleValue());

        if (a instanceof BigInt)
          return SafeMath.log2(((BigInt)a).toBigDecimal(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.log2((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 LOG10 = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLog10(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
          return SafeMath.log10(a.doubleValue());

        if (a instanceof BigInt)
          return SafeMath.log10(((BigInt)a).toBigDecimal(), mc);

        if (a instanceof BigDecimal)
          return SafeMath.log10((BigDecimal)a, mc);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function1 ROUND = new Function1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        final Subject subject = (Subject)a;
        final data.Column<?> column = subject.getColumn();
        if (column instanceof data.ExactNumeric)
          subject.compile(compilation, true);
        else
          compilation.compiler.compileRound(a, compilation);
      }

      @Override
      public Number evaluate(final Number a) {
        if (a == null)
          return null;

        if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInt)
          return a;

        if (a instanceof Float)
          return SafeMath.round(a.floatValue(), RoundingMode.HALF_UP);

        if (a instanceof Double)
          return SafeMath.round(a.doubleValue(), RoundingMode.HALF_UP);

        if (a instanceof BigDecimal)
          return SafeMath.round((BigDecimal)a, 0, RoundingMode.HALF_UP);

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };
  }

  abstract static class Function2 implements operation.Operation2<Number> {
    static final Function2 ATAN2 = new Function2() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAtan2(a, b, compilation);
      }

      @Override
      public Number evaluate(final Number a, final Number b) {
        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.atan2(a.doubleValue(), b.doubleValue());

          if (b instanceof BigInt) // FIXME: Implement efficient alternative
            return SafeMath.atan2(BigDecimal.valueOf(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.atan2(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigInt) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.atan2(((BigInt)a).toBigDecimal(), BigDecimal.valueOf(b.doubleValue()), mc);

          if (b instanceof BigInt)
            return SafeMath.atan2(((BigInt)a).toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.atan2(((BigInt)a).toBigDecimal(), (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigDecimal) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.atan2((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()), mc);

          if (b instanceof BigInt)
            return SafeMath.atan2((BigDecimal)a, ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.atan2((BigDecimal)a, (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function2 LOG = new Function2() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLog(a, b, compilation);
      }

      @Override
      public Number evaluate(final Number a, final Number b) {
        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.log(a.doubleValue(), b.doubleValue());

          if (b instanceof BigInt) // FIXME: Implement efficient alternative
            return SafeMath.log(BigDecimal.valueOf(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.log(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigInt) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.log(((BigInt)a).toBigDecimal(), BigDecimal.valueOf(b.doubleValue()), mc);

          if (b instanceof BigInt)
            return SafeMath.log(((BigInt)a).toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.log(((BigInt)a).toBigDecimal(), (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigDecimal) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.log((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()), mc);

          if (b instanceof BigInt)
            return SafeMath.log((BigDecimal)a, ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.log((BigDecimal)a, (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function2 MOD = new Function2() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileMod(a, b, compilation);
      }

      @Override
      public Number evaluate(final Number a, final Number b) {
        if (a instanceof Float) {
          if (b instanceof Float)
            return a.floatValue() % b.floatValue();

          if (b instanceof Double)
            return a.floatValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.floatValue() % b.byteValue();

          if (b instanceof Short)
            return a.floatValue() % b.shortValue();

          if (b instanceof Integer)
            return a.floatValue() % b.intValue();

          if (b instanceof Long)
            return a.floatValue() % b.longValue();

          if (b instanceof BigInt) // FIXME: Implement efficient alternative
            return new BigDecimal(a.floatValue(), mc).remainder(((BigInt)b).toBigDecimal()).floatValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.floatValue()).remainder((BigDecimal)b).floatValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof Double) {
          if (b instanceof Float)
            return a.doubleValue() % b.floatValue();

          if (b instanceof Double)
            return a.doubleValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.doubleValue() % b.byteValue();

          if (b instanceof Short)
            return a.doubleValue() % b.shortValue();

          if (b instanceof Integer)
            return a.doubleValue() % b.intValue();

          if (b instanceof Long)
            return a.doubleValue() % b.longValue();

          if (b instanceof BigInt) // FIXME: Implement efficient alternative
            return BigDecimal.valueOf(a.doubleValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.doubleValue()).remainder((BigDecimal)b).doubleValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof Byte) {
          if (b instanceof Float)
            return (a.byteValue() % b.floatValue());

          if (b instanceof Double)
            return a.byteValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.byteValue() % b.byteValue();

          if (b instanceof Short)
            return a.byteValue() % b.shortValue();

          if (b instanceof Integer)
            return a.byteValue() % b.intValue();

          if (b instanceof Long)
            return a.byteValue() % b.longValue();

          if (b instanceof BigInt)
            return BigDecimal.valueOf(a.byteValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.byteValue()).remainder((BigDecimal)b).byteValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof Short) {
          if (b instanceof Float)
            return a.shortValue() % b.floatValue();

          if (b instanceof Double)
            return a.shortValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.shortValue() % b.byteValue();

          if (b instanceof Short)
            return a.shortValue() % b.shortValue();

          if (b instanceof Integer)
            return a.shortValue() % b.intValue();

          if (b instanceof Long)
            return a.shortValue() % b.longValue();

          if (b instanceof BigInt)
            return BigDecimal.valueOf(a.shortValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.shortValue()).remainder((BigDecimal)b).shortValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof Integer) {
          if (b instanceof Float)
            return a.intValue() % b.floatValue();

          if (b instanceof Double)
            return a.intValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.intValue() % b.byteValue();

          if (b instanceof Short)
            return a.intValue() % b.shortValue();

          if (b instanceof Integer)
            return a.intValue() % b.intValue();

          if (b instanceof Long)
            return (int)(a.intValue() % b.longValue());

          if (b instanceof BigInt)
            return BigDecimal.valueOf(a.intValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.intValue()).remainder((BigDecimal)b).intValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof Long) {
          if (b instanceof Float)
            return a.longValue() % b.floatValue();

          if (b instanceof Double)
            return a.longValue() % b.doubleValue();

          if (b instanceof Byte)
            return a.longValue() % b.byteValue();

          if (b instanceof Short)
            return a.longValue() % b.shortValue();

          if (b instanceof Integer)
            return (int)(a.longValue() % b.intValue());

          if (b instanceof Long)
            return a.longValue() % b.longValue();

          if (b instanceof BigInt)
            return BigDecimal.valueOf(a.longValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

          if (b instanceof BigDecimal)
            return BigDecimal.valueOf(a.longValue()).remainder((BigDecimal)b).longValue();

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigInt) {
          if (b instanceof Float)
            return ((BigInt)a).toBigDecimal().remainder(BigDecimal.valueOf(b.doubleValue())).floatValue();

          if (b instanceof Double)
            return ((BigInt)a).toBigDecimal().remainder(BigDecimal.valueOf(b.doubleValue())).doubleValue();

          if (b instanceof Byte)
            return ((BigInt)a).mod(new BigInt(b.byteValue())).byteValue();

          if (b instanceof Short)
            return ((BigInt)a).mod(new BigInt(b.shortValue())).shortValue();

          if (b instanceof BigInt)
            return ((BigInt)a).mod(new BigInt(b.intValue())).intValue();

          if (b instanceof Long)
            return ((BigInt)a).mod(new BigInt(b.longValue())).longValue();

          if (b instanceof BigInt)
            return ((BigInt)a).mod((BigInt)b);

          if (b instanceof BigDecimal)
            return ((BigInt)a).toBigDecimal().remainder((BigDecimal)b);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        if (a instanceof BigDecimal) {
          if (b instanceof Float)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.floatValue()));

          if (b instanceof Double)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.doubleValue()));

          if (b instanceof Byte)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.byteValue()));

          if (b instanceof Short)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.shortValue()));

          if (b instanceof Integer)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.intValue()));

          if (b instanceof Long)
            return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.longValue()));

          if (b instanceof BigInt)
            return ((BigDecimal)a).remainder(((BigInt)b).toBigDecimal());

          if (b instanceof BigDecimal)
            return ((BigDecimal)a).remainder((BigDecimal)b);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function2 POW = new Function2() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compilePow(a, b, compilation);
      }

      @Override
      public Number evaluate(final Number a, final Number b) {
        if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
          if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
            return SafeMath.pow(a.doubleValue(), b.doubleValue());

          if (b instanceof BigInt)
            return SafeMath.pow(BigDecimal.valueOf(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

          if (b instanceof BigDecimal)
            return SafeMath.pow(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

          throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
        }

        final BigDecimal bigDecimal;
        if (a instanceof BigDecimal)
          bigDecimal = (BigDecimal)a;
        else
          throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");

        if (b instanceof Float || b instanceof Double)
          return SafeMath.pow(bigDecimal, BigDecimal.valueOf(b.doubleValue()), mc);

        if (b instanceof BigDecimal)
          return SafeMath.pow(bigDecimal, (BigDecimal)b, mc);

        if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.pow(bigDecimal, BigDecimal.valueOf(b.longValue()), mc);

        if (b instanceof BigInt)
          return SafeMath.pow(bigDecimal, ((BigInt)b).toBigDecimal(), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }
    };

    static final Function2 ROUND = new Function2() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileRound(a, b, compilation);
      }

      @Override
      public Number evaluate(final Number a, final Number b) {
        if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInt)
          return a;

        if (a instanceof Float)
          return SafeMath.round(a.floatValue(), b.intValue());

        if (a instanceof Double)
          return SafeMath.round(a.doubleValue(), b.intValue());

        if (a instanceof BigDecimal)
          return SafeMath.round((BigDecimal)a, b.intValue());

        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
      }
    };
  }

  abstract static class NumericOperator2 implements operation.Operation2<Number> {
    static final Addition ADD = new Addition() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAdd(a, b, compilation);
      }

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

    static final Addition SUB = new Addition() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSub(a, b, compilation);
      }

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

    static final Arithmetic MUL = new Arithmetic() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileMul(a, b, compilation);
      }

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

    static final Arithmetic DIV = new Arithmetic() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileDiv(a, b, compilation);
      }

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
  }

  abstract static class Arithmetic extends NumericOperator2 {
    @Override
    public Number evaluate(final Number a, final Number b) {
      if (a == null || b == null)
        return null;

      if (a instanceof Float) {
        if (b instanceof Float || b instanceof Byte || b instanceof Short || b instanceof Integer)
          return evaluate(a.floatValue(), b.floatValue());

        if (b instanceof Double || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else if (b instanceof BigInt)
          bigDecimal = ((BigInt)b).toBigDecimal();
        else
          throw new UnsupportedOperationException("Unsupported Number type: " + b.getClass().getName());

        return evaluate(BigDecimal.valueOf(a.floatValue()), bigDecimal);
      }

      if (a instanceof Double) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return evaluate(a.doubleValue(), b.doubleValue());

        final BigDecimal bigDecimal;
        if (b instanceof BigDecimal)
          bigDecimal = (BigDecimal)b;
        else if (b instanceof BigInt)
          bigDecimal = ((BigInt)b).toBigDecimal();
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

  abstract static class Addition extends Arithmetic {
    abstract LocalDate evaluate(LocalDate a, Interval b);
    abstract LocalDateTime evaluate(LocalDateTime a, Interval b);
    abstract LocalTime evaluate(LocalTime a, Interval b);
  }

  static final class Varchar {
    static final StringN CONCAT = new StringN() {
      @Override
      String evaluate(final String ... strings) {
        return ArrayUtil.toString(strings, ' ');
      }
    };

    static final String1 LOWER_CASE = new String1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLower(a, compilation);
      }

      @Override
      public String evaluate(final String a) {
        return a == null ? null : a.toLowerCase();
      }
    };

    static final String1 UPPER_CASE = new String1() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileUpper(a, compilation);
      }

      @Override
      public String evaluate(final String a) {
        return a == null ? null : a.toUpperCase();
      }
    };

    static final operation.Operation1<String,Integer> LENGTH = new operation.Operation1<String,Integer>() {
      @Override
      public void compile(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileLength(a, compilation);
      }

      @Override
      public Integer evaluate(final String a) {
        return a == null ? null : a.length();
      }
    };

    static final operation.Operation3<String,Integer,Integer> SUBSTRING = new operation.Operation3<String,Integer,Integer>() {
      @Override
      public void compile(final type.Column<?> a, final type.Column<?> from, final type.Column<?> to, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSubstring(a, from, to, compilation);
      }

      @Override
      public String evaluate(final String a, final Integer b, final Integer c) {
        return a == null ? null : a.substring(b != null ? b - 1 : 0, c != null ? c - 1 : a.length());
      }
    };
  }

  abstract static class String1 implements operation.Operation1<String,String> {
  }

  abstract static class String2 implements operation.Operation2<String> {
  }

  abstract static class StringN implements operation.Operation {
    abstract String evaluate(String ... strings);
  }

  abstract static class Set implements operation.Operation {
    abstract void compile(type.Column<?> a, boolean distinct, Compilation compilation) throws IOException, SQLException;

    static final Set SUM = new Set() {
      @Override
      public void compile(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileSum(a, distinct, compilation);
      }
    };
    // FIXME: Remove all these string operators, and put them into the Compiler class
    static final Set AVG = new Set() {
      @Override
      public void compile(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileAvg(a, distinct, compilation);
      }
    };
    static final Set MAX = new Set() {
      @Override
      public void compile(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileMax(a, distinct, compilation);
      }
    };
    static final Set MIN = new Set() {
      @Override
      public void compile(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
        compilation.compiler.compileMin(a, distinct, compilation);
      }
    };
  }

  private function() {
  }
}