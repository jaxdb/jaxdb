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
import org.jaxdb.jsql.type;
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
public abstract class CorrelatedSubQueryTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends CorrelatedSubQueryTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends CorrelatedSubQueryTest {
  }

  @Test
  @AssertSelect(conditionOnlyPrimary=false, cacheableExclusivity=false)
  public void testWhereEntity(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c1 = new classicmodels.Customer();
    final classicmodels.Customer c2 = new classicmodels.Customer();
    try (final RowIterator<data.Table> rows =

      SELECT(p, c2).
      FROM(p,
        SELECT(c1).
        FROM(c1).
        WHERE(GT(c1.creditLimit, 10)).
        AS(c2)).
      WHERE(AND(
        LT(p.purchaseDate, p.requiredDate),
        EQ(p.customerNumber, c2.customerNumber)))
          .execute(transaction)) {

      assertTrue(rows.nextRow());
      do {
        assertSame(p, rows.nextEntity());
        assertSame(c2, rows.nextEntity());
      }
      while (rows.nextRow());
    }
  }

  @Test
  @AssertSelect(conditionOnlyPrimary=false, cacheableExclusivity=false)
  public void testWhereColumn(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c1 = new classicmodels.Customer();
    final classicmodels.Customer c2 = new classicmodels.Customer();
    final data.CHAR cn = new data.CHAR();
    try (final RowIterator<type.Entity> rows =

      SELECT(p, c2.companyName.AS(cn)).
      FROM(p,
        SELECT(c1).
        FROM(c1).
        WHERE(GT(c1.creditLimit, 10)).
        AS(c2)).
      WHERE(AND(
        LT(p.purchaseDate, p.requiredDate),
        EQ(p.customerNumber, c2.customerNumber)))
          .execute(transaction)) {

      assertTrue(rows.nextRow());
      do {
        assertSame(p, rows.nextEntity());
        assertSame(cn, rows.nextEntity());
      }
      while (rows.nextRow());
    }
  }

  @Test
  @AssertSelect(conditionOnlyPrimary=false, cacheableExclusivity=false)
  public void testSelect(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c = classicmodels.Customer();
    final data.INT n = new data.INT();
    try (final RowIterator<type.Entity> rows =

      SELECT(p,
        SELECT(MAX(c.salesEmployeeNumber)).
        FROM(c).
        WHERE(GT(c.creditLimit, 11)).AS(n)).
      FROM(p).
      WHERE(LT(p.purchaseDate, p.requiredDate))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      do {
        assertSame(p, rows.nextEntity());
        assertSame(n, rows.nextEntity());
      }
      while (rows.nextRow());
    }
  }

  @Test
  @AssertSelect(conditionOnlyPrimary=false, cacheableExclusivity=false)
  public void testJoin(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    final classicmodels.Customer c = new classicmodels.Customer();

    final data.BIGINT pd = new data.BIGINT();
    final data.SMALLINT pn = new data.SMALLINT();
    try (final RowIterator<type.Entity> rows =

      SELECT(c, pd).
      FROM(c).
      JOIN(
        SELECT(
          p.customerNumber.AS(pn),
          COUNT(p.purchaseDate).AS(pd)).
        FROM(p).
        GROUP_BY(p.customerNumber).
        HAVING(NE(p.customerNumber, 10))).
      ON(EQ(c.customerNumber, pn)).
      WHERE(NE(c.customerNumber, 10))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      do {
        assertSame(c, rows.nextEntity());
        assertSame(pd, rows.nextEntity());
      }
      while (rows.nextRow());
    }
  }
}