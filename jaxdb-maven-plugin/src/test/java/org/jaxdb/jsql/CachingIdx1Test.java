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
  //public static class IntegrationTest extends CachingIdx1Test {
  //}

  //@DB(MySQL.class)
  @DB(PostgreSQL.class)
  //@DB(Oracle.class)
  public static class RegressionTest extends CachingIdx1Test {
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

      final caching.OneOneIdx1 oo = new caching.OneOneIdx1();
      oo.oneIdx1.set(i);
      INSERT(transaction, oo, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneIdx1.oneIdx1ToOneOneIdx1(i));

      final caching.One o1 = oo.oneIdx1$One_idx1();
      assertEquals(i, afterSleep, o, o1);

      final caching.OneOneIdx1 oo1 = o.idx1$OneOneIdx1_oneIdx1();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyIdx1 = i * iterations + j;
        final caching.OneManyIdx1 om = new caching.OneManyIdx1(oneManyIdx1);
        om.oneIdx1.set(i);
        INSERT(transaction, om, i, k -> {}, k -> {});
        assertEquals(i, afterSleep, om, caching.OneManyIdx1.idToOneManyIdx1(oneManyIdx1));

        final Map<data.Key,caching.OneManyIdx1> oms = caching.OneManyIdx1.oneIdx1ToOneManyIdx1(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final caching.One o2 = om.oneIdx1$One_idx1();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,caching.OneManyIdx1> oms0 = o.idx1$OneManyIdx1_oneIdx1();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyIdx1 = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyIdx1 mm = new caching.ManyManyIdx1(manyManyIdx1);
        mm.oneAIdx1.set(a);
        mm.oneBIdx1.set(b);
        INSERT(transaction, mm, i, j -> {}, j -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyIdx1.idToManyManyIdx1(manyManyIdx1));
        assertEquals(i, afterSleep, caching.One.idToOne(a), mm.oneAIdx1$One_idx1());
        assertEquals(i, afterSleep, caching.One.idToOne(b), mm.oneBIdx1$One_idx1());

        final Map<data.Key,caching.ManyManyIdx1> mmas = caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyIdx1> mmbs = caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyIdx1> list = new ArrayList<>(caching.ManyManyIdx1.idToManyManyIdx1().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyIdx1 mm = list.get(i).clone();

      final int oldIdx1 = mm.id.get();
      final int newIdx1 = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyIdx1.idToManyManyIdx1(oldIdx1));
      assertNull(i, afterSleep, caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));

      final caching.One oa = mm.oneAIdx1$One_idx1();
      final caching.One ob = mm.oneBIdx1$One_idx1();
      assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
      assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));

      assertTrue(mm.id.set(newIdx1));
      // assertSame(mm, caching.ManyManyId.idToManyManyIdx1(oldId));
      assertNull(i, afterSleep, caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));

      assertFalse(mm.id.set(newIdx1));
      mm.id.revert();

      assertEquals(i, afterSleep, oldIdx1, mm.id.getAsInt());
      mm.id.set(newIdx1);

      assertNotEquals(mm.id.get().toString(), data.Column.SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i, false,
        j -> {
          assertNull(j, afterSleep, caching.ManyManyIdx1.idToManyManyIdx1(oldIdx1));
          assertEquals(j, afterSleep, mm, caching.ManyManyIdx1.idToManyManyIdx1(newIdx1));
        },
        (j, as) -> {
          assertEquals(j, as, oa, mm.oneAIdx1$One_idx1());
          assertEquals(j, as, ob, mm.oneBIdx1$One_idx1());

          assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
          assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));
        });

      assertEquals(i, afterSleep, data.Column.SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final int i, final caching.One o, final int id1, final int id2, final caching.OneOneIdx1 oo, final Map<data.Key,caching.OneManyIdx1> oms, final Map<data.Key,caching.ManyManyIdx1> mmAs, final Map<data.Key,caching.ManyManyIdx1> mmBs) {
    assertNull(i, false, caching.One.idx1ToOne(id2));
    assertEquals(i, false, o, caching.One.idx1ToOne(id1));
  }

  private static void checkAsync(final int i, final boolean afterSleep, final caching.One o, final int id1, final int id2, final caching.OneOneIdx1 oo, final Map<data.Key,caching.OneManyIdx1> oms, final Map<data.Key,caching.ManyManyIdx1> mmAs, final Map<data.Key,caching.ManyManyIdx1> mmBs) {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.idx1$OneOneIdx1_oneIdx1()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyIdx1.oneIdx1ToOneManyIdx1(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyIdx1.oneIdx1ToOneManyIdx1(id2).size());

    for (final caching.OneManyIdx1 om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1$OneManyIdx1_oneIdx1().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(id2).size());

    for (final caching.ManyManyIdx1 mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(id2).size());

    for (final caching.ManyManyIdx1 mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBIdx1$One_idx1().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.One> list = new ArrayList<>(caching.One.idToOne().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.One o = list.get(i).clone();

      final int oldIdx1 = o.idx1.get();
      final int newIdx1 = o.idx1.get() + idOffset;

      final caching.OneOneIdx1 oo = o.idx1$OneOneIdx1_oneIdx1();
      final Map<data.Key,caching.OneManyIdx1> oms = new HashMap<>(caching.OneManyIdx1.oneIdx1ToOneManyIdx1(oldIdx1));
      final Map<data.Key,caching.ManyManyIdx1> mmAs = new HashMap<>(caching.ManyManyIdx1.oneAIdx1ToManyManyIdx1(oldIdx1));
      final Map<data.Key,caching.ManyManyIdx1> mmBs = new HashMap<>(caching.ManyManyIdx1.oneBIdx1ToManyManyIdx1(oldIdx1));
      checkAsync(i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      assertTrue(o.idx1.set(newIdx1));
      assertEquals(i, afterSleep, newIdx1, o.idx1.getAsInt());
      checkAsync(i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      assertFalse(o.idx1.set(newIdx1));
      o.idx1.revert();

      assertEquals(i, afterSleep, oldIdx1, o.id.getAsInt());
      assertEquals(i, true, o, caching.One.idToOne(oldIdx1));
      checkAsync(i, true, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);

      o.idx1.set(newIdx1);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(j, o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, as ? null : o, caching.One.idx1ToOne(as ? oldIdx1 : newIdx1));
          checkAsync(j, as, o, newIdx1, oldIdx1, oo, oms, mmAs, mmBs);
        });

      o.idx1.set(oldIdx1);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(j, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, o, caching.One.idx1ToOne(oldIdx1));
          checkAsync(j, as, o, oldIdx1, newIdx1, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyIdx1> list = new ArrayList<>(caching.ManyManyIdx1.idToManyManyIdx1().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyIdx1 mm = list.get(i);
      final caching.One oa = mm.oneAIdx1$One_idx1();
      final caching.One ob = mm.oneBIdx1$One_idx1();

      assertTrue(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
      assertTrue(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));

      DELETE(transaction, mm, i, true,
        j -> {
          assertFalse(caching.ManyManyIdx1.idToManyManyIdx1().containsValue(mm));
          assertFalse(oa.idx1$ManyManyIdx1_oneAIdx1().containsValue(mm));
          assertFalse(ob.idx1$ManyManyIdx1_oneBIdx1().containsValue(mm));
        },
        (j, as) -> {
        });
    }
  }
}