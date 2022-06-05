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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.libj.util.function.BiObjIntConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Batch implements Executable.Modify.Delete, Executable.Modify.Insert, Executable.Modify.Update {
  private static final Logger logger = LoggerFactory.getLogger(Batch.class);
  private static final int DEFAULT_CAPACITY = 10;
  private final int initialCapacity;
  private int listenerOffset;
  private ArrayList<Executable.Modify> statements;
  private ArrayList<BiObjIntConsumer<String,Transaction.Event>> listeners;

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
      @Override
      public boolean add(final Executable.Modify e) {
        return super.add(assertNotNull(e));
      }
    };
  }

  private ArrayList<Executable.Modify> getStatements(final int initialCapacity) {
    return statements == null ? initStatements(initialCapacity) : statements;
  }

  public Batch addStatement(final Executable.Modify.Insert insert, final BiObjIntConsumer<String,Transaction.Event> onEvent) {
    return addStatementAndListener(insert, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Insert insert) {
    return addStatementAndListener(insert, null);
  }

  public Batch addStatement(final Executable.Modify.Update update, final BiObjIntConsumer<String,Transaction.Event> onEvent) {
    return addStatementAndListener(update, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Update update) {
    return addStatementAndListener(update, null);
  }

  public Batch addStatement(final Executable.Modify.Delete delete, final BiObjIntConsumer<String,Transaction.Event> onEvent) {
    return addStatementAndListener(delete, onEvent);
  }

  public Batch addStatement(final Executable.Modify.Delete delete) {
    return addStatementAndListener(delete, null);
  }

  private Batch addStatementAndListener(final Executable.Modify statement, final BiObjIntConsumer<String,Transaction.Event> onEvent) {
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

  private void addEventListener(final Connector connector, final Connection connection, final Command<?> command, final BiConsumer<String,Transaction.Event>[] eventListeners, final AtomicReference<int[]> countRef, final int i, final int eventIndex) {
    final BiObjIntConsumer<String,Transaction.Event> listener = listeners == null || i < listenerOffset || i - listenerOffset >= listeners.size() ? null : listeners.get(i - listenerOffset);
    if (listener != null) {
      eventListeners[i] = (s, e) -> {
        final int count = countRef.get()[eventIndex];
        if (e == Transaction.Event.COMMIT)
          command.onCommit(connector, connection, s, count);

        listener.accept(s, e, count);
      };
    }
    else {
      eventListeners[i] = (s, e) -> {
        if (e == Transaction.Event.COMMIT)
          command.onCommit(connector, connection, s, countRef.get()[eventIndex]);
      };
    }
  }

  private static void notifyListenerEvent(final String sessionId, final Transaction.Event event, final BiConsumer<String,Transaction.Event>[] eventListeners, final int start, final int end) {
    for (int i = start; i < end; ++i)
      eventListeners[i].accept(sessionId, event);
  }

  private static void addListenerCommit(final String sessionId, final Transaction transaction, final BiConsumer<String,Transaction.Event>[] eventListeners, final int start, final int end) {
    if (transaction != null) {
      transaction.addListener(e -> {
        if (e == Transaction.Event.COMMIT)
          notifyListenerEvent(sessionId, Transaction.Event.COMMIT, eventListeners, start, end);
      });
    }
    else {
      notifyListenerEvent(sessionId, Transaction.Event.COMMIT, eventListeners, start, end);
    }
  }

  private static void afterExecute(final Compilation[] compilations, final int start, final int end) {
    for (int i = start; i < end; ++i) {
      try (final Compilation compilation = compilations[i]) {
        compilation.afterExecute(true); // FIXME: This invokes the GenerateOn evaluation of dynamic values, and is happening after notifyListeners(EXECUTE) .. should it happen before? or keep it as is, so it happens after EXECUTE, but before COMMIT
      }
    }
  }

  @SuppressWarnings({"null", "resource", "unchecked"})
  private int execute(final Transaction transaction, final String dataSourceId, final Consumer<Throwable> awaitNotify) throws IOException, SQLException {
    if (statements == null)
      return 0;

    AtomicReference<int[]> countRef = new AtomicReference<>();
    try {
      final int noStatements = statements.size();
      final BiConsumer<String,Transaction.Event>[] eventListeners = new BiConsumer[noStatements];
      final InsertImpl<?>[] insertsWithGeneratedKeys = new InsertImpl<?>[noStatements];
      final Compilation[] compilations = new Compilation[noStatements];
      final Connection connection;
      final Connector connector;

      String last = null;
      Statement statement = null;
      SQLException suppressed = null;
      boolean isPrepared;
      int total = 0;
      int index = 0;

      final Command<?> command0 = (Command<?>)statements.get(0);
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

      String sessionId = null;
      final CountDownLatch latch = new CountDownLatch(noStatements);
      try {
        int listenerIndex = 0;
        for (int statementIndex = 0, eventIndex = 0; statementIndex < noStatements; ++statementIndex) {
          final Command<?> command = (Command<?>)statements.get(statementIndex);

          if (awaitNotify != null) {
            sessionId = connector.getSchema().addNotifyHook(command.getTable(), (s, t) -> {
              try {
                awaitNotify.accept(t);
              }
              finally {
                latch.countDown();
              }
            });
          }

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

          final Compilation compilation = compilations[statementIndex] = new Compilation(command, vendor, isPrepared);
          command.compile(compilation, false);

          final String sql = compilation.toString();
          if (isPrepared) {
            if (!(statement instanceof PreparedStatement) || !sql.equals(last)) {
              if (statement != null) {
                try {
                  final int[] counts = statement.executeBatch();
                  total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
                  index += counts.length;
                  countRef.set(counts);
                  countRef = new AtomicReference<>();
                  notifyListenerEvent(sessionId, Transaction.Event.EXECUTE, eventListeners, listenerIndex, statementIndex);
                  afterExecute(compilations, listenerIndex, statementIndex);
                  addListenerCommit(sessionId, transaction, eventListeners, listenerIndex, statementIndex);
                  listenerIndex = statementIndex;
                  eventIndex = 0;
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
            }
            else if (statement instanceof PreparedStatement) {
              try {
                final int[] counts = statement.executeBatch();
                total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
                index += counts.length;
                countRef.set(counts);
                countRef = new AtomicReference<>();
                notifyListenerEvent(sessionId, Transaction.Event.EXECUTE, eventListeners, listenerIndex, statementIndex);
                afterExecute(compilations, listenerIndex, statementIndex);
                addListenerCommit(sessionId, transaction, eventListeners, listenerIndex, statementIndex);
                listenerIndex = statementIndex;
                eventIndex = 0;
              }
              finally {
                suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
              }

              statement = connection.createStatement();
            }

            statement.addBatch(sql);
          }

          addEventListener(connector, connection, command, eventListeners, countRef, statementIndex, eventIndex++);
        }

        final int[] counts = statement.executeBatch();
        total = aggregate(counts, statement, insertsWithGeneratedKeys, index, total);
        index += counts.length;
        countRef.set(counts);
        notifyListenerEvent(sessionId, Transaction.Event.EXECUTE, eventListeners, listenerIndex, noStatements);
        afterExecute(compilations, listenerIndex, noStatements);
        addListenerCommit(sessionId, transaction, eventListeners, listenerIndex, noStatements);

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
  public int execute(final String dataSourceId, final Consumer<Throwable> awaitNotify) throws IOException, SQLException {
    return execute(null, dataSourceId, awaitNotify);
  }

  @Override
  public int execute(final Transaction transaction, final Consumer<Throwable> awaitNotify) throws IOException, SQLException {
    return execute(transaction, transaction != null ? transaction.getDataSourceId() : null, awaitNotify);
  }

  @Override
  public int execute(final Consumer<Throwable> awaitNotify) throws IOException, SQLException {
    return execute(null, (String)null, awaitNotify);
  }

  @Override
  public final int execute(final String dataSourceId) throws IOException, SQLException {
    return execute(null, dataSourceId, null);
  }

  @Override
  public final int execute(final Transaction transaction) throws IOException, SQLException {
    return execute(transaction, transaction != null ? transaction.getDataSourceId() : null, null);
  }

  @Override
  public int execute() throws IOException, SQLException {
    return execute(null, (String)null, null);
  }
}