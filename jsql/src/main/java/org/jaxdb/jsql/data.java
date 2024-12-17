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
import java.io.InputStream;
import java.io.Reader;
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
import java.util.Comparator;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.jaxdb.jsql.QueryConfig.Concurrency;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.io.DelegateInputStream;
import org.libj.io.DelegateReader;
import org.libj.io.Readers;
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
import org.libj.util.Interval;
import org.libj.util.function.Throwing;

class KeyUtil {
  static int hashCode(final type.Key key) {
    int hashCode = 1;
    for (int i = 0, i$ = key.length(); i < i$; ++i) { // [RA]
      hashCode *= 31;
      final Object value = key.value(i);
      if (value != null)
        hashCode += value.hashCode();
    }

    return hashCode;
  }

  static boolean equals(final type.Key key, final Object obj) {
    if (obj == key)
      return true;

    if (!(obj instanceof type.Key))
      return false;

    final type.Key that = (type.Key)obj;
    final int i$ = key.length();
    if (i$ != that.length())
      return false;

    for (int i = 0; i < i$; ++i) // [RA]
      if (!Objects.equals(key.value(i), that.value(i)))
        return false;

    return true;
  }

  static String toString(final type.Key key) {
    final int i$ = key.length();
    if (i$ == 0)
      return "{}";

    final StringBuilder s = new StringBuilder();
    s.append('{');
    for (int i = 0; i < i$; ++i) // [RA]
      s.append(key.value(i)).append(',');

    s.setCharAt(s.length() - 1, '}');
    return s.toString();
  }
}

public final class data {
  public abstract static class ApproxNumeric<V extends Number> extends Numeric<V> implements type.ApproxNumeric<V> {
    ApproxNumeric(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
    }

    ApproxNumeric(final Table owner, final boolean mutable, final Numeric<V> copy) {
      super(owner, mutable, copy);
    }

    ApproxNumeric(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
    }
  }

  public static class ARRAY<T> extends Objective<T[]> implements type.ARRAY<T> {
    @SuppressWarnings("rawtypes")
    public static final class NULL extends ARRAY implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();

    final Column<T> column;
    private Class<T[]> type;

    private ARRAY(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.column = null;
    }

    public ARRAY(final Class<? extends Column<T>> type) {
      this(null, true, null, null, false, null, true, null, null, null, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, true, null, null, false, null, true, value, null, null, (Class<? extends Column<T>>)value.getClass().getComponentType());
    }

    ARRAY(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.column = null;
    }

    @SuppressWarnings("unchecked")
    ARRAY(final Table owner, final boolean mutable, final ARRAY<T> copy) {
      this(owner, mutable, copy.name, copy.primaryIndexType, copy.isKeyForUpdate, copy.onModify, copy.isNullable, copy.valueCur, copy.generateOnInsert, copy.generateOnUpdate, (Class<? extends Column<T>>)copy.column.getClass());
      this.type = copy.type;
    }

    ARRAY(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final Class<? extends Column<T>> type) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      this.column = newInstance(Classes.getDeclaredConstructor(type));
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<>((Class<? extends Column<T>>)column.getClass());
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileArray(b, this, column, isForUpdateWhere);
    }

    final void copy(final ARRAY<T> copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final T[] valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      // FIXME
      throw new UnsupportedOperationException();
    }

    @Override
    final boolean equal(final T[] a, final T[] b) {
      return Arrays.equals(a, b);
    }

    @Override
    final boolean equals(final Column<T[]> obj) {
      throw new UnsupportedOperationException("FIXME");
    }

