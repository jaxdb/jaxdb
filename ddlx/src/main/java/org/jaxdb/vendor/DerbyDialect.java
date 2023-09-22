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
import java.util.Map;

import org.jaxdb.www.ddlx_0_6.xLygluGCXAA.$Enum;

public class DerbyDialect extends Dialect {
  DerbyDialect() {
    super(DbVendor.DERBY);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)128;
  }

  @Override
  public StringBuilder quoteIdentifier(final StringBuilder b, final CharSequence identifier) {
    return b.append('"').append(identifier).append('"');
  }

  @Override
  public StringBuilder currentTimeFunction(final StringBuilder b) {
    return b.append("CURRENT_TIME");
  }

  @Override
  public StringBuilder currentDateFunction(final StringBuilder b) {
    return b.append("CURRENT_DATE");
  }

  @Override
  public StringBuilder currentDateTimeFunction(final StringBuilder b) {
    return b.append("CURRENT_TIMESTAMP");
  }

  @Override
  public StringBuilder currentTimestampMillisecondsFunction(final StringBuilder b) {
    return b.append("{fn TIMESTAMPDIFF(SQL_TSI_FRAC_SECOND, TIMESTAMP('1970-01-01-00.00.00.000'), CURRENT_TIMESTAMP)}");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("{fn TIMESTAMPDIFF(SQL_TSI_SECOND, TIMESTAMP('1970-01-01-00.00.00'), CURRENT_TIMESTAMP)}");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return b.append("{fn TIMESTAMPDIFF(SQL_TSI_MINUTE, TIMESTAMP('1970-01-01-00.00.00'), CURRENT_TIMESTAMP)}");
  }

  @Override
  public byte minTinyint() {
    return Byte.MIN_VALUE;
  }

  @Override
  public byte maxTinyint() {
    return Byte.MAX_VALUE;
  }

  @Override
  public short minSmallint() {
    return Short.MIN_VALUE;
  }

  @Override
  public short maxSmallint() {
    return Short.MAX_VALUE;
  }

  @Override
  public int minInt() {
    return Integer.MIN_VALUE;
  }

  @Override
  public int maxInt() {
    return Integer.MAX_VALUE;
  }

  @Override
  public long minBigint() {
    return Long.MIN_VALUE;
  }

  @Override
  public long maxBigint() {
    return Long.MAX_VALUE;
  }

  @Override
  public StringBuilder declareBoolean(final StringBuilder b) {
    return b.append("BOOLEAN");
  }

  @Override
  public StringBuilder declareFloat(final StringBuilder b, final Float min) {
    return b.append("FLOAT");
  }

  @Override
  public StringBuilder declareDouble(final StringBuilder b, final Double min) {
    return b.append("DOUBLE");
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj15260.html
  @Override
  public StringBuilder declareDecimal(final StringBuilder b, final Integer precision, Integer scale, final BigDecimal min) {
    if (precision == null) {
      if (scale != null)
        throw new IllegalArgumentException("DECIMAL(precision=null,scale=" + scale + ")");
    }
    else {
      if (scale == null)
        scale = 0;

      assertValidDecimal(precision, scale);
      return b.append("DECIMAL(").append(precision).append(',').append(scale).append(')');
    }

    return b.append("DECIMAL");
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj15260.html
  @Override
  public int decimalMaxPrecision() {
    return 31;
  }

  @Override
  Integer decimalMaxScale() {
    return null;
  }

  @Override
  StringBuilder declareInt8(final StringBuilder b, final Byte precision, final Byte min) {
    return b.append("SMALLINT");
  }

  @Override
  StringBuilder declareInt16(final StringBuilder b, final Byte precision, final Short min) {
    return b.append("SMALLINT");
  }

  @Override
  StringBuilder declareInt32(final StringBuilder b, final Byte precision, final Integer min) {
    return b.append("INTEGER");
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, final Byte precision, final Long min) {
    return b.append("BIGINT");
  }

  @Override
  StringBuilder declareBinary(final StringBuilder b, final boolean varying, final long length) {
    return declareChar(b, varying || length > CHAR_MAX_LENGTH, length).append(" FOR BIT DATA");
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj30118.html
  @Override
  Integer binaryMaxLength() {
    return 32700;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, final Long length) {
    b.append("BLOB");
    if (length != null)
      b.append('(').append(length).append(')');

    return b;
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefblob.html
  @Override
  Long blobMaxLength() {
    return 2147483647L;
  }

  // https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/ref/rrefstringlimits.html
  private static final int VARCHAR_MAX_LENGTH = 32672;
  private static final int CHAR_MAX_LENGTH = 254;

  @Override
  StringBuilder declareChar(final StringBuilder b, final boolean varying, final long length) {
    final String type = varying ? "VARCHAR" : "CHAR";
    final int maxLength = varying ? VARCHAR_MAX_LENGTH : CHAR_MAX_LENGTH;
    if (length > maxLength)
      throw new IllegalArgumentException(type + " length (" + length + ") is greater than maximum (" + maxLength + ") allowed by " + getVendor());

    return b.append(type).append('(').append(length).append(')');
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj41207.html
  @Override
  Integer charMaxLength() {
    return 32672;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, final Long length) {
    if (length == null)
      return b.append("CLOB");

    if (length > 2147483647)
      throw new IllegalArgumentException("CLOB length (" + length + ") is greater than maximum (" + 2147483647 + ") allowed by " + getVendor());

    return b.append("CLOB(").append(length).append(')');
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefblob.html
  @Override
  Long clobMaxLength() {
    return 2147483647L;
  }

  @Override
  public StringBuilder declareDate(final StringBuilder b) {
    return b.append("DATE");
  }

  @Override
  public StringBuilder declareDateTime(final StringBuilder b, final Byte precision) {
    return b.append("TIMESTAMP");
  }

  @Override
  public StringBuilder declareTime(final StringBuilder b, final Byte precision) {
    return b.append("TIME");
  }

  @Override
  public StringBuilder declareInterval(final StringBuilder b) {
    return b.append("INTERVAL");
  }

  @Override
  public StringBuilder declareEnum(final StringBuilder b, final $Enum column, final String enumValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    int maxLength = 0;
    final String[] enums = Dialect.parseEnum(enumValues);
    for (int i = 0, i$ = enums.length; i < i$; ++i) // [RA]
      maxLength = Math.max(maxLength, enums[i].length());

    return b.append("VARCHAR(").append(maxLength).append(')');
  }
}