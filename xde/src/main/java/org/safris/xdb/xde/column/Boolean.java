package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Boolean extends Column<Boolean> {
  public Boolean(final Entity owner, final String cqlName, final String name, final Boolean _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.BOOLEAN, Boolean.class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}