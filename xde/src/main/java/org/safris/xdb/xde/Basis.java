package org.safris.xdb.xde;

public abstract class Basis {
  protected static Entity getOwner(final Column<?> column) {
    return column.owner;
  }

  protected static String getCQLName(final Column<?> column) {
    return column.cqlName;
  }
}