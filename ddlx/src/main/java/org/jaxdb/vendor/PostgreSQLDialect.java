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
import org.libj.lang.Hexadecimal;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLDialect extends Dialect {
  static final Logger logger = LoggerFactory.getLogger(PostgreSQLDialect.class);

  PostgreSQLDialect() {
    super(DBVendor.POSTGRE_SQL);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)63;
  }

  @Override
  public StringBuilder quoteIdentifier(final StringBuilder v, final CharSequence identifier) {
    return v.append('"').append(identifier).append('"');
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
    return b.append("EXTRACT(EPOCH FROM CURRENT_TIMESTAMP(3)) * 1000");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return currentTimestampSecondsFunction(b).append(" / 60");
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
    return b.append("DOUBLE PRECISION");
  }

  @Override
  public StringBuilder declareDecimal(final StringBuilder b, Integer precision, final Integer scale, final BigDecimal min) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    b.append("DECIMAL");
    if (precision != null)
      b.append('(').append(precision).append(',').append(scale != null ? scale : 0).append(')');

    return b;
  }

  // https://www.postgresql.org/docs/9.6/static/datatype-numeric.html
  @Override
  public int decimalMaxPrecision() {
    return 1000;
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
    return b.append("INT");
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, final Byte precision, final Long min) {
    return b.append("BIGINT");
  }

  @Override
  StringBuilder declareBinary(final StringBuilder b, final boolean varying, final long length) {
    return b.append("BYTEA");
  }

  @Override
  Integer binaryMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, final Long length) {
    return b.append("BYTEA");
  }

  @Override
  Long blobMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareChar(final StringBuilder b, final boolean varying, final long length) {
    if (varying)
      b.append("VAR");

    return b.append("CHAR(").append(length).append(')');
  }

  @Override
  Integer charMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, final Long length) {
    return b.append("TEXT");
  }

  @Override
  Long clobMaxLength() {
    return null;
  }

  @Override
  public StringBuilder declareDate(final StringBuilder b) {
    return b.append("DATE");
  }

  @Override
  public StringBuilder declareDateTime(final StringBuilder b, Byte precision) {
    if (precision != null && precision > 6) {
      if (logger.isWarnEnabled()) logger.warn("TIMESTAMP(" + precision + ") precision will be reduced to maximum allowed: 6");
      precision = 6;
    }

    b.append("TIMESTAMP");
    if (precision != null)
      b.append('(').append(precision).append(')');

    return b;
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
    b.append('"');
    Dialect.getTypeName(b, column, tableNameToEnumToOwner);
    return b.append('"');
  }

  @Override
  public StringBuilder hexStringToStringLiteral(final StringBuilder b, final String hex) {
    return b.append("'\\x").append(hex).append('\'');
  }

  @Override
  public byte[] stringLiteralToBinary(final String str) {
    if (str.startsWith("\\x"))
      return Hexadecimal.decode(str, 2, str.length());

    if (str.startsWith("'\\x") && str.endsWith("'"))
      return Hexadecimal.decode(str, 3, str.length() - 1);

    throw new IllegalArgumentException(str);
  }
}