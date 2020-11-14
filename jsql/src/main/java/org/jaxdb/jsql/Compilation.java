/* Copyright (c) 2017 JAX-DB
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.function.Consumer;

import org.jaxdb.vendor.DBVendor;

final class Compilation {
  private final StringBuilder builder = new StringBuilder();
  private final List<type.DataType<?>> parameters = new ArrayList<>();
  private final boolean prepared;
  private Consumer<Boolean> afterExecute;
  private boolean closed;

  final Stack<Command> command = new Stack<>();
  final DBVendor vendor;
  final Compiler compiler;

  private boolean skipFirstColumn;

  Compilation(final Command command, final DBVendor vendor, final boolean prepared) {
    this.command.add(command);
    this.vendor = vendor;
    this.prepared = prepared;
    this.compiler = Compiler.getCompiler(vendor);
  }

  void close() {
    closed = true;
  }

  boolean isPrepared() {
    return this.prepared;
  }

  String getSQL() {
    return builder.toString();
  }

  List<type.DataType<?>> getParameters() {
    return this.parameters;
  }

  boolean skipFirstColumn() {
    return skipFirstColumn;
  }

  void skipFirstColumn(final boolean skipFirstColumn) {
    this.skipFirstColumn = skipFirstColumn;
  }

  private final Map<type.Subject<?>,Alias> aliases = new IdentityHashMap<>();

  Alias registerAlias(final type.Subject<?> subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(aliases.size()));

    return alias;
  }

  Alias getAlias(final type.Subject<?> subject) {
    return aliases.get(subject);
  }

  StringBuilder append(final Object object) {
    return append(object.toString());
  }

  StringBuilder append(final CharSequence seq) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    return builder.append(seq);
  }

  StringBuilder append(final char ch) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    return builder.append(ch);
  }

  void addCondition(final type.DataType<?> dataType, final boolean considerIndirection) throws IOException {
    append(vendor.getDialect().quoteIdentifier(dataType.name));
    if (dataType.get() == null) {
      append(" IS NULL");
    }
    else {
      append(" = ");
      addParameter(dataType, considerIndirection);
    }
  }

  void addParameter(final type.DataType<?> dataType, final boolean considerIndirection) throws IOException {
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

  void afterExecute(final Consumer<Boolean> consumer) {
    this.afterExecute = this.afterExecute == null ? consumer : this.afterExecute.andThen(consumer);
  }

  void afterExecute(final boolean success) {
    if (this.afterExecute != null)
      this.afterExecute.accept(success);
  }

  static PreparedStatement configure(final Connection connection, final QueryConfig config, final String sql) throws SQLException {
    if (config == null)
      return connection.prepareStatement(sql);

    if (config.getHoldability() == null)
      return config.apply(connection.prepareStatement(sql, config.getType().index, config.getConcurrency().index));

    return config.apply(connection.prepareStatement(sql, config.getType().index, config.getConcurrency().index, config.getHoldability().index));
  }

  static Statement configure(final Connection connection, final QueryConfig config) throws SQLException {
    if (config == null)
      return connection.createStatement();

    if (config.getHoldability() == null)
      return config.apply(connection.createStatement(config.getType().index, config.getConcurrency().index));

    return config.apply(connection.createStatement(config.getType().index, config.getConcurrency().index, config.getHoldability().index));
  }

  ResultSet executeQuery(final Connection connection, final QueryConfig config) throws IOException, SQLException {
    if (prepared) {
      final PreparedStatement statement = configure(connection, config, builder.toString());

      for (int i = 0; i < parameters.size(); ++i)
        parameters.get(i).get(statement, i + 1);

      return statement.executeQuery();
    }

    return configure(connection, config).executeQuery(builder.toString());
  }
}