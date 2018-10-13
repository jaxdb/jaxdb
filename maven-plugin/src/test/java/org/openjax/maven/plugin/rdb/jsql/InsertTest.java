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

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.fastjax.test.MixedTest;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.Transaction;
import org.openjax.rdb.jsql.types;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(types.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class InsertTest {
  @Test
  public void testInsertEntity() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      final types.Type t = new types.Type();
      t.bigintType.set(8493l);
      t.binaryType.set("abc".getBytes());
      t.blobType.set(new ByteArrayInputStream("abc".getBytes()));
      t.booleanType.set(false);
      t.charType.set("hello");
      t.clobType.set(new StringReader("abc"));
      t.datetimeType.set(LocalDateTime.now());
      t.dateType.set(LocalDate.now());
      t.decimalType.set(new BigDecimal("12.34"));
      t.doubleType.set(32d);
      t.enumType.set(types.Type.EnumType.FOUR);
      t.floatType.set(42f);
      t.intType.set(2345);
      t.smallintType.set((short)32432);
      t.tinyintType.set((byte)127);
      t.timeType.set(LocalTime.now());
      assertEquals(1, INSERT(t).execute(transaction));

      transaction.rollback();
    }
  }

  @Test
  public void testInsertEntities() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      final types.Type t1 = new types.Type();
      t1.bigintType.set(8493l);
      t1.binaryType.set("abc".getBytes());
      t1.blobType.set(new ByteArrayInputStream("abc".getBytes()));
      t1.booleanType.set(false);
      t1.charType.set("hello");
      t1.clobType.set(new StringReader("abc"));
      t1.datetimeType.set(LocalDateTime.now());
      t1.dateType.set(LocalDate.now());
      t1.decimalType.set(new BigDecimal("12.34"));
      t1.doubleType.set(32d);
      t1.enumType.set(types.Type.EnumType.FOUR);
      t1.floatType.set(42f);
      t1.intType.set(2345);
      t1.smallintType.set((short)32432);
      t1.tinyintType.set((byte)127);
      t1.timeType.set(LocalTime.now());

      final types.Type t2 = new types.Type();
      t2.bigintType.set(843l);
      t2.binaryType.set("abcd".getBytes());
      t2.blobType.set(new ByteArrayInputStream("abcd".getBytes()));
      t2.booleanType.set(true);
      t2.charType.set("hello hi");
      t2.clobType.set(new StringReader("abcd"));
      t2.datetimeType.set(LocalDateTime.now());
      t2.dateType.set(LocalDate.now());
      t2.decimalType.set(new BigDecimal("123.34"));
      t2.doubleType.set(322d);
      t2.enumType.set(types.Type.EnumType.FOUR);
      t2.floatType.set(32f);
      t2.intType.set(1345);
      t2.smallintType.set((short)22432);
      t2.tinyintType.set((byte)-127);
      t2.timeType.set(LocalTime.now());

      // TODO: Implement batching mechanism to allow multiple jsql commands to execute in one batch

      assertEquals(1, INSERT(t1).execute(transaction));
      assertEquals(1, INSERT(t2).execute(transaction));

      transaction.rollback();
    }
  }

  @Test
  public void testInsertColumns() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      final types.Type t = new types.Type();
      t.bigintType.set(8493l);
      t.charType.set("hello");
      t.doubleType.set(32d);
      t.tinyintType.set((byte)127);
      t.timeType.set(LocalTime.now());

      final int results = INSERT(t.bigintType, t.charType, t.doubleType, t.tinyintType, t.timeType).execute(transaction);
      assertEquals(1, results);

      transaction.rollback();
    }
  }

  @Test
  public void testInsertSelectIntoTable() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      final types.TypeBackup b = new types.TypeBackup();
      DELETE(b).execute(transaction);

      final types.Type t = new types.Type();
      final int results = INSERT(b).VALUES(SELECT(t).FROM(t)).execute(transaction);
      assertTrue(results > 999);

      transaction.rollback();
    }
  }

  @Test
  public void testInsertSelectIntoColumns() throws IOException, SQLException {
    try (final Transaction transaction = new Transaction(types.class)) {
      final types.TypeBackup b = new types.TypeBackup();
      final types.Type t1 = new types.Type();
      final types.Type t2 = new types.Type();
      final types.Type t3 = new types.Type();
      DELETE(b).execute(transaction);
      final int results = INSERT(b.binaryType, b.charType, b.enumType).VALUES(SELECT(t1.binaryType, t2.charType, t3.enumType).FROM(t1, t2, t3).WHERE(AND(EQ(t1.charType, t2.charType), EQ(t2.tinyintType, t3.tinyintType), EQ(t3.booleanType, t1.booleanType)))).execute(transaction);
      assertTrue(results > 999);

      transaction.rollback();
    }
  }
}