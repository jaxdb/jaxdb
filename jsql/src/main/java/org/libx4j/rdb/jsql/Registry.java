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
import java.util.IdentityHashMap;

import javax.sql.DataSource;

import org.lib4j.sql.ConnectionProxy;
import org.lib4j.util.IdentityHashSet;

public final class Registry {
  private static Connector makeConnector(final DataSource dataSource) {
    return dataSource == null ? null : new Connector() {
      @Override
      public Connection getConnection() throws SQLException {
        return new ConnectionProxy(dataSource.getConnection());
      }
    };
  }

  private static final IdentityHashMap<Class<? extends Schema>,Connector> dataSources = new IdentityHashMap<Class<? extends Schema>,Connector>();
  private static final IdentityHashSet<Class<? extends Schema>> prepared = new IdentityHashSet<Class<? extends Schema>>();

  public static void register(final Class<? extends Schema> schema, final Connector dataSource) {
    register(schema, dataSource, false);
  }

  public static void register(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), false);
  }

  public static void registerPrepared(final Class<? extends Schema> schema, final Connector connector) {
    register(schema, connector, true);
  }

  public static void registerPrepared(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), true);
  }

  private static void register(final Class<? extends Schema> schema, final Connector dataSource, final boolean prepared) {
    if (dataSource == null)
      throw new NullPointerException("dataSource == null");

    dataSources.put(schema, dataSource);
    if (prepared)
      Registry.prepared.add(schema);
  }

  protected static Connector getDataSource(final Class<? extends Schema> schema) {
    return dataSources.get(schema);
  }

  protected static boolean isPrepared(final Class<? extends Schema> schema) {
    return prepared.contains(schema);
  }

  private Registry() {
  }
}