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
import org.jaxdb.jsql.Database.OnConnectPreLoad;
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
    SQLxTest.loadData(transaction.getConnection(), "classicmodels");
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
    }, new ConcurrentLinkedQueue<>(), c -> c
      .with(OnConnectPreLoad.ALL, classicmodels.Product(), classicmodels.Employee())
      .with(classicmodels.Customer(), classicmodels.Office(), classicmodels.ProductLine(), classicmodels.Purchase(), classicmodels.PurchaseDetail()));

    assertTrue(TestDatabase.called());
  }

  private static void testTreeSingle(final NavigableMap<data.Key,classicmodels.Customer> map, final ArrayList<Short> customerNumbers, final boolean selectCalled) throws IOException, SQLException {
    for (int i = 0, i$ = customerNumbers.size(); i < i$;) { // [RA]
      assertFalse(TestDatabase.called());
      final short customerNumber = customerNumbers.get(i);
      final classicmodels.Customer c = classicmodels.Customer.customerNumberToCustomer(customerNumber);
      assertEquals(selectCalled, TestDatabase.called());
      ++i;
      assertEquals(selectCalled ? i : i$, map.size());
      assertEquals(customerNumber, c.customerNumber.getAsShort());
    }
  }

  @Test
  @Spec(order = 1)
  public void testTreeSingle(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    classicmodels.Customer c = classicmodels.Customer();
    final ArrayList<Short> customerNumbers = new ArrayList<>();
    try (final RowIterator<data.SMALLINT> rows =

      SELECT(c.customerNumber).
      FROM(c).
      ORDER_BY(c.customerNumber)
        .execute(transaction)) {

      while (rows.nextRow())
        customerNumbers.add(rows.nextEntity().get());
    }

    final NavigableMap<data.Key,classicmodels.Customer> map = classicmodels.Customer.customerNumberToCustomer();

    assertFalse(TestDatabase.called());
    assertEquals(0, map.size());

    c = classicmodels.Customer.customerNumberToCustomer((short)0);

    assertTrue(TestDatabase.called());
    assertEquals(0, map.size());
    assertNull(c);

    testTreeSingle(map, customerNumbers, true);
    testTreeSingle(map, customerNumbers, false);
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

    final SortedMap<data.Key,classicmodels.Office> sub = classicmodels.Office.officeCodeToOffice(-5, 1);

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
      final classicmodels.ProductLine pl = classicmodels.ProductLine.productLineToProductLine(productLine);
      assertEquals(selectCalled, TestDatabase.called());
      ++i;
      assertEquals(selectCalled ? i : noProductLines, map.size());
      assertEquals(productLine, pl.productLine.get());
    }
  }

  @Test
  @Spec(order = 3)
  public void testHashSingle(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final Map<data.Key,classicmodels.ProductLine> map = classicmodels.ProductLine.productLineToProductLine();

    assertFalse(TestDatabase.called());
    assertEquals(0, map.size());

    final classicmodels.ProductLine pl = classicmodels.ProductLine.productLineToProductLine("foo");

    assertTrue(TestDatabase.called());
    assertEquals(0, map.size());
    assertNull(pl);

    testHashSingle(map, true);
    testHashSingle(map, false);
  }

  @Test
  @Spec(order = 4)
  public void testOnConnectPreLoadAllHash(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Product p = classicmodels.Product();
    try (final RowIterator<data.BIGINT> rows =
      SELECT(COUNT(p)).
      FROM(p)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().getAsLong(), classicmodels.Product.codeToProduct().size());

      for (final classicmodels.Product pr : classicmodels.Product.codeToProduct().values())
        assertNotNull(pr.productLine$ProductLine_productLine());
    }
  }

  @Test
  @Spec(order = 5)
  public void testOnConnectPreLoadAllTree(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    final classicmodels.Employee e = classicmodels.Employee();
    try (final RowIterator<data.BIGINT> rows =

      SELECT(COUNT(e)).
      FROM(e)
        .execute(transaction)) {

      assertTrue(rows.nextRow());
      assertEquals(rows.nextEntity().getAsLong(), classicmodels.Employee.employeeNumberToEmployee().size());

      for (final classicmodels.Employee em : classicmodels.Employee.employeeNumberToEmployee().values())
        assertNotNull(em.officeCode$Office_officeCode());
    }
  }

  @Test
  @Spec(order = 6)
  public void testTreeTwoDimensions(@Schema(classicmodels.class) final Transaction transaction) throws IOException, SQLException {
    try {
      classicmodels.PurchaseDetail.purchaseNumber$productCodeToPurchaseDetail(10100, 10114, "S10_1678", "S10_1950");
      fail("Expected UnsupportedOperationException");
    }
    catch (final UnsupportedOperationException e) {
    }

    assertFalse(TestDatabase.called());
    classicmodels.PurchaseDetail.purchaseNumber$productCodeToPurchaseDetail(10110, "S24_3969");
    assertTrue(TestDatabase.called());
    final classicmodels.PurchaseDetail pd0 = classicmodels.PurchaseDetail.purchaseNumber$productCodeToPurchaseDetail(10110, "S24_3969");
    assertFalse(TestDatabase.called());
    assertNotNull(pd0.productCode$Product_code());
    assertFalse(TestDatabase.called());
    classicmodels.Purchase p = pd0.purchaseNumber$Purchase_purchaseNumber();
    assertNotNull(p);
    assertTrue(TestDatabase.called());

    assertFalse(TestDatabase.called());
    final Map<data.Key,classicmodels.PurchaseDetail> map = p.purchaseNumber$PurchaseDetail_purchaseNumber();
    assertTrue(TestDatabase.called());
    assertEquals(16, map.size());
    p.purchaseNumber$PurchaseDetail_purchaseNumber();
    assertFalse(TestDatabase.called());

    for (final classicmodels.PurchaseDetail pd1 : map.values()) {
      assertSame(p, pd1.purchaseNumber$Purchase_purchaseNumber());
      assertFalse(TestDatabase.called());
    }

    assertNotNull(p.customerNumber$Customer_customerNumber());
    assertFalse(TestDatabase.called());
  }

  @AfterClass
  public static void recreateSchema(@Schema(classicmodels.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), "classicmodels");
  }
}