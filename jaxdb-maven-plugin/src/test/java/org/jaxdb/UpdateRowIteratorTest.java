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
import org.jaxdb.jsql.QueryConfig.Concurrency;
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
public abstract class UpdateRowIteratorTest {
  @DB(Derby.class)
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
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  @SchemaTestRunner.Unsupported({SQLite.class, PostgreSQL.class, Oracle.class})
  public void testEnum(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.enumType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.ENUM<Types.AbstractType.EnumType> value = (data.ENUM<Types.AbstractType.EnumType>)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.setFromString("SIX");
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.enumType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(Types.AbstractType.EnumType.SIX, ((data.ENUM<Types.AbstractType.EnumType>)rows.nextEntity()).get());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testDate(final Types types, final Transaction transaction) throws IOException, SQLException {
    final LocalDate now = LocalDate.now();
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.dateType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.DATE value = (data.DATE)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.dateType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(now, ((data.DATE)rows.nextEntity()).get());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final LocalTime now = LocalTime.now().truncatedTo(ChronoUnit.SECONDS);
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.timeType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.TIME value = (data.TIME)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.timeType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(ChronoUnit.SECONDS.between(now, ((data.TIME)rows.nextEntity()).get()) <= 1);
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testDateTime(final Types types, final Transaction transaction) throws IOException, SQLException {
    final LocalDateTime now = LocalDateTime.now();
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.datetimeType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.DATETIME value = (data.DATETIME)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(now);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.datetimeType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertTrue(ChronoUnit.SECONDS.between(now, ((data.DATETIME)rows.nextEntity()).get()) <= 1);
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testChar(final Types types, final Transaction transaction) throws IOException, SQLException {
    final String str = "123helloxyz";
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.charType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.CHAR value = (data.CHAR)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(str);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.charType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(str, ((data.CHAR)rows.nextEntity()).get());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testBoolean(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    try (
      final RowIterator<?> rows =
        SELECT(t.booleanType, t.id)
          .FROM(t)
          .WHERE(EQ(t.booleanType, false))
          .execute(transaction, queryConfig)
    ) {
      while (rows.nextRow()) {
        final data.BOOLEAN value = (data.BOOLEAN)rows.nextEntity();
        value.set(true);
        value.update(rows);
        rows.updateRow();
      }
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.booleanType, t.id)
          .FROM(t)
          .WHERE(IS.NOT.NULL(t.booleanType))
          .execute(transaction, queryConfig)
    ) {
      while (rows.nextRow())
        assertTrue(((data.BOOLEAN)rows.nextEntity()).getAsBoolean());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testBinary(final Types types, final Transaction transaction) throws IOException, SQLException {
    final byte[] bytes = {1, 2, 3};
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.binaryType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.BINARY value = (data.BINARY)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(bytes);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.binaryType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertArrayEquals(bytes, ((data.BINARY)rows.nextEntity()).get());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testDecimal(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.decimalType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.DECIMAL value = (data.DECIMAL)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(BigDecimal.TEN);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.decimalType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(0, BigDecimal.TEN.compareTo(((data.DECIMAL)rows.nextEntity()).get()));
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testTinyInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    byte value = 0;
    boolean testing = false;
    final Types.Type t = types.Type$;
    while (true) {
      if (!testing)
        value = (byte)random.nextInt();

      try (
        final RowIterator<?> rows =
          SELECT(t.tinyintType, t.id)
            .FROM(t)
            .WHERE(EQ(t.tinyintType, value))
            .LIMIT(100)
            .execute(transaction, queryConfig)
      ) {
        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.TINYINT)rows.nextEntity()).get().shortValue());
          while (rows.nextRow());
          break;
        }
        else if (rows.nextRow()) {
          while (rows.nextRow());
          continue;
        }
      }

      try (
        final RowIterator<?> rows =
          SELECT(t.tinyintType, t.id)
            .FROM(t)
            .LIMIT(1)
            .execute(transaction, queryConfig)
      ) {
        assertTrue(rows.nextRow());

        final data.TINYINT col = (data.TINYINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();

        assertFalse(rows.nextRow());
      }

      testing = true;
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testSmallInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    short value = 0;
    boolean testing = false;
    final Types.Type t = types.Type$;
    while (true) {
      if (!testing)
        value = (short)random.nextInt();

      try (
        final RowIterator<?> rows =
          SELECT(t.smallintType, t.id)
            .FROM(t)
            .WHERE(EQ(t.smallintType, value))
            .LIMIT(100)
            .execute(transaction, queryConfig)
      ) {
        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.SMALLINT)rows.nextEntity()).get().shortValue());
          while (rows.nextRow());
          break;
        }
        else if (rows.nextRow()) {
          while (rows.nextRow());
          continue;
        }
      }

      try (
        final RowIterator<?> rows =
          SELECT(t.smallintType, t.id)
            .FROM(t)
            .LIMIT(1)
            .execute(transaction, queryConfig)
      ) {
        assertTrue(rows.nextRow());

        final data.SMALLINT col = (data.SMALLINT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();

        assertFalse(rows.nextRow());
      }

      testing = true;
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.intType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.INT value = (data.INT)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(919);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.intType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(919, ((data.INT)rows.nextEntity()).getAsInt());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = false)
  public void testBigInt(final Types types, final Transaction transaction) throws IOException, SQLException {
    final Types.Type t = types.Type$;
    final int id;
    try (
      final RowIterator<?> rows =
        SELECT(t.bigintType, t.id)
          .FROM(t)
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());

      final data.BIGINT value = (data.BIGINT)rows.nextEntity();
      id = ((data.INT)rows.nextEntity()).getAsInt();
      value.set(919L);
      value.update(rows);
      rows.updateRow();
    }

    try (
      final RowIterator<?> rows =
        SELECT(t.bigintType, t.id)
          .FROM(t)
          .WHERE(EQ(t.id, id))
          .execute(transaction, queryConfig)
    ) {
      assertTrue(rows.nextRow());
      assertEquals(919L, ((data.BIGINT)rows.nextEntity()).get().longValue());
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testFloat(final Types types, final Transaction transaction) throws IOException, SQLException {
    Float value = 0f;
    boolean testing = false;
    final Types.Type t = types.Type$;
    while (true) {
      if (!testing)
        value = random.nextFloat();

      final float ulp = Math.ulp(value);
      try (
        final RowIterator<?> rows =
          SELECT(t.floatType, t.id)
            .FROM(t)
            .WHERE(AND(
              GT(t.floatType, value - ulp * 100),
              LT(t.floatType, value + ulp * 100)))
            .LIMIT(100)
            .execute(transaction, queryConfig)
      ) {
        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.FLOAT)rows.nextEntity()).get(), ulp * 100);
          while (rows.nextRow());
          break;
        }
        else if (rows.nextRow()) {
          while (rows.nextRow());
          continue;
        }
      }

      try (
        final RowIterator<?> rows =
          SELECT(t.floatType, t.id)
            .FROM(t)
            .LIMIT(1)
            .execute(transaction, queryConfig)
      ) {
        assertTrue(rows.nextRow());

        final data.FLOAT col = (data.FLOAT)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();

        assertFalse(rows.nextRow());
      }

      testing = true;
    }
  }

  @Test
  @SchemaTestRunner.Unsupported({SQLite.class, Oracle.class})
  @AssertSelect(cacheSelectEntity = false, rowIteratorFullConsume = true)
  public void testDouble(final Types types, final Transaction transaction) throws IOException, SQLException {
    Double value = 0d;
    boolean testing = false;
    final Types.Type t = types.Type$;
    while (true) {
      if (!testing)
        value = random.nextDouble();

      final double ulp = Math.ulp(value);
      try (
        final RowIterator<?> rows =
          SELECT(t.doubleType, t.id)
            .FROM(t)
            .WHERE(AND(
              GT(t.doubleType, value - ulp * 100),
              LT(t.doubleType, value + ulp * 100)))
            .LIMIT(100)
            .execute(transaction, queryConfig)
      ) {
        if (testing) {
          assertTrue(rows.nextRow());
          assertEquals(value, ((data.DOUBLE)rows.nextEntity()).get(), ulp * 100);
          while (rows.nextRow());
          break;
        }
        else if (rows.nextRow()) {
          while (rows.nextRow());
          continue;
        }
      }

      try (
        final RowIterator<?> rows =
          SELECT(t.doubleType, t.id)
            .FROM(t)
            .LIMIT(1)
            .execute(transaction, queryConfig)
      ) {
        assertTrue(rows.nextRow());

        final data.DOUBLE col = (data.DOUBLE)rows.nextEntity();
        col.set(value);
        col.update(rows);
        rows.updateRow();

        assertFalse(rows.nextRow());
      }

      testing = true;
    }
  }
}