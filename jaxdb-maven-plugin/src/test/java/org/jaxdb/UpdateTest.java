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

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema({types.class, classicmodels.class})
public abstract class UpdateTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends UpdateTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends UpdateTest {
  }

  @Test
  public void testUpdateEntity() throws IOException, SQLException {
    classicmodels.Product p = classicmodels.Product();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<classicmodels.Product> rows =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        FOR_UPDATE()
          .execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      p = rows.nextEntity();

      p.price.set(new BigDecimal(20));

      final int results = UPDATE(p).execute(transaction);
      assertEquals(1, results);
    }
  }

  @Test
  public void testUpdateEntities() throws IOException, SQLException {
    classicmodels.Product p = classicmodels.Product();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<classicmodels.Product> rows1 =
        SELECT(p).
        FROM(p).
        LIMIT(1).
        FOR_SHARE()
          .execute(transaction);
    ) {
      assertTrue(rows1.nextRow());
      p = rows1.nextEntity();

      classicmodels.ProductLine pl = classicmodels.ProductLine();
      final RowIterator<classicmodels.ProductLine> rows2 =
        SELECT(pl).
        FROM(pl).
        LIMIT(1).
        FOR_UPDATE(pl)
          .execute(transaction);

      assertTrue(rows2.nextRow());
      pl = rows2.nextEntity();

      p.quantityInStock.set(300);
      pl.description.set(new StringReader("New description"));

      final Batch batch = new Batch();
      batch.addStatement(UPDATE(p), (e,c) -> assertEquals(1, c));
      batch.addStatement(UPDATE(pl), (e,c) -> assertEquals(1, c));

      assertEquals(2, batch.execute(transaction));
    }
  }

  @Test
  public void testUpdateSetWhere() throws IOException, SQLException {
    types.Type t = types.Type();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        FOR_SHARE(t).
        SKIP_LOCKED().
        NOWAIT()
          .execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.enumType, types.Type.EnumType.FOUR).
        WHERE(EQ(t.enumType, types.Type.EnumType.ONE))
          .execute(transaction);

      assertTrue(results > 0);
    }
  }

  @Test
  public void testUpdateSet() throws IOException, SQLException {
    types.Type t = types.Type();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<types.Type> rows =
        SELECT(t).
        FROM(t).
        LIMIT(1).
        FOR_UPDATE(t).
        NOWAIT().
        SKIP_LOCKED()
          .execute(transaction);
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      final int results =
        UPDATE(t).
        SET(t.datetimeType, LocalDateTime.now())
          .execute(transaction);

      assertTrue(results > 300);
    }
  }
}