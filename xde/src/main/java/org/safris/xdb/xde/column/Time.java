package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Time extends Column<java.sql.Time> {
  public Time(final Entity owner, final String cqlName, final String name, final java.sql.Time _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.TIME, java.sql.Time.class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}