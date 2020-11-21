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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.kind.Numeric.UNSIGNED;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.Dialect;
import org.libj.lang.Classes;
import org.libj.lang.Numbers;
import org.libj.util.function.Throwing;

public final class type {
  private static final HashMap<Class<?>,Class<?>> typeToClass = new HashMap<>();

  private static void scanMembers(final Class<?>[] members, final int i) {
    if (i == members.length)
      return;

    final Class<?> member = members[i];
    if (!Modifier.isAbstract(member.getModifiers())) {
      final Type type = Classes.getSuperclassGenericTypes(member)[0];
      if (type instanceof Class<?> && !typeToClass.containsKey((Class<?>)type))
        typeToClass.put((Class<?>)type, member);
    }

    scanMembers(members, i + 1);
    scanMembers(member.getClasses(), 0);
  }

  static {
    typeToClass.put(null, ENUM.class);
    scanMembers(type.class.getClasses(), 0);
  }

  private static Constructor<?> lookupDataTypeConstructor(Class<?> genericType) {
    Class<?> dataTypeClass;
    while ((dataTypeClass = typeToClass.get(genericType)) == null && (genericType = genericType.getSuperclass()) != null);
    return Classes.getConstructor(dataTypeClass, genericType);
  }

  public abstract static class ApproxNumeric<T extends Number> extends Numeric<T> implements kind.ApproxNumeric<T> {
    ApproxNumeric(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    ApproxNumeric(final Numeric<T> copy) {
      super(copy);
    }

    ApproxNumeric() {
      super();
    }
  }

  public static final class ARRAY<T> extends DataType<T[]> implements kind.ARRAY<T[]> {
//    public static final ARRAY<?> NULL = new ARRAY();
    final DataType<T> dataType;

    ARRAY(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T[] _default, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final boolean keyForUpdate, final Class<? extends DataType<T>> type) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      this.dataType = newInstance(Classes.getDeclaredConstructor(type));
    }

    @SuppressWarnings("unchecked")
    ARRAY(final ARRAY<T> copy) {
      this(copy.owner, copy.name, copy.unique, copy.primary, copy.nullable, copy.value, copy.generateOnInsert, copy.generateOnUpdate, copy.keyForUpdate, (Class<? extends DataType<T>>)copy.dataType.getClass());
      this.type = copy.type;
    }

    public ARRAY(final Class<? extends DataType<T>> type) {
      this(null, null, false, false, true, null, null, null, false, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, null, false, false, true, value, null, null, false, (Class<? extends DataType<T>>)value.getClass().getComponentType());
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

    private Class<T[]> type;

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
      if (value != null)
        statement.setArray(parameterIndex, new SQLArray<>(this));
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateArray(columnIndex, new SQLArray<>(this));
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    @SuppressWarnings("unchecked")
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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

  public static final class BIGINT extends ExactNumeric<Long> implements kind.BIGINT {
    public static final class UNSIGNED extends ExactNumeric<BigInteger> implements kind.BIGINT.UNSIGNED {
      public static final BIGINT.UNSIGNED NULL = new BIGINT.UNSIGNED();

      static final Class<BigInteger> type = BigInteger.class;

      private final BigInteger min;
      private final BigInteger max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigInteger _default, final GenerateOn<? super BigInteger> generateOnInsert, final GenerateOn<? super BigInteger> generateOnUpdate, final boolean keyForUpdate, final int precision, final BigInteger min, final BigInteger max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
      final short scale() {
        return 0;
      }

      @Override
      final boolean unsigned() {
        return true;
      }

      @Override
      final BigInteger minValue() {
        return BigInteger.ZERO;
      }

      @Override
      final BigInteger maxValue() {
        return Numbers.Unsigned.UNSIGNED_LONG_MAX_VALUE;
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
      final Class<BigInteger> type() {
        return type;
      }

      @Override
      final int sqlType() {
        return Types.BIGINT;
      }

      @Override
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setObject(parameterIndex, value, sqlType());
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateObject(columnIndex, value, sqlType());
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
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
          return ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(Math.max(precision(), decimal.precision() + 1), decimal.scale()) : new DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
        }

        if (dataType instanceof ApproxNumeric)
          return ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

        if (dataType instanceof ExactNumeric)
          return new BIGINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision() + 1));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      final BigInteger checkValue(final BigInteger value) {
        if (value.compareTo(minValue()) == -1 || maxValue().compareTo(value) == 1)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);

        return value;
      }

