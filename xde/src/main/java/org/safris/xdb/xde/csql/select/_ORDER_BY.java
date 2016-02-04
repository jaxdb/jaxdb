package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Field;
import org.safris.xdb.xde.Data;

public interface _ORDER_BY<T extends Data<?>> {
  public ORDER_BY<T> ORDER_BY(final Field<?> ... column);
}