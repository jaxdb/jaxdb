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

import static org.libj.lang.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.Serializable;
import java.io.UncheckedIOException;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import java.util.function.Function;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.SerializableInputStream;
import org.libj.io.SerializableReader;
import org.libj.io.Streams;
import org.libj.io.UnsynchronizedStringReader;
import org.libj.lang.Classes;
import org.libj.lang.Hexadecimal;
import org.libj.lang.Numbers;
import org.libj.math.BigInt;
import org.libj.math.FastMath;
import org.libj.math.SafeMath;
import org.libj.util.DiscreteTopologies;
import org.libj.util.DiscreteTopology;
import org.libj.util.function.Throwing;

public final class data {
  private static final IdentityHashMap<Class<?>,Class<?>> typeToGeneric = new IdentityHashMap<>(17);

  public enum Except {
    PRIMARY_KEY,
    PRIMARY_KEY_FOR_UPDATE
  }

  static {
    typeToGeneric.put(null, ENUM.class);
    for (final Class<?> member : data.class.getClasses()) { // [A]
      if (!Modifier.isAbstract(member.getModifiers())) {
        final Type[] types = Classes.getSuperclassGenericTypes(member);
        if (types != null) {
          final Type type = types[0];
          if (type instanceof Class<?>)
            typeToGeneric.put((Class<?>)type, member);
        }
      }
    }
  }

  private static Constructor<?> lookupColumnConstructor(Class<?> genericType) {
    Class<?> cls;
    while ((cls = typeToGeneric.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(cls, genericType);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static <V extends Serializable,C extends Column<V>>C wrap(final V value) {
    if (EntityEnum.class.isAssignableFrom(value.getClass()))
      return (C)new ENUM((EntityEnum)value);

    return (C)newInstance(lookupColumnConstructor(value.getClass()), value);
  }

  @SuppressWarnings("unchecked")
  private static <E extends Serializable>ARRAY<E> wrap(final E[] value) {
    final ARRAY<E> array;
    if (value.getClass().getComponentType().isEnum())
      array = new ARRAY<>((Class<? extends Column<E>>)value.getClass().getComponentType());
    else
      array = new ARRAY<>((Class<? extends Column<E>>)typeToGeneric.get(value.getClass().getComponentType()));

    array.set(value);
    return array;
  }

  public abstract static class ApproxNumeric<V extends Number> extends Numeric<V> implements type.ApproxNumeric<V> {
    ApproxNumeric(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
    }

    ApproxNumeric(final Table owner, final boolean mutable, final Numeric<V> copy) {
      super(owner, mutable, copy);
    }

    ApproxNumeric(final boolean mutable) {
      super(null, mutable);
    }
  }

  interface NULL {
  }

  static final Marker PRIMARY_KEY = new Marker();
  static final Marker KEY_FOR_UPDATE = new Marker();

  @SuppressWarnings("rawtypes")
  static final class Marker extends Column {
    Marker() {
      super(null, false);
    }

    @Override
    protected boolean set(final Serializable value) {
      return false;
    }

    @Override
    protected boolean setIfNotNull(final Serializable value) {
      return value != null && set(value);
    }

    @Override
    boolean set(final Serializable value, final SetBy setBy) {
      return false;
    }

    @Override
    boolean setValue(final Serializable value) {
      return false;
    }

    @Override
    public void revert() {
    }

    @Override
    void _commitEntity$() {
    }

    @Override
    public Serializable get() {
      return null;
    }

    @Override
    public Serializable get(final Serializable defaultValue) {
      return null;
    }

    @Override
    public boolean isNull() {
      return false;
    }

    @Override
    Serializable getOld() {
      return null;
    }

    @Override
    boolean isNullOld() {
      return false;
    }

    @Override
    Class type() {
      return null;
    }

    @Override
    int sqlType() {
      return 0;
    }

    @Override
    DiscreteTopology getDiscreteTopology() {
      return null;
    }

    @Override
    Serializable parseString(final DbVendor vendor, final String s) {
      return null;
    }

    @Override
    void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
    }

    @Override
    void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
    }

    @Override
    StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return null;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return b;
    }

    @Override
    Column scaleTo(final Column column) {
      return null;
    }

    @Override
    public Column clone() {
      return null;
    }

    @Override
    boolean equals(final Column that) {
      return false;
    }

    @Override
    int valueHashCode() {
      return 0;
    }

    @Override
    StringBuilder toJson(final StringBuilder b) {
      return null;
    }

    @Override
    public String toString() {
      return null;
    }
  }

  private static ARRAY<?> $array;

  public static ARRAY<?> ARRAY() {
    return $array == null ? $array = new ARRAY<>(false) : $array;
  }

  public static class ARRAY<T extends Serializable> extends Objective<T[]> implements type.ARRAY<T> {
    @SuppressWarnings("rawtypes")
    public static final class NULL extends ARRAY implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    final Column<T> column;
    private Class<T[]> type;

    ARRAY(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final Class<? extends Column<T>> type) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      this.column = newInstance(Classes.getDeclaredConstructor(type));
    }

    @SuppressWarnings("unchecked")
    ARRAY(final Table owner, final boolean mutable, final ARRAY<T> copy) {
      this(owner, mutable, copy.name, copy.primary, copy.keyForUpdate, copy.commitUpdate, copy.nullable, copy.valueCur, copy.generateOnInsert, copy.generateOnUpdate, (Class<? extends Column<T>>)copy.column.getClass());
      this.type = copy.type;
    }

    public ARRAY(final Class<? extends Column<T>> type) {
      this(null, true, null, false, false, null, true, null, null, null, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, true, null, false, false, null, true, value, null, null, (Class<? extends Column<T>>)value.getClass().getComponentType());
    }

