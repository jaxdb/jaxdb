package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.cql.ORDER_BY.Direction;

public class GROUP_BY extends SELECT {
  public HAVING GROUP_BY(final Column<?> column) {
    return new HAVING(toString() + "\nGROUP BY " + SELECT.toString(column));
  }

  public SELECT ORDER_BY(final Column<?> column, final Direction direction) {
    return new WHERE(toString() + "\nORDER BY " + SELECT.toString(column) + " " + direction);
  }

  protected GROUP_BY(final String clause) {
    super(clause);
  }
}