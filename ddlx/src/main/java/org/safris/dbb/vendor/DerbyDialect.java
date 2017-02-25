/* Copyright (c) 2017 Seva Safris
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

package org.safris.dbb.vendor;

import java.util.List;

import org.safris.dbb.ddlx.xe.$ddlx_enum;
import org.safris.dbb.ddlx.xe.$ddlx_table;

public class DerbyDialect extends Dialect {
  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE" : "FLOAT";
  }

  @Override
  public String declareDecimal(final short precision, final short scale, final boolean unsigned) {
    Dialect.checkValidNumber(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")";
  }

  @Override
  public String declareInt8(final short precision, final boolean unsigned) {
    return "SMALLINT";
  }

  @Override
  public String declareInt16(final short precision, final boolean unsigned) {
    return unsigned ? "INTEGER" : "SMALLINT";
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    return unsigned ? "BIGINT" : "INTEGER";
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  public String declareBinary(final boolean varying, final long length) {
    return "CHAR" + (varying ? " VARYING" : "") + "(" + length + ") FOR BIT DATA";
  }

  @Override
  public String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final long length) {
    return "CLOB(" + length + ")";
  }

  @Override
  public String declareBlob(final long length) {
    return "BLOB" + "(" + length + ")";
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "TIMESTAMP";
  }

  @Override
  public String declareTime(final short precision) {
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