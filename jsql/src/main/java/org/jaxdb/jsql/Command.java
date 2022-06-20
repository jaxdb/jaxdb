/* Copyright (c) 2021 JAX-DB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * You should have received a copy of The MIT License (MIT) along with this
 * program. If not, see <http://opensource.org/licenses/MIT/>.
 */

package org.jaxdb.jsql;

import static org.libj.lang.Assertions.*;

import java.sql.Connection;
import java.util.UUID;

import org.jaxdb.jsql.Listener.OnCommit;
import org.jaxdb.jsql.Listener.OnExecute;
import org.jaxdb.jsql.Listener.OnNotify;
import org.jaxdb.jsql.Listener.OnNotifyListener;
import org.jaxdb.jsql.Listener.OnRollback;
import org.libj.lang.UUIDs;

abstract class Command<D extends data.Entity<?>,T> extends Keyword<D> implements Executable.Listenable<T> {
  abstract void onCommit(Connector connector, Connection connection);

  Listener listeners;

  Listener getListeners() {
    return listeners == null ? listeners = new Listener() : listeners;
  }

  @Override
  public T onExecute(final OnExecute listener) {
    Transaction.Event.EXECUTE.add(getListeners(), null, listener);
    return (T)this;
  }

  abstract static class Modify<D extends data.Entity<?>,T extends Executable.Modify> extends Command<D,T> implements Executable.Modify.Listenable<T> {
    String sessionId;

    @Override
    public T onCommit(final OnCommit listener) {
      Transaction.Event.COMMIT.add(getListeners(), null, listener);
      return (T)this;
    }

    @Override
    public T onRollback(final OnRollback listener) {
      Transaction.Event.ROLLBACK.add(getListeners(), null, listener);
      return (T)this;
    }

    @Override
    public T onNotify(long timeout, final OnNotify listener) {
      if (timeout == 0)
        timeout = Long.MAX_VALUE;
      else
        assertPositive(timeout);

      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      Transaction.Event.NOTIFY.add(getListeners(), sessionId, new OnNotifyListener(listener, timeout));
      return (T)this;
    }

    @Override
    public T onNotify(final OnNotify listener) {
      return onNotify(Long.MAX_VALUE, listener);
    }
  }
}