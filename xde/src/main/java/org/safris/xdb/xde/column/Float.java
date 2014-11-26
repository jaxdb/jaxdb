package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Entity;

public class Float extends Decimal {
  public Float(final Entity owner, final String cqlName, final String name, final Double _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final int decimal, final boolean unsigned, final Number min, final Number max) {
    super(Types.FLOAT, Double.class, owner, cqlName, name, _default, unique, primary, nullable, precision, decimal, unsigned, min, max);
  }
}