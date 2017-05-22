/* Copyright (c) 2016 lib4j
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

package org.safris.rdb.jsql;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class RowIterator<T extends Subject<?>> implements AutoCloseable {
  protected final List<T[]> rows = new ArrayList<T[]>();
  protected int rowIndex = -1;

  private T[] entities;
  private int entityIndex = -1;

  public boolean previousRow() {
    if (rowIndex <= 0)
      return false;

    --rowIndex;
    resetEntities();
    return true;
  }

  public abstract boolean nextRow() throws SQLException;

  protected void resetEntities() {
    entities = rows.get(rowIndex);
    entityIndex = -1;
  }

  public T previousEntity() {
    return --entityIndex > -1 ? entities[entityIndex] : null;
  }

  public T nextEntity() {
    return ++entityIndex < entities.length ? entities[entityIndex] : null;
  }

  @Override
  public abstract void close() throws SQLException;
}