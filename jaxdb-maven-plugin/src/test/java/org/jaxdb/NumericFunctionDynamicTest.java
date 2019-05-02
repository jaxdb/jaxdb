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

package org.jaxdb;

import static org.junit.Assert.*;
import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.ext.math.BigDecimals;
import org.openjax.ext.math.SafeMath;
import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.Condition;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Select.untyped.FROM;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.TestTransaction;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.vendor.DBVendor;

public abstract class NumericFunctionDynamicTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(types.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends NumericFunctionDynamicTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(types.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends NumericFunctionDynamicTest {
  }

  private static final MathContext mc = new MathContext(65, RoundingMode.HALF_UP);
  private static int rowNum = 0;

  protected static <E extends type.Entity>RowIterator<E> selectEntity(final E entity, final Condition<?> condition, final Transaction transaction) throws IOException, SQLException {
    final FROM<E> from = SELECT(entity).FROM(entity);
    return condition != null ? from.WHERE(condition).execute(transaction) : from.execute(transaction);
  }

  protected static <E extends type.Entity>RowIterator<E> selectEntity(final E entity, final Transaction transaction) throws IOException, SQLException {
    return selectEntity(entity, null, transaction);
  }

  protected static <E extends type.Entity>E getNthRow(final RowIterator<E> rows, final int rowNum) throws SQLException {
    E row = null;
    for (int i = 0; i <= rowNum && rows.nextRow(); ++i)
      row = rows.nextEntity();

    return row;
  }

  private static void testUpdateRoundN(final int n) throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(ROUND(t.tinyintType, n)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(ROUND(t.smallintType, n)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(ROUND(t.intType, n)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(ROUND(t.bigintType, n)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(ROUND(t.floatType, n));
      t.doubleType.set(ROUND(t.doubleType, n));
      t.decimalType.set(ROUND(t.decimalType, n));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : SafeMath.round(clone.tinyintType.get(), n), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : SafeMath.round(clone.smallintType.get(), n), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : SafeMath.round(clone.intType.get(), n), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : SafeMath.round(clone.bigintType.get(), n), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : SafeMath.round(clone.floatType.get(), n), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.round(clone.doubleType.get(), n), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.round(clone.decimalType.get(), n), t.decimalType.get());
    }
  }

  @Test
  public void testUpdateRound0() throws IOException, SQLException {
    testUpdateRoundN(0);
  }

  @Test
  public void testUpdateRound1() throws IOException, SQLException {
    testUpdateRoundN(1);
  }

  @Test
  public void testSign(final DBVendor vendor) throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(SIGN(t.tinyintType));
      t.smallintType.set(CAST(SIGN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(SIGN(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(SIGN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(CAST(SIGN(t.floatType)).AS.FLOAT());
      t.doubleType.set(CAST(SIGN(t.doubleType)).AS.DOUBLE());
      t.decimalType.set(CAST(SIGN(t.decimalType)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), t.decimalType.scale()));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : SafeMath.signum(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : SafeMath.signum(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : SafeMath.signum(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : SafeMath.signum(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : SafeMath.signum(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.signum(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.signum(clone.decimalType.get()), t.decimalType.get() == null ? null : t.decimalType.get().intValue());
    }
  }

  @Test
  public void testFloor() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(FLOOR(t.tinyintType));
      t.smallintType.set(FLOOR(t.smallintType));
      t.intType.set(FLOOR(t.intType));
      t.bigintType.set(FLOOR(t.bigintType));
      t.floatType.set(CAST(FLOOR(t.floatType)).AS.FLOAT());
      t.doubleType.set(CAST(FLOOR(t.doubleType)).AS.DOUBLE());
      t.decimalType.set(FLOOR(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : SafeMath.floor(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : SafeMath.floor(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : SafeMath.floor(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : SafeMath.floor(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : SafeMath.floor(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.floor(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.floor(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testCeil() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CEIL(t.tinyintType));
      t.smallintType.set(CEIL(t.smallintType));
      t.intType.set(CEIL(t.intType));
      t.bigintType.set(CEIL(t.bigintType));
      t.floatType.set(CAST(CEIL(t.floatType)).AS.FLOAT());
      t.doubleType.set(CAST(CEIL(t.doubleType)).AS.DOUBLE());
      t.decimalType.set(CEIL(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : SafeMath.ceil(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : SafeMath.ceil(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : SafeMath.ceil(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : SafeMath.ceil(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : SafeMath.ceil(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.ceil(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.ceil(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testSqrt() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, 0),
        GTE(t.smallintType, 0),
        GTE(t.intType, 0),
        GTE(t.bigintType, 0),
        GTE(t.floatType, 0),
        GTE(t.doubleType, 0),
        GTE(t.decimalType, 0)), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(SQRT(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(SQRT(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(SQRT(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(SQRT(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(SQRT(t.floatType));
      t.doubleType.set(SQRT(t.doubleType));
      t.decimalType.set(SQRT(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(Byte.valueOf((byte)SafeMath.sqrt(clone.tinyintType.get())), t.tinyintType.get());
      assertEquals(Short.valueOf((short)SafeMath.sqrt(clone.smallintType.get())), t.smallintType.get());
      assertEquals(Integer.valueOf((int)SafeMath.sqrt(clone.intType.get())), t.intType.get());
      assertEquals(Long.valueOf((long)SafeMath.sqrt(clone.bigintType.get())), t.bigintType.get());
      assertEquals(Float.valueOf((float)SafeMath.sqrt(clone.floatType.get())), t.floatType.get());
      assertEquals(Double.valueOf(SafeMath.sqrt(clone.doubleType.get())), t.doubleType.get());
      assertEquals(SafeMath.sqrt(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testSin() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(SIN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(SIN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(SIN(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(SIN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(SIN(t.floatType));
      t.doubleType.set(SIN(t.doubleType));
      t.decimalType.set(SIN(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.sin(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.sin(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.sin(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.sin(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.sin(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.sin(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.sin(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testAsin() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(ASIN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.asin(clone.tinyintType.get()), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(ASIN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.asin(clone.smallintType.get()), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(ASIN(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.asin(clone.intType.get()), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(ASIN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.asin(clone.bigintType.get()), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(ASIN(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.asin(clone.floatType.get()), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(ASIN(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.asin(clone.doubleType.get()), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(ASIN(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.asin(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testCos() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(COS(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(COS(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(COS(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(COS(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(COS(t.floatType));
      t.doubleType.set(COS(t.doubleType));
      t.decimalType.set(COS(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.cos(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.cos(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.cos(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.cos(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.cos(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.cos(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.cos(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testAcos() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(ACOS(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Byte.valueOf((byte)SafeMath.acos(clone.tinyintType.get())), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(ACOS(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Short.valueOf((short)SafeMath.acos(clone.smallintType.get())), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(ACOS(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Integer.valueOf((int)SafeMath.acos(clone.intType.get())), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(ACOS(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Long.valueOf((long)SafeMath.acos(clone.bigintType.get())), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(ACOS(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Float.valueOf((float)SafeMath.acos(clone.floatType.get())), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(ACOS(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Double.valueOf(SafeMath.acos(clone.doubleType.get())), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(ACOS(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(SafeMath.acos(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testTan() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(TAN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(TAN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(TAN(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(TAN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(TAN(t.floatType));
      t.doubleType.set(TAN(t.doubleType));
      t.decimalType.set(TAN(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.tan(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.tan(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.tan(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.tan(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.tan(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.tan(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.tan(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testAtan() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(ATAN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(ATAN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(ATAN(t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(ATAN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(ATAN(t.floatType));
      t.doubleType.set(ATAN(t.doubleType));
      t.decimalType.set(ATAN(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.atan(clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.atan(clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.atan(clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.atan(clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.atan(clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.atan(clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.atan(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  private static void testMod(final int integer) throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      final types.Type t = getNthRow(selectEntity(new types.Type(), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(MOD(t.tinyintType, integer)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(MOD(t.smallintType, integer)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(MOD(t.intType, integer)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(MOD(t.bigintType, integer)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(MOD(t.floatType, integer));
      t.doubleType.set(MOD(t.doubleType, integer));
      t.decimalType.set(MOD(t.decimalType, integer));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)(clone.tinyintType.get() % integer), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)(clone.smallintType.get() % integer), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)(clone.intType.get() % integer), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)(clone.bigintType.get() % integer), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)(clone.floatType.get() % integer), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : (double)(clone.doubleType.get() % integer), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : clone.decimalType.get().remainder(BigDecimal.valueOf(integer)), t.decimalType.get());
    }
  }

  @Test
  public void testModIntPos() throws IOException, SQLException {
    testMod(3);
  }

  @Test
  public void testModIntNeg() throws IOException, SQLException {
    testMod(-3);
  }

  @Test
  public void testModX() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        NE(t.tinyintType, 0),
        NE(t.smallintType, 0),
        NE(t.intType, 0),
        NE(t.bigintType, 0),
        NE(t.floatType, 0),
        NE(t.doubleType, 0),
        NE(t.decimalType, 0)), transaction), rowNum++);
      final types.Type clone = t.clone();

      t.tinyintType.set(CAST(MOD(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
      t.smallintType.set(CAST(MOD(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
      t.intType.set(CAST(MOD(t.intType, t.intType)).AS.INT(t.intType.precision()));
      t.bigintType.set(CAST(MOD(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));
      t.floatType.set(MOD(t.floatType, t.floatType));
      t.doubleType.set(MOD(t.doubleType, t.doubleType));
      t.decimalType.set(MOD(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));

      assertEquals(clone.tinyintType.get() == null ? null : (byte)(clone.tinyintType.get() % clone.tinyintType.get()), t.tinyintType.get());
      assertEquals(clone.smallintType.get() == null ? null : (short)(clone.smallintType.get() % clone.smallintType.get()), t.smallintType.get());
      assertEquals(clone.intType.get() == null ? null : (int)(clone.intType.get() % clone.intType.get()), t.intType.get());
      assertEquals(clone.bigintType.get() == null ? null : (long)(clone.bigintType.get() % clone.bigintType.get()), t.bigintType.get());
      assertEquals(clone.floatType.get() == null ? null : (float)(clone.floatType.get() % clone.floatType.get()), t.floatType.get());
      assertEquals(clone.doubleType.get() == null ? null : (double)(clone.doubleType.get() % clone.doubleType.get()), t.doubleType.get());
      assertEquals(clone.decimalType.get() == null ? null : clone.decimalType.get().remainder(clone.decimalType.get()), t.decimalType.get());
    }
  }

  @Test
  public void testExp() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(EXP(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.exp(clone.tinyintType.get()), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(EXP(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.exp(clone.smallintType.get()), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(EXP(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.exp(clone.intType.get()), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(EXP(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.exp(clone.bigintType.get()), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(EXP(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.exp(clone.floatType.get()), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(EXP(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.exp(clone.doubleType.get()), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(EXP(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.exp(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  private static void testPow(final int integer) throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, -1),
        NE(t.tinyintType, 0),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(POW(t.tinyintType, integer)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.pow(clone.tinyintType.get(), integer), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.smallintType, -1),
        NE(t.smallintType, 0),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(POW(t.smallintType, integer)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.pow(clone.smallintType.get(), integer), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.intType, -1),
        NE(t.intType, 0),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(POW(t.intType, integer)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.pow(clone.intType.get(), integer), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.bigintType, -1),
        NE(t.bigintType, 0),
        LTE(t.bigintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(POW(t.bigintType, integer)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.pow(clone.bigintType.get(), integer), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.floatType, -1),
        NE(t.floatType, 0),
        LTE(t.floatType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(POW(t.floatType, integer));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.pow(clone.floatType.get(), integer), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.doubleType, -1),
        NE(t.doubleType, 0),
        LTE(t.doubleType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(POW(t.doubleType, integer));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.pow(clone.doubleType.get(), integer), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.decimalType, -1),
        NE(t.decimalType, 0),
        LTE(t.decimalType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(POW(t.decimalType, integer));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.pow(clone.decimalType.get(), BigDecimal.valueOf(integer), mc), t.decimalType.get());
    }
  }

  @Test
  public void testPowIntPow() throws IOException, SQLException {
    testPow(3);
  }

  @Test
  public void testPowIntNeg() throws IOException, SQLException {
    testPow(-3);
  }

  private static void testPow2(final double value) throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GTE(t.tinyintType, -1),
        NE(t.tinyintType, 0),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(POW(value, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.pow(value, clone.tinyintType.get()), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.smallintType, -1),
        NE(t.smallintType, 0),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(POW(value, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.pow(value, clone.smallintType.get()), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.intType, -1),
        NE(t.intType, 0),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(POW(value, t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.pow(value, clone.intType.get()), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.bigintType, -1),
        NE(t.bigintType, 0),
        LTE(t.bigintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(POW(value, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.pow(value, clone.bigintType.get()), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.floatType, -1),
        NE(t.floatType, 0),
        LTE(t.floatType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(POW((float)value, t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.pow(value, clone.floatType.get()), t.floatType.get(), 0.000001);

      t = getNthRow(selectEntity(t, AND(
        GTE(t.doubleType, -1),
        NE(t.doubleType, 0),
        LTE(t.doubleType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(POW(value, t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.pow(value, clone.doubleType.get()), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GTE(t.decimalType, -1),
        NE(t.decimalType, 0),
        LTE(t.decimalType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(POW(value, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.pow(BigDecimal.valueOf(value), clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testPow3X() throws IOException, SQLException {
    testPow2(.2);
  }

  @Test
  public void testPowXX() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 1)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(POW(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.pow(clone.tinyintType.get(), clone.tinyintType.get()), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(POW(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.pow(clone.smallintType.get(), clone.smallintType.get()), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 0),
        LTE(t.intType, 1)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(POW(t.intType, t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.pow(clone.intType.get(), clone.intType.get()), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(POW(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.pow(clone.bigintType.get(), clone.bigintType.get()), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(POW(t.floatType, t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.pow(clone.floatType.get(), clone.floatType.get()), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(POW(t.doubleType, t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.pow(clone.doubleType.get(), clone.doubleType.get()), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(POW(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.pow(clone.decimalType.get(), clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLogX3() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 1),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LOG(t.tinyintType, 3)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.log(clone.tinyintType.get(), 3), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 1),
        LTE(t.smallintType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LOG(t.smallintType, 3)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.log(clone.smallintType.get(), 3), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 1),
        LTE(t.intType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LOG(t.intType, 3)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.log(clone.intType.get(), 3), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 1),
        LTE(t.bigintType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LOG(t.bigintType, 3)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.log(clone.bigintType.get(), 3), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
          GT(t.floatType, 1),
          LTE(t.floatType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LOG(t.floatType, 3));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.log(clone.floatType.get(), 3), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
          GT(t.doubleType, 1),
          LTE(t.doubleType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LOG(t.doubleType, 3));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.log(clone.doubleType.get(), 3), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
          GT(t.decimalType, 1),
          LTE(t.decimalType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LOG(t.decimalType, 3));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.log(clone.decimalType.get(), BigDecimal.valueOf(3), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLog3X() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LOG(3, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.tinyintType.get() == null ? null : (byte)SafeMath.log(3, clone.tinyintType.get()), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LOG(3, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.smallintType.get() == null ? null : (short)SafeMath.log(3, clone.smallintType.get()), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 0),
        LTE(t.intType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LOG(3, t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.intType.get() == null ? null : (int)SafeMath.log(3, clone.intType.get()), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LOG(3, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.bigintType.get() == null ? null : (long)SafeMath.log(3, clone.bigintType.get()), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LOG(3, t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.floatType.get() == null ? null : (float)SafeMath.log(3, clone.floatType.get()), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LOG(3, t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.doubleType.get() == null ? null : SafeMath.log(3, clone.doubleType.get()), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LOG(3, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(clone.decimalType.get() == null ? null : SafeMath.log(BigDecimal.valueOf(3), clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLogXX() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 1),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LOG(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Byte.valueOf((byte)SafeMath.log(clone.tinyintType.get(), clone.tinyintType.get())), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 1),
        LTE(t.smallintType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LOG(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Short.valueOf((short)SafeMath.log(clone.smallintType.get(), clone.smallintType.get())), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 1),
        LTE(t.intType, 1000)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LOG(t.intType, t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Integer.valueOf((int)SafeMath.log(clone.intType.get(), clone.intType.get())), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 1),
        LTE(t.bigintType, 10000)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LOG(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Long.valueOf((long)SafeMath.log(clone.bigintType.get(), clone.bigintType.get())), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 1),
        LTE(t.floatType, 100000)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LOG(t.floatType, t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Float.valueOf((float)SafeMath.log(clone.floatType.get(), clone.floatType.get())), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 1),
        LTE(t.doubleType, 1000000)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LOG(t.doubleType, t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Double.valueOf(SafeMath.log(clone.doubleType.get(), clone.doubleType.get())), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 1),
        LTE(t.decimalType, 10000000)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LOG(t.decimalType, t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(SafeMath.log(clone.decimalType.get(), clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLn() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Byte.valueOf((byte)SafeMath.log(clone.tinyintType.get())), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Short.valueOf((short)SafeMath.log(clone.smallintType.get())), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LN(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Integer.valueOf((int)SafeMath.log(clone.intType.get())), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Long.valueOf((long)SafeMath.log(clone.bigintType.get())), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LN(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Float.valueOf((float)SafeMath.log(clone.floatType.get())), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LN(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Double.valueOf(SafeMath.log(clone.doubleType.get())), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LN(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(SafeMath.log(clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLog2() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LOG2(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Byte.valueOf((byte)SafeMath.log(2, clone.tinyintType.get())), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LOG2(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Short.valueOf((short)SafeMath.log(2, clone.smallintType.get())), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LOG2(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Integer.valueOf((int)SafeMath.log(2, clone.intType.get())), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LOG2(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Long.valueOf((long)SafeMath.log(2, clone.bigintType.get())), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LOG2(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Float.valueOf((float)SafeMath.log(2, clone.floatType.get())), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LOG2(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      final double expected = Double.valueOf(SafeMath.log(2, clone.doubleType.get()));
      assertEquals(expected, t.doubleType.get(), 10 * Math.ulp(expected));

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LOG2(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(SafeMath.log(BigDecimals.TWO, clone.decimalType.get(), mc), t.decimalType.get());
    }
  }

  @Test
  public void testLog10() throws IOException, SQLException {
    try (final Transaction transaction = new TestTransaction(types.class)) {
      types.Type t = new types.Type();
      t = getNthRow(selectEntity(t, AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)), transaction), rowNum++);
      types.Type clone = t.clone();

      t.tinyintType.set(CAST(LOG10(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Byte.valueOf((byte)SafeMath.log(10, clone.tinyintType.get())), t.tinyintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)), transaction), rowNum++);
      clone = t.clone();

      t.smallintType.set(CAST(LOG10(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Short.valueOf((short)SafeMath.log(10, clone.smallintType.get())), t.smallintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)), transaction), rowNum++);
      clone = t.clone();

      t.intType.set(CAST(LOG10(t.intType)).AS.INT(t.intType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Integer.valueOf((int)SafeMath.log(10, clone.intType.get())), t.intType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)), transaction), rowNum++);
      clone = t.clone();

      t.bigintType.set(CAST(LOG10(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Long.valueOf((long)SafeMath.log(10, clone.bigintType.get())), t.bigintType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)), transaction), rowNum++);
      clone = t.clone();

      t.floatType.set(LOG10(t.floatType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Float.valueOf((float)SafeMath.log(10, clone.floatType.get())), t.floatType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)), transaction), rowNum++);
      clone = t.clone();

      t.doubleType.set(LOG10(t.doubleType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(Double.valueOf(SafeMath.log(10, clone.doubleType.get())), t.doubleType.get());

      t = getNthRow(selectEntity(t, AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)), transaction), rowNum++);
      clone = t.clone();

      t.decimalType.set(LOG10(t.decimalType));

      assertEquals(1, UPDATE(t).execute(transaction));
      assertEquals(SafeMath.log(BigDecimal.TEN, clone.decimalType.get(), mc), t.decimalType.get());
    }
  }
}