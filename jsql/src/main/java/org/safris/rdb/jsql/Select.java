/* Copyright (c) 2015 lib4j
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

package org.safris.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.safris.commons.lang.Pair;
import org.safris.commons.util.Collections;
import org.safris.rdb.jsql.exception.SQLExceptionCatalog;
import org.safris.rdb.jsql.model.select;
import org.safris.rdb.vendor.DBVendor;

public final class Select {
  private static void serialize(final List<Pair<type.DataType<?>,Integer>> dataTypes, final Subject<?> subject) {
    if (subject instanceof Entity) {
      final Entity entity = (Entity)subject;
      for (int i = 0; i < entity.column().length; i++)
        dataTypes.add(new Pair<type.DataType<?>,Integer>(entity.column()[i], i));
    }
    else if (subject instanceof type.DataType) {
      final type.DataType<?> dataType = (type.DataType<?>)subject;
      dataTypes.add(new Pair<type.DataType<?>,Integer>(dataType, -1));
    }
    else {
      throw new UnsupportedOperationException("Unknown entity type: " + subject.getClass().getName());
    }
  }

  private static <B extends Subject<?>>RowIterator<B> parseResultSet(final DBVendor vendor, final Connection connection, final ResultSet resultSet, final SELECT<?> select, final boolean skipFirstColumn) throws SQLException {
    final List<Pair<type.DataType<?>,Integer>> dataTypes = new ArrayList<Pair<type.DataType<?>,Integer>>();
    for (final Subject<?> entity : select.entities)
      Select.serialize(dataTypes, entity);

    final int columnOffset = skipFirstColumn ? 2 : 1;
    final int noColumns = resultSet.getMetaData().getColumnCount() + 1 - columnOffset;
    return new RowIterator<B>() {
      private final Map<Class<? extends Entity>,Entity> prototypes = new HashMap<Class<? extends Entity>,Entity>();
      private final Map<Entity,Entity> cache = new HashMap<Entity,Entity>();
      private Entity currentTable = null;

      @Override
      @SuppressWarnings({"rawtypes", "unchecked"})
      public boolean nextRow() throws SQLException {
        if (rowIndex + 1 < rows.size()) {
          ++rowIndex;
          resetEntities();
          return true;
        }

        Subject<?>[] row;
        int index;
        Entity entity;
        try {
          if (!resultSet.next())
            return false;

          row = new Subject[select.entities.size()];
          index = 0;
          entity = null;
          for (int i = 0; i < noColumns; i++) {
            final Pair<type.DataType<?>,Integer> dataTypePrototype = dataTypes.get(i);
            final type.DataType dataType;
            if (currentTable != null && (currentTable != dataTypePrototype.a.owner || dataTypePrototype.b == -1)) {
              final Entity cached = cache.get(entity);
              if (cached != null) {
                row[index++] = cached;
              }
              else {
                row[index++] = entity;
                cache.put(entity, entity);
                prototypes.put(entity.getClass(), entity.newInstance());
              }
            }

            if (dataTypePrototype.b == -1) {
              entity = null;
              currentTable = null;
              dataType = dataTypePrototype.a.clone();
              row[index++] = dataType;
            }
            else {
              currentTable = dataTypePrototype.a.owner;
              entity = prototypes.get(currentTable.getClass());
              if (entity == null)
                prototypes.put(currentTable.getClass(), entity = currentTable.newInstance());

              dataType = entity.column()[dataTypePrototype.b];
            }

            dataType.set(resultSet, i + columnOffset);
          }
        }
        catch (final SQLException e) {
          throw SQLExceptionCatalog.lookup(e);
        }

        if (entity != null) {
          final Entity cached = cache.get(entity);
          row[index++] = cached != null ? cached : entity;
        }

        rows.add((B[])row);
        ++rowIndex;
        resetEntities();
        prototypes.clear();
        currentTable = null;
        return true;
      }

      @Override
      public void close() throws SQLException {
        try {
          connection.close();
        }
        catch (final SQLException e) {
          throw SQLExceptionCatalog.lookup(e);
        }
        finally {
          prototypes.clear();
          cache.clear();
          currentTable = null;
          dataTypes.clear();
          rows.clear();
        }
      }
    };
  }

  protected static abstract class Execute<T extends Subject<?>> extends Keyword<T> implements select.SELECT<T>, select.UNION<T> {
    protected Execute(final Keyword<T> parent) {
      super(parent);
    }

    @Override
    public T AS(final T as) {
      final Command command = normalize();
      as.wrapper(new As<T>(command, as));
      return as;
    }

    @Override
    public select.UNION<T> UNION(final select.SELECT<T> union) {
      return new Select.UNION<T>(this, false, union);
    }

    @Override
    public select._UNION.ALL<T> UNION() {
      return new select.UNION.ALL<T>(){
        @Override
        public Select.UNION<T> ALL(final select.SELECT<T> union) {
          return new Select.UNION<T>(Execute.this, true, union);
        }
      };
    }

    @Override
    public final RowIterator<T> execute() throws IOException, SQLException {
      return execute(null);
    }

    @Override
    public RowIterator<T> execute(final Transaction transaction) throws IOException, SQLException {
      final SelectCommand command = (SelectCommand)normalize();

      // FIXME: This means that there MUST BE a FROM clause
      final Class<? extends Schema> schema = command.from().tables.iterator().next().schema();
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        final DBVendor vendor = Schema.getDBVendor(connection);

        final Serialization serialization = new Serialization(command, vendor, DBRegistry.isPrepared(schema), DBRegistry.isBatching(schema));
        command.serialize(serialization);

        final ResultSet resultSet = serialization.executeQuery(connection);
        return parseResultSet(vendor, connection, resultSet, command.select(), serialization.skipFirstColumn());
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }
  }

  protected static abstract class FROM_JOIN_ON<T extends Subject<?>> extends Execute<T> implements select.FROM<T> {
    protected FROM_JOIN_ON(final Keyword<T> parent) {
      super(parent);
    }

    @Override
    public final WHERE<T> WHERE(final Condition<?> condition) {
      return new WHERE<T>(this, condition);
    }

    @Override
    public HAVING<T> HAVING(final Condition<?> condition) {
      return new HAVING<T>(this, condition);
    }

    public final class CROSS implements select.FROM.CROSS<T> {
      @Override
      public final select.ADV_JOIN<T> JOIN(final Entity table) {
        return new JOIN<T>(FROM_JOIN_ON.this, table, true, false, false, false);
      }
    }

    public final CROSS CROSS = new CROSS();

    public final class NATURAL implements select.FROM.NATURAL<T> {
      @Override
      public final select.ADV_JOIN<T> JOIN(final Entity table) {
        return new JOIN<T>(FROM_JOIN_ON.this, table, false, true, false, false);
      }
    }

    public final NATURAL NATURAL = new NATURAL();

    public final class LEFT implements select.FROM.OUTER<T> {
      @Override
      public final select.JOIN<T> JOIN(final Entity table) {
        return new JOIN<T>(FROM_JOIN_ON.this, table, false, false, true, false);
      }
    }

    public final LEFT LEFT = new LEFT();

    public final class RIGHT implements select.FROM.OUTER<T> {
      @Override
      public final select.JOIN<T> JOIN(final Entity table) {
        return new JOIN<T>(FROM_JOIN_ON.this, table, false, false, false, true);
      }
    }

    public final RIGHT RIGHT = new RIGHT();

    public final class FULL implements select.FROM.OUTER<T> {
      @Override
      public final select.JOIN<T> JOIN(final Entity table) {
        return new JOIN<T>(FROM_JOIN_ON.this, table, false, false, true, true);
      }
    }

    public final FULL FULL = new FULL();

    @Override
    public final select.JOIN<T> JOIN(final Entity table) {
      return new JOIN<T>(this, table, false, false, false, false);
    }

    @Override
    public final LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }
  }

  public static final class FROM<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.FROM<T> {
    protected final Collection<Entity> tables;

    protected FROM(final Keyword<T> parent, final Collection<Entity> tables) {
      super(parent);
      this.tables = tables;
    }

    protected FROM(final Keyword<T> parent, final Entity ... tables) {
      this(parent, Collections.asCollection(ArrayList.class, tables));
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... columns) {
      return new GROUP_BY<T>(this, columns);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class GROUP_BY<T extends Subject<?>> extends Execute<T> implements select.GROUP_BY<T> {
    protected final Collection<Subject<?>> subjects;

    protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
      super(parent);
      this.subjects = subjects;
    }

    protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
      this(parent, Collections.asCollection(LinkedHashSet.class, subjects));
    }

    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    public HAVING<T> HAVING(final Condition<?> condition) {
      return new HAVING<T>(this, condition);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class HAVING<T extends Subject<?>> extends Execute<T> implements select.HAVING<T> {
    protected final Condition<?> condition;

    protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class JOIN<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.JOIN<T>, select.ADV_JOIN<T> {
    protected final boolean cross;
    protected final boolean natural;
    protected final boolean left;
    protected final boolean right;
    protected final Entity table;

    protected JOIN(final Keyword<T> parent, final Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
      super(parent);
      this.cross = cross;
      this.natural = natural;
      this.left = left;
      this.right = right;
      this.table = table;
      if (table == null)
        throw new IllegalArgumentException("table == null");
    }

    @Override
    public ON<T> ON(final Condition<?> condition) {
      return new ON<T>(this, condition);
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class ON<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.ON<T> {
    protected final Condition<?> condition;

    protected ON(final Keyword<T> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class ORDER_BY<T extends Subject<?>> extends Execute<T> implements select.ORDER_BY<T> {
    protected final type.DataType<?>[] columns;
    protected final int[] columnNumbers;

    protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
      super(parent);
      this.columns = columns;
      this.columnNumbers = null;
    }

    protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
      super(parent);
      this.columns = null;
      this.columnNumbers = columnNumbers;
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class LIMIT<T extends Subject<?>> extends Execute<T> implements select.LIMIT<T> {
    protected final int rows;

    protected LIMIT(final Keyword<T> parent, final int rows) {
      super(parent);
      this.rows = rows;
    }

    @Override
    public OFFSET<T> OFFSET(final int rows) {
      return new OFFSET<T>(this, rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class OFFSET<T extends Subject<?>> extends Execute<T> implements select.OFFSET<T> {
    protected final int rows;

    protected OFFSET(final Keyword<T> parent, final int rows) {
      super(parent);
      this.rows = rows;
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final Predicate<Subject<?>> entitiesWithOwnerPredicate = new Predicate<Subject<?>>() {
    @Override
    public boolean test(final Subject<?> t) {
      return (t instanceof type.DataType) && ((type.DataType<?>)t).owner == null;
    }
  };

  protected static class SELECT<T extends Subject<?>> extends Keyword<T> implements select._SELECT<T> {
    protected final boolean distinct;
    protected final Collection<T> entities;

    public SELECT(final boolean distinct, final Collection<T> entities) {
      super(null);
      if (entities.size() < 1)
        throw new IllegalArgumentException("entities.size() < 1");

      this.entities = entities;
      this.distinct = distinct;
    }

    @SafeVarargs
    public SELECT(final boolean distinct, final T entity, T ... entities) {
      this(distinct, Collections.asCollection(ArrayList.class, entity));
      java.util.Collections.addAll(this.entities, entities);
    }

    public SELECT(final boolean distinct, final T[] entities) {
      this(distinct, Collections.asCollection(ArrayList.class, entities));
    }

    @Override
    public FROM<T> FROM(final Entity ... table) {
      return new FROM<T>(this, table);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }

    @Override
    public T AS(final T as) {
      final Command command = normalize();
      as.wrapper(new As<T>(command, as));
      return as;
    }

    @Override
    public select.UNION<T> UNION(final select.SELECT<T> union) {
      return new Select.UNION<T>(this, false, union);
    }

    @Override
    public select._UNION.ALL<T> UNION() {
      return new select.UNION.ALL<T>(){
        @Override
        public Select.UNION<T> ALL(final select.SELECT<T> union) {
          return new Select.UNION<T>(SELECT.this, true, union);
        }
      };
    }

    @Override
    protected final Command normalize() {
      return new SelectCommand(this);
    }

    protected Collection<T> getEntitiesWithOwners() {
      final Collection<T> clone = Collections.clone(entities);
      clone.removeIf(entitiesWithOwnerPredicate);
      return clone;
    }

    @Override
    public RowIterator<T> execute() throws IOException, SQLException {
      return execute(null);
    }

    @Override
    public RowIterator<T> execute(final Transaction transaction) throws IOException, SQLException {
      if (entities.size() == 1) {
        final Subject<?> subject = entities.iterator().next();
        if (subject instanceof Entity) {
          final Entity entity = (Entity)subject;
          final Entity out = entity.newInstance();
          final type.DataType<?>[] dataTypes = entity.column();
          String sql = "SELECT ";
          String select = "";
          String where = "";
          for (final type.DataType<?> dataType : dataTypes) {
            if (dataType.primary)
              where += " AND " + dataType.name + " = ?";
            else
              select += ", " + dataType.name;
          }

          sql += select.substring(2) + " FROM " + entity.name() + " WHERE " + where.substring(5);
          try {
            final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(entity.schema());
            final PreparedStatement statement = connection.prepareStatement(sql);
            int index = 0;
            for (final type.DataType<?> dataType : dataTypes)
              if (dataType.primary)
                dataType.get(statement, ++index);

            try (final ResultSet resultSet = statement.executeQuery()) {
              return new RowIterator<T>() {
                @Override
                @SuppressWarnings({"rawtypes", "unchecked"})
                public boolean nextRow() throws SQLException {
                  if (rowIndex + 1 < rows.size()) {
                    ++rowIndex;
                    resetEntities();
                    return true;
                  }

                  try {
                    if (!resultSet.next())
                      return false;

                    int index = 0;
                    for (final type.DataType dataType : out.column())
                      dataType.set(resultSet, ++index);
                  }
                  catch (final SQLException e) {
                    throw SQLExceptionCatalog.lookup(e);
                  }

                  rows.add((T[])new Entity[] {out});
                  ++rowIndex;
                  resetEntities();
                  return true;
                }

                @Override
                public void close() throws SQLException {
                  try {
                    statement.close();
                    connection.close();
                  }
                  catch (final SQLException e) {
                    throw SQLExceptionCatalog.lookup(e);
                  }
                  finally {
                    rows.clear();
                  }
                }
              };
            }
          }
          catch (final SQLException e) {
            throw SQLExceptionCatalog.lookup(e);
          }
        }
      }

      return null;
    }
  }

  public static final class WHERE<T extends Subject<?>> extends Execute<T> implements select.WHERE<T> {
    protected final Condition<?> condition;

    protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, columns);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class UNION<T extends Subject<?>> extends Execute<T> implements select.UNION<T> {
    protected final Serializable select;
    protected final boolean all;

    protected UNION(final Keyword<T> parent, final boolean all, final select.SELECT<T> select) {
      super(parent);
      this.select = (Serializable)select;
      this.all = all;
    }

    @Override
    protected Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }
}