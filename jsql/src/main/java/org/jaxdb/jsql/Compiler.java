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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorBase;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;
import org.libj.lang.PackageLoader;
import org.libj.util.IdentityHashSet;
import org.libj.util.function.Throwing;

abstract class Compiler extends DBVendorBase {
  private static final Compiler[] compilers = new Compiler[DBVendor.values().length];

  static {
    try {
      PackageLoader.getContextPackageLoader().loadPackage(Compiler.class.getPackage(), Throwing.<Class<?>>rethrow(c -> {
        if (Compiler.class.isAssignableFrom(c) && !Modifier.isAbstract(c.getModifiers())) {
          final Compiler compiler = (Compiler)c.getDeclaredConstructor().newInstance();
          compilers[compiler.getVendor().ordinal()] = compiler;
        }

        return false;
      }));
    }
    catch (final Exception e) {
      if (e instanceof InvocationTargetException) {
        if (e.getCause() instanceof RuntimeException)
          throw (RuntimeException)e.getCause();

        throw new ExceptionInInitializerError(e.getCause());
      }

      throw new ExceptionInInitializerError(e);
    }
  }

  static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected Compiler(final DBVendor vendor) {
    super(vendor);
  }

  final void compileEntities(final type.Entity<?>[] entities, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    for (int i = 0; i < entities.length; ++i) {
      if (i > 0)
        compilation.comma();

      final Subject subject = (Subject)entities[i];
      compileNextSubject(subject, i, isFromGroupBy, useAliases, translateTypes, compilation, addToColumnTokens);
    }
  }

  /**
   * Default no-op implementation of method to compile an enum translation
   * phrase.
   *
   * @param from The source enum.
   * @param to The target enum.
   * @return Enum translation phrase.
   */
  String translateEnum(final data.ENUM<?> from, final data.ENUM<?> to) {
    return "";
  }

  private void checkTranslateType(final Map<Integer,data.ENUM<?>> translateTypes, final Subject column, final int index, final Compilation compilation) {
    if (column instanceof data.ENUM<?> && translateTypes != null) {
      final data.ENUM<?> translateType = translateTypes.get(index);
      if (translateType != null)
        compilation.concat(translateEnum((data.ENUM<?>)column, translateType));
    }
  }

  void compileNextSubject(final Subject subject, final int index, final boolean isFromGroupBy, final boolean useAliases, final Map<Integer,data.ENUM<?>> translateTypes, final Compilation compilation, final boolean addToColumnTokens) throws IOException, SQLException {
    if (subject instanceof data.Table) {
      final data.Table<?> table = (data.Table<?>)subject;
      final Alias alias = compilation.registerAlias(table);
      for (int c = 0; c < table._column$.length; ++c) {
        final data.Column<?> column = table._column$[c];
        if (c > 0)
          compilation.comma();

        if (useAliases) {
          alias.compile(compilation, false);
          compilation.append('.');
        }

        compilation.append(q(column.name));
        checkTranslateType(translateTypes, column, c, compilation);
        if (addToColumnTokens)
          compilation.getColumnTokens().add(compilation.tokens.get(compilation.tokens.size() - 1).toString());
      }
    }
    else if (subject instanceof Keyword) {
      compilation.append('(');
      ((Keyword<?>)subject).compile(compilation, false);
      compilation.append(')');
    }
    else if (subject instanceof type.Column) {
      compilation.registerAlias(subject.getTable());
      final Alias alias;
      final Evaluable wrapped;
      if (subject instanceof data.Column && useAliases && isFromGroupBy && (wrapped = ((data.Column<?>)subject).wrapped()) instanceof As && (alias = compilation.getAlias(((As<?>)wrapped).getVariable())) != null)
        alias.compile(compilation, false);
      else
        subject.compile(compilation, false);

      checkTranslateType(translateTypes, subject, index, compilation);
      if (addToColumnTokens)
        compilation.getColumnTokens().add(compilation.tokens.get(compilation.tokens.size() - 1).toString());
    }
    else {
      throw new UnsupportedOperationException("Unsupported subject type: " + subject.getClass().getName());
    }
  }

  abstract void onRegister(Connection connection) throws SQLException;
  abstract void onConnect(Connection connection) throws SQLException;

