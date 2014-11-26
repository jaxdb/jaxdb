package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Entity;

public class SmallInt extends MediumInt {
  public SmallInt(final Entity owner, final String cqlName, final String name, final Integer _default, final boolean unique, final boolean primary, final boolean nullable, final int precision, final boolean unsigned, final Integer min, final Integer max) {
    super(Types.INTEGER, Integer.class, owner, cqlName, name, _default, unique, primary, nullable, precision, unsigned, min, max);
  }
}