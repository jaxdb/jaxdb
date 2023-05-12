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

import java.util.concurrent.atomic.AtomicBoolean;

public class TestDatabase extends Database {
  public static Database global(final Class<? extends Schema> schemaClass) {
    final Object[] localGlobal = schemaClassToLocalGlobal.get(schemaClass);
    Database database = (Database)localGlobal[1];
    if (database == null)
      localGlobal[1] = database = new TestDatabase(schemaClass);

    return database;
  }

  public static boolean called() {
    if (!called.get().get())
      return false;

    called.get().set(false);
    return true;
  }

  private static ThreadLocal<AtomicBoolean> called = new ThreadLocal<AtomicBoolean>() {
    @Override
    protected AtomicBoolean initialValue() {
      return new AtomicBoolean();
    }
  };

  TestDatabase(final Class<? extends Schema> schemaClass) {
    super(schemaClass);
  }

  @Override
  Notifier<?> getCacheNotifier() {
    called.get().set(true);
    return super.getCacheNotifier();
  }
}