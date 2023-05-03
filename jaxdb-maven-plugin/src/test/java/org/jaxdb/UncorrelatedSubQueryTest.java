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
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.types;
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
public abstract class UncorrelatedSubQueryTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends UncorrelatedSubQueryTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends UncorrelatedSubQueryTest {
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testAdd(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<? extends data.Numeric<?>> rows =

      SELECT(
        ADD(t.tinyintType, SELECT(MIN(t.tinyintType)).FROM(t)),
        SUB(t.smallintType, SELECT(MIN(t.tinyintType)).FROM(t)),
        ADD(t.intType, SELECT(MIN(t.smallintType)).FROM(t)),
        SUB(t.floatType, SELECT(MAX(t.bigintType)).FROM(t)),
        ADD(t.doubleType, SELECT(MIN(t.decimalType)).FROM(t)),
        SUB(t.decimalType, SELECT(AVG(t.intType)).FROM(t)),
        ADD(t.bigintType, SELECT(MAX(t.floatType)).FROM(t))
      ).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  // FIXME: Add more tests...
}