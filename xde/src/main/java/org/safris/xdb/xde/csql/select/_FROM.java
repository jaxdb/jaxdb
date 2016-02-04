package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Data;
import org.safris.xdb.xde.Entity;

public interface _FROM<T extends Data<?>> {
  public FROM<T> FROM(final Entity ... tables);
}