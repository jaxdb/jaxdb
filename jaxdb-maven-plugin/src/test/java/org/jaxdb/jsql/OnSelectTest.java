/* Copyright (c) 2023 JAX-DB
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

package org.jaxdb.jsql;

import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.runner.DBTestRunner.Unsupported;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.jaxdb.runner.Vendor;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.xml.sax.SAXException;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class OnSelectTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends OnSelectTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends OnSelectTest {
  }

  @Test
  @Spec(order = 0)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void setUp(@Schema(classicmodels.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.err.flush();
      System.exit(1);
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    DDLxTest.recreateSchema(transaction.getConnection(), "classicmodels");
    transaction.commit();

    final Connector connector = transaction.getConnector();
    final Database database = TestDatabase.global(transaction.getSchemaClass());
    database.configCache(connector, new DefaultCache() {
      @Override
      protected Connector getConnector() {
        return connector;
      }

      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), c -> c.with(classicmodels.getTables()));
  }

  private static void testTreeSingle(final NavigableMap<data.Key,classicmodels.Office> map, final boolean selectCalled) throws IOException, SQLException {
    for (int i = 1; i <= 7; ++i) { // [N]
      assertFalse(TestDatabase.called());
      final classicmodels.Office o = classicmodels.Office.officeCodeToOffice(i);
      assertEquals(selectCalled, TestDatabase.called());
      assertEquals(selectCalled ? i : 7, map.size());
      assertEquals(i, o.officeCode.getAsInt());
    }
  }

  @Test
  @Spec(order = 1)
  public void testTreeSingle(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final NavigableMap<data.Key,classicmodels.Office> map = classicmodels.Office.officeCodeToOffice();

    assertFalse(TestDatabase.called());
    assertEquals(0, map.size());

    classicmodels.Office o = classicmodels.Office.officeCodeToOffice(0);

    assertTrue(TestDatabase.called());
    assertEquals(0, map.size());
    assertNull(o);

    testTreeSingle(map, true);
    testTreeSingle(map, false);
  }

  private static void testTreeRange(final NavigableMap<data.Key,classicmodels.Office> map, final boolean selectCalled) throws IOException, SQLException {
    assertFalse(TestDatabase.called());
    SortedMap<data.Key,classicmodels.Office> sub = classicmodels.Office.officeCodeToOffice(-3, 2);
    assertEquals(selectCalled, TestDatabase.called());
    assertEquals(selectCalled ? 1 : 7, map.size());
    assertEquals(1, sub.size());
    assertEquals(1, sub.values().iterator().next().officeCode.getAsInt());

    assertFalse(TestDatabase.called());
    sub = classicmodels.Office.officeCodeToOffice(1, 4);
    assertEquals(selectCalled, TestDatabase.called());;
    assertEquals(selectCalled ? 3 : 7, map.size());
    assertEquals(3, sub.size());
    Iterator<classicmodels.Office> iterator = sub.values().iterator();
    for (int i = 1; i <= 3; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());

    assertFalse(TestDatabase.called());
    sub = classicmodels.Office.officeCodeToOffice(5, 9);
    assertEquals(selectCalled, TestDatabase.called());;
    assertEquals(selectCalled ? 6 : 7, map.size());
    assertEquals(3, sub.size());
    iterator = sub.values().iterator();
    for (int i = 5; i <= 7; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());

    assertFalse(TestDatabase.called());
    sub = classicmodels.Office.officeCodeToOffice(0, 20);
    assertEquals(selectCalled, TestDatabase.called());;
    assertEquals(7, map.size());
    assertEquals(7, sub.size());
    iterator = sub.values().iterator();
    for (int i = 1; i <= 7; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());
  }

  @Test
  @Spec(order = 2)
  public void testTreeRange(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final NavigableMap<data.Key,classicmodels.Office> map = classicmodels.Office.officeCodeToOffice();

    assertFalse(TestDatabase.called());
    assertEquals(0, map.size());

    SortedMap<data.Key,classicmodels.Office> sub = classicmodels.Office.officeCodeToOffice(-5, 1);

    assertTrue(TestDatabase.called());
    assertEquals(0, map.size());
    assertEquals(0, sub.size());

    testTreeRange(map, true);
    testTreeRange(map, false);
  }

  private static final String[] productLines = {"Classic Cars", "Motorcycles", "Planes", "Ships", "Trains", "Trucks and Buses", "Vintage Cars"};
  private static final int noProductLines = productLines.length;

  private static void testHashSingle(final Map<data.Key,classicmodels.ProductLine> map, final boolean selectCalled) throws IOException, SQLException {
    for (int i = 0; i < noProductLines;) { // [N]
      assertFalse(TestDatabase.called());
      final String productLine = productLines[i];
      classicmodels.ProductLine pl = classicmodels.ProductLine.productLineToProductLine(productLine);
      assertTrue(TestDatabase.called());
      ++i;
      assertEquals(selectCalled ? i : noProductLines, map.size());
      assertEquals(productLine, pl.productLine.get());
    }
  }

  @Test
  @Spec(order = 1)
  public void testHashSingle(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final Map<data.Key,classicmodels.ProductLine> map = classicmodels.ProductLine.productLineToProductLine();

    assertFalse(TestDatabase.called());
    assertEquals(0, map.size());

    classicmodels.ProductLine pl = classicmodels.ProductLine.productLineToProductLine("foo");

    assertTrue(TestDatabase.called());
    assertEquals(0, map.size());
    assertNull(pl);

    testHashSingle(map, true);
    testHashSingle(map, false);
  }

  @AfterClass
  public static void recreateSchema(@Schema(classicmodels.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), "classicmodels");
  }
}