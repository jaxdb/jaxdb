/* Copyright (c) 2022 JAX-DB
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
import java.time.LocalDate;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.vendor.DbVendor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class BatchTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends BatchTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends BatchTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void test(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;

    final boolean isOracle = transaction.getVendor() == DbVendor.ORACLE;
    final Batch batch = new Batch();
    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-09")))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 1)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-06")))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 1)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.purchaseDate, LocalDate.parse("2004-11-17")))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 2)));

    batch.addStatement(
      UPDATE(p).SET(p.status, Classicmodels.Purchase.Status.ON_HOLD)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.DISPUTED))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 3)));

    batch.addStatement(
      UPDATE(p).SET(p.status, Classicmodels.Purchase.Status.ON_HOLD)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.DISPUTED))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 0)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.customerNumber, 112))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 3)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.customerNumber, 114))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 5)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.CANCELED))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 6)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.RESOLVED))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 4)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.IN_PROCESS))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 6)));

    final Classicmodels.Purchase p1 = classicmodels.new Purchase();
    p1.purchaseDate.set(LocalDate.now());
    p1.requiredDate.set(LocalDate.now());
    p1.customerNumber.set((short)114);
    p1.purchaseNumber.set(SELECT(ADD(MAX(p.purchaseNumber), 1)).FROM(p));
    p1.status.set(Classicmodels.Purchase.Status.IN_PROCESS);

    for (int i = 0; i < 5; ++i) { // [N]
      batch.addStatement(
        INSERT(p1)
          .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 1)));
    }

    batch.addStatement(
      UPDATE(p).SET(p.status, Classicmodels.Purchase.Status.CANCELED)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.IN_PROCESS))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 5)));

    batch.addStatement(
      DELETE(p)
        .WHERE(EQ(p.status, Classicmodels.Purchase.Status.CANCELED))
        .onExecute((final int c) -> assertTrue("" + c, isOracle || c == 5)));

    if (!isOracle)
      assertEquals(46, batch.execute(transaction).getCount());
  }
}