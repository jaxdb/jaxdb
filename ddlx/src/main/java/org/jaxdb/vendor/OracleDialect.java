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

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;

public class OracleDialect extends Dialect {
  OracleDialect() {
    super(DbVendor.ORACLE);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)30;
  }

  @Override
  public StringBuilder quoteIdentifier(final StringBuilder b, final CharSequence identifier) {
    return b.append('"').append(identifier).append('"');
  }

  @Override
  public StringBuilder currentTimeFunction(final StringBuilder b) {
    return b.append("(SYSTIMESTAMP - SYSDATE) DAY(9) TO SECOND");
  }

  @Override
  public StringBuilder currentDateFunction(final StringBuilder b) {
    return b.append("SYSDATE");
  }

  @Override
  public StringBuilder currentDateTimeFunction(final StringBuilder b) {
    return b.append("SYSTIMESTAMP");
  }

  @Override
  public StringBuilder currentTimestampMillisecondsFunction(final StringBuilder b) {
    return b.append("(EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60 + EXTRACT(SECOND FROM SYSTIMESTAMP)) * 1000");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60 + EXTRACT(SECOND FROM SYSTIMESTAMP)");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return b.append("EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60");
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
    return b.append("NUMBER(1)");
  }

  @Override
  public StringBuilder declareFloat(final StringBuilder b, final Float min) {
    return b.append("FLOAT");
  }

  @Override
  public StringBuilder declareDouble(final StringBuilder b, final Double min) {
    return b.append("DOUBLE PRECISION");
  }

  @Override
  public StringBuilder declareDecimal(final StringBuilder b, final Integer precision, Integer scale, final BigDecimal min) {
    if (precision != null) {
      if (scale == null)
        scale = 0;

      assertValidDecimal(precision, scale);
      return b.append("DECIMAL(").append(precision).append(',').append(scale).append(')');
    }
    else if (scale != null) {
      throw new IllegalArgumentException("DECIMAL(precision=null,scale=" + scale + ")");
    }

    return b.append("DECIMAL");
  }

  // https://docs.oracle.com/cd/E11882_01/gateways.112/e12068/apa.htm#TGTEU755
  @Override
  public int decimalMaxPrecision() {
    return 38;
  }

  @Override
  Integer decimalMaxScale() {
    return null;
  }

  @Override
  StringBuilder declareInt8(final StringBuilder b, Byte precision, final Byte min) {
    return b.append("NUMBER(").append(precision != null ? precision : "3").append(')');
  }

  @Override
  StringBuilder declareInt16(final StringBuilder b, Byte precision, final Short min) {
    return b.append("NUMBER(").append(precision != null ? precision : "5").append(')');
  }

  @Override
  StringBuilder declareInt32(final StringBuilder b, Byte precision, final Integer min) {
    return b.append("NUMBER(").append(precision != null ? precision : "10").append(')');
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, Byte precision, final Long min) {
    return b.append("NUMBER(").append(precision != null ? precision : "19").append(')');
  }

  @Override
  StringBuilder declareBinary(final StringBuilder b, boolean varying, final long length) {
    if (length > 2000)
      b.append("LONG ");

    return b.append("RAW(").append(length).append(')');
  }

  // http://www.orafaq.com/wiki/RAW
  @Override
  Integer binaryMaxLength() {
    return 2000000000;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, Long length) {
    return b.append("BLOB");
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefblob.html
  @Override
  Long blobMaxLength() {
    return 4294967296L;
  }

  @Override
  StringBuilder declareChar(final StringBuilder b, boolean varying, final long length) {
    return b.append(varying ? "VARCHAR2(" : "CHAR(").append(length == 1 ? 2 : length).append(" CHAR)");
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefsqlj41207.html
  @Override
  Integer charMaxLength() {
    return 32672;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, Long length) {
    return b.append("CLOB");
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefclob.html
  @Override
  Long clobMaxLength() {
    return 4294967296L;
  }

  @Override
  public StringBuilder declareDate(final StringBuilder b) {
    return b.append("DATE");
  }

  private static String getDateTimePrecision(final Byte precision) {
    return precision != null ? String.valueOf(precision) : "6";
  }

  @Override
  public StringBuilder declareDateTime(final StringBuilder b, final Byte precision) {
    return b.append("TIMESTAMP(").append(getDateTimePrecision(precision)).append(')');
  }

  @Override
  public StringBuilder declareTime(final StringBuilder b, final Byte precision) {
    return b.append("INTERVAL DAY(9) TO SECOND(").append(getDateTimePrecision(precision)).append(')');
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

    return b.append("VARCHAR2(").append(maxLength).append(')');
  }
}