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

  @Test
  @Spec(order = 1)
  public void testInsert(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (int i = 0; i < iterations; ++i) {
      final caching.One o = new caching.One(i);
      o.idu.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o);
      assertSame(o, caching.One.idToOne(i));

      final caching.OneOneId oo = new caching.OneOneId();
      oo.oneId.set(i);
      INSERT(transaction, oo);
      assertSame(oo, caching.OneOneId.oneIdToOneOneId(i));

      final caching.One o1 = oo.oneId$One_id();
      assertSame(o, o1);

      final caching.OneOneId oo1 = o.id$OneOneId_oneId();
      assertSame(oo, oo1);

      for (int j = 0; j < iterations; ++j) {
        final int oneManyId = i * iterations + j;
        final caching.OneManyId om = new caching.OneManyId(oneManyId);
        om.oneId.set(i);
        INSERT(transaction, om);
        assertSame(om, caching.OneManyId.idToOneManyId(oneManyId));

        final Map<data.Key,caching.OneManyId> oms = caching.OneManyId.oneIdToOneManyId(i);
        assertTrue(oms.containsValue(om));
        assertEquals(j + 1, oms.size());

        final caching.One o2 = om.oneId$One_id();
        assertSame(o, o2);

        final Map<data.Key,caching.OneManyId> oms0 = o.id$OneManyId_oneId();
        assertEquals(j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) {
        final int manyManyId = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyId mm = new caching.ManyManyId(manyManyId);
        mm.oneAId.set(a);
        mm.oneBId.set(b);
        INSERT(transaction, mm);
        assertSame(mm, caching.ManyManyId.idToManyManyId(manyManyId));
        assertSame(caching.One.idToOne(a), mm.oneAId$One_id());
        assertSame(caching.One.idToOne(b), mm.oneBId$One_id());

        final Map<data.Key,caching.ManyManyId> mmas = caching.ManyManyId.oneAIdToManyManyId(a);
        assertEquals(i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyId> mmbs = caching.ManyManyId.oneBIdToManyManyId(b);
        assertEquals(i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (caching.ManyManyId mm0 : new ArrayList<>(caching.ManyManyId.idToManyManyId().values())) {
      final caching.ManyManyId mm = mm0.clone();

      final int oldId = mm.id.get();
      final int newId = mm.id.get() + idOffset;

      assertSame(mm, caching.ManyManyId.idToManyManyId(oldId));
      assertNull(caching.ManyManyId.idToManyManyId(newId));

      final caching.One oa = mm.oneAId$One_id();
      final caching.One ob = mm.oneBId$One_id();
      assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
      assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));

      mm.id.set(newId);
      // assertSame(mm, caching.ManyManyId.idToManyManyId(oldId));
      assertNull(caching.ManyManyId.idToManyManyId(newId));

      mm.id.set(newId);
      mm.id.revert();
      assertEquals(oldId, mm.id.getAsInt());
      mm.id.set(newId);

      assertNotEquals(mm.id.get().toString(), SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(0, mm.auto.getAsInt());

      UPDATE(transaction, mm,
        () -> {
          assertNull(caching.ManyManyId.idToManyManyId(oldId));
          assertSame(mm, caching.ManyManyId.idToManyManyId(newId));
        },
        () -> {
          assertSame(oa, mm.oneAId$One_id());
          assertSame(ob, mm.oneBId$One_id());

          assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
          assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));
        });

      assertEquals(SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final caching.One o, final int id1, final int id2, final caching.OneOneId oo, final Map<data.Key,caching.OneManyId> oms, final Map<data.Key,caching.ManyManyId> mmAs, final Map<data.Key,caching.ManyManyId> mmBs) {
    assertNull(caching.One.idToOne(id2));
    assertSame(o, caching.One.idToOne(id1));
  }

  private static void checkAsync(final caching.One o, final int id1, final int id2, final caching.OneOneId oo, final Map<data.Key,caching.OneManyId> oms, final Map<data.Key,caching.ManyManyId> mmAs, final Map<data.Key,caching.ManyManyId> mmBs) {
    assertSame(oo, o.id$OneOneId_oneId()); // NOTE: CASCADE rule in DML ensures this is always true
    assertSame(o.getKeyOld(), oo.oneId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals("oldId: " + id1, oms.size(), caching.OneManyId.oneIdToOneManyId(id1).size());
    assertEquals("newId: " + id2, 0, caching.OneManyId.oneIdToOneManyId(id2).size());

    for (final caching.OneManyId om : oms.values()) {
      assertTrue(o.id$OneManyId_oneId().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), om.oneId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldId: " + id1, mmAs.size(), caching.ManyManyId.oneAIdToManyManyId(id1).size());
    assertEquals("newId: " + id2, 0, caching.ManyManyId.oneAIdToManyManyId(id2).size());

    for (final caching.ManyManyId mm : mmAs.values()) {
      assertTrue(o.id$ManyManyId_oneAId().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneAId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldId: " + id1, mmBs.size(), caching.ManyManyId.oneBIdToManyManyId(id1).size());
    assertEquals("newId: " + id2, 0, caching.ManyManyId.oneBIdToManyManyId(id2).size());

    for (final caching.ManyManyId mm : mmBs.values()) {
      assertTrue(o.id$ManyManyId_oneBId().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneBId$One_id().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.One o0 : new ArrayList<>(caching.One.idToOne().values())) {
      final caching.One o = o0.clone();

      final int oldId = o.id.get();
      final int newId = o.id.get() + idOffset;

      final caching.OneOneId oo = o.id$OneOneId_oneId();
      final Map<data.Key,caching.OneManyId> oms = new HashMap<>(caching.OneManyId.oneIdToOneManyId(oldId));
      final Map<data.Key,caching.ManyManyId> mmAs = new HashMap<>(caching.ManyManyId.oneAIdToManyManyId(oldId));
      final Map<data.Key,caching.ManyManyId> mmBs = new HashMap<>(caching.ManyManyId.oneBIdToManyManyId(oldId));
      checkAsync(o, oldId, newId, oo, oms, mmAs, mmBs);

      o.id.set(newId);

      assertEquals(newId, o.id.getAsInt());
      checkAsync(o, oldId, newId, oo, oms, mmAs, mmBs);

      o.id.set(newId);
      o.id.revert();

      assertEquals(oldId, o.id.getAsInt());
      assertSame(o, caching.One.idToOne(oldId));
      checkAsync(o, oldId, newId, oo, oms, mmAs, mmBs);

      o.id.set(newId);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, newId, oldId, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.idToOne(newId));
          checkSync(o, newId, oldId, oo, oms, mmAs, mmBs);
          checkAsync(o, newId, oldId, oo, oms, mmAs, mmBs);
        });

      o.id.set(oldId);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, oldId, newId, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.idToOne(oldId));
          checkSync(o, oldId, newId, oo, oms, mmAs, mmBs);
          checkAsync(o, oldId, newId, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.ManyManyId mm : new ArrayList<>(caching.ManyManyId.idToManyManyId().values())) {
      final caching.One oa = mm.oneAId$One_id();
      final caching.One ob = mm.oneBId$One_id();

      assertTrue(oa.id$ManyManyId_oneAId().containsValue(mm));
      assertTrue(ob.id$ManyManyId_oneBId().containsValue(mm));

      DELETE(transaction, mm,
        () -> {
          assertFalse(caching.ManyManyId.idToManyManyId().containsValue(mm));
          assertFalse(oa.id$ManyManyId_oneAId().containsValue(mm));
          assertFalse(ob.id$ManyManyId_oneBId().containsValue(mm));
        },
        () -> {
        });
    }
  }
}