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

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.lang.Classes;
import org.libj.lang.Numbers;
import org.libj.math.BigInt;
import org.libj.math.FastMath;
import org.libj.math.SafeMath;
import org.libj.util.function.Throwing;

public final class type {
  private static final IdentityHashMap<Class<?>,Class<?>> typeToGeneric = new IdentityHashMap<>(2);

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

  private static final ThreadLocal<LocalContext> localContext = new ThreadLocal<LocalContext>() {
    @Override
    protected LocalContext initialValue() {
      return new LocalContext();
    }
  };

  private static final class LocalContext {
    private ARRAY<?> $array;
    private IdentityHashMap<Integer,ARRAY<?>> $arrays;
    private BIGINT $bigint;
    private IdentityHashMap<Integer,BIGINT> $bigints;
    private BINARY $binary;
    private IdentityHashMap<Integer,BINARY> $binaries;
    private BLOB $blob;
    private IdentityHashMap<Integer,BLOB> $blobs;
    private BOOLEAN $boolean;
    private IdentityHashMap<Integer,BOOLEAN> $booleans;
    private CHAR $char;
    private IdentityHashMap<Integer,CHAR> $chars;
    private CLOB $clob;
    private IdentityHashMap<Integer,CLOB> $clobs;
    private DATE $date;
    private IdentityHashMap<Integer,DATE> $dates;
    private DATETIME $datetime;
    private IdentityHashMap<Integer,DATETIME> $datetimes;
    private DECIMAL $decimal;
    private IdentityHashMap<Integer,DECIMAL> $decimals;
    private DOUBLE $double;
    private IdentityHashMap<Integer,DOUBLE> $doubles;
    private ENUM<?> $enum;
    private IdentityHashMap<Integer,ENUM<?>> $enums;
    private FLOAT $float;
    private IdentityHashMap<Integer,FLOAT> $floats;
    private INT $int;
    private IdentityHashMap<Integer,INT> $ints;
    private SMALLINT $smallint;
    private IdentityHashMap<Integer,SMALLINT> $smallints;
    private TIME $time;
    private IdentityHashMap<Integer,TIME> $times;
    private TINYINT $tinyint;
    private IdentityHashMap<Integer,TINYINT> $tinyints;
  }

