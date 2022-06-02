/* Copyright (c) 2022 JAX-DB
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class CachingIdx1Test extends CachingTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends CachingUqTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends CachingUqTest {
  }

  @Test
  @Spec(order = 1)
  public void testInsert(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (int i = 0; i < iterations; ++i) {
      final caching.One o = new caching.One(i);
      o.idx1.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o);
      assertSame(o, caching.One.idx1ToOne(i));

      final caching.OneOneIdx1 oo = new caching.OneOneIdx1();
      oo.oneIdx1.set(i);
      INSERT(transaction, oo);
      assertSame(oo, caching.OneOneIdx1.oneIdx1ToOneOneIdx1(i));

      final caching.One o1 = oo.oneIdx1$One_idx1();
      assertSame(o, o1);

      final caching.OneOneIdx1 oo1 = o.idx1$OneOneIdx1_oneIdx1();
      assertSame(oo, oo1);

      for (int j = 0; j < iterations; ++j) {
        final int oneManyIdx1 = i * iterations + j;
        final caching.OneManyIdx1 om = new caching.OneManyIdx1(oneManyIdx1);
        om.oneIdx1.set(i);
        INSERT(transaction, om);
        assertSame(om, caching.OneManyIdx1.idToOneManyIdx1(oneManyIdx1));

        final Map<data.Key,caching.OneManyIdx1> oms = caching.OneManyIdx1.oneIdx1ToOneManyIdx1(i);
        assertTrue(oms.containsValue(om));
        assertEquals(j + 1, oms.size());

        final caching.One o2 = om.oneIdx1$One_idx1();
        assertSame(o, o2);

        final Map<data.Key,caching.OneManyIdx1> oms0 = o.idx1$OneManyIdx1_oneIdx1();
        assertEquals(j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) {
        final int manyManyIdx1 = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyIdx1 mm = new caching.ManyManyIdx1(manyManyIdx1);
        mm.oneAIdx1.set(a);
        mm.oneBIdx1.set(b);
        INSERT(transaction, mm);
        assertSame(mm, caching.ManyManyIdx1.idToManyManyIdx1(manyManyIdx1));
        assertSame(caching.One.idx1ToOne(a), mm.oneAIdx1$One_idx1());
        assertSame(caching.One.idx1ToOne(b), mm.oneBIdx1$One_idx1());

        final Map<data.Key,caching.ManyManyIdx1> mmas = caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(a);
        assertEquals(i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyIdx1> mmbs = caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(b);
        assertEquals(i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (caching.ManyManyIdx1 mm0 : new ArrayList<>(caching.ManyManyIdx1.idToManyManyIdx1().values())) {
      final caching.ManyManyIdx1 mm = mm0.clone();

      final int oldIdx1 = mm.id.get();
      final int newIdx1 = mm.id.get() + idOffset;

      assertSame(mm, caching.ManyManyIdx1.idToManyManyIdx1(oldIdx1));
      assertNull(caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));

      final caching.One oa = mm.oneAIdx1$One_idx1();
      final caching.One ob = mm.oneBIdx1$One_idx1();
      assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
      assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));

      mm.id.set(newIdx1);
      // assertSame(mm, caching.ManyManyIdx1.idToManyManyIdx1(oldIdx1));
      assertNull(caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));

      mm.id.set(newIdx1);
      mm.id.revert();
      assertEquals(oldIdx1, mm.id.getAsInt());
      mm.id.set(newIdx1);

      assertNotEquals(mm.id.get().toString(), SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(0, mm.auto.getAsInt());

      UPDATE(transaction, mm,
        () -> {
          assertNull(caching.ManyManyIdx1.idToManyManyIdx1(oldIdx1));
          assertSame(mm, caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));
        },
        () -> {
          assertSame(oa, mm.oneAIdx1$One_idx1());
          assertSame(ob, mm.oneBIdx1$One_idx1());

          assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
          assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));
        });

      assertEquals(SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final caching.One o, final int id1, final int id2, final caching.OneOneIdx1 oo, final Map<data.Key,caching.OneManyIdx1> oms, final Map<data.Key,caching.ManyManyIdx1> mmAs, final Map<data.Key,caching.ManyManyIdx1> mmBs) {
    assertNull(caching.One.idx1ToOne(id2));
    assertSame(o, caching.One.idx1ToOne(id1));
  }

  private static void checkAsync(final caching.One o, final int id1, final int id2, final caching.OneOneIdx1 oo, final Map<data.Key,caching.OneManyIdx1> oms, final Map<data.Key,caching.ManyManyIdx1> mmAs, final Map<data.Key,caching.ManyManyIdx1> mmBs) {
    assertSame(oo, o.idx1$OneOneIdx1_oneIdx1()); // NOTE: CASCADE rule in DML ensures this is always true
    assertSame(o.getKeyOld(), oo.oneIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals("oldIdx1: " + id1, oms.size(), caching.OneManyIdx1.oneIdx1ToOneManyIdx1(id1).size());
    assertEquals("newIdx1: " + id2, 0, caching.OneManyIdx1.oneIdx1ToOneManyIdx1(id2).size());

    for (final caching.OneManyIdx1 om : oms.values()) {
      assertTrue(o.idx1$OneManyIdx1_oneIdx1().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), om.oneIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldIdx1: " + id1, mmAs.size(), caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(id1).size());
    assertEquals("newIdx1: " + id2, 0, caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(id2).size());

    for (final caching.ManyManyIdx1 mm : mmAs.values()) {
      assertTrue(o.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneAIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldIdx1: " + id1, mmBs.size(), caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(id1).size());
    assertEquals("newIdx1: " + id2, 0, caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(id2).size());

    for (final caching.ManyManyIdx1 mm : mmBs.values()) {
      assertTrue(o.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneBIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.One o0 : new ArrayList<>(caching.One.idx1ToOne().values())) {
      final caching.One o = o0.clone();

      final int oldIdx1 = o.idx1.get();
      final int newIdx1 = o.idx1.get() + idOffset;

      final caching.OneOneIdx1 oo = o.idx1$OneOneIdx1_oneIdx1();
      final Map<data.Key,caching.OneManyIdx1> oms = new HashMap<>(caching.OneManyIdx1.oneIdx1ToOneManyIdx1(oldIdx1));
      final Map<data.Key,caching.ManyManyIdx1> mmAs = new HashMap<>(caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(oldIdx1));
      final Map<data.Key,caching.ManyManyIdx1> mmBs = new HashMap<>(caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(oldIdx1));
      checkAsync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      o.idx1.set(newIdx1);

      assertEquals(newIdx1, o.idx1.getAsInt());
      checkAsync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      o.idx1.set(newIdx1);
      o.idx1.revert();

      assertEquals(oldIdx1, o.idx1.getAsInt());
      assertSame(o, caching.One.idx1ToOne(oldIdx1));
      checkAsync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      o.idx1.set(newIdx1);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.idx1ToOne(newIdx1));
          checkSync(o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
          checkAsync(o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        });

      o.idx1.set(oldIdx1);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.idx1ToOne(oldIdx1));
          checkSync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
          checkAsync(o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.ManyManyIdx1 mm : new ArrayList<>(caching.ManyManyIdx1.idToManyManyIdx1().values())) {
      final caching.One oa = mm.oneAIdx1$One_idx1();
      final caching.One ob = mm.oneBIdx1$One_idx1();

      assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
      assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));

      DELETE(transaction, mm,
        () -> {
          assertFalse(caching.ManyManyIdx1.idToManyManyIdx1().containsValue(mm));
          assertFalse(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
          assertFalse(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));
        },
        () -> {
        });
    }
  }
}