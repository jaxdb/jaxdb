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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
public abstract class InsertConflictNothingTest extends InsertConflictUpdateTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends InsertConflictNothingTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends InsertConflictNothingTest {
  }

  @Test
  @Override
  public void testInsertEntity(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t1)
        .execute(transaction));

    t1.doubleType.set(Math.random());
    assertEquals(0,
      INSERT(t1).
        ON_CONFLICT().
        DO_NOTHING()
          .execute(transaction));

    assertFalse(t1.id.isNull());
    assertEquals(InsertTest.getMaxId(transaction, t1), t1.id.getAsInt());
  }

  @Test
  @Override
  public void testInsertColumns(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .execute(transaction));

    t3.charType.set("hi");
    assertEquals(0,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType).
        ON_CONFLICT().
        DO_NOTHING()
          .execute(transaction));

    assertFalse(t3.id.isNull());
    assertEquals(InsertTest.getMaxId(transaction, t3), t3.id.getAsInt());
  }

  @Test
  @Override
  public void testInsertBatch(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    try (final Batch batch = new Batch()) {
      final int expectedCount = transaction.getVendor() == DBVendor.ORACLE ? 0 : 1;
      batch.addStatement(
        INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType),
          (Event e, int c) -> assertEquals(expectedCount, c));
      batch.addStatement(
        INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType).
        ON_CONFLICT().
        DO_NOTHING(),
          (Event e, int c) -> assertEquals(0, c));

      assertEquals(expectedCount, batch.execute(transaction));
    }
  }

  @Test
  @Override
  @VendorRunner.Unsupported(Oracle.class) // FIXME: ORA-00933 command not properly ended
  public void testInsertSelectIntoTable(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.TypeBackup b = new types.TypeBackup();
    DELETE(b)
      .execute(transaction);

    final types.Type t = types.Type();
    assertEquals(1000, INSERT(b).
      VALUES(
        SELECT(t).
        FROM(t).
        WHERE(IS.NOT.NULL(t.id)))
      .execute(transaction));

    assertEquals(0, INSERT(b).
      VALUES(
        SELECT(t).
        FROM(t).
        WHERE(IS.NOT.NULL(t.id))).
      ON_CONFLICT().
      DO_NOTHING()
        .execute(transaction));
  }

  @Override
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