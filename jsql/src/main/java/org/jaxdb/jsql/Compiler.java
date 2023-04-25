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
import java.io.Serializable;
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
import org.libj.io.SerializableInputStream;
import org.libj.io.SerializableReader;
import org.libj.io.Streams;
import org.libj.util.IdentityHashSet;

abstract class Compiler extends DbVendorCompiler {
  private static final Compiler[] compilers = {/*new DB2Compiler(),*/null, new DerbyCompiler(), new MariaDBCompiler(), new MySQLCompiler(), new OracleCompiler(), new PostgreSQLCompiler(), new SQLiteCompiler()};

  static Compiler getCompiler(final DbVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected Compiler(final DbVendor vendor) {
    super(vendor);
  }

  final boolean compileEntities(final type.Entity[] entities, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    boolean isSimple = true;
    for (int i = 0, i$ = entities.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      final type.Entity entity = entities[i];
      isSimple &= compileNextSubject(entity, i, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
    }

    return isSimple;
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

  boolean compileNextSubject(final type.Entity entity, final int index, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    boolean isSimple = true;
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
          isSimple &= alias.compile(compilation, false);
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
      isSimple &= ((Keyword)entity).compile(compilation, false);
      sql.append(')');
    }
    else if (entity instanceof type.Column) {
      final Subject subject = (Subject)entity;
      compilation.registerAlias(subject.getTable());
      final Alias alias;
      final Evaluable wrapped;
      final int start = sql.length();
      if (subject instanceof data.Column && useAliases && isFromGroupBy && (wrapped = ((data.Column<?>)subject).wrapped()) instanceof As && (alias = compilation.getAlias(((As<?>)wrapped).getVariable())) != null)
        isSimple &= alias.compile(compilation, false);
      else
        isSimple &= subject.compile(compilation, false);

      checkTranslateType(sql, translateTypes, subject, index);
      final int end = sql.length();
      if (addToColumnTokens)
        compilation.getColumnTokens().add(sql.substring(start, end));
    }
    else {
      throw new UnsupportedOperationException("Unsupported subject type: " + entity.getClass().getName());
    }

    return isSimple;
  }

  abstract void onRegister(Connection connection) throws SQLException;
  abstract void onConnect(Connection connection) throws SQLException;

  static <T extends type.Entity>Subject toSubject(final T entity) {
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
  boolean compileCaseElse(final data.Column<?> variable, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.sql.append("CASE ");
    return variable.compile(compilation, true);
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param when The {@link Command.CaseImpl.WHEN}.
   * @param compilation The target {@link Compilation}.
   */
  boolean compileWhen(final CaseImpl.Search.WHEN<?> when, final Compilation compilation) {
    compilation.sql.append("CASE");
    return true;
  }

  /**
   * Compile the specified parameters, and append to the provided {@link Compilation}.
   *
   * @param when The {@link Command.CaseImpl.WHEN}.
   * @param then The {@link Command.CaseImpl.THEN}.
   * @param _else The {@link Command.CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @return {@code true} if the compiled clause is "simple", otherwise {@code false}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileWhenThenElse(final Subject when, final data.Column<?> then, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    sql.append(" WHEN ");
    isSimple &= when.compile(compilation, true);
    sql.append(" THEN ");
    isSimple &= then.compile(compilation, true);
    return isSimple;
  }

  /**
   * Compile the specified parameters, and append to the provided {@link Compilation}.
   *
   * @param _else The {@link Command.CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @return {@code true} if the compiled clause is "simple", otherwise {@code false}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileElse(final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(" ELSE ");
    final boolean isSimple = _else.compile(compilation, true);
    sql.append(" END");
    return isSimple;
  }

  boolean compileSelect(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("SELECT ");
    if (select.distinct)
      sql.append("DISTINCT ");

    return compileEntities(select.entities, false, useAliases, select.translateTypes, compilation, true);
  }

  boolean compileFrom(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from() == null)
      return true;

    final StringBuilder sql = compilation.sql;
    sql.append(" FROM ");

    boolean isSimple = true;
    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    final data.Table[] from = select.from();
    for (int i = 0, i$ = from.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      final data.Table table = from[i];
      final Evaluable wrapped = table.wrapped();
      if (wrapped != null) {
        isSimple &= wrapped.compile(compilation, false);
      }
      else {
        tableName(sql, table, compilation);
        if (useAliases) {
          sql.append(' ');
          isSimple &= compilation.getAlias(table).compile(compilation, false);
        }
      }
    }

    return isSimple;
  }

  boolean compileJoin(final Command.Select.untyped.SELECT.JoinKind joinKind, final Object join, final Condition<?> on, final Compilation compilation) throws IOException, SQLException {
    if (join == null)
      return true;

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

    // NOTE: JOIN implies isSimple is false
    return false;
  }

  boolean compileWhere(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    final Condition<?> where = select.where();
    if (where == null)
      return true;

    compilation.sql.append(" WHERE ");
    return where.compile(compilation, false);
  }

  boolean compileGroupByHaving(final Command.Select.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    final data.Entity[] groupBy = select.groupBy;
    if (groupBy != null) {
      sql.append(" GROUP BY ");
      isSimple &= compileEntities(groupBy, true, useAliases, null, compilation, false);
    }

    final Condition<?> having = select.having;
    if (having != null) {
      sql.append(" HAVING ");
      isSimple &= having.compile(compilation, false);
    }

    return isSimple;
  }

  boolean compileOrderBy(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    if (select.orderBy == null && select.orderByIndexes == null)
      return true;

    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    sql.append(" ORDER BY ");
    if (select.orderBy != null) {
      for (int i = 0, i$ = select.orderBy.length; i < i$; ++i) { // [A]
        final data.Column<?> column = select.orderBy[i];
        if (i > 0)
          sql.append(", ");

        if (column.wrapped() instanceof As) {
          // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
          // FIXME: This code is commented-out, because Derby complains when this is done.
          // final Alias alias = compilation.getAlias(((As<?>)column.wrapper()).getVariable());
          // if (alias != null) {
          //   alias.compile(compilation);
          // }
          // else {
          isSimple &= unwrapAlias(column).compile(compilation, false);
          // }
        }
        else {
          compilation.registerAlias(column.getTable());
          isSimple &= column.compile(compilation, false);
        }
      }
    }
    else if (select.orderByIndexes != null) {
      for (int i = 0, i$ = select.orderByIndexes.length; i < i$; ++i) { // [A]
        final int columnIndex = select.orderByIndexes[i];
        if (i > 0)
          sql.append(", ");

        sql.append(String.valueOf(columnIndex));
      }
    }
    else {
      throw new IllegalStateException();
    }

    return isSimple;
  }

  boolean compileLimitOffset(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    final int limit = select.limit;
    if (limit == -1)
      return true;

    final StringBuilder sql = compilation.sql;
    sql.append(" LIMIT ").append(limit);
    final int offset = select.offset;
    if (offset != -1)
      sql.append(" OFFSET ").append(offset);

    return false;
  }

  boolean aliasInForUpdate() {
    return true;
  }

  boolean compileFor(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    if (select.forLockStrength != null) {
      sql.append(" FOR ").append(select.forLockStrength);
      final Subject[] forSubjects = select.forSubjects;
      if (forSubjects != null && forSubjects.length > 0)
        isSimple &= compileForOf(select, compilation);
    }

    if (select.forLockOption != null)
      sql.append(' ').append(select.forLockOption);

    return isSimple;
  }

  boolean compileForOf(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) {
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

      if (tables.size() > 1)
        sql.append(", ");

      q(sql, table.getName());
    }

    return true;
  }

  boolean compileUnion(final Command.Select.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    final ArrayList<Object> unions = select.unions;
    if (unions == null)
      return true;

    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    for (int i = 0, i$ = unions.size(); i < i$;) { // [RA]
      final Boolean all = (Boolean)unions.get(i++);
      final Subject union = (Subject)unions.get(i++);
      sql.append(" UNION ");
      if (all)
        sql.append("ALL ");

      isSimple &= union.compile(compilation, false);
    }

    return isSimple;
  }

  boolean compileInsert(final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
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

    boolean isSimple = true;
    for (int i = 0; i < len; ++i) { // [A]
      final data.Column<?> column = columns[i];
      if (i > 0)
        sql.append(", ");

      if (shouldInsert(column, true, compilation))
        isSimple &= compilation.addParameter(column, false, false);
      else
        sql.append("DEFAULT");
    }

    sql.append(')');
    return isSimple;
  }

  final boolean compileInsert(final data.Table insert, final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    return compileInsert(insert != null ? insert._column$ : columns, ignore, compilation);
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
      column.compile(compilation, false); // FIXME: `isSimple` is not being considered here
      if (column instanceof data.ENUM<?>)
        translateTypes.put(i, (data.ENUM<?>)column);
    }

    sql.append(") ");

    final Command.Select.untyped.SELECT<?> selectImpl = (Command.Select.untyped.SELECT<?>)select;
    final Compilation selectCompilation = compilation.newSubCompilation(selectImpl);
    selectImpl.translateTypes = translateTypes;
    selectImpl.compile(selectCompilation, false); // FIXME: `isSimple` is not being considered here
    sql.append(selectCompilation);
    return selectCompilation;
  }

  abstract boolean compileInsertOnConflict(data.Column<?>[] columns, Select.untyped.SELECT<?> select, data.Column<?>[] onConflict, boolean doUpdate, Compilation compilation) throws IOException, SQLException;

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldInsert(final data.Column column, final boolean modify, final Compilation compilation) {
    if (column.setByCur == data.Column.SetBy.USER)
      return true;

    if (column.generateOnInsert == null || column.generateOnInsert == GenerateOn.AUTO_GENERATED)
      return false;

    if (modify)
      column.generateOnInsert.generate(column, compilation.vendor);

    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldUpdate(final data.Column column, final Compilation compilation) {
    boolean shouldUpdate = column.setByCur == data.Column.SetBy.USER;
    if ((!shouldUpdate || column.keyForUpdate) && column.generateOnUpdate != null) {
      column.generateOnUpdate.generate(column, compilation.vendor);
      shouldUpdate = true;
    }

    if (column.ref != null) {
      shouldUpdate = true;
      compilation.afterExecute(success -> {
        if (success) {
          // NOTE: Column.wasSet must be false, so that the Column.ref can continue to take effect.
          final Serializable evaluated = column.evaluate(new IdentityHashSet<>());
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

  boolean compileUpdate(final data.Table update, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
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
        isSimple &= compilation.addParameter(column, true, false);
        modified = true;
      }
    }

    // No changes were found
    if (!modified)
      throw new IllegalArgumentException("UPDATE does not SET any columns");

    modified = false;
    for (final data.Column<?> column : columns) { // [A]
      if (column.primary || column.keyForUpdate && column.setByCur != null) {
        if (modified)
          sql.append(" AND ");
        else
          sql.append(" WHERE ");

        isSimple &= compilation.addCondition(column, false, true);
        modified = true;
      }
    }

    return isSimple;
  }

  boolean compileUpdate(final data.Table update, final ArrayList<Subject> sets, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("UPDATE ");
    q(sql, update.getName());
    sql.append(" SET ");
    boolean isSimple = true;
    for (int i = 0, i$ = sets.size(); i < i$;) { // [RA]
      if (i > 0)
        sql.append(", ");

      final data.Column<?> column = (data.Column<?>)sets.get(i++);
      final Subject to = sets.get(i++);
      q(sql, column.name).append(" = ");
      if (to == null)
        sql.append("NULL");
      else
        isSimple &= to.compile(compilation, false);
    }

    if (where != null) {
      sql.append(" WHERE ");
      isSimple &= where.compile(compilation, false);
    }

    return isSimple;
  }

  boolean compileDelete(final data.Table delete, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    sql.append("DELETE FROM ");
    q(sql, delete.getName());
    boolean modified = false;
    for (int j = 0, j$ = delete._column$.length; j < j$; ++j) { // [A]
      final data.Column<?> column = delete._column$[j];
      if (column.setByCur == data.Column.SetBy.USER || column.setByCur == data.Column.SetBy.SYSTEM && (column.primary || column.keyForUpdate)) {
        if (modified)
          sql.append(" AND ");
        else
          sql.append(" WHERE ");

        isSimple &= compilation.addCondition(column, false, false);
        modified = true;
      }
    }

    return isSimple;
  }

  boolean compileDelete(final data.Table delete, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("DELETE FROM ");
    q(sql, delete.getName());
    sql.append(" WHERE ");
    return where.compile(compilation, false);
  }

  <D extends data.Entity>boolean compile(final data.Table table, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Evaluable wrapped = table.wrapped();
    if (wrapped != null)
      return wrapped.compile(compilation, isExpression);

    final StringBuilder sql = compilation.sql;
    tableName(sql, table, compilation);
    final Alias alias = compilation.registerAlias(table);
    sql.append(' ');
    return alias.compile(compilation, true);
  }

  boolean compile(final ExpressionImpl.ChangeCase expression, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(expression.o).append('(');
    final boolean isSimple = toSubject(expression.a).compile(compilation, true);
    sql.append(')');
    return isSimple;
  }

  boolean compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    boolean isSimple = true;
    for (int i = 0, i$ = expression.a.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(" || ");

      isSimple &= toSubject(expression.a[i]).compile(compilation, true);
    }

    sql.append(')');
    return isSimple;
  }

  boolean compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    final StringBuilder sql = compilation.sql;
    sql.append("((");
    final boolean isSimple = toSubject(a).compile(compilation, true);
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
    return isSimple;
  }

  boolean compileIntervalAdd(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    return compileInterval(a, "ADD", b, compilation);
  }

  boolean compileIntervalSub(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    return compileInterval(a, "SUB", b, compilation);
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

  boolean compileAs(final As<?> as, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Alias alias = compilation.registerAlias(as.getVariable());
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    final boolean isSimple = as.parent().compile(compilation, true);
    sql.append(')');
    if (!isExpression && as.isExplicit()) {
      sql.append(' ');
      compileAs(sql, as);

      alias.compile(compilation, false);
    }

    return isSimple;
  }

  // FIXME: Move this to a Util class or something
  static <D extends data.Entity>boolean compileCondition(final boolean and, final Condition<?> condition, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    if (condition instanceof BooleanTerm) {
      if (and == ((BooleanTerm)condition).and) {
        isSimple &= condition.compile(compilation, false);
      }
      else {
        final StringBuilder sql = compilation.sql;
        sql.append('(');
        isSimple &= condition.compile(compilation, false);
        sql.append(')');
      }
    }
    else {
      isSimple &= condition.compile(compilation, false);
    }

    return isSimple;
  }

  boolean compileCondition(final BooleanTerm condition, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final String andOr = condition.toString();
    isSimple &= compileCondition(condition.and, condition.a, compilation);
    final StringBuilder sql = compilation.sql;
    sql.append(' ').append(andOr).append(' ');
    isSimple &= compileCondition(condition.and, condition.b, compilation);
    final Condition<?>[] conditions = condition.conditions;
    for (int i = 0, i$ = conditions.length; i < i$; ++i) { // [A]
      sql.append(' ').append(andOr).append(' ');
      isSimple &= compileCondition(condition.and, conditions[i], compilation);
    }

    return isSimple;
  }

  private static Subject unwrapAlias(final Subject subject) {
    if (!(subject instanceof data.Entity))
      return subject;

    final data.Entity entity = (data.Entity)subject;
    final Evaluable wrapped = entity.wrapped();
    return wrapped instanceof As ? ((As<?>)wrapped).parent() : subject;
  }

  boolean compilePredicate(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    if (!compilation.subCompile(predicate.a)) {
      // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
      // FIXME: This code is commented-out, because Derby complains when this is done.
      // final Alias alias;
      // if (predicate.a.wrapper() instanceof As && (alias = compilation.getAlias(((As<?>)predicate.a.wrapper()).getVariable())) != null)
      //   alias.compile(compilation);
      // else
      // FIXME: The braces are really only needed for inner SELECTs. Add the complexity to save the compiled SQL from having an extra couple of braces?!
      final boolean isSelect = predicate.a instanceof Select.untyped.SELECT;

      if (isSelect)
        sql.append('(');

      isSimple &= unwrapAlias(predicate.a).compile(compilation, true);

      if (isSelect)
        sql.append(')');
    }

    sql.append(' ').append(predicate.operator).append(' ');
    if (!compilation.subCompile(predicate.b)) {
      // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
      // FIXME: This code is commented-out, because Derby complains when this is done.
      // final Alias alias;
      // if (predicate.b.wrapper() instanceof As && (alias = compilation.getAlias(((As<?>)predicate.b.wrapper()).getVariable())) != null)
      // alias.compile(compilation);
      //   else
      // FIXME: The braces are really only needed for inner SELECTs. Add the complexity to save the compiled SQL from having an extra couple of braces?!
      final boolean isSelect = predicate.b instanceof Select.untyped.SELECT;

      if (isSelect)
        sql.append('(');

      isSimple &= unwrapAlias(predicate.b).compile(compilation, true);

      if (isSelect)
        sql.append(')');
    }

    return isSimple;
  }

  boolean compileInPredicate(final InPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = toSubject(predicate.column).compile(compilation, true);
    final StringBuilder sql = compilation.sql;
    sql.append(' ');
    if (!predicate.positive)
      sql.append("NOT ");

    sql.append("IN (");
    final Subject[] values = predicate.values;
    for (int i = 0, i$ = values.length; i < i$; ++i) { // [A]
      if (i > 0)
        sql.append(", ");

      isSimple &= values[i].compile(compilation, true);
    }

    sql.append(')');
    return isSimple;
  }

  boolean compileExistsPredicate(final ExistsPredicate predicate, final boolean isPositive, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    if (!isPositive)
      sql.append("NOT ");

    sql.append("EXISTS (");
    final boolean isSimple = predicate.subQuery.compile(compilation, true);
    sql.append(')');
    return isSimple;
  }

  boolean compileLikePredicate(final LikePredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    final boolean isSimple = toSubject(predicate.column).compile(compilation, true);
    sql.append(") ");
    if (!predicate.positive)
      sql.append("NOT ");

    sql.append("LIKE '").append(predicate.pattern).append('\'');
    return isSimple;
  }

  boolean compileQuantifiedComparisonPredicate(final QuantifiedComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(predicate.qualifier).append(" (");
    final boolean isSimple = predicate.subQuery.compile(compilation, true);
    sql.append(')');
    return isSimple;
  }

  boolean compileBetweenPredicate(final BetweenPredicates.BetweenPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append('(');
    final type.Column<?> column = predicate.column;
    final boolean isSimple = toSubject(column).compile(compilation, true);
    sql.append(')');
    if (!predicate.positive)
      sql.append(" NOT");

    sql.append(" BETWEEN ");

    final Subject a = predicate.a();
    final boolean aIsSimple = a.compile(compilation, true);

    sql.append(" AND ");

    final Subject b = predicate.b();
    final boolean bIsSimple = b.compile(compilation, true);

    if (!isSimple || !aIsSimple || !bIsSimple)
      return false;

    if (predicate.positive) {
      compilation.addInterval(column, a, true, b, true);
    }
    else {
      compilation.addInterval(column, null, false, a, false);
      compilation.addInterval(column, b, false, null, false);
    }

    return true;
  }

  boolean compileNullPredicate(final NullPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    final boolean isSimple = toSubject(predicate.column).compile(compilation, true);
    final StringBuilder sql = compilation.sql;
    sql.append(" IS ");
    if (!predicate.is)
      sql.append("NOT ");

    sql.append("NULL");
    return isSimple;
  }

  /**
   * Compile the PI expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  boolean compilePi(final Compilation compilation) {
    compilation.sql.append("PI()");
    return true;
  }

  /**
   * Compile the NOW expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  boolean compileNow(final Compilation compilation) {
    compilation.sql.append("NOW()");
    return false;
  }

  private static boolean compileExpression(final String o, final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(o);
    sql.append('(');
    final boolean isSimple = toSubject(a).compile(compilation, true);
    sql.append(')');
    return isSimple;
  }

  private static boolean compileExpression(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append(o);
    sql.append('(');
    boolean isSimple = toSubject(a).compile(compilation, true);
    sql.append(", ");
    isSimple &= toSubject(b).compile(compilation, true);
    sql.append(')');
    return isSimple;
  }

  private static boolean compileFunction(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("((");
    boolean isSimple = toSubject(a).compile(compilation, true);
    sql.append(')');
    sql.append(' ').append(o).append(' ');
    sql.append('(');
    isSimple &= toSubject(b).compile(compilation, true);
    sql.append("))");
    return isSimple;
  }

  /**
   * Compile the ABS expression, and append to the provided {@link Compilation}.
   *
   * @param a The expression to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileAbs(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ABS", a, compilation);
  }

  /**
   * Compile the SIGN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileSign(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("SIGN", a, compilation);
  }

  /**
   * Compile the ROUND expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileRound(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ROUND", a, compilation);
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
  boolean compileRound(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ROUND", a, b, compilation);
  }

  /**
   * Compile the FLOOR expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileFloor(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("FLOOR", a, compilation);
  }

  /**
   * Compile the CEIL expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileCeil(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("CEIL", a, compilation);
  }

  /**
   * Compile the SQRT expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileSqrt(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("SQRT", a, compilation);
  }

  /**
   * Compile the DEGREES expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileDegrees(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("DEGREES", a, compilation);
  }

  /**
   * Compile the RADIANS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileRadians(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("RADIANS", a, compilation);
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
  boolean compilePow(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("POWER", a, b, compilation);
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
  boolean compileMod(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("MOD", a, b, compilation);
  }

  /**
   * Compile the SIN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileSin(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("SIN", a, compilation);
  }

  /**
   * Compile the ASIN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileAsin(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ASIN", a, compilation);
  }

  /**
   * Compile the COS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileCos(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("COS", a, compilation);
  }

  /**
   * Compile the ACOS expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileAcos(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ACOS", a, compilation);
  }

  /**
   * Compile the TAN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileTan(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("TAN", a, compilation);
  }

  /**
   * Compile the ATAN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileAtan(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ATAN", a, compilation);
  }

  boolean compileSum(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    return compileSet("SUM", a, distinct, compilation);
  }

  boolean compileAvg(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    return compileSet("AVG", a, distinct, compilation);
  }

  boolean compileMax(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    return compileSet("MAX", a, distinct, compilation);
  }

  boolean compileMin(final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    return compileSet("MIN", a, distinct, compilation);
  }

  boolean compileAdd(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileFunction("+", a, b, compilation);
  }

  boolean compileSub(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileFunction("-", a, b, compilation);
  }

  boolean compileMul(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileFunction("*", a, b, compilation);
  }

  boolean compileDiv(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileFunction("/", a, b, compilation);
  }

  boolean compileLower(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LOWER", a, compilation);
  }

  boolean compileUpper(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("UPPER", a, compilation);
  }

  boolean compileLength(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LENGTH", a, compilation);
  }

  boolean compileSubstring(final type.Column<?> col, final type.Column<?> from, final type.Column<?> to, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    sql.append("SUBSTRING");
    sql.append('(');
    isSimple &= toSubject(col).compile(compilation, true);
    if (from != null) {
      sql.append(", ");
      isSimple &= toSubject(from).compile(compilation, true);
    }

    if (to != null) {
      if (from == null) {
        sql.append(", ");
        sql.append('1');
      }

      sql.append(", ");
      isSimple &= toSubject(to).compile(compilation, true);
    }

    sql.append(')');
    return isSimple;
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
  boolean compileAtan2(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("ATAN2", a, b, compilation);
  }

  /**
   * Compile the EXP expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileExp(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("EXP", a, compilation);
  }

  /**
   * Compile the LN expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileLn(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LN", a, compilation);
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
  boolean compileLog(final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LOG", a, b, compilation);
  }

  /**
   * Compile the LOG2 expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileLog2(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LOG2", a, compilation);
  }

  /**
   * Compile the LOG10 expression, and append to the provided {@link Compilation}.
   *
   * @param a The {@link type.Column}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  boolean compileLog10(final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    return compileExpression("LOG10", a, compilation);
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
  boolean compileCount(final data.Entity a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("COUNT").append('(');
    final boolean isSimple;
    if (a instanceof data.Table) {
      sql.append('*');
      isSimple = true;
    }
    else {
      if (distinct)
        sql.append("DISTINCT ");

      isSimple = a.compile(compilation, true);
    }

    sql.append(')');
    return isSimple;
  }

  boolean compileSet(final String o, final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    boolean isSimple = true;
    final StringBuilder sql = compilation.sql;
    sql.append(o).append('(');
    if (a != null) {
      if (distinct)
        sql.append("DISTINCT ");

      isSimple &= toSubject(a).compile(compilation, true);

      // if (expression.b != null) {
      // sql.append(", ");
      // toSubject(expression.b).compile(compilation);
      // }
    }

    sql.append(')');
    return isSimple;
  }

  boolean compileOrder(final OrderingSpec spec, final Compilation compilation) throws IOException, SQLException {
    unwrapAlias(spec.column).compile(compilation, true);
    compilation.sql.append(' ').append(spec.ascending ? "ASC" : "DESC");
    return true;
  }

  boolean compileTemporal(final expression.Temporal expression, final Compilation compilation) {
    compilation.sql.append(expression.function).append("()");
    return true;
  }

  <V extends Serializable>StringBuilder compileArray(final StringBuilder b, final data.ARRAY<? extends V> array, final data.Column<V> column, final boolean isForUpdateWhere) throws IOException {
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

  boolean compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    final StringBuilder sql = compilation.sql;
    sql.append("CAST((");
    final boolean isSimple = toSubject(as.column).compile(compilation, true);
    sql.append(") AS ");
    as.cast.declare(sql, compilation.vendor).append(')');
    return isSimple;
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

  boolean assignAliases(final data.Table[] from, final ArrayList<Object> joins, final Compilation compilation) throws IOException, SQLException {
    if (from != null) {
      for (final data.Table table : from) { // [A]
        // FIXME: Why am I clearing the wrapped entity here?
        table.clearWrap();
        compilation.registerAlias(table);
      }
    }

    if (joins == null)
      return true;

    boolean isSimple = true;
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
        isSimple &= select.compile(subCompilation, false);
      }
      else {
        throw new IllegalStateException();
      }
    }

    return isSimple;
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
  SerializableReader getParameter(final data.CLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Clob value = resultSet.getClob(columnIndex);
    if (value == null)
      return null;

    final Reader in = value.getCharacterStream();
    return in == null ? null : new SerializableReader(in);
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
  SerializableInputStream getParameter(final data.BLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final InputStream in = resultSet.getBinaryStream(columnIndex);
    return in == null ? null : new SerializableInputStream(in);
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
   * Returns the parameter of the specified {@link data.Column} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param column The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code column} from the provided
   *         {@link ResultSet} at the given column index.
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