package org.jaxdb.jsql;

import static org.jaxdb.jsql.DML.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Queue;

import org.jaxdb.jsql.Notification.DefaultListener;

public class CacheConfig {
  static final QueryConfig withoutCacheSelectEntity = new QueryConfig.Builder().withCacheSelectEntity(false).build();

  @FunctionalInterface
  public static interface OnConnectPreLoad {
    void accept(data.Table t) throws IOException, SQLException;

    public static final OnConnectPreLoad ALL = (final data.Table table) -> {
      if (table._mutable$)
        throw new IllegalArgumentException("Table is mutable");

      final Schema schema = table.getSchema();
      final Connector connector = schema.getConnector();
      final Notifier<?> notifier = schema.getCacheNotifier();
      try (final RowIterator<data.Table> rows =
        SELECT(table).
        FROM(table)
          .execute(connector, withoutCacheSelectEntity)) {
        while (rows.nextRow())
          notifier.onSelect(rows.nextEntity());

        table._commitSelectAll$();
      }
    };
  }

  private Schema schema;
  private DefaultListener<data.Table> notificationListener;
  private Queue<Notification<data.Table>> queue;
  private ArrayList<OnConnectPreLoad> onConnectPreLoads = new ArrayList<>();
  private LinkedHashSet<data.Table> tables = new LinkedHashSet<>();

  CacheConfig(final Schema schema, final DefaultListener<data.Table> notificationListener, final Queue<Notification<data.Table>> queue) {
    this.schema = schema;
    this.notificationListener = notificationListener;
    this.queue = queue;
  }

  public CacheConfig with(final data.Table ... tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final data.Table table = tables[i];
      onConnectPreLoads.add(null);
      table.setCacheSelectEntity(true);
      if (!this.tables.add(table))
        throw new IllegalArgumentException("Table \"" + table.getName() + "\" is specified multiple times");
    }

    return this;
  }

  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final data.Table ... tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final data.Table table = tables[i];
      onConnectPreLoads.add(onConnectPreLoad);
      table.setCacheSelectEntity(true);
      if (!this.tables.add(table))
        throw new IllegalArgumentException("Table \"" + table.getName() + "\" is specified multiple times");
    }

    return this;
  }

  public CacheConfig with(final boolean cacheSelectEntity, final data.Table ... tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final data.Table table = tables[i];
      onConnectPreLoads.add(OnConnectPreLoad.ALL);
      table.setCacheSelectEntity(cacheSelectEntity);
      if (!this.tables.add(table))
        throw new IllegalArgumentException("Table \"" + table.getName() + "\" is specified multiple times");
    }

    return this;
  }

  public CacheConfig with(final OnConnectPreLoad onConnectPreLoad, final boolean cacheSelectEntity, final data.Table ... tables) {
    for (int i = 0, i$ = tables.length; i < i$; ++i) { // [A]
      final data.Table table = tables[i];
      onConnectPreLoads.add(onConnectPreLoad);
      table.setCacheSelectEntity(cacheSelectEntity);
      if (!this.tables.add(table))
        throw new IllegalArgumentException("Table \"" + table.getName() + "\" is specified multiple times");
    }

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