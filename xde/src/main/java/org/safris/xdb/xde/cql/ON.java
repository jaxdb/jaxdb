package org.safris.xdb.xde.cql;


public class ON extends SELECT {
  public static class Direction {
    private final String direction;

    public Direction(final String direction) {
      this.direction = direction;
    }

    public String toString() {
      return direction;
    }
  }

  public WHERE ON(final Condition condition) {
    return new WHERE(toString() + " ON (" + condition.toString() + ")");
  }

  protected ON(final String clause) {
    super(clause);
  }
}