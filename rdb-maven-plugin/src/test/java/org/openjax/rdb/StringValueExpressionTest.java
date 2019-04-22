/* Copyright (c) 2017 OpenJAX
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

package org.openjax.rdb;

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.jsql.types;
import org.openjax.rdb.runner.TestTransaction;
import org.openjax.rdb.runner.VendorSchemaRunner;

public abstract class StringValueExpressionTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({types.class, classicmodels.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends StringValueExpressionTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({types.class, classicmodels.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends StringValueExpressionTest {
  }

  @Test
  public void testConcatStatic() throws IOException, SQLException {
    final classicmodels.Office o = new classicmodels.Office();
    try (final RowIterator<type.CHAR> rows =
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
      FROM(o).
      execute()) {
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
  public void testConcatDynamic() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        IS.NOT.NULL(t.charType),
        IS.NOT.NULL(t.enumType)), transaction), 0);
      final types.Type clone = t.clone();

      t.charType.set(CONCAT(t.enumType, t.enumType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.enumType.get().toString() + clone.enumType.get().toString(), t.charType.get());
      clone.charType.set(clone.enumType.get().toString() + clone.enumType.get().toString());

      t.charType.set(CONCAT(t.charType, t.charType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.charType.get() + clone.charType.get(), t.charType.get());
      clone.charType.set(clone.charType.get() + clone.charType.get());

      t.charType.set(CONCAT(t.charType, t.enumType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.charType.get() + clone.enumType.get(), t.charType.get());
    }
  }

  @Test
  public void testChangeCaseStatic() throws IOException, SQLException {
    final classicmodels.Office o = new classicmodels.Office();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        LOWER(o.city),
        UPPER(o.city),
        LOWER("CITY"),
        UPPER("city")).
      FROM(o).
      execute()) {
      assertTrue(rows.nextRow());

      assertEquals("san francisco", rows.nextEntity().get());
      assertEquals("SAN FRANCISCO", rows.nextEntity().get());
      assertEquals("city", rows.nextEntity().get());
      assertEquals("CITY", rows.nextEntity().get());
    }
  }

  @Test
  public void testChangeCaseDynamic() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, IS.NOT.NULL(t.charType), transaction), 0);
      final types.Type clone = t.clone();

      t.charType.set(LOWER(t.charType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.charType.get().toLowerCase(), t.charType.get());

      t.charType.set(UPPER(t.charType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.charType.get().toUpperCase(), t.charType.get());
    }
  }
}