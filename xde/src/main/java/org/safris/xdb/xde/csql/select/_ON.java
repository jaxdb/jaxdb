package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Condition;
import org.safris.xdb.xde.Data;

public interface _ON<T extends Data<?>> {
  public ON<T> ON(final Condition<?> condition);
}