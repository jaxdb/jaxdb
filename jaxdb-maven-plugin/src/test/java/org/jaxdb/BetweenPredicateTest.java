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

import org.jaxdb.jsql.DML.NOT;
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
public abstract class BetweenPredicateTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends BetweenPredicateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends BetweenPredicateTest {
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=true)
  public void testBetweenPrimary1(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Payment p = classicmodels.Payment();
    try (final RowIterator<classicmodels.Payment> rows =

      SELECT(p).
      FROM(p).
      WHERE(BETWEEN(p.customerNumber, 103, 103))
        .execute(transaction)) {

      for (int i = 0; i < 3; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertEquals(103, rows.nextEntity().customerNumber.getAsShort());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=true)
  public void testBetweenPrimary2(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Payment p = new classicmodels.Payment();
    try (final RowIterator<classicmodels.Payment> rows =

      SELECT(p).
      FROM(p).
      WHERE(AND(BETWEEN(p.customerNumber, 103, 103),
        EQ(p.checkNumber, "JM555205")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      rows.nextEntity();
      assertEquals(103, p.customerNumber.getAsShort());
      assertEquals("JM555205", p.checkNumber.get());
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=false, cacheableRowIteratorFullConsume=false)
  public void testBetween1(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
      FROM(p).
      WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsBoolean());
      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testBetween1Wrapped(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(
        SELECT(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)).
        FROM(p).
        WHERE(NOT.BETWEEN(p.shippedDate, p.purchaseDate, p.requiredDate)))
          .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().getAsBoolean());
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=false, cacheableRowIteratorFullConsume=false)
  public void testBetween2(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(BETWEEN(p.msrp, p.price, 100)).
      FROM(p).
      WHERE(BETWEEN(p.msrp, p.price, 100))
        .execute(transaction)) {

      for (int i = 0; i < 59; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=false, cacheableRowIteratorFullConsume=false)
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
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=false, cacheableRowIteratorFullConsume=false)
  public void testBetween4(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(BETWEEN(p.quantityInStock, 500, 1000)).
      FROM(p).
      WHERE(BETWEEN(p.quantityInStock, 500, 1000))
        .execute(transaction)) {

      for (int i = 0; i < 7; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }
}