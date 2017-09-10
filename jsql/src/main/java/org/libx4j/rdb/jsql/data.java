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
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.lib4j.lang.Classes;
import org.lib4j.lang.Numbers;
import org.libx4j.rdb.jsql.kind.Numeric.UNSIGNED;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;

public final class type {
  private static final Map<Class<?>,Class<?>> typeToClass = new HashMap<Class<?>,Class<?>>();

  static {
    typeToClass.put(null, ENUM.class);
    for (final Class<?> cls : type.class.getClasses()) {
      if (!Modifier.isAbstract(cls.getModifiers())) {
        final Type type = Classes.getGenericSuperclasses(cls)[0];
        if (type instanceof Class<?>)
          typeToClass.put((Class<?>)type, cls);
      }
    }
  }

  protected static Constructor<?> lookupDataTypeConstructor(Class<?> genericType) throws NoSuchMethodException {
    Class<?> dataTypeClass;
    while ((dataTypeClass = typeToClass.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return dataTypeClass.getConstructor(genericType);
  }

  public static abstract class ApproxNumeric<T extends Number> extends Numeric<T> implements kind.ApproxNumeric<T> {
    protected ApproxNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    protected ApproxNumeric(final Numeric<T> copy) {
      super(copy);
    }

    protected ApproxNumeric() {
      super();
    }
  }

  protected static final class ARRAY<T> extends DataType<T[]> implements kind.ARRAY<T[]> {
    protected final DataType<T> dataType;

    protected ARRAY(final Entity owner, final String name, final T[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final boolean keyForUpdate, final Class<? extends DataType<T>> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      try {
        this.dataType = type.newInstance();
      }
      catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @SuppressWarnings("unchecked")
    protected ARRAY(final ARRAY<T> copy) {
      this(copy.owner, copy.name, copy.value, copy.unique, copy.primary, copy.nullable, copy.generateOnInsert, copy.generateOnUpdate, copy.keyForUpdate, (Class<? extends DataType<T>>)copy.dataType.getClass());
    }

    public ARRAY(final Class<? extends DataType<T>> type) {
      this(null, null, null, false, false, true, null, null, false, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, null, value, false, false, true, null, null, false, (Class<? extends DataType<T>>)value.getClass().getComponentType());
    }

    public final ARRAY<T> set(final ARRAY<T> value) {
      super.set(value);
      return this;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      // FIXME
      throw new UnsupportedOperationException();
    }

    private Class<T[]> type;

    @Override
    @SuppressWarnings("unchecked")
    protected final Class<T[]> type() {
      return type == null ? type = (Class<T[]>)Array.newInstance(dataType.type(), 0).getClass() : type;
    }

    @Override
    protected final int sqlType() {
      return Types.ARRAY;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setArray(parameterIndex, new SQLArray<T>(this));
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final java.sql.Array array = resultSet.getArray(columnIndex);
      set((T[])array.getArray());
    }

    @Override
    protected final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this, dataType);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      throw new UnsupportedOperationException();
    }

    @Override
    protected final ARRAY<T> wrapper(final Evaluable wrapper) {
      return (ARRAY<T>)super.wrapper(wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<T>((Class<? extends DataType<T>>)dataType.getClass());
    }
  }

  public static final class BIGINT extends ExactNumeric<Long> implements kind.BIGINT {
    public static final class UNSIGNED extends ExactNumeric<BigInteger> implements kind.BIGINT.UNSIGNED {
      protected static final Class<BigInteger> type = BigInteger.class;

      private final BigInteger min;
      private final BigInteger max;

      protected UNSIGNED(final Entity owner, final String name, final BigInteger _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigInteger> generateOnInsert, final GenerateOn<? super BigInteger> generateOnUpdate, final boolean keyForUpdate, final int precision, final BigInteger min, final BigInteger max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final BIGINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      protected UNSIGNED() {
        super(-1);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigInteger value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final UNSIGNED set(final BIGINT.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      public final BigInteger min() {
        return min;
      }

      @Override
      public final BigInteger max() {
        return max;
      }

      @Override
      protected final short scale() {
        return 0;
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final BigInteger minValue() {
        return BigInteger.ZERO;
      }

      @Override
      protected final BigInteger maxValue() {
        return Numbers.Unsigned.UNSIGNED_LONG_MAX_VALUE;
      }

      @Override
      protected final int maxPrecision() {
        return 20;
      }

      @Override
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareInt64(precision(), unsigned());
      }

      @Override
      protected final Class<BigInteger> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.BIGINT;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setObject(parameterIndex, value, sqlType());
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final Object value = resultSet.getObject(columnIndex);
        if (value == null)
          this.value = null;
        else if (value instanceof BigInteger)
          this.value = (BigInteger)value;
        else if (value instanceof BigDecimal)
          this.value = ((BigDecimal)value).toBigInteger();
        else if (value instanceof Long)
          this.value = BigInteger.valueOf((Long)value);
        else if (value instanceof Integer)
          this.value = BigInteger.valueOf((Integer)value);
        else if (value instanceof Double)
          this.value = BigInteger.valueOf(((Double)value).longValue());
        else
          throw new UnsupportedOperationException("Unsupported class for BigInt data type: " + value.getClass().getName());
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), decimal.scale()) : new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        if (dataType instanceof ExactNumeric)
          return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      protected final BigInteger checkValue(final BigInteger value) {
        if (value.compareTo(minValue()) == -1 || maxValue().compareTo(value) == 1)
          throw new IllegalArgumentException(getShortName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);

        return value;
      }

      @Override
      protected final BIGINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (BIGINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final BIGINT.UNSIGNED clone() {
        return new BIGINT.UNSIGNED(this);
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }
    }

    protected static final Class<Long> type = Long.class;

    private final Long min;
    private final Long max;

    protected BIGINT(final Entity owner, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected BIGINT(final BIGINT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public BIGINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    protected BIGINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public BIGINT(final Long value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final BIGINT set(final BIGINT value) {
      super.set(value);
      return this;
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
    protected final short scale() {
      return 0;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final Long minValue() {
      return -9223372036854775808l;
    }

    @Override
    protected final Long maxValue() {
      return 9223372036854775807l;
    }

    @Override
    protected final int maxPrecision() {
      return 19;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareInt64(precision(), unsigned());
    }

    @Override
    protected final Class<Long> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.BIGINT;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setObject(parameterIndex, value, sqlType());
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final Object value = resultSet.getObject(columnIndex);
      if (value == null)
        this.value = null;
      else if (value instanceof BigInteger || value instanceof BigDecimal)
        this.value = ((Number)value).longValue();
      else if (value instanceof Long)
        this.value = Long.valueOf((Long)value);
      else if (value instanceof Integer)
        this.value = Long.valueOf((Integer)value);
      else if ((value instanceof Float || value instanceof Double) && Numbers.isInteger(((Number)value).floatValue()))
        this.value = ((Number)value).longValue();
      else
        throw new UnsupportedOperationException("Unsupported non-integer value for BIGINT: (" + value.getClass().getSimpleName() + ")" + value);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof ExactNumeric)
        return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final BIGINT wrapper(final Evaluable wrapper) {
      return (BIGINT)super.wrapper(wrapper);
    }

    @Override
    public final BIGINT clone() {
      return new BIGINT(this);
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }
  }

  public static final class BINARY extends DataType<byte[]> implements kind.BINARY {
    protected static final Class<byte[]> type = byte[].class;

    private final int length;
    private final boolean varying;

    protected BINARY(final Entity owner, final String name, final byte[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final boolean keyForUpdate, final int length, final boolean varying) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    protected BINARY(final BINARY copy) {
      super(copy);
      this.length = copy.length;
      this.varying = copy.varying;
    }

    public BINARY(final int length, final boolean varying) {
      super();
      checkLength(length);
      this.length = length;
      this.varying = varying;
    }

    public BINARY(final int length) {
      this(length, false);
    }

    public BINARY(final byte[] value) {
      this(value.length, false);
      set(value);
    }

    protected final void checkLength(final int length) {
      if (length <= 0 || length > 65535)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public final int length() {
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
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBinary(varying, length());
    }

    @Override
    protected final Class<byte[]> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      // FIXME: Does it matter if we know if this is BIT, BINARY, VARBINARY, or LONGVARBINARY?
      return varying ? Types.VARBINARY : Types.BINARY;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBytes(parameterIndex, value);
      else
        statement.setNull(parameterIndex, statement.getParameterMetaData().getParameterType(parameterIndex));
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final int columnType = resultSet.getMetaData().getColumnType(columnIndex);
      // FIXME: IS it right to support BIT here? Or should it be in BOOLEAN?
      if (columnType == Types.BIT)
        this.value = new byte[] {resultSet.getBoolean(columnIndex) ? (byte)0x01 : (byte)0x00};
      else
        this.value = resultSet.getBytes(columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BINARY)
        return new BINARY(Math.max(length(), ((BINARY)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final BINARY wrapper(final Evaluable wrapper) {
      return (BINARY)super.wrapper(wrapper);
    }

    @Override
    public final BINARY clone() {
      return new BINARY(this);
    }
  }

  public static final class BLOB extends LargeObject<InputStream> implements kind.BLOB {
    protected static final Class<InputStream> type = InputStream.class;

    protected BLOB(final Entity owner, final String name, final InputStream _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    protected BLOB(final BLOB copy) {
      super(copy);
    }

    public BLOB(final long length) {
      super(length);
    }

    public BLOB(final InputStream value) {
      super(4294967296l);
      set(value);
    }

    public final BLOB set(final BLOB value) {
      super.set(value);
      return this;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBlob(length());
    }

    @Override
    protected final Class<InputStream> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.BLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BLOB)
        return new BLOB(Math.max(length(), ((BLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final BLOB wrapper(final Evaluable wrapper) {
      return (BLOB)super.wrapper(wrapper);
    }

    @Override
    public final BLOB clone() {
      return new BLOB(this);
    }
  }

  public static class BOOLEAN extends Condition<Boolean> implements kind.BOOLEAN, Comparable<DataType<Boolean>> {
    protected static final Class<Boolean> type = Boolean.class;

    protected BOOLEAN(final Entity owner, final String name, final Boolean _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    protected BOOLEAN(final BOOLEAN copy) {
      super(copy);
    }

    public BOOLEAN() {
      super();
    }

    public BOOLEAN(final Boolean value) {
      super();
      set(value);
    }

    public final BOOLEAN set(final BOOLEAN value) {
      super.set(value);
      return this;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBoolean();
    }

    @Override
    protected final Class<Boolean> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.BOOLEAN;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBoolean(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final boolean value = resultSet.getBoolean(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<Boolean> o) {
      return o == null ? this.value == null ? 0 : 1 : this.value.compareTo(o.get());
    }

    @Override
    protected final BOOLEAN wrapper(final Evaluable wrapper) {
      return (BOOLEAN)super.wrapper(wrapper);
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(this);
    }
  }

  public static final class CHAR extends Textual<String> implements kind.CHAR {
    protected static final Class<String> type = String.class;

    private final boolean varying;

    protected CHAR(final Entity owner, final String name, final String _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final boolean keyForUpdate, final int length, final boolean varying) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    protected CHAR(final CHAR copy) {
      super(copy, copy.length());
      this.varying = copy.varying;
    }

    public CHAR(final int length, final boolean varying) {
      super((short)length);
      this.varying = varying;
      checkLength(length);
    }

    public CHAR(final int length) {
      this(length, false);
    }

    public CHAR(final String value) {
      this(65535, true);
      set(value);
    }

    protected CHAR() {
      super(null);
      this.varying = true;
    }

    public final CHAR set(final CHAR value) {
      super.set(value);
      return this;
    }

    protected final void checkLength(final int length) {
      if (length < 0 || (!varying() && length == 0) || length > 65535)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public final boolean varying() {
      return varying;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareChar(varying, length());
    }

    @Override
    protected final Class<String> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return varying ? Types.VARCHAR : Types.CHAR;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final CHAR wrapper(final Evaluable wrapper) {
      return (CHAR)super.wrapper(wrapper);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(this);
    }
  }

  public static final class CLOB extends LargeObject<Reader> implements kind.CLOB {
    protected static final Class<Reader> type = Reader.class;

    protected CLOB(final Entity owner, final String name, final Reader _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, length);
    }

    protected CLOB(final CLOB copy) {
      super(copy);
    }

    public CLOB(final long length) {
      super(length);
    }

    public CLOB(final Reader value) {
      super(4294967296l);
      set(value);
    }

    public final CLOB set(final CLOB value) {
      super.set(value);
      return this;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareClob(length());
    }

    @Override
    protected final Class<Reader> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.CLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) throws IOException {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof CLOB)
        return new CLOB(Math.max(length(), ((CLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final CLOB wrapper(final Evaluable wrapper) {
      return (CLOB)super.wrapper(wrapper);
    }

    @Override
    public final CLOB clone() {
      return new CLOB(this);
    }
  }

  public static final class DATE extends Temporal<LocalDate> implements kind.DATE {
    protected static final Class<LocalDate> type = LocalDate.class;

    protected DATE(final Entity owner, final String name, final LocalDate _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    protected DATE(final DATE copy) {
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
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDate();
    }

    @Override
    protected final Class<LocalDate> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.DATE;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final DATE wrapper(final Evaluable wrapper) {
      return (DATE)super.wrapper(wrapper);
    }

    @Override
    public final DATE clone() {
      return new DATE(this);
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.value == null)
        return this.value == null ? 0 : 1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getShortName(o.getClass()) + " cannot be compared to " + getShortName(getClass()));

      return o instanceof DATE ? this.value.compareTo(((DATE)o).value) : LocalDateTime.of(this.value, LocalTime.MIDNIGHT).compareTo(((DATETIME)o).value);
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (obj instanceof DATE)
        return name.equals(((DATE)obj).name) && compareTo((DATE)obj) == 0;

      if (obj instanceof DATETIME)
        return name.equals(((DATETIME)obj).name) && compareTo((DATETIME)obj) == 0;

      return false;
    }

    @Override
    public final String toString() {
      final LocalDate get = get();
      return get == null ? "NULL" : get.format(Dialect.DATE_FORMAT);
    }
  }

  public static abstract class DataType<T> extends type.Subject<T> implements kind.DataType<T> {
    protected static <T>void setValue(final DataType<T> dataType, final T value) {
      dataType.value = value;
    }

    protected static <T>String compile(final DataType<T> dataType, final DBVendor vendor) throws IOException {
      return dataType.compile(vendor);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static <T,V extends DataType<T>>V wrap(final T value) {
      try {
        if (value.getClass().isEnum())
          return (V)new ENUM((Enum)value);

        if (value instanceof org.libx4j.rdb.jsql.UNSIGNED.UnsignedNumber) {
          final org.libx4j.rdb.jsql.UNSIGNED.UnsignedNumber unsignedNumber = (org.libx4j.rdb.jsql.UNSIGNED.UnsignedNumber)value;
          return (V)unsignedNumber.getTypeClass().getConstructor(unsignedNumber.value().getClass()).newInstance(unsignedNumber.value());
        }

        return (V)lookupDataTypeConstructor(value.getClass()).newInstance(value);
      }
      catch (final IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @SuppressWarnings("unchecked")
    protected static <T>ARRAY<T> wrap(final T[] value) {
      final ARRAY<T> array;
      if (value.getClass().getComponentType().isEnum())
        array = new ARRAY<T>((Class<? extends DataType<T>>)value.getClass().getComponentType());
      else
        array = new ARRAY<T>((Class<? extends DataType<T>>)org.libx4j.rdb.jsql.type.typeToClass.get(value.getClass().getComponentType()));

      array.set(value);
      return array;
    }

    protected static final String getShortName(final Class<?> cls) {
      final String strictName = Classes.getStrictName(cls);
      return strictName.substring(strictName.indexOf("type.") + 5).replace(".", " ");
    }

    protected final Entity owner;
    protected final String name;
    protected final boolean unique;
    protected final boolean primary;
    protected final boolean nullable;
    protected final GenerateOn<? super T> generateOnInsert;
    protected final GenerateOn<? super T> generateOnUpdate;
    protected final boolean keyForUpdate;

    protected DataType(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      this.value = _default;
      this.owner = owner;
      this.name = name;
      this.unique = unique;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;
    }

    protected DataType(final DataType<T> copy) {
      this.value = copy.value;
      this.owner = copy.owner;
      this.name = copy.name;
      this.unique = copy.unique;
      this.primary = copy.primary;
      this.nullable = copy.nullable;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.keyForUpdate = copy.keyForUpdate;
    }

    protected DataType() {
      this(null, null, null, false, false, true, null, null, false);
    }

    protected T value;
    protected DataType<T> indirection;
    protected boolean wasSet;

    public DataType<T> set(final T value) {
      this.wasSet = true;
      this.value = value;
      return this;
    }

    protected final void set(final DataType<T> indirection) {
      this.wasSet = false;
      this.indirection = indirection;
    }

    public final T get() {
      return value;
    }

    public final boolean wasSet() {
      return wasSet;
    }

    public final <V extends DataType<T>>V AS(final V dataType) {
      dataType.wrapper(new As<T>(this, dataType));
      return dataType;
    }

    public final <E extends Enum<?> & EntityEnum>ENUM<E> AS(final ENUM<E> dataType) {
      dataType.wrapper(new As<T>(this, dataType));
      return dataType;
    }

    @Override
    protected void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    @Override
    protected Object evaluate(final Set<Evaluable> visited) {
      if (indirection == null || visited.contains(this))
        return wrapper() != null ? wrapper().evaluate(visited) : get();

      visited.add(this);
      return indirection.evaluate(visited);
    }

    protected abstract Class<T> type();
    protected abstract int sqlType();
    protected abstract void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException;
    protected abstract void set(final ResultSet resultSet, final int columnIndex) throws SQLException;
    protected abstract String compile(final DBVendor vendor) throws IOException;
    protected abstract String declare(final DBVendor vendor);
    protected abstract DataType<?> scaleTo(final DataType<?> dataType);

    @Override
    public abstract DataType<T> clone();

    @Override
    public int hashCode() {
      final T get = get();
      return name.hashCode() + (get == null ? 0 : get.hashCode());
    }

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (obj.getClass() != getClass())
        return false;

      final DataType<?> that = (DataType<?>)obj;
      final T get = get();
      return name.equals(that.name) && (get != null ? get.equals(that.get()) : that.get() == null);
    }

    @Override
    public String toString() {
      final T get = get();
      return get == null ? "NULL" : get.toString();
    }
  }

  public static class DATETIME extends Temporal<LocalDateTime> implements kind.DATETIME {
    protected static final Class<LocalDateTime> type = LocalDateTime.class;
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final short DEFAULT_PRECISION = 6;

    private final short precision;

    protected DATETIME(final Entity owner, final String name, final LocalDateTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (short)precision;
    }

    protected DATETIME(final DATETIME copy) {
      super(copy);
      this.precision = copy.precision;
    }

    public DATETIME(final int precision) {
      super();
      this.precision = (short)precision;
    }

    public DATETIME() {
      this(DEFAULT_PRECISION);
    }

    public DATETIME(final LocalDateTime value) {
      this(Numbers.precision(value.getNano() / (int)Math.pow(10, Numbers.trailingZeroes(value.getNano()))) + DEFAULT_PRECISION);
      set(value);
    }

    public final DATETIME set(final DATETIME value) {
      super.set(value);
      return this;
    }

    public final short precision() {
      return precision;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDateTime(precision);
    }

    @Override
    protected final Class<LocalDateTime> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATETIME)
        return new DATETIME(Math.max(precision(), ((DATETIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected final DATETIME wrapper(final Evaluable wrapper) {
      return (DATETIME)super.wrapper(wrapper);
    }

    @Override
    public DATETIME clone() {
      return new DATETIME(this);
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.value == null)
        return this.value == null ? 0 : 1;

      if (o instanceof TIME)
        throw new IllegalArgumentException(getShortName(o.getClass()) + " cannot be compared to " + getShortName(getClass()));

      return o instanceof DATETIME ? this.value.compareTo(((DATETIME)o).value) : this.value.toLocalDate().compareTo(((DATE)o).value);
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (obj instanceof DATE)
        return name.equals(((DATE)obj).name) && compareTo((DATE)obj) == 0;

      if (obj instanceof DATETIME)
        return name.equals(((DATETIME)obj).name) && compareTo((DATETIME)obj) == 0;

      return false;
    }

    @Override
    public final String toString() {
      final LocalDateTime get = get();
      return get == null ? "NULL" : get.format(Dialect.DATETIME_FORMAT);
    }
  }

  public static final class DECIMAL extends ExactNumeric<BigDecimal> implements kind.DECIMAL {
    public static final class UNSIGNED extends ExactNumeric<BigDecimal> implements kind.DECIMAL.UNSIGNED {
      protected static final Class<BigDecimal> type = BigDecimal.class;
      private static final BigDecimal maxValue = new BigDecimal("340282366920938463463374607431768211455");

      private final Short scale;
      private final BigDecimal min;
      private final BigDecimal max;

      protected UNSIGNED(final Entity owner, final String name, final BigDecimal _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        checkScale(scale);
        this.scale = (short)scale;
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final DECIMAL.UNSIGNED copy) {
        super(copy, copy.precision);
        this.scale = copy.scale;
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision, final int scale) {
        super(precision);
        checkScale(scale);
        this.scale = (short)scale;
        this.min = null;
        this.max = null;
      }

      protected UNSIGNED() {
        super(null);
        this.scale = null;
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigDecimal value) {
        this(value.precision(), value.scale());
        set(value);
      }

      public final UNSIGNED set(final DECIMAL.UNSIGNED value) {
        super.set(value);
        return this;
      }

      private final void checkScale(final int scale) {
        if (scale > maxScale)
          throw new IllegalArgumentException(getShortName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
      }

      @Override
      public final short scale() {
        return scale;
      }

      @Override
      protected final boolean unsigned() {
        return false;
      }

      @Override
      protected final BigDecimal minValue() {
        return BigDecimal.ZERO;
      }

      @Override
      protected final BigDecimal maxValue() {
        return maxValue;
      }

      @Override
      protected final int maxPrecision() {
        return 39;
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
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareDecimal(precision(), scale(), unsigned());
      }

      @Override
      protected final Class<BigDecimal> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.DECIMAL;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setBigDecimal(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final BigDecimal value = resultSet.getBigDecimal(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof ApproxNumeric)
          return new DECIMAL.UNSIGNED(precision() + 1, scale());

        if (dataType instanceof ExactNumeric)
          return new DECIMAL.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      protected final BigDecimal checkValue(final BigDecimal value) {
        if (value.compareTo(minValue()) == -1 || maxValue().compareTo(value) == 1)
          throw new IllegalArgumentException(getShortName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);

        return value;
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final DECIMAL.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DECIMAL.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final DECIMAL.UNSIGNED clone() {
        return new DECIMAL.UNSIGNED(this);
      }
    }

    protected static final Class<BigDecimal> type = BigDecimal.class;
    private static final BigDecimal minValue = new BigDecimal("-170141183460469231731687303715884105728");
    private static final BigDecimal maxValue = new BigDecimal("170141183460469231731687303715884105727");
    private static final byte maxScale = 38;

    private final Short scale;
    private final BigDecimal min;
    private final BigDecimal max;

    protected DECIMAL(final Entity owner, final String name, final BigDecimal _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      checkScale(scale);
      this.scale = (short)scale;
      this.min = min;
      this.max = max;
    }

    protected DECIMAL(final DECIMAL copy) {
      super(copy, copy.precision);
      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;
    }

    public DECIMAL(final int precision, final int scale) {
      super(precision);
      checkScale(scale);
      this.scale = (short)scale;
      this.min = null;
      this.max = null;
    }

    protected DECIMAL() {
      super(null);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final BigDecimal value) {
      this(value.precision(), value.scale());
      set(value);
    }

    public final DECIMAL set(final DECIMAL value) {
      super.set(value);
      return this;
    }

    private final void checkScale(final int scale) {
      if (scale > maxScale)
        throw new IllegalArgumentException(getShortName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
    }

    @Override
    public final short scale() {
      return scale;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final BigDecimal minValue() {
      return minValue;
    }

    @Override
    protected final BigDecimal maxValue() {
      return maxValue;
    }

    @Override
    protected final int maxPrecision() {
      return 39;
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
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDecimal(precision(), scale(), unsigned());
    }

    @Override
    protected final Class<BigDecimal> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.DECIMAL;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBigDecimal(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final BigDecimal value = resultSet.getBigDecimal(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DECIMAL(precision() + 1, scale());

      if (dataType instanceof ExactNumeric)
        return new DECIMAL(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    protected final BigDecimal checkValue(final BigDecimal value) {
      if (value.compareTo(minValue()) == -1 || maxValue().compareTo(value) == 1)
        throw new IllegalArgumentException(getShortName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);

      return value;
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final DECIMAL wrapper(final Evaluable wrapper) {
      return (DECIMAL)super.wrapper(wrapper);
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(this);
    }
  }

  public static class DOUBLE extends ApproxNumeric<Double> implements kind.DOUBLE {
    public static final class UNSIGNED extends ApproxNumeric<Double> implements kind.DOUBLE.UNSIGNED {
      protected static final Class<Double> type = Double.class;

      private final Double min;
      private final Double max;

      protected UNSIGNED(final Entity owner, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final DOUBLE.UNSIGNED copy) {
        super(copy);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Double value) {
        this();
        set(value);
      }

      public UNSIGNED() {
        super();
        this.min = null;
        this.max = null;
      }

      public final UNSIGNED set(final DOUBLE.UNSIGNED value) {
        super.set(value);
        return this;
      }

      @Override
      protected final short precision() {
        return 19;
      }

      @Override
      protected final short scale() {
        return 16;
      }

      @Override
      protected final boolean unsigned() {
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
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareFloat(true, unsigned());
      }

      @Override
      protected final Class<Double> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.DOUBLE;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setDouble(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final double value = resultSet.getDouble(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof Numeric)
          return new DOUBLE.UNSIGNED();

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final DOUBLE.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DOUBLE.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final DOUBLE.UNSIGNED clone() {
        return new DOUBLE.UNSIGNED(this);
      }
    }

    protected static final Class<Double> type = Double.class;

    private final Double min;
    private final Double max;

    protected DOUBLE(final Entity owner, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.min = min;
      this.max = max;
    }

    protected DOUBLE(final DOUBLE copy) {
      super(copy);
      this.min = null;
      this.max = null;
    }

    public DOUBLE(final Double value) {
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
    protected final short precision() {
      return 19;
    }

    @Override
    protected final short scale() {
      return 16;
    }

    @Override
    protected final boolean unsigned() {
      return false;
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
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(true, unsigned());
    }

    @Override
    protected final Class<Double> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.DOUBLE;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setDouble(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final double value = resultSet.getDouble(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof Numeric)
        return new DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final DOUBLE wrapper(final Evaluable wrapper) {
      return (DOUBLE)super.wrapper(wrapper);
    }

    @Override
    public DOUBLE clone() {
      return new DOUBLE(this);
    }
  }

  public static final class ENUM<T extends Enum<?> & EntityEnum> extends Textual<T> implements kind.ENUM<T> {
    private final Class<T> enumType;

    private static short calcEnumLength(final Class<?> enumType) {
      short length = 0;
      for (final Object constant : enumType.getEnumConstants())
        length = (short)Math.max(length, constant.toString().length());

      return length;
    }

    protected ENUM(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Class<T> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, calcEnumLength(type));
      this.enumType = type;
    }

    protected ENUM(final ENUM<T> copy) {
      super(copy, copy.length());
      this.enumType = copy.enumType;
    }

    public ENUM(final Class<T> enumType) {
      super(calcEnumLength(enumType));
      this.enumType = enumType;
    }

    @SuppressWarnings("unchecked")
    public ENUM(final T value) {
      this((Class<T>)value.getClass());
      set(value);
    }

    public final ENUM<T> set(final ENUM<T> value) {
      super.set(value);
      return this;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      throw new UnsupportedOperationException();
    }

    @Override
    protected final Class<T> type() {
      return enumType;
    }

    @Override
    protected final int sqlType() {
      return Types.CHAR;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setObject(parameterIndex, value.toString());
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final ENUM<T> wrapper(final Evaluable wrapper) {
      return (ENUM<T>)super.wrapper(wrapper);
    }

    @Override
    public final ENUM<T> clone() {
      return new ENUM<T>(this);
    }

    @Override
    protected final String evaluate(final Set<Evaluable> visited) {
      final Enum<?> get = get();
      return get == null ? null : get.toString();
    }
  }

  public static final class FLOAT extends ApproxNumeric<Float> implements kind.FLOAT {
    public static final class UNSIGNED extends ApproxNumeric<Float> implements kind.FLOAT.UNSIGNED {
      protected static final Class<Float> type = Float.class;

      private final Float min;
      private final Float max;

      protected UNSIGNED(final Entity owner, final String name, final Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final FLOAT.UNSIGNED copy) {
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
        set(value);
      }

      public final UNSIGNED set(final FLOAT.UNSIGNED value) {
        super.set(value);
        return this;
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
      protected final short precision() {
        return 10;
      }

      @Override
      protected final short scale() {
        return 16;
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareFloat(false, unsigned());
      }

      @Override
      protected final Class<Float> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.FLOAT;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setFloat(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final float value = resultSet.getFloat(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof FLOAT || dataType instanceof TINYINT)
          return unsigned() && ((Numeric<?>)dataType).unsigned() ? new FLOAT.UNSIGNED() : new FLOAT();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL.UNSIGNED(decimal.precision(), decimal.scale());
        }

        if (dataType instanceof Numeric)
          return new DOUBLE.UNSIGNED();

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final FLOAT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (FLOAT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final FLOAT.UNSIGNED clone() {
        return new FLOAT.UNSIGNED(this);
      }
    }

    protected static final Class<Float> type = Float.class;

    private final Float min;
    private final Float max;

    protected FLOAT(final Entity owner, final String name, final Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.min = min;
      this.max = max;
    }

    protected FLOAT(final FLOAT copy) {
      super(copy);
      this.min = null;
      this.max = null;
    }

    public FLOAT() {
      super();
      this.min = null;
      this.max = null;
    }

    public FLOAT(final Float value) {
      this();
      set(value);
    }

    public final FLOAT set(final FLOAT value) {
      super.set(value);
      return this;
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
    protected final short precision() {
      return 10;
    }

    @Override
    protected final short scale() {
      return 16;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(false, unsigned());
    }

    @Override
    protected final Class<Float> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.FLOAT;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setFloat(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final float value = resultSet.getFloat(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT || dataType instanceof TINYINT)
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new FLOAT.UNSIGNED() : new FLOAT();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(decimal.precision(), decimal.scale()) : new DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof Numeric)
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final FLOAT wrapper(final Evaluable wrapper) {
      return (FLOAT)super.wrapper(wrapper);
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(this);
    }
  }

  public static abstract class LargeObject<T extends Closeable> extends DataType<T> implements kind.LargeObject<T> {
    private final long length;

    protected LargeObject(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkLength(length);
      this.length = length;
    }

    protected LargeObject(final LargeObject<T> copy) {
      super(copy);
      this.length = copy.length;
    }

    protected LargeObject(final long length) {
      super();
      this.length = length;
    }

    public final long length() {
      return length;
    }

    private final void checkLength(final long length) {
      if (length <= 0 || length > 4294967295l)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 4294967295] exceeded: " + length);
    }
  }

  public static final class INT extends ExactNumeric<Integer> implements kind.INT {
    public static final class UNSIGNED extends ExactNumeric<Long> implements kind.INT.UNSIGNED {
      protected static final Class<Long> type = Long.class;

      private final Long min;
      private final Long max;

      protected UNSIGNED(final Entity owner, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final INT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      protected UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Long value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final UNSIGNED set(final INT.UNSIGNED value) {
        super.set(value);
        return this;
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
      protected final short scale() {
        return 0;
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final Long minValue() {
        return 0l;
      }

      @Override
      protected final Long maxValue() {
        return 4294967295l;
      }

      @Override
      protected final int maxPrecision() {
        return 10;
      }

      @Override
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareInt32(precision(), unsigned());
      }

      @Override
      protected final Class<Long> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.INTEGER;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setLong(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final long value = resultSet.getLong(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof ApproxNumeric)
          return new DOUBLE();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final INT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (INT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final INT.UNSIGNED clone() {
        return new INT.UNSIGNED(this);
      }
    }

    protected static final Class<Integer> type = Integer.class;

    private final Integer min;
    private final Integer max;

    protected INT(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected INT(final INT copy) {
      super(copy, copy.precision);
      this.min = null;
      this.max = null;
    }

    public INT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    protected INT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public INT(final Integer value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final INT set(final INT value) {
      super.set(value);
      return this;
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
    protected final short scale() {
      return 0;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final Integer minValue() {
      return -2147483648;
    }

    @Override
    protected final Integer maxValue() {
      return 2147483647;
    }

    @Override
    protected final int maxPrecision() {
      return 10;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareInt32(precision(), unsigned());
    }

    @Override
    protected final Class<Integer> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.INTEGER;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setInt(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final int value = resultSet.getInt(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof BIGINT)
        return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final INT wrapper(final Evaluable wrapper) {
      return (INT)super.wrapper(wrapper);
    }

    @Override
    public final INT clone() {
      return new INT(this);
    }
  }

  public static final class SMALLINT extends ExactNumeric<Short> implements kind.SMALLINT {
    public static final class UNSIGNED extends ExactNumeric<Integer> implements kind.SMALLINT.UNSIGNED {
      protected static final Class<Integer> type = Integer.class;

      private final Integer min;
      private final Integer max;

      protected UNSIGNED(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final SMALLINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      protected UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Integer value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final UNSIGNED set(final SMALLINT.UNSIGNED value) {
        super.set(value);
        return this;
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
      protected final short scale() {
        return 0;
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final Integer minValue() {
        return 0;
      }

      @Override
      protected final Integer maxValue() {
        return 65535;
      }

      @Override
      protected final int maxPrecision() {
        return 5;
      }

      @Override
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareInt32(precision(), unsigned());
      }

      @Override
      protected final Class<Integer> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.SMALLINT;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setInt(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final int value = resultSet.getInt(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof ApproxNumeric)
          return new DOUBLE();

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        if (dataType instanceof INT)
          return new INT(Math.max(precision(), ((INT)dataType).precision()));

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final SMALLINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (SMALLINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final SMALLINT.UNSIGNED clone() {
        return new SMALLINT.UNSIGNED(this);
      }
    }

    protected static final Class<Short> type = Short.class;

    private final Short min;
    private final Short max;

    protected SMALLINT(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected SMALLINT(final SMALLINT copy) {
      super(copy, copy.precision);
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    protected SMALLINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final Short value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final SMALLINT set(final SMALLINT value) {
      super.set(value);
      return this;
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
    protected final short scale() {
      return 0;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final Short minValue() {
      return -32768;
    }

    @Override
    protected final Short maxValue() {
      return 32767;
    }

    @Override
    protected final int maxPrecision() {
      return 5;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareInt32(precision(), unsigned());
    }

    @Override
    protected final Class<Short> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.SMALLINT;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setInt(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final short value = resultSet.getShort(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return new DOUBLE();

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof INT)
        return new INT(Math.max(precision(), ((INT)dataType).precision()));

      if (dataType instanceof BIGINT)
        return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final SMALLINT wrapper(final Evaluable wrapper) {
      return (SMALLINT)super.wrapper(wrapper);
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(this);
    }
  }

  public static abstract class Numeric<T extends Number> extends DataType<T> implements Comparable<DataType<? extends Number>>, kind.Numeric<T> {
    protected Numeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    protected Numeric(final Numeric<T> copy) {
      super(copy);
    }

    protected Numeric() {
      super();
    }

    protected abstract short precision();
    protected abstract short scale();
    protected abstract boolean unsigned();
    public abstract T min();
    public abstract T max();

    @Override
    protected final Number evaluate(final Set<Evaluable> visited) {
      return (Number)super.evaluate(visited);
    }

    @Override
    public final boolean equals(final Object obj) {
      if (obj == null)
        return true;

      if (!(obj instanceof Numeric))
        return false;

      return compareTo((Numeric<?>)obj) == 0;
    }
  }

  public static abstract class Entity extends type.Subject<Entity> implements kind.Entity<Entity>, Cloneable {
    protected final type.DataType<?>[] column;
    protected final type.DataType<?>[] primary;
    private final boolean wasSelected;

    protected Entity(final boolean wasSelected, final type.DataType<?>[] column, final type.DataType<?>[] primary) {
      this.wasSelected = wasSelected;
      this.column = column;
      this.primary = primary;
    }

    protected Entity(final Entity entity) {
      this.wasSelected = false;
      this.column = entity.column.clone();
      this.primary = entity.primary.clone();
    }

    protected Entity() {
      this.wasSelected = false;
      this.column = null;
      this.primary = null;
    }

    protected final boolean wasSelected() {
      return wasSelected;
    }

    @SuppressWarnings("unchecked")
    protected final Class<? extends Schema> schema() {
      return (Class<? extends Schema>)getClass().getEnclosingClass();
    }

    @Override
    protected final Entity evaluate(final Set<Evaluable> visited) {
      return this;
    }

    @Override
    protected final void compile(final Compilation compilation) throws IOException {
      Compiler.getCompiler(compilation.vendor).compile(this, compilation);
    }

    protected abstract String name();
    protected abstract Entity newInstance();

    @Override
    protected abstract Entity clone();
  }

  public static abstract class ExactNumeric<T extends Number> extends Numeric<T> implements kind.ExactNumeric<T> {
    protected final Integer precision;

    protected ExactNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkPrecision(precision);
      if (_default != null)
        checkValue(_default.doubleValue());

      this.precision = precision;
    }

    protected ExactNumeric(final Numeric<T> copy, final Integer precision) {
      super(copy);
      checkPrecision(precision);
      this.precision = precision;
    }

    protected ExactNumeric(final Integer precision) {
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

    private final void checkPrecision(final Integer precision) {
      if (precision != null && precision > maxPrecision())
        throw new IllegalArgumentException(getShortName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }

    protected abstract T minValue();
    protected abstract T maxValue();
    protected abstract int maxPrecision();

    protected final double checkValue(final double value) {
      if (value < minValue().doubleValue() || maxValue().doubleValue() < value)
        throw new IllegalArgumentException(getShortName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);

      return value;
    }

    @Override
    public final short precision() {
      return precision.shortValue();
    }

    @Override
    public final ExactNumeric<T> set(final T value) {
      if (value != null)
        checkValue(value.doubleValue());

      super.set(value);
      return this;
    }
  }

  public static final class TINYINT extends ExactNumeric<Byte> implements kind.TINYINT {
    public static final class UNSIGNED extends ExactNumeric<Short> implements kind.TINYINT.UNSIGNED {
      protected static final Class<Short> type = Short.class;

      private final Short min;
      private final Short max;

      protected UNSIGNED(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final TINYINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      protected UNSIGNED() {
        super(null);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Short value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final UNSIGNED set(final TINYINT.UNSIGNED value) {
        super.set(value);
        return this;
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
      protected final short scale() {
        return 0;
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final Short minValue() {
        return 0;
      }

      @Override
      protected final Short maxValue() {
        return 255;
      }

      @Override
      protected final int maxPrecision() {
        return 3;
      }

      @Override
      protected final String declare(final DBVendor vendor) {
        return vendor.getDialect().declareInt8(precision(), unsigned());
      }

      @Override
      protected final Class<Short> type() {
        return type;
      }

      @Override
      protected final int sqlType() {
        return Types.TINYINT;
      }

      @Override
      protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setShort(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        final short value = resultSet.getShort(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      protected final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      protected final DataType<?> scaleTo(final DataType<?> dataType) {
        if (dataType instanceof FLOAT)
          return new FLOAT();

        if (dataType instanceof DOUBLE)
          return new DOUBLE();

        if (dataType instanceof TINYINT)
          return new TINYINT(Math.max(precision(), ((TINYINT)dataType).precision()));

        if (dataType instanceof SMALLINT)
          return new SMALLINT(Math.max(precision(), ((SMALLINT)dataType).precision()));

        if (dataType instanceof INT)
          return new INT(Math.max(precision(), ((INT)dataType).precision()));

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

        if (dataType instanceof DECIMAL) {
          final DECIMAL decimal = (DECIMAL)dataType;
          return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
        }

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      public final int compareTo(final DataType<? extends Number> o) {
        return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
      }

      @Override
      protected final TINYINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (TINYINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final TINYINT.UNSIGNED clone() {
        return new TINYINT.UNSIGNED(this);
      }
    }

    protected static final Class<Byte> type = Byte.class;

    private final Byte min;
    private final Byte max;

    protected TINYINT(final Entity owner, final String name, final Byte _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final boolean keyForUpdate, final int precision, final Byte min, final Byte max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected TINYINT(final TINYINT copy) {
      super(copy, copy.precision);
      this.min = null;
      this.max = null;
    }

    public TINYINT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    protected TINYINT() {
      super(null);
      this.min = null;
      this.max = null;
    }

    public TINYINT(final Byte value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final TINYINT set(final TINYINT value) {
      super.set(value);
      return this;
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
    protected final short scale() {
      return 0;
    }

    @Override
    protected final boolean unsigned() {
      return false;
    }

    @Override
    protected final Byte minValue() {
      return -128;
    }

    @Override
    protected final Byte maxValue() {
      return 127;
    }

    @Override
    protected final int maxPrecision() {
      return 3;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareInt8(precision(), unsigned());
    }

    @Override
    protected final Class<Byte> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.TINYINT;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setByte(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final byte value = resultSet.getByte(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof FLOAT)
        return new FLOAT();

      if (dataType instanceof DOUBLE)
        return new DOUBLE();

      if (dataType instanceof TINYINT)
        return new TINYINT(Math.max(precision(), ((TINYINT)dataType).precision()));

      if (dataType instanceof SMALLINT)
        return new SMALLINT(Math.max(precision(), ((SMALLINT)dataType).precision()));

      if (dataType instanceof INT)
        return new INT(Math.max(precision(), ((INT)dataType).precision()));

      if (dataType instanceof BIGINT)
        return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return new DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null ? 1 : value == null && o.value == null ? 0 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    protected final TINYINT wrapper(final Evaluable wrapper) {
      return (TINYINT)super.wrapper(wrapper);
    }

    @Override
    public final TINYINT clone() {
      return new TINYINT(this);
    }
  }

  public static abstract class Subject<T> extends Evaluable {
    private Evaluable wrapper;

    protected final Evaluable wrapper() {
      return wrapper;
    }

    protected type.Subject<T> wrapper(final Evaluable wrapper) {
      this.wrapper = wrapper;
      return this;
    }
  }

  public static abstract class Temporal<T extends java.time.temporal.Temporal> extends DataType<T> implements Comparable<DataType<? extends java.time.temporal.Temporal>>, kind.Temporal<T> {
    protected Temporal(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    protected Temporal(final Temporal<T> copy) {
      super(copy);
    }

    protected Temporal() {
      super();
    }

    @Override
    protected final java.time.temporal.Temporal evaluate(final Set<Evaluable> visited) {
      return (java.time.temporal.Temporal)super.evaluate(visited);
    }
  }

  public static abstract class Textual<T extends Comparable<?>> extends DataType<T> implements kind.Textual<T>, Comparable<Textual<?>> {
    private final Short length;

    protected Textual(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.length = (short)length;
    }

    protected Textual(final Textual<T> copy, final Short length) {
      super(copy);
      this.length = length;
    }

    protected Textual(final Short length) {
      super();
      this.length = length;
    }

    public final Short length() {
      return length;
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof Textual)
        return new CHAR(Math.max(length(), ((Textual<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    protected String evaluate(final Set<Evaluable> visited) {
      return (String)super.evaluate(visited);
    }

    @Override
    public final int compareTo(final Textual<?> o) {
      if (o == null)
        return this.value == null ? 0 : 1;

      if (!(o instanceof CHAR) || !(o instanceof ENUM))
        throw new IllegalArgumentException(getShortName(o.getClass()) + " cannot be compared to " + getShortName(getClass()));

      return this.value.toString().compareTo(o.toString());
    }

    @Override
    public final int hashCode() {
      final T get = get();
      return name.hashCode() + (get == null ? 0 : get.toString().hashCode());
    }

    @Override
    public final boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (!(obj instanceof CHAR) && !(obj instanceof ENUM))
        return false;

      final DataType<?> that = (DataType<?>)obj;
      final T get = get();
      return name.equals(that.name) && (get == null ? that.get() == null : that.get() != null && get.toString().equals(that.get().toString()));
    }
  }

  public static final class TIME extends Temporal<LocalTime> implements kind.TIME {
    protected static final Class<LocalTime> type = LocalTime.class;

    private static final short DEFAULT_PRECISION = 6;

    private final short precision;

    protected TIME(final Entity owner, final String name, final LocalTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.precision = (short)precision;
    }

    protected TIME(final TIME copy) {
      super(copy);
      this.precision = copy.precision;
    }

    public TIME(final int precision) {
      super();
      this.precision = (short)precision;
    }

    public TIME() {
      this(DEFAULT_PRECISION);
    }

    public TIME(final LocalTime value) {
      this(Numbers.precision(value.getNano() / (int)Math.pow(10, Numbers.trailingZeroes(value.getNano()))) + DEFAULT_PRECISION);
      set(value);
    }

    public final TIME set(final TIME value) {
      super.set(value);
      return this;
    }

    public final short precision() {
      return precision;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareTime(precision);
    }

    @Override
    protected final Class<LocalTime> type() {
      return type;
    }

    @Override
    protected final int sqlType() {
      return Types.TIME;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof TIME)
        return new DATETIME(Math.max(precision(), ((TIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final int compareTo(final DataType<? extends java.time.temporal.Temporal> o) {
      if (o == null || o.value == null)
        return this.value == null ? 0 : 1;

      if (!(o instanceof TIME))
        throw new IllegalArgumentException(getShortName(o.getClass()) + " cannot be compared to " + getShortName(getClass()));

      return this.value.compareTo(((TIME)o).value);
    }

    @Override
    protected final TIME wrapper(final Evaluable wrapper) {
      return (TIME)super.wrapper(wrapper);
    }

    @Override
    public final TIME clone() {
      return new TIME(this);
    }

    @Override
    public final String toString() {
      final LocalTime get = get();
      return get == null ? "NULL" : get.format(Dialect.TIME_FORMAT);
    }
  }
}