  private static Constructor<?> lookupDataTypeConstructor(Class<?> genericType) {
    Class<?> dataTypeClass;
    while ((dataTypeClass = typeToGeneric.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(dataTypeClass, genericType);
  }

  public abstract static class ApproxNumeric<T extends Number> extends Numeric<T> implements kind.ApproxNumeric<T> {
    ApproxNumeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    ApproxNumeric(final Numeric<T> copy) {
      super(copy);
    }

    ApproxNumeric(final boolean mutable) {
      super(mutable);
    }
  }

  public static final class ARRAY<T> extends Objective<T[]> implements kind.ARRAY<T[]> {
//    public static final ARRAY<?> NULL = new ARRAY(false);
    final DataType<T> dataType;
    private Class<T[]> type;

    ARRAY(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final boolean keyForUpdate, final Class<? extends DataType<T>> type) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.dataType = newInstance(Classes.getDeclaredConstructor(type));
    }

    @SuppressWarnings("unchecked")
    ARRAY(final ARRAY<T> copy) {
      this(copy.table, true, copy.name, copy.unique, copy.primary, copy.nullable, copy.value, copy.generateOnInsert, copy.generateOnUpdate, copy.keyForUpdate, (Class<? extends DataType<T>>)copy.dataType.getClass());
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
      assertMutable();
      if (isNull())
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setArray(parameterIndex, new SQLArray<>(this));
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (value != null)
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
      else
        resultSet.updateNull(columnIndex);
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

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof ARRAY && Arrays.equals(value, ((ARRAY<?>)obj).value);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(value);
    }
  }

  public static final BIGINT BIGINT() {
    final LocalContext context = localContext.get();
    return context.$bigint == null ? context.$bigint = new BIGINT(false) : context.$bigint;
  }

  public static final BIGINT BIGINT(final int i) {
    BIGINT value;
    final LocalContext context = localContext.get();
    if (context.$bigints == null) {
      (context.$bigints = new IdentityHashMap<>(2)).put(i, value = new BIGINT());
      return value;
    }

    if ((value = context.$bigints.get(i)) == null)
      context.$bigints.put(i, value = new BIGINT());

    return value;
  }

  public static class BIGINT extends ExactNumeric<Long> implements kind.BIGINT {
    public static final BIGINT NULL = new BIGINT(false);

    private static final Class<Long> type = Long.class;

    private final Long min;
    private final Long max;
    private boolean isNull = true;
    private long value;

    BIGINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
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

    public final BIGINT set(final BIGINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Long value) {
      return value != null ? set((long)value) : isNull && (isNull = true);
    }

    public final boolean set(final long value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final long value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final long value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public long getAsLong() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public long getAsLong(final long defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Long get() {
      return isNull ? null : value;
    }

    @Override
    public Long get(final Long defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setLong(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof ExactNumeric) {
        final ExactNumeric<?> exactNumeric = (ExactNumeric<?>)dataType;
        final Integer precision = precision() == null || exactNumeric.precision() == null ? null : SafeMath.max((int)precision(), exactNumeric.precision() + 1);
        return new BIGINT(precision);
      }

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
      return super.hashCode() ^ (isNull ? 0 : Long.hashCode(value));
    }
  }

  public static final BINARY BINARY() {
    final LocalContext context = localContext.get();
    return context.$binary == null ? context.$binary = new BINARY(false) : context.$binary;
  }

  public static final BINARY BINARY(final int i) {
    BINARY value;
    final LocalContext context = localContext.get();
    if (context.$binaries == null) {
      (context.$binaries = new IdentityHashMap<>(2)).put(i, value = new BINARY());
      return value;
    }

    if ((value = context.$binaries.get(i)) == null)
      context.$binaries.put(i, value = new BINARY());

    return value;
  }

  public static class BINARY extends Objective<byte[]> implements kind.BINARY {
    public static final BINARY NULL = new BINARY(false);

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

    private BINARY() {
      this(true);
    }

    private BINARY(final boolean mutable) {
      super(mutable);
      this.length = 0;
      this.varying = false;
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
      assertMutable();
      if (value == null)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBytes(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (value == null)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BINARY)
        return new BINARY(SafeMath.max(length(), ((BINARY)dataType).length()));

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

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof BINARY && Arrays.equals(value, ((BINARY)obj).value);
    }

    @Override
    public final int hashCode() {
      return super.hashCode() ^ Arrays.hashCode(value);
    }
  }

  public static final BLOB BLOB() {
    final LocalContext context = localContext.get();
    return context.$blob == null ? context.$blob = new BLOB(false) : context.$blob;
  }

  public static final BLOB BLOB(final int i) {
    BLOB value;
    final LocalContext context = localContext.get();
    if (context.$blobs == null) {
      (context.$blobs = new IdentityHashMap<>(2)).put(i, value = new BLOB());
      return value;
    }

    if ((value = context.$blobs.get(i)) == null)
      context.$blobs.put(i, value = new BLOB());

    return value;
  }

  public static class BLOB extends LargeObject<InputStream> implements kind.BLOB {
    public static final BLOB NULL = new BLOB(false);

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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BLOB)
        return new BLOB(SafeMath.max(length(), ((BLOB)dataType).length()));

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

  public static final BOOLEAN BOOLEAN() {
    final LocalContext context = localContext.get();
    return context.$boolean == null ? context.$boolean = new BOOLEAN(false) : context.$boolean;
  }

  public static final BOOLEAN BOOLEAN(final int i) {
    BOOLEAN value;
    final LocalContext context = localContext.get();
    if (context.$booleans == null) {
      (context.$booleans = new IdentityHashMap<>(2)).put(i, value = new BOOLEAN());
      return value;
    }

    if ((value = context.$booleans.get(i)) == null)
      context.$booleans.put(i, value = new BOOLEAN());

    return value;
  }

  public static class BOOLEAN extends Condition<Boolean> implements kind.BOOLEAN, Comparable<DataType<Boolean>> {
    public static final BOOLEAN NULL = new BOOLEAN((Class<BOOLEAN>)null);

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

    public final BOOLEAN set(final BOOLEAN value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Boolean value) {
      return value != null ? set((boolean)value) : isNull && (isNull = true);
    }

    public final boolean set(final boolean value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final boolean value) {
      assertMutable();
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    public boolean getAsBoolean() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public boolean getAsBoolean(final boolean defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Boolean get() {
      return isNull ? null : value;
    }

    @Override
    public Boolean get(final Boolean defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBoolean(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
    final LocalContext context = localContext.get();
    return context.$char == null ? context.$char = new CHAR(false) : context.$char;
  }

  public static final CHAR CHAR(final int i) {
    CHAR value;
    final LocalContext context = localContext.get();
    if (context.$chars == null) {
      (context.$chars = new IdentityHashMap<>(2)).put(i, value = new CHAR());
      return value;
    }

    if ((value = context.$chars.get(i)) == null)
      context.$chars.put(i, value = new CHAR());

    return value;
  }

  public static class CHAR extends Textual<String> implements kind.CHAR {
    public static final CHAR NULL = new CHAR(false);

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

    CHAR() {
      this(true);
    }

    private CHAR(final boolean mutable) {
      super(null, mutable);
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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
    final LocalContext context = localContext.get();
    return context.$clob == null ? context.$clob = new CLOB(false) : context.$clob;
  }

  public static final CLOB CLOB(final int i) {
    CLOB value;
    final LocalContext context = localContext.get();
    if (context.$clobs == null) {
      (context.$clobs = new IdentityHashMap<>(2)).put(i, value = new CLOB());
      return value;
    }

    if ((value = context.$clobs.get(i)) == null)
      context.$clobs.put(i, value = new CLOB());

    return value;
  }

  public static class CLOB extends LargeObject<Reader> implements kind.CLOB {
    public static final CLOB NULL = new CLOB(false);

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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof CLOB)
        return new CLOB(SafeMath.max(length(), ((CLOB)dataType).length()));

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

  public static final DATE DATE() {
    final LocalContext context = localContext.get();
    return context.$date == null ? context.$date = new DATE(false) : context.$date;
  }

  public static final DATE DATE(final int i) {
    DATE value;
    final LocalContext context = localContext.get();
    if (context.$dates == null) {
      (context.$dates = new IdentityHashMap<>(2)).put(i, value = new DATE());
      return value;
    }

    if ((value = context.$dates.get(i)) == null)
      context.$dates.put(i, value = new DATE());

    return value;
  }

  public static class DATE extends Temporal<LocalDate> implements kind.DATE {
    public static final DATE NULL = new DATE(false);

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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
    public final boolean equals(final Temporal<?> obj) {
      return (obj instanceof DATE || obj instanceof DATETIME) && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : Dialect.dateToString(value);
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

  public abstract static class DataType<T> extends type.Entity<T> implements kind.DataType<T>, Cloneable {
    boolean setValue(final T value) {
      assertMutable();

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

    final Table table;
    final String name;
    final boolean mutable;
    final boolean unique;
    final boolean primary;
    final boolean nullable;
    final GenerateOn<? super T> generateOnInsert;
    final GenerateOn<? super T> generateOnUpdate;
    final boolean keyForUpdate;

    DataType(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      this.table = owner;
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
      this.table = copy.table;
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

    DataType(final boolean mutable) {
      this(null, mutable, null, false, false, true, null, null, false);
    }

    @Override
    Table table() {
      return table;
    }

    final void assertMutable() {
      if (!mutable)
        throw new UnsupportedOperationException("static type alias is not mutable");
    }

    int columnIndex;
    DataType<T> indirection;
    boolean wasSet;

    public abstract boolean set(T value);

    void set(final DataType<T> indirection) {
      assertMutable();
      this.wasSet = false;
      this.indirection = indirection;
    }

    public abstract T get();
    public abstract T get(T defaultValue);
    abstract boolean isNull();

    public final boolean wasSet() {
      return wasSet;
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
      assertMutable();
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
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      Compiler.compile(this, compilation, isExpression);
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
      return this == obj || obj instanceof DataType && name.equals(((DataType<?>)obj).name);
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

  public static final DATETIME DATETIME() {
    final LocalContext context = localContext.get();
    return context.$datetime == null ? context.$datetime = new DATETIME(false) : context.$datetime;
  }

  public static final DATETIME DATETIME(final int i) {
    DATETIME value;
    final LocalContext context = localContext.get();
    if (context.$datetimes == null) {
      (context.$datetimes = new IdentityHashMap<>(2)).put(i, value = new DATETIME());
      return value;
    }

    if ((value = context.$datetimes.get(i)) == null)
      context.$datetimes.put(i, value = new DATETIME());

    return value;
  }

  public static class DATETIME extends Temporal<LocalDateTime> implements kind.DATETIME {
    public static final DATETIME NULL = new DATETIME(false);

    private static final Class<LocalDateTime> type = LocalDateTime.class;
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final byte DEFAULT_PRECISION = 6;

    private final Byte precision;

    DATETIME(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (byte)precision;
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

    public final DATETIME set(final DATETIME value) {
      super.set(value);
      return this;
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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATETIME)
        return new DATETIME(SafeMath.max(precision(), ((DATETIME)dataType).precision()));

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
    public final boolean equals(final Temporal<?> obj) {
      return (obj instanceof DATE || obj instanceof DATETIME) && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : Dialect.dateTimeToString(value);
    }
  }

  public static final DECIMAL DECIMAL() {
    final LocalContext context = localContext.get();
    return context.$decimal == null ? context.$decimal = new DECIMAL(false) : context.$decimal;
  }

  public static final DECIMAL DECIMAL(final int i) {
    DECIMAL value;
    final LocalContext context = localContext.get();
    if (context.$decimals == null) {
      (context.$decimals = new IdentityHashMap<>(2)).put(i, value = new DECIMAL());
      return value;
    }

    if ((value = context.$decimals.get(i)) == null)
      context.$decimals.put(i, value = new DECIMAL());

    return value;
  }

  public static class DECIMAL extends ExactNumeric<BigDecimal> implements kind.DECIMAL {
    public static final DECIMAL NULL = new DECIMAL(false);

    private static final Class<BigDecimal> type = BigDecimal.class;
    private static final byte maxScale = 38;

    private final Integer scale;
    private final BigDecimal min;
    private final BigDecimal max;
    private BigDecimal value;

    DECIMAL(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
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

    public DECIMAL set(final DECIMAL value) {
      super.set(value);
      return this;
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
      assertMutable();
      if (value == null)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBigDecimal(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (value != null)
        resultSet.updateBigDecimal(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), SafeMath.max(scale(), decimal.scale()));
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(precision() == null || decimal.precision() == null ? null : SafeMath.max((int)precision(), decimal.precision() + 1), SafeMath.max(scale(), decimal.scale()));
      }

      if (dataType instanceof ApproxNumeric)
        return new DECIMAL(precision() == null ? null : precision() + 1, scale());

      if (dataType instanceof ExactNumeric) {
        final ExactNumeric<?> exactNumeric = (ExactNumeric<?>)dataType;
        final Integer precision = precision() == null || exactNumeric.precision() == null ? null : SafeMath.max((int)precision(), exactNumeric.precision() + 1);
        return new DECIMAL(precision, scale());
      }

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
      return value == null ? "NULL" : value.toString();
    }
  }

  public static final DOUBLE DOUBLE() {
    final LocalContext context = localContext.get();
    return context.$double == null ? context.$double = new DOUBLE(false) : context.$double;
  }

  public static final DOUBLE DOUBLE(final int i) {
    DOUBLE value;
    final LocalContext context = localContext.get();
    if (context.$doubles == null) {
      (context.$doubles = new IdentityHashMap<>(2)).put(i, value = new DOUBLE());
      return value;
    }

    if ((value = context.$doubles.get(i)) == null)
      context.$doubles.put(i, value = new DOUBLE());

    return value;
  }

  public static class DOUBLE extends ApproxNumeric<Double> implements kind.DOUBLE {
    public static final DOUBLE NULL = new DOUBLE(false);

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

    public final DOUBLE set(final DOUBLE value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Double value) {
      return value != null ? set((double)value) : isNull && (isNull = true);
    }

    public final boolean set(final double value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final double value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final double value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public double getAsDouble() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public double getAsDouble(final double defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Double get() {
      return isNull ? null : value;
    }

    @Override
    public Double get(final Double defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setDouble(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision() == null ? null : decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision() == null ? null : decimal.precision() + 1, decimal.scale());
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
      return super.hashCode() ^ (isNull ? 0 : Double.hashCode(value));
    }
  }

  public static final ENUM<?> ENUM() {
    final LocalContext context = localContext.get();
    return context.$enum == null ? context.$enum = new ENUM<>(false) : context.$enum;
  }

  public static final ENUM<?> ENUM(final int i) {
    ENUM<?> value;
    final LocalContext context = localContext.get();
    if (context.$enums == null) {
      (context.$enums = new IdentityHashMap<>(2)).put(i, value = new ENUM<>());
      return value;
    }

    if ((value = context.$enums.get(i)) == null)
      context.$enums.put(i, value = new ENUM<>());

    return value;
  }

  public static class ENUM<T extends Enum<?> & EntityEnum> extends Textual<T> implements kind.ENUM<T> {
    public static final ENUM<?> NULL = new ENUM<>(false);

    private static final IdentityHashMap<Class<?>,Short> typeToLength = new IdentityHashMap<>(2);

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

    ENUM(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Class<T> type) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, calcEnumLength(type));
      this.enumType = type;
    }

    ENUM(final ENUM<T> copy) {
      super(copy, copy.length(), true);
      this.enumType = copy.enumType;
    }

    public ENUM(final Class<T> enumType) {
      super(calcEnumLength(enumType), true);
      this.enumType = enumType;
    }

    private ENUM() {
      this(true);
    }

    private ENUM(final boolean mutable) {
      super(null, mutable);
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
      assertMutable();
      if (value == null)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setObject(parameterIndex, value.toString());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (value != null)
        resultSet.updateObject(columnIndex, value.toString());
      else
        resultSet.updateNull(columnIndex);
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

  public abstract static class Table extends type.Entity<Table> implements kind.Table<Table>, Cloneable {
    final type.DataType<?>[] _column$;
    final type.DataType<?>[] _primary$;
    final type.DataType<?>[] _auto$;
    private final boolean _mutable$;
    private final boolean _wasSelected$;

    Table(final boolean mutable, final boolean _wasSelected$, final type.DataType<?>[] _column$, final type.DataType<?>[] _primary$, final type.DataType<?>[] _auto$) {
      this._mutable$ = mutable;
      this._wasSelected$ = _wasSelected$;
      this._column$ = _column$;
      this._primary$ = _primary$;
      this._auto$ = _auto$;
    }

    Table(final Table copy) {
      this._mutable$ = copy._mutable$;
      this._wasSelected$ = false;
      this._column$ = copy._column$.clone();
      this._primary$ = copy._primary$.clone();
      this._auto$ = copy._auto$.clone();
    }

    Table() {
      this._mutable$ = true;
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
    final Table evaluate(final Set<Evaluable> visited) {
      return this;
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation, isExpression);
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

  public abstract static class ExactNumeric<T extends Number> extends Numeric<T> implements kind.ExactNumeric<T> {
    final Integer precision;

    ExactNumeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Numeric<T> copy, final Integer precision) {
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
    abstract T minValue();
    abstract T maxValue();
    abstract int maxPrecision();

    private void checkPrecision(final Integer precision) {
      if (precision != null && maxPrecision() != -1 && precision > maxPrecision())
        throw new IllegalArgumentException(getSimpleName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }
  }

  public static final FLOAT FLOAT() {
    final LocalContext context = localContext.get();
    return context.$float == null ? context.$float = new FLOAT(false) : context.$float;
  }

  public static final FLOAT FLOAT(final int i) {
    FLOAT value;
    final LocalContext context = localContext.get();
    if (context.$floats == null) {
      (context.$floats = new IdentityHashMap<>(2)).put(i, value = new FLOAT());
      return value;
    }

    if ((value = context.$floats.get(i)) == null)
      context.$floats.put(i, value = new FLOAT());

    return value;
  }

  public static class FLOAT extends ApproxNumeric<Float> implements kind.FLOAT {
    public static final FLOAT NULL = new FLOAT(false);

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

    public final FLOAT set(final FLOAT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Float value) {
      return value != null ? set((float)value) : isNull && (isNull = true);
    }

    public final boolean set(final float value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final float value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final float value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public float getAsFloat() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public float getAsFloat(final float defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Float get() {
      return isNull ? null : value;
    }

    @Override
    public Float get(final Float defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setFloat(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT || dataType instanceof TINYINT)
        return new FLOAT();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
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
      return super.hashCode() ^ (isNull ? 0 : Float.hashCode(value));
    }
  }

  public abstract static class LargeObject<T extends Closeable> extends Objective<T> implements kind.LargeObject<T> {
    private final Long length;

    LargeObject(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
    }

    LargeObject(final LargeObject<T> copy, final boolean mutable) {
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

  public static final INT INT() {
    final LocalContext context = localContext.get();
    return context.$int == null ? context.$int = new INT(false) : context.$int;
  }

  public static final INT INT(final int i) {
    INT value;
    final LocalContext context = localContext.get();
    if (context.$ints == null) {
      (context.$ints = new IdentityHashMap<>(2)).put(i, value = new INT());
      return value;
    }

    if ((value = context.$ints.get(i)) == null)
      context.$ints.put(i, value = new INT());

    return value;
  }

  public static class INT extends ExactNumeric<Integer> implements kind.INT {
    public static final INT NULL = new INT(false);

    private static final Class<Integer> type = Integer.class;

    private final Integer min;
    private final Integer max;
    private boolean isNull = true;
    private int value;

    INT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
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

    public final INT set(final INT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Integer value) {
      return value != null ? set((int)value) : isNull && (isNull = true);
    }

    public final boolean set(final int value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final int value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final int value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public int getAsInt() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public int getAsInt(final int defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Integer get() {
      return isNull ? null : value;
    }

    @Override
    public Integer get(final Integer defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setInt(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

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
      return super.hashCode() ^ (isNull ? 0 : Integer.hashCode(value));
    }
  }

  public static abstract class Objective<T> extends DataType<T> implements kind.Objective<T> {
    T value;

    Objective(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.value = _default;
    }

    Objective(final Objective<T> copy, final boolean mutable) {
      super(mutable);
      this.value = copy.value;
    }

    Objective(final boolean mutable) {
      super(mutable);
    }

    @Override
    public final boolean set(final T value) {
      assertMutable();
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
    public final T get(final T defaultValue) {
      return isNull() ? defaultValue : value;
    }

    @Override
    public boolean isNull() {
      return value == null;
    }
  }

  public abstract static class Primitive<T> extends DataType<T> implements kind.Primitive<T> {
    Primitive(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Primitive(final Primitive<T> copy) {
      super(copy);
    }

    Primitive(final boolean mutable) {
      super(mutable);
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
    final LocalContext context = localContext.get();
    return context.$smallint == null ? context.$smallint = new SMALLINT(false) : context.$smallint;
  }

  public static final SMALLINT SMALLINT(final int i) {
    SMALLINT value;
    final LocalContext context = localContext.get();
    if (context.$smallints == null) {
      (context.$smallints = new IdentityHashMap<>(2)).put(i, value = new SMALLINT());
      return value;
    }

    if ((value = context.$smallints.get(i)) == null)
      context.$smallints.put(i, value = new SMALLINT());

    return value;
  }

  public static class SMALLINT extends ExactNumeric<Short> implements kind.SMALLINT {
    public static final SMALLINT NULL = new SMALLINT(false);

    private static final Class<Short> type = Short.class;

    private final Short min;
    private final Short max;
    private boolean isNull = true;
    private short value;

    SMALLINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
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

    public final SMALLINT set(final SMALLINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Short value) {
      return value != null ? set((short)value) : isNull && (isNull = true);
    }

    public final boolean set(final short value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final short value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final short value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public short getAsShort() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public short getAsShort(final short defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Short get() {
      return isNull ? null : value;
    }

    @Override
    public Short get(final Short defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setShort(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof INT)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new SMALLINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

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
      return super.hashCode() ^ (isNull ? 0 : Short.hashCode(value));
    }
  }

  public abstract static class Numeric<T extends Number> extends Primitive<T> implements Comparable<DataType<? extends Number>>, kind.Numeric<T> {
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

    Numeric(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Numeric(final Numeric<T> copy) {
      super(copy);
    }

    Numeric(final boolean mutable) {
      super(mutable);
    }

    public abstract T min();
    public abstract T max();

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    public final boolean equals(final Object obj) {
      return super.equals(obj) && obj instanceof Numeric && compareTo((Numeric<?>)obj) == 0;
    }
  }

  public static final TINYINT TINYINT() {
    final LocalContext context = localContext.get();
    return context.$tinyint == null ? context.$tinyint = new TINYINT(false) : context.$tinyint;
  }

  public static final TINYINT TINYINT(final int i) {
    TINYINT value;
    final LocalContext context = localContext.get();
    if (context.$tinyints == null) {
      (context.$tinyints = new IdentityHashMap<>(2)).put(i, value = new TINYINT());
      return value;
    }

    if ((value = context.$tinyints.get(i)) == null)
      context.$tinyints.put(i, value = new TINYINT());

    return value;
  }

  public static class TINYINT extends ExactNumeric<Byte> implements kind.TINYINT {
    public static final TINYINT NULL = new TINYINT(false);

    private static final Class<Byte> type = Byte.class;

    private final Byte min;
    private final Byte max;
    private boolean isNull = true;
    private byte value;

    TINYINT(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final boolean keyForUpdate, final int precision, final Byte min, final Byte max) {
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

    public final TINYINT set(final TINYINT value) {
      super.set(value);
      return this;
    }

    @Override
    public final boolean set(final Byte value) {
      return value != null ? set((byte)value) : isNull && (isNull = true);
    }

    public final boolean set(final byte value) {
      final boolean changed = setValue(value);
      wasSet = true;
      return changed;
    }

    final boolean setValue(final byte value) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNull || this.value != value;
      this.value = value;
      this.isNull = false;
      return changed;
    }

    private final void checkValue(final byte value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public byte getAsByte() {
      if (isNull)
        throw new NullPointerException("NULL");

      return value;
    }

    public byte getAsByte(final byte defaultValue) {
      return isNull ? defaultValue : value;
    }

    @Override
    public Byte get() {
      return isNull ? null : value;
    }

    @Override
    public Byte get(final Byte defaultValue) {
      return isNull ? defaultValue : value;
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
      assertMutable();
      if (isNull)
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setByte(parameterIndex, value);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      if (isNull)
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT)
        return new FLOAT();

      if (dataType instanceof DOUBLE)
        return new DOUBLE();

      if (dataType instanceof TINYINT)
        return new TINYINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof SMALLINT)
        return new SMALLINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof INT)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
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
      return super.hashCode() ^ (isNull ? 0 : Byte.hashCode(value));
    }
  }

  public abstract static class Entity<T> extends Evaluable implements kind.Entity<T> {
    private Evaluable wrapper;

    final Evaluable original() {
      Evaluable wrapper = wrapper();
      if (wrapper == null)
        return this;

      while (true) {
        Evaluable next = ((type.Entity<?>)wrapper).wrapper();
        if (next instanceof type.Entity) {
          wrapper = next;
          continue;
        }

        return next != null ? next : wrapper;
      }
    }

    final Evaluable wrapper() {
      return wrapper;
    }

    type.Entity<T> wrapper(final Evaluable wrapper) {
      this.wrapper = wrapper;
      return this;
    }
  }

  public abstract static class Temporal<T extends java.time.temporal.Temporal> extends Objective<T> implements Comparable<DataType<? extends java.time.temporal.Temporal>>, kind.Temporal<T> {
    Temporal(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    Temporal(final Temporal<T> copy, final boolean mutable) {
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
      return value == null ? "NULL" : value.toString();
    }
  }

  public abstract static class Textual<T extends CharSequence & Comparable<?>> extends Objective<T> implements kind.Textual<T>, Comparable<Textual<?>> {
    private final Short length;

    Textual(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.length = (short)length;
    }

    Textual(final Textual<T> copy, final Short length, final boolean mutable) {
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
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof Textual)
        return new CHAR(SafeMath.max(length(), ((Textual<?>)dataType).length()));

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
      if (!super.equals(obj) || !(obj instanceof Textual))
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
    final LocalContext context = localContext.get();
    return context.$time == null ? context.$time = new TIME(false) : context.$time;
  }

  public static final TIME TIME(final int i) {
    TIME value;
    final LocalContext context = localContext.get();
    if (context.$times == null) {
      (context.$times = new IdentityHashMap<>(2)).put(i, value = new TIME());
      return value;
    }

    if ((value = context.$times.get(i)) == null)
      context.$times.put(i, value = new TIME());

    return value;
  }

  public static class TIME extends Temporal<LocalTime> implements kind.TIME {
    public static final TIME NULL = new TIME(false);

    private static final Class<LocalTime> type = LocalTime.class;
    private static final byte DEFAULT_PRECISION = 6;

    private final Byte precision;

    TIME(final Table owner, final boolean mutable, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, mutable, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (byte)precision;
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

    public final TIME set(final TIME value) {
      super.set(value);
      return this;
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
      assertMutable();
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
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
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof TIME)
        return new DATETIME(SafeMath.max(precision(), ((TIME)dataType).precision()));

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
    public final boolean equals(final Temporal<?> obj) {
      return obj instanceof TIME && compareTo(obj) == 0;
    }

    @Override
    public final String toString() {
      return value == null ? "NULL" : Dialect.timeToString(value);
    }
  }

  private type() {
  }
}