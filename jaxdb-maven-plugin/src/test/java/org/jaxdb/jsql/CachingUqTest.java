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
public abstract class CachingUqTest extends CachingTest {
  // @DB(Derby.class)
  // @DB(SQLite.class)
  // public static class IntegrationTest extends CachingUqTest {
  // }

  // @DB(MySQL.class)
  @DB(PostgreSQL.class)
  // @DB(Oracle.class)
  public static class RegressionTest extends CachingUqTest {
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

      final Caching.OneOneIdu oo = caching.new OneOneIdu();
      oo.id.set(i + idOffset);
      oo.oneIdu.set(i);
      INSERT(transaction, oo, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneIdu$.oneIdu_TO_OneOneIdu_CACHED(i));

      final Caching.One o1 = oo.oneIdu_TO_idu_ON_One_CACHED();
      assertEquals(i, afterSleep, o, o1);

      final Caching.OneOneIdu oo1 = o.idu_TO_oneIdu_ON_OneOneIdu_CACHED();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyIdu = i * iterations + j;
        final Caching.OneManyIdu om = caching.new OneManyIdu(oneManyIdu);
        om.oneIdu.set(i);
        INSERT(transaction, om, i, k -> {}, k -> {});
        assertEquals(i, afterSleep, om, caching.OneManyIdu$.id_TO_OneManyIdu_CACHED(oneManyIdu));

        final Map<data.Key,Caching.OneManyIdu> oms = caching.OneManyIdu$.oneIdu_TO_OneManyIdu_CACHED(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final Caching.One o2 = om.oneIdu_TO_idu_ON_One_CACHED();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,Caching.OneManyIdu> oms0 = o.idu_TO_oneIdu_ON_OneManyIdu_CACHED();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyIdu = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final Caching.ManyManyIdu mm = caching.new ManyManyIdu(manyManyIdu);
        mm.oneAIdu.set(a);
        mm.oneBIdu.set(b);
        INSERT(transaction, mm, i, j -> {}, j -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(manyManyIdu));
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(a), mm.oneAIdu_TO_idu_ON_One_CACHED());
        assertEquals(i, afterSleep, caching.One$.id_TO_One_CACHED(b), mm.oneBIdu_TO_idu_ON_One_CACHED());

