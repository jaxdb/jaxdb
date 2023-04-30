/* Copyright (c) 2020 JAX-BD
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
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.RowIterator.Holdability;
import org.jaxdb.jsql.RowIterator.Type;

public class QueryConfig implements Serializable {
  public static class Builder implements Cloneable, Serializable {
    private String cursorName;
    private Boolean escapeProcessing;
    private FetchDirection fetchDirection;
    private long largeMaxRows = -1;
    private int maxFieldSize = -1;
    private int maxRows = -1;
    private Boolean poolable;
    private int queryTimeout = -1;
    private int fetchSize = -1;

    private RowIterator.Type type = Type.FORWARD_ONLY;
    private RowIterator.Concurrency concurrency = Concurrency.READ_ONLY;
    private RowIterator.Holdability holdability;
    private boolean cacheableExclusivity = false;
    private boolean cacheablePrimaryIndexEfficiencyExclusivity = false;

    public Builder withCursorName(final String name) {
      this.cursorName = assertNotNull(name);
      return this;
    }

    public Builder withEscapeProcessing(final boolean enable) {
      this.escapeProcessing = enable;
      return this;
    }

    public Builder withFetchDirection(final FetchDirection direction) {
      this.fetchDirection = assertNotNull(direction);
      return this;
    }

    public Builder withFetchSize(final int fetchSize) {
      if (fetchSize <= 0)
        throw new IllegalArgumentException("fetchSize (" + fetchSize + ") must be greater than 0");

      this.fetchSize = fetchSize;
      return this;
    }

    public Builder withLargeMaxRows(final long max) {
      if (max <= 0)
        throw new IllegalArgumentException("largeMaxRows (" + max + ") must be greater than 0");

      this.largeMaxRows = max;
      return this;
    }

    public Builder withMaxFieldSize(final int max) {
      if (max <= 0)
        throw new IllegalArgumentException("maxFieldSize (" + max + ") must be greater than 0");

      this.maxFieldSize = max;
      return this;
    }

    public Builder withMaxRows(final int max) {
      if (max <= 0)
        throw new IllegalArgumentException("maxRows (" + max + ") must be greater than 0");

      this.maxRows = max;
      return this;
    }

    public Builder withPoolable(final boolean poolable) {
      this.poolable = poolable;
      return this;
    }

    public Builder withQueryTimeout(final int seconds) {
      if (seconds <= 0)
        throw new IllegalArgumentException("queryTimeout (" + seconds + ") must be greater than 0");

      this.queryTimeout = seconds;
      return this;
    }

    public Builder withType(final RowIterator.Type type) {
      this.type = assertNotNull(type);
      return this;
    }

    public Builder withConcurrency(final RowIterator.Concurrency concurrency) {
      this.concurrency = assertNotNull(concurrency);
      return this;
    }

    public Builder withHoldability(final RowIterator.Holdability holdability) {
      this.holdability = assertNotNull(holdability);
      return this;
    }

    public Builder withCacheableExclusivity(final boolean cacheableExclusivity) {
      this.cacheableExclusivity = cacheableExclusivity;
      return this;
    }

    public Builder withCacheablePrimaryIndexEfficiencyExclusivity(final boolean cacheablePrimaryIndexEfficiencyExclusivity) {
      this.cacheablePrimaryIndexEfficiencyExclusivity = cacheablePrimaryIndexEfficiencyExclusivity;
      return this;
    }

    public QueryConfig build() {
      return new QueryConfig(cursorName, escapeProcessing, fetchDirection, fetchSize, largeMaxRows, maxFieldSize, maxRows, poolable, queryTimeout, type, concurrency, holdability, cacheableExclusivity, cacheablePrimaryIndexEfficiencyExclusivity);
    }

    @Override
    protected Builder clone() {
      try {
        return (Builder)super.clone();
      }
      catch (final CloneNotSupportedException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public enum FetchDirection {
    FORWARD(ResultSet.FETCH_FORWARD),
    REVERSE(ResultSet.FETCH_REVERSE),
    UNKNOWN(ResultSet.FETCH_UNKNOWN);

    private final int value;

    private FetchDirection(final int value) {
      this.value = value;
    }
  }

  private final String cursorName;
  private final Boolean escapeProcessing;
  private final FetchDirection fetchDirection;
  private final int fetchSize;
  private final long largeMaxRows;
  private final int maxFieldSize;
  private final int maxRows;
  private final Boolean poolable;
  private final int queryTimeout;

  private final RowIterator.Type type;
  private final RowIterator.Concurrency concurrency;
  private final RowIterator.Holdability holdability;

  private final boolean cacheableExclusivity;
  private final boolean cacheablePrimaryIndexEfficiencyExclusivity;

  private QueryConfig(final String cursorName, final Boolean escapeProcessing, final FetchDirection fetchDirection, final int fetchSize, final long largeMaxRows, final int maxFieldSize, final int maxRows, final Boolean poolable, final int queryTimeout, final RowIterator.Type type, final RowIterator.Concurrency concurrency, final RowIterator.Holdability holdability, final boolean cacheableExclusivity, final boolean cacheablePrimaryIndexEfficiencyExclusivity) {
    this.cursorName = cursorName;
    this.escapeProcessing = escapeProcessing;
    this.fetchDirection = fetchDirection;
    this.fetchSize = fetchSize;
    this.largeMaxRows = largeMaxRows;
    this.maxFieldSize = maxFieldSize;
    this.maxRows = maxRows;
    this.poolable = poolable;
    this.queryTimeout = queryTimeout;
    this.type = type;
    this.concurrency = concurrency;
    this.holdability = holdability;
    this.cacheableExclusivity = cacheableExclusivity;
    this.cacheablePrimaryIndexEfficiencyExclusivity = cacheablePrimaryIndexEfficiencyExclusivity;
  }

  public String getCursorName() {
    return cursorName;
  }

  public Boolean getEscapeProcessing() {
    return escapeProcessing;
  }

  public FetchDirection getFetchDirection() {
    return fetchDirection;
  }

  public int getFetchSize() {
    return fetchSize;
  }

  public long getLargeMaxRows() {
    return largeMaxRows;
  }

  public int getMaxFieldSize() {
    return maxFieldSize;
  }

  public int getMaxRows() {
    return maxRows;
  }

  public Boolean getPoolable() {
    return poolable;
  }

  public int getQueryTimeout() {
    return queryTimeout;
  }

  public Type getType() {
    return type;
  }

  public Concurrency getConcurrency() {
    return concurrency;
  }

  public Holdability getHoldability() {
    return holdability;
  }

  public boolean getCacheableExclusivity() {
    return cacheableExclusivity;
  }

  public boolean getCacheablePrimaryIndexEfficiencyExclusivity() {
    return cacheablePrimaryIndexEfficiencyExclusivity;
  }

  static boolean isCacheableExclusivity(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.cacheableExclusivity || defaultQueryConfig != null && defaultQueryConfig.cacheableExclusivity;
  }

  static boolean isCacheablePrimaryIndexEfficiencyExclusivity(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.cacheablePrimaryIndexEfficiencyExclusivity || defaultQueryConfig != null && defaultQueryConfig.cacheablePrimaryIndexEfficiencyExclusivity;
  }

  private static ResultSet executeQuery(final QueryConfig queryConfig, final Compilation compilation, final Connection connection) throws IOException, SQLException {
    final String sql = compilation.toString();
    final Statement statement;
    final boolean isPrepared = compilation.isPrepared();
    if (queryConfig.holdability != null)
      statement = isPrepared ? connection.prepareStatement(sql, queryConfig.type.index, queryConfig.concurrency.index, queryConfig.holdability.index) : connection.createStatement(queryConfig.type.index, queryConfig.concurrency.index, queryConfig.holdability.index);
    else
      statement = isPrepared ? connection.prepareStatement(sql, queryConfig.type.index, queryConfig.concurrency.index) : connection.createStatement(queryConfig.type.index, queryConfig.concurrency.index);

    if (queryConfig.fetchSize != -1)
      statement.setFetchSize(queryConfig.fetchSize);

    if (queryConfig.cursorName != null)
      statement.setCursorName(queryConfig.cursorName);

    if (queryConfig.escapeProcessing != null)
      statement.setEscapeProcessing(queryConfig.escapeProcessing);

    if (queryConfig.fetchDirection != null)
      statement.setFetchDirection(queryConfig.fetchDirection.value);

    if (queryConfig.largeMaxRows != -1)
      statement.setLargeMaxRows(queryConfig.largeMaxRows);

    if (queryConfig.maxFieldSize != -1)
      statement.setMaxFieldSize(queryConfig.maxFieldSize);

    if (queryConfig.maxRows != -1)
      statement.setMaxRows(queryConfig.maxRows);

    if (queryConfig.poolable != null)
      statement.setPoolable(queryConfig.poolable);

    if (queryConfig.queryTimeout != -1)
      statement.setQueryTimeout(queryConfig.queryTimeout);

    return isPrepared ? executeQueryPrepared((PreparedStatement)statement, compilation) : statement.executeQuery(sql);
  }

  private static ResultSet executeQueryPrepared(final PreparedStatement statement, final Compilation compilation) throws IOException, SQLException {
    final ArrayList<data.Column<?>> parameters = compilation.getParameters();
    if (parameters != null) {
      final Compiler compiler = compilation.compiler;
      for (int i = 0, i$ = parameters.size(); i < i$;)
        parameters.get(i++).write(compiler, statement, false, i);
    }

    return statement.executeQuery();
  }

  static ResultSet executeQuery(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig, final Compilation compilation, final Connection connection) throws IOException, SQLException {
    final String sql = compilation.toString();
    if (contextQueryConfig == null) {
      if (defaultQueryConfig == null)
        return compilation.isPrepared() ? executeQueryPrepared(connection.prepareStatement(sql), compilation) : connection.createStatement().executeQuery(sql);

      return executeQuery(defaultQueryConfig, compilation, connection);
    }

    if (defaultQueryConfig == null)
      return executeQuery(contextQueryConfig, compilation, connection);

    final Type type = contextQueryConfig.type != Type.FORWARD_ONLY ? contextQueryConfig.type : defaultQueryConfig.type;
    final Concurrency concurrency = contextQueryConfig.concurrency != Concurrency.READ_ONLY ? contextQueryConfig.concurrency : defaultQueryConfig.concurrency;
    final Holdability holdability = contextQueryConfig.holdability != null ? contextQueryConfig.holdability : defaultQueryConfig.holdability;
    final Statement statement;
    final boolean isPrepared = compilation.isPrepared();
    if (holdability != null)
      statement = isPrepared ? connection.prepareStatement(sql, type.index, concurrency.index, holdability.index) : connection.createStatement(type.index, concurrency.index, holdability.index);
    else
      statement = isPrepared ? connection.prepareStatement(sql, type.index, concurrency.index) : connection.createStatement(type.index, concurrency.index);

    if (contextQueryConfig.fetchSize != -1)
      statement.setFetchSize(contextQueryConfig.fetchSize);
    else if (defaultQueryConfig.fetchSize != -1)
      statement.setFetchSize(defaultQueryConfig.fetchSize);

    if (contextQueryConfig.cursorName != null)
      statement.setCursorName(contextQueryConfig.cursorName);
    else if (defaultQueryConfig.cursorName != null)
      statement.setCursorName(defaultQueryConfig.cursorName);

    if (contextQueryConfig.escapeProcessing != null)
      statement.setEscapeProcessing(contextQueryConfig.escapeProcessing);
    else if (defaultQueryConfig.escapeProcessing != null)
      statement.setEscapeProcessing(defaultQueryConfig.escapeProcessing);

    if (contextQueryConfig.fetchDirection != null)
      statement.setFetchDirection(contextQueryConfig.fetchDirection.value);
    else if (defaultQueryConfig.fetchDirection != null)
      statement.setFetchDirection(defaultQueryConfig.fetchDirection.value);

    if (contextQueryConfig.largeMaxRows != -1)
      statement.setLargeMaxRows(contextQueryConfig.largeMaxRows);
    else if (defaultQueryConfig.largeMaxRows != -1)
      statement.setLargeMaxRows(defaultQueryConfig.largeMaxRows);

    if (contextQueryConfig.maxFieldSize != -1)
      statement.setMaxFieldSize(contextQueryConfig.maxFieldSize);
    else if (defaultQueryConfig.maxFieldSize != -1)
      statement.setMaxFieldSize(defaultQueryConfig.maxFieldSize);

    if (contextQueryConfig.maxRows != -1)
      statement.setMaxRows(contextQueryConfig.maxRows);
    else if (defaultQueryConfig.maxRows != -1)
      statement.setMaxRows(defaultQueryConfig.maxRows);

    if (contextQueryConfig.poolable != null)
      statement.setPoolable(contextQueryConfig.poolable);
    else if (defaultQueryConfig.poolable != null)
      statement.setPoolable(defaultQueryConfig.poolable);

    if (contextQueryConfig.queryTimeout != -1)
      statement.setQueryTimeout(contextQueryConfig.queryTimeout);
    else if (defaultQueryConfig.queryTimeout != -1)
      statement.setQueryTimeout(defaultQueryConfig.queryTimeout);

    return isPrepared ? executeQueryPrepared((PreparedStatement)statement, compilation) : statement.executeQuery(sql);
  }
}