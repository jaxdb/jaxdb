/* Copyright (c) 2018 OpenJAX
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

package org.openjax.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.openjax.classic.sql.exception.SQLExceptions;

abstract class BatchableKeyword<T extends type.Subject<?>> extends Keyword<T> implements ExecuteUpdate {
  protected BatchableKeyword(final BatchableKeyword<T> parent) {
    super(parent);
  }

  private int execute(final Transaction transaction, final String dataSourceId) throws IOException, SQLException {
    Compilation compilation = null;
    try {
      final Command command = normalize();
      final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(command.getSchema(), dataSourceId);
      compilation = new Compilation(command, Schema.getDBVendor(connection), Registry.isPrepared(command.getSchema()));
      command.compile(compilation);
      final int count = compilation.execute(connection);
      compilation.afterExecute(true);
      if (transaction == null)
        connection.close();

      return count;
    }
    catch (final SQLException e) {
      if (compilation != null)
        compilation.afterExecute(false);

      throw SQLExceptions.getStrongType(e);
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