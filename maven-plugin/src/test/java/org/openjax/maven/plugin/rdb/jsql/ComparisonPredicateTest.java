/* Copyright (c) 2017 OpenJAX
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

package org.openjax.maven.plugin.rdb.jsql;

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.fastjax.test.MixedTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class ComparisonPredicateTest {
  @Test
  public void testLt() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        OR(LT(p.customerNumber, 100), LT(50, p.customerNumber), LT(p.comments, p.status)),
        SELECT(OR(LT(p.customerNumber, 100), LT(50, p.customerNumber), LT(p.comments, p.status))).
          FROM(p).
          WHERE(OR(LT(p.customerNumber, 100), LT(50, p.customerNumber), LT(p.comments, p.status))).
          LIMIT(1)).
      FROM(p).
      WHERE(OR(LT(p.customerNumber, 100), LT(50, p.customerNumber), LT(p.comments, p.status))).
      execute()) {
      for (int i = 0; i < 323; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testLte() throws IOException, SQLException {
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        AND(LTE(c.creditLimit, c.customerNumber), LTE(c.longitude, c.phone), LTE(45, c.phone), LTE(c.creditLimit, 329939933l)),
        SELECT(AND(LTE(c.creditLimit, c.customerNumber), LTE(c.longitude, c.phone), LTE(45, c.phone), LTE(c.creditLimit, 329939933l))).
          FROM(c).
          WHERE(AND(LTE(c.creditLimit, c.customerNumber), LTE(c.longitude, c.phone), LTE(45, c.phone), LTE(c.creditLimit, 329939933l))).
          LIMIT(1)).
      FROM(c).
      WHERE(AND(LTE(c.creditLimit, c.customerNumber), LTE(c.longitude, c.phone), LTE(45, c.phone), LTE(c.creditLimit, 329939933l))).
      execute()) {
      assertTrue(rows.nextRow());
      for (int i = 0; i < 23; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testEq() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        AND(EQ(p.status, p.status), EQ(p.comments, p.comments)),
        SELECT(AND(EQ(p.status, p.status), EQ(p.comments, p.comments))).
          FROM(p).
          WHERE(AND(EQ(p.status, p.status), EQ(p.comments, p.comments))).
          LIMIT(1)).
      FROM(p).
      WHERE(AND(EQ(p.status, p.status), EQ(p.comments, p.comments))).
      execute()) {
      for (int i = 0; i < 79; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testNe() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        NE(p.purchaseDate, p.shippedDate),
        SELECT(NE(p.purchaseDate, p.shippedDate)).
          FROM(p).
          WHERE(NE(p.purchaseDate, p.shippedDate)).
          LIMIT(1)).
      FROM(p).
      WHERE(NE(p.purchaseDate, p.shippedDate)).
      execute()) {
      for (int i = 0; i < 309; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testGt() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        GT(p.purchaseNumber, UNSIGNED(100)),
        SELECT(GT(p.purchaseNumber, UNSIGNED(100))).
          FROM(p).
          WHERE(GT(p.purchaseNumber, UNSIGNED(100))).
          LIMIT(1)).
      FROM(p).
      WHERE(GT(p.purchaseNumber, UNSIGNED(100))).
      execute()) {
      for (int i = 0; i < 323; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testGte() throws IOException, SQLException {
    final classicmodels.PurchaseDetail p = new classicmodels.PurchaseDetail();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        GTE(p.priceEach, p.quantity),
        SELECT(GTE(p.priceEach, p.quantity)).
          FROM(p).
          WHERE(GTE(p.priceEach, p.quantity)).
          LIMIT(1)).
      FROM(p).
      WHERE(GTE(p.priceEach, p.quantity)).
      execute()) {
      for (int i = 0; i < 2875; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
      }
    }
  }
}