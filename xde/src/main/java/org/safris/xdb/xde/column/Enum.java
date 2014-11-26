package org.safris.xdb.xde.column;

import java.sql.Types;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public class Enum<T extends java.lang.Enum<?>> extends Column<T> {
  public Enum(final Entity owner, final String cqlName, final String name, final T _default, final boolean unique, final boolean primary, final boolean nullable, final Class<T> enumClass) {
    super(Types.VARCHAR, enumClass, owner, cqlName, name, _default, unique, primary, nullable);
  }
}