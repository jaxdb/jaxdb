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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;

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
      WHERE(IS.NULL(c.locality)).
      execute()) {
      for (int i = 0; i < 71; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
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
      WHERE(IS.NOT.NULL(c.locality)).
      execute()) {
      for (int i = 0; i < 51; i++) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
      }
    }
  }
}