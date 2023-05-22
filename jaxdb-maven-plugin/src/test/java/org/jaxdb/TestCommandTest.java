/* Copyright (c) 2023 JAX-DB
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

import org.jaxdb.jsql.QueryConfig;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Classicmodels;
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
public abstract class TestCommandTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends TestCommandTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends TestCommandTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testFailEntityOnlySelectTrue(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final QueryConfig queryConfig = TestCommand.Select.configure(transaction);

    try {
      final Classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p.code).
        FROM(p)
          .execute(transaction, queryConfig)) {
      }

      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFailEntityOnlySelectFalse(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final QueryConfig queryConfig = TestCommand.Select.configure(transaction);

    try {
      final Classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction, queryConfig)) {
      }

      fail("Expected AssertionError");
    }
    catch (final AssertionError e) {
      if ("Expected AssertionError".equals(e.getMessage()))
        throw e;
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=true)
  public void testRowIteratorFullConsumeRequiredTrue(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final QueryConfig queryConfig = TestCommand.Select.configure(transaction);

    try {
      final Classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction, queryConfig)) {

        rows.nextRow();
      }

      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testRowIteratorFullConsumeRequiredFalse(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final QueryConfig queryConfig = TestCommand.Select.configure(transaction);

    try {
      final Classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction, queryConfig)) {

        while (rows.nextRow());
      }

      fail("Expected AssertionError");
    }
    catch (final AssertionError e) {
      if ("Expected AssertionError".equals(e.getMessage()))
        throw e;
    }
  }
}