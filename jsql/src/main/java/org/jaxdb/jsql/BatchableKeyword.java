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
import java.util.List;

import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.exception.SQLExceptions;

abstract class BatchableKeyword<T extends type.Subject<?>> extends Keyword<T> implements ExecuteUpdate {
  BatchableKeyword(final BatchableKeyword<T> parent) {
    super(parent);
  }

  @SuppressWarnings("resource")
  private int execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    Compilation compilation = null;
    Connection connection = null;
    Statement statement = null;
    SQLException suppressed = null;
    try {
      final Command<?> command = normalize();
      connection = transaction != null ? transaction.getConnection() : Schema.getConnection(command.getSchema(), dataSourceId, true);
      compilation = new Compilation(command, Schema.getDBVendor(connection), Registry.isPrepared(command.getSchema()));
      command.compile(compilation);
      try {
        final int count;
        if (compilation.isPrepared()) {
          // FIXME: Implement batching.
          // if (batching) {
          // final IntArrayList results = new IntArrayList(statements.size());
          // PreparedStatement jdbcStatement = null;
          // String last = null;
          // for (int i = 0; i < statements.size(); ++i) {
          // final Statement statement = statements.get(i);
          // if (!statement.sql.equals(last)) {
          // if (jdbcStatement != null)
          // results.addAll(jdbcStatement.executeBatch());
          //
          // jdbcStatement = connection.prepareStatement(statement.sql);
          // last = statement.sql;
          // }
          //
          // for (int j = 0; j < statement.parameters.size(); ++j)
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

          final PreparedStatement preparedStatement = connection.prepareStatement(compilation.getSQL().toString());
          statement = preparedStatement;
          final List<type.DataType<?>> parameters = compilation.getParameters();
          if (parameters != null)
            for (int j = 0; j < parameters.size(); ++j)
              parameters.get(j).get(preparedStatement, j + 1);

          count = preparedStatement.executeUpdate();
        }
        else {
          // FIXME: Implement batching.
          // if (batching) {
          // final java.sql.Statement jdbcStatement =
          // connection.createStatement();
          // for (int i = 0; i < statements.size(); ++i) {
          // final Statement statement = statements.get(i);
          // jdbcStatement.addBatch(statement.sql.toString());
          // }
          //
          // return jdbcStatement.executeBatch();
          // }

          // final Statement batch = statements.get(i);
          statement = connection.createStatement();
          count = statement.executeUpdate(compilation.getSQL().toString());
          // }
          //
          // return results;
        }

        compilation.afterExecute(true);
        return count;
      }
      finally {
        if (statement != null)
          suppressed = Throwables.addSuppressed(suppressed, AuditStatement.close(statement));

        if (transaction == null && connection != null)
          suppressed = Throwables.addSuppressed(suppressed, AuditConnection.close(connection));
      }
    }
    catch (final SQLException e) {
      if (compilation != null) {
        compilation.afterExecute(false);
        compilation.close();
      }

      throw SQLExceptions.toStrongType(e);
    }
  }

  @Override
  public final int execute(final String dataSourceId) throws IOException, SQLException {
    return execute(null, dataSourceId);
  }

  @Override
  public final int execute(final Transaction transaction) throws IOException, SQLException {
    return execute(transaction, transaction == null ? null : transaction.getDataSourceId());
  }

  @Override
  public final int execute() throws IOException, SQLException {
    return execute(null, null);
  }
}