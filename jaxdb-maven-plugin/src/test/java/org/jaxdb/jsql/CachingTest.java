package org.jaxdb.jsql;

import static org.jaxdb.jsql.Notification.Action.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.runner.Vendor;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public abstract class CachingTest {
  private static final short delay = 150;
  static final int iterations = 8;
  static final int idOffset = 1000000;

  static void INSERT(final Transaction transaction, final data.Table<?> row) throws IOException, SQLException {
    org.jaxdb.jsql.DML.INSERT(row)
      .execute(transaction);

    transaction.commit();
    try {
      Thread.sleep(delay);
    }
    catch (final InterruptedException e) {
      e.printStackTrace();
    }
  }

  static void UPDATE(final Transaction transaction, final data.Table<?> row, final Runnable sync, final Runnable async) throws IOException, SQLException {
    org.jaxdb.jsql.DML.UPDATE(row)
      .execute(transaction);

    transaction.commit();

    sync.run();

    try {
      Thread.sleep(delay);
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    async.run();
  }

  static void DELETE(final Transaction transaction, final data.Table<?> row, final Runnable sync, final Runnable async) throws IOException, SQLException {
    org.jaxdb.jsql.DML.DELETE(row)
      .execute(transaction);

    transaction.commit();

    sync.run();

    try {
      Thread.sleep(delay);
    }
    catch (final InterruptedException e) {
      throw new RuntimeException(e);
    }

    async.run();
  }

  static void assertSame(final Object expected, final Object actual) {
    Assert.assertEquals(expected, actual);
  }

  @Test
  @Spec(order = 0)
  public void setUp(@Schema(caching.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), "caching");
    transaction.commit();

    final Connector connector = transaction.getConnector();
    final Database database = Database.global(transaction.getSchemaClass());
    database.addNotificationListener(connector, INSERT, UPDATE, DELETE, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }
    }, new ConcurrentLinkedQueue<>(), caching.getTables());
  }
}