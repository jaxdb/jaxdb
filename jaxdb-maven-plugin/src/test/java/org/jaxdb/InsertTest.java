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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.vendor.DbVendor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.io.DelegateInputStream;
import org.libj.io.DelegateReader;
import org.libj.io.UnsynchronizedStringReader;

@RunWith(SchemaTestRunner.class)
public abstract class InsertTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends InsertTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends InsertTest {
  }

  private static class BlobStream extends DelegateInputStream {
    public BlobStream(final String s) {
      in = new ByteArrayInputStream(s.getBytes());
      mark(Integer.MAX_VALUE);
    }

    @Override
    public void close() throws IOException {
      reset();
      mark(Integer.MAX_VALUE);
    }
  }

  private static class ClobStream extends DelegateReader {
    public ClobStream(final String s) {
      in = new UnsynchronizedStringReader(s);
      try {
        mark(Integer.MAX_VALUE);
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }

    @Override
    public void close() {
      try {
        reset();
        mark(Integer.MAX_VALUE);
      }
      catch (final IOException e) {
        throw new UncheckedIOException(e);
      }
    }
  }

  static final types.Type T1 = new types.Type();
  static final types.Type T2 = new types.Type();
  static final types.Type T3 = new types.Type();

  public static void init(final types.Type t1, final types.Type t2, final types.Type t3) {
    t1.bigintType.set(8493L);
    t1.binaryType.set("abc".getBytes());
    t1.blobType.set(new BlobStream("abc"));
    t1.booleanType.set(false);
    t1.charType.set("hello");
    t1.clobType.set(new ClobStream("abc"));
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

    t2.bigintType.set(843L);
    t2.binaryType.set("abcd".getBytes());
    t2.blobType.set(new BlobStream("abcd"));
    t2.booleanType.set(true);
    t2.charType.set("hello hi");
    t2.clobType.set(new ClobStream("abcd"));
    t2.datetimeType.set(LocalDateTime.now());
    t2.dateType.set(LocalDate.now());
    t2.decimalType.set(new BigDecimal("12.334"));
    t2.doubleType.set(322d);
    t2.enumType.set(types.Type.EnumType.FOUR);
    t2.floatType.set(32f);
    t2.intType.set(1345);
    t2.smallintType.set((short)22432);
    t2.tinyintType.set((byte)-127);
    t2.timeType.set(LocalTime.now());

    t3.bigintType.set(8493L);
    t3.charType.set("hello");
    t3.doubleType.set(32d);
    t3.tinyintType.set((byte)127);
    t3.timeType.set(LocalTime.now());
  }

  final types.Type t1 = T1.clone();
  final types.Type t2 = T2.clone();
  final types.Type t3 = T3.clone();

  @Before
  public void before() {
    init(t1, t2, t3);
  }

  static int selectMaxId(final Transaction transaction, final types.Type t) throws IOException, SQLException {
    try (final RowIterator<data.INT> rows =

      SELECT(MAX(t.id))
        .execute(transaction)) {

      return rows.nextRow() ? rows.nextEntity().getAsInt() : 1;
    }
  }

  private static void testInsertEntity(final Transaction transaction, final types.Type t) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t)
        .execute(transaction)
        .getCount());

    assertFalse(t.id.isNull());
    assertEquals(selectMaxId(transaction, t), t.id.getAsInt());
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testInsertEntity(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    testInsertEntity(transaction, t1);
    testInsertEntity(transaction, t2);
    testInsertEntity(transaction, t3);
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void testInsertColumns(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t3 = new types.Type();
    t3.bigintType.set(8493L);
    t3.charType.set("hello");
    t3.doubleType.set(32d);
    t3.tinyintType.set((byte)127);
    t3.timeType.set(LocalTime.now());

    assertEquals(1,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .execute(transaction)
        .getCount());

    final int id;
    try (final RowIterator<data.INT> rows =

      SELECT(MAX(t3.id))
        .execute(transaction)) {

      id = rows.nextRow() ? rows.nextEntity().getAsInt() : 1;
      assertFalse(rows.nextRow());
    }

    assertFalse(t3.id.isNull());
    assertEquals(id, t3.id.getAsInt());
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testInsertBatch(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final DbVendor vendor = transaction.getVendor();
    final boolean isOracle = vendor == DbVendor.ORACLE;
    final Batch batch = new Batch();
    batch.addStatement(
      INSERT(t1)
        .onExecute(c -> assertEquals(isOracle ? 0 : 1, c)));

    batch.addStatement(
      INSERT(t2)
        .onExecute(c -> assertEquals(isOracle ? 0 : 1, c)));

    batch.addStatement(
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .onExecute(c -> assertEquals(isOracle ? 0 : 1, c)));

    assertEquals(isOracle ? 0 : 3, batch.execute(transaction).getCount());

    if (isOracle || vendor == DbVendor.DERBY || vendor == DbVendor.SQLITE)
      return;

    final int id = selectMaxId(transaction, t1);
    assertEquals(id - 2, t1.id.getAsInt());
    assertEquals(id - 1, t2.id.getAsInt());
    assertEquals(id - 0, t3.id.getAsInt());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  @DBTestRunner.Unsupported(Oracle.class) // FIXME: ORA-00933 command not properly ended
  public void testInsertSelectIntoTable(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Backup b = new types.Backup();

    DELETE(b)
      .execute(transaction);

    final types.Type t = types.Type();

    assertEquals(27,
      INSERT(b).
      VALUES(
        SELECT(t).
        FROM(t).
        LIMIT(27))
          .execute(transaction)
          .getCount());
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testInsertSelectIntoColumns(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Backup b = types.Backup();
    final types.Type t1 = new types.Type();
    final types.Type t2 = new types.Type();
    final types.Type t3 = new types.Type();

    DELETE(b)
      .execute(transaction);

    assertEquals(27,
      INSERT(b.binaryType, b.charType, b.enumType).
        VALUES(
          SELECT(t1.binaryType, t2.charType, t3.enumType).
          FROM(t1, t2, t3).
          WHERE(AND(
            EQ(t1.charType, t2.charType),
            EQ(t2.tinyintType, t3.tinyintType),
            EQ(t3.booleanType, t1.booleanType))).
            LIMIT(27))
          .execute(transaction)
          .getCount());
  }
}