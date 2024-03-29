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
public abstract class ComparisonPredicateTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends ComparisonPredicateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends ComparisonPredicateTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testLt(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          OR(
            LT(p.customerNumber, 100),
            LT(50, p.customerNumber),
            LT(p.comments, p.status)),
          SELECT(OR(
            LT(p.customerNumber, 100),
            LT(50, p.customerNumber),
            LT(p.comments, p.status)))
            .FROM(p)
            .WHERE(OR(
              LT(p.customerNumber, 100),
              LT(50, p.customerNumber),
              LT(p.comments, p.status)))
            .LIMIT(1))
          .FROM(p)
          .WHERE(OR(
            LT(p.customerNumber, 100),
            LT(50, p.customerNumber),
            LT(p.comments, p.status)))
          .execute(transaction)
    ) {
      for (int i = 0; i < 326; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testLte(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          AND(
            LTE(c.creditLimit, c.customerNumber),
            LTE(c.longitude, c.phone),
            LTE(45, c.phone),
            LTE(c.creditLimit, 329939933L)),
          SELECT(AND(
            LTE(c.creditLimit, c.customerNumber),
            LTE(c.longitude, c.phone),
            LTE(45, c.phone),
            LTE(c.creditLimit, 329939933L)))
            .FROM(c)
            .WHERE(AND(
              LTE(c.creditLimit, c.customerNumber),
              LTE(c.longitude, c.phone),
              LTE(45, c.phone),
              LTE(c.creditLimit, 329939933L)))
            .LIMIT(1))
          .FROM(c)
          .WHERE(AND(LTE(c.creditLimit, c.customerNumber),
            LTE(c.longitude, c.phone),
            LTE(45, c.phone),
            LTE(c.creditLimit, 329939933L)))
          .execute(transaction)
    ) {
      for (int i = 0; i < 24; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testEq(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          AND(
            EQ(p.status, p.status),
            EQ(p.comments, p.comments)),
          SELECT(AND(
            EQ(p.status, p.status),
            EQ(p.comments, p.comments)))
            .FROM(p)
            .WHERE(AND(
              EQ(p.status, p.status),
              EQ(p.comments, p.comments)))
            .LIMIT(1))
          .FROM(p)
          .WHERE(AND(EQ(p.status, p.status),
            EQ(p.comments, p.comments)))
          .execute(transaction)
    ) {
      for (int i = 0; i < 80; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testNe(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          NE(p.purchaseDate, p.shippedDate),
          SELECT(NE(p.purchaseDate, p.shippedDate))
            .FROM(p)
            .WHERE(NE(p.purchaseDate, p.shippedDate))
            .LIMIT(1))
          .FROM(p)
          .WHERE(NE(p.purchaseDate, p.shippedDate))
          .execute(transaction)
    ) {
      for (int i = 0; i < 312; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testGt(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          GT(p.purchaseNumber, 100),
          SELECT(GT(p.purchaseNumber, 100))
            .FROM(p)
            .WHERE(GT(p.purchaseNumber, 100))
            .LIMIT(1))
          .FROM(p)
          .WHERE(GT(p.purchaseNumber, 100))
          .execute(transaction)
    ) {
      for (int i = 0; i < 326; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testGte(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.PurchaseDetail p = classicmodels.PurchaseDetail$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          GTE(p.priceEach, p.quantity),
          SELECT(GTE(p.priceEach, p.quantity))
            .FROM(p)
            .WHERE(GTE(p.priceEach, p.quantity))
            .LIMIT(1))
          .FROM(p)
          .WHERE(GTE(p.priceEach, p.quantity))
          .execute(transaction)
    ) {
      for (int i = 0; i < 2875; ++i) { // [N]
        assertTrue(String.valueOf(i), rows.nextRow());
        assertTrue(String.valueOf(i), rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }
}