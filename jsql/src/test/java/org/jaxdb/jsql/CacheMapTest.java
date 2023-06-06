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

import static org.junit.Assert.*;

import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.libj.test.TestExecutorService;

public class CacheMapTest {
  private static void sleep(final long millis) {
    if (millis < 0)
      return;

    try {
      Thread.sleep(millis);
    }
    catch (final InterruptedException e) {
      System.err.println(e.getMessage());
      System.err.flush();
      System.exit(-1);
    }
  }

  private static void testNoConcurrentModificationException(final Map<Integer,Integer> map) throws Throwable {
    final TestExecutorService executor = new TestExecutorService(Executors.newFixedThreadPool(4));
    final AtomicInteger count = new AtomicInteger(3);
    executor.execute(() -> {
      int i = 0;
      do {
        map.put(++i, i);
        sleep(2);
      }
      while (count.get() > 0);
    });

    sleep(50);

    executor.execute(() -> {
      try {
        long time;
        for (int i = 0, prev = -1, size; i < 100; ++i, prev = -1) { // [N]
          size = map.size();
          time = System.currentTimeMillis();
          for (final Map.Entry<Integer,Integer> entry : map.entrySet()) { // [S]
            final Integer key = entry.getKey();
            if (key < prev)
              fail("next (" + key + ")" + " < " + "prev (" + prev + ")");

            sleep(5 + time - System.currentTimeMillis());
            prev = key;
          }

          assertTrue(map.size() > size);
          sleep(10);
        }
      }
      finally {
        count.getAndDecrement();
      }
    });

    executor.execute(() -> {
      try {
        long time;
        for (int i = 0, prev = -1, size; i < 100; ++i, prev = -1) { // [N]
          size = map.size();
          time = System.currentTimeMillis();
          for (final Integer key : map.keySet()) { // [S]
            if (key < prev)
              fail("next (" + key + ")" + " < " + "prev (" + prev + ")");

            sleep(5 + time - System.currentTimeMillis());
            prev = key;
          }

          assertTrue(map.size() > size);
          sleep(10);
        }
      }
      finally {
        count.getAndDecrement();
      }
    });

    executor.execute(() -> {
      try {
        long time;
        for (int i = 0, prev = -1, size; i < 100; ++i, prev = -1) { // [N]
          size = map.size();
          time = System.currentTimeMillis();
          for (final Integer value : map.values()) { // [C]
            if (value < prev)
              fail(value + " < " + prev);

            sleep(10 + time - System.currentTimeMillis());
            prev = value;
          }

          assertTrue(map.size() > size);
          sleep(10);
        }
      }
      finally {
        count.getAndDecrement();
      }
    });

    executor.shutdown();
    executor.awaitTermination(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
  }

  @Test
  public void testConcurrentModificationExceptionHashMap() throws Throwable {
    try {
      testNoConcurrentModificationException(new HashMap<>());
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  public void testConcurrentModificationExceptionTreeMap() throws Throwable {
    try {
      testNoConcurrentModificationException(new TreeMap<>());
      fail("Expected ConcurrentModificationException");
    }
    catch (final ConcurrentModificationException e) {
    }
  }

  @Test
  public void testNoConcurrentModificationExceptionConcurrentHashMap() throws Throwable {
    testNoConcurrentModificationException(new ConcurrentHashMap<>());
  }

  @Test
  public void testNoConcurrentModificationExceptionConcurrentSkipListMap() throws Throwable {
    testNoConcurrentModificationException(new ConcurrentSkipListMap<>());
  }
}