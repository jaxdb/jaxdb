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
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.jaxdb.jsql.SelectImpl.untyped;
import org.jaxdb.jsql.data.Column.SetBy;
import org.jaxdb.vendor.DBVendor;
import org.libj.util.function.BooleanConsumer;

final class Compilation implements AutoCloseable {
  static enum Token {
    COMMA(", ");

    private final String value;

    private Token(final String value) {
      this.value = value;
    }

    @Override
    public String toString() {
      return value;
    }
  }

  final ArrayList<Object> tokens = new ArrayList<>();
  private List<String> columnTokens;
  private List<data.Column<?>> parameters;
  private final boolean prepared;
  private BooleanConsumer afterExecute;
  private boolean closed;

  final Keyword<?> command;
  final DBVendor vendor;
  final Compiler compiler;
  private final Compilation parent;

  private boolean skipFirstColumn;

  private Map<Keyword<?>,Compilation> subCompilations;

  Compilation(final Keyword<?> command, final DBVendor vendor, final boolean prepared) {
    this(command, vendor, prepared, null);
  }

  private Compilation(final Keyword<?> command, final DBVendor vendor, final boolean prepared, final Compilation parent) {
    this.command = command;
    this.vendor = vendor;
    this.prepared = prepared;
    this.compiler = Compiler.getCompiler(vendor);
    this.parent = parent;
    if (parent != null)
      this.parameters = parent.parameters;
  }

  public void setSessionId(final Connection connection, final Statement statement, final String sessionId) throws SQLException {
    if (sessionId != null)
      compiler.setSession(connection, statement, sessionId);
  }

  public List<String> getColumnTokens() {
    return this.columnTokens == null ? columnTokens = new ArrayList<>() : columnTokens;
  }

  public void setColumnTokens(final List<String> columnTokens) {
    this.columnTokens = columnTokens;
  }

  Compilation newSubCompilation(final untyped.SELECT<?> command) {
    if (subCompilations == null)
      subCompilations = new HashMap<>();

    final Compilation subCompilation = new Compilation(command, vendor, prepared, this);
    subCompilations.put(command, subCompilation);
    return subCompilation;
  }

  @Override
  public void close() {
    closed = true;
    if (parameters != null)
      parameters.clear();

    tokens.clear();
    if (columnTokens != null)
      columnTokens.clear();

    afterExecute = null;
  }

  boolean isPrepared() {
    return this.prepared;
  }

  List<data.Column<?>> getParameters() {
    return this.parameters;
  }

  boolean skipFirstColumn() {
    return skipFirstColumn;
  }

  void skipFirstColumn(final boolean skipFirstColumn) {
    this.skipFirstColumn = skipFirstColumn;
  }

  private final Map<Subject,Alias> aliases = new IdentityHashMap<>();

  Alias registerAlias(final Subject subject) {
    Alias alias = aliases.get(subject);
    if (alias == null)
      aliases.put(subject, alias = new Alias(subject, aliases.size()));

    return alias;
  }

  Alias getAlias(final Subject subject) {
    return aliases.get(subject);
  }

  Compilation append(final Object object) {
    return append(object.toString());
  }

  Compilation concat(final Object object) {
    return concat(object.toString());
  }

  Compilation comma() {
    return append(Token.COMMA);
  }

  Compilation append(final CharSequence seq) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    tokens.add(seq);
    return this;
  }

  Compilation concat(final CharSequence seq) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    final int index = tokens.size() - 1;
    final String token = tokens.get(index).toString();
    tokens.set(index, token + seq);
    return this;
  }

  Compilation concat(final char ch) {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    final int index = tokens.size() - 1;
    final String token = tokens.get(index).toString();
    tokens.set(index, token + ch);
    return this;
  }

  private int updateWhereIndex = Integer.MAX_VALUE;

  int getUpdateWhereIndex() {
    return updateWhereIndex;
  }

  void addCondition(final data.Column<?> column, final boolean considerIndirection, final boolean isForUpdateWhere) throws IOException, SQLException {
    append(vendor.getDialect().quoteIdentifier(column.name));

    if (column.isNull()) {
      append(" IS NULL");
    }
    else {
      append(" = ");
      addParameter(column, considerIndirection, isForUpdateWhere);
      if (isForUpdateWhere)
        updateWhereIndex = parameters != null ? parameters.size() - 1 : 0;
    }
  }

  @SuppressWarnings("resource")
  void addParameter(final data.Column<?> column, final boolean considerIndirection, final boolean isForUpdateWhere) throws IOException, SQLException {
    if (closed)
      throw new IllegalStateException("Compilation closed");

    if (column.ref != null && considerIndirection && (column.setByCur != SetBy.USER || column.keyForUpdate)) {
      ((Subject)column.ref).compile(this, false);
    }
    else if (prepared) {
      tokens.add(compiler.getPreparedStatementMark(column));
      if (parameters == null) {
        parameters = new ArrayList<>();
        Compilation parent = this;
        while ((parent = parent.parent) != null)
          parent.parameters = parameters;
      }

      parameters.add(column);
    }
    else {
      tokens.add(column.compile(vendor, isForUpdateWhere));
    }
  }

  final void afterExecute(final BooleanConsumer consumer) {
    this.afterExecute = this.afterExecute == null ? consumer : this.afterExecute.andThen(consumer);
  }

  final void afterExecute(final boolean success) {
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

  boolean subCompile(final Subject subject) {
    if (subCompilations == null || !(subject instanceof data.Entity))
      return false;

    for (final Compilation compilation : subCompilations.values()) {
      final Alias alias = compilation.aliases.get(subject);
      if (alias != null) {
        final Alias commandAlias = compilation.getSuperAlias(compilation.command);
        if (commandAlias != null) {
          append(commandAlias).concat('.').concat(alias);
          return true;
        }

        append(alias).concat('.');
        return false;
      }

      if (compilation.subCompile(subject))
        return true;
    }

    return false;
  }

  Compilation getSubCompilation(final Keyword<?> select) {
    return subCompilations.get(select);
  }

  Alias getSuperAlias(final Subject subject) {
    final Alias alias = aliases.get(subject);
    return alias != null ? alias : parent == null ? null : parent.getSuperAlias(subject);
  }

  @Override
  public String toString() {
    final StringBuilder builder = new StringBuilder();
    for (final Object token : tokens)
      builder.append(token);

    return builder.toString();
  }
}