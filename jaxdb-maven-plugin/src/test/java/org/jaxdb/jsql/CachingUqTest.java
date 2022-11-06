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
public abstract class CachingUqTest extends CachingTest {
  //@DB(Derby.class)
  //@DB(SQLite.class)
  //public static class IntegrationTest extends CachingUqTest {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends CachingUqTest {
  }

  private static final boolean afterSleep = false;

  @Test
  @Spec(order = 1)
  public void testInsert(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    for (int i = 0; i < iterations; ++i) { // [N]
      final caching.One o = new caching.One(i);
      o.idu.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, o, caching.One.idToOne(i));

      final caching.OneOneIdu oo = new caching.OneOneIdu();
      oo.id.set(i + idOffset);
      oo.oneIdu.set(i);
      INSERT(transaction, oo, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneIdu.oneIduToOneOneIdu(i));

      final caching.One o1 = oo.oneIdu$One_idu();
      assertEquals(i, afterSleep, o, o1);

      final caching.OneOneIdu oo1 = o.idu$OneOneIdu_oneIdu();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyIdu = i * iterations + j;
        final caching.OneManyIdu om = new caching.OneManyIdu(oneManyIdu);
        om.oneIdu.set(i);
        INSERT(transaction, om, i, k -> {}, k -> {});
        assertEquals(i, afterSleep, om, caching.OneManyIdu.idToOneManyIdu(oneManyIdu));

        final Map<data.Key,caching.OneManyIdu> oms = caching.OneManyIdu.oneIduToOneManyIdu(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final caching.One o2 = om.oneIdu$One_idu();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,caching.OneManyIdu> oms0 = o.idu$OneManyIdu_oneIdu();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyIdu = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyIdu mm = new caching.ManyManyIdu(manyManyIdu);
        mm.oneAIdu.set(a);
        mm.oneBIdu.set(b);
        INSERT(transaction, mm, i, j -> {}, j -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyIdu.idToManyManyIdu(manyManyIdu));
        assertEquals(i, afterSleep, caching.One.idToOne(a), mm.oneAIdu$One_idu());
        assertEquals(i, afterSleep, caching.One.idToOne(b), mm.oneBIdu$One_idu());

        final Map<data.Key,caching.ManyManyIdu> mmas = caching.ManyManyIdu.oneAIduToManyManyIdu(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyIdu> mmbs = caching.ManyManyIdu.oneBIduToManyManyIdu(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyIdu> list = new ArrayList<>(caching.ManyManyIdu.idToManyManyIdu().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyIdu mm = list.get(i).clone();

      final int oldIdu = mm.id.get();
      final int newIdu = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyIdu.idToManyManyIdu(oldIdu));
      assertNull(i, afterSleep, caching.ManyManyIdu.idToManyManyIdu(newIdu));

      final caching.One oa = mm.oneAIdu$One_idu();
      final caching.One ob = mm.oneBIdu$One_idu();
      assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
      assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));

      mm.id.set(newIdu);
      // assertSame(mm, caching.ManyManyId.idToManyManyIdu(oldId));
      assertNull(i, afterSleep, caching.ManyManyIdu.idToManyManyIdu(newIdu));

      mm.id.set(newIdu);
      mm.id.revert();
      assertEquals(i, afterSleep, oldIdu, mm.id.getAsInt());
      mm.id.set(newIdu);

      assertNotEquals(mm.id.get().toString(), SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i,
        j -> {
          assertNull(j, afterSleep, caching.ManyManyIdu.idToManyManyIdu(oldIdu));
          assertEquals(j, afterSleep, mm, caching.ManyManyIdu.idToManyManyIdu(newIdu));
        },
        (j, as) -> {
          assertEquals(j, as, oa, mm.oneAIdu$One_idu());
          assertEquals(j, as, ob, mm.oneBIdu$One_idu());

          assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
          assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));
        });

      assertEquals(i, afterSleep, SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final int i, final caching.One o, final int id1, final int id2, final caching.OneOneIdu oo, final Map<data.Key,caching.OneManyIdu> oms, final Map<data.Key,caching.ManyManyIdu> mmAs, final Map<data.Key,caching.ManyManyIdu> mmBs) {
    assertNull(i, false, caching.One.iduToOne(id2));
    assertEquals(i, false, o, caching.One.iduToOne(id1));
  }

  private static void checkAsync(final int i, final boolean afterSleep, final caching.One o, final int id1, final int id2, final caching.OneOneIdu oo, final Map<data.Key,caching.OneManyIdu> oms, final Map<data.Key,caching.ManyManyIdu> mmAs, final Map<data.Key,caching.ManyManyIdu> mmBs) {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.idu$OneOneIdu_oneIdu()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyIdu.oneIduToOneManyIdu(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyIdu.oneIduToOneManyIdu(id2).size());

    for (final caching.OneManyIdu om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu$OneManyIdu_oneIdu().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyIdu.oneAIduToManyManyIdu(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyIdu.oneAIduToManyManyIdu(id2).size());

    for (final caching.ManyManyIdu mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu$ManyManyIdu_oneAIdu().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyIdu.oneBIduToManyManyIdu(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyIdu.oneBIduToManyManyIdu(id2).size());

    for (final caching.ManyManyIdu mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idu$ManyManyIdu_oneBIdu().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.One> list = new ArrayList<>(caching.One.idToOne().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.One o = list.get(i).clone();

      final int oldIdu = o.idu.get();
      final int newIdu = o.idu.get() + idOffset;

      final caching.OneOneIdu oo = o.idu$OneOneIdu_oneIdu();
      final Map<data.Key,caching.OneManyIdu> oms = new HashMap<>(caching.OneManyIdu.oneIduToOneManyIdu(oldIdu));
      final Map<data.Key,caching.ManyManyIdu> mmAs = new HashMap<>(caching.ManyManyIdu.oneAIduToManyManyIdu(oldIdu));
      final Map<data.Key,caching.ManyManyIdu> mmBs = new HashMap<>(caching.ManyManyIdu.oneBIduToManyManyIdu(oldIdu));
      checkAsync(i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);

      assertEquals(i, afterSleep, newIdu, o.idu.getAsInt());
      checkAsync(i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);
      o.idu.revert();

      assertEquals(i, afterSleep, oldIdu, o.id.getAsInt());
      assertEquals(i, true, o, caching.One.idToOne(oldIdu));
      checkAsync(i, true, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);

      UPDATE(transaction, o, i,
        j -> {
          checkSync(j, o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, as ? null : o, caching.One.iduToOne(as ? oldIdu : newIdu));
          checkAsync(j, as, o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        });

      o.idu.set(oldIdu);

      UPDATE(transaction, o, i,
        j -> {
          checkSync(j, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, o, caching.One.iduToOne(oldIdu));
          checkAsync(j, as, o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyIdu> list = new ArrayList<>(caching.ManyManyIdu.idToManyManyIdu().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyIdu mm = list.get(i);
      final caching.One oa = mm.oneAIdu$One_idu();
      final caching.One ob = mm.oneBIdu$One_idu();

      assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
      assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));

      DELETE(transaction, mm, i,
        j -> {
          assertFalse(caching.ManyManyIdu.idToManyManyIdu().containsValue(mm));
          assertFalse(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
          assertFalse(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));
        },
        (j, as) -> {
        });
    }
  }
}