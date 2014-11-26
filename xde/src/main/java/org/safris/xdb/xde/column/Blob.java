package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Blob extends Column<byte[]> {
  public Blob(final Entity owner, final String cqlName, final String name, final byte[] _default, final boolean unique, final boolean primary, final boolean nullable) {
    super(Types.BLOB, byte[].class, owner, cqlName, name, _default, unique, primary, nullable);
  }
}