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
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class NullPredicateTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends NullPredicateTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends NullPredicateTest {
  }

  @Test
  public void testIs() throws IOException, SQLException {
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        IS.NULL(c.locality),
        SELECT(IS.NULL(c.locality)).
        FROM(c).
        WHERE(IS.NULL(c.locality)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NULL(c.locality))
        .execute()) {
      for (int i = 0; i < 71; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsPrimitive());
        assertTrue(rows.nextEntity().getAsPrimitive());
      }
    }
  }

  @Test
  public void testIsNot() throws IOException, SQLException {
    final classicmodels.Customer c = new classicmodels.Customer();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        IS.NOT.NULL(c.locality),
        SELECT(IS.NOT.NULL(c.locality)).
        FROM(c).
        WHERE(IS.NOT.NULL(c.locality)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NOT.NULL(c.locality))
        .execute()) {
      for (int i = 0; i < 51; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsPrimitive());
        assertTrue(rows.nextEntity().getAsPrimitive());
      }
    }
  }
}