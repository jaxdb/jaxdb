/* Copyright (c) 2014 JAX-DB
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

import javax.sql.DataSource;

import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;

public final class Registry {
  private static Connector makeConnector(final DataSource dataSource) {
    if (dataSource == null)
      throw new IllegalArgumentException("dataSource == null");

    return () -> new AuditConnection(dataSource.getConnection());
  }

  private static class Registration extends ConcurrentNullHashMap<String,Connector> {
    private static final long serialVersionUID = -2091355983072874737L;
    private final boolean prepared;

    private Registration(final boolean prepared) {
      this.prepared = prepared;
    }
  }

  private final ConcurrentNullHashMap<Class<? extends Schema>,Registration> registrations = new ConcurrentNullHashMap<>();

  private void register(final Class<? extends Schema> schema, final Connector dataSource, final boolean prepared, final String id) {
    Registration registration = registrations.get(schema);
    if (registration == null)
      registrations.put(schema, registration = new Registration(prepared));

    registration.put(id, dataSource);
  }

  private static Registry getRegistry() {
    return global != null ? global : threadLocal != null ? threadLocal.get() : null;
  }

  static Connector getConnector(final Class<? extends Schema> schema, final String id) {
    final Registry registry = getRegistry();
    if (registry == null)
      return null;

    final Registration registration = registry.registrations.get(schema);
    return registration == null ? null : registration.get(id);
  }

  static boolean isPrepared(final Class<? extends Schema> schema) {
    final Registry registry = getRegistry();
    if (registry == null)
      return false;

    final Registration registration = registry.registrations.get(schema);
    return registration != null && registration.prepared;
  }

  public void register(final Class<? extends Schema> schema, final Connector connector) {
    if (connector == null)
      throw new IllegalArgumentException("connector == null");

    register(schema, connector, false, null);
  }

  public void register(final Class<? extends Schema> schema, final Connector connector, final String id) {
    if (connector == null)
      throw new IllegalArgumentException("connector == null");

    register(schema, connector, false, id);
  }

  public void register(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), false, null);
  }

  public void register(final Class<? extends Schema> schema, final DataSource dataSource, final String id) {
    register(schema, makeConnector(dataSource), false, id);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final Connector connector) {
    if (connector == null)
      throw new IllegalArgumentException("connector == null");

    register(schema, connector, true, null);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final Connector connector, final String id) {
    if (connector == null)
      throw new IllegalArgumentException("connector == null");

    register(schema, connector, true, id);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), true, null);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final DataSource dataSource, final String id) {
    register(schema, makeConnector(dataSource), true, id);
  }

  private static volatile ThreadLocal<Registry> threadLocal;
  private static volatile Registry global;

  public static Registry threadLocal() {
    if (threadLocal != null)
      return threadLocal.get();

    synchronized (Registry.class) {
      if (threadLocal != null)
        return threadLocal.get();

      threadLocal = new ThreadLocal<Registry>() {
        @Override
        protected Registry initialValue() {
          return new Registry();
        }
      };

      return threadLocal.get();
    }
  }

  public static Registry global() {
    if (global != null)
      return global;

    synchronized (Registry.class) {
      return global == null ? global = new Registry() : global;
    }
  }

  private Registry() {
  }
}