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

import static org.jaxdb.jsql.DML.*;
import static org.junit.Assert.*;

import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.NavigableMap;
import java.util.SortedMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.CacheConfig.OnConnectPreLoad;
import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.TestSpec;
import org.jaxdb.runner.DBTestRunner.Unsupported;
import org.jaxdb.runner.Derby;
import org.jaxdb.runner.MySQL;
import org.jaxdb.runner.Oracle;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SQLite;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.sqlx.SQLxTest;
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
  @TestSpec(order = 0)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void setUp(final Classicmodels classicmodels) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    final UncaughtExceptionHandler uncaughtExceptionHandler = (final Thread t, final Throwable e) -> {
      e.printStackTrace();
      System.err.flush();
      System.exit(1);
    };
    Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);

    final Connector connector = classicmodels.getConnector();
    try (final Connection connection = connector.getConnection(null)) {
      DDLxTest.recreateSchema(connection, "classicmodels");
      SQLxTest.loadData(connection, "classicmodels");
    }

    classicmodels.configCache(new DefaultCache(classicmodels) {
      @Override
      public void onFailure(final String sessionId, final long timestamp, final data.Table table, final Exception e) {
        uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), e);
      }
    }, new ConcurrentLinkedQueue<>(), c -> c
      .with(OnConnectPreLoad.ALL, classicmodels.Product$, classicmodels.Employee$)
      .with(classicmodels.Customer$, classicmodels.Office$, classicmodels.ProductLine$, classicmodels.Purchase$, classicmodels.PurchaseDetail$));

    assertTrue(TestConnectionFactory.called());
  }

  private static void testTreeSingle(final Classicmodels classicmodels, final NavigableMap<data.Key,Classicmodels.Customer> map, final ArrayList<Short> customerNumbers, final boolean selectCalled) throws IOException, SQLException {
    for (int i = 0, i$ = customerNumbers.size(); i < i$;) { // [RA]
      assertFalse(TestConnectionFactory.called());
      final short customerNumber = customerNumbers.get(i);
      final Classicmodels.Customer c = classicmodels.Customer$.customerNumberToCustomer(customerNumber);
      assertEquals(selectCalled, TestConnectionFactory.called());
      ++i;
      assertEquals(selectCalled ? i : i$, map.size());
      assertEquals(customerNumber, c.customerNumber.getAsShort());
    }
  }

  @Test
  @TestSpec(order = 1)
  public void testTreeSingle(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    Classicmodels.Customer c = classicmodels.Customer$;
    final ArrayList<Short> customerNumbers = new ArrayList<>();
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(c.customerNumber).
      FROM(c).
      ORDER_BY(c.customerNumber)
        .execute(transaction)) {

      while (rows.nextRow())
        customerNumbers.add(rows.nextEntity().get());
    }

    final NavigableMap<data.Key,Classicmodels.Customer> map = classicmodels.Customer$._customerNumberToCustomerMap$;
    assertEquals(0, map.size());

    c = classicmodels.Customer$.customerNumberToCustomer((short)0);

    assertTrue(TestConnectionFactory.called());
    assertEquals(0, map.size());
    assertNull(c);

    testTreeSingle(classicmodels, map, customerNumbers, true);
    testTreeSingle(classicmodels, map, customerNumbers, false);
  }

  private static void testTreeRange(final Classicmodels classicmodels, final NavigableMap<data.Key,Classicmodels.Office> map, final boolean selectCalled) throws IOException, SQLException {
    assertFalse(TestConnectionFactory.called());
    SortedMap<data.Key,Classicmodels.Office> sub = classicmodels.Office$.officeCodeToOffice(-3, 2);
    assertEquals(selectCalled, TestConnectionFactory.called());
    assertEquals(selectCalled ? 1 : 7, map.size());
    assertEquals(1, sub.size());
    assertEquals(1, sub.values().iterator().next().officeCode.getAsInt());

    assertFalse(TestConnectionFactory.called());
    sub = classicmodels.Office$.officeCodeToOffice(1, 4);
    assertEquals(selectCalled, TestConnectionFactory.called());;
    assertEquals(selectCalled ? 3 : 7, map.size());
    assertEquals(3, sub.size());
    Iterator<Classicmodels.Office> iterator = sub.values().iterator();
    for (int i = 1; i <= 3; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());

    assertFalse(TestConnectionFactory.called());
    sub = classicmodels.Office$.officeCodeToOffice(5, 9);
    assertEquals(selectCalled, TestConnectionFactory.called());;
    assertEquals(selectCalled ? 6 : 7, map.size());
    assertEquals(3, sub.size());
    iterator = sub.values().iterator();
    for (int i = 5; i <= 7; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());

    assertFalse(TestConnectionFactory.called());
    sub = classicmodels.Office$.officeCodeToOffice(0, 20);
    assertEquals(selectCalled, TestConnectionFactory.called());;
    assertEquals(7, map.size());
    assertEquals(7, sub.size());
    iterator = sub.values().iterator();
    for (int i = 1; i <= 7; ++i) // [N]
      assertEquals(i, iterator.next().officeCode.getAsInt());
  }

  @Test
  @TestSpec(order = 2)
  public void testTreeRange(final Classicmodels classicmodels) throws IOException, SQLException {
    final NavigableMap<data.Key,Classicmodels.Office> map = classicmodels.Office$._officeCodeToOfficeMap$;
    assertEquals(0, map.size());

    final SortedMap<data.Key,Classicmodels.Office> sub = classicmodels.Office$.officeCodeToOffice(-5, 1);

    assertTrue(TestConnectionFactory.called());
    assertEquals(0, map.size());
    assertEquals(0, sub.size());

    testTreeRange(classicmodels, map, true);
    testTreeRange(classicmodels, map, false);
  }

  private static final String[] productLines = {"Classic Cars", "Motorcycles", "Planes", "Ships", "Trains", "Trucks and Buses", "Vintage Cars"};
  private static final int noProductLines = productLines.length;

  private static void testHashSingle(final Classicmodels classicmodels, final Map<data.Key,Classicmodels.ProductLine> map, final boolean selectCalled) throws IOException, SQLException {
    for (int i = 0; i < noProductLines;) { // [N]
      assertFalse(TestConnectionFactory.called());
      final String productLine = productLines[i];
      final Classicmodels.ProductLine pl = classicmodels.ProductLine$.productLineToProductLine(productLine);
      assertEquals(selectCalled, TestConnectionFactory.called());
      ++i;
      assertEquals(selectCalled ? i : noProductLines, map.size());
      assertEquals(productLine, pl.productLine.get());
    }
  }

  @Test
  @TestSpec(order = 3)
  public void testHashSingle(final Classicmodels classicmodels) throws IOException, SQLException {
    final Map<data.Key,Classicmodels.ProductLine> map = classicmodels.ProductLine$._productLineToProductLineMap$;
    assertEquals(0, map.size());

    final Classicmodels.ProductLine pl = classicmodels.ProductLine$.productLineToProductLine("foo");

    assertTrue(TestConnectionFactory.called());
    assertEquals(0, map.size());
    assertNull(pl);

    testHashSingle(classicmodels, map, true);
    testHashSingle(classicmodels, map, false);
  }

  @Test
  @TestSpec(order = 4)
  public void testOnConnectPreLoadAllHash(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Product p = classicmodels.Product$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(COUNT(p)).
      FROM(p)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().getAsLong(), classicmodels.Product$.codeToProduct().size());

      for (final Classicmodels.Product pr : classicmodels.Product$.codeToProduct().values()) // [C]
        assertNotNull(pr.productLine$ProductLine_productLine());
    }
  }

  @Test
  @TestSpec(order = 5)
  public void testOnConnectPreLoadAllTree(final Classicmodels classicmodels, final Transaction transaction) throws IOException, SQLException {
    final Classicmodels.Employee e = classicmodels.Employee$;
    try (final RowIterator<data.BIGINT> rows =

      SELECT(COUNT(e)).
      FROM(e)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().getAsLong(), classicmodels.Employee$.employeeNumberToEmployee().size());

      for (final Classicmodels.Employee em : classicmodels.Employee$.employeeNumberToEmployee().values()) // [C]
        assertNotNull(em.officeCode$Office_officeCode());
    }
  }

  @Test
  @TestSpec(order = 6)
  public void testTreeTwoDimensions(final Classicmodels classicmodels) throws IOException, SQLException {
    try {
      classicmodels.PurchaseDetail$.purchaseNumber$productCodeToPurchaseDetail(10100, 10114, "S10_1678", "S10_1950");
      fail("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e) {
    }

    assertFalse(TestConnectionFactory.called());
    classicmodels.PurchaseDetail$.purchaseNumber$productCodeToPurchaseDetail(10110, "S24_3969");
    assertTrue(TestConnectionFactory.called());
    final Classicmodels.PurchaseDetail pd0 = classicmodels.PurchaseDetail$.purchaseNumber$productCodeToPurchaseDetail(10110, "S24_3969");
    assertFalse(TestConnectionFactory.called());
    assertNotNull(pd0.productCode$Product_code());
    assertFalse(TestConnectionFactory.called());
    Classicmodels.Purchase p = pd0.purchaseNumber$Purchase_purchaseNumber();
    assertNotNull(p);
    assertTrue(TestConnectionFactory.called());

    assertFalse(TestConnectionFactory.called());
    final Map<data.Key,Classicmodels.PurchaseDetail> map = p.purchaseNumber$PurchaseDetail_purchaseNumber();
    assertTrue(TestConnectionFactory.called());
    assertEquals(16, map.size());
    p.purchaseNumber$PurchaseDetail_purchaseNumber();
    assertFalse(TestConnectionFactory.called());

    for (final Classicmodels.PurchaseDetail pd1 : map.values()) { // [C]
      assertSame(p, pd1.purchaseNumber$Purchase_purchaseNumber());
      assertFalse(TestConnectionFactory.called());
    }

    assertNotNull(p.customerNumber$Customer_customerNumber());
    assertFalse(TestConnectionFactory.called());
  }

  @AfterClass
  public static void recreateSchema(final Classicmodels classicmodels, final Transaction transaction) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), classicmodels.getName());
  }
}