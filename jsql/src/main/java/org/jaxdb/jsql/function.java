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
import java.util.Set;

import org.libj.math.BigInt;
import org.libj.math.SafeMath;

// FIXME: The return types need to be examined
final class function {
  // FIXME: Is this the right MathContext?
  private static final MathContext mc = new MathContext(65, RoundingMode.DOWN);

  abstract static class Generic extends expression.Generic<Number> {
    final Compilable a;
    final Compilable b;

    Generic(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    Generic(final kind.Numeric<?> a, final Number b) {
      this.a = (Compilable)a;
      this.b = type.DataType.wrap(b);
    }

    Generic(final Number a, final kind.Numeric<?> b) {
      this.a = type.DataType.wrap(a);
      this.b = (Compilable)b;
    }
  }

  abstract static class Function0 extends Generic {
    Function0() {
      super((kind.Numeric<?>)null, (kind.Numeric<?>)null);
    }
  }

  abstract static class Function1 extends Generic {
    Function1(final kind.Numeric<?> dataType) {
      super(dataType, (kind.Numeric<?>)null);
    }

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      if (a == null || !(a instanceof Evaluable))
        return null;

      final Number evaluated = (Number)((Evaluable)a).evaluate(visited);
      return evaluated == null ? null : evaluate(evaluated);
    }

    abstract Number evaluate(Number a);
  }

  abstract static class Function2 extends Generic {
    Function2(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
    }

    Function2(final kind.Numeric<?> a, final Number b) {
      super(a, b);
    }

    Function2(final Number a, final kind.Numeric<?> b) {
      super(a, b);
    }

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      if (a == null || b == null || !(a instanceof Evaluable) || !(b instanceof Evaluable))
        return null;

      final Number a = (Number)((Evaluable)this.a).evaluate(visited);
      if (a == null)
        return null;

      final Number b = (Number)((Evaluable)this.b).evaluate(visited);
      if (b == null)
        return null;

      return evaluate(a, b);
    }

    abstract Number evaluate(Number a, Number b);
  }

  static final class Pi extends Function0 {
    @Override
    Number evaluate(final Set<Evaluable> visited) {
      return Math.PI;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Abs extends Function1 {
    Abs(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Sign extends Function1 {
    Sign(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Round extends Function2 {
    Round(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
    }

    Round(final kind.Numeric<?> a, final Number b) {
      super(a, b);
    }

    @Override
    Number evaluate(final Number a, final Number b) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Floor extends Function1 {
    Floor(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Ceil extends Function1 {
    Ceil(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Degrees extends Function1 {
    Degrees(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Radians extends Function1 {
    Radians(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Sqrt extends Function1 {
    Sqrt(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Pow extends Function2 {
    Pow(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
    }

    Pow(final kind.Numeric<?> a, final Number b) {
      super(a, b);
    }

    Pow(final Number a, final kind.Numeric<?> b) {
      super(a, b);
    }

    @Override
    Number evaluate(final Number a, final Number b) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Mod extends Function2 {
    Mod(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
    }

    Mod(final kind.Numeric<?> a, final Number b) {
      super(a, b);
    }

    Mod(final Number a, final kind.Numeric<?> b) {
      super(a, b);
    }

    @Override
    Number evaluate(final Number a, final Number b) {
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
          return(a.byteValue() % b.floatValue());

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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Sin extends Function1 {
    Sin(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Asin extends Function1 {
    Asin(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Cos extends Function1 {
    Cos(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Acos extends Function1 {
    Acos(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Tan extends Function1 {
    Tan(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Atan extends Function1 {
    Atan(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Atan2 extends Function2 {
    Atan2(final kind.Numeric<?> a, final kind.Numeric<?> b) {
      super(a, b);
    }

    Atan2(final kind.Numeric<?> a, final Number b) {
      super(a, b);
    }

    Atan2(final Number a, final kind.Numeric<?> b) {
      super(a, b);
    }

    @Override
    Number evaluate(final Number a, final Number b) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Exp extends Function1 {
    Exp(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Ln extends Function1 {
    Ln(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.floatValue());

      if (a instanceof BigInt) // FIXME: Implement efficient alternative
        return SafeMath.log(((BigInt)a).toBigDecimal(), mc);

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Log extends Function2 {
    Log(final kind.Numeric<?> b, final kind.Numeric<?> n) {
      super(b, n);
    }

    Log(final kind.Numeric<?> b, final Number n) {
      super(b, n);
    }

    Log(final Number b, final kind.Numeric<?> n) {
      super(b, n);
    }

    @Override
    Number evaluate(final Number a, final Number b) {
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

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Log2 extends Function1 {
    Log2(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log2(a.doubleValue());

      if (a instanceof BigInt)
        return SafeMath.log2(((BigInt)a).toBigDecimal(), mc);

      if (a instanceof BigDecimal)
        return SafeMath.log2((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  static final class Log10 extends Function1 {
    Log10(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log10(a.doubleValue());

      if (a instanceof BigInt)
        return SafeMath.log10(((BigInt)a).toBigDecimal(), mc);

      if (a instanceof BigDecimal)
        return SafeMath.log10((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException {
      compilation.compiler.compile(this, compilation);
    }
  }

  abstract static class Temporal extends expression.Generic<java.time.temporal.Temporal> {
    final String function;

    Temporal(final String function) {
      this.function = function;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) {
      compilation.compiler.compile(this, compilation);
    }
  }

  private function() {
  }
}