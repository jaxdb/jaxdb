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
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.SQLException;

import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
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
import org.libj.math.SafeMath;

@RunWith(SchemaTestRunner.class)
public abstract class NumericFunctionDynamicTest {
  @DB(Derby.class)
  @DB(SQLite.class)
  public static class IntegrationTest extends NumericFunctionDynamicTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends NumericFunctionDynamicTest {
  }

  public static void assertBigDecimalEquals(final BigDecimal expected, final BigDecimal actual) {
    assertEquals(0, expected.compareTo(actual));
  }

  private static final MathContext mc = new MathContext(65, RoundingMode.DOWN);
  private int rowNum;

  static <D extends data.Table>D getNthRow(final int rowNum, final RowIterator<D> rows) throws SQLException {
    D row = null;
    for (int i = 0; i <= rowNum && rows.nextRow(); ++i) // [I]
      row = rows.nextEntity();

    return row;
  }

  private void testUpdateRoundN(final Types types, final Transaction transaction, final int n) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(ROUND(t.tinyintType, n)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(ROUND(t.smallintType, n)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(ROUND(t.intType, n)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(ROUND(t.bigintType, n)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(ROUND(t.floatType, n));
    t.doubleType.set(ROUND(t.doubleType, n));
    t.decimalType.set(ROUND(t.decimalType, n));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : SafeMath.round(clone.tinyintType.getAsByte(), n), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : SafeMath.round(clone.smallintType.getAsShort(), n), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : SafeMath.round(clone.intType.getAsInt(), n), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : SafeMath.round(clone.bigintType.getAsLong(), n), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : SafeMath.round(clone.floatType.getAsFloat(), n), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.round(clone.doubleType.getAsDouble(), n), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.round(clone.decimalType.get(), n), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testUpdateRound0(final Types types, final Transaction transaction) throws IOException, SQLException {
    testUpdateRoundN(types, transaction, 0);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testUpdateRound1(final Types types, final Transaction transaction) throws IOException, SQLException {
    testUpdateRoundN(types, transaction, 1);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testSign(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(SIGN(t.tinyintType));
    t.smallintType.set(CAST(SIGN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(SIGN(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(SIGN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(CAST(SIGN(t.floatType)).AS.FLOAT());
    t.doubleType.set(CAST(SIGN(t.doubleType)).AS.DOUBLE());
    t.decimalType.set(CAST(SIGN(t.decimalType)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), t.decimalType.scale()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : SafeMath.signum(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.signum(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.signum(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.signum(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : Math.signum(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : Math.signum(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertEquals(clone.decimalType.isNull() ? null : SafeMath.signum(clone.decimalType.get()), t.decimalType.isNull() ? null : t.decimalType.get().byteValue());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testFloor(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(FLOOR(t.tinyintType));
    t.smallintType.set(FLOOR(t.smallintType));
    t.intType.set(FLOOR(t.intType));
    t.bigintType.set(FLOOR(t.bigintType));
    t.floatType.set(CAST(FLOOR(t.floatType)).AS.FLOAT());
    t.doubleType.set(CAST(FLOOR(t.doubleType)).AS.DOUBLE());
    t.decimalType.set(FLOOR(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : SafeMath.floor(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : SafeMath.floor(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : SafeMath.floor(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : SafeMath.floor(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : SafeMath.floor(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.floor(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.floor(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testCeil(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CEIL(t.tinyintType));
    t.smallintType.set(CEIL(t.smallintType));
    t.intType.set(CEIL(t.intType));
    t.bigintType.set(CEIL(t.bigintType));
    t.floatType.set(CAST(CEIL(t.floatType)).AS.FLOAT());
    t.doubleType.set(CAST(CEIL(t.doubleType)).AS.DOUBLE());
    t.decimalType.set(CEIL(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : SafeMath.ceil(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : SafeMath.ceil(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : SafeMath.ceil(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : SafeMath.ceil(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : SafeMath.ceil(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.ceil(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.ceil(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testSqrt(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

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

    t.tinyintType.set(CAST(SQRT(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(SQRT(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(SQRT(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(SQRT(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(SQRT(t.floatType));
    t.doubleType.set(SQRT(t.doubleType));
    t.decimalType.set(SQRT(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.sqrt(clone.tinyintType.getAsByte())), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(Short.valueOf((short)SafeMath.sqrt(clone.smallintType.getAsShort())), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(Integer.valueOf((int)SafeMath.sqrt(clone.intType.getAsInt())), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(Long.valueOf((long)SafeMath.sqrt(clone.bigintType.getAsLong())), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(Float.valueOf((float)SafeMath.sqrt(clone.floatType.getAsFloat())), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(Double.valueOf(SafeMath.sqrt(clone.doubleType.getAsDouble())), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(SafeMath.sqrt(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testDegrees(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        LT(ABS(t.tinyintType), MUL(PI(), 127d / 360d)),
        LT(ABS(t.smallintType), MUL(PI(), 50)),
        LT(ABS(t.intType), MUL(PI(), 100)),
        LT(ABS(t.bigintType), MUL(PI(), 1000000000)),
        NE(t.floatType, 0),
        NE(t.doubleType, 0),
        NE(t.decimalType, 0)))
          .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(DEGREES(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(DEGREES(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(DEGREES(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(DEGREES(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(DEGREES(t.floatType));
    t.doubleType.set(DEGREES(t.doubleType));
    t.decimalType.set(DEGREES(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.toDegrees(clone.tinyintType.getAsByte())), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(Short.valueOf((short)SafeMath.toDegrees(clone.smallintType.getAsShort())), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(Integer.valueOf((int)SafeMath.toDegrees(clone.intType.getAsInt())), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(Long.valueOf((long)SafeMath.toDegrees(clone.bigintType.getAsLong())), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(Float.valueOf((float)SafeMath.toDegrees(clone.floatType.getAsFloat())), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(Double.valueOf(SafeMath.toDegrees(clone.doubleType.getAsDouble())), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(SafeMath.toDegrees(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testRadians(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

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

    t.tinyintType.set(CAST(RADIANS(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(RADIANS(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(RADIANS(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(RADIANS(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(RADIANS(t.floatType));
    t.doubleType.set(RADIANS(t.doubleType));
    t.decimalType.set(RADIANS(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.toRadians(clone.tinyintType.getAsByte())), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(Short.valueOf((short)SafeMath.toRadians(clone.smallintType.getAsShort())), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(Integer.valueOf((int)SafeMath.toRadians(clone.intType.getAsInt())), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(Long.valueOf((long)SafeMath.toRadians(clone.bigintType.getAsLong())), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(Float.valueOf((float)SafeMath.toRadians(clone.floatType.getAsFloat())), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(Double.valueOf(SafeMath.toRadians(clone.doubleType.getAsDouble())), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(SafeMath.toRadians(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testSin(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(SIN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(SIN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(SIN(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(SIN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(SIN(t.floatType));
    t.doubleType.set(SIN(t.doubleType));
    t.decimalType.set(SIN(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.sin(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.sin(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.sin(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.sin(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.sin(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.sin(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.sin(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testAsin(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(ASIN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.asin(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(ASIN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.asin(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(ASIN(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.asin(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(ASIN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.asin(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(ASIN(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.asin(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(ASIN(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.asin(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(ASIN(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.asin(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testCos(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(COS(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(COS(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(COS(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(COS(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(COS(t.floatType));
    t.doubleType.set(COS(t.doubleType));
    t.decimalType.set(COS(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.cos(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.cos(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.cos(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.cos(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.cos(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.cos(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.cos(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testAcos(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(ACOS(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : Byte.valueOf((byte)SafeMath.acos(clone.tinyintType.getAsByte())), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(ACOS(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : Short.valueOf((short)SafeMath.acos(clone.smallintType.getAsShort())), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(ACOS(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : Integer.valueOf((int)SafeMath.acos(clone.intType.getAsInt())), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(ACOS(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : Long.valueOf((long)SafeMath.acos(clone.bigintType.getAsLong())), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(ACOS(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : Float.valueOf((float)SafeMath.acos(clone.floatType.getAsFloat())), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(ACOS(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : Double.valueOf(SafeMath.acos(clone.doubleType.getAsDouble())), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(ACOS(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(SafeMath.acos(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testTan(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(TAN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(TAN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(TAN(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(TAN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(TAN(t.floatType));
    t.doubleType.set(TAN(t.doubleType));
    t.decimalType.set(TAN(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.tan(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.tan(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.tan(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.tan(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.tan(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.tan(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.tan(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testAtan(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(ATAN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(ATAN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(ATAN(t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(ATAN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(ATAN(t.floatType));
    t.doubleType.set(ATAN(t.doubleType));
    t.decimalType.set(ATAN(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.atan(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.atan(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.atan(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.atan(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.atan(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : SafeMath.atan(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.atan(clone.decimalType.get(), mc), t.decimalType.get());
  }

  private void testMod(final Types types, final Transaction transaction, final int integer) throws IOException, SQLException {
    final Types.Type t = getNthRow(rowNum++,

      SELECT(types.Type())
        .execute(transaction));

    final Types.Type clone = t.clone();

    t.tinyintType.set(CAST(MOD(t.tinyintType, integer)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(MOD(t.smallintType, integer)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(MOD(t.intType, integer)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(MOD(t.bigintType, integer)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(MOD(t.floatType, integer));
    t.doubleType.set(MOD(t.doubleType, integer));
    t.decimalType.set(MOD(t.decimalType, integer));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)(clone.tinyintType.getAsByte() % integer), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)(clone.smallintType.getAsShort() % integer), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : clone.intType.getAsInt() % integer, t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : clone.bigintType.getAsLong() % integer, t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : clone.floatType.getAsFloat() % integer, t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : clone.doubleType.getAsDouble() % integer, t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : clone.decimalType.get().remainder(BigDecimal.valueOf(integer)), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testModIntPos(final Types types, final Transaction transaction) throws IOException, SQLException {
    testMod(types, transaction, 3);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testModIntNeg(final Types types, final Transaction transaction) throws IOException, SQLException {
    testMod(types, transaction, -3);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testModX(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

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

    t.tinyintType.set(CAST(MOD(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));
    t.smallintType.set(CAST(MOD(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));
    t.intType.set(CAST(MOD(t.intType, t.intType)).AS.INT(t.intType.precision()));
    t.bigintType.set(CAST(MOD(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));
    t.floatType.set(MOD(t.floatType, t.floatType));
    t.doubleType.set(MOD(t.doubleType, t.doubleType));
    t.decimalType.set(MOD(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)(clone.tinyintType.getAsByte() % clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));
    assertEquals(clone.smallintType.isNull() ? null : (short)(clone.smallintType.getAsShort() % clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));
    assertEquals(clone.intType.isNull() ? null : (clone.intType.getAsInt() % clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));
    assertEquals(clone.bigintType.isNull() ? null : (clone.bigintType.getAsLong() % clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));
    assertEquals(clone.floatType.isNull() ? null : (clone.floatType.getAsFloat() % clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));
    assertEquals(clone.doubleType.isNull() ? null : (clone.doubleType.getAsDouble() % clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));
    assertBigDecimalEquals(clone.decimalType.isNull() ? null : clone.decimalType.get().remainder(clone.decimalType.get()), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testExp(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -1),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(EXP(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.exp(clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.smallintType, -1),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(EXP(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.exp(clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.intType, -1),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(EXP(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.exp(clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.bigintType, -1),
        LTE(t.bigintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(EXP(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.exp(clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.floatType, -1),
        LTE(t.floatType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(EXP(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.exp(clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.doubleType, -1),
        LTE(t.doubleType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(EXP(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.exp(clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.decimalType, -1),
        LTE(t.decimalType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(EXP(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.exp(clone.decimalType.get(), mc), t.decimalType.get());
  }

  private void testPow(final Types types, final Transaction transaction, final int integer) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -1),
        NE(t.tinyintType, 0),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(POW(t.tinyintType, integer)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.pow(clone.tinyintType.getAsByte(), integer), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.smallintType, -1),
        NE(t.smallintType, 0),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(POW(t.smallintType, integer)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.pow(clone.smallintType.getAsShort(), integer), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.intType, -1),
        NE(t.intType, 0),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(POW(t.intType, integer)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.pow(clone.intType.getAsInt(), integer), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.bigintType, -1),
        NE(t.bigintType, 0),
        LTE(t.bigintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(POW(t.bigintType, integer)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.pow(clone.bigintType.getAsLong(), integer), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.floatType, -1),
        NE(t.floatType, 0),
        LTE(t.floatType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(POW(t.floatType, integer));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.pow(clone.floatType.getAsFloat(), integer), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.doubleType, -1),
        NE(t.doubleType, 0),
        LTE(t.doubleType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(POW(t.doubleType, integer));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.pow(clone.doubleType.getAsDouble(), integer), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.decimalType, -1),
        NE(t.decimalType, 0),
        LTE(t.decimalType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(POW(t.decimalType, integer));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.pow(clone.decimalType.get(), BigDecimal.valueOf(integer), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testPowIntPow(final Types types, final Transaction transaction) throws IOException, SQLException {
    testPow(types, transaction, 3);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testPowIntNeg(final Types types, final Transaction transaction) throws IOException, SQLException {
    testPow(types, transaction, -3);
  }

  private void testPow2(final Types types, final Transaction transaction, final double value) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.tinyintType, -1),
        NE(t.tinyintType, 0),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(POW(value, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.pow(value, clone.tinyintType.getAsByte()), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.smallintType, -1),
        NE(t.smallintType, 0),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(POW(value, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.pow(value, clone.smallintType.getAsShort()), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.intType, -1),
        NE(t.intType, 0),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(POW(value, t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.pow(value, clone.intType.getAsInt()), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.bigintType, -1),
        NE(t.bigintType, 0),
        LTE(t.bigintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(POW(value, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.pow(value, clone.bigintType.getAsLong()), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.floatType, -1),
        NE(t.floatType, 0),
        LTE(t.floatType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(POW((float)value, t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.pow(value, clone.floatType.getAsFloat()), Float.valueOf(t.floatType.getAsFloat()), 0.000001);

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.doubleType, -1),
        NE(t.doubleType, 0),
        LTE(t.doubleType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(POW(value, t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.pow(value, clone.doubleType.getAsDouble()), Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GTE(t.decimalType, -1),
        NE(t.decimalType, 0),
        LTE(t.decimalType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(CAST(POW(value, t.decimalType)).AS.DECIMAL());

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.pow(BigDecimal.valueOf(value), clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testPow3X(final Types types, final Transaction transaction) throws IOException, SQLException {
    testPow2(types, transaction, .2);
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testPowXX(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 1)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(POW(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.pow(clone.tinyintType.getAsByte(), clone.tinyintType.getAsByte()), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(POW(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.pow(clone.smallintType.getAsShort(), clone.smallintType.getAsShort()), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 0),
        LTE(t.intType, 1)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(POW(t.intType, t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.pow(clone.intType.getAsInt(), clone.intType.getAsInt()), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(POW(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.pow(clone.bigintType.getAsLong(), clone.bigintType.getAsLong()), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(POW(t.floatType, t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.pow(clone.floatType.getAsFloat(), clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(POW(t.doubleType, t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.pow(clone.doubleType.getAsDouble(), clone.doubleType.getAsDouble()), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(POW(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.pow(clone.decimalType.get(), clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLogX3(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 1),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LOG(t.tinyintType, 3)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.log(clone.tinyintType.getAsByte(), 3), t.tinyintType.isNull() ? null : Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 1),
        LTE(t.smallintType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LOG(t.smallintType, 3)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.log(clone.smallintType.getAsShort(), 3), t.smallintType.isNull() ? null : Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 1),
        LTE(t.intType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LOG(t.intType, 3)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.log(clone.intType.getAsInt(), 3), t.intType.isNull() ? null : Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 1),
        LTE(t.bigintType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LOG(t.bigintType, 3)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.log(clone.bigintType.getAsLong(), 3), t.bigintType.isNull() ? null : Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 1),
        LTE(t.floatType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LOG(t.floatType, 3));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.log(clone.floatType.getAsFloat(), 3), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 1),
        LTE(t.doubleType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LOG(t.doubleType, (long)3));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.log(clone.doubleType.getAsDouble(), 3), t.doubleType.isNull() ? null : Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 1),
        LTE(t.decimalType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(CAST(LOG(t.decimalType, 3)).AS.DECIMAL());

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.log(clone.decimalType.get(), BigDecimal.valueOf(3), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLog3X(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LOG(3, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.tinyintType.isNull() ? null : (byte)SafeMath.log(3, clone.tinyintType.getAsByte()), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LOG(3, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.smallintType.isNull() ? null : (short)SafeMath.log(3, clone.smallintType.getAsShort()), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 0),
        LTE(t.intType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LOG(3, t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.intType.isNull() ? null : (int)SafeMath.log(3, clone.intType.getAsInt()), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LOG(3, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.bigintType.isNull() ? null : (long)SafeMath.log(3, clone.bigintType.getAsLong()), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LOG(3, t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.floatType.isNull() ? null : (float)SafeMath.log(3, clone.floatType.getAsFloat()), t.floatType.isNull() ? null : Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LOG(3, t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(clone.doubleType.isNull() ? null : SafeMath.log(3, clone.doubleType.getAsDouble()), Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(LOG(3, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(clone.decimalType.isNull() ? null : SafeMath.log(BigDecimal.valueOf(3), clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLogXX(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 1),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LOG(t.tinyintType, t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.log(clone.tinyintType.getAsByte(), clone.tinyintType.getAsByte())), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 1),
        LTE(t.smallintType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LOG(t.smallintType, t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Short.valueOf((short)SafeMath.log(clone.smallintType.getAsShort(), clone.smallintType.getAsShort())), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 1),
        LTE(t.intType, 1000)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LOG(t.intType, t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Integer.valueOf((int)SafeMath.log(clone.intType.getAsInt(), clone.intType.getAsInt())), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 1),
        LTE(t.bigintType, 10000)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LOG(t.bigintType, t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Long.valueOf((long)SafeMath.log(clone.bigintType.getAsLong(), clone.bigintType.getAsLong())), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 1),
        LTE(t.floatType, 100000)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LOG(t.floatType, t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Float.valueOf((float)SafeMath.log(clone.floatType.getAsFloat(), clone.floatType.getAsFloat())), Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 1),
        LTE(t.doubleType, 1000000)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LOG(t.doubleType, t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Double.valueOf(SafeMath.log(clone.doubleType.getAsDouble(), clone.doubleType.getAsDouble())), Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 1),
        LTE(t.decimalType, 10000000)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(LOG(t.decimalType, t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(SafeMath.log(clone.decimalType.get(), clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLn(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LN(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.log(clone.tinyintType.getAsByte())), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LN(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Short.valueOf((short)SafeMath.log(clone.smallintType.getAsShort())), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LN(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Integer.valueOf((int)SafeMath.log(clone.intType.getAsInt())), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LN(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Long.valueOf((long)SafeMath.log(clone.bigintType.getAsLong())), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LN(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Float.valueOf((float)SafeMath.log(clone.floatType.getAsFloat())), Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LN(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(SafeMath.log(clone.doubleType.getAsDouble()), t.doubleType.getAsDouble(), 0.0000001);

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(LN(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(SafeMath.log(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLog2(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LOG2(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.log2(clone.tinyintType.getAsByte())), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LOG2(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Short.valueOf((short)SafeMath.log2(clone.smallintType.getAsShort())), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LOG2(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Integer.valueOf((int)SafeMath.log2(clone.intType.getAsInt())), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LOG2(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Long.valueOf((long)SafeMath.log2(clone.bigintType.getAsLong())), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LOG2(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Float.valueOf((float)SafeMath.log2(clone.floatType.getAsFloat())), Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LOG2(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    final double expected = SafeMath.log2(clone.doubleType.getAsDouble());
    assertEquals(expected, Double.valueOf(t.doubleType.getAsDouble()), 10 * Math.ulp(expected));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(LOG2(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(SafeMath.log2(clone.decimalType.get(), mc), t.decimalType.get());
  }

  @Test
  @AssertSelect(cacheSelectEntity=true, rowIteratorFullConsume=false)
  public void testLog10(final Types types, final Transaction transaction) throws IOException, SQLException {
    Types.Type t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.tinyintType, 0),
        LTE(t.tinyintType, 10)))
          .execute(transaction));

    Types.Type clone = t.clone();

    t.tinyintType.set(CAST(LOG10(t.tinyintType)).AS.TINYINT(t.tinyintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Byte.valueOf((byte)SafeMath.log10(clone.tinyintType.getAsByte())), Byte.valueOf(t.tinyintType.getAsByte()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.smallintType, 0),
        LTE(t.smallintType, 100)))
          .execute(transaction));

    clone = t.clone();

    t.smallintType.set(CAST(LOG10(t.smallintType)).AS.SMALLINT(t.smallintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Short.valueOf((short)SafeMath.log10(clone.smallintType.getAsShort())), Short.valueOf(t.smallintType.getAsShort()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.intType, 0),
        LTE(t.intType, 1000)))
          .execute(transaction));

    clone = t.clone();

    t.intType.set(CAST(LOG10(t.intType)).AS.INT(t.intType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Integer.valueOf((int)SafeMath.log10(clone.intType.getAsInt())), Integer.valueOf(t.intType.getAsInt()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.bigintType, 0),
        LTE(t.bigintType, 10000)))
          .execute(transaction));

    clone = t.clone();

    t.bigintType.set(CAST(LOG10(t.bigintType)).AS.BIGINT(t.bigintType.precision()));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Long.valueOf((long)SafeMath.log10(clone.bigintType.getAsLong())), Long.valueOf(t.bigintType.getAsLong()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.floatType, 0),
        LTE(t.floatType, 100000)))
          .execute(transaction));

    clone = t.clone();

    t.floatType.set(LOG10(t.floatType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Float.valueOf((float)SafeMath.log10(clone.floatType.getAsFloat())), Float.valueOf(t.floatType.getAsFloat()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.doubleType, 0),
        LTE(t.doubleType, 1000000)))
          .execute(transaction));

    clone = t.clone();

    t.doubleType.set(LOG10(t.doubleType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertEquals(Double.valueOf(SafeMath.log10(clone.doubleType.getAsDouble())), Double.valueOf(t.doubleType.getAsDouble()));

    t = types.Type();
    t = getNthRow(rowNum++,

      SELECT(t).
      FROM(t).
      WHERE(AND(
        GT(t.decimalType, 0),
        LTE(t.decimalType, 10000000)))
          .execute(transaction));

    clone = t.clone();

    t.decimalType.set(LOG10(t.decimalType));

    assertEquals(1,
      UPDATE(t)
        .execute(transaction)
        .getCount());

    assertBigDecimalEquals(SafeMath.log10(clone.decimalType.get(), mc), t.decimalType.get());
  }
}