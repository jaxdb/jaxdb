/* Copyright (c) 2015 lib4j
 *
 * Permission is hereby granted, final free of charge, final to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), final to deal
 * in the Software without restriction, final including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, final and/or sell
 * copies of the Software, final and to permit persons to whom the Software is
 * furnished to do so, final subject to the following conditions:
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
import org.libx4j.rdb.jsql.type;
import org.libx4j.rdb.jsql.exception.SQLExceptionCatalog;
import org.libx4j.rdb.jsql.Select;
import org.libx4j.rdb.vendor.DBVendor;

class SelectImpl {
  protected static final Predicate<Compilable> entitiesWithOwnerPredicate = new Predicate<Compilable>() {
    @Override
    public boolean test(final Compilable t) {
      return (t instanceof type.DataType) && ((type.DataType<?>)t).owner == null;
    }
  };

  private static void compile(final List<AbstractMap.SimpleEntry<type.DataType<?>,Integer>> dataTypes, final Compilable subject) {
    if (subject instanceof type.Entity) {
      final type.Entity entity = (type.Entity)subject;
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

  protected static <T extends Subject<?>>RowIterator<T> execute(final Transaction transaction, final Keyword<T> keyword) throws IOException, SQLException {
    final SelectCommand command = (SelectCommand)keyword.normalize();

    final Class<? extends Schema> schema = command.from() != null ? command.from().tables.iterator().next().schema() : null;
    try {
      final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
      final DBVendor vendor = Schema.getDBVendor(connection);

      final Compilation compilation = new Compilation(command, vendor, Registry.isPrepared(schema), Registry.isBatching(schema));
      command.compile(compilation);

      final ResultSet resultSet = compilation.executeQuery(connection);
      return parseResultSet(vendor, connection, resultSet, command.select(), compilation.skipFirstColumn());
    }
    catch (final SQLException e) {
      throw SQLExceptionCatalog.lookup(e);
    }
  }

  private static <B extends Subject<?>>RowIterator<B> parseResultSet(final DBVendor vendor, final Connection connection, final ResultSet resultSet, final untyped.SELECT<?> select, final boolean skipFirstColumn) throws SQLException {
    final List<AbstractMap.SimpleEntry<type.DataType<?>,Integer>> dataTypes = new ArrayList<AbstractMap.SimpleEntry<type.DataType<?>,Integer>>();
    for (final Compilable entity : select.entities)
      compile(dataTypes, entity);

    final int columnOffset = skipFirstColumn ? 2 : 1;
    final int noColumns = resultSet.getMetaData().getColumnCount() + 1 - columnOffset;
    return new RowIterator<B>() {
      private final Map<Class<? extends type.Entity>,type.Entity> prototypes = new HashMap<Class<? extends type.Entity>,type.Entity>();
      private final Map<type.Entity,type.Entity> cache = new HashMap<type.Entity,type.Entity>();
      private type.Entity currentTable = null;

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
        type.Entity entity;
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
              final type.Entity cached = cache.get(entity);
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
          final type.Entity cached = cache.get(entity);
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

  public static class untyped {
    protected static abstract class Execute<T extends Subject<?>> extends Keyword<T> implements Select.untyped.SELECT<T>, Select.untyped.UNION<T> {
      protected Execute(final Keyword<T> parent) {
        super(parent);
      }

      @Override
      public final T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public final RowIterator<T> execute() throws IOException, SQLException {
        return execute(null);
      }

      @Override
      public final RowIterator<T> execute(final Transaction transaction) throws IOException, SQLException {
        return SelectImpl.execute(transaction, this);
      }
    }

    public static abstract class FROM<T extends Subject<?>> extends Execute<T> implements Select.untyped.FROM<T> {
      protected final Collection<type.Entity> tables;

      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent);
        this.tables = tables;
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
      }

      @Override
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class GROUP_BY<T extends Subject<?>> extends Execute<T> implements Select.untyped.GROUP_BY<T> {
      protected final Collection<Subject<?>> subjects;

      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent);
        this.subjects = subjects;
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class HAVING<T extends Subject<?>> extends Execute<T> implements Select.untyped.HAVING<T> {
      protected final Condition<?> condition;

      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent);
        this.condition = condition;
      }

      @Override
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class JOIN<T extends Subject<?>> extends Execute<T> implements Select.untyped.JOIN<T>, Select.untyped.ADV_JOIN<T>, Select.untyped.FROM<T> {
      protected final boolean cross;
      protected final boolean natural;
      protected final boolean left;
      protected final boolean right;
      protected final type.Entity table;

      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
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
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class ON<T extends Subject<?>> extends Execute<T> implements Select.untyped.ON<T>, Select.untyped.FROM<T> {
      protected final Condition<?> condition;

      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent);
        this.condition = condition;
      }

      @Override
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class ORDER_BY<T extends Subject<?>> extends Execute<T> implements Select.untyped.ORDER_BY<T> {
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
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    public static abstract class LIMIT<T extends Subject<?>> extends Execute<T> implements Select.untyped.LIMIT<T> {
      protected final int rows;

      protected LIMIT(final Keyword<T> parent, final int rows) {
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

    public static abstract class OFFSET<T extends Subject<?>> extends Execute<T> implements Select.untyped.OFFSET<T> {
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

    protected static abstract class SELECT<T extends Subject<?>> extends Keyword<T> implements Select.untyped._SELECT<T> {
      protected final boolean distinct;
      protected final Collection<Compilable> entities;

      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(null);
        if (entities.size() < 1)
          throw new IllegalArgumentException("entities.size() < 1");

        this.entities = entities;
        this.distinct = distinct;
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
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
      public final RowIterator<T> execute() throws IOException, SQLException {
        return execute(null);
      }

      @Override
      public final RowIterator<T> execute(final Transaction transaction) throws IOException, SQLException {
        if (entities.size() == 1) {
          final Compilable subject = entities.iterator().next();
          if (subject instanceof type.Entity) {
            final type.Entity entity = (type.Entity)subject;
            final type.Entity out = entity.newInstance();
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

                    rows.add((T[])new type.Entity[] {out});
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

        return SelectImpl.execute(transaction, this);
      }
    }

    public static abstract class WHERE<T extends Subject<?>> extends Execute<T> implements Select.untyped.WHERE<T> {
      protected final Condition<?> condition;

      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent);
        this.condition = condition;
      }

      @Override
      protected final Command normalize() {
        final SelectCommand command = (SelectCommand)parent().normalize();
        command.add(this);
        return command;
      }
    }

    protected static abstract class UNION<T extends Subject<?>> extends Execute<T> implements Select.untyped.UNION<T> {
      protected final Compilable select;
      protected final boolean all;

      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent);
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

  public static class ARRAY {
    protected static interface Execute<T extends Subject<?>> extends Select.ARRAY.SELECT<T>, Select.ARRAY.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.ARRAY.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.ARRAY.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.ARRAY.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.ARRAY.JOIN<T>, Select.ARRAY.ADV_JOIN<T>, Select.ARRAY.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.ARRAY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.ARRAY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ARRAY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.ARRAY.ON<T>, Select.ARRAY.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ARRAY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ARRAY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ARRAY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ARRAY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.ARRAY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.ARRAY.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.ARRAY.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.ARRAY.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.ARRAY._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.ARRAY.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.ARRAY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ARRAY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ARRAY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.ARRAY.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.ARRAY.UNION<T> UNION(final Select.ARRAY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ARRAY._UNION.ALL<T> UNION() {
        return new Select.ARRAY._UNION.ALL<T>() {
          @Override
          public Select.ARRAY.UNION<T> ALL(final Select.ARRAY.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class BIGINT {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.BIGINT.UNSIGNED.SELECT<T>, Select.BIGINT.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.BIGINT.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.BIGINT.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.BIGINT.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.BIGINT.UNSIGNED.JOIN<T>, Select.BIGINT.UNSIGNED.ADV_JOIN<T>, Select.BIGINT.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.BIGINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.BIGINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.BIGINT.UNSIGNED.ON<T>, Select.BIGINT.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.BIGINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.BIGINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.BIGINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.BIGINT.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.BIGINT.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.BIGINT.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.BIGINT.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.BIGINT.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.BIGINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.BIGINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.BIGINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.BIGINT.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.BIGINT.UNSIGNED.UNION<T> UNION(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.BIGINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.BIGINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.BIGINT.UNSIGNED.UNION<T> ALL(final Select.BIGINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.BIGINT.SELECT<T>, Select.BIGINT.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.BIGINT.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.BIGINT.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.BIGINT.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.BIGINT.JOIN<T>, Select.BIGINT.ADV_JOIN<T>, Select.BIGINT.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.BIGINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BIGINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BIGINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.BIGINT.ON<T>, Select.BIGINT.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BIGINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BIGINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BIGINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BIGINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BIGINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.BIGINT.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.BIGINT.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.BIGINT.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.BIGINT._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.BIGINT.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.BIGINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BIGINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BIGINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.BIGINT.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.BIGINT.UNION<T> UNION(final Select.BIGINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BIGINT._UNION.ALL<T> UNION() {
        return new Select.BIGINT._UNION.ALL<T>() {
          @Override
          public Select.BIGINT.UNION<T> ALL(final Select.BIGINT.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class BINARY {
    protected static interface Execute<T extends Subject<?>> extends Select.BINARY.SELECT<T>, Select.BINARY.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.BINARY.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BINARY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BINARY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BINARY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BINARY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.BINARY.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.BINARY.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.BINARY.JOIN<T>, Select.BINARY.ADV_JOIN<T>, Select.BINARY.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BINARY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BINARY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BINARY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BINARY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.BINARY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BINARY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BINARY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.BINARY.ON<T>, Select.BINARY.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BINARY.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BINARY.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BINARY.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BINARY.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BINARY.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BINARY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BINARY.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.BINARY.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.BINARY.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.BINARY.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.BINARY._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.BINARY.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.BINARY.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BINARY.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BINARY.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.BINARY.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.BINARY.UNION<T> UNION(final Select.BINARY.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BINARY._UNION.ALL<T> UNION() {
        return new Select.BINARY._UNION.ALL<T>() {
          @Override
          public Select.BINARY.UNION<T> ALL(final Select.BINARY.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class BLOB {
    protected static interface Execute<T extends Subject<?>> extends Select.BLOB.SELECT<T>, Select.BLOB.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.BLOB.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.BLOB.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.BLOB.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.BLOB.JOIN<T>, Select.BLOB.ADV_JOIN<T>, Select.BLOB.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.BLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.BLOB.ON<T>, Select.BLOB.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.BLOB.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.BLOB.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.BLOB.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.BLOB._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.BLOB.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.BLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.BLOB.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.BLOB.UNION<T> UNION(final Select.BLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BLOB._UNION.ALL<T> UNION() {
        return new Select.BLOB._UNION.ALL<T>() {
          @Override
          public Select.BLOB.UNION<T> ALL(final Select.BLOB.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class BOOLEAN {
    protected static interface Execute<T extends Subject<?>> extends Select.BOOLEAN.SELECT<T>, Select.BOOLEAN.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.BOOLEAN.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.BOOLEAN.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.BOOLEAN.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.BOOLEAN.JOIN<T>, Select.BOOLEAN.ADV_JOIN<T>, Select.BOOLEAN.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.BOOLEAN.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BOOLEAN.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BOOLEAN.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.BOOLEAN.ON<T>, Select.BOOLEAN.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.BOOLEAN.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.BOOLEAN.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BOOLEAN.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.BOOLEAN.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.BOOLEAN.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.BOOLEAN.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.BOOLEAN.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.BOOLEAN._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.BOOLEAN.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.BOOLEAN.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.BOOLEAN.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.BOOLEAN.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.BOOLEAN.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.BOOLEAN.UNION<T> UNION(final Select.BOOLEAN.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.BOOLEAN._UNION.ALL<T> UNION() {
        return new Select.BOOLEAN._UNION.ALL<T>() {
          @Override
          public Select.BOOLEAN.UNION<T> ALL(final Select.BOOLEAN.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class CHAR {
    protected static interface Execute<T extends Subject<?>> extends Select.CHAR.SELECT<T>, Select.CHAR.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.CHAR.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CHAR.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CHAR.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CHAR.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CHAR.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.CHAR.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.CHAR.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.CHAR.JOIN<T>, Select.CHAR.ADV_JOIN<T>, Select.CHAR.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CHAR.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CHAR.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CHAR.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CHAR.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.CHAR.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.CHAR.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CHAR.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.CHAR.ON<T>, Select.CHAR.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CHAR.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CHAR.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CHAR.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CHAR.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CHAR.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CHAR.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.CHAR.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.CHAR.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.CHAR.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.CHAR.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.CHAR._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.CHAR.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.CHAR.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CHAR.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CHAR.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.CHAR.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.CHAR.UNION<T> UNION(final Select.CHAR.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CHAR._UNION.ALL<T> UNION() {
        return new Select.CHAR._UNION.ALL<T>() {
          @Override
          public Select.CHAR.UNION<T> ALL(final Select.CHAR.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class CLOB {
    protected static interface Execute<T extends Subject<?>> extends Select.CLOB.SELECT<T>, Select.CLOB.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.CLOB.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.CLOB.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.CLOB.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.CLOB.JOIN<T>, Select.CLOB.ADV_JOIN<T>, Select.CLOB.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.CLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.CLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.CLOB.ON<T>, Select.CLOB.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.CLOB.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.CLOB.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.CLOB.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.CLOB.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.CLOB.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.CLOB.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.CLOB.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.CLOB.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.CLOB.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.CLOB._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.CLOB.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.CLOB.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.CLOB.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.CLOB.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.CLOB.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.CLOB.UNION<T> UNION(final Select.CLOB.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.CLOB._UNION.ALL<T> UNION() {
        return new Select.CLOB._UNION.ALL<T>() {
          @Override
          public Select.CLOB.UNION<T> ALL(final Select.CLOB.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class DataType {
    protected static interface Execute<T extends Subject<?>> extends Select.DataType.SELECT<T>, Select.DataType.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DataType.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DataType.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DataType.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DataType.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DataType.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DataType.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DataType.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DataType.JOIN<T>, Select.DataType.ADV_JOIN<T>, Select.DataType.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DataType.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DataType.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DataType.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DataType.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.DataType.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DataType.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DataType.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DataType.ON<T>, Select.DataType.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DataType.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DataType.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DataType.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DataType.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DataType.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DataType.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DataType.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DataType.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DataType.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DataType.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DataType._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DataType.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.DataType.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DataType.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DataType.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DataType.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.DataType.UNION<T> UNION(final Select.DataType.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DataType._UNION.ALL<T> UNION() {
        return new Select.DataType._UNION.ALL<T>() {
          @Override
          public Select.DataType.UNION<T> ALL(final Select.DataType.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class DATE {
    protected static interface Execute<T extends Subject<?>> extends Select.DATE.SELECT<T>, Select.DATE.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DATE.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DATE.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DATE.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DATE.JOIN<T>, Select.DATE.ADV_JOIN<T>, Select.DATE.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.DATE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DATE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DATE.ON<T>, Select.DATE.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DATE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DATE.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DATE.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DATE.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DATE._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DATE.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.DATE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DATE.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.DATE.UNION<T> UNION(final Select.DATE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATE._UNION.ALL<T> UNION() {
        return new Select.DATE._UNION.ALL<T>() {
          @Override
          public Select.DATE.UNION<T> ALL(final Select.DATE.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class DATETIME {
    protected static interface Execute<T extends Subject<?>> extends Select.DATETIME.SELECT<T>, Select.DATETIME.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DATETIME.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DATETIME.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DATETIME.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DATETIME.JOIN<T>, Select.DATETIME.ADV_JOIN<T>, Select.DATETIME.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.DATETIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DATETIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATETIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DATETIME.ON<T>, Select.DATETIME.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DATETIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DATETIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DATETIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATETIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DATETIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DATETIME.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DATETIME.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DATETIME.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DATETIME._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DATETIME.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.DATETIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DATETIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DATETIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DATETIME.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.DATETIME.UNION<T> UNION(final Select.DATETIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DATETIME._UNION.ALL<T> UNION() {
        return new Select.DATETIME._UNION.ALL<T>() {
          @Override
          public Select.DATETIME.UNION<T> ALL(final Select.DATETIME.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class DECIMAL {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.DECIMAL.UNSIGNED.SELECT<T>, Select.DECIMAL.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.JOIN<T>, Select.DECIMAL.UNSIGNED.ADV_JOIN<T>, Select.DECIMAL.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.ON<T>, Select.DECIMAL.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DECIMAL.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.DECIMAL.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DECIMAL.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.DECIMAL.UNSIGNED.UNION<T> UNION(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DECIMAL.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DECIMAL.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DECIMAL.UNSIGNED.UNION<T> ALL(final Select.DECIMAL.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.DECIMAL.SELECT<T>, Select.DECIMAL.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DECIMAL.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DECIMAL.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DECIMAL.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DECIMAL.JOIN<T>, Select.DECIMAL.ADV_JOIN<T>, Select.DECIMAL.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.DECIMAL.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DECIMAL.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DECIMAL.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DECIMAL.ON<T>, Select.DECIMAL.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DECIMAL.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DECIMAL.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DECIMAL.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DECIMAL.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DECIMAL.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DECIMAL.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DECIMAL.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DECIMAL.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DECIMAL._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DECIMAL.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.DECIMAL.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DECIMAL.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DECIMAL.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DECIMAL.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.DECIMAL.UNION<T> UNION(final Select.DECIMAL.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DECIMAL._UNION.ALL<T> UNION() {
        return new Select.DECIMAL._UNION.ALL<T>() {
          @Override
          public Select.DECIMAL.UNION<T> ALL(final Select.DECIMAL.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class DOUBLE {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.DOUBLE.UNSIGNED.SELECT<T>, Select.DOUBLE.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.JOIN<T>, Select.DOUBLE.UNSIGNED.ADV_JOIN<T>, Select.DOUBLE.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.ON<T>, Select.DOUBLE.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DOUBLE.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.DOUBLE.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DOUBLE.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.DOUBLE.UNSIGNED.UNION<T> UNION(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.DOUBLE.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.DOUBLE.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.DOUBLE.UNSIGNED.UNION<T> ALL(final Select.DOUBLE.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.DOUBLE.SELECT<T>, Select.DOUBLE.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.DOUBLE.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.DOUBLE.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.DOUBLE.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.DOUBLE.JOIN<T>, Select.DOUBLE.ADV_JOIN<T>, Select.DOUBLE.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.DOUBLE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DOUBLE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DOUBLE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.DOUBLE.ON<T>, Select.DOUBLE.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.DOUBLE.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.DOUBLE.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.DOUBLE.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DOUBLE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.DOUBLE.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.DOUBLE.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.DOUBLE.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.DOUBLE.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.DOUBLE._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.DOUBLE.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.DOUBLE.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.DOUBLE.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.DOUBLE.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.DOUBLE.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.DOUBLE.UNION<T> UNION(final Select.DOUBLE.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.DOUBLE._UNION.ALL<T> UNION() {
        return new Select.DOUBLE._UNION.ALL<T>() {
          @Override
          public Select.DOUBLE.UNION<T> ALL(final Select.DOUBLE.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class Entity {
    protected static interface Execute<T extends Subject<?>> extends Select.Entity.SELECT<T>, Select.Entity.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.Entity.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Entity.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Entity.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Entity.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Entity.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.Entity.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.Entity.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.Entity.JOIN<T>, Select.Entity.ADV_JOIN<T>, Select.Entity.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Entity.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Entity.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Entity.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Entity.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.Entity.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Entity.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Entity.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.Entity.ON<T>, Select.Entity.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Entity.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Entity.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Entity.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Entity.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Entity.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Entity.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Entity.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.Entity.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.Entity.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.Entity.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.Entity._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.Entity.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.Entity.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Entity.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Entity.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.Entity.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.Entity.UNION<T> UNION(final Select.Entity.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Entity._UNION.ALL<T> UNION() {
        return new Select.Entity._UNION.ALL<T>() {
          @Override
          public Select.Entity.UNION<T> ALL(final Select.Entity.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class ENUM {
    protected static interface Execute<T extends Subject<?>> extends Select.ENUM.SELECT<T>, Select.ENUM.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.ENUM.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ENUM.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ENUM.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ENUM.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ENUM.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.ENUM.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.ENUM.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.ENUM.JOIN<T>, Select.ENUM.ADV_JOIN<T>, Select.ENUM.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ENUM.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ENUM.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ENUM.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ENUM.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.ENUM.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.ENUM.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ENUM.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.ENUM.ON<T>, Select.ENUM.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.ENUM.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.ENUM.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.ENUM.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.ENUM.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.ENUM.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ENUM.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.ENUM.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.ENUM.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.ENUM.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.ENUM.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.ENUM._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.ENUM.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.ENUM.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.ENUM.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.ENUM.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.ENUM.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.ENUM.UNION<T> UNION(final Select.ENUM.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.ENUM._UNION.ALL<T> UNION() {
        return new Select.ENUM._UNION.ALL<T>() {
          @Override
          public Select.ENUM.UNION<T> ALL(final Select.ENUM.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class FLOAT {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.FLOAT.UNSIGNED.SELECT<T>, Select.FLOAT.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.FLOAT.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.FLOAT.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.FLOAT.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.FLOAT.UNSIGNED.JOIN<T>, Select.FLOAT.UNSIGNED.ADV_JOIN<T>, Select.FLOAT.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.FLOAT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.FLOAT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.FLOAT.UNSIGNED.ON<T>, Select.FLOAT.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.FLOAT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.FLOAT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.FLOAT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.FLOAT.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.FLOAT.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.FLOAT.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.FLOAT.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.FLOAT.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.FLOAT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.FLOAT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.FLOAT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.FLOAT.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.FLOAT.UNSIGNED.UNION<T> UNION(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.FLOAT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.FLOAT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.FLOAT.UNSIGNED.UNION<T> ALL(final Select.FLOAT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.FLOAT.SELECT<T>, Select.FLOAT.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.FLOAT.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.FLOAT.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.FLOAT.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.FLOAT.JOIN<T>, Select.FLOAT.ADV_JOIN<T>, Select.FLOAT.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.FLOAT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.FLOAT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.FLOAT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.FLOAT.ON<T>, Select.FLOAT.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.FLOAT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.FLOAT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.FLOAT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.FLOAT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.FLOAT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.FLOAT.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.FLOAT.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.FLOAT.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.FLOAT._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.FLOAT.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.FLOAT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.FLOAT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.FLOAT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.FLOAT.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.FLOAT.UNION<T> UNION(final Select.FLOAT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.FLOAT._UNION.ALL<T> UNION() {
        return new Select.FLOAT._UNION.ALL<T>() {
          @Override
          public Select.FLOAT.UNION<T> ALL(final Select.FLOAT.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class INT {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.INT.UNSIGNED.SELECT<T>, Select.INT.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.INT.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.INT.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.INT.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.INT.UNSIGNED.JOIN<T>, Select.INT.UNSIGNED.ADV_JOIN<T>, Select.INT.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.INT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.INT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.INT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.INT.UNSIGNED.ON<T>, Select.INT.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.INT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.INT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.INT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.INT.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.INT.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.INT.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.INT.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.INT.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.INT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.INT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.INT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.INT.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.INT.UNSIGNED.UNION<T> UNION(final Select.INT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.INT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.INT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.INT.UNSIGNED.UNION<T> ALL(final Select.INT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.INT.SELECT<T>, Select.INT.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.INT.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.INT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.INT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.INT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.INT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.INT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.INT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.INT.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.INT.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.INT.JOIN<T>, Select.INT.ADV_JOIN<T>, Select.INT.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.INT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.INT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.INT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.INT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.INT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.INT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.INT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.INT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.INT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.INT.ON<T>, Select.INT.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.INT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.INT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.INT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.INT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.INT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.INT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.INT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.INT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.INT.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.INT.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.INT.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.INT._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.INT.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.INT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.INT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.INT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.INT.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.INT.UNION<T> UNION(final Select.INT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.INT._UNION.ALL<T> UNION() {
        return new Select.INT._UNION.ALL<T>() {
          @Override
          public Select.INT.UNION<T> ALL(final Select.INT.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class LargeObject {
    protected static interface Execute<T extends Subject<?>> extends Select.LargeObject.SELECT<T>, Select.LargeObject.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.LargeObject.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.LargeObject.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.LargeObject.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.LargeObject.JOIN<T>, Select.LargeObject.ADV_JOIN<T>, Select.LargeObject.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.LargeObject.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.LargeObject.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.LargeObject.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.LargeObject.ON<T>, Select.LargeObject.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.LargeObject.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.LargeObject.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.LargeObject.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.LargeObject.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.LargeObject.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.LargeObject.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.LargeObject.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.LargeObject.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.LargeObject._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.LargeObject.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.LargeObject.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.LargeObject.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.LargeObject.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.LargeObject.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.LargeObject.UNION<T> UNION(final Select.LargeObject.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.LargeObject._UNION.ALL<T> UNION() {
        return new Select.LargeObject._UNION.ALL<T>() {
          @Override
          public Select.LargeObject.UNION<T> ALL(final Select.LargeObject.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class Numeric {
    protected static interface Execute<T extends Subject<?>> extends Select.Numeric.SELECT<T>, Select.Numeric.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.Numeric.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Numeric.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Numeric.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Numeric.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Numeric.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.Numeric.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.Numeric.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.Numeric.JOIN<T>, Select.Numeric.ADV_JOIN<T>, Select.Numeric.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Numeric.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Numeric.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Numeric.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Numeric.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.Numeric.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Numeric.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Numeric.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.Numeric.ON<T>, Select.Numeric.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Numeric.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Numeric.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Numeric.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Numeric.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Numeric.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Numeric.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Numeric.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.Numeric.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.Numeric.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.Numeric.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.Numeric._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.Numeric.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.Numeric.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Numeric.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Numeric.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.Numeric.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.Numeric.UNION<T> UNION(final Select.Numeric.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Numeric._UNION.ALL<T> UNION() {
        return new Select.Numeric._UNION.ALL<T>() {
          @Override
          public Select.Numeric.UNION<T> ALL(final Select.Numeric.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class SMALLINT {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.SMALLINT.UNSIGNED.SELECT<T>, Select.SMALLINT.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.JOIN<T>, Select.SMALLINT.UNSIGNED.ADV_JOIN<T>, Select.SMALLINT.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.ON<T>, Select.SMALLINT.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.SMALLINT.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.SMALLINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.SMALLINT.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.SMALLINT.UNSIGNED.UNION<T> UNION(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.SMALLINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.SMALLINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.SMALLINT.UNSIGNED.UNION<T> ALL(final Select.SMALLINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.SMALLINT.SELECT<T>, Select.SMALLINT.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.SMALLINT.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.SMALLINT.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.SMALLINT.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.SMALLINT.JOIN<T>, Select.SMALLINT.ADV_JOIN<T>, Select.SMALLINT.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.SMALLINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.SMALLINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.SMALLINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.SMALLINT.ON<T>, Select.SMALLINT.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.SMALLINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.SMALLINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.SMALLINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.SMALLINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.SMALLINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.SMALLINT.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.SMALLINT.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.SMALLINT.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.SMALLINT._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.SMALLINT.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.SMALLINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.SMALLINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.SMALLINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.SMALLINT.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.SMALLINT.UNION<T> UNION(final Select.SMALLINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.SMALLINT._UNION.ALL<T> UNION() {
        return new Select.SMALLINT._UNION.ALL<T>() {
          @Override
          public Select.SMALLINT.UNION<T> ALL(final Select.SMALLINT.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class Temporal {
    protected static interface Execute<T extends Subject<?>> extends Select.Temporal.SELECT<T>, Select.Temporal.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.Temporal.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Temporal.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Temporal.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Temporal.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Temporal.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.Temporal.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.Temporal.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.Temporal.JOIN<T>, Select.Temporal.ADV_JOIN<T>, Select.Temporal.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Temporal.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Temporal.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Temporal.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Temporal.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.Temporal.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Temporal.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Temporal.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.Temporal.ON<T>, Select.Temporal.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Temporal.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Temporal.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Temporal.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Temporal.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Temporal.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Temporal.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Temporal.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.Temporal.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.Temporal.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.Temporal.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.Temporal._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.Temporal.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.Temporal.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Temporal.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Temporal.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.Temporal.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.Temporal.UNION<T> UNION(final Select.Temporal.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Temporal._UNION.ALL<T> UNION() {
        return new Select.Temporal._UNION.ALL<T>() {
          @Override
          public Select.Temporal.UNION<T> ALL(final Select.Temporal.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class Textual {
    protected static interface Execute<T extends Subject<?>> extends Select.Textual.SELECT<T>, Select.Textual.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.Textual.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Textual.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Textual.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Textual.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Textual.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.Textual.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.Textual.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.Textual.JOIN<T>, Select.Textual.ADV_JOIN<T>, Select.Textual.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Textual.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Textual.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Textual.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Textual.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.Textual.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Textual.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Textual.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.Textual.ON<T>, Select.Textual.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.Textual.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.Textual.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.Textual.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.Textual.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.Textual.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Textual.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.Textual.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.Textual.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.Textual.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.Textual.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.Textual._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.Textual.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.Textual.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.Textual.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.Textual.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.Textual.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.Textual.UNION<T> UNION(final Select.Textual.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.Textual._UNION.ALL<T> UNION() {
        return new Select.Textual._UNION.ALL<T>() {
          @Override
          public Select.Textual.UNION<T> ALL(final Select.Textual.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class TIME {
    protected static interface Execute<T extends Subject<?>> extends Select.TIME.SELECT<T>, Select.TIME.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.TIME.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.TIME.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.TIME.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.TIME.JOIN<T>, Select.TIME.ADV_JOIN<T>, Select.TIME.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.TIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.TIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.TIME.ON<T>, Select.TIME.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TIME.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TIME.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TIME.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TIME.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TIME.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.TIME.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.TIME.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.TIME.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.TIME.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.TIME._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.TIME.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.TIME.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TIME.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TIME.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.TIME.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.TIME.UNION<T> UNION(final Select.TIME.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TIME._UNION.ALL<T> UNION() {
        return new Select.TIME._UNION.ALL<T>() {
          @Override
          public Select.TIME.UNION<T> ALL(final Select.TIME.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }

  public static class TINYINT {
    public static class UNSIGNED {
      protected static interface Execute<T extends Subject<?>> extends Select.TINYINT.UNSIGNED.SELECT<T>, Select.TINYINT.UNSIGNED.UNION<T> {
      }

      public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.TINYINT.UNSIGNED.FROM<T> {
        protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
          super(parent, tables);
        }

        protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
          this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(FROM.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.TINYINT.UNSIGNED.GROUP_BY<T> {
        protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
          super(parent, subjects);
        }

        protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
          this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(GROUP_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.TINYINT.UNSIGNED.HAVING<T> {
        protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(HAVING.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }
      }

      public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.TINYINT.UNSIGNED.JOIN<T>, Select.TINYINT.UNSIGNED.ADV_JOIN<T>, Select.TINYINT.UNSIGNED.FROM<T> {
        protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
          super(parent, table, cross, natural, left, right);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(JOIN.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.ON<T> ON(final Condition<?> condition) {
          return new ON<T>(this, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.TINYINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.TINYINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }
      }

      public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.TINYINT.UNSIGNED.ON<T>, Select.TINYINT.UNSIGNED.FROM<T> {
        protected ON(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, true, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, true, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> LEFT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> RIGHT_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> FULL_JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, true, true);
        }

        @Override
        public Select.TINYINT.UNSIGNED.JOIN<T> JOIN(final type.Entity table) {
          return new JOIN<T>(this, table, false, false, false, false);
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.TINYINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }

        @Override
        public Select.TINYINT.UNSIGNED.WHERE<T> WHERE(final Condition<?> condition) {
          return new WHERE<T>(this, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ON.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.HAVING<T> HAVING(final Condition<?> condition) {
          return new HAVING<T>(this, condition);
        }
      }

      public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.TINYINT.UNSIGNED.ORDER_BY<T> {
        protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
          super(parent, columns);
        }

        protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
          super(parent, columnNumbers);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(ORDER_BY.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }
      }

      public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.TINYINT.UNSIGNED.LIMIT<T> {
        protected LIMIT(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(LIMIT.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.OFFSET<T> OFFSET(final int rows) {
          return new OFFSET<T>(this, rows);
        }
      }

      public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.TINYINT.UNSIGNED.OFFSET<T> {
        protected OFFSET(final Keyword<T> parent, final int rows) {
          super(parent, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(OFFSET.this, true, union);
            }
          };
        }
      }

      protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.TINYINT.UNSIGNED._SELECT<T> {
        public SELECT(final boolean distinct, final Collection<Compilable> entities) {
          super(distinct, entities);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
          this(distinct, Collections.asCollection(new ArrayList(), entities));
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED.FROM<T> FROM(final type.Entity ... tables) {
          return new FROM<T>(this, tables);
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public T AS(final T as) {
          as.wrapper(new As<T>(this, as, true));
          return as;
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(SELECT.this, true, union);
            }
          };
        }
      }

      public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.TINYINT.UNSIGNED.WHERE<T> {
        protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
          super(parent, condition);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(WHERE.this, true, union);
            }
          };
        }

        @Override
        public Select.TINYINT.UNSIGNED.LIMIT<T> LIMIT(final int rows) {
          return new LIMIT<T>(this, rows);
        }

        @Override
        public Select.TINYINT.UNSIGNED.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
          return new ORDER_BY<T>(this, columns);
        }

        @Override
        public Select.TINYINT.UNSIGNED.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
          return new GROUP_BY<T>(this, subjects);
        }
      }

      protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.TINYINT.UNSIGNED.UNION<T> {
        protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
          super(parent, all, select);
        }

        @Override
        public Select.TINYINT.UNSIGNED.UNION<T> UNION(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
          return new UNION<T>(this, false, union);
        }

        @Override
        public Select.TINYINT.UNSIGNED._UNION.ALL<T> UNION() {
          return new Select.TINYINT.UNSIGNED._UNION.ALL<T>() {
            @Override
            public Select.TINYINT.UNSIGNED.UNION<T> ALL(final Select.TINYINT.UNSIGNED.SELECT<T> union) {
              return new UNION<T>(UNION.this, true, union);
            }
          };
        }
      }
    }

    protected static interface Execute<T extends Subject<?>> extends Select.TINYINT.SELECT<T>, Select.TINYINT.UNION<T> {
    }

    public static final class FROM<T extends Subject<?>> extends untyped.FROM<T> implements Execute<T>, Select.TINYINT.FROM<T> {
      protected FROM(final Keyword<T> parent, final Collection<type.Entity> tables) {
        super(parent, tables);
      }

      protected FROM(final Keyword<T> parent, final type.Entity ... tables) {
        this(parent, Collections.asCollection(new ArrayList<type.Entity>(), tables));
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
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(FROM.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class GROUP_BY<T extends Subject<?>> extends untyped.GROUP_BY<T> implements Execute<T>, Select.TINYINT.GROUP_BY<T> {
      protected GROUP_BY(final Keyword<T> parent, final Collection<Subject<?>> subjects) {
        super(parent, subjects);
      }

      protected GROUP_BY(final Keyword<T> parent, final Subject<?> ... subjects) {
        this(parent, Collections.asCollection(new LinkedHashSet<Subject<?>>(), subjects));
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(GROUP_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class HAVING<T extends Subject<?>> extends untyped.HAVING<T> implements Execute<T>, Select.TINYINT.HAVING<T> {
      protected HAVING(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(HAVING.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }
    }

    public static final class JOIN<T extends Subject<?>> extends untyped.JOIN<T> implements Execute<T>, Select.TINYINT.JOIN<T>, Select.TINYINT.ADV_JOIN<T>, Select.TINYINT.FROM<T> {
      protected JOIN(final Keyword<T> parent, final type.Entity table, final boolean cross, final boolean natural, final boolean left, final boolean right) {
        super(parent, table, cross, natural, left, right);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(JOIN.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.ON<T> ON(final Condition<?> condition) {
        return new ON<T>(this, condition);
      }

      @Override
      public Select.TINYINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.TINYINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TINYINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }
    }

    public static final class ON<T extends Subject<?>> extends untyped.ON<T> implements Execute<T>, Select.TINYINT.ON<T>, Select.TINYINT.FROM<T> {
      protected ON(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> CROSS_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, true, false, false, false);
      }

      @Override
      public Select.TINYINT.ADV_JOIN<T> NATURAL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, true, false, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> LEFT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, false);
      }

      @Override
      public Select.TINYINT.JOIN<T> RIGHT_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> FULL_JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, true, true);
      }

      @Override
      public Select.TINYINT.JOIN<T> JOIN(final type.Entity table) {
        return new JOIN<T>(this, table, false, false, false, false);
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TINYINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }

      @Override
      public Select.TINYINT.WHERE<T> WHERE(final Condition<?> condition) {
        return new WHERE<T>(this, condition);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(ON.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.HAVING<T> HAVING(final Condition<?> condition) {
        return new HAVING<T>(this, condition);
      }
    }

    public static final class ORDER_BY<T extends Subject<?>> extends untyped.ORDER_BY<T> implements Execute<T>, Select.TINYINT.ORDER_BY<T> {
      protected ORDER_BY(final Keyword<T> parent, final type.DataType<?> ... columns) {
        super(parent, columns);
      }

      protected ORDER_BY(final Keyword<T> parent, final int ... columnNumbers) {
        super(parent, columnNumbers);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(ORDER_BY.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }
    }

    public static final class LIMIT<T extends Subject<?>> extends untyped.LIMIT<T> implements Execute<T>, Select.TINYINT.LIMIT<T> {
      protected LIMIT(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(LIMIT.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.OFFSET<T> OFFSET(final int rows) {
        return new OFFSET<T>(this, rows);
      }
    }

    public static final class OFFSET<T extends Subject<?>> extends untyped.OFFSET<T> implements Execute<T>, Select.TINYINT.OFFSET<T> {
      protected OFFSET(final Keyword<T> parent, final int rows) {
        super(parent, rows);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(OFFSET.this, true, union);
          }
        };
      }
    }

    protected static class SELECT<T extends Subject<?>> extends untyped.SELECT<T> implements Execute<T>, Select.TINYINT._SELECT<T> {
      public SELECT(final boolean distinct, final Collection<Compilable> entities) {
        super(distinct, entities);
      }

      @SuppressWarnings({"rawtypes", "unchecked"})
      public SELECT(final boolean distinct, final kind.Subject<?>[] entities) {
        this(distinct, Collections.asCollection(new ArrayList(), entities));
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT.FROM<T> FROM(final type.Entity ... tables) {
        return new FROM<T>(this, tables);
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public T AS(final T as) {
        as.wrapper(new As<T>(this, as, true));
        return as;
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(SELECT.this, true, union);
          }
        };
      }
    }

    public static final class WHERE<T extends Subject<?>> extends untyped.WHERE<T> implements Execute<T>, Select.TINYINT.WHERE<T> {
      protected WHERE(final Keyword<T> parent, final Condition<?> condition) {
        super(parent, condition);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(WHERE.this, true, union);
          }
        };
      }

      @Override
      public Select.TINYINT.LIMIT<T> LIMIT(final int rows) {
        return new LIMIT<T>(this, rows);
      }

      @Override
      public Select.TINYINT.ORDER_BY<T> ORDER_BY(final type.DataType<?> ... columns) {
        return new ORDER_BY<T>(this, columns);
      }

      @Override
      public Select.TINYINT.GROUP_BY<T> GROUP_BY(final Subject<?> ... subjects) {
        return new GROUP_BY<T>(this, subjects);
      }
    }

    protected static final class UNION<T extends Subject<?>> extends untyped.UNION<T> implements Execute<T>, Select.TINYINT.UNION<T> {
      protected UNION(final Keyword<T> parent, final boolean all, final Select.untyped.SELECT<T> select) {
        super(parent, all, select);
      }

      @Override
      public Select.TINYINT.UNION<T> UNION(final Select.TINYINT.SELECT<T> union) {
        return new UNION<T>(this, false, union);
      }

      @Override
      public Select.TINYINT._UNION.ALL<T> UNION() {
        return new Select.TINYINT._UNION.ALL<T>() {
          @Override
          public Select.TINYINT.UNION<T> ALL(final Select.TINYINT.SELECT<T> union) {
            return new UNION<T>(UNION.this, true, union);
          }
        };
      }
    }
  }
}