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
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
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
public abstract class UnionExpressionTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends UnionExpressionTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends UnionExpressionTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testUnion(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Purchase p = classicmodels.Purchase();
    final Classicmodels.Customer c = classicmodels.Customer();

    try (final RowIterator<?> rows =
      SELECT(p, c).
      FROM(p).
      LEFT_JOIN(c).ON(EQ(p.customerNumber, c.customerNumber)).
      UNION(
        SELECT(p, c).
        FROM(p).
        LEFT_JOIN(c).ON(EQ(p.customerNumber, c.customerNumber)))
      .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }
}