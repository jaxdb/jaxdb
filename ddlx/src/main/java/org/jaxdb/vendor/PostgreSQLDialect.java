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
  public String quoteIdentifier(final CharSequence identifier) {
    return "\"" + identifier + "\"";
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
  public String currentTimeFunction() {
    return "CURRENT_TIME";
  }

  @Override
  public String currentDateFunction() {
    return "CURRENT_DATE";
  }

  @Override
  public String currentDateTimeFunction() {
    return "CURRENT_TIMESTAMP";
  }

  @Override
  public String currentTimestampMillisecondsFunction() {
    return "EXTRACT(EPOCH FROM CURRENT_TIMESTAMP(3)) * 1000";
  }

  @Override
  public String currentTimestampSecondsFunction() {
    return "EXTRACT(EPOCH FROM CURRENT_TIMESTAMP)";
  }

  @Override
  public String currentTimestampMinutesFunction() {
    return currentTimestampSecondsFunction() + " / 60";
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
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
  public String declareDecimal(Integer precision, final Integer scale, final BigDecimal min) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + "," + (scale != null ? scale : 0) + ")";
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
  String declareInt8(final Byte precision, final Byte min) {
    return "SMALLINT";
  }

  @Override
  String declareInt16(final Byte precision, final Short min) {
    return "SMALLINT";
  }

  @Override
  String declareInt32(final Byte precision, final Integer min) {
    return "INT";
  }

  @Override
  String declareInt64(final Byte precision, final Long min) {
    return "BIGINT";
  }

  @Override
  String declareBinary(final boolean varying, final long length) {
    return "BYTEA";
  }

  @Override
  Integer binaryMaxLength() {
    return null;
  }

  @Override
  String declareBlob(final Long length) {
    return "BYTEA";
  }

  @Override
  Long blobMaxLength() {
    return null;
  }

  @Override
  String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  Integer charMaxLength() {
    return null;
  }

  @Override
  String declareClob(final Long length) {
    return "TEXT";
  }

  @Override
  Long clobMaxLength() {
    return null;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(Byte precision) {
    if (precision != null && precision > 6) {
      if (logger.isWarnEnabled())
        logger.warn("TIMESTAMP(" + precision + ") precision will be reduced to maximum allowed: 6");

      precision = 6;
    }

    return "TIMESTAMP" + (precision != null ? "(" + precision + ")" : "");
  }

  @Override
  public String declareTime(final Byte precision) {
    return "TIME";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $Enum type, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    return q(Dialect.getTypeName(type, tableNameToEnumToOwner));
  }

  @Override
  public String hexStringToStringLiteral(final String hex) {
    return "'\\x" + hex + "'";
  }

  @Override
  public String stringLiteralToHexString(final String str) {
    // FIXME: Make efficient
    if (str.startsWith("\\x"))
      return str.substring(2);

    if (str.startsWith("'\\x") && str.endsWith("'"))
      return str.substring(3, str.length() - 1);

    throw new IllegalArgumentException(str);
  }
}