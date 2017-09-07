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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.lib4j.util.Collections;
import org.libx4j.rdb.jsql.exception.SQLExceptionCatalog;
import org.libx4j.rdb.jsql.model.kind;
import org.libx4j.rdb.jsql.model.select;
import org.libx4j.rdb.vendor.DBVendor;

public final class Select {
  private static void compile(final List<AbstractMap.SimpleEntry<type.DataType<?>,Integer>> dataTypes, final Compilable subject) {
    if (subject instanceof Entity) {
      final Entity entity = (Entity)subject;
      for (int i = 0; i < entity.column.length; i++)
        dataTypes.add(new AbstractMap.SimpleEntry<type.DataType<?>,Integer>(entity.column[i], i));
    }
    else if (subject instanceof type.DataType) {
      final type.DataType<?> dataType = (type.DataType<?>)subject;
      dataTypes.add(new AbstractMap.SimpleEntry<type.DataType<?>,Integer>(dataType, -1));
    }
    else if (subject instanceof Keyword) {
      final Keyword<?> keyword = (Keyword<?>)subject;
      final SelectCommand command = (SelectCommand)keyword.normalize();
      if (command.select().entities.size() != 1)
        throw new UnsupportedOperationException("Expected 1 entity, but got " + command.select().entities.size());

      final Compilable entity = command.select().entities.iterator().next();
      if (!(entity instanceof type.DataType))
        throw new UnsupportedOperationException("Expected DataType, but got: " + entity.getClass().getName());

      dataTypes.add(new AbstractMap.SimpleEntry<type.DataType<?>,Integer>((type.DataType<?>)entity, -1));
    }
    else {
      throw new UnsupportedOperationException("Unknown entity type: " + subject.getClass().getName());
    }
  }

  private static <T extends Subject<?>>RowIterator<T> execute(final Transaction transaction, final Keyword<T> keyword) throws IOException, SQLException {
    final SelectCommand command = (SelectCommand)keyword.normalize();

    // FIXME: This means that there MUST BE a FROM clause
    final Class<? extends Schema> schema = command.from() != null ? command.from().tables.iterator().next().schema() : null;
    try {
      final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
      final DBVendor vendor = Schema.getDBVendor(connection);

      final Compilation compilation = new Compilation(command, vendor, DBRegistry.isPrepared(schema), DBRegistry.isBatching(schema));
      command.compile(compilation);

      final ResultSet resultSet = compilation.executeQuery(connection);
      return parseResultSet(vendor, connection, resultSet, command.select(), compilation.skipFirstColumn());
    }
    catch (final SQLException e) {
      throw SQLExceptionCatalog.lookup(e);
    }
  }

