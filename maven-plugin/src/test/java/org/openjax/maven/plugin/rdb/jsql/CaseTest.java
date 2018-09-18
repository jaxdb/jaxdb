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

import static org.openjax.rdb.jsql.DML.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.fastjax.test.MixedTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.openjax.rdb.ddlx.runner.Derby;
import org.openjax.rdb.ddlx.runner.MySQL;
import org.openjax.rdb.ddlx.runner.Oracle;
import org.openjax.rdb.ddlx.runner.PostgreSQL;
import org.openjax.rdb.ddlx.runner.SQLite;
import org.openjax.rdb.jsql.DML.CASE;
import org.openjax.rdb.jsql.DML.IS;
import org.openjax.maven.plugin.rdb.jsql.runner.VendorSchemaRunner;
import org.openjax.rdb.jsql.RowIterator;
import org.openjax.rdb.jsql.type;
import org.openjax.rdb.jsql.types;

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema(types.class)
@VendorSchemaRunner.Test({Derby.class, SQLite.class})
@VendorSchemaRunner.Integration({MySQL.class, PostgreSQL.class, Oracle.class})
@Category(MixedTest.class)
public class CaseTest {
  @Test
  public void testSimpleBoolean() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new type.BOOLEAN()),
        CASE(t.booleanType).WHEN(true).THEN(true).ELSE(t.booleanType).END().AS(new type.BOOLEAN()),
        CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(true).END().AS(new type.BOOLEAN()),
        SELECT(
          CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new type.BOOLEAN())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.tinyintType).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.tinyintType).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE((byte)3).END().AS(new type.FLOAT()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE((short)3).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3l).END().AS(new type.DOUBLE()),
        SELECT(
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.tinyintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.tinyintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE((byte)3).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE((short)3).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3l).END().AS(new type.DOUBLE()),
        SELECT(
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleDecimal() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3f).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.doubleType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.doubleType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3d).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.tinyintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.tinyintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE((byte)3).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.smallintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.smallintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE((short)3).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.intType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.intType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.bigintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(new BigDecimal("3")).ELSE(t.bigintType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3l).END().AS(new type.DECIMAL(10, 4)),
        SELECT(
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.tinyintType).END().AS(new type.TINYINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.tinyintType).END().AS(new type.TINYINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE((byte)3).END().AS(new type.TINYINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE((short)3).END().AS(new type.SMALLINT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3).END().AS(new type.INT(3)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.tinyintType).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.tinyintType).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE((byte)3).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE((short)3).END().AS(new type.SMALLINT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3).END().AS(new type.INT(3)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.tinyintType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.tinyintType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE((byte)3).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.smallintType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.smallintType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE((short)3).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3).END().AS(new type.INT(3)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.intType).WHEN(1).THEN(3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.tinyintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.tinyintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE((byte)3).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.smallintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.smallintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE((short)3).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.intType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.intType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(3).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(3l).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE(t.bigintType).WHEN(1l).THEN(t.bigintType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleBinary() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BINARY> rows =
      SELECT(
        CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new type.BINARY(255)),
        CASE(t.binaryType).WHEN("value".getBytes()).THEN(new byte[] {0x00, 0x01}).ELSE(t.binaryType).END().AS(new type.BINARY(255)),
        CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(new byte[] {0x00, 0x01}).END().AS(new type.BINARY(255)),
        SELECT(
          CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new type.BINARY(255))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleDate() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATE> rows =
      SELECT(
        CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(t.dateType).END().AS(new type.DATE()),
        CASE(t.dateType).WHEN(LocalDate.now()).THEN(LocalDate.now()).ELSE(t.dateType).END().AS(new type.DATE()),
        CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(LocalDate.now()).END().AS(new type.DATE()),
        SELECT(
          CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(t.dateType).END().AS(new type.DATE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(
        CASE(t.timeType).WHEN(LocalTime.now()).THEN(t.timeType).ELSE(t.timeType).END().AS(new type.TIME()),
        CASE(t.timeType).WHEN(LocalTime.now()).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new type.TIME()),
        CASE(t.timeType).WHEN(LocalTime.now()).THEN(t.timeType).ELSE(LocalTime.now()).END().AS(new type.TIME()),
        SELECT(
          CASE(t.timeType).WHEN(LocalTime.now()).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new type.TIME())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleDateTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATETIME> rows =
      SELECT(
        CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new type.DATETIME()),
        CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(LocalDateTime.now()).ELSE(t.datetimeType).END().AS(new type.DATETIME()),
        CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(LocalDateTime.now()).END().AS(new type.DATETIME()),
        SELECT(
          CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new type.DATETIME())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CASE(t.charType).WHEN("").THEN(t.charType).ELSE(t.charType).END().AS(new type.CHAR(255, true)),
        CASE(t.charType).WHEN("abc").THEN("char").ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE(t.charType).WHEN("").THEN(t.charType).ELSE("char").END().AS(new type.CHAR(255, true)),
        CASE(t.charType).WHEN("abc").THEN(t.charType).ELSE(t.enumType).END().AS(new type.CHAR(255, false)),
        CASE(t.charType).WHEN("").THEN("char").ELSE(t.enumType).END().AS(new type.CHAR(255, true)),
        CASE(t.charType).WHEN("abc").THEN(t.enumType).ELSE("char").END().AS(new type.CHAR(255, false)),
        SELECT(
          CASE(t.charType).WHEN("").THEN(t.charType).ELSE(t.charType).END().AS(new type.CHAR(255, true))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSimpleEnum() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Textual<?>> rows =
      SELECT(
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(t.enumType).ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(types.Type.EnumType.EIGHT).ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(t.enumType).ELSE(types.Type.EnumType.EIGHT).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(t.enumType).ELSE(t.enumType).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(types.Type.EnumType.EIGHT).ELSE(t.enumType).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(t.enumType).ELSE(types.Type.EnumType.EIGHT).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        SELECT(
          CASE(t.enumType).WHEN(types.Type.EnumType.EIGHT).THEN(t.enumType).ELSE(t.charType).END().AS(new type.CHAR(255, false))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchBoolean() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(
        CASE.WHEN(EQ(t.booleanType, true)).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new type.BOOLEAN()),
        CASE.WHEN(EQ(t.booleanType, true)).THEN(true).ELSE(t.booleanType).END().AS(new type.BOOLEAN()),
        CASE.WHEN(EQ(t.booleanType, true)).THEN(t.booleanType).ELSE(true).END().AS(new type.BOOLEAN()),
        SELECT(
          CASE.WHEN(EQ(t.booleanType, true)).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new type.BOOLEAN())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchFloat() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.tinyintType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.tinyintType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE((byte)3).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE((short)3).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3l).END().AS(new type.DOUBLE()),
        SELECT(
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchDouble() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.tinyintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.tinyintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE((byte)3).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.smallintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE((short)3).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.intType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.bigintType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3l).END().AS(new type.DOUBLE()),
        SELECT(
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchDecimal() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3f).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.doubleType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.doubleType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3d).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.tinyintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.tinyintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE((byte)3).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.smallintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.smallintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE((short)3).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.intType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.intType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.bigintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(new BigDecimal("3")).ELSE(t.bigintType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3l).END().AS(new type.DECIMAL(10, 4)),
        SELECT(
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.floatType).END().AS(new type.DECIMAL(10, 4))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchSmallInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.tinyintType).END().AS(new type.TINYINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.tinyintType).END().AS(new type.TINYINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE((byte)3).END().AS(new type.TINYINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE((short)3).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchMediumInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.floatType).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3f).END().AS(new type.FLOAT()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.tinyintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.tinyintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE((byte)3).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.smallintType).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE((short)3).END().AS(new type.SMALLINT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.floatType).END().AS(new type.FLOAT())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.tinyintType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.tinyintType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE((byte)3).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.smallintType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.smallintType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE((short)3).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.intType).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3).END().AS(new type.INT(3)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchBigInt() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Numeric<?>> rows =
      SELECT(
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.floatType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3f).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.doubleType).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3d).END().AS(new type.DOUBLE()),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.decimalType).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(new BigDecimal("3.4")).END().AS(new type.DECIMAL(10, 4)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.tinyintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.tinyintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE((byte)3).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.smallintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.smallintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE((short)3).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.intType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.intType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(3l).ELSE(t.bigintType).END().AS(new type.BIGINT(10)),
        CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3l).END().AS(new type.BIGINT(10)),
        SELECT(
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.floatType).END().AS(new type.DOUBLE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchBinary() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.BINARY> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new type.BINARY(255)),
        CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(new byte[] {0x00, 0x01}).ELSE(t.binaryType).END().AS(new type.BINARY(255)),
        CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(t.binaryType).ELSE(new byte[] {0x00, 0x01}).END().AS(new type.BINARY(255)),
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new type.BINARY(255))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchDate() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATE> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(t.dateType).ELSE(t.dateType).END().AS(new type.DATE()),
        CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(LocalDate.now()).ELSE(t.dateType).END().AS(new type.DATE()),
        CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(t.dateType).ELSE(LocalDate.now()).END().AS(new type.DATE()),
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(t.dateType).ELSE(t.dateType).END().AS(new type.DATE())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.timeType)).THEN(t.timeType).ELSE(t.timeType).END().AS(new type.TIME()),
        CASE.WHEN(IS.NOT.NULL(t.timeType)).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new type.TIME()),
        CASE.WHEN(IS.NOT.NULL(t.timeType)).THEN(t.timeType).ELSE(LocalTime.now()).END().AS(new type.TIME())
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchDateTime() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.DATETIME> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new type.DATETIME()),
        CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(LocalDateTime.now()).ELSE(t.datetimeType).END().AS(new type.DATETIME()),
        CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(t.datetimeType).ELSE(LocalDateTime.now()).END().AS(new type.DATETIME()),
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new type.DATETIME())
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchChar() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.charType).ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN("char").ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.charType).ELSE("char").END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.charType).ELSE(t.enumType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN("char").ELSE(t.enumType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.enumType).ELSE("char").END().AS(new type.CHAR(255, false)),
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.charType).ELSE(t.charType).END().AS(new type.CHAR(255, false))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }

  @Test
  public void testSearchEnum() throws IOException, SQLException {
    final types.Type t = new types.Type();
    try (final RowIterator<? extends type.Textual<?>> rows =
      SELECT(
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(types.Type.EnumType.EIGHT).ELSE(t.charType).END().AS(new type.CHAR(255, false)),
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(types.Type.EnumType.EIGHT).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(t.enumType).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(types.Type.EnumType.EIGHT).ELSE(t.enumType).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(types.Type.EnumType.EIGHT).END().AS(new type.ENUM<>(types.Type.EnumType.class)),
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(t.charType).END().AS(new type.CHAR(255, false))
        ).
        FROM(t).LIMIT(1)
      ).
      FROM(t).
      execute()) {
      Assert.assertTrue(rows.nextRow());
    }
  }
}