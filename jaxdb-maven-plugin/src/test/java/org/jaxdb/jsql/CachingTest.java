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

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.IntConsumer;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.CacheConfig.OnConnectPreLoad;
import org.jaxdb.jsql.keyword.Delete.DELETE_NOTIFY;
import org.jaxdb.jsql.keyword.Insert.CONFLICT_ACTION_NOTIFY;
import org.jaxdb.jsql.keyword.Update.UPDATE_NOTIFY;
import org.jaxdb.jsql.statement.NotifiableModification;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
import org.jaxdb.runner.DBTestRunner.TestSpec;
import org.jaxdb.runner.SchemaTestRunner.TestSchema;
import org.jaxdb.runner.Vendor;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public abstract class CachingTest {
  private static final int sleepCascade = 5;

  static final int iterations = 8;
  static final int idOffset = 1000000;

  static void sleep(final int sleepTime) {
    try {
      Thread.sleep(sleepTime);
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }
    finally {
      System.err.println("sleep " + sleepTime);
    }
  }

  private static final AtomicBoolean waiting = new AtomicBoolean();

  private static final Executor executor = Executors.newFixedThreadPool(1);

  private static void tryWait() {
    if (waiting.get()) {
      synchronized (waiting) {
        if (waiting.get()) {
          try {
            waiting.wait();
          }
          catch (final InterruptedException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }
  }

  static void INSERT(final Transaction transaction, final data.Table row, final int i, final ThrowingIntConsumer<Exception> sync, final IntConsumer async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final AtomicInteger count = new AtomicInteger();

    final CONFLICT_ACTION_NOTIFY statement =
      DML.INSERT(row)
        .onNotify((e, idx, cnt) -> {
          count.getAndAdd(cnt);
          async.accept(i);
          return true;
        });

    final NotifiableResult result = exec(transaction, i, statement);

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    Assert.assertEquals(result.getCount(), count.get());

    waiting.set(false);
    synchronized (waiting) {
      waiting.notify();
    }

    sync.accept(i);
  }

  static void UPDATE(final Transaction transaction, final data.Table row, final int i, final boolean sleepForCascade, final ThrowingIntConsumer<Exception> sync, final ThrowingIntBooleanConsumer<Exception> async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final AtomicInteger count = new AtomicInteger();

    final UPDATE_NOTIFY statement =
      DML.UPDATE(row)
        .onNotify((e, idx, cnt) -> {
          count.getAndAdd(cnt);
          async.accept(i, false);
          return true;
        });

    final NotifiableResult result = exec(transaction, i, statement);
    Assert.assertNotEquals(0, result.getCount());

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    Assert.assertEquals(result.getCount(), count.get());

    sync.accept(i);

    waiting.set(true);
    executor.execute(() -> {
      if (sleepForCascade)
        sleep(sleepCascade);

      async.accept(i, true);
      waiting.set(false);
      synchronized (waiting) {
        waiting.notify();
      }
    });
  }

  static NotifiableResult exec(final Transaction transaction, final int i, final NotifiableModification statement) throws IOException, SQLException {
    return i % 2 == 0 ? statement.execute(transaction) : new Batch(statement).execute(transaction);
  }

  static void DELETE(final Transaction transaction, final data.Table row, final int i, final boolean sleepForCascade, final ThrowingIntConsumer<Exception> sync, final ThrowingIntBooleanConsumer<Exception> async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final AtomicInteger count = new AtomicInteger();

    final DELETE_NOTIFY statement =
      DML.DELETE(row)
        .onNotify((e, idx, cnt) -> {
          count.getAndAdd(cnt);
          async.accept(i, false);
          return true;
        });

    final NotifiableResult result = exec(transaction, i, statement);

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    Assert.assertEquals(result.getCount(), count.get());

    sync.accept(i);

    waiting.set(true);
    executor.execute(() -> {
      if (sleepForCascade)
        sleep(sleepCascade);

      async.accept(i, true);
      waiting.set(false);
      synchronized (waiting) {
        waiting.notify();
      }
    });
  }

  static void assertEquals(final int i, final boolean afterSleep, final String message, final Object expected, final Object actual) {
    Assert.assertEquals("i = " + i + ", afterSleep = " + afterSleep + (message != null ? " " + message : ""), expected, actual);
  }

  static void assertEquals(final int i, final boolean afterSleep, final Object expected, final Object actual) {
    assertEquals(i, afterSleep, null, expected, actual);
  }

  static void assertNull(final int i, final boolean afterSleep, final Object actual) {
    Assert.assertNull("i = " + i + ", afterSleep = " + afterSleep, actual);
  }

  @Test
  @TestSpec(order = 0)
  @TestSchema(caching.class)
  public void setUp(final Connector connector, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.err.flush();
      System.exit(1);
    };

    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    try (final Connection connection = connector.getConnection(null)) {
      DDLxTest.recreateSchema(connection, "caching");
    }

    connector.getSchema().configCache(connector, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }

      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), c -> c.with(OnConnectPreLoad.ALL, caching.getTables()));
  }
}