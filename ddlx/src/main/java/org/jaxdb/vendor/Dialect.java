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

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Map;

import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Schema;
import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Table;
import org.jaxsb.runtime.Binding;
import org.libj.lang.Hexadecimal;
import org.libj.lang.Strings;
import org.libj.util.DecimalFormatter;
import org.openjax.xml.api.CharacterDatas;
import org.w3.www._2001.XMLSchema.yAA;

public abstract class Dialect extends DbVendorCompiler {
  // FIXME: Remove this hack!
  private abstract static class BindingProxy extends Binding {
    protected static yAA.$AnyType<?> owner(final Binding binding) {
      return Binding.owner(binding);
    }
  }

  protected Dialect(final DbVendor vendor) {
    super(vendor);
  }

  void assertValidDecimal(final Integer precision, final Integer scale) {
    if (precision != null && precision > decimalMaxPrecision())
      throw new IllegalArgumentException("DECIMAL precision (" + precision + ") exceeds maximum (" + decimalMaxPrecision() + ") allowed by " + getVendor());

    if (scale != null && decimalMaxScale() != null && scale > decimalMaxScale())
      throw new IllegalArgumentException("DECIMAL precision (" + scale + ") exceeds maximum (" + decimalMaxPrecision() + ") allowed by " + getVendor());

    if (precision != null && scale != null && precision < scale)
      throw new IllegalArgumentException("Illegal DECIMAL(M,S) declaration: M [" + precision + "] must be >= S [" + scale + "]");
  }

  public static StringBuilder getTypeName(final StringBuilder b, final $Enum column, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    if (column.getTemplate$() != null)
      return b.append("ty_").append(column.getTemplate$().text());

    final yAA.$AnyType<?> owner = BindingProxy.owner(column);
    if (owner instanceof $Schema)
      return b.append("ty_").append(column.getName$().text());

    String tableName = (($Table)owner).getName$().text();
    if (tableNameToEnumToOwner != null)
      tableName = tableNameToEnumToOwner.get(tableName).get(column.getName$().text());

    return b.append("ty_").append(tableName).append('_').append(column.getName$().text());
  }

  public static String[] parseEnum(String value) {
    value = value.replace("\\\\", "\\");
    value = CharacterDatas.unescapeFromAttr(new StringBuilder(), value, '"').toString();
    final StringBuilder builder = new StringBuilder();
    return parseEnum(builder, value, 0, 0);
  }

  private static String[] parseEnum(final StringBuilder builder, final String value, final int index, final int depth) {
    boolean escaped = false;
    for (int i = index, i$ = value.length(); i < i$; ++i) { // [N]
      final char ch = value.charAt(i);
      if (ch == '\\') {
        escaped = true;
      }
      else if (ch != ' ' || escaped) {
        escaped = false;
        builder.append(ch);
      }
      else if (builder.length() > 0) {
        final String enm = builder.toString();
        builder.setLength(0);
        final String[] enums = parseEnum(builder, value, i + 1, depth + 1);
        enums[depth] = enm;
        return enums;
      }
    }

    if (builder.length() > 0) {
      final String[] enums = new String[depth + 1];
      enums[depth] = builder.toString();
      builder.setLength(0);
      return enums;
    }

    return depth > 0 ? new String[depth] : Strings.EMPTY_ARRAY;
  }