    private ARRAY(final boolean mutable) {
      super(null, mutable);
      this.column = null;
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
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);

//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    @Override
    DiscreteTopology<T[]> getDiscreteTopology() {
      throw new UnsupportedOperationException();
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
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
    final T[] parseString(final DbVendor vendor, final String s) {
      return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final java.sql.Array array = resultSet.getArray(columnIndex);
      set((T[])array.getArray()); // FIXME: This is incorrect.
      this.valueOld = this.valueCur;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setArray(parameterIndex, new SQLArray<>(this)); // FIXME: isForUpdateWhere
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileArray(b, this, column, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    final ARRAY<T> wrap(final Evaluable wrapped) {
      return (ARRAY<T>)super.wrap(wrapped);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<>((Class<? extends Column<T>>)column.getClass());
    }

    @Override
    final boolean equals(final Column<T[]> obj) {
      throw new UnsupportedOperationException("FIXME");
    }

    @Override
    boolean equal(final T[] a, final T[] b) {
      return Arrays.equals(a, b);
    }

    @Override
    final int valueHashCode() {
      return Arrays.hashCode(valueCur);
    }

    @Override
    public final String toString() {
      throw new UnsupportedOperationException("FIXME");
    }
  }

  private static BIGINT $bigint;

  public static BIGINT BIGINT() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private long valueOld;
    private long valueCur;

    BIGINT(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final Integer precision, final Long min, final Long max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    BIGINT(final Table owner, final boolean mutable, final BIGINT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public BIGINT(final int precision) {
      this(null, true, precision);
    }

    public BIGINT(final Integer precision) {
      this(null, true, precision);
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

    private BIGINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    private BIGINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    final void copy(final BIGINT copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final BIGINT set(final type.BIGINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BIGINT set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    @Override
    public final boolean set(final Long value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Long value, final SetBy setBy) {
      return value == null ? setNull() : set((long)value, setBy);
    }

    public final boolean set(final long value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final long value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Long value) {
      return value == null ? setNull() : setValue((long)value);
    }

    final boolean setValue(final long value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Long> getDiscreteTopology() {
      return DiscreteTopologies.LONG;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final long value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final long getAsLong() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final long getAsLong(final long defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Long get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Long get(final Long defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Long getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt64(b, Numbers.cast(precision(), Byte.class), min);
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
    final Long parseString(final DbVendor vendor, final String s) {
      return Long.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final long value = resultSet.getLong(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateLong(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setLong(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
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

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final BIGINT wrap(final Evaluable wrapped) {
      return (BIGINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Long.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Long.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Long.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Long.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Float.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Double.compare(valueCur, ((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(valueCur).compareTo(((DECIMAL)o).valueCur);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final BIGINT clone() {
      return new BIGINT(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Long.hashCode(valueCur);
    }
  }

  private static BINARY $binary;

  public static BINARY BINARY() {
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

    BINARY(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final byte[] _default, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    BINARY(final Table owner, final boolean mutable, final BINARY copy) {
      super(owner, mutable, copy);
      this.length = copy.length;
      this.varying = copy.varying;
    }

    public BINARY(final long length, final boolean varying) {
      super(null, true);
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
      super(null, mutable);
      this.length = 0;
      this.varying = false;
    }

    final void copy(final BINARY copy) {
      // assertMutable();
      this.changed = !Arrays.equals(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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
    DiscreteTopology<byte[]> getDiscreteTopology() {
      return DiscreteTopologies.BYTES;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileBinary(b, varying, length);
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
    final byte[] parseString(final DbVendor vendor, final String s) {
      return vendor.getDialect().stringLiteralToBinary(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final int columnType = resultSet.getMetaData().getColumnType(columnIndex);
      // FIXME: IS it right to support BIT here? Or should it be in BOOLEAN?
      this.valueOld = this.valueCur = columnType == Types.BIT ? new byte[] {resultSet.getBoolean(columnIndex) ? (byte)0x01 : (byte)0x00} : resultSet.getBytes(columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBytes(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBytes(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BINARY)
        return new BINARY(SafeMath.max(length(), ((BINARY)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final BINARY wrap(final Evaluable wrapped) {
      return (BINARY)super.wrap(wrapped);
    }

    @Override
    public final BINARY clone() {
      return new BINARY(getTable(), true, this);
    }

    @Override
    final boolean equals(final Column<byte[]> obj) {
      return equal(valueCur, ((BINARY)obj).valueCur);
    }

    @Override
    final boolean equal(final byte[] a, final byte[] b) {
      return Arrays.equals(a, b);
    }

    @Override
    final int valueHashCode() {
      return Arrays.hashCode(valueCur);
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Hexadecimal.encode(valueCur);
    }
  }

  private static BLOB $blob;

  public static BLOB BLOB() {
    return $blob == null ? $blob = new BLOB(false) : $blob;
  }

  public static class BLOB extends LargeObject<SerializableInputStream> implements type.BLOB {
    public static final class NULL extends BLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<SerializableInputStream> type = SerializableInputStream.class;

    BLOB(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final SerializableInputStream _default, final GenerateOn<? super SerializableInputStream> generateOnInsert, final GenerateOn<? super SerializableInputStream> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate, length);
    }

    BLOB(final Table owner, final boolean mutable, final BLOB copy) {
      super(owner, mutable, copy);
    }

    public BLOB(final long length) {
      super(null, true, length);
    }

    public BLOB(final SerializableInputStream value) {
      super(null, true, (Long)null);
      set(value);
    }

    public BLOB() {
      this(true);
    }

    private BLOB(final boolean mutable) {
      super(null, mutable, (Long)null);
    }

    final void copy(final BLOB copy) {
      // assertMutable();
      this.changed = this.valueOld != copy.valueCur;
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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

    @Override
    DiscreteTopology<SerializableInputStream> getDiscreteTopology() {
      throw new UnsupportedOperationException();
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileBlob(b, length());
    }

    @Override
    final Class<SerializableInputStream> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.BLOB;
    }

    @Override
    final SerializableInputStream parseString(final DbVendor vendor, final String s) {
      return new SerializableInputStream(new ByteArrayInputStream(s.getBytes()));
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BLOB)
        return new BLOB(SafeMath.max(length(), ((BLOB)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final BLOB wrap(final Evaluable wrapped) {
      return (BLOB)super.wrap(wrapped);
    }

    @Override
    public final BLOB clone() {
      return new BLOB(getTable(), true, this);
    }

    // FIXME: Warning! Calling equals will result in the underlying stream to be fully read
    @Override
    final boolean equals(final Column<SerializableInputStream> obj) {
      final BLOB that = (BLOB)obj;
      initBlobInputStream();
      that.initBlobInputStream();
      return equal(valueCur, that.valueCur);
    }

    // FIXME: Warning! Calling hashCode will result in the underlying stream to be fully read
    @Override
    final int valueHashCode() {
      initBlobInputStream();
      return valueCur.hashCode();
    }

    // FIXME: Is this a bad pattern? Read the full stream just to get toString()?
    private final class BlobInputStream extends SerializableInputStream {
      private final String string;
      private final byte[] buf;

      private BlobInputStream(final byte[] buf) {
        super(new ByteArrayInputStream(buf));
        this.buf = buf;
        this.string = Hexadecimal.encode(buf);
      }

      @Override
      public boolean equals(final Object obj) {
        return obj == this || obj instanceof BlobInputStream && Arrays.equals(buf, ((BlobInputStream)obj).buf);
      }

      @Override
      public int hashCode() {
        return Arrays.hashCode(buf);
      }

      @Override
      public String toString() {
        return string;
      }
    }

    private void initBlobInputStream() {
      if (!(valueCur instanceof BlobInputStream)) {
        try {
          setValue(new BlobInputStream(Streams.readBytes(valueCur)));
        }
        catch (final IOException e) {
          throw new UncheckedIOException(e);
        }
      }
    }

    @Override
    public final String toString() {
      if (isNull())
        return "NULL";

      initBlobInputStream();
      return valueCur.toString();
    }
  }

  private static BOOLEAN $boolean;

  public static BOOLEAN BOOLEAN() {
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

    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private boolean valueOld;
    private boolean valueCur;

    BOOLEAN(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Boolean _default, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }
    }

    BOOLEAN(final Table owner, final boolean mutable, final BOOLEAN copy) {
      super(owner, mutable, copy);

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public BOOLEAN(final Boolean value) {
      super(null, true);
      if (value != null)
        set(value);
    }

    public BOOLEAN(final boolean value) {
      super(null, true);
      set(value);
    }

    public BOOLEAN() {
      super(null, true);
    }

    BOOLEAN(final Table owner) {
      super(owner, true);
    }

    @SuppressWarnings("unused")
    private BOOLEAN(final Class<BOOLEAN> cls) {
      super(null, false);
    }

    final void copy(final BOOLEAN copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final BOOLEAN set(final type.BOOLEAN value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final BOOLEAN set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    @Override
    public final boolean set(final Boolean value) {
      return set(value, SetBy.USER);
    }

    @Override
    public boolean setIfNotNull(final Boolean value) {
      return value != null && set(value);
    }

    @Override
    public final boolean set(final Boolean value, final SetBy setBy) {
      return value == null ? setNull() : set((boolean)value, setBy);
    }

    public final boolean set(final boolean value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final boolean value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Boolean value) {
      return value == null ? setNull() : setValue((boolean)value);
    }

    final boolean setValue(final boolean value) {
      assertMutable();
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Boolean> getDiscreteTopology() {
      return DiscreteTopologies.BOOLEAN;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    public final boolean getAsBoolean() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final boolean getAsBoolean(final boolean defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Boolean get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Boolean get(final Boolean defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Boolean getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareBoolean(b);
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
    final Boolean parseString(final DbVendor vendor, final String s) {
      return Boolean.parseBoolean(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final boolean value = resultSet.getBoolean(columnIndex);
      this.valueOld = this.valueCur = !(this.isNullOld = this.isNullCur = resultSet.wasNull()) && value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBoolean(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBoolean(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final BOOLEAN wrap(final Evaluable wrapped) {
      return (BOOLEAN)super.wrap(wrapped);
    }

    @Override
    void collectColumns(final ArrayList<Column<?>> list) {
      list.add(getColumn());
    }

    @Override
    public final int compareTo(final Column<Boolean> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      return Boolean.compare(valueCur, ((BOOLEAN)o).valueCur);
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(getTable(), true, this);
    }

    @Override
    final boolean equals(final Column<Boolean> that) {
      return getAsBoolean() == ((BOOLEAN)that).getAsBoolean();
    }

    @Override
    final int valueHashCode() {
      return Boolean.hashCode(valueCur);
    }
  }

  private static CHAR $char;

  public static CHAR CHAR() {
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

    CHAR(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final String _default, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    CHAR(final Table owner, final boolean mutable, final CHAR copy) {
      super(owner, mutable, copy, copy.length());
      this.varying = copy.varying;
    }

    public CHAR(final long length, final boolean varying) {
      super(null, true, (short)length);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final Long length, final boolean varying) {
      super(null, true, Numbers.cast(length, Short.class));
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
      super(null, mutable, null);
      this.varying = true;
    }

    final void copy(final CHAR copy) {
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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

    final void checkLength(final long length) {
      if (length < 0 || (!varying() && length == 0) || length > 65535)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    DiscreteTopology<String> getDiscreteTopology() {
      return DiscreteTopologies.STRING;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      if (length() == null)
        throw new UnsupportedOperationException("Cannot declare a CHAR with null length");

      return vendor.getDialect().compileChar(b, varying, length() == null ? 1L : length());
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
    final String parseString(final DbVendor vendor, final String s) {
      return s;
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final CHAR wrap(final Evaluable wrapped) {
      return (CHAR)super.wrap(wrapped);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(getTable(), true, this);
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : valueCur;
    }
  }

  private static CLOB $clob;

  public static CLOB CLOB() {
    return $clob == null ? $clob = new CLOB(false) : $clob;
  }

  public static class CLOB extends LargeObject<SerializableReader> implements type.CLOB {
    public static final class NULL extends CLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    private static final Class<SerializableReader> type = SerializableReader.class;

    CLOB(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final SerializableReader _default, final GenerateOn<? super SerializableReader> generateOnInsert, final GenerateOn<? super SerializableReader> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate, length);
    }

    CLOB(final Table owner, final boolean mutable, final CLOB copy) {
      super(owner, mutable, copy);
    }

    public CLOB(final long length) {
      super(null, true, length);
    }

    public CLOB(final SerializableReader value) {
      super(null, true, (Long)null);
      set(value);
    }

    public CLOB() {
      this(true);
    }

    private CLOB(final boolean mutable) {
      super(null, mutable, (Long)null);
    }

    final void copy(final CLOB copy) {
      // assertMutable();
      this.changed = this.valueOld != copy.valueCur;
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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

    @Override
    DiscreteTopology<SerializableReader> getDiscreteTopology() {
      throw new UnsupportedOperationException();
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileClob(b, length());
    }

    @Override
    final Class<SerializableReader> type() {
      return type;
    }

    @Override
    final int sqlType() {
      return Types.CLOB;
    }

    @Override
    final SerializableReader parseString(final DbVendor vendor, final String s) {
      return new SerializableReader(new UnsynchronizedStringReader(s));
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof CLOB)
        return new CLOB(SafeMath.max(length(), ((CLOB)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final CLOB wrap(final Evaluable wrapped) {
      return (CLOB)super.wrap(wrapped);
    }

    @Override
    public final CLOB clone() {
      return new CLOB(getTable(), true, this);
    }

    // FIXME: Warning! Calling equals will result in the underlying stream to be fully read
    @Override
    final boolean equals(final Column<SerializableReader> obj) {
      final CLOB that = (CLOB)obj;
      initClobReader();
      that.initClobReader();
      return valueCur.equals(that.valueCur);
    }

    // FIXME: Warning! Calling hashCode will result in the underlying stream to be fully read
    @Override
    final int valueHashCode() {
      initClobReader();
      return valueCur.hashCode();
    }

    // FIXME: Is this a bad pattern? Read the full stream just to get toString()?
    private final class ClobReader extends SerializableReader {
      private final String string;

      private ClobReader(final String string) {
        super(new UnsynchronizedStringReader(string));
        this.string = string;
      }

      @Override
      public boolean equals(final Object obj) {
        return obj == this || obj instanceof ClobReader && Objects.equals(string, ((ClobReader)obj).string);
      }

      @Override
      public int hashCode() {
        return string.hashCode();
      }

      @Override
      public String toString() {
        return string;
      }
    }

    private void initClobReader() {
      if (!(valueCur instanceof ClobReader)) {
        try {
          setValue(new ClobReader(Readers.readFully(valueCur)));
        }
        catch (final IOException e) {
          throw new UncheckedIOException(e);
        }
      }
    }

    @Override
    public final String toString() {
      if (isNull())
        return "NULL";

      initClobReader();
      return valueCur.toString();
    }
  }

  private static DATE $date;

  public static DATE DATE() {
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

    DATE(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final LocalDate _default, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
    }

    DATE(final Table owner, final boolean mutable, final DATE copy) {
      super(owner, mutable, copy);
    }

    public DATE(final LocalDate value) {
      super(null, true);
      set(value);
    }

    public DATE() {
      super(null, true);
    }

    private DATE(final boolean mutable) {
      super(null, mutable);
    }

    final void copy(final DATE copy) {
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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

    @Override
    DiscreteTopology<LocalDate> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_DATE;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDate(b);
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
    final LocalDate parseString(final DbVendor vendor, final String s) {
      return Dialect.dateFromString(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final DATE wrap(final Evaluable wrapped) {
      return (DATE)super.wrap(wrapped);
    }

    @Override
    public final DATE clone() {
      return new DATE(getTable(), true, this);
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATE ? valueCur.compareTo(((DATE)o).valueCur) : LocalDateTime.of(valueCur, LocalTime.MIDNIGHT).compareTo(((DATETIME)o).valueCur);
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateToString(valueCur);
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

  public abstract static class Column<V extends Serializable> extends Entity implements type.Column<V> {
    static enum SetBy {
      USER,
      SYSTEM
    }

    abstract boolean setValue(final V value);

    static String getSimpleName(final Class<?> cls) {
      String name = cls.getCanonicalName();
      if (name == null)
        name = cls.getName();

      return name.substring(name.indexOf("data.") + 5).replace('.', ' ');
    }

    private final Table table;
    final String name;
    final boolean primary;
    final boolean nullable;
    final GenerateOn<? super V> generateOnInsert;
    final GenerateOn<? super V> generateOnUpdate;
    final boolean keyForUpdate;

    @SuppressWarnings("rawtypes")
    final Consumer commitUpdate;

    Column(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(mutable);
      this.table = owner;
      this.name = name;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;
      this.commitUpdate = commitUpdate;
    }

    Column(final Table owner, final boolean mutable, final Column<V> copy) {
      super(mutable);
      this.table = owner;
      this.name = copy.name;
      this.primary = copy.primary;
      this.nullable = copy.nullable;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.keyForUpdate = copy.keyForUpdate;
      this.commitUpdate = copy.commitUpdate;

      // NOTE: Deliberately not copying ref or setBy
      // this.ref = copy.ref;
      this.setByCur = copy.setByCur;
      this.setByOld = copy.setByOld;
    }

    Column(final Table owner, final boolean mutable) {
      this(owner, mutable, null, false, false, null, true, null, null);
    }

    @Override
    public final Table getTable() {
      return table;
    }

    @Override
    final Column<?> getColumn() {
      return this;
    }

    /**
     * Returns the name of this {@link Column}.
     *
     * @return The name of this {@link Column}.
     */
    public final String getName() {
      return name;
    }

    int columnIndex;
    type.Column<V> ref;
    SetBy setByOld;
    SetBy setByCur;
    boolean changed;

    protected abstract boolean set(V value);
    protected abstract boolean setIfNotNull(V value);
    abstract boolean set(V value, SetBy setBy);
    public abstract void revert();
    abstract void _commitEntity$();
    abstract DiscreteTopology<V> getDiscreteTopology();

    final boolean setFromString(final DbVendor vendor, final String value, final SetBy setBy) {
      assertMutable();
      return set(value == null ? null : parseString(vendor, value), setBy);
    }

    final void set(final type.Column<V> ref) {
      assertMutable();
      this.setByCur = null;
      this.ref = ref;
    }

    boolean setNull() {
      assertMutable();

      this.changed = !isNullOld();
      this.setByCur = SetBy.USER;
      this.ref = null;
      return changed;
    }

    public final boolean wasSet() {
      return setByCur != null;
    }

    public final boolean reset() {
      final boolean wasSet = setByCur != null;
      changed = false;
      setByCur = null;
      return wasSet;
    }

    public abstract V get();
    public abstract V get(V defaultValue);
    public abstract boolean isNull();

    abstract V getOld();
    abstract boolean isNullOld();

    final boolean isForUpdateWhereGetOld(final boolean isForUpdateWhere) {
      return isForUpdateWhere && primary && setByOld != null;
    }

    final boolean getForUpdateWhereIsNullOld(final boolean isForUpdateWhere) {
      return isForUpdateWhereGetOld(isForUpdateWhere) ? isNullOld() : isNull();
    }

    final V getForUpdateWhereGetOld(final boolean isForUpdateWhere) {
      return isForUpdateWhereGetOld(isForUpdateWhere) ? getOld() : get();
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
      assertMutable();
      if (rows.getConcurrency() == Concurrency.READ_ONLY)
        throw new IllegalStateException(rows.getConcurrency().getClass().getSimpleName() + "." + rows.getConcurrency());

      update(Compiler.getCompiler(DbVendor.valueOf(rows.resultSet.getStatement().getConnection().getMetaData())), rows.resultSet, columnIndex);
    }

    public final <C extends Column<V>>C AS(final C column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    public final <E extends EntityEnum>ENUM<E> AS(final ENUM<E> column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    @Override
    boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Evaluable wrapped = wrapped();
      if (wrapped == null) {
        final data.Table table = getTable();
        if (table != null) {
          final Alias alias = compilation.getAlias(table);
          final StringBuilder sql = compilation.sql;
          if (alias != null) {
            alias.compile(compilation, false);
            sql.append('.');
            compilation.vendor.getDialect().quoteIdentifier(sql, name);
          }
          else if (!compilation.subCompile(table)) {
            compilation.vendor.getDialect().quoteIdentifier(sql, name);
          }
        }
        else {
          return compilation.addParameter(this, false, false);
        }
      }
      else if (!compilation.subCompile(this)) {
        return wrapped.compile(compilation, isExpression);
      }

      return primary;
    }

    @Override
    Serializable evaluate(final Set<Evaluable> visited) {
      if (ref == null || visited.contains(this)) {
        final Evaluable wrapped = wrapped();
        return wrapped != null ? wrapped.evaluate(visited) : get();
      }

      visited.add(this);
      return ((Evaluable)ref).evaluate(visited);
    }

    abstract Class<V> type();
    abstract int sqlType();
    abstract V parseString(DbVendor vendor, String s);
    abstract void read(Compiler compiler, ResultSet resultSet, int columnIndex) throws SQLException;
    abstract void update(Compiler compiler, ResultSet resultSet, int columnIndex) throws SQLException;
    abstract void write(Compiler compiler, PreparedStatement statement, boolean isForUpdateWhere, int parameterIndex) throws IOException, SQLException;
    abstract StringBuilder compile(Compiler compiler, StringBuilder b, boolean isForUpdateWhere) throws IOException;
    abstract StringBuilder declare(StringBuilder b, DbVendor vendor);
    abstract Column<?> scaleTo(Column<?> column);

    @Override
    public abstract Column<V> clone();

    abstract boolean equals(Column<V> that);

    boolean equal(final V a, final V b) {
      return Objects.equals(a, b);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (getClass() != obj.getClass())
        return false;

      final Column<V> that = (Column<V>)obj;
      if (!name.equals(that.name))
        return false;

      return isNull() ? that.isNull() : !that.isNull() && equals(that);
    }

    abstract int valueHashCode();

    @Override
    public final int hashCode() {
      final int hashCode = name.hashCode();
      return isNull() ? hashCode : hashCode ^ valueHashCode();
    }

    abstract StringBuilder toJson(StringBuilder b);

    @Override
    public abstract String toString();
  }

  private static DATETIME $datetime;

  public static DATETIME DATETIME() {
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
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL topology, but their own default is 0
    private static final byte DEFAULT_PRECISION = 6;

    private final Byte precision;

    DATETIME(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      this.precision = precision == null ? null : precision.byteValue();
    }

    DATETIME(final Table owner, final boolean mutable, final DATETIME copy) {
      super(owner, mutable, copy);
      this.precision = copy.precision;
    }

    public DATETIME(final int precision) {
      super(null, true);
      this.precision = (byte)precision;
    }

    public DATETIME(final Integer precision) {
      super(null, true);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public DATETIME() {
      this(true);
    }

    private DATETIME(final boolean mutable) {
      super(null, mutable);
      this.precision = null;
    }

    public DATETIME(final LocalDateTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    final void copy(final DATETIME copy) {
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
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

    public final Byte precision() {
      return precision;
    }

    @Override
    DiscreteTopology<LocalDateTime> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_DATE_TIME[precision];
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDateTime(b, precision);
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
    final LocalDateTime parseString(final DbVendor vendor, final String s) {
      return Dialect.dateTimeFromString(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATETIME)
        return new DATETIME(SafeMath.max(precision(), ((DATETIME)column).precision()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final DATETIME wrap(final Evaluable wrapped) {
      return (DATETIME)super.wrap(wrapped);
    }

    @Override
    public final DATETIME clone() {
      return new DATETIME(getTable(), true, this);
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return o instanceof DATETIME ? valueCur.compareTo(((DATETIME)o).valueCur) : valueCur.toLocalDate().compareTo(((DATE)o).valueCur);
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateTimeToString(valueCur);
    }
  }

  private static DECIMAL $decimal;

  public static DECIMAL DECIMAL() {
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
    private BigDecimal valueOld;
    private BigDecimal valueCur;

    DECIMAL(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final Integer precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
      }

      checkScale(precision, scale);
      this.scale = scale;
      this.min = min;
      this.max = max;
    }

    DECIMAL(final Table owner, final boolean mutable, final DECIMAL copy) {
      super(owner, mutable, copy, copy.precision);

      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public DECIMAL(final int precision, final int scale) {
      super(null, true, precision);
      checkScale(precision, scale);
      this.scale = scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final Integer precision, final Integer scale) {
      super(null, true, precision);
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
      super(null, true, value == null ? null : value.precision());
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
      super(null, mutable, null);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    final void copy(final DECIMAL copy) {
      // assertMutable();
      final BigDecimal value = copy.valueCur;
      this.changed = !equal(this.valueCur, value);

//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final DECIMAL set(final type.DECIMAL value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DECIMAL set(final NULL value) {
      setNull();
      return this;
    }

    @Override
    public final boolean set(final BigDecimal value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final BigDecimal value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    boolean setValue(final BigDecimal value) {
      assertMutable();
      if (equal(valueCur, value))
        return false;

      if (value != null)
        checkValue(value);

      this.changed = !equal(valueOld, value);
      this.valueCur = value;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = valueCur != null;

      valueCur = null;
      return changed;
    }

    @Override
    DiscreteTopology<BigDecimal> getDiscreteTopology() {
      return DiscreteTopologies.BIG_DECIMAL(scale);
    }

    @Override
    final void _commitEntity$() {
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    @Override
    public final BigDecimal get() {
      return valueCur;
    }

    @Override
    public final BigDecimal get(final BigDecimal defaultValue) {
      return isNull() ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return valueCur == null;
    }

    @Override
    final boolean isNullOld() {
      return valueOld == null;
    }

    @Override
    final BigDecimal getOld() {
      return setByOld == null ? get() : isNullOld() ? null : valueOld;
    }

    private void checkValue(final BigDecimal value) {
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDecimal(b, precision(), scale(), min);
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
    final BigDecimal parseString(final DbVendor vendor, final String s) {
      return new BigDecimal(s);
    }

    @Override
    final String primitiveToString() {
      throw new UnsupportedOperationException();
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final BigDecimal value = resultSet.getBigDecimal(columnIndex);
      this.valueOld = this.valueCur = resultSet.wasNull() ? null : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBigDecimal(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBigDecimal(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      final Integer precisionThis = precision();
      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        final Integer precisionThat = decimal.precision();
        return new DECIMAL(precisionThis == null || precisionThat == null ? null : SafeMath.max((int)precisionThis, precisionThat + 1), SafeMath.max(scale(), decimal.scale()));
      }

      if (column instanceof ApproxNumeric)
        return new DECIMAL(precisionThis == null ? null : precisionThis + 1, scale());

      if (column instanceof ExactNumeric) {
        final ExactNumeric<?> exactNumeric = (ExactNumeric<?>)column;
        final Integer precisionThat = exactNumeric.precision();
        final Integer precision = precisionThis == null || precisionThat == null ? null : SafeMath.max((int)precisionThis, precisionThat + 1);
        return new DECIMAL(precision, scale());
      }

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
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
        return valueCur.compareTo(BigDecimal.valueOf(((TINYINT)o).valueCur));

      if (o instanceof SMALLINT)
        return valueCur.compareTo(BigDecimal.valueOf(((SMALLINT)o).valueCur));

      if (o instanceof INT)
        return valueCur.compareTo(BigDecimal.valueOf(((INT)o).valueCur));

      if (o instanceof BIGINT)
        return valueCur.compareTo(BigDecimal.valueOf(((BIGINT)o).valueCur));

      if (o instanceof FLOAT)
        return valueCur.compareTo(BigDecimal.valueOf(((FLOAT)o).valueCur));

      if (o instanceof DOUBLE)
        return valueCur.compareTo(BigDecimal.valueOf(((DOUBLE)o).valueCur));

      if (o instanceof DECIMAL)
        return valueCur.compareTo(((DECIMAL)o).valueCur);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(getTable(), true, this);
    }

    @Override
    boolean equal(final BigDecimal a, final BigDecimal b) {
      return a == b || a != null && b != null && a.compareTo(b) == 0;
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }

    @Override
    final StringBuilder toJson(final StringBuilder b) {
      return b.append(isNull() ? "null" : valueCur.toString());
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : valueCur.toString();
    }
  }

  private static DOUBLE $double;

  public static DOUBLE DOUBLE() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private double valueOld;
    private double valueCur;

    DOUBLE(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final Double min, final Double max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    DOUBLE(final Table owner, final boolean mutable, final DOUBLE copy) {
      super(owner, mutable, copy);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
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

    final void copy(final DOUBLE copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final DOUBLE set(final type.DOUBLE value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final DOUBLE set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    @Override
    public final boolean set(final Double value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Double value, final SetBy setBy) {
      return value == null ? setNull() : set((double)value, setBy);
    }

    public final boolean set(final double value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final double value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Double value) {
      return value == null ? setNull() : setValue((double)value);
    }

    final boolean setValue(final double value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Double> getDiscreteTopology() {
      return DiscreteTopologies.DOUBLE;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final double value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final double getAsDouble() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final double getAsDouble(final double defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Double get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Double get(final Double defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Double getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDouble(b, min);
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
    final Double parseString(final DbVendor vendor, final String s) {
      return Double.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final double value = resultSet.getDouble(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? Double.NaN : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateDouble(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setDouble(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision() == null ? null : decimal.precision() + 1, decimal.scale());
      }

      if (column instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final DOUBLE wrap(final Evaluable wrapped) {
      return (DOUBLE)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Double.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Double.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Double.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Double.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Double.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Double.compare(valueCur, ((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return Double.compare(valueCur, ((DECIMAL)o).valueCur.doubleValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final DOUBLE clone() {
      return new DOUBLE(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Double.hashCode(valueCur);
    }
  }

  private static ENUM<?> $enum;

  public static ENUM<?> ENUM() {
    return $enum == null ? $enum = new ENUM<>(false) : $enum;
  }

  public static class ENUM<E extends EntityEnum> extends Textual<E> implements type.ENUM<E> {
    // FIXME: data.ENUM.NULL
    // @org.jaxdb.jsql.EntityEnum.Spec(table="relation", column="units")
    private static final class NULL_ENUM implements EntityEnum {
      public static final NULL_ENUM NULL;

      private static byte index;

      private static final NULL_ENUM[] values = {
        NULL = new NULL_ENUM("NULL")
      };

      public static NULL_ENUM[] values() {
        return values;
      }

      public static NULL_ENUM valueOf(final String string) {
        if (string == null)
          return null;

        for (final NULL_ENUM value : values()) // [A]
          if (string.equals(value.name))
            return value;

        return null;
      }

      private final byte ordinal;
      private final String name;

      private NULL_ENUM(final String name) {
        this.ordinal = index++;
        this.name = name;
      }

      @Override
      public int length() {
        return 0;
      }

      @Override
      public char charAt(final int index) {
        return 0;
      }

      @Override
      public CharSequence subSequence(final int start, final int end) {
        return null;
      }

      @Override
      public byte ordinal() {
        return ordinal;
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
    final E[] constants;
    private final Function<String,E> fromStringFunction;

    @SuppressWarnings("unchecked")
    private static <E extends EntityEnum>E[] getConstants(final Class<E> enumType) {
      try {
        return (E[])enumType.getMethod("values").invoke(null);
      }
      catch (final IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
      catch (final InvocationTargetException e) {
        if (e.getCause() instanceof RuntimeException)
          throw (RuntimeException)e.getCause();

        throw new RuntimeException(e.getCause());
      }
    }

    private static short calcEnumLength(final EntityEnum[] constants) {
      final Short cached = typeToLength.get(constants.getClass().getComponentType());
      if (cached != null)
        return cached;

      short length = 0;
      for (final EntityEnum constant : constants) { // [A]
        final int len = constant.toString().length();
        if (length < len)
          length = (short)len;
      }

      typeToLength.put(constants.getClass().getComponentType(), length);
      return length;
    }

    @SuppressWarnings("unchecked")
    ENUM(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final E _default, final GenerateOn<? super E> generateOnInsert, final GenerateOn<? super E> generateOnUpdate, final E[] constants, final Function<String,E> fromStringFunction) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate, calcEnumLength(constants));
      this.enumType = (Class<E>)constants.getClass().getComponentType();
      this.constants = constants;
      this.fromStringFunction = fromStringFunction;
    }

    ENUM(final Table owner, final boolean mutable, final ENUM<E> copy) {
      super(owner, mutable, copy, copy.length());
      this.enumType = copy.enumType;
      this.constants = copy.constants;
      this.fromStringFunction = copy.fromStringFunction;
    }

    public ENUM(final Class<E> enumType) {
      this(enumType, getConstants(enumType));
    }

    @SuppressWarnings("unchecked")
    private ENUM(final Class<E> enumType, final E[] constants) {
      super(null, true, constants == null ? null : calcEnumLength(constants));
      this.enumType = enumType;
      this.constants = constants;
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

          return (E)method.invoke(this, s);
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
      super(null, mutable, null);
      this.enumType = null;
      this.constants = null;
      this.fromStringFunction = null;
    }

    @SuppressWarnings("unchecked")
    public ENUM(final E value) {
      this(value == null ? null : (Class<E>)value.getClass());
      set(value);
    }

    public final ENUM<E> set(final type.ENUM<E> value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final ENUM<E> set(final NULL value) {
      super.setNull();
      return this;
    }

    final void copy(final ENUM<E> copy) {
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final boolean setFromString(final String value) {
      return set(value == null ? null : fromStringFunction.apply(value));
    }

    public final String getAsString() {
      return valueCur == null ? null : valueCur.toString();
    }

    // FIXME: This is UNTESTED!
    private final DiscreteTopology<E> topology = new DiscreteTopology<E>() {
      @Override
      public boolean isMinValue(final E v) {
        return constants[0].ordinal() == v.ordinal();
      }

      @Override
      public boolean isMaxValue(final E v) {
        return constants[constants.length - 1].ordinal() == v.ordinal();
      }

      @Override
      public E prevValue(final E v) {
        return isMinValue(v) ? v : constants[v.ordinal() - 1];
      }

      @Override
      public E nextValue(final E v) {
        return isMaxValue(v) ? v : constants[v.ordinal() + 1];
      }
    };

    @Override
    DiscreteTopology<E> getDiscreteTopology() {
      return topology;
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
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
    final E parseString(final DbVendor vendor, final String s) {
      return fromStringFunction.apply(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final String value = resultSet.getString(columnIndex);
      if (value == null) {
        this.valueOld = this.valueCur = null;
        this.setByOld = this.setByCur = SetBy.SYSTEM;
        return;
      }

      for (final E constant : constants) { // [A]
        if (constant.toString().equals(value)) {
          this.valueOld = this.valueCur = constant;
          this.setByOld = this.setByCur = SetBy.SYSTEM;
          return;
        }
      }

      throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateObject(columnIndex, valueCur.toString());
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setObject(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld.toString() : valueCur.toString());
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    @SuppressWarnings("unchecked")
    final ENUM<E> wrap(final Evaluable wrapped) {
      return (ENUM<E>)super.wrap(wrapped);
    }

    @Override
    public final ENUM<E> clone() {
      return new ENUM<>(getTable(), true, this);
    }

    @Override
    final String evaluate(final Set<Evaluable> visited) {
      return isNull() ? null : valueCur.toString();
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : get().toString();
    }
  }

  public abstract static class Table extends Entity implements type.Table {
    protected static final Column<?>[] empty = {};

    final Column<?>[] _column$;
    final Column<?>[] _primary$;
    final Column<?>[] _keyForUpdate$;
    final Column<?>[] _auto$;
    private final boolean _wasSelected$;
    private final data.MutableKey _primaryKey$;
    private final data.MutableKey _primaryKey$Old;

    Table(final boolean mutable, final boolean _wasSelected$, final Column<?>[] _column$, final Column<?>[] _primary$, final Column<?>[] _keyForUpdate$, final Column<?>[] _auto$) {
      super(mutable);
      this._wasSelected$ = _wasSelected$;
      this._column$ = _column$;
      this._primary$ = _primary$;
      this._keyForUpdate$ = _keyForUpdate$;
      this._auto$ = _auto$;
      this._primaryKey$ = data.Key.cur(_primary$);
      this._primaryKey$Old = data.Key.old(_primary$);
    }

    Table(final boolean mutable, final Table copy) {
      super(mutable);
      this._wasSelected$ = false;
      this._column$ = copy._column$.clone();
      this._primary$ = copy._primary$.clone();
      this._keyForUpdate$ = copy._keyForUpdate$.clone();
      this._auto$ = copy._auto$.clone();
      this._primaryKey$ = data.Key.cur(_primary$);
      this._primaryKey$Old = data.Key.old(_primary$);
    }

    Table() {
      super(true);
      this._wasSelected$ = false;
      this._column$ = null;
      this._primary$ = null;
      this._keyForUpdate$ = null;
      this._auto$ = null;
      this._primaryKey$ = null;
      this._primaryKey$Old = null;
    }

    public final data.MutableKey getKey() {
      return _primaryKey$;
    }

    final data.MutableKey getKeyOld() {
      return _primaryKey$Old;
    }

    @SuppressWarnings("unchecked")
    final void _commitUpdate$() {
      for (final Column<?> column : _column$) // [A]
        if (column.changed && column.commitUpdate != null)
          column.commitUpdate.accept(this);
    }

    public final void revert() {
      for (final Column<?> column : getColumns()) // [A]
        column.revert();
    }

    final boolean wasSelected() {
      return _wasSelected$;
    }

    @Override
    final Table getTable() {
      return this;
    }

    OneToOneMap getCache() {
      return singleton().getCache();
    }

    abstract Table singleton();
    // FIXME: This was not used, so I removed it...
    // abstract Schema getSchema();

    @Override
    final Column<?> getColumn() {
      throw new UnsupportedOperationException();
    }

    @Override
    final Table evaluate(final Set<Evaluable> visited) {
      return this;
    }

    @Override
    final boolean compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      return compilation.compiler.compile(this, compilation, isExpression);
    }

    /**
     * Set the provided {@link Map map} specifying the values (parsable by the given {@link DbVendor}) for the named columns in this
     * {@link Table}.
     *
     * @param vendor The {@link DbVendor}.
     * @param map The {@link Map Map&lt;String,String&gt;} specifying the values for the named columns in this {@link Table}.
     * @param setBy The {@link SetBy} value to be used when setting each column.
     * @return A list of column names that were not found (and thus not set) in the table, or {@code null} if all columns were found
     *         (and thus set).
     * @throws NullPointerException If the provided {@link Map map} is null.
     * @throws IllegalArgumentException If this {@link Table} does not define a named column for a key in the {@link Map map}.
     */
    final String[] setColumns(final DbVendor vendor, final Map<String,String> map, final SetBy setBy) {
      return map.size() == 0 ? null : setColumns(vendor, setBy, map.entrySet().iterator(), 0);
    }

    private String[] setColumns(final DbVendor vendor, final SetBy setBy, final Iterator<Map.Entry<String,String>> iterator, final int depth) {
      while (iterator.hasNext()) {
        final Map.Entry<String,String> entry = iterator.next();
        final String key = entry.getKey();
        final Column<?> column = getColumn(key);
        if (column == null) {
          final String[] notFound = setColumns(vendor, setBy, iterator, depth + 1);
          notFound[depth] = key;
          return notFound;
        }

        column.setFromString(vendor, entry.getValue(), setBy);
        column._commitEntity$();
      }

      return depth == 0 ? null : new String[depth];
    }

    /**
     * Returns the {@link Column}s in {@code this} {@link Table}.
     *
     * @return The {@link Column}s in {@code this} {@link Table}.
     */
    public final Column<?>[] getColumns() {
      return _column$;
    }

    /**
     * Returns the {@link Column} in {@code this} {@link Table} matching the specified {@code name}, or {@code null} there is no
     * match.
     *
     * @param name The name of the {@link Column}.
     * @return The {@link Column} in {@code this} {@link Table} matching the specified {@code name}, or {@code null} there is no
     *         match.
     * @throws NullPointerException If {@code name} is null.
     */
    public final Column<?> getColumn(final String name) {
      final int index = Arrays.binarySearch(_columnName$(), name);
      return index < 0 ? null : _column$[_columnIndex$()[index]];
    }

    public final void reset() {
      for (final Column<?> column : _column$) // [A]
        column.reset();
    }

    public final void reset(final Except except) {
      if (except == null) {
        reset();
      }
      else if (except == Except.PRIMARY_KEY_FOR_UPDATE) {
        for (final Column<?> column : _column$) // [A]
          if (!column.primary && !column.keyForUpdate)
            column.reset();
      }
      else if (except == Except.PRIMARY_KEY) {
        for (final Column<?> column : _column$) // [A]
          if (!column.primary)
            column.reset();
      }
      else {
        throw new UnsupportedOperationException("Unsupported Except: " + except);
      }
    }

    void _initCache$() {
    }

    final void _commitEntity$() {
      for (final Column<?> column : _column$) // [A]
        column._commitEntity$();
    }

    void _commitInsert$(final boolean addRange) {
    }

    void _commitDelete$() {
    }

    public abstract String getName();
    abstract String[] _columnName$();
    abstract byte[] _columnIndex$();
    abstract Table newInstance();
    abstract void _merge$(Table table);
    abstract Table clone(boolean _mutable$);

    public final void merge(final Table table) {
      if (table != this)
        _merge$(table);
    }

    @Override
    public abstract Table clone();

    @Override
    public abstract boolean equals(final Object obj);

    @Override
    public abstract int hashCode();

    protected abstract void toString(boolean wasSetOnly, StringBuilder s);

    protected final String toString(final boolean wasSetOnly) {
      final StringBuilder s = new StringBuilder();
      toString(wasSetOnly, s);
      return s.toString();
    }

    @Override
    public String toString() {
      return toString(false);
    }
  }

  public abstract static class ExactNumeric<V extends Number> extends Numeric<V> implements type.ExactNumeric<V> {
    final Integer precision;

    ExactNumeric(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Table owner, final boolean mutable, final Numeric<V> copy, final Integer precision) {
      super(owner, mutable, copy);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable);
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

  public static FLOAT FLOAT() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private float valueOld;
    private float valueCur;

    FLOAT(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final Float min, final Float max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    FLOAT(final Table owner, final boolean mutable, final FLOAT copy) {
      super(owner, mutable, copy);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
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
      this.isNullCur = true;
      return this;
    }

    final void copy(final FLOAT copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    @Override
    public final boolean set(final Float value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Float value, final SetBy setBy) {
      return value == null ? setNull() : set((float)value, setBy);
    }

    public final boolean set(final float value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final float value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Float value) {
      return value == null ? setNull() : setValue((float)value);
    }

    final boolean setValue(final float value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Float> getDiscreteTopology() {
      return DiscreteTopologies.FLOAT;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final float value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final float getAsFloat() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final float getAsFloat(final float defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Float get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Float get(final Float defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Float getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareFloat(b, min);
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
    final Float parseString(final DbVendor vendor, final String s) {
      return Float.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final float value = resultSet.getFloat(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? Float.NaN : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateFloat(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setFloat(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof FLOAT || column instanceof TINYINT)
        return new FLOAT();

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (column instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final FLOAT wrap(final Evaluable wrapped) {
      return (FLOAT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Float.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Float.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Float.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Float.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Float.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Float.compare(valueCur, (float)((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return Float.compare(valueCur, ((DECIMAL)o).valueCur.floatValue());

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Float.hashCode(valueCur);
    }
  }

  public abstract static class LargeObject<V extends Closeable & Serializable> extends Objective<V> implements type.LargeObject<V> {
    private final Long length;

    LargeObject(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
    }

    LargeObject(final Table owner, final boolean mutable, final LargeObject<V> copy) {
      super(owner, mutable, copy);
      this.length = copy.length;
    }

    LargeObject(final Table owner, final boolean mutable, final Long length) {
      super(owner, mutable);
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

  public static INT INT() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private int valueOld;
    private int valueCur;

    INT(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final Integer precision, final Integer min, final Integer max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    INT(final Table owner, final boolean mutable, final INT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public INT(final short precision) {
      super(null, true, (int)precision);
      this.min = null;
      this.max = null;
    }

    public INT(final Short precision) {
      this(null, true, precision);
    }

    public INT(final Integer value) {
      this(value == null ? null : (short)Numbers.precision(value));
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

    private INT(final Table owner, final boolean mutable, final Short precision) {
      super(owner, mutable, precision == null ? null : precision.intValue());
      this.min = null;
      this.max = null;
    }

    private INT(final boolean mutable) {
      this(null, mutable, (Short)null);
    }

    public final INT set(final type.INT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final INT set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    final void copy(final INT copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    @Override
    public final boolean set(final Integer value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Integer value, final SetBy setBy) {
      return value == null ? setNull() : set((int)value, setBy);
    }

    public final boolean set(final int value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final int value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Integer value) {
      return value == null ? setNull() : setValue((int)value);
    }

    final boolean setValue(final int value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Integer> getDiscreteTopology() {
      return DiscreteTopologies.INTEGER;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final int value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final int getAsInt() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final int getAsInt(final int defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Integer get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Integer get(final Integer defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Integer getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt32(b, Numbers.cast(precision(), Byte.class), min);
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
    final Integer parseString(final DbVendor vendor, final String s) {
      return Integer.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final int value = resultSet.getInt(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setInt(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof ApproxNumeric)
        return new DOUBLE();

      if (column instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)column;
        return new DECIMAL(SafeMath.max(precision(), decimal.precision()), decimal.scale());
      }

      if (column instanceof BIGINT)
        return new BIGINT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      if (column instanceof ExactNumeric)
        return new INT(SafeMath.max(precision(), ((ExactNumeric<?>)column).precision()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final INT wrap(final Evaluable wrapped) {
      return (INT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Integer.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Integer.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Integer.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Long.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Float.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Double.compare(valueCur, ((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(valueCur).compareTo(((DECIMAL)o).valueCur);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final INT clone() {
      return new INT(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Integer.hashCode(valueCur);
    }
  }

  public abstract static class Objective<V extends Serializable> extends Column<V> implements type.Objective<V> {
    V valueOld;
    V valueCur;

    Objective(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
      this.valueOld = this.valueCur = _default;
    }

    Objective(final Table owner, final boolean mutable, final Objective<V> copy) {
      super(owner, mutable, copy);

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    Objective(final Table owner, final boolean mutable) {
      super(owner, mutable);
    }

    @Override
    public final boolean set(final V value) {
      return set(value, SetBy.USER);
    }

    @Override
    public final boolean setIfNotNull(final V value) {
      return value != null && set(value);
    }

    @Override
    final boolean set(final V value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final V value) {
      assertMutable();
      if (equal(this.valueCur, value))
        return false;

      this.changed = !equal(this.valueOld, value);
      this.valueCur = value;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = valueCur != null;

      valueCur = null;
      return changed;
    }

    @Override
    final void _commitEntity$() {
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    @Override
    public final V get() {
      return valueCur;
    }

    @Override
    public final V get(final V defaultValue) {
      return isNull() ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return valueCur == null;
    }

    @Override
    final boolean isNullOld() {
      return valueOld == null;
    }

    @Override
    final V getOld() {
      return setByOld == null ? get() : isNullOld() ? null : valueOld;
    }

    @Override
    final StringBuilder toJson(final StringBuilder b) {
      return isNull() ? b.append("null") : b.append('"').append(this).append('"');
    }
  }

  public abstract static class Primitive<V extends Serializable> extends Column<V> implements type.Primitive<V> {
    Primitive(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
    }

    Primitive(final Table owner, final boolean mutable, final Primitive<V> copy) {
      super(owner, mutable, copy);
    }

    Primitive(final Table owner, final boolean mutable) {
      super(owner, mutable);
    }

    abstract String primitiveToString();

    @Override
    StringBuilder toJson(final StringBuilder b) {
      return b.append(isNull() ? "null" : primitiveToString());
    }

    @Override
    public String toString() {
      return isNull() ? "NULL" : primitiveToString();
    }
  }

  private static SMALLINT $smallint;

  public static SMALLINT SMALLINT() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private short valueOld;
    private short valueCur;

    SMALLINT(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final Integer precision, final Short min, final Short max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    SMALLINT(final Table owner, final boolean mutable, final SMALLINT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public SMALLINT(final int precision) {
      this(null, true, precision);
    }

    public SMALLINT(final Integer precision) {
      this(null, true, precision);
    }

    public SMALLINT(final Short value) {
      this(value == null ? null : (int)Numbers.precision(value));
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

    private SMALLINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    private SMALLINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    public final SMALLINT set(final type.SMALLINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final SMALLINT set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    final void copy(final SMALLINT copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    @Override
    public final boolean set(final Short value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Short value, final SetBy setBy) {
      return value == null ? setNull() : set((short)value, setBy);
    }

    public final boolean set(final short value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final short value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Short value) {
      return value == null ? setNull() : setValue((short)value);
    }

    final boolean setValue(final short value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Short> getDiscreteTopology() {
      return DiscreteTopologies.SHORT;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final short value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final short getAsShort() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final short getAsShort(final short defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Short get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Short get(final Short defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Short getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt16(b, Numbers.cast(precision(), Byte.class), min);
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
    final Short parseString(final DbVendor vendor, final String s) {
      return Short.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final short value = resultSet.getShort(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setShort(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof ApproxNumeric)
        return new DOUBLE();

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

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final SMALLINT wrap(final Evaluable wrapped) {
      return (SMALLINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Short.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Short.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Integer.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Long.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Float.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Double.compare(valueCur, ((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(valueCur).compareTo(((DECIMAL)o).valueCur);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Short.hashCode(valueCur);
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

    Numeric(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate);
    }

    Numeric(final Table owner, final boolean mutable, final Numeric<V> copy) {
      super(owner, mutable, copy);
    }

    Numeric(final Table owner, final boolean mutable) {
      super(owner, mutable);
    }

    public abstract V min();
    public abstract V max();

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    final boolean equals(final Column<V> obj) {
      return compareTo(obj) == 0;
    }

    @Override
    public boolean setIfNotNull(final V value) {
      return value != null && set(value);
    }
  }

  private static TINYINT $tinyint;

  public static TINYINT TINYINT() {
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
    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private byte valueOld;
    private byte valueCur;

    TINYINT(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final Integer precision, final Byte min, final Byte max) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    TINYINT(final Table owner, final boolean mutable, final TINYINT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.setByOld = copy.setByOld;
      this.setByCur = copy.setByCur;

      this.changed = copy.changed;
    }

    public TINYINT(final int precision) {
      this(null, true, precision);
    }

    public TINYINT(final Integer precision) {
      this(null, true, precision);
    }

    public TINYINT(final Byte value) {
      this(value == null ? null : (int)Numbers.precision(value));
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

    private TINYINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    private TINYINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    public final TINYINT set(final type.TINYINT value) {
      super.set(value);
      return this;
    }

    @SuppressWarnings("unused")
    public final TINYINT set(final NULL value) {
      super.setNull();
      this.isNullCur = true;
      return this;
    }

    final void copy(final TINYINT copy) {
      // assertMutable();
      this.changed = isNullCur != copy.isNullCur || valueCur != copy.valueCur;
//      if (!changed)
//        return;

      this.isNullCur = copy.isNullCur;
      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    @Override
    public final boolean set(final Byte value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Byte value, final SetBy setBy) {
      return value == null ? setNull() : set((byte)value, setBy);
    }

    public final boolean set(final byte value) {
      return set(value, SetBy.USER);
    }

    final boolean set(final byte value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    final boolean setValue(final Byte value) {
      return value == null ? setNull() : setValue((byte)value);
    }

    final boolean setValue(final byte value) {
      assertMutable();
      checkValue(value);
      if (!isNullCur && valueCur == value)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.isNullCur = false;
      return changed;
    }

    @Override
    public final boolean setNull() {
      super.setNull();
      final boolean changed = !isNullCur;

      isNullCur = true;
      return changed;
    }

    @Override
    DiscreteTopology<Byte> getDiscreteTopology() {
      return DiscreteTopologies.BYTE;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
    }

    private void checkValue(final byte value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    public final byte getAsByte() {
      if (isNullCur)
        throw new NullPointerException("NULL");

      return valueCur;
    }

    public final byte getAsByte(final byte defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final Byte get() {
      return isNullCur ? null : valueCur;
    }

    @Override
    public final Byte get(final Byte defaultValue) {
      return isNullCur ? defaultValue : valueCur;
    }

    @Override
    public final boolean isNull() {
      return isNullCur;
    }

    @Override
    final boolean isNullOld() {
      return isNullOld;
    }

    @Override
    final Byte getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueOld;
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
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt8(b, Numbers.cast(precision(), Byte.class), min);
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
    final Byte parseString(final DbVendor vendor, final String s) {
      return Byte.valueOf(s);
    }

    @Override
    final String primitiveToString() {
      return String.valueOf(valueCur);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      final byte value = resultSet.getByte(columnIndex);
      this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        // FIXME: This is updateShort (though it should be updateByte) cause PostgreSQL does String.valueOf(byte). Why does it do that?!
        resultSet.updateShort(columnIndex, valueCur);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setByte(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
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

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final TINYINT wrap(final Evaluable wrapped) {
      return (TINYINT)super.wrap(wrapped);
    }

    @Override
    public final int compareTo(final Column<? extends Number> o) {
      if (o == null || o.isNull())
        return isNullCur ? 0 : 1;

      if (isNullCur)
        return -1;

      if (o instanceof TINYINT)
        return Byte.compare(valueCur, ((TINYINT)o).valueCur);

      if (o instanceof SMALLINT)
        return Short.compare(valueCur, ((SMALLINT)o).valueCur);

      if (o instanceof INT)
        return Integer.compare(valueCur, ((INT)o).valueCur);

      if (o instanceof BIGINT)
        return Long.compare(valueCur, ((BIGINT)o).valueCur);

      if (o instanceof FLOAT)
        return Float.compare(valueCur, ((FLOAT)o).valueCur);

      if (o instanceof DOUBLE)
        return Double.compare(valueCur, ((DOUBLE)o).valueCur);

      if (o instanceof DECIMAL)
        return BigDecimal.valueOf(valueCur).compareTo(((DECIMAL)o).valueCur);

      throw new UnsupportedOperationException("Unsupported type: " + o.getClass().getName());
    }

    @Override
    public final TINYINT clone() {
      return new TINYINT(getTable(), true, this);
    }

    @Override
    final int valueHashCode() {
      return Byte.hashCode(valueCur);
    }
  }

  public abstract static class Entity extends Evaluable implements type.Entity {
    final boolean _mutable$;

    protected Entity(final boolean mutable) {
      this._mutable$ = mutable;
    }

    final boolean assertMutable() {
      if (!_mutable$)
        throw new IllegalArgumentException(Classes.getCompositeName(getClass()) + " is not mutable");

      return true;
    }

    private Evaluable wrapped;

    final Evaluable original() {
      Entity wrapped = this;
      for (Evaluable next;; wrapped = (Entity)next) { // [X]
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

    Entity wrap(final Evaluable wrapped) {
      assertMutable();
      this.wrapped = wrapped;
      return this;
    }
  }

  public abstract static class Temporal<V extends java.time.temporal.Temporal & Serializable> extends Objective<V> implements Comparable<Column<? extends java.time.temporal.Temporal>>, type.Temporal<V> {
    Temporal(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
    }

    Temporal(final Table owner, final boolean mutable, final Temporal<V> copy) {
      super(owner, mutable, copy);
    }

    Temporal(final Table owner, final boolean mutable) {
      super(owner, mutable);
    }

    @Override
    final boolean equals(final Column<V> obj) {
      return compareTo(obj) == 0;
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }

    @Override
    public String toString() {
      return isNull() ? "NULL" : valueCur.toString();
    }
  }

  public abstract static class Textual<V extends CharSequence & Comparable<?> & Serializable> extends Objective<V> implements type.Textual<V>, Comparable<Textual<?>> {
    private final Short length;

    Textual(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final long length) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      this.length = (short)length;
    }

    Textual(final Table owner, final boolean mutable, final Textual<V> copy, final Short length) {
      super(owner, mutable, copy);
      this.length = length;
    }

    Textual(final Table owner, final boolean mutable, final Short length) {
      super(owner, mutable);
      this.length = length;
    }

    public final Short length() {
      return length;
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof Textual)
        return new CHAR(SafeMath.max(length(), ((Textual<?>)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    String evaluate(final Set<Evaluable> visited) {
      return (String)super.evaluate(visited);
    }

    @Override
    public final int compareTo(final Textual<?> o) {
      return o == null || o.isNull() ? isNull() ? 0 : 1 : isNull() ? -1 : valueCur.toString().compareTo(o.valueCur.toString());
    }

    @Override
    final boolean equals(final Column<V> obj) {
      return valueCur.toString().equals(((Textual<?>)obj).valueCur.toString());
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }
  }

  private static TIME $time;

  public static TIME TIME() {
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

    TIME(final Table owner, final boolean mutable, final String name, final boolean primary, final boolean keyForUpdate, final Consumer<? extends Table> commitUpdate, final boolean nullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primary, keyForUpdate, commitUpdate, nullable, _default, generateOnInsert, generateOnUpdate);
      this.precision = precision == null ? null : precision.byteValue();
    }

    TIME(final Table owner, final boolean mutable, final TIME copy) {
      super(owner, mutable, copy);
      this.precision = copy.precision;
    }

    public TIME(final int precision) {
      super(null, true);
      this.precision = (byte)precision;
    }

    public TIME(final Integer precision) {
      super(null, true);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public TIME() {
      this(true);
    }

    private TIME(final boolean mutable) {
      super(null, mutable);
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
      // assertMutable();
      this.changed = !equal(this.valueOld, copy.valueCur);
//      if (!changed)
//        return;

      this.valueCur = copy.valueCur;
      this.setByCur = copy.setByCur;
    }

    public final Byte precision() {
      return precision;
    }

    @Override
    DiscreteTopology<LocalTime> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_TIME[precision];
    }

    @Override
    StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareTime(b, precision);
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
    final LocalTime parseString(final DbVendor vendor, final String s) {
      return Dialect.timeFromString(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof TIME)
        return new DATETIME(SafeMath.max(precision(), ((TIME)column).precision()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final Column<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.isNull())
        return isNull() ? 0 : 1;

      if (isNull())
        return -1;

      if (!(o instanceof TIME))
        throw new IllegalArgumentException(getSimpleName(o.getClass()) + " cannot be compared to " + getSimpleName(getClass()));

      return valueCur.compareTo(((TIME)o).valueCur);
    }

    @Override
    final TIME wrap(final Evaluable wrapped) {
      return (TIME)super.wrap(wrapped);
    }

    @Override
    public final TIME clone() {
      return new TIME(getTable(), true, this);
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.timeToString(valueCur);
    }
  }

  public static final class Key extends type.Key {
    static MutableKey cur(final data.Column<?>[] columns) {
      return new MutableKey(columns) {
        @Override
        public Key immutable() {
          final Serializable[] values = new Serializable[columns.length];
          for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
            values[i] = columns[i].get();

          return new Key(columns, values);
        }

        @Override
        public Serializable value(final int i) {
          return columns[i].get();
        }
      };
    }

    static MutableKey old(final data.Column<?>[] columns) {
      return new MutableKey(columns) {
        @Override
        public Key immutable() {
          final Serializable[] values = new Serializable[columns.length];
          for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
            values[i] = columns[i].getOld();

          return new Key(columns, values);
        }

        @Override
        public Serializable value(final int i) {
          return columns[i].getOld();
        }
      };
    }

    private static final data.ARRAY<?>[] _array = {data.ARRAY()};

    public static Key with(final Serializable[] value) {
      return new Key(_array, value);
    }

    private static final data.BIGINT[] _bigint = {data.BIGINT()};

    public static Key with(final Long value) {
      return new Key(_bigint, value);
    }

    private static final data.BINARY[] _binary = {data.BINARY()};

    public static Key with(final byte[] value) {
      return new Key(_binary, value);
    }

    private static final data.BLOB[] _blob = {data.BLOB()};

    public static Key with(final SerializableInputStream value) {
      return new Key(_blob, value);
    }

    private static final data.BOOLEAN[] _boolean = {data.BOOLEAN()};

    public static Key with(final Boolean value) {
      return new Key(_boolean, value);
    }

    private static final data.CHAR[] _char = {data.CHAR()};

    public static Key with(final String value) {
      return new Key(_char, value);
    }

    private static final data.CLOB[] _clob = {data.CLOB()};

    public static Key with(final SerializableReader value) {
      return new Key(_clob, value);
    }

    private static final data.DATE[] _date = {data.DATE()};

    public static Key with(final LocalDate value) {
      return new Key(_date, value);
    }

    private static final data.DATETIME[] _datetime = {data.DATETIME()};

    public static Key with(final LocalDateTime value) {
      return new Key(_datetime, value);
    }

    private static final data.DECIMAL[] _decimal = {data.DECIMAL()};

    public static Key with(final BigDecimal value) {
      return new Key(_decimal, value);
    }

    private static final data.DOUBLE[] _double = {data.DOUBLE()};

    public static Key with(final Double value) {
      return new Key(_double, value);
    }

    private static final data.ENUM<?>[] _enum = {data.ENUM()};

    public static Key with(final EntityEnum value) {
      return new Key(_enum, value);
    }

    private static final data.FLOAT[] _float = {data.FLOAT()};

    public static Key with(final Float value) {
      return new Key(_float, value);
    }

    private static final data.INT[] _int = {data.INT()};

    public static Key with(final Integer value) {
      return new Key(_int, value);
    }

    private static final data.SMALLINT[] _smallint = {data.SMALLINT()};

    public static Key with(final Short value) {
      return new Key(_smallint, value);
    }

    private static final data.TIME[] _time = {data.TIME()};

    public static Key with(final LocalTime value) {
      return new Key(_time, value);
    }

    private static final data.TINYINT[] _tinyint = {data.TINYINT()};

    public static Key with(final Byte value) {
      return new Key(_tinyint, value);
    }

    static Key with(final data.Column<?>[] columns, final Serializable ... values) {
      return new Key(columns, values);
    }

    private final Serializable[] values;
    private final data.Column[] columns;

    private Key(final data.Column<?>[] columns, final Serializable ... values) {
      this.columns = columns;
      this.values = values;
    }

    DiscreteTopology<Object[]> topology() {
      return topology == null ? topology = new DiscreteTopology<Object[]>() {
        @Override
        public boolean isMinValue(final Object[] v) {
          for (int i = 0, i$ = columns.length; i < i$; ++i) // [A]
            if (!columns[i].getDiscreteTopology().isMinValue(v[i]))
              return false;

          return true;
        }

        @Override
        public boolean isMaxValue(final Object[] v) {
          for (int i = 0, i$ = columns.length; i < i$; ++i) // [A]
            if (!columns[i].getDiscreteTopology().isMaxValue(v[i]))
              return false;

          return true;
        }

        @Override
        public Object[] prevValue(final Object[] key) {
          final Object[] prev = new Object[key.length];
          Object k, p;
          for (int i = key.length - 1; i >= 0; --i) {
            k = key[i];
            p = prev[i] = columns[i].getDiscreteTopology().prevValue(k);
            if (p != k) {
              while (--i >= 0)
                prev[i] = key[i];

              return prev;
            }
          }

          return key;
        }

        @Override
        public Object[] nextValue(final Object[] key) {
          final Object[] prev = new Object[key.length];
          Object k, p;
          for (int i = key.length - 1; i >= 0; --i) {
            k = key[i];
            p = prev[i] = columns[i].getDiscreteTopology().nextValue(k);
            if (p != k) {
              while (--i >= 0)
                prev[i] = key[i];

              return prev;
            }
          }

          return key;
        }
      } : topology;
    }

    DiscreteTopology<Object[]> topology;

    @Override
    public final Key immutable() {
      return this;
    }

    @Override
    final int length() {
      return values.length;
    }

    @Override
    public final Serializable value(final int i) {
      return values[i];
    }

    @Override
    public Column column(final int i) {
      return columns[i];
    }

    data.Key next() {
      return new Key(columns, topology().nextValue(values));
    }
  }

  public static abstract class MutableKey extends type.Key {
    private final data.Column<?>[] columns;

    private MutableKey(final data.Column<?>[] columns) {
      this.columns = assertNotNull(columns);
    }

    @Override
    public Column column(final int i) {
      return columns[i];
    }

    @Override
    public abstract Key immutable();

    @Override
    final int length() {
      return columns.length;
    }
  }

  private data() {
  }
}