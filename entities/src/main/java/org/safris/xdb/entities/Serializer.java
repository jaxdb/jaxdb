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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;

import org.safris.commons.lang.PackageLoader;
import org.safris.commons.lang.PackageNotFoundException;
import org.safris.commons.lang.reflect.Classes;
import org.safris.xdb.entities.DML.Direction;
import org.safris.xdb.entities.Delete.DELETE;
import org.safris.xdb.entities.Update.UPDATE;
import org.safris.xdb.entities.binding.Interval;
import org.safris.xdb.entities.binding.Interval.Unit;
import org.safris.xdb.schema.DBVendor;

abstract class Serializer {
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
    return serializers[vendor.ordinal()];
  }

  public static void serialize(final Serializable serializable, final Serialization serialization) {
    final Serializer serializer = getSerializer(serialization.getVendor());
    if (serializer == null)
      throw new UnsupportedOperationException("Vendor " + serialization.getVendor() + " is not supported");

      try {
        Method method = null;
        Class<?> cls = serializable.getClass();
        do
          method = Classes.getDeclaredMethod(Serializer.class, "serialize", cls, Serialization.class);
        while (method == null && (cls = cls.getSuperclass()) != null);
        method.invoke(serializer, serializable, serialization);
      }
      catch (final IllegalAccessException | InvocationTargetException e) {
        throw new UnsupportedOperationException(e);
      }
  }

  protected static void serializeEntities(final LinkedHashSet<? extends Subject<?>> entities, final Serialization serialization) {
    final Iterator<? extends Subject<?>> iterator = entities.iterator();
    while (iterator.hasNext()) {
      final Subject<?> subject = iterator.next();
      if (subject instanceof Entity) {
        final Entity entity = (Entity)subject;
        final String alias = Subject.subjectAlias(entity, true);
        final DataType<?>[] dataTypes = entity.column();
        for (int j = 0; j < dataTypes.length; j++) {
          final DataType<?> dataType = dataTypes[j];
          if (j > 0)
            serialization.append(", ");

          serialization.append(alias).append(".").append(dataType.name);
        }
      }
      else if (subject instanceof DataType<?>) {
        Subject.subjectAlias(((DataType<?>)subject).entity, true);
        final DataType<?> dataType = (DataType<?>)subject;
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

  protected String getPreparedStatementMark(final DataType<?> dataType) {
    return "?";
  }

  protected <T extends Subject<?>>void serialize(final Entity serializable, final Serialization serialization) {
    serialization.append(tableName(serializable, serialization));
    final String alias = Subject.subjectAlias(serializable, true);
    if (serialization.getType() == Select.class)
      serialization.append(" ").append(alias);
  }

  protected <T extends Subject<?>>void serialize(final Evaluation<T> serializable, final Serialization serialization) {
    serialization.append("(");
    Keyword.format(serializable.a, serialization);
    for (int i = serializable.startIndex; i < serializable.args.length; i++) {
      final Object arg = serializable.args[i];
      if (arg instanceof Interval) {
        final Interval interval = (Interval)arg;

        final Set<Unit> units = interval.getUnits();
        final StringBuilder clause = new StringBuilder();
        for (final Unit unit : units)
          clause.append(" ").append(interval.getComponent(unit)).append(" " + unit.name());

        serialization.append(" '").append(clause.substring(1)).append("'");
      }
      else {
        serialization.append(" ").append(serializable.operator.toString()).append(" ");
        Keyword.format(arg, serialization);
      }
    }
    serialization.append(")");
  }

  protected <T extends Subject<?>>void serialize(final Select.SELECT<T> serializable, final Serialization serialization) {
    serialization.append("SELECT ");
    if (serializable.all != null) {
      serializable.all.serialize(serialization);
      serialization.append(" ");
    }

    if (serializable.distinct != null) {
      serializable.distinct.serialize(serialization);
      serialization.append(" ");
    }

    serializeEntities(serializable.entities, serialization);
  }

  protected <T extends Subject<?>>void serialize(final Select.FROM<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" FROM ");

    // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
    // FIXME: referring to, because this table must be the last in the table order here
    final Entity[] tables = serializable.tables;
    for (int i = 0; i < tables.length; i++) {
      if (i > 0)
        serialization.append(", ");

      serialization.append(tableName(tables[i], serialization)).append(" ").append(Subject.subjectAlias(tables[i], true));
    }
  }

  protected <T extends Subject<?>>void serialize(final Select.WHERE<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" WHERE ");
    serializable.condition.serialize(serialization);
  }

  protected <T extends Subject<?>>void serialize(final Select.GROUP_BY<T> serializable, final Serialization serialization) {
    if (serializable.parent() != null)
      serializable.parent().serialize(serialization);

    serialization.append(" GROUP BY ");
    serializeEntities(serializable.subjects, serialization);
  }

  protected <T extends Subject<?>>void serialize(final Select.JOIN<T> serializable, final Serialization serialization) {
    // NOTE: JOINed tables must have aliases. So, if the JOINed table is not part of the SELECT,
    // NOTE: it will not have had this assignment made. Therefore, ensure it's been made!
    Subject.subjectAlias(serializable.entity, true);
    serializable.parent().serialize(serialization);
    serialization.append(serializable.natural != null ? " NATURAL" : "");
    if (serializable.type != null) {
      serialization.append(" ");
      serializable.type.serialize(serialization);
    }

    serialization.append(" JOIN ").append(tableName(serializable.entity, serialization)).append(" ").append(Subject.subjectAlias(serializable.entity, true));
  }

  protected <T extends Subject<?>>void serialize(final Select.ON<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" ON (");
    serializable.condition.serialize(serialization);
    serialization.append(")");
  }

  protected <T extends Subject<?>>void serialize(final Select.LIMIT<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" LIMIT " + serializable.limit);
  }

  protected <T extends Subject<?>>void serialize(final Select.ORDER_BY<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" ORDER BY ");
    for (int i = 0; i < serializable.columns.length; i++) {
      final Variable<?> variable = serializable.columns[i];
      if (i > 0)
        serialization.append(", ");

      if (variable instanceof DataType<?>) {
        final DataType<?> dataType = (DataType<?>)variable;
        Subject.subjectAlias(dataType.entity, true);
        serialization.append(dataType.toString());
        serialization.append(" ASC");
      }
      else if (variable instanceof Direction<?>) {
        ((Direction<?>)variable).serialize(serialization);
      }
      else {
        throw new UnsupportedOperationException("Unsupported column type: " + variable.getClass().getName());
      }
    }
  }

  protected <T extends Subject<?>>void serialize(final Select.HAVING<T> serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" HAVING ");
    serializable.condition.serialize(serialization);
  }

  protected <T>void serialize(final As<T> serializable, final Serialization serialization) {
    Subject.subjectAlias(serializable.getVariable(), true);
    serializable.getParent().serialize(serialization);
    serialization.append(" AS ").append(serializable.getVariable().serialize());
    serializable.getVariable().setWrapper(serializable.getParent());
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void serialize(final Update.UPDATE serializable, final Serialization serialization) {
    if (serializable.getClass() != UPDATE.class) // means that there are subsequent clauses
      throw new Error("Need to override this");

    if (!(serialization.getCaller().peek() instanceof Update.SET) && serializable.entity.primary().length == 0)
      throw new UnsupportedOperationException("Entity '" + serializable.entity.name() + "' does not have a primary key, nor was WHERE clause specified");

    serialization.append("UPDATE ");
    serializable.entity.serialize(serialization);
    if (!(serialization.getCaller().peek() instanceof Update.SET)) {
      final StringBuilder setClause = new StringBuilder();
      for (final DataType dataType : serializable.entity.column()) {
        if (!dataType.primary && (dataType.wasSet() || dataType.generateOnUpdate != null)) {
          if (dataType.generateOnUpdate != null)
            dataType.value = dataType.generateOnUpdate.generateStatic(dataType);

          serialization.addParameter(dataType);
          setClause.append(", ").append(dataType.name).append(" = ").append(getPreparedStatementMark(dataType));
        }
      }

      serialization.append(" SET ").append(setClause.substring(2));
      final StringBuilder whereClause = new StringBuilder();
      for (final DataType dataType : serializable.entity.column()) {
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

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected void serialize(final Update.SET serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    if (serializable.parent() instanceof Update.UPDATE)
      serialization.append(" SET ");

    serializable.set.serialize(serialization);
    serialization.append(" = ");
    Keyword.format(serializable.to, serialization);
    if (serialization.getCaller().peek() instanceof Update.SET) {
      serialization.append(", ");
    }
    else {
      final Set<DataType<?>> setColumns = new HashSet<DataType<?>>();
      final Entity entity = serializable.getSetColumns(setColumns);
      final StringBuilder setClause = new StringBuilder();
      for (final DataType column : entity.column())
        if (!setColumns.contains(column) && column.generateOnUpdate != null)
          setClause.append(", ").append(column.name).append(" = ").append(column.generateOnUpdate.generateDynamic(serialization, column));

      serialization.append(setClause);
    }
  }

  protected void serialize(final Update.WHERE serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" WHERE ");
    serializable.condition.serialize(serialization);
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  protected <T extends Entity>void serialize(final Insert.INSERT<T> serializable, final Serialization serialization) {
    if (serializable.entities.length == 0)
      throw new IllegalArgumentException("entities.length == 0");

    final StringBuilder columns = new StringBuilder();
    final StringBuilder values = new StringBuilder();
    if (serialization.statementType == PreparedStatement.class) {
      for (int i = 0; i < serializable.entities.length; i++) {
        final Entity entity = serializable.entities[i];
        serialization.append("INSERT INTO ");
        entity.serialize(serialization);
        for (final DataType dataType : entity.column()) {
          if (!dataType.wasSet()) {
            if (dataType.generateOnInsert == null)
              continue;

            dataType.value = dataType.generateOnInsert.generateStatic(dataType);
          }

          columns.append(", ").append(dataType.name);
          values.append(", ").append(getPreparedStatementMark(dataType));
          serialization.addParameter(dataType);
        }

        serialization.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
        if (i < serializable.entities.length - 1) {
          serialization.addBatch();
          columns.setLength(0);
          values.setLength(0);
        }
      }
    }
    else {
      for (int i = 0; i < serializable.entities.length; i++) {
        final Entity entity = serializable.entities[i];
        serialization.append("INSERT INTO ");
        entity.serialize(serialization);
        for (final DataType dataType : entity.column()) {
          if (!dataType.wasSet()) {
            if (dataType.generateOnInsert == null)
              continue;

            dataType.value = dataType.generateOnInsert.generateStatic(dataType);
          }

          columns.append(", ").append(dataType.name);
          values.append(", ").append(VariableWrapper.toString(dataType.get()));
        }

        serialization.append(" (").append(columns.substring(2)).append(") VALUES (").append(values.substring(2)).append(")");
        if (i < serializable.entities.length - 1) {
          serialization.addBatch();
          columns.setLength(0);
          values.setLength(0);
        }
      }
    }
  }

  protected <T extends Entity>void serialize(final Delete.DELETE serializable, final Serialization serialization) {
    if (serializable.getClass() != DELETE.class) // means that there are subsequent clauses
      throw new Error("Need to override this");

    if (serializable.entity.primary().length == 0)
      throw new UnsupportedOperationException("Entity '" + serializable.entity.name() + "' does not have a primary key");

    if (serialization.getCaller().peek() == serializable && !serializable.entity.wasSelected())
      throw new UnsupportedOperationException("Entity '" + serializable.entity.name() + "' did not come from a SELECT");

    serialization.append("DELETE FROM ");
    serializable.entity.serialize(serialization);

    if (serialization.getCaller().peek() == serializable) {
      final StringBuilder whereClause = new StringBuilder();
      for (final DataType<?> dataType : serializable.entity.primary()) {
        serialization.addParameter(dataType);
        whereClause.append(" AND ").append(dataType.name).append(" = ?");
      }

      serialization.append(" WHERE ").append(whereClause.substring(5));
    }
  }

  protected <T extends Entity>void serialize(final Delete.WHERE serializable, final Serialization serialization) {
    serializable.parent().serialize(serialization);
    serialization.append(" WHERE ");
    serializable.condition.serialize(serialization);
  }
}