/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.vendor;

import java.util.List;

import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;

public class SQLiteDialect extends Dialect {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.SQLITE;
  }

  @Override
  public boolean allowsUnsignedNumeric() {
    return false;
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE" : "FLOAT";
  }

  @Override
  public String declareDecimal(Short precision, final Short scale, final boolean unsigned) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + ", " + (scale != null ? scale : 0) + ")";
  }

  // http://www.sqlite.org/datatype3.html
  @Override
  public short decimalMaxPrecision() {
    return 15;
  }

  @Override
  protected Integer decimalMaxScale() {
    return null;
  }

  @Override
  protected String declareInt8(final byte precision, final boolean unsigned) {
    return "TINYINT";
  }

  @Override
  protected String declareInt16(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  protected String declareInt32(final byte precision, final boolean unsigned) {
    return precision < 8 ? "MEDIUMINT" : "INT";
  }

  @Override
  protected String declareInt64(final byte precision, final boolean unsigned) {
    return "BIGINT" + (unsigned ? " UNSIGNED" : "");
  }

  // FIXME: Could not find a definitive spec for BINARY/BLOB
  @Override
  protected String declareBinary(final boolean varying, final int length) {
    return "BINARY" + "(" + length + ")";
  }

  // https://sqlite.org/limits.html#max_length
  @Override
  protected Integer binaryMaxLength() {
    return null;
  }

  @Override
  protected String declareBlob(final Long length) {
    return "BLOB" + (length != null ? "(" + length + ")" : "");
  }

  @Override
  protected Long blobMaxLength() {
    return null;
  }

  @Override
  protected String declareChar(final boolean varying, final int length) {
    return (varying ? "VARCHAR" : "CHARACTER") + "(" + length + ")";
  }

  // QLite does not impose any length restrictions (other than the large global SQLITE_MAX_LENGTH limit) on the length of strings, BLOBs or numeric values.
  @Override
  protected Integer charMaxLength() {
    return null;
  }

  @Override
  protected String declareClob(final Long length) {
    return "TEXT" + (length != null ? "(" + length + ")" : "");
  }

  @Override
  protected Long clobMaxLength() {
    return null;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final byte precision) {
    return "DATETIME";
  }

  @Override
  public String declareTime(final byte precision) {
    return "TIME";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    if (type._values$().isNull())
      return "VARCHAR(0)";

    final List<String> enums = Dialect.parseEnum(type._values$().text());
    int maxLength = 0;
    for (final String value : enums)
      maxLength = Math.max(maxLength, value.length());

    return "VARCHAR(" + maxLength + ")";
  }
}