  static <T extends type.Entity<?>>Subject toSubject(final T entity) {
    return (Subject)entity;
  }

  /**
   * Returns the quoted name of the specified {@link data.Table}.
   *
   * @param table The {@link data.Table}.
   * @param compilation The {@link Compilation}
   * @return The quoted name of the specified {@link data.Table}.
   */
  String tableName(final data.Table<?> table, final Compilation compilation) {
    return q(table.getName());
  }

  /**
   * Get the parameter mark for {@link PreparedStatement}s.
   *
   * @param column The {@link data.Column} for the requested mark.
   * @return The mark.
   */
  String getPreparedStatementMark(final data.Column<?> column) {
    return "?";
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param variable The variable to evaluate.
   * @param _else The {@link CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileCaseElse(final data.Column<?> variable, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CASE ");
    variable.compile(compilation, true);
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param when The {@link CaseImpl.WHEN}.
   * @param compilation The target {@link Compilation}.
   */
  void compileWhen(final CaseImpl.Search.WHEN<?> when, final Compilation compilation) {
    compilation.append("CASE");
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param when The {@link CaseImpl.WHEN}.
   * @param then The {@link CaseImpl.THEN}.
   * @param _else The {@link CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileWhenThenElse(final Subject when, final data.Column<?> then, final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.append(" WHEN ");
    when.compile(compilation, true);
    compilation.append(" THEN ");
    then.compile(compilation, true);
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param _else The {@link CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void compileElse(final data.Column<?> _else, final Compilation compilation) throws IOException, SQLException {
    compilation.append(" ELSE ");
    _else.compile(compilation, true);
    compilation.append(" END");
  }

  void compileSelect(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    compilation.append("SELECT ");
    if (select.distinct)
      compilation.append("DISTINCT ");

    compileEntities(select.entities, false, useAliases, select.translateTypes, compilation, true);
  }

  void compileFrom(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.from() == null)
      return;

    compilation.append(" FROM ");

    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    final data.Table<?>[] from = select.from();
    for (int i = 0; i < from.length; ++i) {
      if (i > 0)
        compilation.comma();

      final data.Table<?> table = from[i];
      if (table.wrapped() != null) {
        table.wrapped().compile(compilation, false);
      }
      else {
        compilation.append(tableName(table, compilation));
        if (useAliases) {
          compilation.append(' ');
          compilation.getAlias(table).compile(compilation, false);
        }
      }
    }
  }

  void compileJoin(final SelectImpl.untyped.SELECT.JoinKind joinKind, final Object join, final Condition<?> on, final Compilation compilation) throws IOException, SQLException {
    if (join != null) {
      compilation.append(joinKind);
      compilation.append(" JOIN ");
      if (join instanceof data.Table) {
        final data.Table<?> table = (data.Table<?>)join;
        compilation.append(tableName(table, compilation)).append(' ');
        compilation.registerAlias(table).compile(compilation, false);
        if (on != null) {
          compilation.append(" ON (");
          on.compile(compilation, false);
          compilation.append(')');
        }
      }
      else if (join instanceof SelectImpl.untyped.SELECT) {
        final SelectImpl.untyped.SELECT<?> select = (SelectImpl.untyped.SELECT<?>)join;
        compilation.append('(');
        final Compilation subCompilation = compilation.getSubCompilation(select);
        final Alias alias = compilation.getAlias(select);
        compilation.append(subCompilation);
        compilation.append(") ");
        compilation.append(alias);
        if (on != null) {
          compilation.append(" ON (");
          on.compile(compilation, false);
          compilation.subCompile(on);
          compilation.append(')');
        }
      }
      else {
        throw new IllegalStateException();
      }
    }
  }

  void compileWhere(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    if (select.where() != null) {
      compilation.append(" WHERE ");
      select.where().compile(compilation, false);
    }
  }

  void compileGroupByHaving(final SelectImpl.untyped.SELECT<?> select, final boolean useAliases, final Compilation compilation) throws IOException, SQLException {
    if (select.groupBy != null) {
      compilation.append(" GROUP BY ");
      compileEntities(select.groupBy, true, useAliases, null, compilation, false);
    }

    if (select.having != null) {
      compilation.append(" HAVING ");
      select.having.compile(compilation, false);
    }
  }

  void compileOrderBy(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    if (select.orderBy != null || select.orderByIndexes != null) {
      compilation.append(" ORDER BY ");
      if (select.orderBy != null) {
        for (int i = 0; i < select.orderBy.length; ++i) {
          final data.Column<?> column = select.orderBy[i];
          if (i > 0)
            compilation.comma();

          if (column.wrapped() instanceof As) {
            // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
            // FIXME: This code is commented-out, because Derby complains when this is done.
            // final Alias alias = compilation.getAlias(((As<?>)column.wrapper()).getVariable());
            // if (alias != null) {
            //   alias.compile(compilation);
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
      else if (select.orderByIndexes != null) {
        for (int i = 0; i < select.orderByIndexes.length; ++i) {
          final int columnIndex = select.orderByIndexes[i];
          if (i > 0)
            compilation.comma();

          compilation.append(String.valueOf(columnIndex));
        }
      }
      else {
        throw new IllegalStateException();
      }
    }
  }

  void compileLimitOffset(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.limit != -1) {
      compilation.append(" LIMIT " + select.limit);
      if (select.offset != -1)
        compilation.append(" OFFSET " + select.offset);
    }
  }

  boolean aliasInForUpdate() {
    return true;
  }

  void compileFor(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    if (select.forLockStrength != null) {
      compilation.append(" FOR ").append(select.forLockStrength);
      if (select.forSubjects != null && select.forSubjects.length > 0)
        compileForOf(select, compilation);
    }

    if (select.forLockOption != null)
      compilation.append(' ').append(select.forLockOption);
  }

  void compileForOf(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) {
    compilation.append(" OF ");
    final HashSet<data.Table<?>> tables = new HashSet<>(1);
    for (int i = 0; i < select.forSubjects.length; ++i) {
      final data.Entity<?> entity = select.forSubjects[i];
      final data.Table<?> table;
      if (entity instanceof data.Table)
        table = (data.Table<?>)entity;
      else if (entity instanceof data.Column)
        table = ((data.Column<?>)entity).getTable();
      else
        throw new UnsupportedOperationException("Unsupported type.Entity: " + entity.getClass().getName());

      if (!tables.add(table))
        continue;

      if (tables.size() > 1)
        compilation.comma();

      compilation.append(q(table.getName()));
    }
  }

  void compileUnion(final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException, SQLException {
    if (select.unions != null) {
      for (int i = 0, len = select.unions.size(); i < len;) {
        final Boolean all = (Boolean)select.unions.get(i++);
        final Subject union = (Subject)select.unions.get(i++);
        compilation.append(" UNION ");
        if (all)
          compilation.append("ALL ");

        union.compile(compilation, false);
      }
    }
  }

  void compileInsert(final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    compilation.append("INSERT ");
    if (ignore)
      compilation.append("IGNORE ");

    compilation.append("INTO ");
    compilation.append(q(columns[0].getTable().getName())).append(" (");
    for (int i = 0; i < columns.length; ++i) {
      final data.Column<?> column = columns[i];
      if (i > 0)
        compilation.comma();

      compilation.append(q(column.name));
    }

    compilation.append(") VALUES (");

    for (int i = 0; i < columns.length; ++i) {
      final data.Column<?> column = columns[i];
      if (i > 0)
        compilation.comma();

      if (shouldInsert(column, true, compilation))
        compilation.addParameter(column, false);
      else
        compilation.append("DEFAULT");
    }

    compilation.append(')');
  }

  final void compileInsert(final data.Table<?> insert, final data.Column<?>[] columns, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    compileInsert(insert != null ? insert._column$ : columns, ignore, compilation);
  }

  Compilation compileInsertSelect(final data.Column<?>[] columns, final Select.untyped.SELECT<?> select, final boolean ignore, final Compilation compilation) throws IOException, SQLException {
    final HashMap<Integer,data.ENUM<?>> translateTypes = new HashMap<>();
    compilation.append("INSERT ");
    if (ignore)
      compilation.append("IGNORE ");

    compilation.append("INTO ");
    compilation.append(q(columns[0].getTable().getName()));
    compilation.append(" (");
    for (int i = 0; i < columns.length; ++i) {
      if (i > 0)
        compilation.comma();

      final data.Column<?> column = columns[i];
      column.compile(compilation, false);
      if (column instanceof data.ENUM<?>)
        translateTypes.put(i, (data.ENUM<?>)column);
    }

    compilation.append(") ");

    final SelectImpl.untyped.SELECT<?> selectImpl = (SelectImpl.untyped.SELECT<?>)select;
    final Compilation selectCompilation = compilation.newSubCompilation(selectImpl);
    selectImpl.translateTypes = translateTypes;
    selectImpl.compile(selectCompilation, false);
    compilation.append(selectCompilation);
    return selectCompilation;
  }

  abstract void compileInsertOnConflict(data.Column<?>[] columns, Select.untyped.SELECT<?> select, data.Column<?>[] onConflict, boolean doUpdate, Compilation compilation) throws IOException, SQLException;

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldInsert(final data.Column column, final boolean modify, final Compilation compilation) {
    if (column.wasSet())
      return true;

    if (column.generateOnInsert == null || column.generateOnInsert == GenerateOn.AUTO_GENERATED)
      return false;

    if (modify)
      column.generateOnInsert.generate(column, compilation.vendor);

    return true;
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  static boolean shouldUpdate(final data.Column column, final Compilation compilation) {
    if (column.primary)
      return false;

    boolean shouldUpdate = column.wasSet();
    if ((!shouldUpdate || column.keyForUpdate) && column.generateOnUpdate != null) {
      column.generateOnUpdate.generate(column, compilation.vendor);
      shouldUpdate = true;
    }

    if (column.ref != null) {
      shouldUpdate = true;
      compilation.afterExecute(success -> {
        if (success) {
          // NOTE: Column.wasSet must be false, so that the Column.ref can continue to take effect.
          final Object evaluated = column.evaluate(new IdentityHashSet<>());
          if (evaluated == null)
            column.setValue(null);
          else if (column.type() == evaluated.getClass())
            column.setValue(evaluated);
          else if (evaluated instanceof Number && Number.class.isAssignableFrom(column.type()))
            column.setValue(data.Numeric.valueOf((Number)evaluated, (Class<? extends Number>)column.type()));
          else
            throw new IllegalStateException("Value is greater than maximum value of type " + data.Column.getSimpleName(column.getClass()) + ": " + evaluated);
        }
      });
    }

    return shouldUpdate;
  }

  void compileUpdate(final data.Table<?> update, final Compilation compilation) throws IOException, SQLException {
    compilation.append("UPDATE ");
    compilation.append(q(update.getName()));
    compilation.append(" SET ");
    boolean modified = false;
    for (int c = 0; c < update._column$.length; ++c) {
      final data.Column<?> column = update._column$[c];
      if (shouldUpdate(column, compilation)) {
        if (modified)
          compilation.comma();

        compilation.append(q(column.name)).append(" = ");
        compilation.addParameter(column, true);
        modified = true;
      }
    }

    // No changes were found
    if (!modified)
      return;

    modified = false;
    for (final data.Column<?> column : update._column$) {
      if (column.primary || column.keyForUpdate && column.wasSet) {
        if (modified)
          compilation.append(" AND ");
        else
          compilation.append(" WHERE ");

        compilation.addCondition(column, false);
        modified = true;
      }
    }
  }

  void compileUpdate(final data.Table<?> update, final List<Subject> sets, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    compilation.append("UPDATE ");
    compilation.append(q(update.getName()));
    compilation.append(" SET ");
    for (int i = 0, len = sets.size(); i < len;) {
      if (i > 0)
        compilation.comma();

      final data.Column<?> column = (data.Column<?>)sets.get(i++);
      final Subject to = sets.get(i++);
      compilation.append(q(column.name)).append(" = ");
      if (to == null)
        compilation.append("NULL");
      else
        to.compile(compilation, false);
    }

    if (where != null) {
      compilation.append(" WHERE ");
      where.compile(compilation, false);
    }
  }

  void compileDelete(final data.Table<?> delete, final Compilation compilation) throws IOException, SQLException {
    compilation.append("DELETE FROM ");
    compilation.append(q(delete.getName()));
    boolean modified = false;
    for (int j = 0; j < delete._column$.length; ++j) {
      final data.Column<?> column = delete._column$[j];
      if (column.wasSet()) {
        if (modified)
          compilation.append(" AND ");
        else
          compilation.append(" WHERE ");

        compilation.addCondition(column, false);
        modified = true;
      }
    }
  }

  void compileDelete(final data.Table<?> delete, final Condition<?> where, final Compilation compilation) throws IOException, SQLException {
    compilation.append("DELETE FROM ");
    compilation.append(q(delete.getName()));
    compilation.append(" WHERE ");
    where.compile(compilation, false);
  }

  <D extends data.Entity<?>>void compile(final data.Table<?> table, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    if (table.wrapped() != null) {
      table.wrapped().compile(compilation, isExpression);
    }
    else {
      compilation.append(tableName(table, compilation));
      final Alias alias = compilation.registerAlias(table);
      compilation.append(' ');
      alias.compile(compilation, true);
    }
  }

  void compile(final ExpressionImpl.ChangeCase expression, final Compilation compilation) throws IOException, SQLException {
    compilation.append(expression.o).append('(');
    toSubject(expression.a).compile(compilation, true);
    compilation.append(')');
  }

  void compile(final ExpressionImpl.Concat expression, final Compilation compilation) throws IOException, SQLException {
    compilation.append('(');
    for (int i = 0; i < expression.a.length; ++i) {
      if (i > 0)
        compilation.append(" || ");

      toSubject(expression.a[i]).compile(compilation, true);
    }

    compilation.append(')');
  }

  void compileInterval(final type.Column<?> a, final String o, final Interval b, final Compilation compilation) throws IOException, SQLException {
    // FIXME: {@link Interval#compile(Compilation,boolean)}
    compilation.append("((");
    toSubject(a).compile(compilation, true);
    compilation.append(") ");
    compilation.append(o);
    compilation.append(" (");
    compilation.append("INTERVAL '");
    final List<TemporalUnit> units = b.getUnits();
    for (int i = 0, len = units.size(); i < len; ++i) {
      if (i > 0)
        compilation.append(' ');

      final TemporalUnit unit = units.get(i);
      compilation.append(b.get(unit)).append(' ').append(unit);
    }

    compilation.append('\'');
    compilation.append("))");
  }

  void compileIntervalAdd(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "ADD", b, compilation);
  }

  void compileIntervalSub(final type.Column<?> a, final Interval b, final Compilation compilation) throws IOException, SQLException {
    compileInterval(a, "SUB", b, compilation);
  }

  static void compile(final data.Column<?> column, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    if (column.wrapped() == null) {
      if (column.getTable() != null) {
        Alias alias = compilation.getAlias(column.getTable());
        if (alias != null) {
          alias.compile(compilation, false);
          compilation.concat("." + compilation.vendor.getDialect().quoteIdentifier(column.name));
        }
        else if (!compilation.subCompile(column.getTable())) {
          compilation.append(compilation.vendor.getDialect().quoteIdentifier(column.name));
        }
        else {
          return;
        }
      }
      else {
        compilation.addParameter(column, false);
      }
    }
    else if (!compilation.subCompile(column)) {
      column.wrapped().compile(compilation, isExpression);
    }
  }

  /**
   * Compile the specified {@link Alias}, and append to the provided {@link Compilation}.
   *
   * @param alias The {@link Alias}.
   * @param compilation The target {@link Compilation}.
   */
  void compile(final Alias alias, final Compilation compilation) {
    compilation.append(alias.name);
  }

  /**
   * Returns the string representation of the specified {@link As}.
   *
   * @param as The {@link As}.
   * @return The string representation of the specified {@link As}.
   */
  String compileAs(final As<?> as) {
    return "AS";
  }

  void compileAs(final As<?> as, final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
    final Alias alias = compilation.registerAlias(as.getVariable());
    compilation.append('(');
    as.parent().compile(compilation, true);
    compilation.append(')');
    if (!isExpression && as.isExplicit()) {
      final String string = compileAs(as);
      compilation.append(' ');
      if (string != null && string.length() != 0)
        compilation.append(string).append(' ');

      alias.compile(compilation, false);
    }
  }

  // FIXME: Move this to a Util class or something
  static <D extends data.Entity<?>> void formatBraces(final boolean and, final Condition<?> condition, final Compilation compilation) throws IOException, SQLException {
    if (condition instanceof BooleanTerm) {
      if (and == ((BooleanTerm)condition).and) {
        condition.compile(compilation, false);
      }
      else {
        compilation.append('(');
        condition.compile(compilation, false);
        compilation.append(')');
      }
    }
    else {
      condition.compile(compilation, false);
    }
  }

  void compileCondition(final BooleanTerm condition, final Compilation compilation) throws IOException, SQLException {
    final String string = condition.toString();
    formatBraces(condition.and, condition.a, compilation);
    compilation.append(' ').append(string).append(' ');
    formatBraces(condition.and, condition.b, compilation);
    for (int i = 0; i < condition.conditions.length; ++i) {
      compilation.append(' ').append(string).append(' ');
      formatBraces(condition.and, condition.conditions[i], compilation);
    }
  }

  private static Subject unwrapAlias(final Subject subject) {
    if (!(subject instanceof data.Entity))
      return subject;

    final data.Entity<?> entity = (data.Entity<?>)subject;
    if (!(entity.wrapped() instanceof As))
      return subject;

    return ((As<?>)entity.wrapped()).parent();
  }

  void compilePredicate(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
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
        compilation.append('(');

        unwrapAlias(predicate.a).compile(compilation, true);

      if (isSelect)
        compilation.append(')');
    }

    compilation.append(' ').append(predicate.operator).append(' ');
    if (!compilation.subCompile(predicate.b)) {
      // FIXME: This commented-out code replaces the variables in the comparison to aliases in case an AS is used.
      // FIXME: This code is commented-out, because Derby complains when this is done.
      // final Alias alias;
      // if (predicate.b.wrapper() instanceof As && (alias = compilation.getAlias(((As<?>)predicate.b.wrapper()).getVariable())) != null)
      // alias.compile(compilation);
      //   else
      // FIXME: The braces are really only needed for inner SELECTs. Add the complexity to save the compiled SQL from having an extra couple of braces?!
      final boolean isSelect = predicate.a instanceof Select.untyped.SELECT;

      if (isSelect)
        compilation.append('(');

        unwrapAlias(predicate.b).compile(compilation, true);

      if (isSelect)
        compilation.append(')');
    }
  }

  void compileInPredicate(final InPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    toSubject(predicate.column).compile(compilation, true);
    compilation.append(' ');
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("IN (");
    for (int i = 0; i < predicate.values.length; ++i) {
      if (i > 0)
        compilation.comma();

      predicate.values[i].compile(compilation, true);
    }

    compilation.append(')');
  }

  void compileExistsPredicate(final ExistsPredicate predicate, final boolean isPositive, final Compilation compilation) throws IOException, SQLException {
    if (!isPositive)
      compilation.append("NOT ");

    compilation.append("EXISTS (");
    predicate.subQuery.compile(compilation, true);
    compilation.append(')');
  }

  void compileLikePredicate(final LikePredicate predicate, final Compilation compilation) throws IOException, SQLException {
    compilation.append('(');
    toSubject(predicate.column).compile(compilation, true);
    compilation.append(") ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("LIKE '").append(predicate.pattern).append('\'');
  }

  void compileQuantifiedComparisonPredicate(final QuantifiedComparisonPredicate<?> predicate, final Compilation compilation) throws IOException, SQLException {
    compilation.append(predicate.qualifier).append(" (");
    predicate.subQuery.compile(compilation, true);
    compilation.append(')');
  }

  void compileBetweenPredicate(final BetweenPredicates.BetweenPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    compilation.append('(');
    toSubject(predicate.column).compile(compilation, true);
    compilation.append(')');
    if (!predicate.positive)
      compilation.append(" NOT");

    compilation.append(" BETWEEN ");
    predicate.a().compile(compilation, true);
    compilation.append(" AND ");
    predicate.b().compile(compilation, true);
  }

  <T> void compileNullPredicate(final NullPredicate predicate, final Compilation compilation) throws IOException, SQLException {
    toSubject(predicate.column).compile(compilation, true);
    compilation.append(" IS ");
    if (!predicate.is)
      compilation.append("NOT ");

    compilation.append("NULL");
  }

  /**
   * Compile the PI expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  void compilePi(final Compilation compilation) {
    compilation.append("PI()");
  }

  /**
   * Compile the NOW expression, and append to the provided {@link Compilation}.
   *
   * @param compilation The target {@link Compilation}.
   */
  void compileNow(final Compilation compilation) {
    compilation.append("NOW()");
  }

  private static void compileExpression(final String o, final type.Column<?> a, final Compilation compilation) throws IOException, SQLException {
    compilation.append(o);
    compilation.append('(');
    toSubject(a).compile(compilation, true);
    compilation.append(')');
  }

  private static void compileExpression(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.append(o);
    compilation.append('(');
    toSubject(a).compile(compilation, true);
    compilation.comma();
    toSubject(b).compile(compilation, true);
    compilation.append(')');
  }

  private static void compileFunction(final String o, final type.Column<?> a, final type.Column<?> b, final Compilation compilation) throws IOException, SQLException {
    compilation.append("((");
    toSubject(a).compile(compilation, true);
    compilation.append(')');
    compilation.append(' ').append(o).append(' ');
    compilation.append('(');
    toSubject(b).compile(compilation, true);
    compilation.append("))");
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
   * Compile the SIGN expression, and append to the provided
   * {@link Compilation}.
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
  void compileCount(final type.Entity<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compilation.append("COUNT").append('(');
    if (a instanceof data.Table) {
      compilation.append('*');
    }
    else {
      if (distinct)
        compilation.append("DISTINCT ");

      toSubject(a).compile(compilation, true);
    }

    compilation.append(')');
  }

  void compileSet(final String o, final type.Column<?> a, final boolean distinct, final Compilation compilation) throws IOException, SQLException {
    compilation.append(o).append('(');
    if (a != null) {
      if (distinct)
        compilation.append("DISTINCT ");

      toSubject(a).compile(compilation, true);

      // if (expression.b != null) {
      // compilation.comma();
      // toSubject(expression.b).compile(compilation);
      // }
    }

    compilation.append(')');
  }

  void compileOrder(final OrderingSpec spec, final Compilation compilation) throws IOException, SQLException {
    unwrapAlias(spec.column).compile(compilation, true);
    compilation.append(' ').append(spec.ascending ? "ASC" : "DESC");
  }

  void compileTemporal(final expression.Temporal expression, final Compilation compilation) {
    compilation.append(expression.function).append("()");
  }

  <V>String compileArray(final data.ARRAY<? extends V> array, final data.Column<V> column) throws IOException {
    final StringBuilder builder = new StringBuilder("(");
    final data.Column<V> clone = column.clone();
    final V[] items = array.get();
    for (int i = 0; i < items.length; ++i) {
      clone.setValue(items[i]);
      if (i > 0)
        builder.append(", ");

      builder.append(data.Column.compile(column, getVendor()));
    }

    return builder.append(')').toString();
  }

  void compileCast(final Cast.AS as, final Compilation compilation) throws IOException, SQLException {
    compilation.append("CAST((");
    toSubject(as.column).compile(compilation, true);
    compilation.append(") AS ").append(as.cast.declare(compilation.vendor)).append(')');
  }

  String compileCast(final data.Column<?> column, final Compilation compilation) {
    return column.declare(compilation.vendor);
  }

  String compileColumn(final data.BIGINT column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  final String compileColumn(final data.BINARY column) {
    return column.isNull() ? "NULL" : getDialect().binaryToStringLiteral(column.get());
  }

  String compileColumn(final data.BLOB column) throws IOException {
    try (final InputStream in = column.get()) {
      return in == null ? "NULL" : getDialect().binaryToStringLiteral(Streams.readBytes(in));
    }
  }

  String compileColumn(final data.BOOLEAN column) {
    return String.valueOf(column.get()).toUpperCase();
  }

  String compileColumn(final data.CHAR column) {
    return column.isNull() ? "NULL" : "'" + column.get().replace("'", "''") + "'";
  }

  String compileColumn(final data.CLOB column) throws IOException {
    try (final Reader in = column.get()) {
      return in == null ? "NULL" : "'" + Readers.readFully(in) + "'";
    }
  }

  String compileColumn(final data.DATE column) {
    return column.isNull() ? "NULL" : "'" + Dialect.dateToString(column.get()) + "'";
  }

  String compileColumn(final data.DATETIME column) {
    return column.isNull() ? "NULL" : "'" + Dialect.dateTimeToString(column.get()) + "'";
  }

  String compileColumn(final data.DECIMAL column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.DOUBLE column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.ENUM<?> column) {
    return column.isNull() ? "NULL" : "'" + column.get() + "'";
  }

  String compileColumn(final data.FLOAT column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.INT column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.SMALLINT column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.TINYINT column) {
    return column.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(column.get());
  }

  String compileColumn(final data.TIME column) {
    return column.isNull() ? "NULL" : "'" + Dialect.timeToString(column.get()) + "'";
  }

  void assignAliases(final data.Table<?>[] from, final List<Object> joins, final Compilation compilation) throws IOException, SQLException {
    if (from != null) {
      for (final data.Table<?> table : from) {
        // FIXME: Why am I clearing the wrapped entity here?
        table.clearWrap();
        compilation.registerAlias(table);
      }
    }

    if (joins != null) {
      for (int i = 0, len = joins.size(); i < len;) {
        final SelectImpl.untyped.SELECT.JoinKind joinKind = (SelectImpl.untyped.SELECT.JoinKind)joins.get(i++);
        final Subject join = (Subject)joins.get(i++);
        if (join instanceof data.Table) {
          final data.Table<?> table = (data.Table<?>)join;
          // FIXME: Why am I clearing the wrapped entity here?
          table.clearWrap();
          compilation.registerAlias(table);
        }
        else if (join instanceof SelectImpl.untyped.SELECT) {
          final SelectImpl.untyped.SELECT<?> select = (SelectImpl.untyped.SELECT<?>)join;
          final Compilation subCompilation = compilation.newSubCompilation(select);
          compilation.registerAlias(select);
          select.compile(subCompilation, false);
        }
        else {
          throw new IllegalStateException();
        }
      }
    }
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
  void setParameter(final data.CHAR column, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    if (column.isNull())
      statement.setNull(parameterIndex, column.sqlType());
    else
      statement.setString(parameterIndex, column.get());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
  void setParameter(final data.CLOB column, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    final Reader in = column.get();
    if (in != null)
      statement.setClob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
  Reader getParameter(final data.CLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Clob value = resultSet.getClob(columnIndex);
    return value == null ? null : value.getCharacterStream();
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
  void setParameter(final data.BLOB column, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    final InputStream in = column.get();
    if (in == null)
      statement.setBlob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, Types.BLOB);
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
  InputStream getParameter(final data.BLOB column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getBinaryStream(columnIndex);
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
  @SuppressWarnings("deprecation")
  void setParameter(final data.DATE column, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDate value = column.get();
    if (value != null)
      statement.setDate(parameterIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final data.TIME column, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = column.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, Timestamp.valueOf("1970-01-01 " + Dialect.timeToString(value)));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param column The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalTime value = column.get();
    if (value != null)
      resultSet.updateTimestamp(columnIndex, Timestamp.valueOf("1970-01-01 " + Dialect.timeToString(value)));
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
  LocalTime getParameter(final data.TIME column, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : value.toLocalDateTime().toLocalTime();
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
  void setParameter(final data.DATETIME column, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = column.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, dt.DATETIME.toTimestamp(value));
    else
      statement.setNull(parameterIndex, column.sqlType());
  }

  /**
   * Sets the specified {@link data.Column} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
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

  String prepareSqlReturning(final String sql, final data.Column<?>[] autos) {
    return sql;
  }

  PreparedStatement prepareStatementReturning(final Connection connection, final String sql, final data.Column<?>[] autos) throws SQLException {
    return connection.prepareStatement(prepareSqlReturning(sql, autos), Statement.RETURN_GENERATED_KEYS);
  }

  int executeUpdateReturning(final Statement statement, final String sql, final data.Column<?>[] autos) throws SQLException {
    return statement.executeUpdate(prepareSqlReturning(sql, autos), Statement.RETURN_GENERATED_KEYS);
  }
}