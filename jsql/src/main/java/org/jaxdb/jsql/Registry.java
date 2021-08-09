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

import org.libj.lang.Assertions;
import org.libj.lang.ObjectUtil;
import org.libj.sql.AuditConnection;
import org.libj.util.ConcurrentNullHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Registry {
  private static final Logger logger = LoggerFactory.getLogger(Registry.class);

  private static Connector makeConnector(final DataSource dataSource) {
    Assertions.assertNotNull(dataSource, "dataSource == null");
    return () -> new AuditConnection(dataSource.getConnection());
  }

  private class Registration {
    private final Connector connector;
    private final boolean isPrepared;

    private Registration(final Connector connector, final boolean isPrepared) {
      this.connector = connector;
      this.isPrepared = isPrepared;
    }
  }

  private final ConcurrentNullHashMap<Class<? extends Schema>,ConcurrentNullHashMap<String,Registration>> registrations = new ConcurrentNullHashMap<>();

  private static Registry getRegistry() {
    return global != null ? global : threadLocal != null ? threadLocal.get() : null;
  }

  static Connector getConnector(final Class<? extends Schema> schema, final String id) {
    final Registry registry = getRegistry();
    if (registry == null)
      return null;

    final ConcurrentNullHashMap<String,Registration> registrations = registry.registrations.get(schema);
    if (registrations == null)
      return null;

    final Registration registration = registrations.get(id);
    return registration == null ? null : registration.connector;
  }

  static boolean isPrepared(final Class<? extends Schema> schema, final String id) {
    final Registry registry = getRegistry();
    if (registry == null)
      return false;

    final ConcurrentNullHashMap<String,Registration> registrations = registry.registrations.get(schema);
    if (registrations == null)
      return false;

    final Registration registration = registrations.get(id);
    return registration != null && registration.isPrepared;
  }

  private void register(final Class<? extends Schema> schema, final Connector connector, final boolean prepared, final String id) {
    if (logger.isDebugEnabled())
      logger.debug("register(" + (schema == null ? "null" : schema.getName()) + "," + ObjectUtil.simpleIdentityString(connector) + "," + prepared + ",\"" + id + "\")");

    ConcurrentNullHashMap<String,Registration> registrations = this.registrations.get(schema);
    if (registrations == null)
      this.registrations.put(schema, registrations = new ConcurrentNullHashMap<>(2));

    registrations.put(id, new Registration(connector, prepared));
  }

  public void register(final Class<? extends Schema> schema, final Connector connector) {
    register(schema, Assertions.assertNotNull(connector, "connector == null"), false, null);
  }

  public void register(final Class<? extends Schema> schema, final Connector connector, final String id) {
    register(schema, Assertions.assertNotNull(connector, "connector == null"), false, id);
  }

  public void register(final Class<? extends Schema> schema, final DataSource dataSource) {
    register(schema, makeConnector(dataSource), false, null);
  }

  public void register(final Class<? extends Schema> schema, final DataSource dataSource, final String id) {
    register(schema, makeConnector(dataSource), false, id);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final Connector connector) {
    register(schema, Assertions.assertNotNull(connector, "connector == null"), true, null);
  }

  public void registerPrepared(final Class<? extends Schema> schema, final Connector connector, final String id) {
    register(schema, Assertions.assertNotNull(connector, "connector == null"), true, id);
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