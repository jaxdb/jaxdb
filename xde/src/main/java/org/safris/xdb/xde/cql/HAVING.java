package org.safris.xdb.xde.cql;

public class HAVING extends SELECT {
  public ORDER_BY HAVING(final Condition condition) {
    return new ORDER_BY(toString() + "\nHAVING " + condition.toString());
  }

  protected HAVING(final String clause) {
    super(clause);
  }
}