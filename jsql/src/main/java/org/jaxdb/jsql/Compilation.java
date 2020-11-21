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
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import org.jaxdb.vendor.DBVendor;

final class Compilation {
  private final StringBuilder builder = new StringBuilder();
  private List<type.DataType<?>> parameters;
  private final boolean prepared;
  private Consumer<Boolean> afterExecute;
  private boolean closed;

  final Command<?> command;
  final DBVendor vendor;
  final Compiler compiler;
  private final Compilation parent;

  private boolean skipFirstColumn;

  private Map<Command<?>,Compilation> subCompilations;

  Compilation(final Command<?> command, final DBVendor vendor, final boolean prepared) {
    this(command, vendor, prepared, null);
  }

  private Compilation(final Command<?> command, final DBVendor vendor, final boolean prepared, final Compilation parent) {
    this.command = command;
    this.vendor = vendor;
    this.prepared = prepared;
    this.compiler = Compiler.getCompiler(vendor);
    this.parent = parent;
    if (parent != null)
      this.parameters = parent.parameters;
  }

  Compilation newSubCompilation(final SelectCommand command) {
    if (subCompilations == null)
      subCompilations = new HashMap<>();

    final Compilation subCompilation = new Compilation(command, vendor, prepared, this);
    subCompilations.put(command, subCompilation);
    return subCompilation;
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

  private final Map<Compilable,Alias> aliases = new IdentityHashMap<>();

  Alias registerAlias(final Compilable subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(aliases.size()));

    return alias;
  }

  Alias getAlias(final Compilable subject) {
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
      if (parameters == null) {
        parameters = new ArrayList<>();
        Compilation parent = this;
        while ((parent = parent.parent) != null)
          parent.parameters = parameters;
      }

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
      if (parameters != null)
        for (int i = 0; i < parameters.size();)
          parameters.get(i++).get(statement, i);

      return statement.executeQuery();
    }

    return configure(connection, config).executeQuery(builder.toString());
  }

  boolean subCompile(final Compilable compilable) {
    if (subCompilations == null)
      return false;

    for (final Compilation compilation : subCompilations.values()) {
      final Alias alias = compilation.aliases.get(compilable);
      if (alias != null) {
        final Alias commandAlias = compilation.getSuperAlias(compilation.command);
        if (commandAlias != null)
          append(commandAlias).append('.');

        append(alias);
        return true;
      }

      if (compilation.subCompile(compilable))
        return true;
    }

    return false;
  }

  Compilation getSubCompilation(final Command<?> select) {
    return subCompilations.get(select);
  }

  Alias getSuperAlias(final Compilable compilable) {
    final Alias alias = aliases.get(compilable);
    return alias != null ? alias : parent == null ? null : parent.getSuperAlias(compilable);
  }

  @Override
  public String toString() {
    return builder.toString();
  }
}