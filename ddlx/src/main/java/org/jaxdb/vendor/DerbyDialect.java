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

public class DerbyDialect extends Dialect {
  @Override
  DBVendor getVendor() {
    return DBVendor.DERBY;
  }

  @Override
  public String quoteIdentifier(final String identifier) {
    return "\"" + identifier + "\"";
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
  public boolean allowsUnsignedNumeric() {
    return false;
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

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj15260.html
  @Override
  public String declareDecimal(Short precision, Short scale, final boolean unsigned) {
    if (precision == null)
      precision = 5;

    if (scale == null)
      scale = 0;

    assertValidDecimal(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj15260.html
  @Override
  public short decimalMaxPrecision() {
    return 31;
  }

  @Override
  Integer decimalMaxScale() {
    return null;
  }

  @Override
  String declareInt8(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  String declareInt16(final byte precision, final boolean unsigned) {
    return unsigned ? "INTEGER" : "SMALLINT";
  }

  @Override
  String declareInt32(final byte precision, final boolean unsigned) {
    return unsigned ? "BIGINT" : "INTEGER";
  }

  @Override
  String declareInt64(final byte precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  String declareBinary(final boolean varying, final long length) {
    return declareChar(varying || length > CHAR_MAX_LENGTH, length) + " FOR BIT DATA";
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj30118.html
  @Override
  Integer binaryMaxLength() {
    return 32700;
  }

  @Override
  String declareBlob(final Long length) {
    return "BLOB" + (length != null ? "(" + length + ")" : "");
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefblob.html
  @Override
  Long blobMaxLength() {
    return 2147483647L;
  }

  // https://builds.apache.org/job/Derby-docs/lastSuccessfulBuild/artifact/trunk/out/ref/rrefstringlimits.html
  private static final int VARCHAR_MAX_LENGTH = 32672;
  private static final int CHAR_MAX_LENGTH = 254;

  @Override
  String declareChar(final boolean varying, final long length) {
    final String type = varying ? "VARCHAR" : "CHAR";
    final int maxLength = varying ? VARCHAR_MAX_LENGTH : CHAR_MAX_LENGTH;
    if (length > maxLength)
      throw new IllegalArgumentException("Maximum length of " + type + " (" + maxLength + ") exceeded: " + length);

    return type + "(" + length + ")";
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefsqlj41207.html
  @Override
  Integer charMaxLength() {
    return 32672;
  }

  @Override
  String declareClob(final Long length) {
    if (length == null)
      return "CLOB";

    if (length > 2147483647)
      throw new IllegalArgumentException("Maximum length of CLOB (2147483647) exceeded: " + length);

    return "CLOB" + "(" + length + ")";
  }

  // https://db.apache.org/derby/docs/10.2/ref/rrefblob.html
  @Override
  Long clobMaxLength() {
    return 2147483647L;
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final byte precision) {
    return "TIMESTAMP";
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