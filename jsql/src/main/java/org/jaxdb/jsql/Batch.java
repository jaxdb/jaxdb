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
import java.util.function.ObjIntConsumer;

import org.jaxdb.jsql.Delete.DELETE;
import org.jaxdb.jsql.Insert.INSERT;
import org.jaxdb.jsql.Update.UPDATE;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;

public class Batch {
  private final ArrayList<ExecuteUpdate> executeUpdates = new ArrayList<>();
  private final ArrayList<ObjIntConsumer<Transaction.Event>> listeners = new ArrayList<>();

  public Batch(final ExecuteUpdate ... statements) {
    addStatements(statements);
  }

  public Batch addStatements(final ExecuteUpdate ... statements) {
    Collections.addAll(executeUpdates, statements);
    return this;
  }

  public Batch addStatement(final INSERT<?> insert, final ObjIntConsumer<Transaction.Event> onEvent) {
    executeUpdates.add(insert);
    this.listeners.add(onEvent);
    return this;
  }

  public Batch addStatement(final INSERT<?> insert) {
    return addStatement(insert, null);
  }

  public Batch addStatement(final UPDATE update, final ObjIntConsumer<Transaction.Event> onEvent) {
    executeUpdates.add(update);
    this.listeners.add(onEvent);
    return this;
  }

  public Batch addStatement(final UPDATE update) {
    return addStatement(update, null);
  }

  public Batch addStatement(final DELETE delete, final ObjIntConsumer<Transaction.Event> onEvent) {
    executeUpdates.add(delete);
    this.listeners.add(onEvent);
    return this;
  }

  public Batch addStatement(final DELETE delete) {
    return addStatement(delete, null);
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
      final int[] allCounts = new int[executeUpdates.size()];
      int index = 0;
      Class<? extends Schema> schema = null;
      Connection connection = null;
      SQLException suppressed = null;
      try {
        for (final ExecuteUpdate executeUpdate : executeUpdates) {
          final BatchableKeyword<?> keyword = (BatchableKeyword<?>)executeUpdate;
          final Command<?> command = keyword.normalize();

          if (connection == null)
            connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema = command.getSchema(), dataSourceId, true);
          else if (schema != null && schema != command.getSchema())
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.getSchema().getSimpleName());

          try (final Compilation compilation = new Compilation(command, Schema.getDBVendor(connection), Registry.isPrepared(command.getSchema(), dataSourceId))) {
            command.compile(compilation, false);

            final String sql = compilation.getSQL().toString();
            if (compilation.isPrepared()) {
              if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
                if (statement != null) {
                  try {
                    final int[] counts = statement.executeBatch();
                    System.arraycopy(counts, 0, allCounts, index, counts.length);
                    index += counts.length;
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
                  final int[] counts = statement.executeBatch();
                  System.arraycopy(counts, 0, allCounts, index, counts.length);
                  index += counts.length;
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

        final int[] counts = statement.executeBatch();
        System.arraycopy(counts, 0, allCounts, index, counts.length);
        index += counts.length;

        if (transaction != null)
          transaction.addListener(p -> onEvent(p, allCounts));

        onEvent(Transaction.Event.EXECUTE, allCounts);
        return allCounts;
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

  private void onEvent(final Transaction.Event event, final int[] counts) {
    for (int i = 0; i < counts.length; ++i) {
      final ObjIntConsumer<Transaction.Event> listener = this.listeners.get(i);
      if (listener != null)
        listener.accept(event, counts[i]);
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