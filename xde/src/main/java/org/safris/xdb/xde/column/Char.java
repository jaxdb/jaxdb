package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Char extends Column<Character> {
  public Char(final Entity owner, final String cqlName, final String name, final Character _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.CHAR, Character.class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}