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

import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.TestSpec;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SchemaTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class CachingIdx1Test extends CachingTest {
  // @DB(Derby.class)
  // @DB(SQLite.class)
  // public static class IntegrationTest extends CachingIdx1Test {
  // }

  // @DB(MySQL.class)
  @DB(PostgreSQL.class)
  // @DB(Oracle.class)
  public static class RegressionTest extends CachingIdx1Test {
  }

  private static final boolean afterSleep = false;

  @Test
  @TestSpec(order = 1)
  public void testInsert(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    for (int i = 0; i < iterations; ++i) { // [N]
      final Caching.One o = caching.new One(i);
      o.idu.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o, i, (final int j) -> {}, (final int j) -> {});
      assertEquals(i, afterSleep, o, caching.One$.id_TO_One_CACHED(i));

      final Caching.OneOneIdx1 oo = caching.new OneOneIdx1();
      oo.oneIdx1.set(i);
      INSERT(transaction, oo, i, (final int j) -> {}, (final int j) -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneIdx1$.oneIdx1_TO_OneOneIdx1_CACHED(i));

      final Caching.One o1 = oo.oneIdx1_TO_idx1_ON_One_CACHED();
      assertEquals(i, afterSleep, o, o1);

      final Caching.OneOneIdx1 oo1 = o.idx1_TO_oneIdx1_ON_OneOneIdx1_CACHED();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyIdx1 = i * iterations + j;
        final Caching.OneManyIdx1 om = caching.new OneManyIdx1(oneManyIdx1);
        om.oneIdx1.set(i);
        INSERT(transaction, om, i, (final int k) -> {}, (final int k) -> {});
        assertEquals(i, afterSleep, om, caching.OneManyIdx1$.id_TO_OneManyIdx1_CACHED(oneManyIdx1));

        final Map<data.Key,Caching.OneManyIdx1> oms = caching.OneManyIdx1$.oneIdx1_TO_OneManyIdx1_CACHED(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final Caching.One o2 = om.oneIdx1_TO_idx1_ON_One_CACHED();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,Caching.OneManyIdx1> oms0 = o.idx1_TO_oneIdx1_ON_OneManyIdx1_CACHED();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyIdx1 = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final Caching.ManyManyIdx1 mm = caching.new ManyManyIdx1(manyManyIdx1);
        mm.oneAIdx1.set(a);
        mm.oneBIdx1.set(b);
        INSERT(transaction, mm, i, (final int j) -> {}, (final int j) -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(manyManyIdx1));
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(a), mm.oneAIdx1_TO_idx1_ON_One_CACHED());
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(b), mm.oneBIdx1_TO_idx1_ON_One_CACHED());

        final Map<data.Key,Caching.ManyManyIdx1> mmas = caching.ManyManyIdx1$.oneAIdx1_TO_ManyManyIdx1_CACHED(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,Caching.ManyManyIdx1> mmbs = caching.ManyManyIdx1$.oneBIdx1_TO_ManyManyIdx1_CACHED(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @TestSpec(order = 2)
  public void testUpdatePrimaryKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyIdx1> list = new ArrayList<>(caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyIdx1 mm = list.get(i).clone();

      final int oldIdx1 = mm.id.get();
      final int newIdx1 = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(oldIdx1));
      assertNull(i, afterSleep, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(newIdx1));

      final Caching.One oa = mm.oneAIdx1_TO_idx1_ON_One_CACHED();
      final Caching.One ob = mm.oneBIdx1_TO_idx1_ON_One_CACHED();
      assertTrue(oa.idx1_TO_oneAIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
      assertTrue(ob.idx1_TO_oneBIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));

      assertTrue(mm.id.set(newIdx1));
      // assertSame(mm, caching.ManyManyId.idToManyManyIdx1(oldId));
      assertNull(i, afterSleep, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(newIdx1));

      assertFalse(mm.id.set(newIdx1));
      mm.id.revert();

      assertEquals(i, afterSleep, oldIdx1, mm.id.getAsInt());
      mm.id.set(newIdx1);

      assertNotEquals(mm.id.get().toString(), data.Column.SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i, false,
        (final int j) -> {
          assertNull(j, afterSleep, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(oldIdx1));
          assertEquals(j, afterSleep, mm, caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED(newIdx1));
        },
        (final int j, final boolean as) -> {
          assertEquals(j, as, oa, mm.oneAIdx1_TO_idx1_ON_One_CACHED());
          assertEquals(j, as, ob, mm.oneBIdx1_TO_idx1_ON_One_CACHED());

          assertTrue(oa.idx1_TO_oneAIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
          assertTrue(ob.idx1_TO_oneBIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
        });

      assertEquals(i, afterSleep, data.Column.SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final Caching caching, final int i, final Caching.One o, final int id1, final int id2, final Caching.OneOneIdx1 oo, final Map<data.Key,Caching.OneManyIdx1> oms, final Map<data.Key,Caching.ManyManyIdx1> mmAs, final Map<data.Key,Caching.ManyManyIdx1> mmBs) {
    assertNull(i, false, caching.One$.idx1_TO_One_CACHED(id2));
    assertEquals(i, false, o, caching.One$.idx1_TO_One_CACHED(id1));
  }

  private static void checkAsync(final Caching caching, final int i, final boolean afterSleep, final Caching.One o, final int id1, final int id2, final Caching.OneOneIdx1 oo, final Map<data.Key,Caching.OneManyIdx1> oms, final Map<data.Key,Caching.ManyManyIdx1> mmAs, final Map<data.Key,Caching.ManyManyIdx1> mmBs) {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.idx1_TO_oneIdx1_ON_OneOneIdx1_CACHED()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneIdx1_TO_idx1_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyIdx1$.oneIdx1_TO_OneManyIdx1_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyIdx1$.oneIdx1_TO_OneManyIdx1_CACHED(id2).size());

    for (final Caching.OneManyIdx1 om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1_TO_oneIdx1_ON_OneManyIdx1_CACHED().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneIdx1_TO_idx1_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyIdx1$.oneAIdx1_TO_ManyManyIdx1_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyIdx1$.oneAIdx1_TO_ManyManyIdx1_CACHED(id2).size());

    for (final Caching.ManyManyIdx1 mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1_TO_oneAIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAIdx1_TO_idx1_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyIdx1$.oneBIdx1_TO_ManyManyIdx1_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyIdx1$.oneBIdx1_TO_ManyManyIdx1_CACHED(id2).size());

    for (final Caching.ManyManyIdx1 mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1_TO_oneBIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBIdx1_TO_idx1_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @TestSpec(order = 3)
  public void testUpdateForeignKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.One> list = new ArrayList<>(caching.One$.id_TO_One_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.One o = list.get(i).clone();

      final int oldIdx1 = o.idx1.get();
      final int newIdx1 = o.idx1.get() + idOffset;

      final Caching.OneOneIdx1 oo = o.idx1_TO_oneIdx1_ON_OneOneIdx1_CACHED();
      final Map<data.Key,Caching.OneManyIdx1> oms = new HashMap<>(caching.OneManyIdx1$.oneIdx1_TO_OneManyIdx1_CACHED(oldIdx1));
      final Map<data.Key,Caching.ManyManyIdx1> mmAs = new HashMap<>(caching.ManyManyIdx1$.oneAIdx1_TO_ManyManyIdx1_CACHED(oldIdx1));
      final Map<data.Key,Caching.ManyManyIdx1> mmBs = new HashMap<>(caching.ManyManyIdx1$.oneBIdx1_TO_ManyManyIdx1_CACHED(oldIdx1));
      checkAsync(caching, i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      assertTrue(o.idx1.set(newIdx1));
      assertEquals(i, afterSleep, newIdx1, o.idx1.getAsInt());
      checkAsync(caching, i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      assertFalse(o.idx1.set(newIdx1));
      o.idx1.revert();

      assertEquals(i, afterSleep, oldIdx1, o.id.getAsInt());
      assertEquals(i, true, o, caching.One$.id_TO_One_CACHED(oldIdx1));
      checkAsync(caching, i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      o.idx1.set(newIdx1);

      UPDATE(transaction, o, i, true,
        (final int j) -> {
          checkSync(caching, j, o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        },
        (final int j, final boolean as) -> {
          assertEquals(j, as, as ? null : o, caching.One$.idx1_TO_One_CACHED(as ? oldIdx1 : newIdx1));
          checkAsync(caching, j, as, o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        });

      o.idx1.set(oldIdx1);

      UPDATE(transaction, o, i, true,
        (final int j) -> {
          checkSync(caching, j, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        },
        (final int j, final boolean as) -> {
          assertEquals(j, as, o, caching.One$.idx1_TO_One_CACHED(oldIdx1));
          checkAsync(caching, j, as, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @TestSpec(order = 4)
  public void testDelete(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyIdx1> list = new ArrayList<>(caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyIdx1 mm = list.get(i);
      final Caching.One oa = mm.oneAIdx1_TO_idx1_ON_One_CACHED();
      final Caching.One ob = mm.oneBIdx1_TO_idx1_ON_One_CACHED();

      assertTrue(oa.idx1_TO_oneAIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
      assertTrue(ob.idx1_TO_oneBIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));

      DELETE(transaction, mm, i, true,
        (final int j) -> {
          assertFalse(caching.ManyManyIdx1$.id_TO_ManyManyIdx1_CACHED().containsValue(mm));
          assertFalse(oa.idx1_TO_oneAIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
          assertFalse(ob.idx1_TO_oneBIdx1_ON_ManyManyIdx1_CACHED().containsValue(mm));
        },
        (final int j, final boolean as) -> {});
    }
  }
}