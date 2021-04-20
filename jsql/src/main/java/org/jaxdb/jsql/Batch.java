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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.ObjIntConsumer;

import org.jaxdb.jsql.Delete.DELETE;
import org.jaxdb.jsql.Insert.INSERT;
import org.jaxdb.jsql.Update.UPDATE;
import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;

public class Batch implements Executable.Modify.Delete, Executable.Modify.Insert, Executable.Modify.Update {
  private static final int DEFAULT_CAPACITY = 10;
  private final int initialCapacity;
  private int listenerOffset;
  private ArrayList<Executable.Modify.Statement> statements;
  private ArrayList<ObjIntConsumer<Transaction.Event>> listeners;

  public Batch(final Executable.Modify.Statement ... statements) {
    this(statements.length);
    Collections.addAll(initStatements(statements.length), statements);
  }

  public Batch(final Collection<Executable.Modify.Statement> statements) {
    this(statements.size());
    this.statements.addAll(statements);
  }

  public Batch(final int initialCapacity) {
    this.initialCapacity = initialCapacity;
  }

  public Batch() {
    this.initialCapacity = DEFAULT_CAPACITY;
  }

  private ArrayList<Executable.Modify.Statement> initStatements(final int initialCapacity) {
    return statements = new ArrayList<Executable.Modify.Statement>(initialCapacity) {
      private static final long serialVersionUID = -3687681471695840544L;

      @Override
      public boolean add(final Executable.Modify.Statement e) {
        return super.add(Objects.requireNonNull(e));
      }
    };
  }

  private ArrayList<Executable.Modify.Statement> getStatements(final int initialCapacity) {
    return statements == null ? initStatements(initialCapacity) : statements;
  }

  public Batch addStatement(final INSERT<?> insert, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(insert, onEvent);
  }

  public Batch addStatement(final INSERT<?> insert) {
    return addStatementAndListener(insert, null);
  }

  public Batch addStatement(final UPDATE update, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(update, onEvent);
  }

  public Batch addStatement(final UPDATE update) {
    return addStatementAndListener(update, null);
  }

  public Batch addStatement(final DELETE delete, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(delete, onEvent);
  }

  public Batch addStatement(final DELETE delete) {
    return addStatementAndListener(delete, null);
  }

  private Batch addStatementAndListener(final Executable.Modify.Statement statement, final ObjIntConsumer<Transaction.Event> onEvent) {
    if (onEvent != null) {
      if (listeners == null) {
        listenerOffset = statements == null ? 0 : statements.size();
        listeners = new ArrayList<>(initialCapacity > listenerOffset ? initialCapacity - listenerOffset : DEFAULT_CAPACITY);
      }

      listeners.add(onEvent);
    }

    getStatements(initialCapacity).add(statement);
    return this;
  }

  public Batch addStatements(final Executable.Modify.Update ... statements) {
    Collections.addAll(getStatements(Math.max(initialCapacity, statements.length)), statements);
    return this;
  }

  public Batch addStatements(final Collection<Executable.Modify.Update> statements) {
    getStatements(Math.max(initialCapacity, statements.size())).addAll(statements);
    return this;
  }

  public int size() {
    return statements == null ? 0 : statements.size();
  }

  private static int aggregate(final int[] counts, final int[] allCounts, final int index, int total) {
    if (total == Statement.EXECUTE_FAILED)
      return Statement.EXECUTE_FAILED;

    boolean hasInfo = total != Statement.SUCCESS_NO_INFO;
    if (!hasInfo)
      total = 0;

    for (int i = 0; i < counts.length; ++i) {
      final int count = counts[i];
      if (count == Statement.EXECUTE_FAILED)
        return Statement.EXECUTE_FAILED;

      if (count != Statement.SUCCESS_NO_INFO) {
        hasInfo = true;
        total += count;
      }
      else {
        counts[i] = 0;
      }
    }

    System.arraycopy(counts, 0, allCounts, index, counts.length);
    return hasInfo ? total : Statement.SUCCESS_NO_INFO;
  }

  @SuppressWarnings({"null"})
  private int execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    if (statements == null)
      return 0;

    try {
      String last = null;
      Statement statement = null;
      final int[] allCounts = new int[statements.size()];
      int total = 0;
      int index = 0;
      Class<? extends Schema> schema = null;
      Connection connection = null;
      SQLException suppressed = null;
      try {
        for (int i = 0; i < statements.size(); ++i) {
          final Executable.Modify.Command<?> command = (Executable.Modify.Command<?>)statements.get(i);
          if (connection == null)
            connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema = command.schema(), dataSourceId, true);
          else if (schema != null && schema != command.schema())
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.schema().getSimpleName());

          final DBVendor vendor = Schema.getDBVendor(connection);
          final boolean isPrepared = Registry.isPrepared(command.schema(), dataSourceId) && Compiler.getCompiler(vendor).supportsPreparedBatch();
          try (final Compilation compilation = new Compilation(command, vendor, isPrepared)) {
            command.compile(compilation, false);

            final String sql = compilation.getSQL().toString();
            if (isPrepared) {
              if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
                if (statement != null) {
                  try {
                    final int[] counts = statement.executeBatch();
                    if (listeners != null) {
                      total = aggregate(counts, allCounts, index, total);
                      index += counts.length;
                    }
                  }
                  finally {
                    suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
                  }
                }

                statement = connection.prepareStatement(last = sql);
              }

              final List<type.DataType<?>> parameters = compilation.getParameters();
              // FIXME: This loop can avoid the j + 1
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
                  if (listeners != null) {
                    total = aggregate(counts, allCounts, index, total);
                    index += counts.length;
                  }
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
        if (listeners != null) {
          total = aggregate(counts, allCounts, index, total);
          index += counts.length;

          if (transaction != null)
            transaction.addListener(p -> onEvent(p, allCounts));

          onEvent(Transaction.Event.EXECUTE, allCounts);
        }

        return total;
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
    for (int i = listenerOffset; i < counts.length; ++i) {
      final ObjIntConsumer<Transaction.Event> listener = listeners.get(i - listenerOffset);
      if (listener != null)
        listener.accept(event, counts[i]);
    }
  }

  @Override
  public final int execute(final String dataSourceId) throws IOException, SQLException {
    return execute(null, dataSourceId);
  }

  @Override
  public final int execute(final Transaction transaction) throws IOException, SQLException {
    return execute(transaction, transaction != null ? transaction.getDataSourceId() : null);
  }

  @Override
  public int execute() throws IOException, SQLException {
    return execute(null, null);
  }
}