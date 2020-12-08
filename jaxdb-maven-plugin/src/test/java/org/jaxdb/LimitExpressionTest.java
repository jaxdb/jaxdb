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

public abstract class LimitExpressionTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends LimitExpressionTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends LimitExpressionTest {
  }

  @Test
  public void testLimit() throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(3)
        .execute()) {

      assertTrue(rows.nextRow());
      assertEquals(33.19, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(22.57, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(35.36, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(15.91, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(37.76, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(16.24, rows.nextEntity().get().doubleValue(), 0.0000000001);
    }
  }

  @Test
  public void testLimitOffset() throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(2).
      OFFSET(1)
        .execute()) {

      assertTrue(rows.nextRow());
      assertEquals(35.36, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(15.91, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(37.76, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(16.24, rows.nextEntity().get().doubleValue(), 0.0000000001);
    }
  }
}