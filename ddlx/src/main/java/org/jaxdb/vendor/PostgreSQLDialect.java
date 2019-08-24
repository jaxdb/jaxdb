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
  protected static final Logger logger = LoggerFactory.getLogger(PostgreSQLDialect.class);

  @Override
  protected DBVendor getVendor() {
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
  public String declareDecimal(Short precision, final Short scale, final boolean unsigned) {
    if (precision == null && scale != null)
      precision = scale;

    assertValidDecimal(precision, scale);
    return precision == null ? "DECIMAL" : "DECIMAL(" + precision + ", " + (scale != null ? scale : 0) + ")";
  }

  // https://www.postgresql.org/docs/9.6/static/datatype-numeric.html
  @Override
  public short decimalMaxPrecision() {
    return 1000;
  }

  @Override
  protected Integer decimalMaxScale() {
    return null;
  }

  @Override
  protected String declareInt8(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  protected String declareInt16(final byte precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  protected String declareInt32(final byte precision, final boolean unsigned) {
    return "INT";
  }

  @Override
  protected String declareInt64(final byte precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  protected String declareBinary(final boolean varying, final long length) {
    return "BYTEA";
  }

  @Override
  protected Integer binaryMaxLength() {
    return null;
  }

  @Override
  protected String declareBlob(final Long length) {
    return "BYTEA";
  }

  @Override
  protected Long blobMaxLength() {
    return null;
  }

  @Override
  protected String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  protected Integer charMaxLength() {
    return null;
  }

  @Override
  protected String declareClob(final Long length) {
    return "TEXT";
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
    if (precision > 6)
      logger.warn("TIMESTAMP(" + precision + ") precision will be reduced to maximum allowed, 6");

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
    return Dialect.getTypeName(type);
  }
}