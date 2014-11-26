package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Int extends Column<Long> {
  public final int precision;
  public final boolean unsigned;
  public final Long min;
  public final Long max;

  public Int(final Entity owner, final String cqlName, final String name, final Long _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Long min, final Long max) {
    super(Types.INTEGER, Long.class, owner, cqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }
}