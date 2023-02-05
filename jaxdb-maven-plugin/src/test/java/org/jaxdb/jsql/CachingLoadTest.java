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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import org.jaxdb.jsql.keyword.Insert.CONFLICT_ACTION_NOTIFY;
import org.jaxdb.jsql.keyword.Update.UPDATE_NOTIFY;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.util.concurrent.ThreadFactoryBuilder;
import org.libj.util.function.Throwing;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
@Ignore("Need to finish implementing this")
public abstract class CachingLoadTest extends NotificationTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends CachingLoadTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends CachingLoadTest {
  }

  private static final int cardinality = 10;
  private static final int iterations = 100;

  @Test
  public void test(@Schema(caching.class) final Transaction transaction) throws Exception {
    final ExecutorService executor = Executors.newFixedThreadPool(iterations, new ThreadFactoryBuilder()
      .withUncaughtExceptionHandler(uncaughtExceptionHandler)
      .build());

    for (int i = 0; i < iterations; ++i) {
      final int iteration = i;
      final int j = i % 3;
      if (j == 0) {
        executor.execute(Throwing.rethrow(() -> {
          final Integer[] ids = new Integer[cardinality];

          final AtomicInteger count = new AtomicInteger();
          for (int c = 0; c < cardinality; ++c) {
            final int id = ids[c] = iteration * c;
            exec(() -> insertOne(id, count), count);
            exec(() -> insertOneOne(id, count), count);
          }

          count.set(0);
          final caching.OneOneId oo = new caching.OneOneId();
          exec(() -> update(oo, IN(oo.oneId, ids), count), count);
        }));
      }
      else if (j == 1) {
        executor.execute(Throwing.rethrow(() -> {
          final Integer[] ids = new Integer[cardinality];

          final AtomicInteger count = new AtomicInteger();
          for (int c = 0; c < cardinality; ++c) {
            final int id = ids[c] = iteration * c;
            exec(() -> insertOne(id, count), count);
            exec(() -> insertOneOne(id, count), count);
          }

          for (int k = 0, j$ = ids.length; k < j$; ++k) { // [RA]
            final Integer id = ids[k];
            final caching.OneOneId oo = new caching.OneOneId();

            count.set(0);
            exec(() -> update(oo, EQ((data.Column<?>)oo.oneId, id), count), count);
          }
        }));
      }
      else {
        executor.execute(Throwing.rethrow(() -> {
          final Integer[] ids = new Integer[cardinality];

          final AtomicInteger count = new AtomicInteger();
          for (int c = 0; c < cardinality; ++c) {
            final int id = ids[c] = iteration * c;
            exec(() -> insertOne(id, count), count);
            exec(() -> insertOneOne(id, count), count);
          }

          exec(() -> {
            final caching.OneOneId oo = new caching.OneOneId();
            final Batch batch = new Batch();
            for (int k = 0, j$ = ids.length; k < j$; ++k) { // [RA]
              final Integer id = ids[k];
              batch.addStatement(update(oo, EQ((data.Column<?>)oo.oneId, id), count));
            }

            return batch;
          }, count);
        }));
      }
    }

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);

    for (int id = 0; id < cardinality * iterations; ++id) { // [RA]
      final caching.One o = caching.One.idToOne(id);
      assertEquals(id, 1 + iterations, o.id$OneOneId_oneId().version.getAsInt());
    }
  }

  private static CONFLICT_ACTION_NOTIFY insertOne(final int id, final AtomicInteger count) {
    final caching.One o = new caching.One();
    o.id.set(id);
    o.idu.set(id);
    o.idx1.set(id);
    o.idx2.set(id);
    return insert(o, count);
  }

  private static CONFLICT_ACTION_NOTIFY insertOneOne(final int id, final AtomicInteger count) {
    final caching.OneOneId oo = new caching.OneOneId();
    oo.oneId.set(id);
    return insert(oo, count);
  }

  private static CONFLICT_ACTION_NOTIFY insert(final data.Table<?> t, final AtomicInteger count) {
    return
      INSERT(t)
        .onNotify((e, idx, cnt) -> {
          if (e != null)
            throw e;

          if (cnt == 0)
            throw new IllegalArgumentException();

          count.getAndAdd(cnt);
          return false;
        });
  }

  private static UPDATE_NOTIFY update(final caching.OneOneId oo, final Condition<?> condition, final AtomicInteger count) {
    return
      UPDATE(oo).
      SET(oo.version, ADD(oo.version, 1)).
      WHERE(condition)
        .onNotify((e, idx, cnt) -> {
          if (e != null)
            throw e;

          if (cnt == 0)
            throw new IllegalArgumentException();

          count.getAndAdd(cnt);
          return false;
        });
  }

  private static void exec(final Supplier<statement.NotifiableModification> update, final AtomicInteger count) {
    PERMA_SQL.run(() -> {
      try (final Transaction transaction = new Transaction(caching.class, "repeatable")) {
        final NotifiableResult result = update.get().execute(transaction);

        transaction.commit();
        if (!result.awaitNotify(1000))
          throw new IllegalStateException("Timeout: " + Arrays.toString(result.getSessionId()));

        assertEquals(Arrays.toString(result.getSessionId()), result.getCount(), count.get());
      }
    });
  }
}