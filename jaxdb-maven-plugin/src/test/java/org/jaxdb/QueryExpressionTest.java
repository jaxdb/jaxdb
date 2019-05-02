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

package org.jaxdb;

import static org.junit.Assert.*;
import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.VendorSchemaRunner;

public abstract class QueryExpressionTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends QueryExpressionTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends QueryExpressionTest {
  }

  @Test
  public void testFrom() throws IOException, SQLException {
    final classicmodels.Office o = new classicmodels.Office();
    try (final RowIterator<classicmodels.Office> rows =
      SELECT(o).
      FROM(o).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals("100 Market Street", rows.nextEntity().address1.get());
      assertTrue(rows.nextRow() && rows.nextRow() && rows.nextRow() && rows.nextRow() && rows.nextRow() && rows.nextRow());
      assertEquals("25 Old Broad Street", rows.nextEntity().address1.get());
      assertTrue(!rows.nextRow());
    }
  }

  @Test
  public void testFromMultiple() throws IOException, SQLException {
    final classicmodels.Office o = new classicmodels.Office();
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<classicmodels.Address> rows =
      SELECT(o, c).
      FROM(o, c).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals("100 Market Street", rows.nextEntity().address1.get());
      assertEquals("54, rue Royale", rows.nextEntity().address1.get());
    }
  }

  @Test
  public void testWhere() throws IOException, SQLException {
    final classicmodels.Office o = new classicmodels.Office();
    try (final RowIterator<? extends type.DataType<?>> rows =
      SELECT(o.address1, o.latitude).
      FROM(o).
      WHERE(AND(
        EQ(o.phone, 81332245000l),
        OR(GT(o.latitude, 20d),
          LT(o.longitude, 100d)))).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals("4-1 Kioicho", rows.nextEntity().get());
      assertEquals(35.6811759, ((BigDecimal)rows.nextEntity().get()).doubleValue(), 0.0000000001);
    }
  }
}