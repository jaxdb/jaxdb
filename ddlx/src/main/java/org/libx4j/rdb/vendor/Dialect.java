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

import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;

import org.lib4j.util.NumberFormatter;
import org.libx4j.rdb.ddlx.xe.$ddlx_enum;
import org.libx4j.rdb.ddlx.xe.$ddlx_table;

public abstract class Dialect {
  protected static void checkValidNumber(final int precision, final short scale) {
    if (precision < scale)
      throw new IllegalArgumentException("[ERROR] ERROR 1427 (42000): For decimal(M,S), M [" + precision + "] must be >= S [" + scale + "].");
  }

  public static String getTypeName(final String tableName, final String columnName) {
    return "ty_" + tableName + "_" + columnName;
  }

  public static List<String> parseEnum(final String value) {
    final List<String> enums = new ArrayList<String>();
    final char[] chars = value.replace("\\\\", "\\").toCharArray();
    final StringBuilder builder = new StringBuilder();
    boolean escaped = false;
    for (int i = 0; i < chars.length; i++) {
      char ch = chars[i];
      if (ch == '\\') {
        escaped = true;
      }
      else if (ch != ' ' || escaped) {
        escaped = false;
        builder.append(ch);
      }
      else if (builder.length() > 0) {
        enums.add(builder.toString());
        builder.setLength(0);
      }
    }

    enums.add(builder.toString());
    return enums;
  }

  public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
  public static final ThreadLocal<DecimalFormat> NUMBER_FORMAT = NumberFormatter.createDecimalFormat("################.################;-################.################");
  public static final DateTimeFormatter TIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("H:m:s").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();
  public static final DateTimeFormatter DATETIME_FORMAT = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd H:m:s").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

  protected abstract DBVendor getVendor();

  public abstract int decimalMaxPrecision();
  public abstract boolean allowsUnsigned();

  public abstract String declareBoolean();

  public abstract String declareFloat(final boolean doublePrecision, final boolean unsigned);
  public abstract String declareDecimal(final int precision, final short scale, final boolean unsigned);

  public abstract String declareInt8(final short precision, final boolean unsigned);
  public abstract String declareInt16(final short precision, final boolean unsigned);
  public abstract String declareInt32(final short precision, final boolean unsigned);
  public abstract String declareInt64(final short precision, final boolean unsigned);

  public abstract String declareBinary(final boolean varying, final long length);
  public abstract String declareChar(final boolean varying, final long length);
  public abstract String declareClob(final long length);
  public abstract String declareBlob(final long length);
  public abstract String declareDate();
  public abstract String declareDateTime(final short precision);
  public abstract String declareTime(final short precision);
  public abstract String declareInterval();
  public abstract String declareEnum(final $ddlx_table table, final $ddlx_enum type);
}