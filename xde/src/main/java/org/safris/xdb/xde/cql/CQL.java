package org.safris.xdb.xde.cql;

import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.xde.Basis;
import org.safris.xdb.xde.Column;
import org.safris.xdb.xde.Entity;

public abstract class CQL extends Basis {
  // This is implemented as a ThreadLocal variable, because of one main reason:
  // The CQL query contruct crosses into static space when applying Condition(s),
  // such as AND, OR, EQ, etc. A ThreadLocal reference to the aliases list works,
  // because the CQL query is rendered immediately upon construct. As it is
  // guaranteed to run linearly in a single thread, a ThreadLocal variable fits.
  private static final ThreadLocal<List<Entity>> aliases = new ThreadLocal<List<Entity>>() {
    protected List<Entity> initialValue() {
      return new ArrayList<Entity>();
    }
  };

  protected static String lookupAlias(final Entity entity, final boolean register) {
    final List<Entity> list = aliases.get();
    int i;
    for (i = 0; i < list.size(); i++)
      if (list.get(i) == entity)
        return String.valueOf((char)('a' + i));

    if (!register)
      return null;

    list.add(entity);
    return String.valueOf((char)('a' + i));
  }

  protected static String toString(final Column<?> column) {
    return CQL.lookupAlias(getOwner(column), false) + "." + getCQLName(column);
  }
}