  private static <B extends Subject<?>>RowIterator<B> parseResultSet(final DBVendor vendor, final Connection connection, final ResultSet resultSet, final SELECT<?> select, final boolean skipFirstColumn) throws SQLException {
    final List<AbstractMap.SimpleEntry<type.DataType<?>,Integer>> dataTypes = new ArrayList<AbstractMap.SimpleEntry<type.DataType<?>,Integer>>();
    for (final Compilable entity : select.entities)
      Select.compile(dataTypes, entity);

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
            final AbstractMap.SimpleEntry<type.DataType<?>,Integer> dataTypePrototype = dataTypes.get(i);
            final type.DataType dataType;
            if (currentTable != null && (currentTable != dataTypePrototype.getKey().owner || dataTypePrototype.getValue() == -1)) {
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

            if (dataTypePrototype.getValue() == -1) {
              entity = null;
              currentTable = null;
              dataType = dataTypePrototype.getKey().clone();
              row[index++] = dataType;
            }
            else {
              currentTable = dataTypePrototype.getKey().owner;
              entity = prototypes.get(currentTable.getClass());
              if (entity == null)
                prototypes.put(currentTable.getClass(), entity = currentTable.newInstance());

              dataType = entity.column[dataTypePrototype.getValue()];
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

  protected static abstract class Execute<T extends Subject<?>> extends Keyword<T> implements select.typed.SELECT<T>, select.typed.UNION<T> {
    protected Execute(final Keyword<T> parent, final Class<?> kind) {
      super(parent, kind);
    }

    @Override
    public T AS(final T as) {
      as.wrapper(new As<T>(this, as, true));
      return as;
    }

    @Override
    public T AS() {
      return type.DataType.as(this);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Entity.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.DataType.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Numeric.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Textual.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.typed.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BIGINT.UNION<T> UNION(final select.BIGINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.ARRAY.UNION<T> UNION(final select.ARRAY.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BOOLEAN.UNION<T> UNION(final select.BOOLEAN.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.ENUM.UNION<T> UNION(final select.ENUM.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.CHAR.UNION<T> UNION(final select.CHAR.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DECIMAL.UNION<T> UNION(final select.DECIMAL.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.FLOAT.UNION<T> UNION(final select.FLOAT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.SMALLINT.UNION<T> UNION(final select.SMALLINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DOUBLE.UNION<T> UNION(final select.DOUBLE.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.TIME.UNION<T> UNION(final select.TIME.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DATE.UNION<T> UNION(final select.DATE.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BINARY.UNION<T> UNION(final select.BINARY.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.CLOB.UNION<T> UNION(final select.CLOB.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.INT.UNION<T> UNION(final select.INT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DATETIME.UNION<T> UNION(final select.DATETIME.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.TINYINT.UNION<T> UNION(final select.TINYINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BLOB.UNION<T> UNION(final select.BLOB.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.untyped.UNION<T> UNION(final select.untyped.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed._UNION.ALL<T> UNION() {
      return new select.typed._UNION.ALL<T>(){
        @Override
        public UNION<T> ALL(final select.Entity.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.DataType.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.Numeric.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.Textual.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.CHAR.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.TINYINT.UNION<T> ALL(final select.TINYINT.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.DATETIME.UNION<T> ALL(final select.DATETIME.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.BINARY.UNION<T> ALL(final select.BINARY.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.BLOB.UNION<T> ALL(final select.BLOB.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.INT.UNION<T> ALL(final select.INT.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.CLOB.UNION<T> ALL(final select.CLOB.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.DECIMAL.UNION<T> ALL(final select.DECIMAL.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.TIME.UNION<T> ALL(final select.TIME.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.ENUM.UNION<T> ALL(final select.ENUM.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.ARRAY.UNION<T> ALL(final select.ARRAY.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.FLOAT.UNION<T> ALL(final select.FLOAT.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.BOOLEAN.UNION<T> ALL(final select.BOOLEAN.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.DOUBLE.UNION<T> ALL(final select.DOUBLE.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.BIGINT.UNION<T> ALL(final select.BIGINT.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.DATE.UNION<T> ALL(final select.DATE.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public select.SMALLINT.UNION<T> ALL(final select.SMALLINT.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.untyped.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.typed.SELECT<T> union) {
          return new UNION<T>(Execute.this, kind(), true, union);
        }
      };
    }

    @Override
    public final RowIterator<T> execute() throws IOException, SQLException {
      return execute(null);
    }

    @Override
    public RowIterator<T> execute(final Transaction transaction) throws IOException, SQLException {
      return Select.execute(transaction, this);
    }
  }

  protected static abstract class FROM_JOIN_ON<T extends Subject<?>> extends Execute<T> implements select.typed.FROM<T> {
    protected FROM_JOIN_ON(final Keyword<T> parent, final Class<?> kind) {
      super(parent, kind);
    }

    @Override
    public final WHERE<T> WHERE(final Condition<?> condition) {
      return new WHERE<T>(this, kind(), condition);
    }

    @Override
    public HAVING<T> HAVING(final Condition<?> condition) {
      return new HAVING<T>(this, kind(), condition);
    }

    @Override
    public final select.typed.ADV_JOIN<T> CROSS_JOIN(final Entity table) {
      return new JOIN<T>(FROM_JOIN_ON.this, kind(), table, true, false, false, false);
    }

    @Override
    public final select.typed.ADV_JOIN<T> NATURAL_JOIN(final Entity table) {
      return new JOIN<T>(FROM_JOIN_ON.this, kind(), table, false, true, false, false);
    }

    @Override
    public final select.typed.JOIN<T> LEFT_JOIN(final Entity table) {
      return new JOIN<T>(FROM_JOIN_ON.this, kind(), table, false, false, true, false);
    }

    @Override
    public final select.typed.JOIN<T> RIGHT_JOIN(final Entity table) {
      return new JOIN<T>(FROM_JOIN_ON.this, kind(), table, false, false, false, true);
    }

    @Override
    public final select.typed.JOIN<T> FULL_JOIN(final Entity table) {
      return new JOIN<T>(FROM_JOIN_ON.this, kind(), table, false, false, true, true);
    }

    @Override
    public final JOIN<T> JOIN(final Entity table) {
      return new JOIN<T>(this, kind(), table, false, false, false, false);
    }

    @Override
    public final LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }
  }

  public static final class FROM<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.typed.FROM<T> {
    protected final Collection<Entity> tables;

    protected FROM(final Keyword<T> parent, final Class<?> kind, final Collection<Entity> tables) {
      super(parent, kind);
      this.tables = tables;
    }

    protected FROM(final Keyword<T> parent, final Class<?> kind, final Entity ... tables) {
      this(parent, kind, Collections.asCollection(new ArrayList<Entity>(), tables));
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... columns) {
      return new GROUP_BY<T>(this, kind(), columns);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class GROUP_BY<T extends Subject<?>> extends Execute<T> implements select.typed.GROUP_BY<T> {
    protected final Collection<Subject<?>> subjects;

    protected GROUP_BY(final Keyword<T> parent, final Class<?> kind, final Collection<Subject<?>> subjects) {
      super(parent, kind);
      this.subjects = subjects;
    }

    protected GROUP_BY(final Keyword<T> parent, final Class<?> kind, final Subject<?> ... subjects) {
      this(parent, kind, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
    }

    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    public HAVING<T> HAVING(final Condition<?> condition) {
      return new HAVING<T>(this, kind(), condition);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class HAVING<T extends Subject<?>> extends Execute<T> implements select.typed.HAVING<T> {
    protected final Condition<?> condition;

    protected HAVING(final Keyword<T> parent, final Class<?> kind, final Condition<?> condition) {
      super(parent, kind);
      this.condition = condition;
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class JOIN<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.typed.JOIN<T>, select.typed.ADV_JOIN<T> {
    protected final boolean cross;
    protected final boolean natural;
    protected final boolean left;
    protected final boolean right;
    protected final Entity table;

    protected JOIN(final Keyword<T> parent, final Class<?> kind, final Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
      super(parent, kind);
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
      return new ON<T>(this, kind(), condition);
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, kind(), subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class ON<T extends Subject<?>> extends FROM_JOIN_ON<T> implements select.typed.ON<T> {
    protected final Condition<?> condition;

    protected ON(final Keyword<T> parent, final Class<?> kind, final Condition<?> condition) {
      super(parent, kind);
      this.condition = condition;
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, kind(), subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class ORDER_BY<T extends Subject<?>> extends Execute<T> implements select.typed.ORDER_BY<T> {
    protected final type.DataType<?>[] columns;
    protected final int[] columnNumbers;

    protected ORDER_BY(final Keyword<T> parent, final Class<?> kind, final type.DataType<?> ... columns) {
      super(parent, kind);
      this.columns = columns;
      this.columnNumbers = null;
    }

    protected ORDER_BY(final Keyword<T> parent, final Class<?> kind, final int ... columnNumbers) {
      super(parent, kind);
      this.columns = null;
      this.columnNumbers = columnNumbers;
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class LIMIT<T extends Subject<?>> extends Execute<T> implements select.typed.LIMIT<T> {
    protected final int rows;

    protected LIMIT(final Keyword<T> parent, final Class<?> kind, final int rows) {
      super(parent, kind);
      this.rows = rows;
    }

    @Override
    public OFFSET<T> OFFSET(final int rows) {
      return new OFFSET<T>(this, kind(), rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  public static final class OFFSET<T extends Subject<?>> extends Execute<T> implements select.typed.OFFSET<T> {
    protected final int rows;

    protected OFFSET(final Keyword<T> parent, final Class<?> kind, final int rows) {
      super(parent, kind);
      this.rows = rows;
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final Predicate<Compilable> entitiesWithOwnerPredicate = new Predicate<Compilable>() {
    @Override
    public boolean test(final Compilable t) {
      return (t instanceof type.DataType) && ((type.DataType<?>)t).owner == null;
    }
  };

  protected static class SELECT<T extends Subject<?>> extends Keyword<T> implements select.typed._SELECT<T> {
    protected final Class<?> kind;
    protected final boolean distinct;
    protected final Collection<Compilable> entities;

    public SELECT(final boolean distinct, final Class<?> kind, final Collection<Compilable> entities) {
      super(null, kind);
      this.kind = kind;
      if (entities.size() < 1)
        throw new IllegalArgumentException("entities.size() < 1");

      this.entities = entities;
      this.distinct = distinct;
    }

    @SafeVarargs
    public SELECT(final boolean distinct, final Class<?> kind, final T entity, kind.DataType<?> ... entities) {
      this(distinct, kind, Collections.asCollection(new ArrayList<Compilable>(), entity));
      for (final kind.DataType<?> e : entities)
        this.entities.add((Compilable)e);
    }

    public SELECT(final boolean distinct, final Class<?> kind, final kind.DataType<?>[] entities) {
      this(distinct, kind, Collections.asCollection(new ArrayList(), entities));
    }

    public SELECT(final boolean distinct, final Class<?> kind, final kind.Subject<?>[] entities) {
      this(distinct, kind, Collections.asCollection(new ArrayList(), entities));
    }

    @Override
    public FROM<T> FROM(final Entity ... table) {
      return new FROM<T>(this, kind(), table);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }

    @Override
    public T AS(final T as) {
      as.wrapper(new As<T>(this, as, true));
      return as;
    }

    @Override
    public T AS() {
      return type.DataType.as(this);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Entity.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.DataType.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Numeric.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.Textual.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed.UNION<T> UNION(final select.typed.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DECIMAL.UNION<T> UNION(final select.DECIMAL.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DATETIME.UNION<T> UNION(final select.DATETIME.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.CHAR.UNION<T> UNION(final select.CHAR.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DATE.UNION<T> UNION(final select.DATE.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.TIME.UNION<T> UNION(final select.TIME.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BOOLEAN.UNION<T> UNION(final select.BOOLEAN.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.ENUM.UNION<T> UNION(final select.ENUM.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BINARY.UNION<T> UNION(final select.BINARY.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.SMALLINT.UNION<T> UNION(final select.SMALLINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.TINYINT.UNION<T> UNION(final select.TINYINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.CLOB.UNION<T> UNION(final select.CLOB.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.ARRAY.UNION<T> UNION(final select.ARRAY.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BIGINT.UNION<T> UNION(final select.BIGINT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.INT.UNION<T> UNION(final select.INT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.BLOB.UNION<T> UNION(final select.BLOB.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.FLOAT.UNION<T> UNION(final select.FLOAT.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.DOUBLE.UNION<T> UNION(final select.DOUBLE.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.untyped.UNION<T> UNION(final select.untyped.SELECT<T> union) {
      return new UNION<T>(this, kind(), false, union);
    }

    @Override
    public select.typed._UNION.ALL<T> UNION() {
      return new select.typed._UNION.ALL<T>(){
        @Override
        public UNION<T> ALL(final select.Entity.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.DataType.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.Textual.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.Numeric.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.CHAR.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.TINYINT.UNION<T> ALL(final select.TINYINT.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.DATETIME.UNION<T> ALL(final select.DATETIME.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.BINARY.UNION<T> ALL(final select.BINARY.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.BLOB.UNION<T> ALL(final select.BLOB.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.INT.UNION<T> ALL(final select.INT.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.CLOB.UNION<T> ALL(final select.CLOB.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.DECIMAL.UNION<T> ALL(final select.DECIMAL.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.TIME.UNION<T> ALL(final select.TIME.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.ENUM.UNION<T> ALL(final select.ENUM.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.ARRAY.UNION<T> ALL(final select.ARRAY.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.FLOAT.UNION<T> ALL(final select.FLOAT.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.BOOLEAN.UNION<T> ALL(final select.BOOLEAN.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.DOUBLE.UNION<T> ALL(final select.DOUBLE.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.BIGINT.UNION<T> ALL(final select.BIGINT.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.DATE.UNION<T> ALL(final select.DATE.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public select.SMALLINT.UNION<T> ALL(final select.SMALLINT.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.untyped.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }

        @Override
        public UNION<T> ALL(final select.typed.SELECT<T> union) {
          return new UNION<T>(SELECT.this, kind(), true, union);
        }
      };
    }

    @Override
    protected final Command normalize() {
      return new SelectCommand(this);
    }

    protected Collection<Compilable> getEntitiesWithOwners() {
      final Collection<Compilable> clone = Collections.clone(entities);
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
        final Compilable subject = entities.iterator().next();
        if (subject instanceof Entity) {
          final Entity entity = (Entity)subject;
          final Entity out = entity.newInstance();
          String sql = "SELECT ";
          String select = "";
          String where = "";
          for (final type.DataType<?> dataType : entity.column) {
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
            for (final type.DataType<?> dataType : entity.column)
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
                    for (final type.DataType dataType : out.column)
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

      return Select.execute(transaction, this);
    }
  }

  public static final class WHERE<T extends Subject<?>> extends Execute<T> implements select.typed.WHERE<T> {
    protected final Condition<?> condition;

    protected WHERE(final Keyword<T> parent, final Class<?> kind, final Condition<?> condition) {
      super(parent, kind);
      this.condition = condition;
    }

    @Override
    public GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
      return new GROUP_BY<T>(this, kind(), subjects);
    }

    @Override
    public ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
      return new ORDER_BY<T>(this, kind(), columns);
    }

    @Override
    public LIMIT<T> LIMIT(final int rows) {
      return new LIMIT<T>(this, kind(), rows);
    }

    @Override
    protected final Command normalize() {
      final SelectCommand command = (SelectCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class UNION<T extends Subject<?>> extends Execute<T> implements select.typed.UNION<T> {
    protected final Compilable select;
    protected final boolean all;

    protected UNION(final Keyword<T> parent, final Class<?> kind, final boolean all, final select.untyped.SELECT<T> select) {
      super(parent, kind);
      this.select = (Compilable)select;
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