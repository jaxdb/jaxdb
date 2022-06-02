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

  @Test
  @Spec(order = 1)
  public void testInsert(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (int i = 0; i < iterations; ++i) {
      final caching.One o = new caching.One(i);
      o.idu.set(i);
      o.idx1.set(i);
      o.idx2.set(i);

      INSERT(transaction, o);
      assertSame(o, caching.One.iduToOne(i));

      final caching.OneOneIdu oo = new caching.OneOneIdu();
      oo.id.set(i + idOffset);
      oo.oneIdu.set(i);
      INSERT(transaction, oo);
      assertSame(oo, caching.OneOneIdu.oneIduToOneOneIdu(i));

      final caching.One o1 = oo.oneIdu$One_idu();
      assertSame(o, o1);

      final caching.OneOneIdu oo1 = o.idu$OneOneIdu_oneIdu();
      assertSame(oo, oo1);

      for (int j = 0; j < iterations; ++j) {
        final int oneManyIdu = i * iterations + j;
        final caching.OneManyIdu om = new caching.OneManyIdu(oneManyIdu);
        om.oneIdu.set(i);
        INSERT(transaction, om);
        assertSame(om, caching.OneManyIdu.idToOneManyIdu(oneManyIdu));

        final Map<data.Key,caching.OneManyIdu> oms = caching.OneManyIdu.oneIduToOneManyIdu(i);
        assertTrue(oms.containsValue(om));
        assertEquals(j + 1, oms.size());

        final caching.One o2 = om.oneIdu$One_idu();
        assertSame(o, o2);

        final Map<data.Key,caching.OneManyIdu> oms0 = o.idu$OneManyIdu_oneIdu();
        assertEquals(j + 1, oms0.size());
        assertTrue(oms0.containsValue(om));
      }

      for (int k = 1; k <= i; ++k) {
        final int manyManyIdu = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyIdu mm = new caching.ManyManyIdu(manyManyIdu);
        mm.oneAIdu.set(a);
        mm.oneBIdu.set(b);
        INSERT(transaction, mm);
        assertSame(mm, caching.ManyManyIdu.idToManyManyIdu(manyManyIdu));
        assertSame(caching.One.iduToOne(a), mm.oneAIdu$One_idu());
        assertSame(caching.One.iduToOne(b), mm.oneBIdu$One_idu());

        final Map<data.Key,caching.ManyManyIdu> mmas = caching.ManyManyIdu.oneAIduToManyManyIdu(a);
        assertEquals(i + 1 - k, mmas.size());

        final Map<data.Key,caching.ManyManyIdu> mmbs = caching.ManyManyIdu.oneBIduToManyManyIdu(b);
        assertEquals(i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (caching.ManyManyIdu mm0 : new ArrayList<>(caching.ManyManyIdu.idToManyManyIdu().values())) {
      final caching.ManyManyIdu mm = mm0.clone();

      final int oldIdu = mm.id.get();
      final int newIdu = mm.id.get() + idOffset;

      assertSame(mm, caching.ManyManyIdu.idToManyManyIdu(oldIdu));
      assertNull(caching.ManyManyIdu.idToManyManyIdu(newIdu));

      final caching.One oa = mm.oneAIdu$One_idu();
      final caching.One ob = mm.oneBIdu$One_idu();
      assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
      assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));

      mm.id.set(newIdu);
      // assertSame(mm, caching.ManyManyIdu.idToManyManyIdu(oldIdu));
      assertNull(caching.ManyManyIdu.idToManyManyIdu(newIdu));

      mm.id.set(newIdu);
      mm.id.revert();
      assertEquals(oldIdu, mm.id.getAsInt());
      mm.id.set(newIdu);

      assertNotEquals(mm.id.get().toString(), SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(0, mm.auto.getAsInt());

      UPDATE(transaction, mm,
        () -> {
          assertNull(caching.ManyManyIdu.idToManyManyIdu(oldIdu));
          assertSame(mm, caching.ManyManyIdu.idToManyManyIdu(newIdu));
        },
        () -> {
          assertSame(oa, mm.oneAIdu$One_idu());
          assertSame(ob, mm.oneBIdu$One_idu());

          assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
          assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));
        });

      assertEquals(SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(1, mm.auto.getAsInt());
    }
  }

  private static void checkSync(final caching.One o, final int id1, final int id2, final caching.OneOneIdu oo, final Map<data.Key,caching.OneManyIdu> oms, final Map<data.Key,caching.ManyManyIdu> mmAs, final Map<data.Key,caching.ManyManyIdu> mmBs) {
    assertNull(caching.One.iduToOne(id2));
    assertSame(o, caching.One.iduToOne(id1));
  }

  private static void checkAsync(final caching.One o, final int id1, final int id2, final caching.OneOneIdu oo, final Map<data.Key,caching.OneManyIdu> oms, final Map<data.Key,caching.ManyManyIdu> mmAs, final Map<data.Key,caching.ManyManyIdu> mmBs) {
    assertSame(oo, o.idu$OneOneIdu_oneIdu()); // NOTE: CASCADE rule in DML ensures this is always true
    assertSame(o.getKeyOld(), oo.oneIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true

    assertEquals("oldIdu: " + id1, oms.size(), caching.OneManyIdu.oneIduToOneManyIdu(id1).size());
    assertEquals("newIdu: " + id2, 0, caching.OneManyIdu.oneIduToOneManyIdu(id2).size());

    for (final caching.OneManyIdu om : oms.values()) {
      assertTrue(o.idu$OneManyIdu_oneIdu().containsValue(om)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), om.oneIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldIdu: " + id1, mmAs.size(), caching.ManyManyIdu.oneAIduToManyManyIdu(id1).size());
    assertEquals("newIdu: " + id2, 0, caching.ManyManyIdu.oneAIduToManyManyIdu(id2).size());

    for (final caching.ManyManyIdu mm : mmAs.values()) {
      assertTrue(o.idu$ManyManyIdu_oneAIdu().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneAIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }

    assertEquals("oldIdu: " + id1, mmBs.size(), caching.ManyManyIdu.oneBIduToManyManyIdu(id1).size());
    assertEquals("newIdu: " + id2, 0, caching.ManyManyIdu.oneBIduToManyManyIdu(id2).size());

    for (final caching.ManyManyIdu mm : mmBs.values()) {
      assertTrue(o.idu$ManyManyIdu_oneBIdu().containsValue(mm)); // NOTE: CASCADE rule in DML ensures this is always true
      assertSame(o.getKeyOld(), mm.oneBIdu$One_idu().getKey()); // NOTE: CASCADE rule in DML ensures this is always true
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.One o0 : new ArrayList<>(caching.One.iduToOne().values())) {
      final caching.One o = o0.clone();

      final int oldIdu = o.idu.get();
      final int newIdu = o.idu.get() + idOffset;

      final caching.OneOneIdu oo = o.idu$OneOneIdu_oneIdu();
      final Map<data.Key,caching.OneManyIdu> oms = new HashMap<>(caching.OneManyIdu.oneIduToOneManyIdu(oldIdu));
      final Map<data.Key,caching.ManyManyIdu> mmAs = new HashMap<>(caching.ManyManyIdu.oneAIduToManyManyIdu(oldIdu));
      final Map<data.Key,caching.ManyManyIdu> mmBs = new HashMap<>(caching.ManyManyIdu.oneBIduToManyManyIdu(oldIdu));
      checkAsync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);

      assertEquals(newIdu, o.idu.getAsInt());
      checkAsync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);
      o.idu.revert();

      assertEquals(oldIdu, o.idu.getAsInt());
      assertSame(o, caching.One.iduToOne(oldIdu));
      checkAsync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);

      o.idu.set(newIdu);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.iduToOne(newIdu));
          checkSync(o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
          checkAsync(o, newIdu, oldIdu, oo, oms, mmAs, mmBs);
        });

      o.idu.set(oldIdu);

      UPDATE(transaction, o,
        () -> {
          checkSync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        },
        () -> {
          assertSame(o, caching.One.iduToOne(oldIdu));
          checkSync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
          checkAsync(o, oldIdu, newIdu, oo, oms, mmAs, mmBs);
        });
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.ManyManyIdu mm : new ArrayList<>(caching.ManyManyIdu.idToManyManyIdu().values())) {
      final caching.One oa = mm.oneAIdu$One_idu();
      final caching.One ob = mm.oneBIdu$One_idu();

      assertTrue(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
      assertTrue(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));

      DELETE(transaction, mm,
        () -> {
          assertFalse(caching.ManyManyIdu.idToManyManyIdu().containsValue(mm));
          assertFalse(oa.idu$ManyManyIdu_oneAIdu().containsValue(mm));
          assertFalse(ob.idu$ManyManyIdu_oneBIdu().containsValue(mm));
        },
        () -> {
        });
    }
  }
}