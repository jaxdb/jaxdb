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
public abstract class LikePredicateTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends LikePredicateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends LikePredicateTest {
  }

  @Test
  @AssertSelect(entityOnlySelect=true, rowIteratorFullConsume=true)
  public void testLikeSimple(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<classicmodels.Product> rows =

      SELECT(p).
      FROM(p).
      WHERE(LIKE(p.name, "%"))
        .execute(transaction)) {

      for (int i = 0; i < 110; ++i) // [N]
        assertTrue(rows.nextRow());

      assertFalse(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(entityOnlySelect=true, rowIteratorFullConsume=true)
  public void testLikePrimary(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<classicmodels.Product> rows =

      SELECT(p).
      FROM(p).
      WHERE(LIKE(p.code, "S10%"))
        .execute(transaction)) {

      for (int i = 0; i < 6; ++i) // [N]
        assertTrue(rows.nextRow());

      assertFalse(rows.nextRow());
    }
  }

  private static final String $Ford$ = "%Ford%";

  @Test
  @AssertSelect(entityOnlySelect=false, rowIteratorFullConsume=false)
  public void testLikeComplex(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =

      SELECT(OR(
        LIKE(p.name, $Ford$),
        LIKE(
          SELECT(p.name).
          FROM(p).
          LIMIT(1), $Ford$)),
        SELECT(OR(
          LIKE(p.name, $Ford$),
          LIKE(
            SELECT(p.name).
            FROM(p).
            LIMIT(1), $Ford$))).
        FROM(p).
        WHERE(OR(
          LIKE(p.name, $Ford$),
          LIKE(
            SELECT(p.name).
            FROM(p).
            LIMIT(1), $Ford$))).
        LIMIT(1)).
      FROM(p).
      WHERE(OR(
        LIKE(p.name, $Ford$),
        LIKE(
          SELECT(p.name).
          FROM(p).
          LIMIT(1), $Ford$)))
            .execute(transaction)) {

      for (int i = 0; i < 15; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }
    }
  }
}