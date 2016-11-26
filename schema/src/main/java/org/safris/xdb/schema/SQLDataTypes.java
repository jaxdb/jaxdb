/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.schema;

import java.math.BigInteger;

import org.safris.xdb.xds.xe.$xds_index;
import org.safris.xdb.xds.xe.$xds_integer;
import org.safris.xdb.xds.xe.$xds_named;
import org.safris.xdb.xds.xe.$xds_table;
import org.safris.commons.lang.Strings;
import org.safris.xsb.runtime.BindingList;

public final class SQLDataTypes {
  public static String csvNames(final BindingList<$xds_named> names) {
    return names.size() == 0 ? "" : csvNames(names.toArray(new $xds_named[names.size()]));
  }

  public static String csvNames(final $xds_named ... names) {
    if (names.length == 0)
      return "";

    String csv = "";
    for (final $xds_named name : names)
      csv += ", " + name._name$().text();

    return csv.length() > 0 ? csv.substring(2) : csv;
  }

  public static String getSequenceName(final $xds_table table, final $xds_integer column) {
    return "seq_" + table._name$().text() + "_" + column._name$().text();
  }

  public static String getIndexName(final $xds_table table, final $xds_index index, final $xds_named ... column) {
    if (index == null || column.length == 0)
      return null;

    String name = "";
    for (final $xds_named c : column)
      name += "_" + c._name$().text();

    return "idx_" + table._name$().text() + name;
  }

  public static String getIndexName(final $xds_table table, final $xds_table._indexes._index index) {
    return getIndexName(table, index, index._column().toArray(new $xds_named[index._column().size()]));
  }

  public static String getTriggerName(final String tableName, final $xds_table._triggers._trigger trigger, final String action) {
    return tableName + "_" + trigger._time$().text().toLowerCase() + "_" + action.toLowerCase();
  }

  public static String toEnumValue(String value) {
    value = value.replaceAll("\\\\\\\\", "\\\\"); // \\ is \
    value = value.replaceAll("\\\\_", " "); // \_ is <space>
    value = value.replaceAll("\\\\", ""); // anything else escaped is itself
    value = value.replace("'", "\\'"); // ' must be \'
    return value;
  }

  public static void checkValidNumber(final String var, final Integer precision, final Integer decimal) {
    if (precision < decimal)
      throw new IllegalArgumentException("[ERROR] ERROR 1427 (42000): For decimal(M,D), M must be >= D (column '" + var + "').");
  }

  public static int getNumericByteCount(final int precision, final boolean unsigned, BigInteger min, BigInteger max) {
    final BigInteger maxForPrecision = new BigInteger(Strings.createRepeat('9', precision));
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