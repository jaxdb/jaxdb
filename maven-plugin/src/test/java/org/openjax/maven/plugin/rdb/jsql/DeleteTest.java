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
import java.sql.Statement;
import java.time.LocalDate;

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
import org.openjax.rdb.jsql.Batch;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.classicmodels;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class DeleteTest {
  @Test
  public void testDeleteEntity() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(classicmodels.class)) {
      final classicmodels.Purchase p = new classicmodels.Purchase();
      p.purchaseNumber.set(10102l);
      p.customerNumber.set(181);

      final int results = DELETE(p).execute(transaction);
      assertEquals(1, results);

      transaction.rollback();
    }
  }

  @Test
  public void testDeleteEntities() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(classicmodels.class)) {
      final classicmodels.Purchase p = new classicmodels.Purchase();
      p.purchaseNumber.set(10100l);
      p.customerNumber.set(363);

      final classicmodels.Payment pa = new classicmodels.Payment();
      pa.customerNumber.set(103);

      // TODO: Implement batching mechanism to allow multiple jsql commands to execute in one batch

      final Batch batch = new Batch();
      batch.addStatement(DELETE(p));
      batch.addStatement(DELETE(pa));
      final int[] result = batch.execute(transaction);

      assertTrue(result[0] == 1 || result[0] == Statement.SUCCESS_NO_INFO);
      assertTrue(result[1] == 3 || result[1] == Statement.SUCCESS_NO_INFO);

      transaction.rollback();
    }
  }

  @Test
  public void testDeleteWhere() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(classicmodels.class)) {
      final classicmodels.Purchase p = new classicmodels.Purchase();

      final int results = DELETE(p).WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-09"))).execute(transaction);
      assertEquals(1, results);

      transaction.rollback();
    }
  }

  @Test
  public void testDeleteAll() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(classicmodels.class)) {
      final classicmodels.PurchaseDetail p = new classicmodels.PurchaseDetail();

      final int results = DELETE(p).execute(transaction);
      assertTrue(results > 2985);

      transaction.rollback();
    }
  }
}