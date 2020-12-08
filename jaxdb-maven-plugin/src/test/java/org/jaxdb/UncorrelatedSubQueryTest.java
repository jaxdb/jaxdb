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
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.jsql.world;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class UncorrelatedSubQueryTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class, world.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends UncorrelatedSubQueryTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class, world.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends UncorrelatedSubQueryTest {
  }

  @Test
  public void testAdd() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (
      final Transaction transaction = new TestTransaction(types.class);
      final RowIterator<? extends type.Numeric<?>> rows =
        SELECT(
          ADD(t.tinyintType, SELECT(MIN(t.bigintType)).FROM(t)),
          SUB(t.smallintType, SELECT(AVG(t.intType)).FROM(t)),
          ADD(t.intType, SELECT(COUNT(t.smallintType)).FROM(t)),
          SUB(t.floatType, SELECT(MAX(t.tinyintType)).FROM(t)),
          ADD(t.doubleType, SELECT(MIN(t.decimalType)).FROM(t)),
          SUB(t.decimalType, SELECT(AVG(t.intType)).FROM(t)),
          ADD(t.bigintType, SELECT(MAX(t.floatType)).FROM(t))
        ).
        FROM(t)
          .execute(transaction);
    ) {
      assertTrue(rows.nextRow());
    }
  }

  // FIXME: Add more tests...
}