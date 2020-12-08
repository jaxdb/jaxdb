package org.jaxdb;

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.jaxdb.ddlx.runner.Derby;
import org.jaxdb.ddlx.runner.MySQL;
import org.jaxdb.ddlx.runner.Oracle;
import org.jaxdb.ddlx.runner.PostgreSQL;
import org.jaxdb.ddlx.runner.SQLite;
import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.QueryConfig;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.classicmodels;
import org.jaxdb.jsql.type;
import org.jaxdb.jsql.types;
import org.jaxdb.jsql.types.Type.EnumType;
import org.jaxdb.runner.VendorSchemaRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

public abstract class UpdateRowIteratorTest {
  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({Derby.class, SQLite.class})
  public static class IntegrationTest extends UpdateRowIteratorTest {
  }

  @RunWith(VendorSchemaRunner.class)
  @VendorSchemaRunner.Schema({classicmodels.class, types.class})
  @VendorSchemaRunner.Vendor({MySQL.class, PostgreSQL.class, Oracle.class})
  public static class RegressionTest extends UpdateRowIteratorTest {
  }

  private static final Random random = new Random();
  private static final QueryConfig queryConfig = new QueryConfig.Builder().withConcurrency(Concurrency.UPDATABLE).build();

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testEnum() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<type.ENUM<EnumType>> rows =
      SELECT(t.enumType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.ENUM<EnumType> value = rows.nextEntity();
      value.set(types.Type.EnumType.SIX);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.ENUM<EnumType>> rows =
      SELECT(t.enumType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(types.Type.EnumType.SIX, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testDate() throws IOException, SQLException {
    final LocalDate now = LocalDate.now();
    final types.Type t = types.Type();
    try (final RowIterator<type.DATE> rows =
      SELECT(t.dateType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DATE value = rows.nextEntity();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.DATE> rows =
      SELECT(t.dateType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testTime() throws IOException, SQLException {
    final LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    final types.Type t = types.Type();
    try (final RowIterator<type.TIME> rows =
      SELECT(t.timeType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.TIME value = rows.nextEntity();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.TIME> rows =
      SELECT(t.timeType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testDateTime() throws IOException, SQLException {
    final LocalDateTime now = LocalDateTime.now();
    final types.Type t = types.Type();
    try (final RowIterator<type.DATETIME> rows =
      SELECT(t.datetimeType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DATETIME value = rows.nextEntity();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.DATETIME> rows =
      SELECT(t.datetimeType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testChar() throws IOException, SQLException {
    final String str = "123helloxyz";
    final types.Type t = types.Type();
    try (final RowIterator<type.CHAR> rows =
      SELECT(t.charType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.CHAR value = rows.nextEntity();
      value.set(str);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.CHAR> rows =
      SELECT(t.charType).
      FROM(t).
      WHERE(EQ(t.charType, str)). // Select it directly, otherwise some other row may be returned
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(str, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testBoolean() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(t.booleanType).
      FROM(t).
      WHERE(EQ(t.booleanType, false))
        .execute(queryConfig)) {

      while (rows.nextRow()) {
        final type.BOOLEAN value = rows.nextEntity();
        value.set(true);
        value.update(rows);
        rows.updateRow();
      }
    }

    try (final RowIterator<type.BOOLEAN> rows =
      SELECT(t.booleanType).
      FROM(t).
      WHERE(IS.NOT.NULL(t.booleanType))
        .execute(queryConfig)) {

      while(rows.nextRow())
        assertTrue(rows.nextEntity().getAsBoolean());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testBinary() throws IOException, SQLException {
    final byte[] bytes = {1, 2, 3};
    final types.Type t = types.Type();
    try (final RowIterator<type.BINARY> rows =
      SELECT(t.binaryType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.BINARY value = rows.nextEntity();
      value.set(bytes);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.BINARY> rows =
      SELECT(t.binaryType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertArrayEquals(bytes, rows.nextEntity().get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testDecimal() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<type.DECIMAL> rows =
      SELECT(t.decimalType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DECIMAL value = rows.nextEntity();
      value.set(BigDecimal.TEN);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.DECIMAL> rows =
      SELECT(t.decimalType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(0, BigDecimal.TEN.compareTo(rows.nextEntity().get()));
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testTinyInt() throws IOException, SQLException {
    byte value = 0;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = (byte)random.nextInt();

      try (final RowIterator<type.TINYINT> rows =
        SELECT(t.tinyintType).
        FROM(t).
        WHERE(EQ(t.tinyintType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, rows.nextEntity().get().shortValue());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<type.TINYINT> rows =
        SELECT(t.tinyintType).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.TINYINT col = rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testSmallInt() throws IOException, SQLException {
    short value = 0;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = (short)random.nextInt();

      try (final RowIterator<type.SMALLINT> rows =
        SELECT(t.smallintType).
        FROM(t).
        WHERE(EQ(t.smallintType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, rows.nextEntity().get().shortValue());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<type.SMALLINT> rows =
        SELECT(t.smallintType).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.SMALLINT col = rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testInt() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<type.INT> rows =
      SELECT(t.intType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.INT value = rows.nextEntity();
      value.set(919);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.INT> rows =
      SELECT(t.intType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919, rows.nextEntity().get().intValue());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testBigInt() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<type.BIGINT> rows =
      SELECT(t.bigintType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.BIGINT value = rows.nextEntity();
      value.set(919L);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<type.BIGINT> rows =
      SELECT(t.bigintType).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919L, rows.nextEntity().get().longValue());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testFloat() throws IOException, SQLException {
    Float value = null;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = random.nextFloat();

      try (final RowIterator<type.FLOAT> rows =
        SELECT(t.floatType).
        FROM(t).
        WHERE(EQ(t.floatType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, rows.nextEntity().get());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<type.FLOAT> rows =
        SELECT(t.floatType).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.FLOAT col = rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported(SQLite.class)
  public void testDouble() throws IOException, SQLException {
    Double value = null;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = random.nextDouble();

      try (final RowIterator<type.DOUBLE> rows =
        SELECT(t.doubleType).
        FROM(t).
        WHERE(EQ(t.doubleType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, rows.nextEntity().get());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<type.DOUBLE> rows =
        SELECT(t.doubleType).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.DOUBLE col = rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }
}