package org.jaxdb.jsql;

import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Queue;

import org.jaxdb.jsql.Notification.DefaultListener;
import org.jaxdb.jsql.keyword.Select.Entity.SELECT;

public class CacheConfig {
  static final QueryConfig withoutCacheSelectEntity = new QueryConfig.Builder().withCacheSelectEntity(false).build();

  @FunctionalInterface
  public static interface OnConnectPreLoad<T extends data.Table> {
    SELECT<T> apply(T t) throws IOException, SQLException;

    public static final OnConnectPreLoad<data.Table> ALL = (final data.Table table) -> {
      if (table._mutable$)
        throw new IllegalArgumentException("Table is mutable");

      final Schema schema = table.getSchema();
      final Connector connector = schema.getConnector();
      final Notifier<?> notifier = schema.getCacheNotifier();
      try (
        final RowIterator<data.Table> rows =
          SELECT(table)
            .FROM(table)
            .execute(connector, withoutCacheSelectEntity)
      ) {
        while (rows.nextRow())
          notifier.onSelect(rows.nextEntity());

        table._commitSelectAll$();
      }

      return null;
    };
  }

  private Schema schema;
  private DefaultListener notificationListener;
  private Queue queue;
  private ArrayList<OnConnectPreLoad> onConnectPreLoads = new ArrayList<>();
  private LinkedHashSet tables = new LinkedHashSet<>();

  CacheConfig(final Schema schema, final DefaultListener<data.Table> notificationListener, final Queue<Notification<data.Table>> queue) {
    this.schema = schema;
    this.notificationListener = notificationListener;
    this.queue = queue;
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> CacheConfig with(final T table) {
    final data.Table t = (data.Table)table;
    onConnectPreLoads.add(null);
    t.setCacheSelectEntity(true);
    if (!this.tables.add(t))
      throw new IllegalArgumentException("Table \"" + t.getName() + "\" is specified multiple times");

    return this;
  }

  public CacheConfig with(final type.Table$[] tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) // [A]
      with(tables[i]);

    return this;
  }

  public CacheConfig with(final type.Table$ table, final type.Table$ ... tables) {
    with(table);
    with(tables);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> CacheConfig with(final OnConnectPreLoad<? super T> onConnectPreLoad, final T table) {
    final data.Table t = (data.Table)table;
    onConnectPreLoads.add(onConnectPreLoad);
    t.setCacheSelectEntity(true);
    if (!this.tables.add(t))
      throw new IllegalArgumentException("Table \"" + t.getName() + "\" is specified multiple times");

    return this;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final type.Table$[] tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) // [A]
      with(onConnectPreLoad, tables[i]);

    return this;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final type.Table$ table, final type.Table$ ... tables) {
    with(onConnectPreLoad, table);
    with(onConnectPreLoad, tables);
    return this;
  }

  @SuppressWarnings("unchecked")
  public CacheConfig with(final boolean cacheSelectEntity, final type.Table$ table) {
    final data.Table t = (data.Table)table;
    onConnectPreLoads.add(OnConnectPreLoad.ALL);
    t.setCacheSelectEntity(cacheSelectEntity);
    if (!this.tables.add(t))
      throw new IllegalArgumentException("Table \"" + t.getName() + "\" is specified multiple times");

    return this;
  }

  public CacheConfig with(final boolean cacheSelectEntity, final type.Table$[] tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) // [A]
      with(cacheSelectEntity, tables[i]);

    return this;
  }

  public CacheConfig with(final boolean cacheSelectEntity, final type.Table$ table, final type.Table$ ... tables) {
    with(cacheSelectEntity, table);
    with(cacheSelectEntity, tables);
    return this;
  }

  @SuppressWarnings("unchecked")
  public <T extends type.Table$> CacheConfig with(final OnConnectPreLoad<? super T> onConnectPreLoad, final boolean cacheSelectEntity, final T table) {
    final data.Table t = (data.Table)table;
    onConnectPreLoads.add(onConnectPreLoad);
    t.setCacheSelectEntity(cacheSelectEntity);
    if (!this.tables.add(t))
      throw new IllegalArgumentException("Table \"" + t.getName() + "\" is specified multiple times");

    return this;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final boolean cacheSelectEntity, final type.Table$[] tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) // [A]
      with(onConnectPreLoad, cacheSelectEntity, tables[i]);

    return this;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final boolean cacheSelectEntity, final type.Table$ table, final type.Table$ ... tables) {
    with(onConnectPreLoad, cacheSelectEntity, table);
    with(onConnectPreLoad, cacheSelectEntity, tables);
    return this;
  }

  void commit() throws IOException, SQLException {
    schema.initCache(notificationListener, queue, tables, onConnectPreLoads);

    schema = null;
    notificationListener = null;
    queue = null;
    onConnectPreLoads = null;
    tables = null;
  }
}