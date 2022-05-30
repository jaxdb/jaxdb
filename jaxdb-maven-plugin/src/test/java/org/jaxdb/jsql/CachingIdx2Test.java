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
import java.util.Map;

import org.jaxdb.runner.DBTestRunner.Config;
import org.jaxdb.runner.DBTestRunner.DB;
import org.jaxdb.runner.DBTestRunner.Spec;
import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.runner.PostgreSQL;
import org.jaxdb.runner.SchemaTestRunner;
import org.jaxdb.runner.SchemaTestRunner.Schema;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SchemaTestRunner.class)
@Config(sync = true, deferLog = false, failFast = true)
public abstract class CachingIdx2Test extends CachingTest {
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
      o.idx2.set(i);
      o.idx2.set(i);
      o.idx2.set(i);

      INSERT(transaction, o);
      assertSame(o, caching.One.idx2ToOne(i));

      final caching.OneOneIdx2 oo = new caching.OneOneIdx2();
      oo.oneIdx2.set(i);
      INSERT(transaction, oo);
      assertSame(oo, caching.OneOneIdx2.oneIdx2ToOneOneIdx2(i));

//      final caching.One o1 = oo.oneIdx2$One_idx2();
//      assertSame(o, o1);

//      final caching.OneOneIdx2 oo1 = o.idx2$OneOneIdx2_oneIdx2();
//      assertSame(oo, oo1);

      for (int j = 0; j < iterations; ++j) {
        final int oneManyIdx2 = i * iterations + j;
        final caching.OneManyIdx2 om = new caching.OneManyIdx2(oneManyIdx2);
        om.oneIdx2.set(i);
        INSERT(transaction, om);
        assertSame(om, caching.OneManyIdx2.idToOneManyIdx2(oneManyIdx2));

//        final ConcurrentHashMap<Key,caching.OneManyIdx2> oms = caching.OneManyIdx2.oneIdx2ToOneManyIdx2(i);
//        assertTrue(oms.contains(om));
//        assertEquals(j + 1, oms.size());

//        final caching.One o2 = om.oneIdx2$One_idx2();
//        assertSame(o, o2);

//        final ConcurrentHashMap<Key,caching.OneManyIdx2> oms0 = o.idx2$OneManyIdx2_oneIdx2();
//        assertEquals(j + 1, oms0.size());
//        assertTrue(oms0.contains(om));
      }

