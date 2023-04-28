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

import org.jaxdb.jsql.DML.NOT;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.AssertSelect;
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
public abstract class InPredicateTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends InPredicateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends InPredicateTest {
  }

  @Test
  @AssertSelect(isSimple=false)
  public void testInList(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(
        IN(p.productLine, "Ships", "Planes", "Trains"),
        SELECT(IN(p.productLine, "Ships", "Planes", "Trains")).
        FROM(p).
        WHERE(IN(p.productLine, "Ships", "Planes", "Trains")).
        LIMIT(1)).
      FROM(p).
      WHERE(IN(p.productLine, "Ships", "Planes", "Trains"))
        .execute(transaction)) {
      for (int i = 0; i < 24; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }
    }
  }

  @Test
  @AssertSelect(isSimple=false)
  public void testNotInList(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(
        NOT.IN(p.productLine, "Ships", "Planes", "Trains"),
        SELECT(NOT.IN(p.productLine, "Ships", "Planes", "Trains")).
        FROM(p).
        WHERE(NOT.IN(p.productLine, "Ships", "Planes", "Trains")).
        LIMIT(1)).
      FROM(p).
      WHERE(NOT.IN(p.productLine, "Ships", "Planes", "Trains"))
        .execute(transaction)) {
      for (int i = 0; i < 86; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }
    }
  }

  @Test
  @AssertSelect(isSimple=false)
  public void testInSubQuery(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(
        IN(p.productLine, SELECT(p.productLine).FROM(p)),
        SELECT(IN(p.productLine, SELECT(p.productLine).FROM(p))).
        FROM(p).
        WHERE(IN(p.productLine, SELECT(p.productLine).FROM(p))).
        LIMIT(1)).
      FROM(p).
      WHERE(IN(p.productLine, SELECT(p.productLine).FROM(p)))
        .execute(transaction)) {
      for (int i = 0; i < 110; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }
    }
  }

  @Test
  @AssertSelect(isSimple=false)
  public void testNotInSubQuery(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BOOLEAN> rows =
      SELECT(
        NOT.IN(p.code, SELECT(p.productLine).FROM(p)),
        SELECT(NOT.IN(p.code, SELECT(p.productLine).FROM(p))).
        FROM(p).
        WHERE(NOT.IN(p.code, SELECT(p.productLine).FROM(p))).
        LIMIT(1)).
      FROM(p).
      WHERE(NOT.IN(p.code, SELECT(p.productLine).FROM(p)))
        .execute(transaction)) {
      for (int i = 0; i < 110; ++i) { // [N]
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().getAsBoolean());
        assertTrue(rows.nextEntity().getAsBoolean());
      }
    }
  }
}