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

import org.libj.math.BigInt;

public final class UNSIGNED {
  abstract static class UnsignedNumber<N extends Number> extends Number {
    private static final long serialVersionUID = 7628853114521559991L;

    abstract N value();
    abstract Class<? extends type.DataType<?>> getTypeClass();
  }

  public static final class Float extends UnsignedNumber<java.lang.Float> {
    private static final long serialVersionUID = -8375720072364561556L;
    private final float value;

    Float(final float value) {
      if (value < 0)
        throw new IllegalArgumentException(value + " must be positive");

      this.value = value;
    }

    @Override
    java.lang.Float value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.FLOAT.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return (int)value;
    }

    @Override
    public long longValue() {
      return (long)value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static final class Double extends UnsignedNumber<java.lang.Double> {
    private static final long serialVersionUID = -4914395179413988303L;
    private final double value;

    Double(final double value) {
      if (value < 0)
        throw new IllegalArgumentException(value + " must be positive");

      this.value = value;
    }

    @Override
    java.lang.Double value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.DOUBLE.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return (int)value;
    }

    @Override
    public long longValue() {
      return (long)value;
    }

    @Override
    public float floatValue() {
      return (float)value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static final class BigDecimal extends UnsignedNumber<java.math.BigDecimal> {
    private static final long serialVersionUID = 9193891427427757209L;
    private final java.math.BigDecimal value;

    BigDecimal(final java.math.BigDecimal value) {
      if (value.signum() < 0)
        throw new IllegalArgumentException(value + " must be positive");

      this.value = value;
    }

    @Override
    java.math.BigDecimal value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.DECIMAL.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return value.intValue();
    }

    @Override
    public long longValue() {
      return value.longValue();
    }

    @Override
    public float floatValue() {
      return value.floatValue();
    }

    @Override
    public double doubleValue() {
      return value.doubleValue();
    }
  }

  public static final class Byte extends UnsignedNumber<java.lang.Short> {
    private static final long serialVersionUID = 5786848776445446242L;
    private final short value;

    Byte(final short value) {
      if (value < 0)
        throw new IllegalArgumentException(value + " must be positive");

      if ((value >> 8) != 0)
        throw new ArithmeticException(value + " is too big to fit in 8 bits of an unsigned byte");

      this.value = value;
    }

    @Override
    java.lang.Short value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.TINYINT.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static final class Short extends UnsignedNumber<java.lang.Integer> {
    private static final long serialVersionUID = 565230929953326486L;
    private final int value;

    Short(final int value) {
      if (value < 0)
        throw new IllegalArgumentException(value + " must be positive");

      if ((value >> 16) != 0)
        throw new ArithmeticException(value + " is too big to fit in 16 bits of an unsigned short");

      this.value = value;
    }

    @Override
    java.lang.Integer value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.SMALLINT.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static final class Integer extends UnsignedNumber<java.lang.Long> {
    private static final long serialVersionUID = -8558598418279135566L;
    private final long value;

    Integer(final long value) {
      if (value < 0)
        throw new IllegalArgumentException(value + " must be positive");

      if ((value >> 32) != 0)
        throw new ArithmeticException(value + " is too big to fit in 32 bits of an unsigned int");

      this.value = value;
    }

    @Override
    java.lang.Long value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.INT.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return (int)value;
    }

    @Override
    public long longValue() {
      return value;
    }

    @Override
    public float floatValue() {
      return value;
    }

    @Override
    public double doubleValue() {
      return value;
    }
  }

  public static final class Long extends UnsignedNumber<BigInt> {
    private static final long serialVersionUID = 9034890203684695014L;
    private final BigInt value;

    Long(final BigInt value) {
      if (value.signum() < 0)
        throw new IllegalArgumentException(value + " must be positive");

      if (value.bitLength() > 64)
        throw new ArithmeticException(value + " is too big to fit in 64 bits of an unsigned long");

      this.value = value;
    }

    @Override
    BigInt value() {
      return value;
    }

    @Override
    Class<? extends type.DataType<?>> getTypeClass() {
      return type.BIGINT.UNSIGNED.class;
    }

    @Override
    public int intValue() {
      return value.intValue();
    }

    @Override
    public long longValue() {
      return value.longValue();
    }

    @Override
    public float floatValue() {
      return value.floatValue();
    }

    @Override
    public double doubleValue() {
      return value.doubleValue();
    }
  }

  private UNSIGNED() {
  }
}