      for (int k = 1; k <= i; ++k) {
        final int manyManyIdx2 = i * iterations + k;
        final int a = k - 1;
        final int b = k;

        final caching.ManyManyIdx2 mm = new caching.ManyManyIdx2(manyManyIdx2);
        mm.oneAIdx2.set(a);
        mm.oneBIdx2.set(b);
        INSERT(transaction, mm);
        assertSame(mm, caching.ManyManyIdx2.idToManyManyIdx2(manyManyIdx2));
//        assertSame(caching.One.idx2ToOne(a), mm.oneAIdx2$One_idx2());
//        assertSame(caching.One.idx2ToOne(b), mm.oneBIdx2$One_idx2());

//        final ConcurrentHashMap<Key,caching.ManyManyIdx2> mmas = caching.ManyManyIdx2.oneAIdx2ToManyManyIdx2(a);
//        assertEquals(i + 1 - k, mmas.size());

//        final ConcurrentHashMap<Key,caching.ManyManyIdx2> mmbs = caching.ManyManyIdx2.oneBIdx2ToManyManyIdx2(b);
//        assertEquals(i + 1 - k, mmbs.size());
      }
    }
  }

  @Test
  @Spec(order = 2)
  public void testUpdatePrimaryKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (caching.ManyManyIdx2 mm0 : new ArrayList<>(caching.ManyManyIdx2.idToManyManyIdx2())) {
      final caching.ManyManyIdx2 mm = mm0.clone();

      final int oldIdx2 = mm.id.get();
      final int newIdx2 = mm.id.get() + idOffset;

      assertSame(mm, caching.ManyManyIdx2.idToManyManyIdx2(oldIdx2));
      assertNull(caching.ManyManyIdx2.idToManyManyIdx2(newIdx2));

//      final caching.One oa = mm.oneAIdx2$One_idx2();
//      final caching.One ob = mm.oneBIdx2$One_idx2();
//      assertTrue(oa.idx2$ManyManyIdx2_oneAIdx2().contains(mm));
//      assertTrue(ob.idx2$ManyManyIdx2_oneBIdx2().contains(mm));

      mm.id.set(newIdx2);
      // assertSame(mm, caching.ManyManyIdx2.idToManyManyIdx2(oldIdx2));
      assertNull(caching.ManyManyIdx2.idToManyManyIdx2(newIdx2));

      mm.id.set(newIdx2);
      mm.id.revert();
      assertEquals(oldIdx2, mm.id.getAsInt());
      mm.id.set(newIdx2);

      assertNotEquals(mm.id.get().toString(), SetBy.USER, mm.auto.setByCur); // Can be null, or can be SYSTEM if Notifier updates fast enough to call Column.setColumns(JSON)
      assertEquals(0, mm.auto.getAsInt());

      UPDATE(transaction, mm,
        () -> {
          assertNull(caching.ManyManyIdx2.idToManyManyIdx2(oldIdx2));
          assertSame(mm, caching.ManyManyIdx2.idToManyManyIdx2(newIdx2));
        },
        () -> {
//          assertSame(oa, mm.oneAIdx2$One_idx2());
//          assertSame(ob, mm.oneBIdx2$One_idx2());

//          assertTrue(oa.idx2$ManyManyIdx2_oneAIdx2().contains(mm));
//          assertTrue(ob.idx2$ManyManyIdx2_oneBIdx2().contains(mm));
        });

      assertEquals(SetBy.SYSTEM, mm.auto.setByCur);
      assertEquals(1, mm.auto.getAsInt());
    }
  }

  @Test
  @Spec(order = 3)
  public void testUpdateForeignKey(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final Map<data.Key,caching.One> os : new ArrayList<>(caching.One.idx2ToOne())) {
      for (final caching.One o0 : os.values()) {
        final caching.One o = o0.clone();

        final int oldIdx2 = o.idx2.get();
        final int newIdx2 = o.idx2.get() + idOffset;

  //      final caching.OneOneIdx2 oo = o.idx2$OneOneIdx2_oneIdx2();
  //      final Map<Key,caching.OneManyIdx2> oms = new HashMap<>(caching.OneManyIdx2.oneIdx2ToOneManyIdx2(oldIdx2));
  //      final Map<Key,caching.ManyManyIdx2> mmAs = new HashMap<>(caching.ManyManyIdx2.oneAIdx2ToManyManyIdx2(oldIdx2));
  //      final Map<Key,caching.ManyManyIdx2> mmBs = new HashMap<>(caching.ManyManyIdx2.oneBIdx2ToManyManyIdx2(oldIdx2));
  //      checkAsync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);

        o.idx2.set(newIdx2);

        assertEquals(newIdx2, o.idx2.getAsInt());
  //      checkAsync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);

        o.idx2.set(newIdx2);
        o.idx2.revert();

        assertEquals(oldIdx2, o.idx2.getAsInt());
        assertSame(o, caching.One.idx2ToOne(oldIdx2));
  //      checkAsync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);

        o.idx2.set(newIdx2);

        UPDATE(transaction, o,
          () -> {
  //          checkSync(o, newIdx2, oldIdx2, oo, oms, mmAs, mmBs);
          },
          () -> {
            assertSame(o, caching.One.idx2ToOne(newIdx2));
  //          checkSync(o, newIdx2, oldIdx2, oo, oms, mmAs, mmBs);
  //          checkAsync(o, newIdx2, oldIdx2, oo, oms, mmAs, mmBs);
          });

        o.idx2.set(oldIdx2);

        UPDATE(transaction, o,
          () -> {
  //          checkSync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);
          },
          () -> {
            assertSame(o, caching.One.idx2ToOne(oldIdx2));
  //          checkSync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);
  //          checkAsync(o, oldIdx2, newIdx2, oo, oms, mmAs, mmBs);
          });
      }
    }
  }

  @Test
  @Spec(order = 4)
  public void testDelete(@Schema(caching.class) final Transaction transaction) throws IOException, SQLException {
    for (final caching.ManyManyIdx2 mm : new ArrayList<>(caching.ManyManyIdx2.idToManyManyIdx2())) {
//      final caching.One oa = mm.oneAIdx2$One_idx2();
//      final caching.One ob = mm.oneBIdx2$One_idx2();

//      assertTrue(oa.idx2$ManyManyIdx2_oneAIdx2().contains(mm));
//      assertTrue(ob.idx2$ManyManyIdx2_oneBIdx2().contains(mm));

      DELETE(transaction, mm,
        () -> {
          assertFalse(caching.ManyManyIdx2.idToManyManyIdx2().contains(mm));
//          assertFalse(oa.idx2$ManyManyIdx2_oneAIdx2().contains(mm));
//          assertFalse(ob.idx2$ManyManyIdx2_oneBIdx2().contains(mm));
        },
        () -> {
        });
    }
  }
}