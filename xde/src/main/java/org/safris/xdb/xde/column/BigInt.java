package org.safris.xdb.xde.column;

import java.math.BigInteger;
import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class BigInt extends Column<BigInteger> {
  public final int precision;
  public final boolean unsigned;
  public final BigInteger min;
  public final BigInteger max;

  // FIXME: This is not properly supported by Derby, as in derby, only signed numbers are allowed. But in MySQL, unsigned values of up to 18446744073709551615 are allowed.
  public BigInt(final Entity owner, final String cqlName, final String name, final BigInteger _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final BigInteger min, final BigInteger max) {
    super(Types.BIGINT, BigInteger.class, owner, cqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }
}