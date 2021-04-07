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

package org.jaxdb.vendor;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Table;
import org.jaxsb.runtime.Binding;
import org.libj.math.BigInt;
import org.libj.util.DecimalFormatter;
import org.w3.www._2001.XMLSchema.yAA.$AnySimpleType;

public abstract class Dialect extends DBVendorSpecific {
  private abstract static class BindingProxy extends Binding {
    private static final long serialVersionUID = -5727439225507675790L;

    protected static $AnySimpleType owner(final Binding binding) {
      return Binding.owner(binding);
    }
  }

  void assertValidDecimal(final Integer precision, final Integer scale) {
    if (precision != null && precision > decimalMaxPrecision())
      throw new IllegalArgumentException("DECIMAL precision of " + precision + " exceeds max of " + decimalMaxPrecision() + " allowed by " + getVendor());

    if (scale != null && decimalMaxScale() != null && scale > decimalMaxScale())
      throw new IllegalArgumentException("DECIMAL precision of " + scale + " exceeds max of " + decimalMaxPrecision() + " allowed by " + getVendor());

    if (precision != null && scale != null && precision < scale)
      throw new IllegalArgumentException("Illegal DECIMAL(M,S) declaration: M [" + precision + "] must be >= S [" + scale + "]");
  }

  public static String getTypeName(final $Enum column) {
    return getTypeName(column.id() != null ? column.id() : (($Table)BindingProxy.owner(column)).getName$().text(), column.getName$().text());
  }

  public static String getTypeName(final String tableName, final String columnName) {
    return "ty_" + tableName + "_" + columnName;
  }

  public static List<String> parseEnum(final String value) {
    final List<String> enums = new ArrayList<>();
    final String str = value.replace("\\\\", "\\");
    final StringBuilder builder = new StringBuilder();
    boolean escaped = false;
    for (int i = 0, len = str.length(); i < len; ++i) {
      final char ch = str.charAt(i);
      if (ch == '\\') {
        escaped = true;
      }
      else if (ch != ' ' || escaped) {
        escaped = false;
        builder.append(ch);
      }
      else if (builder.length() > 0) {
        enums.add(builder.toString());
        builder.setLength(0);
      }
    }

    enums.add(builder.toString());
    return enums;
  }

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  public static final ThreadLocal<DecimalFormat> NUMBER_FORMAT = DecimalFormatter.createDecimalFormat("################.################;-################.################");
  public static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();
  public static final DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

  public abstract short constraintNameMaxLength();

  /**
   * Quote a named identifier.
   *
   * @param identifier The identifier.
   * @return The quoted identifier.
   */
  public abstract String quoteIdentifier(CharSequence identifier);

  public abstract String currentTimeFunction();
  public abstract String currentDateFunction();
  public abstract String currentDateTimeFunction();

  public abstract String currentTimestampMinutesFunction();
  public abstract String currentTimestampSecondsFunction();
  public abstract String currentTimestampMillisecondsFunction();

  public abstract boolean allowsUnsignedNumeric();

  public abstract byte minTinyint();
  public abstract byte maxTinyint();
  public abstract short maxTinyintUnsigned();
  public abstract short minSmallint();
  public abstract short maxSmallint();
  public abstract int maxSmallintUnsigned();
  public abstract int minInt();
  public abstract int maxInt();
  public abstract long maxIntUnsigned();
  public abstract long minBigint();
  public abstract long maxBigint();
  public abstract BigInt maxBigintUnsigned();

  abstract String declareBinary(boolean varying, long length);
  abstract Integer binaryMaxLength();
  // FIXME: Change long to Long, and declare a default value if null
  public String compileBinary(final boolean varying, final long length) {
    if (binaryMaxLength() != null && length > binaryMaxLength())
      throw new IllegalArgumentException("BINARY length of " + length + " exceeds max of " + binaryMaxLength() + " allowed by " + getVendor());

    return declareBinary(varying, length);
  }

  abstract String declareBlob(Long length);
  abstract Long blobMaxLength();
  public String compileBlob(final Long length) {
    if (length != null && blobMaxLength() != null && length > blobMaxLength())
      throw new IllegalArgumentException("BLOB length of " + length + " exceeds max of " + blobMaxLength() + " allowed by " + getVendor());

    return declareBlob(length);
  }

  public abstract String declareBoolean();

  abstract String declareChar(boolean varying, long length);
  abstract Integer charMaxLength();
  public String compileChar(final boolean varying, final Long length) {
    if (length != null && charMaxLength() != null && length > charMaxLength())
      throw new IllegalArgumentException("CHAR length of " + length + " exceeds max of " + charMaxLength() + " allowed by " + getVendor());

    return declareChar(varying, length == null ? 1L : length);
  }

  abstract String declareClob(Long length);
  abstract Long clobMaxLength();
  public String compileClob(final Long length) {
    if (length != null && clobMaxLength() != null && length > clobMaxLength())
      throw new IllegalArgumentException("CLOB length of " + length + " exceeds max of " + clobMaxLength() + " allowed by " + getVendor());

    return declareClob(length);
  }

  public abstract String declareDate();

  public abstract String declareDateTime(Byte precision);

  public abstract String declareDecimal(Integer precision, Integer scale, boolean unsigned);
  public abstract int decimalMaxPrecision();
  abstract Integer decimalMaxScale();

  public abstract String declareFloat(boolean unsigned);

  public abstract String declareDouble(boolean unsigned);

  abstract String declareInt8(Byte precision, boolean unsigned);
  static final byte int8SignedMaxPrecision = 3;
  static final byte int8UnsignedMaxPrecision = 3;
  public String compileInt8(final Byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int8UnsignedMaxPrecision : Dialect.int8SignedMaxPrecision;
    if (precision != null && precision > maxPrecision)
      throw new IllegalArgumentException("TINYINT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt8(precision, unsigned);
  }

  abstract String declareInt16(Byte precision, boolean unsigned);
  static final byte int16SignedMaxPrecision = 5;
  static final byte int16UnsignedMaxPrecision = 5;
  public String compileInt16(final Byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int16UnsignedMaxPrecision : Dialect.int16SignedMaxPrecision;
    if (precision != null && precision > maxPrecision)
      throw new IllegalArgumentException("SMALLINT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt16(precision, unsigned);
  }

  abstract String declareInt32(Byte precision, boolean unsigned);
  static final byte int32SignedMaxPrecision = 10;
  static final byte int32UnsignedMaxPrecision = 10;
  public String compileInt32(final Byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int32UnsignedMaxPrecision : Dialect.int32SignedMaxPrecision;
    if (precision != null && precision > maxPrecision)
      throw new IllegalArgumentException("INT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt32(precision, unsigned);
  }

  abstract String declareInt64(Byte precision, boolean unsigned);
  static final byte int64SignedMaxPrecision = 19;
  static final byte int64UnsignedMaxPrecision = 20;
  public String compileInt64(Byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int64UnsignedMaxPrecision : Dialect.int64SignedMaxPrecision;
    if (precision != null && precision > maxPrecision)
      throw new IllegalArgumentException("BIGINT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt64(precision, unsigned);
  }

  public abstract String declareTime(Byte precision);
  public abstract String declareInterval();
  public abstract String declareEnum($Enum type);
}