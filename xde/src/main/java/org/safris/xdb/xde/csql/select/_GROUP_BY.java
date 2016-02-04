package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Field;
import org.safris.xdb.xde.Data;

public interface _GROUP_BY<T extends Data<?>> {
  public GROUP_BY<T> GROUP_BY(final Field<?> field);
}