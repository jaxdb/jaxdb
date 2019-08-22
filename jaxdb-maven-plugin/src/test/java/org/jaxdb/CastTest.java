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
import java.sql.SQLException;

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.runner.VendorSchemaRunner;
import org.jaxdb.vendor.DBVendor;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class CastTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(types.class)
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends CastTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema(types.class)
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends CastTest {
  }

  @Test
  public void testBooleanToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.booleanType).AS.CHAR(5),
        CAST(SELECT(t.booleanType).FROM(t).LIMIT(1)).AS.CHAR(5)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBooleanToClob() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CLOB> rows =
      SELECT(
        CAST(t.booleanType).AS.CLOB(5),
        CAST(SELECT(t.booleanType).FROM(t).LIMIT(1)).AS.CLOB(5)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.floatType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.floatType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.UNSIGNED(),
        CAST(SELECT(MIN(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(SUM(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.floatType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(t.floatType).FROM(t).LIMIT(1)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MAX(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.floatType).AS.TINYINT(3),
        CAST(SELECT(MIN(t.floatType)).FROM(t).WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(AVG(t.floatType)).FROM(t).WHERE(AND(GTE(t.floatType, 0), LTE(t.floatType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.floatType, 0), LTE(t.floatType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.floatType).AS.SMALLINT(5),
        CAST(SELECT(t.floatType).FROM(t).WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE))).LIMIT(1)).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.floatType, Byte.MIN_VALUE), LTE(t.floatType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MAX(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.floatType).AS.INT(10),
        CAST(SELECT(MIN(t.floatType)).FROM(t)).AS.INT(10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.INT.UNSIGNED(10),
        CAST(SELECT(AVG(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.floatType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.floatType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testFloatToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.floatType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(MIN(t.floatType)).FROM(t).WHERE(GTE(t.floatType, 0))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(GTE(t.floatType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.doubleType).AS.FLOAT(),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MAX(t.doubleType)).FROM(t).WHERE(GTE(t.doubleType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.doubleType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.UNSIGNED(),
        CAST(SELECT(MIN(t.doubleType)).FROM(t).WHERE(GTE(t.doubleType, 0))).AS.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.doubleType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.doubleType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MAX(t.doubleType)).FROM(t).WHERE(GTE(t.doubleType, 0))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.doubleType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.doubleType).AS.TINYINT(3),
        CAST(SELECT(MIN(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, Byte.MIN_VALUE), LTE(t.doubleType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, Byte.MIN_VALUE), LTE(t.doubleType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(AVG(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, 0), LTE(t.doubleType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, 0), LTE(t.doubleType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.doubleType).AS.SMALLINT(5),
        CAST(SELECT(MAX(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, Short.MIN_VALUE), LTE(t.doubleType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, Short.MIN_VALUE), LTE(t.doubleType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MIN(t.doubleType)).FROM(t).WHERE(AND(GTE(t.doubleType, 0), LT(t.doubleType, Short.MAX_VALUE)))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.doubleType, 0), LT(t.doubleType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.doubleType).AS.INT(10),
        CAST(SELECT(AVG(t.doubleType)).FROM(t)).AS.INT(10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.INT.UNSIGNED(10),
        CAST(SELECT(MAX(t.doubleType)).FROM(t).WHERE(GTE(t.doubleType, 0))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(GTE(t.doubleType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.doubleType).AS.BIGINT(19),
        CAST(SELECT(MIN(t.doubleType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDoubleToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.doubleType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(AVG(t.doubleType)).FROM(t).WHERE(GTE(t.doubleType, 0))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(GTE(t.doubleType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.decimalType).AS.FLOAT(),
        CAST(SELECT(MAX(t.decimalType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MIN(t.decimalType)).FROM(t).WHERE(GTE(t.decimalType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.decimalType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.decimalType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.decimalType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(GTE(t.decimalType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.decimalType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.decimalType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.decimalType)).FROM(t)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.decimalType)).FROM(t).WHERE(GTE(t.decimalType, 0))).AS.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.decimalType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.decimalType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(MIN(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.decimalType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, Byte.MIN_VALUE), LTE(t.decimalType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LT(t.decimalType, Short.MAX_VALUE)))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LT(t.decimalType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.decimalType).AS.INT(10),
        CAST(SELECT(MIN(t.decimalType)).FROM(t).WHERE(AND(LTE(t.decimalType, Integer.MAX_VALUE), GTE(t.decimalType, Integer.MIN_VALUE)))).AS.INT(10)).
      FROM(t).
      WHERE(AND(LTE(t.decimalType, Integer.MAX_VALUE), GTE(t.decimalType, Integer.MIN_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.INT.UNSIGNED(10),
        CAST(SELECT(AVG(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, 16777215)))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, 16777215))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.decimalType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE)))).AS.BIGINT(19)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.decimalType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(MIN(t.decimalType)).FROM(t).WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE)))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(AND(GTE(t.decimalType, 0), LTE(t.decimalType, Integer.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDecimalToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.decimalType).AS.CHAR(254),
        CAST(SELECT(AVG(t.decimalType)).FROM(t)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.tinyintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.tinyintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.tinyintType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.tinyintType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.tinyintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.tinyintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.UNSIGNED(3),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, 0), LTE(t.tinyintType, 255)))).AS.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, 0), LTE(t.tinyintType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.tinyintType).AS.SMALLINT(5),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, Byte.MIN_VALUE), LTE(t.tinyintType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t).WHERE(AND(GTE(t.tinyintType, 0), LTE(t.tinyintType, 255)))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.tinyintType, 0), LTE(t.tinyintType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.tinyintType).AS.INT(10),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t)).AS.INT(10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.INT.UNSIGNED(10),
        CAST(SELECT(AVG(t.tinyintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.tinyintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.tinyintType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(MIN(t.tinyintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSmallIntToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.tinyintType).AS.CHAR(254),
        CAST(SELECT(MAX(t.tinyintType)).FROM(t)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.smallintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.smallintType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MIN(t.smallintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.smallintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.smallintType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(MAX(t.smallintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.smallintType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.smallintType)).FROM(t)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.smallintType)).FROM(t).WHERE(GTE(t.tinyintType, 0))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.tinyintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.smallintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, Byte.MIN_VALUE), LTE(t.smallintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, Byte.MIN_VALUE), LTE(t.smallintType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(MIN(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, 0), LTE(t.smallintType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, 0), LTE(t.smallintType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.smallintType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, Short.MIN_VALUE), LTE(t.smallintType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, Short.MIN_VALUE), LTE(t.smallintType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.UNSIGNED(5),
        CAST(SELECT(MAX(t.smallintType)).FROM(t).WHERE(AND(GTE(t.smallintType, 0), LTE(t.smallintType, Short.MAX_VALUE)))).AS.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.smallintType, 0), LTE(t.smallintType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.smallintType).AS.INT(10),
        CAST(SELECT(MIN(t.smallintType)).FROM(t)).AS.INT(10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.INT.UNSIGNED(10),
        CAST(SELECT(AVG(t.smallintType)).FROM(t).WHERE(GTE(t.smallintType, 0))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(GTE(t.smallintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.smallintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.smallintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.smallintType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(MIN(t.smallintType)).FROM(t).WHERE(GTE(t.smallintType, 0))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(GTE(t.smallintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testMediumIntToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.smallintType).AS.CHAR(254),
        CAST(SELECT(AVG(t.smallintType)).FROM(t)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.intType).AS.FLOAT(),
        CAST(SELECT(MAX(t.intType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MIN(t.intType)).FROM(t).WHERE(GTE(t.intType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.intType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.intType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.intType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(MAX(t.intType)).FROM(t).WHERE(GTE(t.intType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.intType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.intType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(MIN(t.intType)).FROM(t)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(AVG(t.intType)).FROM(t).WHERE(GTE(t.intType, 0))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(GTE(t.intType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.intType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, Byte.MIN_VALUE), LTE(t.intType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.intType, Byte.MIN_VALUE), LTE(t.intType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(MIN(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, 0), LTE(t.intType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.intType, 0), LTE(t.intType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.intType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, Short.MIN_VALUE), LTE(t.intType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.intType, Short.MIN_VALUE), LTE(t.intType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MAX(t.intType)).FROM(t).WHERE(AND(GTE(t.intType, 0), LT(t.intType, Short.MAX_VALUE)))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.intType, 0), LT(t.intType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.intType).AS.INT(10),
        CAST(SELECT(MIN(t.intType)).FROM(t)).AS.INT(10)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.UNSIGNED(10),
        CAST(SELECT(AVG(t.intType)).FROM(t).WHERE(GTE(t.intType, 0))).AS.UNSIGNED(10)).
      FROM(t).
      WHERE(GTE(t.intType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.intType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.intType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.intType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(MIN(t.intType)).FROM(t).WHERE(GTE(t.intType, 0))).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(GTE(t.intType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testIntToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.intType).AS.CHAR(254),
        CAST(SELECT(AVG(t.intType)).FROM(t)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT> rows =
      SELECT(
        CAST(t.bigintType).AS.FLOAT(),
        CAST(SELECT(MAX(t.bigintType)).FROM(t)).AS.FLOAT()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToFloatUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.FLOAT.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.FLOAT.UNSIGNED(),
        CAST(SELECT(MIN(t.bigintType)).FROM(t).WHERE(GTE(t.bigintType, 0))).AS.FLOAT.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.bigintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE> rows =
      SELECT(
        CAST(t.bigintType).AS.DOUBLE(),
        CAST(SELECT(AVG(t.bigintType)).FROM(t)).AS.DOUBLE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToDoubleUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DOUBLE.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.DOUBLE.UNSIGNED(),
        CAST(SELECT(MAX(t.bigintType)).FROM(t).WHERE(GTE(t.bigintType, 0))).AS.DOUBLE.UNSIGNED()).
      FROM(t).
      WHERE(GTE(t.bigintType, 0)).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.bigintType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 5),
        CAST(SELECT(MIN(t.bigintType)).FROM(t).WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 5)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 5),
        CAST(SELECT(AVG(t.bigintType)).FROM(t).WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GTE(t.bigintType, 0)))).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 5)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GTE(t.bigintType, 0))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.bigintType).AS.TINYINT(3),
        CAST(SELECT(MAX(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, Byte.MIN_VALUE), LTE(t.bigintType, Byte.MAX_VALUE)))).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, Byte.MIN_VALUE), LTE(t.bigintType, Byte.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(MIN(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, 0), LTE(t.bigintType, 255)))).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, 0), LTE(t.bigintType, 255))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.bigintType).AS.SMALLINT(5),
        CAST(SELECT(AVG(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, Short.MIN_VALUE), LTE(t.bigintType, Short.MAX_VALUE)))).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, Short.MIN_VALUE), LTE(t.bigintType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(MAX(t.bigintType)).FROM(t).WHERE(AND(GTE(t.bigintType, 0), LT(t.bigintType, Short.MAX_VALUE)))).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(GTE(t.bigintType, 0), LT(t.bigintType, Short.MAX_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.bigintType).AS.INT(10),
        CAST(SELECT(MIN(t.bigintType)).FROM(t).WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE)))).AS.INT(10)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GT(t.bigintType, Integer.MIN_VALUE))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.INT.UNSIGNED(10),
        CAST(SELECT(AVG(t.bigintType)).FROM(t).WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GTE(t.bigintType, 0)))).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(AND(LT(t.bigintType, Integer.MAX_VALUE), GTE(t.bigintType, 0))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.bigintType).AS.BIGINT(19),
        CAST(SELECT(MAX(t.bigintType)).FROM(t)).AS.BIGINT(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.bigintType).AS.UNSIGNED(19),
        CAST(SELECT(MIN(t.bigintType)).FROM(t)).AS.UNSIGNED(19)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBigIntToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.bigintType).AS.CHAR(254),
        CAST(SELECT(AVG(t.bigintType)).FROM(t)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToDecimal(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(
        CAST(t.charType).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.DECIMAL(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToDecimalUnsigned(final DBVendor vendor) throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DECIMAL.UNSIGNED> rows =
      SELECT(
        CAST(t.charType).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.DECIMAL.UNSIGNED(vendor.getDialect().decimalMaxPrecision(), 10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT> rows =
      SELECT(
        CAST(t.charType).AS.TINYINT(3),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.TINYINT(3)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToSmallIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TINYINT.UNSIGNED> rows =
      SELECT(
        CAST(t.charType).AS.TINYINT.UNSIGNED(3),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.TINYINT.UNSIGNED(3)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT> rows =
      SELECT(
        CAST(t.charType).AS.SMALLINT(5),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.SMALLINT(5)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToMediumIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.SMALLINT.UNSIGNED> rows =
      SELECT(
        CAST(t.charType).AS.SMALLINT.UNSIGNED(5),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.SMALLINT.UNSIGNED(5)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(
        CAST(t.charType).AS.INT(10),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.INT(10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.INT.UNSIGNED> rows =
      SELECT(
        CAST(t.charType).AS.INT.UNSIGNED(10),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.INT.UNSIGNED(10)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(
        CAST(t.charType).AS.BIGINT(19),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.BIGINT(19)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToBigIntUnsigned() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BIGINT.UNSIGNED> rows =
      SELECT(
        CAST(t.charType).AS.BIGINT.UNSIGNED(19),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).LIMIT(1)).AS.BIGINT.UNSIGNED(19)).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%1%"), NOT.LIKE(t.charType, "%.%"), NOT.LIKE(t.charType, "-%"), NOT.LIKE(t.charType, "%-%"), NOT.LIKE(t.charType, "%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.charType).AS.CHAR(254),
        CAST(SELECT(t.charType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToDate() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATE> rows =
      SELECT(
        CAST(t.charType).AS.DATE(),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%-%-%"), NOT.LIKE(t.charType, "%-%-% %"))).LIMIT(1)).AS.DATE()).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%-%-%"), NOT.LIKE(t.charType, "%-%-% %"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(
        CAST(t.charType).AS.TIME(),
        CAST(SELECT(t.charType).FROM(t).WHERE(AND(LIKE(t.charType, "%:%:%"), NOT.LIKE(t.charType, "% %:%:%"))).LIMIT(1)).AS.TIME()).
      FROM(t).
      WHERE(AND(LIKE(t.charType, "%:%:%"), NOT.LIKE(t.charType, "% %:%:%"))).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToDateTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATETIME> rows =
      SELECT(
        CAST(t.charType).AS.DATETIME(),
        CAST(SELECT(t.charType).FROM(t).WHERE(LIKE(t.charType, "%-%-% %:%:%")).LIMIT(1)).AS.DATETIME()).
      FROM(t).
      WHERE(LIKE(t.charType, "%-%-% %:%:%")).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testCharToClob() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CLOB> rows =
      SELECT(
        CAST(t.charType).AS.CLOB(254),
        CAST(SELECT(t.charType).FROM(t).LIMIT(1)).AS.CLOB(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDateToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.dateType).AS.CHAR(254),
        CAST(SELECT(t.dateType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testTimeToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.timeType).AS.CHAR(254),
        CAST(SELECT(t.timeType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testTimeToTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(
        CAST(t.timeType).AS.TIME(),
        CAST(SELECT(t.timeType).FROM(t).LIMIT(1)).AS.TIME()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDateTimeToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.datetimeType).AS.CHAR(254),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDateTimeToDate() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATE> rows =
      SELECT(
        CAST(t.datetimeType).AS.DATE(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.DATE()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDateTimeToTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(
        CAST(t.datetimeType).AS.TIME(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.TIME()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testDateTimeToDateTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATETIME> rows =
      SELECT(
        CAST(t.datetimeType).AS.DATETIME(),
        CAST(SELECT(t.datetimeType).FROM(t).LIMIT(1)).AS.DATETIME()).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testClobToChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CAST(t.clobType).AS.CHAR(254),
        CAST(SELECT(t.clobType).FROM(t).LIMIT(1)).AS.CHAR(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testClobToClob() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CLOB> rows =
      SELECT(
        CAST(t.clobType).AS.CLOB(254),
        CAST(SELECT(t.clobType).FROM(t).LIMIT(1)).AS.CLOB(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBlobToBlob() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BLOB> rows =
      SELECT(
        CAST(t.blobType).AS.BLOB(254),
        CAST(SELECT(t.blobType).FROM(t).LIMIT(1)).AS.BLOB(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBinaryToBlob() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BLOB> rows =
      SELECT(
        CAST(t.binaryType).AS.BLOB(254),
        CAST(SELECT(t.binaryType).FROM(t).LIMIT(1)).AS.BLOB(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testBinaryToBinary() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BINARY> rows =
      SELECT(
        CAST(t.binaryType).AS.BINARY(254),
        CAST(SELECT(t.binaryType).FROM(t).LIMIT(1)).AS.BINARY(254)).
      FROM(t).
      execute()) {
      assertTrue(rows.nextRow());
    }
  }
}