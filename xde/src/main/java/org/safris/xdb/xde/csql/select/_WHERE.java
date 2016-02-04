package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Condition;
import org.safris.xdb.xde.Data;

public interface _WHERE<T extends Data<?>> {
  public WHERE<T> WHERE(final Condition<?> condition);
}