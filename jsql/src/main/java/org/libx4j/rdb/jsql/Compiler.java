/* Copyright (c) 2017 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
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
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalUnit;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import org.lib4j.io.Readers;
import org.lib4j.io.Streams;
import org.lib4j.lang.Numbers;
import org.lib4j.lang.PackageLoader;
import org.lib4j.util.Hexadecimal;
import org.lib4j.util.IdentityHashSet;
import org.libx4j.rdb.jsql.InsertImpl.VALUES;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;

abstract class Compiler {
  private static final Compiler[] compilers = new Compiler[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemContextPackageLoader().loadPackage(Compiler.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Compiler.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Compiler compiler = (Compiler)cls.newInstance();
          compilers[compiler.getVendor().ordinal()] = compiler;
        }
      }
    }
    catch (final ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Compiler getCompiler(final DBVendor vendor) {
    final Compiler compiler = compilers[vendor.ordinal()];
    if (compiler == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return compiler;
  }

  protected void compileEntities(final Collection<? extends Compilable> entities, final Keyword<?> source, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation) throws IOException {
    final Iterator<? extends Compilable> iterator = entities.iterator();
    int index = 0;
    while (iterator.hasNext()) {
      final Compilable subject = iterator.next();
      compileNextSubject(subject, index++, source, translateTypes, compilation);
      if (iterator.hasNext())
        compilation.append(", ");
    }
  }

  protected String translateEnum(final type.ENUM<?> from, final type.ENUM<?> to) {
    return "";
  }

  private void checkTranslateType(final Map<Integer,type.ENUM<?>> translateTypes, final type.DataType<?> column, final int index, final Compilation compilation) {
    if (column instanceof type.ENUM<?> && translateTypes != null) {
      final type.ENUM<?> translateType = translateTypes.get(index);
      if (translateType != null)
        compilation.append(translateEnum((type.ENUM<?>)column, translateTypes.get(index)));
    }
  }

  protected void compileNextSubject(final Compilable subject, final int index, final Keyword<?> source, final Map<Integer,type.ENUM<?>> translateTypes, final Compilation compilation) throws IOException {
    if (subject instanceof type.Entity) {
      final type.Entity entity = (type.Entity)subject;
      final Alias alias = compilation.registerAlias(entity);
      for (int c = 0; c < entity.column.length; c++) {
        final type.DataType<?> column = entity.column[c];
        if (c > 0)
          compilation.append(", ");

        alias.compile(compilation);
        compilation.append(".").append(column.name);
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
      compilation.append("(");
      subject.compile(compilation);
      compilation.append(")");
    }
    else {
      throw new UnsupportedOperationException("Unsupported subject type: " + subject.getClass().getName());
    }
  }

  protected abstract DBVendor getVendor();

  protected abstract void onRegister(final Connection connection) throws SQLException;

  protected static <T extends kind.DataType<?>>Compilable compilable(final T kind) {
    return (Compilable)kind;
  }

  protected String tableName(final type.Entity entity, final Compilation compilation) {
    return entity.name();
  }

  protected String getPreparedStatementMark(final type.DataType<?> dataType) {
    return "?";
  }

  protected void compile(final CaseImpl.Simple.CASE<?,?> case_, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append("CASE ");
    case_.variable.compile(compilation);
  }

  protected void compile(final CaseImpl.Search.WHEN<?> case_, final Compilation compilation) {
    compilation.append("CASE");
  }

  protected void compile(final CaseImpl.WHEN<?> when, final CaseImpl.THEN<?,?> then, final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append(" WHEN ");
    when.condition.compile(compilation);
    compilation.append(" THEN ");
    then.value.compile(compilation);
  }

  protected void compile(final CaseImpl.ELSE<?> _else, final Compilation compilation) throws IOException {
    compilation.append(" ELSE ");
    _else.value.compile(compilation);
    compilation.append(" END");
  }

  protected void compile(final SelectCommand command, final SelectImpl.untyped.SELECT<?> select, final Compilation compilation) throws IOException {
    compilation.append("SELECT ");
    if (select.distinct)
      compilation.append("DISTINCT ");

    compileEntities(select.entities, select, command.getTranslateTypes(), compilation);
  }

  protected void compile(final SelectImpl.untyped.FROM<?> from, final Compilation compilation) throws IOException {
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
        compilation.append(tableName(table, compilation)).append(" ");
        compilation.registerAlias(table).compile(compilation);
      }

      if (iterator.hasNext())
        compilation.append(", ");
      else
        break;
    }
  }

  protected void compile(final SelectImpl.untyped.JOIN<?> join, final SelectImpl.untyped.ON<?> on, final Compilation compilation) throws IOException {
    if (join != null) {
      // NOTE: JOINed tables must have aliases. So, if the JOINed table is not part of the SELECT,
      // NOTE: it will not have had this assignment made. Therefore, ensure it's been made!
      compilation.registerAlias(join.table);
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

      compilation.append(" JOIN ").append(tableName(join.table, compilation)).append(" ");
      compilation.registerAlias(join.table).compile(compilation);
      if (on != null) {
        compilation.append(" ON (");
        on.condition.compile(compilation);
        compilation.append(")");
      }
    }
  }

  protected void compile(final SelectImpl.untyped.WHERE<?> where, final Compilation compilation) throws IOException {
    if (where != null) {
      compilation.append(" WHERE ");
      where.condition.compile(compilation);
    }
  }

  protected void compile(final SelectImpl.untyped.GROUP_BY<?> groupBy, final Compilation compilation) throws IOException {
    if (groupBy != null) {
      compilation.append(" GROUP BY ");
      compileEntities(groupBy.subjects, groupBy, null, compilation);
    }
  }

  protected void compile(final SelectImpl.untyped.HAVING<?> having, final Compilation compilation) throws IOException {
    if (having != null) {
      compilation.append(" HAVING ");
      having.condition.compile(compilation);
    }
  }

  protected void compile(final SelectImpl.untyped.ORDER_BY<?> orderBy, final Compilation compilation) throws IOException {
    if (orderBy != null) {
      compilation.append(" ORDER BY ");
      if (orderBy.columns != null) {
        for (int i = 0; i < orderBy.columns.length; i++) {
          final type.DataType<?> dataType = orderBy.columns[i];
          if (i > 0)
            compilation.append(", ");

          compilation.registerAlias(dataType.owner);
          dataType.compile(compilation);
        }
      }
      else {
        for (int i = 0; i < orderBy.columnNumbers.length; i++) {
          final int columnNumber = orderBy.columnNumbers[i];
          if (i > 0)
            compilation.append(", ");

          compilation.append(String.valueOf(columnNumber));
        }
      }
    }
  }

  protected void compile(final SelectImpl.untyped.LIMIT<?> limit, final SelectImpl.untyped.OFFSET<?> offset, final Compilation compilation) {
    if (limit != null) {
      compilation.append(" LIMIT " + limit.rows);
      if (offset != null)
        compilation.append(" OFFSET " + offset.rows);
    }
  }

  protected void compile(final Collection<SelectImpl.untyped.UNION<?>> unions, final Compilation compilation) throws IOException {
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
    for (int j = 0; j < columns.length; j++) {
      final type.DataType column = columns[j];
      if (!column.wasSet()) {
        if (column.generateOnInsert == null)
          continue;

        column.generateOnInsert.generate(column);
      }

      if (builder.length() > 0)
        builder.append(", ");

      builder.append(column.name);
    }

    compilation.append(" (").append(builder).append(") VALUES (");

    boolean paramAdded = false;
    for (int j = 0; j < entity.column.length; j++) {
      final type.DataType dataType = entity.column[j];
      if (dataType.wasSet() || dataType.generateOnInsert != null) {
        if (paramAdded)
          compilation.append(", ");

        compilation.addParameter(dataType, false);
        paramAdded = true;
      }
    }

    compilation.append(")");
  }

  @SuppressWarnings("rawtypes")
  protected void compile(final InsertImpl.INSERT insert, final Compilation compilation) throws IOException {
    if (insert.entities != null && insert.entities.length > 1) {
      for (int i = 0; i < insert.entities.length; i++) {
        compileInsert(insert.entities[i].column, compilation);
        compilation.addBatch();
      }
    }
    else {
      compileInsert(insert.entities != null ? insert.entities[0].column : insert.columns, compilation);
    }
  }

  @SuppressWarnings("rawtypes")
  protected void compile(final InsertImpl.INSERT insert, final VALUES<?> values, final Compilation compilation) throws IOException {
    final Map<Integer,type.ENUM<?>> translateTypes = new HashMap<Integer,type.ENUM<?>>();
    if (insert.entities != null) {
      if (insert.entities.length > 1)
        throw new UnsupportedOperationException("This is not supported, and should not be!");

      compilation.append("INSERT INTO ");
      final type.Entity entity = insert.entities[0];
      entity.compile(compilation);
      compilation.append(" (");
      for (int i = 0; i < entity.column.length; i++) {
        if (i > 0)
          compilation.append(", ");

        final type.DataType<?> column = entity.column[i];
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
      for (int i = 0; i < insert.columns.length; i++) {
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
    compilation.command.add(selectCommand);
    selectCommand.setTranslateTypes(translateTypes);
    selectCommand.compile(compilation);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void compile(final UpdateImpl.UPDATE update, final Compilation compilation) throws IOException {
    for (int i = 0; i < update.entities.length; i++) {
      final type.Entity entity = update.entities[i];
      compilation.append("UPDATE ");
      update.entities[i].compile(compilation);
      compilation.append(" SET ");
      boolean paramAdded = false;
      for (int c = 0; c < entity.column.length; c++) {
        final type.DataType column = entity.column[c];
        if (!column.primary && (column.wasSet() || column.generateOnUpdate != null || column.indirection != null)) {
          if (column.generateOnUpdate != null)
            column.generateOnUpdate.generate(column);

          if (column.indirection != null) {
            compilation.afterExecute(new Consumer<Boolean>() {
              @Override
              public void accept(final Boolean success) {
                if (success) {
                  final Object evaluated = column.evaluate(new IdentityHashSet<Evaluable>());
                  if (evaluated == null) {
                    column.value = null;
                  }
                  else if (column instanceof kind.Numeric.UNSIGNED && ((Number)evaluated).doubleValue() < 0) {
                    throw new IllegalStateException("Attempted to assign negative value to UNSIGNED " + type.DataType.getShortName(column.getClass()) + ": " + evaluated);
                  }
                  else if (column.type() != evaluated.getClass()) {
                    if (evaluated instanceof Number && Number.class.isAssignableFrom(column.type()))
                      column.value = Numbers.valueOf((Class<? extends Number>)column.type(), (Number)evaluated);
                    else
                      throw new IllegalStateException("Value exceeds bounds of type " + type.DataType.getShortName(column.getClass()) + ": " + evaluated);
                  }
                  else {
                    column.value = evaluated;
                  }
                }
              }
            });
          }

          if (paramAdded)
            compilation.append(", ");

          compilation.append(column.name).append(" = ");
          compilation.addParameter(column, true);
          paramAdded = true;
        }
      }

      // No changes were found
      if (!paramAdded)
        return;

      paramAdded = false;
      for (final type.DataType column : entity.column) {
        if (column.primary || column.keyForUpdate) {
          if (paramAdded)
            compilation.append(" AND ");
          else
            compilation.append(" WHERE ");

          compilation.addCondition(column, false);
          paramAdded = true;
        }
      }

      compilation.addBatch();
    }
  }

  protected void compile(final UpdateImpl.UPDATE update, final List<UpdateImpl.SET> sets, final UpdateImpl.WHERE where, final Compilation compilation) throws IOException {
    if (update.entities.length > 1)
      throw new UnsupportedOperationException("This is not supported, and should not be!");

    compilation.append("UPDATE ");
    update.entities[0].compile(compilation);
    compilation.append(" SET ");
    for (int i = 0; i < sets.size(); i++) {
      final UpdateImpl.SET set = sets.get(i);
      if (i > 0)
        compilation.append(", ");

      compilation.append(set.column.name).append(" = ");
      set.to.compile(compilation);
    }

    if (where != null) {
      compilation.append(" WHERE ");
      where.condition.compile(compilation);
    }
  }

  protected void compile(final DeleteImpl.DELETE delete, final Compilation compilation) throws IOException {
    for (int i = 0; i < delete.entities.length; i++) {
      compilation.append("DELETE FROM ");
      delete.entities[i].compile(compilation);
      boolean paramAdded = false;
      for (int j = 0; j < delete.entities[i].column.length; j++) {
        final type.DataType<?> column = delete.entities[i].column[j];
        if (column.wasSet()) {
          if (paramAdded)
            compilation.append(" AND ");
          else
            compilation.append(" WHERE ");

          compilation.addCondition(column, false);
          paramAdded = true;
        }
      }

      compilation.addBatch();
    }
  }

  protected void compile(final DeleteImpl.DELETE delete, final DeleteImpl.WHERE where, final Compilation compilation) throws IOException {
    if (delete.entities.length > 1)
      throw new UnsupportedOperationException("This is not supported, and should not be!");

    compilation.append("DELETE FROM ");
    delete.entities[0].compile(compilation);
    compilation.append(" WHERE ");

    where.condition.compile(compilation);
  }

  protected <T extends type.Subject<?>>void compile(final type.Entity entity, final Compilation compilation) throws IOException {
    if (entity.wrapper() != null) {
      entity.wrapper().compile(compilation);
    }
    else {
      compilation.append(tableName(entity, compilation));
      final Alias alias = compilation.registerAlias(entity);
      if (compilation.command.peek() instanceof SelectCommand) {
        compilation.append(" ");
        alias.compile(compilation);
      }
    }
  }

  protected void compile(final expression.ChangeCase expression, final Compilation compilation) throws IOException {
    compilation.append(expression.operator.toString()).append("(");
    compilable(expression.arg).compile(compilation);
    compilation.append(")");
  }

  protected void compile(final expression.Concat expression, final Compilation compilation) throws IOException {
    compilation.append("(");
    for (int i = 0; i < expression.args.length; i++) {
      final Compilable arg = compilable(expression.args[i]);
      if (i > 0)
        compilation.append(" || ");

      arg.compile(compilation);
    }
    compilation.append(")");
  }

  protected void compile(final Interval interval, final Compilation compilation) {
    final List<TemporalUnit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final TemporalUnit unit : units)
      clause.append(" ").append(interval.get(unit)).append(" " + unit);

    compilation.append("INTERVAL '").append(clause.substring(1)).append("'");
  }

  protected void compile(final expression.Temporal expression, final Compilation compilation) throws IOException {
    compilation.append("((");
    expression.a.compile(compilation);
    compilation.append(") ");
    compilation.append(expression.operator.toString());
    compilation.append(" (");
    expression.b.compile(compilation);
    compilation.append("))");
  }

  protected void compile(final expression.Numeric expression, final Compilation compilation) throws IOException {
    compilation.append("((");
    compilable(expression.a).compile(compilation);
    compilation.append(") ").append(expression.operator.toString()).append(" (");
    compilable(expression.b).compile(compilation);
    compilation.append("))");
  }

  protected void compile(final type.DataType<?> dataType, final Compilation compilation) throws IOException {
    if (dataType.wrapper() != null) {
      dataType.wrapper().compile(compilation);
    }
    else {
      if (dataType.owner != null) {
        final Alias alias = compilation.getAlias(dataType.owner);
        if (alias != null) {
          if (compilation.command.peek() instanceof SelectCommand) {
            alias.compile(compilation);
            compilation.append(".");
          }

          compilation.append(dataType.name);
          return;
        }
      }
      else {
        compilation.addParameter(dataType, false);
      }
    }
  }

  protected void compile(final Alias alias, final Compilation compilation) {
    compilation.append(alias.name);
  }

  protected String compile(final As<?> as) {
    return "AS";
  }

  protected void compile(final As<?> as, final Compilation compilation) throws IOException {
    final Alias alias = compilation.registerAlias(as.getVariable());
    compilation.append("(");
    as.parent().compile(compilation);
    compilation.append(")");
    if (as.isExplicit()) {
      final String string = compile(as);
      compilation.append(" ");
      if (string != null && string.length() != 0)
        compilation.append(string).append(" ");

      alias.compile(compilation);
    }
  }

  // FIXME: Move this to a Util class or something
  protected static <T extends type.Subject<?>>void formatBraces(final operator.Boolean operator, final Condition<?> condition, final Compilation compilation) throws IOException {
    if (condition instanceof BooleanTerm) {
      if (operator == ((BooleanTerm)condition).operator) {
        condition.compile(compilation);
      }
      else {
        compilation.append("(");
        condition.compile(compilation);
        compilation.append(")");
      }
    }
    else {
      condition.compile(compilation);
    }
  }

  protected void compile(final BooleanTerm condition, final Compilation compilation) throws IOException {
    formatBraces(condition.operator, condition.a, compilation);
    compilation.append(" ").append(condition.operator).append(" ");
    formatBraces(condition.operator, condition.b, compilation);
    for (int i = 0; i < condition.conditions.length; i++) {
      compilation.append(" ").append(condition.operator).append(" ");
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

  protected void compile(final ComparisonPredicate<?> predicate, final Compilation compilation) throws IOException {
    unwrapAlias(predicate.a).compile(compilation);
    compilation.append(" ").append(predicate.operator).append(" ");
    unwrapAlias(predicate.b).compile(compilation);
  }

  protected void compile(final InPredicate predicate, final Compilation compilation) throws IOException {
    compilable(predicate.dataType).compile(compilation);
    compilation.append(" ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("IN").append(" (");
    for (int i = 0; i < predicate.values.length; i++) {
      if (i > 0)
        compilation.append(", ");

      predicate.values[i].compile(compilation);
    }

    compilation.append(")");
  }

  protected void compile(final ExistsPredicate predicate, final Compilation compilation) throws IOException {
    compilation.append("EXISTS").append(" (");
    predicate.subQuery.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final LikePredicate predicate, final Compilation compilation) throws IOException {
    compilation.append("(");
    compilable(predicate.dataType).compile(compilation);
    compilation.append(") ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("LIKE").append(" '").append(predicate.pattern).append("'");
  }

  protected void compile(final QuantifiedComparisonPredicate<?> predicate, final Compilation compilation) throws IOException {
    compilation.append(predicate.qualifier).append(" (");
    predicate.subQuery.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final BetweenPredicates.BetweenPredicate predicate, final Compilation compilation) throws IOException {
    compilation.append("(");
    compilable(predicate.dataType).compile(compilation);
    compilation.append(")");
    if (!predicate.positive)
      compilation.append(" NOT");

    compilation.append(" BETWEEN ");
    predicate.a().compile(compilation);
    compilation.append(" AND ");
    predicate.b().compile(compilation);
  }

  protected <T>void compile(final NullPredicate predicate, final Compilation compilation) throws IOException {
    compilable(predicate.dataType).compile(compilation);
    compilation.append(" IS ");
    if (!predicate.positive)
      compilation.append("NOT ");

    compilation.append("NULL");
  }

  protected void compile(final function.Pi function, final Compilation compilation) {
    compilation.append("PI()");
  }

  protected void compile(final function.Abs function, final Compilation compilation) throws IOException {
    compilation.append("ABS(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Sign function, final Compilation compilation) throws IOException {
    compilation.append("SIGN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Round function, final Compilation compilation) throws IOException {
    compilation.append("ROUND(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Floor function, final Compilation compilation) throws IOException {
    compilation.append("FLOOR(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Ceil function, final Compilation compilation) throws IOException {
    compilation.append("CEIL(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Sqrt function, final Compilation compilation) throws IOException {
    compilation.append("SQRT(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Pow function, final Compilation compilation) throws IOException {
    compilation.append("POWER(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Mod function, final Compilation compilation) throws IOException {
    compilation.append("MOD(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Sin function, final Compilation compilation) throws IOException {
    compilation.append("SIN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Asin function, final Compilation compilation) throws IOException {
    compilation.append("ASIN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Cos function, final Compilation compilation) throws IOException {
    compilation.append("COS(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Acos function, final Compilation compilation) throws IOException {
    compilation.append("ACOS(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Tan function, final Compilation compilation) throws IOException {
    compilation.append("TAN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Atan function, final Compilation compilation) throws IOException {
    compilation.append("ATAN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Atan2 function, final Compilation compilation) throws IOException {
    compilation.append("ATAN2(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Exp function, final Compilation compilation) throws IOException {
    compilation.append("EXP(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Ln function, final Compilation compilation) throws IOException {
    compilation.append("LN(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Log function, final Compilation compilation) throws IOException {
    compilation.append("LOG(");
    function.a.compile(compilation);
    compilation.append(", ");
    function.b.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Log2 function, final Compilation compilation) throws IOException {
    compilation.append("LOG2(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final function.Log10 function, final Compilation compilation) throws IOException {
    compilation.append("LOG10(");
    function.a.compile(compilation);
    compilation.append(")");
  }

  protected void compile(final expression.Count expression, final Compilation compilation) throws IOException {
    compilation.append(expression.function).append("(");
    if (expression.column == null) {
      compilation.append("*");
    }
    else {
      if (expression.distinct)
        compilation.append("DISTINCT ");

      compilable(expression.column).compile(compilation);
    }

    compilation.append(")");
  }

  protected void compile(final expression.Set expression, final Compilation compilation) throws IOException {
    compilation.append(expression.function).append("(");
    if (expression.a != null) {
      if (expression.distinct)
        compilation.append("DISTINCT ");

      expression.a.compile(compilation);

//      if (function.b != null) {
//        compilation.append(", ");
//        function.b.compile(compilation);
//      }
    }

    compilation.append(")");
  }

  protected void compile(final OrderingSpec spec, final Compilation compilation) throws IOException {
    unwrapAlias(spec.dataType).compile(compilation);
    compilation.append(" ").append(spec.operator);
  }

  protected void compile(final function.Temporal function, final Compilation compilation) {
    compilation.append(function.function).append("()");
  }

  protected <T>String compile(final type.ARRAY<T> column, final type.DataType<T> dataType) throws IOException {
    final StringBuilder builder = new StringBuilder();
    final type.DataType<T> clone = dataType.clone();
    for (final T item : column.get()) {
      type.DataType.setValue(clone, item);
      builder.append(", ").append(type.DataType.compile(dataType, getVendor()));
    }

    return "(" + builder.substring(2) + ")";
  }

  protected void compile(final Cast.AS as, final Compilation compilation) throws IOException {
    compilation.append("CAST((");
    compilable(as.dataType).compile(compilation);
    compilation.append(") AS ").append(as.cast.declare(compilation.vendor)).append(")");
  }

  protected String cast(final type.DataType<?> dataType, final Compilation compilation) {
    return dataType.declare(compilation.vendor);
  }

  protected String compile(final type.BIGINT dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.BIGINT.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.BINARY dataType) {
    return dataType.get() == null ? "NULL" : "X'" + new Hexadecimal(dataType.get()) + "'";
  }

  protected String compile(final type.BLOB dataType) throws IOException {
    return dataType.get() == null ? "NULL" : "X'" + new Hexadecimal(Streams.readBytes(dataType.get())) + "'";
  }

  protected String compile(final type.BOOLEAN dataType) {
    return String.valueOf(dataType.get()).toUpperCase();
  }

  protected String compile(final type.CHAR dataType) {
    return dataType.get() == null ? "NULL" : "'" + dataType.get().replace("'", "''") + "'";
  }

  protected String compile(final type.CLOB dataType) throws IOException {
    return dataType.get() == null ? "NULL" : "'" + Readers.readFully(dataType.get()) + "'";
  }

  protected String compile(final type.DATE dataType) {
    return dataType.get() == null ? "NULL" : "'" + Dialect.DATE_FORMAT.format(dataType.get()) + "'";
  }

  protected String compile(final type.DATETIME dataType) {
    return dataType.get() == null ? "NULL" : "'" + Dialect.DATETIME_FORMAT.format(dataType.get()) + "'";
  }

  protected String compile(final type.DECIMAL dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.DECIMAL.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.DOUBLE dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.DOUBLE.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.ENUM<?> dataType) {
    return dataType.get() == null ? "NULL" : "'" + dataType.get() + "'";
  }

  protected String compile(final type.FLOAT dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.FLOAT.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.INT dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.INT.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.SMALLINT dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.SMALLINT.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.TINYINT dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.TINYINT.UNSIGNED dataType) {
    return dataType.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(dataType.get());
  }

  protected String compile(final type.TIME dataType) {
    return dataType.get() == null ? "NULL" : "'" + Dialect.TIME_FORMAT.format(dataType.get()) + "'";
  }

  protected void assignAliases(final SelectImpl.untyped.FROM<?> from, final Compilation compilation) {
    if (from != null)
      for (final type.Entity table : from.tables)
        compilation.registerAlias(table);
  }

  protected void setParameter(final type.CHAR dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    if (dataType.get() != null)
      statement.setString(parameterIndex, dataType.get());
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  protected String getParameter(final type.CHAR dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getString(columnIndex);
  }

  @SuppressWarnings("unused")
  protected void setParameter(final type.CLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    if (dataType.get() != null)
      statement.setClob(parameterIndex, dataType.get());
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  protected Reader getParameter(final type.CLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Clob value = resultSet.getClob(columnIndex);
    return value == null ? null : value.getCharacterStream();
  }

  @SuppressWarnings("unused")
  protected void setParameter(final type.BLOB dataType, final PreparedStatement statement, final int parameterIndex) throws IOException, SQLException {
    if (dataType.get() != null)
      statement.setBlob(parameterIndex, dataType.get());
    else
      statement.setNull(parameterIndex, Types.BLOB);
  }

  protected InputStream getParameter(final type.BLOB dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    return resultSet.getBinaryStream(columnIndex);
  }

  @SuppressWarnings("deprecation")
  protected void setParameter(final type.DATE dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDate value = dataType.get();
    if (value != null)
      statement.setDate(parameterIndex, new Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @SuppressWarnings("deprecation")
  protected LocalDate getParameter(final type.DATE dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Date value = resultSet.getDate(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDate.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate());
  }

  protected void setParameter(final type.TIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalTime value = dataType.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, Timestamp.valueOf("1970-01-01 " + value.format(Dialect.TIME_FORMAT)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  protected LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
//    return (LocalTime)resultSet.getObject(columnIndex);
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : value.toLocalDateTime().toLocalTime();
  }

  private static final DateTimeFormatter TIMESTAMP_FORMATTER = new DateTimeFormatterBuilder().appendPattern("yyyy-MM-dd HH:mm:ss").appendFraction(ChronoField.NANO_OF_SECOND, 0, 9, true).toFormatter();

  protected void setParameter(final type.DATETIME dataType, final PreparedStatement statement, final int parameterIndex) throws SQLException {
    final LocalDateTime value = dataType.get();
    if (value != null)
      statement.setTimestamp(parameterIndex, Timestamp.valueOf(value.format(TIMESTAMP_FORMATTER)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  @SuppressWarnings("deprecation")
  protected LocalDateTime getParameter(final type.DATETIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
//  return (LocalDateTime)resultSet.getObject(columnIndex);
    final Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate(), value.getHours(), value.getMinutes(), value.getSeconds(), value.getNanos());
  }
}