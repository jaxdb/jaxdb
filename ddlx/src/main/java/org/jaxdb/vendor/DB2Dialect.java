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
import org.libj.math.BigInt;

public class DB2Dialect extends Dialect {
  @Override
  public DBVendor getVendor() {
    return DBVendor.DB2;
  }

  @Override
  public String quoteIdentifier(final String identifier) {
    return "\"" + identifier + "\"";
  }

  @Override
  public String currentTimeFunction() {
    return "CURRENT TIME";
  }

  @Override
  public String currentDateFunction() {
    return "CURRENT DATE";
  }

  @Override
  public String currentDateTimeFunction() {
    return "CURRENT TIMESTAMP";
  }

  @Override
  public String currentTimestampMillisecondsFunction() {
    // FIXME:...
    throw new UnsupportedOperationException("FIXME");
  }

  @Override
  public String currentTimestampSecondsFunction() {
    return "EXTRACT(EPOCH FROM NOW())";
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

  private static final BigInt maxBigintUnsigned = new BigInt(Long.MAX_VALUE);

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
    return "FLOAT";
  }

  @Override
  public String declareDouble(final boolean unsigned) {
    return "DOUBLE";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0000791.html
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

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_11.0.0/intro/src/tpc/db2z_numericdatatypes.html
  @Override
  public int decimalMaxPrecision() {
    return 31;
  }

  @Override
  Integer decimalMaxScale() {
    return null;
  }

  @Override
  String declareInt8(final Byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  String declareInt16(final Byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  String declareInt32(final Byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  String declareInt64(final Byte precision, final boolean unsigned) {
    throw new UnsupportedOperationException();
  }

  @Override
  String declareBinary(final boolean varying, final long length) {
    return "VARBINARY" + "(" + length + ")";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/sqlref/src/tpc/db2z_bif_varbinary.html
  @Override
  Integer binaryMaxLength() {
    return 32704;
  }

  @Override
  String declareBlob(final Long length) {
    return "BLOB" + (length != null ? "(" + length + ")" : "");
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  Long blobMaxLength() {
    return 2147483647L;
  }

  @Override
  String declareChar(final boolean varying, final long length) {
    return varying && length <= 255 ? "CHAR(" + length + ")" : "VARCHAR(" + length + ")";
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_11.0.0/intro/src/tpc/db2z_stringdatatypes.html
  @Override
  Integer charMaxLength() {
    return 32704;
  }

  @Override
  String declareClob(final Long length) {
    return "CLOB" + (length != null ? "(" + length + ")" : "");
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  Long clobMaxLength() {
    return 2147483647L;
  }

  @Override
  public String declareDate() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareDateTime(final Byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareTime(final Byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareInterval() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String declareEnum(final $Enum type) {
    throw new UnsupportedOperationException();
  }
}