package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Entity;

public abstract class DML extends SELECT {
  public static class ALL extends Criteria {
    public ALL() {
      super("ALL");
    }
  }

  public static class DISTINCT extends Criteria {
    public DISTINCT() {
      super("DISTINCT");
    }
  }

  public static abstract class Criteria {
    private final String criteria;

    public Criteria(final String criteria) {
      this.criteria = criteria;
    }

    public String toString() {
      return criteria;
    }
  }

  public static ALL ALL = new ALL();
  public static DISTINCT DISTINCT = new DISTINCT();

  public static FROM SELECT(final Entity ... table) {
    return SELECT(null, null, table);
  }

  public static FROM SELECT(final ALL all, final Entity ... table) {
    return SELECT(all, null, table);
  }

  public static FROM SELECT(final DISTINCT distinct, final Entity ... table) {
    return SELECT(null, distinct, table);
  }

  public static FROM SELECT(final ALL all, final DISTINCT distinct, final Entity ... table) {
    String out = "";
    for (final Entity entity : table)
      out += ", " + SELECT.lookupAlias(entity, true);


    return new FROM("SELECT " + (all != null ? all + " " : "") + (distinct != null ? distinct + " " : "") + out.substring(2));
  }

  protected DML(final String clause) {
    super(clause.toString());
  }
}