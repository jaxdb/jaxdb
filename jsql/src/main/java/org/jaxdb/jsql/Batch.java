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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.ObjIntConsumer;

import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Assertions;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Batch implements Executable.Modify.Delete, Executable.Modify.Insert, Executable.Modify.Update {
  private static final Logger logger = LoggerFactory.getLogger(Batch.class);
  private static final int DEFAULT_CAPACITY = 10;
  private final int initialCapacity;
  private int listenerOffset;
  private ArrayList<Executable.Modify> statements;
  private ArrayList<ObjIntConsumer<Transaction.Event>> listeners;

  public Batch(final Executable.Modify ... statements) {
    this(statements.length);
    Collections.addAll(initStatements(statements.length), statements);
  }

  public Batch(final Collection<Executable.Modify> statements) {
    this(statements.size());
    this.statements.addAll(statements);
  }

  public Batch(final int initialCapacity) {
    this.initialCapacity = initialCapacity;
  }

  public Batch() {
    this.initialCapacity = DEFAULT_CAPACITY;
  }

  private ArrayList<Executable.Modify> initStatements(final int initialCapacity) {
    return statements = new ArrayList<Executable.Modify>(initialCapacity) {
      private static final long serialVersionUID = -3687681471695840544L;

      @Override
      public boolean add(final Executable.Modify e) {
        return super.add(Assertions.assertNotNull(e));
      }
    };
  }

  private ArrayList<Executable.Modify> getStatements(final int initialCapacity) {
    return statements == null ? initStatements(initialCapacity) : statements;
  }

  public Batch addStatement(final Executable.Modify.Insert insert, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(insert, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Insert insert) {
    return addStatementAndListener(insert, null);
  }

  public Batch addStatement(final Executable.Modify.Update update, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(update, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Update update) {
    return addStatementAndListener(update, null);
  }

  public Batch addStatement(final Executable.Modify.Delete delete, final ObjIntConsumer<Transaction.Event> onEvent) {
    return addStatementAndListener(delete, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Delete delete) {
    return addStatementAndListener(delete, null);
  }

  private Batch addStatementAndListener(final Executable.Modify statement, final ObjIntConsumer<Transaction.Event> onEvent) {
    getStatements(initialCapacity).add(statement);
    if (onEvent != null) {
      if (listeners == null) {
        listenerOffset = statements.size() - 1;
        listeners = new ArrayList<>(initialCapacity > listenerOffset ? initialCapacity - listenerOffset : DEFAULT_CAPACITY);
      }

      listeners.add(onEvent);
    }

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

  private static int aggregate(final int[] counts, final int[] allCounts, final Statement statement, final InsertImpl<?>[] generatedKeys, final int index, int total) throws SQLException {
    ResultSet resultSet = null;
    for (int i = index, leni = index + counts.length; i < leni; ++i) {
      if (generatedKeys[i] != null) {
        if (resultSet == null)
          resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
          final data.Column<?>[] autos = generatedKeys[i].autos;
          for (int j = 0, lenj = autos.length; j < lenj;)
            autos[j].set(resultSet, ++j);
        }
      }
    }

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

  @SuppressWarnings("null")
  private int execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    if (statements == null)
      return 0;

    try {
      String last = null;
      Statement statement = null;
      final int noStatements = statements.size();
      final int[] allCounts = new int[noStatements];
      final InsertImpl<?>[] insertsWithGeneratedKeys = new InsertImpl<?>[noStatements];
      int total = 0;
      int index = 0;
      Class<? extends Schema> schema = null;
      Connection connection = null;
      SQLException suppressed = null;
      try {
        for (int i = 0; i < noStatements; ++i) {
          final Command<?> command = (Command<?>)statements.get(i);

          boolean isPrepared;
          if (transaction != null) {
            connection = transaction.getConnection();
            isPrepared = transaction.isPrepared();
          }
          else if (schema != null && schema != command.schema()) {
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.schema().getSimpleName());
          }
          else {
            final Connector connector = Database.getConnector(command.schema(), dataSourceId);
            connection = connector.getConnection();
            connection.setAutoCommit(true);
            isPrepared = connector.isPrepared();
          }

          schema = command.schema();

          final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
          final Compiler compiler = Compiler.getCompiler(vendor);
          if (isPrepared && !compiler.supportsPreparedBatch()) {
            logger.warn(vendor + " does not support prepared statement batch execution");
            isPrepared = false;
          }

          final boolean returnGeneratedKeys;
          if (command instanceof InsertImpl && ((InsertImpl<?>)command).autos.length > 0) {
            if (!compiler.supportsReturnGeneratedKeysBatch()) {
              logger.warn(vendor + " does not support return of generated keys during batch execution");
              returnGeneratedKeys = false;
            }
            else if (returnGeneratedKeys = isPrepared) {
              insertsWithGeneratedKeys[i] = (InsertImpl<?>)command;
            }
            else {
              logger.warn("Generated keys can only be provided with prepared statement batch execution");
            }
          }
          else {
            returnGeneratedKeys = false;
          }

          try (final Compilation compilation = new Compilation(command, vendor, isPrepared)) {
            command.compile(compilation, false);

            final String sql = compilation.toString();
            if (isPrepared) {
              if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
                if (statement != null) {
                  try {
                    final int[] counts = statement.executeBatch();
                    if (listeners != null) {
                      total = aggregate(counts, allCounts, statement, insertsWithGeneratedKeys, index, total);
                      index += counts.length;
                    }
                  }
                  finally {
                    suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
                  }
                }

                statement = returnGeneratedKeys ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
                last = sql;
              }

              final List<data.Column<?>> parameters = compilation.getParameters();
              if (parameters != null)
                for (int j = 0, len = parameters.size(); j < len;)
                  parameters.get(j).get((PreparedStatement)statement, ++j);

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
                    total = aggregate(counts, allCounts, statement, insertsWithGeneratedKeys, index, total);
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
          total = aggregate(counts, allCounts, statement, insertsWithGeneratedKeys, index, total);
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

  @Override
  public void close() {
    if (statements != null)
      for (final Executable.Modify statement : statements)
        statement.close();
  }
}