    @Override
    final DiscreteTopology<T[]> getDiscreteTopology() {
      throw new UnsupportedOperationException();
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
      this.valueOld = this.valueCur; // FIXME: This is incorrect.
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unused")
    public final ARRAY<T> set(final NULL value) {
      super.setNull();
      return this;
    }

    public final ARRAY<T> set(final type.ARRAY<T> value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.ARRAY;
    }

    @Override
    public final String toString() {
      throw new UnsupportedOperationException("FIXME");
    }

    @Override
    @SuppressWarnings("unchecked")
    final Class<T[]> type() {
      return type == null ? type = (Class<T[]>)Array.newInstance(column.type(), 0).getClass() : type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
    }

    @Override
    final int valueHashCode() {
      return Arrays.hashCode(valueCur);
    }

    @Override
    @SuppressWarnings("unchecked")
    final ARRAY<T> wrap(final Evaluable wrapped) {
      return (ARRAY<T>)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setArray(parameterIndex, new SQLArray<>(this)); // FIXME: isForUpdateWhere
    }
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
    private Long valueObjOld;
    private Long valueObjCur;

    public BIGINT() {
      this(true);
    }

    private BIGINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    public BIGINT(final int precision) {
      this(null, true, precision);
    }

    public BIGINT(final Integer precision) {
      this(null, true, precision);
    }

    public BIGINT(final long value) {
      this(Numbers.precision(value));
      set(value);
    }

    public BIGINT(final Long value) {
      this(value == null ? null : Integer.valueOf(Numbers.precision(value)));
      if (value != null)
        set(value);
    }

    private BIGINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    BIGINT(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    BIGINT(final Table owner, final boolean mutable, final BIGINT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    BIGINT(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final Integer precision, final Long min, final Long max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final long value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final BIGINT clone() {
      return new BIGINT(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final BIGINT copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final long valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt64(b, Numbers.cast(precision(), Byte.class), min);
    }

    @Override
    public final Long get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Long get(final Long defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Long> getDiscreteTopology() {
      return DiscreteTopologies.LONG;
    }

    @Override
    final Long getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Long max() {
      return max;
    }

    @Override
    final int maxPrecision() {
      return 19;
    }

    @Override
    final Long maxValue() {
      return 9223372036854775807L;
    }

    @Override
    public final Long min() {
      return min;
    }

    @Override
    final Long minValue() {
      return -9223372036854775808L;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    final Integer scale() {
      return 0;
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

    public final boolean set(final long value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final long value, final Long valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Long value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Long value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final BIGINT set(final NULL value) {
      setNull();
      return this;
    }

    public final BIGINT set(final type.BIGINT value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final long value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final long value, final Long valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;

      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Long.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Long value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.BIGINT;
    }

    @Override
    final Class<Long> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateLong(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Long.hashCode(valueCur);
    }

    @Override
    final BIGINT wrap(final Evaluable wrapped) {
      return (BIGINT)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setLong(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
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

    private BINARY(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.length = 0;
      this.varying = false;
    }

    public BINARY(final byte[] value) {
      this(value.length, false);
      set(value);
    }

    public BINARY(final long length) {
      this(length, false);
    }

    public BINARY(final long length, final boolean varying) {
      super(null, true, (OnModify<?>)null);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    BINARY(final long length, final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.length = length;
      this.varying = false;
    }

    BINARY(final Table owner, final boolean mutable, final BINARY copy) {
      super(owner, mutable, copy);
      this.length = copy.length;
      this.varying = copy.varying;
    }

    BINARY(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final byte[] _default, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    private void checkLength(final long length) {
      if (length <= 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " illegal length: " + length);
    }

    @Override
    public final BINARY clone() {
      return new BINARY(getTable(), true, this);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final BINARY copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final byte[] valueCur = copy.valueCur;
      this.changed = !Arrays.equals(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileBinary(b, varying, length);
    }

    @Override
    final boolean equal(final byte[] a, final byte[] b) {
      return Arrays.equals(a, b);
    }

    @Override
    final boolean equals(final Column<byte[]> obj) {
      return equal(valueCur, ((BINARY)obj).valueCur);
    }

    @Override
    final DiscreteTopology<byte[]> getDiscreteTopology() {
      return DiscreteTopologies.BYTES;
    }

    public final long length() {
      return length;
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
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BINARY)
        return new BINARY(SafeMath.max(length(), ((BINARY)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final BINARY set(final NULL value) {
      super.setNull();
      return this;
    }

    public final BINARY set(final type.BINARY value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      // FIXME: Does it matter if we know if this is BIT, BINARY, VARBINARY, or LONGVARBINARY?
      return varying ? Types.VARBINARY : Types.BINARY;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Hexadecimal.encode(valueCur);
    }

    @Override
    final Class<byte[]> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBytes(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Arrays.hashCode(valueCur);
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    final BINARY wrap(final Evaluable wrapped) {
      return (BINARY)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBytes(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  public static class BLOB extends LargeObject<InputStream> implements type.BLOB {
    // FIXME: Is this a bad pattern? Read the full stream just to get toString()?
    private final class BlobInputStream extends DelegateInputStream {
      private final byte[] buf;
      private String string;

      private BlobInputStream(final byte[] buf) {
        in = new ByteArrayInputStream(buf);
        this.buf = buf;
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
        return string == null ? string = Hexadecimal.encode(buf) : string;
      }
    }

    public static final class NULL extends BLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();
    private static final Class<InputStream> type = InputStream.class;

    public BLOB() {
      this(true);
    }

    private BLOB(final boolean mutable) {
      super(null, mutable, (Long)null, null);
    }

    public BLOB(final InputStream value) {
      super(null, true, (Long)null, null);
      set(value);
    }

    public BLOB(final long length) {
      super(null, true, length, null);
    }

    BLOB(final OnModify<? extends Table> onModify) {
      super(null, true, null, onModify);
    }

    BLOB(final Table owner, final boolean mutable, final BLOB copy) {
      super(owner, mutable, copy);
    }

    BLOB(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final InputStream _default, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, length);
    }

    @Override
    public final BLOB clone() {
      return new BLOB(getTable(), true, this);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final BLOB copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final InputStream valueCur = copy.valueCur;
      this.changed = valueOld != valueCur;
      final boolean changed = this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileBlob(b, length());
    }

    // FIXME: Warning! Calling equals will result in the underlying stream to be fully read
    @Override
    final boolean equals(final Column<InputStream> obj) {
      final BLOB that = (BLOB)obj;
      initBlobInputStream();
      that.initBlobInputStream();
      return equal(valueCur, that.valueCur);
    }

    @Override
    final DiscreteTopology<InputStream> getDiscreteTopology() {
      throw new UnsupportedOperationException();
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
    final InputStream parseString(final DbVendor vendor, final String s) {
      return new ByteArrayInputStream(s.getBytes());
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BLOB)
        return new BLOB(SafeMath.max(length(), ((BLOB)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final BLOB set(final NULL value) {
      super.setNull();
      return this;
    }

    public final BLOB set(final type.BLOB value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.BLOB;
    }

    @Override
    public final String toString() {
      if (isNull())
        return "NULL";

      initBlobInputStream();
      return valueCur.toString();
    }

    @Override
    final Class<InputStream> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    // FIXME: Warning! Calling hashCode will result in the underlying stream to be fully read
    @Override
    final int valueHashCode() {
      initBlobInputStream();
      return valueCur.hashCode();
    }

    @Override
    final BLOB wrap(final Evaluable wrapped) {
      return (BLOB)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
  }

  public static class BOOLEAN extends Condition<Boolean> implements type.BOOLEAN, Comparable<Column<Boolean>> {
    public static final class NULL extends BOOLEAN implements data.NULL {
      private NULL() {
        super((Class<BOOLEAN>)null);
      }
    }

    public static final NULL NULL = new NULL();

    public static final BOOLEAN TRUE = new BOOLEAN(true, Boolean.TRUE);
    public static final BOOLEAN FALSE = new BOOLEAN(false, Boolean.FALSE);

    private static final Class<Boolean> type = Boolean.class;

    private boolean isNullOld = true;
    private boolean isNullCur = true;
    private boolean valueOld;
    private boolean valueCur;
    private Boolean valueObjOld;
    private Boolean valueObjCur;

    public BOOLEAN() {
      super(null, true, (OnModify<?>)null);
    }

    public BOOLEAN(final boolean value) {
      super(null, true, (OnModify<?>)null);
      set(value);
    }

    public BOOLEAN(final Boolean value) {
      super(null, true, (OnModify<?>)null);
      if (value != null)
        set(value);
    }

    private BOOLEAN(final boolean value, final Boolean valueObj) {
      super(null, false, (OnModify<?>)null);
      this.isNullCur = false;
      this.isNullOld = false;
      this.valueCur = value;
      this.valueOld = value;
      this.valueObjCur = valueObj;
      this.valueObjOld = valueObj;
      this.setByCur = SetBy.SYSTEM;
      this.setByOld = SetBy.SYSTEM;
    }

    @SuppressWarnings("unused")
    private BOOLEAN(final Class<BOOLEAN> cls) {
      super(null, false, (OnModify<?>)null);
    }

    BOOLEAN(final Table owner) {
      super(owner, true, (OnModify<?>)null);
    }

    BOOLEAN(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
    }

    BOOLEAN(final Table owner, final boolean mutable, final BOOLEAN copy) {
      super(owner, mutable, copy);

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    BOOLEAN(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Boolean _default, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(getTable(), true, this);
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
    StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final BOOLEAN copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final boolean valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareBoolean(b);
    }

    @Override
    final boolean equals(final Column<Boolean> that) {
      return getAsBoolean() == ((BOOLEAN)that).getAsBoolean();
    }

    @Override
    public final Boolean get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Boolean get(final Boolean defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Boolean> getDiscreteTopology() {
      return DiscreteTopologies.BOOLEAN;
    }

    @Override
    final Boolean getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = !(this.isNullOld = this.isNullCur = resultSet.wasNull()) && value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    public final boolean set(final boolean value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final boolean value, final Boolean valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Boolean value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Boolean value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final BOOLEAN set(final NULL value) {
      setNull();
      return this;
    }

    public final BOOLEAN set(final type.BOOLEAN value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final boolean value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    @Override
    public final boolean setIfNotEqual(final Boolean value) {
      if (!setValue(value))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    @Override
    public final boolean setIfNotNull(final Boolean value) {
      return value != null && set(value);
    }

    @Override
    public final boolean setIfNotNullOrEqual(final Boolean value) {
      return value != null && setIfNotEqual(value);
    }

    final boolean setValue(final boolean value, final Boolean valueObj) {
      assertMutable();
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Boolean.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Boolean value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.BOOLEAN;
    }

    @Override
    final Class<Boolean> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBoolean(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Boolean.hashCode(valueCur);
    }

    @Override
    final BOOLEAN wrap(final Evaluable wrapped) {
      return (BOOLEAN)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBoolean(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
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

    public CHAR() {
      this(true);
    }

    private CHAR(final boolean mutable) {
      super(null, mutable, null, (OnModify<?>)null);
      this.varying = true;
    }

    public CHAR(final int length) {
      this(length, false);
    }

    public CHAR(final Integer length) {
      this(length, false);
    }

    public CHAR(final long length, final boolean varying) {
      super(null, true, (int)length, null);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final Long length, final boolean varying) {
      super(null, true, Numbers.cast(length, Integer.class), (OnModify<?>)null);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final String value) {
      this(value.length(), true);
      set(value);
    }

    CHAR(final OnModify<? extends Table> onModify) {
      super(null, true, null, onModify);
      this.varying = true;
    }

    CHAR(final Table owner, final boolean mutable, final CHAR copy) {
      super(owner, mutable, copy, copy.length());
      this.varying = copy.varying;
    }

    CHAR(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final String _default, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final long length, final boolean varying) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    final void checkLength(final long length) {
      if (length < 0 || (!varying() && length == 0) || length > 65535)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(getTable(), true, this);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final CHAR copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final String valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      if (length() == null)
        throw new UnsupportedOperationException("Cannot declare a CHAR with null length");

      return vendor.getDialect().compileChar(b, varying, length() == null ? 1L : length());
    }

    @Override
    final DiscreteTopology<String> getDiscreteTopology() {
      return DiscreteTopologies.STRING;
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
      onChange(CUR_OLD);
    }

    @SuppressWarnings("unused")
    public final CHAR set(final NULL value) {
      super.setNull();
      return this;
    }

    public final CHAR set(final type.CHAR value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return varying ? Types.VARCHAR : Types.CHAR;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : valueCur;
    }

    @Override
    final Class<String> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    final CHAR wrap(final Evaluable wrapped) {
      return (CHAR)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
  }

  public static class CLOB extends LargeObject<Reader> implements type.CLOB {
    // FIXME: Is this a bad pattern? Read the full stream just to get toString()?
    private final class ClobReader extends DelegateReader {
      private final String string;

      private ClobReader(final String string) {
        in = new UnsynchronizedStringReader(string);
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

    public static final class NULL extends CLOB implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();
    private static final Class<Reader> type = Reader.class;

    public CLOB() {
      this(true);
    }

    private CLOB(final boolean mutable) {
      super(null, mutable, (Long)null, null);
    }

    public CLOB(final long length) {
      super(null, true, length, null);
    }

    public CLOB(final Reader value) {
      super(null, true, (Long)null, null);
      set(value);
    }

    CLOB(final OnModify<? extends Table> onModify) {
      super(null, true, null, onModify);
    }

    CLOB(final Table owner, final boolean mutable, final CLOB copy) {
      super(owner, mutable, copy);
    }

    CLOB(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Reader _default, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, length);
    }

    @Override
    public final CLOB clone() {
      return new CLOB(getTable(), true, this);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) throws IOException {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final CLOB copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final Reader valueCur = copy.valueCur;
      this.changed = valueOld != valueCur;
      final boolean changed = this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileClob(b, length());
    }

    // FIXME: Warning! Calling equals will result in the underlying stream to be fully read
    @Override
    final boolean equals(final Column<Reader> obj) {
      final CLOB that = (CLOB)obj;
      initClobReader();
      that.initClobReader();
      return valueCur.equals(that.valueCur);
    }

    @Override
    final DiscreteTopology<Reader> getDiscreteTopology() {
      throw new UnsupportedOperationException();
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
    final Reader parseString(final DbVendor vendor, final String s) {
      return new UnsynchronizedStringReader(s);
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof CLOB)
        return new CLOB(SafeMath.max(length(), ((CLOB)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final CLOB set(final NULL value) {
      super.setNull();
      return this;
    }

    public final CLOB set(final type.CLOB value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.CLOB;
    }

    @Override
    public final String toString() {
      if (isNull())
        return "NULL";

      initClobReader();
      return valueCur.toString();
    }

    @Override
    final Class<Reader> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    // FIXME: Warning! Calling hashCode will result in the underlying stream to be fully read
    @Override
    final int valueHashCode() {
      initClobReader();
      return valueCur.hashCode();
    }

    @Override
    final CLOB wrap(final Evaluable wrapped) {
      return (CLOB)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws IOException, SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
  }

  public abstract static class Column<V> extends Entity implements type.Column<V> {
    static enum SetBy {
      USER,
      SYSTEM
    }

    static String getSimpleName(final Class<?> cls) {
      String name = cls.getCanonicalName();
      if (name == null)
        name = cls.getName();

      return name.substring(name.indexOf("data.") + 5).replace('.', ' ');
    }

    final Table table;

    final String name;
    final IndexType primaryIndexType;
    final boolean isKeyForUpdate;
    @SuppressWarnings("rawtypes")
    final OnModify onModify;
    final boolean isNullable;
    final boolean hasDefault;
    final GenerateOn<? super V> generateOnInsert;
    final GenerateOn<? super V> generateOnUpdate;
    int columnIndex;
    type.Column<V> ref;
    SetBy setByOld;
    SetBy setByCur;
    boolean changed;

    Column(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      this(owner, mutable, null, null, false, onModify, true, null, null, null);
    }

    Column(final Table owner, final boolean mutable, final Column<V> copy) {
      super(mutable);
      this.table = owner;
      this.name = copy.name;
      this.primaryIndexType = copy.primaryIndexType;
      this.isNullable = copy.isNullable;
      this.hasDefault = copy.hasDefault;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.isKeyForUpdate = copy.isKeyForUpdate;
      this.onModify = copy.onModify;

      // NOTE: Deliberately not copying ref
      // this.ref = copy.ref;
      this.setByCur = copy.setByCur;
      this.setByOld = copy.setByOld;
      this.changed = copy.changed;
    }

    Column(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(mutable);
      this.table = owner;
      this.name = name;
      this.primaryIndexType = primaryIndexType;
      this.isKeyForUpdate = isKeyForUpdate;
      this.onModify = onModify;
      this.isNullable = isNullable;
      this.hasDefault = _default != null;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
    }

    abstract void _commitEntity$();
    @Override
    public abstract Column<V> clone();
    abstract StringBuilder compile(Compiler compiler, StringBuilder b, boolean isForUpdateWhere) throws IOException;
    abstract StringBuilder declare(StringBuilder b, DbVendor vendor);
    abstract boolean equals(Column<V> that);
    public abstract V get();
    public abstract V get(V defaultValue);
    abstract DiscreteTopology<V> getDiscreteTopology();
    abstract V getOld();
    public abstract boolean isNull();
    abstract boolean isNullOld();
    abstract V parseString(DbVendor vendor, String s);
    abstract void read(Compiler compiler, ResultSet resultSet, int columnIndex) throws SQLException;
    public abstract void revert();
    abstract Column<?> scaleTo(Column<?> column);
    protected abstract boolean set(V value);
    abstract boolean set(V value, SetBy setBy);
    protected abstract boolean setIfNotEqual(V value);
    protected abstract boolean setIfNotNull(V value);
    protected abstract boolean setIfNotNullOrEqual(V value);
    abstract boolean setValue(V value);
    abstract boolean setValueNull();
    abstract int sqlType();
    abstract StringBuilder toJson(StringBuilder b);
    @Override
    public abstract String toString();
    abstract Class<V> type();
    abstract void update(Compiler compiler, ResultSet resultSet, int columnIndex) throws SQLException;
    abstract int valueHashCode();
    abstract void write(Compiler compiler, PreparedStatement statement, boolean isForUpdateWhere, int parameterIndex) throws IOException, SQLException;

    public final <C extends Column<V>> C AS(final C column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    public final <E extends EntityEnum> ENUM<E> AS(final ENUM<E> column) {
      column.wrap(new As<>(this, column));
      return column;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Evaluable wrapped = wrapped();
      if (wrapped == null) {
        final Table table = getTable();
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
          compilation.addParameter(this, false, false);
        }
      }
      else if (!compilation.subCompile(this)) {
        wrapped.compile(compilation, isExpression);
      }
    }

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

    @Override
    Object evaluate(final Set<Evaluable> visited) {
      if (ref == null || visited.contains(this)) {
        final Evaluable wrapped = wrapped();
        return wrapped != null ? wrapped.evaluate(visited) : get();
      }

      visited.add(this);
      return ((Evaluable)ref).evaluate(visited);
    }

    @Override
    final Column<?> getColumn() {
      return this;
    }

    final V getForUpdateWhereGetOld(final boolean isForUpdateWhere) {
      return isForUpdateWhereGetOld(isForUpdateWhere) ? getOld() : get();
    }

    final boolean getForUpdateWhereIsNullOld(final boolean isForUpdateWhere) {
      return isForUpdateWhereGetOld(isForUpdateWhere) ? isNullOld() : isNull();
    }

    /**
     * Returns the name of this {@link Column}.
     *
     * @return The name of this {@link Column}.
     */
    public final String getName() {
      return name;
    }

    @Override
    public final Table getTable() {
      return table;
    }

    @Override
    public final int hashCode() {
      final int hashCode = name.hashCode();
      return isNull() ? hashCode : hashCode ^ valueHashCode();
    }

    final boolean isForUpdateWhereGetOld(final boolean isForUpdateWhere) {
      return isForUpdateWhere && primaryIndexType != null && setByOld != null;
    }

    void onChange() {
    }

    /**
     * Cue {@code this} {@link Column} for its value to be considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and
     * {@code DELETE} statements.
     *
     * @param to The value to which {@code this} {@link Column}'s cue value is to be set.
     * @return {@code true} if the execution of this method resulted in a change to the cue state of {@code this} {@link Column},
     *         otherwise {@code false}.
     */
    public final boolean cue(final boolean to) {
      final boolean changed;
      if (to) {
        changed = setByCur == null;
        // this.changed = false;
        setByCur = SetBy.USER;
      }
      else {
        changed = setByCur != null;
        this.changed = false;
        setByCur = null;
      }

      return changed;
    }

    static final byte CUR = 0b00000001;
    static final byte OLD = 0b00000010;
    static final byte CUR_OLD = 0b00000100;

    final void onChange(final byte curOld) {
      if (primaryIndexType != null) {
        if ((curOld & CUR) != 0)
          table._primaryKeyImmutable$ = null;

        if ((curOld & OLD) != 0 || setByOld == null) // Special case for "setByOld == null", under which condition the column's "old" value is equal to "cur".
          table._primaryKeyOldImmutable$ = null;
      }

      if (onModify != null) {
        if ((curOld & CUR) != 0)
          onModify.changeCur(table);

        if ((curOld & OLD) != 0)
          onModify.changeOld(table);
      }
    }

    public final boolean revert(final boolean andCue) {
      revert();
      return cue(andCue);
    }

    final void set(final type.Column<V> ref) {
      assertMutable();
      if (this.ref != ref) {
        this.ref = ref;
        this.setByCur = null;
      }
    }

    final boolean setFromString(final DbVendor vendor, final String value, final SetBy setBy) {
      assertMutable();
      return set(value == null ? null : parseString(vendor, value), setBy);
    }

    final boolean setNull() {
      final boolean changed = setValueNull();
      this.ref = null;
      this.setByCur = SetBy.USER;
      return changed;
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
      assertMutable();
      if (rows.getConcurrency() == Concurrency.READ_ONLY)
        throw new IllegalStateException(rows.getConcurrency().getClass().getSimpleName() + "." + rows.getConcurrency());

      update(Compiler.getCompiler(DbVendor.valueOf(rows.resultSet.getStatement().getConnection().getMetaData())), rows.resultSet, columnIndex);
    }

    /**
     * Returns {@code true} if this {@link Column}'s value was cued (by {@link SetBy#USER user}) to be considered in {@code SELECT},
     * {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     *
     * @return {@code true} if this {@link Column}'s value was cued (by {@link SetBy#USER user}) to be considered in {@code SELECT},
     *         {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     */
    public final boolean cuedByUser() {
      return setByCur == SetBy.USER;
    }

    /**
     * Returns {@code true} if this {@link Column}'s value was cued (by {@link SetBy#SYSTEM system}) to be considered in {@code SELECT},
     * {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     *
     * @return {@code true} if this {@link Column}'s value was cued (by {@link SetBy#SYSTEM system}) to be considered in {@code SELECT},
     *         {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     */
    public final boolean cuedBySystem() {
      return setByCur == SetBy.SYSTEM;
    }

    /**
     * Returns {@code true} if this {@link Column}'s value was cued (by the {@link SetBy#USER user} or the {@link SetBy#SYSTEM system})
     * to be considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     *
     * @return {@code true} if this {@link Column}'s value was cued (by the {@link SetBy#USER user} or the {@link SetBy#SYSTEM system})
     *         to be considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise
     *         {@code false}.
     */
    public final boolean cued() {
      return setByCur != null;
    }
  }

  public static class DATE extends Temporal<LocalDate> implements type.DATE {
    public static final class NULL extends DATE implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();
    private static final Class<LocalDate> type = LocalDate.class;

    public DATE() {
      super(null, true, (OnModify<?>)null);
    }

    private DATE(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
    }

    public DATE(final LocalDate value) {
      super(null, true, (OnModify<?>)null);
      set(value);
    }

    DATE(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
    }

    DATE(final Table owner, final boolean mutable, final DATE copy) {
      super(owner, mutable, copy);
    }

    DATE(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final LocalDate _default, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final DATE copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final LocalDate valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDate(b);
    }

    @Override
    final DiscreteTopology<LocalDate> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_DATE;
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
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final DATE set(final NULL value) {
      super.setNull();
      return this;
    }

    public final DATE set(final type.DATE value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.DATE;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateToString(valueCur);
    }

    @Override
    final Class<LocalDate> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final DATE wrap(final Evaluable wrapped) {
      return (DATE)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
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

    public DATETIME() {
      this(true);
    }

    private DATETIME(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.precision = null;
    }

    public DATETIME(final int precision) {
      super(null, true, (OnModify<?>)null);
      this.precision = (byte)precision;
    }

    public DATETIME(final Integer precision) {
      super(null, true, (OnModify<?>)null);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public DATETIME(final LocalDateTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    DATETIME(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.precision = null;
    }

    DATETIME(final Table owner, final boolean mutable, final DATETIME copy) {
      super(owner, mutable, copy);
      this.precision = copy.precision;
    }

    DATETIME(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      this.precision = precision == null ? null : precision.byteValue();
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final DATETIME copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final LocalDateTime valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDateTime(b, precision);
    }

    @Override
    final DiscreteTopology<LocalDateTime> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_DATE_TIME[precision];
    }

    @Override
    final LocalDateTime parseString(final DbVendor vendor, final String s) {
      return Dialect.dateTimeFromString(s);
    }

    public final Byte precision() {
      return precision;
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof DATETIME)
        return new DATETIME(SafeMath.max(precision(), ((DATETIME)column).precision()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final DATETIME set(final NULL value) {
      super.setNull();
      return this;
    }

    public final DATETIME set(final type.DATETIME value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.dateTimeToString(valueCur);
    }

    @Override
    final Class<LocalDateTime> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final DATETIME wrap(final Evaluable wrapped) {
      return (DATETIME)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
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

    public DECIMAL() {
      this(true);
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

    private DECIMAL(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.scale = null;
      this.min = null;
      this.max = null;
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

    DECIMAL(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    DECIMAL(final Table owner, final boolean mutable, final DECIMAL copy) {
      super(owner, mutable, copy, copy.precision);

      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;
    }

    DECIMAL(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = _default;
      }

      checkScale(precision, scale);
      this.scale = scale;
      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkScale(final int precision, final int scale) {
      if (precision < scale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");

      if (scale > maxScale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
    }

    private void checkValue(final BigDecimal value) {
      if (min != null && value.compareTo(min) < 0 || max != null && max.compareTo(value) < 0)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final DECIMAL copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final BigDecimal valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDecimal(b, precision(), scale(), min);
    }

    @Override
    final boolean equal(final BigDecimal a, final BigDecimal b) {
      return a == b || a != null && b != null && a.compareTo(b) == 0;
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
    final DiscreteTopology<BigDecimal> getDiscreteTopology() {
      return DiscreteTopologies.BIG_DECIMAL(scale);
    }

    @Override
    final BigDecimal getOld() {
      return setByOld == null ? get() : isNullOld() ? null : valueOld;
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
    public final BigDecimal max() {
      return max;
    }

    @Override
    final int maxPrecision() {
      return -1;
    }

    @Override
    final BigDecimal maxValue() {
      return null;
    }

    @Override
    public final BigDecimal min() {
      return min;
    }

    @Override
    final BigDecimal minValue() {
      return null;
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
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    public final Integer scale() {
      return scale;
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
    public final boolean set(final BigDecimal value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final BigDecimal value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @SuppressWarnings("unused")
    public final DECIMAL set(final NULL value) {
      setNull();
      return this;
    }

    public final DECIMAL set(final type.DECIMAL value) {
      super.set(value);
      return this;
    }

    @Override
    boolean setValue(final BigDecimal value) {
      assertMutable();
      final boolean changed = !equal(valueCur, value);
      if (!changed)
        return false;

      if (value != null)
        checkValue(value);

      this.changed = !equal(valueOld, value);
      this.valueCur = value;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = valueCur != null;
      if (!changed)
        return false;

      this.changed = valueOld != null;
      this.valueCur = null;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.DECIMAL;
    }

    @Override
    final StringBuilder toJson(final StringBuilder b) {
      return b.append(isNull() ? "null" : valueCur.toString());
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : valueCur.toString();
    }

    @Override
    final Class<BigDecimal> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateBigDecimal(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }

    @Override
    final DECIMAL wrap(final Evaluable wrapped) {
      return (DECIMAL)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setBigDecimal(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
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
    private Double valueObjOld;
    private Double valueObjCur;

    public DOUBLE() {
      this(true);
    }

    private DOUBLE(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.min = null;
      this.max = null;
    }

    public DOUBLE(final double value) {
      this();
      set(value);
    }

    public DOUBLE(final Double value) {
      this();
      if (value != null)
        set(value);
    }

    DOUBLE(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    DOUBLE(final Table owner, final boolean mutable, final DOUBLE copy) {
      super(owner, mutable, copy);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    DOUBLE(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final Double min, final Double max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final double value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final DOUBLE clone() {
      return new DOUBLE(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final DOUBLE copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final double valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareDouble(b, min);
    }

    @Override
    public final Double get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Double get(final Double defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Double> getDiscreteTopology() {
      return DiscreteTopologies.DOUBLE;
    }

    @Override
    final Double getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Double max() {
      return max;
    }

    @Override
    public final Double min() {
      return min;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? Double.NaN : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
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

    public final boolean set(final double value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final double value, final Double valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Double value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Double value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final DOUBLE set(final NULL value) {
      setNull();
      return this;
    }

    public final DOUBLE set(final type.DOUBLE value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final double value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final double value, final Double valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Double.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Double value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.DOUBLE;
    }

    @Override
    final Class<Double> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateDouble(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Double.hashCode(valueCur);
    }

    @Override
    final DOUBLE wrap(final Evaluable wrapped) {
      return (DOUBLE)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setDouble(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  public abstract static class Entity extends Evaluable implements type.Entity {
    final boolean _mutable$;
    private Evaluable wrapped;

    protected Entity(final boolean mutable) {
      this._mutable$ = mutable;
    }

    final boolean assertMutable() {
      if (!_mutable$)
        throw new IllegalArgumentException(Classes.getCompositeName(getClass()) + " is not mutable");

      return true;
    }

    final void clearWrap() {
      if (wrapped != null) {
        assertMutable();
        wrapped = null;
      }
    }

    final Evaluable original() {
      Entity wrapped = this;
      for (Evaluable next;; wrapped = (Entity)next) { // [X]
        next = wrapped.wrapped();
        if (!(next instanceof Entity))
          return next != null ? next : wrapped;
      }
    }

    Entity wrap(final Evaluable wrapped) {
      assertMutable();
      this.wrapped = wrapped;
      return this;
    }

    final Evaluable wrapped() {
      return wrapped;
    }
  }

  public static class ENUM<E extends EntityEnum> extends Textual<E> implements type.ENUM<E> {
    public static final class NULL extends ENUM<EntityEnum> implements data.NULL {
      private NULL() {
        super(false);
      }
    }

    public static final NULL NULL = new NULL();
    private static final IdentityHashMap<Class<?>,Integer> typeToLength = new IdentityHashMap<>(2);
    private static volatile ConcurrentHashMap<Class<?>,Method> classToFromStringMethod;

    private static int calcEnumLength(final EntityEnum[] constants) {
      final Integer cached = typeToLength.get(constants.getClass().getComponentType());
      if (cached != null)
        return cached;

      int length = 0;
      for (final EntityEnum constant : constants) { // [A]
        final int len = constant.toString().length();
        if (length < len)
          length = len;
      }

      typeToLength.put(constants.getClass().getComponentType(), length);
      return length;
    }

    @SuppressWarnings("unchecked")
    private static <E extends EntityEnum> E[] getConstants(final Class<E> enumType) {
      try {
        return (E[])enumType.getMethod("values").invoke(null);
      }
      catch (final IllegalAccessException | NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
      catch (final InvocationTargetException e) {
        final Throwable cause = e.getCause();
        if (cause instanceof RuntimeException)
          throw (RuntimeException)cause;

        throw new RuntimeException(cause);
      }
    }

    private final Class<E> enumType;
    final E[] constants;
    private final Function<String,E> fromStringFunction;

    private final DiscreteTopology<E> topology = new DiscreteTopology<E>() {
      @Override
      public int compare(final E o1, final E o2) {
        return Integer.compare(o1.ordinal(), o2.ordinal());
      }

      @Override
      public boolean isMaxValue(final E v) {
        return constants[constants.length - 1].ordinal() == v.ordinal();
      }

      @Override
      public boolean isMinValue(final E v) {
        return constants[0].ordinal() == v.ordinal();
      }

      @Override
      public E nextValue(final E v) {
        return isMaxValue(v) ? v : constants[v.ordinal() + 1];
      }

      @Override
      public E prevValue(final E v) {
        return isMinValue(v) ? v : constants[v.ordinal() - 1];
      }
    };

    private ENUM(final boolean mutable) {
      super(null, mutable, null, (OnModify<?>)null);
      this.enumType = null;
      this.constants = null;
      this.fromStringFunction = null;
    }

    public ENUM(final Class<E> enumType) {
      this(enumType, getConstants(enumType), null);
    }

    @SuppressWarnings("unchecked")
    private ENUM(final Class<E> enumType, final E[] constants, final OnModify<? extends Table> onModify) {
      super(null, true, constants == null ? null : calcEnumLength(constants), onModify);
      this.enumType = enumType;
      this.constants = constants;
      this.fromStringFunction = enumType == null ? null : (final String s) -> {
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
          final Throwable cause = e.getCause();
          if (cause instanceof RuntimeException)
            throw (RuntimeException)cause;

          throw new RuntimeException(cause);
        }
      };
    }

    @SuppressWarnings("unchecked")
    public ENUM(final E value) {
      this(value == null ? null : (Class<E>)value.getClass());
      set(value);
    }

    ENUM(final Class<E> enumType, final OnModify<? extends Table> onModify) {
      this(enumType, getConstants(enumType), onModify);
    }

    ENUM(final Table owner, final boolean mutable, final ENUM<E> copy) {
      super(owner, mutable, copy, copy.length());
      this.enumType = copy.enumType;
      this.constants = copy.constants;
      this.fromStringFunction = copy.fromStringFunction;
    }

    @SuppressWarnings("unchecked")
    ENUM(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final E _default, final GenerateOn<? super E> generateOnInsert, final GenerateOn<? super E> generateOnUpdate, final E[] constants, final Function<String,E> fromStringFunction) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, calcEnumLength(constants));
      this.enumType = (Class<E>)constants.getClass().getComponentType();
      this.constants = constants;
      this.fromStringFunction = fromStringFunction;
    }

    @Override
    public final ENUM<E> clone() {
      return new ENUM<>(getTable(), true, this);
    }

    @Override
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final ENUM<E> copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final E valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      throw new UnsupportedOperationException();
    }

    @Override
    final String evaluate(final Set<Evaluable> visited) {
      return isNull() ? null : valueCur.toString();
    }

    public final String getAsString() {
      return valueCur == null ? null : valueCur.toString();
    }

    @Override
    final DiscreteTopology<E> getDiscreteTopology() {
      return topology;
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
        onChange(CUR_OLD);
        return;
      }

      for (final E constant : constants) { // [A]
        if (constant.toString().equals(value)) {
          this.valueOld = this.valueCur = constant;
          this.setByOld = this.setByCur = SetBy.SYSTEM;
          onChange(CUR_OLD);
          return;
        }
      }

      throw new IllegalArgumentException("Unknown enum value: " + value);
    }

    @SuppressWarnings("unused")
    public final ENUM<E> set(final NULL value) {
      super.setNull();
      return this;
    }

    public final ENUM<E> set(final type.ENUM<E> value) {
      super.set(value);
      return this;
    }

    public final boolean setFromString(final String value) {
      return set(value == null ? null : fromStringFunction.apply(value));
    }

    @Override
    final int sqlType() {
      return Types.CHAR;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : get().toString();
    }

    @Override
    final Class<E> type() {
      return enumType;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNull())
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateObject(columnIndex, valueCur.toString());
    }

    @Override
    @SuppressWarnings("unchecked")
    final ENUM<E> wrap(final Evaluable wrapped) {
      return (ENUM<E>)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setObject(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld.toString() : valueCur.toString());
    }
  }

  public abstract static class ExactNumeric<V extends Number> extends Numeric<V> implements type.ExactNumeric<V> {
    final Integer precision;

    ExactNumeric(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
      this.precision = null;
    }

    ExactNumeric(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, (OnModify<?>)null);
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

    ExactNumeric(final Table owner, final boolean mutable, final Numeric<V> copy, final Integer precision) {
      super(owner, mutable, copy);
      checkPrecision(precision);
      this.precision = precision;
    }

    ExactNumeric(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      checkPrecision(precision);
      this.precision = precision;
    }

    abstract int maxPrecision();
    abstract V maxValue();
    abstract V minValue();
    abstract Integer scale();

    private void checkPrecision(final Integer precision) {
      if (precision != null && maxPrecision() != -1 && precision > maxPrecision())
        throw new IllegalArgumentException(getSimpleName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }

    public final Integer precision() {
      return precision;
    }
  }

  public enum Except {
    PRIMARY_KEY,
    PRIMARY_KEY_FOR_UPDATE
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
    private Float valueObjOld;
    private Float valueObjCur;

    public FLOAT() {
      this(true);
    }

    private FLOAT(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.min = null;
      this.max = null;
    }

    public FLOAT(final float value) {
      this();
      set(value);
    }

    public FLOAT(final Float value) {
      this();
      if (value != null)
        set(value);
    }

    FLOAT(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    FLOAT(final Table owner, final boolean mutable, final FLOAT copy) {
      super(owner, mutable, copy);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    FLOAT(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final Float min, final Float max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final float value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final FLOAT copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final float valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareFloat(b, min);
    }

    @Override
    public final Float get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Float get(final Float defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Float> getDiscreteTopology() {
      return DiscreteTopologies.FLOAT;
    }

    @Override
    final Float getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Float max() {
      return max;
    }

    @Override
    public final Float min() {
      return min;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? Float.NaN : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
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

    public final boolean set(final float value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final float value, final Float valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Float value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Float value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final FLOAT set(final NULL value) {
      setNull();
      return this;
    }

    public final FLOAT set(final type.FLOAT value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final float value) {
      if (!setValue(value))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final float value, final Float valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Float.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Float value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.FLOAT;
    }

    @Override
    final Class<Float> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateFloat(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Float.hashCode(valueCur);
    }

    @Override
    final FLOAT wrap(final Evaluable wrapped) {
      return (FLOAT)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setFloat(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  static final class IndexType extends Condition.Identity {
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
    private Integer valueObjOld;
    private Integer valueObjCur;

    public INT() {
      this(true);
    }

    private INT(final boolean mutable) {
      this(null, mutable, (Short)null);
    }

    public INT(final int value) {
      this(Numbers.precision(value));
      set(value);
    }

    public INT(final Integer value) {
      this(value == null ? null : (short)Numbers.precision(value));
      if (value != null)
        set(value);
    }

    public INT(final short precision) {
      super(null, true, (int)precision);
      this.min = null;
      this.max = null;
    }

    public INT(final Short precision) {
      this(null, true, precision);
    }

    INT(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    INT(final Table owner, final boolean mutable, final INT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    private INT(final Table owner, final boolean mutable, final Short precision) {
      super(owner, mutable, precision == null ? null : precision.intValue());
      this.min = null;
      this.max = null;
    }

    INT(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final Integer precision, final Integer min, final Integer max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final int value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final INT clone() {
      return new INT(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final INT copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final int valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt32(b, Numbers.cast(precision(), Byte.class), min);
    }

    @Override
    public final Integer get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Integer get(final Integer defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Integer> getDiscreteTopology() {
      return DiscreteTopologies.INTEGER;
    }

    @Override
    final Integer getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Integer max() {
      return max;
    }

    @Override
    final int maxPrecision() {
      return 10;
    }

    @Override
    final Integer maxValue() {
      return 2147483647;
    }

    @Override
    public final Integer min() {
      return min;
    }

    @Override
    final Integer minValue() {
      return -2147483648;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    final Integer scale() {
      return 0;
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

    public final boolean set(final int value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final int value, final Integer valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Integer value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Integer value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final INT set(final NULL value) {
      setNull();
      return this;
    }

    public final INT set(final type.INT value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final int value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final int value, final Integer valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Integer.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Integer value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.INTEGER;
    }

    @Override
    final Class<Integer> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Integer.hashCode(valueCur);
    }

    @Override
    final INT wrap(final Evaluable wrapped) {
      return (INT)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setInt(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  public static class Key extends Interval<Key> implements type.Key {
    static final Key ALL = new Key(null) {
      @Override
      public int compareTo(final Interval<Key> o) {
        return -1;
      }

      @Override
      public Key getMax() {
        return null;
      }
    };

    static {
      ALL.min = null;
    }

    private static final ARRAY<?>[] _array = {ARRAY()};
    private static final BIGINT[] _bigint = {BIGINT()};
    private static final BINARY[] _binary = {BINARY()};
    private static final BLOB[] _blob = {BLOB()};
    private static final BOOLEAN[] _boolean = {BOOLEAN()};
    private static final CHAR[] _char = {CHAR()};
    private static final CLOB[] _clob = {CLOB()};
    private static final DATE[] _date = {DATE()};
    private static final DATETIME[] _datetime = {DATETIME()};
    private static final DECIMAL[] _decimal = {DECIMAL()};
    private static final DOUBLE[] _double = {DOUBLE()};
    private static final ENUM<?>[] _enum = {ENUM()};
    private static final FLOAT[] _float = {FLOAT()};
    private static final INT[] _int = {INT()};
    private static final SMALLINT[] _smallint = {SMALLINT()};
    private static final TIME[] _time = {TIME()};
    private static final TINYINT[] _tinyint = {TINYINT()};

    static MutableKey cur(final Column<?>[] columns) {
      return new MutableKey(columns) {
        @Override
        public Key immutable() {
          final Object[] values = new Object[columns.length];
          for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
            values[i] = columns[i].get();

          return new Key(columns, values);
        }

        @Override
        public Object value(final int i) {
          return columns[i].get();
        }
      };
    }

    static MutableKey old(final Column<?>[] columns) {
      return new MutableKey(columns) {
        @Override
        public Key immutable() {
          final Object[] values = new Object[columns.length];
          for (int i = 0, i$ = values.length; i < i$; ++i) // [A]
            values[i] = columns[i].getOld();

          return new Key(columns, values);
        }

        @Override
        public Object value(final int i) {
          return columns[i].getOld();
        }
      };
    }

    public static Key with(final BigDecimal value) {
      return new Key(_decimal, value);
    }

    public static Key with(final Boolean value) {
      return new Key(_boolean, value);
    }

    public static Key with(final Byte value) {
      return new Key(_tinyint, value);
    }

    public static Key with(final byte[] value) {
      return new Key(_binary, value);
    }

    static Key with(final Column<?>[] columns, final Object ... values) {
      return new Key(columns, values);
    }

    public static Key with(final Double value) {
      return new Key(_double, value);
    }

    public static Key with(final EntityEnum value) {
      return new Key(_enum, value);
    }

    public static Key with(final Float value) {
      return new Key(_float, value);
    }

    public static Key with(final InputStream value) {
      return new Key(_blob, value);
    }

    public static Key with(final Integer value) {
      return new Key(_int, value);
    }

    public static Key with(final LocalDate value) {
      return new Key(_date, value);
    }

    public static Key with(final LocalDateTime value) {
      return new Key(_datetime, value);
    }

    public static Key with(final LocalTime value) {
      return new Key(_time, value);
    }

    public static Key with(final Long value) {
      return new Key(_bigint, value);
    }

    public static Key with(final Object[] value) {
      return new Key(_array, value);
    }

    public static Key with(final Reader value) {
      return new Key(_clob, value);
    }

    public static Key with(final Short value) {
      return new Key(_smallint, value);
    }

    public static Key with(final String value) {
      return new Key(_char, value);
    }

    private DiscreteTopology<Object[]> topology;
    private final Object[] values;
    private final Column[] columns;

    private static final Comparator<Key> comparator = (final Key o1, final Key o2) -> o1.compareTo(o2);

    private Key(final Column<?>[] columns, final Object ... values) {
      min = this;
      c = comparator;

      this.columns = columns;
      this.values = values;
    }

    @Override
    public Key getMax() {
      return max == null ? max = next() : max;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compareTo(final Interval<Key> o) {
      if (o == Key.ALL)
        return 1;

      final Key key = (Key)o;
      final int i$ = length();
      if (i$ != key.length())
        throw new IllegalArgumentException("this.length() (" + i$ + ") != that.length() (" + key.length() + ")");

      for (int i = 0; i < i$; ++i) { // [RA]
        final Object a = value(i);
        final Object b = key.value(i);
        if (a == null) {
          if (b == null)
            continue;

          return -1;
        }

        if (b == null)
          return 1;

        if (a.getClass() != b.getClass())
          throw new IllegalArgumentException(a.getClass().getName() + " != " + b.getClass().getName());

        final int c = ((Comparable<Object>)a).compareTo(b);
        if (c != 0)
          return c;
      }

      return 0;
    }

    @Override
    public final Column column(final int i) {
      return columns[i];
    }

    @Override
    public final int length() {
      return values.length;
    }

    final Key next() {
      try {
        return new Key(columns, topology().nextValue(values));
      }
      catch (final Exception e) {
        final StringBuilder b = new StringBuilder();
        for (int i = 0, i$ = columns.length; i < i$; ++i) // [A]
          b.append(columns[i].getName()).append('=').append(values[i]);

        b.append(')').setCharAt(0, '(');
        throw new JSQLException(b.toString(), e);
      }
    }

    final DiscreteTopology<Object[]> topology() {
      return topology == null ? topology = new DiscreteTopology<Object[]>() {
        @Override
        public int compare(final Object[] o1, final Object[] o2) {
          final int len = o1.length;
          if (len < o2.length)
            return -1;

          if (len > o2.length)
            return 1;

          for (int i = 0, c; i < len; ++i) { // [RA]
            c = columns[i].getDiscreteTopology().compare(o1[i], o2[i]);
            if (c != 0)
              return c;
          }

          return 0;
        }

        @Override
        public boolean isMaxValue(final Object[] v) {
          for (int i = 0, i$ = columns.length; i < i$; ++i) // [A]
            if (columns[i].getDiscreteTopology().isMaxValue(v[i]))
              return true;

          return false;
        }

        @Override
        public boolean isMinValue(final Object[] v) {
          for (int i = 0, i$ = columns.length; i < i$; ++i) // [A]
            if (columns[i].getDiscreteTopology().isMinValue(v[i]))
              return true;

          return false;
        }

        @Override
        public Object[] nextValue(final Object[] key) {
          final Object[] next = key.clone();
          Object k, p;
          for (int i = key.length - 1; i >= 0; --i) { // [A]
            k = key[i];
            p = next[i] = columns[i].getDiscreteTopology().nextValue(k);
            if (p == k)
              next[i] = null;
          }

          return next;
        }

        @Override
        public Object[] prevValue(final Object[] key) {
          final Object[] prev = key.clone();
          Object k, p;
          for (int i = key.length - 1; i >= 0; --i) { // [A]
            k = key[i];
            p = prev[i] = columns[i].getDiscreteTopology().prevValue(k);
            if (p == null || p == k)
              prev[i] = null;
          }

          return prev;
        }
      } : topology;
    }

    @Override
    public final Object value(final int i) {
      return values[i];
    }

    @Override
    public final int hashCode() {
      return KeyUtil.hashCode(this);
    }

    @Override
    public final boolean equals(final Object obj) {
      return KeyUtil.equals(this, obj);
    }

    @Override
    public String toString() {
      return KeyUtil.toString(this);
    }
  }

  public abstract static class LargeObject<V extends Closeable> extends Objective<V> implements type.LargeObject<V> {
    private final Long length;

    LargeObject(final Table owner, final boolean mutable, final LargeObject<V> copy) {
      super(owner, mutable, copy);
      this.length = copy.length;
    }

    LargeObject(final Table owner, final boolean mutable, final Long length, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
      this.length = length;
    }

    LargeObject(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final Long length) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
    }

    private void checkLength(final Long length) {
      if (length != null && length <= 0)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " illegal length: " + length);
    }

    public final Long length() {
      return length;
    }
  }

  static abstract class MutableKey implements type.Key {
    private final Column<?>[] columns;

    private MutableKey(final Column<?>[] columns) {
      this.columns = assertNotNull(columns);
    }

    @Override
    public final Column column(final int i) {
      return columns[i];
    }

    abstract Key immutable();

    @Override
    public final int length() {
      return columns.length;
    }

    @Override
    public final int hashCode() {
      return KeyUtil.hashCode(this);
    }

    @Override
    public final boolean equals(final Object obj) {
      return KeyUtil.equals(this, obj);
    }

    @Override
    public String toString() {
      return KeyUtil.toString(this);
    }
  }

  interface NULL {
  }

  public abstract static class Numeric<V extends Number> extends Primitive<V> implements Comparable<Column<? extends Number>>, type.Numeric<V> {
    @SuppressWarnings("unchecked")
    static <T extends Number> T valueOf(final Number number, final Class<T> as) {
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

    Numeric(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
    }

    Numeric(final Table owner, final boolean mutable, final Numeric<V> copy) {
      super(owner, mutable, copy);
    }

    Numeric(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
    }

    public abstract V max();
    public abstract V min();

    @Override
    final boolean equals(final Column<V> obj) {
      return compareTo(obj) == 0;
    }

    @Override
    final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    public final boolean setIfNotEqual(final V value) {
      if (!setValue(value))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    @Override
    public final boolean setIfNotNull(final V value) {
      return value != null && set(value);
    }

    @Override
    public final boolean setIfNotNullOrEqual(final V value) {
      return value != null && setIfNotEqual(value);
    }

    IllegalArgumentException valueRangeExceeded(final Number min, final Number max, final Number value) {
      return new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + (min != null ? min : "") + ", " + (max != null ? max : "") + "] exceeded: " + value);
    }
  }

  public abstract static class Objective<V> extends Column<V> implements type.Objective<V> {
    V valueOld;
    V valueCur;

    Objective(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
    }

    Objective(final Table owner, final boolean mutable, final Objective<V> copy) {
      super(owner, mutable, copy);

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;
    }

    Objective(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      this.valueOld = this.valueCur = _default;
    }

    @Override
    final void _commitEntity$() {
      valueOld = valueCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
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
    final V getOld() {
      return setByOld == null ? get() : isNullOld() ? null : valueOld;
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
    public final void revert() {
      // FIXME: Optimize this to only revert if `changed == true`. But that means it must absolutely be the case that `changed = true`
      // when `valueCur` is modified.
      valueCur = valueOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    public final boolean set(final V value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final V value, final SetBy setBy) {
      final boolean changed = setValue(value);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean setIfNotEqual(final V value) {
      if (!setValue(value))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    @Override
    public final boolean setIfNotNull(final V value) {
      return value != null && set(value);
    }

    @Override
    public final boolean setIfNotNullOrEqual(final V value) {
      return value != null && setIfNotEqual(value);
    }

    @Override
    final boolean setValue(final V value) {
      assertMutable();
      final boolean changed = !equal(valueCur, value);
      if (!changed)
        return false;

      this.changed = !equal(valueOld, value);
      this.valueCur = value;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = valueCur != null;
      if (!changed)
        return false;

      this.changed = valueOld != null;
      this.valueCur = null;

      onChange(CUR);
      return true;
    }

    @Override
    final StringBuilder toJson(final StringBuilder b) {
      return isNull() ? b.append("null") : b.append('"').append(this).append('"');
    }
  }

  public abstract static class Primitive<V> extends Column<V> implements type.Primitive<V> {
    Primitive(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
    }

    Primitive(final Table owner, final boolean mutable, final Primitive<V> copy) {
      super(owner, mutable, copy);
    }

    Primitive(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
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
    private Short valueObjOld;
    private Short valueObjCur;

    public SMALLINT() {
      this(true);
    }

    private SMALLINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    public SMALLINT(final int precision) {
      this(null, true, precision);
    }

    public SMALLINT(final Integer precision) {
      this(null, true, precision);
    }

    public SMALLINT(final short value) {
      this((int)Numbers.precision(value));
      set(value);
    }

    public SMALLINT(final Short value) {
      this(value == null ? null : (int)Numbers.precision(value));
      if (value != null)
        set(value);
    }

    private SMALLINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    SMALLINT(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    SMALLINT(final Table owner, final boolean mutable, final SMALLINT copy) {
      super(owner, mutable, copy, copy.precision);

      this.min = copy.min;
      this.max = copy.max;

      this.isNullOld = copy.isNullOld;
      this.isNullCur = copy.isNullCur;

      this.valueOld = copy.valueOld;
      this.valueCur = copy.valueCur;

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    SMALLINT(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final Integer precision, final Short min, final Short max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
        this.isNullOld = this.isNullCur = false;
      }

      this.min = min;
      this.max = max;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final short value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final SMALLINT copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final short valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt16(b, Numbers.cast(precision(), Byte.class), min);
    }

    @Override
    public final Short get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Short get(final Short defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Short> getDiscreteTopology() {
      return DiscreteTopologies.SHORT;
    }

    @Override
    final Short getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Short max() {
      return max;
    }

    @Override
    final int maxPrecision() {
      return 5;
    }

    @Override
    final Short maxValue() {
      return 32767;
    }

    @Override
    public final Short min() {
      return min;
    }

    @Override
    final Short minValue() {
      return -32768;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    final Integer scale() {
      return 0;
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

    @SuppressWarnings("unused")
    public final SMALLINT set(final NULL value) {
      setNull();
      return this;
    }

    public final boolean set(final short value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final short value, final Short valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Short value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Short value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    public final SMALLINT set(final type.SMALLINT value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final short value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final short value, final Short valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Short.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Short value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.SMALLINT;
    }

    @Override
    final Class<Short> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (isNullCur)
        resultSet.updateNull(columnIndex);
      else
        resultSet.updateInt(columnIndex, valueCur);
    }

    @Override
    final int valueHashCode() {
      return Short.hashCode(valueCur);
    }

    @Override
    final SMALLINT wrap(final Evaluable wrapped) {
      return (SMALLINT)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setShort(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  public abstract static class Table extends Entity implements type.Table {
    protected static final Column<?>[] empty = {};

    final Column<?>[] _column$;
    final Column<?>[] _primary$;
    final Column<?>[] _keyForUpdate$;
    final Column<?>[] _auto$;
    private final MutableKey _primaryKey$;
    private final MutableKey _primaryKeyOld$;
    Key _primaryKeyImmutable$;
    Key _primaryKeyOldImmutable$;

    Table() {
      super(true);
      this._column$ = null;
      this._primary$ = null;
      this._keyForUpdate$ = null;
      this._auto$ = null;
      this._primaryKey$ = null;
      this._primaryKeyOld$ = null;
    }

    Table(final boolean mutable, final boolean _wasSelected$, final Column<?>[] _column$, final Column<?>[] _primary$, final Column<?>[] _keyForUpdate$, final Column<?>[] _auto$) {
      super(mutable);
      this._column$ = _column$;
      this._primary$ = _primary$;
      this._keyForUpdate$ = _keyForUpdate$;
      this._auto$ = _auto$;
      this._primaryKey$ = Key.cur(_primary$);
      this._primaryKeyOld$ = Key.old(_primary$);
    }

    Table(final boolean mutable, final Table copy) {
      super(mutable);
      this._column$ = copy._column$.clone();
      this._primary$ = copy._primary$.clone();
      this._keyForUpdate$ = copy._keyForUpdate$.clone();
      this._auto$ = copy._auto$.clone();
      this._primaryKey$ = Key.cur(_primary$);
      this._primaryKeyOld$ = Key.old(_primary$);
    }

    abstract byte[] _columnIndex$();
    abstract String[] _columnName$();
    @Override
    public abstract Table clone();
    abstract Table clone(boolean _mutable$);
    @Override
    public abstract boolean equals(Object obj);

    /**
     * Returns the table name.
     *
     * @return The table name.
     */
    public abstract String getName();
    @Override
    abstract Schema getSchema();
    @Override
    public abstract int hashCode();
    abstract Table newInstance();
    abstract Table singleton();
    protected abstract void toString(boolean wasCuedOnly, StringBuilder s);

    void _commitDelete$() {
    }

    final void _commitEntity$() {
      for (final Column<?> column : _column$) // [A]
        column._commitEntity$();
    }

    void _commitInsert$() {
    }

    void _commitSelectAll$() {
    }

    @SuppressWarnings("unchecked")
    final void _onModifyUpdate$() {
      for (final Column<?> column : _column$) // [A]
        if (column.changed && column.onModify != null)
          column.onModify.update(this);
    }

    void _initCache$() {
    }

    void _merge$(final Table table, final boolean checkMutable) {
      if (checkMutable)
        assertMutable();
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      compilation.compiler.compile(this, compilation, isExpression);
    }

    void setCacheSelectEntity(final boolean cacheSelectEntity) {
      throw new UnsupportedOperationException();
    }

    @Override
    final Table evaluate(final Set<Evaluable> visited) {
      return this;
    }

    CacheMap<? extends Table> getCache() {
      return singleton().getCache();
    }

    boolean getCacheSelectEntity() {
      return singleton().getCacheSelectEntity();
    }

    @Override
    final Column<?> getColumn() {
      throw new UnsupportedOperationException();
    }

    /**
     * Returns the {@link Column} in {@code this} {@link Table} matching the specified {@code name}, or {@code null} there is no match.
     *
     * @param name The name of the {@link Column}.
     * @return The {@link Column} in {@code this} {@link Table} matching the specified {@code name}, or {@code null} there is no match.
     * @throws NullPointerException If {@code name} is null.
     */
    public final Column<?> getColumn(final String name) {
      final int index = Arrays.binarySearch(_columnName$(), name);
      return index < 0 ? null : _column$[_columnIndex$()[index]];
    }

    /**
     * Returns the {@link Column}s in {@code this} {@link Table}.
     *
     * @return The {@link Column}s in {@code this} {@link Table}.
     */
    public final Column<?>[] getColumns() {
      return _column$;
    }

    public final Key getKey() {
      return _primaryKeyImmutable$ == null ? _primaryKeyImmutable$ = _primaryKey$.immutable() : _primaryKeyImmutable$;
    }

    final Key getKeyOld() {
      return _primaryKeyOldImmutable$ == null ? _primaryKeyOldImmutable$ = _primaryKeyOld$.immutable() : _primaryKeyOldImmutable$;
    }

    @Override
    final Table getTable() {
      return this;
    }

    public final Table merge(final Table table, final boolean andCue) {
      merge(table);
      cue(andCue);
      return this;
    }

    public final Table merge(final Table table) {
      if (table != this)
        _merge$(table, true);

      return this;
    }

    final Table merge$(final Table table) {
      if (table != this)
        _merge$(table, false);

      return this;
    }

    /**
     * Cue all {@link Column}s in {@code this} {@link Table} to the value of {@code to}.
     *
     * @param to The value to which all {@link Column}s in {@code this} {@link Table} are to be cued.
     */
    public final void cue(final boolean to) {
      for (final Column<?> column : _column$) // [A]
        column.cue(to);
    }

    /**
     * Cue all {@link Column}s in {@code this} {@link Table} to the value of {@code to}, except for the {@link Column}s matching the
     * {@link Except} spec provided by {@code except}.
     *
     * @param to The value to which all {@link Column}s in {@code this} {@link Table} are to be cued, except for the {@link Column}s
     *          matching the {@link Except} spec provided by {@code except}.
     * @param except The {@link Except} spec defining which {@link Column}s are to be excepted.
     */
    public final void cue(final boolean to, final Except except) {
      if (except == null) {
        cue(to);
      }
      else if (except == Except.PRIMARY_KEY_FOR_UPDATE) {
        for (final Column<?> column : _column$) // [A]
          if (column.primaryIndexType == null && !column.isKeyForUpdate)
            column.cue(to);
      }
      else if (except == Except.PRIMARY_KEY) {
        for (final Column<?> column : _column$) // [A]
          if (column.primaryIndexType == null)
            column.cue(to);
      }
      else {
        throw new UnsupportedOperationException("Unsupported Except: " + except);
      }
    }

    public final void revert() {
      for (final Column<?> column : getColumns()) // [A]
        column.revert();
    }

    /**
     * Set the provided {@link Map map} specifying the values (parsable by the given {@link DbVendor}) for the named columns in this
     * {@link Table}.
     *
     * @param vendor The {@link DbVendor}.
     * @param map The {@link Map Map&lt;String,String&gt;} specifying the values for the named columns in this {@link Table}.
     * @param setBy The {@link Column.SetBy} value to be used when setting each column.
     * @return A list of column names that were not found (and thus not set) in the table, or {@code null} if all columns were found
     *         (and thus set).
     * @throws NullPointerException If the provided {@link Map map} is null.
     * @throws IllegalArgumentException If this {@link Table} does not define a named column for a key in the {@link Map map}.
     */
    final String[] setColumns(final DbVendor vendor, final Map<String,String> map, final Column.SetBy setBy) {
      return map.size() == 0 ? null : setColumns(vendor, setBy, map.entrySet().iterator(), 0);
    }

    private String[] setColumns(final DbVendor vendor, final Column.SetBy setBy, final Iterator<Map.Entry<String,String>> iterator, final int depth) {
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
     * Returns a JSON string representation of <u>all</u> {@link Column}s in this {@link Table}.
     *
     * @return A JSON string representation of <u>all</u> {@link Column}s in this {@link Table}.
     */
    @Override
    public String toString() {
      return toString(false);
    }

    /**
     * Returns a JSON string representation of <u>only the cued</u> {@link Column}s in this {@link Table}.
     *
     * @return A JSON string representation of <u>only the cued</u> {@link Column}s in this {@link Table}.
     */
    public final String toStringCued() {
      return toString(true);
    }

    protected final String toString(final boolean wasCuedOnly) {
      final StringBuilder s = new StringBuilder();
      toString(wasCuedOnly, s);
      if (s.length() > 0)
        s.setCharAt(0, '{');
      else
        s.append('{');

      return s.append('}').toString();
    }

    /**
     * Returns {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#USER user}) to be
     * considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     *
     * @return {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#USER user}) to be
     *         considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     */
    public final boolean cuedByUser() {
      for (final Column<?> column : _column$) // [A]
        if (column.cuedByUser())
          return true;

      return false;
    }

    /**
     * Returns {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#SYSTEM system}) to be
     * considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     *
     * @return {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#SYSTEM system}) to be
     *         considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE} statements; otherwise {@code false}.
     */
    public final boolean cuedBySystem() {
      for (final Column<?> column : _column$) // [A]
        if (column.cuedBySystem())
          return true;

      return false;
    }

    /**
     * Returns {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#USER user} or the
     * {@link Column.SetBy#SYSTEM system}) to be considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and {@code DELETE}
     * statements; otherwise {@code false}.
     *
     * @return {@code true} if any {@link Column} in this {@link Table} was cued (by the {@link Column.SetBy#USER user} or the
     *         {@link Column.SetBy#SYSTEM system}) to be considered in {@code SELECT}, {@code INSERT}, {@code UPDATE}, and
     *         {@code DELETE} statements; otherwise {@code false}.
     */
    public final boolean cued() {
      for (final Column<?> column : _column$) // [A]
        if (column.cued())
          return true;

      return false;
    }
  }

  public abstract static class Temporal<V extends java.time.temporal.Temporal> extends Objective<V> implements Comparable<Column<? extends java.time.temporal.Temporal>>, type.Temporal<V> {
    Temporal(final Table owner, final boolean mutable, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
    }

    Temporal(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
    }

    Temporal(final Table owner, final boolean mutable, final Temporal<V> copy) {
      super(owner, mutable, copy);
    }

    @Override
    final boolean equals(final Column<V> obj) {
      return compareTo(obj) == 0;
    }

    @Override
    public String toString() {
      return isNull() ? "NULL" : valueCur.toString();
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }
  }

  public abstract static class Textual<V extends CharSequence & Comparable<?>> extends Objective<V> implements type.Textual<V>, Comparable<Textual<?>> {
    private final Integer length;

    Textual(final Table owner, final boolean mutable, final Integer length, final OnModify<? extends Table> onModify) {
      super(owner, mutable, onModify);
      this.length = length;
    }

    Textual(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final V _default, final GenerateOn<? super V> generateOnInsert, final GenerateOn<? super V> generateOnUpdate, final long length) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      this.length = (int)length;
    }

    Textual(final Table owner, final boolean mutable, final Textual<V> copy, final Integer length) {
      super(owner, mutable, copy);
      this.length = length;
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
    String evaluate(final Set<Evaluable> visited) {
      return (String)super.evaluate(visited);
    }

    public final Integer length() {
      return length;
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof Textual)
        return new CHAR(SafeMath.max(length(), ((Textual<?>)column).length()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @Override
    final int valueHashCode() {
      return valueCur.hashCode();
    }
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

    public TIME() {
      this(true);
    }

    private TIME(final boolean mutable) {
      super(null, mutable, (OnModify<?>)null);
      this.precision = null;
    }

    public TIME(final int precision) {
      super(null, true, (OnModify<?>)null);
      this.precision = (byte)precision;
    }

    public TIME(final Integer precision) {
      super(null, true, (OnModify<?>)null);
      this.precision = Numbers.cast(precision, Byte.class);
    }

    public TIME(final LocalTime value) {
      this(Numbers.precision(value.getNano() / FastMath.intE10[Numbers.trailingZeroes(value.getNano())]) + DEFAULT_PRECISION);
      set(value);
    }

    TIME(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.precision = null;
    }

    TIME(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final Integer precision) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate);
      this.precision = precision == null ? null : precision.byteValue();
    }

    TIME(final Table owner, final boolean mutable, final TIME copy) {
      super(owner, mutable, copy);
      this.precision = copy.precision;
    }

    @Override
    public final TIME clone() {
      return new TIME(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final TIME copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final LocalTime valueCur = copy.valueCur;
      this.changed = !equal(valueOld, valueCur);
      final boolean changed = !equal(this.valueCur, valueCur);

      // if (!changed)
      // return;

      this.valueCur = valueCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().declareTime(b, precision);
    }

    @Override
    final DiscreteTopology<LocalTime> getDiscreteTopology() {
      return DiscreteTopologies.LOCAL_TIME[precision];
    }

    @Override
    final LocalTime parseString(final DbVendor vendor, final String s) {
      return Dialect.timeFromString(s);
    }

    public final Byte precision() {
      return precision;
    }

    @Override
    final void read(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      assertMutable();
      this.columnIndex = columnIndex;
      this.valueOld = this.valueCur = compiler.getParameter(this, resultSet, columnIndex);
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    final Column<?> scaleTo(final Column<?> column) {
      if (column instanceof TIME)
        return new DATETIME(SafeMath.max(precision(), ((TIME)column).precision()));

      throw new IllegalArgumentException("data." + getClass().getSimpleName() + " cannot be scaled against data." + column.getClass().getSimpleName());
    }

    @SuppressWarnings("unused")
    public final TIME set(final NULL value) {
      super.setNull();
      return this;
    }

    public final TIME set(final type.TIME value) {
      super.set(value);
      return this;
    }

    @Override
    final int sqlType() {
      return Types.TIME;
    }

    @Override
    public final String toString() {
      return isNull() ? "NULL" : Dialect.timeToString(valueCur);
    }

    @Override
    final Class<LocalTime> type() {
      return type;
    }

    @Override
    final void update(final Compiler compiler, final ResultSet resultSet, final int columnIndex) throws SQLException {
      compiler.updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final TIME wrap(final Evaluable wrapped) {
      return (TIME)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      compiler.setParameter(this, statement, parameterIndex, isForUpdateWhere);
    }
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
    private Byte valueObjOld;
    private Byte valueObjCur;

    public TINYINT() {
      this(true);
    }

    private TINYINT(final boolean mutable) {
      this(null, mutable, (Integer)null);
    }

    public TINYINT(final byte value) {
      this((int)Numbers.precision(value));
      set(value);
    }

    public TINYINT(final Byte value) {
      this(value == null ? null : (int)Numbers.precision(value));
      if (value != null)
        set(value);
    }

    public TINYINT(final int precision) {
      this(null, true, precision);
    }

    public TINYINT(final Integer precision) {
      this(null, true, precision);
    }

    private TINYINT(final Table owner, final boolean mutable, final Integer precision) {
      super(owner, mutable, precision);
      this.min = null;
      this.max = null;
    }

    TINYINT(final OnModify<? extends Table> onModify) {
      super(null, true, onModify);
      this.min = null;
      this.max = null;
    }

    TINYINT(final Table owner, final boolean mutable, final String name, final IndexType primaryIndexType, final boolean isKeyForUpdate, final OnModify<? extends Table> onModify, final boolean isNullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final Integer precision, final Byte min, final Byte max) {
      super(owner, mutable, name, primaryIndexType, isKeyForUpdate, onModify, isNullable, _default, generateOnInsert, generateOnUpdate, precision);
      if (_default != null) {
        checkValue(_default);
        this.valueOld = this.valueCur = this.valueObjOld = this.valueObjCur = _default;
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

      this.valueObjOld = copy.valueObjOld;
      this.valueObjCur = copy.valueObjCur;
    }

    @Override
    final void _commitEntity$() {
      isNullOld = isNullCur;
      valueOld = valueCur;
      valueObjOld = valueObjCur;
      setByOld = setByCur;
      changed = false;
      onChange(OLD);
    }

    private void checkValue(final byte value) {
      if (min != null && value < min || max != null && max < value)
        throw valueRangeExceeded(min, max, value);
    }

    @Override
    public final TINYINT clone() {
      return new TINYINT(getTable(), true, this);
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
    final StringBuilder compile(final Compiler compiler, final StringBuilder b, final boolean isForUpdateWhere) {
      return compiler.compileColumn(b, this, isForUpdateWhere);
    }

    final void copy(final TINYINT copy) {
      // FIXME: Make copy(...) return boolean changed
      // assertMutable();
      final boolean isNullCur = copy.isNullCur;
      final byte valueCur = copy.valueCur;
      this.changed = isNullOld != isNullCur || valueOld != valueCur;
      final boolean changed = this.isNullCur != isNullCur || this.valueCur != valueCur;

      // if (!changed)
      // return;

      this.isNullCur = isNullCur;
      this.valueCur = valueCur;
      this.valueObjCur = copy.valueObjCur;
      this.setByCur = copy.setByCur;

      if (changed)
        onChange(CUR);
    }

    @Override
    final StringBuilder declare(final StringBuilder b, final DbVendor vendor) {
      return vendor.getDialect().compileInt8(b, Numbers.cast(precision(), Byte.class), min);
    }

    @Override
    public final Byte get() {
      return isNullCur ? null : valueObjCur;
    }

    @Override
    public final Byte get(final Byte defaultValue) {
      return isNullCur ? defaultValue : valueObjCur;
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
    final DiscreteTopology<Byte> getDiscreteTopology() {
      return DiscreteTopologies.BYTE;
    }

    @Override
    final Byte getOld() {
      return setByOld == null ? get() : isNullOld ? null : valueObjOld;
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
    public final Byte max() {
      return max;
    }

    @Override
    final int maxPrecision() {
      return 3;
    }

    @Override
    final Byte maxValue() {
      return 127;
    }

    @Override
    public final Byte min() {
      return min;
    }

    @Override
    final Byte minValue() {
      return -128;
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
      this.valueObjOld = this.valueObjCur = this.valueOld = this.valueCur = (this.isNullOld = this.isNullCur = resultSet.wasNull()) ? 0 : value;
      this.setByOld = this.setByCur = SetBy.SYSTEM;
      onChange(CUR_OLD);
    }

    @Override
    public final void revert() {
      isNullCur = isNullOld;
      valueCur = valueOld;
      valueObjCur = valueObjOld;
      setByCur = setByOld;
      changed = false;
      onChange(CUR);
    }

    @Override
    final Integer scale() {
      return 0;
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

    public final boolean set(final byte value) {
      return set(value, value, SetBy.USER);
    }

    final boolean set(final byte value, final Byte valueObj, final SetBy setBy) {
      final boolean changed = setValue(value, valueObj);
      setByCur = setBy;
      return changed;
    }

    @Override
    public final boolean set(final Byte value) {
      return set(value, SetBy.USER);
    }

    @Override
    final boolean set(final Byte value, final SetBy setBy) {
      return value == null ? setNull() : set(value, value, setBy);
    }

    @SuppressWarnings("unused")
    public final TINYINT set(final NULL value) {
      setNull();
      return this;
    }

    public final TINYINT set(final type.TINYINT value) {
      super.set(value);
      return this;
    }

    public final boolean setIfNotEqual(final byte value) {
      if (!setValue(value, null))
        return false;

      this.setByCur = SetBy.USER;
      return true;
    }

    final boolean setValue(final byte value, final Byte valueObj) {
      assertMutable();
      checkValue(value);
      final boolean changed = isNullCur || valueCur != value;
      if (!changed)
        return false;

      this.changed = isNullOld || valueOld != value;
      this.valueCur = value;
      this.valueObjCur = valueObj != null ? valueObj : Byte.valueOf(value);
      this.isNullCur = false;

      onChange(CUR);
      return true;
    }

    @Override
    final boolean setValue(final Byte value) {
      return value == null ? setValueNull() : setValue(value, value);
    }

    @Override
    final boolean setValueNull() {
      assertMutable();
      final boolean changed = !isNullCur;
      if (!changed)
        return false;

      this.changed = !isNullOld;
      this.isNullCur = true;

      onChange(CUR);
      return true;
    }

    @Override
    final int sqlType() {
      return Types.TINYINT;
    }

    @Override
    final Class<Byte> type() {
      return type;
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
    final int valueHashCode() {
      return Byte.hashCode(valueCur);
    }

    @Override
    final TINYINT wrap(final Evaluable wrapped) {
      return (TINYINT)super.wrap(wrapped);
    }

    @Override
    final void write(final Compiler compiler, final PreparedStatement statement, final boolean isForUpdateWhere, final int parameterIndex) throws SQLException {
      if (getForUpdateWhereIsNullOld(isForUpdateWhere))
        statement.setNull(parameterIndex, sqlType());
      else
        statement.setByte(parameterIndex, isForUpdateWhereGetOld(isForUpdateWhere) ? valueOld : valueCur);
    }
  }

  private static final IdentityHashMap<Class<?>,Class<?>> typeToGeneric = new IdentityHashMap<>(17);

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

  static final IndexType BTREE = new IndexType();
  static final IndexType HASH = new IndexType();
  static final Condition.Identity KEY_FOR_UPDATE = new Condition.Identity();

  private static ARRAY<?> $array;
  private static BIGINT $bigint;
  private static BINARY $binary;
  private static BLOB $blob;
  private static BOOLEAN $boolean;
  private static CHAR $char;
  private static CLOB $clob;
  private static DATE $date;
  private static DATETIME $datetime;
  private static DECIMAL $decimal;
  private static DOUBLE $double;
  private static ENUM<?> $enum;
  private static FLOAT $float;
  private static INT $int;
  private static SMALLINT $smallint;
  private static TINYINT $tinyint;
  private static TIME $time;

  public static ARRAY<?> ARRAY() {
    return $array == null ? $array = new ARRAY<>(false) : $array;
  }

  public static BIGINT BIGINT() {
    return $bigint == null ? $bigint = new BIGINT(false) : $bigint;
  }

  public static BINARY BINARY() {
    return $binary == null ? $binary = new BINARY(false) : $binary;
  }

  public static BLOB BLOB() {
    return $blob == null ? $blob = new BLOB(false) : $blob;
  }

  public static BOOLEAN BOOLEAN() {
    return $boolean == null ? $boolean = new BOOLEAN(false) : $boolean;
  }

  public static CHAR CHAR() {
    return $char == null ? $char = new CHAR(false) : $char;
  }

  public static CLOB CLOB() {
    return $clob == null ? $clob = new CLOB(false) : $clob;
  }

  public static DATE DATE() {
    return $date == null ? $date = new DATE(false) : $date;
  }

  public static DATETIME DATETIME() {
    return $datetime == null ? $datetime = new DATETIME(false) : $datetime;
  }

  public static DECIMAL DECIMAL() {
    return $decimal == null ? $decimal = new DECIMAL(false) : $decimal;
  }

  public static DOUBLE DOUBLE() {
    return $double == null ? $double = new DOUBLE(false) : $double;
  }

  public static ENUM<?> ENUM() {
    return $enum == null ? $enum = new ENUM<>(false) : $enum;
  }

  public static FLOAT FLOAT() {
    return $float == null ? $float = new FLOAT(false) : $float;
  }

  public static INT INT() {
    return $int == null ? $int = new INT(false) : $int;
  }

  private static Constructor<?> lookupColumnConstructor(Class<?> genericType) {
    Class<?> cls;
    while ((cls = typeToGeneric.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(cls, genericType);
  }

  private static <T> T newInstance(final Constructor<T> constructor, final Object ... initargs) {
    try {
      return constructor.newInstance(initargs);
    }
    catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
      if (e instanceof InvocationTargetException) {
        final Throwable cause = e.getCause();
        if (cause instanceof RuntimeException)
          throw (RuntimeException)cause;

        if (cause instanceof IOException)
          Throwing.rethrow(cause);
      }

      throw new RuntimeException(e);
    }
  }

  public static SMALLINT SMALLINT() {
    return $smallint == null ? $smallint = new SMALLINT(false) : $smallint;
  }

  public static TIME TIME() {
    return $time == null ? $time = new TIME(false) : $time;
  }

  public static TINYINT TINYINT() {
    return $tinyint == null ? $tinyint = new TINYINT(false) : $tinyint;
  }

  @SuppressWarnings("unchecked")
  private static <E> ARRAY<E> wrap(final E[] value) {
    final ARRAY<E> array;
    if (value.getClass().getComponentType().isEnum())
      array = new ARRAY<>((Class<? extends Column<E>>)value.getClass().getComponentType());
    else
      array = new ARRAY<>((Class<? extends Column<E>>)typeToGeneric.get(value.getClass().getComponentType()));

    array.set(value);
    return array;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static <V,C extends Column<V>> C wrap(final V value) {
    return (C)(EntityEnum.class.isAssignableFrom(value.getClass()) ? new ENUM((EntityEnum)value) : newInstance(lookupColumnConstructor(value.getClass()), value));
  }

  private data() {
  }
}