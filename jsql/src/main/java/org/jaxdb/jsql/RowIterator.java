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

  public boolean previousRow() {
    if (rowIndex <= 0)
      return false;

    --rowIndex;
    resetEntities();
    return true;
  }

  public boolean nextRow() throws SQLException {
    if (++rowIndex < rows.size()) {
      resetEntities();
      return true;
    }

    --rowIndex;
    return false;
  }

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