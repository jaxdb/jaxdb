package org.safris.xdb.xde.cql;

public final class Constant {
  public static final Constant NULL = new Constant("NULL");

  private final String value;

  private Constant(final String value) {
    this.value = value;
  }

  public String toString() {
    return value;
  }
}