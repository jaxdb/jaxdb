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

import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.TestDML.IS;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class HavingClauseTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends HavingClauseTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends HavingClauseTest {
  }

  @Test
  @AssertSelect(entityOnlySelect=false, absolutePrimaryKeyCondition=true, rowIteratorFullConsume=false)
  public void testPrimary(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BIGINT> rows =

      SELECT(COUNT(p)).
      FROM(p).
      HAVING(IS.NOT.NULL(p.code))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(1, rows.nextEntity().getAsLong());
    }
  }

  @Test
  @AssertSelect(entityOnlySelect=false, absolutePrimaryKeyCondition=false, rowIteratorFullConsume=false)
  public void testNotPrimary(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BIGINT> rows =

      SELECT(COUNT(p)).
      FROM(p).
      HAVING(OR(IS.NOT.NULL(p.msrp), IS.NOT.NULL(p.code)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(1, rows.nextEntity().getAsLong());
    }
  }

  @Test
  @AssertSelect(entityOnlySelect=false, absolutePrimaryKeyCondition=false, rowIteratorFullConsume=false)
  public void test(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    final data.DECIMAL d = p.msrp.clone();
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        SIN(p.msrp).AS(d),
        SELECT(SIN(p.msrp).AS(d)).
        FROM(p).
        WHERE(GT(p.price, 10)).
        GROUP_BY(p).
        HAVING(LT(d, 10)).
        ORDER_BY(DESC(d)).
        LIMIT(1)).
      FROM(p).
      WHERE(GT(p.price, 10)).
      GROUP_BY(p).
      HAVING(LT(d, 10)).
      ORDER_BY(DESC(d))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(0.9995201585807313, rows.nextEntity().get().doubleValue(), 0.0000000001);
      assertEquals(0.9995201585807313, rows.nextEntity().get().doubleValue(), 0.0000000001);
    }
  }
}