      @Override
      final BIGINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (BIGINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final BIGINT.UNSIGNED clone() {
        return new BIGINT.UNSIGNED(this);
      }
    }

    public static final BIGINT NULL = new BIGINT();

    static final Class<Long> type = Long.class;

    private final Long min;
    private final Long max;

    BIGINT(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
    final short scale() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setObject(parameterIndex, value, sqlType());
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateObject(columnIndex, value, sqlType());
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final Object value = resultSet.getObject(columnIndex);
      if (value == null)
        this.value = null;
      else if (value instanceof BigInteger || value instanceof BigDecimal)
        this.value = ((Number)value).longValue();
      else if (value instanceof Long)
        this.value = (Long)value;
      else if (value instanceof Integer)
        this.value = Long.valueOf((Integer)value);
      else if ((value instanceof Float || value instanceof Double) && Numbers.isWhole(((Number)value).floatValue()))
        this.value = ((Number)value).longValue();
      else
        throw new UnsupportedOperationException("Unsupported non-integer value for BIGINT: (" + value.getClass().getSimpleName() + ")" + value);
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
    public final BIGINT clone() {
      return new BIGINT(this);
    }
  }

  public static final class BINARY extends DataType<byte[]> implements kind.BINARY {
    public static final BINARY NULL = new BINARY((byte[])null);

    static final Class<byte[]> type = byte[].class;

    private final long length;
    private final boolean varying;

