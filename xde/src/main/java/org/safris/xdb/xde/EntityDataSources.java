package org.safris.xdb.xde;

import java.util.HashMap;
import java.util.Map;

public final class EntityDataSources {
  private static Map<Class<? extends Schema>,EntityDataSource> sources = new HashMap<Class<? extends Schema>,EntityDataSource>();

  public static void register(final Class<? extends Schema> schema, final EntityDataSource source) {
    sources.put(schema, source);
  }

  protected static EntityDataSource lookup(final Class<? extends Schema> schema) {
    return sources.get(schema);
  }

  private EntityDataSources() {
  }
}