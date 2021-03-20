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

import java.util.Iterator;
import java.util.List;

import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.libj.math.BigInt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MySQLDialect extends Dialect {
  static final Logger logger = LoggerFactory.getLogger(MySQLDialect.class);

  @Override
  public DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  public String quoteIdentifier(final CharSequence identifier) {
    return "`" + identifier + "`";
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
    return "CAST(1000 * UNIX_TIMESTAMP(CURRENT_TIMESTAMP(3)) AS UNSIGNED INTEGER)";
  }

  @Override
  public String currentTimestampSecondsFunction() {
    return "UNIX_TIMESTAMP()";
  }

  @Override
  public String currentTimestampMinutesFunction() {
    return currentTimestampSecondsFunction() + " / 60";
  }

  @Override
  public boolean allowsUnsignedNumeric() {
    return false;
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
  public short maxTinyintUnsigned() {
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
  public int maxSmallintUnsigned() {
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
  public long maxIntUnsigned() {
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

  private static final BigInt maxBigintUnsigned = new BigInt(1).shiftLeft(64).sub(1);

  @Override
  public BigInt maxBigintUnsigned() {
    return maxBigintUnsigned;
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean unsigned) {
    return "FLOAT" + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareDouble(final boolean unsigned) {
    return "DOUBLE" + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareDecimal(final Integer precision, Integer scale, final boolean unsigned) {
    if (precision == null) {
      if (scale != null)
        throw new IllegalArgumentException("DECIMAL(precision=null,scale=" + scale + ")");
    }
    else {
      if (scale == null)
        scale = 0;

      assertValidDecimal(precision, scale);
      return "DECIMAL(" + precision + ", " + scale + ")";
    }

    return "DECIMAL";
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
  String declareInt8(final Byte precision, final boolean unsigned) {
    return "TINYINT" + (precision != null ?  "(" + precision + ")" : "") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  String declareInt16(final Byte precision, final boolean unsigned) {
    return "SMALLINT" + (precision != null ?  "(" + precision + ")" : "") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  String declareInt32(final Byte precision, final boolean unsigned) {
    if (precision != null) {
      if (unsigned && precision < 9)
        return "MEDIUMINT(" + precision + ") UNSIGNED";

      if (!unsigned && precision < 8)
        return "MEDIUMINT(" + precision + ")";
    }

    return "INT" + (precision != null ?  "(" + precision + ")" : "") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  String declareInt64(final Byte precision, final boolean unsigned) {
    return "BIGINT" + (precision != null ?  "(" + precision + ")" : "") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  String declareBinary(final boolean varying, final long length) {
    return (varying ? "VAR" : "") + "BINARY" + "(" + length + ")";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  Integer binaryMaxLength() {
    return 65535;
  }

  @Override
  String declareBlob(final Long length) {
    if (length != null && length >= 4294967296L)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYBLOB, BLOB, MEDIUMBLOB, or LONGBLOB in " + getVendor());

    return length == null ? "LONGBLOB" : length < 256 ? "TINYBLOB" : length < 65536 ? "BLOB" : length < 16777216 ? "MEDIUMBLOB" : "LONGBLOB";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYBLOB = 256B, BLOB = 64KB, MEDIUMBLOB = 16MB and LONGBLOB = 4GB
  @Override
  Long blobMaxLength() {
    return 4294967296L;
  }

  @Override
  String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  Integer charMaxLength() {
    return 65535;
  }

  @Override
  String declareClob(final Long length) {
    if (length != null && length >= 4294967296L)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYTEXT, TEXT, MEDIUMTEXT, or LONGTEXT in " + getVendor());

    return length == null ? "LONGTEXT" : length < 256 ? "TINYTEXT" : length < 65536 ? "TEXT" : length < 16777216 ? "MEDIUMTEXT" : "LONGTEXT";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYTEXT = 256B, TEXT = 64KB, MEDIUMTEXT = 16MB and LONGTEXT = 4GB
  @Override
  Long clobMaxLength() {
    return 4294967296L;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(Byte precision) {
    if (precision != null && precision > 6) {
      logger.warn("DATETIME(" + precision + ") precision will be reduced to maximum allowed: 6");
      precision = 6;
    }

    return "DATETIME" + (precision != null ? "(" + precision + ")" : "");
  }

  @Override
  public String declareTime(final Byte precision) {
    return "TIME" + (precision != null ? "(" + precision + ")" : "");
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $Enum type) {
    if (type.getValues$() == null)
      return "ENUM()";

    final List<String> enums = Dialect.parseEnum(type.getValues$().text());
    final StringBuilder builder = new StringBuilder();
    final Iterator<String> iterator = enums.iterator();
    for (int i = 0; iterator.hasNext(); ++i) {
      if (i > 0)
        builder.append(", ");

      builder.append('\'').append(iterator.next()).append('\'');
    }

    return "ENUM(" + builder.append(')');
  }
}