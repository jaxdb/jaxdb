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

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class BetweenPredicateTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends BetweenPredicateTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends BetweenPredicateTest {
  }

  @Test
  public void testBetween1() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
      FROM(p).
      WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
      execute()) {
      Assert.assertTrue(rows.nextRow());
      Assert.assertEquals(Boolean.TRUE, rows.nextEntity().get());
    }
  }

  @Test
  public void testBetween1Wrapped() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
        FROM(p).
        WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate))).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Boolean.TRUE, rows.nextEntity().get());
    }
  }

  @Test
  public void testBetween2() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(BETWEEN(p.msrp, p.price, 100)).
      FROM(p).
      WHERE(BETWEEN(p.msrp, p.price, 100)).
      execute()) {
      for (int i = 0; i < 59; ++i) {
        assertTrue(rows.nextRow());
        assertEquals(Boolean.TRUE, rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testBetween3() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(BETWEEN(p.scale, "a", "b")).
      FROM(p).
      WHERE(BETWEEN(p.scale, "a", "b")).
      execute()) {
      assertFalse(rows.nextRow());
    }
  }

  @Test
  public void testBetween4() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(BETWEEN(p.quantityInStock, 500, 1000)).
      FROM(p).
      WHERE(BETWEEN(p.quantityInStock, 500, 1000)).
      execute()) {
      for (int i = 0; i < 7; ++i) {
        assertTrue(rows.nextRow());
        assertEquals(Boolean.TRUE, rows.nextEntity().get());
      }
    }
  }
}