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

import org.libj.lang.Numbers;
import org.libj.math.BigDecimals;
import org.libj.math.BigInt;
import org.libj.math.Decimal;
import org.libj.math.SafeMath;

final class function {
  private static final MathContext mc = new MathContext(65, RoundingMode.HALF_UP);
  // FIXME: Remove these...
  private static final BigDecimal LOG_2 = SafeMath.log(BigDecimals.TWO, mc);
  private static final BigDecimal LOG_10 = SafeMath.log(BigDecimal.TEN, mc);

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
    void compile(final Compilation compilation) {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return ((Decimal)a).clone().abs();

      if (a instanceof BigDecimal)
        return SafeMath.abs((BigDecimal)a);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return new Decimal(((Decimal)a).signum());

      if (a instanceof BigDecimal)
        return BigDecimal.valueOf(((BigDecimal)a).signum());

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.round((Decimal)a, b.shortValue());

      if (a instanceof BigDecimal)
        return SafeMath.round((BigDecimal)a, b.intValue());

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.floor((Decimal)a);

      if (a instanceof BigDecimal)
        return SafeMath.floor((BigDecimal)a);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.ceil((Decimal)a);

      if (a instanceof BigDecimal)
        return SafeMath.ceil((BigDecimal)a);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Sqrt extends Function1 {
    Sqrt(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
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
        return SafeMath.sqrt((BigInt)a, mc).toBigInt();

      if (a instanceof Decimal)
        return SafeMath.sqrt((Decimal)a, mc);

      if (a instanceof BigDecimal)
        return SafeMath.sqrt((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

        if (b instanceof Decimal)
          return SafeMath.pow(new BigDecimal(a.doubleValue()), ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigInt)
          return SafeMath.pow(new BigDecimal(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.pow(new BigDecimal(a.doubleValue()), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Decimal) {
        final Decimal decimal = (Decimal)a;
        if (b instanceof Float || b instanceof Double)
          return SafeMath.pow(decimal.toBigDecimal(), new BigDecimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.pow(decimal.toBigDecimal(), ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.pow(decimal.toBigDecimal(), (BigDecimal)b, mc);

        if (b instanceof Byte || b instanceof Short || b instanceof Integer)
          return decimal.toBigDecimal().pow(b.intValue());

        if (b instanceof Long)
          return SafeMath.pow(decimal.toBigDecimal(), BigDecimal.valueOf(b.longValue()), mc);

        if (b instanceof BigInt)
          return SafeMath.pow(decimal.toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      final BigDecimal bigDecimal;
      if (a instanceof BigDecimal)
        bigDecimal = (BigDecimal)a;
      else
        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");

      if (b instanceof Float || b instanceof Double)
        return SafeMath.pow(bigDecimal, new BigDecimal(b.doubleValue()), mc);

      if (b instanceof Decimal)
        return SafeMath.pow(bigDecimal, ((Decimal)b).toBigDecimal(), mc);

      if (b instanceof BigDecimal)
        return SafeMath.pow(bigDecimal, (BigDecimal)b, mc);

      if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
        return SafeMath.pow(bigDecimal, BigDecimal.valueOf(b.longValue()), mc);

      if (b instanceof BigInt)
        return SafeMath.pow(bigDecimal, ((BigInt)b).toBigDecimal(), mc);

      throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

        if (b instanceof Decimal)
          return new Decimal(a.floatValue()).rem((Decimal)b).floatValue();

        if (b instanceof BigInt)
          return new BigDecimal(a.floatValue()).remainder(((BigInt)b).toBigDecimal()).floatValue();

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

        if (b instanceof Decimal)
          return new Decimal(a.doubleValue()).rem((Decimal)b).doubleValue();

        if (b instanceof BigInt)
          return new BigDecimal(a.doubleValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

        if (b instanceof BigDecimal)
          return new BigDecimal(a.doubleValue()).remainder((BigDecimal)b).doubleValue();

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

        if (b instanceof Decimal)
          return new Decimal(a.byteValue(), (short)0).rem((Decimal)b).byteValue();

        if (b instanceof BigInt)
          return new BigDecimal(a.byteValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

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

        if (b instanceof Decimal)
          return new Decimal(a.shortValue(), (short)0).rem((Decimal)b).shortValue();

        if (b instanceof BigInt)
          return new BigDecimal(a.shortValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

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

        if (b instanceof Decimal)
          return new Decimal(a.intValue(), (short)0).rem((Decimal)b).intValue();

        if (b instanceof BigInt)
          return new BigDecimal(a.intValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

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

        if (b instanceof Decimal) {
          if (Decimal.MIN_VALUE < a.longValue() && a.longValue() < Decimal.MAX_VALUE)
            return new Decimal(a.longValue(), (short)0).rem((Decimal)b).longValue();

          return new BigDecimal(a.longValue()).remainder(((Decimal)b).toBigDecimal()).longValue();
        }

        if (b instanceof BigInt)
          return new BigDecimal(a.longValue()).remainder(((BigInt)b).toBigDecimal()).doubleValue();

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.longValue()).remainder((BigDecimal)b).longValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Decimal) {
        if (b instanceof Float)
          return ((Decimal)a).rem(new Decimal(b.floatValue()));

        if (b instanceof Double)
          return ((Decimal)a).rem(new Decimal(b.doubleValue()));

        if (b instanceof Byte)
          return ((Decimal)a).rem(new Decimal(b.byteValue(), (short)0));

        if (b instanceof Short)
          return ((Decimal)a).rem(new Decimal(b.shortValue(), (short)0));

        if (b instanceof Integer)
          return ((Decimal)a).rem(new Decimal(b.intValue(), (short)0));

        if (b instanceof Long) {
          if (Decimal.MIN_VALUE < b.longValue() && b.longValue() < Decimal.MAX_VALUE)
            return ((Decimal)a).rem(new Decimal(b.longValue(), (short)0));

          return ((Decimal)a).toBigDecimal().remainder(((Decimal)b).toBigDecimal()).longValue();
        }

        if (b instanceof BigInt)
          return ((Decimal)a).toBigDecimal().remainder(((BigInt)b).toBigDecimal());

        if (b instanceof Decimal)
          return ((Decimal)a).rem((Decimal)b);

        if (b instanceof BigDecimal)
          return ((Decimal)a).toBigDecimal().remainder((BigDecimal)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInt) {
        if (b instanceof Float)
          return ((BigInt)a).toBigDecimal().remainder(new BigDecimal(b.floatValue())).floatValue();

        if (b instanceof Double)
          return ((BigInt)a).toBigDecimal().remainder(new BigDecimal(b.doubleValue())).doubleValue();

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

        if (b instanceof Decimal)
          return ((BigInt)a).toBigDecimal().remainder(((Decimal)b).toBigDecimal());

        if (b instanceof BigDecimal)
          return ((BigInt)a).toBigDecimal().remainder((BigDecimal)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof Float)
          return ((BigDecimal)a).remainder(BigDecimal.valueOf(b.floatValue()));

        if (b instanceof Double)
          return ((BigDecimal)a).remainder(new BigDecimal(b.doubleValue()));

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

        if (b instanceof Decimal)
          return ((BigDecimal)a).remainder(((Decimal)b).toBigDecimal());

        if (b instanceof BigDecimal)
          return ((BigDecimal)a).remainder((BigDecimal)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.sin((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.sin((BigInt)a, mc);

      if (a instanceof BigDecimal)
        return SafeMath.sin((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.asin((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.asin((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.asin((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.cos((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.cos((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.cos((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.acos((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.acos((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.acos((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.tan((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.tan((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.tan((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.atan((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.atan((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.atan((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

        if (b instanceof Decimal)
          return SafeMath.atan2(new Decimal(a.doubleValue()), (Decimal)b);

        if (b instanceof BigInt)
          return SafeMath.atan2(new BigDecimal(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2(new BigDecimal(a.doubleValue()), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Decimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2((Decimal)a, new Decimal(b.doubleValue()));

        if (b instanceof Decimal)
          return SafeMath.atan2((Decimal)a, (Decimal)b);

        if (b instanceof BigInt)
          return SafeMath.atan2(((Decimal)a).toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2(((Decimal)a).toBigDecimal(), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInt) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2(((BigInt)a).toBigDecimal(), new BigDecimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.atan2(((BigInt)a).toBigDecimal(), ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigInt)
          return SafeMath.atan2(((BigInt)a).toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2(((BigInt)a).toBigDecimal(), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2((BigDecimal)a, new BigDecimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.atan2((BigDecimal)a, ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigInt)
          return SafeMath.atan2((BigDecimal)a, ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2((BigDecimal)a, (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

      if (a instanceof Decimal)
        return SafeMath.exp((Decimal)a, mc);

      if (a instanceof BigInt)
        return SafeMath.exp((BigInt)a, mc).toBigInt();

      if (a instanceof BigDecimal)
        return SafeMath.exp((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Ln extends Function1 {
    Ln(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue());

      if (a instanceof Decimal)
        return SafeMath.log((Decimal)a);

      if (a instanceof BigInt)
        return SafeMath.log(((BigInt)a).toBigDecimal(), mc);

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
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

        if (b instanceof Decimal)
          return SafeMath.log(new Decimal(a.doubleValue()), (Decimal)b);

        if (b instanceof BigInt)
          return SafeMath.log(new BigDecimal(a.doubleValue()), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log(new BigDecimal(a.doubleValue()), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Decimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log((Decimal)a, new Decimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.log((Decimal)a, (Decimal)b, mc);

        if (b instanceof BigInt)
          return SafeMath.log((Decimal)a, ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log((Decimal)a, (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInt) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log(((BigInt)a).toBigDecimal(), new BigDecimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.log(((BigInt)a).toBigDecimal(), ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigInt)
          return SafeMath.log(((BigInt)a).toBigDecimal(), ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log(((BigInt)a).toBigDecimal(), (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log((BigDecimal)a, new BigDecimal(b.doubleValue()), mc);

        if (b instanceof Decimal)
          return SafeMath.log((BigDecimal)a, ((Decimal)b).toBigDecimal(), mc);

        if (b instanceof BigInt)
          return SafeMath.log((BigDecimal)a, ((BigInt)b).toBigDecimal(), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log((BigDecimal)a, (BigDecimal)b, mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Log2 extends Function1 {
    Log2(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue()) / Numbers.LOG_2;

      if (a instanceof Decimal)
        return SafeMath.log2((Decimal)a);

      if (a instanceof BigInt)
        return SafeMath.log(((BigInt)a).toBigDecimal(), mc).divide(LOG_2, mc);

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc).divide(LOG_2, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Log10 extends Function1 {
    Log10(final kind.Numeric<?> a) {
      super(a);
    }

    @Override
    Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue()) / Numbers.LOG_10;

      if (a instanceof Decimal)
        return SafeMath.log10((Decimal)a);

      if (a instanceof BigInt)
        return SafeMath.log(((BigInt)a).toBigDecimal(), mc).divide(LOG_10, mc);

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc).divide(LOG_10, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  abstract static class Temporal extends expression.Generic<java.time.temporal.Temporal> {
    final String function;

    Temporal(final String function) {
      this.function = function;
    }

    @Override
    final void compile(final Compilation compilation) {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  private function() {
  }
}