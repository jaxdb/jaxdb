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

package org.openjax.maven.plugin.rdb.jsql;

import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.fastjax.test.MixedTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.jsql.types;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema({types.class, classicmodels.class})
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class StringValueExpressionTest {
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
      Assert.assertTrue(rows.nextRow());

      // Char/Enum
      Assert.assertEquals("San FranciscoUS", rows.nextEntity().get());
      Assert.assertEquals("-San FranciscoUS", rows.nextEntity().get());
      Assert.assertEquals("San Francisco-US", rows.nextEntity().get());
      Assert.assertEquals("San FranciscoUS-", rows.nextEntity().get());
      Assert.assertEquals("-San FranciscoUS-", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco-US-", rows.nextEntity().get());
      Assert.assertEquals("San Francisco-US-", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco-US", rows.nextEntity().get());

      // Enum/Char
      Assert.assertEquals("USSan Francisco", rows.nextEntity().get());
      Assert.assertEquals("-USSan Francisco", rows.nextEntity().get());
      Assert.assertEquals("US-San Francisco", rows.nextEntity().get());
      Assert.assertEquals("USSan Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-USSan Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-US-San Francisco-", rows.nextEntity().get());
      Assert.assertEquals("US-San Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-US-San Francisco", rows.nextEntity().get());

      // Char/Char
      Assert.assertEquals("San FranciscoSan Francisco", rows.nextEntity().get());
      Assert.assertEquals("-San FranciscoSan Francisco", rows.nextEntity().get());
      Assert.assertEquals("San Francisco-San Francisco", rows.nextEntity().get());
      Assert.assertEquals("San FranciscoSan Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-San FranciscoSan Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco-San Francisco-", rows.nextEntity().get());
      Assert.assertEquals("San Francisco-San Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco-San Francisco", rows.nextEntity().get());

      // Enum/Enum
      Assert.assertEquals("USUS", rows.nextEntity().get());
      Assert.assertEquals("-USUS", rows.nextEntity().get());
      Assert.assertEquals("US-US", rows.nextEntity().get());
      Assert.assertEquals("USUS-", rows.nextEntity().get());
      Assert.assertEquals("-USUS-", rows.nextEntity().get());
      Assert.assertEquals("-US-US-", rows.nextEntity().get());
      Assert.assertEquals("US-US-", rows.nextEntity().get());
      Assert.assertEquals("-US-US", rows.nextEntity().get());

      // Char
      Assert.assertEquals("San Francisco-", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco", rows.nextEntity().get());
      Assert.assertEquals("-San Francisco-", rows.nextEntity().get());

      // Enum
      Assert.assertEquals("US-", rows.nextEntity().get());
      Assert.assertEquals("-US", rows.nextEntity().get());
      Assert.assertEquals("-US-", rows.nextEntity().get());
    }
  }

  @Test
  public void testConcatDynamic() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        IS.NOT.NULL(t.charType),
        IS.NOT.NULL(t.enumType)), transaction), 0);
      final types.Type clone = t.clone();

      t.charType.set(CONCAT(t.enumType, t.enumType));

      Assert.assertEquals(1, UPDATE(t).execute(transaction));
      Assert.assertEquals(clone.enumType.get().toString() + clone.enumType.get().toString(), t.charType.get());
      clone.charType.set(clone.enumType.get().toString() + clone.enumType.get().toString());

      t.charType.set(CONCAT(t.charType, t.charType));

      Assert.assertEquals(1, UPDATE(t).execute(transaction));
      Assert.assertEquals(clone.charType.get() + clone.charType.get(), t.charType.get());
      clone.charType.set(clone.charType.get() + clone.charType.get());

      t.charType.set(CONCAT(t.charType, t.enumType));

      Assert.assertEquals(1, UPDATE(t).execute(transaction));
      Assert.assertEquals(clone.charType.get() + clone.enumType.get(), t.charType.get());

      transaction.rollback();
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
      Assert.assertTrue(rows.nextRow());

      Assert.assertEquals("san francisco", rows.nextEntity().get());
      Assert.assertEquals("SAN FRANCISCO", rows.nextEntity().get());
      Assert.assertEquals("city", rows.nextEntity().get());
      Assert.assertEquals("CITY", rows.nextEntity().get());
    }
  }

  @Test
  public void testChangeCaseDynamic() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, IS.NOT.NULL(t.charType), transaction), 0);

      final types.Type clone = t.clone();

      t.charType.set(LOWER(t.charType));

      Assert.assertEquals(1, UPDATE(t).execute(transaction));
      Assert.assertEquals(clone.charType.get().toLowerCase(), t.charType.get());

      t.charType.set(UPPER(t.charType));

      Assert.assertEquals(1, UPDATE(t).execute(transaction));
      Assert.assertEquals(clone.charType.get().toUpperCase(), t.charType.get());

      transaction.rollback();
    }
  }
}