        final Map<data.Key,Caching.ManyManyIdu> mmas = caching.ManyManyIdu$.oneAIdu_TO_ManyManyIdu_CACHED(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,Caching.ManyManyIdu> mmbs = caching.ManyManyIdu$.oneBIdu_TO_ManyManyIdu_CACHED(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @TestSpec(order = 2)
  public void testUpdatePrimaryKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyIdu> list = new ArrayList<>(caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyIdu mm = list.get(i).clone();

      final int oldIdu = mm.id.get();
      final int newIdu = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(oldIdu));
      assertNull(i, afterSleep, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(newIdu));

      final Caching.One oa = mm.oneAIdu_TO_idu_ON_One_CACHED();
      final Caching.One ob = mm.oneBIdu_TO_idu_ON_One_CACHED();
      assertTrue(oa.idu_TO_oneAIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
      assertTrue(ob.idu_TO_oneBIdu_ON_ManyManyIdu_CACHED().containsValue(mm));

      assertTrue(mm.id.set(newIdu));
      // assertSame(mm, caching.ManyManyId.idToManyManyIdu(oldId));
      assertNull(i, afterSleep, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(newIdu));

      assertFalse(mm.id.set(newIdu));
      mm.id.revert();

      assertEquals(i, afterSleep, oldIdu, mm.id.getAsInt());
      mm.id.set(newIdu);

      assertNotEquals(mm.id.get().toString(), data.Column.SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i, false,
        j -> {
          assertNull(j, afterSleep, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(oldIdu));
          assertEquals(j, afterSleep, mm, caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED(newIdu));
        },
        (j, as) -> {
          assertEquals(j, as, oa, mm.oneAIdu_TO_idu_ON_One_CACHED());
          assertEquals(j, as, ob, mm.oneBIdu_TO_idu_ON_One_CACHED());

          assertTrue(oa.idu_TO_oneAIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
          assertTrue(ob.idu_TO_oneBIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
        });

      assertEquals(i, afterSleep, data.Column.SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final Caching caching, final int i, final Caching.One o, final int id1, final int id2, final Caching.OneOneIdu oo, final Map<data.Key,Caching.OneManyIdu> oms, final Map<data.Key,Caching.ManyManyIdu> mmAs, final Map<data.Key,Caching.ManyManyIdu> mmBs) {
    assertNull(i, false, caching.One$.idu_TO_One_CACHED(id2));
    assertEquals(i, false, o, caching.One$.idu_TO_One_CACHED(id1));
  }

  private static void checkAsync(final Caching caching, final int i, final boolean afterSleep, final Caching.One o, final int id1, final int id2, final Caching.OneOneIdu oo, final Map<data.Key,Caching.OneManyIdu> oms, final Map<data.Key,Caching.ManyManyIdu> mmAs, final Map<data.Key,Caching.ManyManyIdu> mmBs) {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.idu_TO_oneIdu_ON_OneOneIdu_CACHED()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneIdu_TO_idu_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyIdu$.oneIdu_TO_OneManyIdu_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyIdu$.oneIdu_TO_OneManyIdu_CACHED(id2).size());

    for (final Caching.OneManyIdu om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu_TO_oneIdu_ON_OneManyIdu_CACHED().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneIdu_TO_idu_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyIdu$.oneAIdu_TO_ManyManyIdu_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyIdu$.oneAIdu_TO_ManyManyIdu_CACHED(id2).size());

    for (final Caching.ManyManyIdu mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu_TO_oneAIdu_ON_ManyManyIdu_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAIdu_TO_idu_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyIdu$.oneBIdu_TO_ManyManyIdu_CACHED(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyIdu$.oneBIdu_TO_ManyManyIdu_CACHED(id2).size());

    for (final Caching.ManyManyIdu mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu_TO_oneBIdu_ON_ManyManyIdu_CACHED().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBIdu_TO_idu_ON_One_CACHED().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @TestSpec(order = 3)
  public void testUpdateForeignKey(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.One> list = new ArrayList<>(caching.One$.id_TO_One_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.One o = list.get(i).clone();

      final int oldIdu = o.idu.get();
      final int newIdu = o.idu.get() + idOffset;

      final Caching.OneOneIdu oo = o.idu_TO_oneIdu_ON_OneOneIdu_CACHED();
      final Map<data.Key,Caching.OneManyIdu> oms = new HashMap<>(caching.OneManyIdu$.oneIdu_TO_OneManyIdu_CACHED(oldIdu));
      final Map<data.Key,Caching.ManyManyIdu> mmAs = new HashMap<>(caching.ManyManyIdu$.oneAIdu_TO_ManyManyIdu_CACHED(oldIdu));
      final Map<data.Key,Caching.ManyManyIdu> mmBs = new HashMap<>(caching.ManyManyIdu$.oneBIdu_TO_ManyManyIdu_CACHED(oldIdu));
      checkAsync(caching, i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      assertTrue(o.idu.set(newIdu));

      assertEquals(i, afterSleep, newIdu, o.idu.getAsInt());
      checkAsync(caching, i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      assertFalse(o.idu.set(newIdu));
      o.idu.revert();

      assertEquals(i, afterSleep, oldIdu, o.id.getAsInt());
      assertEquals(i, true, o, caching.One$.id_TO_One_CACHED(oldIdu));
      checkAsync(caching, i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(caching, j, o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, as ? null : o, caching.One$.idu_TO_One_CACHED(as ? oldIdu : newIdu));
          checkAsync(caching, j, as, o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        });

      o.idu.set(oldIdu);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(caching, j, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, o, caching.One$.idu_TO_One_CACHED(oldIdu));
          checkAsync(caching, j, as, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @TestSpec(order = 4)
  public void testDelete(final Caching caching, final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<Caching.ManyManyIdu> list = new ArrayList<>(caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final Caching.ManyManyIdu mm = list.get(i);
      final Caching.One oa = mm.oneAIdu_TO_idu_ON_One_CACHED();
      final Caching.One ob = mm.oneBIdu_TO_idu_ON_One_CACHED();

      assertTrue(oa.idu_TO_oneAIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
      assertTrue(ob.idu_TO_oneBIdu_ON_ManyManyIdu_CACHED().containsValue(mm));

      DELETE(transaction, mm, i, true,
        j -> {
          assertFalse(caching.ManyManyIdu$.id_TO_ManyManyIdu_CACHED().containsValue(mm));
          assertFalse(oa.idu_TO_oneAIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
          assertFalse(ob.idu_TO_oneBIdu_ON_ManyManyIdu_CACHED().containsValue(mm));
        },
        (j, as) -> {});
    }
  }
}