/* Copyright (c) 2017 JAX-DB
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

package org.jaxdb;

import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class LimitExpressionTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends LimitExpressionTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends LimitExpressionTest {
  }

  @Test
  @AssertSelect(entityOnlySelect=true, absolutePrimaryKeyCondition=false, rowIteratorFullConsume=true)
  public void testLimitPrimary(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<classicmodels.Product> rows =

      SELECT(p).
      FROM(p).
      ORDER_BY(p.code).
      LIMIT(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals("S10_1678", rows.nextEntity().code.get());
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(entityOnlySelect=false, absolutePrimaryKeyCondition=false, rowIteratorFullConsume=true)
  public void testLimit(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(3)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(33.19, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(22.57, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(35.36, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(15.91, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(37.76, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(16.24, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(entityOnlySelect=false, absolutePrimaryKeyCondition=false, rowIteratorFullConsume=true)
  public void testLimitOffset(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(2).
      OFFSET(1)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(35.36, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(15.91, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(37.76, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(16.24, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertFalse(rows.nextRow());
    }
  }
}