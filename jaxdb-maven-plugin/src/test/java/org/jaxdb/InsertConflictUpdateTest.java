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

import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalTime;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.vendor.DbVendor;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
public abstract class InsertConflictUpdateTest extends InsertTest {
  @DB(Derby.class)
  @DB(SQLite.class)
  public static class IntegrationTest extends InsertConflictUpdateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends InsertConflictUpdateTest {
  }

  Types.Type t1;
  Types.Type t2;
  Types.Type t3;

  @Before
  @Override
  public void before(final Types types) {
    T1 = types.new Type();
    T2 = types.new Type();
    T3 = types.new Type();

    t1 = T1.clone();
    t2 = T2.clone();
    t3 = T3.clone();

    init(t1, t2, t3);

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
  @Override
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInsertEntity(final Types types, final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t1)
        .execute(transaction)
        .getCount());

    try {
      INSERT(t1)
        .execute(transaction);

      fail("Expected SQLIntegrityConstraintViolationException");
    }
    catch (final SQLIntegrityConstraintViolationException e) {
    }

    transaction.rollback();

    assertEquals(1,
      INSERT(t1)
        .ON_CONFLICT()
        .DO_UPDATE()
        .execute(transaction)
        .getCount());

    t1.doubleType.set(Math.random());

    assertTrue(0 <
        INSERT(t1)
          .ON_CONFLICT()
          .DO_UPDATE()
          .execute(transaction)
          .getCount());

    assertFalse(t1.id.isNull());
    assertEquals(selectMaxId(transaction, t1), t1.id.getAsInt());
  }

  @Test
  @Override
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInsertColumns(final Types types, final Transaction transaction) throws IOException, SQLException {
    assertEquals(1,
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .execute(transaction)
        .getCount());

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
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .ON_CONFLICT()
        .DO_UPDATE()
        .execute(transaction)
        .getCount());

    assertFalse(t3.id.isNull());
    assertEquals(selectMaxId(transaction, t3), t3.id.getAsInt());
  }

  @Test
  @Override
  public void testInsertBatch(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Batch batch = new Batch();
    final int expectedCount = transaction.getVendor() == DbVendor.ORACLE ? 0 : 1;

    batch.addStatement(
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .onExecute((final int c) -> assertEquals(expectedCount, c)));

    batch.addStatement(
      INSERT(t3.id, t3.bigintType, t3.charType, t3.doubleType, t3.tinyintType, t3.timeType)
        .ON_CONFLICT()
        .DO_UPDATE()
        .onExecute((final int c) -> assertEquals(expectedCount, c)));

    assertEquals(2 * expectedCount, batch.execute(transaction).getCount());
  }

  @Test
  @DBTestRunner.Unsupported(Oracle.class) // FIXME: ORA-00933 command not properly ended
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInsertSelectIntoTable1(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Backup b = types.new Backup();

    DELETE(b)
      .execute(transaction);

    final Types.Type t = types.Type$;

    assertEquals(10,
      INSERT(b).VALUES(
        SELECT(t)
          .FROM(t)
          .WHERE(NOT(IS.NULL(t.id)))
          .LIMIT(10))
        .execute(transaction)
        .getCount());
  }

  @Test
  @DBTestRunner.Unsupported(Oracle.class) // FIXME: ORA-00933 command not properly ended
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInsertSelectIntoTable2(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Backup b = types.new Backup();

    DELETE(b)
      .execute(transaction);

    final Types.Type t = types.Type$;

    assertEquals(1000,
      INSERT(b).VALUES(
        SELECT(t).FROM(t).WHERE(IS.NOT.NULL(t.id)))
        // FIXME: LIMIT is not supported by Derby (and neither is JOIN)
        .ON_CONFLICT()
        .DO_UPDATE()
        .execute(transaction)
        .getCount());
  }

  @Override
  @Test
  @Ignore("Not sure if this is supported by MERGE")
  public void testInsertSelectIntoColumns(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Backup b = types.Backup$;
    final Types.Type t1 = types.new Type();
    final Types.Type t2 = types.new Type();
    final Types.Type t3 = types.new Type();

    DELETE(b)
      .execute(transaction);

    final int results =
      INSERT(b.binaryType, b.charType, b.enumType)
        .VALUES(
          SELECT(t1.binaryType, t2.charType, t3.enumType).FROM(t1, t2, t3)
            .WHERE(AND(
              EQ(t1.charType, t2.charType),
              EQ(t2.tinyintType, t3.tinyintType),
              EQ(t3.booleanType, t1.booleanType)))
            .LIMIT(27))
        .execute(transaction)
        .getCount();
    assertEquals(27, results);
  }
}