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

import org.jaxdb.jsql.Listener.OnCommit;
import org.jaxdb.jsql.Listener.OnExecute;
import org.jaxdb.jsql.Listener.OnNotifies;
import org.jaxdb.jsql.Listener.OnNotify;
import org.jaxdb.jsql.Listener.OnRollback;
import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Classes;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Executable {
  private static final Logger logger = LoggerFactory.getLogger(Executable.class);

  @SuppressWarnings({"null", "resource"})
  private static <D extends data.Entity<?>,T extends Executable.Modify>int execute(final Command.Modify<D,T> command, final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    logm(logger, TRACE, "Executable.execute", "%?,%?,%s", command, transaction, dataSourceId);
    final Connection connection;
    final Connector connector;
    Statement statement = null;
    Compilation compilation = null;
    SQLException suppressed = null;

    final String sessionId = command.sessionId;

    final data.Column<?>[] autos = command instanceof Command.Insert && ((Command.Insert<?>)command).autos.length > 0 ? ((Command.Insert<?>)command).autos : null;
    try {
      final boolean isPrepared;
      if (transaction != null) {
        connector = transaction.getConnector();
        connection = transaction.getConnection();
        isPrepared = transaction.isPrepared();

        transaction.setListeners(command.listeners);
        command.getListeners().getCommit().add(0, c -> {
          command.onCommit(connector, connection);
        });
      }
      else {
        connector = Database.getConnector(command.schemaClass(), dataSourceId);
        connection = connector.getConnection();
        connection.setAutoCommit(true);
        isPrepared = connector.isPrepared();
      }

      final OnNotifies onNotifies = command.listeners == null || command.listeners.notify == null ? null : command.listeners.notify.get(sessionId);
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
        else if (command.listeners != null)
          command.listeners.onExecute(sessionId, count);

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
          if (command.listeners != null) {
            try {
              command.listeners.onCommit(count);
              if (onNotifies != null)
                onNotifies.await();
            }
            finally {
              if (command.listeners != null)
                command.listeners.clear();
            }
          }
        }

        return count;
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

  public interface Listenable<T> {
    T onExecute(OnExecute listener);
  }

  public interface Modify {
    public interface Listenable<T extends Executable.Modify> extends Executable.Listenable<T>, Executable.Modify {
      T onCommit(OnCommit listener);
      T onRollback(OnRollback listener);
      T onNotify(long timeout, OnNotify listener);
      T onNotify(long timeout);
      T onNotify(OnNotify listener);
    }

    default int execute(final String dataSourceId) throws IOException, SQLException {
      return Executable.execute((Command.Modify<?,?>)this, null, dataSourceId);
    }

    default int execute(final Transaction transaction) throws IOException, SQLException {
      return Executable.execute((Command.Modify<?,?>)this, transaction, transaction == null ? null : transaction.getDataSourceId());
    }

    default int execute() throws IOException, SQLException {
      return Executable.execute((Command.Modify<?,?>)this, null, null);
    }

    public interface Delete extends Modify {
    }

    public interface Update extends Modify {
    }

    public interface Insert extends Modify {
    }
  }

  private Executable() {
  }
}