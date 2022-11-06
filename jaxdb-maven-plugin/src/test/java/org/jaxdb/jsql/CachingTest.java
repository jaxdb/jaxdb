package org.jaxdb.jsql;

import static org.jaxdb.jsql.Notification.Action.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.sql.SQLTimeoutException;
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
import org.jaxdb.jsql.keyword.Delete.DELETE_NOTIFY;
import org.jaxdb.jsql.keyword.Insert.CONFLICT_ACTION_NOTIFY;
import org.jaxdb.jsql.keyword.Update.UPDATE_NOTIFY;
import org.jaxdb.jsql.statement.NotifiableModification;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
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

  static void INSERT(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntConsumer async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final CONFLICT_ACTION_NOTIFY statement =
        DML.INSERT(row)
          .onNotify((e, idx, cnt) -> {
            async.accept(i);
            return true;
          });

    final NotifiableResult result = exec(transaction, i, statement);

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    waiting.set(false);
    synchronized (waiting) {
      waiting.notify();
    }

    sync.accept(i);
  }

  static void UPDATE(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntBooleanConsumer async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final UPDATE_NOTIFY statement =
      DML.UPDATE(row)
        .onNotify((e, idx, cnt) -> {
          async.accept(i, false);
          return false;
        });

    final NotifiableResult result = exec(transaction, i, statement);

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    waiting.set(true);
    executor.execute(() -> {
      sleep(sleepAfter);
      async.accept(i, true);
      waiting.set(false);
      synchronized (waiting) {
        waiting.notify();
      }
    });

    sync.accept(i);
  }

  static NotifiableResult exec(final Transaction transaction, final int i, final NotifiableModification statement) throws IOException, SQLException {
    return i % 2 == 0 ? statement.execute(transaction) : new Batch(statement).execute(transaction);
  }

  static void DELETE(final Transaction transaction, final data.Table<?> row, final int i, final IntConsumer sync, final IntBooleanConsumer async) throws InterruptedException, IOException, SQLException {
    tryWait();

    final DELETE_NOTIFY statement =
      DML.DELETE(row)
        .onNotify((e, idx, cnt) -> {
          async.accept(i, false);
          return true;
        });

    final NotifiableResult result = exec(transaction, i, statement);

    transaction.commit();
    if (!result.awaitNotify(100))
      throw new SQLTimeoutException();

    waiting.set(true);
    executor.execute(() -> {
      sleep(sleepAfter);
      async.accept(i, true);
      waiting.set(false);
      synchronized (waiting) {
        waiting.notify();
      }
    });

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
      public void onFailure(final String sessionId, final long timestamp, final data.Table<?> table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }

      @Override
      public Table<?> onUpdate(final String sessionId, final long timestamp, final data.Table<?> row, final Map<String,String> keyForUpdate) {
        if (getConnector().getSchema().getSession(sessionId) != null)
          sleep(sleepBefore);

        return super.onUpdate(sessionId, timestamp, row, keyForUpdate);
      }

      @Override
      public Table<?> onDelete(final String sessionId, final long timestamp, final data.Table<?> row) {
        if (getConnector().getSchema().getSession(sessionId) != null)
          sleep(sleepBefore);

        return super.onDelete(sessionId, timestamp, row);
      }
    }, new ConcurrentLinkedQueue<>(), caching.getTables());
  }
}