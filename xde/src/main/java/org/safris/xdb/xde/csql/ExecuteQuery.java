package org.safris.xdb.xde.csql;

import org.safris.xdb.xde.Data;
import org.safris.xdb.xde.RowIterator;
import org.safris.xdb.xde.Transaction;
import org.safris.xdb.xde.XDEException;

public interface ExecuteQuery<T extends Data<?>> {
  public RowIterator<T> execute(final Transaction transaction) throws XDEException;
  public RowIterator<T> execute() throws XDEException;
}