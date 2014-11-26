package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class TinyInt extends Column<Short> {
  public final int precision;
  public final boolean unsigned;
  public final Short min;
  public final Short max;

  public TinyInt(final Entity owner, final String cqlName, final String name, final Short _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Short min, final Short max) {
    super(Types.TINYINT, Short.class, owner, cqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }
}