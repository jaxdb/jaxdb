package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Varchar extends Column<String> {
  public final int length;

  public Varchar(final Entity owner, final String cqlName, final String name, final String _default, final boolean unique, final boolean primary, final boolean nullable, final int length) {
    super(Types.VARCHAR, String.class, owner, cqlName, name, _default, unique, primary, nullable);
    this.length = length;
  }
}