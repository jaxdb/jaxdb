package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Entity;

public class WHERE extends SELECT {
  public ON INNER_JOIN(final Entity ... table) {
    String out = "";
    for (final Entity entity : table)
      out += ", " + entity.getClass().getEnclosingClass().getSimpleName() + "." + entity.getClass().getSimpleName() + " " + lookupAlias(entity, true);

    return new ON(toString() + "\nINNER JOIN (" + out.substring(2) + ")");
  }

  public ON LEFT_OUTER_JOIN(final Entity entity) {
    final String out = entity.getClass().getEnclosingClass().getSimpleName() + "." + entity.getClass().getSimpleName() + " " + lookupAlias(entity, true);
    return new ON(toString() + "\nLEFT OUTER JOIN (" + out + ")");
  }

  public ON RIGHT_OUTER_JOIN(final Entity entity) {
    final String out = entity.getClass().getEnclosingClass().getSimpleName() + "." + entity.getClass().getSimpleName() + " " + lookupAlias(entity, true);
    return new ON(toString() + "\nRIGHT OUTER JOIN (" + out + ")");
  }

  public GROUP_BY WHERE(final Condition condition) {
    final String c = condition.toString();
    return new GROUP_BY(toString() + "\nWHERE " + c);
  }

  protected WHERE(final String clause) {
    super(clause);
  }
}