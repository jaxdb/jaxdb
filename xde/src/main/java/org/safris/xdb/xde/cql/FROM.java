package org.safris.xdb.xde.cql;

import org.safris.xdb.xde.Entity;

public class FROM extends SELECT {
  public WHERE FROM(final Entity ... table) {
    String out = "";
    for (final Entity entity : table)
      out += ", " + entity.getClass().getEnclosingClass().getSimpleName() + "." + entity.getClass().getSimpleName() + " " + lookupAlias(entity, true);

    return new WHERE(toString() + "\nFROM " + out.substring(2));
  }

  protected FROM(final String clause) {
    super(clause);
  }
}