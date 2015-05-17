package org.safris.xdb.xdl;

import java.math.BigInteger;

import org.safris.commons.lang.Strings;

public final class SQLDataTypes {
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