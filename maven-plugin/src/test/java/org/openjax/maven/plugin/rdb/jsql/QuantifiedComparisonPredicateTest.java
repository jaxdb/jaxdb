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
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(classicmodels.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class QuantifiedComparisonPredicateTest {
  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testAll() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.INT> rows =
      SELECT(COUNT()).
      FROM(c).
      WHERE(
        LT(c.creditLimit,
          ALL(SELECT(COUNT()).
            FROM(p).
            WHERE(NE(p.purchaseDate, p.shippedDate))))).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Integer.valueOf(24), rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testAny() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.INT> rows =
      SELECT(COUNT()).
      FROM(c).
      WHERE(
        GT(c.customerNumber,
          ANY(SELECT(COUNT()).
            FROM(p).
            WHERE(GT(p.purchaseDate, p.shippedDate))))).
      execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().get() > 100);
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testSome() throws IOException, SQLException {
    final classicmodels.Purchase p = new classicmodels.Purchase();
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.INT> rows =
      SELECT(COUNT()).
      FROM(c).
      WHERE(
        GT(c.customerNumber,
          SOME(SELECT(COUNT()).
            FROM(p).
            WHERE(LT(p.purchaseDate, p.shippedDate))))).
      execute()) {
      assertTrue(rows.nextRow());
      assertTrue(rows.nextEntity().get() > 50);
    }
  }
}