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

import static org.libj.lang.Assertions.*;

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

import org.jaxdb.vendor.DBVendor;
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
  private ArrayList<Command.Modify<?,?>> statements;

  public Batch(final Executable.Modify ... statements) {
    this(statements.length);
    Collections.addAll(initStatements(initialCapacity), statements);
  }

  public Batch(final Collection<Executable.Modify> statements) {
    this(statements.size());
    initStatements(initialCapacity).addAll(statements);
  }

  public Batch(final int initialCapacity) {
    this.initialCapacity = initialCapacity;
  }

  public Batch() {
    this.initialCapacity = DEFAULT_CAPACITY;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private ArrayList<Executable.Modify> initStatements(final int initialCapacity) {
    return (ArrayList)(statements = new ArrayList<Command.Modify<?,?>>(initialCapacity) {
      @Override
      public boolean add(final Command.Modify<?,?> e) {
        return super.add(assertNotNull(e));
      }
    });
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private ArrayList<Executable.Modify> getStatements(final int initialCapacity) {
    return (ArrayList)(statements == null ? initStatements(initialCapacity) : statements);
  }

  public Batch addStatement(final Executable.Modify.Insert insert) {
    getStatements(initialCapacity).add(insert);
    return this;
  }

  public Batch addStatement(final Executable.Modify.Update update) {
    getStatements(initialCapacity).add(update);
    return this;
  }

  public Batch addStatement(final Executable.Modify.Delete delete) {
    getStatements(initialCapacity).add(delete);
    return this;
  }

  public Batch addStatements(final Executable.Modify ... statements) {
    Collections.addAll(getStatements(Math.max(initialCapacity, statements.length)), statements);
    return this;
  }

  public Batch addStatements(final Collection<Executable.Modify> statements) {
    getStatements(Math.max(initialCapacity, statements.size())).addAll(statements);
    return this;
  }

  public int size() {
    return statements == null ? 0 : statements.size();
  }

  private static int aggregate(final int[] counts, final Statement statement, final InsertImpl<?>[] generatedKeys, final int index, int total) throws SQLException {
    ResultSet resultSet = null;
    for (int i = index, leni = index + counts.length; i < leni; ++i) {
      if (generatedKeys[i] != null) {
        if (resultSet == null)
          resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
          final data.Column<?>[] autos = generatedKeys[i].autos;
          for (int j = 0, lenj = autos.length; j < lenj;)
            autos[j].getParameter(resultSet, ++j);
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

    return hasInfo ? total : Statement.SUCCESS_NO_INFO;
  }

  private void onExecute(final String sessionId, final int start, final int end, final int[] counts) {
    for (int i = start; i < end; ++i) {
      final Command.Modify<?,?> statement = statements.get(i);
      if (statement.listeners != null)
        statement.listeners.onExecute(sessionId, counts[i - start]);
    }
  }

  private void onCommit(final Transaction transaction, final Connector connector, final Connection connection, final String sessionId, final int start, final int end, final int[] counts) {
    for (int i = start; i < end; ++i) {
      final Command.Modify<?,?> statement = statements.get(i);
      if (transaction != null) {
        Transaction.Event.COMMIT.add(transaction.getListeners(), sessionId, c -> {
          statement.onCommit(connector, connection);
          if (statement.listeners != null)
            statement.listeners.onCommit(c);
        });
      }
      else {
        statement.onCommit(connector, connection);
        if (statement.listeners != null)
          statement.listeners.onCommit(counts[i - start]);
      }
    }
  }

  private static void afterExecute(final Compilation[] compilations, final int start, final int end) {
    for (int i = start; i < end; ++i) {
      try (final Compilation compilation = compilations[i]) {
        compilation.afterExecute(true); // FIXME: This invokes the GenerateOn evaluation of dynamic values, and is happening after notifyListeners(EXECUTE) .. should it happen before? or keep it as is, so it happens after EXECUTE, but before COMMIT
      }
    }
  }

  @SuppressWarnings({"null", "resource"})
  private int execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    if (statements == null)
      return 0;

    try {
      final int noStatements = statements.size();
      final InsertImpl<?>[] insertsWithGeneratedKeys = new InsertImpl<?>[noStatements];
      final Compilation[] compilations = new Compilation[noStatements];
      final Connection connection;
      final Connector connector;
      boolean isPrepared;

      final Command<?,?> command0 = statements.get(0);
      final Class<? extends Schema> schema = command0.schemaClass();
      if (transaction != null) {
        connector = transaction.getConnector();
        connection = transaction.getConnection();
        isPrepared = transaction.isPrepared();
      }
      else {
        connector = Database.getConnector(command0.schemaClass(), dataSourceId);
        connection = connector.getConnection();
        connection.setAutoCommit(true);
        isPrepared = connector.isPrepared();
      }

      String last = null;
      Statement statement = null;
      SQLException suppressed = null;
      int total = 0;
      int index = 0;

      String sessionId = null; // FIXME: NOT FUNCITONAL!!!
      Compilation compilation = null;
      try {
        int listenerIndex = 0;
        for (int statementIndex = 0; statementIndex < noStatements; ++statementIndex) {
          final Command<?,?> command = statements.get(statementIndex);

          if (schema != command.schemaClass())
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schema.getSimpleName() + " and " + command.schemaClass().getSimpleName());

          final DBVendor vendor = DBVendor.valueOf(connection.getMetaData());
          final Compiler compiler = Compiler.getCompiler(vendor);
          if (isPrepared && !compiler.supportsPreparedBatch()) {
            if (logger.isWarnEnabled())
              logger.warn(vendor + " does not support prepared statement batch execution");

            isPrepared = false;
          }

          final boolean returnGeneratedKeys;
          if (command instanceof InsertImpl && ((InsertImpl<?>)command).autos.length > 0) {
            if (!compiler.supportsReturnGeneratedKeysBatch()) {
              if (logger.isWarnEnabled())
                logger.warn(vendor + " does not support return of generated keys during batch execution");

              returnGeneratedKeys = false;
            }
            else if (returnGeneratedKeys = isPrepared) {
              insertsWithGeneratedKeys[statementIndex] = (InsertImpl<?>)command;
            }
            else if (logger.isWarnEnabled()) {
              logger.warn("Generated keys can only be provided with prepared statement batch execution");
            }
          }
          else {
            returnGeneratedKeys = false;
          }

          compilation = compilations[statementIndex] = new Compilation(command, vendor, isPrepared);
          command.compile(compilation, false);

          final String sql = compilation.toString();
          if (isPrepared) {
            if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
              if (statement != null) {
                try {
                  compilation.setSessionId(connection, statement, sessionId);
                  final int[] counts = statement.executeBatch();
                  total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
                  index += counts.length;
                  afterExecute(compilations, listenerIndex, statementIndex);
                  onExecute(sessionId, listenerIndex, statementIndex, counts);
                  onCommit(transaction, connector, connection, sessionId, listenerIndex, statementIndex, counts);
                  listenerIndex = statementIndex;
                }
                finally {
                  suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
                }
              }

              statement = returnGeneratedKeys ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
              last = sql;
            }

            final List<data.Column<?>> parameters = compilation.getParameters();
            if (parameters != null) {
              final int updateWhereIndex = compilation.getUpdateWhereIndex();
              for (int p = 0, len = parameters.size(); p < len;)
                parameters.get(p).setParameter((PreparedStatement)statement, p >= updateWhereIndex, ++p);
            }

            ((PreparedStatement)statement).addBatch();
          }
          else {
            if (statement == null) {
              statement = connection.createStatement();
              compilation.setSessionId(connection, statement, sessionId);
            }
            else if (statement instanceof PreparedStatement) {
              try {
                compilation.setSessionId(connection, statement, sessionId);
                final int[] counts = statement.executeBatch();
                total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
                index += counts.length;
                afterExecute(compilations, listenerIndex, statementIndex);
                onExecute(sessionId, listenerIndex, statementIndex, counts);
                onCommit(transaction, connector, connection, sessionId, listenerIndex, statementIndex, counts);
                listenerIndex = statementIndex;
              }
              finally {
                suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
              }

              statement = connection.createStatement();
            }

            statement.addBatch(sql);
          }
        }

        compilation.setSessionId(connection, statement, sessionId);
        final int[] counts = statement.executeBatch();
        total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
        index += counts.length;
        afterExecute(compilations, listenerIndex, noStatements);
        onExecute(sessionId, listenerIndex, noStatements, counts);
        onCommit(transaction, connector, connection, sessionId, listenerIndex, noStatements, counts);

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
    return execute(null, (String)null);
  }
}