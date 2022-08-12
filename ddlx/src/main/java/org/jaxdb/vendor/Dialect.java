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
import java.util.ArrayList;
import java.util.Map;

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Schema;
import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Table;
import org.jaxsb.runtime.Binding;
import org.libj.lang.Hexadecimal;
import org.libj.util.DecimalFormatter;
import org.openjax.xml.api.CharacterDatas;
import org.w3.www._2001.XMLSchema.yAA.$AnyType;

public abstract class Dialect extends DBVendorBase {
  // FIXME: Remove this hack!
  private abstract static class BindingProxy extends Binding {
    protected static $AnyType<?> owner(final Binding binding) {
      return Binding.owner(binding);
    }
  }

  protected Dialect(final DBVendor vendor) {
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

  public static String getTypeName(final $Enum column, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    if (column.getTemplate$() != null)
      return "ty_" + column.getTemplate$().text();

    final $AnyType<?> owner = BindingProxy.owner(column);
    if (owner instanceof $Schema)
      return "ty_" + column.getName$().text();

    String tableName = (($Table)owner).getName$().text();
    if (tableNameToEnumToOwner != null)
      tableName = tableNameToEnumToOwner.get(tableName).get(column.getName$().text());

    return "ty_" + tableName + "_" + column.getName$().text();
  }

  public static ArrayList<String> parseEnum(String value) {
    value = value.replace("\\\\", "\\");
    value = CharacterDatas.unescapeFromAttr(new StringBuilder(), value, '"').toString();
    final ArrayList<String> enums = new ArrayList<>();
    final StringBuilder builder = new StringBuilder();
    boolean escaped = false;
    for (int i = 0, i$ = value.length(); i < i$; ++i) { // [N]
      final char ch = value.charAt(i);
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

  public abstract byte minTinyint();
  public abstract byte maxTinyint();
  public abstract short minSmallint();
  public abstract short maxSmallint();
  public abstract int minInt();
  public abstract int maxInt();
  public abstract long minBigint();
  public abstract long maxBigint();

  abstract String declareBinary(boolean varying, long length);
  abstract Integer binaryMaxLength();
  // FIXME: Change long to Long, and declare a default value if null
  public String compileBinary(final boolean varying, final long length) {
    if (binaryMaxLength() != null && length > binaryMaxLength())
      throw new IllegalArgumentException("BINARY length (" + length + ") is greater than maximum (" + binaryMaxLength() + ") allowed by " + getVendor());

    return declareBinary(varying, length);
  }

  abstract String declareBlob(Long length);
  abstract Long blobMaxLength();
  public String compileBlob(final Long length) {
    if (length != null && blobMaxLength() != null && length > blobMaxLength())
      throw new IllegalArgumentException("BLOB length (" + length + ") is greater than maximum (" + blobMaxLength() + ") allowed by " + getVendor());

    return declareBlob(length);
  }

  public abstract String declareBoolean();

  abstract String declareChar(boolean varying, long length);
  abstract Integer charMaxLength();
  public String compileChar(final boolean varying, final Long length) {
    if (length != null && charMaxLength() != null && length > charMaxLength())
      throw new IllegalArgumentException("CHAR length (" + length + ") is greater than maximum (" + charMaxLength() + ") allowed by " + getVendor());

    return declareChar(varying, length == null ? 1L : length);
  }

  abstract String declareClob(Long length);
  abstract Long clobMaxLength();
  public String compileClob(final Long length) {
    if (length != null && clobMaxLength() != null && length > clobMaxLength())
      throw new IllegalArgumentException("CLOB length (" + length + ") is greater than maximum (" + clobMaxLength() + ") allowed by " + getVendor());

    return declareClob(length);
  }

  public abstract String declareDate();

  public abstract String declareDateTime(Byte precision);

  public abstract String declareDecimal(Integer precision, Integer scale, BigDecimal min);
  public abstract int decimalMaxPrecision();
  abstract Integer decimalMaxScale();

  public abstract String declareFloat(Float min);

  public abstract String declareDouble(Double min);

  abstract String declareInt8(Byte precision, Byte min);
  static final byte int8SignedMaxPrecision = 3;
  public String compileInt8(final Byte precision, final Byte min) {
    if (precision != null && precision > Dialect.int8SignedMaxPrecision)
      throw new IllegalArgumentException("TINYINT precision (" + precision + ") is greater than maximum (" + Dialect.int8SignedMaxPrecision + ")");

    return declareInt8(precision, min);
  }

  abstract String declareInt16(Byte precision, Short min);
  static final byte int16SignedMaxPrecision = 5;
  public String compileInt16(final Byte precision, final Short min) {
    if (precision != null && precision > Dialect.int16SignedMaxPrecision)
      throw new IllegalArgumentException("SMALLINT precision (" + precision + ") is greater than maximum (" + Dialect.int16SignedMaxPrecision + ")");

    return declareInt16(precision, min);
  }

  abstract String declareInt32(Byte precision, Integer min);
  static final byte int32SignedMaxPrecision = 10;
  public String compileInt32(final Byte precision, final Integer min) {
    if (precision != null && precision > Dialect.int32SignedMaxPrecision)
      throw new IllegalArgumentException("INT precision (" + precision + ") is greater than maximum (" + Dialect.int32SignedMaxPrecision + ")");

    return declareInt32(precision, min);
  }

  abstract String declareInt64(Byte precision, Long min);
  static final byte int64SignedMaxPrecision = 19;
  public String compileInt64(Byte precision, final Long min) {
    if (precision != null && precision > Dialect.int64SignedMaxPrecision)
      throw new IllegalArgumentException("BIGINT precision (" + precision + ") is greater than maximum (" + Dialect.int64SignedMaxPrecision + ")");

    return declareInt64(precision, min);
  }

  public abstract String declareTime(Byte precision);
  public abstract String declareInterval();
  public abstract String declareEnum($Enum type, Map<String,Map<String,String>> tableNameToEnumToOwner);

  public final byte[] stringLiteralToBinary(final String str) {
    return Hexadecimal.decode(stringLiteralToHexString(str));
  }

  public final String binaryToStringLiteral(final byte[] bytes) {
    return hexStringToStringLiteral(Hexadecimal.encode(bytes));
  }

  public String hexStringToStringLiteral(final String hex) {
    return "X'" + hex + "'";
  }

  public String stringLiteralToHexString(final String str) {
    if (!str.startsWith("X'") || !str.endsWith("'"))
      throw new IllegalArgumentException();

    // FIXME: Make efficient
    return str.substring(2, str.length() - 1);
  }
}