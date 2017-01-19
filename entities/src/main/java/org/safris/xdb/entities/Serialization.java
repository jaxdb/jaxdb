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
import java.util.List;
import java.util.Stack;

import org.safris.xdb.schema.DBVendor;

final class Serialization {
  private final Stack<Serializable> callStack = new Stack<Serializable>();
  private final List<String> sqls = new ArrayList<String>();

  private final List<List<? extends Variable<?>>> batches;
  private List<Variable<?>> current;

  protected final Class<?> type;
  private final DBVendor vendor;
  protected final Class<? extends Statement> statementType;
  private final StringBuilder sql = new StringBuilder();

  protected Serialization(final Class<?> type, final DBVendor vendor, final Class<? extends Statement> statementType) {
    this.type = type;
    this.vendor = vendor;
    this.statementType = statementType;
    if (statementType == PreparedStatement.class) {
      batches = new ArrayList<List<? extends Variable<?>>>();
      batches.add(current = new ArrayList<Variable<?>>());
    }
    else {
      batches = null;
    }
  }

  protected Stack<Serializable> getCaller() {
    return callStack;
  }

  protected DBVendor getVendor() {
    return vendor;
  }

  protected Serializer getSerializer(final Serializable serializable) {
    return Serializer.getSerializer(vendor);
  }

  protected void addCaller(final Serializable serializable) {
    if (callStack.empty() || callStack.peek() != serializable)
      callStack.add(serializable);
  }

  public Class<?> getType() {
    return type;
  }

  protected void addParameter(final Variable<?> parameter) {
    if (parameter == null)
      throw new IllegalArgumentException("parameter cannot be null");

    current.add(parameter);
  }

  public StringBuilder append(final CharSequence sql) {
    return this.sql.append(sql);
  }

  protected void addBatch() {
    sqls.add(sql.toString());
    sql.setLength(0);
    if (statementType == PreparedStatement.class)
      batches.add(current = new ArrayList<Variable<?>>());
  }

  protected final ResultSet executeQuery(final Connection connection) throws SQLException {
    if (statementType == PreparedStatement.class) {
      final PreparedStatement statement = connection.prepareStatement(sql.toString());
      set(statement, current);
      return statement.executeQuery();
    }

    final Statement statement = connection.createStatement();
    return statement.executeQuery(sql.toString());
  }

  protected int[] executeUpdate(final Connection connection) throws SQLException {
    sqls.add(sql.toString());
    final int[] counts = new int[sqls.size()];
    boolean batching = false;
    String lastSQL = null;
    if (statementType == PreparedStatement.class) {
      PreparedStatement statement = null;
      String sql = null;
      List<? extends Variable<?>> parameters = null;
      for (int i = 0;; i++) {
        if (i < sqls.size()) {
          sql = sqls.get(i);
          parameters = batches.get(i);
          if (sql.equals(lastSQL)) {
            statement.addBatch();
            set(statement, parameters);
            batching = true;
            continue;
          }
        }

        if (batching) {
          final int[] batchCounts = statement.executeBatch();
          statement.close();
          System.arraycopy(batchCounts, 0, counts, i - batchCounts.length - 1, batchCounts.length);
          batching = false;
        }
        else if (statement != null) {
          counts[i - 1] = statement.executeUpdate();
          statement.close();
        }

        if (i == sqls.size())
          break;

        statement = connection.prepareStatement(sql);
        set(statement, parameters);
      }

      return counts;
    }

    try (final Statement statement = connection.createStatement()) {
      if (sqls.size() == 1)
        return new int[] {statement.executeUpdate(sql.toString())};

      if (connection.getMetaData().supportsBatchUpdates()) {
        for (final String sql : sqls)
          statement.addBatch(sql);

        return statement.executeBatch();
      }

      for (int i = 0; i < sqls.size(); i++)
        counts[i] = statement.executeUpdate(sqls.get(i));

      return counts;
    }
  }

  private static PreparedStatement set(final PreparedStatement statement, final List<? extends Variable<?>> parameters) throws SQLException {
    for (int i = 0; i < parameters.size(); i++)
      parameters.get(i).get(statement, i + 1);

    return statement;
  }
}