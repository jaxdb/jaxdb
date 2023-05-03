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

import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
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
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testFailSelectEntityExclusivityTrue(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p.code).
        FROM(p)
          .execute(transaction)) {
      }

      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testFailSelectEntityExclusivityFalse(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction)) {
      }

      fail("Expected AssertionError");
    }
    catch (final AssertionError e) {
      if ("Expected AssertionError".equals(e.getMessage()))
        throw e;
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testFailConditionAbsolutePrimaryKeyExclusivityTrue(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p).
        WHERE(LIKE(p.code, "%abc%"))
          .execute(transaction)) {
      }

      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=false, cacheableRowIteratorFullConsume=false)
  public void testFailConditionAbsolutePrimaryKeyExclusivityFalse(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();

      try (final RowIterator<?> rows =
        SELECT(p).
        FROM(p)
          .execute(transaction)) {
      }

      fail("Expected AssertionError");
    }
    catch (final AssertionError e) {
      if ("Expected AssertionError".equals(e.getMessage()))
        throw e;
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=true)
  public void testRowIteratorFullConsumeRequiredTrue1(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction)) {

        rows.nextRow();
      }

      fail("Expected IllegalStateException");
    }
    catch (final IllegalStateException e) {
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=false, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=true)
  public void testRowIteratorFullConsumeRequiredTrue2(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p.code).
        FROM(p)
          .execute(transaction)) {

        while (rows.nextRow());
      }

      fail("Expected AssertionError");
    }
    catch (final AssertionError e) {
      if ("Expected AssertionError".equals(e.getMessage()))
        throw e;
    }
  }

  @Test
  @AssertSelect(selectEntityOnly=true, allConditionsByAbsolutePrimaryKey=true, cacheableRowIteratorFullConsume=false)
  public void testRowIteratorFullConsumeRequiredFalse(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      final classicmodels.Product p = classicmodels.Product();
      try (final RowIterator<?> rows =

        SELECT(p).
        FROM(p)
          .execute(transaction)) {

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