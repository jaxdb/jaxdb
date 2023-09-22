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

public class SQLiteDialect extends Dialect {
  SQLiteDialect() {
    super(DbVendor.SQLITE);
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
    return b.append("TIME('now')");
  }

  @Override
  public StringBuilder currentDateFunction(final StringBuilder b) {
    return b.append("DATE('now')");
  }

  @Override
  public StringBuilder currentDateTimeFunction(final StringBuilder b) {
    return b.append("DATETIME('now')");
  }

  @Override
  public StringBuilder currentTimestampMillisecondsFunction(final StringBuilder b) {
    return b.append("CAST(ROUND((JULIANDAY('now') - 2440587.5) * 86400000) AS INTEGER)");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("CAST(ROUND((JULIANDAY('now') - 2440587.5) * 86400) AS INTEGER)");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return b.append("CAST(ROUND((JULIANDAY('now') - 2440587.5) * 1440) AS INTEGER)");
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
  StringBuilder declareInt8(final StringBuilder b, Byte precision, final Byte min) {
    return b.append("TINYINT");
  }

  @Override
  StringBuilder declareInt16(final StringBuilder b, Byte precision, final Short min) {
    return b.append("SMALLINT");
  }

  @Override
  StringBuilder declareInt32(final StringBuilder b, Byte precision, final Integer min) {
    return b.append(precision != null && precision < 8 ? "MEDIUMINT" : "INT");
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, Byte precision, final Long min) {
    b.append("BIGINT");
    if (min != null && min >= 0)
      b.append(" UNSIGNED");

    return b;
  }

  // FIXME: Could not find a definitive spec for BINARY/BLOB
  @Override
  StringBuilder declareBinary(final StringBuilder b, boolean varying, final long length) {
    return b.append("BINARY(").append(length).append(')');
  }

  // https://sqlite.org/limits.html#max_length
  @Override
  Integer binaryMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, Long length) {
    b.append("BLOB");
    if (length != null)
      b.append('(').append(length).append(')');

    return b;
  }

  @Override
  Long blobMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareChar(final StringBuilder b, boolean varying, final long length) {
    return b.append(varying ? "VARCHAR(" : "CHARACTER(").append(length).append(')');
  }

  // QLite does not impose any length restrictions (other than the large global SQLITE_MAX_LENGTH limit) on the length of strings, BLOBs or numeric values.
  @Override
  Integer charMaxLength() {
    return null;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, Long length) {
    b.append("TEXT");
    if (length != null)
      b.append('(').append(length).append(')');

    return b;
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
  public StringBuilder declareDateTime(final StringBuilder b, final Byte precision) {
    return b.append("DATETIME");
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
    if (enumValues == null)
      return b.append("VARCHAR(0)");

    int maxLength = 0;
    final String[] enums = Dialect.parseEnum(enumValues);
    for (int i = 0, i$ = enums.length; i < i$; ++i) // [RA]
      maxLength = Math.max(maxLength, enums[i].length());

    return b.append("VARCHAR(").append(maxLength).append(')');
  }
}