package org.safris.xdb.xde;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.safris.xdb.xde.csql.Entity;

public abstract class RowIterator<T extends Entity> {
  protected final List<T[]> rows = new ArrayList<T[]>();
  protected int rowIndex = -1;

  private T[] entities;
  private int entityIndex = -1;

  public boolean previousRow() throws SQLException {
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

  public abstract void close() throws SQLException;
}