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

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class CorrelatedSubQueryTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends CorrelatedSubQueryTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends CorrelatedSubQueryTest {
  }

  @Test
  public void testWhere() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c1 = new classicmodels.Customer();
    final classicmodels.Customer c2 = new classicmodels.Customer();
    try (final RowIterator<? extends type.Subject<?>> rows =
      SELECT(p, c2).
      FROM(p,
        SELECT(c1).
        FROM(c1).
        WHERE(GT(c1.creditLimit, 10)).
        AS(c2)).
      WHERE(AND(
        LT(p.purchaseDate, p.requiredDate),
        EQ(p.customerNumber, c2.customerNumber)))
          .execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity() instanceof classicmodels.Purchase);
      assertTrue(rows.nextEntity() instanceof classicmodels.Customer);
    }
  }

  @Test
  public void testSelect() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c = new classicmodels.Customer();
    final type.INT.UNSIGNED s = c.salesEmployeeNumber.clone();
    try (final RowIterator<? extends type.Subject<?>> rows =
      SELECT(p,
        SELECT(MAX(c.salesEmployeeNumber)).
        FROM(c).
        WHERE(GT(c.creditLimit, 10)).
        AS(s)).
      FROM(p).
      WHERE(
        LT(p.purchaseDate, p.requiredDate))
          .execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity() instanceof classicmodels.Purchase);
    }
  }
}