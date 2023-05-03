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
import java.util.Objects;

import org.jaxdb.jsql.RowIterator.Cacheability;
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

    private Type type = Type.FORWARD_ONLY;
    private Concurrency concurrency = Concurrency.READ_ONLY;
    private Holdability holdability;
    private Cacheability cacheability;
    private boolean cacheableRowIteratorFullConsume = false;

    Builder(final QueryConfig config) {
      this.cursorName = config.cursorName;
      this.escapeProcessing = config.escapeProcessing;
      this.fetchDirection = config.fetchDirection;
      this.largeMaxRows = config.largeMaxRows;
      this.maxFieldSize = config.maxFieldSize;
      this.maxRows = config.maxRows;
      this.poolable = config.poolable;
      this.queryTimeout = config.queryTimeout;
      this.fetchSize = config.fetchSize;
      this.type = config.type;
      this.concurrency = config.concurrency;
      this.holdability = config.holdability;
      this.cacheability = config.cacheability;
      this.cacheableRowIteratorFullConsume = config.cacheableRowIteratorFullConsume;
    }

    public Builder() {
    }

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

    public Builder withType(final Type type) {
      this.type = assertNotNull(type);
      return this;
    }

    public Builder withConcurrency(final Concurrency concurrency) {
      this.concurrency = assertNotNull(concurrency);
      return this;
    }

    public Builder withHoldability(final Holdability holdability) {
      this.holdability = holdability;
      return this;
    }

    public Builder withCacheability(final Cacheability cacheability) {
      this.cacheability = cacheability;
      return this;
    }

    public Builder withCacheableRowIteratorFullConsume(final boolean cacheableRowIteratorFullConsume) {
      this.cacheableRowIteratorFullConsume = cacheableRowIteratorFullConsume;
      return this;
    }

    public QueryConfig build() {
      return new QueryConfig(cursorName, escapeProcessing, fetchDirection, fetchSize, largeMaxRows, maxFieldSize, maxRows, poolable, queryTimeout, type, concurrency, holdability, cacheability, cacheableRowIteratorFullConsume);
    }

    @Override
    public int hashCode() {
      int hashCode = 0;
      hashCode = hashCode * 31 + Objects.hashCode(cursorName);
      hashCode = hashCode * 31 + Objects.hashCode(escapeProcessing);
      hashCode = hashCode * 31 + Objects.hashCode(fetchDirection);
      hashCode = hashCode * 31 + Long.hashCode(largeMaxRows);
      hashCode = hashCode * 31 + Integer.hashCode(maxFieldSize);
      hashCode = hashCode * 31 + Integer.hashCode(maxRows);
      hashCode = hashCode * 31 + Objects.hashCode(poolable);
      hashCode = hashCode * 31 + Integer.hashCode(queryTimeout);
      hashCode = hashCode * 31 + Integer.hashCode(fetchSize);
      hashCode = hashCode * 31 + Objects.hashCode(type);
      hashCode = hashCode * 31 + Objects.hashCode(concurrency);
      hashCode = hashCode * 31 + Objects.hashCode(holdability);
      hashCode = hashCode * 31 + Objects.hashCode(cacheability);
      hashCode = hashCode * 31 + Boolean.hashCode(cacheableRowIteratorFullConsume);
      return hashCode;
    }

    @Override
    public boolean equals(final Object obj) {
      if (obj == this)
        return true;

      if (!(obj instanceof Builder))
        return false;

      final Builder that = (Builder)obj;

      if (!Objects.equals(cursorName, that.cursorName))
        return false;

      if (!Objects.equals(escapeProcessing, that.escapeProcessing))
        return false;

      if (!Objects.equals(fetchDirection, that.fetchDirection))
        return false;

      if (largeMaxRows != that.largeMaxRows)
        return false;

      if (maxFieldSize != that.maxFieldSize)
        return false;

      if (maxRows != that.maxRows)
        return false;

      if (!Objects.equals(poolable, that.poolable))
        return false;

      if (queryTimeout != that.queryTimeout)
        return false;

      if (fetchSize != that.fetchSize)
        return false;

      if (!type.equals(that.type))
        return false;

      if (!concurrency.equals(that.concurrency))
        return false;

      if (!Objects.equals(holdability, that.holdability))
        return false;

      if (!Objects.equals(cacheability, that.cacheability))
        return false;

      if (cacheableRowIteratorFullConsume != that.cacheableRowIteratorFullConsume)
        return false;

      return true;
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

  private final Type type;
  private final Concurrency concurrency;
  private final Holdability holdability;
  private final Cacheability cacheability;

  private final boolean cacheableRowIteratorFullConsume;

  private QueryConfig(final String cursorName, final Boolean escapeProcessing, final FetchDirection fetchDirection, final int fetchSize, final long largeMaxRows, final int maxFieldSize, final int maxRows, final Boolean poolable, final int queryTimeout, final Type type, final Concurrency concurrency, final Holdability holdability, final Cacheability cacheability, final boolean cacheableRowIteratorFullConsume) {
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
    this.cacheability = cacheability;
    this.cacheableRowIteratorFullConsume = cacheableRowIteratorFullConsume;
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

  public Cacheability getCacheability() {
    return cacheability;
  }

  public boolean getCacheableRowIteratorFullConsume() {
    return cacheableRowIteratorFullConsume;
  }

  public Builder toBuilder() {
    return new Builder(this);
  }

  @Override
  public int hashCode() {
    int hashCode = 0;
    hashCode = hashCode * 31 + Objects.hashCode(cursorName);
    hashCode = hashCode * 31 + Objects.hashCode(escapeProcessing);
    hashCode = hashCode * 31 + Objects.hashCode(fetchDirection);
    hashCode = hashCode * 31 + Long.hashCode(largeMaxRows);
    hashCode = hashCode * 31 + Integer.hashCode(maxFieldSize);
    hashCode = hashCode * 31 + Integer.hashCode(maxRows);
    hashCode = hashCode * 31 + Objects.hashCode(poolable);
    hashCode = hashCode * 31 + Integer.hashCode(queryTimeout);
    hashCode = hashCode * 31 + Integer.hashCode(fetchSize);
    hashCode = hashCode * 31 + Objects.hashCode(type);
    hashCode = hashCode * 31 + Objects.hashCode(concurrency);
    hashCode = hashCode * 31 + Objects.hashCode(holdability);
    hashCode = hashCode * 31 + Objects.hashCode(cacheability);
    hashCode = hashCode * 31 + Boolean.hashCode(cacheableRowIteratorFullConsume);
    return hashCode;
  }

  @Override
  public boolean equals(final Object obj) {
    if (obj == this)
      return true;

    if (!(obj instanceof QueryConfig))
      return false;

    final QueryConfig that = (QueryConfig)obj;

    if (!Objects.equals(cursorName, that.cursorName))
      return false;

    if (!Objects.equals(escapeProcessing, that.escapeProcessing))
      return false;

    if (!Objects.equals(fetchDirection, that.fetchDirection))
      return false;

    if (largeMaxRows != that.largeMaxRows)
      return false;

    if (maxFieldSize != that.maxFieldSize)
      return false;

    if (maxRows != that.maxRows)
      return false;

    if (!Objects.equals(poolable, that.poolable))
      return false;

    if (queryTimeout != that.queryTimeout)
      return false;

    if (fetchSize != that.fetchSize)
      return false;

    if (!type.equals(that.type))
      return false;

    if (!concurrency.equals(that.concurrency))
      return false;

    if (!Objects.equals(holdability, that.holdability))
      return false;

    if (!Objects.equals(cacheability, that.cacheability))
      return false;

    if (cacheableRowIteratorFullConsume != that.cacheableRowIteratorFullConsume)
      return false;

    return true;
  }

  static Type getType(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.type != Type.FORWARD_ONLY ? contextQueryConfig.type : defaultQueryConfig != null ? defaultQueryConfig.type : Type.FORWARD_ONLY;
  }

  static Concurrency getConcurrency(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.concurrency != Concurrency.READ_ONLY ? contextQueryConfig.concurrency : defaultQueryConfig != null ? defaultQueryConfig.concurrency : Concurrency.READ_ONLY;
  }

  static Cacheability getCacheability(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.cacheability != null ? contextQueryConfig.cacheability : defaultQueryConfig != null ? defaultQueryConfig.cacheability : null;
  }

  static boolean isCacheableRowIteratorFullConsume(final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    return contextQueryConfig != null && contextQueryConfig.cacheableRowIteratorFullConsume || defaultQueryConfig != null && defaultQueryConfig.cacheableRowIteratorFullConsume;
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