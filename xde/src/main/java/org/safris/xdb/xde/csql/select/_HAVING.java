package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Condition;
import org.safris.xdb.xde.Data;

public interface _HAVING<T extends Data<?>> {
  public HAVING<T> HAVING(final Condition<?> condition);
}