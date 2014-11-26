package org.safris.xdb.xde.cql;

import java.sql.SQLException;
import java.util.List;

import org.safris.xdb.xde.Entities;
import org.safris.xdb.xde.Entity;

public abstract class SELECT extends CQL {
  protected final String clause;

  protected SELECT(final String clause) {
    this.clause = clause;
  }

  public List<Entity[]> execute() throws SQLException {
    return Entities.executeQuery(toString());
  }

  public String toString() {
    return clause;
  }
}