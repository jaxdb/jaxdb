/* Copyright (c) 2015 JAX-DB
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

package org.jaxdb.ddlx;

import java.math.BigInteger;

import org.jaxdb.vendor.Dialect;
import org.jaxdb.www.ddlx_0_4.xLygluGCXAA.$Named;
import org.jaxsb.runtime.BindingList;
import org.libj.math.SafeMath;
import org.libj.util.ArrayUtil;

public final class SQLDataTypes {
  static String csvNames(final Dialect dialect, final BindingList<$Named> names) {
    return names.size() == 0 ? "" : csvNames(dialect, names.toArray(new $Named[names.size()]));
  }

  static String csvNames(final Dialect dialect, final $Named ... names) {
    if (names.length == 0)
      return "";

    final StringBuilder csv = new StringBuilder();
    for (int i = 0; i < names.length; ++i) {
      if (i > 0)
        csv.append(", ");

      csv.append(dialect.quoteIdentifier(names[i].getName$().text()));
    }

    return csv.toString();
  }

  static int getNumericByteCount(final int precision, final boolean unsigned, BigInteger min, BigInteger max) {
    final BigInteger maxForPrecision = new BigInteger(String.valueOf(ArrayUtil.createRepeat('9', precision)));
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
    return (int)Math.ceil((SafeMath.log2(max.subtract(min).doubleValue())) / 8);
  }

  private SQLDataTypes() {
  }
}