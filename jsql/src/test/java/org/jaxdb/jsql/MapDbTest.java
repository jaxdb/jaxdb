/* Copyright (c) 2023 JAX-DB
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

import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import org.junit.Test;
import org.mapdb.DB;
import org.mapdb.DBMaker;

public class MapDbTest {
  static void fail(final String message) {
    System.err.println(message);
    System.err.flush();
    System.exit(-1);
  }

  @Test
  @SuppressWarnings("unchecked")
  public void testNoConcurrentModificationException() throws InterruptedException {
    final DB db = DBMaker.heapDB().make();
    final Map<Integer,Integer> map = (Map<Integer,Integer>)db.treeMap("map").counterEnable().create();
    final ExecutorService executor = Executors.newFixedThreadPool(4);
    final AtomicBoolean finished = new AtomicBoolean();
    executor.execute(() -> {
      for (int i = 0; i < 100; ++i) { // [N]
        map.put(i, i);
        try {
          Thread.sleep(10);
        }
        catch (final InterruptedException e) {
          fail(e.getMessage());
        }
      }

      finished.set(true);
    });

    executor.execute(() -> {
      do {
        int last = -1;
        for (final Map.Entry<Integer,Integer> entry : map.entrySet()) { // [S]
          final Integer key = entry.getKey();
          if (key < last)
            fail(key + " < " + last);

          last = key;
        }

        try {
          last = -1;
          Thread.sleep(10);
        }
        catch (final InterruptedException e) {
          fail(e.getMessage());
        }
      }
      while (!finished.get());
    });

    executor.execute(() -> {
      do {
        int last = -1;
        for (final Integer key : map.keySet()) { // [S]
          if (key < last)
            fail(key + " < " + last);

          last = key;
        }

        try {
          last = -1;
          Thread.sleep(10);
        }
        catch (final InterruptedException e) {
          fail(e.getMessage());
        }
      }
      while (!finished.get());
    });

    executor.execute(() -> {
      do {
        int last = -1;
        for (final Integer value : map.values()) { // [C]
          if (value < last)
            fail(value + " < " + last);

          last = value;
        }

        try {
          last = -1;
          Thread.sleep(10);
        }
        catch (final InterruptedException e) {
          fail(e.getMessage());
        }
      }
      while (!finished.get());
    });

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
  }
}