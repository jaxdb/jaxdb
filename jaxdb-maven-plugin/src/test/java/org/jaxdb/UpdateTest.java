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
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.vendor.DbVendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.io.SerializableReader;
import org.libj.io.UnsynchronizedStringReader;

@RunWith(SchemaTestRunner.class)
public abstract class UpdateTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends UpdateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends UpdateTest {
  }

  @Test
  public void testUpdateEntity(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<classicmodels.Product> rows =
      SELECT(p).
      FROM(p).
      LIMIT(1).
      FOR_UPDATE()
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      p = rows.nextEntity();

      p.price.set(new BigDecimal(20));

      assertEquals(1,
        UPDATE(p)
          .execute(transaction)
          .getCount());
    }
  }

  @Test
  public void testUpdateEntities(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<classicmodels.Product> rows1 =
      SELECT(p).
      FROM(p).
      LIMIT(1).
      FOR_SHARE()
        .execute(transaction)) {
      assertTrue(rows1.nextRow());
      p = rows1.nextEntity();

      classicmodels.ProductLine pl = classicmodels.ProductLine();
      final RowIterator<classicmodels.ProductLine> rows2 =
        SELECT(pl).
        FROM(pl).
        LIMIT(1).
        FOR_UPDATE(pl)
          .execute(transaction);

      assertTrue(rows2.nextRow());
      pl = rows2.nextEntity();

      p.quantityInStock.set((short)300);
      pl.description.set(new SerializableReader(new UnsynchronizedStringReader("New description")));

      final Batch batch = new Batch();
      final boolean isOracle = transaction.getVendor() == DbVendor.ORACLE;
      batch.addStatement(UPDATE(p)
        .onExecute(c -> assertEquals(isOracle ? 0 : 1, c)));
      batch.addStatement(UPDATE(pl)
        .onExecute(c -> assertEquals(isOracle ? 0 : 1, c)));

      assertEquals(isOracle ? 0 : 2, batch.execute(transaction).getCount());
    }
  }

  @Test
  public void testUpdateSetWhere(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    types.Type t = types.Type();
    try (final RowIterator<types.Type> rows =
      SELECT(t).
      FROM(t).
      LIMIT(1).
      FOR_SHARE(t).
      SKIP_LOCKED()
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      assertTrue(0 <
        UPDATE(t).
        SET(t.enumType, types.Type.EnumType.FOUR).
        WHERE(EQ(t.enumType, types.Type.EnumType.ONE))
          .execute(transaction)
          .getCount());
    }
  }

  @Test
  public void testUpdateSet(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    types.Type t = types.Type();
    try (final RowIterator<types.Type> rows =
      SELECT(t).
      FROM(t).
      LIMIT(1).
      FOR_UPDATE(t).
      NOWAIT()
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();

      assertTrue(300 <
        UPDATE(t).
        SET(t.datetimeType, LocalDateTime.now())
          .execute(transaction)
          .getCount());
    }
  }
}