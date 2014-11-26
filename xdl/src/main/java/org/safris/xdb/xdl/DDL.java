package org.safris.xdb.xdl;

public class DDL {
  public static enum Type {
    CREATE,
    DROP
  }

  public final String name;
  public final String[] drop;
  public final String[] create;

  public DDL(final String name, final String[] drop, final String[] create) {
    this.name = name;
    this.drop = drop;
    this.create = create;
  }

  public String[] get(final Type type) {
    return type == Type.CREATE ? create : type == Type.DROP ? drop : null;
  }
}