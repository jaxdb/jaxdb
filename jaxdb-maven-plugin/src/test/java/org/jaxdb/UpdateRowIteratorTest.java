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

@RunWith(VendorSchemaRunner.class)
@VendorSchemaRunner.Schema({classicmodels.class, types.class})
public abstract class UpdateRowIteratorTest {
  @VendorSchemaRunner.Vendor(value=Derby.class, parallel=2)
  @VendorSchemaRunner.Vendor(SQLite.class)
  public static class IntegrationTest extends UpdateRowIteratorTest {
  }

  @VendorSchemaRunner.Vendor(MySQL.class)
  @VendorSchemaRunner.Vendor(PostgreSQL.class)
  @VendorSchemaRunner.Vendor(Oracle.class)
  public static class RegressionTest extends UpdateRowIteratorTest {
  }

  private static final Random random = new Random();
  private static final QueryConfig queryConfig = new QueryConfig.Builder().withConcurrency(Concurrency.UPDATABLE).build();

  @Test
  @SuppressWarnings("unchecked")
  @VendorSchemaRunner.Unsupported({SQLite.class, PostgreSQL.class, Oracle.class})
  public void testEnum() throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.enumType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.ENUM<EnumType> value = (type.ENUM<EnumType>)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(types.Type.EnumType.SIX);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.enumType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(types.Type.EnumType.SIX, ((type.ENUM<EnumType>)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDate() throws IOException, SQLException {
    final LocalDate now = LocalDate.now();
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.dateType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DATE value = (type.DATE)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.dateType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, ((type.DATE)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testTime() throws IOException, SQLException {
    final LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.timeType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.TIME value = (type.TIME)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.timeType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, ((type.TIME)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDateTime() throws IOException, SQLException {
    final LocalDateTime now = LocalDateTime.now();
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.datetimeType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DATETIME value = (type.DATETIME)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.datetimeType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, ((type.DATETIME)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testChar() throws IOException, SQLException {
    final String str = "123helloxyz";
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.charType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.CHAR value = (type.CHAR)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(str);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.charType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(str, ((type.CHAR)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBoolean() throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<?> rows =
      SELECT(t.booleanType, t.id).
      FROM(t).
      WHERE(EQ(t.booleanType, false))
        .execute(queryConfig)) {

      while (rows.nextRow()) {
        final type.BOOLEAN value = (type.BOOLEAN)rows.nextEntity();
        value.set(true);
        value.update(rows);
        rows.updateRow();
      }
    }

    try (final RowIterator<?> rows =
      SELECT(t.booleanType, t.id).
      FROM(t).
      WHERE(IS.NOT.NULL(t.booleanType))
        .execute(queryConfig)) {

      while(rows.nextRow())
        assertTrue(((type.BOOLEAN)rows.nextEntity()).getAsBoolean());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBinary() throws IOException, SQLException {
    final byte[] bytes = {1, 2, 3};
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.binaryType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.BINARY value = (type.BINARY)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(bytes);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.binaryType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertArrayEquals(bytes, ((type.BINARY)rows.nextEntity()).get());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDecimal() throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.decimalType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.DECIMAL value = (type.DECIMAL)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(BigDecimal.TEN);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.decimalType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(0, BigDecimal.TEN.compareTo(((type.DECIMAL)rows.nextEntity()).get()));
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testTinyInt() throws IOException, SQLException {
    byte value = 0;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = (byte)random.nextInt();

      try (final RowIterator<?> rows =
        SELECT(t.tinyintType, t.id).
        FROM(t).
        WHERE(EQ(t.tinyintType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((type.TINYINT)rows.nextEntity()).get().shortValue());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<?> rows =
        SELECT(t.tinyintType, t.id).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.TINYINT col = (type.TINYINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testSmallInt() throws IOException, SQLException {
    short value = 0;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = (short)random.nextInt();

      try (final RowIterator<?> rows =
        SELECT(t.smallintType, t.id).
        FROM(t).
        WHERE(EQ(t.smallintType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((type.SMALLINT)rows.nextEntity()).get().shortValue());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<?> rows =
        SELECT(t.smallintType, t.id).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.SMALLINT col = (type.SMALLINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testInt() throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.intType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.INT value = (type.INT)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(919);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.intType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919, ((type.INT)rows.nextEntity()).get().intValue());
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBigInt() throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;;
    try (final RowIterator<?> rows =
      SELECT(t.bigintType, t.id).
      FROM(t).
      LIMIT(1)
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());

      final type.BIGINT value = (type.BIGINT)rows.nextEntity();
      id = ((type.INT)rows.nextEntity()).getAsInt();
      value.set(919L);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.bigintType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919L, ((type.BIGINT)rows.nextEntity()).get().longValue());
    }
  }

  @Test
  @SuppressWarnings("null")
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testFloat() throws IOException, SQLException {
    Float value = null;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = random.nextFloat();

      final float ulp = Math.ulp(value);
      try (final RowIterator<?> rows =
        SELECT(t.floatType, t.id).
        FROM(t).
        WHERE(AND(GT(t.floatType, value - ulp), LT(t.floatType, value + ulp)))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((type.FLOAT)rows.nextEntity()).get(), ulp * 100);
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<?> rows =
        SELECT(t.floatType, t.id).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.FLOAT col = (type.FLOAT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @VendorSchemaRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDouble() throws IOException, SQLException {
    Double value = null;
    boolean testing = false;
    final types.Type t = types.Type();
    while (true) {
      if (!testing)
        value = random.nextDouble();

      try (final RowIterator<?> rows =
        SELECT(t.doubleType, t.id).
        FROM(t).
        WHERE(EQ(t.doubleType, value))
          .execute(queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((type.DOUBLE)rows.nextEntity()).get());
          break;
        }
        else if (rows.nextRow()) {
          continue;
        }
      }

      try (final RowIterator<?> rows =
        SELECT(t.doubleType, t.id).
        FROM(t).
        LIMIT(1)
          .execute(queryConfig)) {

        assertTrue(rows.nextRow());

        final type.DOUBLE col = (type.DOUBLE)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }
}