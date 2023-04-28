/* Copyright (c) 2021 JAX-DB
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

import static org.jaxdb.jsql.Notification.Action.DELETE;
import static org.jaxdb.jsql.Notification.Action.INSERT;
import static org.jaxdb.jsql.Notification.Action.UPDATE;
import static org.jaxdb.jsql.TestDML.*;
import static org.jaxdb.jsql.TestDML.DELETE;
import static org.jaxdb.jsql.TestDML.INSERT;
import static org.jaxdb.jsql.TestDML.UPDATE;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.xml.transform.TransformerException;

import org.jaxdb.ddlx.DDLxTest;
import org.jaxdb.ddlx.GeneratorExecutionException;
import org.jaxdb.jsql.Connector;
import org.jaxdb.jsql.Database;
import org.jaxdb.jsql.DefaultCache;
import org.jaxdb.jsql.Notification;
import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Transaction;
import org.jaxdb.jsql.data;
import org.jaxdb.jsql.types;
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
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.libj.lang.ObjectUtil;
import org.xml.sax.SAXException;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class NotifierTest {
//  @DB(Derby.class)
//  @DB(SQLite.class)
//  public static class IntegrationTest extends NotifierTest {
//  }

//  @DB(MySQL.class)
  @DB(PostgreSQL.class)
//  @DB(Oracle.class)
  public static class RegressionTest extends NotifierTest {
  }

  private static final int id = 10000;
  private static final Random r = new Random();
  private static final Map<Integer,Integer> checks = new HashMap<>();
  private static final Map<Integer,data.Table> pre = new HashMap<>();
  private final Map<Integer,data.Table> post = new HashMap<>();
  private final Map<Integer,Integer> expectedChecks = new HashMap<>();

  private static void setPre(final types.Type t) {
    pre.put(t.id.get(), t);
  }

  private void checkPre(final Action action, final types.Type t) {
    try {
      final data.Table e = pre.get(t.id.get());
      assertNotNull(action.toString(), e);
      assertEquals(action.toString(), e.toString(), t.toString());
      checks.put(t.id.get(), checks.getOrDefault(t.id.get(), 0) + 1);
      post.put(t.id.get(), t);
      System.err.println("[OK] checkPre(" + action + "," + t.getName() + ")");
    }
    catch (final Throwable e) {
      throw e;
    }
  }

  private void checkPost(final types.Type t) {
    assertEquals(t, post.remove(t.id.get()));
  }

  private final ConcurrentHashMap<Vendor,DefaultCache> vendorToTableCache = new ConcurrentHashMap<Vendor,DefaultCache>() {
    @Override
    public DefaultCache get(final Object key) {
      final Vendor vendor = (Vendor)key;
      DefaultCache value = super.get(vendor);
      if (value == null)
        super.put(vendor, value = new DefaultCache(Database.threadLocal(types.class).connect(vendor::getConnection)));

      return value;
    }
  };

  @After
  public void after() {
    assertEquals(0, post.size());
    if (expectedChecks.size() > 0)
      for (final Map.Entry<Integer,Integer> entry : expectedChecks.entrySet()) // [S]
        assertEquals("" + entry.getKey(), entry.getValue(), checks.get(entry.getKey()));
  }

  @Test
  @Spec(order = 0)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void setUp(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws GeneratorExecutionException, IOException, SAXException, SQLException, TransformerException {
    DDLxTest.recreateSchema(transaction.getConnection(), "types");
    transaction.commit();

    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.removeNotificationListeners();
  }

  private static int run = 1;

  private class Handler<T extends types.Type> implements Notification.InsertListener<T>, Notification.UpdateListener<T>, Notification.DeleteListener<T> {
    private final String calledFrom;
    private final Vendor vendor;

    private Handler(final String calledFrom, final Vendor vendor) {
      this.calledFrom = calledFrom;
      this.vendor = vendor;
    }

    @Override
    public T onInsert(final String sessionId, final long timestamp, final T row) {
      System.err.println("[PG] " + calledFrom + "(): " + this + " INSERT: " + ObjectUtil.simpleIdentityString(row));
      checkPre(Action.INSERT, row);
      return null;
//      return vendor == null ? null : (T)vendorToTableCache.get(vendor).onInsert(sessionId, timestamp, row);
    }

    @Override
    public T onUpdate(final String sessionId, final long timestamp, final T row, final Map<String,String> keyForUpdate) {
      System.err.println("[PG] " + calledFrom + "(): " + this + " UPDATE: " + ObjectUtil.simpleIdentityString(row));
      checkPre(keyForUpdate != null ? Action.UPGRADE : Action.UPDATE, row);
      return null;
//      return vendor == null ? null : (T)vendorToTableCache.get(vendor).onUpdate(sessionId, timestamp, row, keyForUpdate);
    }

    @Override
    public T onDelete(final String sessionId, final long timestamp, final T row) {
      System.err.println("[PG] " + calledFrom + "(): " + this + " DELETE: " + ObjectUtil.simpleIdentityString(row));
      checkPre(Action.DELETE, row);
      return null;
//      return vendor == null ? null : (T)vendorToTableCache.get(vendor).onDelete(sessionId, timestamp, row);
    }
  }

  @Test
  @Spec(order = 1)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  @SuppressWarnings({"rawtypes", "unchecked"})
  public void testFast(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast1", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast2", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast3", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast4", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast5", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast6", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast7", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast8", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast9", vendor), queue, types.Type());
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testFast10", vendor), queue, types.Type());

    types.Type t = types.Type();
    final ArrayList<types.Type> inserts = new ArrayList<>();
    for (int id = 0; id < 100; ++id) { // [N]
      inserts.add(t = new types.Type());
      t.id.set(NotifierTest.id + 50 + id);
      t.intType.set(r.nextInt());

      INSERT(t)
        .execute(transaction);

      setPre(t);
    }

    transaction.commit();
    Thread.sleep(300);

    for (int i = 0, i$ = inserts.size(); i < i$; ++i) // [RA]
      checkPost(inserts.get(i));

    for (int i = 0; i < 10; ++i) { // [N]
      final int value = r.nextInt();
      UPDATE(t).
      SET(t.intType, value)
        .execute(transaction);

      Thread.sleep(300);
      pre.values().forEach(c -> ((types.Type)c).intType.set(value));
      transaction.commit();
    }

    Thread.sleep(300);
    post.clear();
    connector.removeNotificationListeners();
  }

  @Test
  @Spec(order = 2, cardinality = 3)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testMulti(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    connector.addNotificationListener(INSERT, UPDATE, DELETE, new Handler<>("testMulti", vendor), queue, types.Type());

    final int id = NotifierTest.id + 2;

    final types.Type t = new types.Type();

    t.id.set(id);
    t.tinyintType.set((byte)48);
    t.smallintType.set((short)3293);
    t.intType.set(489233);

    setPre(t);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    t.intType.set(489234);

    setPre(t);

    UPDATE(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    setPre(t);

    DELETE(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    // https://www.wolframalpha.com/input/?i=0%2C+3%2C+9%2C+18%2C+30%2C+45%2C+63%2C+84%2C+108%2C+135%2C+165%2C+198
    expectedChecks.put(id, (int)((3d / 2) * run * ++run));
    System.err.println("[OK] testMulti(" + (run - 1) + ")");
    Thread.sleep(1000);
  }

  @Test
  @Spec(order = 3)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testInsert(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    connector.addNotificationListener(INSERT, new Handler<>("testInsert", null), queue, types.Type());

    final int id = NotifierTest.id + 3;

    final types.Type t = new types.Type();

    t.id.set(id);
    t.tinyintType.set((byte)47);
    t.smallintType.set((short)3292);
    t.intType.set(489232);

    setPre(t);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    expectedChecks.put(id, 4);
    System.err.println("[OK] testInsert()");
  }

  @Test
  @Spec(order = 4)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testUpdate(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    connector.addNotificationListener(INSERT, UPDATE, new Handler<>("testUpdate", null), queue, types.Type());

    final int id = NotifierTest.id + 4;

    final types.Type t = new types.Type();
    t.id.set(id);

    t.tinyintType.set((byte)48);
    t.smallintType.set((short)3293);
    t.intType.set(489233);

    setPre(t);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    t.intType.set(489234);

    setPre(t);

    UPDATE(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    expectedChecks.put(id, 9);
    System.err.println("[OK] testUpdate()");
  }

  @Test
  @Spec(order = 5)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testDelete(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    connector.addNotificationListener(INSERT, DELETE, new Handler<>("testDelete", null), queue, types.Type());

    final int id = NotifierTest.id + 5;

    final types.Type t = new types.Type();

    t.id.set(id);
    t.tinyintType.set((byte)48);
    t.smallintType.set((short)3293);
    t.intType.set(489233);

    setPre(t);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    setPre(t);

    DELETE(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    expectedChecks.put(id, 10);
    System.err.println("[OK] testDelete()");
  }

  @Test
  @Spec(order = 6)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testRemove(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    pre.clear();
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.removeNotificationListeners(DELETE);

    final types.Type t = new types.Type();

    DELETE(t).
    WHERE(BETWEEN(t.id, id, id + 100))
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);

    connector.removeNotificationListeners(INSERT);

    t.id.set(id + 3);
    t.tinyintType.set((byte)48);
    t.smallintType.set((short)3293);
    t.intType.set(489233);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);

    connector.removeNotificationListeners(UPDATE);

    t.intType.set(489234);

    UPDATE(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);

    assertEquals(0, post.size());
    System.err.println("[OK] testRemove()");
  }

  @Test
  @Spec(order = 7)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testAddAgain(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    final ConcurrentLinkedQueue queue = new ConcurrentLinkedQueue<>();
    connector.addNotificationListener(INSERT, new Handler<>("testAddAgain", null), queue, types.Type());

    final int id = NotifierTest.id + 7;

    final types.Type t = new types.Type();

    t.id.set(id);
    t.tinyintType.set((byte)47);
    t.smallintType.set((short)3292);
    t.intType.set(489232);

    setPre(t);

    INSERT(t)
      .execute(transaction);

    transaction.commit();
    Thread.sleep(300);
    checkPost(t);

    expectedChecks.put(id, 1);
    System.err.println("[OK] testAddAgain()");
  }

  @Test
  @Spec(order = 8)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void cleanUp(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();

    DELETE(t).
    WHERE(BETWEEN(t.id, id, id + 200))
      .execute(transaction);

    transaction.commit();
  }
}