  public static final ThreadLocal<DecimalFormat> NUMBER_FORMAT = DecimalFormatter.createDecimalFormat("################.################;-################.################");
  private static final DateTimeFormatter DATE_PARSE = new DateTimeFormatterBuilder().appendPattern("yyyy-M-d").toFormatter();
  private static final DateTimeFormatter DATE_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd").toFormatter();
  private static final DateTimeFormatter TIME_PARSE = new DateTimeFormatterBuilder().appendPattern("H:m:s").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();
  private static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("HH:mm:ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();
  private static final DateTimeFormatter DATETIME_PARSE = new DateTimeFormatterBuilder().appendPattern("yyyy-M-d H:m:s").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();
  private static final DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").optionalStart().appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).optionalEnd().toFormatter();

  public static String timeToString(final LocalTime temporal) {
    return TIME_FORMAT.format(temporal);
  }

  public static LocalTime timeFromString(final String str) {
    return LocalTime.parse(str, TIME_PARSE);
  }

  public static String dateToString(final LocalDate temporal) {
    return DATE_FORMAT.format(temporal);
  }

  public static LocalDate dateFromString(final String str) {
    return LocalDate.parse(str, DATE_PARSE);
  }

  public static String dateTimeToString(final LocalDateTime temporal) {
    return DATETIME_FORMAT.format(temporal);
  }

  public static LocalDateTime dateTimeFromString(final String str) {
    return LocalDateTime.parse(str, DATETIME_PARSE);
  }

  public abstract short constraintNameMaxLength();

  /**
   * Quote a named identifier.
   *
   * @param b The {@link StringBuilder} in which to quote the provided {@code identifier}.
   * @param identifier The identifier.
   * @return The provided {@link StringBuilder}.
   */
  public abstract StringBuilder quoteIdentifier(StringBuilder b, CharSequence identifier);

  public abstract StringBuilder currentTimeFunction(StringBuilder b);
  public abstract StringBuilder currentDateFunction(StringBuilder b);
  public abstract StringBuilder currentDateTimeFunction(StringBuilder b);

  public abstract StringBuilder currentTimestampMinutesFunction(StringBuilder b);
  public abstract StringBuilder currentTimestampSecondsFunction(StringBuilder b);
  public abstract StringBuilder currentTimestampMillisecondsFunction(StringBuilder b);

  public abstract byte minTinyint();
  public abstract byte maxTinyint();
  public abstract short minSmallint();
  public abstract short maxSmallint();
  public abstract int minInt();
  public abstract int maxInt();
  public abstract long minBigint();
  public abstract long maxBigint();

  abstract StringBuilder declareBinary(StringBuilder b, boolean varying, long length);
  abstract Integer binaryMaxLength();

  // FIXME: Change long to Long, and declare a default value if null
  public StringBuilder compileBinary(final StringBuilder b, final boolean varying, final long length) {
    if (binaryMaxLength() != null && length > binaryMaxLength())
      throw new IllegalArgumentException("BINARY length (" + length + ") is greater than maximum (" + binaryMaxLength() + ") allowed by " + getVendor());

    return declareBinary(b, varying, length);
  }

  abstract StringBuilder declareBlob(StringBuilder b, Long length);
  abstract Long blobMaxLength();

  public StringBuilder compileBlob(final StringBuilder b, final Long length) {
    if (length != null && blobMaxLength() != null && length > blobMaxLength())
      throw new IllegalArgumentException("BLOB length (" + length + ") is greater than maximum (" + blobMaxLength() + ") allowed by " + getVendor());

    return declareBlob(b, length);
  }

  public abstract StringBuilder declareBoolean(StringBuilder b);

  abstract StringBuilder declareChar(StringBuilder b, boolean varying, long length);
  abstract Integer charMaxLength();

  public StringBuilder compileChar(final StringBuilder b, final boolean varying, final Long length) {
    if (length != null && charMaxLength() != null && length > charMaxLength())
      throw new IllegalArgumentException("CHAR length (" + length + ") is greater than maximum (" + charMaxLength() + ") allowed by " + getVendor());

    return declareChar(b, varying, length == null ? 1L : length);
  }

  abstract StringBuilder declareClob(StringBuilder b, Long length);
  abstract Long clobMaxLength();

  public StringBuilder compileClob(final StringBuilder b, final Long length) {
    if (length != null && clobMaxLength() != null && length > clobMaxLength())
      throw new IllegalArgumentException("CLOB length (" + length + ") is greater than maximum (" + clobMaxLength() + ") allowed by " + getVendor());

    return declareClob(b, length);
  }

  public abstract StringBuilder declareDate(StringBuilder b);

  public abstract StringBuilder declareDateTime(StringBuilder b, Byte precision);

  public abstract StringBuilder declareDecimal(StringBuilder b, Integer precision, Integer scale, BigDecimal min);
  public abstract int decimalMaxPrecision();
  abstract Integer decimalMaxScale();

  public abstract StringBuilder declareFloat(StringBuilder b, Float min);

  public abstract StringBuilder declareDouble(StringBuilder b, Double min);

  abstract StringBuilder declareInt8(StringBuilder b, Byte precision, Byte min);

  static final byte int8SignedMaxPrecision = 3;

  public StringBuilder compileInt8(final StringBuilder b, final Byte precision, final Byte min) {
    if (precision != null && precision > Dialect.int8SignedMaxPrecision)
      throw new IllegalArgumentException("TINYINT precision (" + precision + ") is greater than maximum (" + Dialect.int8SignedMaxPrecision + ")");

    return declareInt8(b, precision, min);
  }

  abstract StringBuilder declareInt16(StringBuilder b, Byte precision, Short min);

  static final byte int16SignedMaxPrecision = 5;

  public StringBuilder compileInt16(final StringBuilder b, final Byte precision, final Short min) {
    if (precision != null && precision > Dialect.int16SignedMaxPrecision)
      throw new IllegalArgumentException("SMALLINT precision (" + precision + ") is greater than maximum (" + Dialect.int16SignedMaxPrecision + ")");

    return declareInt16(b, precision, min);
  }

  abstract StringBuilder declareInt32(StringBuilder b, Byte precision, Integer min);

  static final byte int32SignedMaxPrecision = 10;

  public StringBuilder compileInt32(final StringBuilder b, final Byte precision, final Integer min) {
    if (precision != null && precision > Dialect.int32SignedMaxPrecision)
      throw new IllegalArgumentException("INT precision (" + precision + ") is greater than maximum (" + Dialect.int32SignedMaxPrecision + ")");

    return declareInt32(b, precision, min);
  }

  abstract StringBuilder declareInt64(StringBuilder b, Byte precision, Long min);

  static final byte int64SignedMaxPrecision = 19;

  public StringBuilder compileInt64(final StringBuilder b, Byte precision, final Long min) {
    if (precision != null && precision > Dialect.int64SignedMaxPrecision)
      throw new IllegalArgumentException("BIGINT precision (" + precision + ") is greater than maximum (" + Dialect.int64SignedMaxPrecision + ")");

    return declareInt64(b, precision, min);
  }

  public abstract StringBuilder declareTime(StringBuilder b, Byte precision);
  public abstract StringBuilder declareInterval(StringBuilder b);
  public abstract StringBuilder declareEnum(StringBuilder b, $Enum column, String enumValues, Map<String,Map<String,String>> tableNameToEnumToOwner);

  public byte[] stringLiteralToBinary(final String str) {
    if (!str.startsWith("X'") || !str.endsWith("'"))
      throw new IllegalArgumentException();

    return Hexadecimal.decode(str, 2, str.length() - 1);
  }

  public final StringBuilder binaryToStringLiteral(final StringBuilder b, final byte[] bytes) {
    return hexStringToStringLiteral(b, Hexadecimal.encode(bytes));
  }

  public StringBuilder hexStringToStringLiteral(final StringBuilder b, final String hex) {
    return b.append("X'").append(hex).append('\'');
  }
}