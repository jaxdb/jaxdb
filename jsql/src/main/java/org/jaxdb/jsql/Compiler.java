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
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jaxdb.ddlx.dt;
import org.jaxdb.vendor.DBVendor;
import org.jaxdb.vendor.DBVendorSpecific;
import org.jaxdb.vendor.Dialect;
import org.libj.io.Readers;
import org.libj.io.Streams;
import org.libj.lang.Hexadecimal;
import org.libj.lang.PackageLoader;
import org.libj.util.IdentityHashSet;
import org.libj.util.function.Throwing;

abstract class Compiler extends DBVendorSpecific {
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

  void compileEntities(final kind.Subject<?>[] entities, final Keyword<?> source, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation) throws IOException {
    for (int i = 0; i < entities.length; ++i) {
      if (i > 0)
        compilation.append(", ");

      final kind.Subject<?> subject = entities[i];
      compileNextSubject(subject, i, source, translateTypes, compilation);
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
  String translateEnum(final type.ENUM<?> from, final type.ENUM<?> to) {
    return "";
  }

  private void checkTranslateType(final Map<Integer,type.ENUM<?>> translateTypes, final type.DataType<?> column, final int index, final Compilation compilation) {
    if (column instanceof type.ENUM<?> && translateTypes != null) {
      final type.ENUM<?> translateType = translateTypes.get(index);
      if (translateType != null)
        compilation.append(translateEnum((type.ENUM<?>)column, translateType));
    }
  }

  void compileNextSubject(final kind.Subject<?> subject, final int index, final Keyword<?> source, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation) throws IOException {
    if (subject instanceof type.Entity) {
      final type.Entity entity = (type.Entity)subject;
      final Alias alias = compilation.registerAlias(entity);
      for (int c = 0; c < entity._column$.length; ++c) {
        final type.DataType<?> column = entity._column$[c];
        if (c > 0)
          compilation.append(", ");

        alias.compile(compilation);
        compilation.append('.').append(compilation.vendor.getDialect().quoteIdentifier(column.name));
        checkTranslateType(translateTypes, column, c, compilation);
      }
    }
    else if (subject instanceof type.DataType) {
      final type.DataType<?> column = (type.DataType<?>)subject;
      compilation.registerAlias(column.owner);
      column.compile(compilation);
      checkTranslateType(translateTypes, column, index, compilation);
    }
    else if (subject instanceof Keyword) {
      compilation.append('(');
      ((Keyword<?>)subject).compile(compilation);
      compilation.append(')');
    }
    else {
      throw new UnsupportedOperationException("Unsupported subject type: " + subject.getClass().getName());
    }
  }

  abstract void onRegister(Connection connection) throws SQLException;
  abstract void onConnect(Connection connection) throws SQLException;

  static <T extends kind.DataType<?>>Compilable compilable(final T kind) {
    return (Compilable)kind;
  }

  String tableName(final type.Entity entity, final Compilation compilation) {
    return compilation.vendor.getDialect().quoteIdentifier(entity.name());
  }

  /**
   * Get the parameter mark for {@link PreparedStatement}s.
   *
   * @param dataType The {@link type.DataType} for the requested mark.
   * @return The mark.
   */
  String getPreparedStatementMark(final type.DataType<?> dataType) {
    return "?";
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param case_ The {@link CaseImpl.Simple.CASE}.
   * @param _else The {@link CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final CaseImpl.Simple.CASE<?,?> case_, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append("CASE ");
    case_.variable.compile(compilation);
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param when The {@link CaseImpl.WHEN}.
   * @param compilation The target {@link Compilation}.
   */
  void compile(final CaseImpl.Search.WHEN<?> when, final Compilation compilation) {
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
   */
  void compile(final CaseImpl.WHEN<?> when, final CaseImpl.THEN<?,?> then, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append(" WHEN ");
    when.condition.compile(compilation);
    compilation.append(" THEN ");
    then.value.compile(compilation);
  }

  /**
   * Compile the specified parameters, and append to the provided
   * {@link Compilation}.
   *
   * @param _else The {@link CaseImpl.ELSE}.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append(" ELSE ");
    _else.value.compile(compilation);
    compilation.append(" END");
  }

  void compile(final SelectCommand command, final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException {
    compilation.append("SELECT ");
    if (select.distinct)
      compilation.append("DISTINCT ");

    compileEntities(select.entities, select, command.getTranslateTypes(), compilation);
  }

  void compile(final SelectImpl.untyped.FROM<?> from, final Compilation compilation) throws IOException {
    if (from == null)
      return;

    compilation.append(" FROM ");

    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    final Iterator<type.Entity> iterator = from.tables.iterator();
    while (true) {
      final type.Entity table = iterator.next();
      if (table.wrapper() != null) {
        table.wrapper().compile(compilation);
      }
      else {
        compilation.append(tableName(table, compilation)).append(' ');
        compilation.getAlias(table).compile(compilation);
      }

      if (iterator.hasNext())
        compilation.append(", ");
      else
        break;
    }
  }

  void compile(final SelectImpl.untyped.JOIN<?> join, final SelectImpl.untyped.ON<?> on, final Compilation compilation) throws IOException {
    if (join != null) {
      if (join.cross)
        compilation.append(" CROSS");

      if (join.natural)
        compilation.append(" NATURAL");

      if (join.left && join.right)
        compilation.append(" FULL OUTER");
      else if (join.left)
        compilation.append(" LEFT OUTER");
      else if (join.right)
        compilation.append(" RIGHT OUTER");

      compilation.append(" JOIN ");
      if (join.table != null) {
        compilation.append(tableName(join.table, compilation)).append(' ');
        compilation.registerAlias(join.table).compile(compilation);
        if (on != null) {
          compilation.append(" ON (");
          on.condition.compile(compilation);
          compilation.append(')');
        }
      }
      else if (join.select != null) {
        compilation.append('(');
        final Command<?> command = join.select.normalize();
        final Compilation subCompilation = compilation.getSubCompilation(command);
        final Alias alias = compilation.getAlias(command);
        compilation.append(subCompilation);
        compilation.append(") ");
        compilation.append(alias);
        if (on != null) {
          compilation.append(" ON (");
          on.condition.compile(compilation);
          compilation.subCompile(on.condition);
          compilation.append(')');
        }
      }
      else {
        throw new IllegalStateException();
      }
    }
  }

  void compile(final SelectImpl.untyped.WHERE<?> where, final Compilation compilation) throws IOException {
    if (where != null) {
      compilation.append(" WHERE ");
      where.condition.compile(compilation);
    }
  }

  void compile(final SelectImpl.untyped.GROUP_BY<?> groupBy, final Compilation compilation) throws IOException {
    if (groupBy != null) {
      compilation.append(" GROUP BY ");
      compileEntities(groupBy.subjects, groupBy, null, compilation);
    }
  }

  void compile(final SelectImpl.untyped.HAVING<?> having, final Compilation compilation) throws IOException {
    if (having != null) {
      compilation.append(" HAVING ");
      having.condition.compile(compilation);
    }
  }

  void compile(final SelectImpl.untyped.ORDER_BY<?> orderBy, final Compilation compilation) throws IOException {
    if (orderBy != null) {
      compilation.append(" ORDER BY ");
      if (orderBy.columns != null) {
        for (int i = 0; i < orderBy.columns.length; ++i) {
          final type.DataType<?> dataType = orderBy.columns[i];
          if (i > 0)
            compilation.append(", ");

          compilation.registerAlias(dataType.owner);
          dataType.compile(compilation);
        }
      }
      else {
        for (int i = 0; i < orderBy.columnNumbers.length; ++i) {
          final int columnNumber = orderBy.columnNumbers[i];
          if (i > 0)
            compilation.append(", ");

          compilation.append(String.valueOf(columnNumber));
        }
      }
    }
  }

  void compile(final SelectImpl.untyped.LIMIT<?> limit, final SelectImpl.untyped.OFFSET<?> offset, final Compilation compilation) {
    if (limit != null) {
      compilation.append(" LIMIT " + limit.rows);
      if (offset != null)
        compilation.append(" OFFSET " + offset.rows);
    }
  }

  void compile(final Collection<? extends SelectImpl.untyped.UNION<?>> unions, final Compilation compilation) throws IOException {
    if (unions != null) {
      for (final SelectImpl.untyped.UNION<?> union : unions) {
        compilation.append(" UNION ");
        if (union.all)
          compilation.append("ALL ");

        union.select.compile(compilation);
      }
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void compileInsert(final type.DataType<?>[] columns, final Compilation compilation) throws IOException {
    final StringBuilder builder = new StringBuilder();
    compilation.append("INSERT INTO ");
    final type.Entity entity = columns[0].owner;
    entity.compile(compilation);
    for (int j = 0; j < columns.length; ++j) {
      final type.DataType column = columns[j];
      if (column.isNull()) {
        if (!column.wasSet() && column.generateOnInsert != null)
          column.generateOnInsert.generate(column, compilation.vendor);
        else
          continue;
      }

      if (builder.length() > 0)
        builder.append(", ");

      builder.append(compilation.vendor.getDialect().quoteIdentifier(column.name));
    }

    compilation.append(" (").append(builder).append(") VALUES (");

    boolean paramAdded = false;
    for (int j = 0; j < entity._column$.length; ++j) {
      final type.DataType column = entity._column$[j];
      if (column.isNull() && (column.wasSet() || column.generateOnInsert == null))
        continue;

      if (paramAdded)
        compilation.append(", ");

      compilation.addParameter(column, false);
      paramAdded = true;
    }

    compilation.append(')');
  }

  @SuppressWarnings("rawtypes")
  void compile(final InsertImpl.INSERT insert, final Compilation compilation) throws IOException {
    compileInsert(insert.entity != null ? insert.entity._column$ : insert.columns, compilation);
  }

  @SuppressWarnings("rawtypes")
  void compile(final InsertImpl.INSERT insert, final InsertImpl.VALUES<?> values, final Compilation compilation) throws IOException {
    final HashMap<Integer,type.ENUM<?>> translateTypes = new HashMap<>();
    if (insert.entity != null) {
      compilation.append("INSERT INTO ");
      final type.Entity entity = insert.entity;
      entity.compile(compilation);
      compilation.append(" (");
      for (int i = 0; i < entity._column$.length; ++i) {
        if (i > 0)
          compilation.append(", ");

        final type.DataType<?> column = entity._column$[i];
        column.compile(compilation);
        if (column instanceof type.ENUM<?>)
          translateTypes.put(i, (type.ENUM<?>)column);
      }

      compilation.append(") ");
    }
    else if (insert.columns != null) {
      compilation.append("INSERT INTO ");
      final type.Entity entity = insert.columns[0].owner;
      entity.compile(compilation);
      compilation.append(" (");
      for (int i = 0; i < insert.columns.length; ++i) {
        if (i > 0)
          compilation.append(", ");

        final type.DataType<?> column = insert.columns[i];
        column.compile(compilation);
        if (column instanceof type.ENUM<?>)
          translateTypes.put(i, (type.ENUM<?>)column);
      }

      compilation.append(") ");
    }

    final SelectCommand selectCommand = (SelectCommand)((Keyword<?>)values.select).normalize();
    final Compilation selectCompilation = compilation.newSubCompilation(selectCommand);
    selectCommand.setTranslateTypes(translateTypes);
    selectCommand.compile(selectCompilation);
    compilation.append(selectCompilation);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  void compile(final UpdateImpl.UPDATE update, final Compilation compilation) throws IOException {
    final type.Entity entity = update.entity;
    compilation.append("UPDATE ");
    update.entity.compile(compilation);
    compilation.append(" SET ");
    boolean paramAdded = false;
    for (int c = 0; c < entity._column$.length; ++c) {
      final type.DataType column = entity._column$[c];
      if (!column.primary && (column.wasSet() || column.generateOnUpdate != null || column.indirection != null)) {
        if (!column.wasSet() && column.generateOnUpdate != null)
          column.generateOnUpdate.generate(column, compilation.vendor);

        if (column.indirection != null) {
          compilation.afterExecute(success -> {
            if (success) {
              final Object evaluated = column.evaluate(new IdentityHashSet<>());
              if (evaluated == null)
                column.setValue(null);
              else if (column instanceof kind.Numeric.UNSIGNED && ((Number)evaluated).doubleValue() < 0)
                throw new IllegalStateException("Attempted to assign negative value to UNSIGNED " + type.DataType.getSimpleName(column.getClass()) + ": " + evaluated);
              else if (column.type() == evaluated.getClass())
                column.setValue(evaluated);
              else if (evaluated instanceof Number && Number.class.isAssignableFrom(column.type()))
                column.setValue(type.Numeric.valueOf((Number)evaluated, (Class<? extends Number>)column.type()));
              else
                throw new IllegalStateException("Value exceeds bounds of type " + type.DataType.getSimpleName(column.getClass()) + ": " + evaluated);
            }
          });
        }

        if (paramAdded)
          compilation.append(", ");

        compilation.append(compilation.vendor.getDialect().quoteIdentifier(column.name)).append(" = ");
        compilation.addParameter(column, true);
        paramAdded = true;
      }
    }

    // No changes were found
    if (!paramAdded)
      return;

    paramAdded = false;
    for (final type.DataType column : entity._column$) {
      if (column.primary || column.keyForUpdate) {
        if (paramAdded)
          compilation.append(" AND ");
        else
          compilation.append(" WHERE ");

        compilation.addCondition(column, false);
        paramAdded = true;
      }
    }
  }

  void compile(final UpdateImpl.UPDATE update, final List<UpdateImpl.SET> sets, final UpdateImpl.WHERE where, final Compilation compilation) throws IOException {
    compilation.append("UPDATE ");
    update.entity.compile(compilation);
    compilation.append(" SET ");
    for (int i = 0; i < sets.size(); ++i) {
      final UpdateImpl.SET set = sets.get(i);
      if (i > 0)
        compilation.append(", ");

      compilation.append(compilation.vendor.getDialect().quoteIdentifier(set.column.name)).append(" = ");
      set.to.compile(compilation);
    }

    if (where != null) {
      compilation.append(" WHERE ");
      where.condition.compile(compilation);
    }
  }

  void compile(final DeleteImpl.DELETE delete, final Compilation compilation) throws IOException {
    compilation.append("DELETE FROM ");
    delete.entity.compile(compilation);
    boolean paramAdded = false;
    for (int j = 0; j < delete.entity._column$.length; ++j) {
      final type.DataType<?> column = delete.entity._column$[j];
      if (column.wasSet()) {
        if (paramAdded)
          compilation.append(" AND ");
        else
          compilation.append(" WHERE ");

        compilation.addCondition(column, false);
        paramAdded = true;
      }
    }
  }

  void compile(final DeleteImpl.DELETE delete, final DeleteImpl.WHERE where, final Compilation compilation) throws IOException {
    compilation.append("DELETE FROM ");
    delete.entity.compile(compilation);
    compilation.append(" WHERE ");

    where.condition.compile(compilation);
  }

  <T extends type.Subject<?>>void compile(final type.Entity entity, final Compilation compilation) throws IOException {
    if (entity.wrapper() != null) {
      entity.wrapper().compile(compilation);
    }
    else {
      compilation.append(tableName(entity, compilation));
      final Alias alias = compilation.registerAlias(entity);
      if (compilation.command instanceof SelectCommand) {
        compilation.append(' ');
        alias.compile(compilation);
      }
    }
  }

  void compile(final expression.ChangeCase expression, final Compilation compilation) throws IOException {
    compilation.append(expression.operator).append('(');
    compilable(expression.arg).compile(compilation);
    compilation.append(')');
  }

  void compile(final expression.Concat expression, final Compilation compilation) throws IOException {
    compilation.append('(');
    for (int i = 0; i < expression.args.length; ++i) {
      final Compilable arg = compilable(expression.args[i]);
      if (i > 0)
        compilation.append(" || ");

      arg.compile(compilation);
    }
    compilation.append(')');
  }

  void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final TemporalUnit unit : units)
      clause.append(' ').append(interval.get(unit)).append(' ').append(unit);

    compilation.append("INTERVAL '").append(clause.substring(1)).append('\'');
  }

  void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    compilation.append("((");
    expression.a.compile(compilation);
    compilation.append(") ");
    compilation.append(expression.operator);
    compilation.append(" (");
    expression.b.compile(compilation);
    compilation.append("))");
  }

  void compile(final expression.Numeric expression, final Compilation compilation) throws IOException {
    compilation.append("((");
    compilable(expression.a).compile(compilation);
    compilation.append(") ").append(expression.operator).append(" (");
    compilable(expression.b).compile(compilation);
    compilation.append("))");
  }

  void compile(final type.DataType<?> dataType, final Compilation compilation) throws IOException {
    if (dataType.wrapper() == null) {
      if (dataType.owner != null) {
        Alias alias = compilation.getAlias(dataType.owner);
        if (alias != null) {
          if (compilation.command instanceof SelectCommand) {
            alias.compile(compilation);
            compilation.append('.');
          }

          compilation.append(compilation.vendor.getDialect().quoteIdentifier(dataType.name));
        }
        else if (!compilation.subCompile(dataType.owner)) {
          throw new IllegalArgumentException("Missing alias for table " + compilation.vendor.getDialect().quoteIdentifier(dataType.owner.name()) + " needed for column " + compilation.vendor.getDialect().quoteIdentifier(dataType.name) + "`");
        }
      }
      else {
        compilation.addParameter(dataType, false);
      }
    }
    else if (!compilation.subCompile(dataType)) {
      dataType.wrapper().compile(compilation);
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
  String compile(final As<?> as) {
    return "AS";
  }

  void compile(final As<?> as, final Compilation compilation) throws IOException {
    final Alias alias = compilation.registerAlias(as.getVariable());
    compilation.append('(');
    as.parent().compile(compilation);
    compilation.append(')');
    if (as.isExplicit()) {
      final String string = compile(as);
      compilation.append(' ');
      if (string != null && string.length() != 0)
        compilation.append(string).append(' ');

      alias.compile(compilation);
    }
  }

  // FIXME: Move this to a Util class or something
  static <T extends type.Subject<?>> void formatBraces(final operator.Boolean operator, final Condition<?> condition, final Compilation compilation) throws IOException {
    if (condition instanceof BooleanTerm) {
      if (operator == ((BooleanTerm)condition).operator) {
        condition.compile(compilation);
      }
      else {
        compilation.append('(');
        condition.compile(compilation);
        compilation.append(')');
      }
    }
    else {
      condition.compile(compilation);
    }
  }

  void compile(final BooleanTerm condition, final Compilation compilation) throws IOException {
    formatBraces(condition.operator, condition.a, compilation);
    compilation.append(' ').append(condition.operator).append(' ');
    formatBraces(condition.operator, condition.b, compilation);
    for (int i = 0; i < condition.conditions.length; ++i) {
      compilation.append(' ').append(condition.operator).append(' ');
      formatBraces(condition.operator, condition.conditions[i], compilation);
    }
  }

  private static Compilable unwrapAlias(final Compilable compilable) {
    if (!(compilable instanceof type.Subject))
      return compilable;

    final type.Subject<?> subject = (type.Subject<?>)compilable;
    if (!(subject.wrapper() instanceof As))
      return compilable;

    return ((As<?>)subject.wrapper()).parent();
  }

  void compile(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException {
    if (!compilation.subCompile(predicate.a))
      unwrapAlias(predicate.a).compile(compilation);

    compilation.append(' ').append(predicate.operator).append(' ');
    if (!compilation.subCompile(predicate.b))
      unwrapAlias(predicate.b).compile(compilation);
  }

  void compile(final InPredicate predicate, final Compilation compilation) throws IOException {
    compilable(predicate.dataType).compile(compilation);
    compilation.append(' ');
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("IN").append(" (");
    for (int i = 0; i < predicate.values.length; ++i) {
      if (i > 0)
        compilation.append(", ");

      predicate.values[i].compile(compilation);
    }

    compilation.append(')');
  }

  void compile(final ExistsPredicate predicate, final Compilation compilation) throws IOException {
    compilation.append("EXISTS").append(" (");
    predicate.subQuery.compile(compilation);
    compilation.append(')');
  }

  void compile(final LikePredicate predicate, final Compilation compilation) throws IOException {
    compilation.append('(');
    compilable(predicate.dataType).compile(compilation);
    compilation.append(") ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("LIKE").append(" '").append(predicate.pattern).append('\'');
  }

  void compile(final QuantifiedComparisonPredicate<?> predicate, final Compilation compilation) throws IOException {
    compilation.append(predicate.qualifier).append(" (");
    predicate.subQuery.compile(compilation);
    compilation.append(')');
  }

  void compile(final BetweenPredicates.BetweenPredicate predicate, final Compilation compilation) throws IOException {
    compilation.append('(');
    compilable(predicate.dataType).compile(compilation);
    compilation.append(')');
    if (!predicate.positive)
      compilation.append(" NOT");

    compilation.append(" BETWEEN ");
    predicate.a().compile(compilation);
    compilation.append(" AND ");
    predicate.b().compile(compilation);
  }

  <T> void compile(final NullPredicate predicate, final Compilation compilation) throws IOException {
    compilable(predicate.dataType).compile(compilation);
    compilation.append(" IS ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("NULL");
  }

  /**
   * Compile the PI function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   */
  void compile(final function.Pi function, final Compilation compilation) {
    compilation.append("PI()");
  }

  /**
   * Compile the ABS function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Abs function, final Compilation compilation) throws IOException {
    compilation.append("ABS(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the SIGN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Sign function, final Compilation compilation) throws IOException {
    compilation.append("SIGN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the ROUND function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Round function, final Compilation compilation) throws IOException {
    compilation.append("ROUND(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the FLOOR function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Floor function, final Compilation compilation) throws IOException {
    compilation.append("FLOOR(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the CEIL function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Ceil function, final Compilation compilation) throws IOException {
    compilation.append("CEIL(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the SQRT function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Sqrt function, final Compilation compilation) throws IOException {
    compilation.append("SQRT(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the POW function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Pow function, final Compilation compilation) throws IOException {
    compilation.append("POWER(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the MOD function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Mod function, final Compilation compilation) throws IOException {
    compilation.append("MOD(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the SIN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Sin function, final Compilation compilation) throws IOException {
    compilation.append("SIN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the ASIN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Asin function, final Compilation compilation) throws IOException {
    compilation.append("ASIN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the COS function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Cos function, final Compilation compilation) throws IOException {
    compilation.append("COS(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the ACOS function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Acos function, final Compilation compilation) throws IOException {
    compilation.append("ACOS(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the TAN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Tan function, final Compilation compilation) throws IOException {
    compilation.append("TAN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the ATAN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Atan function, final Compilation compilation) throws IOException {
    compilation.append("ATAN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the ATAN2 function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Atan2 function, final Compilation compilation) throws IOException {
    compilation.append("ATAN2(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the EXP function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Exp function, final Compilation compilation) throws IOException {
    compilation.append("EXP(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the LN function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Ln function, final Compilation compilation) throws IOException {
    compilation.append("LN(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the LOG function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Log function, final Compilation compilation) throws IOException {
    compilation.append("LOG(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the LOG2 function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Log2 function, final Compilation compilation) throws IOException {
    compilation.append("LOG2(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the LOG10 function, and append to the provided {@link Compilation}.
   *
   * @param function The function to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final function.Log10 function, final Compilation compilation) throws IOException {
    compilation.append("LOG10(");
    function.a.compile(compilation);
    compilation.append(')');
  }

  /**
   * Compile the COUNT expression, and append to the provided {@link Compilation}.
   *
   * @param expression The expression to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final expression.Count expression, final Compilation compilation) throws IOException {
    compilation.append("COUNT").append('(');
    if (expression.column == null) {
      compilation.append('*');
    }
    else {
      if (expression.distinct)
        compilation.append("DISTINCT ");

      compilable(expression.column).compile(compilation);
    }

    compilation.append(')');
  }

  /**
   * Compile the SET expression, and append to the provided {@link Compilation}.
   *
   * @param expression The expression to compile.
   * @param compilation The target {@link Compilation}.
   * @throws IOException If an I/O error has occurred.
   */
  void compile(final expression.Set expression, final Compilation compilation) throws IOException {
    compilation.append(expression.function).append('(');
    if (expression.a != null) {
      if (expression.distinct)
        compilation.append("DISTINCT ");

      expression.a.compile(compilation);

      // if (function.b != null) {
      // compilation.append(", ");
      // function.b.compile(compilation);
      // }
    }

    compilation.append(')');
  }

  void compile(final OrderingSpec spec, final Compilation compilation) throws IOException {
    unwrapAlias(spec.dataType).compile(compilation);
    compilation.append(' ').append(spec.operator);
  }

  void compile(final function.Temporal function, final Compilation compilation) {
    compilation.append(function.function).append("()");
  }

  <T>String compile(final type.ARRAY<? extends T> column, final type.DataType<T> dataType) throws IOException {
    final StringBuilder builder = new StringBuilder("(");
    final type.DataType<T> clone = dataType.clone();
    final T[] items = column.get();
    for (int i = 0; i < items.length; ++i) {
      clone.setValue(items[i]);
      if (i > 0)
        builder.append(", ");

      builder.append(type.DataType.compile(dataType, getVendor()));
    }

    return builder.append(')').toString();
  }

  void compile(final Cast.AS as, final Compilation compilation) throws IOException {
    compilation.append("CAST((");
    compilable(as.dataType).compile(compilation);
    compilation.append(") AS ").append(as.cast.declare(compilation.vendor)).append(')');
  }

  String cast(final type.DataType<?> dataType, final Compilation compilation) {
    return dataType.declare(compilation.vendor);
  }

  String compile(final type.BIGINT dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.BIGINT.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.BINARY dataType) {
    return dataType.isNull() ? "NULL" : "X'" + new Hexadecimal(dataType.get()) + "'";
  }

  String compile(final type.BLOB dataType) throws IOException {
    try (final InputStream in = dataType.get()) {
      return in == null ? "NULL" : "X'" + new Hexadecimal(Streams.readBytes(in)) + "'";
    }
  }

  String compile(final type.BOOLEAN dataType) {
    return String.valueOf(dataType.get()).toUpperCase();
  }

  String compile(final type.CHAR dataType) {
    return dataType.isNull() ? "NULL" : "'" + dataType.get().replace("'", "''") + "'";
  }

  String compile(final type.CLOB dataType) throws IOException {
    try (final Reader in = dataType.get()) {
      return in == null ? "NULL" : "'" + Readers.readFully(in) + "'";
    }
  }

  String compile(final type.DATE dataType) {
    return dataType.isNull() ? "NULL" : "'" + Dialect.DATE_FORMAT.format(dataType.get()) + "'";
  }

  String compile(final type.DATETIME dataType) {
    return dataType.isNull() ? "NULL" : "'" + Dialect.DATETIME_FORMAT.format(dataType.get()) + "'";
  }

  String compile(final type.DECIMAL dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.DECIMAL.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.DOUBLE dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.DOUBLE.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.ENUM<?> dataType) {
    return dataType.isNull() ? "NULL" : "'" + dataType.get() + "'";
  }

  String compile(final type.FLOAT dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.FLOAT.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.INT dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.INT.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.SMALLINT dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.SMALLINT.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.TINYINT dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.TINYINT.UNSIGNED dataType) {
    return dataType.isNull() ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  String compile(final type.TIME dataType) {
    return dataType.isNull() ? "NULL" : "'" + Dialect.TIME_FORMAT.format(dataType.get()) + "'";
  }

  void assignAliases(final SelectImpl.untyped.FROM<?> from, final List<? extends SelectImpl.untyped.JOIN<?>> joins, final Compilation compilation) throws IOException {
    if (from != null) {
      for (final type.Entity table : from.tables) {
        table.wrapper(null);
        compilation.registerAlias(table);
      }
    }

    if (joins != null) {
      for (final SelectImpl.untyped.JOIN<?> join : joins) {
        if (join.table != null) {
          join.table.wrapper(null);
          compilation.registerAlias(join.table);
        }
        else {
          final SelectCommand command = (SelectCommand)join.select.normalize();
          final Compilation subCompilation = compilation.newSubCompilation(command);
          compilation.registerAlias(command);
          command.compile(subCompilation);
        }
      }
    }
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final type.CHAR dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    if (dataType.isNull())
      statement.setNull(parameterIndex, dataType.sqlType());
    else
      statement.setString(parameterIndex, dataType.get());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    if (dataType.isNull())
      resultSet.updateNull(columnIndex);
    else
      resultSet.updateString(columnIndex, dataType.get());
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  // FIXME: This should be named getColumn()
  String getParameter(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getString(columnIndex);
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final type.CLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    final Reader in = dataType.get();
    if (in != null)
      statement.setClob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final type.CLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Reader in = dataType.get();
    if (in != null)
      resultSet.updateClob(columnIndex, in);
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  Reader getParameter(final type.CLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Clob value = resultSet.getClob(columnIndex);
    return value == null ? null : value.getCharacterStream();
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws IOException If an I/O error has occurred.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    final InputStream in = dataType.get();
    if (in != null)
      statement.setBlob(parameterIndex, in);
    else
      statement.setNull(parameterIndex, Types.BLOB);
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final type.BLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final InputStream in = dataType.get();
    if (in != null)
      resultSet.updateBlob(columnIndex, in);
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  InputStream getParameter(final type.BLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getBinaryStream(columnIndex);
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  void setParameter(final type.DATE dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDate value = dataType.get();
    if (value != null)
      statement.setDate(parameterIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  void updateColumn(final type.DATE dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalDate value = dataType.get();
    if (value != null)
      resultSet.updateDate(columnIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  LocalDate getParameter(final type.DATE dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Date value = resultSet.getDate(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDate.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, Timestamp.valueOf("1970-01-01 " + value.format(Dialect.TIME_FORMAT)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      resultSet.updateTimestamp(columnIndex, Timestamp.valueOf("1970-01-01 " + value.format(Dialect.TIME_FORMAT)));
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : value.toLocalDateTime().toLocalTime();
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param statement The {@link PreparedStatement}.
   * @param parameterIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void setParameter(final type.DATETIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, dt.DATETIME.toTimestamp(value));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  /**
   * Sets the specified {@link type.DataType} as a parameter in the provided
   * {@link PreparedStatement} at the given parameter index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link PreparedStatement}.
   * @param columnIndex The parameter index.
   * @throws SQLException If a SQL error has occurred.
   */
  void updateColumn(final type.DATETIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      resultSet.updateTimestamp(columnIndex, dt.DATETIME.toTimestamp(value));
    else
      resultSet.updateNull(columnIndex);
  }

  /**
   * Returns the parameter of the specified {@link type.DataType} from the
   * provided {@link ResultSet} at the given column index.
   *
   * @param dataType The data type.
   * @param resultSet The {@link ResultSet}.
   * @param columnIndex The column index.
   * @return The parameter of the specified {@code dataType} from the provided
   *         {@link ResultSet} at the given column index.
   * @throws SQLException If a SQL error has occurred.
   */
  @SuppressWarnings("deprecation")
  LocalDateTime getParameter(final type.DATETIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate(), value.getHours(), value.getMinutes(), value.getSeconds(), value.getNanos());
  }
}