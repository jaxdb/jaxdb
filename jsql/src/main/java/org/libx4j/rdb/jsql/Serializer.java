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
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.lib4j.io.Readers;
import org.lib4j.io.Streams;
import org.lib4j.lang.PackageLoader;
import org.lib4j.lang.PackageNotFoundException;
import org.lib4j.util.Hexadecimal;
import org.libx4j.rdb.jsql.Insert.VALUES;
import org.libx4j.rdb.vendor.DBVendor;
import org.libx4j.rdb.vendor.Dialect;

public abstract class Serializer {
  private static final Serializer[] serializers = new Serializer[DBVendor.values().length];

  static {
    try {
      final Set<Class<?>> classes = PackageLoader.getSystemPackageLoader().loadPackage(Serializer.class.getPackage());
      for (final Class<?> cls : classes) {
        if (Serializer.class.isAssignableFrom(cls) && !Modifier.isAbstract(cls.getModifiers())) {
          final Serializer serializer = (Serializer)cls.newInstance();
          serializers[serializer.getVendor().ordinal()] = serializer;
        }
      }
    }
    catch (final PackageNotFoundException | ReflectiveOperationException e) {
      throw new ExceptionInInitializerError(e);
    }
  }

  protected static Serializer getSerializer(final DBVendor vendor) {
    final Serializer serializer = serializers[vendor.ordinal()];
    if (serializer == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return serializer;
  }

  protected void serializeEntities(final Collection<? extends Subject<?>> entities, final Keyword<?> source, final Serialization serialization) throws IOException {
    final Iterator<? extends Subject<?>> iterator = entities.iterator();
    int index = 0;
    while (iterator.hasNext()) {
      final Subject<?> subject = iterator.next();
      serializeNextSubject(subject, index++, source, serialization);
      if (iterator.hasNext())
        serialization.append(", ");
    }
  }

  protected void serializeNextSubject(final Subject<?> subject, final int index, final Keyword<?> source, final Serialization serialization) throws IOException {
    if (subject instanceof Entity) {
      final Entity entity = (Entity)subject;
      final Alias alias = serialization.registerAlias(entity);
      final type.DataType<?>[] columns = entity.column();
      for (int j = 0; j < columns.length; j++) {
        final type.DataType<?> column = columns[j];
        if (j > 0)
          serialization.append(", ");

        alias.serialize(serialization);
        serialization.append(".").append(column.name);
      }
    }
    else if (subject instanceof type.DataType) {
      final type.DataType<?> column = (type.DataType<?>)subject;
      serialization.registerAlias(column.owner);
      column.serialize(serialization);
    }
    else {
      throw new UnsupportedOperationException("Unexpected subject type: " + subject.getClass().getName());
    }
  }

  protected abstract DBVendor getVendor();

  protected abstract void onRegister(final Connection connection) throws SQLException;

  protected String tableName(final Entity entity, final Serialization serialization) {
    return entity.name();
  }

  protected String getPreparedStatementMark(final type.DataType<?> dataType) {
    return "?";
  }

  protected void serialize(final Case.Simple.CASE<?,?> case_, final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    serialization.append("CASE ");
    case_.variable.serialize(serialization);
  }

  protected void serialize(final Case.Search.WHEN<?> case_, final Serialization serialization) {
    serialization.append("CASE");
  }

  protected void serialize(final Case.WHEN<?> when, final Case.THEN<?,?> then, final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    serialization.append(" WHEN ");
    when.condition.serialize(serialization);
    serialization.append(" THEN ");
    then.value.serialize(serialization);
  }

  protected void serialize(final Case.ELSE<?> _else, final Serialization serialization) throws IOException {
    serialization.append(" ELSE ");
    _else.value.serialize(serialization);
    serialization.append(" END");
  }

  protected void serialize(final SelectCommand command, final Select.SELECT<?> select, final Serialization serialization) throws IOException {
    serialization.append("SELECT ");
    if (select.distinct)
      serialization.append("DISTINCT ");

    serializeEntities(select.entities, select, serialization);
  }

  protected void serialize(final Select.FROM<?> from, final Serialization serialization) throws IOException {
    serialization.append(" FROM ");

    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    final Iterator<Entity> iterator = from.tables.iterator();
    while (true) {
      final Entity table = iterator.next();
      if (table.wrapper() != null) {
        table.wrapper().serialize(serialization);
      }
      else {
        serialization.append(tableName(table, serialization)).append(" ");
        serialization.registerAlias(table).serialize(serialization);
      }

      if (iterator.hasNext())
        serialization.append(", ");
      else
        break;
    }
  }

  protected void serialize(final Select.JOIN<?> join, final Select.ON<?> on, final Serialization serialization) throws IOException {
    if (join != null) {
      // NOTE: JOINed tables must have aliases. So, if the JOINed table is not part of the SELECT,
      // NOTE: it will not have had this assignment made. Therefore, ensure it's been made!
      serialization.registerAlias(join.table);
      if (join.cross)
        serialization.append(" CROSS");

      if (join.natural)
        serialization.append(" NATURAL");

      if (join.left && join.right)
        serialization.append(" FULL OUTER");
      else if (join.left)
        serialization.append(" LEFT OUTER");
      else if (join.right)
        serialization.append(" RIGHT OUTER");

      serialization.append(" JOIN ").append(tableName(join.table, serialization)).append(" ");
      serialization.registerAlias(join.table).serialize(serialization);
      if (on != null) {
        serialization.append(" ON (");
        on.condition.serialize(serialization);
        serialization.append(")");
      }
    }
  }

  protected void serialize(final Select.WHERE<?> where, final Serialization serialization) throws IOException {
    if (where != null) {
      serialization.append(" WHERE ");
      where.condition.serialize(serialization);
    }
  }

  protected void serialize(final Select.GROUP_BY<?> groupBy, final Serialization serialization) throws IOException {
    if (groupBy != null) {
      serialization.append(" GROUP BY ");
      serializeEntities(groupBy.subjects, groupBy, serialization);
    }
  }

  protected void serialize(final Select.HAVING<?> having, final Serialization serialization) throws IOException {
    if (having != null) {
      serialization.append(" HAVING ");
      having.condition.serialize(serialization);
    }
  }

  protected void serialize(final Select.ORDER_BY<?> orderBy, final Serialization serialization) throws IOException {
    if (orderBy != null) {
      serialization.append(" ORDER BY ");
      if (orderBy.columns != null) {
        for (int i = 0; i < orderBy.columns.length; i++) {
          final type.DataType<?> dataType = orderBy.columns[i];
          if (i > 0)
            serialization.append(", ");

          serialization.registerAlias(dataType.owner);
          dataType.serialize(serialization);
        }
      }
      else {
        for (int i = 0; i < orderBy.columnNumbers.length; i++) {
          final int columnNumber = orderBy.columnNumbers[i];
          if (i > 0)
            serialization.append(", ");

          serialization.append(String.valueOf(columnNumber));
        }
      }
    }
  }

  protected void serialize(final Select.LIMIT<?> limit, final Select.OFFSET<?> offset, final Serialization serialization) {
    if (limit != null) {
      serialization.append(" LIMIT " + limit.rows);
      if (offset != null)
        serialization.append(" OFFSET " + offset.rows);
    }
  }

  protected void serialize(final Select.UNION<?> union, final Serialization serialization) throws IOException {
    if (union != null) {
      serialization.append(" UNION ");
      if (union.all)
        serialization.append("ALL ");

      union.select.serialize(serialization);
    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  private static void serializeInsert(final type.DataType<?>[] columns, final Serialization serialization) throws IOException {
    final StringBuilder builder = new StringBuilder();
    serialization.append("INSERT INTO ");
    final Entity entity = columns[0].owner;
    entity.serialize(serialization);
    for (int j = 0; j < columns.length; j++) {
      final type.DataType column = columns[j];
      if (!column.wasSet()) {
        if (column.generateOnInsert == null)
          continue;

        column.value = column.generateOnInsert.generateStatic(column);
      }

      if (builder.length() > 0)
        builder.append(", ");

      builder.append(column.name);
    }

    serialization.append(" (").append(builder).append(") VALUES (");

    boolean paramAdded = false;
    for (int j = 0; j < entity.column().length; j++) {
      final type.DataType dataType = entity.column()[j];
      if (dataType.wasSet() || dataType.generateOnInsert != null) {
        if (paramAdded)
          serialization.append(", ");

        serialization.addParameter(dataType);
        paramAdded = true;
      }
    }

    serialization.append(")");
  }

  @SuppressWarnings("rawtypes")
  protected void serialize(final Insert.INSERT insert, final Serialization serialization) throws IOException {
    if (insert.entities != null && insert.entities.length > 1) {
      for (int i = 0; i < insert.entities.length; i++) {
        serializeInsert(insert.entities[i].column(), serialization);
        serialization.addBatch();
      }
    }
    else {
      serializeInsert(insert.entities != null ? insert.entities[0].column() : insert.columns, serialization);
    }
  }

  @SuppressWarnings("rawtypes")
  protected void serialize(final Insert.INSERT insert, final VALUES<?> values, final Serialization serialization) throws IOException {
    final SelectCommand selectCommand = (SelectCommand)((Keyword<?>)values.select).normalize();
    if (insert.entities != null) {
      if (insert.entities.length > 1)
        throw new UnsupportedOperationException("This is not supported, and should not be!");

      serialization.append("INSERT INTO ");
      final Entity entity = insert.entities[0];
      entity.serialize(serialization);
      serialization.append(" (");
      for (int i = 0; i < entity.column().length; i++) {
        if (i > 0)
          serialization.append(", ");

        entity.column()[i].serialize(serialization);
      }

      serialization.append(") ");
    }
    else if (insert.columns != null) {
      serialization.append("INSERT INTO ");
      final Entity entity = insert.columns[0].owner;
      entity.serialize(serialization);
      serialization.append(" (");
      for (int i = 0; i < insert.columns.length; i++) {
        if (i > 0)
          serialization.append(", ");

        insert.columns[i].serialize(serialization);
      }

      serialization.append(") ");
    }

    selectCommand.serialize(serialization);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void serialize(final Update.UPDATE update, final Serialization serialization) throws IOException {
    for (int i = 0; i < update.entities.length; i++) {
      final Entity entity = update.entities[i];
      serialization.append("UPDATE ");
      update.entities[i].serialize(serialization);
      serialization.append(" SET ");
      boolean paramAdded = false;
      for (final type.DataType column : entity.column()) {
        if (!column.primary && (column.wasSet() || column.generateOnUpdate != null)) {
          if (column.generateOnUpdate != null)
            column.value = column.generateOnUpdate.generateStatic(column);

          if (paramAdded)
            serialization.append(", ");

          serialization.append(column.name).append(" = ");
          serialization.addParameter(column);
          paramAdded = true;
        }
      }

      paramAdded = false;
      for (final type.DataType column : entity.column()) {
        if (column.primary) {
          if (paramAdded)
            serialization.append(" AND ");
          else
            serialization.append(" WHERE ");

          serialization.addCondition(column);
          paramAdded = true;
        }
      }

      serialization.addBatch();
    }
  }

  protected void serialize(final Update.UPDATE update, final List<Update.SET> sets, final Update.WHERE where, final Serialization serialization) throws IOException {
    if (update.entities.length > 1)
      throw new UnsupportedOperationException("This is not supported, and should not be!");

    serialization.append("UPDATE ");
    update.entities[0].serialize(serialization);
    serialization.append(" SET ");
    for (int i = 0; i < sets.size(); i++) {
      final Update.SET set = sets.get(i);
      if (i > 0)
        serialization.append(", ");

      serialization.append(set.column.name).append(" = ");
      set.to.serialize(serialization);
    }

    if (where != null) {
      serialization.append(" WHERE ");
      where.condition.serialize(serialization);
    }
  }

  protected void serialize(final Delete.DELETE delete, final Serialization serialization) throws IOException {
    for (int i = 0; i < delete.entities.length; i++) {
      serialization.append("DELETE FROM ");
      delete.entities[i].serialize(serialization);
      boolean paramAdded = false;
      for (int j = 0; j < delete.entities[i].column().length; j++) {
        final type.DataType<?> column = delete.entities[i].column()[j];
        if (column.wasSet()) {
          if (paramAdded)
            serialization.append(" AND ");
          else
            serialization.append(" WHERE ");

          serialization.addCondition(column);
          paramAdded = true;
        }
      }

      serialization.addBatch();
    }
  }

  protected void serialize(final Delete.DELETE delete, final Delete.WHERE where, final Serialization serialization) throws IOException {
    if (delete.entities.length > 1)
      throw new UnsupportedOperationException("This is not supported, and should not be!");

    serialization.append("DELETE FROM ");
    delete.entities[0].serialize(serialization);
    serialization.append(" WHERE ");

    where.condition.serialize(serialization);
  }

  protected <T extends Subject<?>>void serialize(final Entity entity, final Serialization serialization) throws IOException {
    if (entity.wrapper() != null) {
      entity.wrapper().serialize(serialization);
    }
    else {
      serialization.append(tableName(entity, serialization));
      final Alias alias = serialization.registerAlias(entity);
      if (serialization.command instanceof SelectCommand) {
        serialization.append(" ");
        alias.serialize(serialization);
      }
    }
  }

  protected void serialize(final StringExpression serializable, final Serialization serialization) throws IOException {
    serialization.append("(");
    for (int i = 0; i < serializable.args.length; i++) {
      final Serializable arg = serializable.args[i];
      if (i > 0)
        serialization.append(" ").append(serializable.operator.toString()).append(" ");

      arg.serialize(serialization);
    }
    serialization.append(")");
  }

  protected void serialize(final Interval interval, final Serialization serialization) {
    final Set<Interval.Unit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final Interval.Unit unit : units)
      clause.append(" ").append(interval.getComponent(unit)).append(" " + unit.name());

    serialization.append("INTERVAL '").append(clause.substring(1)).append("'");
  }

  protected void serialize(final TemporalExpression expression, final Serialization serialization) throws IOException {
    serialization.append("(");
    expression.a.serialize(serialization);
    serialization.append(" ");
    serialization.append(expression.operator.toString());
    serialization.append(" ");
    expression.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final NumericExpression expression, final Serialization serialization) throws IOException {
    serialization.append("(");
    expression.a.serialize(serialization);
    serialization.append(" ").append(expression.operator.toString()).append(" ");
    expression.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final type.DataType<?> dataType, final Serialization serialization) throws IOException {
    if (dataType.wrapper() != null) {
      dataType.wrapper().serialize(serialization);
    }
    else {
      if (dataType.owner != null) {
        final Alias alias = serialization.getAlias(dataType.owner);
        if (alias != null) {
          if (serialization.command instanceof SelectCommand) {
            alias.serialize(serialization);
            serialization.append(".");
          }

          serialization.append(dataType.name);
          return;
        }
      }
      else {
        serialization.addParameter(dataType);
      }
    }
  }

  protected void serialize(final Alias alias, final Serialization serialization) {
    serialization.append(alias.name);
  }

  protected String serialize(final As<?> as) {
    return "AS";
  }

  protected void serialize(final As<?> as, final Serialization serialization) throws IOException {
    final Alias alias = serialization.registerAlias(as.getVariable());
    serialization.append("(");
    as.parent().serialize(serialization);
    serialization.append(")");
    final String s = serialize(as);
    serialization.append(" ");
    if (s != null && s.length() != 0)
      serialization.append(s).append(" ");

    alias.serialize(serialization);
    as.getVariable().wrapper(as.parent());
  }

  // FIXME: Move this to a Util class or something
  protected static <T extends Subject<?>>void formatBraces(final Operator<BooleanTerm> operator, final Condition<?> condition, final Serialization serialization) throws IOException {
    if (condition instanceof BooleanTerm) {
      if (operator == ((BooleanTerm)condition).operator) {
        condition.serialize(serialization);
      }
      else {
        serialization.append("(");
        condition.serialize(serialization);
        serialization.append(")");
      }
    }
    else {
      condition.serialize(serialization);
    }
  }

  protected void serialize(final BooleanTerm condition, final Serialization serialization) throws IOException {
    formatBraces(condition.operator, condition.a, serialization);
    serialization.append(" ").append(condition.operator).append(" ");
    formatBraces(condition.operator, condition.b, serialization);
    for (int i = 0; i < condition.conditions.length; i++) {
      serialization.append(" ").append(condition.operator).append(" ");
      formatBraces(condition.operator, condition.conditions[i], serialization);
    }
  }

  protected void serialize(final ComparisonPredicate<?> predicate, final Serialization serialization) throws IOException {
    predicate.a.serialize(serialization);
    serialization.append(" ").append(predicate.operator).append(" ");
    predicate.b.serialize(serialization);
  }

  protected void serialize(final InPredicate<?> predicate, final Serialization serialization) throws IOException {
    predicate.dataType.serialize(serialization);
    serialization.append(" ");
    if (!predicate.positive)
      serialization.append("NOT ");

    serialization.append("IN").append(" (");
    for (int i = 0; i < predicate.values.length; i++) {
      if (i > 0)
        serialization.append(", ");

      predicate.values[i].serialize(serialization);
    }

    serialization.append(")");
  }

  protected void serialize(final ExistsPredicate<?> predicate, final Serialization serialization) throws IOException {
    serialization.append("EXISTS").append(" (");
    predicate.subQuery.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final LikePredicate predicate, final Serialization serialization) throws IOException {
    predicate.dataType.serialize(serialization);
    serialization.append(" ");
    if (!predicate.positive)
      serialization.append("NOT ");

    serialization.append("LIKE").append(" '").append(predicate.pattern).append("'");
  }

  protected void serialize(final QuantifiedComparisonPredicate<?> predicate, final Serialization serialization) throws IOException {
    serialization.append(predicate.qualifier).append(" (");
    predicate.subQuery.serialize(serialization);
    serialization.append(")");
  }

  protected <T>void serialize(final BetweenPredicate<T> predicate, final Serialization serialization) throws IOException {
    serialization.append("(");
    predicate.dataType.serialize(serialization);
    serialization.append(")");
    if (!predicate.positive)
      serialization.append(" NOT ");

    serialization.append(" BETWEEN ");
    predicate.a.serialize(serialization);
    serialization.append(" AND ");
    predicate.b.serialize(serialization);
  }

  protected <T>void serialize(final NullPredicate<T> predicate, final Serialization serialization) throws IOException {
    predicate.dataType.serialize(serialization);
    serialization.append(" IS ");
    if (!predicate.positive)
      serialization.append("NOT ");

    serialization.append("NULL");
  }

  protected void serialize(final function.numeric.Pi function, final Serialization serialization) {
    serialization.append("PI()");
  }

  protected void serialize(final function.numeric.Abs function, final Serialization serialization) throws IOException {
    serialization.append("ABS(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Sign function, final Serialization serialization) throws IOException {
    serialization.append("SIGN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Round function, final Serialization serialization) throws IOException {
    serialization.append("ROUND(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Floor function, final Serialization serialization) throws IOException {
    serialization.append("FLOOR(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Ceil function, final Serialization serialization) throws IOException {
    serialization.append("CEIL(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Sqrt function, final Serialization serialization) throws IOException {
    serialization.append("SQRT(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Pow function, final Serialization serialization) throws IOException {
    serialization.append("POWER(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Mod function, final Serialization serialization) throws IOException {
    serialization.append("MOD(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Sin function, final Serialization serialization) throws IOException {
    serialization.append("SIN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Asin function, final Serialization serialization) throws IOException {
    serialization.append("ASIN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Cos function, final Serialization serialization) throws IOException {
    serialization.append("COS(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Acos function, final Serialization serialization) throws IOException {
    serialization.append("ACOS(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Tan function, final Serialization serialization) throws IOException {
    serialization.append("TAN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Atan function, final Serialization serialization) throws IOException {
    serialization.append("ATAN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Atan2 function, final Serialization serialization) throws IOException {
    serialization.append("ATAN2(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Exp function, final Serialization serialization) throws IOException {
    serialization.append("EXP(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Ln function, final Serialization serialization) throws IOException {
    serialization.append("LN(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Log function, final Serialization serialization) throws IOException {
    serialization.append("LOG(");
    function.a.serialize(serialization);
    serialization.append(", ");
    function.b.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Log2 function, final Serialization serialization) throws IOException {
    serialization.append("LOG2(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final function.numeric.Log10 function, final Serialization serialization) throws IOException {
    serialization.append("LOG10(");
    function.a.serialize(serialization);
    serialization.append(")");
  }

  protected void serialize(final CountFunction count, final Serialization serialization) throws IOException {
    serialization.append(count.function).append("(");
    if (count.column == null) {
      serialization.append("*");
    }
    else {
      if (count.distinct)
        serialization.append("DISTINCT ");

      count.column.serialize(serialization);
    }

    serialization.append(")");
  }

  protected void serialize(final SetFunction function, final Serialization serialization) throws IOException {
    serialization.append(function.function).append("(");
    if (function.a != null) {
      if (function.distinct)
        serialization.append("DISTINCT ");

      function.a.serialize(serialization);

      if (function.b != null) {
        serialization.append(", ");
        function.b.serialize(serialization);
      }
    }

    serialization.append(")");
  }

  protected void serialize(final OrderingSpec spec, final Serialization serialization) throws IOException {
    spec.dataType.serialize(serialization);
    serialization.append(" ").append(spec.operator);
  }

  protected void serialize(final TemporalFunction function, final Serialization serialization) {
    serialization.append(function.function).append("()");
  }

  protected <T>String serialize(final type.ARRAY<T> serializable, final type.DataType<T> dataType) throws IOException {
    final StringBuilder builder = new StringBuilder();
    final type.DataType<T> clone = dataType.clone();
    for (final T item : serializable.get()) {
      type.DataType.setValue(clone, item);
      builder.append(", ").append(type.DataType.serialize(dataType, getVendor()));
    }

    return "(" + builder.substring(2) + ")";
  }

  protected void serialize(final Cast.AS as, final Serialization serialization) throws IOException {
    serialization.append("CAST(");
    as.dataType.serialize(serialization);
    serialization.append(" AS ").append(as.cast.declare(serialization.vendor)).append(")");
  }

  protected String cast(final type.DataType<?> dataType, final Serialization serialization) {
    return dataType.declare(serialization.vendor);
  }

  protected String serialize(final type.BIGINT serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.BIGINT.UNSIGNED serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.BINARY serializable) {
    return serializable.get() == null ? "NULL" : "X'" + new Hexadecimal(serializable.get()) + "'";
  }

  protected String serialize(final type.BLOB serializable) throws IOException {
    return serializable.get() == null ? "NULL" : "X'" + new Hexadecimal(Streams.readBytes(serializable.get())) + "'";
  }

  protected String serialize(final type.BOOLEAN serializable) {
    return String.valueOf(serializable.get()).toUpperCase();
  }

  protected String serialize(final type.CHAR serializable) {
    return serializable.get() == null ? "NULL" : "'" + serializable.get().replace("'", "''") + "'";
  }

  protected String serialize(final type.CLOB serializable) throws IOException {
    return serializable.get() == null ? "NULL" : "'" + Readers.readFully(serializable.get()) + "'";
  }

  protected String serialize(final type.DATE serializable) {
    return serializable.get() == null ? "NULL" : Dialect.DATE_FORMAT.format(serializable.get());
  }

  protected String serialize(final type.DATETIME serializable) {
    return serializable.get() == null ? "NULL" : Dialect.DATETIME_FORMAT.format(serializable.get());
  }

  protected String serialize(final type.DECIMAL serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.DOUBLE serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.ENUM<?> serializable) {
    return serializable.get() == null ? "NULL" : "'" + serializable.get() + "'";
  }

  protected String serialize(final type.FLOAT serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.INT serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.INT.UNSIGNED serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.SMALLINT serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.SMALLINT.UNSIGNED serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.TINYINT serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.TINYINT.UNSIGNED serializable) {
    return serializable.get() == null ? "NULL" : Dialect.NUMBER_FORMAT.get().format(serializable.get());
  }

  protected String serialize(final type.TIME serializable) {
    return serializable.get() == null ? "NULL" : Dialect.TIME_FORMAT.format(serializable.get());
  }

  protected void assignAliases(final Select.FROM<?> from, final Serialization serialization) {
    if (from != null)
      for (final Entity table : from.tables)
        serialization.registerAlias(table);
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
    final java.sql.Clob value = resultSet.getClob(columnIndex);
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
      statement.setDate(parameterIndex, new java.sql.Date(value.getYear() - 1900, value.getMonthValue() - 1, value.getDayOfMonth()));
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
      statement.setTimestamp(parameterIndex, java.sql.Timestamp.valueOf("1970-01-01 " + value.format(Dialect.TIME_FORMAT)));
    else
      statement.setNull(parameterIndex, dataType.sqlType());
  }

  protected LocalTime getParameter(final type.TIME dataType, final ResultSet resultSet, final int columnIndex) throws SQLException {
    final Time value = resultSet.getTime(columnIndex);
    return resultSet.wasNull() || value == null ? null : value.toLocalTime();
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
    final java.sql.Timestamp value = resultSet.getTimestamp(columnIndex);
    return resultSet.wasNull() || value == null ? null : LocalDateTime.of(value.getYear() + 1900, value.getMonth() + 1, value.getDate(), value.getHours(), value.getMinutes(), value.getSeconds(), value.getNanos());
  }
}