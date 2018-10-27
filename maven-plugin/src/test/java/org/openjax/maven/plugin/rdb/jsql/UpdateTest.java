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
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.Batch;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.types;

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
    try (final Transaction transaction = new Transaction(types.class)) {
      classicmodels.Product p = new classicmodels.Product();
      final RowIterator<classicmodels.Product> rows =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        execute(transaction);

      assertTrue(rows.nextRow());
      p = rows.nextEntity();

      p.price.set(BigDecimal.valueOf(20l));

      final int results = UPDATE(p).execute(transaction);
      assertEquals(1, results);

      transaction.rollback();
    }
  }

  @Test
  public void testUpdateEntities() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      classicmodels.Product p = new classicmodels.Product();
      final RowIterator<classicmodels.Product> rows1 =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        execute(transaction);

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

      transaction.rollback();
    }
  }

  @Test
  public void testUpdateSetWhere() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      types.Type t = new types.Type();
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        execute(transaction);

      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.enumType, types.Type.EnumType.FOUR).
        WHERE(EQ(t.enumType, types.Type.EnumType.ONE)).
        execute(transaction);

      assertTrue(results > 0);

      transaction.rollback();
    }
  }

  @Test
  public void testUpdateSet() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      types.Type t = new types.Type();
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        execute(transaction);

      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.datetimeType, LocalDateTime.now()).
        execute(transaction);

      assertTrue(results > 300);

      transaction.rollback();
    }
  }
}