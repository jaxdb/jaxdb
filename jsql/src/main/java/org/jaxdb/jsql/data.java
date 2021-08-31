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
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.lang.Assertions;
import org.libj.lang.Classes;
import org.libj.lang.Numbers;
import org.libj.math.BigInt;
import org.libj.math.FastMath;
import org.libj.math.SafeMath;
import org.libj.util.function.Throwing;

public final class data {
  private static final IdentityHashMap<Class<?>,Class<?>> typeToGeneric = new IdentityHashMap<>(17);

  static {
    typeToGeneric.put(null, ENUM.class);
    for (final Class<?> member : data.class.getClasses()) {
      if (!Modifier.isAbstract(member.getModifiers())) {
        final Type type = Classes.getSuperclassGenericTypes(member)[0];
        if (type instanceof Class<?>)
          typeToGeneric.put((Class<?>)type, member);
      }
    }
  }

  private static Constructor<?> lookupColumnConstructor(Class<?> genericType) {
    Class<?> cls;
    while ((cls = typeToGeneric.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(cls, genericType);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static <V,C extends Column<V>>C wrap(final V value) {
    if (value.getClass().isEnum())
      return (C)new ENUM((Enum)value);

    return (C)newInstance(lookupColumnConstructor(value.getClass()), value);
  }

  @SuppressWarnings("unchecked")
  static <E>ARRAY<E> wrap(final E[] value) {
    final ARRAY<E> array;
    if (value.getClass().getComponentType().isEnum())
      array = new ARRAY<>((Class<? extends Column<E>>)value.getClass().getComponentType());
    else
      array = new ARRAY<>((Class<? extends Column<E>>)typeToGeneric.get(value.getClass().getComponentType()));

    array.set(value);
    return array;
  }

  public abstract static class ApproxNumeric<V extends Number> extends Numeric<V> implements type.ApproxNumeric<V> {
    ApproxNumeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    ApproxNumeric(final Numeric<V> copy) {
      super(copy);
    }

    ApproxNumeric(final boolean mutable) {
      super(mutable);
    }
  }

  interface NULL {
  }

  public static class ARRAY<T> extends Objective<T[]> implements type.ARRAY<T> {
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static final class NULL extends ARRAY implements data.NULL {
      private NULL() {
        super(ARRAY.class);
      }
    }

    public static final NULL NULL = new NULL();

    final Column<T> column;
    private Class<T[]> type;

    ARRAY(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final boolean keyForUpdate, final Class<? extends Column<T>> type) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.column = newInstance(Classes.getDeclaredConstructor(type));
    }

    @SuppressWarnings("unchecked")
    ARRAY(final ARRAY<T> copy) {
      this(copy.table(), true, copy.name, copy.unique, copy.primary, copy.nullable, copy.value, copy.generateOnInsert, copy.generateOnUpdate, copy.keyForUpdate, (Class<? extends Column<T>>)copy.column.getClass());
      this.type = copy.type;
    }

    public ARRAY(final Class<? extends Column<T>> type) {
      this(null, true, null, false, false, true, null, null, null, false, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, true, null, false, false, true, value, null, null, false, (Class<? extends Column<T>>)value.getClass().getComponentType());
    }

    public final ARRAY<T> set(final type.ARRAY<T> value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final ARRAY<T> set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final ARRAY<T> copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    @Override
    final String declare(final DBVendor vendor) {
      // FIXME
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    final Class<T[]> type() {
      return type == null ? type = (Class<T[]>)Array.newInstance(column.type(), 0).getClass() : type;
    }

    @Override
    final int sqlType() {
      return Types.ARRAY;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setArray(parameterIndex, new SQLArray<>(this));
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
    }

    @Override
    @SuppressWarnings("unchecked")
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final java.sql.Array array = resultSet.getArray(columnIndex);
      set((T[])array.getArray());
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compileArray(this, column);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      throw new UnsupportedOperationException();
    }

    @Override
    final ARRAY<T> wrap(final Evaluable wrapped) {
      return (ARRAY<T>)super.wrap(wrapped);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<>((Class<? extends Column<T>>)column.getClass());
    }

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof ARRAY && Arrays.equals(value, ((ARRAY<?>)obj).value);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(value);
    }
  }

  private static BIGINT $bigint;

  public static final BIGINT BIGINT() {
    return $bigint == null ? $bigint = new BIGINT(false) : $bigint;
  }

  public static class BIGINT extends ExactNumeric<Long> implements type.BIGINT {
    public static final class NULL extends BIGINT implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Long> type = Long.class;

    private final Long min;
    private final Long max;
    private boolean isNull = true;
    private long value;

    BIGINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final Integer precision, final Long min, final Long max) {
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
      this(precision, true);
    }

    public BIGINT(final Integer precision) {
      this(precision, true);
    }

    public BIGINT(final Long value) {
      this(value == null ? null : Integer.valueOf(Numbers.precision(value)));
      if (value != null)
        set(value);
    }

    public BIGINT(final long value) {
      this(Numbers.precision(value));
      set(value);
    }

    public BIGINT() {
      this(true);
    }

    private BIGINT(final Integer precision, final boolean mutable) {
      super(precision, mutable);
      this.min = null;
      this.max = null;
    }

    private BIGINT(final boolean mutable) {
      this((Integer)null, mutable);
    }

    public final BIGINT set(final type.BIGINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BIGINT set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final BIGINT copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Long value) {
      return value != null ? set((long)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final long value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final long value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final long value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public long getAsLong() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public long getAsLong(final long defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Long get() {
      return isNull() ? null : value;
    }

    @Override
    public Long get(final Long defaultValue) {
      return isNull() ? defaultValue : value;
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
    final Integer scale() {
      return 0;
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
      return vendor.getDialect().compileInt64(Numbers.cast(precision(), Byte.class), min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setLong(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateLong(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final long value = resultSet.getLong(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), decimal.scale());
      }

      if (column instanceof ApproxNumeric)
        return new DOUBLE();

      if (column instanceof ExactNumeric) {
        final ExactNumeric<?> exactNumeric = (ExactNumeric<?>)column;
        return new BIGINT(precision() == null || exactNumeric.precision() == null ? null : SafeMath.max((int)precision(), exactNumeric.precision() + 1));
      }

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final BIGINT wrap(final Evaluable wrapped) {
      return (BIGINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Long.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Long.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Long.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final BIGINT clone() {
      return new BIGINT(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Long.hashCode(value));
    }
  }

  private static BINARY $binary;

  public static final BINARY BINARY() {
    return $binary == null ? $binary = new BINARY(false) : $binary;
  }

  public static class BINARY extends Objective<byte[]> implements type.BINARY {
    public static final class NULL extends BINARY implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<byte[]> type = byte[].class;

    private final long length;
    private final boolean varying;

    BINARY(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final byte[] _default, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    BINARY(final BINARY copy) {
      super(copy, true);
      this.length = copy.length;
      this.varying = copy.varying;
    }

    public BINARY(final long length, final boolean varying) {
      super(true);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    public BINARY(final long length) {
      this(length, false);
    }

    public BINARY(final byte[] value) {
      this(value.length, false);
      set(value);
    }

    private BINARY(final boolean mutable) {
      super(mutable);
      this.length = 0;
      this.varying = false;
    }

    public final BINARY set(final type.BINARY value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BINARY set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final BINARY copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    private void checkLength(final long length) {
      if (length <= 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " illegal length: " + length);
    }

    public final long length() {
      return length;
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBytes(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBytes(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BINARY)
        return new BINARY(SafeMath.max(length(), ((BINARY)column).length()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final BINARY wrap(final Evaluable wrapped) {
      return (BINARY)super.wrap(wrapped);
    }

    @Override
    public final BINARY clone() {
      return new BINARY(this);
    }

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof BINARY && Arrays.equals(value, ((BINARY)obj).value);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(value);
    }
  }

  private static BLOB $blob;

  public static final BLOB BLOB() {
    return $blob == null ? $blob = new BLOB(false) : $blob;
  }

  public static class BLOB extends LargeObject<InputStream> implements type.BLOB {
    public static final class NULL extends BLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<InputStream> type = InputStream.class;

    BLOB(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final InputStream _default, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    BLOB(final BLOB copy) {
      super(copy, true);
    }

    public BLOB(final long length) {
      super(length, true);
    }

    public BLOB(final InputStream value) {
      super((Long)null, true);
      set(value);
    }

    public BLOB() {
      this(true);
    }

    private BLOB(final boolean mutable) {
      super((Long)null, mutable);
    }

    public final BLOB set(final type.BLOB value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BLOB set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final BLOB copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BLOB)
        return new BLOB(SafeMath.max(length(), ((BLOB)column).length()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final BLOB wrap(final Evaluable wrapped) {
      return (BLOB)super.wrap(wrapped);
    }

    @Override
    public final BLOB clone() {
      return new BLOB(this);
    }

    @Override
    public final boolean equals(final Object obj) {
      // FIXME: This performs an object identity check. Otherwise, we'd need to
      // FIXME: fully read and rewind the InputStream to perform a content check.
      return super.equals(obj) && obj instanceof BLOB && value == ((BLOB)obj).value;
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Objects.hashCode(value);
    }
  }

  private static BOOLEAN $boolean;

  public static final BOOLEAN BOOLEAN() {
    return $boolean == null ? $boolean = new BOOLEAN(false) : $boolean;
  }

  public static class BOOLEAN extends Condition<Boolean> implements type.BOOLEAN, Comparable<Column<Boolean>> {
    public static final class NULL extends BOOLEAN implements data.NULL {
      private NULL() {
        super((Class<BOOLEAN>)null);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Boolean> type = Boolean.class;

    private boolean isNull = true;
    private boolean value;

    BOOLEAN(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Boolean _default, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      if (_default != null) {
        this.value = _default;
        this.isNull = false;
      }
    }

    BOOLEAN(final BOOLEAN copy) {
      super(copy);
    }

    public BOOLEAN(final Boolean value) {
      super(true);
      if (value != null)
        set(value);
    }

    public BOOLEAN(final boolean value) {
      super(true);
      set(value);
    }

    public BOOLEAN() {
      super(true);
    }

    @SuppressWarnings("unused")
    private BOOLEAN(final Class<BOOLEAN> cls) {
      super(false);
    }

    public final BOOLEAN set(final type.BOOLEAN value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BOOLEAN set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final BOOLEAN copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Boolean value) {
      return value != null ? set((boolean)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final boolean value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final boolean value) {
      assertMutable();
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    public boolean getAsBoolean() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public boolean getAsBoolean(final boolean defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Boolean get() {
      return isNull() ? null : value;
    }

    @Override
    public Boolean get(final Boolean defaultValue) {
      return isNull() ? defaultValue : value;
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBoolean(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBoolean(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final boolean value = resultSet.getBoolean(columnIndex);
      this.value = !(isNull = resultSet.wasNull()) && value;
    }

    @Override
    String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final BOOLEAN wrap(final Evaluable wrapped) {
      return (BOOLEAN)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<Boolean> o) {
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

  private static CHAR $char;

  public static final CHAR CHAR() {
    return $char == null ? $char = new CHAR(false) : $char;
  }

  public static class CHAR extends Textual<String> implements type.CHAR {
    public static final class NULL extends CHAR implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<String> type = String.class;

    private final boolean varying;

    CHAR(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final String _default, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    CHAR(final CHAR copy) {
      super(copy, copy.length(), true);
      this.varying = copy.varying;
    }

    public CHAR(final long length, final boolean varying) {
      super((short)length, true);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final Long length, final boolean varying) {
      super(Numbers.cast(length, Short.class), true);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final int length) {
      this(length, false);
    }

    public CHAR(final Integer length) {
      this(length, false);
    }

    public CHAR(final String value) {
      this(value.length(), true);
      set(value);
    }

    public CHAR() {
      this(true);
    }

    private CHAR(final boolean mutable) {
      super(null, mutable);
      this.varying = true;
    }

    public final CHAR set(final type.CHAR value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final CHAR set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final CHAR copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
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
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final CHAR wrap(final Evaluable wrapped) {
      return (CHAR)super.wrap(wrapped);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(this);
    }
  }

  private static CLOB $clob;

  public static final CLOB CLOB() {
    return $clob == null ? $clob = new CLOB(false) : $clob;
  }

  public static class CLOB extends LargeObject<Reader> implements type.CLOB {
    public static final class NULL extends CLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Reader> type = Reader.class;

    CLOB(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Reader _default, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    CLOB(final CLOB copy) {
      super(copy, true);
    }

    public CLOB(final long length) {
      super(length, true);
    }

    public CLOB(final Reader value) {
      super((Long)null, true);
      set(value);
    }

    public CLOB() {
      this(true);
    }

    private CLOB(final boolean mutable) {
      super((Long)null, mutable);
    }

    public final CLOB set(final type.CLOB value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final CLOB set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final CLOB copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof CLOB)
        return new CLOB(SafeMath.max(length(), ((CLOB)column).length()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final CLOB wrap(final Evaluable wrapped) {
      return (CLOB)super.wrap(wrapped);
    }

    @Override
    public final CLOB clone() {
      return new CLOB(this);
    }

    @Override
    public final boolean equals(final Object obj) {
      // FIXME: This performs an object identity check. Otherwise, we'd need to
      // FIXME: fully read and rewind the InputStream to perform a content check.
      return super.equals(obj) && obj instanceof CLOB && value == ((CLOB)obj).value;
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Objects.hashCode(value);
    }
  }

  private static DATE $date;

  public static final DATE DATE() {
    return $date == null ? $date = new DATE(false) : $date;
  }

  public static class DATE extends Temporal<LocalDate> implements type.DATE {
    public static final class NULL extends DATE implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<LocalDate> type = LocalDate.class;

    DATE(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDate _default, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    DATE(final DATE copy) {
      super(copy, true);
    }

    public DATE(final LocalDate value) {
      super(true);
      set(value);
    }

    public DATE() {
      super(true);
    }

    private DATE(final boolean mutable) {
      super(mutable);
    }

    public final DATE set(final type.DATE value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DATE set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final DATE copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
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
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final DATE wrap(final Evaluable wrapped) {
      return (DATE)super.wrap(wrapped);
    }

    @Override
    public final DATE clone() {
      return new DATE(this);
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATE ? value.compareTo(((DATE)o).value) : LocalDateTime.of(value, LocalTime.MIDNIGHT).compareTo(((DATETIME)o).value);
    }

    @Override
    public final boolean equals(final Temporal<?> obj) {
      return (obj instanceof DATE || obj instanceof DATETIME) && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateToString(value);
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

  public abstract static class Column<V> extends Entity<V> implements type.Column<V> {
    boolean setValue(final V value) {
      assertMutable();

      // FIXME: Can we get away from this wasSet hack?
      final boolean wasSet = this.wasSet;
      final boolean result = set(value);
      this.wasSet = wasSet;
      return result;
    }

    static <V>String compile(final Column<V> column, final DBVendor vendor) throws IOException {
      return column.compile(vendor);
    }

    static String getSimpleName(final Class<?> cls) {
      final String canonicalName = cls.getCanonicalName();
      return canonicalName.substring(canonicalName.indexOf("col.") + 5).replace('.', ' ');
    }

    private final Table table;
    final String name;
    final boolean unique;
    final boolean primary;
    final boolean nullable;
    final GenerateOn<? super V> generateOnInsert;
    final GenerateOn<? super V> generateOnUpdate;
    final boolean keyForUpdate;

    Column(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(mutable);
      this.table = owner;
      this.name = name;
      this.unique = unique;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;
    }

    Column(final Column<V> copy) {
      super(true);
      this.table = copy.table;
      this.name = copy.name;
      this.unique = copy.unique;
      this.primary = copy.primary;
      this.nullable = copy.nullable;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.keyForUpdate = copy.keyForUpdate;

      // NOTE: Deliberately not copying ref or wasSet
      // this.ref = copy.ref;
      // this.wasSet = copy.wasSet;
    }

    Column(final boolean mutable) {
      this(null, mutable, null, false, false, true, null, null, false);
    }

    @Override
    Table table() {
      return table;
    }

    @Override
    Column<?> column() {
      return this;
    }

    int columnIndex;
    type.Column<V> ref;
    boolean wasSet;

    public abstract boolean set(V value);

    void set(final type.Column<V> ref) {
      assertMutable();
      this.wasSet = false;
      this.ref = ref;
    }

    void setNull() {
      assertMutable();
      this.wasSet = true;
      this.ref = null;
    }

    public abstract V get();
    public abstract V get(V defaultValue);
    public abstract boolean isNull();

    public final boolean wasSet() {
      return wasSet;
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
      assertMutable();
      if (rows.getConcurrency() == Concurrency.READ_ONLY)
        throw new IllegalStateException(rows.getConcurrency().getClass().getSimpleName() + "." + rows.getConcurrency());

      update(rows.resultSet, columnIndex);
    }

    public final <C extends Column<V>>C AS(final C column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    public final <E extends Enum<?> & EntityEnum>ENUM<E> AS(final ENUM<E> column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      Compiler.compile(this, compilation, isExpression);
    }

    @Override
    Object evaluate(final Set<Evaluable> visited) {
      if (ref == null || visited.contains(this))
        return wrapped() != null ? wrapped().evaluate(visited) : get();

      visited.add(this);
      return ((Evaluable)ref).evaluate(visited);
    }

    abstract Class<V> type();
    abstract int sqlType();
    abstract void get(PreparedStatement statement, int parameterIndex) throws IOException, SQLException;
    abstract void set(ResultSet resultSet, int columnIndex) throws SQLException;
    abstract void update(ResultSet resultSet, int columnIndex) throws SQLException;
    abstract String compile(DBVendor vendor) throws IOException;
    abstract String declare(DBVendor vendor);
    abstract Column<?> scaleTo(Column<?> column);

    @Override
    public abstract Column<V> clone();

    @Override
    public boolean equals(final Object obj) {
      return this == obj || obj instanceof Column && name.equals(((Column<?>)obj).name);
    }

    @Override
    public int hashCode() {
      return name.hashCode();
    }

    @Override
    public String toString() {
      return String.valueOf(get());
    }
  }

  private static DATETIME $datetime;

  public static final DATETIME DATETIME() {
    return $datetime == null ? $datetime = new DATETIME(false) : $datetime;
  }

  public static class DATETIME extends Temporal<LocalDateTime> implements type.DATETIME {
    public static final class NULL extends DATETIME implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<LocalDateTime> type = LocalDateTime.class;
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final byte DEFAULT_PRECISION = 6;

    private final Byte precision;

    DATETIME(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final boolean keyForUpdate, final Integer precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = precision == null ? null : Byte.valueOf(precision.byteValue());
    }

    DATETIME(final DATETIME copy) {
      super(copy, true);
      this.precision = copy.precision;
    }

    public DATETIME(final int precision) {
      super(true);
      this.precision = (byte)precision;
    }

    public DATETIME(final Integer precision) {
      super(true);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public DATETIME() {
      this(true);
    }

    private DATETIME(final boolean mutable) {
      super(mutable);
      this.precision = null;
    }

    public DATETIME(final LocalDateTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    public final DATETIME set(final type.DATETIME value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DATETIME set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final DATETIME copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    public final Byte precision() {
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
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATETIME)
        return new DATETIME(SafeMath.max(precision(), ((DATETIME)column).precision()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final DATETIME wrap(final Evaluable wrapped) {
      return (DATETIME)super.wrap(wrapped);
    }

    @Override
    public DATETIME clone() {
      return new DATETIME(this);
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATETIME ? value.compareTo(((DATETIME)o).value) : value.toLocalDate().compareTo(((DATE)o).value);
    }

    @Override
    public final boolean equals(final Temporal<?> obj) {
      return (obj instanceof DATE || obj instanceof DATETIME) && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateTimeToString(value);
    }
  }

  private static DECIMAL $decimal;

  public static final DECIMAL DECIMAL() {
    return $decimal == null ? $decimal = new DECIMAL(false) : $decimal;
  }

  public static class DECIMAL extends ExactNumeric<BigDecimal> implements type.DECIMAL {
    public static final class NULL extends DECIMAL implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<BigDecimal> type = BigDecimal.class;
    private static final byte maxScale = 38;

    private final Integer scale;
    private final BigDecimal min;
    private final BigDecimal max;
    private BigDecimal value;

    DECIMAL(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final Integer precision, final int scale, final BigDecimal min, final BigDecimal max) {
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
      super(precision, true);
      checkScale(precision, scale);
      this.scale = scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final Integer precision, final Integer scale) {
      super(precision, true);
      if (precision == null) {
        if (scale != null)
          throw new IllegalArgumentException("Both \"precision\" and \"scale\" must be null or not null");
      }
      else if (scale == null) {
        throw new IllegalArgumentException("Both \"precision\" and \"scale\" must be null or not null");
      }
      else {
        checkScale(precision, scale);
      }

      this.scale = scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final BigDecimal value) {
      super(value == null ? null : value.precision(), true);
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
      this(true);
    }

    private DECIMAL(final boolean mutable) {
      super(null, mutable);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    public DECIMAL set(final type.DECIMAL value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DECIMAL set(final NULL value) {
      super.setNull();
      this.value = null;
      return this;
    }

    final void copy(final DECIMAL copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final BigDecimal value) {
      assertMutable();
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
    public BigDecimal get(final BigDecimal defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public boolean isNull() {
      return value == null;
    }

    private final void checkValue(final BigDecimal value) {
      if (min != null && value.compareTo(min) < 0 || max != null && max.compareTo(value) < 0)
        throw valueRangeExceeded(min, max, value);
    }

    private void checkScale(final int precision, final int scale) {
      if (precision < scale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");

      if (scale > maxScale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
    }

    @Override
    public final Integer scale() {
      return scale;
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
      return vendor.getDialect().declareDecimal(precision(), scale(), min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBigDecimal(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBigDecimal(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final BigDecimal value = resultSet.getBigDecimal(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), SafeMath.max(scale(), decimal.scale()));
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), SafeMath.max(scale(), decimal.scale()));
      }

      if (column instanceof ApproxNumeric)
        return new DECIMAL(precision() == null ? null : precision() + 1, scale());

      if (column instanceof ExactNumeric) {
        final ExactNumeric<?> exactNumeric = (ExactNumeric<?>)column;
        final Integer precision = precision() == null || exactNumeric.precision() == null ? null : SafeMath.max((int)precision(), exactNumeric.precision() + 1);
        return new DECIMAL(precision, scale());
      }

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final DECIMAL wrap(final Evaluable wrapped) {
      return (DECIMAL)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return value.compareTo(BigDecimal.valueOf(((TINYINT)o).value));

      if (o instanceof SMALLINT)
        return value.compareTo(BigDecimal.valueOf(((SMALLINT)o).value));

      if (o instanceof INT)
        return value.compareTo(BigDecimal.valueOf(((INT)o).value));

      if (o instanceof BIGINT)
        return value.compareTo(BigDecimal.valueOf(((BIGINT)o).value));

      if (o instanceof FLOAT)
        return value.compareTo(BigDecimal.valueOf(((FLOAT)o).value));

      if (o instanceof DOUBLE)
        return value.compareTo(BigDecimal.valueOf(((DOUBLE)o).value));

      if (o instanceof DECIMAL)
        return value.compareTo(((DECIMAL)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Objects.hashCode(value);
    }

    @Override
    public String toString() {
      return isNull() ? "NULL" : value.toString();
    }
  }

  private static DOUBLE $double;

  public static final DOUBLE DOUBLE() {
    return $double == null ? $double = new DOUBLE(false) : $double;
  }

  public static class DOUBLE extends ApproxNumeric<Double> implements type.DOUBLE {
    public static final class NULL extends DOUBLE implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Double> type = Double.class;

    private final Double min;
    private final Double max;
    private boolean isNull = true;
    private double value;

    DOUBLE(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
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
      this(true);
    }

    private DOUBLE(final boolean mutable) {
      super(mutable);
      this.min = null;
      this.max = null;
    }

    public final DOUBLE set(final type.DOUBLE value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DOUBLE set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final DOUBLE copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Double value) {
      return value != null ? set((double)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final double value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final double value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final double value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public double getAsDouble() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public double getAsDouble(final double defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Double get() {
      return isNull() ? null : value;
    }

    @Override
    public Double get(final Double defaultValue) {
      return isNull() ? defaultValue : value;
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
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDouble(min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setDouble(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateDouble(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final double value = resultSet.getDouble(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? Double.NaN : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision() == null ? null : decimal.precision() + 1, decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision() == null ? null : decimal.precision() + 1, decimal.scale());
      }

      if (column instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final DOUBLE wrap(final Evaluable wrapped) {
      return (DOUBLE)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Double.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Double.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Double.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Double.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Double.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return Double.compare(value, ((DECIMAL)o).value.doubleValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public DOUBLE clone() {
      return new DOUBLE(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Double.hashCode(value));
    }
  }

  private static ENUM<?> $enum;

  public static final ENUM<?> ENUM() {
    return $enum == null ? $enum = new ENUM<>(false) : $enum;
  }

  public static class ENUM<E extends Enum<?> & EntityEnum> extends Textual<E> implements type.ENUM<E> {
    // FIXME: data.ENUM.NULL
    // @org.jaxdb.jsql.EntityEnum.Spec(table="relation", column="units")
    private enum NULL_ENUM implements EntityEnum {
      NULL;

      @Override
      public int length() {
        return 0;
      }

      @Override
      public char charAt(final int index) {
        return 0;
      }

      @Override
      public CharSequence subSequence(int start, int end) {
        return null;
      }
    }

    public static final class NULL extends ENUM<NULL_ENUM> implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final IdentityHashMap<Class<?>,Short> typeToLength = new IdentityHashMap<>(2);
    private static volatile ConcurrentHashMap<Class<?>,Method> classToFromStringMethod;

    private final Class<E> enumType;
    private final Function<String,E> fromStringFunction;

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

    ENUM(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final E _default, final GenerateOn<? super E> generateOnInsert, final GenerateOn<? super E> generateOnUpdate, final boolean keyForUpdate, final Class<E> type, final Function<String,E> fromStringFunction) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, calcEnumLength(type));
      this.enumType = type;
      this.fromStringFunction = fromStringFunction;
    }

    ENUM(final ENUM<E> copy) {
      super(copy, copy.length(), true);
      this.enumType = copy.enumType;
      this.fromStringFunction = copy.fromStringFunction;
    }

    @SuppressWarnings("unchecked")
    public ENUM(final Class<E> enumType) {
      super(enumType == null ? null : calcEnumLength(enumType), true);
      this.enumType = enumType;
      this.fromStringFunction = enumType == null ? null : s -> {
        Method method;
        if (classToFromStringMethod == null) {
          synchronized (enumType) {
            if (classToFromStringMethod == null) {
              classToFromStringMethod = new ConcurrentHashMap<>();
              method = null;
            }
            else {
              method = classToFromStringMethod.get(enumType);
            }
          }
        }
        else {
          method = classToFromStringMethod.get(enumType);
        }

        try {
          if (method == null) {
            method = enumType.getMethod("fromString", String.class);
            classToFromStringMethod.put(enumType, method);
          }

          return (E)method.invoke(ENUM.this, s);
        }
        catch (final IllegalAccessException | NoSuchMethodException e) {
          throw new RuntimeException(e);
        }
        catch (final InvocationTargetException e) {
          if (e.getCause() instanceof RuntimeException)
            throw (RuntimeException)e.getCause();

          throw new RuntimeException(e.getCause());
        }
      };
    }

    private ENUM(final boolean mutable) {
      super(null, mutable);
      this.enumType = null;
      this.fromStringFunction = null;
    }

    @SuppressWarnings("unchecked")
    public ENUM(final E value) {
      this(value == null ? null : (Class<E>)value.getClass());
      set(value);
    }

    public ENUM<E> set(final type.ENUM<E> value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final ENUM<E> set(final NULL value) {
      super.setNull();
      return this;
    }

    void copy(final ENUM<E> copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    public final boolean setFromString(final String value) {
      assertMutable();
      return set(fromStringFunction.apply(value));
    }

    public final String getAsString() {
      return value == null ? null : value.toString();
    }

    @Override
    final String declare(final DBVendor vendor) {
      throw new UnsupportedOperationException();
    }

    @Override
    final Class<E> type() {
      return enumType;
    }

    @Override
    final int sqlType() {
      return Types.CHAR;
    }

    @Override
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setObject(parameterIndex, value.toString());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateObject(columnIndex, value.toString());
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final String value = resultSet.getString(columnIndex);
      if (value == null) {
        this.value = null;
        return;
      }

      for (final E constant : enumType.getEnumConstants()) {
        if (constant.toString().equals(value)) {
          this.value = constant;
          return;
        }
      }

      throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final ENUM<E> wrap(final Evaluable wrapped) {
      return (ENUM<E>)super.wrap(wrapped);
    }

    @Override
    public final ENUM<E> clone() {
      return new ENUM<>(this);
    }

    @Override
    final String evaluate(final Set<Evaluable> visited) {
      return isNull() ? null : value.toString();
    }
  }

  public abstract static class Table extends Entity<type.Table> implements type.Table {
    final Column<?>[] _column$;
    final Column<?>[] _primary$;
    final Column<?>[] _auto$;
    private final boolean _wasSelected$;

    Table(final boolean mutable, final boolean _wasSelected$, final Column<?>[] _column$, final Column<?>[] _primary$, final Column<?>[] _auto$) {
      super(mutable);
      this._wasSelected$ = _wasSelected$;
      this._column$ = _column$;
      this._primary$ = _primary$;
      this._auto$ = _auto$;
    }

    Table(final Table copy) {
      super(copy._mutable$);
      this._wasSelected$ = false;
      this._column$ = copy._column$.clone();
      this._primary$ = copy._primary$.clone();
      this._auto$ = copy._auto$.clone();
    }

    Table() {
      super(true);
      this._wasSelected$ = false;
      this._column$ = null;
      this._primary$ = null;
      this._auto$ = null;
    }

    final boolean wasSelected() {
      return _wasSelected$;
    }

    @Override
    final Table table() {
      return this;
    }

    @Override
    final Column<?> column() {
      throw new UnsupportedOperationException();
    }

    @Override
    final Table evaluate(final Set<Evaluable> visited) {
      return this;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation, isExpression);
    }

    public final Column<?> getColumn(final String name) {
      Assertions.assertNotNull(name);
      for (final Column<?> column : _column$)
        if (name.equals(column.name))
          return column;

      return null;
    }

    abstract String name();
    abstract Table newInstance();

    @Override
    protected abstract Table clone();

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();
  }

  public abstract static class ExactNumeric<V extends Number> extends Numeric<V> implements type.ExactNumeric<V> {
    final Integer precision;

    ExactNumeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate, final Integer precision) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Numeric<V> copy, final Integer precision) {
      super(copy);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Integer precision, final boolean mutable) {
      super(mutable);
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

    public final Integer precision() {
      return precision;
    }

    abstract Integer scale();
    abstract V minValue();
    abstract V maxValue();
    abstract int maxPrecision();

    private void checkPrecision(final Integer precision) {
      if (precision != null && maxPrecision() != -1 && precision > maxPrecision())
        throw new IllegalArgumentException(getSimpleName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }
  }

  private static FLOAT $float;

  public static final FLOAT FLOAT() {
    return $float == null ? $float = new FLOAT(false) : $float;
  }

  public static class FLOAT extends ApproxNumeric<Float> implements type.FLOAT {
    public static final class NULL extends FLOAT implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Float> type = Float.class;

    private final Float min;
    private final Float max;
    private boolean isNull = true;
    private float value;

    FLOAT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
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

    public FLOAT(final Float value) {
      this();
      if (value != null)
        set(value);
    }

    public FLOAT(final float value) {
      this();
      set(value);
    }

    public FLOAT() {
      this(true);
    }

    private FLOAT(final boolean mutable) {
      super(mutable);
      this.min = null;
      this.max = null;
    }

    public final FLOAT set(final type.FLOAT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final FLOAT set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final FLOAT copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Float value) {
      return value != null ? set((float)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final float value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final float value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final float value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public float getAsFloat() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public float getAsFloat(final float defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Float get() {
      return isNull() ? null : value;
    }

    @Override
    public Float get(final Float defaultValue) {
      return isNull() ? defaultValue : value;
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
    final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setFloat(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateFloat(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final float value = resultSet.getFloat(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? Float.NaN : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof FLOAT || column instanceof TINYINT)
        return new FLOAT();

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (column instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final FLOAT wrap(final Evaluable wrapped) {
      return (FLOAT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Float.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Float.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Float.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Float.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Float.compare(value, (float)((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return Float.compare(value, ((DECIMAL)o).value.floatValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Float.hashCode(value));
    }
  }

  public abstract static class LargeObject<V extends Closeable> extends Objective<V> implements type.LargeObject<V> {
    private final Long length;

    LargeObject(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
    }

    LargeObject(final LargeObject<V> copy, final boolean mutable) {
      super(copy, mutable);
      this.length = copy.length;
    }

    LargeObject(final Long length, final boolean mutable) {
      super(mutable);
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

  private static INT $int;

  public static final INT INT() {
    return $int == null ? $int = new INT(false) : $int;
  }

  public static class INT extends ExactNumeric<Integer> implements type.INT {
    public static final class NULL extends INT implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Integer> type = Integer.class;

    private final Integer min;
    private final Integer max;
    private boolean isNull = true;
    private int value;

    INT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final Integer precision, final Integer min, final Integer max) {
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
      super((int)precision, true);
      this.min = null;
      this.max = null;
    }

    public INT(final Short precision) {
      this(precision, true);
    }

    public INT(final Integer value) {
      this(value == null ? null : Short.valueOf(Numbers.precision(value)));
      if (value != null)
        set(value);
    }

    public INT(final int value) {
      this(Numbers.precision(value));
      set(value);
    }

    public INT() {
      this(true);
    }

    private INT(final Short precision, final boolean mutable) {
      super(precision == null ? null : precision.intValue(), mutable);
      this.min = null;
      this.max = null;
    }

    private INT(final boolean mutable) {
      this((Short)null, mutable);
    }

    public final INT set(final type.INT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final INT set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final INT copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Integer value) {
      return value != null ? set((int)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final int value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final int value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final int value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public int getAsInt() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public int getAsInt(final int defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Integer get() {
      return isNull() ? null : value;
    }

    @Override
    public Integer get(final Integer defaultValue) {
      return isNull() ? defaultValue : value;
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
    final Integer scale() {
      return 0;
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
      return vendor.getDialect().compileInt32(Numbers.cast(precision(), Byte.class), min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setInt(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final int value = resultSet.getInt(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof ApproxNumeric)
        return new DOUBLE();

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof ExactNumeric)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final INT wrap(final Evaluable wrapped) {
      return (INT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Integer.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Integer.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final INT clone() {
      return new INT(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Integer.hashCode(value));
    }
  }

  public abstract static class Objective<V> extends Column<V> implements type.Objective<V> {
    V value;

    Objective(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.value = _default;
    }

    Objective(final Objective<V> copy, final boolean mutable) {
      super(mutable);
      this.value = copy.value;
    }

    Objective(final boolean mutable) {
      super(mutable);
    }

    @Override
    public final boolean set(final V value) {
      assertMutable();
      wasSet = true;
      final boolean changed = !Objects.equals(this.value, value);
      this.value = value;
      return changed;
    }

    @Override
    public final void setNull() {
      super.setNull();
      this.value = null;
    }

    @Override
    public final V get() {
      return value;
    }

    @Override
    public final V get(final V defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public boolean isNull() {
      return value == null;
    }
  }

  public abstract static class Primitive<V> extends Column<V> implements type.Primitive<V> {
    Primitive(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Primitive(final Primitive<V> copy) {
      super(copy);
    }

    Primitive(final boolean mutable) {
      super(mutable);
    }

    @Override
    final void set(final type.Column<V> ref) {
      super.set(ref);
    }

    abstract String primitiveToString();

    @Override
    public String toString() {
      return isNull() ? "NULL" : primitiveToString();
    }
  }

  private static SMALLINT $smallint;

  public static final SMALLINT SMALLINT() {
    return $smallint == null ? $smallint = new SMALLINT(false) : $smallint;
  }

  public static class SMALLINT extends ExactNumeric<Short> implements type.SMALLINT {
    public static final class NULL extends SMALLINT implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Short> type = Short.class;

    private final Short min;
    private final Short max;
    private boolean isNull = true;
    private short value;

    SMALLINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final Integer precision, final Short min, final Short max) {
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
      this(precision, true);
    }

    public SMALLINT(final Integer precision) {
      this(precision, true);
    }

    public SMALLINT(final Short value) {
      this(value == null ? null : Integer.valueOf(Numbers.precision(value)));
      if (value != null)
        set(value);
    }

    public SMALLINT(final short value) {
      this((int)Numbers.precision(value));
      set(value);
    }

    public SMALLINT() {
      this(true);
    }

    private SMALLINT(final Integer precision, final boolean mutable) {
      super(precision, mutable);
      this.min = null;
      this.max = null;
    }

    private SMALLINT(final boolean mutable) {
      this((Integer)null, mutable);
    }

    public final SMALLINT set(final type.SMALLINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final SMALLINT set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final SMALLINT copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Short value) {
      return value != null ? set((short)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final short value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final short value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final short value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public short getAsShort() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public short getAsShort(final short defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Short get() {
      return isNull() ? null : value;
    }

    @Override
    public Short get(final Short defaultValue) {
      return isNull() ? defaultValue : value;
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
    final Integer scale() {
      return 0;
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
      return vendor.getDialect().compileInt16(Numbers.cast(precision(), Byte.class), min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setShort(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final short value = resultSet.getShort(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof ApproxNumeric)
        return new DOUBLE();

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof INT)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof ExactNumeric)
        return new SMALLINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final SMALLINT wrap(final Evaluable wrapped) {
      return (SMALLINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Short.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Short.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Short.hashCode(value));
    }
  }

  public abstract static class Numeric<V extends Number> extends Primitive<V> implements Comparable<Column<? extends Number>>, type.Numeric<V> {
    @SuppressWarnings("unchecked")
    static <T extends Number>T valueOf(final Number number, final Class<T> as) {
      if (float.class == as || Float.class == as)
        return (T)Float.valueOf(number.floatValue());

      if (double.class == as || Double.class == as)
        return (T)Double.valueOf(number.doubleValue());

      if (byte.class == as || Byte.class == as)
        return (T)Byte.valueOf(number.byteValue());

      if (short.class == as || Short.class == as)
        return (T)Short.valueOf(number.shortValue());

      if (int.class == as || Integer.class == as)
        return (T)Integer.valueOf(number.intValue());

      if (long.class == as || Long.class == as)
        return (T)Long.valueOf(number.longValue());

      if (number instanceof Byte || number instanceof Short || number instanceof Integer || number instanceof Long) {
        if (BigDecimal.class.isAssignableFrom(as))
          return (T)BigDecimal.valueOf(number.longValue());

        if (BigInteger.class.isAssignableFrom(as))
          return (T)BigInteger.valueOf(number.longValue());

        if (BigInt.class.isAssignableFrom(as))
          return (T)new BigInt(number.longValue());

        throw new UnsupportedOperationException("Unsupported Number type: " + as.getName());
      }

      if (number instanceof Float || number instanceof Double) {
        if (BigDecimal.class.isAssignableFrom(as))
          return (T)BigDecimal.valueOf(number.doubleValue());

        if (BigInteger.class.isAssignableFrom(as))
          return (T)BigInteger.valueOf(number.longValue());

        if (BigInt.class.isAssignableFrom(as))
          return (T)new BigInt(number.longValue());

        throw new UnsupportedOperationException("Unsupported Number type: " + as.getName());
      }

      if (number instanceof BigInteger) {
        if (BigDecimal.class.isAssignableFrom(as))
          return (T)new BigDecimal((BigInteger)number);

        if (BigInteger.class.isAssignableFrom(as))
          return (T)number;

        if (BigInt.class.isAssignableFrom(as))
          return (T)new BigInt((BigInteger)number);

        throw new UnsupportedOperationException("Unsupported Number type: " + as.getName());
      }

      if (number instanceof BigDecimal) {
        if (BigDecimal.class.isAssignableFrom(as))
          return (T)number;

        if (BigInteger.class.isAssignableFrom(as))
          return (T)((BigDecimal)number).toBigInteger();

        if (BigInt.class.isAssignableFrom(as))
          return (T)new BigInt(((BigDecimal)number).toBigInteger());

        throw new UnsupportedOperationException("Unsupported Number type: " + as.getName());
      }

      throw new UnsupportedOperationException("Unsupported Number type: " + as.getName());
    }

    IllegalArgumentException valueRangeExceeded(final Number min, final Number max, final Number value) {
      return new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + (min != null ? min : "") + ", " + (max != null ? max : "") + "] exceeded: " + value);
    }

    Numeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Numeric(final Numeric<V> copy) {
      super(copy);
    }

    Numeric(final boolean mutable) {
      super(mutable);
    }

    public abstract V min();
    public abstract V max();

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof Numeric && compareTo((Numeric<?>)obj) == 0;
    }
  }

  private static TINYINT $tinyint;

  public static final TINYINT TINYINT() {
    return $tinyint == null ? $tinyint = new TINYINT(false) : $tinyint;
  }

  public static class TINYINT extends ExactNumeric<Byte> implements type.TINYINT {
    public static final class NULL extends TINYINT implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<Byte> type = Byte.class;

    private final Byte min;
    private final Byte max;
    private boolean isNull = true;
    private byte value;

    TINYINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final boolean keyForUpdate, final Integer precision, final Byte min, final Byte max) {
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
      this(precision, true);
    }

    public TINYINT(final Integer precision) {
      this(precision, true);
    }

    public TINYINT(final Byte value) {
      this(value == null ? null : Integer.valueOf(Numbers.precision(value)));
      if (value != null)
        set(value);
    }

    public TINYINT(final byte value) {
      this((int)Numbers.precision(value));
      set(value);
    }

    public TINYINT() {
      this(true);
    }

    private TINYINT(final Integer precision, final boolean mutable) {
      super(precision, mutable);
      this.min = null;
      this.max = null;
    }

    private TINYINT(final boolean mutable) {
      this((Integer)null, mutable);
    }

    public final TINYINT set(final type.TINYINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final TINYINT set(final NULL value) {
      super.setNull();
      this.isNull = true;
      return this;
    }

    final void copy(final TINYINT copy) {
      assertMutable();
      this.value = copy.value;
      this.isNull = copy.isNull;
      this.wasSet = copy.wasSet;
    }

    @Override
    public final boolean set(final Byte value) {
      return value != null ? set((byte)value) : assertMutable() && isNull() && (isNull = true) && (wasSet = true);
    }

    public final boolean set(final byte value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final byte value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull() || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final byte value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public byte getAsByte() {
      if (isNull())
        throw new NullPointerException("NULL");

      return value;
    }

    public byte getAsByte(final byte defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public Byte get() {
      return isNull() ? null : value;
    }

    @Override
    public Byte get(final Byte defaultValue) {
      return isNull() ? defaultValue : value;
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
    final Integer scale() {
      return 0;
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
      return vendor.getDialect().compileInt8(Numbers.cast(precision(), Byte.class), min);
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
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setByte(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        // FIXME: This is updateShort (though it should be updateByte) cause PostgreSQL does String.valueOf(byte). Why does it do that?!
        resultSet.updateShort(columnIndex, value);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final byte value = resultSet.getByte(columnIndex);
      this.value = (isNull = resultSet.wasNull()) ? 0 : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof FLOAT)
        return new FLOAT();

      if (column instanceof DOUBLE)
        return new DOUBLE();

      if (column instanceof TINYINT)
        return new TINYINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof SMALLINT)
        return new SMALLINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof INT)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    final TINYINT wrap(final Evaluable wrapped) {
      return (TINYINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TINYINT)
        return Byte.compare(value, ((TINYINT)o).value);

      if (o instanceof SMALLINT)
        return Short.compare(value, ((SMALLINT)o).value);

      if (o instanceof INT)
        return Integer.compare(value, ((INT)o).value);

      if (o instanceof BIGINT)
        return Long.compare(value, ((BIGINT)o).value);

      if (o instanceof FLOAT)
        return Float.compare(value, ((FLOAT)o).value);

      if (o instanceof DOUBLE)
        return Double.compare(value, ((DOUBLE)o).value);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(value).compareTo(((DECIMAL)o).value);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final TINYINT clone() {
      return new TINYINT(this);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ (isNull() ? 0 : Byte.hashCode(value));
    }
  }

  public abstract static class Entity<V> extends Evaluable implements type.Entity<V> {
    final boolean _mutable$;

    protected Entity(final boolean mutable) {
      this._mutable$ = mutable;
    }

    final boolean assertMutable() {
      if (!_mutable$)
        throw new IllegalArgumentException(Classes.getCompoundName(getClass()) + " is not mutable");

      return true;
    }

    private Evaluable wrapped;

    final Evaluable original() {
      Entity<?> wrapped = this;
      for (Evaluable next;; wrapped = (Entity<?>)next) {
        next = wrapped.wrapped();
        if (!(next instanceof Entity))
          return next != null ? next : wrapped;
      }
    }

    final Evaluable wrapped() {
      return wrapped;
    }

    final void clearWrap() {
      if (wrapped != null) {
        assertMutable();
        wrapped = null;
      }
    }

    Entity<V> wrap(final Evaluable wrapped) {
      assertMutable();
      this.wrapped = Assertions.assertNotNull(wrapped);
      return this;
    }
  }

  public abstract static class Temporal<V extends java.time.temporal.Temporal> extends Objective<V> implements Comparable<Column<? extends java.time.temporal.Temporal>>, type.Temporal<V> {
    Temporal(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Temporal(final Temporal<V> copy, final boolean mutable) {
      super(copy, mutable);
    }

    Temporal(final boolean mutable) {
      super(mutable);
    }

    @Override
    final java.time.temporal.Temporal evaluate(final Set<Evaluable> visited) {
      return (java.time.temporal.Temporal)super.evaluate(visited);
    }

    abstract boolean equals(final Temporal<?> obj);

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof Temporal && equals((Temporal<?>)obj);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Objects.hashCode(value);
    }

    @Override
    public String toString() {
      return isNull() ? "NULL" : value.toString();
    }
  }

  public abstract static class Textual<V extends CharSequence & Comparable<?>> extends Objective<V> implements type.Textual<V>, Comparable<Textual<?>> {
    private final Short length;

    Textual(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.length = (short)length;
    }

    Textual(final Textual<V> copy, final Short length, final boolean mutable) {
      super(copy, mutable);
      this.length = length;
    }

    Textual(final Short length, final boolean mutable) {
      super(mutable);
      this.length = length;
    }

    public final Short length() {
      return length;
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof Textual)
        return new CHAR(SafeMath.max(length(), ((Textual<?>)column).length()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    String evaluate(final Set<Evaluable> visited) {
      return (String)super.evaluate(visited);
    }

    @Override
    public final int compareTo(final Textual<?> o) {
      return o == null || o.isNull() ? isNull() ? 0 : 1 : isNull() ? -1 : value.toString().compareTo(o.value.toString());
    }

    @Override
    public final boolean equals(final Object obj) {
      if (!super.equals(obj) || !(obj instanceof Textual))
        return false;

      final Textual<?> that = (Textual<?>)obj;
      return name.equals(that.name) && (isNull() ? that.isNull() : !that.isNull() && value.toString().equals(that.value.toString()));
    }

    @Override
    public final int hashCode() {
      return name.hashCode() + (isNull() ? 0 : value.toString().hashCode());
    }
  }

  private static TIME $time;

  public static final TIME TIME() {
    return $time == null ? $time = new TIME(false) : $time;
  }

  public static class TIME extends Temporal<LocalTime> implements type.TIME {
    public static final class NULL extends TIME implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<LocalTime> type = LocalTime.class;
    private static final byte DEFAULT_PRECISION = 6;

    private final Byte precision;

    TIME(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final boolean keyForUpdate, final Integer precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = precision == null ? null : Byte.valueOf(precision.byteValue());
    }

    TIME(final TIME copy) {
      super(copy, true);
      this.precision = copy.precision;
    }

    public TIME(final int precision) {
      super(true);
      this.precision = (byte)precision;
    }

    public TIME(final Integer precision) {
      super(true);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public TIME() {
      this(true);
    }

    private TIME(final boolean mutable) {
      super(mutable);
      this.precision = null;
    }

    public TIME(final LocalTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    public final TIME set(final type.TIME value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final TIME set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final TIME copy) {
      assertMutable();
      this.value = copy.value;
      this.wasSet = copy.wasSet;
    }

    public final Byte precision() {
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
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compileColumn(this);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof TIME)
        return new DATETIME(SafeMath.max(precision(), ((TIME)column).precision()));

      throw new IllegalArgumentException("col." + getClass().getSimpleName() + " cannot be scaled against col." + column.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (!(o instanceof TIME))
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return value.compareTo(((TIME)o).value);
    }

    @Override
    final TIME wrap(final Evaluable wrapped) {
      return (TIME)super.wrap(wrapped);
    }

    @Override
    public final TIME clone() {
      return new TIME(this);
    }

    @Override
    public final boolean equals(final Temporal<?> obj) {
      return obj instanceof TIME && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.timeToString(value);
    }
  }

  private data() {
  }
}