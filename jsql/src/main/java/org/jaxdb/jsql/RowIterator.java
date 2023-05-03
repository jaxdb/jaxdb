/* Copyright (c) 2016 JAX-DB
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.libj.sql.exception.SQLExceptions;

public abstract class RowIterator<D extends type.Entity> implements AutoCloseable, Iterable<D> {
  public enum Type {
    FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    private static final Type[] values = values();
    final int index;

    private Type(final int index) {
      this.index = index;
    }

    public static Type fromInt(final int concurrency) {
      return values[concurrency - ResultSet.TYPE_FORWARD_ONLY];
    }
  }

  public enum Concurrency {
    READ_ONLY(ResultSet.CONCUR_READ_ONLY),
    UPDATABLE(ResultSet.CONCUR_UPDATABLE);

    private static final Concurrency[] values = values();
    final int index;

    private Concurrency(final int index) {
      this.index = index;
    }

    public static Concurrency fromInt(final int concurrency) {
      return values[concurrency - ResultSet.CONCUR_READ_ONLY];
    }
  }

  public enum Holdability {
    HOLD_CURSORS_OVER_COMMIT(ResultSet.HOLD_CURSORS_OVER_COMMIT),
    CLOSE_CURSORS_AT_COMMIT(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    private static final Holdability[] values = values();
    final int index;

    private Holdability(final int index) {
      this.index = index;
    }

    public static Holdability fromInt(final int holdability) {
      return values[holdability - ResultSet.HOLD_CURSORS_OVER_COMMIT];
    }
  }

  public enum Cacheability {
    CACHEABLE_ENTITY,
    CACHEABLE_ENTITY_LOOKUP;
  }

  final ResultSet resultSet;
  private final Type type;
  private final Concurrency concurrency;

  boolean endReached;
  SQLException suppressed;

  private D[] row;
  private int entityIndex = -1;

  public RowIterator(final ResultSet resultSet, final QueryConfig contextQueryConfig, final QueryConfig defaultQueryConfig) {
    this.resultSet = resultSet;
    this.type = QueryConfig.getType(contextQueryConfig, defaultQueryConfig);
    this.concurrency = QueryConfig.getConcurrency(contextQueryConfig, defaultQueryConfig);
  }

  public RowIterator(final ResultSet resultSet) {
    this.resultSet = resultSet;
    this.type = Type.FORWARD_ONLY;
    this.concurrency = Concurrency.READ_ONLY;
  }

  protected D[] getRow() {
    return row;
  }

  protected void setRow(final D[] row) {
    this.row = row;
    entityIndex = -1;
  }

  public Type getType() {
    return type;
  }

  public Concurrency getConcurrency() {
    return concurrency;
  }

  public final Holdability getHoldability() throws SQLException {
    return Holdability.fromInt(resultSet.getHoldability());
  }

  /**
   * Moves the cursor forward one row from its current position. A {@link RowIterator}'s cursor is initially positioned before the
   * first row; the first call to {@link #nextRow()} method next makes the first row the current row; the second call makes the
   * second row the current row, and so on.
   * <p>
   * When a call to this method returns {@code false}, the cursor is positioned after the last row. Any invocation of a
   * {@link RowIterator}'s method which requires a current row will result in a {@link SQLException} to be thrown. If
   * {@linkplain #getType() the result set type} is {@link Type#FORWARD_ONLY}, it is vendor specified whether their JDBC driver
   * implementation will return {@code false} or throw an {@link SQLException} on a subsequent call to next.
   *
   * @return {@code true} if the new current row is valid; {@code false} if there are no more rows.
   * @throws SQLException If a database access error occurs or this method is called on a closed result set.
   */
  public abstract boolean nextRow() throws SQLException;

  /**
   * Moves the cursor to the previous row in this {@link RowIterator} object.
   * <p>
   * When a call to this method returns {@code false}, the cursor is positioned before the first row. Any invocation of a
   * {@link RowIterator} method which requires a current row will result in a {@link SQLException} to be thrown.
   * <p>
   * If an input stream is open for the current row, a call to this method will implicitly close it. A {@link RowIterator} object's
   * warning change is cleared when a new row is read.
   * <p>
   *
   * @return {@code true} if the cursor is now positioned on a valid row; {@code false} if the cursor is positioned before the first
   *         row.
   * @throws SQLException If a database access error occurs; this method is called on a closed result set or {@linkplain #getType()
   *           the result set type} is {@link Type#FORWARD_ONLY}.
   * @throws SQLFeatureNotSupportedException If the JDBC driver does not support this method.
   */
  public boolean previousRow() throws SQLException {
    throw new UnsupportedOperationException();
  }

  /**
   * Updates the underlying database with the new contents of the current row of this {@link RowIterator} object. This method cannot
   * be called when the cursor is on the insert row.
   *
   * @throws SQLException If a database access error occurs; the {@linkplain #getConcurrency() result set concurrency} is
   *           {@link Concurrency#READ_ONLY}; this method is called on a closed result set or if this method is called when the
   *           cursor is on the insert row.
   * @throws SQLFeatureNotSupportedException If the JDBC driver does not support this method.
   */
  public final void updateRow() throws SQLException {
    try {
      resultSet.updateRow();
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  /**
   * Returns the next {@link data.Entity}.
   *
   * @return The next {@link data.Entity}.
   * @throws SQLException If a database access error occurs or this method is called on a closed result set.
   */
  public final D nextEntity() throws SQLException {
    return nextEntity(true);
  }

  /**
   * Returns the next {@link data.Entity}.
   *
   * @param addRange Whether to have relevant caches add the range for this entity.
   * @return The next {@link data.Entity}.
   * @throws SQLException If a database access error occurs or this method is called on a closed result set.
   */
  D nextEntity(final boolean addRange) throws SQLException {
    if (row == null)
      throw new SQLException("RowIterator.nextRow() was not called");

    return ++entityIndex < row.length ? row[entityIndex] : null;
  }

  /**
   * Returns the previous {@link data.Entity}.
   *
   * @return The previous {@link data.Entity}.
   * @throws SQLException If a database access error occurs or this method is called on a closed result set.
   */
  public D previousEntity() throws SQLException {
    if (row == null)
      throw new SQLException("RowIterator.next() was not called");

    return --entityIndex < 0 ? null : row[entityIndex];
  }

  @Override
  public Iterator<D> iterator() {
    return new Iterator<D>() {
      private Boolean hasNext;
      private D next;

      @Override
      public boolean hasNext() {
        if (hasNext != null)
          return hasNext;

        try {
          next = nextEntity();
          if (hasNext = next != null)
            return true;

          if (!(hasNext = nextRow()))
            return false;

          next = nextEntity();
          return hasNext = next != null;
        }
        catch (final SQLException e) {
          throw new RuntimeException(e);
        }
      }

      @Override
      public D next() {
        if (!hasNext())
          throw new NoSuchElementException();

        final D next = this.next;
        this.next = null;
        this.hasNext = null;
        return next;
      }
    };
  }

  @Override
  public abstract void close() throws SQLException;
}