package org.jaxdb.jsql;

import static org.jaxdb.jsql.Notification.Action.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.IntConsumer;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.data.Table;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.runner.Vendor;
import org.junit.Assert;
import org.junit.Test;
import org.libj.util.function.IntBooleanConsumer;
import org.xml.sax.SAXException;

public abstract class CachingTest {
  static final int sleepBefore = 5;
  static final int sleepAfter = 50;
  static final int iterations = 8;
  static final int idOffset = 1000000;

  private static final AtomicBoolean waiting = new AtomicBoolean();

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

  static void INSERT(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntConsumer async) throws IOException, SQLException {
    tryWait();
    org.jaxdb.jsql.DML.INSERT(row)
      .onNotify(t -> {
        async.accept(i);
        waiting.set(false);
        synchronized (waiting) {
          waiting.notify();
        }
      })
      .execute(transaction);

    transaction.commit();
    sync.accept(i);
  }

  static void UPDATE(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntBooleanConsumer async) throws IOException, SQLException {
    tryWait();
    final int count = org.jaxdb.jsql.DML.UPDATE(row)
      .onNotify(t -> {
        async.accept(i, false);
        waiting.set(true);
        executor.execute(() -> {
          sleep(sleepAfter);
          async.accept(i, true);
          waiting.set(false);
          synchronized (waiting) {
            waiting.notify();
          }
        });
      })
      .execute(transaction);

    System.err.println("count: " + count);
    transaction.commit();
    sync.accept(i);
  }

  static void DELETE(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntBooleanConsumer async) throws IOException, SQLException {
    tryWait();
    org.jaxdb.jsql.DML.DELETE(row)
      .onNotify(t -> {
        async.accept(i, false);
        waiting.set(true);
        executor.execute(() -> {
          sleep(sleepAfter);
          async.accept(i, true);
          waiting.set(false);
          synchronized (waiting) {
            waiting.notify();
          }
        });
      })
      .execute(transaction);

    transaction.commit();
    sync.accept(i);
  }

  static void assertEquals(final int i, final boolean afterSleep, final String message, final Object expected, final Object actual) {
    Assert.assertEquals("i = " + i + ", afterSleep = " + afterSleep + (message != null ? message : ""), expected, actual);
  }

  static void assertEquals(final int i, final boolean afterSleep, final Object expected, final Object actual) {
    assertEquals(i, afterSleep, null, expected, actual);
  }

  static void assertNull(final int i, final boolean afterSleep, final Object actual) {
    Assert.assertNull("i = " + i + ", afterSleep = " + afterSleep, actual);
  }

  @Test
  @Spec(order = 0)
  public void setUp(@Schema(caching.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = new UncaughtExceptionHandler() {
      @Override
      public void uncaughtException(final Thread t, final Throwable e) {
        e.printStackTrace();
        System.exit(1);
      }
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    DDLxTest.recreateSchema(transaction.getConnection(), "caching");
    transaction.commit();

    final Connector connector = transaction.getConnector();
    final Database database = Database.global(transaction.getSchemaClass());
    database.addNotificationListener(connector, INSERT, UPDATE, DELETE, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }

      @Override
      public void onFailure(final String sessionId, final data.Table<?> table, final Throwable t) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
      }

      @Override
      public Table<?> onUpdate(final String sessionId, final data.Table<?> row, final Map<String,String> keyForUpdate) {
        if (getConnector().getSchema().getSession(sessionId) != null)
          sleep(sleepBefore);

        return super.onUpdate(sessionId, row, keyForUpdate);
      }

      @Override
      public Table<?> onDelete(final String sessionId, final data.Table<?> row) {
        if (getConnector().getSchema().getSession(sessionId) != null)
          sleep(sleepBefore);

        return super.onDelete(sessionId, row);
      }
    }, new ConcurrentLinkedQueue<>(), caching.getTables());
  }
}