package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

// FIXME: this is the wrong type, as it doesnt carry a time
public class DateTime extends Column<java.sql.Timestamp> {
  public DateTime(final Entity owner, final String cqlName, final String name, final java.sql.Timestamp _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.TIMESTAMP, java.sql.Timestamp.class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}