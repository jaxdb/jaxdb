/* Copyright (c) 2016 Seva Safris
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

package org.safris.xdb.entities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.safris.xdb.schema.DBVendor;

public class Serialization {
  private final List<String> sqls;
  private String heading;

  private final List<List<? extends Variable<?>>> batches;
  private List<Variable<?>> current;

  protected final Class<?> type;
  protected final DBVendor vendor;
  protected final Class<? extends Statement> statementType;
  private final StringBuilder sql = new StringBuilder();

  protected Serialization(final Class<?> type, final DBVendor vendor, final Class<? extends Statement> statementType) {
    this.type = type;
    this.vendor = vendor;
    this.statementType = statementType;
    if (statementType == PreparedStatement.class) {
      batches = new ArrayList<List<? extends Variable<?>>>();
      sqls = null;
      addBatch();
    }
    else {
      batches = null;
      sqls = new ArrayList<String>();
    }
  }

  public Class<?> getType() {
    return type;
  }

  protected void addParameter(final Variable<?> parameter) {
    if (parameter == null)
      throw new IllegalArgumentException("parameter cannot be null");

    current.add(parameter);
  }

  protected StringBuilder append(final CharSequence sql) {
    return this.sql.append(sql);
  }

  protected void setHeading() {
    heading = sql.toString();
    sql.setLength(0);
  }

  protected void addBatch() {
    if (statementType == PreparedStatement.class) {
      batches.add(current = new ArrayList<Variable<?>>());
    }
    else {
      sqls.add(sql.toString());
      sql.setLength(0);
    }
  }

  protected final ResultSet executeQuery(final Connection connection) throws SQLException {
    try (final Statement statement = createStatement(connection)) {
      if (statement instanceof PreparedStatement)
        return ((PreparedStatement)statement).executeQuery();

      return statement.executeQuery(sql.toString());
    }
  }

  protected int[] executeUpdate(final Connection connection) throws SQLException {
    try (final Statement statement = createStatement(connection)) {
      if (statement instanceof PreparedStatement) {
        final PreparedStatement preparedStatement = (PreparedStatement)statement;
        if (batches.size() == 1)
          return new int[] {set(preparedStatement, current).executeUpdate()};

        if (connection.getMetaData().supportsBatchUpdates())
          return set(preparedStatement).executeBatch();

        final int[] counts = new int[batches.size()];
        for (int i = 0; i < batches.size(); i++) {
          final List<? extends Variable<?>> parameters = batches.get(i);
          set(preparedStatement, parameters);
          counts[i] = preparedStatement.executeUpdate();
          preparedStatement.clearParameters();
        }

        return counts;
      }

      if (sqls.size() == 1)
        return new int[] {statement.executeUpdate(heading + sql.toString())};

      if (connection.getMetaData().supportsBatchUpdates()) {
        for (final String sql : sqls)
          statement.addBatch(heading + sql);

        return statement.executeBatch();
      }

      final int[] counts = new int[sqls.size()];
      for (int i = 0; i < sqls.size(); i++)
        counts[i] = statement.executeUpdate(heading + sqls.get(i));

      return counts;
    }
  }

  private Statement createStatement(final Connection connection) throws SQLException {
    if (statementType == PreparedStatement.class)
      return connection.prepareStatement(heading + sql.toString());

    return connection.createStatement();
  }

  private static PreparedStatement set(final PreparedStatement statement, final List<? extends Variable<?>> parameters) throws SQLException {
    for (int i = 0; i < parameters.size(); i++)
      parameters.get(i).get(statement, i + 1);

    return statement;
  }

  private PreparedStatement set(final PreparedStatement statement) throws SQLException {
    final Iterator<List<? extends Variable<?>>> iterator = batches.iterator();
    while (true) {
      set(statement, iterator.next());
      if (iterator.hasNext())
        statement.addBatch();
      else
        return statement;
    }
  }
}