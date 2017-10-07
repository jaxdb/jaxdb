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

import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;

public class DB2Dialect extends Dialect {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.DB2;
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

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0000791.html
  @Override
  public String declareDecimal(Short precision, Short scale, final boolean unsigned) {
    if (precision == null)
      precision = 31;

    if (scale == null)
      scale = 0;

    assertValidDecimal(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_11.0.0/intro/src/tpc/db2z_numericdatatypes.html
  @Override
  public short decimalMaxPrecision() {
    return 31;
  }

  @Override
  protected Integer decimalMaxScale() {
    return null;
  }

  @Override
  protected String declareInt8(final byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected String declareInt16(final byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected String declareInt32(final byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected String declareInt64(final byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  protected String declareBinary(final boolean varying, final int length) {
    return "VARBINARY" + "(" + length + ")";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/sqlref/src/tpc/db2z_bif_varbinary.html
  @Override
  protected Integer binaryMaxLength() {
    return 32704;
  }

  @Override
  protected String declareBlob(final Long length) {
    return "BLOB" + (length != null ? "(" + length + ")" : "");
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  protected Long blobMaxLength() {
    return 2147483647l;
  }

  @Override
  protected String declareChar(final boolean varying, final int length) {
    return varying && length <= 255 ? "CHAR(" + length + ")" : "VARCHAR(" + length + ")";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_11.0.0/intro/src/tpc/db2z_stringdatatypes.html
  @Override
  protected Integer charMaxLength() {
    return 32704;
  }

  @Override
  protected String declareClob(final Long length) {
    return "CLOB" + (length != null ? "(" + length + ")" : "");
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  protected Long clobMaxLength() {
    return 2147483647l;
  }

  @Override
  public String declareDate() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareDateTime(final byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareTime(final byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareInterval() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareEnum(final $ddlx_table table, final $ddlx_enum type) {
    throw new UnsupportedOperationException();
  }
}