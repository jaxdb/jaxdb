/* Copyright (c) 2017 OpenJAX
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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import org.openjax.rdb.vendor.DBVendor;

final class Compilation {
  private final StringBuilder builder = new StringBuilder();
  private final List<type.DataType<?>> parameters = new ArrayList<>();
  private final boolean prepared;
  private Consumer<Boolean> afterExecute;
  private boolean closed = false;

  protected final Stack<Command> command = new Stack<>();
  protected final DBVendor vendor;
  protected final Compiler compiler;

  private boolean skipFirstColumn = false;

  protected Compilation(final Command command, final DBVendor vendor, final boolean prepared) {
    this.command.add(command);
    this.vendor = vendor;
    this.prepared = prepared;
    this.compiler = Compiler.getCompiler(vendor);
  }

  protected void close() {
    closed = true;
  }

  protected boolean isPrepared() {
    return this.prepared;
  }

  protected String getSQL() {
    return builder.toString();
  }

  protected List<type.DataType<?>> getParameters() {
    return this.parameters;
  }

  protected boolean skipFirstColumn() {
    return skipFirstColumn;
  }

  protected void skipFirstColumn(final boolean skipFirstColumn) {
    this.skipFirstColumn = skipFirstColumn;
  }

  private final Map<type.Subject<?>,Alias> aliases = new IdentityHashMap<>();

  protected Alias registerAlias(final type.Subject<?> subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(aliases.size()));

    return alias;
  }

  protected Alias getAlias(final type.Subject<?> subject) {
    return aliases.get(subject);
  }

  protected StringBuilder append(final CharSequence seq) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    return builder.append(seq);
  }

  protected StringBuilder append(final char ch) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    return builder.append(ch);
  }

  protected void addCondition(final type.DataType<?> dataType, final boolean considerIndirection) throws IOException {
    append(vendor.getDialect().quoteIdentifier(dataType.name));
    if (dataType.get() == null) {
      append(" IS NULL");
    }
    else {
      append(" = ");
      addParameter(dataType, considerIndirection);
    }
  }

  protected void addParameter(final type.DataType<?> dataType, final boolean considerIndirection) throws IOException {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    if (considerIndirection && !dataType.wasSet() && dataType.indirection != null) {
      dataType.indirection.compile(this);
    }
    else if (prepared) {
      builder.append(Compiler.getCompiler(vendor).getPreparedStatementMark(dataType));
      parameters.add(dataType);
    }
    else {
      builder.append(dataType.compile(vendor));
    }
  }

  protected void afterExecute(final Consumer<Boolean> consumer) {
    this.afterExecute = this.afterExecute == null ? consumer : this.afterExecute.andThen(consumer);
  }

  protected void afterExecute(final boolean success) {
    if (this.afterExecute != null)
      this.afterExecute.accept(success);
  }

  protected ResultSet executeQuery(final Connection connection) throws IOException, SQLException {
    if (prepared) {
      final PreparedStatement statement = connection.prepareStatement(builder.toString());
      for (int i = 0; i < parameters.size(); ++i)
        parameters.get(i).get(statement, i + 1);

      return statement.executeQuery();
    }

    final java.sql.Statement statement = connection.createStatement();
    return statement.executeQuery(builder.toString());
  }

  protected int execute(final Connection connection) throws IOException, SQLException {
    if (prepared) {
//      if (batching) {
//        final IntArrayList results = new IntArrayList(statements.size());
//        PreparedStatement jdbcStatement = null;
//        String last = null;
//        for (int i = 0; i < statements.size(); ++i) {
//          final Statement statement = statements.get(i);
//          if (!statement.sql.equals(last)) {
//            if (jdbcStatement != null)
//              results.addAll(jdbcStatement.executeBatch());
//
//            jdbcStatement = connection.prepareStatement(statement.sql);
//            last = statement.sql;
//          }
//
//          for (int j = 0; j < statement.parameters.size(); ++j)
//            statement.parameters.get(j).get(jdbcStatement, j + 1);
//
//          jdbcStatement.addBatch();
//        }
//
//        if (jdbcStatement != null)
//          results.addAll(jdbcStatement.executeBatch());
//
//        return results.toArray();
//      }

        final PreparedStatement jdbcStatement = connection.prepareStatement(builder.toString());
        for (int j = 0; j < parameters.size(); ++j)
          parameters.get(j).get(jdbcStatement, j + 1);

      return jdbcStatement.executeUpdate();
    }

//    if (batching) {
//      final java.sql.Statement jdbcStatement = connection.createStatement();
//      for (int i = 0; i < statements.size(); ++i) {
//        final Statement statement = statements.get(i);
//        jdbcStatement.addBatch(statement.sql.toString());
//      }
//
//      return jdbcStatement.executeBatch();
//    }

//    final Statement batch = statements.get(i);
    final java.sql.Statement statement = connection.createStatement();
    return statement.executeUpdate(builder.toString());
//    }
//
//    return results;
  }
}