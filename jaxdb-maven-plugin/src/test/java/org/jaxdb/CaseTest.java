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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import org.jaxdb.jsql.DML.CASE;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.TestCommand.Select.AssertCommand;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.Types;
import org.jaxdb.jsql.Types.$AbstractType.EnumType;
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
public abstract class CaseTest {
  @DB(value = Derby.class, parallel = 2)
  @DB(SQLite.class)
  public static class IntegrationTest extends CaseTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends CaseTest {
  }

  @Test
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testSimpleBooleanPrimary(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new data.BOOLEAN()),
          CASE(t.booleanType).WHEN(true).THEN(true).ELSE(t.booleanType).END().AS(new data.BOOLEAN()),
          CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(true).END().AS(new data.BOOLEAN()))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleBoolean(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new data.BOOLEAN()),
          CASE(t.booleanType).WHEN(true).THEN(true).ELSE(t.booleanType).END().AS(new data.BOOLEAN()),
          CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(true).END().AS(new data.BOOLEAN()),
          SELECT(
            CASE(t.booleanType).WHEN(true).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new data.BOOLEAN()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.tinyintType).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.tinyintType).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE((byte)3).END().AS(new data.FLOAT()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE((short)3).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(3f).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(3L).END().AS(new data.DOUBLE()),
          SELECT(
            CASE(t.floatType).WHEN(1f).THEN(t.floatType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.tinyintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.tinyintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE((byte)3).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE((short)3).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(3d).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(3L).END().AS(new data.DOUBLE()),
          SELECT(
            CASE(t.doubleType).WHEN(1d).THEN(t.doubleType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final BigDecimal three = new BigDecimal(3);
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3f).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.doubleType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.doubleType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3d).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.tinyintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.tinyintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE((byte)3).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.smallintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.smallintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE((short)3).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.intType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.intType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.bigintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(three).ELSE(t.bigintType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(3L).END().AS(new data.DECIMAL(10, 4)),
          SELECT(
            CASE(t.decimalType).WHEN(BigDecimal.ONE).THEN(t.decimalType).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.tinyintType).END().AS(new data.TINYINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.tinyintType).END().AS(new data.TINYINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE((byte)3).END().AS(new data.TINYINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE((short)3).END().AS(new data.SMALLINT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3).END().AS(new data.INT(3)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.tinyintType).WHEN((byte)1).THEN((byte)3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE(t.tinyintType).WHEN((byte)1).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.tinyintType).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.tinyintType).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE((byte)3).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE((short)3).END().AS(new data.SMALLINT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3).END().AS(new data.INT(3)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.smallintType).WHEN((short)1).THEN((short)3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE(t.smallintType).WHEN((short)1).THEN(t.smallintType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.tinyintType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.tinyintType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE((byte)3).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.smallintType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.smallintType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE((short)3).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3).END().AS(new data.INT(3)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.intType).WHEN(1).THEN(3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE(t.intType).WHEN(1).THEN(t.intType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.tinyintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.tinyintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE((byte)3).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.smallintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.smallintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE((short)3).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.intType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.intType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(3).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(3L).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE(t.bigintType).WHEN(1L).THEN(t.bigintType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleBinary(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.BINARY> rows =
        SELECT(
          CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new data.BINARY(255)),
          CASE(t.binaryType).WHEN("value".getBytes()).THEN(new byte[] {0x00, 0x01}).ELSE(t.binaryType).END().AS(new data.BINARY(255)),
          CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(new byte[] {0x00, 0x01}).END().AS(new data.BINARY(255)),
          SELECT(
            CASE(t.binaryType).WHEN("value".getBytes()).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new data.BINARY(255)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.DATE> rows =
        SELECT(
          CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(t.dateType).END().AS(new data.DATE()),
          CASE(t.dateType).WHEN(LocalDate.now()).THEN(LocalDate.now()).ELSE(t.dateType).END().AS(new data.DATE()),
          CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(LocalDate.now()).END().AS(new data.DATE()),
          SELECT(
            CASE(t.dateType).WHEN(LocalDate.now()).THEN(t.dateType).ELSE(t.dateType).END().AS(new data.DATE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.TIME> rows =
        SELECT(
          CASE(t.timeType).WHEN(LocalTime.now()).THEN(t.timeType).ELSE(t.timeType).END().AS(new data.TIME()),
          CASE(t.timeType).WHEN(LocalTime.now()).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new data.TIME()),
          CASE(t.timeType).WHEN(LocalTime.now()).THEN(t.timeType).ELSE(LocalTime.now()).END().AS(new data.TIME()),
          SELECT(
            CASE(t.timeType).WHEN(LocalTime.now()).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new data.TIME()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleDateTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.DATETIME> rows =
        SELECT(
          CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new data.DATETIME()),
          CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(LocalDateTime.now()).ELSE(t.datetimeType).END().AS(new data.DATETIME()),
          CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(LocalDateTime.now()).END().AS(new data.DATETIME()),
          SELECT(
            CASE(t.datetimeType).WHEN(LocalDateTime.now()).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new data.DATETIME()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.CHAR> rows =
        SELECT(
          CASE(t.charType).WHEN("").THEN(t.charType).ELSE(t.charType).END().AS(new data.CHAR(255, true)),
          CASE(t.charType).WHEN("abc").THEN("char").ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE(t.charType).WHEN("").THEN(t.charType).ELSE("char").END().AS(new data.CHAR(255, true)),
          CASE(t.charType).WHEN("abc").THEN(t.charType).ELSE(t.enumType).END().AS(new data.CHAR(255, false)),
          CASE(t.charType).WHEN("").THEN("char").ELSE(t.enumType).END().AS(new data.CHAR(255, true)),
          CASE(t.charType).WHEN("abc").THEN(t.enumType).ELSE("char").END().AS(new data.CHAR(255, false)),
          SELECT(
            CASE(t.charType).WHEN("").THEN(t.charType).ELSE(t.charType).END().AS(new data.CHAR(255, true)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSimpleEnum(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Textual<?>> rows =
        SELECT(
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(t.enumType).ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(EnumType.EIGHT).ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(t.enumType).ELSE(EnumType.EIGHT).END().AS(new data.ENUM<>(EnumType.class)),
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(t.enumType).ELSE(t.enumType).END().AS(new data.ENUM<>(EnumType.class)),
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(EnumType.EIGHT).ELSE(t.enumType).END().AS(new data.ENUM<>(EnumType.class)),
          CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(t.enumType).ELSE(EnumType.EIGHT).END().AS(new data.ENUM<>(EnumType.class)),
          SELECT(
            CASE(t.enumType).WHEN(EnumType.EIGHT).THEN(t.enumType).ELSE(t.charType).END().AS(new data.CHAR(255, false)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchBoolean(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.BOOLEAN> rows =
        SELECT(
          CASE.WHEN(EQ(t.booleanType, true)).THEN(t.booleanType).ELSE(t.booleanType).END().AS(new data.BOOLEAN()),
          CASE.WHEN(NE(t.booleanType, false)).THEN(data.BOOLEAN.TRUE).ELSE(data.BOOLEAN.FALSE).END().AS(new data.BOOLEAN()),
          CASE.WHEN(EQ(t.booleanType, data.BOOLEAN.TRUE)).THEN(t.booleanType).ELSE(true).END().AS(new data.BOOLEAN()),
          SELECT(
            CASE.WHEN(NE(t.booleanType, false)).THEN(data.BOOLEAN.TRUE).ELSE(t.booleanType).END().AS(new data.BOOLEAN()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.tinyintType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.tinyintType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE((byte)3).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE((short)3).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(3f).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(3L).END().AS(new data.DOUBLE()),
          SELECT(
            CASE.WHEN(LT(t.floatType, 1)).THEN(t.floatType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.tinyintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.tinyintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE((byte)3).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.smallintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE((short)3).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.intType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(3d).ELSE(t.bigintType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(3L).END().AS(new data.DOUBLE()),
          SELECT(
            CASE.WHEN(LT(t.doubleType, 1)).THEN(t.doubleType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final BigDecimal three = new BigDecimal(3);
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3f).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.doubleType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.doubleType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3d).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.tinyintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.tinyintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE((byte)3).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.smallintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.smallintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE((short)3).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.intType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.intType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.bigintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(three).ELSE(t.bigintType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(3L).END().AS(new data.DECIMAL(10, 4)),
          SELECT(
            CASE.WHEN(LT(t.decimalType, 1)).THEN(t.decimalType).ELSE(t.floatType).END().AS(new data.DECIMAL(10, 4)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.tinyintType).END().AS(new data.TINYINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.tinyintType).END().AS(new data.TINYINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE((byte)3).END().AS(new data.TINYINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE((short)3).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN((byte)3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE.WHEN(LT(t.tinyintType, 1)).THEN(t.tinyintType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchMediumInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.floatType).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3f).END().AS(new data.FLOAT()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.tinyintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.tinyintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE((byte)3).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.smallintType).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE((short)3).END().AS(new data.SMALLINT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN((short)3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE.WHEN(LT(t.smallintType, 1)).THEN(t.smallintType).ELSE(t.floatType).END().AS(new data.FLOAT()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.tinyintType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.tinyintType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE((byte)3).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.smallintType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.smallintType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE((short)3).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.intType).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3).END().AS(new data.INT(3)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.intType, 1)).THEN(3).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE.WHEN(LT(t.intType, 1)).THEN(t.intType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Numeric<?>> rows =
        SELECT(
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.floatType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3f).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.doubleType).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3d).END().AS(new data.DOUBLE()),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.decimalType).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(new BigDecimal("3.4")).END().AS(new data.DECIMAL(10, 4)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.tinyintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.tinyintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE((byte)3).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.smallintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.smallintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE((short)3).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.intType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.intType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(3L).ELSE(t.bigintType).END().AS(new data.BIGINT(10)),
          CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(3L).END().AS(new data.BIGINT(10)),
          SELECT(
            CASE.WHEN(LT(t.bigintType, 1)).THEN(t.bigintType).ELSE(t.floatType).END().AS(new data.DOUBLE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchBinary(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.BINARY> rows =
        SELECT(
          CASE.WHEN(NOT(IS.NULL(t.binaryType))).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new data.BINARY(255)),
          CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(new byte[] {0x00, 0x01}).ELSE(t.binaryType).END().AS(new data.BINARY(255)),
          CASE.WHEN(NOT(IS.NULL(t.binaryType))).THEN(t.binaryType).ELSE(new byte[] {0x00, 0x01}).END().AS(new data.BINARY(255)),
          SELECT(
            CASE.WHEN(IS.NOT.NULL(t.binaryType)).THEN(t.binaryType).ELSE(t.binaryType).END().AS(new data.BINARY(255)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.DATE> rows =
        SELECT(
          CASE.WHEN(NOT(IS.NULL(t.dateType))).THEN(t.dateType).ELSE(t.dateType).END().AS(new data.DATE()),
          CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(LocalDate.now()).ELSE(t.dateType).END().AS(new data.DATE()),
          CASE.WHEN(NOT(IS.NULL(t.dateType))).THEN(t.dateType).ELSE(LocalDate.now()).END().AS(new data.DATE()),
          SELECT(
            CASE.WHEN(IS.NOT.NULL(t.dateType)).THEN(t.dateType).ELSE(t.dateType).END().AS(new data.DATE()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.TIME> rows =
        SELECT(
          CASE.WHEN(NOT(IS.NULL(t.timeType))).THEN(t.timeType).ELSE(t.timeType).END().AS(new data.TIME()),
          CASE.WHEN(IS.NOT.NULL(t.timeType)).THEN(LocalTime.now()).ELSE(t.timeType).END().AS(new data.TIME()),
          CASE.WHEN(NOT(IS.NULL(t.timeType))).THEN(t.timeType).ELSE(LocalTime.now()).END().AS(new data.TIME()))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchDateTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.DATETIME> rows =
        SELECT(
          CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new data.DATETIME()),
          CASE.WHEN(NOT(IS.NULL(t.datetimeType))).THEN(LocalDateTime.now()).ELSE(t.datetimeType).END().AS(new data.DATETIME()),
          CASE.WHEN(IS.NOT.NULL(t.datetimeType)).THEN(t.datetimeType).ELSE(LocalDateTime.now()).END().AS(new data.DATETIME()),
          SELECT(
            CASE.WHEN(NOT(IS.NULL(t.datetimeType))).THEN(t.datetimeType).ELSE(t.datetimeType).END().AS(new data.DATETIME()))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<data.CHAR> rows =
        SELECT(
          CASE.WHEN(NOT(IS.NULL(t.charType))).THEN(t.charType).ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(IS.NOT.NULL(t.charType)).THEN("char").ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(NOT(IS.NULL(t.charType))).THEN(t.charType).ELSE("char").END().AS(new data.CHAR(255, false)),
          CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.charType).ELSE(t.enumType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(NOT(IS.NULL(t.charType))).THEN("char").ELSE(t.enumType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(IS.NOT.NULL(t.charType)).THEN(t.enumType).ELSE("char").END().AS(new data.CHAR(255, false)),
          SELECT(
            CASE.WHEN(NOT(IS.NULL(t.charType))).THEN(t.charType).ELSE(t.charType).END().AS(new data.CHAR(255, false)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }

  @Test
  @AssertCommand(ignore = true)
  public void testSearchEnum(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<? extends data.Textual<?>> rows =
        SELECT(
          CASE.WHEN(NOT(IS.NULL(t.enumType))).THEN(t.enumType).ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(EnumType.EIGHT).ELSE(t.charType).END().AS(new data.CHAR(255, false)),
          CASE.WHEN(NOT(IS.NULL(t.enumType))).THEN(t.enumType).ELSE(EnumType.EIGHT).END().AS(new data.ENUM<>(EnumType.class)),
          CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(t.enumType).END().AS(new data.ENUM<>(EnumType.class)),
          CASE.WHEN(NOT(IS.NULL(t.enumType))).THEN(EnumType.EIGHT).ELSE(t.enumType).END().AS(new data.ENUM<>(EnumType.class)),
          CASE.WHEN(IS.NOT.NULL(t.enumType)).THEN(t.enumType).ELSE(EnumType.EIGHT).END().AS(new data.ENUM<>(EnumType.class)),
          SELECT(
            CASE.WHEN(NOT(IS.NULL(t.enumType))).THEN(t.enumType).ELSE(t.charType).END().AS(new data.CHAR(255, false)))
            .FROM(t)
            .LIMIT(1))
          .FROM(t)
          .execute(transaction)
    ) {
      assertTrue(rows.nextRow());
    }
  }
}