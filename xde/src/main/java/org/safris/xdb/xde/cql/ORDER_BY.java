package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Column;

public class ORDER_BY extends SELECT {
  public static class Direction {
    private final String direction;

    public Direction(final String direction) {
      this.direction = direction;
    }

    public String toString() {
      return direction;
    }
  }

  public static Direction ASC = new Direction("ASC");
  public static Direction DESC = new Direction("DESC");

  public SELECT ORDER_BY(final Column column) {
    return ORDER_BY(column, ASC);
  }

  public SELECT ORDER_BY(final Column column, final Direction direction) {
    return new WHERE(toString() + "\nORDER BY " + SELECT.toString(column) + " " + direction);
  }

  protected ORDER_BY(final String clause) {
    super(clause);
  }
}