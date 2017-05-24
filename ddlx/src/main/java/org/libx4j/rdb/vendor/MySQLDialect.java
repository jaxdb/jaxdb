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
  public String declareBoolean() {
    return "BOOLEAN";
  }

  @Override
  public String declareFloat(final boolean doublePrecision, final boolean unsigned) {
    return (doublePrecision ? "DOUBLE" : "FLOAT") + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareDecimal(final short precision, final short scale, final boolean unsigned) {
    Dialect.checkValidNumber(precision, scale);
    return "DECIMAL(" + precision + ", " + scale + ")" + (unsigned ? " UNSIGNED" : "");
  }

  @Override
  public String declareInt8(final short precision, final boolean unsigned) {
    return "TINYINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt16(final short precision, final boolean unsigned) {
    return "SMALLINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt32(final short precision, final boolean unsigned) {
    if (unsigned && precision < 9)
      return "MEDIUMINT(" + precision + ") UNSIGNED";

    if (!unsigned && precision < 8)
      return "MEDIUMINT(" + precision + ")";

    return "INT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareInt64(final short precision, final boolean unsigned) {
    return "BIGINT(" + precision + (unsigned ? ") UNSIGNED" : ")");
  }

  @Override
  public String declareBinary(final boolean varying, final long length) {
    return (varying ? "VAR" : "") + "BINARY" + "(" + length + ")";
  }

  @Override
  public String declareChar(final boolean varying, final long length) {
    return (varying ? "VARCHAR" : "CHAR") + "(" + length + ")";
  }

  @Override
  public String declareClob(final long length) {
    return "TEXT(" + length + ")";
  }

  @Override
  public String declareBlob(final long length) {
    return "BLOB(" + length + ")";
  }

  @Override
  public String declareDate() {
    return "DATE";
  }

  @Override
  public String declareDateTime(final short precision) {
    return "DATETIME(" + precision + ")";
  }

  @Override
  public String declareTime(final short precision) {
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