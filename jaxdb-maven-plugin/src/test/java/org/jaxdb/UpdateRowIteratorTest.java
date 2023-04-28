package org.jaxdb;

import static org.jaxdb.jsql.TestDML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

import org.jaxdb.jsql.DML.IS;
import org.jaxdb.jsql.QueryConfig;
import org.jaxdb.jsql.RowIterator;
import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.TestCommand.Select.AssertSelect;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
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

@RunWith(SchemaTestRunner.class)
public abstract class UpdateRowIteratorTest {
  @DB(value=Derby.class, parallel=2)
  @DB(SQLite.class)
  public static class IntegrationTest extends UpdateRowIteratorTest {
  }

  @DB(MySQL.class)
  @DB(PostgreSQL.class)
  @DB(Oracle.class)
  public static class RegressionTest extends UpdateRowIteratorTest {
  }

  private static final Random random = new Random();
  private static final QueryConfig queryConfig = new QueryConfig.Builder().withConcurrency(Concurrency.UPDATABLE).build();

  @Test
  @SuppressWarnings("unchecked")
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, PostgreSQL.class, Oracle.class})
  public void testEnum(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.enumType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.ENUM<types.Type.EnumType> value = (data.ENUM<types.Type.EnumType>)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.setFromString("SIX");
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.enumType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(types.Type.EnumType.SIX, ((data.ENUM<types.Type.EnumType>)rows.nextEntity()).get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDate(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final LocalDate now = LocalDate.now();
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.dateType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.DATE value = (data.DATE)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.dateType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(now, ((data.DATE)rows.nextEntity()).get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testTime(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.timeType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.TIME value = (data.TIME)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.timeType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertTrue(ChronoUnit.SECONDS.between(now, ((data.TIME)rows.nextEntity()).get()) <= 1);
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDateTime(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final LocalDateTime now = LocalDateTime.now();
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.datetimeType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.DATETIME value = (data.DATETIME)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.datetimeType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertTrue(ChronoUnit.SECONDS.between(now, ((data.DATETIME)rows.nextEntity()).get()) <= 1);
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testChar(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final String str = "123helloxyz";
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.charType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.CHAR value = (data.CHAR)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(str);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.charType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(str, ((data.CHAR)rows.nextEntity()).get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBoolean(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    try (final RowIterator<?> rows =
      SELECT(t.booleanType, t.id).
      FROM(t).
      WHERE(EQ(t.booleanType, false))
        .execute(transaction, queryConfig)) {

      while (rows.nextRow()) {
        final data.BOOLEAN value = (data.BOOLEAN)rows.nextEntity();
        value.set(true);
        value.update(rows);
        rows.updateRow();
      }
    }

    try (final RowIterator<?> rows =
      SELECT(t.booleanType, t.id).
      FROM(t).
      WHERE(IS.NOT.NULL(t.booleanType))
        .execute(transaction, queryConfig)) {

      while (rows.nextRow())
        assertTrue(((data.BOOLEAN)rows.nextEntity()).getAsBoolean());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBinary(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final byte[] bytes = {1, 2, 3};
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.binaryType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.BINARY value = (data.BINARY)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(bytes);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.binaryType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertArrayEquals(bytes, ((data.BINARY)rows.nextEntity()).get());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDecimal(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.decimalType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.DECIMAL value = (data.DECIMAL)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(BigDecimal.TEN);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.decimalType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(0, BigDecimal.TEN.compareTo(((data.DECIMAL)rows.nextEntity()).get()));
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testTinyInt(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
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
          .execute(transaction, queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.TINYINT)rows.nextEntity()).get().shortValue());
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
          .execute(transaction, queryConfig)) {

        assertTrue(rows.nextRow());

        final data.TINYINT col = (data.TINYINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testSmallInt(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
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
          .execute(transaction, queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.SMALLINT)rows.nextEntity()).get().shortValue());
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
          .execute(transaction, queryConfig)) {

        assertTrue(rows.nextRow());

        final data.SMALLINT col = (data.SMALLINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testInt(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.intType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.INT value = (data.INT)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(919);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.intType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919, ((data.INT)rows.nextEntity()).get().intValue());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=true)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testBigInt(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();
    final int id;
    try (final RowIterator<?> rows =
      SELECT(t.bigintType, t.id).
      FROM(t)
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());

      final data.BIGINT value = (data.BIGINT)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(919L);
      value.update(rows);
      rows.updateRow();
    }

    try (final RowIterator<?> rows =
      SELECT(t.bigintType, t.id).
      FROM(t).
      WHERE(EQ(t.id, id))
        .execute(transaction, queryConfig)) {

      assertTrue(rows.nextRow());
      assertEquals(919L, ((data.BIGINT)rows.nextEntity()).get().longValue());
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testFloat(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    Float value = 0f;
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
          .execute(transaction, queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.FLOAT)rows.nextEntity()).get(), ulp * 100);
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
          .execute(transaction, queryConfig)) {

        assertTrue(rows.nextRow());

        final data.FLOAT col = (data.FLOAT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }

  @Test
  @AssertSelect(isConditionOnlyPrimary=false)
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  public void testDouble(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
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
          .execute(transaction, queryConfig)) {

        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.DOUBLE)rows.nextEntity()).get());
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
          .execute(transaction, queryConfig)) {

        assertTrue(rows.nextRow());

        final data.DOUBLE col = (data.DOUBLE)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();
      }

      testing = true;
    }
  }
}