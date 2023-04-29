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

import static org.jaxdb.NumericFunctionDynamicTest.*;
import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;

import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class StringValueExpressionTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends StringValueExpressionTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends StringValueExpressionTest {
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  public void testConcatStatic(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Office o = classicmodels.Office();
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        // Char/Enum
        CONCAT(o.city, o.country),
        CONCAT("-", o.city, o.country),
        CONCAT(o.city, "-", o.country),
        CONCAT(o.city, o.country, "-"),
        CONCAT("-", o.city, o.country, "-"),
        CONCAT("-", o.city, "-", o.country, "-"),
        CONCAT(o.city, "-", o.country, "-"),
        CONCAT("-", o.city, "-", o.country),

        // Enum/Char
        CONCAT(o.country, o.city),
        CONCAT("-", o.country, o.city),
        CONCAT(o.country, "-", o.city),
        CONCAT(o.country, o.city, "-"),
        CONCAT("-", o.country, o.city, "-"),
        CONCAT("-", o.country, "-", o.city, "-"),
        CONCAT(o.country, "-", o.city, "-"),
        CONCAT("-", o.country, "-", o.city),

        // Char/Char
        CONCAT(o.city, o.city),
        CONCAT("-", o.city, o.city),
        CONCAT(o.city, "-", o.city),
        CONCAT(o.city, o.city, "-"),
        CONCAT("-", o.city, o.city, "-"),
        CONCAT("-", o.city, "-", o.city, "-"),
        CONCAT(o.city, "-", o.city, "-"),
        CONCAT("-", o.city, "-", o.city),

        // Enum/Enum
        CONCAT(o.country, o.country),
        CONCAT("-", o.country, o.country),
        CONCAT(o.country, "-", o.country),
        CONCAT(o.country, o.country, "-"),
        CONCAT("-", o.country, o.country, "-"),
        CONCAT("-", o.country, "-", o.country, "-"),
        CONCAT(o.country, "-", o.country, "-"),
        CONCAT("-", o.country, "-", o.country),

        // Char
        CONCAT(o.city, "-"),
        CONCAT("-", o.city),
        CONCAT("-", o.city, "-"),

        // Enum
        CONCAT(o.country, "-"),
        CONCAT("-", o.country),
        CONCAT("-", o.country, "-")).
      FROM(o)
        .execute(transaction)) {

      assertTrue(rows.nextRow());

      // Char/Enum
      assertEquals("San FranciscoUS", rows.nextEntity().get());
      assertEquals("-San FranciscoUS", rows.nextEntity().get());
      assertEquals("San Francisco-US", rows.nextEntity().get());
      assertEquals("San FranciscoUS-", rows.nextEntity().get());
      assertEquals("-San FranciscoUS-", rows.nextEntity().get());
      assertEquals("-San Francisco-US-", rows.nextEntity().get());
      assertEquals("San Francisco-US-", rows.nextEntity().get());
      assertEquals("-San Francisco-US", rows.nextEntity().get());

      // Enum/Char
      assertEquals("USSan Francisco", rows.nextEntity().get());
      assertEquals("-USSan Francisco", rows.nextEntity().get());
      assertEquals("US-San Francisco", rows.nextEntity().get());
      assertEquals("USSan Francisco-", rows.nextEntity().get());
      assertEquals("-USSan Francisco-", rows.nextEntity().get());
      assertEquals("-US-San Francisco-", rows.nextEntity().get());
      assertEquals("US-San Francisco-", rows.nextEntity().get());
      assertEquals("-US-San Francisco", rows.nextEntity().get());

      // Char/Char
      assertEquals("San FranciscoSan Francisco", rows.nextEntity().get());
      assertEquals("-San FranciscoSan Francisco", rows.nextEntity().get());
      assertEquals("San Francisco-San Francisco", rows.nextEntity().get());
      assertEquals("San FranciscoSan Francisco-", rows.nextEntity().get());
      assertEquals("-San FranciscoSan Francisco-", rows.nextEntity().get());
      assertEquals("-San Francisco-San Francisco-", rows.nextEntity().get());
      assertEquals("San Francisco-San Francisco-", rows.nextEntity().get());
      assertEquals("-San Francisco-San Francisco", rows.nextEntity().get());

      // Enum/Enum
      assertEquals("USUS", rows.nextEntity().get());
      assertEquals("-USUS", rows.nextEntity().get());
      assertEquals("US-US", rows.nextEntity().get());
      assertEquals("USUS-", rows.nextEntity().get());
      assertEquals("-USUS-", rows.nextEntity().get());
      assertEquals("-US-US-", rows.nextEntity().get());
      assertEquals("US-US-", rows.nextEntity().get());
      assertEquals("-US-US", rows.nextEntity().get());

      // Char
      assertEquals("San Francisco-", rows.nextEntity().get());
      assertEquals("-San Francisco", rows.nextEntity().get());
      assertEquals("-San Francisco-", rows.nextEntity().get());

      // Enum
      assertEquals("US-", rows.nextEntity().get());
      assertEquals("-US", rows.nextEntity().get());
      assertEquals("-US-", rows.nextEntity().get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  public void testConcatDynamic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    types.Type t = types.Type();
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(AND(
      IS.NOT.NULL(t.charType),
      IS.NOT.NULL(t.enumType)))
        .execute(transaction));

    final types.Type clone = t.clone();

    t.charType.set(CONCAT(t.enumType, t.enumType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.enumType.get().toString() + clone.enumType.get().toString(), t.charType.get());
    clone.charType.set(clone.enumType.get().toString() + clone.enumType.get().toString());

    t.charType.set(CONCAT(t.charType, t.charType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.charType.get() + clone.charType.get(), t.charType.get());
    clone.charType.set(clone.charType.get() + clone.charType.get());

    t.charType.set(CONCAT(t.charType, t.enumType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.charType.get() + clone.enumType.get(), t.charType.get());
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  public void testChangeCaseStatic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Office o = classicmodels.Office();
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        LOWER(o.city),
        UPPER(o.city),
        LOWER("CITY"),
        UPPER("city")).
      FROM(o)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals("san francisco", rows.nextEntity().get());
      assertEquals("SAN FRANCISCO", rows.nextEntity().get());
      assertEquals("city", rows.nextEntity().get());
      assertEquals("CITY", rows.nextEntity().get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  public void testChangeCaseDynamic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    types.Type t = types.Type();
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(IS.NOT.NULL(t.charType))
        .execute(transaction));

    final types.Type clone = t.clone();

    t.charType.set(LOWER(t.charType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.charType.get().toLowerCase(), t.charType.get());

    t.charType.set(UPPER(t.charType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.charType.get().toUpperCase(), t.charType.get());
  }

  @Test
  @Ignore
  @AssertSelect(isConditionOnlyPrimary=true)
  public void testLengthStatic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Office o = classicmodels.Office();
    try (final RowIterator<data.INT> rows =

      SELECT(
        // Char
        LENGTH(o.city),
        // Enum
        LENGTH(o.country),
        // String
        LENGTH("hello")).
      FROM(o)
        .execute(transaction)) {

      assertTrue(rows.nextRow());

      // Char/Enum
      assertEquals(Integer.valueOf(13), rows.nextEntity().get());
      assertEquals(Integer.valueOf(2), rows.nextEntity().get());
      assertEquals(Integer.valueOf(5), rows.nextEntity().get());
    }
  }

  @Test
  @Ignore
  @AssertSelect(isConditionOnlyPrimary=true)
  public void testLengthDynamic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Office o = classicmodels.Office();
    try (final RowIterator<data.INT> rows =

      SELECT(LENGTH(CONCAT("-", o.country, "-", o.city, "-"))).
      FROM(o)
        .execute(transaction)) {

      assertTrue(rows.nextRow());

      // Char/Enum
      assertEquals(Integer.valueOf(18), rows.nextEntity().get());
    }
  }

  @Test
  @Ignore
  public void testSubstringStatic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    // TODO: Implement this
  }

  @Test
  @Ignore
  public void testSubstringDynamic(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    // TODO: Implement this
  }
}