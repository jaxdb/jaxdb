package org.safris.xdb.xde.csql.select;

import org.safris.xdb.xde.Data;
import org.safris.xdb.xde.Entity;
import org.safris.xdb.xde.DML.NATURAL;
import org.safris.xdb.xde.DML.TYPE;

public interface _JOIN<T extends Data<?>> {
  public JOIN<T> JOIN(final Entity entity);
  public JOIN<T> JOIN(final TYPE type, final Entity entity);
  public JOIN<T> JOIN(final NATURAL natural, final Entity entity);
  public JOIN<T> JOIN(final NATURAL natural, final TYPE type, final Entity entity);
}