    BINARY(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final byte[] _default, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      if (value != null)
        statement.setBytes(parameterIndex, value);
      else
        statement.setNull(parameterIndex, statement.getParameterMetaData().getParameterType(parameterIndex));
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateBytes(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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

  public static final class BLOB extends LargeObject<InputStream> implements kind.BLOB {
    public static final BLOB NULL = new BLOB();

    static final Class<InputStream> type = InputStream.class;

    BLOB(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final InputStream _default, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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

  public static class BOOLEAN extends Condition<Boolean> implements kind.BOOLEAN, Comparable<DataType<Boolean>> {
    public static final BOOLEAN NULL = new BOOLEAN();

    static final Class<Boolean> type = Boolean.class;

    BOOLEAN(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Boolean _default, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
    }

    BOOLEAN(final BOOLEAN copy) {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBoolean(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateBoolean(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final boolean value = resultSet.getBoolean(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
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
    public final int compareTo(final DataType<Boolean> o) {
      return o == null || o.value == null ? value == null ? 0 : 1 : value == null ? -1 : value.compareTo(o.value);
    }

    @Override
    final BOOLEAN wrapper(final Evaluable wrapper) {
      return (BOOLEAN)super.wrapper(wrapper);
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(this);
    }
  }

  public static final class CHAR extends Textual<String> implements kind.CHAR {
    public static final CHAR NULL = new CHAR();

    static final Class<String> type = String.class;

    private final boolean varying;

    CHAR(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final String _default, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final boolean keyForUpdate, final long length, final boolean varying) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
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

    public final CHAR set(final CHAR value) {
      super.set(value);
      return this;
    }

    CHAR() {
      super(null);
      this.varying = true;
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

  public static final class CLOB extends LargeObject<Reader> implements kind.CLOB {
    public static final CLOB NULL = new CLOB();

    static final Class<Reader> type = Reader.class;

    CLOB(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Reader _default, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, length);
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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

  public static final class DATE extends Temporal<LocalDate> implements kind.DATE {
    public static final DATE NULL = new DATE();

    static final Class<LocalDate> type = LocalDate.class;

    DATE(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDate _default, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      Compiler.getCompiler(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      Compiler.getCompiler(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).updateColumn(this, resultSet, columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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
      if (o == null || o.value == null)
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
    static <T>void setValue(final DataType<T> dataType, final T value) {
      dataType.value = value;
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
        array = new ARRAY<>((Class<? extends DataType<T>>)org.jaxdb.jsql.type.typeToClass.get(value.getClass().getComponentType()));

      array.set(value);
      return array;
    }

    static String getSimpleName(final Class<?> cls) {
      final String canonicalName = cls.getCanonicalName();
      return canonicalName.substring(canonicalName.indexOf("type.") + 5).replace('.', ' ');
    }

    final Entity owner;
    final String name;
    final boolean unique;
    final boolean primary;
    final boolean nullable;
    final GenerateOn<? super T> generateOnInsert;
    final GenerateOn<? super T> generateOnUpdate;
    final boolean keyForUpdate;

    DataType(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      this.owner = owner;
      this.name = name;
      this.unique = unique;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
      this.keyForUpdate = keyForUpdate;

      this.value = _default;
    }

    DataType(final DataType<T> copy) {
      this.owner = copy.owner;
      this.name = copy.name;
      this.unique = copy.unique;
      this.primary = copy.primary;
      this.nullable = copy.nullable;
      this.generateOnInsert = copy.generateOnInsert;
      this.generateOnUpdate = copy.generateOnUpdate;
      this.keyForUpdate = copy.keyForUpdate;

      this.value = copy.value;
      // NOTE: Deliberately not copying indirection or wasSet
//      this.indirection = copy.indirection;
//      this.wasSet = copy.wasSet;
    }

    DataType() {
      this(null, null, false, false, true, null, null, null, false);
    }

    T value;
    int columnIndex;
    DataType<T> indirection;
    boolean wasSet;

    public boolean set(final T value) {
      this.wasSet = true;
      final boolean changed = !Objects.equals(this.value, value);
      this.value = value;
      return changed;
    }

    final void set(final DataType<T> indirection) {
      this.wasSet = false;
      this.indirection = indirection;
    }

    public final T get() {
      return value;
    }

    public final boolean wasSet() {
      return wasSet;
    }

    public final void update(final RowIterator<?> rows) throws SQLException {
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
        return wrapper() != null ? wrapper().evaluate(visited) : value;

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
      return name.equals(that.name) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
      return 31 * name.hashCode() + Objects.hash(value);
    }

    @Override
    public String toString() {
      return value == null ? "NULL" : value.toString();
    }
  }

  public static class DATETIME extends Temporal<LocalDateTime> implements kind.DATETIME {
    public static final DATETIME NULL = new DATETIME();

    static final Class<LocalDateTime> type = LocalDateTime.class;
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final byte DEFAULT_PRECISION = 6;

    private final byte precision;

    DATETIME(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalDateTime _default, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      this(Numbers.precision(value.getNano() / (int)StrictMath.pow(10, Numbers.trailingZeroes(value.getNano()))) + DEFAULT_PRECISION);
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
      if (o == null || o.value == null)
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

  public static final class DECIMAL extends ExactNumeric<BigDecimal> implements kind.DECIMAL {
    public static final class UNSIGNED extends ExactNumeric<BigDecimal> implements kind.DECIMAL.UNSIGNED {
      public static final DECIMAL.UNSIGNED NULL = new DECIMAL.UNSIGNED();

      static final Class<BigDecimal> type = BigDecimal.class;

      private final Short scale;
      private final BigDecimal min;
      private final BigDecimal max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        checkScale(precision, scale);
        this.scale = (short)scale;
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
        this.scale = (short)scale;
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigDecimal value) {
        this(value.precision(), value.scale());
        set(value);
      }

      public UNSIGNED() {
        super(null);
        this.scale = null;
        this.min = null;
        this.max = null;
      }

      public final UNSIGNED set(final DECIMAL.UNSIGNED value) {
        super.set(value);
        return this;
      }

      private void checkScale(final int precision, final int scale) {
        if (precision < scale)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");

        if (scale > maxScale)
          throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
      }

      @Override
      public final short scale() {
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setBigDecimal(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateBigDecimal(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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
        if (dataType instanceof ApproxNumeric)
          return new DECIMAL.UNSIGNED(precision() + 1, scale());

        if (dataType instanceof ExactNumeric)
          return new DECIMAL.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final DECIMAL.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DECIMAL.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final DECIMAL.UNSIGNED clone() {
        return new DECIMAL.UNSIGNED(this);
      }
    }

    public static final DECIMAL NULL = new DECIMAL();

    static final Class<BigDecimal> type = BigDecimal.class;
    private static final byte maxScale = 38;

    private final Short scale;
    private final BigDecimal min;
    private final BigDecimal max;

    DECIMAL(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final BigDecimal _default, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final boolean keyForUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      checkScale(precision, scale);
      this.scale = (short)scale;
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
      this.scale = (short)scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final BigDecimal value) {
      this(value.precision(), value.scale());
      set(value);
    }

    public DECIMAL() {
      super(null);
      this.scale = null;
      this.min = null;
      this.max = null;
    }

    public final DECIMAL set(final DECIMAL value) {
      super.set(value);
      return this;
    }

    private void checkScale(final int precision, final int scale) {
      if (precision < scale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [" + scale + "] cannot be greater than precision [" + precision + "]");

      if (scale > maxScale)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " scale [0, " + maxScale + "] exceeded: " + scale);
    }

    @Override
    public final short scale() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBigDecimal(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateBigDecimal(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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
    public final DECIMAL clone() {
      return new DECIMAL(this);
    }
  }

  public static class DOUBLE extends ApproxNumeric<Double> implements kind.DOUBLE {
    public static final class UNSIGNED extends ApproxNumeric<Double> implements kind.DOUBLE.UNSIGNED {
      public static final DOUBLE.UNSIGNED NULL = new DOUBLE.UNSIGNED();

      static final Class<Double> type = Double.class;

      private final Double min;
      private final Double max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setDouble(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateDouble(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        final double value = resultSet.getDouble(columnIndex);
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
          return new DECIMAL.UNSIGNED(decimal.precision() + 1, decimal.scale());
        }

        if (dataType instanceof Numeric)
          return new DOUBLE.UNSIGNED();

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final DOUBLE.UNSIGNED wrapper(final Evaluable wrapper) {
        return (DOUBLE.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final DOUBLE.UNSIGNED clone() {
        return new DOUBLE.UNSIGNED(this);
      }
    }

    public static final DOUBLE NULL = new DOUBLE();

    static final Class<Double> type = Double.class;

    private final Double min;
    private final Double max;

    DOUBLE(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Double _default, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean keyForUpdate, final Double min, final Double max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
    final boolean unsigned() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setDouble(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateDouble(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final double value = resultSet.getDouble(columnIndex);
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
    public DOUBLE clone() {
      return new DOUBLE(this);
    }
  }

  public static final class ENUM<T extends Enum<?> & EntityEnum> extends Textual<T> implements kind.ENUM<T> {
    public static final ENUM<?> NULL = new ENUM<>();
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

    ENUM(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Class<T> type) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, calcEnumLength(type));
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

    public final ENUM<T> set(final ENUM<T> value) {
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
      if (value != null)
        statement.setObject(parameterIndex, value.toString());
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateObject(columnIndex, value.toString());
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
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

  public static final class FLOAT extends ApproxNumeric<Float> implements kind.FLOAT {
    public static final class UNSIGNED extends ApproxNumeric<Float> implements kind.FLOAT.UNSIGNED {
      public static final FLOAT.UNSIGNED NULL = new FLOAT.UNSIGNED();

      static final Class<Float> type = Float.class;

      private final Float min;
      private final Float max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setFloat(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateFloat(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        final float value = resultSet.getFloat(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
      }

      @Override
      final String compile(final DBVendor vendor) {
        return Compiler.getCompiler(vendor).compile(this);
      }

      @Override
      final DataType<?> scaleTo(final DataType<?> dataType) {
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
      final FLOAT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (FLOAT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final FLOAT.UNSIGNED clone() {
        return new FLOAT.UNSIGNED(this);
      }
    }

    public static final FLOAT NULL = new FLOAT();

    static final Class<Float> type = Float.class;

    private final Float min;
    private final Float max;

    FLOAT(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Float _default, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean keyForUpdate, final Float min, final Float max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setFloat(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateFloat(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final float value = resultSet.getFloat(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
    }

    @Override
    final String compile(final DBVendor vendor) {
      return Compiler.getCompiler(vendor).compile(this);
    }

    @Override
    final DataType<?> scaleTo(final DataType<?> dataType) {
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
    final FLOAT wrapper(final Evaluable wrapper) {
      return (FLOAT)super.wrapper(wrapper);
    }

    @Override
    public final FLOAT clone() {
      return new FLOAT(this);
    }
  }

  public abstract static class LargeObject<T extends Closeable> extends DataType<T> implements kind.LargeObject<T> {
    private final Long length;

    LargeObject(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final Long length) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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

  public static final class INT extends ExactNumeric<Integer> implements kind.INT {
    public static final class UNSIGNED extends ExactNumeric<Long> implements kind.INT.UNSIGNED {
      public static final INT.UNSIGNED NULL = new INT.UNSIGNED();

      static final Class<Long> type = Long.class;

      private final Long min;
      private final Long max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Long _default, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final boolean keyForUpdate, final int precision, final Long min, final Long max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
        this(Numbers.precision(value));
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
      public final Long min() {
        return min;
      }

      @Override
      public final Long max() {
        return max;
      }

      @Override
      final short scale() {
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setLong(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateLong(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        final long value = resultSet.getLong(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
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

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final INT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (INT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final INT.UNSIGNED clone() {
        return new INT.UNSIGNED(this);
      }
    }

    public static final INT NULL = new INT();

    static final Class<Integer> type = Integer.class;

    private final Integer min;
    private final Integer max;

    INT(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
      this.min = min;
      this.max = max;
    }

    INT(final INT copy) {
      super(copy, copy.precision);
      this.min = copy.min;
      this.max = copy.max;
    }

    public INT(final int precision) {
      super(precision);
      this.min = null;
      this.max = null;
    }

    public INT(final Integer value) {
      this(Numbers.precision(value));
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
    public final Integer min() {
      return min;
    }

    @Override
    public final Integer max() {
      return max;
    }

    @Override
    final short scale() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setInt(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateInt(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final int value = resultSet.getInt(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
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

      if (dataType instanceof BIGINT)
        return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new INT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final INT wrapper(final Evaluable wrapper) {
      return (INT)super.wrapper(wrapper);
    }

    @Override
    public final INT clone() {
      return new INT(this);
    }
  }

  public static final class SMALLINT extends ExactNumeric<Short> implements kind.SMALLINT {
    public static final class UNSIGNED extends ExactNumeric<Integer> implements kind.SMALLINT.UNSIGNED {
      public static final SMALLINT.UNSIGNED NULL = new SMALLINT.UNSIGNED();

      static final Class<Integer> type = Integer.class;

      private final Integer min;
      private final Integer max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Integer _default, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final boolean keyForUpdate, final int precision, final Integer min, final Integer max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
        this.min = min;
        this.max = max;
      }

      UNSIGNED(final SMALLINT.UNSIGNED copy) {
        super(copy, copy.precision);
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super(precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Integer value) {
        this(Numbers.precision(value));
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
      public final Integer min() {
        return min;
      }

      @Override
      public final Integer max() {
        return max;
      }

      @Override
      final short scale() {
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setInt(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateInt(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        final int value = resultSet.getInt(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
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

        if (dataType instanceof INT)
          return new INT(Math.max(precision(), ((INT)dataType).precision()));

        if (dataType instanceof BIGINT)
          return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

        if (dataType instanceof ExactNumeric)
          return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

        throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
      }

      @Override
      final SMALLINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (SMALLINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final SMALLINT.UNSIGNED clone() {
        return new SMALLINT.UNSIGNED(this);
      }
    }

    public static final SMALLINT NULL = new SMALLINT();

    static final Class<Short> type = Short.class;

    private final Short min;
    private final Short max;

    SMALLINT(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
      this(Numbers.precision(value));
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
    public final Short min() {
      return min;
    }

    @Override
    public final Short max() {
      return max;
    }

    @Override
    final short scale() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setInt(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateInt(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final short value = resultSet.getShort(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
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

      if (dataType instanceof INT)
        return new INT(Math.max(precision(), ((INT)dataType).precision()));

      if (dataType instanceof BIGINT)
        return new BIGINT(Math.max(precision(), ((BIGINT)dataType).precision()));

      if (dataType instanceof ExactNumeric)
        return new SMALLINT(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    final SMALLINT wrapper(final Evaluable wrapper) {
      return (SMALLINT)super.wrapper(wrapper);
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(this);
    }
  }

  public abstract static class Numeric<T extends Number> extends DataType<T> implements Comparable<DataType<? extends Number>>, kind.Numeric<T> {
    Numeric(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
    public final int compareTo(final DataType<? extends Number> o) {
      return o == null || o.value == null ? value == null ? 0 : 1 : value == null ? -1 : Double.compare(value.doubleValue(), o.value.doubleValue());
    }

    @Override
    public final boolean equals(final Object obj) {
      return obj == this || obj instanceof Numeric && compareTo((Numeric<?>)obj) == 0;
    }
  }

  public abstract static class Entity extends type.Subject<Entity> implements kind.Entity<Entity>, Cloneable {
    final type.DataType<?>[] column;
    final type.DataType<?>[] primary;
    private final boolean wasSelected;

    Entity(final boolean wasSelected, final type.DataType<?>[] column, final type.DataType<?>[] primary) {
      this.wasSelected = wasSelected;
      this.column = column;
      this.primary = primary;
    }

    Entity(final Entity entity) {
      this.wasSelected = false;
      this.column = entity.column.clone();
      this.primary = entity.primary.clone();
    }

    Entity() {
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

    ExactNumeric(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
      checkPrecision(precision);
      if (_default != null)
        checkValue(_default.doubleValue());

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

    public final short precision() {
      return precision.shortValue();
    }

    abstract short scale();
    abstract T minValue();
    abstract T maxValue();
    abstract int maxPrecision();

    private void checkPrecision(final Integer precision) {
      if (precision != null && maxPrecision() != -1 && precision > maxPrecision())
        throw new IllegalArgumentException(getSimpleName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }

    final void checkValue(final double value) {
      if (minValue() != null && value < minValue().doubleValue() || maxValue() != null && maxValue().doubleValue() < value)
        throw new IllegalArgumentException(getSimpleName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);
    }

    @Override
    public final boolean set(final T value) {
      if (value != null)
        checkValue(value.doubleValue());

      return super.set(value);
    }
  }

  public static final class TINYINT extends ExactNumeric<Byte> implements kind.TINYINT {
    public static final class UNSIGNED extends ExactNumeric<Short> implements kind.TINYINT.UNSIGNED {
      public static final TINYINT.UNSIGNED NULL = new TINYINT.UNSIGNED();

      static final Class<Short> type = Short.class;

      private final Short min;
      private final Short max;

      UNSIGNED(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Short _default, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final boolean keyForUpdate, final int precision, final Short min, final Short max) {
        super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
        this(Numbers.precision(value));
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
      public final Short min() {
        return min;
      }

      @Override
      public final Short max() {
        return max;
      }

      @Override
      final short scale() {
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
      final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
        if (value != null)
          statement.setShort(parameterIndex, value);
        else
          statement.setNull(parameterIndex, sqlType());
      }

      @Override
      final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
        if (value != null)
          resultSet.updateShort(columnIndex, value);
        else
          resultSet.updateNull(columnIndex);
      }

      @Override
      final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
        this.columnIndex = columnIndex;
        final short value = resultSet.getShort(columnIndex);
        this.value = resultSet.wasNull() ? null : value;
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
      final TINYINT.UNSIGNED wrapper(final Evaluable wrapper) {
        return (TINYINT.UNSIGNED)super.wrapper(wrapper);
      }

      @Override
      public final TINYINT.UNSIGNED clone() {
        return new TINYINT.UNSIGNED(this);
      }
    }

    public static final TINYINT NULL = new TINYINT();

    static final Class<Byte> type = Byte.class;

    private final Byte min;
    private final Byte max;

    TINYINT(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final Byte _default, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final boolean keyForUpdate, final int precision, final Byte min, final Byte max) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate, precision);
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
      this(Numbers.precision(value));
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
    public final Byte min() {
      return min;
    }

    @Override
    public final Byte max() {
      return max;
    }

    @Override
    final short scale() {
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
    final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setByte(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    final void update(final ResultSet resultSet, final int columnIndex) throws SQLException {
      if (value != null)
        resultSet.updateByte(columnIndex, value);
      else
        resultSet.updateNull(columnIndex);
    }

    @Override
    final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.columnIndex = columnIndex;
      final byte value = resultSet.getByte(columnIndex);
      this.value = resultSet.wasNull() ? null : value;
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
    final TINYINT wrapper(final Evaluable wrapper) {
      return (TINYINT)super.wrapper(wrapper);
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

  public abstract static class Temporal<T extends java.time.temporal.Temporal> extends DataType<T> implements Comparable<DataType<? extends java.time.temporal.Temporal>>, kind.Temporal<T> {
    Temporal(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
  }

  public abstract static class Textual<T extends CharSequence & Comparable<?>> extends DataType<T> implements kind.Textual<T>, Comparable<Textual<?>> {
    private final Short length;

    Textual(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final T _default, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean keyForUpdate, final long length) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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

      if (!(obj instanceof CHAR) && !(obj instanceof ENUM))
        return false;

      final DataType<?> that = (DataType<?>)obj;
      return name.equals(that.name) && (value == null ? that.value == null : that.value != null && value.toString().equals(that.value.toString()));
    }

    @Override
    public final int hashCode() {
      return name.hashCode() + (value == null ? 0 : value.toString().hashCode());
    }
  }

  public static final class TIME extends Temporal<LocalTime> implements kind.TIME {
    public static final TIME NULL = new TIME();

    static final Class<LocalTime> type = LocalTime.class;

    private static final byte DEFAULT_PRECISION = 6;

    private final byte precision;

    TIME(final Entity owner, final String name, final boolean unique, final boolean primary, final boolean nullable, final LocalTime _default, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final boolean keyForUpdate, final int precision) {
      super(owner, name, unique, primary, nullable, _default, generateOnInsert, generateOnUpdate, keyForUpdate);
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
      this(Numbers.precision(value.getNano() / (int)StrictMath.pow(10, Numbers.trailingZeroes(value.getNano()))) + DEFAULT_PRECISION);
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
      if (o == null || o.value == null)
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