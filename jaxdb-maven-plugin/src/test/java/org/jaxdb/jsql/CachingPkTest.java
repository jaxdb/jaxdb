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
public abstract class CachingPkTest extends CachingTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends CachingPkTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends CachingPkTest {
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

      INSERT(transaction, o, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, o, caching.One$.id_TO_One_CACHED(i));

      final Caching.OneOneId oo = caching.new OneOneId();
      oo.oneId.set(i);
      INSERT(transaction, oo, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneId$.oneId_TO_OneOneId_CACHED(i));

      final Caching.One o1 = oo.oneId_TO_id_ON_One_CACHED();
      assertEquals(i, afterSleep, o, o1);

      final Caching.OneOneId oo1 = o.id_TO_oneId_ON_OneOneId_CACHED();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyId = i * iterations + j;
        final Caching.OneManyId om = caching.new OneManyId(oneManyId);
        om.oneId.set(i);
        INSERT(transaction, om, i, k -> {}, k -> {});
        assertEquals(i, afterSleep, om, caching.OneManyId$.id_TO_OneManyId_CACHED(oneManyId));

        final Map<data.Key,Caching.OneManyId> oms = caching.OneManyId$.oneId_TO_OneManyId_CACHED(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final Caching.One o2 = om.oneId_TO_id_ON_One_CACHED();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,Caching.OneManyId> oms0 = o.id_TO_oneId_ON_OneManyId_CACHED();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyId = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final Caching.ManyManyId mm = caching.new ManyManyId(manyManyId);
        mm.oneAId.set(a);
        mm.oneBId.set(b);
        INSERT(transaction, mm, i, j -> {}, j -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyId$.id_TO_ManyManyId_CACHED(manyManyId));
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(a), mm.oneAId_TO_id_ON_One_CACHED());
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(b), mm.oneBId_TO_id_ON_One_CACHED());

        final Map<data.Key,Caching.ManyManyId> mmas = caching.ManyManyId$.oneAId_TO_ManyManyId_CACHED(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,Caching.ManyManyId> mmbs = caching.ManyManyId$.oneBId_TO_ManyManyId_CACHED(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @TestSpec(order = 2)
  public void testUpdatePrimaryKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyId> list = new ArrayList<>(caching.ManyManyId$.id_TO_ManyManyId_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyId mm = list.get(i).clone();

      final int oldId = mm.id.get();
      final int newId = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyId$.id_TO_ManyManyId_CACHED(oldId));
      assertNull(i, afterSleep, caching.ManyManyId$.id_TO_ManyManyId_CACHED(newId));

      final Caching.One oa = mm.oneAId_TO_id_ON_One_CACHED();
      final Caching.One ob = mm.oneBId_TO_id_ON_One_CACHED();
      assertTrue(oa.id_TO_oneAId_ON_ManyManyId_CACHED().containsValue(mm));
      assertTrue(ob.id_TO_oneBId_ON_ManyManyId_CACHED().containsValue(mm));

      assertTrue(mm.id.set(newId));
      // assertSame(mm, caching.ManyManyId$.id_TO_ManyManyId_CACHED(oldId));
      assertNull(i, afterSleep, caching.ManyManyId$.id_TO_ManyManyId_CACHED(newId));

      assertFalse(mm.id.set(newId));
      mm.id.revert();
      assertEquals(i, afterSleep, oldId, mm.id.getAsInt());
      mm.id.set(newId);

      assertNotEquals(mm.id.get().toString(), data.Column.SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i, false,
        j -> {
          assertNull(j, afterSleep, caching.ManyManyId$.id_TO_ManyManyId_CACHED(oldId));
          assertEquals(j, afterSleep, mm, caching.ManyManyId$.id_TO_ManyManyId_CACHED(newId));
        },
        (j, as) -> {
          assertEquals(j, as, oa, mm.oneAId_TO_id_ON_One_CACHED());
          assertEquals(j, as, ob, mm.oneBId_TO_id_ON_One_CACHED());

          assertTrue(oa.id_TO_oneAId_ON_ManyManyId_CACHED().containsValue(mm));
          assertTrue(ob.id_TO_oneBId_ON_ManyManyId_CACHED().containsValue(mm));
        });

      assertEquals(i, afterSleep, data.Column.SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final Caching caching, final int i, final Caching.One o, final int id1, final int id2, final Caching.OneOneId oo, final Map<data.Key,Caching.OneManyId> oms, final Map<data.Key,Caching.ManyManyId> mmAs, final Map<data.Key,Caching.ManyManyId> mmBs) {
    assertNull(i, false, caching.One$.id_TO_One_CACHED(id2));
    assertEquals(i, false, o, caching.One$.id_TO_One_CACHED(id1));
  }

  private static void checkAsync(final Caching caching, final int i, final boolean afterSleep, final Caching.One o, final int id1, final int id2, final Caching.OneOneId oo, final Map<data.Key,Caching.OneManyId> oms, final Map<data.Key,Caching.ManyManyId> mmAs, final Map<data.Key,Caching.ManyManyId> mmBs) {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.id_TO_oneId_ON_OneOneId_CACHED()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneId_TO_id_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyId$.oneId_TO_OneManyId_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyId$.oneId_TO_OneManyId_CACHED(id2).size());

    for (final Caching.OneManyId om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id_TO_oneId_ON_OneManyId_CACHED().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneId_TO_id_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyId$.oneAId_TO_ManyManyId_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyId$.oneAId_TO_ManyManyId_CACHED(id2).size());

    for (final Caching.ManyManyId mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id_TO_oneAId_ON_ManyManyId_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAId_TO_id_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyId$.oneBId_TO_ManyManyId_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyId$.oneBId_TO_ManyManyId_CACHED(id2).size());

    for (final Caching.ManyManyId mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id_TO_oneBId_ON_ManyManyId_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBId_TO_id_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @TestSpec(order = 3)
  public void testUpdateForeignKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.One> list = new ArrayList<>(caching.One$.id_TO_One_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.One o = list.get(i).clone();

      final int oldId = o.id.get();
      final int newId = o.id.get() + idOffset;

      final Caching.OneOneId oo = o.id_TO_oneId_ON_OneOneId_CACHED();
      final Map<data.Key,Caching.OneManyId> oms = new HashMap<>(caching.OneManyId$.oneId_TO_OneManyId_CACHED(oldId));
      final Map<data.Key,Caching.ManyManyId> mmAs = new HashMap<>(caching.ManyManyId$.oneAId_TO_ManyManyId_CACHED(oldId));
      final Map<data.Key,Caching.ManyManyId> mmBs = new HashMap<>(caching.ManyManyId$.oneBId_TO_ManyManyId_CACHED(oldId));
      checkAsync(caching, i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      assertTrue(o.id.set(newId));

      assertEquals(i, afterSleep, newId, o.id.getAsInt());
      checkAsync(caching, i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      assertFalse(o.id.set(newId));
      o.id.revert();

      assertEquals(i, afterSleep, oldId, o.id.getAsInt());
      assertEquals(i, true, o, caching.One$.id_TO_One_CACHED(oldId));
      checkAsync(caching, i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      o.id.set(newId);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(caching, j, o, newId, oldId, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, as ? o.getKeyOld() : o.getKey(), caching.One$.id_TO_One_CACHED(newId).getKey());
          checkAsync(caching, j, as, o, newId, oldId, oo, oms, mmAs, mmBs);
        });

      o.id.set(oldId);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(caching, j, o, oldId, newId, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, o, caching.One$.id_TO_One_CACHED(oldId));
          checkAsync(caching, j, as, o, oldId, newId, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @TestSpec(order = 4)
  public void testDelete(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyId> list = new ArrayList<>(caching.ManyManyId$.id_TO_ManyManyId_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyId mm = list.get(i);
      final Caching.One oa = mm.oneAId_TO_id_ON_One_CACHED();
      final Caching.One ob = mm.oneBId_TO_id_ON_One_CACHED();

      assertTrue(oa.id_TO_oneAId_ON_ManyManyId_CACHED().containsValue(mm));
      assertTrue(ob.id_TO_oneBId_ON_ManyManyId_CACHED().containsValue(mm));

      DELETE(transaction, mm, i, true,
        j -> {
          assertFalse(caching.ManyManyId$.id_TO_ManyManyId_CACHED().containsValue(mm));
          assertFalse(oa.id_TO_oneAId_ON_ManyManyId_CACHED().containsValue(mm));
          assertFalse(ob.id_TO_oneBId_ON_ManyManyId_CACHED().containsValue(mm));
        },
        (j, as) -> {
        });
    }
  }
}