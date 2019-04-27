/* Copyright (c) 2018 OpenJAX
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

package org.openjax.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.openjax.ext.sql.exception.SQLExceptions;
import org.openjax.rdb.jsql.Delete.DELETE;
import org.openjax.rdb.jsql.Insert.INSERT;
import org.openjax.rdb.jsql.Update.UPDATE;
import org.openjax.ext.util.ArrayIntList;

public class Batch {
  private final List<ExecuteUpdate> executeUpdates = new ArrayList<>();

  public Batch(final ExecuteUpdate ... statements) {
    addStatements(statements);
  }

  public Batch addStatements(final ExecuteUpdate ... statements) {
    Collections.addAll(executeUpdates, statements);
    return this;
  }

  public Batch addStatement(final INSERT<?> insert) {
    executeUpdates.add(insert);
    return this;
  }

  public Batch addStatement(final UPDATE update) {
    executeUpdates.add(update);
    return this;
  }

  public Batch addStatement(final DELETE delete) {
    executeUpdates.add(delete);
    return this;
  }

  public int size() {
    return executeUpdates.size();
  }

  @SuppressWarnings("resource")
  private int[] execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    try {
      if (executeUpdates.size() == 0)
        return null;

      String last = null;
      Statement statement = null;
      final ArrayIntList results = new ArrayIntList(executeUpdates.size());
      Class<? extends Schema> schema = null;
      Connection connection = null;
      for (final ExecuteUpdate executeUpdate : executeUpdates) {
        final BatchableKeyword<?> keyword = (BatchableKeyword<?>)executeUpdate;
        final Command command = keyword.normalize();

        if (connection == null)
          connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema = command.getSchema(), dataSourceId);
        else if (schema != null && schema != command.getSchema())
          throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.getSchema().getSimpleName());

        final Compilation compilation = new Compilation(command, Schema.getDBVendor(connection), Registry.isPrepared(command.getSchema()));
        command.compile(compilation);

        final String sql = compilation.getSQL();
        if (compilation.isPrepared()) {
          if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
            if (statement != null) {
              results.addAll(statement.executeBatch());
              statement.close();
            }

            statement = connection.prepareStatement(last = sql);
          }

          final List<type.DataType<?>> parameters = compilation.getParameters();
          for (int j = 0; j < parameters.size(); ++j)
            parameters.get(j).get((PreparedStatement)statement, j + 1);

          ((PreparedStatement)statement).addBatch();
        }
        else {
          if (statement instanceof PreparedStatement) {
            results.addAll(statement.executeBatch());
            statement.close();
            statement = connection.createStatement();
          }
          else if (statement == null) {
            statement = connection.createStatement();
          }

          statement.addBatch(sql.toString());
        }
      }

      results.addAll(statement.executeBatch());
      statement.close();
      if (transaction == null)
        connection.close();

      return results.toArray(new int[results.size()]);
    }
    catch (final SQLException e) {
      throw SQLExceptions.getStrongType(e);
    }
  }

  public final int[] execute(final String dataSourceId) throws IOException, SQLException {
    return execute(null, dataSourceId);
  }

  public final int[] execute(final Transaction transaction) throws IOException, SQLException {
    return execute(transaction, transaction != null ? transaction.getDataSourceId() : null);
  }

  public int[] execute() throws IOException, SQLException {
    return execute(null, null);
  }

  @Override
  public boolean equals(final Object obj) {
    return obj == this || obj instanceof Batch && executeUpdates.containsAll(((Batch)obj).executeUpdates);
  }

  @Override
  public int hashCode() {
    return executeUpdates.hashCode();
  }
}