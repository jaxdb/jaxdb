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

  public static boolean sql(final Exception e, final int attemptNo, final long delayMs) {
    // 1. Retry these exceptions by default, and log with level=DEBUG.
    // FIXME: Replace with SQL State codes...
    final String message = e.getMessage();
    if (message != null && (e instanceof SQLTransactionRollbackException && message.contains("could not serialize access") ||
        e instanceof SQLInternalErrorException && message.endsWith("tuple concurrently updated") ||
        e instanceof SQLFeatureNotSupportedException && message.endsWith("cached plan must not change result type") ||
        e instanceof SQLNonTransientConnectionException && message.startsWith("Connection Error"))) {
      if (logger.isDebugEnabled()) { logger.debug(message); }
      return true;
    }

    // 2. All other SQLTransactionRollbackException(s) and SQLTransientException(s) with level=WARNING.
    if (e instanceof SQLTransactionRollbackException || e instanceof SQLTransientException || e instanceof SQLNonTransientConnectionException || e instanceof SQLOperatorInterventionException) {
      if (logger.isWarnEnabled()) { logger.warn(message, e); }
      return true;
    }

    // 3. IOException with level=INFO
    if (e instanceof IOException) {
      if (logger.isInfoEnabled()) { logger.info(message, e); }
      return true;
    }

    // 3. Otherwise, fail immediately.
    return false;
  }

  private static final RetryPolicy.Builder<RetryFailureRuntimeException> PERMA = new RetryPolicy.Builder<>((final Exception lastException, final List<Exception> suppressedExceptions, final int attemptNo, final long delayMs) -> new RetryFailureRuntimeException(lastException, attemptNo, delayMs))
    .withStartDelay(10)
    .withMaxRetries(Integer.MAX_VALUE)
    .withBackoffFactor(1.5)
    .withMaxDelayMs(1000);

  static final RetryPolicy<RetryFailureRuntimeException> PERMA_SQL = PERMA.withMaxRetries(Integer.MAX_VALUE).build(NotificationTest::sql);

  @Test
  @TestSpec(order = 0)
  public void setUp(final Caching caching) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.exit(1);
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

    final Connector connector = caching.getConnector();
    try (final Connection connection = connector.getConnection()) {
      DDLxTest.recreateSchema(connection, "caching");
    }

    caching.configCache(new DefaultCache(caching) {
      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), (final CacheConfig c) -> c.with(caching.getTables()));
  }
}