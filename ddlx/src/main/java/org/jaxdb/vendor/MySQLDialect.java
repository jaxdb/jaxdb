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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLDialect extends Dialect {
  static final Logger logger = LoggerFactory.getLogger(MySQLDialect.class);

  MySQLDialect() {
    super(DbVendor.MY_SQL);
  }

  MySQLDialect(final DbVendor vendor) {
    super(vendor);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)64;
  }

  @Override
  public StringBuilder quoteIdentifier(final StringBuilder b, final CharSequence identifier) {
    return b.append('`').append(identifier).append('`');
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
    return b.append("CAST(1000 * UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) AS UNSIGNED INTEGER)");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("UNIX_TIMESTAMP()");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return currentTimestampSecondsFunction(b).append(" / 60");
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
    b.append("FLOAT");
    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

  @Override
  public StringBuilder declareDouble(final StringBuilder b, final Double min) {
    b.append("DOUBLE");
    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

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

  // https://dev.mysql.com/doc/refman/5.5/en/fixed-point-types.html
  @Override
  public int decimalMaxPrecision() {
    return 65;
  }

  @Override
  Integer decimalMaxScale() {
    return 30;
  }

  @Override
  StringBuilder declareInt8(final StringBuilder b, final Byte precision, final Byte min) {
    b.append("TINYINT");
    if (precision != null)
      b.append('(').append(precision).append(')');

    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

  @Override
  StringBuilder declareInt16(final StringBuilder b, final Byte precision, final Short min) {
    b.append("SMALLINT");
    if (precision != null)
      b.append('(').append(precision).append(')');

    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

  @Override
  StringBuilder declareInt32(final StringBuilder b, final Byte precision, final Integer min) {
    final boolean unsigned = min != null && min >= 0;
    if (precision != null) {
      if (unsigned && precision < 9)
        b.append("MEDIUMINT(").append(precision).append(") UNSIGNED");

      if (!unsigned && precision < 8)
        return b.append("MEDIUMINT(").append(precision).append(')');
    }

    b.append("INT");
    if (precision != null)
      b.append('(').append(precision).append(')');

    if (unsigned)
      b.append(" UNSIGNED");

    return b;
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, final Byte precision, final Long min) {
    b.append("BIGINT");
    if (precision != null)
      b.append('(').append(precision).append(')');

    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

  @Override
  StringBuilder declareBinary(final StringBuilder b, final boolean varying, final long length) {
    if (varying)
      b.append("VAR");

    return b.append("BINARY(").append(length).append(')');
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  Integer binaryMaxLength() {
    return 65535;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, final Long length) {
    if (length != null && length >= 4294967296L)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYBLOB, BLOB, MEDIUMBLOB, or LONGBLOB in " + getVendor());

    return b.append(length == null ? "LONGBLOB" : length < 256 ? "TINYBLOB" : length < 65536 ? "BLOB" : length < 16777216 ? "MEDIUMBLOB" : "LONGBLOB");
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYBLOB = 256B, BLOB = 64KB, MEDIUMBLOB = 16MB and LONGBLOB = 4GB
  @Override
  Long blobMaxLength() {
    return 4294967296L;
  }

  @Override
  StringBuilder declareChar(final StringBuilder b, final boolean varying, final long length) {
    if (varying)
      b.append("VAR");

    return b.append("CHAR(").append(length).append(')');
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  Integer charMaxLength() {
    return 65535;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, final Long length) {
    if (length != null && length >= 4294967296L)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYTEXT, TEXT, MEDIUMTEXT, or LONGTEXT in " + getVendor());

    return b.append(length == null ? "LONGTEXT" : length < 256 ? "TINYTEXT" : length < 65536 ? "TEXT" : length < 16777216 ? "MEDIUMTEXT" : "LONGTEXT");
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYTEXT = 256B, TEXT = 64KB, MEDIUMTEXT = 16MB and LONGTEXT = 4GB
  @Override
  Long clobMaxLength() {
    return 4294967296L;
  }

  @Override
  public StringBuilder declareDate(final StringBuilder b) {
    return b.append("DATE");
  }

  @Override
  public StringBuilder declareDateTime(final StringBuilder b, Byte precision) {
    if (precision != null && precision > 6) {
      if (logger.isWarnEnabled()) { logger.warn("DATETIME(" + precision + ") precision will be reduced to maximum allowed: 6"); }
      precision = 6;
    }

    b.append("DATETIME");
    if (precision != null)
      b.append('(').append(precision).append(')');

    return b;
  }

  @Override
  public StringBuilder declareTime(final StringBuilder b, final Byte precision) {
    b.append("TIME");
    if (precision != null)
      b.append('(').append(precision).append(')');

    return b;
  }

  @Override
  public StringBuilder declareInterval(final StringBuilder b) {
    return b.append("INTERVAL");
  }

  @Override
  public StringBuilder declareEnum(final StringBuilder b, final $Enum column, final String enumValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    b.append("ENUM(");
    final String[] enums = Dialect.parseEnum(enumValues);
    for (int i = 0, i$ = enums.length; i < i$; ++i) { // [A]
      if (i > 0)
        b.append(", ");

      b.append('\'').append(enums[i]).append('\'');
    }

    return b.append(')');
  }
}