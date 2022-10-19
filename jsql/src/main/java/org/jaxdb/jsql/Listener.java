/* Copyright (c) 2022 JAX-DB
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

import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

import org.jaxdb.jsql.Transaction.Event;
import org.libj.sql.exception.SQLOperatorInterventionException;
import org.libj.util.MultiHashMap;
import org.libj.util.MultiMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Listener {
  private static final Logger logger = LoggerFactory.getLogger(Listener.class);

  public interface OnCommit extends IntConsumer {
  }

  public interface OnExecute extends IntConsumer {
  }

  public interface OnNotify extends Consumer<Throwable> {
  }

  public interface OnRollback extends Runnable {
  }

  static class OnNotifyListener implements Comparable<OnNotifyListener>, Consumer<Object> {
    private final AtomicBoolean done = new AtomicBoolean();
    final OnNotify onNotify;
    final long timeout;

    OnNotifyListener(final OnNotify onNotify, final long timeout) {
      this.onNotify = onNotify;
      this.timeout = timeout;
    }

    @Override
    public int compareTo(final OnNotifyListener o) {
      return Long.compare(timeout, o.timeout);
    }

    @Override
    public void accept(final Object t) {
      done.set(true);
      if (onNotify != null) {
        onNotify.accept(t == null ? null : t instanceof Throwable ? (Throwable)t : new SQLTimeoutException((String)t));
      }
      else if (logger.isDebugEnabled()) {
        if (t instanceof Throwable) {
          final Throwable throwable = (Throwable)t;
          logger.debug(throwable.getMessage(), t);
        }
        else {
          logger.debug((String)t);
        }
      }
    }
  }

  static class OnNotifies extends TreeSet<OnNotifyListener> {
    private final AtomicBoolean done = new AtomicBoolean();
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    int count;

    void await() throws SQLException {
      if (done.get() || count <= 0)
        return;

      lock.lock();
      try {
        if (!done.get() || count > 0) {
          final long startTime = System.currentTimeMillis();
          if (size() > 0) {
            for (final OnNotifyListener listener : this) { // [S]
              if (listener.done.get())
                continue;

              final long now = System.currentTimeMillis();
              final long sleep = startTime + listener.timeout - now;
              if (sleep <= 0 || !condition.await(sleep, TimeUnit.MILLISECONDS) && !listener.done.get())
                listener.accept("Elapsed " + listener.timeout + "ms timeout awaiting NOTIFY");
            }
          }

          done.set(true);
        }
      }
      catch (final InterruptedException e) {
        throw new SQLOperatorInterventionException(e);
      }
      finally {
        lock.unlock();
        clear();
      }
    }

    void accept(final Throwable t) {
      if (done.get())
        return;

      if (size() > 0) {
        for (final OnNotifyListener listener : this) { // [S]
          if (done.get())
            break;

          if (!listener.done.get())
            listener.accept(t);
        }
      }

      lock.lock();
      done.set(true);
      try {
        condition.signal();
      }
      finally {
        lock.unlock();
        clear();
      }
    }
  }

  ArrayList<OnExecute> execute;
  ArrayList<OnCommit> commit;
  ArrayList<OnRollback> rollback;
  MultiMap<String,OnNotifyListener,OnNotifies> notify;

  ArrayList<OnExecute> getExecute() {
    return execute == null ? execute = new ArrayList<>() : execute;
  }

  ArrayList<OnCommit> getCommit() {
    return commit == null ? commit = new ArrayList<>() : commit;
  }

  ArrayList<OnRollback> getRollback() {
    return rollback == null ? rollback = new ArrayList<>() : rollback;
  }

  MultiMap<String,OnNotifyListener,OnNotifies> getNotify() {
    return notify == null ? notify = new MultiHashMap<>(OnNotifies::new) : notify;
  }

  void onExecute(final String sessionId, final int count) {
    Event.EXECUTE.notify(this, null, null, count);
    if (sessionId == null)
      return;

    final OnNotifies onNotifies = notify.get(sessionId);
    if (onNotifies != null)
      onNotifies.count = count;
  }

  void onCommit(final int count) {
    Event.COMMIT.notify(this, null, null, count);
  }

  void onRollback() {
    Event.ROLLBACK.notify(this, null, null, Statement.EXECUTE_FAILED);
  }

  void clear() {
    if (execute != null)
      execute.clear();

    if (commit != null)
      commit.clear();

    if (rollback != null)
      rollback.clear();

    if (notify != null)
      notify.clear();
  }

  Listener() {
  }
}