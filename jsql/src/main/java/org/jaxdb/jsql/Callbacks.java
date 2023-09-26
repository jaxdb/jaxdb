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

import static org.libj.lang.Assertions.*;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.IntConsumer;

import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
import org.libj.util.DelegateCollection;
import org.libj.util.MultiHashMap;
import org.libj.util.MultiMap;
import org.libj.util.function.ThrowingObjBiIntPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Callbacks implements Closeable {
  private static final Logger logger = LoggerFactory.getLogger(Callbacks.class);

  public interface OnExecute extends IntConsumer {
  }

  public interface OnCommit extends IntConsumer {
  }

  public interface OnRollback extends Runnable {
  }

  /**
   * Predicate to be called for each notification generated by the DB as a result of the statement to which {@code this}
   * {@link OnNotify} is associated, with the following parameters:
   * <ol>
   * <li><code>({@link Exception} t)</code>: An exception that occurs while waiting for a notification generated by the DB.</li>
   * <li>{@code (int v1)}: The {@code index} of the notification generated by the DB.</li>
   * <li>{@code (int v2)}: The total {@code count} of the notifications expected to be generated by the DB.</li>
   * </ol>
   * Since the notifications generated by the DB are asynchronous, {@link NotifiableResult#awaitNotify(long)} can be used to block the
   * current thread until:
   * <ul>
   * <li>the return from {@link Callbacks.OnNotify OnNotify#test(Exception,int,int)} is {@code false}.</li>
   * <li>the receipt of all notifications generated by the DB as a result of {@code this} statement, or</li>
   * </ul>
   */
  public interface OnNotify extends ThrowingObjBiIntPredicate<Exception,Exception> {
  }

  static class OnNotifyCallback implements ThrowingObjBiIntPredicate<Exception,Exception> {
    final OnNotify onNotify;
    final Boolean b;
    final AtomicReference<OnNotifyCallback> next = new AtomicReference<>();

    OnNotifyCallback(final OnNotify onNotify) {
      this.onNotify = assertNotNull(onNotify);
      this.b = null;
    }

    OnNotifyCallback(final boolean onNotify) {
      this.onNotify = null;
      this.b = onNotify;
    }

    @Override
    public boolean testThrows(final Exception e, final int index, final int count) throws Exception {
      if (onNotify != null)
        return onNotify.testThrows(e, index, count);

      if (e != null)
        throw e;

      return b;
    }
  }

  static class OnNotifyCallbackList extends DelegateCollection<OnNotifyCallback> implements BiConsumer<Schema,Exception> {
    final String sessionId;
    private final AtomicInteger count = new AtomicInteger(-1);
    private final AtomicInteger indexIn = new AtomicInteger();
    private final AtomicInteger indexOut = new AtomicInteger();
    private final AtomicReference<OnNotifyCallback> root = new AtomicReference<>();
    private final AtomicReference<OnNotifyCallback> head = new AtomicReference<>();

    OnNotifyCallbackList(final String sessionId) {
      this.sessionId = sessionId;
    }

    void setCount(final int count) {
      synchronized (this.count) {
        this.count.set(count);
        this.count.notify();
      }
    }

    boolean await(final long timeout) throws InterruptedException {
      if (timeout <= 0)
        return false;

      if (isEmpty())
        return true;

      int index = this.indexOut.get();
      int count = this.count.get();
      if (index == count) {
        clear();
        return true;
      }

      synchronized (this.count) {
        try {
          index = this.indexOut.get();
          count = this.count.get();
          if (index == count)
            return true;

          final long ts = System.currentTimeMillis();
          // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].await(" + timeout + ")"); }
          this.count.wait(timeout);
          return System.currentTimeMillis() - ts < timeout || this.indexOut.get() == this.count.get();
        }
        finally {
          clear();
        }
      }
    }

    @Override
    public void accept(final Schema schema, final Exception e) {
      final int index = this.indexIn.incrementAndGet();
      int count = this.count.get();
      if (count == -1) {
        synchronized (this.count) {
          count = this.count.get();
          if (count == -1) {
            try {
              this.count.wait();
            }
            catch (final InterruptedException ie) {
              throw new IllegalStateException(ie);
            }
          }
        }
      }

      if (index > count)
        throw new IllegalStateException("index (" + index + ") > count (" + count + ") for sessionId = " + sessionId, e);

      try {
        OnNotifyCallback prev = null;
        // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].test(" +
        // ObjectUtil.simpleIdentityString(e) + "): " + index + " " + count + "... " + ObjectUtil.simpleIdentityString(root.get()) + " " +
        // ObjectUtil.simpleIdentityString(head.get())); }
        for (OnNotifyCallback next, cursor = root.get(); cursor != null; prev = cursor, cursor = next) { // [X]
          next = cursor.next.get();

          boolean retain;
          try {
            // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].testThrows(" +
            // ObjectUtil.simpleIdentityString(e) + "): " + index + " " + count); }
            retain = cursor.testThrows(e, index, count);
          }
          catch (final Exception e1) {
            if (logger.isWarnEnabled()) { logger.warn(e1.getMessage(), e1); }
            retain = false;
          }

          if (!retain)
            (prev != null ? prev.next : root).set(next);
        }

        head.set(prev);
      }
      finally {
        if (indexOut.incrementAndGet() == count || isEmpty()) {
          schema.removeSession(sessionId);
          synchronized (this.count) {
            // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].testThrows(" +
            // ObjectUtil.simpleIdentityString(e) + ").notify()"); }
            this.count.notify();
            clear();
          }
        }
      }
    }

    @Override
    public boolean add(final OnNotifyCallback e) {
      // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].add(" +
      // ObjectUtil.simpleIdentityString(e) + ")"); }
      final OnNotifyCallback head = this.head.get();
      if (head != null)
        head.next.set(e);
      else
        root.set(e);

      return true;
    }

    @Override
    public void clear() {
      // if (logger.isTraceEnabled()) { logger.trace(getClass().getSimpleName() + "[" + sessionId + "].clear() " + indexIn.get() + " " +
      // count.get(), new Exception()); }
      root.set(null);
      head.set(null);
    }

    @Override
    public boolean isEmpty() {
      return root == null;
    }

    @Override
    public String toString() {
      if (root.get() == null)
        return "[]";

      final StringBuilder builder = new StringBuilder();
      for (OnNotifyCallback cursor = root.get(); cursor != null; cursor = cursor.next.get()) // [X]
        builder.append(',').append(cursor.toString());

      builder.setCharAt(0, '[');
      return builder.toString();
    }
  }

  ArrayList<OnExecute> onExecutes;
  ArrayList<OnCommit> onCommits;
  ArrayList<OnRollback> onRollbacks;
  MultiMap<String,OnNotifyCallback,OnNotifyCallbackList> onNotifys;

  void addOnExecute(final OnExecute onExecute) {
    assertNotClosed();
    (onExecutes == null ? onExecutes = new ArrayList<>() : onExecutes).add(onExecute);
  }

  void addOnCommit(final OnCommit onCommit) {
    assertNotClosed();
    (onCommits == null ? onCommits = new ArrayList<>() : onCommits).add(onCommit);
  }

  void addOnRollback(final OnRollback onRollback) {
    assertNotClosed();
    (onRollbacks == null ? onRollbacks = new ArrayList<>() : onRollbacks).add(onRollback);
  }

  void addOnNotify(final String sessionId, final OnNotify onNotify) {
    addOnNotify(sessionId, new OnNotifyCallback(onNotify));
  }

  void addOnNotify(final String sessionId, final boolean onNotify) {
    addOnNotify(sessionId, new OnNotifyCallback(onNotify));
  }

  private void addOnNotify(final String sessionId, final OnNotifyCallback onNotify) {
    assertNotClosed();
    (onNotifys == null ? onNotifys = new MultiHashMap<>(() -> new OnNotifyCallbackList(sessionId)) : onNotifys).add(sessionId, onNotify);
  }

  void onExecute(final int count) {
    final ArrayList<OnExecute> onExecutes = this.onExecutes;
    if (onExecutes != null) {
      final int size = onExecutes.size();
      if (size != 0) {
        try {
          int i = 0;
          do
            onExecutes.get(i).accept(count);
          while (++i < size);
        }
        finally {
          onExecutes.clear();
        }
      }
    }
  }

  void onCommit(final int count) {
    final ArrayList<OnCommit> onCommits = this.onCommits;
    if (onCommits != null) {
      final int size = onCommits.size();
      if (size > 0) {
        try {
          int i = 0;
          do
            onCommits.get(i).accept(count);
          while (++i < size);
        }
        finally {
          onCommits.clear();
        }
      }
    }
  }

  void onRollback() {
    final ArrayList<OnRollback> onRollbacks = this.onRollbacks;
    if (onRollbacks != null) {
      final int size = onRollbacks.size();
      if (size > 0) {
        try {
          int i = 0;
          do
            onRollbacks.get(i).run();
          while (++i < size); // [RA]
        }
        finally {
          onRollbacks.clear();
        }
      }
    }
  }

  void merge(final Callbacks callbacks) {
    assertNotClosed();
    if (onCommits == null)
      onCommits = callbacks.onCommits;
    else if (callbacks.onCommits != null)
      onCommits.addAll(callbacks.onCommits);

    if (onRollbacks == null)
      onRollbacks = callbacks.onRollbacks;
    else if (callbacks.onRollbacks != null)
      onRollbacks.addAll(callbacks.onRollbacks);

    if (onNotifys == null) {
      onNotifys = callbacks.onNotifys;
    }
    else if (callbacks.onNotifys != null && callbacks.onNotifys.size() > 0) {
      for (final Map.Entry<String,OnNotifyCallbackList> entry : callbacks.onNotifys.entrySet()) { // [S]
        final OnNotifyCallbackList list = onNotifys.get(entry.getKey());
        if (list != null)
          list.addAll(entry.getValue());
        else
          onNotifys.put(entry.getKey(), entry.getValue());
      }
    }
  }

  private boolean closed;

  private void assertNotClosed() {
    if (closed)
      throw new IllegalStateException("Closed");
  }

  @Override
  public void close() {
    if (closed)
      return;

    closed = true;

    if (onExecutes != null) {
      onExecutes.clear();
      onExecutes = null;
    }

    if (onCommits != null) {
      onCommits.clear();
      onCommits = null;
    }

    if (onRollbacks != null) {
      onRollbacks.clear();
      onRollbacks = null;
    }

    if (onNotifys != null) {
      onNotifys.clear();
      onNotifys = null;
    }
  }

  Callbacks() {
  }
}