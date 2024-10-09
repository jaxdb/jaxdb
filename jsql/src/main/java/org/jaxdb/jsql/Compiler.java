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
import java.io.InputStream;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.jaxdb.ddlx.dt;
import org.jaxdb.jsql.Command.CaseImpl;
import org.jaxdb.jsql.keyword.Cast;
import org.jaxdb.jsql.keyword.Keyword;
import org.jaxdb.jsql.keyword.Select;
import org.jaxdb.vendor.DbVendor;
import org.jaxdb.vendor.DbVendorCompiler;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;
import org.libj.util.IdentityHashSet;

abstract class Compiler extends DbVendorCompiler {
  private static final Compiler[] compilers = {
    /* new DB2Compiler(), */null,
    new DerbyCompiler(),
    new MariaDBCompiler(),
    new MySQLCompiler(),
    new OracleCompiler(),
    new PostgreSQLCompiler(),
    new SQLiteCompiler()};

  static Compiler getCompiler(final DbVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected Compiler(final DbVendor vendor) {
    super(vendor);
  }

  final void compileEntities(final type.Entity[] entities, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    for (int i = 0, i$ = entities.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      final type.Entity entity = entities[i];
      compileNextSubject(entity, i, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
    }
  }

  /**
   * Default no-op implementation of method to compile an enum translation phrase.
   *
   * @param b The target {@link StringBuilder}.
   * @param from The source enum.
   * @param to The target enum.
   * @return The target {@link StringBuilder}.
   */
  StringBuilder translateEnum(final StringBuilder b, final data.ENUM<?> from, final data.ENUM<?> to) {
    return b;
  }

  private void checkTranslateType(final StringBuilder b, final Map<Integer,data.ENUM<?>> translateTypes, final Subject column, final int index) {
    if (column instanceof data.ENUM<?> && translateTypes != null) {
      final data.ENUM<?> translateType = translateTypes.get(index);
      if (translateType != null)
        translateEnum(b, (data.ENUM<?>)column, translateType);
    }
  }

  void compileNextSubject(final type.Entity entity, final int index, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    if (entity instanceof data.Table) {
      final data.Table table = (data.Table)entity;
      final Alias alias = compilation.registerAlias(table);
      final data.Column<?>[] columns = table._column$;
      for (int c = 0, c$ = columns.length; c < c$; ++c) { // [A]
        final data.Column<?> column = columns[c];
        if (c > 0)
          sql.append(", ");

        if (useAliases) {
          alias.compile(compilation, false);
          sql.append('.');
        }

        final StringBuilder b = new StringBuilder();
        q(b, column.name);
        checkTranslateType(b, translateTypes, column, c);
        sql.append(b);
        if (addToColumnTokens)
          compilation.getColumnTokens().add(b.toString());
      }
    }
    else if (entity instanceof Keyword) {
      sql.append('(');
      ((Keyword)entity).compile(compilation, false);
      sql.append(')');
    }
    else if (entity instanceof type.Column) {
      final Subject subject = (Subject)entity;
      compilation.registerAlias(subject.getTable());
      final Alias alias;
      final Evaluable wrapped;
      final int start = sql.length();
      if (subject instanceof data.Column && useAliases && isFromGroupBy && (wrapped = ((data.Column<?>)subject).wrapped()) instanceof As && (alias = compilation.getAlias(((As<?>)wrapped).getVariable())) != null)
        alias.compile(compilation, false);
      else
        subject.compile(compilation, false);

      checkTranslateType(sql, translateTypes, subject, index);
      final int end = sql.length();
      if (addToColumnTokens)
        compilation.getColumnTokens().add(sql.substring(start, end));
    }
    else {
      throw new UnsupportedOperationException("Unsupported subject type: " + entity.getClass().getName());
    }
  }

  abstract void onRegister(Connection connection) throws SQLException;
  abstract void onConnect(Connection connection) throws SQLException;

  static <T> Subject toSubject(final T entity) {
    return (Subject)entity;
  }

  /**
   * Returns the quoted name of the specified {@link data.Table}.
   *
   * @param b The target {@link StringBuilder}.
   * @param table The {@link data.Table}.
   * @param compilation The {@link Compilation}
   * @return The target {@link StringBuilder}.
   */
  StringBuilder tableName(final StringBuilder b, final data.Table table, final Compilation compilation) {
    return q(b, table.getName());
  }

  /**
   * Get the parameter mark for {@link PreparedStatement}s.
   *
   * @param b The target {@link StringBuilder}.
   * @param column The {@link data.Column} for the requested mark.
   * @return The target {@link StringBuilder}.
   */
  StringBuilder getPreparedStatementMark(final StringBuilder b, final data.Column<?> column) {
    return b.append('?');
  }

  /**
   * Compile the specified parameters, and append to the provided {@link Compilation}.
   *
   * @param variable The variable to evaluate.
   * @param _else The {@link Command.CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileCaseElse(final data.Column<?> variable, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("CASE ");
    variable.compile(compilation, true);
  }

  /**
   * Compile the specified parameters, and append to the provided {@link Compilation}.
   *
   * @param when The {@link Command.CaseImpl.WHEN}.
   * @param compilation The target {@link Compilation}.
   */
  void compileWhen(final CaseImpl.Search.WHEN<?> when, final Compilation compilation) {
    compilation.sql.append("CASE");
  }

  /**
   * Compile the specified parameters, and append to the provided {@link Compilation}.
   *
   * @param _else The {@link Command.CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileWhenThenElse(final ArrayList<data.Column<?>> whenThen, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    for (int i = 0, i$ = whenThen.size(); i < i$;) { // [RA]
      final data.Column<?> when = whenThen.get(i++);
      final data.Column<?> then = whenThen.get(i++);
      sql.append(" WHEN ");
      when.compile(compilation, true);
      sql.append(" THEN ");
      then.compile(compilation, true);
    }

    if (_else != null) {
      sql.append(" ELSE ");
      _else.compile(compilation, true);
      sql.append(" END");
    }
  }

  void compileSelect(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("SELECT ");
    if (select.distinct)
      sql.append("DISTINCT ");

    compileEntities(select.entities, false, useAliases, select.translateTypes, compilation, true);
  }

  void compileFrom(final data.Table[] from, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (from == null)
      return;

    final StringBuilder sql = compilation.sql;
    sql.append(" FROM ");

    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    for (int i = 0, i$ = from.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      final data.Table table = from[i];
      final Evaluable wrapped = table.wrapped();
      if (wrapped != null) {
        wrapped.compile(compilation, false);
      }
      else {
        tableName(sql, table, compilation);
        if (useAliases) {
          sql.append(' ');
          compilation.getAlias(table).compile(compilation, false);
        }
      }
    }
  }

  void compileJoin(final Command.Select.untyped.SELECT.JoinKind joinKind, final Object join, final Condition<?> on, final Compilation compilation) throws IOException, SQLException {
    if (join == null)
      return;

    final StringBuilder sql = compilation.sql;
    sql.append(joinKind);
    sql.append(" JOIN ");
    if (join instanceof data.Table) {
      final data.Table table = (data.Table)join;
      tableName(sql, table, compilation).append(' ');
      compilation.registerAlias(table).compile(compilation, false);
      if (on != null) {
        sql.append(" ON (");
        on.compile(compilation, false);
        sql.append(')');
      }
    }
    else if (join instanceof Command.Select.untyped.SELECT) {
      final Command.Select.untyped.SELECT<?> select = (Command.Select.untyped.SELECT<?>)join;
      sql.append('(');
      final Compilation subCompilation = compilation.getSubCompilation(select);
      final Alias alias = compilation.getAlias(select);
      sql.append(subCompilation);
      sql.append(") ");
      sql.append(alias);
      if (on != null) {
        sql.append(" ON (");
        on.compile(compilation, false);
        compilation.subCompile(on);
        sql.append(')');
      }
    }
    else {
      throw new IllegalStateException();
    }
  }

  void compileWhere(final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    if (where == null)
      return;

    compilation.sql.append(" WHERE ");
    where.compile(compilation, false);
  }

  void compileGroupByHaving(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    final data.Entity[] groupBy = select.groupBy;

    if (groupBy != null) {
      sql.append(" GROUP BY ");
      compileEntities(groupBy, true, useAliases, null, compilation, false);
      select.isConditionalSelect = true;
    }

    // NOTE: "When GROUP BY is not used, HAVING behaves like a WHERE clause."
    // NOTE: The difference between where and having: WHERE filters ROWS, while HAVING filters groups.
    final Condition<?> having = select.having;
    if (having != null) {
      sql.append(" HAVING ");
      having.compile(compilation, false);
      select.isConditionalSelect = true;
    }
  }

  void compileOrderBy(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    final data.Column<?>[] orderBy = select.orderBy;
    final int[] orderByIndexes = select.orderByIndexes;
    if (orderBy == null && orderByIndexes == null)
      return;

    final StringBuilder sql = compilation.sql;
    sql.append(" ORDER BY ");
    if (orderBy != null) {
      final int len = orderBy.length;
      for (int i = 0; i < len; ++i) { // [A]
        final data.Column<?> column = orderBy[i];
        if (i > 0)
          sql.append(", ");

        final Evaluable wrapped = column.wrapped();
        if (wrapped instanceof As) {
          // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
          // FIXME: This code is commented-out, because Derby complains when this is done.
          // final Alias alias = compilation.getAlias(((As<?>)column.wrapper()).getVariable());
          // if (alias != null) {
          // alias.compile(compilation);
          // }
          // else {
          unwrapAlias(column).compile(compilation, false);
          // }
        }
        else {
          compilation.registerAlias(column.getTable());
          column.compile(compilation, false);
        }
      }
    }
    else if (orderByIndexes != null) {
      for (int i = 0, i$ = orderByIndexes.length; i < i$; ++i) { // [A]
        final int columnIndex = orderByIndexes[i];
        if (i > 0)
          sql.append(", ");

        sql.append(String.valueOf(columnIndex));
      }
    }
    else {
      throw new IllegalStateException();
    }
  }

  void compileLimitOffset(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    final int limit = select.limit;
    if (limit == -1)
      return;

    final StringBuilder sql = compilation.sql;
    sql.append(" LIMIT ").append(limit);
    final int offset = select.offset;
    if (offset != -1)
      sql.append(" OFFSET ").append(offset);
  }

  boolean aliasInForUpdate() {
    return true;
  }

  void compileFor(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    final StringBuilder sql = compilation.sql;
    if (select.forLockStrength != null) {
      sql.append(" FOR ").append(select.forLockStrength);
      final Subject[] forSubjects = select.forSubjects;
      if (forSubjects != null && forSubjects.length > 0)
        compileForOf(select, compilation);
    }

    if (select.forLockOption != null)
      sql.append(' ').append(select.forLockOption);
  }

  void compileForOf(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    final StringBuilder sql = compilation.sql;
    sql.append(" OF ");
    final HashSet<data.Table> tables = new HashSet<>(1);
    final Subject[] forSubjects = select.forSubjects;
    for (int i = 0, i$ = forSubjects.length; i < i$; ++i) { // [A]
      final Subject entity = forSubjects[i];
      final data.Table table;
      if (entity instanceof data.Table)
        table = (data.Table)entity;
      else if (entity instanceof data.Column)
        table = ((data.Column<?>)entity).getTable();
      else
        throw new UnsupportedOperationException("Unsupported type.Entity: " + entity.getClass().getName());

      if (!tables.add(table))
        continue;

      if (i > 0)
        sql.append(", ");

      appendForOf(sql, table, compilation);
    }
  }

  void appendForOf(final StringBuilder sql, final data.Table table, final Compilation compilation) {
    q(sql, table.getName());
  }

  void compileUnion(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    final ArrayList<Object> unions = select.unions;
    if (unions == null)
      return;

    final StringBuilder sql = compilation.sql;
    for (int i = 0, i$ = unions.size(); i < i$;) { // [RA]
      final Boolean all = (Boolean)unions.get(i++);
      final Subject union = (Subject)unions.get(i++);
      sql.append(" UNION ");
      if (all)
        sql.append("ALL ");

      union.compile(compilation, false);
    }
  }

  void compileInsert(final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("INSERT ");
    if (ignore)
      sql.append("IGNORE ");

    sql.append("INTO ");
    q(sql, columns[0].getTable().getName()).append(" (");
    final int len = columns.length;
    for (int i = 0; i < len; ++i) { // [A]
      final data.Column<?> column = columns[i];
      if (i > 0)
        sql.append(", ");

      q(sql, column.name);
    }

    sql.append(") VALUES (");

    for (int i = 0; i < len; ++i) { // [A]
      final data.Column<?> column = columns[i];
      if (i > 0)
        sql.append(", ");

      if (shouldInsert(column, true, compilation))
        compilation.addParameter(column, false, false);
      else
        sql.append("DEFAULT");
    }

    sql.append(')');
  }

  final void compileInsert(final data.Table insert, final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    compileInsert(insert != null ? insert._column$ : columns, ignore, compilation);
  }

  Compilation compileInsertSelect(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("INSERT ");
    if (ignore)
      sql.append("IGNORE ");

    sql.append("INTO ");
    q(sql, columns[0].getTable().getName());
    sql.append(" (");

    final int len = columns.length;
    final HashMap<Integer,data.ENUM<?>> translateTypes = new HashMap<>(len);
    for (int i = 0; i < len; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      final data.Column<?> column = columns[i];
      column.compile(compilation, false); // FIXME: `isAbsolutePrimaryCondition` is not being considered here
      if (column instanceof data.ENUM<?>)
        translateTypes.put(i, (data.ENUM<?>)column);
    }

    sql.append(") ");

    final Command.Select.untyped.SELECT<?> selectImpl = (Command.Select.untyped.SELECT<?>)select;
    final Compilation selectCompilation = compilation.newSubCompilation(selectImpl);
    selectImpl.translateTypes = translateTypes;
    selectImpl.compile(selectCompilation, false); // FIXME: `isAbsolutePrimaryCondition` is not being considered here
    sql.append(selectCompilation);
    return selectCompilation;
  }

  abstract void compileInsertOnConflict(data.Column<?>[] columns, Select.untyped.SELECT<?> select, data.Column<?>[] onConflict, boolean doUpdate, Compilation compilation) throws IOException, SQLException;

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldInsert(final data.Column column, final boolean modify, final Compilation compilation) {
    if (column.setByCur == data.Column.SetBy.USER || column.setByCur == data.Column.SetBy.SYSTEM && (column.primaryIndexType != null || column.isKeyForUpdate))
      return true;

    if (column.generateOnInsert == null) {
      if (column.hasDefault)
        column.setByCur = data.Column.SetBy.SYSTEM;

      return false;
    }

    if (column.generateOnInsert == GenerateOn.AUTO_GENERATED)
      return false;

    if (modify)
      column.generateOnInsert.generate(column, compilation.vendor);

    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldUpdate(final data.Column column, final Compilation compilation) {
    boolean shouldUpdate = column.setByCur == data.Column.SetBy.USER;
    if ((!shouldUpdate || column.isKeyForUpdate) && column.generateOnUpdate != null) {
      column.generateOnUpdate.generate(column, compilation.vendor);
      shouldUpdate = true;
    }

    if (column.ref != null) {
      shouldUpdate = true;
      compilation.afterExecute((final boolean success) -> {
        if (success) {
          // NOTE: Column.wasSet must be false, so that the Column.ref can continue to take effect.
          final Object evaluated = column.evaluate(new IdentityHashSet<>());
          if (evaluated == null) {
            column.setValue(null);
          }
          else {
            final Class type = column.type();
            if (type == evaluated.getClass())
              column.setValue(evaluated);
            else if (evaluated instanceof Number && Number.class.isAssignableFrom(type))
              column.setValue(data.Numeric.valueOf((Number)evaluated, (Class<? extends Number>)type));
            else
              throw new IllegalStateException("Value is greater than maximum value of type " + data.Column.getSimpleName(column.getClass()) + ": " + evaluated);
          }

          column.setByCur = data.Column.SetBy.SYSTEM;
        }
      });
    }

    return shouldUpdate;
  }

  void compileUpdate(final data.Table update, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("UPDATE ");
    q(sql, update.getName());
    sql.append(" SET ");
    boolean modified = false;
    final data.Column<?>[] columns = update._column$;
    for (final data.Column<?> column : columns) { // [A]
      if (shouldUpdate(column, compilation)) {
        if (modified)
          sql.append(", ");

        q(sql, column.name).append(" = ");
        compilation.addParameter(column, true, false);
        modified = true;
      }
    }

    // No changes were found
    if (!modified)
      throw new IllegalArgumentException("UPDATE did not SET any columns");

    modified = false;
    for (final data.Column<?> column : columns) { // [A]
      if (column.primaryIndexType != null || column.isKeyForUpdate && column.setByCur != null) {
        if (modified)
          sql.append(" AND ");
        else
          sql.append(" WHERE ");

        compilation.addCondition(column, false, true);
        modified = true;
      }
    }
  }

  void compileUpdate(final data.Table update, final ArrayList<Subject> sets, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("UPDATE ");
    q(sql, update.getName());
    sql.append(" SET ");
    for (int i = 0, i$ = sets.size(); i < i$;) { // [RA]
      if (i > 0)
        sql.append(", ");

      final data.Column<?> column = (data.Column<?>)sets.get(i++);
      final Subject to = sets.get(i++);
      q(sql, column.name).append(" = ");
      if (to == null)
        sql.append("NULL");
      else
        to.compile(compilation, false);
    }

    if (where != null) {
      sql.append(" WHERE ");
      where.compile(compilation, false);
    }
  }

  void compileDelete(final data.Table delete, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("DELETE FROM ");
    q(sql, delete.getName());
    boolean modified = false;
    for (int j = 0, j$ = delete._column$.length; j < j$; ++j) { // [A]
      final data.Column<?> column = delete._column$[j];
      if (column.setByCur == data.Column.SetBy.USER || column.setByCur == data.Column.SetBy.SYSTEM && (column.primaryIndexType != null || column.isKeyForUpdate)) {
        if (modified)
          sql.append(" AND ");
        else
          sql.append(" WHERE ");

        compilation.addCondition(column, false, false);
        modified = true;
      }
    }
  }

  void compileDelete(final data.Table delete, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("DELETE FROM ");
    q(sql, delete.getName());
    sql.append(" WHERE ");
    where.compile(compilation, false);
  }

  <D extends data.Entity> void compile(final data.Table table, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Evaluable wrapped = table.wrapped();
    if (wrapped != null) {
      wrapped.compile(compilation, isExpression);
    }
    else {
      final StringBuilder sql = compilation.sql;
      tableName(sql, table, compilation);
      final Alias alias = compilation.registerAlias(table);
      sql.append(' ');
    }
  }

  void compile(final ExpressionImpl.ChangeCase expression, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(expression.o).append('(');
    toSubject(expression.a).compile(compilation, true);
    sql.append(')');
  }

  void compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    for (int i = 0, i$ = expression.a.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(" || ");

      toSubject(expression.a[i]).compile(compilation, true);
    }

    sql.append(')');
  }

  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    final StringBuilder sql = compilation.sql;
    sql.append("((");
    toSubject(a).compile(compilation, true);
    sql.append(") ");
    sql.append(o);
    sql.append(" (");
    sql.append("INTERVAL '");
    final ArrayList<TemporalUnit> units = b.getUnits();
    for (int i = 0, i$ = units.size(); i < i$; ++i) { // [RA]
      if (i > 0)
        sql.append(' ');

      final TemporalUnit unit = units.get(i);
      sql.append(b.get(unit)).append(' ').append(unit);
    }

    sql.append("'))");
  }

  void compileIntervalAdd(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "ADD", b, compilation);
  }

  void compileIntervalSub(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "SUB", b, compilation);
  }

  /**
   * Returns the string representation of the specified {@link As}.
   *
   * @param b The target {@link StringBuilder}.
   * @param as The {@link As}.
   * @return The target {@link StringBuilder}.
   */
  StringBuilder compileAs(final StringBuilder b, final As<?> as) {
    return b.append("AS ");
  }

  void compileAs(final As<?> as, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Alias alias = compilation.registerAlias(as.getVariable());
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    as.parent().compile(compilation, true);
    sql.append(')');
    if (!isExpression && as.isExplicit()) {
      sql.append(' ');
      compileAs(sql, as);

      alias.compile(compilation, false);
    }
  }

  // FIXME: Move this to a Util class or something
  private static <D extends data.Entity> void compileCondition(final boolean and, final Condition<?> condition, final Compilation compilation) throws IOException, SQLException {
    if (!(condition instanceof BooleanTerm) || and == condition instanceof BooleanTerm.And) {
      condition.compile(compilation, false);
    }
    else {
      final StringBuilder sql = compilation.sql;
      sql.append('(');
      condition.compile(compilation, false);
      sql.append(')');
    }
  }

  void compileCondition(final BooleanTerm condition, final Compilation compilation) throws IOException, SQLException {
    final boolean and = condition instanceof BooleanTerm.And;
    compileCondition(and, condition.a, compilation);
    final StringBuilder sql = compilation.sql;
    final String andOr = condition.toString();
    sql.append(' ').append(andOr).append(' ');
    compileCondition(and, condition.b, compilation);
    final Condition<?>[] conditions = condition.conditions;
    for (int i = 0, i$ = conditions.length; i < i$; ++i) { // [A]
      sql.append(' ').append(andOr).append(' ');
      compileCondition(and, conditions[i], compilation);
    }
  }

  private static Subject unwrapAlias(final Subject subject) {
    if (!(subject instanceof data.Entity))
      return subject;

    final data.Entity entity = (data.Entity)subject;
    final Evaluable wrapped = entity.wrapped();
    return wrapped instanceof As ? ((As<?>)wrapped).parent() : subject;
  }

  void compilePredicate(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    if (!compilation.subCompile(predicate.a)) {
      // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
      // FIXME: This code is commented-out, because Derby complains when this is done.
      // final Alias alias;
      // if (predicate.a.wrapper() instanceof As && (alias = compilation.getAlias(((As<?>)predicate.a.wrapper()).getVariable())) != null)
      // alias.compile(compilation);
      // else
      // FIXME: The braces are really only needed for inner SELECTs. Add the complexity to save the compiled SQL from having an extra
      // couple of braces?!
      final boolean isSelect = predicate.a instanceof Select.untyped.SELECT;

      if (isSelect) {
        sql.append('(');
        unwrapAlias(predicate.a).compile(compilation, true);
        sql.append(')');
      }
      else if (predicate.a == null) {
        throw new IllegalArgumentException("lhs is NULL");
      }
      else {
        unwrapAlias(predicate.a).compile(compilation, true);
      }
    }

    sql.append(' ');
    predicate.compile(null, sql, false);
    sql.append(' ');
    if (!compilation.subCompile(predicate.b)) {
      // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
      // FIXME: This code is commented-out, because Derby complains when this is done.
      // final Alias alias;
      // if (predicate.b.wrapper() instanceof As && (alias = compilation.getAlias(((As<?>)predicate.b.wrapper()).getVariable())) != null)
      // alias.compile(compilation);
      // else
      // FIXME: The braces are really only needed for inner SELECTs. Add the complexity to save the compiled SQL from having an extra
      // couple of braces?!
      final boolean isSelect = predicate.b instanceof Select.untyped.SELECT;

      if (isSelect) {
        sql.append('(');
        unwrapAlias(predicate.b).compile(compilation, true);
        sql.append(')');
      }
      else if (predicate.b == null) {
        compilation.sql.append("NULL");
      }
      else {
        unwrapAlias(predicate.b).compile(compilation, true);
      }
    }
  }

  void compileNotPredicate(final NotPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("NOT (");
    toSubject(predicate.query != null ? predicate.query : predicate.column).compile(compilation, true);
    sql.append(')');
  }

  void compileInPredicate(final InPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    toSubject(predicate.column).compile(compilation, true);
    final StringBuilder sql = compilation.sql;
    sql.append(" IN (");
    final Subject[] values = predicate.values;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      values[i].compile(compilation, true);
    }

    sql.append(')');
  }

  void compileExistsPredicate(final ExistsPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("EXISTS (");
    predicate.subQuery.compile(compilation, true);
    sql.append(')');
  }

  void compileLikePredicate(final LikePredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    toSubject(predicate.column).compile(compilation, true);
    sql.append(") LIKE '").append(predicate.pattern).append('\'');
  }

  void compileQuantifiedComparisonPredicate(final QuantifiedComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(predicate.qualifier).append(" (");
    predicate.subQuery.compile(compilation, true);
    sql.append(')');
  }

  void compileBetweenPredicate(final BetweenPredicates.BetweenPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    final type.Column<?> column = predicate.column;
    toSubject(column).compile(compilation, true);
    sql.append(") BETWEEN ");

    final Subject a = predicate.a();
    a.compile(compilation, true);

    sql.append(" AND ");

    final Subject b = predicate.b();
    b.compile(compilation, true);
  }

  void compileNullPredicate(final NullPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    toSubject(predicate.column).compile(compilation, true);
    final StringBuilder sql = compilation.sql;
    sql.append(" IS ");
    if (!predicate.is)
      sql.append("NOT ");

    sql.append("NULL");
  }

  /**
   * Compile the PI expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  void compilePi(final Compilation compilation) {
    compilation.sql.append("PI()");
  }

  /**
   * Compile the NOW expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  void compileNow(final Compilation compilation) {
    compilation.sql.append("NOW()");
  }

  private static void compileExpression(final String o, final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(o);
    sql.append('(');
    toSubject(a).compile(compilation, true);
    sql.append(')');
  }

  private static void compileExpression(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(o);
    sql.append('(');
    toSubject(a).compile(compilation, true);
    sql.append(", ");
    toSubject(b).compile(compilation, true);
    sql.append(')');
  }

  private static void compileFunction(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("((");
    toSubject(a).compile(compilation, true);
    sql.append(')');
    sql.append(' ').append(o).append(' ');
    sql.append('(');
    toSubject(b).compile(compilation, true);
    sql.append("))");
  }

  /**
   * Compile the ABS expression, and append to the provided {@link Compilation}.
   *
   * @param a The expression to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileAbs(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ABS", a, compilation);
  }

  /**
   * Compile the SIGN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileSign(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("SIGN", a, compilation);
  }

  /**
   * Compile the ROUND expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileRound(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ROUND", a, compilation);
  }

  /**
   * Compile the ROUND expression, and append to the provided {@link Compilation}.
   *
   * @param a The first {@link type.Column}.
   * @param b The second {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileRound(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ROUND", a, b, compilation);
  }

  /**
   * Compile the FLOOR expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileFloor(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("FLOOR", a, compilation);
  }

  /**
   * Compile the CEIL expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileCeil(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("CEIL", a, compilation);
  }

  /**
   * Compile the SQRT expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileSqrt(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("SQRT", a, compilation);
  }

  /**
   * Compile the DEGREES expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileDegrees(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("DEGREES", a, compilation);
  }

  /**
   * Compile the RADIANS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileRadians(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("RADIANS", a, compilation);
  }

  /**
   * Compile the POW expression, and append to the provided {@link Compilation}.
   *
   * @param a The first {@link type.Column}.
   * @param b The second {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compilePow(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileExpression("POWER", a, b, compilation);
  }

  /**
   * Compile the MOD expression, and append to the provided {@link Compilation}.
   *
   * @param a The first {@link type.Column}.
   * @param b The second {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileExpression("MOD", a, b, compilation);
  }

  /**
   * Compile the SIN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileSin(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("SIN", a, compilation);
  }

  /**
   * Compile the ASIN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileAsin(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ASIN", a, compilation);
  }

  /**
   * Compile the COS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileCos(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("COS", a, compilation);
  }

  /**
   * Compile the ACOS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileAcos(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ACOS", a, compilation);
  }

  /**
   * Compile the TAN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileTan(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("TAN", a, compilation);
  }

  /**
   * Compile the ATAN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileAtan(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ATAN", a, compilation);
  }

  void compileSum(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compileSet("SUM", a, distinct, compilation);
  }

  void compileAvg(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compileSet("AVG", a, distinct, compilation);
  }

  void compileMax(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compileSet("MAX", a, distinct, compilation);
  }

  void compileMin(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compileSet("MIN", a, distinct, compilation);
  }

  void compileAdd(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileFunction("+", a, b, compilation);
  }

  void compileSub(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileFunction("-", a, b, compilation);
  }

  void compileMul(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileFunction("*", a, b, compilation);
  }

  void compileDiv(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileFunction("/", a, b, compilation);
  }

  void compileLower(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LOWER", a, compilation);
  }

  void compileUpper(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("UPPER", a, compilation);
  }

  void compileLength(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LENGTH", a, compilation);
  }

  void compileSubstring(final type.Column<?> col, final type.Column<?> from, final type.Column<?> to, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("SUBSTRING");
    sql.append('(');
    toSubject(col).compile(compilation, true);
    if (from != null) {
      sql.append(", ");
      toSubject(from).compile(compilation, true);
    }

    if (to != null) {
      if (from == null) {
        sql.append(", ");
        sql.append('1');
      }

      sql.append(", ");
      toSubject(to).compile(compilation, true);
    }

    sql.append(')');
  }

  /**
   * Compile the ATAN2 expression, and append to the provided {@link Compilation}.
   *
   * @param a The first {@link type.Column}.
   * @param b The second {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileAtan2(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileExpression("ATAN2", a, b, compilation);
  }

  /**
   * Compile the EXP expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileExp(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("EXP", a, compilation);
  }

  /**
   * Compile the LN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileLn(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LN", a, compilation);
  }

  /**
   * Compile the LOG expression, and append to the provided {@link Compilation}.
   *
   * @param a The first {@link type.Column}.
   * @param b The second {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileLog(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LOG", a, b, compilation);
  }

  /**
   * Compile the LOG2 expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileLog2(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LOG2", a, compilation);
  }

  /**
   * Compile the LOG10 expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileLog10(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compileExpression("LOG10", a, compilation);
  }

  /**
   * Compile the COUNT expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param distinct Whether to count DISTINCT results.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileCount(final data.Entity a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("COUNT").append('(');
    if (a instanceof data.Table) {
      sql.append('*');
    }
    else {
      if (distinct)
        sql.append("DISTINCT ");

      a.compile(compilation, true);
    }

    sql.append(')');
  }

  void compileSet(final String o, final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(o).append('(');
    if (a != null) {
      if (distinct)
        sql.append("DISTINCT ");

      toSubject(a).compile(compilation, true);

      // if (expression.b != null) {
      // sql.append(", ");
      // toSubject(expression.b).compile(compilation);
      // }
    }

    sql.append(')');
  }

  void compileOrder(final OrderingSpec spec, final Compilation compilation) throws IOException, SQLException {
    unwrapAlias(spec.column).compile(compilation, true);
    compilation.sql.append(' ').append(spec.ascending ? "ASC" : "DESC");
  }

  <V> StringBuilder compileArray(final StringBuilder b, final data.ARRAY<? extends V> array, final data.Column<V> column, final boolean isForUpdateWhere) throws IOException {
    b.append('(');
    final data.Column<V> clone = column.clone();
    final V[] items = array.get();
    for (int i = 0, i$ = items.length; i < i$; ++i) { // [A]
      clone.setValue(items[i]);
      if (i > 0)
        b.append(", ");

      column.compile(this, b, isForUpdateWhere);
    }

    return b.append(')');
  }

  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("CAST((");
    toSubject(as.column).compile(compilation, true);
    sql.append(") AS ");
    as.cast.declare(sql, compilation.vendor).append(')');
  }

  StringBuilder compileCast(final StringBuilder b, final data.Column<?> column, final Compilation compilation) {
    return column.declare(b, compilation.vendor);
  }

  StringBuilder compileColumn(final StringBuilder b, final data.BIGINT column, final boolean isForUpdateWhere) {
    return b.append(column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  final StringBuilder compileColumn(final StringBuilder b, final data.BINARY column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : getDialect().binaryToStringLiteral(b, column.getForUpdateWhereGetOld(isForUpdateWhere));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.BLOB column, final boolean isForUpdateWhere) throws IOException {
    try (final InputStream in = column.getForUpdateWhereGetOld(isForUpdateWhere)) {
      return in == null ? b.append("NULL") : getDialect().binaryToStringLiteral(b, Streams.readBytes(in));
    }
  }

  StringBuilder compileColumn(final StringBuilder b, final data.BOOLEAN column, final boolean isForUpdateWhere) {
    return b.append(String.valueOf(column.getForUpdateWhereGetOld(isForUpdateWhere)).toUpperCase());
  }

  StringBuilder compileColumn(final StringBuilder b, final data.CHAR column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(column.getForUpdateWhereGetOld(isForUpdateWhere).replace("'", "''")).append('\'');
  }

  StringBuilder compileColumn(final StringBuilder b, final data.CLOB column, final boolean isForUpdateWhere) throws IOException {
    try (final Reader in = column.getForUpdateWhereGetOld(isForUpdateWhere)) {
      return in == null ? b.append("NULL") : b.append('\'').append(Readers.readFully(in)).append('\'');
    }
  }

  StringBuilder compileColumn(final StringBuilder b, final data.DATE column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(Dialect.dateToString(column.getForUpdateWhereGetOld(isForUpdateWhere))).append('\'');
  }

  StringBuilder compileColumn(final StringBuilder b, final data.DATETIME column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(Dialect.dateTimeToString(column.getForUpdateWhereGetOld(isForUpdateWhere))).append('\'');
  }

  StringBuilder compileColumn(final StringBuilder b, final data.DECIMAL column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.DOUBLE column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.ENUM<?> column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(column.getForUpdateWhereGetOld(isForUpdateWhere)).append('\'');
  }

  StringBuilder compileColumn(final StringBuilder b, final data.FLOAT column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.INT column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.SMALLINT column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.TINYINT column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append(Dialect.NUMBER_FORMAT.get().format(column.getForUpdateWhereGetOld(isForUpdateWhere)));
  }

  StringBuilder compileColumn(final StringBuilder b, final data.TIME column, final boolean isForUpdateWhere) {
    return column.getForUpdateWhereIsNullOld(isForUpdateWhere) ? b.append("NULL") : b.append('\'').append(Dialect.timeToString(column.getForUpdateWhereGetOld(isForUpdateWhere))).append('\'');
  }

  /**
   * Method called during statement execution workflow to set the statement's session.
   *
   * @param statement The {@link Statement} with which to set the session ID.
   * @param sessionId The session ID to set.
   * @throws SQLException If a SQL error has occurred.
   */
  void setSessionId(final Statement statement, final String sessionId) throws SQLException {
  }

  void assignAliases(final data.Table[] from, final ArrayList<Object> joins, final Compilation compilation) throws IOException, SQLException {
    if (from != null) {
      for (final data.Table table : from) { // [A]
        // FIXME: Why am I clearing the wrapped entity here?
        table.clearWrap();
        compilation.registerAlias(table);
      }
    }

    if (joins == null)
      return;

    for (int i = 0, i$ = joins.size(); i < i$;) { // [RA]
      final Command.Select.untyped.SELECT.JoinKind joinKind = (Command.Select.untyped.SELECT.JoinKind)joins.get(i++);
      final Subject join = (Subject)joins.get(i++);
      if (join instanceof data.Table) {
        final data.Table table = (data.Table)join;
        // FIXME: Why am I clearing the wrapped entity here?
        table.clearWrap();
        compilation.registerAlias(table);
      }
      else if (join instanceof Command.Select.untyped.SELECT) {
        final Command.Select.untyped.SELECT<?> select = (Command.Select.untyped.SELECT<?>)join;
        final Compilation subCompilation = compilation.newSubCompilation(select);
        compilation.registerAlias(select);
        select.compile(subCompilation, false);
      }
      else {
        throw new IllegalStateException();
      }
    }
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.CHAR column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    if (column.getForUpdateWhereIsNullOld(isForUpdateWhere))
      statement.setNull(parameterIndex, column.sqlType());
    else
      statement.setString(parameterIndex, column.getForUpdateWhereGetOld(isForUpdateWhere));
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.CHAR column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    if (column.isNull())
      resultSet.updateNull(columnIndex);
    else
      resultSet.updateString(columnIndex, column.get());
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  // FIXME: This should be named getColumn()
  String getParameter(final data.CHAR column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getString(columnIndex);
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.CLOB column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws IOException, SQLException {
    final Reader in = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (in != null)
      statement.setClob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.CLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Reader in = column.get();
    if (in != null)
      resultSet.updateClob(columnIndex, in);
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  Reader getParameter(final data.CLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Clob value = resultSet.getClob(columnIndex);
    return value == null ? null : value.getCharacterStream();
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.BLOB column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws IOException, SQLException {
    final InputStream in = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (in == null)
      statement.setBlob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, Types.BLOB);
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.BLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final InputStream in = column.get();
    if (in != null)
      resultSet.updateBlob(columnIndex, in);
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  InputStream getParameter(final data.BLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getBinaryStream(columnIndex);
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  void setParameter(final data.DATE column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalDate value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setDate(parameterIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  void updateColumn(final data.DATE column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalDate value = column.get();
    if (value != null)
      resultSet.updateDate(columnIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  LocalDate getParameter(final data.DATE column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Date value = resultSet.getDate(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDate.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.TIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setTimestamp(parameterIndex, Timestamp.valueOf("1970-01-01 " + Dialect.timeToString(value))); // FIXME: StringBuilder
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalTime value = column.get();
    if (value != null)
      resultSet.updateTimestamp(columnIndex, Timestamp.valueOf("1970-01-01 " + Dialect.timeToString(value))); // FIXME: StringBuilder
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  LocalTime getParameter(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : value.toLocalDateTime().toLocalTime();
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.DATETIME column, final PreparedStatement statement, final int parameterIndex, final boolean isForUpdateWhere) throws SQLException {
    final LocalDateTime value = column.getForUpdateWhereGetOld(isForUpdateWhere);
    if (value != null)
      statement.setTimestamp(parameterIndex, dt.DATETIME.toTimestamp(value));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.DATETIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalDateTime value = column.get();
    if (value != null)
      resultSet.updateTimestamp(columnIndex, dt.DATETIME.toTimestamp(value));
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link data.Column} from the provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  LocalDateTime getParameter(final data.DATETIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate(), value.getHours(), value.getMinutes(), value.getSeconds(), value.getNanos());
  }

  boolean supportsPreparedBatch() {
    return true;
  }

  boolean supportsReturnGeneratedKeysBatch() {
    return true;
  }

  StringBuilder prepareSqlReturning(final StringBuilder sql, final data.Column<?>[] autos) {
    return sql;
  }

  PreparedStatement prepareStatementReturning(final Connection connection, final StringBuilder sql, final data.Column<?>[] autos) throws SQLException {
    return connection.prepareStatement(prepareSqlReturning(sql, autos).toString(), Statement.RETURN_GENERATED_KEYS);
  }

  int executeUpdateReturning(final Statement statement, final StringBuilder sql, final data.Column<?>[] autos) throws SQLException {
    return statement.executeUpdate(prepareSqlReturning(sql, autos).toString(), Statement.RETURN_GENERATED_KEYS);
  }
}