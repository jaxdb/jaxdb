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
    super(DBVendor.ORACLE);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)30;
  }

  @Override
  public String quoteIdentifier(final CharSequence identifier) {
    return "\"" + identifier + "\"";
  }

  @Override
  public String currentTimeFunction() {
    return "(SYSTIMESTAMP - SYSDATE) DAY(9) TO SECOND";
  }

  @Override
  public String currentDateFunction() {
    return "SYSDATE";
  }

  @Override
  public String currentDateTimeFunction() {
    return "SYSTIMESTAMP";
  }

  @Override
  public String currentTimestampMillisecondsFunction() {
    return "(EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60 + EXTRACT(SECOND FROM SYSTIMESTAMP)) * 1000";
  }

  @Override
  public String currentTimestampSecondsFunction() {
    return "EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60 + EXTRACT(SECOND FROM SYSTIMESTAMP)";
  }

  @Override
  public String currentTimestampMinutesFunction() {
    return "EXTRACT(DAY FROM (SYSTIMESTAMP - TIMESTAMP '1970-01-01 00:00:00 UTC') * 24 * 60) * 60";
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
  public String declareBoolean() {
    return "NUMBER(1)";
  }

  @Override
  public String declareFloat(final Float min) {
    return "FLOAT";
  }

  @Override
  public String declareDouble(final Double min) {
    return "DOUBLE PRECISION";
  }

  @Override
  public String declareDecimal(final Integer precision, Integer scale, final BigDecimal min) {
    if (precision != null) {
      if (scale == null)
        scale = 0;

      assertValidDecimal(precision, scale);
      return "DECIMAL(" + precision + "," + scale + ")";
    }
    else if (scale != null) {
      throw new IllegalArgumentException("DECIMAL(precision=null,scale=" + scale + ")");
    }

    return "DECIMAL";
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
  String declareInt8(final Byte precision, final Byte min) {
    return "NUMBER(" + (precision != null ? precision : "3") + ")";
  }

  @Override
  String declareInt16(final Byte precision, final Short min) {
    return "NUMBER(" + (precision != null ? precision : "5") + ")";
  }

  @Override
  String declareInt32(final Byte precision, final Integer min) {
    return "NUMBER(" + (precision != null ? precision : "10") + ")";
  }

  @Override
  String declareInt64(final Byte precision, final Long min) {
    return "NUMBER(" + (precision != null ? precision : "19") + ")";
  }

  @Override
  String declareBinary(final boolean varying, final long length) {
    return (length > 2000 ? "LONG RAW" : "RAW") + "(" + length + ")";
  }

  // http://www.orafaq.com/wiki/RAW
  @Override
  Integer binaryMaxLength() {
    return 2000000000;
  }

  @Override
  String declareBlob(final Long length) {
    return "BLOB";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefblob.html
  @Override
  Long blobMaxLength() {
    return 4294967296L;
  }

  @Override
  String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR2" : "CHAR") + "(" + (length == 1 ? 2 : length) + " CHAR)";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefsqlj41207.html
  @Override
  Integer charMaxLength() {
    return 32672;
  }

  @Override
  String declareClob(final Long length) {
    return "CLOB";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefclob.html
  @Override
  Long clobMaxLength() {
    return 4294967296L;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  private static String getDateTimePrecision(final Byte precision) {
    return precision != null ? String.valueOf(precision) : "6";
  }

  @Override
  public String declareDateTime(final Byte precision) {
    return "TIMESTAMP(" + getDateTimePrecision(precision) + ")";
  }

  @Override
  public String declareTime(final Byte precision) {
    return "INTERVAL DAY(9) TO SECOND(" + getDateTimePrecision(precision) + ")";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $Enum type, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    if (type.getValues$() == null)
      return "VARCHAR2(0)";

    int maxLength = 0;
    final String[] enums = Dialect.parseEnum(type.getValues$().text());
    for (int i = 0, i$ = enums.length; i < i$; ++i) // [RA]
      maxLength = Math.max(maxLength, enums[i].length());

    return "VARCHAR2(" + maxLength + ")";
  }
}