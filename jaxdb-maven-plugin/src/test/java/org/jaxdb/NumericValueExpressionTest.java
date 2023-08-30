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

import static org.jaxdb.NumericFunctionDynamicTest.*;
import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.RoundingMode;
import java.sql.SQLException;

import org.jaxdb.jsql.Classicmodels;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
import org.jaxdb.jsql.World;
import org.jaxdb.jsql.data;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class NumericValueExpressionTest {
  @RunWith(SchemaTestRunner.class)
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends NumericValueExpressionTest {
  }

  @RunWith(SchemaTestRunner.class)
  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends NumericValueExpressionTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=true)
  public void test(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Product p = classicmodels.Product$;
    final data.BIGINT b = new data.BIGINT();
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        COUNT(p),
        ADD(COUNT(p), COUNT(p)).AS(b),
        ADD(COUNT(p), 5),
        SUB(COUNT(p), COUNT(p)),
        SUB(COUNT(p), 5),
        MUL(COUNT(p), COUNT(p)),
        MUL(COUNT(p), 2),
        DIV(COUNT(p), COUNT(p)),
        DIV(COUNT(p), 2)).
      FROM(p).
      WHERE(OR(
        LT(ADD(p.msrp, p.price), 20),
        GT(SUB(p.msrp, p.quantityInStock), 10),
        EQ(MUL(p.msrp, ADD(p.msrp, p.price)), 40),
        EQ(DIV(p.msrp, SUB(p.msrp, p.quantityInStock)), 7)))
          .execute(transaction)) {

      assertTrue(rows.nextRow());
      do {
        final long count = rows.nextEntity().getAsLong();
        assertSame(b, rows.nextEntity());
        assertEquals(count + count, b.getAsLong());
        assertEquals(count + 5, rows.nextEntity().getAsLong());
        assertEquals(0, rows.nextEntity().getAsLong());
        assertEquals(count - 5, rows.nextEntity().getAsLong());
        assertEquals(count * count, rows.nextEntity().getAsLong());
        assertEquals(count * 2, rows.nextEntity().getAsLong());
        assertEquals(1, rows.nextEntity().getAsLong());
        assertEquals(count / 2, rows.nextEntity().getAsLong());
      }
      while (rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testAdd(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        LTE(t.tinyintType, 0),
        LTE(t.smallintType, 0),
        LTE(t.intType, 0),
        LTE(t.bigintType, 0),
        LTE(t.floatType, 0),
        LTE(t.doubleType, 0),
        LTE(t.decimalType, 0)))
          .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(ADD(t.tinyintType, t.tinyintType));
    t.smallintType.set(ADD(t.smallintType, t.smallintType));
    t.intType.set(ADD(t.intType, t.intType));
    t.bigintType.set(ADD(t.bigintType, t.bigintType));
    t.floatType.set(ADD(t.floatType, t.floatType));
    t.doubleType.set(ADD(t.doubleType, t.doubleType));
    t.decimalType.set(ADD(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals((byte)(clone.tinyintType.getAsByte() + clone.tinyintType.getAsByte()), t.tinyintType.getAsByte());
    assertEquals((short)(clone.smallintType.getAsShort() + clone.smallintType.getAsShort()), t.smallintType.getAsShort());
    assertEquals(clone.intType.getAsInt() + clone.intType.getAsInt(), t.intType.getAsInt());
    assertEquals(clone.bigintType.getAsLong() + clone.bigintType.getAsLong(), t.bigintType.getAsLong());
    assertEquals(clone.floatType.getAsFloat() + clone.floatType.getAsFloat(), t.floatType.getAsFloat(), Math.ulp(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.getAsDouble() + clone.doubleType.getAsDouble(), t.doubleType.getAsDouble(), Math.ulp(t.doubleType.getAsDouble()));
    assertEquals(clone.decimalType.get().add(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testSubtract(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, 0),
        GTE(t.smallintType, 0),
        GTE(t.intType, 0),
        GTE(t.bigintType, 0),
        GTE(t.floatType, 0),
        GTE(t.doubleType, 0),
        GTE(t.decimalType, 0)))
          .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(SUB(t.tinyintType, t.tinyintType));
    t.smallintType.set(SUB(t.smallintType, t.smallintType));
    t.intType.set(SUB(t.intType, t.intType));
    t.bigintType.set(SUB(t.bigintType, t.bigintType));
    t.floatType.set(SUB(t.floatType, t.floatType));
    t.doubleType.set(SUB(t.doubleType, t.doubleType));
    t.decimalType.set(SUB(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals((byte)(clone.tinyintType.getAsByte() - clone.tinyintType.getAsByte()), t.tinyintType.getAsByte());
    assertEquals((short)(clone.smallintType.getAsShort() - clone.smallintType.getAsShort()), t.smallintType.getAsShort());
    assertEquals(clone.intType.getAsInt() - clone.intType.getAsInt(), t.intType.getAsInt());
    assertEquals(clone.bigintType.getAsLong() - clone.bigintType.getAsLong(), t.bigintType.getAsLong());
    assertEquals(clone.floatType.getAsFloat() - clone.floatType.getAsFloat(), t.floatType.getAsFloat(), Math.ulp(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.getAsDouble() - clone.doubleType.getAsDouble(), t.doubleType.getAsDouble(), Math.ulp(t.doubleType.getAsDouble()));
    assertEquals(clone.decimalType.get().subtract(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testMultiply(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -10), LTE(t.tinyintType, 10),
        GTE(t.smallintType, -100), LTE(t.smallintType, 100),
        GTE(t.intType, -1000), LTE(t.intType, 1000),
        GTE(t.bigintType, -10000), LTE(t.bigintType, 10000),
        IS.NOT.NULL(t.floatType),
        IS.NOT.NULL(t.doubleType),
        GTE(t.decimalType, -100000), LTE(t.decimalType, 100000)))
          .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(MUL(t.tinyintType, t.tinyintType));
    t.smallintType.set(MUL(t.smallintType, t.smallintType));
    t.intType.set(MUL(t.intType, t.intType));
    t.bigintType.set(MUL(t.bigintType, t.bigintType));
    t.floatType.set(MUL(t.floatType, t.floatType));
    t.doubleType.set(MUL(t.doubleType, t.doubleType));
    t.decimalType.set(MUL(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals((byte)(clone.tinyintType.getAsByte() * clone.tinyintType.getAsByte()), t.tinyintType.getAsByte());
    assertEquals((short)(clone.smallintType.getAsShort() * clone.smallintType.getAsShort()), t.smallintType.getAsShort());
    assertEquals(clone.intType.getAsInt() * clone.intType.getAsInt(), t.intType.getAsInt());
    assertEquals(clone.bigintType.getAsLong() * clone.bigintType.getAsLong(), t.bigintType.getAsLong());
    assertEquals(clone.floatType.getAsFloat() * clone.floatType.getAsFloat(), t.floatType.getAsFloat(), Math.ulp(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.getAsDouble() * clone.doubleType.getAsDouble(), t.doubleType.getAsDouble(), Math.ulp(t.doubleType.getAsDouble()));
    assertEquals(clone.decimalType.get().multiply(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testDivide(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type$;
    t = getNthRow(0,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        NE(t.tinyintType, 0),
        NE(t.smallintType, 0),
        NE(t.intType, 0),
        NE(t.bigintType, 0),
        NE(t.floatType, 0),
        NE(t.doubleType, 0),
        NE(t.decimalType, 0)))
          .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(DIV(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(DIV(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(DIV(t.intType, t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(DIV(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(DIV(t.floatType, t.floatType));
    t.doubleType.set(DIV(t.doubleType, t.doubleType));
    t.decimalType.set(DIV(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals((byte)(clone.tinyintType.getAsByte() / clone.tinyintType.getAsByte()), t.tinyintType.getAsByte());
    assertEquals((short)(clone.smallintType.getAsShort() / clone.smallintType.getAsShort()), t.smallintType.getAsShort());
    assertEquals(clone.intType.getAsInt() / clone.intType.getAsInt(), t.intType.getAsInt());
    assertEquals(clone.bigintType.getAsLong() / clone.bigintType.getAsLong(), t.bigintType.getAsLong());
    assertEquals(clone.floatType.getAsFloat() / clone.floatType.getAsFloat(), t.floatType.getAsFloat(), Math.ulp(t.floatType.getAsFloat()));
    assertEquals((clone.doubleType.getAsDouble() / clone.doubleType.getAsDouble()), t.doubleType.getAsDouble(), Math.ulp(t.doubleType.getAsDouble()));
    assertEquals(clone.decimalType.get().divide(clone.decimalType.get(), RoundingMode.HALF_UP), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testUpdateVersion(final World world, final Transaction transaction) throws IOException, SQLException {
    World.City c = world.City$;
    try (final RowIterator<World.City> rows =

      SELECT(c).
      FROM(c)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      c = rows.nextEntity();

      assertEquals(1,
        UPDATE(c)
          .execute(transaction)
          .getCount());

      assertEquals(1,
        UPDATE(c)
          .execute(transaction)
          .getCount());

      final int version = c.version.getAsInt();
      c.version.set(0);
      assertEquals(0,
        UPDATE(c)
          .execute(transaction)
          .getCount());

      c.version.set(version);
      assertEquals(1,
        UPDATE(c)
          .execute(transaction)
          .getCount());

      c.version.set(0);
      assertEquals(0,
        UPDATE(c)
          .execute(transaction)
          .getCount());

      // If version is wrong, but version.wasSet is false
      c.version.set(0);
      c.version.cue(false);
      assertEquals(1,
        UPDATE(c)
          .execute(transaction)
          .getCount());
    }
  }
}