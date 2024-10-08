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
import java.time.LocalDate;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Classicmodels;
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
public abstract class DeleteTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends DeleteTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends DeleteTest {
  }

  @Test
  public void testDeleteEntity(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.new Purchase();
    p.purchaseNumber.set(10102);
    p.customerNumber.set((short)181);

    assertEquals(1,
      DELETE(p)
        .execute(transaction)
        .getCount());
  }

  @Test
  public void testDeleteEntities(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p1 = classicmodels.new Purchase();
    p1.purchaseNumber.set(10102);
    p1.customerNumber.set((short)181);

    final Classicmodels.Purchase p2 = classicmodels.new Purchase();
    p2.purchaseNumber.set(10100);
    p2.customerNumber.set((short)363);

    final Classicmodels.Payment pa = classicmodels.new Payment();
    pa.customerNumber.set((short)103);

    // TODO: Implement batching mechanism to allow multiple jsql commands to execute in one batch
    final boolean isOracle = transaction.getVendor() == DbVendor.ORACLE;
    final Batch batch = new Batch();
    batch.addStatement(
      DELETE(p1)
        .onExecute((final int c) -> assertTrue(isOracle || c != 0)));
    batch.addStatement(
      DELETE(pa)
        .onExecute((final int c) -> assertTrue(isOracle || c != 0)));
    batch.addStatement(
      DELETE(p2)
        .onExecute((final int c) -> assertTrue(isOracle || c != 0)));

    if (!isOracle)
      assertEquals(5, batch.execute(transaction).getCount());
  }

  @Test
  public void testDeleteWhere(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase$;

    assertEquals(1,
      DELETE(p)
        .WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-09")))
        .execute(transaction)
        .getCount());
  }

  @Test
  public void testDeleteAll(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.PurchaseDetail p = classicmodels.PurchaseDetail$;

    assertTrue(2985 <
        DELETE(p)
          .execute(transaction)
          .getCount());
  }
}