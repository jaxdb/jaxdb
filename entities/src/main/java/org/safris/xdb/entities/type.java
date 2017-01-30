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

import org.safris.commons.lang.Classes;
import org.safris.commons.util.DateUtil;
import org.safris.commons.util.Formats;
import org.safris.xdb.schema.DBVendor;

public final class type {
  protected static final Map<Class<?>,Class<?>> typeToClass = new HashMap<Class<?>,Class<?>>();

  static {
    final Class<?>[] classes = type.class.getClasses();
    typeToClass.put(null, type.Enum.class);
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

  public static final class Array<T> extends DataType<T[]> {
    public static final Array<Object> type = new Array<Object>(null);

    protected final DataType<T> dataType;

    public Array(final Entity owner, final String name, final T[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T[]> generateOnInsert, final GenerateOn<? super T[]> generateOnUpdate, final Class<? extends DataType<T>> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      try {
        this.dataType = type.newInstance();
      }
      catch (final ReflectiveOperationException e) {
        throw new UnsupportedOperationException(e);
      }
    }

    public Array(final Entity owner, final Class<? extends DataType<T>> type) {
      this(owner, null, null, false, false, true, null, null, type);
    }

    public Array(final Class<? extends DataType<T>> type) {
      this(null, type);
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
    @SuppressWarnings("unchecked")
    protected final Array<T> clone() {
      return new Array<T>((Class<? extends DataType<T>>)dataType.getClass());
    }

    @Override
    @SuppressWarnings("unchecked")
    // NOTE: This creates a new instance that has the same type as this instance
    protected final Array<T> newInstance(final Entity owner) {
      return new Array<T>(owner, (Class<? extends DataType<T>>)dataType.getClass());
    }
  }

  public static final class BigInt extends Numeric<BigInteger> {
    public static final BigInt type = new BigInt();

    public final int precision;
    public final boolean unsigned;
    public final BigInteger min;
    public final BigInteger max;

    // FIXME: This is not properly supported by Derby, as in derby, only signed numbers are allowed. But in MySQL, unsigned values of up to 18446744073709551615 are allowed.
    public BigInt(final Entity owner, final String name, final BigInteger _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super BigInteger> generateOnInsert, final GenerateOn<? super BigInteger> generateOnUpdate, final int precision, final boolean unsigned, final BigInteger min, final BigInteger max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public BigInt(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, false, null, null);
    }

    public BigInt() {
      this(null);
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
      else if (value instanceof java.lang.Long)
        this.value = BigInteger.valueOf((java.lang.Long)value);
      else
        throw new UnsupportedOperationException("Unsupported class for BigInt data type: " + value.getClass().getName());
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final BigInt clone() {
      return new BigInt(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final BigInt newInstance(final Entity owner) {
      return new BigInt(owner);
    }
  }

  public static final class Binary extends Serial<byte[]> {
    public static final Binary type = new Binary();

    public final int length;
    public final boolean varying;

    public Binary(final Entity owner, final String name, final byte[] _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super byte[]> generateOnInsert, final GenerateOn<? super byte[]> generateOnUpdate, final int length, final boolean varying) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = length;
      this.varying = varying;
    }

    public Binary(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, true);
    }

    public Binary() {
      this(null);
    }

    @Override
    protected final int sqlType() {
      return varying ? Types.VARBINARY : Types.BINARY;
    }

    @Override
    protected final void get(final PreparedStatement statement, final int parameterIndex) throws SQLException {
      if (value != null)
        statement.setBytes(parameterIndex, value);
      else
        // FIXME: Does it matter if we know if this is BIT, BINARY, VARBINARY, or LONGVARBINARY?
        statement.setNull(parameterIndex, statement.getParameterMetaData().getParameterType(parameterIndex));
    }

    @Override
    protected final void set(final ResultSet resultSet, final int columnIndex) throws SQLException {
      final int columnType = resultSet.getMetaData().getColumnType(columnIndex);
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
    protected final Binary clone() {
      return new Binary(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, length, varying);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Binary newInstance(final Entity owner) {
      return new Binary(owner);
    }
  }

  public static final class Blob extends LargeObject<InputStream> {
    public static final Blob type = new Blob();

    public final int length;

    public Blob(final Entity owner, final String name, final InputStream _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super InputStream> generateOnInsert, final GenerateOn<? super InputStream> generateOnUpdate, final int length) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = length;
    }

    public Blob(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE);
    }

    public Blob() {
      this(null);
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
    protected final Blob clone() {
      return new Blob(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, length);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Blob newInstance(final Entity owner) {
      return new Blob(owner);
    }
  }

  public static class Boolean extends Condition<java.lang.Boolean> {
    public static final Boolean type = new Boolean();

    public Boolean(final Entity owner, final String name, final java.lang.Boolean _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super java.lang.Boolean> generateOnInsert, final GenerateOn<? super java.lang.Boolean> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    public Boolean(final Entity owner) {
      this(null, null, null, false, false, true, null, null);
    }

    public Boolean() {
      this(null);
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
    protected final Boolean clone() {
      return new Boolean(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Boolean newInstance(final Entity owner) {
      return new Boolean(owner);
    }
  }

  public static final class Char extends Textual<String> {
    public static final Char type = new Char();

    public final int length;
    public final boolean varying;
    public final boolean national;

    public Char(final Entity owner, final String name, final String _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super String> generateOnInsert, final GenerateOn<? super String> generateOnUpdate, final int length, final boolean varying, final boolean national) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = length;
      this.varying = varying;
      this.national = national;
    }

    public Char(final Entity owner) {
      this(owner, null, null, false, false, true, null, null, Integer.MAX_VALUE, true, false);
    }

    public Char() {
      this((Entity)null);
    }

    @Override
    protected final int sqlType() {
      return varying ? (national ? Types.NVARCHAR : Types.VARCHAR) : (national ? Types.NCHAR : Types.CHAR);
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
    protected final Char clone() {
      return new Char(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, length, varying, national);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Char newInstance(final Entity owner) {
      return new Char(owner);
    }
  }

  public static final class Clob extends LargeObject<Reader> {
    public static final Clob type = new Clob();

    public final int length;
    public final boolean national;

    public Clob(final Entity owner, final String name, final Reader _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Reader> generateOnInsert, final GenerateOn<? super Reader> generateOnUpdate, final int length, final boolean national) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.length = length;
      this.national = national;
    }

    public Clob(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, false);
    }

    public Clob() {
      this(null);
    }

    @Override
    protected final int sqlType() {
      return national ? Types.NCLOB : Types.CLOB;
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
    protected final Clob clone() {
      return new Clob(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, length, national);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Clob newInstance(final Entity owner) {
      return new Clob(owner);
    }
  }

  public static final class Date extends Temporal<LocalDate> {
    public static final Date type = new Date();

    protected static final DateTimeFormatter dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;

    public Date(final Entity owner, final String name, final LocalDate _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDate> generateOnInsert, final GenerateOn<? super LocalDate> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    public Date(final Entity owner) {
      this(null, null, null, false, false, true, null, null);
    }

    public Date() {
      this(null);
    }

    @Override
    protected final int sqlType() {
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
    protected final Date clone() {
      return new Date(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Date newInstance(final Entity owner) {
      return new Date(owner);
    }
  }

  public static class DateTime extends Temporal<LocalDateTime> {
    public static final DateTime type = new DateTime();

    protected static final DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");

    public DateTime(final Entity owner, final String name, final LocalDateTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalDateTime> generateOnInsert, final GenerateOn<? super LocalDateTime> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    public DateTime(final Entity owner) {
      this(null, null, null, false, false, true, null, null);
    }

    public DateTime() {
      this(null);
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
    protected DateTime clone() {
      return new DateTime(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final DateTime newInstance(final Entity owner) {
      return new DateTime(owner);
    }
  }

  public static class Decimal extends Numeric<java.lang.Double> {
    public static final Decimal type = new Decimal();

    public final int precision;
    public int decimal;
    public final boolean unsigned;
    public final java.lang.Double min;
    public final java.lang.Double max;

    public Decimal(final Entity owner, final String name, final java.lang.Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super java.lang.Double> generateOnInsert, final GenerateOn<? super java.lang.Double> generateOnUpdate, final int precision, final int decimal, final boolean unsigned, final java.lang.Double min, final java.lang.Double max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public Decimal(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, Integer.MAX_VALUE, false, java.lang.Double.MIN_VALUE, java.lang.Double.MAX_VALUE);
    }

    public Decimal() {
      this(null);
    }

    @Override
    protected final int sqlType() {
      return Types.DECIMAL;
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
    protected Decimal clone() {
      return new Decimal(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, decimal, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Decimal newInstance(final Entity owner) {
      return new Decimal(owner);
    }
  }

  public static final class Double extends Numeric<java.lang.Double> {
    public static final Double type = new Double();

    public final int precision;
    public int decimal;
    public final boolean unsigned;
    public final java.lang.Double min;
    public final java.lang.Double max;

    public Double(final Entity owner, final String name, final java.lang.Double _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super java.lang.Double> generateOnInsert, final GenerateOn<? super java.lang.Double> generateOnUpdate, final int precision, final int decimal, final boolean unsigned, final java.lang.Double min, final java.lang.Double max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public Double(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, Integer.MAX_VALUE, false, java.lang.Double.MIN_VALUE, java.lang.Double.MAX_VALUE);
    }

    public Double() {
      this(null);
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
    protected final Double clone() {
      return new Double(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, decimal, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Double newInstance(final Entity owner) {
      return new Double(owner);
    }
  }

  public static final class Enum<T extends java.lang.Enum<?>> extends Textual<T> {
    public static final Enum<java.lang.Enum<?>> type = new Enum<java.lang.Enum<?>>(null);

    private final Class<T> enumClass;

    public Enum(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate, final Class<T> type) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.enumClass = type;
    }

    public Enum(final Entity owner, final Class<T> type) {
      this(owner, null, null, false, false, true, null, null, type);
    }

    public Enum(final Class<T> type) {
      this(null, type);
    }

    @Override
    protected final Class<T> type() {
      return enumClass;
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

      for (final T constant : enumClass.getEnumConstants()) {
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
    protected final Enum<T> clone() {
      return new Enum<T>(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, enumClass);
    }

    @Override
    @SuppressWarnings("unchecked")
    // NOTE: This creates a new instance that has the same type as this instance
    protected final Enum<T> newInstance(final Entity owner) {
      return new Enum<T>(owner, enumClass);
    }
  }

  public static final class Float extends Numeric<java.lang.Float> {
    public static final Float type = new Float();

    public final int precision;
    public int decimal;
    public final boolean unsigned;
    public final java.lang.Float min;
    public final java.lang.Float max;

    public Float(final Entity owner, final String name, final java.lang.Float _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super java.lang.Float> generateOnInsert, final GenerateOn<? super java.lang.Float> generateOnUpdate, final int precision, final int decimal, final boolean unsigned, final java.lang.Float min, final java.lang.Float max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public Float(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, Integer.MAX_VALUE, false, java.lang.Float.MIN_VALUE, java.lang.Float.MAX_VALUE);
    }

    public Float() {
      this(null);
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
    protected final Float clone() {
      return new Float(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, decimal, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Float newInstance(final Entity owner) {
      return new Float(owner);
    }
  }

  public static abstract class LargeObject<T> extends DataType<T> {
    protected LargeObject(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }
  }

  public static final class Long extends Numeric<java.lang.Long> {
    public static final Long type = new Long();

    public final int precision;
    public final boolean unsigned;
    public final java.lang.Long min;
    public final java.lang.Long max;

    public Long(final Entity owner, final String name, final java.lang.Long _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super java.lang.Long> generateOnInsert, final GenerateOn<? super java.lang.Long> generateOnUpdate, final int precision, final boolean unsigned, final java.lang.Long min, final java.lang.Long max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public Long(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, false, java.lang.Long.MIN_VALUE, java.lang.Long.MAX_VALUE);
    }

    public Long() {
      this(null);
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
    protected final Long clone() {
      return new Long(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Long newInstance(final Entity owner) {
      return new Long(owner);
    }
  }

  public static final class MediumInt extends Numeric<Integer> {
    public static final MediumInt type = new MediumInt();

    public final int precision;
    public final boolean unsigned;
    public final Integer min;
    public final Integer max;

    public MediumInt(final Entity owner, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Integer> generateOnInsert, final GenerateOn<? super Integer> generateOnUpdate, final int precision, final boolean unsigned, final Integer min, final Integer max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public MediumInt(final Entity owner) {
      this(null, null, null, false, false, true, null, null, Integer.MAX_VALUE, false, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    public MediumInt() {
      this(null);
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
      this.value = resultSet.wasNull() ? null : resultSet.getInt(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final MediumInt clone() {
      return new MediumInt(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final MediumInt newInstance(final Entity owner) {
      return new MediumInt(owner);
    }
  }

  public static abstract class Numeric<T extends Number> extends DataType<T> {
    protected static final ThreadLocal<DecimalFormat> numberFormat = Formats.createDecimalFormat("###############.###############;-###############.###############");

    protected Numeric(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }
  }

  public static abstract class Serial<T> extends DataType<T> {
    protected Serial(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }
  }

  public static final class SmallInt extends Numeric<Short> {
    public static final SmallInt type = new SmallInt();

    public final int precision;
    public final boolean unsigned;
    public final Short min;
    public final Short max;

    public SmallInt(final Entity owner, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super Short> generateOnInsert, final GenerateOn<? super Short> generateOnUpdate, final int precision, final boolean unsigned, final Short min, final Short max) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
      this.precision = precision;
      this.unsigned = unsigned;
      this.min = min;
      this.max = max;
    }

    public SmallInt(final Entity owner) {
      this(owner, null, null, false, false, true, null, null, Integer.MAX_VALUE, false, java.lang.Short.MIN_VALUE, java.lang.Short.MAX_VALUE);
    }

    public SmallInt() {
      this(null);
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
      this.value = resultSet.wasNull() ? null : resultSet.getShort(columnIndex);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final SmallInt clone() {
      return new SmallInt(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate, precision, unsigned, min, max);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final SmallInt newInstance(final Entity owner) {
      return new SmallInt(owner);
    }
  }

  public static abstract class Temporal<T extends java.time.temporal.Temporal> extends DataType<T> {
    protected Temporal(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }
  }

  public static abstract class Textual<T> extends DataType<T> {
    protected Textual(final Entity owner, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super T> generateOnInsert, final GenerateOn<? super T> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    protected Textual(final Entity owner) {
      super(owner);
    }

    protected Textual() {
      super();
    }
  }

  public static final class Time extends Temporal<LocalTime> {
    public static final Time type = new Time();

    protected static final DateTimeFormatter timeFormat = DateTimeFormatter.ISO_LOCAL_TIME;

    public Time(final Entity owner, final String name, final LocalTime _default, final boolean unique, final boolean primary, final boolean nullable, final GenerateOn<? super LocalTime> generateOnInsert, final GenerateOn<? super LocalTime> generateOnUpdate) {
      super(owner, name, _default, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    public Time(final Entity owner) {
      this(owner, null, null, false, false, true, null, null);
    }

    public Time() {
      this(null);
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
      this.value = time == null ? null : LocalTime.ofNanoOfDay(time.getTime() * DateUtil.NANOSECONDS_IN_MILLISECONDS);
    }

    @Override
    protected final String serialize(final DBVendor vendor) throws IOException {
      return Serializer.getSerializer(vendor).serialize(this);
    }

    @Override
    protected final Time clone() {
      return new Time(owner, name, value, unique, primary, nullable, generateOnInsert, generateOnUpdate);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected final Time newInstance(final Entity owner) {
      return new Time(owner);
    }
  }
}