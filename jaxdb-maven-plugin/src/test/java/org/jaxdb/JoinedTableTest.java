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

import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class JoinedTableTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends JoinedTableTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends JoinedTableTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testCrossJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .CROSS_JOIN(c)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 3900);
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testNaturalJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .NATURAL_JOIN(c)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 300);
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInnerJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Employee e = classicmodels.Employee$;
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .JOIN(c)
          .ON(EQ(p.customerNumber, c.customerNumber))
          .JOIN(e)
          .ON(EQ(c.salesEmployeeNumber, e.employeeNumber))
          .execute(transaction)
    ) {

      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 300);
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testLeftOuterJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .LEFT_JOIN(c)
          .ON(EQ(p.purchaseNumber, c.customerNumber))
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 300);
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported(SQLite.class)
  public void testRightOuterJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .RIGHT_JOIN(c)
          .ON(EQ(p.purchaseNumber, c.customerNumber))
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 100);
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported({Derby.class, SQLite.class, MySQL.class})
  public void testFullOuterJoin(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BIGINT> rows =
        SELECT(COUNT(p))
          .FROM(p)
          .FULL_JOIN(c)
          .ON(EQ(p.purchaseNumber, c.customerNumber))
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsLong() > 300);
    }
  }
}