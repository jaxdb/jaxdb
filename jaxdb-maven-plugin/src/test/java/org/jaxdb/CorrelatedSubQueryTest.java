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

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
public abstract class CorrelatedSubQueryTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends CorrelatedSubQueryTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends CorrelatedSubQueryTest {
  }

  @Test
  public void testWhereEntity() throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    final classicmodels.Customer c1 = classicmodels.Customer(1);
    final classicmodels.Customer c2 = classicmodels.Customer(2);
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
  public void testWhereColumn() throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    final classicmodels.Customer c1 = classicmodels.Customer(1);
    final classicmodels.Customer c2 = classicmodels.Customer(2);
    try (final RowIterator<? extends type.Subject<?>> rows =
      SELECT(p, c2.companyName).
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
      assertTrue(rows.nextEntity() instanceof type.CHAR);
    }
  }

  @Test
  public void testSelect() throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    final classicmodels.Customer c = classicmodels.Customer();
    try (final RowIterator<? extends type.Subject<?>> rows =
      SELECT(p,
        SELECT(MAX(c.salesEmployeeNumber)).
        FROM(c).
        WHERE(GT(c.creditLimit, 10))).
      FROM(p).
      WHERE(
        LT(p.purchaseDate, p.requiredDate))
          .execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity() instanceof classicmodels.Purchase);
      assertNotNull(((type.INT)rows.nextEntity()).get());
    }
  }

  @Test
  public void testJoin() throws IOException, SQLException {
    final classicmodels.Purchase p = classicmodels.Purchase();
    final classicmodels.Customer c = classicmodels.Customer();

    final type.BIGINT pd = type.BIGINT();
    final type.SMALLINT pn = type.SMALLINT();
    try (final RowIterator<? extends type.Subject<?>> rows =
      SELECT(c, pd).
      FROM(c).
      JOIN(
        SELECT(p.customerNumber.AS(pn), COUNT(p.purchaseDate).AS(pd)).
        FROM(p).
        GROUP_BY(p.customerNumber).
        HAVING(NE(p.customerNumber, 10))).
      ON(EQ(c.customerNumber, pn)).
      WHERE(NE(c.customerNumber, 10))
        .execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity() instanceof classicmodels.Customer);
      assertNotNull(((type.BIGINT)rows.nextEntity()).get());
    }
  }
}