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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalTime;

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Transaction.Event;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(types.class)
public abstract class InsertOnConflictTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends InsertOnConflictTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends InsertOnConflictTest {
  }

  static final types.Type t1 = InsertTest.t1.clone();
  static final types.Type t2 = InsertTest.t2.clone();
  static final types.Type t3 = InsertTest.t3.clone();

  static {
    t1.id.set(1001);
    t2.id.set(1002);
    t3.id.set(1003);
  }

  @Test
  public void testInsertEntity() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      assertEquals(1, INSERT(t1).execute(transaction));
      t1.doubleType.set(Math.random());
      assertEquals(1, INSERT(t1).ON_CONFLICT().DO_UPDATE().execute(transaction));
    }
  }

  @Test
  @Ignore
  public void testInsertEntities() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      assertEquals(1, INSERT(t1).execute(transaction));
      assertEquals(1, INSERT(t2).execute(transaction));
    }
  }

  @Test
  @Ignore
  public void testInsertColumns() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t3 = new types.Type();
      t3.bigintType.set(8493L);
      t3.charType.set("hello");
      t3.doubleType.set(32d);
      t3.tinyintType.set((byte)127);
      t3.timeType.set(LocalTime.now());

      final int results =
        INSERT(t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
          .execute(transaction);
      assertEquals(1, results);
    }
  }

  @Test
  @Ignore
  public void testInsertBatch() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final Batch batch = new Batch();
      batch.addStatement(INSERT(t1), (Event e, int c) -> assertEquals(1, c));
      batch.addStatement(INSERT(t2), (Event e, int c) -> assertEquals(1, c));
      batch.addStatement(INSERT(t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType), (Event e, int c) -> assertEquals(1, c));
      assertEquals(3, batch.execute(transaction));
    }
  }

  @Test
  @Ignore
  public void testInsertSelectIntoTable() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.TypeBackup b = types.TypeBackup();
      DELETE(b)
        .execute(transaction);

      final types.Type t = types.Type();
      final int results =
        INSERT(b).
        VALUES(
          SELECT(t).
          FROM(t).
          LIMIT(27))
          .execute(transaction);

      assertEquals(27, results);
    }
  }

  @Test
  @Ignore
  public void testInsertSelectIntoColumns() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.TypeBackup b = types.TypeBackup();
      final types.Type t1 = types.Type(1);
      final types.Type t2 = types.Type(2);
      final types.Type t3 = types.Type(3);

      DELETE(b).execute(transaction);
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
}