/* Copyright (c) 2014 lib4j
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

package org.libx4j.rdb.jsql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.lib4j.sql.ConnectionProxy;

public final class DBRegistry {
  private static DBConnector makeConnector(final DataSource dataSource) {
    return new DBConnector() {
      @Override
      public Connection getConnection() throws SQLException {
        return new ConnectionProxy(dataSource.getConnection());
      }
    };
  }

  private static final Map<Class<? extends Schema>,DBConnector> dataSources = new HashMap<Class<? extends Schema>,DBConnector>();
  private static final Set<Class<? extends Schema>> prepared = new HashSet<Class<? extends Schema>>();
  private static final Set<Class<? extends Schema>> batching = new HashSet<Class<? extends Schema>>();

  public static void register(final Class<? extends Schema> schema, final DBConnector dataSource) {
    register(schema, dataSource, false, false);
  }

  public static void register(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), false, false);
  }

  public static void registerPrepared(final Class<? extends Schema> schema, final DBConnector connector) {
    register(schema, connector, true, false);
  }

  public static void registerPrepared(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), true, false);
  }

  public static void registerBatching(final Class<? extends Schema> schema, final DBConnector connector) {
    register(schema, connector, false, true);
  }

  public static void registerBatching(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), false, true);
  }

  public static void registerPreparedBatching(final Class<? extends Schema> schema, final DBConnector connector) {
    register(schema, connector, true, true);
  }

  public static void registerPreparedBatching(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), true, true);
  }

  private static void register(final Class<? extends Schema> schema, final DBConnector dataSource, final boolean prepared, final boolean batching) {
    dataSources.put(schema, dataSource);
    if (prepared)
      DBRegistry.prepared.add(schema);

    if (batching)
      DBRegistry.batching.add(schema);
  }

  protected static DBConnector getDataSource(final Class<? extends Schema> schema) {
    return dataSources.get(schema);
  }

  protected static boolean isPrepared(final Class<? extends Schema> schema) {
    return prepared.contains(schema);
  }

  protected static boolean isBatching(final Class<? extends Schema> schema) {
    return batching.contains(schema);
  }

  private DBRegistry() {
  }
}