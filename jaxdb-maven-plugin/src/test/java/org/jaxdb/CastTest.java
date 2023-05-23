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

import org.jaxdb.jsql.DML.NOT;
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

@RunWith(SchemaTestRunner.class)
public abstract class CastTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends CastTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends CastTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBooleanToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.booleanType).AS.CHAR(5),
        CAST(SELECT(t.booleanType).FROM(t).LIMIT(1)).AS.CHAR(5)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBooleanToClob(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CLOB> rows =

      SELECT(
        CAST(t.booleanType).AS.CLOB(5),
        CAST(SELECT(t.booleanType).FROM(t).LIMIT(1)).AS.CLOB(5)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.floatType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.floatType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.floatType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(t.floatType).FROM(t).LIMIT(1)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.floatType).AS.TINYINT(3),
        CAST(SELECT(MIN(t.floatType)).FROM(t).WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.floatType).AS.SMALLINT(5),
        CAST(SELECT(t.floatType).FROM(t).WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE))).LIMIT(1)).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.floatType).AS.INT(10),
        CAST(SELECT(MIN(t.floatType)).FROM(t)).AS.INT(10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testFloatToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.floatType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.floatType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.doubleType).AS.FLOAT(),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.doubleType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.doubleType).AS.TINYINT(3),
        CAST(SELECT(MIN(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, Byte.MIN_VALUE), LTE(t.doubleType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, Byte.MIN_VALUE), LTE(t.doubleType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.doubleType).AS.SMALLINT(5),
        CAST(SELECT(MAX(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, Short.MIN_VALUE), LTE(t.doubleType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, Short.MIN_VALUE), LTE(t.doubleType, Short.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.doubleType).AS.INT(10),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.INT(10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDoubleToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.doubleType).AS.BIGINT(19),
        CAST(SELECT(MIN(t.doubleType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.decimalType).AS.FLOAT(),
        CAST(SELECT(MAX(t.decimalType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.decimalType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.decimalType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.decimalType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.decimalType)).FROM(t)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.decimalType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.decimalType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.decimalType).AS.INT(10),
        CAST(SELECT(MIN(t.decimalType)).FROM(t).WHERE(AND(LTE(t.decimalType, Integer.MAX_VALUE), GTE(t.decimalType, Integer.MIN_VALUE)))).AS.INT(10)).
      FROM(t).
      WHERE(AND(LTE(t.decimalType, Integer.MAX_VALUE), GTE(t.decimalType, Integer.MIN_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.decimalType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE)))).AS.BIGINT(19)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDecimalToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.decimalType).AS.CHAR(254),
        CAST(SELECT(AVG(t.decimalType)).FROM(t)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.tinyintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.tinyintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.tinyintType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.tinyintType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.tinyintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.tinyintType).AS.SMALLINT(5),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.tinyintType).AS.INT(10),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t)).AS.INT(10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.tinyintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testSmallIntToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.tinyintType).AS.CHAR(254),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.smallintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.smallintType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.smallintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.smallintType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.smallintType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.smallintType)).FROM(t)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.smallintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, Byte.MIN_VALUE), LTE(t.smallintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, Byte.MIN_VALUE), LTE(t.smallintType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.smallintType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, Short.MIN_VALUE), LTE(t.smallintType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, Short.MIN_VALUE), LTE(t.smallintType, Short.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.smallintType).AS.INT(10),
        CAST(SELECT(MIN(t.smallintType)).FROM(t)).AS.INT(10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.smallintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.smallintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testMediumIntToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.smallintType).AS.CHAR(254),
        CAST(SELECT(AVG(t.smallintType)).FROM(t)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.intType).AS.FLOAT(),
        CAST(SELECT(MAX(t.intType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.intType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.intType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.intType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.intType)).FROM(t)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.intType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, Byte.MIN_VALUE), LTE(t.intType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.intType, Byte.MIN_VALUE), LTE(t.intType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.intType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, Short.MIN_VALUE), LTE(t.intType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.intType, Short.MIN_VALUE), LTE(t.intType, Short.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.intType).AS.INT(10),
        CAST(SELECT(MIN(t.intType)).FROM(t)).AS.INT(10)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.intType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.intType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testIntToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.intType).AS.CHAR(254),
        CAST(SELECT(AVG(t.intType)).FROM(t)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.FLOAT> rows =

      SELECT(
        CAST(t.bigintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.bigintType)).FROM(t)).AS.FLOAT()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DOUBLE> rows =

      SELECT(
        CAST(t.bigintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.bigintType)).FROM(t)).AS.DOUBLE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.bigintType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 5),
        CAST(
          SELECT(MIN(t.bigintType)).
            FROM(t).
            WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 5)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.bigintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, Byte.MIN_VALUE), LTE(t.bigintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, Byte.MIN_VALUE), LTE(t.bigintType, Byte.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.bigintType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, Short.MIN_VALUE), LTE(t.bigintType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, Short.MIN_VALUE), LTE(t.bigintType, Short.MAX_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.bigintType).AS.INT(10),
        CAST(SELECT(MIN(t.bigintType)).FROM(t).WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))).AS.INT(10)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.bigintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.bigintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBigIntToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.bigintType).AS.CHAR(254),
        CAST(SELECT(AVG(t.bigintType)).FROM(t)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DECIMAL> rows =

      SELECT(
        CAST(t.charType).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10),
        CAST(
          SELECT(t.charType).
          FROM(t).
          WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.DECIMAL(transaction.getVendor().getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TINYINT> rows =

      SELECT(
        CAST(t.charType).AS.TINYINT(3),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(
        CAST(t.charType).AS.SMALLINT(5),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.INT> rows =

      SELECT(
        CAST(t.charType).AS.INT(10),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.INT(10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(
        CAST(t.charType).AS.BIGINT(19),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.BIGINT(19)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.charType).AS.CHAR(254),
        CAST(SELECT(t.charType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DATE> rows =

      SELECT(
        CAST(t.charType).AS.DATE(),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%-%-%"), NOT.LIKE(t.charType, "%-%-% %"))).LIMIT(1)).AS.DATE()).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%-%-%"), NOT.LIKE(t.charType, "%-%-% %")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TIME> rows =

      SELECT(
        CAST(t.charType).AS.TIME(),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%:%:%"), NOT.LIKE(t.charType, "% %:%:%"))).LIMIT(1)).AS.TIME()).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%:%:%"), NOT.LIKE(t.charType, "% %:%:%")))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToDateTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DATETIME> rows =

      SELECT(
        CAST(t.charType).AS.DATETIME(),
        CAST(SELECT(t.charType).FROM(t).WHERE(LIKE(t.charType, "%-%-% %:%:%")).LIMIT(1)).AS.DATETIME()).
      FROM(t).
      WHERE(LIKE(t.charType, "%-%-% %:%:%"))
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testCharToClob(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CLOB> rows =

      SELECT(
        CAST(t.charType).AS.CLOB(254),
        CAST(SELECT(t.charType).FROM(t).LIMIT(1)).AS.CLOB(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDateToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.dateType).AS.CHAR(254),
        CAST(SELECT(t.dateType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testTimeToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.timeType).AS.CHAR(254),
        CAST(SELECT(t.timeType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testTimeToTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TIME> rows =

      SELECT(
        CAST(t.timeType).AS.TIME(),
        CAST(SELECT(t.timeType).FROM(t).LIMIT(1)).AS.TIME()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDateTimeToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.datetimeType).AS.CHAR(254),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDateTimeToDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DATE> rows =

      SELECT(
        CAST(t.datetimeType).AS.DATE(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.DATE()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDateTimeToTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.TIME> rows =

      SELECT(
        CAST(t.datetimeType).AS.TIME(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.TIME()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testDateTimeToDateTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.DATETIME> rows =

      SELECT(
        CAST(t.datetimeType).AS.DATETIME(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.DATETIME()).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testClobToChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CHAR> rows =

      SELECT(
        CAST(t.clobType).AS.CHAR(254),
        CAST(SELECT(t.clobType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testClobToClob(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.CLOB> rows =

      SELECT(
        CAST(t.clobType).AS.CLOB(254),
        CAST(SELECT(t.clobType).FROM(t).LIMIT(1)).AS.CLOB(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBlobToBlob(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BLOB> rows =

      SELECT(
        CAST(t.blobType).AS.BLOB(254),
        CAST(SELECT(t.blobType).FROM(t).LIMIT(1)).AS.BLOB(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBinaryToBlob(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BLOB> rows =

      SELECT(
        CAST(t.binaryType).AS.BLOB(254),
        CAST(SELECT(t.binaryType).FROM(t).LIMIT(1)).AS.BLOB(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertSelect(cacheSelectEntity=false, rowIteratorFullConsume=false)
  public void testBinaryToBinary(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (final RowIterator<data.BINARY> rows =

      SELECT(
        CAST(t.binaryType).AS.BINARY(254),
        CAST(SELECT(t.binaryType).FROM(t).LIMIT(1)).AS.BINARY(254)).
      FROM(t)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
    }
  }
}