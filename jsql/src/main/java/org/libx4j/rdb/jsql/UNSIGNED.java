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

import org.libx4j.rdb.jsql.data.DataType;

public final class UNSIGNED {
  protected static abstract class UnsignedNumber<N extends Number> extends Number {
    private static final long serialVersionUID = 7628853114521559991L;

    protected abstract N value();
    protected abstract Class<? extends data.DataType<?>> getTypeClass();
  }

  public static UnsignedNumber<?> toUnsigned(final Number number) {
    if (number == null)
      return null;

    if (number.doubleValue() < 0)
      throw new IllegalArgumentException("number < 0: " + number);

    if (number instanceof Float)
      return new Float(number.floatValue());

    if (number instanceof Double)
      return new Double(number.doubleValue());

    if (number instanceof java.math.BigDecimal)
      return new BigDecimal((java.math.BigDecimal)number);

    if (number instanceof Byte || number instanceof Short)
      return new Byte(number.shortValue());

    if (number instanceof Integer)
      return new Short(number.intValue());

    if (number instanceof Long)
      return new Integer(number.longValue());

    if (number instanceof java.math.BigInteger)
      return new Long((java.math.BigInteger)number);

    throw new UnsupportedOperationException("Unsupported Number type: " + number.getClass().getName());
  }

  public static final class Float extends UnsignedNumber<java.lang.Float> {
    private static final long serialVersionUID = -8375720072364561556L;
    private final float value;

    public Float(final float value) {
      assert(value >= 0);
      this.value = value;
    }

    @Override
    protected java.lang.Float value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.FLOAT.UNSIGNED.class;
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

    public Double(final double value) {
      assert(value >= 0);
      this.value = value;
    }

    @Override
    protected java.lang.Double value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.DOUBLE.UNSIGNED.class;
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

    public BigDecimal(final java.math.BigDecimal value) {
      assert(value != null);
      assert(value.signum() >= 0);
      this.value = value;
    }

    @Override
    protected java.math.BigDecimal value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.DECIMAL.UNSIGNED.class;
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

    public Byte(final short value) {
      assert(value >= 0);
      this.value = value;
    }

    @Override
    protected java.lang.Short value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.TINYINT.UNSIGNED.class;
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

    public Short(final int value) {
      assert(value >= 0);
      this.value = value;
    }

    @Override
    protected java.lang.Integer value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.SMALLINT.UNSIGNED.class;
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

    public Integer(final long value) {
      assert(value >= 0);
      this.value = value;
    }

    @Override
    protected java.lang.Long value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.INT.UNSIGNED.class;
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

  public static final class Long extends UnsignedNumber<java.math.BigInteger> {
    private static final long serialVersionUID = 9034890203684695014L;
    private final java.math.BigInteger value;

    public Long(final java.math.BigInteger value) {
      assert(value != null);
      assert(value.signum() >= 0);
      this.value = value;
    }

    @Override
    protected java.math.BigInteger value() {
      return value;
    }

    @Override
    protected Class<? extends DataType<?>> getTypeClass() {
      return data.BIGINT.UNSIGNED.class;
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
}