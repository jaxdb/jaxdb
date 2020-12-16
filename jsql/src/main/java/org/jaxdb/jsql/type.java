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

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.lang.Classes;
import org.libj.lang.Numbers;
import org.libj.math.BigInt;
import org.libj.math.FastMath;
import org.libj.util.function.Throwing;

public final class type {
  private static final IdentityHashMap<Class<?>,Class<?>> typeToGeneric = new IdentityHashMap<>();

  private static void scanMembers(final Class<?>[] members, final int i) {
    if (i == members.length)
      return;

    final Class<?> member = members[i];
    if (!Modifier.isAbstract(member.getModifiers())) {
      final Type type = Classes.getSuperclassGenericTypes(member)[0];
      if (type instanceof Class<?> && !typeToGeneric.containsKey((Class<?>)type))
        typeToGeneric.put((Class<?>)type, member);
    }

    scanMembers(members, i + 1);
    scanMembers(member.getClasses(), 0);
  }

  static {
    typeToGeneric.put(null, ENUM.class);
    scanMembers(type.class.getClasses(), 0);
  }

  private static Constructor<?> lookupDataTypeConstructor(Class<?> genericType) {
    Class<?> dataTypeClass;
    while ((dataTypeClass = typeToGeneric.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(dataTypeClass, genericType);
  }

  public abstract static class ApproxNumeric<T extends Number> extends Numeric<T> implements kind.ApproxNumeric<T> {
    ApproxNumeric(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    ApproxNumeric(final Numeric<T> copy) {
      super(copy);
    }

    ApproxNumeric() {
      super();
    }
  }

  public static final class ARRAY<T> extends Objective<T[]> implements kind.ARRAY<T[]> {
//    public static final ARRAY<?> NULL = new ARRAY();
    final DataType<T> dataType;
    private Class<T[]> type;

    ARRAY(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final boolean keyForUpdate, final Class<? extends DataType<T>> type) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.dataType = newInstance(Classes.getDeclaredConstructor(type));
    }

    @SuppressWarnings("unchecked")
    ARRAY(final ARRAY<T> copy) {
      this(copy.owner, true, copy.name, copy.unique, copy.primary, copy.nullable, copy.value, copy.generateOnInsert, copy.generateOnUpdate, copy.keyForUpdate, (Class<? extends DataType<T>>)copy.dataType.getClass());
      this.type = copy.type;
    }

    public ARRAY(final Class<? extends DataType<T>> type) {
      this(null, true, null, false, false, true, null, null, null, false, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, true, null, false, false, true, value, null, null, false, (Class<? extends DataType<T>>)value.getClass().getComponentType());
    }

    public final ARRAY<T> set(final ARRAY<T> value) {
      super.set(value);
      return this;
    }

    @Override
    final String declare(final DBVendor vendor) {
      // FIXME
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    final Class<T[]> type() {
      return type == null ? type = (Class<T[]>)Array.newInstance(dataType.type(), 0).getClass() : type;
    }

    @Override
    final int sqlType() {
      return Types.ARRAY;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setArray(parameterIndex, new SQLArray<>(this));
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (value != null)
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final java.sql.Array array = resultSet.getArray(columnIndex);
      set((T[])array.getArray());
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this, dataType);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      throw new UnsupportedOperationException();
    }

    @Override
    final ARRAY<T> wrapper(final Evaluable wrapper) {
      return (ARRAY<T>)super.wrapper(wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<>((Class<? extends DataType<T>>)dataType.getClass());
    }
  }

  public static final BIGINT BIGINT() {
    return BIGINT.NULL;
  }

  public static final BIGINT BIGINT(final int i) {
    BIGINT singleton = BIGINT.singletons.get(i);
    if (singleton == null)
      BIGINT.singletons.put(i, singleton = BIGINT.NULL.clone());

    return singleton;
  }

  public static class BIGINT extends ExactNumeric<Long> implements kind.BIGINT {
    public static final BIGINT.UNSIGNED UNSIGNED() {
      return BIGINT.UNSIGNED.NULL;
    }

    public static final BIGINT.UNSIGNED UNSIGNED(final int i) {
      BIGINT.UNSIGNED singleton = BIGINT.UNSIGNED.singletons.get(i);
      if (singleton == null)
        BIGINT.UNSIGNED.singletons.put(i, singleton = BIGINT.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ExactNumeric<BigInt> implements kind.BIGINT.UNSIGNED {
      public static final BIGINT.UNSIGNED NULL = new BIGINT.UNSIGNED();

      private static final IdentityHashMap<Integer,BIGINT.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<BigInt> type = BigInt.class;
      private static final BigInt minValue = new BigInt(0);
      private static final BigInt maxValue = new BigInt(-1, Long.MAX_VALUE);

      private final BigInt min;
      private final BigInt max;
      private BigInt value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigInt _default, final GenerateOn<? super BigInt> generateOnInsert, final GenerateOn<? super BigInt> generateOnUpdate, final boolean keyForUpdate, final int precision, final BigInt min, final BigInt max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final BIGINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigInt value) {
        super(value == null ? null : value.precision());
        this.min = null;
        this.max = null;
        if (value != null)
          set(value);
      }

      public final UNSIGNED set(final BIGINT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final BigInt value) {
        checkMutable();
        if (value != null)
          checkValue(value);

        wasSet = true;
        final boolean changed = !Objects.equals(this.value, value);
        this.value = value;
        return changed;
      }

      private final void checkValue(final BigInt value) {
        if (min != null && value.compareTo(min) < 0 || max != null && max.compareTo(value) < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value.signum() < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      @Override
      public BigInt get() {
        return value;
      }

      @Override
      boolean isNull() {
        return value == null;
      }

      @Override
      public final BigInt min() {
        return min;
      }

      @Override
      public final BigInt max() {
        return max;
      }

      @Override
      final int scale() {
        return 0;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final BigInt minValue() {
        return minValue;
      }

      @Override
      final BigInt maxValue() {
        return maxValue;
      }

      @Override
      final int maxPrecision() {
        return 20;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().compileInt64((byte)precision(), unsigned());
      }

      @Override
      final Class<BigInt> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.BIGINT;
      }

      @Override
      final String primitiveToString() {
        throw new UnsupportedOperationException();
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (value == null)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setObject(parameterIndex, value.toString(), sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (value != null)
          resultSet.updateObject(columnIndex, value.toString(), sqlType());
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final Object value = resultSet.getObject(columnIndex);
        if (value == null)
          this.value = null;
        else if (value instanceof BigDecimal)
          this.value = new BigInt(((BigDecimal)value).toBigInteger());
        else if (value instanceof Long)
          this.value = new BigInt((Long)value);
        else if (value instanceof Integer)
          this.value = new BigInt((Integer)value);
        else if (value instanceof Double)
          this.value = new BigInt(((Double)value).longValue());
        else
          throw new UnsupportedOperationException("Unsupported class for BIGINT.UNSIGNED data type: " + value.getClass().getName());
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          // FIXME: Why precision() + 1?
          return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          // FIXME: Why precision() + 1?
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          // FIXME: Why precision() + 1?
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          // FIXME: Why precision() + 1?
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        if (dataType instanceof ExactNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new BIGINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1)) : new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final BIGINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (BIGINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return value.compareTo(new BigInt(((TINYINT)o).value));

        if (o instanceof TINYINT.UNSIGNED)
          return value.compareTo(new BigInt(((TINYINT.UNSIGNED)o).value));

        if (o instanceof SMALLINT)
          return value.compareTo(new BigInt(((SMALLINT)o).value));

        if (o instanceof SMALLINT.UNSIGNED)
          return value.compareTo(new BigInt(((SMALLINT.UNSIGNED)o).value));

        if (o instanceof INT)
          return value.compareTo(new BigInt(((INT)o).value));

        if (o instanceof INT.UNSIGNED)
          return value.compareTo(new BigInt(((INT.UNSIGNED)o).value));

        if (o instanceof BIGINT)
          return BigInt.compareTo(value.val(), BigInt.valueOf(((BIGINT)o).value));

        if (o instanceof BIGINT.UNSIGNED)
          return value.compareTo(((BIGINT.UNSIGNED)o).value);

        if (o instanceof FLOAT || o instanceof FLOAT.UNSIGNED)
          return Float.compare(value.floatValue(), ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Float.compare(value.floatValue(), ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Double.compare(value.doubleValue(), ((FLOAT)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Double.compare(value.doubleValue(), ((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return value.toBigDecimal().compareTo(((DECIMAL)o).value);

        if (o instanceof DECIMAL.UNSIGNED)
          return value.toBigDecimal().compareTo(((DECIMAL.UNSIGNED)o).value);

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final BIGINT.UNSIGNED clone() {
        return new BIGINT.UNSIGNED(this);
      }

      @Override
      public String toString() {
        return value == null ? "NULL" : value.toString();
      }
    }

    public static final BIGINT NULL = new BIGINT();

    private static final IdentityHashMap<Integer,BIGINT> singletons = new IdentityHashMap<>();
    private static final Class<Long> type = Long.class;

    private final Long min;
    private final Long max;
    private boolean isNull = true;
    private long value;

    BIGINT(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    BIGINT(final BIGINT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public BIGINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    public BIGINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public BIGINT(final Long value) {
      super(value == null ? null : Integer.valueOf(Numbers.precision(value)));
      this.min = null;
      this.max = null;
      if (!(isNull = value == null))
        set(value);
    }

    public BIGINT(final long value) {
      super(Integer.valueOf(Numbers.precision(value)));
      this.min = null;
      this.max = null;
      set(value);
    }

    public final BIGINT set(final BIGINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Long value) {
      return value != null ? set((long)value) : isNull && (isNull = true);
    }

    public final boolean set(final long value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final long value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public long getAsLong() {
      return value;
    }

    @Override
    public Long get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Long min() {
      return min;
    }

    @Override
    public final Long max() {
      return max;
    }

    @Override
    final int scale() {
      return 0;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final Long minValue() {
      return -9223372036854775808L;
    }

    @Override
    final Long maxValue() {
      return 9223372036854775807L;
    }

    @Override
    final int maxPrecision() {
      return 19;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileInt64((byte)precision(), unsigned());
    }

    @Override
    final Class<Long> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.BIGINT;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setLong(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateLong(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final long value = resultSet.getLong(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof ExactNumeric)
        return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final BIGINT wrapper(final Evaluable wrapper) {
      return (BIGINT)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Long.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Long.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Long.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Long.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Long.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Long.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      if (o instanceof DECIMAL.UNSIGNED)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final BIGINT clone() {
      return new BIGINT(this);
    }
  }

  public static final BINARY BINARY() {
    return BINARY.NULL;
  }

  public static final BINARY BINARY(final int i) {
    BINARY singleton = BINARY.singletons.get(i);
    if (singleton == null)
      BINARY.singletons.put(i, singleton = BINARY.NULL.clone());

    return singleton;
  }

  public static class BINARY extends Objective<byte[]> implements kind.BINARY {
    public static final BINARY NULL = new BINARY((byte[])null);

    private static final IdentityHashMap<Integer,BINARY> singletons = new IdentityHashMap<>();
    private static final Class<byte[]> type = byte[].class;

    private final long length;
    private final boolean varying;

    BINARY(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final byte[] _default, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    BINARY(final BINARY copy) {
      super(copy);
      this.length = copy.length;
      this.varying = copy.varying;
    }

    public BINARY(final long length, final boolean varying) {
      super();
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    public BINARY(final long length) {
      this(length, false);
    }

    public BINARY(final byte[] value) {
      super();
      this.length = value == null ? 0 : value.length;
      this.varying = false;
      set(value);
    }

    BINARY() {
      this(0, false);
    }

    private void checkLength(final long length) {
      if (length <= 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " illegal length: " + length);
    }

    public final long length() {
      return length;
    }

    public final BINARY set(final BINARY value) {
      super.set(value);
      return this;
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileBinary(varying, length);
    }

    @Override
    final Class<byte[]> type() {
      return type;
    }

    @Override
    final int sqlType() {
      // FIXME: Does it matter if we know if this is BIT, BINARY, VARBINARY, or LONGVARBINARY?
      return varying ? Types.VARBINARY : Types.BINARY;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (value == null)
        statement.setNull(parameterIndex, statement.getParameterMetaData().getParameterType(parameterIndex));
      else
        statement.setBytes(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (value == null)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBytes(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final int columnType = resultSet.getMetaData().getColumnType(columnIndex);
      // FIXME: IS it right to support BIT here? Or should it be in BOOLEAN?
      if (columnType == Types.BIT)
        this.value = new byte[] {resultSet.getBoolean(columnIndex) ? (byte)0x01 : (byte)0x00};
      else
        this.value = resultSet.getBytes(columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BINARY)
        return new BINARY(Math.max(length(), ((BINARY)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final BINARY wrapper(final Evaluable wrapper) {
      return (BINARY)super.wrapper(wrapper);
    }

    @Override
    public final BINARY clone() {
      return new BINARY(this);
    }
  }

  public static final BLOB BLOB() {
    return BLOB.NULL;
  }

  public static final BLOB BLOB(final int i) {
    BLOB singleton = BLOB.singletons.get(i);
    if (singleton == null)
      BLOB.singletons.put(i, singleton = BLOB.NULL.clone());

    return singleton;
  }

  public static class BLOB extends LargeObject<InputStream> implements kind.BLOB {
    public static final BLOB NULL = new BLOB();

    private static final IdentityHashMap<Integer,BLOB> singletons = new IdentityHashMap<>();
    private static final Class<InputStream> type = InputStream.class;

    BLOB(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final InputStream _default, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    BLOB(final BLOB copy) {
      super(copy);
    }

    public BLOB(final long length) {
      super(length);
    }

    public BLOB() {
      super((Long)null);
    }

    public BLOB(final InputStream value) {
      super((Long)null);
      set(value);
    }

    public final BLOB set(final BLOB value) {
      super.set(value);
      return this;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileBlob(length());
    }

    @Override
    final Class<InputStream> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.BLOB;
    }

    @Override
    void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BLOB)
        return new BLOB(Math.max(length(), ((BLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final BLOB wrapper(final Evaluable wrapper) {
      return (BLOB)super.wrapper(wrapper);
    }

    @Override
    public final BLOB clone() {
      return new BLOB(this);
    }
  }

  public static final BOOLEAN BOOLEAN() {
    return BOOLEAN.NULL;
  }

  public static final BOOLEAN BOOLEAN(final int i) {
    BOOLEAN singleton = BOOLEAN.singletons.get(i);
    if (singleton == null)
      BOOLEAN.singletons.put(i, singleton = BOOLEAN.NULL.clone());

    return singleton;
  }

  public static class BOOLEAN extends Condition<Boolean> implements kind.BOOLEAN, Comparable<DataType<Boolean>> {
    public static final BOOLEAN NULL = new BOOLEAN();

    private static final IdentityHashMap<Integer,BOOLEAN> singletons = new IdentityHashMap<>();
    private static final Class<Boolean> type = Boolean.class;

    private boolean isNull = true;
    private boolean value;

    BOOLEAN(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Boolean _default, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      if (_default != null) {
        this.value = _default;
        this.isNull = false;
      }
    }

    BOOLEAN(final BOOLEAN copy) {
      super(copy);
    }

    public BOOLEAN() {
      super();
    }

    public BOOLEAN(final Boolean value) {
      super();
      if (value != null)
        set(value);
    }

    public BOOLEAN(final boolean value) {
      super();
      set(value);
    }

    public final BOOLEAN set(final BOOLEAN value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Boolean value) {
      return value != null ? set((boolean)value) : isNull && (isNull = true);
    }

    public final boolean set(final boolean value) {
      checkMutable();
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    public boolean getAsBoolean() {
      return value;
    }

    @Override
    public Boolean get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBoolean();
    }

    @Override
    final Class<Boolean> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.BOOLEAN;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBoolean(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBoolean(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final boolean value = resultSet.getBoolean(columnIndex);
      this.value = !(isNull = resultSet.wasNull()) && value;
    }

    @Override
    String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final BOOLEAN wrapper(final Evaluable wrapper) {
      return (BOOLEAN)super.wrapper(wrapper);
    }

    @Override
    public final int compareTo(final DataType<Boolean> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      return Boolean.compare(value, ((BOOLEAN)o).value);
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(this);
    }
  }

  public static final CHAR CHAR() {
    return CHAR.NULL;
  }

  public static final CHAR CHAR(final int i) {
    CHAR singleton = CHAR.singletons.get(i);
    if (singleton == null)
      CHAR.singletons.put(i, singleton = CHAR.NULL.clone());

    return singleton;
  }

  public static class CHAR extends Textual<String> implements kind.CHAR {
    public static final CHAR NULL = new CHAR();

    private static final IdentityHashMap<Integer,CHAR> singletons = new IdentityHashMap<>();
    private static final Class<String> type = String.class;

    private final boolean varying;

    CHAR(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final String _default, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    CHAR(final CHAR copy) {
      super(copy, copy.length());
      this.varying = copy.varying;
    }

    public CHAR(final long length, final boolean varying) {
      super((short)length);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final int length) {
      this(length, false);
    }

    public CHAR(final String value) {
      this(value.length(), true);
      set(value);
    }

    CHAR() {
      super(null);
      this.varying = true;
    }

    public final CHAR set(final CHAR value) {
      super.set(value);
      return this;
    }

    final void checkLength(final long length) {
      if (length < 0 || (!varying() && length == 0) || length > 65535)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    final String declare(final DBVendor vendor) {
      if (length() == null)
        throw new UnsupportedOperationException("Cannot declare a CHAR with null length");

      return vendor.getDialect().compileChar(varying, length() == null ? 1L : length());
    }

    @Override
    final Class<String> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return varying ? Types.VARCHAR : Types.CHAR;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final CHAR wrapper(final Evaluable wrapper) {
      return (CHAR)super.wrapper(wrapper);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(this);
    }
  }

  public static final CLOB CLOB() {
    return CLOB.NULL;
  }

  public static final CLOB CLOB(final int i) {
    CLOB singleton = CLOB.singletons.get(i);
    if (singleton == null)
      CLOB.singletons.put(i, singleton = CLOB.NULL.clone());

    return singleton;
  }

  public static class CLOB extends LargeObject<Reader> implements kind.CLOB {
    public static final CLOB NULL = new CLOB();

    private static final Class<Reader> type = Reader.class;
    private static final IdentityHashMap<Integer,CLOB> singletons = new IdentityHashMap<>();

    CLOB(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Reader _default, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    CLOB(final CLOB copy) {
      super(copy);
    }

    public CLOB(final long length) {
      super(length);
    }

    public CLOB(final Reader value) {
      super((Long)null);
      set(value);
    }

    public CLOB() {
      super((Long)null);
    }

    public final CLOB set(final CLOB value) {
      super.set(value);
      return this;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileClob(length());
    }

    @Override
    final Class<Reader> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.CLOB;
    }

    @Override
    void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof CLOB)
        return new CLOB(Math.max(length(), ((CLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final CLOB wrapper(final Evaluable wrapper) {
      return (CLOB)super.wrapper(wrapper);
    }

    @Override
    public final CLOB clone() {
      return new CLOB(this);
    }
  }

  public static final DATE DATE() {
    return DATE.NULL;
  }

  public static final DATE DATE(final int i) {
    DATE singleton = DATE.singletons.get(i);
    if (singleton == null)
      DATE.singletons.put(i, singleton = DATE.NULL.clone());

    return singleton;
  }

  public static class DATE extends Temporal<LocalDate> implements kind.DATE {
    public static final DATE NULL = new DATE();

    private static final Class<LocalDate> type = LocalDate.class;
    private static final IdentityHashMap<Integer,DATE> singletons = new IdentityHashMap<>();

    DATE(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDate _default, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    DATE(final DATE copy) {
      super(copy);
    }

    public DATE() {
      super();
    }

    public DATE(final LocalDate value) {
      super();
      set(value);
    }

    public final DATE set(final DATE value) {
      super.set(value);
      return this;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDate();
    }

    @Override
    final Class<LocalDate> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.DATE;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final DATE wrapper(final Evaluable wrapper) {
      return (DATE)super.wrapper(wrapper);
    }

    @Override
    public final DATE clone() {
      return new DATE(this);
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || isNull())
        return value == null ? 0 : 1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATE ? value.compareTo(((DATE)o).value) : LocalDateTime.of(value, LocalTime.MIDNIGHT).compareTo(((DATETIME)o).value);
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (!(obj instanceof DATE) && !(obj instanceof DATETIME))
        return false;

      return name.equals(((Temporal<?>)obj).name) && compareTo((Temporal<?>)obj) == 0;
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : value.format(Dialect.DATE_FORMAT);
    }
  }

  private static <T>T newInstance(final Constructor<T> constructor, final Object ... initargs) {
    try {
      return constructor.newInstance(initargs);
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
      if (e instanceof InvocationTargetException) {
        if (e.getCause() instanceof RuntimeException)
          throw (RuntimeException)e.getCause();

        if (e.getCause() instanceof IOException)
          Throwing.rethrow(e.getCause());
      }

      throw new RuntimeException(e);
    }
  }

  public abstract static class DataType<T> extends type.Subject<T> implements kind.DataType<T>, Cloneable {
    boolean setValue(final T value) {
      checkMutable();

      // FIXME: Can we get away from this wasSet hack?
      final boolean wasSet = this.wasSet;
      final boolean result = set(value);
      this.wasSet = wasSet;
      return result;
    }

    static <T>String compile(final DataType<T> dataType, final DBVendor vendor) throws IOException {
      return dataType.compile(vendor);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    static <T,V extends DataType<T>>V wrap(final T value) {
      if (value.getClass().isEnum())
        return (V)new ENUM((Enum)value);

      if (value instanceof org.jaxdb.jsql.UNSIGNED.UnsignedNumber) {
        final org.jaxdb.jsql.UNSIGNED.UnsignedNumber unsignedNumber = (org.jaxdb.jsql.UNSIGNED.UnsignedNumber)value;
        return (V)newInstance(Classes.getConstructor(unsignedNumber.getTypeClass(), unsignedNumber.value().getClass()), unsignedNumber.value());
      }

      return (V)newInstance(lookupDataTypeConstructor(value.getClass()), value);
    }

    @SuppressWarnings("unchecked")
    static <T>ARRAY<T> wrap(final T[] value) {
      final ARRAY<T> array;
      if (value.getClass().getComponentType().isEnum())
        array = new ARRAY<>((Class<? extends DataType<T>>)value.getClass().getComponentType());
      else
        array = new ARRAY<>((Class<? extends DataType<T>>)org.jaxdb.jsql.type.typeToGeneric.get(value.getClass().getComponentType()));

      array.set(value);
      return array;
    }

    static String getSimpleName(final Class<?> cls) {
      final String canonicalName = cls.getCanonicalName();
      return canonicalName.substring(canonicalName.indexOf("type.") + 5).replace('.', ' ');
    }

    final Entity owner;
    final String name;
    final boolean mutable;
    final boolean unique;
    final boolean primary;
    final boolean nullable;
    final GenerateOn<? super T> generateOnInsert;
    final GenerateOn<? super T> generateOnUpdate;
    final boolean keyForUpdate;

    DataType(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      this.owner = owner;
      this.name = name;
      this.mutable = mutable;
      this.unique = unique;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;
    }

    DataType(final DataType<T> copy) {
      this.owner = copy.owner;
      this.name = copy.name;
      this.mutable = true;
      this.unique = copy.unique;
      this.primary = copy.primary;
      this.nullable = copy.nullable;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.keyForUpdate = copy.keyForUpdate;

      // NOTE: Deliberately not copying indirection or wasSet
      // this.indirection = copy.indirection;
      // this.wasSet = copy.wasSet;
    }

    DataType() {
      this(null, true, null, false, false, true, null, null, false);
    }

    final void checkMutable() {
      if (!mutable)
        throw new UnsupportedOperationException();
    }

    int columnIndex;
    DataType<T> indirection;
    boolean wasSet;

    public abstract boolean set(T value);

    void set(final DataType<T> indirection) {
      checkMutable();
      this.wasSet = false;
      this.indirection = indirection;
    }

    public abstract T get();
    abstract boolean isNull();

    public final boolean wasSet() {
      return wasSet;
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
      checkMutable();
      if (rows.getConcurrency() == Concurrency.READ_ONLY)
        throw new IllegalStateException(rows.getConcurrency().getClass().getSimpleName() + "." + rows.getConcurrency());

      update(rows.resultSet, columnIndex);
    }

    public final <V extends DataType<T>>V AS(final V dataType) {
      dataType.wrapper(new As<>(this, dataType));
      return dataType;
    }

    public final <E extends Enum<?> & EntityEnum>ENUM<E> AS(final ENUM<E> dataType) {
      dataType.wrapper(new As<>(this, dataType));
      return dataType;
    }

    @Override
    void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    Object evaluate(final Set<Evaluable> visited) {
      if (indirection == null || visited.contains(this))
        return wrapper() != null ? wrapper().evaluate(visited) : get();

      visited.add(this);
      return indirection.evaluate(visited);
    }

    abstract Class<T> type();
    abstract int sqlType();
    abstract void get(PreparedStatement statement, int parameterIndex) throws IOException, SQLException;
    abstract void set(ResultSet resultSet, int columnIndex) throws SQLException;
    abstract void update(ResultSet resultSet, int columnIndex) throws SQLException;
    abstract String compile(DBVendor vendor) throws IOException;
    abstract String declare(DBVendor vendor);
    abstract DataType<?> scaleTo(DataType<?> dataType);

    @Override
    public abstract DataType<T> clone();

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (obj.getClass() != getClass())
        return false;

      final DataType<?> that = (DataType<?>)obj;
      if (!name.equals(that.name))
        return false;

//      if (!Objects.equals(value, that.value))
//        return false;

      return true;
    }

    @Override
    public int hashCode() {
      return name.hashCode(); //31 * name.hashCode() + Objects.hash(value);
    }
  }

  public static final DATETIME DATETIME() {
    return DATETIME.NULL;
  }

  public static final DATETIME DATETIME(final int i) {
    DATETIME singleton = DATETIME.singletons.get(i);
    if (singleton == null)
      DATETIME.singletons.put(i, singleton = DATETIME.NULL.clone());

    return singleton;
  }

  public static class DATETIME extends Temporal<LocalDateTime> implements kind.DATETIME {
    public static final DATETIME NULL = new DATETIME();

    private static final IdentityHashMap<Integer,DATETIME> singletons = new IdentityHashMap<>();
    private static final Class<LocalDateTime> type = LocalDateTime.class;
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final byte DEFAULT_PRECISION = 6;

    private final byte precision;

    DATETIME(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (byte)precision;
    }

    DATETIME(final DATETIME copy) {
      super(copy);
      this.precision = copy.precision;
    }

    public DATETIME(final int precision) {
      super();
      this.precision = (byte)precision;
    }

    public DATETIME() {
      this(DEFAULT_PRECISION);
    }

    public DATETIME(final LocalDateTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    public final DATETIME set(final DATETIME value) {
      super.set(value);
      return this;
    }

    public final int precision() {
      return precision;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDateTime(precision);
    }

    @Override
    final Class<LocalDateTime> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATETIME)
        return new DATETIME(Math.max(precision(), ((DATETIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final DATETIME wrapper(final Evaluable wrapper) {
      return (DATETIME)super.wrapper(wrapper);
    }

    @Override
    public DATETIME clone() {
      return new DATETIME(this);
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || isNull())
        return value == null ? 0 : 1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATETIME ? value.compareTo(((DATETIME)o).value) : value.toLocalDate().compareTo(((DATE)o).value);
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (!(obj instanceof DATE) && !(obj instanceof DATETIME))
        return false;

      return name.equals(((Temporal<?>)obj).name) && compareTo((Temporal<?>)obj) == 0;
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : value.format(Dialect.DATETIME_FORMAT);
    }
  }

  public static final DECIMAL UNSIGNED() {
    return DECIMAL.NULL;
  }

  public static final DECIMAL UNSIGNED(final int i) {
    DECIMAL singleton = DECIMAL.singletons.get(i);
    if (singleton == null)
      DECIMAL.singletons.put(i, singleton = DECIMAL.NULL.clone());

    return singleton;
  }

  public static class DECIMAL extends ExactNumeric<BigDecimal> implements kind.DECIMAL {
    public static final DECIMAL.UNSIGNED UNSIGNED() {
      return DECIMAL.UNSIGNED.NULL;
    }

    public static final DECIMAL.UNSIGNED UNSIGNED(final int i) {
      DECIMAL.UNSIGNED singleton = DECIMAL.UNSIGNED.singletons.get(i);
      if (singleton == null)
        DECIMAL.UNSIGNED.singletons.put(i, singleton = DECIMAL.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ExactNumeric<BigDecimal> implements kind.DECIMAL.UNSIGNED {
      public static final DECIMAL.UNSIGNED NULL = new DECIMAL.UNSIGNED();

      private static final IdentityHashMap<Integer,DECIMAL.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<BigDecimal> type = BigDecimal.class;

      private final Integer scale;
      private final BigDecimal min;
      private final BigDecimal max;
      private BigDecimal value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
        }

        checkScale(precision, scale);
        this.scale = scale;
        this.min = min;
        this.max = max;
      }

      UNSIGNED(final DECIMAL.UNSIGNED copy) {
        super(copy, copy.precision);
        this.scale = copy.scale;
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision, final int scale) {
        super(precision);
        checkScale(precision, scale);
        this.scale = scale;
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigDecimal value) {
        super(value == null ? null : value.precision());
        if (value == null) {
          this.scale = null;
        }
        else {
          this.scale = value.scale();
          checkScale(precision, scale);
          set(value);
        }

        this.min = null;
        this.max = null;
      }

      public UNSIGNED() {
        super(null);
        this.scale = null;
        this.min = null;
        this.max = null;
      }

      public final DECIMAL.UNSIGNED set(final DECIMAL.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final BigDecimal value) {
        checkMutable();
        if (value != null)
          checkValue(value);

        wasSet = true;
        final boolean changed = !Objects.equals(this.value, value);
        this.value = value;
        return changed;
      }

      @Override
      public BigDecimal get() {
        return value;
      }

      @Override
      boolean isNull() {
        return value == null;
      }

      private final void checkValue(final BigDecimal value) {
        if (min != null && value.compareTo(min) < 0 || max != null && max.compareTo(value) < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value.signum() < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      private void checkScale(final int precision, final int scale) {
        if (precision < scale)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");
      }

      @Override
      public final int scale() {
        return scale;
      }

      @Override
      final boolean unsigned() {
        return false;
      }

      @Override
      final BigDecimal minValue() {
        return BigDecimal.ZERO;
      }

      @Override
      final BigDecimal maxValue() {
        return null;
      }

      @Override
      final int maxPrecision() {
        return -1;
      }

      @Override
      public final BigDecimal min() {
        return min;
      }

      @Override
      public final BigDecimal max() {
        return max;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareDecimal(precision(), scale(), unsigned());
      }

      @Override
      final Class<BigDecimal> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.DECIMAL;
      }

      @Override
      final String primitiveToString() {
        throw new UnsupportedOperationException();
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (value == null)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setBigDecimal(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (value != null)
          resultSet.updateBigDecimal(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        final BigDecimal value = resultSet.getBigDecimal(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
        }

        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(precision() + 1, scale()) : new DECIMAL(precision() + 1, scale());

        if (dataType instanceof ExactNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale()) : new DECIMAL(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final DECIMAL.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DECIMAL.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return value.compareTo(BigDecimal.valueOf((((TINYINT)o).value)));

        if (o instanceof TINYINT.UNSIGNED)
          return value.compareTo(BigDecimal.valueOf(((TINYINT.UNSIGNED)o).value));

        if (o instanceof SMALLINT)
          return value.compareTo(BigDecimal.valueOf(((SMALLINT)o).value));

        if (o instanceof SMALLINT.UNSIGNED)
          return value.compareTo(BigDecimal.valueOf(((SMALLINT.UNSIGNED)o).value));

        if (o instanceof INT)
          return value.compareTo(BigDecimal.valueOf(((INT)o).value));

        if (o instanceof INT.UNSIGNED)
          return value.compareTo(BigDecimal.valueOf(((INT.UNSIGNED)o).value));

        if (o instanceof BIGINT)
          return value.compareTo(BigDecimal.valueOf(((BIGINT)o).value));

        if (o instanceof BIGINT.UNSIGNED)
          return value.compareTo(((BIGINT.UNSIGNED)o).value.toBigDecimal());

        if (o instanceof FLOAT)
          return value.compareTo(BigDecimal.valueOf(((FLOAT)o).value));

        if (o instanceof FLOAT.UNSIGNED)
          return value.compareTo(BigDecimal.valueOf(((FLOAT.UNSIGNED)o).value));

        if (o instanceof DOUBLE)
          return value.compareTo(BigDecimal.valueOf(((DOUBLE)o).value));

        if (o instanceof DOUBLE.UNSIGNED)
          return value.compareTo(BigDecimal.valueOf(((DOUBLE.UNSIGNED)o).value));

        if (o instanceof DECIMAL)
          return value.compareTo(((DECIMAL)o).value);

        if (o instanceof DECIMAL.UNSIGNED)
          return value.compareTo(((DECIMAL.UNSIGNED)o).value);

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final DECIMAL.UNSIGNED clone() {
        return new DECIMAL.UNSIGNED(this);
      }
    }

    public static final DECIMAL NULL = new DECIMAL();

    private static final IdentityHashMap<Integer,DECIMAL> singletons = new IdentityHashMap<>();
    private static final Class<BigDecimal> type = BigDecimal.class;
    private static final byte maxScale = 38;

    private final Integer scale;
    private final BigDecimal min;
    private final BigDecimal max;
    private BigDecimal value;

    DECIMAL(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
      }

      checkScale(precision, scale);
      this.scale = scale;
      this.min = min;
      this.max = max;
    }

    DECIMAL(final DECIMAL copy) {
      super(copy, copy.precision);
      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;
    }

    public DECIMAL(final int precision, final int scale) {
      super(precision);
      checkScale(precision, scale);
      this.scale = scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final BigDecimal value) {
      super(value == null ? null : value.precision());
      if (value == null) {
        this.scale = null;
      }
      else {
        this.scale = value.scale();
        checkScale(precision, scale);
        set(value);
      }

      this.min = null;
      this.max = null;
    }

    public DECIMAL() {
      super(null);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    public DECIMAL set(final DECIMAL value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final BigDecimal value) {
      checkMutable();
      if (value != null)
        checkValue(value);

      wasSet = true;
      final boolean changed = !Objects.equals(this.value, value);
      this.value = value;
      return changed;
    }

    @Override
    public BigDecimal get() {
      return value;
    }

    @Override
    public boolean isNull() {
      return value == null;
    }

    private final void checkValue(final BigDecimal value) {
      if (min != null && value.compareTo(min) < 0 || max != null && max.compareTo(value) < 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    private void checkScale(final int precision, final int scale) {
      if (precision < scale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");

      if (scale > maxScale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
    }

    @Override
    public final int scale() {
      return scale;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final BigDecimal minValue() {
      return null;
    }

    @Override
    final BigDecimal maxValue() {
      return null;
    }

    @Override
    final int maxPrecision() {
      return -1;
    }

    @Override
    public final BigDecimal min() {
      return min;
    }

    @Override
    public final BigDecimal max() {
      return max;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDecimal(precision(), scale(), unsigned());
    }

    @Override
    final Class<BigDecimal> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.DECIMAL;
    }

    @Override
    final String primitiveToString() {
      throw new UnsupportedOperationException();
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (value == null)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBigDecimal(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (value != null)
        resultSet.updateBigDecimal(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final BigDecimal value = resultSet.getBigDecimal(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), Math.max(scale(), decimal.scale()));
      }

      if (dataType instanceof ApproxNumeric)
        return new DECIMAL(precision() + 1, scale());

      if (dataType instanceof ExactNumeric)
        return new DECIMAL(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final DECIMAL wrapper(final Evaluable wrapper) {
      return (DECIMAL)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return value.compareTo(BigDecimal.valueOf(((TINYINT)o).value));

      if (o instanceof TINYINT.UNSIGNED)
        return value.compareTo(BigDecimal.valueOf(((TINYINT.UNSIGNED)o).value));

      if (o instanceof SMALLINT)
        return value.compareTo(BigDecimal.valueOf(((SMALLINT)o).value));

      if (o instanceof SMALLINT.UNSIGNED)
        return value.compareTo(BigDecimal.valueOf(((SMALLINT.UNSIGNED)o).value));

      if (o instanceof INT)
        return value.compareTo(BigDecimal.valueOf(((INT)o).value));

      if (o instanceof INT.UNSIGNED)
        return value.compareTo(BigDecimal.valueOf(((INT.UNSIGNED)o).value));

      if (o instanceof BIGINT)
        return value.compareTo(BigDecimal.valueOf(((BIGINT)o).value));

      if (o instanceof BIGINT.UNSIGNED)
        return value.compareTo(((BIGINT.UNSIGNED)o).value.toBigDecimal());

      if (o instanceof FLOAT)
        return value.compareTo(BigDecimal.valueOf(((FLOAT)o).value));

      if (o instanceof FLOAT.UNSIGNED)
        return value.compareTo(BigDecimal.valueOf(((FLOAT.UNSIGNED)o).value));

      if (o instanceof DOUBLE)
        return value.compareTo(BigDecimal.valueOf(((DOUBLE)o).value));

      if (o instanceof DOUBLE.UNSIGNED)
        return value.compareTo(BigDecimal.valueOf(((DOUBLE.UNSIGNED)o).value));

      if (o instanceof DECIMAL)
        return value.compareTo(((DECIMAL)o).value);

      if (o instanceof DECIMAL.UNSIGNED)
        return value.compareTo(((DECIMAL.UNSIGNED)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(this);
    }

    @Override
    public String toString() {
      return value == null ? "NULL" : value.toString();
    }
  }

  public static final DOUBLE DOUBLE() {
    return DOUBLE.NULL;
  }

  public static final DOUBLE DOUBLE(final int i) {
    DOUBLE singleton = DOUBLE.singletons.get(i);
    if (singleton == null)
      DOUBLE.singletons.put(i, singleton = DOUBLE.NULL.clone());

    return singleton;
  }

  public static class DOUBLE extends ApproxNumeric<Double> implements kind.DOUBLE {
    public static final DOUBLE.UNSIGNED BIGINT() {
      return DOUBLE.UNSIGNED.NULL;
    }

    public static final DOUBLE.UNSIGNED BIGINT(final int i) {
      DOUBLE.UNSIGNED singleton = DOUBLE.UNSIGNED.singletons.get(i);
      if (singleton == null)
        DOUBLE.UNSIGNED.singletons.put(i, singleton = DOUBLE.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ApproxNumeric<Double> implements kind.DOUBLE.UNSIGNED {
      public static final DOUBLE.UNSIGNED NULL = new DOUBLE.UNSIGNED();

      private static final IdentityHashMap<Integer,DOUBLE.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<Double> type = Double.class;

      private final Double min;
      private final Double max;
      private boolean isNull = true;
      private double value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
          this.isNull = false;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final DOUBLE.UNSIGNED copy) {
        super(copy);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final Double value) {
        this();
        if (value != null)
          set(value);
      }

      public UNSIGNED(final double value) {
        this();
        set(value);
      }

      public UNSIGNED() {
        super();
        this.min = null;
        this.max = null;
      }

      public final DOUBLE.UNSIGNED set(final DOUBLE.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final Double value) {
        return value != null ? set((double)value) : isNull && (isNull = true);
      }

      public final boolean set(final double value) {
        checkMutable();
        checkValue(value);
        wasSet = true;
        final boolean changed = isNull || this.value != value;
        this.value = value;
        this.isNull = false;
        return changed;
      }

      private final void checkValue(final double value) {
        if (min != null && value < min || max != null && max < value)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      public double getAsDouble() {
        return value;
      }

      @Override
      public Double get() {
        return isNull ? null : value;
      }

      @Override
      public boolean isNull() {
        return isNull;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      public final Double min() {
        return min;
      }

      @Override
      public final Double max() {
        return max;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareDouble(unsigned());
      }

      @Override
      final Class<Double> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.DOUBLE;
      }

      @Override
      final String primitiveToString() {
        return String.valueOf(value);
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (isNull)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setDouble(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (isNull)
          resultSet.updateNull(columnIndex);
        else
          resultSet.updateDouble(columnIndex, value);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final double value = resultSet.getDouble(columnIndex);
        this.value = (isNull = resultSet.wasNull()) ? Double.NaN : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof Numeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final DOUBLE.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DOUBLE.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return Double.compare(value, ((TINYINT)o).value);

        if (o instanceof TINYINT.UNSIGNED)
          return Double.compare(value, ((TINYINT.UNSIGNED)o).value);

        if (o instanceof SMALLINT)
          return Double.compare(value, ((SMALLINT)o).value);

        if (o instanceof SMALLINT.UNSIGNED)
          return Double.compare(value, ((SMALLINT.UNSIGNED)o).value);

        if (o instanceof INT)
          return Double.compare(value, ((INT)o).value);

        if (o instanceof INT.UNSIGNED)
          return Double.compare(value, ((INT.UNSIGNED)o).value);

        if (o instanceof BIGINT)
          return Double.compare(value, ((BIGINT)o).value);

        if (o instanceof BIGINT.UNSIGNED)
          return Double.compare(value, ((BIGINT.UNSIGNED)o).value.doubleValue());

        if (o instanceof FLOAT)
          return Double.compare(value, ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Double.compare(value, ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Double.compare(value, ((DOUBLE)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return Double.compare(value, ((DECIMAL)o).value.doubleValue());

        if (o instanceof DECIMAL.UNSIGNED)
          return Double.compare(value, ((DECIMAL.UNSIGNED)o).value.doubleValue());

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final DOUBLE.UNSIGNED clone() {
        return new DOUBLE.UNSIGNED(this);
      }
    }

    public static final DOUBLE NULL = new DOUBLE();

    private static final IdentityHashMap<Integer,DOUBLE> singletons = new IdentityHashMap<>();
    private static final Class<Double> type = Double.class;

    private final Double min;
    private final Double max;
    private boolean isNull = true;
    private double value;

    DOUBLE(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    DOUBLE(final DOUBLE copy) {
      super(copy);
      this.min = copy.min;
      this.max = copy.max;
    }

    public DOUBLE(final Double value) {
      this();
      if (value != null)
        set(value);
    }

    public DOUBLE(final double value) {
      this();
      set(value);
    }

    public DOUBLE() {
      super();
      this.min = null;
      this.max = null;
    }

    public final DOUBLE set(final DOUBLE value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Double value) {
      return value != null ? set((double)value) : isNull && (isNull = true);
    }

    public final boolean set(final double value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final double value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public double getAsDouble() {
      return value;
    }

    @Override
    public Double get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Double min() {
      return min;
    }

    @Override
    public final Double max() {
      return max;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDouble(unsigned());
    }

    @Override
    final Class<Double> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.DOUBLE;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setDouble(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateDouble(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final double value = resultSet.getDouble(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? Double.NaN : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final DOUBLE wrapper(final Evaluable wrapper) {
      return (DOUBLE)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Double.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Double.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Double.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Double.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Double.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Double.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Double.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return Double.compare(value, ((BIGINT.UNSIGNED)o).value.doubleValue());

      if (o instanceof FLOAT)
        return Double.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Double.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return Double.compare(value, ((DECIMAL)o).value.doubleValue());

      if (o instanceof DECIMAL.UNSIGNED)
        return Double.compare(value, ((DECIMAL.UNSIGNED)o).value.doubleValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public DOUBLE clone() {
      return new DOUBLE(this);
    }
  }

  public static final ENUM<?> ENUM() {
    return ENUM.NULL;
  }

  public static final ENUM<?> ENUM(final int i) {
    ENUM<?> singleton = ENUM.singletons.get(i);
    if (singleton == null)
      ENUM.singletons.put(i, singleton = ENUM.NULL.clone());

    return singleton;
  }

  public static class ENUM<T extends Enum<?> & EntityEnum> extends Textual<T> implements kind.ENUM<T> {
    public static final ENUM<?> NULL = new ENUM<>();

    private static final IdentityHashMap<Integer,ENUM<?>> singletons = new IdentityHashMap<>();
    private static final IdentityHashMap<Class<?>,Short> typeToLength = new IdentityHashMap<>();

    private final Class<T> enumType;

    private static short calcEnumLength(final Class<?> enumType) {
      final Short cached = typeToLength.get(enumType);
      if (cached != null)
        return cached;

      short length = 0;
      for (final Object constant : enumType.getEnumConstants())
        length = (short)Math.max(length, constant.toString().length());

      typeToLength.put(enumType, length);
      return length;
    }

    ENUM(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Class<T> type) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, calcEnumLength(type));
      this.enumType = type;
    }

    ENUM(final ENUM<T> copy) {
      super(copy, copy.length());
      this.enumType = copy.enumType;
    }

    public ENUM(final Class<T> enumType) {
      super(calcEnumLength(enumType));
      this.enumType = enumType;
    }

    private ENUM() {
      super(null);
      this.enumType = null;
    }

    @SuppressWarnings("unchecked")
    public ENUM(final T value) {
      this((Class<T>)value.getClass());
      set(value);
    }

    ENUM<T> set(final ENUM<T> value) {
      super.set(value);
      return this;
    }

    @Override
    final String declare(final DBVendor vendor) {
      throw new UnsupportedOperationException();
    }

    @Override
    final Class<T> type() {
      return enumType;
    }

    @Override
    final int sqlType() {
      return Types.CHAR;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (value == null)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setObject(parameterIndex, value.toString());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (value != null)
        resultSet.updateObject(columnIndex, value.toString());
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final String value = resultSet.getString(columnIndex);
      if (value == null) {
        this.value = null;
        return;
      }

      for (final T constant : enumType.getEnumConstants()) {
        if (constant.toString().equals(value)) {
          this.value = constant;
          return;
        }
      }

      throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final ENUM<T> wrapper(final Evaluable wrapper) {
      return (ENUM<T>)super.wrapper(wrapper);
    }

    @Override
    public final ENUM<T> clone() {
      return new ENUM<>(this);
    }

    @Override
    final String evaluate(final Set<Evaluable> visited) {
      return value == null ? null : value.toString();
    }
  }

  public abstract static class Entity extends type.Subject<Entity> implements kind.Entity<Entity>, Cloneable {
    final type.DataType<?>[] column;
    final type.DataType<?>[] primary;
    private final boolean mutable;
    private final boolean wasSelected;

    Entity(final boolean mutable, final boolean wasSelected, final type.DataType<?>[] column, final type.DataType<?>[] primary) {
      this.mutable = mutable;
      this.wasSelected = wasSelected;
      this.column = column;
      this.primary = primary;
    }

    Entity(final Entity entity) {
      this.mutable = entity.mutable;
      this.wasSelected = false;
      this.column = entity.column.clone();
      this.primary = entity.primary.clone();
    }

    Entity() {
      this.mutable = true;
      this.wasSelected = false;
      this.column = null;
      this.primary = null;
    }

    final boolean wasSelected() {
      return wasSelected;
    }

    @SuppressWarnings("unchecked")
    final Class<? extends Schema> schema() {
      return (Class<? extends Schema>)getClass().getEnclosingClass();
    }

    @Override
    final Entity evaluate(final Set<Evaluable> visited) {
      return this;
    }

    @Override
    final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    abstract String name();
    abstract Entity newInstance();

    @Override
    protected abstract Entity clone();
  }

  public abstract static class ExactNumeric<T extends Number> extends Numeric<T> implements kind.ExactNumeric<T> {
    final Integer precision;

    ExactNumeric(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Numeric<T> copy, final Integer precision) {
      super(copy);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Integer precision) {
      super();
      checkPrecision(precision);
      if (precision != null) {
        this.precision = precision;
        if (precision <= 0)
          throw new IllegalArgumentException("precision must be >= 1");
      }
      else {
        this.precision = null;
      }
    }

    public final int precision() {
      return precision;
    }

    abstract int scale();
    abstract T minValue();
    abstract T maxValue();
    abstract int maxPrecision();

    private void checkPrecision(final Integer precision) {
      if (precision != null && maxPrecision() != -1 && precision > maxPrecision())
        throw new IllegalArgumentException(getSimpleName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }
  }

  public static final FLOAT FLOAT() {
    return FLOAT.NULL;
  }

  public static final FLOAT FLOAT(final int i) {
    FLOAT singleton = FLOAT.singletons.get(i);
    if (singleton == null)
      FLOAT.singletons.put(i, singleton = FLOAT.NULL.clone());

    return singleton;
  }

  public static class FLOAT extends ApproxNumeric<Float> implements kind.FLOAT {
    public static final FLOAT.UNSIGNED BIGINT() {
      return FLOAT.UNSIGNED.NULL;
    }

    public static final FLOAT.UNSIGNED BIGINT(final int i) {
      FLOAT.UNSIGNED singleton = FLOAT.UNSIGNED.singletons.get(i);
      if (singleton == null)
        FLOAT.UNSIGNED.singletons.put(i, singleton = FLOAT.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ApproxNumeric<Float> implements kind.FLOAT.UNSIGNED {
      public static final FLOAT.UNSIGNED NULL = new FLOAT.UNSIGNED();

      private static final IdentityHashMap<Integer,FLOAT.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<Float> type = Float.class;

      private final Float min;
      private final Float max;
      private boolean isNull = true;
      private float value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
          this.isNull = false;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final FLOAT.UNSIGNED copy) {
        super(copy);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED() {
        super();
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Float value) {
        this();
        if (value != null)
          set(value);
      }

      public UNSIGNED(final float value) {
        this();
        set(value);
      }

      public final FLOAT.UNSIGNED set(final FLOAT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final Float value) {
        return value != null ? set((float)value) : isNull && (isNull = true);
      }

      public final boolean set(final float value) {
        checkMutable();
        checkValue(value);
        wasSet = true;
        final boolean changed = isNull || this.value != value;
        this.value = value;
        this.isNull = false;
        return changed;
      }

      private final void checkValue(final float value) {
        if (min != null && value < min || max != null && max < value)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      public float getAsFloat() {
        return value;
      }

      @Override
      public Float get() {
        return isNull ? null : value;
      }

      @Override
      public boolean isNull() {
        return isNull;
      }

      @Override
      public final Float min() {
        return min;
      }

      @Override
      public final Float max() {
        return max;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareFloat(unsigned());
      }

      @Override
      final Class<Float> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.FLOAT;
      }

      @Override
      final String primitiveToString() {
        return String.valueOf(value);
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (isNull)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setFloat(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (isNull)
          resultSet.updateNull(columnIndex);
        else
          resultSet.updateFloat(columnIndex, value);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final float value = resultSet.getFloat(columnIndex);
        this.value = (isNull = resultSet.wasNull()) ? Float.NaN : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof FLOAT || dataType instanceof TINYINT)
          return ((Numeric<?>)dataType).unsigned() ? new FLOAT.UNSIGNED() : new FLOAT();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(decimal.precision(), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision(), decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(decimal.precision(), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision(), decimal.scale());
        }

        if (dataType instanceof Numeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final FLOAT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (FLOAT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return Float.compare(value, ((TINYINT)o).value);

        if (o instanceof TINYINT.UNSIGNED)
          return Float.compare(value, ((TINYINT.UNSIGNED)o).value);

        if (o instanceof SMALLINT)
          return Float.compare(value, ((SMALLINT)o).value);

        if (o instanceof SMALLINT.UNSIGNED)
          return Float.compare(value, ((SMALLINT.UNSIGNED)o).value);

        if (o instanceof INT)
          return Float.compare(value, ((INT)o).value);

        if (o instanceof INT.UNSIGNED)
          return Float.compare(value, ((INT.UNSIGNED)o).value);

        if (o instanceof BIGINT)
          return Float.compare(value, ((BIGINT)o).value);

        if (o instanceof BIGINT.UNSIGNED)
          return Float.compare(value, ((BIGINT.UNSIGNED)o).value.floatValue());

        if (o instanceof FLOAT)
          return Float.compare(value, ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Float.compare(value, (float)((DOUBLE)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Float.compare(value, (float)((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return Float.compare(value, ((DECIMAL)o).value.floatValue());

        if (o instanceof DECIMAL.UNSIGNED)
          return Float.compare(value, ((DECIMAL.UNSIGNED)o).value.floatValue());

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final FLOAT.UNSIGNED clone() {
        return new FLOAT.UNSIGNED(this);
      }
    }

    public static final FLOAT NULL = new FLOAT();

    private static final IdentityHashMap<Integer,FLOAT> singletons = new IdentityHashMap<>();
    private static final Class<Float> type = Float.class;

    private final Float min;
    private final Float max;
    private boolean isNull = true;
    private float value;

    FLOAT(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    FLOAT(final FLOAT copy) {
      super(copy);
      this.min = copy.min;
      this.max = copy.max;
    }

    public FLOAT() {
      super();
      this.min = null;
      this.max = null;
    }

    public FLOAT(final Float value) {
      this();
      if (value != null)
        set(value);
    }

    public FLOAT(final float value) {
      this();
      set(value);
    }

    public final FLOAT set(final FLOAT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Float value) {
      return value != null ? set((float)value) : isNull && (isNull = true);
    }

    public final boolean set(final float value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final float value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public float getAsFloat() {
      return value;
    }

    @Override
    public Float get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Float min() {
      return min;
    }

    @Override
    public final Float max() {
      return max;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(unsigned());
    }

    @Override
    final Class<Float> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.FLOAT;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setFloat(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateFloat(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final float value = resultSet.getFloat(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? Float.NaN : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT || dataType instanceof FLOAT.UNSIGNED || dataType instanceof TINYINT || dataType instanceof TINYINT.UNSIGNED)
        return new FLOAT();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final FLOAT wrapper(final Evaluable wrapper) {
      return (FLOAT)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Float.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Float.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Float.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Float.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Float.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Float.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Float.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return Float.compare(value, ((BIGINT.UNSIGNED)o).value.floatValue());

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Float.compare(value, (float)((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Float.compare(value, (float)((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return Float.compare(value, ((DECIMAL)o).value.floatValue());

      if (o instanceof DECIMAL.UNSIGNED)
        return Float.compare(value, ((DECIMAL.UNSIGNED)o).value.floatValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(this);
    }
  }

  public abstract static class LargeObject<T extends Closeable> extends Objective<T> implements kind.LargeObject<T> {
    private final Long length;

    LargeObject(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
    }

    LargeObject(final LargeObject<T> copy) {
      super(copy);
      this.length = copy.length;
    }

    LargeObject(final Long length) {
      super();
      this.length = length;
    }

    public final Long length() {
      return length;
    }

    private void checkLength(final Long length) {
      if (length != null && length <= 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " illegal length: " + length);
    }
  }

  public static final INT INT() {
    return INT.NULL;
  }

  public static final INT INT(final int i) {
    INT singleton = INT.singletons.get(i);
    if (singleton == null)
      INT.singletons.put(i, singleton = INT.NULL.clone());

    return singleton;
  }

  public static class INT extends ExactNumeric<Integer> implements kind.INT {
    public static final INT.UNSIGNED UNSIGNED() {
      return INT.UNSIGNED.NULL;
    }

    public static final INT.UNSIGNED UNSIGNED(final int i) {
      INT.UNSIGNED singleton = INT.UNSIGNED.singletons.get(i);
      if (singleton == null)
        INT.UNSIGNED.singletons.put(i, singleton = INT.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ExactNumeric<Long> implements kind.INT.UNSIGNED {
      public static final INT.UNSIGNED NULL = new INT.UNSIGNED();

      private static final IdentityHashMap<Integer,INT.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<Long> type = Long.class;

      private final Long min;
      private final Long max;
      private boolean isNull = true;
      private long value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
          this.isNull = false;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final INT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Long value) {
        super(value == null ? null : (int)Numbers.precision(value));
        this.min = null;
        this.max = null;
        if (value != null)
          set(value);
      }

      public UNSIGNED(final long value) {
        this();
        set(value);
      }

      public UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public final UNSIGNED set(final INT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final Long value) {
        return value != null ? set((long)value) : isNull && (isNull = true);
      }

      public final boolean set(final long value) {
        checkMutable();
        checkValue(value);
        wasSet = true;
        final boolean changed = isNull || this.value != value;
        this.value = value;
        this.isNull = false;
        return changed;
      }

      private final void checkValue(final long value) {
        if (min != null && value < min || max != null && max < value)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      public long getAsLong() {
        return value;
      }

      @Override
      public Long get() {
        return isNull ? null : value;
      }

      @Override
      public boolean isNull() {
        return isNull;
      }

      @Override
      public final Long min() {
        return min;
      }

      @Override
      public final Long max() {
        return max;
      }

      @Override
      final int scale() {
        return 0;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final Long minValue() {
        return 0L;
      }

      @Override
      final Long maxValue() {
        return 4294967295L;
      }

      @Override
      final int maxPrecision() {
        return 10;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().compileInt32((byte)precision(), unsigned());
      }

      @Override
      final Class<Long> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.INTEGER;
      }

      @Override
      final String primitiveToString() {
        return String.valueOf(value);
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (isNull)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setLong(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (isNull)
          resultSet.updateNull(columnIndex);
        else
          resultSet.updateLong(columnIndex, value);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final long value = resultSet.getLong(columnIndex);
        this.value = (isNull = resultSet.wasNull()) ? 0 : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof BIGINT.UNSIGNED)
          return new BIGINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new INT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision())) : new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final INT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (INT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return Long.compare(value, ((TINYINT)o).value);

        if (o instanceof TINYINT.UNSIGNED)
          return Long.compare(value, ((TINYINT.UNSIGNED)o).value);

        if (o instanceof SMALLINT)
          return Long.compare(value, ((SMALLINT)o).value);

        if (o instanceof SMALLINT.UNSIGNED)
          return Long.compare(value, ((SMALLINT.UNSIGNED)o).value);

        if (o instanceof INT)
          return Long.compare(value, ((INT)o).value);

        if (o instanceof INT.UNSIGNED)
          return Long.compare(value, ((INT.UNSIGNED)o).value);

        if (o instanceof BIGINT)
          return Long.compare(value, ((BIGINT)o).value);

        if (o instanceof BIGINT.UNSIGNED)
          return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

        if (o instanceof FLOAT)
          return Float.compare(value, ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Double.compare(value, ((DOUBLE)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

        if (o instanceof DECIMAL.UNSIGNED)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final INT.UNSIGNED clone() {
        return new INT.UNSIGNED(this);
      }
    }

    public static final INT NULL = new INT();

    private static final IdentityHashMap<Integer,INT> singletons = new IdentityHashMap<>();
    private static final Class<Integer> type = Integer.class;

    private final Integer min;
    private final Integer max;
    private boolean isNull = true;
    private int value;

    INT(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    INT(final INT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public INT(final short precision) {
      super((int)precision);
      this.min = null;
      this.max = null;
    }

    public INT(final Integer value) {
      super(value == null ? null : (int)Numbers.precision(value));
      this.min = null;
      this.max = null;
      if (value != null)
        set(value);
    }

    public INT(final int value) {
      this();
      set(value);
    }

    public INT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public final INT set(final INT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Integer value) {
      return value != null ? set((int)value) : isNull && (isNull = true);
    }

    public final boolean set(final int value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final int value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public int getAsInt() {
      return value;
    }

    @Override
    public Integer get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Integer min() {
      return min;
    }

    @Override
    public final Integer max() {
      return max;
    }

    @Override
    final int scale() {
      return 0;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final Integer minValue() {
      return -2147483648;
    }

    @Override
    final Integer maxValue() {
      return 2147483647;
    }

    @Override
    final int maxPrecision() {
      return 10;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileInt32((byte)precision(), unsigned());
    }

    @Override
    final Class<Integer> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.INTEGER;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setInt(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final int value = resultSet.getInt(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof BIGINT || dataType instanceof BIGINT.UNSIGNED)
        return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final INT wrapper(final Evaluable wrapper) {
      return (INT)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Integer.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Integer.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Integer.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Integer.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Long.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      if (o instanceof DECIMAL.UNSIGNED)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final INT clone() {
      return new INT(this);
    }
  }

  public static abstract class Objective<T> extends DataType<T> implements kind.Objective<T> {
    T value;

    Objective(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.value = _default;
    }

    Objective(final Objective<T> copy) {
      this.value = copy.value;
    }

    Objective() {
    }

    @Override
    public final boolean set(final T value) {
      checkMutable();
      wasSet = true;
      final boolean changed = !Objects.equals(this.value, value);
      this.value = value;
      return changed;
    }

    @Override
    public final T get() {
      return value;
    }

    @Override
    public boolean isNull() {
      return value == null;
    }
  }

  public abstract static class Primitive<T> extends DataType<T> implements kind.Primitive<T> {
    Primitive(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Primitive(final Primitive<T> copy) {
      super(copy);
    }

    Primitive() {
      super();
    }

    @Override
    final void set(final DataType<T> indirection) {
      super.set(indirection);
    }

    abstract String primitiveToString();

    @Override
    public String toString() {
      return isNull() ? "NULL" : primitiveToString();
    }
  }

  public static final SMALLINT SMALLINT() {
    return SMALLINT.NULL;
  }

  public static final SMALLINT SMALLINT(final int i) {
    SMALLINT singleton = SMALLINT.singletons.get(i);
    if (singleton == null)
      SMALLINT.singletons.put(i, singleton = SMALLINT.NULL.clone());

    return singleton;
  }

  public static class SMALLINT extends ExactNumeric<Short> implements kind.SMALLINT {
    public static final SMALLINT.UNSIGNED UNSIGNED() {
      return SMALLINT.UNSIGNED.NULL;
    }

    public static final SMALLINT.UNSIGNED UNSIGNED(final int i) {
      SMALLINT.UNSIGNED singleton = SMALLINT.UNSIGNED.singletons.get(i);
      if (singleton == null)
        SMALLINT.UNSIGNED.singletons.put(i, singleton = SMALLINT.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ExactNumeric<Integer> implements kind.SMALLINT.UNSIGNED {
      public static final SMALLINT.UNSIGNED NULL = new SMALLINT.UNSIGNED();

      private static final IdentityHashMap<Integer,SMALLINT.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<Integer> type = Integer.class;

      private final Integer min;
      private final Integer max;
      private boolean isNull = true;
      private int value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
          this.isNull = false;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final SMALLINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final short precision) {
        super((int)precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Integer value) {
        super(value == null ? null : (int)Numbers.precision(value));
        this.min = null;
        this.max = null;
        if (value != null)
          set(value);
      }

      public UNSIGNED(final int value) {
        this();
        set(value);
      }

      public UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public final UNSIGNED set(final SMALLINT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final Integer value) {
        return value != null ? set((int)value) : isNull && (isNull = true);
      }

      public final boolean set(final int value) {
        checkMutable();
        checkValue(value);
        wasSet = true;
        final boolean changed = isNull || this.value != value;
        this.value = value;
        this.isNull = false;
        return changed;
      }

      private final void checkValue(final int value) {
        if (min != null && value < min || max != null && max < value)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      public int getAsInt() {
        return value;
      }

      @Override
      public Integer get() {
        return isNull ? null : value;
      }

      @Override
      public boolean isNull() {
        return isNull;
      }

      @Override
      public final Integer min() {
        return min;
      }

      @Override
      public final Integer max() {
        return max;
      }

      @Override
      final int scale() {
        return 0;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final Integer minValue() {
        return 0;
      }

      @Override
      final Integer maxValue() {
        return 65535;
      }

      @Override
      final int maxPrecision() {
        return 5;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().compileInt16((byte)precision(), unsigned());
      }

      @Override
      final Class<Integer> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.SMALLINT;
      }

      @Override
      final String primitiveToString() {
        return String.valueOf(value);
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (isNull)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setInt(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (isNull)
          resultSet.updateNull(columnIndex);
        else
          resultSet.updateInt(columnIndex, value);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final int value = resultSet.getInt(columnIndex);
        this.value = (isNull = resultSet.wasNull()) ? 0 : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof INT)
          return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if ( dataType instanceof INT.UNSIGNED)
          return new INT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof BIGINT.UNSIGNED)
          return new BIGINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new SMALLINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision())) : new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final SMALLINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (SMALLINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return Integer.compare(value, ((TINYINT)o).value);

        if (o instanceof TINYINT.UNSIGNED)
          return Integer.compare(value, ((TINYINT.UNSIGNED)o).value);

        if (o instanceof SMALLINT)
          return Integer.compare(value, ((SMALLINT)o).value);

        if (o instanceof SMALLINT.UNSIGNED)
          return Integer.compare(value, ((SMALLINT.UNSIGNED)o).value);

        if (o instanceof INT)
          return Integer.compare(value, ((INT)o).value);

        if (o instanceof INT.UNSIGNED)
          return Long.compare(value, ((INT.UNSIGNED)o).value);

        if (o instanceof BIGINT)
          return Long.compare(value, ((BIGINT)o).value);

        if (o instanceof BIGINT.UNSIGNED)
          return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

        if (o instanceof FLOAT)
          return Float.compare(value, ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Double.compare(value, ((DOUBLE)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

        if (o instanceof DECIMAL.UNSIGNED)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final SMALLINT.UNSIGNED clone() {
        return new SMALLINT.UNSIGNED(this);
      }
    }

    public static final SMALLINT NULL = new SMALLINT();

    private static final IdentityHashMap<Integer,SMALLINT> singletons = new IdentityHashMap<>();
    private static final Class<Short> type = Short.class;

    private final Short min;
    private final Short max;
    private boolean isNull = true;
    private short value;

    SMALLINT(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    SMALLINT(final SMALLINT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public SMALLINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final Short value) {
      super(value == null ? null : (int)Numbers.precision(value));
      this.min = null;
      this.max = null;
      if (value != null)
        set(value);
    }

    public SMALLINT(final short value) {
      this();
      set(value);
    }

    public SMALLINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public final SMALLINT set(final SMALLINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Short value) {
      return value != null ? set((short)value) : isNull && (isNull = true);
    }

    public final boolean set(final short value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final short value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public short getAsShort() {
      return value;
    }

    @Override
    public Short get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Short min() {
      return min;
    }

    @Override
    public final Short max() {
      return max;
    }

    @Override
    final int scale() {
      return 0;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final Short minValue() {
      return -32768;
    }

    @Override
    final Short maxValue() {
      return 32767;
    }

    @Override
    final int maxPrecision() {
      return 5;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileInt16((byte)precision(), unsigned());
    }

    @Override
    final Class<Short> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.SMALLINT;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setShort(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final short value = resultSet.getShort(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof INT || dataType instanceof INT.UNSIGNED)
        return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof BIGINT || dataType instanceof BIGINT.UNSIGNED)
        return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final SMALLINT wrapper(final Evaluable wrapper) {
      return (SMALLINT)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Short.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Short.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Short.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Integer.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Long.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      if (o instanceof DECIMAL.UNSIGNED)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(this);
    }
  }

  public abstract static class Numeric<T extends Number> extends Primitive<T> implements Comparable<DataType<? extends Number>>, kind.Numeric<T> {
    Numeric(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Numeric(final Numeric<T> copy) {
      super(copy);
    }

    Numeric() {
      super();
    }

    abstract boolean unsigned();
    public abstract T min();
    public abstract T max();

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    public final boolean equals(final Object obj) {
      return obj == this || obj instanceof Numeric && compareTo((Numeric<?>)obj) == 0;
    }
  }

  public static final TINYINT TINYINT() {
    return TINYINT.NULL;
  }

  public static final TINYINT TINYINT(final int i) {
    TINYINT singleton = TINYINT.singletons.get(i);
    if (singleton == null)
      TINYINT.singletons.put(i, singleton = TINYINT.NULL.clone());

    return singleton;
  }

  public static class TINYINT extends ExactNumeric<Byte> implements kind.TINYINT {
    public static final TINYINT.UNSIGNED UNSIGNED() {
      return TINYINT.UNSIGNED.NULL;
    }

    public static final TINYINT.UNSIGNED UNSIGNED(final int i) {
      TINYINT.UNSIGNED singleton = TINYINT.UNSIGNED.singletons.get(i);
      if (singleton == null)
        TINYINT.UNSIGNED.singletons.put(i, singleton = TINYINT.UNSIGNED.NULL.clone());

      return singleton;
    }

    public static class UNSIGNED extends ExactNumeric<Short> implements kind.TINYINT.UNSIGNED {
      public static final TINYINT.UNSIGNED NULL = new TINYINT.UNSIGNED();

      private static final IdentityHashMap<Integer,TINYINT.UNSIGNED> singletons = new IdentityHashMap<>();
      private static final Class<Short> type = Short.class;

      private final Short min;
      private final Short max;
      private boolean isNull = true;
      private short value;

      UNSIGNED(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
        super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        if (_default != null) {
          checkValue(_default);
          this.value = _default;
          this.isNull = false;
        }

        this.min = min;
        this.max = max;
      }

      UNSIGNED(final TINYINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Short value) {
        super(value == null ? null : (int)Numbers.precision(value));
        this.min = null;
        this.max = null;
        if (value != null)
          set(value);
      }

      public UNSIGNED(final short value) {
        this();
        set(value);
      }

      public UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public final UNSIGNED set(final TINYINT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final boolean set(final Short value) {
        return value != null ? set((short)value) : isNull && (isNull = true);
      }

      public final boolean set(final short value) {
        checkMutable();
        checkValue(value);
        wasSet = true;
        final boolean changed = isNull || this.value != value;
        this.value = value;
        this.isNull = false;
        return changed;
      }

      private final void checkValue(final short value) {
        if (min != null && value < min || max != null && max < value)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);

        if (value < 0)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value [" + value + "] must be positive for unsigned type");
      }

      public short getAsShort() {
        return value;
      }

      @Override
      public Short get() {
        return isNull ? null : value;
      }

      @Override
      public boolean isNull() {
        return isNull;
      }

      @Override
      public final Short min() {
        return min;
      }

      @Override
      public final Short max() {
        return max;
      }

      @Override
      final int scale() {
        return 0;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final Short minValue() {
        return 0;
      }

      @Override
      final Short maxValue() {
        return 255;
      }

      @Override
      final int maxPrecision() {
        return 3;
      }

      @Override
      final String declare(final DBVendor vendor) {
        return vendor.getDialect().compileInt8((byte)precision(), unsigned());
      }

      @Override
      final Class<Short> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.TINYINT;
      }

      @Override
      final String primitiveToString() {
        return String.valueOf(value);
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        checkMutable();
        if (isNull)
          statement.setNull(parameterIndex, sqlType());
        else
          statement.setShort(parameterIndex, value);
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        if (isNull)
          resultSet.updateNull(columnIndex);
        else
          resultSet.updateShort(columnIndex, value);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        checkMutable();
        this.columnIndex = columnIndex;
        final short value = resultSet.getShort(columnIndex);
        this.value = (isNull = resultSet.wasNull()) ? 0 : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof FLOAT)
          return new FLOAT();

        if (dataType instanceof FLOAT.UNSIGNED)
          return new FLOAT.UNSIGNED();

        if (dataType instanceof DOUBLE)
          return new DOUBLE();

        if (dataType instanceof DOUBLE.UNSIGNED)
          return new DOUBLE.UNSIGNED();

        if (dataType instanceof TINYINT)
          return new TINYINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof TINYINT.UNSIGNED)
          return new TINYINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof SMALLINT)
          return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof SMALLINT.UNSIGNED)
          return new SMALLINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof INT)
          return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof INT.UNSIGNED)
          return new INT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof BIGINT.UNSIGNED)
          return new BIGINT.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof DECIMAL.UNSIGNED) {
          final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
          return new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final TINYINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (TINYINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public int compareTo(final DataType<? extends Number> o) {
        if (o == null || o.isNull())
          return isNull() ? 0 : 1;

        if (isNull())
          return -1;

        if (o instanceof TINYINT)
          return Short.compare(value, ((TINYINT)o).value);

        if (o instanceof TINYINT.UNSIGNED)
          return Short.compare(value, ((TINYINT.UNSIGNED)o).value);

        if (o instanceof SMALLINT)
          return Short.compare(value, ((SMALLINT)o).value);

        if (o instanceof SMALLINT.UNSIGNED)
          return Integer.compare(value, ((SMALLINT.UNSIGNED)o).value);

        if (o instanceof INT)
          return Integer.compare(value, ((INT)o).value);

        if (o instanceof INT.UNSIGNED)
          return Long.compare(value, ((INT.UNSIGNED)o).value);

        if (o instanceof BIGINT)
          return Long.compare(value, ((BIGINT)o).value);

        if (o instanceof BIGINT.UNSIGNED)
          return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

        if (o instanceof FLOAT)
          return Float.compare(value, ((FLOAT)o).value);

        if (o instanceof FLOAT.UNSIGNED)
          return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

        if (o instanceof DOUBLE)
          return Double.compare(value, ((DOUBLE)o).value);

        if (o instanceof DOUBLE.UNSIGNED)
          return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

        if (o instanceof DECIMAL)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

        if (o instanceof DECIMAL.UNSIGNED)
          return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

        throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
      }

      @Override
      public final TINYINT.UNSIGNED clone() {
        return new TINYINT.UNSIGNED(this);
      }
    }

    public static final TINYINT NULL = new TINYINT();

    private static final IdentityHashMap<Integer,TINYINT> singletons = new IdentityHashMap<>();
    private static final Class<Byte> type = Byte.class;

    private final Byte min;
    private final Byte max;
    private boolean isNull = true;
    private byte value;

    TINYINT(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final boolean keyForUpdate, final int precision, final Byte min, final Byte max) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.value = _default;
        this.isNull = false;
      }

      this.min = min;
      this.max = max;
    }

    TINYINT(final TINYINT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public TINYINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    public TINYINT(final Byte value) {
      super(value == null ? null : (int)Numbers.precision(value));
      this.min = null;
      this.max = null;
      if (value != null)
        set(value);
    }

    public TINYINT(final byte value) {
      this();
      set(value);
    }

    public TINYINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public final TINYINT set(final TINYINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Byte value) {
      return value != null ? set((byte)value) : isNull && (isNull = true);
    }

    public final boolean set(final byte value) {
      checkMutable();
      checkValue(value);
      wasSet = true;
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final byte value) {
      if (min != null && value < min || max != null && max < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + min + ", " + max + "] exceeded: " + value);
    }

    public byte getAsByte() {
      return value;
    }

    @Override
    public Byte get() {
      return isNull ? null : value;
    }

    @Override
    public boolean isNull() {
      return isNull;
    }

    @Override
    public final Byte min() {
      return min;
    }

    @Override
    public final Byte max() {
      return max;
    }

    @Override
    final int scale() {
      return 0;
    }

    @Override
    final boolean unsigned() {
      return false;
    }

    @Override
    final Byte minValue() {
      return -128;
    }

    @Override
    final Byte maxValue() {
      return 127;
    }

    @Override
    final int maxPrecision() {
      return 3;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().compileInt8((byte)precision(), unsigned());
    }

    @Override
    final Class<Byte> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.TINYINT;
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(value);
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setByte(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      if (isNull)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateByte(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      final byte value = resultSet.getByte(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT || dataType instanceof FLOAT.UNSIGNED)
        return new FLOAT();

      if (dataType instanceof DOUBLE || dataType instanceof DOUBLE.UNSIGNED)
        return new DOUBLE();

      if (dataType instanceof TINYINT || dataType instanceof TINYINT.UNSIGNED)
        return new TINYINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof SMALLINT || dataType instanceof SMALLINT.UNSIGNED)
        return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof INT || dataType instanceof INT.UNSIGNED)
        return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof BIGINT || dataType instanceof BIGINT.UNSIGNED)
        return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL.UNSIGNED) {
        final DECIMAL.UNSIGNED decimal = (DECIMAL.UNSIGNED)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final TINYINT wrapper(final Evaluable wrapper) {
      return (TINYINT)super.wrapper(wrapper);
    }

    @Override
    public int compareTo(final DataType<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Byte.compare(value, ((TINYINT)o).value);

      if (o instanceof TINYINT.UNSIGNED)
        return Short.compare(value, ((TINYINT.UNSIGNED)o).value);

      if (o instanceof SMALLINT)
        return Short.compare(value, ((SMALLINT)o).value);

      if (o instanceof SMALLINT.UNSIGNED)
        return Integer.compare(value, ((SMALLINT.UNSIGNED)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof INT.UNSIGNED)
        return Long.compare(value, ((INT.UNSIGNED)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof BIGINT.UNSIGNED)
        return BigInt.compareTo(BigInt.valueOf(value), ((BIGINT.UNSIGNED)o).value.val());

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof FLOAT.UNSIGNED)
        return Float.compare(value, ((FLOAT.UNSIGNED)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DOUBLE.UNSIGNED)
        return Double.compare(value, ((DOUBLE.UNSIGNED)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      if (o instanceof DECIMAL.UNSIGNED)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL.UNSIGNED)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final TINYINT clone() {
      return new TINYINT(this);
    }
  }

  public abstract static class Subject<T> extends Evaluable implements kind.Subject<T> {
    private Evaluable wrapper;

    final Evaluable wrapper() {
      return wrapper;
    }

    type.Subject<T> wrapper(final Evaluable wrapper) {
      this.wrapper = wrapper;
      return this;
    }
  }

  public abstract static class Temporal<T extends java.time.temporal.Temporal> extends Objective<T> implements Comparable<DataType<? extends java.time.temporal.Temporal>>, kind.Temporal<T> {
    Temporal(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Temporal(final Temporal<T> copy) {
      super(copy);
    }

    Temporal() {
      super();
    }

    @Override
    final java.time.temporal.Temporal evaluate(final Set<Evaluable> visited) {
      return (java.time.temporal.Temporal)super.evaluate(visited);
    }

    @Override
    public String toString() {
      return value == null ? "NULL" : value.toString();
    }
  }

  public abstract static class Textual<T extends CharSequence & Comparable<?>> extends Objective<T> implements kind.Textual<T>, Comparable<Textual<?>> {
    private final Short length;

    Textual(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.length = (short)length;
    }

    Textual(final Textual<T> copy, final Short length) {
      super(copy);
      this.length = length;
    }

    Textual(final Short length) {
      super();
      this.length = length;
    }

    public final Short length() {
      return length;
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof Textual)
        return new CHAR(Math.max(length(), ((Textual<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    String evaluate(final Set<Evaluable> visited) {
      return (String)super.evaluate(visited);
    }

    @Override
    public final int compareTo(final Textual<?> o) {
      return o == null || o.value == null ? value == null ? 0 : 1 : value == null ? -1 : value.toString().compareTo(o.value.toString());
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (!(obj instanceof Textual))
        return false;

      final Textual<?> that = (Textual<?>)obj;
      return name.equals(that.name) && (value == null ? that.value == null : that.value != null && value.toString().equals(that.value.toString()));
    }

    @Override
    public final int hashCode() {
      return name.hashCode() + (value == null ? 0 : value.toString().hashCode());
    }
  }

  public static final TIME TIME() {
    return TIME.NULL;
  }

  public static final TIME TIME(final int i) {
    TIME singleton = TIME.singletons.get(i);
    if (singleton == null)
      TIME.singletons.put(i, singleton = TIME.NULL.clone());

    return singleton;
  }

  public static class TIME extends Temporal<LocalTime> implements kind.TIME {
    public static final TIME NULL = new TIME();

    private static final IdentityHashMap<Integer,TIME> singletons = new IdentityHashMap<>();
    private static final Class<LocalTime> type = LocalTime.class;
    private static final byte DEFAULT_PRECISION = 6;

    private final byte precision;

    TIME(final Entity owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (byte)precision;
    }

    TIME(final TIME copy) {
      super(copy);
      this.precision = copy.precision;
    }

    public TIME(final int precision) {
      super();
      this.precision = (byte)precision;
    }

    public TIME() {
      this(DEFAULT_PRECISION);
    }

    public TIME(final LocalTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    public final TIME set(final TIME value) {
      super.set(value);
      return this;
    }

    public final int precision() {
      return precision;
    }

    @Override
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareTime(precision);
    }

    @Override
    final Class<LocalTime> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.TIME;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      checkMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof TIME)
        return new DATETIME(Math.max(precision(), ((TIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return value == null ? 0 : 1;

      if (!(o instanceof TIME))
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return value.compareTo(((TIME)o).value);
    }

    @Override
    final TIME wrapper(final Evaluable wrapper) {
      return (TIME)super.wrapper(wrapper);
    }

    @Override
    public final TIME clone() {
      return new TIME(this);
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : value.format(Dialect.TIME_FORMAT);
    }
  }

  private type() {
  }
}