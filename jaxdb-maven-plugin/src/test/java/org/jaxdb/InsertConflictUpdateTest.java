/* Copyright (c) 2021 JAX-DB
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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Transaction.Event;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.VendorRunner;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.runner.VendorSchemaRunner.Schema;
import org.jaxdb.vendor.DBVendor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
public abstract class InsertConflictUpdateTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends InsertConflictUpdateTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends InsertConflictUpdateTest {
  }

  final types.Type t1 = InsertTest.T1.clone();
  final types.Type t2 = InsertTest.T2.clone();
  final types.Type t3 = InsertTest.T3.clone();

  @Before
  public void before() {
    InsertTest.init(t1, t2, t3);
    t1.id.set(1001);
    t2.id.set(1002);
    t3.id.set(1003);
    t3.bigintType.set(8493L);
    t3.charType.set("hello");
    t3.doubleType.set(32d);
    t3.tinyintType.set((byte)127);
    t3.timeType.set(LocalTime.now());
  }

  @Test
  public void testInsertEntity(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t1)
        .execute(transaction));

    try {
      INSERT(t1)
        .execute(transaction);

      fail("Expected SQLIntegrityConstraintViolationException");
    }
    catch (final SQLIntegrityConstraintViolationException e) {
    }

    transaction.rollback();

    assertEquals(1,
      INSERT(t1).
        ON_CONFLICT().
        DO_UPDATE()
          .execute(transaction));

    t1.doubleType.set(Math.random());
    assertTrue(0 <
      INSERT(t1).
        ON_CONFLICT().
        DO_UPDATE()
          .execute(transaction));

    assertFalse(t1.id.isNull());
    assertEquals(InsertTest.getMaxId(transaction, t1), t1.id.getAsInt());
  }

  @Test
  public void testInsertColumns(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .execute(transaction));

    try {
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .execute(transaction);

      fail("Expected SQLIntegrityConstraintViolationException");
    }
    catch (final SQLIntegrityConstraintViolationException e) {
    }

    transaction.rollback();

    t3.charType.set("hi");
    assertEquals(1,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType).
        ON_CONFLICT().
        DO_UPDATE()
          .execute(transaction));

    assertFalse(t3.id.isNull());
    assertEquals(InsertTest.getMaxId(transaction, t3), t3.id.getAsInt());
  }

  @Test
  public void testInsertBatch(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    try (final Batch batch = new Batch()) {
      final int expectedCount = transaction.getVendor() == DBVendor.ORACLE ? 0 : 1;
      batch.addStatement(
        INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType),
          (Event e, int c) -> assertEquals(expectedCount, c));
      batch.addStatement(
        INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType).
        ON_CONFLICT().
        DO_UPDATE(),
          (Event e, int c) -> assertEquals(expectedCount, c));

      assertEquals(2 * expectedCount, batch.execute(transaction));
    }
  }

  @Test
  @VendorRunner.Unsupported(Oracle.class) // FIXME: ORA-00933 command not properly ended
  public void testInsertSelectIntoTable(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.TypeBackup b = types.TypeBackup();
    DELETE(b)
      .execute(transaction);

    final types.Type t = types.Type();
    assertEquals(10, INSERT(b).
      VALUES(
        SELECT(t).
        FROM(t).
        WHERE(IS.NOT.NULL(t.id)).
        LIMIT(10))
      .execute(transaction));

    assertEquals(1000, INSERT(b).
      VALUES(
        SELECT(t).
        FROM(t).
        WHERE(IS.NOT.NULL(t.id))).
      // FIXME: LIMIT is not supported by Derby (and neither is JOIN)
      ON_CONFLICT().
      DO_UPDATE()
        .execute(transaction));
  }

  @Ignore("Not sure if this is supported by MERGE")
  public void testInsertSelectIntoColumns(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.TypeBackup b = types.TypeBackup();
    final types.Type t1 = types.Type(1);
    final types.Type t2 = types.Type(2);
    final types.Type t3 = types.Type(3);

    DELETE(b)
      .execute(transaction);

    final int results =
      INSERT(b.binaryType, b.charType, b.enumType).
      VALUES(
        SELECT(t1.binaryType, t2.charType, t3.enumType).
        FROM(t1, t2, t3).
        WHERE(AND(
          EQ(t1.charType, t2.charType),
          EQ(t2.tinyintType, t3.tinyintType),
          EQ(t3.booleanType, t1.booleanType))).
          LIMIT(27))
      .execute(transaction);
    assertEquals(27, results);
  }
}