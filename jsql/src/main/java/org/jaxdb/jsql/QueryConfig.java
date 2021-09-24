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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.jaxdb.jsql.RowIterator.Concurrency;
import org.jaxdb.jsql.RowIterator.Holdability;
import org.jaxdb.jsql.RowIterator.Type;

public class QueryConfig {
  public static class Builder {
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

    public QueryConfig build() {
      return new QueryConfig(cursorName, escapeProcessing, fetchDirection, fetchSize, largeMaxRows, maxFieldSize, maxRows, poolable, queryTimeout, type, concurrency, holdability);
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

  private String cursorName;
  private Boolean escapeProcessing;
  private FetchDirection fetchDirection;
  private int fetchSize = -1;
  private long largeMaxRows = -1;
  private int maxFieldSize = -1;
  private int maxRows = -1;
  private Boolean poolable;
  private int queryTimeout = -1;

  private RowIterator.Type type;
  private RowIterator.Concurrency concurrency;
  private RowIterator.Holdability holdability;

  private QueryConfig(final String cursorName, final Boolean escapeProcessing, final FetchDirection fetchDirection, final int fetchSize, final long largeMaxRows, final int maxFieldSize, final int maxRows, final Boolean poolable, final int queryTimeout, final RowIterator.Type type, final RowIterator.Concurrency concurrency, final RowIterator.Holdability holdability) {
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
  }

  public String getCursorName() {
    return this.cursorName;
  }

  public Boolean getEscapeProcessing() {
    return this.escapeProcessing;
  }

  public int getFetchDirection() {
    return this.fetchDirection == null ? -1 : this.fetchDirection.value;
  }

  public int getFetchSize() {
    return this.fetchSize;
  }

  public long getLargeMaxRows() {
    return this.largeMaxRows;
  }

  public int getMaxFieldSize() {
    return this.maxFieldSize;
  }

  public int getMaxRows() {
    return this.maxRows;
  }

  public Boolean getPoolable() {
    return this.poolable;
  }

  public int getQueryTimeout() {
    return this.queryTimeout;
  }

  public Type getType() {
    return this.type;
  }

  public Concurrency getConcurrency() {
    return this.concurrency;
  }

  public Holdability getHoldability() {
    return this.holdability;
  }

  public <T extends Statement>T apply(final T statement) throws SQLException {
    if (fetchSize != -1)
      statement.setFetchSize(fetchSize);

    if (cursorName != null)
      statement.setCursorName(cursorName);

    if (escapeProcessing != null)
      statement.setEscapeProcessing(escapeProcessing);

    if (fetchDirection != null)
      statement.setFetchDirection(fetchDirection.value);

    if (largeMaxRows != -1)
      statement.setLargeMaxRows(largeMaxRows);

    if (maxFieldSize != -1)
      statement.setMaxFieldSize(maxFieldSize);

    if (maxRows != -1)
      statement.setMaxRows(maxRows);

    if (poolable != null)
      statement.setPoolable(poolable);

    if (queryTimeout != -1)
      statement.setQueryTimeout(queryTimeout);

    return statement;
  }
}