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

package org.libx4j.rdb.vendor;

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.lib4j.util.NumberFormatter;
import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;

public abstract class Dialect {
  protected void assertValidDecimal(final Short precision, final Short scale) {
    if (precision != null && precision > decimalMaxPrecision())
      throw new IllegalArgumentException("DECIMAL precision of " + precision + " exceeds max of " + decimalMaxPrecision() + " allowed by " + getVendor());

    if (scale != null && decimalMaxScale() != null && scale > decimalMaxScale())
      throw new IllegalArgumentException("DECIMAL precision of " + scale + " exceeds max of " + decimalMaxPrecision() + " allowed by " + getVendor());

    if (precision != null && scale != null && precision < scale)
      throw new IllegalArgumentException("Illegal DECIMAL(M,S) declaration: M [" + precision + "] must be >= S [" + scale + "]");
  }

  public static String getTypeName(final String tableName, final String columnName) {
    return "ty_" + tableName + "_" + columnName;
  }

  public static List<String> parseEnum(final String value) {
    final List<String> enums = new ArrayList<String>();
    final char[] chars = value.replace("\\\\", "\\").toCharArray();
    final StringBuilder builder = new StringBuilder();
    boolean escaped = false;
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
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
  public static final ThreadLocal<DecimalFormat> NUMBER_FORMAT = NumberFormatter.createDecimalFormat("################.################;-################.################");
  public static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("H:m:s").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();
  public static final DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd H:m:s").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

  protected abstract DBVendor getVendor();

  public abstract boolean allowsUnsignedNumeric();

  protected abstract String declareBinary(final boolean varying, final int length);
  protected abstract Integer binaryMaxLength();
  public String compileBinary(final boolean varying, final Integer length) {
    if (length != null && binaryMaxLength() != null && length > binaryMaxLength())
      throw new IllegalArgumentException("BINARY length of " + length + " exceeds max of " + binaryMaxLength() + " allowed by " + getVendor());

    return declareBinary(varying, length);
  }

  protected abstract String declareBlob(final Long length);
  protected abstract Long blobMaxLength();
  public String compileBlob(final Long length) {
    if (length != null && blobMaxLength() != null && length > blobMaxLength())
      throw new IllegalArgumentException("BLOB length of " + length + " exceeds max of " + blobMaxLength() + " allowed by " + getVendor());

    return declareBlob(length);
  }

  public abstract String declareBoolean();

  protected abstract String declareChar(final boolean varying, final int length);
  protected abstract Integer charMaxLength();
  public String compileChar(final boolean varying, final int length) {
    if (charMaxLength() != null && length > charMaxLength())
      throw new IllegalArgumentException("CHAR length of " + length + " exceeds max of " + charMaxLength() + " allowed by " + getVendor());

    return declareChar(varying, length);
  }

  protected abstract String declareClob(final Long length);
  protected abstract Long clobMaxLength();
  public String compileClob(final Long length) {
    if (length != null && clobMaxLength() != null && length > clobMaxLength())
      throw new IllegalArgumentException("CLOB length of " + length + " exceeds max of " + clobMaxLength() + " allowed by " + getVendor());

    return declareClob(length);
  }

  public abstract String declareDate();

  public abstract String declareDateTime(final byte precision);

  public abstract String declareDecimal(final Short precision, final Short scale, final boolean unsigned);
  public abstract short decimalMaxPrecision();
  protected abstract Integer decimalMaxScale();

  public abstract String declareFloat(final boolean doublePrecision, final boolean unsigned);

  protected abstract String declareInt8(final byte precision, final boolean unsigned);
  protected static final byte int8SignedMaxPrecision = 3;
  protected static final byte int8UnsignedMaxPrecision = 3;
  public String compileInt8(final byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int8UnsignedMaxPrecision : Dialect.int8SignedMaxPrecision;
    if (precision > maxPrecision)
      throw new IllegalArgumentException("TINYINT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt8(precision, unsigned);
  }

  protected abstract String declareInt16(final byte precision, final boolean unsigned);
  protected static final byte int16SignedMaxPrecision = 5;
  protected static final byte int16UnsignedMaxPrecision = 5;
  public String compileInt16(final byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int16UnsignedMaxPrecision : Dialect.int16SignedMaxPrecision;
    if (precision > maxPrecision)
      throw new IllegalArgumentException("SMALLINT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt16(precision, unsigned);
  }

  protected abstract String declareInt32(final byte precision, final boolean unsigned);
  protected static final byte int32SignedMaxPrecision = 10;
  protected static final byte int32UnsignedMaxPrecision = 10;
  public String compileInt32(final byte precision, final boolean unsigned) {
    final byte maxPrecision = unsigned ? Dialect.int32UnsignedMaxPrecision : Dialect.int32SignedMaxPrecision;
    if (precision > maxPrecision)
      throw new IllegalArgumentException("INT" + (unsigned ? " UNSIGNED" : "") + " precision of " + precision + " exceeds max of " + maxPrecision);

    return declareInt32(precision, unsigned);
  }

  protected abstract String declareInt64(final byte precision, final boolean unsigned);
  protected static final byte int64SignedMaxPrecision = 19;
  protected static final byte int64UnsignedMaxPrecision = 20;
  public String compileInt64(Byte precision, final boolean unsigned) {
    if (unsigned) {
      final byte maxPrecision = allowsUnsignedNumeric() ? Dialect.int64UnsignedMaxPrecision : Dialect.int64SignedMaxPrecision;
      if (precision == null)
        precision = maxPrecision;
      else if (precision > maxPrecision)
        throw new IllegalArgumentException("BIGINT UNSIGNED precision of " + precision + " exceeds max of " + maxPrecision + " allowed by " + getVendor());
    }
    else {
      if (precision == null)
        precision = Dialect.int64SignedMaxPrecision;
      else if (precision > Dialect.int64SignedMaxPrecision)
        throw new IllegalArgumentException("BIGINT precision of " + precision + " exceeds max of " + Dialect.int64SignedMaxPrecision);
    }

    return declareInt64(precision, unsigned);
  }

  public abstract String declareTime(final byte precision);
  public abstract String declareInterval();
  public abstract String declareEnum(final $ddlx_table table, final $ddlx_enum type);
}