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

public class MySQLDialect extends Dialect {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.MY_SQL;
  }

  @Override
  public boolean allowsUnsignedNumeric() {
    return true;
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return (doublePrecision ? "DOUBLE" : "FLOAT") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareDecimal(Short precision, Short scale, final boolean unsigned) {
    if (precision == null)
      precision = 10;

    if (scale == null)
      scale = 0;

    assertValidDecimal(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")" + (unsigned ? " UNSIGNED" : "");
  }

  // https://dev.mysql.com/doc/refman/5.5/en/fixed-point-types.html
  @Override
  public short decimalMaxPrecision() {
    return 65;
  }

  @Override
  protected Integer decimalMaxScale() {
    return 30;
  }

  @Override
  protected String declareInt8(final byte precision, final boolean unsigned) {
    return "TINYINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  protected String declareInt16(final byte precision, final boolean unsigned) {
    return "SMALLINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  protected String declareInt32(final byte precision, final boolean unsigned) {
    if (unsigned && precision < 9)
      return "MEDIUMINT(" + precision + ") UNSIGNED";

    if (!unsigned && precision < 8)
      return "MEDIUMINT(" + precision + ")";

    return "INT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  protected String declareInt64(final byte precision, final boolean unsigned) {
    return "BIGINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  protected String declareBinary(final boolean varying, final int length) {
    return (varying ? "VAR" : "") + "BINARY" + "(" + length + ")";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  protected Integer binaryMaxLength() {
    return 65535;
  }

  @Override
  protected String declareBlob(final Long length) {
    if (length != null && length >= 4294967296l)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYBLOB, BLOB, MEDIUMBLOB, or LONGBLOB in " + getVendor());

    return length == null ? "LONGBLOB" : length < 256 ? "TINYBLOB" : length < 65536 ? "BLOB" : length < 16777216 ? "MEDIUMBLOB" : "LONGBLOB";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYBLOB = 256B, BLOB = 64KB, MEDIUMBLOB = 16MB and LONGBLOB = 4GB
  @Override
  protected Long blobMaxLength() {
    return 4294967296l;
  }

  @Override
  protected String declareChar(final boolean varying, final int length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/char.html
  @Override
  protected Integer charMaxLength() {
    return 65535;
  }

  @Override
  protected String declareClob(final Long length) {
    if (length != null && length >= 4294967296l)
      throw new IllegalArgumentException("Length of " + length + " is illegal for TINYTEXT, TEXT, MEDIUMTEXT, or LONGTEXT in " + getVendor());

    return length == null ? "LONGTEXT" : length < 256 ? "TINYTEXT" : length < 65536 ? "TEXT" : length < 16777216 ? "MEDIUMTEXT" : "LONGTEXT";
  }

  // https://dev.mysql.com/doc/refman/5.7/en/blob.html
  // TINYTEXT = 256B, TEXT = 64KB, MEDIUMTEXT = 16MB and LONGTEXT = 4GB
  @Override
  protected Long clobMaxLength() {
    return 4294967296l;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final byte precision) {
    return "DATETIME(" + precision + ")";
  }

  @Override
  public String declareTime(final byte precision) {
    return "TIME(" + precision + ")";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    if (type._values$().isNull())
      return "ENUM()";

    final List<String> enums = Dialect.parseEnum(type._values$().text());
    final StringBuilder builder = new StringBuilder();
    for (final String value : enums)
      builder.append(", '").append(value).append("'");

    return "ENUM(" + builder.append(")").substring(2);
  }
}