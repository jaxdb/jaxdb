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

import static org.libj.lang.Assertions.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import org.jaxdb.jsql.Callbacks.OnCommit;
import org.jaxdb.jsql.Callbacks.OnExecute;
import org.jaxdb.jsql.Callbacks.OnNotify;
import org.jaxdb.jsql.Callbacks.OnNotifyCallbackList;
import org.jaxdb.jsql.Callbacks.OnRollback;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.Classes;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;

public final class statement {
  @SuppressWarnings({"null", "resource"})
  private static <D extends type.Entity,E,C,R>Modification.Result execute(final boolean async, final Command.Modification<E,C,R> command, final Transaction transaction, final Connector connector, Connection connection, boolean isPrepared, final Transaction.Isolation isolation) throws IOException, SQLException {
    command.assertNotClosed();

    Statement statement = null;
    Compilation compilation = null;
    SQLException suppressed = null;

    final data.Column<?>[] autos = command instanceof Command.Insert && ((Command.Insert<?>)command).autos.length > 0 ? ((Command.Insert<?>)command).autos : null;
    try {
      final Schema schema = command.getSchema();
      final String sessionId = command.sessionId;
      if (connector != null) {
        isPrepared = connector.isPrepared();
        connection = connector.getConnection(isolation);
        connection.setAutoCommit(true);
      }
      else if (transaction != null) {
        isPrepared = transaction.isPrepared();
        connection = transaction.getConnection();
        transaction.addCallbacks(command.callbacks);
      }

      final DbVendor vendor = DbVendor.valueOf(connection.getMetaData());
      compilation = new Compilation(command, vendor, isPrepared);
      command.compile(compilation, false);

      final OnNotifyCallbackList onNotifyCallbackList;
      if (sessionId != null) {
        onNotifyCallbackList = async && command.callbacks != null && command.callbacks.onNotifys != null ? command.callbacks.onNotifys.get(sessionId) : null;
        if (onNotifyCallbackList != null) {
          schema.awaitNotify(sessionId, onNotifyCallbackList);

          if (transaction != null)
            transaction.onNotify(onNotifyCallbackList);
        }
      }
      else {
        onNotifyCallbackList = null;
      }

      final int count;
      try {
        final ResultSet resultSet;
        final Compiler compiler = compilation.compiler;
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

          final PreparedStatement preparedStatement = autos == null ? connection.prepareStatement(compilation.toString()) : compiler.prepareStatementReturning(connection, compilation.sql, autos);
          statement = preparedStatement;
          final ArrayList<data.Column<?>> parameters = compilation.getParameters();
          if (parameters != null) {
            final int updateWhereIndex = compilation.getUpdateWhereIndex();
            for (int p = 0, i$ = parameters.size(); p < i$;) // [RA]
              parameters.get(p).write(compiler, preparedStatement, p >= updateWhereIndex, ++p);
          }

          command.close();

          Statement sessionStatement = null;
          if (sessionId != null)
            compiler.setSessionId(sessionStatement = connection.createStatement(), sessionId);

          try {
            count = preparedStatement.executeUpdate();
            if (onNotifyCallbackList != null)
              onNotifyCallbackList.count.set(count);

            if (sessionId != null)
              compiler.setSessionId(sessionStatement, null);

            resultSet = autos == null ? null : preparedStatement.getGeneratedKeys();
          }
          catch (final Exception e) {
            // FIXME: Why am I doing this a second time here in the catch block?
            if (parameters != null) {
              final int updateWhereIndex = compilation.getUpdateWhereIndex();
              for (int p = 0, i$ = parameters.size(); p < i$;) // [RA]
                parameters.get(p).write(compiler, preparedStatement, p >= updateWhereIndex, ++p);
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

          command.close();

          if (sessionId != null)
            compiler.setSessionId(statement, sessionId);

          if (autos == null) {
            count = statement.executeUpdate(compilation.toString());
            if (onNotifyCallbackList != null)
              onNotifyCallbackList.count.set(count);

            if (sessionId != null)
              compiler.setSessionId(statement, null);

            resultSet = null;
          }
          else {
            count = compiler.executeUpdateReturning(statement, compilation.sql, autos);
            resultSet = statement.getGeneratedKeys();
          }
          // }
          //
          // return results;
        }

        compilation.afterExecute(true); // FIXME: This invokes the GenerateOn evaluation of dynamic values, and is happening after notifyListeners(EXECUTE) .. should it happen before? or keep it as is, so it happens after EXECUTE, but before COMMIT

        if (transaction != null)
          transaction.incUpdateCount(count);

        if (command.callbacks != null)
          command.callbacks.onExecute(count);

        if (resultSet != null) {
          while (resultSet.next()) {
            for (int i = 0, i$ = autos.length; i < i$;) { // [A]
              final data.Column<?> auto = autos[i++];
              if (!auto._mutable$)
                throw new IllegalArgumentException(Classes.getCanonicalCompositeName(auto.getClass()) + " bound to " + auto.getTable().getName() + "." + auto.name + " must be mutable to accept auto-generated values");

              auto.read(compiler, resultSet, i);
            }
          }
        }

        if (transaction == null && command.callbacks != null)
          command.callbacks.onCommit(count);
      }
      finally {
        if (statement != null)
          suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));

        if (connector != null)
          suppressed = Throwables.addSuppressed(suppressed, AuditConnection.close(connection));
      }

      return async ? new NotifiableModification.NotifiableResult(count) {
        private String[] sessionIds;

        @Override
        String[] getSessionId() {
          return sessionIds == null ? sessionIds = new String[] {sessionId} : sessionIds;
        }

        @Override
        public boolean awaitNotify(final long timeout) throws InterruptedException  {
          return onNotifyCallbackList == null || onNotifyCallbackList.await(timeout);
        }
      } : new Modification.Result(count);
    }
    catch (final SQLException e) {
      command.revertEntity();

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
       * @see statement.NotifiableModification.Notifiable#onNotify(OnNotify)
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
       * @see statement.NotifiableModification.Notifiable#onNotify(OnNotify)
       */
      T onRollback(OnRollback onRollback);
    }

