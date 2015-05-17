/* Copyright (c) 2014 Seva Safris
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

package org.safris.xdb.xde;

import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public final class EntityDataSources {
  private static Map<Class<? extends Schema>,EntityDataSource> sources = new HashMap<Class<? extends Schema>,EntityDataSource>();
  private static Map<Class<? extends Schema>,Class<? extends Statement>> prototypes = new HashMap<Class<? extends Schema>,Class<? extends Statement>>();

  public static void register(final Class<? extends Schema> schema, final Class<? extends Statement> prototype, final EntityDataSource source) {
    sources.put(schema, source);
    prototypes.put(schema, prototype);
  }

  protected static EntityDataSource getDataSource(final Class<? extends Schema> schema) {
    return sources.get(schema);
  }

  protected static Class<? extends Statement> getPrototype(final Class<? extends Schema> schema) {
    return prototypes.get(schema);
  }

  private EntityDataSources() {
  }
}