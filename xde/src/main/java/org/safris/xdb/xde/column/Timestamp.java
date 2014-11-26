package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Timestamp extends Column<java.sql.Date> {
  public Timestamp(final Entity owner, final String cqlName, final String name, final java.sql.Date _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.TIMESTAMP, java.sql.Date.class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}