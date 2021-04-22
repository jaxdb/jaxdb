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

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.vendor.DBVendor;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
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
  public void testDeleteEntity() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(classicmodels.class)) {
      final classicmodels.Purchase p = new classicmodels.Purchase();
      p.purchaseNumber.set(10102);
      p.customerNumber.set((short)181);

      final int counts =
        DELETE(p)
          .execute(transaction);

      assertEquals(1, counts);
    }
  }

  @Test
  public void testDeleteEntities() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(classicmodels.class)) {
      final classicmodels.Purchase p = new classicmodels.Purchase();
      p.purchaseNumber.set(10100);
      p.customerNumber.set((short)363);

      final classicmodels.Payment pa = new classicmodels.Payment();
      pa.customerNumber.set((short)103);

      // TODO: Implement batching mechanism to allow multiple jsql commands to execute in one batch
      final boolean isOracle = DBVendor.valueOf(transaction.getConnection().getMetaData()) == DBVendor.ORACLE;
      final Batch batch = new Batch();
      batch.addStatement(DELETE(p), (e, c) -> assertTrue(isOracle || 0 != c));
      batch.addStatement(DELETE(pa), (e, c) -> assertTrue(isOracle || 0 != c));

      assertTrue(isOracle || 4 == batch.execute(transaction));
    }
  }

  @Test
  public void testDeleteWhere() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(classicmodels.class)) {
      final classicmodels.Purchase p = classicmodels.Purchase();

      final int counts =
        DELETE(p).
        WHERE(EQ(p.purchaseDate, LocalDate.parse("2003-01-09")))
          .execute(transaction);

      assertEquals(1, counts);
    }
  }

  @Test
  public void testDeleteAll() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(classicmodels.class)) {
      final classicmodels.PurchaseDetail p = classicmodels.PurchaseDetail();

      final int counts =
        DELETE(p)
          .execute(transaction);

      assertTrue(counts > 2985);
    }
  }
}