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

package org.safris.rdb.jsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Constructor;
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

import org.safris.commons.lang.Classes;
import org.safris.commons.lang.Numbers;
import org.safris.rdb.vendor.DBVendor;

public final class type {
  public static interface UNSIGNED {
  }

  private static final Map<Class<?>,Class<?>> typeToClass = new HashMap<Class<?>,Class<?>>();

  static {
    final Class<?>[] classes = type.class.getClasses();
    typeToClass.put(null, ENUM.class);
    for (final Class<?> cls : classes) {
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

  public static abstract class ApproxNumeric<T extends Number> extends Numeric<T> {
    protected ApproxNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    protected ApproxNumeric(final Numeric<T> copy) {
      super(copy);
    }

    protected ApproxNumeric() {
      super();
    }
  }

  protected static final class ARRAY<T> extends DataType<T[]> {
    protected final DataType<T> dataType;

    protected ARRAY(final Entity owner, final String name, final T[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final Class<? extends DataType<T>> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      try {
        this.dataType = type.newInstance();
      }
      catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @SuppressWarnings("unchecked")
    protected ARRAY(final ARRAY<T> copy) {
      this(copy.owner, copy.name, copy.value, copy.unique, copy.primary, copy.nullable, copy.generateOnInsert, copy.generateOnUpdate, (Class<? extends DataType<T>>)copy.dataType.getClass());
    }

    public ARRAY(final Class<? extends DataType<T>> type) {
      this(null, null, null, false, false, true, null, null, type);
    }

    @SuppressWarnings("unchecked")
    public ARRAY(final T[] value) {
      this(null, null, value, false, false, true, null, null, (Class<? extends DataType<T>>)value.getClass().getComponentType());
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      // FIXME
      throw new UnsupportedOperationException();
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
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this, dataType);
    }

    @Override
    protected DataType<?> scaleTo(final DataType<?> dataType) {
      throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ARRAY<T> clone() {
      return new ARRAY<T>((Class<? extends DataType<T>>)dataType.getClass());
    }
  }

  public static final class BIGINT extends ExactNumeric<Long> {
    public static final class UNSIGNED extends ExactNumeric<BigInteger> implements type.UNSIGNED {
      private final BigInteger min;
      private final BigInteger max;

      protected UNSIGNED(final Entity owner, final String name, final BigInteger _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigInteger> generateOnInsert, final GenerateOn<? super BigInteger> generateOnUpdate, final int precision, final BigInteger min, final BigInteger max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final BIGINT.UNSIGNED copy) {
        super(copy, copy.precision());
        this.min = copy.min;
        this.max = copy.max;
      }

      public UNSIGNED(final int precision) {
        super((short)precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final BigInteger value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final BigInteger min() {
        return min;
      }

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
        return new BigInteger("18446744073709551615");
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
      protected final String serialize(final DBVendor vendor) {
        return Serializer.getSerializer(vendor).serialize(this);
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

      @Override
      public final UNSIGNED clone() {
        return new UNSIGNED(this);
      }
    }

    private final Long min;
    private final Long max;

    protected BIGINT(final Entity owner, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final int precision, final Long min, final Long max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected BIGINT(final BIGINT copy) {
      super(copy, copy.precision());
      this.min = copy.min;
      this.max = copy.max;
    }

    public BIGINT(final int precision) {
      super((short)precision);
      this.min = null;
      this.max = null;
    }

    public BIGINT(final Long value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final Long min() {
      return min;
    }

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
      else
        throw new UnsupportedOperationException("Unsupported class for BigInt data type: " + value.getClass().getName());
    }

    @Override
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
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
    public final BIGINT clone() {
      return new BIGINT(this);
    }
  }

  public static final class BINARY extends Serial<byte[]> {
    private final boolean varying;

    protected BINARY(final Entity owner, final String name, final byte[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final int length, final boolean varying) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, length);
      this.varying = varying;
    }

    protected BINARY(final BINARY copy) {
      super(copy);
      this.varying = copy.varying;
    }

    public BINARY(final int length, final boolean varying) {
      super(length);
      this.varying = varying;
    }

    public BINARY(final int length) {
      this(length, false);
    }

    public BINARY(final byte[] value) {
      this(value.length, false);
      set(value);
    }

    public boolean varying() {
      return varying;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBinary(varying, length());
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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof Serial)
        return new BINARY(Math.max(length(), ((Serial<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final BINARY clone() {
      return new BINARY(this);
    }
  }

  public static final class BLOB extends LargeObject<InputStream> {
    private final long length;

    protected BLOB(final Entity owner, final String name, final InputStream _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final long length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = (short)length;
    }

    protected BLOB(final BLOB copy) {
      super(copy);
      this.length = copy.length;
    }

    public BLOB(final long length) {
      super();
      checkLength(length);
      this.length = (short)length;
    }

    public BLOB(final InputStream value) {
      super();
      this.length = 4294967296l;
      set(value);
    }

    protected final void checkLength(final long length) {
      if (length <= 0 || length > 4294967295l)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 4294967295] exceeded: " + length);
    }

    public final long length() {
      return length;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBlob(length);
    }

    @Override
    protected final int sqlType() {
      return Types.BLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BLOB)
        return new BLOB(Math.max(length(), ((BLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final BLOB clone() {
      return new BLOB(this);
    }
  }

  public static class BOOLEAN extends Condition<Boolean> {
    protected BOOLEAN(final Entity owner, final String name, final Boolean _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Boolean> generateOnInsert, final GenerateOn<? super Boolean> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareBoolean();
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
    protected String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof BOOLEAN)
        return new BOOLEAN();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final BOOLEAN clone() {
      return new BOOLEAN(this);
    }
  }

  public static final class CHAR extends Textual<String> {
    private final boolean varying;

    protected CHAR(final Entity owner, final String name, final String _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final int length, final boolean varying) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, length);
      this.varying = varying;
      checkLength(length);
    }

    protected CHAR(final CHAR copy) {
      super(copy, copy.length());
      this.varying = copy.varying;
    }

    public CHAR(final int length, final boolean varying) {
      super(length);
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

    protected final void checkLength(final int length) {
      if (length < 0 || (!varying() && length == 0) || length > 65535)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public boolean varying() {
      return varying;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareChar(varying, length());
    }

    @Override
    protected final int sqlType() {
      return varying ? Types.VARCHAR : Types.CHAR;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(this);
    }
  }

  public static final class CLOB extends LargeObject<Reader> {
    private final long length;

    protected CLOB(final Entity owner, final String name, final Reader _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final long length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
    }

    protected CLOB(final CLOB copy) {
      super(copy);
      this.length = copy.length;
    }

    public CLOB(final long length) {
      super();
      checkLength(length);
      this.length = length;
    }

    public CLOB(final Reader value) {
      super();
      this.length = 4294967296l;
      set(value);
    }

    protected final void checkLength(final long length) {
      if (length <= 0 || length > 4294967295l)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 4294967295] exceeded: " + length);
    }

    public final long length() {
      return length;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareClob(length);
    }

    @Override
    protected int sqlType() {
      return Types.CLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof CLOB)
        return new CLOB(Math.max(length(), ((CLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final CLOB clone() {
      return new CLOB(this);
    }
  }

  public static final class DATE extends Temporal<LocalDate> {
    protected DATE(final Entity owner, final String name, final LocalDate _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDate();
    }

    @Override
    protected final int sqlType() {
      return Types.DATE;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATE)
        return new DATE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final DATE clone() {
      return new DATE(this);
    }
  }

  public static abstract class DataType<T> extends Subject<T> {
    protected static <T>void setValue(final DataType<T> dataType, final T value) {
      dataType.value = value;
    }

    protected static <T>String serialize(final DataType<T> dataType, final DBVendor vendor) throws IOException {
      return dataType.serialize(vendor);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    protected static <T,V extends DataType<T>>V wrap(final T value) {
      try {
        if (value.getClass().isEnum())
          return (V)new ENUM((Enum)value);

        if (value instanceof org.safris.rdb.jsql.UNSIGNED.UnsignedNumber) {
          final org.safris.rdb.jsql.UNSIGNED.UnsignedNumber unsignedNumber = (org.safris.rdb.jsql.UNSIGNED.UnsignedNumber)value;
          return (V)unsignedNumber.getTypeClass().getConstructor(unsignedNumber.value().getClass()).newInstance(unsignedNumber.value());
        }

        return (V)lookupDataTypeConstructor(value.getClass()).newInstance(value);
      }
      catch (final Exception e) {
        throw new UnsupportedOperationException(e);
      }
    }

    @SuppressWarnings("unchecked")
    protected static <T>ARRAY<T> wrap(final T[] value) {
      final ARRAY<T> array;
      if (value.getClass().getComponentType().isEnum())
        array = new ARRAY<T>((Class<? extends DataType<T>>)value.getClass().getComponentType());
      else
        array = new ARRAY<T>((Class<? extends DataType<T>>)org.safris.rdb.jsql.type.typeToClass.get(value.getClass().getComponentType()));

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

    protected DataType(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      this.value = _default;
      this.owner = owner;
      this.name = name;
      this.unique = unique;
      this.primary = primary;
      this.nullable = nullable;
      this.generateOnInsert = generateOnInsert;
      this.generateOnUpdate = generateOnUpdate;
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
    }

    protected DataType() {
      this(null, null, null, false, false, true, null, null);
    }

    protected T value;
    protected boolean wasSet;

    public void set(final T value) {
      this.wasSet = true;
      this.value = value;
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
    protected void serialize(final Serialization serialization) throws IOException {
      Serializer.getSerializer(serialization.vendor).serialize(this, serialization);
    }

    private Class<T> type;

    @SuppressWarnings("unchecked")
    protected Class<T> type() {
      return type == null ? type = (Class<T>)Classes.getGenericSuperclasses(getClass())[0] : type;
    }

    protected abstract int sqlType();
    protected abstract void get(final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException;
    protected abstract void set(final ResultSet resultSet, final int columnIndex) throws SQLException;
    protected abstract String serialize(final DBVendor vendor) throws IOException;
    protected abstract String declare(final DBVendor vendor);
    protected abstract DataType<?> scaleTo(final DataType<?> dataType);

    @Override
    public abstract DataType<T> clone();

    @Override
    public boolean equals(final Object obj) {
      if (this == obj)
        return true;

      if (obj.getClass() != getClass())
        return false;

      final T get = get();
      return get != null ? get.equals(((DataType<?>)obj).get()) : ((DataType<?>)obj).get() == null;
    }
  }

  public static class DATETIME extends Temporal<LocalDateTime> {
    // FIXME: Is this the correct default? MySQL says that 6 is per the SQL spec, but their own default is 0
    private static final short DEFAULT_PRECISION = 6;

    private final short precision;

    protected DATETIME(final Entity owner, final String name, final LocalDateTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    public final short precision() {
      return precision;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDateTime(precision);
    }

    @Override
    protected final int sqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DATETIME)
        return new DATETIME(Math.max(precision(), ((DATETIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public DATETIME clone() {
      return new DATETIME(this);
    }
  }

  public static class DECIMAL extends ExactNumeric<BigDecimal> {
    public static final class UNSIGNED extends DECIMAL implements type.UNSIGNED {
      protected UNSIGNED(final Entity owner, final String name, final BigDecimal _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, scale, min, max);
      }

      protected UNSIGNED(final DECIMAL.UNSIGNED copy) {
        super(copy);
      }

      public UNSIGNED(final int precision, final int scale) {
        super(precision, scale);
      }

      public UNSIGNED(final BigDecimal value) {
        super(value);
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      protected final BigDecimal minValue() {
        return BigDecimal.ZERO;
      }

      @Override
      protected final BigDecimal maxValue() {
        return new BigDecimal("340282366920938463463374607431768211455");
      }

      @Override
      public final DECIMAL.UNSIGNED clone() {
        return new DECIMAL.UNSIGNED(this);
      }
    }

    private final short scale;
    private final BigDecimal min;
    private final BigDecimal max;

    protected DECIMAL(final Entity owner, final String name, final BigDecimal _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final int precision, final int scale, final BigDecimal min, final BigDecimal max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
      checkScale(scale);
      this.scale = (short)scale;
      this.min = min;
      this.max = max;
    }

    protected DECIMAL(final DECIMAL copy) {
      super(copy, copy.precision());
      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;
    }

    public DECIMAL(final int precision, final int scale) {
      super((short)precision);
      checkScale(scale);
      this.scale = (short)scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final BigDecimal value) {
      this(value.precision(), value.scale());
      set(value);
    }

    private final void checkScale(final int scale) {
      if (scale > maxScale())
        throw new IllegalArgumentException(getShortName(getClass()) + " scale [0, " + maxScale() + "] exceeded: " + scale);
    }

    @Override
    public final short scale() {
      return scale;
    }

    @Override
    protected boolean unsigned() {
      return false;
    }

    @Override
    protected BigDecimal minValue() {
      return new BigDecimal("-170141183460469231731687303715884105728");
    }

    @Override
    protected BigDecimal maxValue() {
      return new BigDecimal("170141183460469231731687303715884105727");
    }

    @Override
    protected final int maxPrecision() {
      return 39;
    }

    protected static final int maxScale() {
      return 38;
    }

    public final BigDecimal min() {
      return min;
    }

    public final BigDecimal max() {
      return max;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareDecimal(precision(), scale(), unsigned());
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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof ApproxNumeric)
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(precision() + 1, scale()) : new DECIMAL(precision() + 1, scale());

      if (dataType instanceof ExactNumeric)
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale()) : new DECIMAL(Math.max(precision(), ((ExactNumeric<?>)dataType).precision()) + 1, scale());

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public DECIMAL clone() {
      return new DECIMAL(this);
    }
  }

  public static class DOUBLE extends ApproxNumeric<Double> {
    public static final class UNSIGNED extends DOUBLE implements type.UNSIGNED {
      protected UNSIGNED(final Entity owner, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final Double min, final Double max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, min, max);
      }

      protected UNSIGNED(final DOUBLE.UNSIGNED copy) {
        super(copy);
      }

      public UNSIGNED(final Double value) {
        super(value);
      }

      public UNSIGNED() {
        super();
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      public final DOUBLE.UNSIGNED clone() {
        return new DOUBLE.UNSIGNED(this);
      }
    }

    private final Double min;
    private final Double max;

    protected DOUBLE(final Entity owner, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final Double min, final Double max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    @Override
    protected final short precision() {
      return 19;
    }

    @Override
    protected final short scale() {
      return 16;
    }

    @Override
    protected boolean unsigned() {
      return false;
    }

    public final Double min() {
      return min;
    }

    public final Double max() {
      return max;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(true, unsigned());
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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof DECIMAL) {
        final DECIMAL decimal = (DECIMAL)dataType;
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DECIMAL.UNSIGNED(decimal.precision() + 1, decimal.scale()) : new DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof Numeric)
        return unsigned() && ((Numeric<?>)dataType).unsigned() ? new DOUBLE.UNSIGNED() : new DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public DOUBLE clone() {
      return new DOUBLE(this);
    }
  }

  public static final class ENUM<T extends Enum<?> & EntityEnum> extends Textual<T> {
    private final Class<T> enumType;

    private static short calcEnumLength(final Class<?> enumType) {
      short length = 0;
      for (final Object constant : enumType.getEnumConstants())
        length = (short)Math.max(length, constant.toString().length());

      return length;
    }

    protected ENUM(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final Class<T> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, calcEnumLength(type));
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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    public final ENUM<T> clone() {
      return new ENUM<T>(this);
    }
  }

  public static class FLOAT extends ApproxNumeric<Float> {
    public static final class UNSIGNED extends FLOAT implements type.UNSIGNED {
      protected UNSIGNED(final Entity owner, final String name, final Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final Float min, final Float max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, min, max);
      }

      protected UNSIGNED(final FLOAT.UNSIGNED copy) {
        super(copy);
      }

      public UNSIGNED(final Float value) {
        super(value);
      }

      public UNSIGNED() {
        super();
      }

      @Override
      protected final boolean unsigned() {
        return true;
      }

      @Override
      public final FLOAT.UNSIGNED clone() {
        return new FLOAT.UNSIGNED(this);
      }
    }

    private final Float min;
    private final Float max;

    protected FLOAT(final Entity owner, final String name, final Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final Float min, final Float max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    public final Float min() {
      return min;
    }

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
    protected boolean unsigned() {
      return false;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareFloat(false, unsigned());
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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
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
    public FLOAT clone() {
      return new FLOAT(this);
    }
  }

  public static abstract class LargeObject<T> extends DataType<T> {
    protected LargeObject(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    protected LargeObject(final LargeObject<T> copy) {
      super(copy);
    }

    protected LargeObject() {
      super();
    }
  }

  public static final class INT extends ExactNumeric<Integer> {
    public static final class UNSIGNED extends ExactNumeric<Long> implements type.UNSIGNED {
      private final Long min;
      private final Long max;

      protected UNSIGNED(final Entity owner, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final int precision, final Long min, final Long max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final INT.UNSIGNED copy) {
        super(copy, copy.precision());
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final int precision) {
        super((short)precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Long value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final Long min() {
        return min;
      }

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
      protected final String serialize(final DBVendor vendor) {
        return Serializer.getSerializer(vendor).serialize(this);
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
      public final UNSIGNED clone() {
        return new UNSIGNED(this);
      }
    }

    private final Integer min;
    private final Integer max;

    protected INT(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final int precision, final Integer min, final Integer max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected INT(final INT copy) {
      super(copy, copy.precision());
      this.min = null;
      this.max = null;
    }

    public INT(final int precision) {
      super((short)precision);
      this.min = null;
      this.max = null;
    }

    public INT(final Integer value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final Integer min() {
      return min;
    }

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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
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
    public final INT clone() {
      return new INT(this);
    }
  }

  public static final class SMALLINT extends ExactNumeric<Short> {
    public static final class UNSIGNED extends ExactNumeric<Integer> implements type.UNSIGNED {
      private final Integer min;
      private final Integer max;

      protected UNSIGNED(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final int precision, final Integer min, final Integer max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final SMALLINT.UNSIGNED copy) {
        super(copy, copy.precision());
        this.min = null;
        this.max = null;
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

      public final Integer min() {
        return min;
      }

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
      protected final String serialize(final DBVendor vendor) {
        return Serializer.getSerializer(vendor).serialize(this);
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
      public final UNSIGNED clone() {
        return new UNSIGNED(this);
      }
    }

    private final Short min;
    private final Short max;

    protected SMALLINT(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final int precision, final Short min, final Short max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected SMALLINT(final SMALLINT copy) {
      super(copy, copy.precision());
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final int precision) {
      super((short)precision);
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final Short value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final Short min() {
      return min;
    }

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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
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
    public final SMALLINT clone() {
      return new SMALLINT(this);
    }
  }

  public static abstract class Numeric<T extends Number> extends DataType<T> {
    protected Numeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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
  }

  public static abstract class ExactNumeric<T extends Number> extends Numeric<T> {
    private final short precision;

    protected ExactNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      checkPrecision(precision);
      checkValue(_default);
      this.precision = (short)precision;
    }

    protected ExactNumeric(final Numeric<T> copy, final int precision) {
      super(copy);
      checkPrecision(precision);
      this.precision = (short)precision;
    }

    protected ExactNumeric(final int precision) {
      super();
      checkPrecision(precision);
      this.precision = (short)precision;
      if (precision <= 0)
        throw new IllegalArgumentException("precision must be >= 1");
    }

    @SuppressWarnings("unchecked")
    private final void checkValue(final T value) {
      if (value != null && (((Comparable<T>)value).compareTo(minValue()) < 0 || ((Comparable<T>)value).compareTo(maxValue()) > 0))
        throw new IllegalArgumentException(getShortName(getClass()) + " value range [" + minValue() + ", " + maxValue() + "] exceeded: " + value);
    }

    private final void checkPrecision(final int precision) {
      if (precision > maxPrecision())
        throw new IllegalArgumentException(getShortName(getClass()) + " precision [0, " + maxPrecision() + "] exceeded: " + precision);
    }

    protected abstract T minValue();
    protected abstract T maxValue();
    protected abstract int maxPrecision();

    @Override
    public final short precision() {
      return precision;
    }

    @Override
    public final void set(final T value) {
      checkValue(value);
      super.set(value);
    }
  }

  public static abstract class Serial<T> extends DataType<T> {
    private final int length;

    protected Serial(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      checkLength(length);
      this.length = length;
    }

    protected Serial(final Serial<T> copy) {
      super(copy);
      this.length = copy.length;
    }

    protected Serial(final int length) {
      super();
      checkLength(length);
      this.length = length;
    }

    protected void checkLength(final int length) {
      if (length <= 0 || length > 65535)
        throw new IllegalArgumentException(getShortName(getClass()) + " length [1, 65535] exceeded: " + length);
    }

    public final int length() {
      return length;
    }
  }

  public static final class TINYINT extends ExactNumeric<Byte> {
    public static final class UNSIGNED extends ExactNumeric<Short> implements type.UNSIGNED {
      private final Short min;
      private final Short max;

      protected UNSIGNED(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final int precision, final Short min, final Short max) {
        super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
        this.min = min;
        this.max = max;
      }

      protected UNSIGNED(final TINYINT.UNSIGNED copy) {
        super(copy, copy.precision());
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final int precision) {
        super((short)precision);
        this.min = null;
        this.max = null;
      }

      public UNSIGNED(final Short value) {
        this(Numbers.precision(value));
        set(value);
      }

      public final Short min() {
        return min;
      }

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
      protected final String serialize(final DBVendor vendor) {
        return Serializer.getSerializer(vendor).serialize(this);
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
      public final UNSIGNED clone() {
        return new UNSIGNED(this);
      }
    }

    private final Byte min;
    private final Byte max;

    protected TINYINT(final Entity owner, final String name, final Byte _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Byte> generateOnInsert, final GenerateOn<? super Byte> generateOnUpdate, final int precision, final Byte min, final Byte max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision);
      this.min = min;
      this.max = max;
    }

    protected TINYINT(final TINYINT copy) {
      super(copy, copy.precision());
      this.min = null;
      this.max = null;
    }

    public TINYINT(final int precision) {
      super((short)precision);
      this.min = null;
      this.max = null;
    }

    public TINYINT(final Byte value) {
      this(Numbers.precision(value));
      set(value);
    }

    public final Byte min() {
      return min;
    }

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
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
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
    public final TINYINT clone() {
      return new TINYINT(this);
    }
  }

  public static abstract class Temporal<T extends java.time.temporal.Temporal> extends DataType<T> {
    protected Temporal(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    protected Temporal(final Temporal<T> copy) {
      super(copy);
    }

    protected Temporal() {
      super();
    }
  }

  public static abstract class Textual<T> extends DataType<T> {
    private final short length;

    protected Textual(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = (short)length;
    }

    protected Textual(final Textual<T> copy, final int length) {
      super(copy);
      this.length = (short)length;
    }

    protected Textual(final int length) {
      super();
      this.length = (short)length;
    }

    public final short length() {
      return length;
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof Textual)
        return new CHAR(Math.max(length(), ((Textual<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }
  }

  public static final class TIME extends Temporal<LocalTime> {
    private static final short DEFAULT_PRECISION = 6;

    private final short precision;

    protected TIME(final Entity owner, final String name, final LocalTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
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

    public final short precision() {
      return precision;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getDialect().declareTime(precision);
    }

    @Override
    protected final int sqlType() {
      return Types.TIME;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      Serializer.getSerializer(DBVendor.valueOf(statement.getConnection().getMetaData())).setParameter(this, statement, parameterIndex);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = Serializer.getSerializer(DBVendor.valueOf(resultSet.getStatement().getConnection().getMetaData())).getParameter(this, resultSet, columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof TIME)
        return new DATETIME(Math.max(precision(), ((TIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final TIME clone() {
      return new TIME(this);
    }
  }
}