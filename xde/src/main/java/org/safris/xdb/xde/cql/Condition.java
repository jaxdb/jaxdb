package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Column;

public abstract class Condition extends SELECT {
  protected static String format(final Object column) {
    if (column instanceof Column)
      return SELECT.toString((Column<?>)column);

    if (column instanceof String)
      return "'" + column + "'";

    return column.toString();
  }

  protected Condition(final String operator, final Object a, final Object b) {
    super(format(a) + " " + operator + " " + format(b));
  }

  protected Condition(final String clause) {
    super(clause);
  }
}