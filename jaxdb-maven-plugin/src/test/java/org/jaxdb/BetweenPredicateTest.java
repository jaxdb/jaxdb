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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.jaxdb.jsql.DML.NOT;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.runner.VendorSchemaRunner.Schema;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
public abstract class BetweenPredicateTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends BetweenPredicateTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends BetweenPredicateTest {
  }

  @Test
  public void testBetween1(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
      FROM(p).
      WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate))
        .execute(transaction)) {
      Assert.assertTrue(rows.nextRow());
      Assert.assertEquals(Boolean.TRUE, rows.nextEntity().getAsBoolean());
    }
  }

  @Test
  public void testBetween1Wrapped(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(
        SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
        FROM(p).
        WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)))
          .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertEquals(Boolean.TRUE, rows.nextEntity().getAsBoolean());
    }
  }

  @Test
  public void testBetween2(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(BETWEEN(p.msrp, p.price, 100)).
      FROM(p).
      WHERE(BETWEEN(p.msrp, p.price, 100))
        .execute(transaction)) {
      for (int i = 0; i < 59; ++i) {
        assertTrue(rows.nextRow());
        assertEquals(Boolean.TRUE, rows.nextEntity().getAsBoolean());
      }
    }
  }

  @Test
  public void testBetween3(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(BETWEEN(p.scale, "a", "b")).
      FROM(p).
      WHERE(BETWEEN(p.scale, "a", "b"))
        .execute(transaction)) {
      assertFalse(rows.nextRow());
    }
  }

  @Test
  public void testBetween4(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(BETWEEN(p.quantityInStock, 500, 1000)).
      FROM(p).
      WHERE(BETWEEN(p.quantityInStock, 500, 1000))
        .execute(transaction)) {
      for (int i = 0; i < 7; ++i) {
        assertTrue(rows.nextRow());
        assertEquals(Boolean.TRUE, rows.nextEntity().getAsBoolean());
      }
    }
  }
}