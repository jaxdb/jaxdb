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

import static org.jaxdb.jsql.Notification.Action.*;
import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.jaxdb.jsql.Notification.Action.DELETE;
import org.jaxdb.jsql.Notification.Action.INSERT;
import org.jaxdb.jsql.Notification.Action.UPDATE;
import org.jaxdb.vendor.DBVendor;
import org.libj.sql.exception.SQLExceptions;
import org.libj.util.ConcurrentHashSet;

public class Connector implements ConnectionFactory {
  private static final ConcurrentHashMap<String,ConcurrentHashSet<Class<? extends Schema>>> initialized = new ConcurrentHashMap<>();

  private final Class<? extends Schema> schemaClass;
  private final String dataSourceId;

  private ConnectionFactory connectionFactory;
  private boolean prepared;

  private Notifier<?> notifier;

  protected Connector(final Class<? extends Schema> schemaClass, final String dataSourceId) {
    this.schemaClass = assertNotNull(schemaClass);
    this.dataSourceId = dataSourceId;
  }

  protected void set(final ConnectionFactory connectionFactory, final boolean prepared) {
    this.connectionFactory = assertNotNull(connectionFactory);
    this.prepared = prepared;
  }

  public boolean isPrepared() {
    return prepared;
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), null, null, notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final UPDATE update, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, assertNotNull(update), null, notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, null, assertNotNull(delete), notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final UPDATE update, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), assertNotNull(update), null, notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final UPDATE update, final DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(null, assertNotNull(update), assertNotNull(delete), notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), null, assertNotNull(delete), notificationListener, tables);
  }

  @SuppressWarnings("unchecked")
  public <T extends data.Table<?>>boolean addNotificationListener(final INSERT insert, final UPDATE update, final DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    return addNotificationListener0(assertNotNull(insert), assertNotNull(update), assertNotNull(delete), notificationListener, tables);
  }

  @SuppressWarnings({"resource", "unchecked"})
  private <T extends data.Table<?>>boolean addNotificationListener0(final INSERT insert, final UPDATE update, final DELETE delete, final Notification.Listener<T> notificationListener, final T ... tables) throws IOException, SQLException {
    assertNotNull(notificationListener);
    assertNotEmpty(tables);
    if (notifier == null) {
      final Connection connection = connectionFactory.getConnection();
      final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
      if (vendor == DBVendor.POSTGRE_SQL) {
        notifier = new PostgreSQLNotifier(connection, this);
      }
      else {
        connection.close();
        throw new UnsupportedOperationException("Unsupported DBVendor: " + vendor);
      }
    }

    return notifier.addNotificationListener(insert, update, delete, notificationListener, tables);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners() throws IOException, SQLException {
    return removeNotificationListeners0(INSERT, UPDATE, DELETE);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, null);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final UPDATE update) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(update), null);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(null, null, assertNotNull(delete));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UPDATE update) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(update), null);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, assertNotNull(delete));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final UPDATE update, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(update), assertNotNull(delete));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UPDATE update, final DELETE delete) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(update), assertNotNull(delete));
  }

  private <T extends data.Table<?>>boolean removeNotificationListeners0(final INSERT insert, final UPDATE update, final DELETE delete) throws IOException, SQLException {
    return notifier != null && notifier.removeNotificationListeners(insert, update, delete);
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, null, assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final UPDATE update, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(update), null, assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final DELETE delete, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(null, null, assertNotNull(delete), assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UPDATE update, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(update), null, assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final DELETE delete, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), null, assertNotNull(delete), assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final UPDATE update, final DELETE delete, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(null, assertNotNull(update), assertNotNull(delete), assertNotNull(table));
  }

  public <T extends data.Table<?>>boolean removeNotificationListeners(final INSERT insert, final UPDATE update, final DELETE delete, final T table) throws IOException, SQLException {
    return removeNotificationListeners0(assertNotNull(insert), assertNotNull(update), assertNotNull(delete), assertNotNull(table));
  }

  private <T extends data.Table<?>>boolean removeNotificationListeners0(final INSERT insert, final UPDATE update, final DELETE delete, final T table) throws IOException, SQLException {
    return notifier != null && notifier.removeNotificationListeners(insert, update, delete, assertNotNull(table));
  }

  @Override
  public Connection getConnection() throws IOException, SQLException {
    try {
      final Connection connection = connectionFactory.getConnection();
      final String url = connection.getMetaData().getURL();
      ConcurrentHashSet<Class<? extends Schema>> schemas = initialized.get(url);
      final DatabaseMetaData metaData = connection.getMetaData();
      if (schemas == null) {
        initialized.put(url, schemas = new ConcurrentHashSet<>());
        schemas.add(schemaClass);
        final Compiler compiler = Compiler.getCompiler(DBVendor.valueOf(metaData));
        compiler.onConnect(connection);
        compiler.onRegister(connection);
        if (!connection.getAutoCommit())
          connection.commit();
      }
      else if (schemas.add(schemaClass)) {
        final Compiler compiler = Compiler.getCompiler(DBVendor.valueOf(metaData));
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

  // FIXME: Neither hashCode nor equals are actually being used... which means knowledge of schemaClass or id is not needed in this class!?
  @Override
  public int hashCode() {
    int hashCode = 1;
    hashCode = 31 * hashCode + schemaClass.hashCode();
    if (dataSourceId != null)
      hashCode = 31 * hashCode + dataSourceId.hashCode();

    return hashCode;
  }

  @Override
  public boolean equals(final Object obj) {
    if (this == obj)
      return true;

    if (!(obj instanceof Connector))
      return false;

    final Connector that = (Connector)obj;
    return schemaClass.equals(that.schemaClass) && Objects.equals(dataSourceId, that.dataSourceId);
  }
}