    default Result execute(final Transaction transaction) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?>)this, assertNotNull(transaction), null, null, false, null);
    }

    default Result execute(final Connector connector, final Transaction.Isolation isolation) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?>)this, null, assertNotNull(connector), null, false, isolation);
    }

    default Result execute(final Connector connector) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?>)this, null, assertNotNull(connector), null, false, null);
    }

    default Result execute(final Connection connection, final boolean isPrepared) throws IOException, SQLException {
      return statement.execute(false, (Command.Modification<?,?,?>)this, null, null, assertNotNull(connection), isPrepared, null);
    }

    default Result execute(final Transaction.Isolation isolation) throws IOException, SQLException {
      final Command.Modification<?,?,?> command = (Command.Modification<?,?,?>)this;
      return statement.execute(false, command, null, command.getSchema().getConnector(), null, false, isolation);
    }

    default Result execute() throws IOException, SQLException {
      final Command.Modification<?,?,?> command = (Command.Modification<?,?,?>)this;
      return statement.execute(false, command, null, command.getSchema().getConnector(), null, false, null);
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
        return count;
      }

      @Override
      public String toString() {
        return "{\"count\":" + getCount() + "}";
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
         * <li>the receipt of all notifications generated by the DB as a result of {@code this} statement</li>
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
       * <li>the receipt of all notifications generated by the DB as a result of {@code this} statement</li>
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
    default NotifiableResult execute(final Transaction transaction) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?>)this, assertNotNull(transaction), null, null, false, null);
    }

    @Override
    default NotifiableResult execute(final Connector connector, final Transaction.Isolation isolation) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?>)this, null, assertNotNull(connector), null, false, isolation);
    }

    @Override
    default NotifiableResult execute(final Connector connector) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?>)this, null, assertNotNull(connector), null, false, null);
    }

    @Override
    default NotifiableResult execute(final Connection connection, boolean isPrepared) throws IOException, SQLException {
      return (NotifiableResult)statement.execute(true, (Command.Modification<?,?,?>)this, null, null, assertNotNull(connection), isPrepared, null);
    }

    @Override
    default NotifiableResult execute(final Transaction.Isolation isolation) throws IOException, SQLException {
      final Command.Modification<?,?,?> command = (Command.Modification<?,?,?>)this;
      return (NotifiableResult)statement.execute(true, command, null, command.getSchema().getConnector(), null, false, isolation);
    }

    @Override
    default NotifiableResult execute() throws IOException, SQLException {
      final Command.Modification<?,?,?> command = (Command.Modification<?,?,?>)this;
      return (NotifiableResult)statement.execute(true, command, null, command.getSchema().getConnector(), null, false, null);
    }

    public interface Delete extends statement.Modification.Delete, NotifiableModification {
    }

    public interface Update extends statement.Modification.Update, NotifiableModification {
    }

    public interface Insert extends statement.Modification.Insert, NotifiableModification {
    }

    public abstract static class NotifiableResult extends Result {
      NotifiableResult(final int count) {
        super(count);
      }

      abstract String[] getSessionId();
      public abstract boolean awaitNotify(final long timeout) throws InterruptedException;
    }

    public static class NotifiableBatchResult extends NotifiableResult {
      private final ArrayList<OnNotifyCallbackList> onNotifyCallbackLists;
      private String[] sessionIds;

      NotifiableBatchResult(final int count, final ArrayList<OnNotifyCallbackList> onNotifyCallbackLists) {
        super(count);
        this.onNotifyCallbackLists = onNotifyCallbackLists;
      }

      @Override
      String[] getSessionId() {
        if (sessionIds != null || onNotifyCallbackLists == null)
          return sessionIds;

        sessionIds = new String[onNotifyCallbackLists.size()]; // Note: size() should always be more than 0 here
        for (int i = 0, i$ = onNotifyCallbackLists.size(); i < i$; ++i) // [RA]
          sessionIds[i] = onNotifyCallbackLists.get(i).sessionId;

        return sessionIds;
      }

      @Override
      public boolean awaitNotify(long timeout) throws InterruptedException {
        if (onNotifyCallbackLists == null)
          return true;

        long ts = System.currentTimeMillis();
        for (int i = 0, i$ = onNotifyCallbackLists.size(); i < i$; ++i, timeout -= System.currentTimeMillis() - ts) // [RA]
          if (!onNotifyCallbackLists.get(i).await(timeout))
            return false;

        return true;
      }
    }
  }

  public interface Query<D extends type.Entity> {
    RowIterator<D> execute(Transaction.Isolation isolation) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector, Transaction.Isolation isolation) throws IOException, SQLException;
    RowIterator<D> execute(Connection connection, boolean isPrepared) throws IOException, SQLException;
    RowIterator<D> execute(Transaction transaction) throws IOException, SQLException;
    RowIterator<D> execute() throws IOException, SQLException;

    RowIterator<D> execute(Transaction.Isolation isolation, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Connector connector, Transaction.Isolation isolation, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Connection connection, boolean isPrepared, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Transaction transaction, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(QueryConfig config) throws IOException, SQLException;
  }

  private statement() {
  }
}