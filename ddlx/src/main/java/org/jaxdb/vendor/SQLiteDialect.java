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

import java.util.List;

import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.libj.math.BigInt;

public class SQLiteDialect extends Dialect {
  @Override
  public DBVendor getVendor() {
    return DBVendor.SQLITE;
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

  private static final BigInt maxBigintUnsigned = new BigInt(Long.MAX_VALUE);

  @Override
  public BigInt maxBigintUnsigned() {
    return maxBigintUnsigned;
  }

  @Override
  public String currentTimeFunction() {
    return "TIME('now')";
  }

  @Override
  public String currentDateFunction() {
    return "DATE('now')";
  }

  @Override
  public String currentDateTimeFunction() {
    return "DATETIME('now')";
  }

  @Override
  public String currentTimestampMillisecondsFunction() {
    return "CAST(ROUND((JULIANDAY('now') - 2440587.5) * 86400000) AS INTEGER)";
  }

  @Override
  public String currentTimestampSecondsFunction() {
    return "CAST(ROUND((JULIANDAY('now') - 2440587.5) * 86400) AS INTEGER)";
  }

  @Override
  public String currentTimestampMinutesFunction() {
    return "CAST(ROUND((JULIANDAY('now') - 2440587.5) * 1440) AS INTEGER)";
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean unsigned) {
    return "FLOAT";
  }

  @Override
  public String declareDouble(final boolean unsigned) {
    return "DOUBLE";
  }

  @Override
  public String declareDecimal(Integer precision, final Integer scale, final boolean unsigned) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + "," + (scale != null ? scale : 0) + ")";
  }

  // http://www.sqlite.org/datatype3.html
  @Override
  public int decimalMaxPrecision() {
    return 15;
  }

  @Override
  Integer decimalMaxScale() {
    return null;
  }

  @Override
  String declareInt8(final Byte precision, final boolean unsigned) {
    return "TINYINT";
  }

  @Override
  String declareInt16(final Byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  String declareInt32(final Byte precision, final boolean unsigned) {
    return precision != null && precision < 8 ? "MEDIUMINT" : "INT";
  }

  @Override
  String declareInt64(final Byte precision, final boolean unsigned) {
    return "BIGINT" + (unsigned ? " UNSIGNED" : "");
  }

  // FIXME: Could not find a definitive spec for BINARY/BLOB
  @Override
  String declareBinary(final boolean varying, final long length) {
    return "BINARY" + "(" + length + ")";
  }

  // https://sqlite.org/limits.html#max_length
  @Override
  Integer binaryMaxLength() {
    return null;
  }

  @Override
  String declareBlob(final Long length) {
    return "BLOB" + (length != null ? "(" + length + ")" : "");
  }

  @Override
  Long blobMaxLength() {
    return null;
  }

  @Override
  String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHARACTER") + "(" + length + ")";
  }

  // QLite does not impose any length restrictions (other than the large global SQLITE_MAX_LENGTH limit) on the length of strings, BLOBs or numeric values.
  @Override
  Integer charMaxLength() {
    return null;
  }

  @Override
  String declareClob(final Long length) {
    return "TEXT" + (length != null ? "(" + length + ")" : "");
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
  public String declareDateTime(final Byte precision) {
    return "DATETIME";
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
  public String declareEnum(final $Enum type) {
    if (type.getValues$() == null)
      return "VARCHAR(0)";

    final List<String> enums = Dialect.parseEnum(type.getValues$().text());
    int maxLength = 0;
    for (final String value : enums)
      maxLength = Math.max(maxLength, value.length());

    return "VARCHAR(" + maxLength + ")";
  }
}