package org.safris.xdb.xde.csql;

import org.safris.xdb.xde.Transaction;
import org.safris.xdb.xde.XDEException;

public interface ExecuteUpdate {
  public int execute(final Transaction transaction) throws XDEException;
  public int execute() throws XDEException;
}
