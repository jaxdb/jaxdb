/* Copyright (c) 2018 JAX-DB
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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jaxdb.jsql.Delete.DELETE;
import org.jaxdb.jsql.Insert.INSERT;
import org.jaxdb.jsql.Update.UPDATE;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.libj.util.primitive.ArrayIntList;

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

  @SuppressWarnings({"null"})
  private int[] execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    if (executeUpdates.size() == 0)
      return null;

    try {
      String last = null;
      Statement statement = null;
      final ArrayIntList results = new ArrayIntList(executeUpdates.size());
      Class<? extends Schema> schema = null;
      Connection connection = null;
      SQLException suppressed = null;
      try {
        for (final ExecuteUpdate executeUpdate : executeUpdates) {
          final BatchableKeyword<?> keyword = (BatchableKeyword<?>)executeUpdate;
          final Command command = keyword.normalize();

          if (connection == null)
            connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema = command.getSchema(), dataSourceId, true);
          else if (schema != null && schema != command.getSchema())
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.getSchema().getSimpleName());

          try (final Compilation compilation = new Compilation(command, Schema.getDBVendor(connection), Registry.isPrepared(command.getSchema()))) {
            command.compile(compilation);

            final String sql = compilation.getSQL();
            if (compilation.isPrepared()) {
              if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
                if (statement != null) {
                  try {
                    results.addAll(statement.executeBatch());
                  }
                  finally {
                    suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
                  }
                }

                statement = connection.prepareStatement(last = sql);
              }

              final List<type.DataType<?>> parameters = compilation.getParameters();
              for (int j = 0; j < parameters.size(); ++j)
                parameters.get(j).get((PreparedStatement)statement, j + 1);

              ((PreparedStatement)statement).addBatch();
            }
            else {
              if (statement == null) {
                statement = connection.createStatement();
              }
              else if (statement instanceof PreparedStatement) {
                try {
                  results.addAll(statement.executeBatch());
                }
                finally {
                  suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
                }

                statement = connection.createStatement();
              }

              statement.addBatch(sql);
            }
          }
        }

        results.addAll(statement.executeBatch());
        return results.toArray(new int[results.size()]);
      }
      finally {
        SQLException e = Throwables.addSuppressed(statement == null ? null : AuditStatement.close(statement), suppressed);
        if (transaction == null && connection != null)
          e = Throwables.addSuppressed(e, AuditConnection.close(connection));

        if (e != null)
          throw e;
      }
    }
    catch (final SQLException e) {
      throw SQLExceptions.toStrongType(e);
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
    return 31 + executeUpdates.hashCode();
  }
}