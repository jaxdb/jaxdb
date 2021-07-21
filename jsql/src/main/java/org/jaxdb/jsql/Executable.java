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

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;

public final class Executable {
  @SuppressWarnings("resource")
  private static <D extends data.Entity<?>>int execute(final org.jaxdb.jsql.Command<D> command, final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    Compilation compilation = null;
    Connection connection = null;
    java.sql.Statement statement = null;
    SQLException suppressed = null;
    final data.Column<?>[] autos = command instanceof InsertImpl && ((InsertImpl<?>)command).autos.length > 0 ? ((InsertImpl<?>)command).autos : null;
    try {
      connection = transaction != null ? transaction.getConnection() : Schema.getConnection(command.schema(), dataSourceId, true);
      compilation = new Compilation(command, DBVendor.valueOf(connection.getMetaData()), Registry.isPrepared(command.schema(), dataSourceId));
      command.compile(compilation, false);
//      final type.Column<?>[] returning = getReturning();
      try {
        final int count;
        final ResultSet resultSet;
        if (compilation.isPrepared()) {
          // FIXME: Implement batching.
          // if (batching) {
          // final IntArrayList results = new IntArrayList(statements.size());
          // PreparedStatement jdbcStatement = null;
          // String last = null;
          // for (int i = 0, len = statements.size(); i < len; ++i) {
          // final Statement statement = statements.get(i);
          // if (!statement.sql.equals(last)) {
          // if (jdbcStatement != null)
          // results.addAll(jdbcStatement.executeBatch());
          //
          // jdbcStatement = connection.prepareStatement(statement.sql);
          // last = statement.sql;
          // }
          //
          // for (int j = 0, len = statement.parameters.size(); j < len; ++j)
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
          final List<data.Column<?>> parameters = compilation.getParameters();
          if (parameters != null)
            for (int i = 0, len = parameters.size(); i < len;)
              parameters.get(i).get(preparedStatement, ++i);

          try {
            count = preparedStatement.executeUpdate();
            resultSet = autos == null ? null : preparedStatement.getGeneratedKeys();
          }
          catch (final Exception e) {
            // FIXME: Why am I doing this a second time here in the catch block?
            if (parameters != null)
              for (int i = 0, len = parameters.size(); i < len;)
                parameters.get(i).get(preparedStatement, ++i);

            if (e instanceof SQLException)
              throw SQLExceptions.toStrongType((SQLException)e);

            throw e;
          }
        }
        else {
          // FIXME: Implement batching.
          // if (batching) {
          // final java.sql.Statement jdbcStatement =
          // connection.createStatement();
          // for (int i = 0, len = statements.size(); i < len; ++i) {
          // final Statement statement = statements.get(i);
          // jdbcStatement.addBatch(statement.sql.toString());
          // }
          //
          // return jdbcStatement.executeBatch();
          // }

          // final Statement batch = statements.get(i);
          statement = connection.createStatement();
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

        compilation.afterExecute(true);
        if (resultSet != null) {
          while (resultSet.next()) {
            for (int i = 0, len = autos.length; i < len;) {
              autos[i].set(resultSet, ++i);
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
    RowIterator<D> execute(Transaction transaction) throws IOException, SQLException;
    RowIterator<D> execute() throws IOException, SQLException;

    RowIterator<D> execute(String dataSourceId, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(Transaction transaction, QueryConfig config) throws IOException, SQLException;
    RowIterator<D> execute(QueryConfig config) throws IOException, SQLException;
  }

  public interface Modify extends AutoCloseable {
    default int execute(final String dataSourceId) throws IOException, SQLException {
      return Executable.execute((org.jaxdb.jsql.Command<?>)this, null, dataSourceId);
    }

    default int execute(final Transaction transaction) throws IOException, SQLException {
      return Executable.execute((org.jaxdb.jsql.Command<?>)this, transaction, transaction == null ? null : transaction.getDataSourceId());
    }

    default int execute() throws IOException, SQLException {
      return Executable.execute((org.jaxdb.jsql.Command<?>)this, null, null);
    }

    @Override
    void close();

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