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

import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Enum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PostgreSQLDialect extends Dialect {
  static final Logger logger = LoggerFactory.getLogger(PostgreSQLDialect.class);

  @Override
  public DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  public String quoteIdentifier(final String identifier) {
    return "\"" + identifier + "\"";
  }

  @Override
  public boolean allowsUnsignedNumeric() {
    return false;
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
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean unsigned) {
    return "FLOAT";
  }

  @Override
  public String declareDouble(final boolean unsigned) {
    return "DOUBLE PRECISION";
  }

  @Override
  public String declareDecimal(Integer precision, final Integer scale, final boolean unsigned) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + ", " + (scale != null ? scale : 0) + ")";
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

  // FIXME: What is default precision and scale?
  @Override
  public String declareBigDecimal(Integer precision, final Integer scale, final boolean unsigned) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + ", " + (scale != null ? scale : 0) + ")";
  }

  // FIXME: What is default precision and scale?
  @Override
  public int bigDecimalMaxPrecision() {
    return 1000;
  }

  // FIXME: What is default precision and scale?
  @Override
  Integer bigDecimalMaxScale() {
    return null;
  }

  @Override
  String declareInt8(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  String declareInt16(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  String declareInt32(final byte precision, final boolean unsigned) {
    return "INT";
  }

  @Override
  String declareInt64(final byte precision, final boolean unsigned) {
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
  public String declareDateTime(byte precision) {
    if (precision > 6) {
      logger.warn("TIMESTAMP(" + precision + ") precision will be reduced to maximum allowed: 6");
      precision = 6;
    }

    return "TIMESTAMP(" + precision + ")";
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
    return q(Dialect.getTypeName(type));
  }
}