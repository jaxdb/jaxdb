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

import org.safris.rdb.ddlx.xe.$ddlx_enum;
import org.safris.rdb.ddlx.xe.$ddlx_table;

public class PostgreSQLDialect extends Dialect {
  @Override
  protected DBVendor getVendor() {
    return DBVendor.POSTGRE_SQL;
  }

  @Override
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return doublePrecision ? "DOUBLE PRECISION" : "REAL";
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
    return "SMALLINT";
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    return "INT";
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT";
  }

  @Override
  public String declareBinary(final boolean varying, final long length) {
    return "BYTEA";
  }

  @Override
  public String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final long length) {
    return "TEXT";
  }

  @Override
  public String declareBlob(final long length) {
    return "BYTEA";
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "TIMESTAMP(" + precision + ")";
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
    return Dialect.getTypeName(table._name$().text(), type._name$().text());
  }
}