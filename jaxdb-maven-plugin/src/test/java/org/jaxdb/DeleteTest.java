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
import java.time.LocalDate;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.runner.VendorSchemaRunner.Schema;
import org.jaxdb.vendor.DBVendor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
public abstract class DeleteTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends DeleteTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends DeleteTest {
  }

  @Test
  public void testDeleteEntity(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    p.purchaseNumber.set(10102);
    p.customerNumber.set((short)181);

    assertEquals(1,
      DELETE(p)
        .execute(transaction));
  }

  @Test
  public void testDeleteEntities(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    p.purchaseNumber.set(10100);
    p.customerNumber.set((short)363);

    final classicmodels.Payment pa = new classicmodels.Payment();
    pa.customerNumber.set((short)103);

    // TODO: Implement batching mechanism to allow multiple jsql commands to execute in one batch
    final boolean isOracle = transaction.getVendor() == DBVendor.ORACLE;
    final Batch batch = new Batch();
    batch.addStatement(DELETE(p),
      (e, c) -> assertTrue(isOracle || 0 != c));
    batch.addStatement(DELETE(pa),
      (e, c) -> assertTrue(isOracle || 0 != c));

    assertTrue(isOracle || 4 == batch.execute(transaction));
  }

  @Test
  public void testDeleteWhere(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();

    assertEquals(1,
      DELETE(p).
        WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-09")))
          .execute(transaction));
  }

  @Test
  public void testDeleteAll(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.PurchaseDetail p = classicmodels.PurchaseDetail();

    assertTrue(2985 <
      DELETE(p)
        .execute(transaction));
  }
}