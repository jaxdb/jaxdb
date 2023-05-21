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
import org.jaxdb.runner.SchemaTestRunner.TestSchema;
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
  @TestSchema(caching.class)
  public void testInsert(final Transaction transaction) throws InterruptedException, IOException, SQLException {
    for (int i = 0; i < iterations; ++i) { // [N]
      final caching.One o = new caching.One(i);
      o.idu.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, o, caching.One.idToOne(i));

      final caching.OneOneId oo = new caching.OneOneId();
      oo.oneId.set(i);
      INSERT(transaction, oo, i, j -> {}, j -> {});
      assertEquals(i, afterSleep, oo, caching.OneOneId.oneIdToOneOneId(i));

      final caching.One o1 = oo.oneId$One_id();
      assertEquals(i, afterSleep, o, o1);

      final caching.OneOneId oo1 = o.id$OneOneId_oneId();
      assertEquals(i, afterSleep, oo, oo1);

      for (int j = 0; j < iterations; ++j) { // [N]
        final int oneManyId = i * iterations + j;
        final caching.OneManyId om = new caching.OneManyId(oneManyId);
        om.oneId.set(i);
        INSERT(transaction, om, i, k -> {}, k -> {});
        assertEquals(i, afterSleep, om, caching.OneManyId.idToOneManyId(oneManyId));

        final Map<data.Key,caching.OneManyId> oms = caching.OneManyId.oneIdToOneManyId(i);
        assertTrue(oms.containsValue(om));
        assertEquals(i, afterSleep, j + 1, oms.size());

        final caching.One o2 = om.oneId$One_id();
        assertEquals(i, afterSleep, o, o2);

        final Map<data.Key,caching.OneManyId> oms0 = o.id$OneManyId_oneId();
        assertEquals(i, afterSleep, j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) { // [N]
        final int manyManyId = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyId mm = new caching.ManyManyId(manyManyId);
        mm.oneAId.set(a);
        mm.oneBId.set(b);
        INSERT(transaction, mm, i, j -> {}, j -> {});
        assertEquals(i, afterSleep, mm, caching.ManyManyId.idToManyManyId(manyManyId));
        assertEquals(i, afterSleep, caching.One.idToOne(a), mm.oneAId$One_id());
        assertEquals(i, afterSleep, caching.One.idToOne(b), mm.oneBId$One_id());

        final Map<data.Key,caching.ManyManyId> mmas = caching.ManyManyId.oneAIdToManyManyId(a);
        assertEquals(i, afterSleep, i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyId> mmbs = caching.ManyManyId.oneBIdToManyManyId(b);
        assertEquals(i, afterSleep, i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @TestSpec(order = 2)
  @TestSchema(caching.class)
  public void testUpdatePrimaryKey(final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyId> list = new ArrayList<>(caching.ManyManyId.idToManyManyId().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyId mm = list.get(i).clone();

      final int oldId = mm.id.get();
      final int newId = mm.id.get() + idOffset;

      assertEquals(i, afterSleep, mm, caching.ManyManyId.idToManyManyId(oldId));
      assertNull(i, afterSleep, caching.ManyManyId.idToManyManyId(newId));

      final caching.One oa = mm.oneAId$One_id();
      final caching.One ob = mm.oneBId$One_id();
      assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
      assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));

      assertTrue(mm.id.set(newId));
      // assertSame(mm, caching.ManyManyId.idToManyManyId(oldId));
      assertNull(i, afterSleep, caching.ManyManyId.idToManyManyId(newId));

      assertFalse(mm.id.set(newId));
      mm.id.revert();
      assertEquals(i, afterSleep, oldId, mm.id.getAsInt());
      mm.id.set(newId);

      assertNotEquals(mm.id.get().toString(), data.Column.SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(i, afterSleep, 0, mm.auto.getAsInt());

      UPDATE(transaction, mm, i, false,
        j -> {
          assertNull(j, afterSleep, caching.ManyManyId.idToManyManyId(oldId));
          assertEquals(j, afterSleep, mm, caching.ManyManyId.idToManyManyId(newId));
        },
        (j, as) -> {
          assertEquals(j, as, oa, mm.oneAId$One_id());
          assertEquals(j, as, ob, mm.oneBId$One_id());

          assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
          assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));
        });

      assertEquals(i, afterSleep, data.Column.SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(i, afterSleep, 1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final int i, final caching.One o, final int id1, final int id2, final caching.OneOneId oo, final Map<data.Key,caching.OneManyId> oms, final Map<data.Key,caching.ManyManyId> mmAs, final Map<data.Key,caching.ManyManyId> mmBs) throws IOException, SQLException {
    assertNull(i, false, caching.One.idToOne(id2));
    assertEquals(i, false, o, caching.One.idToOne(id1));
  }

  private static void checkAsync(final int i, final boolean afterSleep, final caching.One o, final int id1, final int id2, final caching.OneOneId oo, final Map<data.Key,caching.OneManyId> oms, final Map<data.Key,caching.ManyManyId> mmAs, final Map<data.Key,caching.ManyManyId> mmBs) throws IOException, SQLException {
    assertEquals(i, afterSleep, afterSleep ? oo : null, o.id$OneOneId_oneId()); // NOTE: CASCADE rule in DML ensures this is always true
    if (afterSleep)
      assertEquals(i, afterSleep, o.getKeyOld(), oo.oneId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? oms.size() : 0, caching.OneManyId.oneIdToOneManyId(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : oms.size(), caching.OneManyId.oneIdToOneManyId(id2).size());

    for (final caching.OneManyId om : oms.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id$OneManyId_oneId().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), om.oneId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmAs.size() : 0, caching.ManyManyId.oneAIdToManyManyId(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmAs.size(), caching.ManyManyId.oneAIdToManyManyId(id2).size());

    for (final caching.ManyManyId mm : mmAs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id$ManyManyId_oneAId().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneAId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals(i, afterSleep, "oldId: " + id1, afterSleep ? mmBs.size() : 0, caching.ManyManyId.oneBIdToManyManyId(id1).size());
    assertEquals(i, afterSleep, "newId: " + id2, afterSleep ? 0 : mmBs.size(), caching.ManyManyId.oneBIdToManyManyId(id2).size());

    for (final caching.ManyManyId mm : mmBs.values()) { // [C]
      assertEquals(i, afterSleep, afterSleep, o.id$ManyManyId_oneBId().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      if (afterSleep)
        assertEquals(i, afterSleep, o.getKeyOld(), mm.oneBId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @TestSpec(order = 3)
  @TestSchema(caching.class)
  public void testUpdateForeignKey(final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.One> list = new ArrayList<>(caching.One.idToOne().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.One o = list.get(i).clone();

      final int oldId = o.id.get();
      final int newId = o.id.get() + idOffset;

      final caching.OneOneId oo = o.id$OneOneId_oneId();
      final Map<data.Key,caching.OneManyId> oms = new HashMap<>(caching.OneManyId.oneIdToOneManyId(oldId));
      final Map<data.Key,caching.ManyManyId> mmAs = new HashMap<>(caching.ManyManyId.oneAIdToManyManyId(oldId));
      final Map<data.Key,caching.ManyManyId> mmBs = new HashMap<>(caching.ManyManyId.oneBIdToManyManyId(oldId));
      checkAsync(i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      assertTrue(o.id.set(newId));

      assertEquals(i, afterSleep, newId, o.id.getAsInt());
      checkAsync(i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      assertFalse(o.id.set(newId));
      o.id.revert();

      assertEquals(i, afterSleep, oldId, o.id.getAsInt());
      assertEquals(i, true, o, caching.One.idToOne(oldId));
      checkAsync(i, true, o, oldId, newId, oo, oms, mmAs, mmBs);

      o.id.set(newId);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(j, o, newId, oldId, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, as ? o.getKeyOld() : o.getKey(), caching.One.idToOne(newId).getKey());
          checkAsync(j, as, o, newId, oldId, oo, oms, mmAs, mmBs);
        });

      o.id.set(oldId);

      UPDATE(transaction, o, i, true,
        j -> {
          checkSync(j, o, oldId, newId, oo, oms, mmAs, mmBs);
        },
        (j, as) -> {
          assertEquals(j, as, o, caching.One.idToOne(oldId));
          checkAsync(j, as, o, oldId, newId, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @TestSpec(order = 4)
  @TestSchema(caching.class)
  public void testDelete(final Transaction transaction) throws InterruptedException, IOException, SQLException {
    final ArrayList<caching.ManyManyId> list = new ArrayList<>(caching.ManyManyId.idToManyManyId().values());
    for (int i = 0, i$ = list.size(); i < i$; ++i) { // [RA]
      final caching.ManyManyId mm = list.get(i);
      final caching.One oa = mm.oneAId$One_id();
      final caching.One ob = mm.oneBId$One_id();

      assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
      assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));

      DELETE(transaction, mm, i, true,
        j -> {
          assertFalse(caching.ManyManyId.idToManyManyId().containsValue(mm));
          assertFalse(oa.id$ManyManyId_oneAId().containsValue(mm));
          assertFalse(ob.id$ManyManyId_oneBId().containsValue(mm));
        },
        (j, as) -> {
        });
    }
  }
}