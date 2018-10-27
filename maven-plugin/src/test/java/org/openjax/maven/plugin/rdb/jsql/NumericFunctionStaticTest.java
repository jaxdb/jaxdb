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

package org.openjax.maven.plugin.rdb.jsql;

import static org.junit.Assert.*;
import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;

import org.fastjax.math.SafeMath;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.Select;
import org.openjax.rdb.jsql.classicmodels;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.jsql.types;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class NumericFunctionStaticTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends NumericFunctionStaticTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends NumericFunctionStaticTest {
  }

  private static final Logger logger = LoggerFactory.getLogger(NumericFunctionStaticTest.class);

  private static Select.untyped.SELECT<type.Subject<?>> selectVicinity(final double latitude, final double longitude, final double distance, final int limit) {
    final classicmodels.Customer c = new classicmodels.Customer();
    final type.DECIMAL d = c.longitude.clone();

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
  public void testVicinity() throws IOException, SQLException {
    try (final RowIterator<? extends type.Subject<?>> rows = selectVicinity(37.78536811469731, -122.3931884765625, 10, 1).execute()) {
      while (rows.nextRow()) {
        final classicmodels.Customer c = (classicmodels.Customer)rows.nextEntity();
        assertEquals("Mini Wheels Co.", c.companyName.get());
        final type.DECIMAL d = (type.DECIMAL)rows.nextEntity();
        assertEquals(Double.valueOf(2.22069), d.get().doubleValue(), 0.00001);
      }
    }
  }

  @Test
  public void testRound0() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        t.doubleType,
        ROUND(t.doubleType, 0)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.round(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testRound1() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        t.doubleType,
        ROUND(t.doubleType, 1)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = SafeMath.round(rows.nextEntity().get().doubleValue(), 1);
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSign() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        SIGN(t.doubleType)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(Math.signum(rows.nextEntity().get().doubleValue()), rows.nextEntity().get().intValue(), 0);
    }
  }

  @Test
  public void testFloor() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        FLOOR(t.doubleType)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.floor(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testCeil() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        CEIL(t.doubleType)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.doubleType)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.ceil(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSqrt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        SQRT(t.doubleType)).
      FROM(t).
      WHERE(GT(t.doubleType, 10)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.sqrt(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testSin() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        SIN(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.sin(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAsin() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        ASIN(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.asin(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testCos() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        COS(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.cos(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAcos() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        ACOS(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.acos(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testTan() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        TAN(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.tan(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testAtan() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        ATAN(t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 1))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.atan(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testModInt1() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.intType,
        MOD(t.intType, 3)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.intType)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().get().intValue() % 3, rows.nextEntity().get().intValue());
    }
  }

  @Test
  public void testModInt2() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.intType,
        MOD(t.intType, -3)).
      FROM(t).
      WHERE(IS.NOT.NULL(t.intType)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().get().intValue() % -3, rows.nextEntity().get().intValue());
    }
  }

  @Test
  public void testModInt3() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        t.intType,
        MOD(t.doubleType, t.intType)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), NE(t.intType, 0))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().get().intValue() % rows.nextEntity().get().intValue(), rows.nextEntity().get().intValue());
    }
  }


  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testModDouble1() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        MOD(t.doubleType, 1.2)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = rows.nextEntity().get().doubleValue() % 1.2;
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 1000);
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testModDouble2() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        MOD(t.doubleType, -1.2)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = rows.nextEntity().get().doubleValue() % -1.2;
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 1000);
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testModDouble3() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        t.floatType,
        MOD(t.doubleType, t.floatType)).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), GT(ABS(t.floatType), 10), LT(ABS(t.floatType), 100), GT(ABS(t.doubleType), 10), LT(ABS(t.doubleType), 100))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      // FIXME: Is there something wrong with DMOD() for Derby?
      final double expected = rows.nextEntity().get().doubleValue() % rows.nextEntity().get().floatValue();
      final double actual = rows.nextEntity().get().doubleValue();
      if (Math.abs(expected - actual) > 0.000001)
        logger.warn("Math.abs(expected - actual) > 0.000001: " + Math.abs(expected - actual));

      assertEquals(expected, actual, 0.003);
    }
  }

  @Test
  public void testExp() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        EXP(MUL(t.doubleType, -1))).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.exp(-rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPowX3() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        POW(t.doubleType, 3)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 10))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.pow(rows.nextEntity().get().doubleValue(), 3);
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPow3X() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        POW(3, MUL(t.doubleType, -1))).
      FROM(t).
      WHERE(AND(IS.NOT.NULL(t.doubleType), LT(ABS(t.doubleType), 100))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.pow(3, -rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testPowXX() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        t.doubleType,
        POW(t.doubleType, t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.doubleType, 0), LT(t.doubleType, 10))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.pow(rows.nextEntity().get().doubleValue(), rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog3X() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        LOG(3, t.doubleType)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log(rows.nextEntity().get().doubleValue()) / Math.log(3);
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLogX3() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        LOG(t.doubleType, 3)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log(3) / Math.log(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLogXX() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        t.intType,
        LOG(t.intType, t.doubleType)).
      FROM(t).
      WHERE(AND(GT(t.intType, 1), GT(t.doubleType, 0), GT(t.doubleType, 1), LT(t.doubleType, 10))).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log(rows.nextEntity().get().doubleValue()) / Math.log(rows.nextEntity().get().intValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLn() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        LN(t.doubleType)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog2() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        LOG2(t.doubleType)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log(rows.nextEntity().get().doubleValue()) / Math.log(2);
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }

  @Test
  public void testLog10() throws IOException,   SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        t.doubleType,
        LOG10(t.doubleType)).
      FROM(t).
      WHERE(GT(t.doubleType, 0)).
      LIMIT(1).
      execute()) {
      assertTrue(rows.nextRow());
      final double expected = Math.log10(rows.nextEntity().get().doubleValue());
      assertEquals(expected, rows.nextEntity().get().doubleValue(), Math.ulp(expected) * 100);
    }
  }
}