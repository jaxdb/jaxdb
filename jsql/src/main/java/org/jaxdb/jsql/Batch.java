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

import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Batch implements statement.NotifiableModification.Delete, statement.NotifiableModification.Insert, statement.NotifiableModification.Update {
  private static final Logger logger = LoggerFactory.getLogger(Batch.class);
  protected static final int DEFAULT_CAPACITY = 10;

  private final int initialCapacity;
  private ArrayList<Command.Modification<?,?,?>> commands;

  public Batch(final statement.Modification ... statements) {
    this(statements.length);
    Collections.addAll(initCommands(initialCapacity), statements);
  }

  public Batch(final Collection<statement.Modification> statements) {
    this(statements.size());
    initCommands(initialCapacity).addAll(statements);
  }

  public Batch(final int initialCapacity) {
    this.initialCapacity = initialCapacity;
  }

  public Batch() {
    this.initialCapacity = DEFAULT_CAPACITY;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private ArrayList<statement.Modification> initCommands(final int initialCapacity) {
    return (ArrayList)(commands = new ArrayList<Command.Modification<?,?,?>>(initialCapacity) {
      @Override
      public boolean add(final Command.Modification<?,?,?> e) {
        return super.add(assertNotNull(e));
      }
    });
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private ArrayList<statement.Modification> getCommands(final int initialCapacity) {
    return (ArrayList)(commands == null ? initCommands(initialCapacity) : commands);
  }

  public Batch addStatement(final statement.Modification.Insert insert) {
    getCommands(initialCapacity).add(insert);
    return this;
  }

  public Batch addStatement(final statement.Modification.Update update) {
    getCommands(initialCapacity).add(update);
    return this;
  }

  public Batch addStatement(final statement.Modification.Delete delete) {
    getCommands(initialCapacity).add(delete);
    return this;
  }

  public Batch addStatements(final statement.Modification ... statements) {
    Collections.addAll(getCommands(Math.max(initialCapacity, statements.length)), statements);
    return this;
  }

  public Batch addStatements(final Collection<statement.Modification> statements) {
    getCommands(Math.max(initialCapacity, statements.size())).addAll(statements);
    return this;
  }

  public int size() {
    return commands == null ? 0 : commands.size();
  }

  public void clear() {
    if (commands != null)
      commands.clear();
  }

  private static int aggregate(final Compiler compiler, final OnNotifyCallbackList onNotifyCallbackList, final int[] counts, final Statement statement, final Command.Insert<?>[] generatedKeys, final int index, int total) throws SQLException {
    if (total != Statement.EXECUTE_FAILED) {
      boolean hasInfo = total != Statement.SUCCESS_NO_INFO;
      if (!hasInfo)
        total = 0;

      int aggregate = 0;
      for (int i = 0, i$ = counts.length; i < i$; ++i) { // [A]
        final int count = counts[i];
        if (count == Statement.EXECUTE_FAILED)
          return Statement.EXECUTE_FAILED;

        if (count != Statement.SUCCESS_NO_INFO) {
          hasInfo = true;
          aggregate += count;
        }
        else {
          counts[i] = 0;
        }
      }

      if (onNotifyCallbackList != null)
        onNotifyCallbackList.setCount(aggregate);

      total = hasInfo ? total + aggregate : Statement.SUCCESS_NO_INFO;
    }

    ResultSet resultSet = null;
    for (int i = index, i$ = index + counts.length; i < i$; ++i) { // [A]
      if (generatedKeys[i] != null) {
        if (resultSet == null)
          resultSet = statement.getGeneratedKeys();

        if (resultSet.next()) {
          final data.Column<?>[] autos = generatedKeys[i].autos;
          for (int j = 0, j$ = autos.length; j < j$;) // [A]
            autos[j].read(compiler, resultSet, ++j);
        }
      }
    }

    return total;
  }

  private void onExecute(final int start, final int end, final int[] counts) {
    for (int i = start; i < end; ++i) { // [RA]
      final Command.Modification<?,?,?> command = commands.get(i);
      if (command.callbacks != null)
        command.callbacks.onExecute(counts[i - start]);
    }
  }

  private void onCommit(final Transaction transaction, final int start, final int end, final int[] counts) {
    for (int i = start; i < end; ++i) { // [RA]
      final Command.Modification<?,?,?> command = commands.get(i);
      if (transaction != null) {
        final Callbacks callbacks = transaction.getCallbacks();
        if (command.callbacks != null)
          callbacks.addOnCommit((final int c) -> command.callbacks.onCommit(c));
      }
      else if (command.callbacks != null) {
        command.callbacks.onCommit(counts[i - start]);
      }
    }
  }

  private static void afterExecute(final Compilation[] compilations, final int start, final int end) {
    for (int i = start; i < end; ++i) { // [A]
      try (final Compilation compilation = compilations[i]) {
        // FIXME: This invokes the GenerateOn evaluation of dynamic values, and is happening after notifyListeners(EXECUTE)...
        // FIXME: Should it happen before? or keep it as is, so it happens after EXECUTE, but before COMMIT?
        compilation.afterExecute(true);
      }
    }
  }

  @SuppressWarnings({"null", "resource"})
  private NotifiableBatchResult execute(final Transaction transaction, Connector connector, Connection connection, boolean isPrepared, final Transaction.Isolation isolation) throws IOException, SQLException {
    final int noCommands;
    if (commands == null || (noCommands = commands.size()) == 0)
      return null;

    final Schema schema = commands.get(0).getSchema();
    try {
      final Command.Insert<?>[] insertsWithGeneratedKeys = new Command.Insert<?>[noCommands];
      final Compilation[] compilations = new Compilation[noCommands];

      final Class<? extends Schema> schemaClass = schema.getClass();

      if (transaction != null) {
        isPrepared = transaction.isPrepared();
        connection = transaction.getConnection();
      }
      else if (connection == null) {
        if (connector == null)
          connector = schema.getConnector();

        isPrepared = connector.isPrepared();
        connection = connector.getConnection(isolation);
        connection.setAutoCommit(true);
      }

      String sql;
      String sqlPrev = null;
      Statement statement = null;
      PreparedStatement preparedStatement = null;
      SQLException suppressed = null;
      int total = 0;
      int index = 0;

      ArrayList<OnNotifyCallbackList> onNotifyCallbackLists = null;
      Compilation compilation = null;
      String sessionId;
      String sessionIdPrev = null;
      OnNotifyCallbackList onNotifyCallbackListPrev = null;
      OnNotifyCallbackList onNotifyCallbackList = null;
      final DbVendor vendor = DbVendor.valueOf(connection.getMetaData());
      final Compiler compiler = Compiler.getCompiler(vendor);
      if (isPrepared && !compiler.supportsPreparedBatch()) {
        if (logger.isWarnEnabled()) { logger.warn(vendor + " does not support prepared statement batch execution"); }
        isPrepared = false;
      }

      if (!isPrepared)
        statement = connection.createStatement();

      try {
        int listenerIndex = 0;
        for (int statementIndex = 0; statementIndex < noCommands; ++statementIndex, sqlPrev = sql, sessionIdPrev = sessionId, onNotifyCallbackListPrev = onNotifyCallbackList) { // [RA]
          final Command.Modification<?,?,?> command = commands.get(statementIndex);

          if (schemaClass != command.getSchema().getClass())
            throw new IllegalArgumentException("Cannot execute batch across different schemas: " + schemaClass.getSimpleName() + " and " + command.getSchema().getClass().getSimpleName());

          final boolean returnGeneratedKeys;
          if (command instanceof Command.Insert && ((Command.Insert<?>)command).autos.length > 0) {
            if (!compiler.supportsReturnGeneratedKeysBatch()) {
              if (logger.isWarnEnabled()) { logger.warn(vendor + " does not support return of generated keys during batch execution"); }
              returnGeneratedKeys = false;
            }
            else if (returnGeneratedKeys = isPrepared) {
              insertsWithGeneratedKeys[statementIndex] = (Command.Insert<?>)command;
            }
            else {
              if (logger.isWarnEnabled()) { logger.warn("Generated keys can only be provided with prepared statement batch execution"); }
            }
          }
          else {
            returnGeneratedKeys = false;
          }

          sessionId = command.sessionId;
          if (sessionId != null) {
            onNotifyCallbackList = command.callbacks != null && command.callbacks.onNotifys != null ? command.callbacks.onNotifys.get(sessionId) : null;
            if (onNotifyCallbackList != null) {
              schema.awaitNotify(sessionId, onNotifyCallbackList);

              if (transaction != null)
                transaction.onNotify(onNotifyCallbackList);

              if (onNotifyCallbackLists == null)
                onNotifyCallbackLists = new ArrayList<>();

              onNotifyCallbackLists.add(onNotifyCallbackList);
            }
          }
          else {
            onNotifyCallbackList = null;
          }

          compilation = compilations[statementIndex] = new Compilation(command, vendor, compiler, isPrepared);
          command.compile(compilation, false);

          sql = compilation.toString();

          if (isPrepared) {
            if (sessionIdPrev != null || !sql.equals(sqlPrev)) {
              if (preparedStatement != null) {
                try {
                  Statement sessionStatement = null;
                  if (sessionIdPrev != null)
                    compiler.setSessionId(sessionStatement = connection.createStatement(), sessionIdPrev);

                  final int[] counts = preparedStatement.executeBatch();
                  total = aggregate(compiler, onNotifyCallbackListPrev, counts, preparedStatement, insertsWithGeneratedKeys, index, total);

                  if (sessionIdPrev != null)
                    compiler.setSessionId(sessionStatement, null);

                  index += counts.length;
                  afterExecute(compilations, listenerIndex, statementIndex);
                  onExecute(listenerIndex, statementIndex, counts);
                  onCommit(transaction, listenerIndex, statementIndex, counts);
                  listenerIndex = statementIndex;
                }
                finally {
                  suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(preparedStatement));
                }
              }

              preparedStatement = returnGeneratedKeys ? connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS) : connection.prepareStatement(sql);
            }

            final ArrayList<data.Column<?>> parameters = compilation.getParameters();
            if (parameters != null) {
              final int updateWhereIndex = compilation.getUpdateWhereIndex();
              for (int p = 0, p$ = parameters.size(); p < p$;) // [RA]
                parameters.get(p).write(compiler, preparedStatement, p >= updateWhereIndex, ++p);
            }

            command.close();
            preparedStatement.addBatch();
          }
          else {
            if (sessionIdPrev != null) {
              try {
                compiler.setSessionId(statement, sessionIdPrev);

                final int[] counts = statement.executeBatch();
                total = aggregate(compiler, onNotifyCallbackListPrev, counts, statement, insertsWithGeneratedKeys, index, total);

                compiler.setSessionId(statement, null);

                index += counts.length;
                afterExecute(compilations, listenerIndex, statementIndex);
                onExecute(listenerIndex, statementIndex, counts);
                onCommit(transaction, listenerIndex, statementIndex, counts);
                listenerIndex = statementIndex;
              }
              finally {
                suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));
              }

              statement = connection.createStatement();
            }

            command.close();
            statement.addBatch(sql);
          }
        }

        Statement sessionStatement = null;
        if (isPrepared) {
          if (sessionIdPrev != null)
            compiler.setSessionId(sessionStatement = connection.createStatement(), sessionIdPrev);

          statement = preparedStatement;
        }
        else if (sessionIdPrev != null) {
          compiler.setSessionId(sessionStatement = statement, sessionIdPrev);
        }

        final int[] counts = statement.executeBatch();
        total = aggregate(compiler, onNotifyCallbackListPrev, counts, statement, insertsWithGeneratedKeys, index, total);

        if (sessionIdPrev != null)
          compiler.setSessionId(sessionStatement, null);

        index += counts.length;
        afterExecute(compilations, listenerIndex, noCommands);
        onExecute(listenerIndex, noCommands, counts);
        onCommit(transaction, listenerIndex, noCommands, counts);

        if (transaction != null)
          transaction.incUpdateCount(total);

        return new NotifiableBatchResult(total, onNotifyCallbackLists);
      }
      finally {
        SQLException e = Throwables.addSuppressed(preparedStatement != null ? AuditStatement.close(preparedStatement) : statement != null ? AuditStatement.close(statement) : null, suppressed);
        if (connector != null)
          e = Throwables.addSuppressed(e, AuditConnection.close(connection));

        if (e != null)
          throw e;
      }
    }
    catch (final SQLException e) {
      for (int statementIndex = 0; statementIndex < noCommands; ++statementIndex) // [RA]
        commands.get(statementIndex).revertEntity();

      throw SQLExceptions.toStrongType(e);
    }
  }

  @Override
  public final NotifiableBatchResult execute(final Transaction transaction) throws IOException, SQLException {
    return execute(transaction, null, null, false, null);
  }

  @Override
  public final NotifiableBatchResult execute(final Connector connector, final Transaction.Isolation isolation) throws IOException, SQLException {
    return execute(null, connector, null, false, isolation);
  }

  @Override
  public final NotifiableBatchResult execute(final Connector connector) throws IOException, SQLException {
    return execute(null, connector, null, false, null);
  }

  @Override
  public final NotifiableBatchResult execute(final Connection connection, final boolean isPrepared) throws IOException, SQLException {
    return execute(null, null, assertNotNull(connection), isPrepared, null);
  }

  @Override
  public final NotifiableBatchResult execute(final Transaction.Isolation isolation) throws IOException, SQLException {
    return execute(null, null, null, false, isolation);
  }

  @Override
  public NotifiableBatchResult execute() throws IOException, SQLException {
    return execute(null, null, null, false, null);
  }
}