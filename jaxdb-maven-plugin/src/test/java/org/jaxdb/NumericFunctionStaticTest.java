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
import java.sql.SQLException;

import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.math.SafeMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(SchemaTestRunner.class)
public abstract class NumericFunctionStaticTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends NumericFunctionStaticTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends NumericFunctionStaticTest {
  }

  private static final Logger logger = LoggerFactory.getLogger(NumericFunctionStaticTest.class);

  private static Select.untyped.SELECT<type.Entity> selectVicinity(final double latitude, final double longitude, final double distance, final int limit) {
    final classicmodels.Customer c = classicmodels.Customer();
    final data.DOUBLE d = new data.DOUBLE();

    return SELECT(c, MUL(3959 * 2, ATAN2(
      SQRT(ADD(
        POW(SIN(DIV(MUL(SUB(c.latitude, latitude), PI()), 360)), 2),
        MUL(MUL(
          COS(DIV(MUL(c.latitude, PI()), 180)),
          COS(DIV(MUL(latitude, PI()), 180))),
          POW(SIN(DIV(MUL(SUB(c.longitude, longitude), PI()), 360)), 2)))),
      SQRT(ADD(
        SUB(1, POW(SIN(DIV(MUL(SUB(c.latitude, latitude), PI()), 360)), 2)),
        MUL(MUL(
          COS(DIV(MUL(latitude, PI()), 180)),
          COS(DIV(MUL(c.latitude, PI()), 180))),
          POW(SIN(DIV(MUL(SUB(c.longitude, longitude), PI()), 360)), 2)))))).
        AS(d)).
      FROM(c).
      GROUP_BY(c).
      HAVING(LT(d, distance)).
      ORDER_BY(DESC(d)).
      LIMIT(limit);
  }

  @Test
  public void testVicinity(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try (final RowIterator<type.Entity> rows = selectVicinity(37.78536811469731, -122.3931884765625, 10, 1)
      .execute(transaction)) {
      while (rows.nextRow()) {
        final classicmodels.Customer c = (classicmodels.Customer)rows.nextEntity();
        assertEquals("Mini Wheels Co.", c.companyName.get());
        final data.DOUBLE d = (data.DOUBLE)rows.nextEntity();
        assertEquals(2.22069, d.get().doubleValue(), 0.00001);
      }
    }
  }

  @Test
  public void testRound0(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        ROUND(t.doubleType, 0).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = Math.round(a.get());
      assertEquals(expected, b.get(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testRound1(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        ROUND(t.doubleType, 1).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.round(a.get(), 1);
      assertEquals(expected, b.get(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSign(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        SIGN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertEquals(Math.signum(a.get().doubleValue()), b.get().intValue(), 0);
    }
  }

  @Test
  public void testFloor(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        FLOOR(t.doubleType).AS(b)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = Math.floor(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testCeil(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        CEIL(t.doubleType).AS(b)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = Math.ceil(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSqrt(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        SQRT(t.doubleType).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = Math.sqrt(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testDegrees(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        DEGREES(t.doubleType).AS(b)).
      FROM(t).
      WHERE(NE(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.toDegrees(a.get().doubleValue());
      assertEquals(a.get().toString(), expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testRadians(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        RADIANS(t.doubleType).AS(b)).
      FROM(t).
      WHERE(NE(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.toRadians(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSin(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        SIN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.sin(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAsin(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        ASIN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.asin(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testCos(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        COS(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.cos(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAcos(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        ACOS(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.acos(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testTan(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        TAN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.tan(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAtan(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        ATAN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.atan(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testModInt1(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.INT a = new data.INT();
    final data.INT b = new data.INT();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.intType.AS(a),
        MOD(t.intType, 3).AS(b)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.intType)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertEquals(a.get().intValue() % 3, b.get().intValue());
    }
  }

  @Test
  public void testModInt2(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.INT a = new data.INT();
    final data.INT b = new data.INT();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.intType.AS(a),
        MOD(t.intType, -3).AS(b)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.intType)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertEquals(a.get().intValue() % -3, b.get().intValue());
    }
  }

  @Test
  public void testModInt3(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.INT b = new data.INT();
    final data.DOUBLE c = new data.DOUBLE();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.doubleType.AS(a),
        t.intType.AS(b),
        MOD(t.doubleType, t.intType).AS(c)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), NE(t.intType, 0))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertSame(c, rows.nextEntity());
      assertEquals(a.get().intValue() % b.get().intValue(), c.get().intValue());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported(SQLite.class)
  public void testModDouble1(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        MOD(t.doubleType, 1.2).AS(b)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = a.get().doubleValue() % 1.2;
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 1000);
    }
  }

  @Test
  @SchemaTestRunner.Unsupported(SQLite.class)
  public void testModDouble2(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        MOD(t.doubleType, -1.2).AS(b)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = a.get().doubleValue() % -1.2;
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 1000);
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testModDouble3(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.FLOAT b = new data.FLOAT();
    final data.DOUBLE c = new data.DOUBLE();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.doubleType.AS(a),
        t.floatType.AS(b),
        MOD(t.doubleType, t.floatType).AS(c)).
      FROM(t).
      WHERE(AND(
        IS.NOT.NULL(t.doubleType),
        GT(ABS(t.floatType), 10),
        LT(ABS(t.floatType), 100),
        GT(ABS(t.doubleType), 10),
        LT(ABS(t.doubleType), 100))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertSame(c, rows.nextEntity());
      // FIXME: Is there something wrong with DMOD() for Derby?
      final double expected = a.get().doubleValue() % b.get().floatValue();
      final double actual = c.get().doubleValue();
      if (Math.abs(expected - actual) > 0.000001 && logger.isWarnEnabled()) logger.warn("Math.abs(expected - actual) > 0.000001: " + Math.abs(expected - actual));
      assertEquals(expected, actual, 0.003);
    }
  }

  @Test
  public void testExp(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        EXP(MUL(t.doubleType, -1)).AS(b)).
      FROM(t).
      WHERE(AND(
        IS.NOT.NULL(t.doubleType),
        LT(ABS(t.doubleType), 100))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.exp(-a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPowX3(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        POW(t.doubleType, 3).AS(b)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 10))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.pow(a.get().doubleValue(), 3);
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPow3X(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        POW(3, MUL(t.doubleType, -1)).AS(b)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.pow(3, -a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPowXX(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    final data.DOUBLE c = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        t.doubleType.AS(b),
        POW(t.doubleType, t.doubleType).AS(c)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 10))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertSame(c, rows.nextEntity());
      final double expected = StrictMath.pow(a.get().doubleValue(), b.get().doubleValue());
      assertEquals(expected, c.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog3X(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        LOG(3, t.doubleType).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.log(3, a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLogX3(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.FLOAT b = new data.FLOAT();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.doubleType.AS(a),
        LOG(t.doubleType, 3).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.log(a.get().doubleValue(), 3);
      assertEquals(expected, b.get().doubleValue(), 0.00001);
    }
  }

  @Test
  public void testLogXX(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.INT b = new data.INT();
    final data.DOUBLE c = new data.DOUBLE();
    try (final RowIterator<? extends data.Numeric<?>> rows =
      SELECT(
        t.doubleType.AS(a),
        t.intType.AS(b),
        LOG(t.intType, t.doubleType).AS(c)).
      FROM(t).
      WHERE(AND(GT(t.intType, 1), GT(t.doubleType, 0), GT(t.doubleType, 1), LT(t.doubleType, 10))).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      assertSame(c, rows.nextEntity());
      final double expected = StrictMath.log(a.get().doubleValue()) / StrictMath.log(b.get().intValue());
      assertEquals(expected, c.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLn(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        LN(t.doubleType).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.log(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog2(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        LOG2(t.doubleType).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = SafeMath.log2(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog10(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final data.DOUBLE a = new data.DOUBLE();
    final data.DOUBLE b = new data.DOUBLE();
    try (final RowIterator<data.DOUBLE> rows =
      SELECT(
        t.doubleType.AS(a),
        LOG10(t.doubleType).AS(b)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1)
        .execute(transaction)) {
      assertTrue(rows.nextRow());
      assertSame(a, rows.nextEntity());
      assertSame(b, rows.nextEntity());
      final double expected = StrictMath.log10(a.get().doubleValue());
      assertEquals(expected, b.get().doubleValue(), Math.ulp(expected) * 100);
    }
  }
}