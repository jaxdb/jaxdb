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

import org.jaxdb.www.ddlx_0_5.xLygluGCXAA.$Enum;

public class DB2Dialect extends Dialect {
  DB2Dialect() {
    super(DBVendor.DB2);
  }

  @Override
  public short constraintNameMaxLength() {
    return (short)18;
  }

  @Override
  public StringBuilder quoteIdentifier(final StringBuilder b, final CharSequence identifier) {
    return b.append('"').append(identifier).append('"');
  }

  @Override
  public StringBuilder currentTimeFunction(final StringBuilder b) {
    return b.append("CURRENT TIME");
  }

  @Override
  public StringBuilder currentDateFunction(final StringBuilder b) {
    return b.append("CURRENT DATE");
  }

  @Override
  public StringBuilder currentDateTimeFunction(final StringBuilder b) {
    return b.append("CURRENT TIMESTAMP");
  }

  @Override
  public StringBuilder currentTimestampMillisecondsFunction(final StringBuilder b) {
    // FIXME:...
    throw new UnsupportedOperationException("FIXME");
  }

  @Override
  public StringBuilder currentTimestampSecondsFunction(final StringBuilder b) {
    return b.append("EXTRACT(EPOCH FROM NOW())");
  }

  @Override
  public StringBuilder currentTimestampMinutesFunction(final StringBuilder b) {
    return currentTimestampSecondsFunction(b).append(" / 60");
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

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0000791.html
  @Override
  public StringBuilder declareDecimal(final StringBuilder b, final Integer precision, Integer scale, final BigDecimal min) {
    if (precision == null) {
      if (scale != null)
        throw new IllegalArgumentException("DECIMAL(precision=null,scale=" + scale + ")");
    }
    else {
      if (scale == null)
        scale = 0;

      assertValidDecimal(precision, scale);
      return b.append("DECIMAL(").append(precision).append(',').append(scale).append(')');
    }

    return b.append("DECIMAL");
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
  StringBuilder declareInt8(final StringBuilder b, final Byte precision, final Byte min) {
    throw new UnsupportedOperationException();
  }

  @Override
  StringBuilder declareInt16(final StringBuilder b, final Byte precision, final Short min) {
    throw new UnsupportedOperationException();
  }

  @Override
  StringBuilder declareInt32(final StringBuilder b, final Byte precision, final Integer min) {
    throw new UnsupportedOperationException();
  }

  @Override
  StringBuilder declareInt64(final StringBuilder b, final Byte precision, final Long min) {
    throw new UnsupportedOperationException();
  }

  @Override
  StringBuilder declareBinary(final StringBuilder b, final boolean varying, final long length) {
    return b.append("VARBINARY(").append(length).append(')');
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_10.0.0/sqlref/src/tpc/db2z_bif_varbinary.html
  @Override
  Integer binaryMaxLength() {
    return 32704;
  }

  @Override
  StringBuilder declareBlob(final StringBuilder b, final Long length) {
    b.append("BLOB");
    if (length != null)
      b.append('(').append(length).append(')');

    return b;
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  Long blobMaxLength() {
    return 2147483647L;
  }

  @Override
  StringBuilder declareChar(final StringBuilder b, final boolean varying, final long length) {
    return b.append(varying && length <= 255 ? "CHAR(" : "VARCHAR(").append(length).append(')');
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPEK_11.0.0/intro/src/tpc/db2z_stringdatatypes.html
  @Override
  Integer charMaxLength() {
    return 32704;
  }

  @Override
  StringBuilder declareClob(final StringBuilder b, final Long length) {
    b.append("CLOB");
    if (length != null)
      b.append('(').append(length).append(')');

    return b;
  }

  // https://www.ibm.com/support/knowledgecenter/en/SSEPGG_9.7.0/com.ibm.db2.luw.sql.ref.doc/doc/r0001029.html
  @Override
  Long clobMaxLength() {
    return 2147483647L;
  }

  @Override
  public StringBuilder declareDate(final StringBuilder b) {
    throw new UnsupportedOperationException();
  }

  @Override
  public StringBuilder declareDateTime(final StringBuilder b, final Byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public StringBuilder declareTime(final StringBuilder b, final Byte precision) {
    throw new UnsupportedOperationException();
  }

  @Override
  public StringBuilder declareInterval(final StringBuilder b) {
    throw new UnsupportedOperationException();
  }

  @Override
  public StringBuilder declareEnum(final StringBuilder b, final $Enum column, final String enumValues, final Map<String,Map<String,String>> tableNameToEnumToOwner) {
    throw new UnsupportedOperationException();
  }
}