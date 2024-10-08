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

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDateTime;

import org.jaxdb.jsql.Batch;
import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
import org.jaxdb.jsql.Types.$AbstractType.EnumType;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.vendor.DbVendor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.io.UnsynchronizedStringReader;

@RunWith(SchemaTestRunner.class)
public abstract class UpdateTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends UpdateTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends UpdateTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity = true, rowIteratorFullConsume = true)
  public void testSelectForUpdateEntity(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    Classicmodels.Product p = classicmodels.Product$;
    try (
      final RowIterator<Classicmodels.Product> rows =
        SELECT(p)
          .FROM(p)
          .LIMIT(1)
          .FOR_UPDATE()
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      p = rows.nextEntity();
      assertFalse(rows.nextRow());

      p.price.set(new BigDecimal(20));

      assertEquals(1,
        UPDATE(p)
          .execute(transaction)
          .getCount());
    }
  }

  @Test
  @DBTestRunner.Unsupported(Derby.class) // FIXME: ERROR 42Y90: FOR UPDATE is not permitted in this type of statement.
  @AssertSelect(cacheSelectEntity = true, rowIteratorFullConsume = true)
  public void testSelectForUpdateEntities(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    Classicmodels.Product p = classicmodels.Product$;
    Classicmodels.ProductLine pl = classicmodels.ProductLine$;
    try (
      final RowIterator<data.Table> rows =
        SELECT(p, pl)
          .FROM(p, pl)
          .WHERE(EQ(p.productLine, pl.productLine))
          .LIMIT(1)
          .FOR_UPDATE(p)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      p = (Classicmodels.Product)rows.nextEntity();
      assertFalse(rows.nextRow());

      p.price.set(new BigDecimal(20));

      assertEquals(1,
        UPDATE(p)
          .execute(transaction)
          .getCount());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = true, rowIteratorFullConsume = true)
  public void testUpdateEntities(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    Classicmodels.Product p = classicmodels.Product$;
    try (
      final RowIterator<Classicmodels.Product> rows1 =
        SELECT(p)
          .FROM(p)
          .LIMIT(1)
          .FOR_SHARE()
          .execute(transaction)
    ) {
      assertTrue(rows1.nextRow());
      p = rows1.nextEntity();
      assertFalse(rows1.nextRow());

      Classicmodels.ProductLine pl = classicmodels.ProductLine$;
      try (
        final RowIterator<Classicmodels.ProductLine> rows2 =
          SELECT(pl)
            .FROM(pl)
            .LIMIT(1)
            .FOR_UPDATE(pl)
            .execute(transaction)
      ) {
        assertTrue(rows2.nextRow());
        pl = rows2.nextEntity();
        assertFalse(rows2.nextRow());
      }

      p.quantityInStock.set((short)300);
      pl.description.set(new UnsynchronizedStringReader("New description"));

      final Batch batch = new Batch();
      final boolean isOracle = transaction.getVendor() == DbVendor.ORACLE;
      batch.addStatement(UPDATE(p)
        .onExecute((final int c) -> assertEquals(isOracle ? 0 : 1, c)));
      batch.addStatement(UPDATE(pl)
        .onExecute((final int c) -> assertEquals(isOracle ? 0 : 1, c)));

      assertEquals(isOracle ? 0 : 2, batch.execute(transaction).getCount());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = true, rowIteratorFullConsume = true)
  public void testUpdateSetWhere(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    try (
      final RowIterator<Types.Type> rows =
        SELECT(t)
          .FROM(t)
          .LIMIT(1)
          .FOR_SHARE(t)
          .SKIP_LOCKED()
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertTrue(0 <
          UPDATE(t)
            .SET(t.enumType, EnumType.FOUR)
            .WHERE(EQ(t.enumType, EnumType.ONE))
            .execute(transaction)
            .getCount());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity = true, rowIteratorFullConsume = true)
  public void testUpdateSet(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    try (
      final RowIterator<Types.Type> rows =
        SELECT(t)
          .FROM(t)
          .LIMIT(1)
          .FOR_UPDATE(t)
          .NOWAIT()
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
      t = rows.nextEntity();
      assertFalse(rows.nextRow());

      assertTrue(300 <
          UPDATE(t)
            .SET(t.datetimeType, LocalDateTime.now())
            .execute(transaction)
            .getCount());
    }
  }
}