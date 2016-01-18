package org.safris.xdb.xde;

import java.sql.SQLException;

public interface EntityIterator<T> {
  public boolean hasNext() throws SQLException;
  public T next() throws SQLException;
  public void remove() throws SQLException;
}