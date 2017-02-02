/* Copyright (c) 2017 Seva Safris
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

package org.safris.xdb.entities;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.safris.commons.io.Readers;
import org.safris.commons.io.Streams;
import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.PackageNotFoundException;
import org.safris.commons.util.Hexadecimal;
import org.safris.xdb.entities.Interval.Unit;
import org.safris.xdb.schema.DBVendor;

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

  public static Serializer getSerializer(final DBVendor vendor) {
    final Serializer serializer = serializers[vendor.ordinal()];
    if (serializer == null)
      throw new UnsupportedOperationException("Vendor " + vendor + " is not supported");

    return serializer;
  }

  protected static void serializeEntities(final Collection<? extends Subject<?>> entities, final Serialization serialization) throws IOException {
    final Iterator<? extends Subject<?>> iterator = entities.iterator();
    while (iterator.hasNext()) {
      final Subject<?> subject = iterator.next();
      if (subject instanceof Entity) {
        final Entity entity = (Entity)subject;
        final Alias alias = serialization.registerAlias(entity);
        final type.DataType<?>[] dataTypes = entity.column();
        for (int j = 0; j < dataTypes.length; j++) {
          final type.DataType<?> dataType = dataTypes[j];
          if (j > 0)
            serialization.append(", ");

          alias.serialize(serialization);
          serialization.append(".").append(dataType.name);
        }
      }
      else if (subject instanceof type.DataType<?>) {
        serialization.registerAlias(((type.DataType<?>)subject).owner);
        final type.DataType<?> dataType = (type.DataType<?>)subject;
        dataType.serialize(serialization);
      }

      if (iterator.hasNext())
        serialization.append(", ");
    }
  }

  protected abstract DBVendor getVendor();

  @SuppressWarnings("unused")
  protected void onRegister(final Connection connection) throws SQLException {
  }

  protected String tableName(final Entity entity, final Serialization serialization) {
    return entity.name();
  }

  protected String getPreparedStatementMark(final type.DataType<?> dataType) {
    return "?";
  }

  protected void serialize(final Case.CASE_WHEN caseWhen, final CaseCommand command, final Serialization serialization) {
    // TODO
    throw new UnsupportedOperationException();
  }

  protected void serialize(final Case.THEN then, final CaseCommand command, final Serialization serialization) {
    // TODO
    throw new UnsupportedOperationException();
  }

  protected void serialize(final Case.ELSE<?> els, final CaseCommand command, final Serialization serialization) {
    // TODO
    throw new UnsupportedOperationException();
  }

  protected void serialize(final Select.SELECT<?> select, final Serialization serialization) throws IOException {
    serialization.append("SELECT ");
    if (select.distinct)
      serialization.append("DISTINCT ");

    serializeEntities(select.entities, serialization);
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
      serialization.append(" LEFT OUTER");

    serialization.append(" JOIN ").append(tableName(join.table, serialization)).append(" ");
    serialization.registerAlias(join.table).serialize(serialization);
    if (on != null) {
      serialization.append(" ON (");
      on.condition.serialize(serialization);
      serialization.append(")");
    }
  }

  protected void serialize(final Select.WHERE<?> where, final Serialization serialization) throws IOException {
    if (where != null) {
      serialization.append(" WHERE ");
      where.condition.serialize(serialization);
    }
  }

  protected void serialize(final Select.GROUP_BY<?> groupBy, final Serialization serialization) throws IOException {
    serialization.append(" GROUP BY ");
    serializeEntities(groupBy.subjects, serialization);
  }

  protected void serialize(final Select.HAVING<?> having, final Serialization serialization) throws IOException {
    serialization.append(" HAVING ");
    having.condition.serialize(serialization);
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
    serialization.append(" LIMIT " + limit.rows);
  }

  protected void serialize(final Select.UNION<?> union, final Serialization serialization) throws IOException {
    if (union != null) {
      serialization.append(" UNION ");
      if (union.all)
        serialization.append("ALL ");

      union.select.serialize(serialization);
    }
  }

  protected void serialize(final Insert.INSERT<?> insert, final Serialization serialization) {
    if (insert.entities.length == 0)
      throw new IllegalArgumentException("entities.length == 0");

    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();
//    if (serialization.statementType == PreparedStatement.class) {
//      for (int i = 0; i < serializable.entities.length; i++) {
//        final Entity entity = serializable.entities[i];
//        serialization.append("INSERT INTO ");
//        entity.serialize(serialization);
//        for (final type.DataType dataType : entity.column()) {
//          if (!dataType.wasSet()) {
//            if (dataType.generateOnInsert == null)
//              continue;
//
//            dataType.value = dataType.generateOnInsert.generateStatic(dataType);
//          }
//
//          columns.append(", ").append(dataType.name);
//          values.append(", ").append(getPreparedStatementMark(dataType));
//          serialization.addParameter(dataType);
//        }
//
//        serialization.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
//        if (i < serializable.entities.length - 1) {
//          serialization.addBatch();
//          columns.setLength(0);
//          values.setLength(0);
//        }
//      }
//    }
//    else {
//      for (int i = 0; i < serializable.entities.length; i++) {
//        final Entity entity = serializable.entities[i];
//        serialization.append("INSERT INTO ");
//        entity.serialize(serialization);
//        for (final type.DataType dataType : entity.column()) {
//          if (!dataType.wasSet()) {
//            if (dataType.generateOnInsert == null)
//              continue;
//
//            dataType.value = dataType.generateOnInsert.generateStatic(dataType);
//          }
//
//          columns.append(", ").append(dataType.name);
//          values.append(", ").append(VariableWrapper.toString(dataType.get()));
//        }
//
//        serialization.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
//        if (i < serializable.entities.length - 1) {
//          serialization.addBatch();
//          columns.setLength(0);
//          values.setLength(0);
//        }
//      }
//    }
  }

  protected void serialize(final Update.UPDATE update, final Serialization serialization) throws IOException {
    final UpdateCommand command = (UpdateCommand)serialization.command;
    if (command.set() == null && update.entity.primary().length == 0)
      throw new UnsupportedOperationException("Entity '" + update.entity.name() + "' does not have a primary key, nor was WHERE clause specified");

    serialization.append("UPDATE ");
    update.entity.serialize(serialization);
    if (command.set() == null) {
      final StringBuilder setClause = new StringBuilder();
      for (final type.DataType dataType : update.entity.column()) {
        if (!dataType.primary && (dataType.wasSet() || dataType.generateOnUpdate != null)) {
          if (dataType.generateOnUpdate != null)
            dataType.value = dataType.generateOnUpdate.generateStatic(dataType);

          serialization.addParameter(dataType);
          setClause.append(", ").append(dataType.name).append(" = ").append(getPreparedStatementMark(dataType));
        }
      }

      serialization.append(" SET ").append(setClause.substring(2));
      final StringBuilder whereClause = new StringBuilder();
      for (final type.DataType dataType : update.entity.column()) {
        if (dataType.primary) {
          if (dataType.generateOnUpdate != null)
            dataType.value = dataType.generateOnUpdate.generateStatic(dataType);

          serialization.addParameter(dataType);
          whereClause.append(" AND ").append(dataType.name).append(" = ").append(getPreparedStatementMark(dataType));
        }
      }

      serialization.append(" WHERE ").append(whereClause.substring(5));
    }
  }

  protected void serialize(final Update.SET set, final Serialization serialization) throws IOException {
    if (set.parent() instanceof Update.UPDATE)
      serialization.append(" SET ");

    set.set.serialize(serialization);
    serialization.append(" = ");
    set.to.serialize(serialization);
    if (set.parent() instanceof Update.SET) {
      serialization.append(", ");
    }
    else {
      final Set<type.DataType<?>> setColumns = new HashSet<type.DataType<?>>();
      final Entity entity = set.getSetColumns(setColumns);
      final StringBuilder setClause = new StringBuilder();
      for (final type.DataType column : entity.column())
        if (!setColumns.contains(column) && column.generateOnUpdate != null)
          setClause.append(", ").append(column.name).append(" = ").append(column.generateOnUpdate.generateDynamic(serialization, column));

      serialization.append(setClause);
    }
  }

  protected void serialize(final Update.WHERE where, final Serialization serialization) throws IOException {
    serialization.append(" WHERE ");
    where.condition.serialize(serialization);
  }

  protected void serialize(final Delete.DELETE delete, final Serialization serialization) throws IOException {
    if (delete.entity.primary().length == 0)
      throw new UnsupportedOperationException("Entity '" + delete.entity.name() + "' does not have a primary key");

//    if (serialization.getCaller().peek() == serializable && !serializable.entity.wasSelected())
//      throw new UnsupportedOperationException("Entity '" + serializable.entity.name() + "' did not come from a SELECT");

    serialization.append("DELETE FROM ");
    delete.entity.serialize(serialization);

//    if (serialization.getCaller().peek() == serializable) {
//      final StringBuilder whereClause = new StringBuilder();
//      for (final type.DataType<?> dataType : serializable.entity.primary()) {
//        serialization.addParameter(dataType);
//        whereClause.append(" AND ").append(dataType.name).append(" = ?");
//      }
//
//      serialization.append(" WHERE ").append(whereClause.substring(5));
//    }
  }

  protected void serialize(final Delete.WHERE where, final Serialization serialization) throws IOException {
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
    final Set<Unit> units = interval.getUnits();
    final StringBuilder clause = new StringBuilder();
    for (final Unit unit : units)
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

  protected <T>void serialize(final As<T> as, final Serialization serialization) throws IOException {
    final Alias alias = serialization.registerAlias(as.getVariable());
    serialization.append("(");
    as.parent().serialize(serialization);
    serialization.append(")");
    serialization.append(" AS ");
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

  @SuppressWarnings("static-method")
  protected final void serialize(final ComparisonPredicate<?> predicate, final Serialization serialization) throws IOException {
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

  public <T>String serialize(final type.ARRAY<T> serializable, final type.DataType<T> dataType) throws IOException {
    final StringBuilder builder = new StringBuilder();
    final type.DataType<T> clone = dataType.clone();
    for (final T item : serializable.get()) {
      type.DataType.setValue(clone, item);
      builder.append(", ").append(type.DataType.serialize(dataType, getVendor()));
    }

    return "(" + builder.substring(2) + ")";
  }

  public void serialize(final Cast.AS as, final Serialization serialization) throws IOException {
    serialization.append("CAST(");
    as.dataType.serialize(serialization);
    serialization.append(" AS ").append(as.castAs.declare(serialization.vendor)).append(")");
  }

  protected String serialize(final type.BIGINT serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.BINARY serializable) {
    return serializable.get() == null ? "NULL" : "X'" + new Hexadecimal(serializable.get()) + "'";
  }

  protected String serialize(final type.BLOB serializable) throws IOException {
    return serializable.get() == null ? "NULL" : "X'" + new Hexadecimal(Streams.getBytes(serializable.get())) + "'";
  }

  protected String serialize(final type.BOOLEAN serializable) {
    return String.valueOf(serializable.get()).toUpperCase();
  }

  protected String serialize(final type.CHAR serializable) {
    return serializable.get() == null ? "NULL" : "'" + serializable.get() + "'";
  }

  protected String serialize(final type.CLOB serializable) throws IOException {
    return serializable.get() == null ? "NULL" : "'" + Readers.readFully(serializable.get()) + "'";
  }

  protected String serialize(final type.DATE serializable) {
    return serializable.get() == null ? "NULL" : type.DATE.dateFormat.format(serializable.get());
  }

  protected String serialize(final type.DATETIME serializable) {
    return serializable.get() == null ? "NULL" : type.DATETIME.dateTimeFormat.format(serializable.get());
  }

  protected String serialize(final type.DECIMAL serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.DOUBLE serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.ENUM<?> serializable) {
    return serializable.get() == null ? "NULL" : "'" + serializable.get() + "'";
  }

  protected String serialize(final type.FLOAT serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.INTEGER serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.MEDIUMINT serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.TINYINT serializable) {
    return serializable.get() == null ? "NULL" : type.Numeric.numberFormat.get().format(serializable.get());
  }

  protected String serialize(final type.TIME serializable) {
    return serializable.get() == null ? "NULL" : type.TIME.timeFormat.format(serializable.get());
  }

  public void assignAliases(final Select.FROM<?> from, final Serialization serialization) {
    if (from != null)
      for (final Entity table : from.tables)
        serialization.registerAlias(table);
  }
}