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
import java.util.ArrayList;

import org.libj.sql.exception.SQLExceptions;

public abstract class RowIterator<D extends data.Entity<?>> implements AutoCloseable {
  public enum Type {
    FORWARD_ONLY(ResultSet.TYPE_FORWARD_ONLY),
    SCROLL_INSENSITIVE(ResultSet.TYPE_SCROLL_INSENSITIVE),
    SCROLL_SENSITIVE(ResultSet.TYPE_SCROLL_SENSITIVE);

    final int index;

    private Type(final int index) {
      this.index = index;
    }
  }

  public enum Concurrency {
    READ_ONLY(ResultSet.CONCUR_READ_ONLY),
    UPDATABLE(ResultSet.CONCUR_UPDATABLE);

    final int index;

    private Concurrency(final int index) {
      this.index = index;
    }
  }

  public enum Holdability {
    HOLD_CURSORS_OVER_COMMIT(ResultSet.HOLD_CURSORS_OVER_COMMIT),
    CLOSE_CURSORS_AT_COMMIT(ResultSet.CLOSE_CURSORS_AT_COMMIT);

    final int index;

    private Holdability(final int index) {
      this.index = index;
    }

    public static Holdability fromInt(final int holdability) {
      if (holdability == HOLD_CURSORS_OVER_COMMIT.index)
        return HOLD_CURSORS_OVER_COMMIT;

      if (holdability == CLOSE_CURSORS_AT_COMMIT.index)
        return CLOSE_CURSORS_AT_COMMIT;

      throw new IllegalArgumentException("Illegal holdability: " + holdability);
    }
  }

  final ResultSet resultSet;
  private final Type type;
  private final Concurrency concurrency;

  final ArrayList<D[]> rows = new ArrayList<>();

  int rowIndex = -1;
  boolean endReached;
  SQLException suppressed;

  private D[] entities;
  private int entityIndex = -1;

  public RowIterator(final ResultSet resultSet, final QueryConfig config) {
    this.resultSet = resultSet;
    if (config != null) {
      this.type = config.getType();
      this.concurrency = config.getConcurrency();
    }
    else {
      this.type = Type.FORWARD_ONLY;
      this.concurrency = Concurrency.READ_ONLY;
    }
  }

  public RowIterator(final ResultSet resultSet) {
    this.resultSet = resultSet;
    this.type = Type.FORWARD_ONLY;
    this.concurrency = Concurrency.READ_ONLY;
  }

  public Type getType() {
    return this.type;
  }

  public Concurrency getConcurrency() {
    return this.concurrency;
  }

  public final Holdability getHoldability() throws SQLException {
    return Holdability.fromInt(resultSet.getHoldability());
  }

  /**
   * Moves the cursor to the previous row in this {@link RowIterator} object.
   * <p>
   * When a call to this method returns {@code false}, the cursor is positioned
   * before the first row. Any invocation of a {@link RowIterator} method which
   * requires a current row will result in a {@link SQLException} to be thrown.
   * <p>
   * If an input stream is open for the current row, a call to this method will
   * implicitly close it. A {@link RowIterator} object's warning change is
   * cleared when a new row is read.
   * <p>
   *
   * @return {@code true} if the cursor is now positioned on a valid row;
   *         {@code false} if the cursor is positioned before the first row.
   * @throws SQLException If a database access error occurs; this method is
   *           called on a closed result set or {@linkplain #getType() the
   *           result set type} is {@link Type#FORWARD_ONLY}.
   * @throws SQLFeatureNotSupportedException If the JDBC driver does not support
   *           this method.
   */
  public boolean previousRow() throws SQLException {
    // FIXME: As per the spec of this method's javadoc, the
    // FIXME: following line should move the cursor back 1.
    if (rowIndex <= 0)
      return false;

    --rowIndex;
    resetEntities();
    return true;
  }

  /**
   * Moves the cursor forward one row from its current position. A
   * {@link RowIterator}'s cursor is initially positioned before the first row;
   * the first call to {@link #nextRow()} method next makes the first row the
   * current row; the second call makes the second row the current row, and so
   * on.
   * <p>
   * When a call to this method returns {@code false}, the cursor is
   * positioned after the last row. Any invocation of a {@link RowIterator}'s
   * method which requires a current row will result in a {@link SQLException}
   * to be thrown. If {@linkplain #getType() the result set type} is
   * {@link Type#FORWARD_ONLY}, it is vendor specified whether their JDBC driver
   * implementation will return {@code false} or throw an {@link SQLException}
   * on a subsequent call to next.
   *
   * @return {@code true} if the new current row is valid; {@code false} if
   *         there are no more rows.
   * @throws SQLException If a database access error occurs or this method is
   *           called on a closed result set.
   */
  public boolean nextRow() throws SQLException {
    if (++rowIndex < rows.size()) {
      resetEntities();
      return true;
    }

    // FIXME: As per the spec of this method's javadoc,
    // FIXME: the following line should not be there.
    --rowIndex;
    return false;
  }

  /**
   * Updates the underlying database with the new contents of the current row of
   * this {@link RowIterator} object. This method cannot be called when the
   * cursor is on the insert row.
   *
   * @throws SQLException If a database access error occurs; the
   *           {@linkplain #getConcurrency() result set concurrency} is
   *           {@link Concurrency#READ_ONLY}; this method is called on a closed
   *           result set or if this method is called when the cursor is on the
   *           insert row.
   * @throws SQLFeatureNotSupportedException If the JDBC driver does not support
   *           this method.
   */
  public final void updateRow() throws SQLException {
    try {
      resultSet.updateRow();
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
    }
  }

  void resetEntities() {
    entities = rows.get(rowIndex);
    entityIndex = -1;
  }

  public D previousEntity() {
    return --entityIndex > -1 ? entities[entityIndex] : null;
  }

  public D nextEntity() {
    return ++entityIndex < entities.length ? entities[entityIndex] : null;
  }

  @Override
  public abstract void close() throws SQLException;
}