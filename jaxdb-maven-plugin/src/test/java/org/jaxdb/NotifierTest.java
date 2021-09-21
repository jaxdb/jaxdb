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

import static org.jaxdb.jsql.DML.*;
import static org.jaxdb.jsql.DML.DELETE;
import static org.jaxdb.jsql.DML.INSERT;
import static org.jaxdb.jsql.DML.UPDATE;
import static org.jaxdb.jsql.Notification.Action.DELETE;
import static org.jaxdb.jsql.Notification.Action.INSERT;
import static org.jaxdb.jsql.Notification.Action.UPDATE;
import static org.junit.Assert.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Connector;
import org.jaxdb.jsql.Database;
import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.RowCache;
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

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false)
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
  private static final ConcurrentHashMap<Vendor,RowCache> vendorToRowCache = new ConcurrentHashMap<Vendor,RowCache>() {
    private static final long serialVersionUID = 8511976281450639374L;

    @Override
    public RowCache get(final Object key) {
      final Vendor vendor = (Vendor)key;
      RowCache value = super.get(vendor);
      if (value == null)
        super.put(vendor, value = new RowCache());

      return value;
    }
  };
  private static final Map<Integer,Integer> checks = new HashMap<>();
  private static final Map<Integer,data.Table<?>> pre = new HashMap<>();
  private final Map<Integer,data.Table<?>> post = new HashMap<>();
  private Map<Integer,Integer> expectedChecks = new HashMap<>();

  private static void setPre(final types.Type t) {
    pre.put(t.id.get(), t);
  }

  private void checkPre(final Action action, final types.Type t) {
    final data.Table<?> e = pre.get(t.id.get());
    assertNotNull(action.toString(), e);
    assertEquals(action.toString(), e.toString(), t.toString());
    checks.put(t.id.get(), checks.getOrDefault(t.id.get(), 0) + 1);
    post.put(t.id.get(), t);
    System.err.println("[OK] checkPre(" + action + "," + t.getName() + ")");
  }

  private void checkPost(final types.Type t) {
    assertEquals(t, post.remove(t.id.get()));
  }

  @After
  public void after() {
    assertEquals(0, post.size());
    for (final Map.Entry<Integer,Integer> entry : expectedChecks.entrySet()) {
      assertEquals(entry.getValue(), checks.get(entry.getKey()));
    }
  }

  @Test
  @Spec(order = 0)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void setUp(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();

    DELETE(t).
    WHERE(BETWEEN(t.id, id, id + 100))
      .execute(transaction);

    transaction.commit();
  }

  private static int run = 1;

  @Test
  @Spec(order = 1, cardinality = 3)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testMulti(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.addNotificationListener(INSERT, UPDATE, DELETE, (a, t) -> {
      System.err.println(this + " " + a + ": " + ObjectUtil.simpleIdentityString(t));
      checkPre(a, t);
      vendorToRowCache.get(vendor).handle(a, t);
    }, types.Type());

    final int id = NotifierTest.id + 1;

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
  @Spec(order = 2)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testInsert(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.addNotificationListener(INSERT, (a, t) -> {
      System.err.println(this + " " + a + ": " + ObjectUtil.simpleIdentityString(t));
      checkPre(a, t);
    }, types.Type());

    final int id = NotifierTest.id + 2;

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
  @Spec(order = 3)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testUpdate(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.addNotificationListener(INSERT, UPDATE, (a, t) -> {
      System.err.println(this + " " + a + ": " + ObjectUtil.simpleIdentityString(t));
      checkPre(a, t);
    }, types.Type());

    final int id = NotifierTest.id + 3;

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
  @Spec(order = 4)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testDelete(@Schema(types.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(transaction::getConnection);
    connector.addNotificationListener(INSERT, DELETE, (a, t) -> {
      System.err.println(this + " " + a + ": " + ObjectUtil.simpleIdentityString(t));
      checkPre(a, t);
    }, types.Type());

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
  @Spec(order = 5)
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
  @Spec(order = 6)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void testAddAgain(@Schema(types.class) final Transaction transaction, final Vendor vendor) throws InterruptedException, IOException, SQLException {
    final Connector connector = Database.threadLocal(transaction.getSchemaClass()).connect(vendor::getConnection);
    connector.addNotificationListener(INSERT, (a, t) -> {
      System.err.println(this + " " + a + ": " + ObjectUtil.simpleIdentityString(t));
      checkPre(a, t);
    }, types.Type());

    final int id = NotifierTest.id + 5;

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
  @Spec(order = 7)
  @Unsupported({Derby.class, SQLite.class, MySQL.class, Oracle.class})
  public void cleanUp(@Schema(types.class) final Transaction transaction) throws IOException, SQLException {
    final types.Type t = types.Type();

    DELETE(t).
    WHERE(BETWEEN(t.id, id, id + 100))
      .execute(transaction);

    transaction.commit();
  }
}