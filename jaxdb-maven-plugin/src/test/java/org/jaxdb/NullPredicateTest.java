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
import org.jaxdb.jsql.DML.IS;
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
public abstract class NullPredicateTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends NullPredicateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends NullPredicateTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testPrimaryIs(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(
        IS.NULL(c.customerNumber),
        SELECT(IS.NULL(c.customerNumber)).
        FROM(c).
        WHERE(IS.NULL(c.customerNumber)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NULL(c.customerNumber))
        .execute(transaction)) {

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testPrimaryIsNot(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(
        IS.NOT.NULL(c.customerNumber),
        SELECT(IS.NOT.NULL(c.customerNumber)).
        FROM(c).
        WHERE(IS.NOT.NULL(c.customerNumber)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NOT.NULL(c.customerNumber))
        .execute(transaction)) {

      for (int i = 0; i < 122; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testNotPrimaryIs(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(
        IS.NULL(c.locality),
        SELECT(IS.NULL(c.locality)).
        FROM(c).
        WHERE(IS.NULL(c.locality)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NULL(c.locality))
        .execute(transaction)) {

      for (int i = 0; i < 71; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testNotPrimaryIsNot(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Customer c = classicmodels.Customer$;
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(
        IS.NOT.NULL(c.locality),
        SELECT(IS.NOT.NULL(c.locality)).
        FROM(c).
        WHERE(IS.NOT.NULL(c.locality)).
        LIMIT(1)).
      FROM(c).
      WHERE(IS.NOT.NULL(c.locality))
        .execute(transaction)) {

      for (int i = 0; i < 51; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }

      assertFalse(rows.nextRow());
    }
  }
}