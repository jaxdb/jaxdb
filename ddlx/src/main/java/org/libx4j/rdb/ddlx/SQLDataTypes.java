/* Copyright (c) 2015 lib4j
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

package org.libx4j.rdb.ddlx;

import java.math.BigInteger;

import org.lib4j.lang.Arrays;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Index;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Integer;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Named;
import org.libx4j.rdb.ddlx098.xLzAluECXYQJdhA.$Table;
import org.libx4j.xsb.runtime.BindingList;

final class SQLDataTypes {
  protected static String csvNames(final BindingList<$Named> names) {
    return names.size() == 0 ? "" : csvNames(names.toArray(new $Named[names.size()]));
  }

  protected static String csvNames(final $Named ... names) {
    if (names.length == 0)
      return "";

    String csv = "";
    for (final $Named name : names)
      csv += ", " + name.getName$().text();

    return csv.length() > 0 ? csv.substring(2) : csv;
  }

  protected static String getSequenceName(final $Table table, final $Integer column) {
    return "seq_" + table.getName$().text() + "_" + column.getName$().text();
  }

  protected static String getTriggerName(final $Table table, final $Integer column) {
    return "trg_" + table.getName$().text() + "_" + column.getName$().text();
  }

  protected static String getIndexName(final $Table table, final $Index index, final $Named ... column) {
    if (index == null || column.length == 0)
      return null;

    String name = "";
    for (final $Named c : column)
      name += "_" + c.getName$().text();

    return "idx_" + table.getName$().text() + name;
  }

  protected static String getIndexName(final $Table table, final $Table.Indexes.Index index) {
    return getIndexName(table, index, index.getColumn().toArray(new $Named[index.getColumn().size()]));
  }

  protected static String getTriggerName(final String tableName, final $Table.Triggers.Trigger trigger, final String action) {
    return tableName + "_" + trigger.getTime$().text().toLowerCase() + "_" + action.toLowerCase();
  }

  protected static int getNumericByteCount(final int precision, final boolean unsigned, BigInteger min, BigInteger max) {
    final BigInteger maxForPrecision = new BigInteger(String.valueOf(Arrays.createRepeat('9', precision)));
    if (max == null)
      max = maxForPrecision;
    else if (maxForPrecision.compareTo(max) == -1)
      throw new IllegalArgumentException("max of " + max + " cannot be contained in a precision of " + precision + (unsigned ? " un" : " ") + "signed digits");

    final BigInteger minForPrecision = unsigned ? BigInteger.ZERO : maxForPrecision.negate();
    if (min == null)
      min = minForPrecision;
    else if (min.compareTo(minForPrecision) == -1)
      throw new IllegalArgumentException("min of " + min + " cannot be contained in a precision of " + precision + (unsigned ? " un" : " ") + "signed digits");

    // FIXME: getting longValue of BigInteger!!!! Cause I was having issues taking a log2 of a BigInteger
    return (int)Math.ceil((Math.log(max.subtract(min).doubleValue()) / Math.log(2)) / 8);
  }

  private SQLDataTypes() {
  }
}