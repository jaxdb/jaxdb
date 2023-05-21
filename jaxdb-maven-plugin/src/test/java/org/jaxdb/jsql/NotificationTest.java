/* Copyright (c) 2023 JAX-DB
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

import static org.jaxdb.jsql.Notification.Action.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.SQLNonTransientConnectionException;
import java.sql.SQLTransactionRollbackException;
import java.sql.SQLTransientException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.DBTestRunner.TestSpec;
import org.jaxdb.runner.SchemaTestRunner.TestSchema;
import org.jaxdb.runner.Vendor;
import org.junit.Test;
import org.libj.sql.exception.SQLInternalErrorException;
import org.libj.sql.exception.SQLOperatorInterventionException;
import org.libj.util.retry.RetryFailureRuntimeException;
import org.libj.util.retry.RetryPolicy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

public abstract class NotificationTest {
  static final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
    e.printStackTrace();
    System.err.flush();
    System.exit(1);
  };

  static {
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
  }

  static final Logger logger = LoggerFactory.getLogger(NotificationTest.class);

  public static boolean sql(final Exception e) {
    // 1. Retry these exceptions by default, and log with level=DEBUG.
    // FIXME: Replace with SQL State codes...
    if (e.getMessage() != null && (
        e instanceof SQLTransactionRollbackException && e.getMessage().contains("could not serialize access") ||
        e instanceof SQLInternalErrorException && e.getMessage().endsWith("tuple concurrently updated") ||
        e instanceof SQLFeatureNotSupportedException && e.getMessage().endsWith("cached plan must not change result type") ||
        e instanceof SQLNonTransientConnectionException && e.getMessage().startsWith("Connection Error"))) {
      if (logger.isDebugEnabled()) logger.debug(e.getMessage());
      return true;
    }

    // 2. All other SQLTransactionRollbackException(s) and SQLTransientException(s) with level=WARNING.
    if (e instanceof SQLTransactionRollbackException || e instanceof SQLTransientException || e instanceof SQLNonTransientConnectionException || e instanceof SQLOperatorInterventionException) {
      if (logger.isWarnEnabled()) logger.warn(e.getMessage(), e);
      return true;
    }

    // 3. IOException with level=INFO
    if (e instanceof IOException) {
      if (logger.isInfoEnabled()) logger.info(e.getMessage(), e);
      return true;
    }

    // 3. Otherwise, fail immediately.
    return false;
  }

  private static final RetryPolicy.Builder<RetryFailureRuntimeException> PERMA = new RetryPolicy
    .Builder<>((final Exception lastException, final List<Exception> suppressedExceptions, final int attemptNo, final long delayMs)
      -> new RetryFailureRuntimeException(lastException, attemptNo, delayMs))
        .withStartDelay(10)
        .withMaxRetries(Integer.MAX_VALUE)
        .withBackoffFactor(1.5)
        .withMaxDelayMs(1000);

  static final RetryPolicy<RetryFailureRuntimeException> PERMA_SQL = PERMA.withMaxRetries(Integer.MAX_VALUE).build(NotificationTest::sql);

  @Test
  @TestSpec(order = 0)
  @TestSchema(caching.class)
  public void setUp(final Connector connector, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.exit(1);
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    try (final Connection connection = connector.getConnection(null)) {
      DDLxTest.recreateSchema(connection, "caching");
    }

    connector.addNotificationListener(INSERT, UPDATE, DELETE, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }

      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), caching.getTables());
  }
}