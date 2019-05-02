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

import static org.junit.Assert.*;
import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.DML.NOT;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.runner.VendorSchemaRunner;

public abstract class InPredicateTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends InPredicateTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(classicmodels.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends InPredicateTest {
  }

  @Test
  public void testInList() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        IN(p.productLine, "Ships", "Planes", "Trains"),
        SELECT(IN(p.productLine, "Ships", "Planes", "Trains")).
        FROM(p).
        WHERE(IN(p.productLine, "Ships", "Planes", "Trains")).
        LIMIT(1)).
      FROM(p).
      WHERE(IN(p.productLine, "Ships", "Planes", "Trains")).
      execute()) {
      for (int i = 0; i < 24; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testNotInList() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        NOT.IN(p.productLine, "Ships", "Planes", "Trains"),
        SELECT(NOT.IN(p.productLine, "Ships", "Planes", "Trains")).
        FROM(p).
        WHERE(NOT.IN(p.productLine, "Ships", "Planes", "Trains")).
        LIMIT(1)).
      FROM(p).
      WHERE(NOT.IN(p.productLine, "Ships", "Planes", "Trains")).
      execute()) {
      for (int i = 0; i < 86; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testInSubQuery() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        IN(p.productLine, SELECT(p.productLine).FROM(p)),
        SELECT(IN(p.productLine, SELECT(p.productLine).FROM(p))).
        FROM(p).
        WHERE(IN(p.productLine, SELECT(p.productLine).FROM(p))).
        LIMIT(1)).
      FROM(p).
      WHERE(IN(p.productLine, SELECT(p.productLine).FROM(p))).
      execute()) {
      for (int i = 0; i < 110; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
      }
    }
  }

  @Test
  public void testNotInSubQuery() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        NOT.IN(p.code, SELECT(p.productLine).FROM(p)),
        SELECT(NOT.IN(p.code, SELECT(p.productLine).FROM(p))).
        FROM(p).
        WHERE(NOT.IN(p.code, SELECT(p.productLine).FROM(p))).
        LIMIT(1)).
      FROM(p).
      WHERE(NOT.IN(p.code, SELECT(p.productLine).FROM(p))).
      execute()) {
      for (int i = 0; i < 110; ++i) {
        assertTrue(rows.nextRow());
        assertTrue(rows.nextEntity().get());
        assertTrue(rows.nextEntity().get());
      }
    }
  }
}