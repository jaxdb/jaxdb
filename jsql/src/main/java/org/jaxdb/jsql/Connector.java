/* Copyright (c) 2021 JAX-DB
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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Notification.Action;
import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UP;
import org.jaxdb.vendor.DbVendor;
import org.libj.sql.exception.SQLExceptions;
import org.libj.util.ConcurrentHashSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Connector implements ConnectionFactory {
  private static final Logger logger = LoggerFactory.getLogger(Connector.class);
  private static final ConcurrentHashMap<String,ConcurrentHashSet<Class<? extends Schema>>> initialized = new ConcurrentHashMap<>();

  private final Schema schema;
  private final Class<? extends Schema> schemaClass;

  private final ConnectionFactory connectionFactory;
  private final boolean isPrepared;
  private volatile Notifier<?> notifier;

  protected Connector(final Schema schema, final ConnectionFactory connectionFactory, final boolean isPrepared) {
    this.schema = schema;
    this.schemaClass = schema.getClass();
    this.connectionFactory = assertNotNull(connectionFactory);
    this.isPrepared = isPrepared;
  }

  Schema getSchema() {
    return schema;
  }

  public boolean isPrepared() {
    return isPrepared;
  }

  Notifier<?> getNotifier() {
    return notifier;
  }

  public boolean hasNotificationListener(final INSERT insert, final type.Table$ table) {
    return hasNotificationListener0(assertNotNull(insert), null, null, table);
  }

  public boolean hasNotificationListener(final UP up, final type.Table$ table) {
    return hasNotificationListener0(null, assertNotNull(up), null, table);
  }

  public boolean hasNotificationListener(final DELETE delete, final type.Table$ table) {
    return hasNotificationListener0(null, null, assertNotNull(delete), table);
  }

  public boolean hasNotificationListener(final INSERT insert, final UP up, final type.Table$ table) {
    return hasNotificationListener0(assertNotNull(insert), assertNotNull(up), null, table);
  }

  public boolean hasNotificationListener(final UP up, final DELETE delete, final type.Table$ table) {
    return hasNotificationListener0(null, assertNotNull(up), assertNotNull(delete), table);
  }

  public boolean hasNotificationListener(final INSERT insert, final DELETE delete, final type.Table$ table) {
    return hasNotificationListener0(assertNotNull(insert), null, assertNotNull(delete), table);
  }

  public boolean hasNotificationListener(final INSERT insert, final UP up, final DELETE delete, final type.Table$ table) {
    return hasNotificationListener0(assertNotNull(insert), assertNotNull(up), assertNotNull(delete), table);
  }

  private boolean hasNotificationListener0(final INSERT insert, final UP up, final DELETE delete, final type.Table$ table) {
    final Notifier<?> notifier = this.notifier;
    return notifier != null && notifier.hasNotificationListener(insert, up, delete, (data.Table)table);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$> boolean addNotificationListener(final INSERT insert, final Notification.InsertListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), null, null, assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$> boolean addNotificationListener(final UP up, final Notification.UpdateListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, assertNotNull(up), null, assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$> boolean addNotificationListener(final DELETE delete, final Notification.DeleteListener<T> notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, null, assertNotNull(delete), assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$,L extends Notification.InsertListener<T> & Notification.UpdateListener<T>> boolean addNotificationListener(final INSERT insert, final UP up, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), assertNotNull(up), null, assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$,L extends Notification.UpdateListener<T> & Notification.DeleteListener<T>> boolean addNotificationListener(final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, assertNotNull(up), assertNotNull(delete), assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$,L extends Notification.InsertListener<T> & Notification.DeleteListener<T>> boolean addNotificationListener(final INSERT insert, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), null, assertNotNull(delete), assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table & type.Table$,L extends Notification.InsertListener<T> & Notification.UpdateListener<T> & Notification.DeleteListener<T>> boolean addNotificationListener(final INSERT insert, final UP up, final DELETE delete, final L notificationListener, final Queue<Notification<T>> queue, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), assertNotNull(up), assertNotNull(delete), assertNotNull(notificationListener), queue, assertNotEmpty(tables));
  }

  @SuppressWarnings({"rawtypes", "resource", "unchecked"})
  <T extends data.Table & type.Table$> boolean addNotificationListener0(final INSERT insert, final UP up, final DELETE delete, final Notification.Listener<T> notificationListener, final Queue<Notification<T>> queue, final T[] tables) throws IOException, SQLException {
    Notifier<?> notifier = this.notifier;
    if (notifier == null) {
      synchronized (this) {
        notifier = this.notifier;
        if (notifier == null) {
          final Connection connection = connectionFactory.getConnection();
          final DbVendor vendor = DbVendor.valueOf(connection.getMetaData());
          if (vendor == DbVendor.POSTGRE_SQL) {
            this.notifier = notifier = new PostgreSQLNotifier(connection, this);
          }
          else {
            connection.close();
            throw new UnsupportedOperationException("Unsupported DbVendor: " + vendor);
          }
        }
      }
    }

    return notifier.addNotificationListener(insert, up, delete, notificationListener, (Queue)queue, tables);
  }

  public <T extends data.Table> boolean removeNotificationListeners() throws IOException, SQLException {
    return removeNotificationListeners0(Action.INSERT, Action.UP, Action.DELETE);
  }

  public <T extends data.Table> boolean removeNotificationListeners(final INSERT insert) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, null);
  }

  public <T extends data.Table> boolean removeNotificationListeners(final UP up) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(up), null);
  }

  public <T extends data.Table> boolean removeNotificationListeners(final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(null, null, assertNotNull(delete));
  }

  public <T extends data.Table> boolean removeNotificationListeners(final INSERT insert, final UP up) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(up), null);
  }

  public <T extends data.Table> boolean removeNotificationListeners(final INSERT insert, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, assertNotNull(delete));
  }

  public <T extends data.Table> boolean removeNotificationListeners(final UP up, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(up), assertNotNull(delete));
  }

  public <T extends data.Table> boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(up), assertNotNull(delete));
  }

  private <T extends data.Table> boolean removeNotificationListeners0(final INSERT insert, final UP up, final DELETE delete) throws IOException, SQLException {
    final Notifier<?> notifier = this.notifier;
    return notifier != null && notifier.removeNotificationListeners(insert, up, delete);
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final INSERT insert, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, null, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final UP up, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(up), null, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final DELETE delete, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(null, null, assertNotNull(delete), assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final INSERT insert, final UP up, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(up), null, assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final INSERT insert, final DELETE delete, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, assertNotNull(delete), assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final UP up, final DELETE delete, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(up), assertNotNull(delete), assertNotEmpty(tables));
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> boolean removeNotificationListeners(final INSERT insert, final UP up, final DELETE delete, final T ... tables) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(up), assertNotNull(delete), assertNotEmpty(tables));
  }

  private final <T extends type.Table$> boolean removeNotificationListeners0(final INSERT insert, final UP up, final DELETE delete, final T[] tables) throws IOException, SQLException {
    final Notifier<?> notifier = this.notifier;
    return notifier != null && notifier.removeNotificationListeners(insert, up, delete, (data.Table[])tables);
  }

  @Override
  public Connection getConnection(final Transaction.Isolation isolation) throws IOException, SQLException {
    try {
      final Connection connection = connectionFactory.getConnection(isolation);
      final String url = connection.getMetaData().getURL();
      ConcurrentHashSet<Class<? extends Schema>> schemas = initialized.get(url);
      if (schemas == null) {
        synchronized (initialized) {
          schemas = initialized.get(url);
          if (schemas == null) {
            initialized.put(url, schemas = new ConcurrentHashSet<>());
            schemas.add(schemaClass);
            final Compiler compiler = Compiler.getCompiler(DbVendor.valueOf(connection.getMetaData()));
            compiler.onConnect(connection);

            if (!connection.isReadOnly())
              compiler.onRegister(connection);
            else if (logger.isWarnEnabled()) { logger.warn("Unable to run onRegister(Connection) hooks on read-only connection"); }

            if (!connection.getAutoCommit())
              connection.commit();
          }
        }
      }
      else if (schemas.add(schemaClass)) {
        final Compiler compiler = Compiler.getCompiler(DbVendor.valueOf(connection.getMetaData()));
        compiler.onRegister(connection);
        if (!connection.getAutoCommit())
          connection.commit();
      }

      return connection;
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  @Override
  public int hashCode() {
    return 31 + schema.hashCode();
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Connector))
      return false;

    final Connector that = (Connector)obj;
    return schema.equals(that.schema);
  }
}