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

package org.openjax.rdb;

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.runner.VendorSchemaRunner;

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
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(3).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Double.valueOf(33.19), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(Double.valueOf(22.57), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(Double.valueOf(35.36), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(Double.valueOf(15.91), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(Double.valueOf(37.76), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(Double.valueOf(16.24), rows.nextEntity().get().doubleValue(), 0.0000000001);
    }
  }

  @Test
  public void testLimitOffset() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(p.msrp, p.price).
      FROM(p).
      ORDER_BY(p.msrp, p.price).
      LIMIT(2).
      OFFSET(1).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Double.valueOf(35.36), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(Double.valueOf(15.91), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertTrue(rows.nextRow());
      assertEquals(Double.valueOf(37.76), rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(Double.valueOf(16.24), rows.nextEntity().get().doubleValue(), 0.0000000001);
    }
  }
}