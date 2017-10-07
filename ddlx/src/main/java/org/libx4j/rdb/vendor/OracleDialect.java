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

public class OracleDialect extends Dialect {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.ORACLE;
  }

  @Override
  public boolean allowsUnsignedNumeric() {
    return false;
  }

  @Override
  public String declareBoolean() {
    return "NUMBER(1)";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE PRECISION" : "FLOAT";
  }

  @Override
  public String declareDecimal(Short precision, Short scale, final boolean unsigned) {
    if (precision == null)
      precision = 5;

    if (scale == null)
      scale = 0;

    assertValidDecimal(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  // https://docs.oracle.com/cd/B19306_01/olap.102/b14346/dml_datatypes002.htm
  @Override
  public short decimalMaxPrecision() {
    return 38;
  }

  @Override
  protected Integer decimalMaxScale() {
    return null;
  }

  @Override
  protected String declareInt8(final byte precision, final boolean unsigned) {
    return "NUMBER(" + precision + ")";
  }

  @Override
  protected String declareInt16(final byte precision, final boolean unsigned) {
    return "NUMBER(" + precision + ")";
  }

  @Override
  protected String declareInt32(final byte precision, final boolean unsigned) {
    return "NUMBER(" + precision + ")";
  }

  @Override
  protected String declareInt64(final byte precision, final boolean unsigned) {
    return "NUMBER(" + precision + ")";
  }

  @Override
  protected String declareBinary(final boolean varying, final int length) {
    return (length > 2000 ? "LONG RAW" : "RAW") + "(" + length + ")";
  }

  // http://www.orafaq.com/wiki/RAW
  @Override
  protected Integer binaryMaxLength() {
    return 2000000000;
  }

  @Override
  protected String declareBlob(final Long length) {
    return "BLOB";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefblob.html
  @Override
  protected Long blobMaxLength() {
    return 4294967296l;
  }

  @Override
  protected String declareChar(final boolean varying, final int length) {
    return (varying ? "VARCHAR2" : "CHAR") + "(" + (length == 1 ? 2 : length) + " CHAR)";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefsqlj41207.html
  @Override
  protected Integer charMaxLength() {
    return 32672;
  }

  @Override
  protected String declareClob(final Long length) {
    return "CLOB";
  }

  // http://docs.oracle.com/javadb/10.6.2.1/ref/rrefclob.html
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
    return "TIMESTAMP(" + precision + ")";
  }

  @Override
  public String declareTime(final byte precision) {
    return "INTERVAL DAY(0) TO SECOND(" + precision + ")";
  }

  @Override
  public String declareInterval() {
    return "INTERVAL";
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    if (type._values$().isNull())
      return "VARCHAR2(0)";

    final List<String> enums = Dialect.parseEnum(type._values$().text());
    int maxLength = 0;
    for (final String value : enums)
      maxLength = Math.max(maxLength, value.length());

    return "VARCHAR2(" + maxLength + ")";
  }
}