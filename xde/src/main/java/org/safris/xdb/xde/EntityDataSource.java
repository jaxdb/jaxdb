package org.safris.xdb.xde;

import java.sql.Connection;

public interface EntityDataSource {
  public Connection getConnection();
}