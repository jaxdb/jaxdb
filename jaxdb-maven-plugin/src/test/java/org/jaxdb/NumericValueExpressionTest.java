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

import static org.junit.Assert.*;
import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.jsql.world;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;

public abstract class NumericValueExpressionTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class, world.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends NumericValueExpressionTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class, world.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends NumericValueExpressionTest {
  }

  @Test
  public void test() throws IOException, SQLException {
    final classicmodels.Product p = new classicmodels.Product();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        ADD(COUNT(), 5),
        SUB(COUNT(), 5),
        MUL(COUNT(), 2),
        DIV(COUNT(), 2)).
      FROM(p).
      WHERE(OR(
        LT(ADD(p.msrp, p.price), 20),
        GT(SUB(p.msrp, p.quantityInStock), 10),
        EQ(MUL(p.msrp, ADD(p.msrp, p.price)), 40),
        EQ(DIV(p.msrp, SUB(p.msrp, p.quantityInStock)), 7))).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Long.valueOf(rows.nextEntity().get().longValue() - 5), Long.valueOf(rows.nextEntity().get().longValue() + 5));
      assertEquals(Long.valueOf(rows.nextEntity().get().longValue() / 2), Long.valueOf(rows.nextEntity().get().longValue() * 2));
    }
  }

  @Test
  public void testAdd() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        LTE(t.tinyintType, 0),
        LTE(t.smallintType, 0),
        LTE(t.intType, 0),
        LTE(t.bigintType, 0),
        LTE(t.floatType, 0),
        LTE(t.doubleType, 0),
        LTE(t.decimalType, 0)), transaction), 0);
      final types.Type clone = t.clone();

      t.tinyintType.set(ADD(t.tinyintType, t.tinyintType));
      t.smallintType.set(ADD(t.smallintType, t.smallintType));
      t.intType.set(ADD(t.intType, t.intType));
      t.bigintType.set(ADD(t.bigintType, t.bigintType));
      t.floatType.set(ADD(t.floatType, t.floatType));
      t.doubleType.set(ADD(t.doubleType, t.doubleType));
      t.decimalType.set(ADD(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(Byte.valueOf((byte)(clone.tinyintType.get() + clone.tinyintType.get())), t.tinyintType.get());
      assertEquals(Short.valueOf((short)(clone.smallintType.get() + clone.smallintType.get())), t.smallintType.get());
      assertEquals(Integer.valueOf(clone.intType.get() + clone.intType.get()), t.intType.get());
      assertEquals(Long.valueOf(clone.bigintType.get() + clone.bigintType.get()), t.bigintType.get());
      assertEquals(Float.valueOf(clone.floatType.get() + clone.floatType.get()), t.floatType.get());
      assertEquals(Double.valueOf(clone.doubleType.get() + clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get().add(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testSubtract() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        GTE(t.tinyintType, 0),
        GTE(t.smallintType, 0),
        GTE(t.intType, 0),
        GTE(t.bigintType, 0),
        GTE(t.floatType, 0),
        GTE(t.doubleType, 0),
        GTE(t.decimalType, 0)), transaction), 0);
      final types.Type clone = t.clone();

      t.tinyintType.set(SUB(t.tinyintType, t.tinyintType));
      t.smallintType.set(SUB(t.smallintType, t.smallintType));
      t.intType.set(SUB(t.intType, t.intType));
      t.bigintType.set(SUB(t.bigintType, t.bigintType));
      t.floatType.set(SUB(t.floatType, t.floatType));
      t.doubleType.set(SUB(t.doubleType, t.doubleType));
      t.decimalType.set(SUB(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(Byte.valueOf((byte)(clone.tinyintType.get() - clone.tinyintType.get())), t.tinyintType.get());
      assertEquals(Short.valueOf((short)(clone.smallintType.get() - clone.smallintType.get())), t.smallintType.get());
      assertEquals(Integer.valueOf(clone.intType.get() - clone.intType.get()), t.intType.get());
      assertEquals(Long.valueOf(clone.bigintType.get() - clone.bigintType.get()), t.bigintType.get());
      assertEquals(Float.valueOf(clone.floatType.get() - clone.floatType.get()), t.floatType.get());
      assertEquals(Double.valueOf(clone.doubleType.get() - clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get().subtract(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testMultiply() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        GTE(t.tinyintType, -10), LTE(t.tinyintType, 10),
        GTE(t.smallintType, -100), LTE(t.smallintType, 100),
        GTE(t.intType, -1000), LTE(t.intType, 1000),
        GTE(t.bigintType, -10000), LTE(t.bigintType, 10000),
        IS.NOT.NULL(t.floatType),
        IS.NOT.NULL(t.doubleType),
        GTE(t.decimalType, -100000), LTE(t.decimalType, 100000)), transaction), 0);
      final types.Type clone = t.clone();

      t.tinyintType.set(MUL(t.tinyintType, t.tinyintType));
      t.smallintType.set(MUL(t.smallintType, t.smallintType));
      t.intType.set(MUL(t.intType, t.intType));
      t.bigintType.set(MUL(t.bigintType, t.bigintType));
      t.floatType.set(MUL(t.floatType, t.floatType));
      t.doubleType.set(MUL(t.doubleType, t.doubleType));
      t.decimalType.set(MUL(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(Byte.valueOf((byte)(clone.tinyintType.get() * clone.tinyintType.get())), t.tinyintType.get());
      assertEquals(Short.valueOf((short)(clone.smallintType.get() * clone.smallintType.get())), t.smallintType.get());
      assertEquals(Integer.valueOf(clone.intType.get() * clone.intType.get()), t.intType.get());
      assertEquals(Long.valueOf(clone.bigintType.get() * clone.bigintType.get()), t.bigintType.get());
      assertEquals(Float.valueOf(clone.floatType.get() * clone.floatType.get()), t.floatType.get());
      assertEquals(Double.valueOf(clone.doubleType.get() * clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get().multiply(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testDivide() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = NumericFunctionDynamicTest.getNthRow(NumericFunctionDynamicTest.selectEntity(t, AND(
        NE(t.tinyintType, 0),
        NE(t.smallintType, 0),
        NE(t.intType, 0),
        NE(t.bigintType, 0),
        NE(t.floatType, 0),
        NE(t.doubleType, 0),
        NE(t.decimalType, 0)), transaction), 0);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(DIV(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(DIV(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(DIV(t.intType, t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(DIV(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(DIV(t.floatType, t.floatType));
      t.doubleType.set(DIV(t.doubleType, t.doubleType));
      t.decimalType.set(DIV(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(Byte.valueOf((byte)(clone.tinyintType.get() / clone.tinyintType.get())), t.tinyintType.get());
      assertEquals(Short.valueOf((short)(clone.smallintType.get() / clone.smallintType.get())), t.smallintType.get());
      assertEquals(Integer.valueOf(clone.intType.get() / clone.intType.get()), t.intType.get());
      assertEquals(Long.valueOf(clone.bigintType.get() / clone.bigintType.get()), t.bigintType.get());
      assertEquals(Float.valueOf(clone.floatType.get() / clone.floatType.get()), t.floatType.get());
      assertEquals(Double.valueOf((clone.doubleType.get() / clone.doubleType.get())), t.doubleType.get());
      assertEquals(clone.decimalType.get().divide(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testUpdateVersion() throws IOException, SQLException {
    world.City c = new world.City();
    try (
      final Transaction transaction = new TestTransaction(world.class);
      final RowIterator<world.City> rows =
        SELECT(c).
        FROM(c).
        execute();
    ) {
      assertTrue(rows.nextRow());
      c = rows.nextEntity();

      assertEquals(1, UPDATE(c).execute(transaction));
      assertEquals(1, UPDATE(c).execute(transaction));

      final long version = c.version.get();
      c.version.set(0l);
      assertEquals(0, UPDATE(c).execute(transaction));

      c.version.set(version);
      assertEquals(1, UPDATE(c).execute(transaction));
    }
  }
}