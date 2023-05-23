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

import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.Classicmodels.Country;
import org.jaxdb.jsql.DML.SUM;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class SetFunctionTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends SetFunctionTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends SetFunctionTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testSetFunctions(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (final RowIterator<? extends data.Column<?>> rows =

      SELECT(
        AVG(c.phone),
        MAX(c.city),
        MIN(c.country),
        SUM.DISTINCT(c.salesEmployeeNumber)).
      FROM(c)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(24367857008L, rows.nextEntity().get());
      assertEquals("White Plains", rows.nextEntity().get());
      assertEquals(Country.AU, rows.nextEntity().get());
      assertEquals(21003, rows.nextEntity().get());
      assertFalse(rows.nextRow());
    }
  }
}