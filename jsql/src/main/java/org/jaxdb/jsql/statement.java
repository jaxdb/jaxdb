/* Copyright (c) 2015 JAX-DB
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

import static org.libj.logging.LoggerUtil.*;
import static org.slf4j.event.Level.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jaxdb.jsql.Callbacks.OnCommit;
import org.jaxdb.jsql.Callbacks.OnExecute;
import org.jaxdb.jsql.Callbacks.OnNotifies;
import org.jaxdb.jsql.Callbacks.OnNotify;
import org.jaxdb.jsql.Callbacks.OnRollback;
import org.jaxdb.jsql.statement.Modification.Result;
import org.jaxdb.jsql.statement.NotifiableModification.NotifiableResult;
import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Classes;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class statement {
  private static final Logger logger = LoggerFactory.getLogger(Modification.class);

  @SuppressWarnings({"null", "resource"})
  private static <D extends data.Entity<?>,E,C,R>Result execute(final boolean async, final Command.Modification<D,E,C,R> command, final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    logm(logger, TRACE, "executable.execute", "%b,%?,%?,%s", async, command, transaction, dataSourceId);
    final Connection connection;
    final Connector connector;
    Statement statement = null;
    Compilation compilation = null;
    SQLException suppressed = null;

    final data.Column<?>[] autos = command instanceof Command.Insert && ((Command.Insert<?>)command).autos.length > 0 ? ((Command.Insert<?>)command).autos : null;
    try {
      final boolean isPrepared;
      if (transaction != null) {
        connector = transaction.getConnector();
        connection = transaction.getConnection();
        isPrepared = transaction.isPrepared();

        transaction.setCallbacks(command.callbacks);
        command.getCallbacks().getOnCommits().add(0, c -> {
          command.onCommit(connector, connection);
        });
      }
      else {
        connector = Database.getConnector(command.schemaClass(), dataSourceId);
        connection = connector.getConnection();
        connection.setAutoCommit(true);
        isPrepared = connector.isPrepared();
      }

      final String sessionId = command.sessionId;
      final OnNotifies onNotifies = async && command.callbacks != null && command.callbacks.onNotifys != null ? command.callbacks.onNotifys.get(sessionId) : null;
      if (onNotifies != null)
        connector.getSchema().awaitNotify(sessionId, onNotifies);

      compilation = new Compilation(command, DBVendor.valueOf(connection.getMetaData()), isPrepared);
      command.compile(compilation, false);
      try {
        final int count;
        final ResultSet resultSet;
        if (compilation.isPrepared()) {
          // FIXME: Implement batching.
          // if (batching) {
          // final IntArrayList results = new IntArrayList(statements.size());
          // PreparedStatement jdbcStatement = null;
          // String last = null;
          // for (int i = 0, i$ = statements.size(); i < i$; ++i) { // [RA]
          // final Statement statement = statements.get(i);
          // if (!statement.sql.equals(last)) {
          // if (jdbcStatement != null)
          // results.addAll(jdbcStatement.executeBatch());
          //
          // jdbcStatement = connection.prepareStatement(statement.sql);
          // last = statement.sql;
          // }
          //
          // for (int j = 0, i$ = statement.parameters.size(); j < i$; ++j) // [RA]
          // statement.parameters.get(j).get(jdbcStatement, j + 1);
          //
          // jdbcStatement.addBatch();
          // }
          //
          // if (jdbcStatement != null)
          // results.addAll(jdbcStatement.executeBatch());
          //
          // return results.toArray();
          // }

          final String sql = compilation.toString();
          final PreparedStatement preparedStatement = autos == null ? connection.prepareStatement(sql) : compilation.compiler.prepareStatementReturning(connection, sql, autos);
          statement = preparedStatement;
          final ArrayList<data.Column<?>> parameters = compilation.getParameters();
          if (parameters != null) {
            final int updateWhereIndex = compilation.getUpdateWhereIndex();
            for (int p = 0, i$ = parameters.size(); p < i$;) // [RA]
              parameters.get(p).setParameter(preparedStatement, p >= updateWhereIndex, ++p);
          }

          compilation.setSessionId(connection, statement, sessionId);

          try {
            count = preparedStatement.executeUpdate();
            resultSet = autos == null ? null : preparedStatement.getGeneratedKeys();
          }
          catch (final Exception e) {
            // FIXME: Why am I doing this a second time here in the catch block?
            if (parameters != null) {
              final int updateWhereIndex = compilation.getUpdateWhereIndex();
              for (int p = 0, i$ = parameters.size(); p < i$;) // [RA]
                parameters.get(p).setParameter(preparedStatement, p >= updateWhereIndex, ++p);
            }

            if (e instanceof SQLException)
              throw SQLExceptions.toStrongType((SQLException)e);

            throw e;
          }
        }
        else {
          // FIXME: Implement batching.
          // if (batching) {
          //   final Statement jdbcStatement = connection.createStatement();
          //   for (int i = 0, i$ = statements.size(); i < i$; ++i) { // [RA]
          //     final Statement statement = statements.get(i);
          //     jdbcStatement.addBatch(statement.sql.toString());
          // }
          //
          // return jdbcStatement.executeBatch();
          // }

          // final Statement batch = statements.get(i);
          statement = connection.createStatement();

          compilation.setSessionId(connection, statement, sessionId);

          final String sql = compilation.toString();
          if (autos == null) {
            count = statement.executeUpdate(sql);
            resultSet = null;
          }
          else {
            count = compilation.compiler.executeUpdateReturning(statement, sql, autos);
            resultSet = statement.getGeneratedKeys();
          }
          // }
          //
          // return results;
        }

        compilation.afterExecute(true); // FIXME: This invokes the GenerateOn evaluation of dynamic values, and is happening after notifyListeners(EXECUTE) .. should it happen before? or keep it as is, so it happens after EXECUTE, but before COMMIT

        if (transaction != null)
          transaction.onExecute(sessionId, count);
        else if (command.callbacks != null)
          command.callbacks.onExecute(sessionId, count);

        if (resultSet != null) {
          while (resultSet.next()) {
            for (int i = 0, i$ = autos.length; i < i$;) { // [A]
              final data.Column<?> auto = autos[i++];
              if (!auto._mutable$)
                throw new IllegalArgumentException(Classes.getCanonicalCompositeName(auto.getClass()) + " bound to " + auto.getTable().getName() + "." + auto.name + " must be mutable to accept auto-generated values");

              auto.getParameter(resultSet, i);
            }
          }
        }

        if (transaction == null) {
          command.onCommit(connector, connection);
          if (command.callbacks != null)
            command.callbacks.onCommit(count);
        }

        return async ? new NotifiableResult(count, onNotifies) : new Result(count);
      }
      finally {
        if (statement != null)
          suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));

        if (transaction == null)
          suppressed = Throwables.addSuppressed(suppressed, AuditConnection.close(connection));
      }
    }
    catch (final SQLException e) {
      if (compilation != null) {
        compilation.afterExecute(false);
        compilation.close();
      }

      Throwables.addSuppressed(e, suppressed);
      throw SQLExceptions.toStrongType(e);
    }
  }

  public interface Modification {
    public interface Executable<T> extends statement.Modification {
      T onExecute(OnExecute onExecute);
    }

    public interface Committable<T extends statement.Modification> extends statement.Modification {
      /**
       * Sets an {@link OnCommit} predicate to be called immediately after {@link Connection#commit()} is called.
       *
       * @param onCommit The {@link OnCommit} predicate.
       * @return {@code this} statement.
       * @see Rollbackable#onRollback(OnRollback)
       * @see NotifiableModification.Notifiable#onNotify(OnNotify)
       */
      T onCommit(OnCommit onCommit);
    }

    public interface Rollbackable<T extends statement.Modification> extends statement.Modification {
      /**
       * Sets an {@link OnRollback} predicate to be called immediately after {@link Connection#rollback()} is called.
       *
       * @param onRollback The {@link OnRollback} predicate.
       * @return {@code this} statement.
       * @see Committable#onCommit(OnCommit)
       * @see NotifiableModification.Notifiable#onNotify(OnNotify)
       */
      T onRollback(OnRollback onRollback);
    }

    default Result execute(final String dataSourceId) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?,?>)this, null, dataSourceId);
    }

    default Result execute(final Transaction transaction) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?,?>)this, transaction, transaction == null ? null : transaction.getDataSourceId());
    }

    default Result execute() throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?,?>)this, null, null);
    }

    public interface Delete extends Modification {
    }

    public interface Update extends Modification {
    }

    public interface Insert extends Modification {
    }

    public static class Result {
      private final int count;

      Result(final int count) {
        this.count = count;
      }

      public int getCount() {
        return this.count;
      }

      @Override
      public String toString() {
        return "{\"count\":" + count + "}";
      }
    }
  }

  public interface NotifiableModification extends Modification {
    public interface Notifiable<T extends statement.NotifiableModification> extends statement.Modification {
      public interface Static<T extends statement.NotifiableModification> extends statement.Modification {
        /**
         * Sets a static value to the be returned in place of a {@link OnNotify} predicate for each notification generated by the DB
         * as a result of {@code this} statement. Since the notifications generated by the DB are asynchronous,
         * {@link NotifiableResult#awaitNotify(long)} can be used to block the current thread until:
         * <ul>
         * <li>the {@code onNotify} argument is {@code false}.</li>
         * <li>the receipt of all notifications generated by the DB as a result of {@code this} statement, or</li>
         * </ul>
         *
         * @param onNotify The {@link OnNotify} predicate.
         * @return {@code this} statement.
         * @see Committable#onCommit(OnCommit)
         * @see Rollbackable#onRollback(OnRollback)
         * @see #onNotify(OnNotify)
         * @see NotifiableResult#awaitNotify(long)
         */
        T onNotify(boolean onNotify);
      }

      /**
       * Sets an {@link OnNotify} predicate to be called for each notification generated by the DB as a result of {@code this}
       * statement. Since the notifications generated by the DB are asynchronous, {@link NotifiableResult#awaitNotify(long)} can be
       * used to block the current thread until:
       * <ul>
       * <li>the return from {@link Callbacks.OnNotify OnNotify#test(Exception,int,int)} is {@code false}.</li>
       * <li>the receipt of all notifications generated by the DB as a result of {@code this} statement, or</li>
       * </ul>
       *
       * @param onNotify The {@link OnNotify} predicate.
       * @return {@code this} statement.
       * @see Committable#onCommit(OnCommit)
       * @see Rollbackable#onRollback(OnRollback)
       * @see Static#onNotify(boolean)
       * @see NotifiableResult#awaitNotify(long)
       */
      T onNotify(OnNotify onNotify);
    }

    @Override
    default NotifiableResult execute(final String dataSourceId) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?,?>)this, null, dataSourceId);
    }

    @Override
    default NotifiableResult execute(final Transaction transaction) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?,?>)this, transaction, transaction == null ? null : transaction.getDataSourceId());
    }

    @Override
    default NotifiableResult execute() throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?,?>)this, null, null);
    }

    public interface Delete extends NotifiableModification {
    }

    public interface Update extends NotifiableModification {
    }

    public interface Insert extends NotifiableModification {
    }

    public static class NotifiableResult extends Result {
      private final OnNotifies onNotifies;

      NotifiableResult(final int count, final OnNotifies onNotifies) {
        super(count);
        this.onNotifies = onNotifies;
      }

      public boolean awaitNotify(final long timeout) throws InterruptedException {
        return onNotifies == null || onNotifies.await(timeout);
      }
    }
  }

  public interface Query<D extends data.Entity<?>> {
    RowIterator<D> execute(String dataSourceId) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector) throws IOException, SQLException;
    RowIterator<D> execute(Connection connection) throws IOException, SQLException;
    RowIterator<D> execute(Transaction transaction) throws IOException, SQLException;
    RowIterator<D> execute() throws IOException, SQLException;

    RowIterator<D> execute(String dataSourceId, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Connection connection, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Transaction transaction, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(QueryConfig config) throws IOException, SQLException;
  }

  private statement() {
  }
}