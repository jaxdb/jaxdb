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
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class UpdateTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({types.class, classicmodels.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends UpdateTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({types.class, classicmodels.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends UpdateTest {
  }

  @Test
  public void testUpdateEntity() throws IOException, SQLException {
    classicmodels.Product p = new classicmodels.Product();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<classicmodels.Product> rows =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      p = rows.nextEntity();

      p.price.set(BigDecimal.valueOf(20L));

      final int results = UPDATE(p).execute(transaction);
      assertEquals(1, results);
    }
  }

  @Test
  public void testUpdateEntities() throws IOException, SQLException {
    classicmodels.Product p = new classicmodels.Product();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<classicmodels.Product> rows1 =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        execute(transaction);
    ) {
      assertTrue(rows1.nextRow());
      p = rows1.nextEntity();

      classicmodels.ProductLine pl = new classicmodels.ProductLine();
      final RowIterator<classicmodels.ProductLine> rows2 =
        SELECT(pl).
        FROM(pl).
        LIMIT(1).
        execute(transaction);

      assertTrue(rows2.nextRow());
      pl = rows2.nextEntity();

      p.quantityInStock.set(300);
      pl.description.set(new StringReader("New description"));

      final Batch batch = new Batch();
      batch.addStatement(UPDATE(p));
      batch.addStatement(UPDATE(pl));

      final int[] result = batch.execute(transaction);
      assertTrue(result[0] == 1 || result[0] == Statement.SUCCESS_NO_INFO);
      assertTrue(result[1] == 1 || result[1] == Statement.SUCCESS_NO_INFO);
    }
  }

  @Test
  public void testUpdateSetWhere() throws IOException, SQLException {
    types.Type t = new types.Type();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.enumType, types.Type.EnumType.FOUR).
        WHERE(EQ(t.enumType, types.Type.EnumType.ONE)).
        execute(transaction);

      assertTrue(results > 0);
    }
  }

  @Test
  public void testUpdateSet() throws IOException, SQLException {
    types.Type t = new types.Type();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.datetimeType, LocalDateTime.now()).
        execute(transaction);

      assertTrue(results > 300);
    }
  }
}