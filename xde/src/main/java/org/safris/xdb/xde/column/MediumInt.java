package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class MediumInt extends Column<Integer> {
  public final int precision;
  public final boolean unsigned;
  public final Integer min;
  public final Integer max;

  public MediumInt(final Entity owner, final String cqlName, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Integer min, final Integer max) {
    this(Types.INTEGER, Integer.class, owner, cqlName, name, _default, unique, primary, nullable, precision, unsigned, min, max);
  }

  public MediumInt(final int sqlType, final Class<Integer> type, final Entity owner, final String cqlName, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Integer min, final Integer max) {
    super(sqlType, type, owner, cqlName, name, _default, unique, primary, nullable);
    this.precision = precision;
    this.unsigned = unsigned;
    this.min = min;
    this.max = max;
  }
}