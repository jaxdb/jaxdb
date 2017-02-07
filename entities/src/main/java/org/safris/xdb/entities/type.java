/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.safris.commons.io.Readers;
import org.safris.commons.io.Streams;
import org.safris.commons.lang.Classes;
import org.safris.commons.lang.Numbers;
import org.safris.commons.util.Formats;
import org.safris.xdb.schema.DBVendor;

public final class type {
  protected static final Map<Class<?>,Class<?>> typeToClass = new HashMap<Class<?>,Class<?>>();

  static {
    final Class<?>[] classes = type.class.getClasses();
    typeToClass.put(null, type.ENUM.class);
    for (final Class<?> cls : classes) {
      if (!Modifier.isAbstract(cls.getModifiers())) {
        final Type type = Classes.getGenericSuperclasses(cls)[0];
        if (type instanceof Class<?>)
          typeToClass.put((Class<?>)type, cls);
        else
          System.out.println("XXX");
      }
    }
  }

  public static abstract class ApproxNumeric<T extends Number> extends Numeric<T> {
    protected ApproxNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean unsigned) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned);
    }

    protected ApproxNumeric(final Numeric<T> copy, final boolean unsigned) {
      super(copy, unsigned);
    }

    protected ApproxNumeric(final boolean unsigned) {
      super(unsigned);
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
      if (value == null)
        statement.setNull(parameterIndex, Types.ARRAY);
      else
        statement.setArray(parameterIndex, new SQLArray<T>(this));
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

  public static final class BIGINT extends ExactNumeric<BigInteger> {
    private static final BigInteger MIN = new BigInteger("-9223372036854775808");
    private static final BigInteger MAX_SIGNED = new BigInteger("9223372036854775807");
    private static final BigInteger MAX_UNSIGNED = new BigInteger("18446744073709551615");

    private final BigInteger min;
    private final BigInteger max;

    // FIXME: This is not properly supported by Derby, as in derby, only signed numbers are allowed. But in MySQL, unsigned values of up to 18446744073709551615 are allowed.
    protected BIGINT(final Entity owner, final String name, final BigInteger _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigInteger> generateOnInsert, final GenerateOn<? super BigInteger> generateOnUpdate, final int precision, final boolean unsigned, final BigInteger min, final BigInteger max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned, precision);
      this.min = min;
      this.max = max;
    }

    protected BIGINT(final BIGINT copy) {
      super(copy, copy.unsigned(), copy.precision());
      this.min = copy.min;
      this.max = copy.max;
    }

    public BIGINT(final int precision, final boolean unsigned) {
      super(unsigned, (short)precision);
      this.min = null;
      this.max = null;
    }

    public BIGINT(final int precision) {
      this(precision, false);
    }

    public BIGINT(final BigInteger value) {
      this(Numbers.precision(value), value.signum() >= 0);
      set(value);
    }

    public final BigInteger min() {
      return min;
    }

    public final BigInteger max() {
      return max;
    }

    @Override
    public final void set(final BigInteger value) {
      if (value != null) {
        if (unsigned() && value.compareTo(MAX_UNSIGNED) > 0)
          throw new IllegalArgumentException("value is out of range for UNSIGNED BIGINT: " + value);

        if (value.compareTo(MAX_SIGNED) > 0)
          throw new IllegalArgumentException("value is out of range for BIGINT: " + value);

        if (value.compareTo(MIN) < 0)
          throw new IllegalArgumentException("value is out of range for BIGINT: " + value);
      }

      super.set(value);
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareInt64(precision(), unsigned());
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
      else if (value instanceof Long)
        this.value = BigInteger.valueOf((Long)value);
      else
        throw new UnsupportedOperationException("Unsupported class for BigInt data type: " + value.getClass().getName());
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(Math.max(precision(), decimal.precision() + 1), decimal.scale());
      }

      if (dataType instanceof type.ApproxNumeric)
        return new type.DOUBLE();

      if (dataType instanceof type.ExactNumeric)
        return new type.BIGINT(Math.max(precision(), ((type.ExactNumeric<?>)dataType).precision() + 1));

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
      return vendor.getSQLSpec().declareBinary(varying, length());
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
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.Serial)
        return new type.BINARY(Math.max(length(), ((type.Serial<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final BINARY clone() {
      return new BINARY(this);
    }
  }

  public static final class BLOB extends LargeObject<InputStream> {
    private final int length;

    protected BLOB(final Entity owner, final String name, final InputStream _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = (short)length;
    }

    protected BLOB(final BLOB copy) {
      super(copy);
      this.length = copy.length;
    }

    public BLOB(final int length) {
      super();
      this.length = (short)length;
    }

    // NOTE: This implies that the InputStream must support mark() and reset().
    // NOTE: If not, then an IOException will be thrown.
    public BLOB(final InputStream value) throws IOException {
      super();
      value.mark(Integer.MAX_VALUE);
      this.length = (short)Streams.getBytes(value).length;
      value.reset();
      set(value);
    }

    public final int length() {
      return length;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareBlob(length);
    }

    @Override
    protected final int sqlType() {
      return Types.BLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBlob(parameterIndex, value);
      else
        statement.setNull(parameterIndex, Types.BLOB);
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = resultSet.getBinaryStream(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.BLOB)
        return new type.BLOB(Math.max(length(), ((type.BLOB)dataType).length()));

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
      return vendor.getSQLSpec().declareBoolean();
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
      this.value = resultSet.wasNull() ? null : resultSet.getBoolean(columnIndex);
    }

    @Override
    protected String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.BOOLEAN)
        return new type.BOOLEAN();

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
    }

    protected CHAR(final CHAR copy) {
      super(copy, copy.length());
      this.varying = copy.varying;
    }

    public CHAR(final int length, final boolean varying) {
      super((short)length);
      this.varying = varying;
    }

    public CHAR(final int length) {
      this(length, false);
    }

    public CHAR(final String value) {
      this(value.length(), false);
      set(value);
    }

    public boolean varying() {
      return varying;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareChar(varying, length());
    }

    @Override
    protected final int sqlType() {
      return varying ? Types.VARCHAR : Types.CHAR;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setString(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      this.value = resultSet.getString(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    public final CHAR clone() {
      return new CHAR(this);
    }
  }

  public static final class CLOB extends LargeObject<Reader> {
    private final int length;

    protected CLOB(final Entity owner, final String name, final Reader _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = (short)length;
    }

    protected CLOB(final CLOB copy) {
      super(copy);
      this.length = copy.length;
    }

    public CLOB(final int length) {
      super();
      this.length = (short)length;
    }

    // NOTE: This implies that the Reader must support mark() and reset().
    // NOTE: If not, then an IOException will be thrown.
    public CLOB(final Reader value) throws IOException {
      super();
      value.mark(Integer.MAX_VALUE);
      this.length = Readers.readFully(value).length();
      value.reset();
      set(value);
    }

    public final int length() {
      return length;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareClob(length);
    }

    @Override
    protected int sqlType() {
      return Types.CLOB;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setClob(parameterIndex, value);
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final java.sql.Clob value = resultSet.getClob(columnIndex);
      this.value = value == null ? null : value.getCharacterStream();
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.CLOB)
        return new type.CLOB(Math.max(length(), ((type.CLOB)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final CLOB clone() {
      return new CLOB(this);
    }
  }

  public static final class DATE extends Temporal<LocalDate> {
    protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;

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
      return vendor.getSQLSpec().declareDate();
    }

    @Override
    protected int sqlType() {
      return Types.DATE;
    }

    @Override
    @SuppressWarnings("deprecation")
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setDate(parameterIndex, new java.sql.Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    @SuppressWarnings("deprecation")
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final java.sql.Date value = resultSet.getDate(columnIndex);
      this.value = value == null ? null : LocalDate.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate());
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.DATE)
        return new type.DATE();

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
        final V dataType;
        if (value.getClass().isEnum()) {
          dataType = (V)new ENUM(value.getClass());
        }
        else {
          dataType = (V)org.safris.xdb.entities.type.typeToClass.get(value.getClass()).getConstructor(value.getClass()).newInstance(value);
        }

        dataType.set(value);
        return dataType;
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
        array = new ARRAY<T>((Class<? extends DataType<T>>)org.safris.xdb.entities.type.typeToClass.get(value.getClass().getComponentType()));

      array.set(value);
      return array;
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

    public final <E extends Enum<?>>type.ENUM<E> AS(final type.ENUM<E> dataType) {
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
    protected abstract void get(final PreparedStatement statement, final int parameterIndex) throws SQLException;
    protected abstract void set(final ResultSet resultSet, final int columnIndex) throws SQLException;
    protected abstract String serialize(final DBVendor vendor) throws IOException;
    protected abstract String declare(final DBVendor vendor);
    protected abstract type.DataType<?> scaleTo(final type.DataType<?> dataType);

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
    private static final short DEFAULT_PRECISION = 6;

    protected static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

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
      return vendor.getSQLSpec().declareDateTime(precision);
    }

    @Override
    protected final int sqlType() {
      return Types.TIMESTAMP;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setTimestamp(parameterIndex, new Timestamp(value.toEpochSecond(ZoneOffset.UTC)));
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    @SuppressWarnings("deprecation")
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final java.sql.Timestamp value = resultSet.getTimestamp(columnIndex);
      this.value = value == null ? null : LocalDateTime.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate(), value.getHours(), value.getMinutes(), value.getSeconds(), value.getNanos());
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.DATETIME)
        return new type.DATETIME(Math.max(precision(), ((type.DATETIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public DATETIME clone() {
      return new DATETIME(this);
    }
  }

  public static final class DECIMAL extends ExactNumeric<BigDecimal> {
    private final short scale;
    private final BigDecimal min;
    private final BigDecimal max;

    protected DECIMAL(final Entity owner, final String name, final BigDecimal _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigDecimal> generateOnInsert, final GenerateOn<? super BigDecimal> generateOnUpdate, final int precision, final int scale, final boolean unsigned, final BigDecimal min, final BigDecimal max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned, precision);
      this.scale = (short)scale;
      this.min = min;
      this.max = max;
    }

    protected DECIMAL(final DECIMAL copy) {
      super(copy, copy.unsigned(), copy.precision());
      this.scale = copy.scale;
      this.min = copy.min;
      this.max = copy.max;
    }

    public DECIMAL(final int precision, final int scale, final boolean unsigned) {
      super(unsigned, (short)precision);
      this.scale = (short)scale;
      this.min = null;
      this.max = null;
    }

    public DECIMAL(final int precision, final int scale) {
      this(precision, scale, false);
    }

    public DECIMAL(final BigDecimal value) {
      this(value.precision(), value.scale(), value.signum() >= 0);
      set(value);
    }

    public short scale() {
      return scale;
    }

    public final BigDecimal min() {
      return min;
    }

    public final BigDecimal max() {
      return max;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareDecimal(precision(), scale(), unsigned());
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
      this.value = resultSet.wasNull() ? null : resultSet.getBigDecimal(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.ApproxNumeric)
        return new type.DECIMAL(precision() + 1, scale());

      if (dataType instanceof type.ExactNumeric)
        return new type.DECIMAL(Math.max(precision(), ((type.ExactNumeric<?>)dataType).precision()) + 1, scale());

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final DECIMAL clone() {
      return new DECIMAL(this);
    }
  }

  public static class DOUBLE extends ApproxNumeric<Double> {
    private final Double min;
    private final Double max;

    protected DOUBLE(final Entity owner, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Double> generateOnInsert, final GenerateOn<? super Double> generateOnUpdate, final boolean unsigned, final Double min, final Double max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned);
      this.min = min;
      this.max = max;
    }

    protected DOUBLE(final DOUBLE copy) {
      super(copy, copy.unsigned());
      this.min = null;
      this.max = null;
    }

    public DOUBLE(final boolean unsigned) {
      super(unsigned);
      this.min = null;
      this.max = null;
    }

    public DOUBLE(final Double value) {
      this(value >= 0);
      set(value);
    }

    public DOUBLE() {
      this(false);
    }

    public final Double min() {
      return min;
    }

    public final Double max() {
      return max;
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareFloat(true, unsigned());
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
      this.value = resultSet.wasNull() ? null : resultSet.getDouble(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(decimal.precision() + 1, decimal.scale());
      }

      if (dataType instanceof type.Numeric)
        return new type.DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public DOUBLE clone() {
      return new DOUBLE(this);
    }
  }

  public static final class ENUM<T extends Enum<?>> extends Textual<T> {
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
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    public final ENUM<T> clone() {
      return new ENUM<T>(this);
    }
  }

  public static final class FLOAT extends ApproxNumeric<Float> {
    private final Float min;
    private final Float max;

    protected FLOAT(final Entity owner, final String name, final Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Float> generateOnInsert, final GenerateOn<? super Float> generateOnUpdate, final boolean unsigned, final Float min, final Float max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned);
      this.min = min;
      this.max = max;
    }

    protected FLOAT(final FLOAT copy) {
      super(copy, copy.unsigned());
      this.min = null;
      this.max = null;
    }

    public FLOAT(final boolean unsigned) {
      super(unsigned);
      this.min = null;
      this.max = null;
    }

    public FLOAT() {
      this(false);
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
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareFloat(false, unsigned());
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
      this.value = resultSet.wasNull() ? null : resultSet.getFloat(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.FLOAT || dataType instanceof type.SMALLINT)
        return new type.FLOAT();

      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(decimal.precision(), decimal.scale());
      }

      if (dataType instanceof type.Numeric)
        return new type.DOUBLE();

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final FLOAT clone() {
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

  public static final class INT extends ExactNumeric<Long> {
    private final Long min;
    private final Long max;

    protected INT(final Entity owner, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Long> generateOnInsert, final GenerateOn<? super Long> generateOnUpdate, final int precision, final boolean unsigned, final Long min, final Long max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned, precision);
      this.min = min;
      this.max = max;
    }

    protected INT(final INT copy) {
      super(copy, copy.unsigned(), copy.precision());
      this.min = null;
      this.max = null;
    }

    public INT(final int precision, final boolean unsigned) {
      super(unsigned, (short)precision);
      this.min = null;
      this.max = null;
    }

    public INT(final int precision) {
      this(precision, false);
    }

    public INT(final Long value) {
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
    public final void set(final Long value) {
      if (value != null) {
        if (unsigned() && value > 2147483647)
          throw new IllegalArgumentException("value is out of range for UNSIGNED INT: " + value);

        if (value > 4294967295l)
          throw new IllegalArgumentException("value is out of range for INT: " + value);

        if (value < -2147483648)
          throw new IllegalArgumentException("value is out of range for INT: " + value);
      }

      super.set(value);
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareInt32(precision(), unsigned());
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
      this.value = resultSet.wasNull() ? null : resultSet.getLong(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.ApproxNumeric)
        return new type.DOUBLE();

      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof type.BIGINT)
        return new type.BIGINT(Math.max(precision(), ((type.BIGINT)dataType).precision()));

      if (dataType instanceof type.ExactNumeric)
        return new type.INT(Math.max(precision(), ((type.ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final INT clone() {
      return new INT(this);
    }
  }

  public static final class MEDIUMINT extends ExactNumeric<Integer> {
    private final Integer min;
    private final Integer max;

    protected MEDIUMINT(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final int precision, final boolean unsigned, final Integer min, final Integer max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned, precision);
      this.min = min;
      this.max = max;
    }

    protected MEDIUMINT(final MEDIUMINT copy) {
      super(copy, copy.unsigned(), copy.precision());
      this.min = null;
      this.max = null;
    }

    public MEDIUMINT(final int precision, final boolean unsigned) {
      super(unsigned, (short)precision);
      this.min = null;
      this.max = null;
    }

    public MEDIUMINT(final int precision) {
      this(precision, false);
    }

    public MEDIUMINT(final Integer value) {
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
    public final void set(final Integer value) {
      if (value != null) {
        if (unsigned() && value > 8388607)
          throw new IllegalArgumentException("value is out of range for UNSIGNED MEDIUMINT: " + value);

        if (value > 16777215)
          throw new IllegalArgumentException("value is out of range for MEDIUMINT: " + value);

        if (value < -8388608)
          throw new IllegalArgumentException("value is out of range for MEDIUMINT: " + value);
      }

      super.set(value);
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareInt24(precision(), unsigned());
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
      this.value = resultSet.wasNull() ? null : resultSet.getInt(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.ApproxNumeric)
        return new type.DOUBLE();

      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      if (dataType instanceof type.INT)
        return new type.INT(Math.max(precision(), ((type.INT)dataType).precision()));

      if (dataType instanceof type.BIGINT)
        return new type.BIGINT(Math.max(precision(), ((type.BIGINT)dataType).precision()));

      if (dataType instanceof type.ExactNumeric)
        return new type.MEDIUMINT(Math.max(precision(), ((type.ExactNumeric<?>)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final MEDIUMINT clone() {
      return new MEDIUMINT(this);
    }
  }

  public static abstract class Numeric<T extends Number> extends DataType<T> {
    protected static final ThreadLocal<DecimalFormat> numberFormat = Formats.createDecimalFormat("###############.###############;-###############.###############");

    private final boolean unsigned;

    protected Numeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean unsigned) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.unsigned = unsigned;
    }

    protected Numeric(final Numeric<T> copy, final boolean unsigned) {
      super(copy);
      this.unsigned = unsigned;
    }

    protected Numeric(final boolean unsigned) {
      super();
      this.unsigned = unsigned;
    }

    public final boolean unsigned() {
      return unsigned;
    }

    @Override
    public void set(final T value) {
      if (value != null && value.doubleValue() < 0 && unsigned)
        throw new IllegalArgumentException("Attempt to set negative value for unsigned type: " + value);

      super.set(value);
    }
  }

  public static abstract class ExactNumeric<T extends Number> extends Numeric<T> {
    protected static final ThreadLocal<DecimalFormat> numberFormat = Formats.createDecimalFormat("###############.###############;-###############.###############");

    private final short precision;

    protected ExactNumeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final boolean unsigned, final int precision) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned);
      this.precision = (short)precision;
    }

    protected ExactNumeric(final Numeric<T> copy, final boolean unsigned, final int precision) {
      super(copy, unsigned);
      this.precision = (short)precision;
    }

    protected ExactNumeric(final boolean unsigned, final int precision) {
      super(unsigned);
      this.precision = (short)precision;
      if (precision <= 0)
        throw new IllegalArgumentException("precision must be >= 1");
    }

    public final short precision() {
      return precision;
    }
  }

  public static abstract class Serial<T> extends DataType<T> {
    private final short length;

    protected Serial(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = (short)length;
    }

    protected Serial(final Serial<T> copy) {
      super(copy);
      this.length = copy.length;
    }

    protected Serial(final int length) {
      super();
      this.length = (short)length;
    }

    public final short length() {
      return length;
    }
  }

  public static final class SMALLINT extends ExactNumeric<Short> {
    private final Short min;
    private final Short max;

    protected SMALLINT(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final int precision, final boolean unsigned, final Short min, final Short max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate, unsigned, precision);
      this.min = min;
      this.max = max;
    }

    protected SMALLINT(final SMALLINT copy) {
      super(copy, copy.unsigned(), copy.precision());
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final int precision, final boolean unsigned) {
      super(unsigned, (short)precision);
      this.min = null;
      this.max = null;
    }

    public SMALLINT(final int precision) {
      this(precision, false);
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
    public final void set(final Short value) {
      if (value != null) {
        if (unsigned() && value > 127)
          throw new IllegalArgumentException("value is out of range for UNSIGNED SMALLINT: " + value);

        if (value > 255)
          throw new IllegalArgumentException("value is out of range for SMALLINT: " + value);

        if (value < -128)
          throw new IllegalArgumentException("value is out of range for SMALLINT: " + value);
      }

      super.set(value);
    }

    @Override
    protected final String declare(final DBVendor vendor) {
      return vendor.getSQLSpec().declareInt8(precision(), unsigned());
    }

    @Override
    protected final int sqlType() {
      return Types.SMALLINT;
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
      this.value = resultSet.wasNull() ? null : resultSet.getShort(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.FLOAT)
        return new type.FLOAT();

      if (dataType instanceof type.DOUBLE)
        return new type.DOUBLE();

      if (dataType instanceof type.SMALLINT)
        return new type.SMALLINT(Math.max(precision(), ((type.SMALLINT)dataType).precision()));

      if (dataType instanceof type.MEDIUMINT)
        return new type.MEDIUMINT(Math.max(precision(), ((type.MEDIUMINT)dataType).precision()));

      if (dataType instanceof type.INT)
        return new type.INT(Math.max(precision(), ((type.INT)dataType).precision()));

      if (dataType instanceof type.BIGINT)
        return new type.BIGINT(Math.max(precision(), ((type.BIGINT)dataType).precision()));

      if (dataType instanceof type.DECIMAL) {
        final type.DECIMAL decimal = (type.DECIMAL)dataType;
        return new type.DECIMAL(Math.max(precision(), decimal.precision()), decimal.scale());
      }

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final SMALLINT clone() {
      return new SMALLINT(this);
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
      if (dataType instanceof type.Textual)
        return new type.CHAR(Math.max(length(), ((type.Textual<?>)dataType).length()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }
  }

  public static final class TIME extends Temporal<LocalTime> {
    private static final short DEFAULT_PRECISION = 6;

    protected static final DateTimeFormatter timeFormat = DateTimeFormatter.ISO_LOCAL_TIME;

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
      return vendor.getSQLSpec().declareTime(precision);
    }

    @Override
    protected final int sqlType() {
      return Types.TIME;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setTime(parameterIndex, java.sql.Time.valueOf(value));
      else
        statement.setNull(parameterIndex, sqlType());
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final java.sql.Time time = resultSet.getTime(columnIndex);
      this.value = time == null ? null : time.toLocalTime();
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final DataType<?> scaleTo(final DataType<?> dataType) {
      if (dataType instanceof type.TIME)
        return new type.DATETIME(Math.max(precision(), ((type.TIME)dataType).precision()));

      throw new IllegalArgumentException("type." + getClass().getSimpleName() + " cannot be scaled against type." + dataType.getClass().getSimpleName());
    }

    @Override
    public final TIME clone() {
      return new TIME(this);
    }
  }
}