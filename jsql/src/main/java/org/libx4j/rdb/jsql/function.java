/* Copyright (c) 2017 lib4j
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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Set;

import org.lib4j.lang.Numbers;
import org.lib4j.math.BigDecimals;
import org.lib4j.math.SafeMath;

final class function {
  private static final MathContext mc = new MathContext(65, RoundingMode.HALF_UP);
  private static final BigDecimal LOG_2 = SafeMath.log(BigDecimals.TWO, mc);
  private static final BigDecimal LOG_10 = SafeMath.log(BigDecimal.TEN, mc);

  protected static abstract class Generic extends expression.Generic<Number> {
    protected final Compilable a;
    protected final Compilable b;

    protected Generic(final type.Numeric<?> a, final type.Numeric<?> b) {
      this.a = (Compilable)a;
      this.b = (Compilable)b;
    }

    protected Generic(final type.Numeric<?> a, final Number b) {
      this.a = (Compilable)a;
      this.b = data.DataType.wrap(b);
    }

    protected Generic(final Number a, final type.Numeric<?> b) {
      this.a = data.DataType.wrap(a);
      this.b = (Compilable)b;
    }
  }

  protected static abstract class Function0 extends Generic {
    protected Function0() {
      super((type.Numeric<?>)null, (type.Numeric<?>)null);
    }
  }

  protected static abstract class Function1 extends Generic {
    protected Function1(final type.Numeric<?> dataType) {
      super(dataType, (type.Numeric<?>)null);
    }

    @Override
    protected final Number evaluate(final Set<Evaluable> visited) {
      if (a == null || !(a instanceof Evaluable))
        return null;

      final Number evaluated = (Number)((Evaluable)a).evaluate(visited);
      return evaluated == null ? null : evaluate(evaluated);
    }

    protected abstract Number evaluate(final Number a);
  }

  protected static abstract class Function2 extends Generic {
    protected Function2(final type.Numeric<?> a, final type.Numeric<?> b) {
      super(a, b);
    }

    protected Function2(final type.Numeric<?> a, final Number b) {
      super(a, b);
    }

    protected Function2(final Number a, final type.Numeric<?> b) {
      super(a, b);
    }

    @Override
    protected final Number evaluate(final Set<Evaluable> visited) {
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

    protected abstract Number evaluate(final Number a, final Number b);
  }

  static final class Pi extends Function0 {
    protected Pi() {
      super();
    }

    @Override
    protected Number evaluate(final Set<Evaluable> visited) {
      return Math.PI;
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Abs extends Function1 {
    protected Abs(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Float)
        return SafeMath.abs(a.floatValue());

      if (a instanceof Double)
        return SafeMath.abs(a.doubleValue());

      if (a instanceof Byte || a instanceof Short || a instanceof Integer)
        return SafeMath.abs(a.intValue());

      if (a instanceof Long)
        return SafeMath.abs(a.longValue());

      if (a instanceof BigInteger)
        return SafeMath.abs((BigInteger)a);

      if (a instanceof BigDecimal)
        return SafeMath.abs((BigDecimal)a);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Sign extends Function1 {
    protected Sign(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigInteger)
        return BigInteger.valueOf(((BigInteger)a).signum());

      if (a instanceof BigDecimal)
        return BigDecimal.valueOf(((BigDecimal)a).signum());

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Round extends Function2 {
    protected Round(final type.Numeric<?> a, final type.Numeric<?> b) {
      super(a, b);
    }

    protected Round(final type.Numeric<?> a, final Number b) {
      super(a, b);
    }

    @Override
    protected Number evaluate(final Number a, final Number b) {
      if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInteger)
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
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Floor extends Function1 {
    protected Floor(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInteger)
        return a;

      if (a instanceof Float)
        return (float)SafeMath.floor(a.floatValue());

      if (a instanceof Double)
        return SafeMath.floor(a.doubleValue());

      if (a instanceof BigDecimal)
        return SafeMath.floor((BigDecimal)a);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Ceil extends Function1 {
    protected Ceil(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long || a instanceof BigInteger)
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
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Sqrt extends Function1 {
    protected Sqrt(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.sqrt((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.sqrt((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Pow extends Function2 {
    protected Pow(final type.Numeric<?> a, final type.Numeric<?> b) {
      super(a, b);
    }

    protected Pow(final type.Numeric<?> a, final Number b) {
      super(a, b);
    }

    protected Pow(final Number a, final type.Numeric<?> b) {
      super(a, b);
    }

    @Override
    protected Number evaluate(final Number a, final Number b) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.pow(a.doubleValue(), b.doubleValue());

        if (b instanceof BigDecimal)
          return SafeMath.pow(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.pow(BigDecimal.valueOf(a.doubleValue()), new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      final BigDecimal bigDecimal;
      if (a instanceof BigDecimal)
        bigDecimal = (BigDecimal)a;
      else if (a instanceof BigDecimal)
        bigDecimal = new BigDecimal((BigInteger)a);
      else
        throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");

      if (b instanceof Float || b instanceof Double)
        return SafeMath.pow(bigDecimal, BigDecimal.valueOf(b.doubleValue()), mc);

      if (b instanceof BigDecimal)
        return SafeMath.pow(bigDecimal, (BigDecimal)b, mc);

      if (b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
        return SafeMath.pow(bigDecimal, BigDecimal.valueOf(b.longValue()), mc);

      if (b instanceof BigInteger)
        return SafeMath.pow(bigDecimal, new BigDecimal((BigInteger)b), mc);

      throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Mod extends Function2 {
    protected Mod(final type.Numeric<?> a, final type.Numeric<?> b) {
      super(a, b);
    }

    protected Mod(final type.Numeric<?> a, final Number b) {
      super(a, b);
    }

    protected Mod(final Number a, final type.Numeric<?> b) {
      super(a, b);
    }

    @Override
    protected Number evaluate(final Number a, final Number b) {
      if (a instanceof Float) {
        if (b instanceof Float)
          return (float)(a.floatValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.floatValue() % b.doubleValue());

        if (b instanceof Byte)
          return (float)(a.floatValue() % b.byteValue());

        if (b instanceof Short)
          return (float)(a.floatValue() % b.shortValue());

        if (b instanceof Integer)
          return (float)(a.floatValue() % b.intValue());

        if (b instanceof Long)
          return (float)(a.floatValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.floatValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigDecimal.valueOf(a.floatValue()).remainder(new BigDecimal((BigInteger)b)).floatValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Double) {
        if (b instanceof Float)
          return (double)(a.doubleValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.doubleValue() % b.doubleValue());

        if (b instanceof Byte)
          return (double)(a.doubleValue() % b.byteValue());

        if (b instanceof Short)
          return (double)(a.doubleValue() % b.shortValue());

        if (b instanceof Integer)
          return (double)(a.doubleValue() % b.intValue());

        if (b instanceof Long)
          return (double)(a.doubleValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.doubleValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigDecimal.valueOf(a.doubleValue()).remainder(new BigDecimal((BigInteger)b)).doubleValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Byte) {
        if (b instanceof Float)
          return (float)(a.byteValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.byteValue() % b.doubleValue());

        if (b instanceof Byte)
          return (byte)(a.byteValue() % b.byteValue());

        if (b instanceof Short)
          return (byte)(a.byteValue() % b.shortValue());

        if (b instanceof Integer)
          return (byte)(a.byteValue() % b.intValue());

        if (b instanceof Long)
          return (byte)(a.byteValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.byteValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigInteger.valueOf(a.byteValue()).mod((BigInteger)b).byteValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Short) {
        if (b instanceof Float)
          return (float)(a.shortValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.shortValue() % b.doubleValue());

        if (b instanceof Byte)
          return (byte)(a.shortValue() % b.byteValue());

        if (b instanceof Short)
          return (short)(a.shortValue() % b.shortValue());

        if (b instanceof Integer)
          return (short)(a.shortValue() % b.intValue());

        if (b instanceof Long)
          return (short)(a.shortValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.shortValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigInteger.valueOf(a.shortValue()).mod((BigInteger)b).shortValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Integer) {
        if (b instanceof Float)
          return (float)(a.intValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.intValue() % b.doubleValue());

        if (b instanceof Byte)
          return (byte)(a.intValue() % b.byteValue());

        if (b instanceof Short)
          return (short)(a.intValue() % b.shortValue());

        if (b instanceof Integer)
          return (int)(a.intValue() % b.intValue());

        if (b instanceof Long)
          return (int)(a.intValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.intValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigInteger.valueOf(a.intValue()).mod((BigInteger)b).intValue();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof Long) {
        if (b instanceof Float)
          return (float)(a.longValue() % b.floatValue());

        if (b instanceof Double)
          return (double)(a.longValue() % b.doubleValue());

        if (b instanceof Byte)
          return (byte)(a.longValue() % b.byteValue());

        if (b instanceof Short)
          return (short)(a.longValue() % b.shortValue());

        if (b instanceof Integer)
          return (int)(a.longValue() % b.intValue());

        if (b instanceof Long)
          return (long)(a.longValue() % b.longValue());

        if (b instanceof BigDecimal)
          return BigDecimal.valueOf(a.longValue()).remainder((BigDecimal)b);

        if (b instanceof BigInteger)
          return BigInteger.valueOf(a.longValue()).mod((BigInteger)b).longValue();

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

        if (b instanceof BigInteger)
          return ((BigDecimal)a).remainder(new BigDecimal((BigInteger)b));

        if (b instanceof BigDecimal)
          return ((BigDecimal)a).remainder((BigDecimal)b);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInteger) {
        if (b instanceof Float)
          return new BigDecimal((BigInteger)a).remainder(BigDecimal.valueOf(b.floatValue())).floatValue();

        if (b instanceof Double)
          return new BigDecimal((BigInteger)a).remainder(BigDecimal.valueOf(b.doubleValue())).doubleValue();

        if (b instanceof Byte)
          return ((BigInteger)a).mod(BigInteger.valueOf(b.byteValue())).byteValue();

        if (b instanceof Short)
          return ((BigInteger)a).mod(BigInteger.valueOf(b.shortValue())).shortValue();

        if (b instanceof Integer)
          return ((BigInteger)a).mod(BigInteger.valueOf(b.intValue())).intValue();

        if (b instanceof Long)
          return ((BigInteger)a).mod(BigInteger.valueOf(b.longValue())).longValue();

        if (b instanceof BigInteger)
          return ((BigInteger)a).mod((BigInteger)b);

        if (b instanceof BigDecimal)
          return new BigDecimal((BigInteger)a).remainder((BigDecimal)b).toBigInteger();

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Sin extends Function1 {
    protected Sin(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.sin((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.sin((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Asin extends Function1 {
    protected Asin(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.asin((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.asin((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Cos extends Function1 {
    protected Cos(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.cos((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.cos((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Acos extends Function1 {
    protected Acos(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.acos((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.acos((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Tan extends Function1 {
    protected Tan(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.tan((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.tan((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Atan extends Function1 {
    protected Atan(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.atan((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.atan((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Atan2 extends Function2 {
    protected Atan2(final type.Numeric<?> a, final type.Numeric<?> b) {
      super(a, b);
    }

    protected Atan2(final type.Numeric<?> a, final Number b) {
      super(a, b);
    }

    protected Atan2(final Number a, final type.Numeric<?> b) {
      super(a, b);
    }

    @Override
    protected Number evaluate(final Number a, final Number b) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2(a.doubleValue(), b.doubleValue());

        if (b instanceof BigDecimal)
          return SafeMath.atan2(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.atan2(BigDecimal.valueOf(a.doubleValue()), new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2((BigDecimal)a, (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.atan2((BigDecimal)a, new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInteger) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.atan2(new BigDecimal((BigInteger)a), BigDecimal.valueOf(b.doubleValue()), mc);

        if (b instanceof BigDecimal)
          return SafeMath.atan2(new BigDecimal((BigInteger)a), (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.atan2(new BigDecimal((BigInteger)a), new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Exp extends Function1 {
    protected Exp(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
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

      if (a instanceof BigDecimal)
        return SafeMath.exp((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.exp((BigInteger)a, mc).toBigInteger();

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Ln extends Function1 {
    protected Ln(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue());

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc);

      if (a instanceof BigInteger)
        return SafeMath.log(new BigDecimal((BigInteger)a), mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Log extends Function2 {
    protected Log(final type.Numeric<?> b, final type.Numeric<?> n) {
      super(b, n);
    }

    protected Log(final type.Numeric<?> b, final Number n) {
      super(b, n);
    }

    protected Log(final Number b, final type.Numeric<?> n) {
      super(b, n);
    }

    @Override
    protected Number evaluate(final Number a, final Number b) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log(a.doubleValue(), b.doubleValue());

        if (b instanceof BigDecimal)
          return SafeMath.log(BigDecimal.valueOf(a.doubleValue()), (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.log(BigDecimal.valueOf(a.doubleValue()), new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigDecimal) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log((BigDecimal)a, BigDecimal.valueOf(b.doubleValue()), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log((BigDecimal)a, (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.log((BigDecimal)a, new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      if (a instanceof BigInteger) {
        if (b instanceof Float || b instanceof Double || b instanceof Byte || b instanceof Short || b instanceof Integer || b instanceof Long)
          return SafeMath.log(new BigDecimal((BigInteger)a), BigDecimal.valueOf(b.doubleValue()), mc);

        if (b instanceof BigDecimal)
          return SafeMath.log(new BigDecimal((BigInteger)a), (BigDecimal)b, mc);

        if (b instanceof BigInteger)
          return SafeMath.log(new BigDecimal((BigInteger)a), new BigDecimal((BigInteger)b), mc);

        throw new UnsupportedOperationException(b.getClass().getName() + " is not a supported Number type");
      }

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Log2 extends Function1 {
    protected Log2(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue()) / Numbers.LOG_2;

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc).divide(LOG_2, mc);

      if (a instanceof BigInteger)
        return SafeMath.log(new BigDecimal((BigInteger)a), mc).divide(LOG_2, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  static final class Log10 extends Function1 {
    protected Log10(final type.Numeric<?> a) {
      super(a);
    }

    @Override
    protected Number evaluate(final Number a) {
      if (a instanceof Float || a instanceof Double || a instanceof Byte || a instanceof Short || a instanceof Integer || a instanceof Long)
        return SafeMath.log(a.doubleValue()) / Numbers.LOG_10;

      if (a instanceof BigDecimal)
        return SafeMath.log((BigDecimal)a, mc).divide(LOG_10, mc);

      if (a instanceof BigInteger)
        return SafeMath.log(new BigDecimal((BigInteger)a), mc).divide(LOG_10, mc);

      throw new UnsupportedOperationException(a.getClass().getName() + " is not a supported Number type");
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  protected static abstract class Temporal extends expression.Generic<java.time.temporal.Temporal> {
    protected final String function;

    protected Temporal(final String function) {
      this.function = function;
    }

    @Override
    protected final void compile(final Compilation compilation) {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }
  }

  private function() {
  }
}