/* Copyright (c) 2014 JAX-DB
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
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

import org.jaxdb.vendor.DBVendor;
import org.libj.lang.Assertions;
import org.libj.lang.Throwables;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.ResultSets;
import org.libj.sql.exception.SQLExceptions;

// FIXME: Order the declarations to be same as in Select.java
final class SelectImpl {
  private static final Predicate<type.Entity<?>> entitiesWithOwnerPredicate = t -> !(t instanceof data.Column) || ((data.Column<?>)t).table() != null;

  private static Object[][] compile(final type.Entity<?>[] entities, final int index, final int depth) {
    if (index == entities.length)
      return new Object[depth][2];

    final type.Entity<?> entity = entities[index];
    if (entity instanceof data.Table) {
      final data.Table table = (data.Table)entity;
      final Object[][] columns = compile(entities, index + 1, depth + table._column$.length);
      for (int i = 0; i < table._column$.length; ++i) {
        final Object[] array = columns[depth + i];
        array[0] = table._column$[i];
        array[1] = i;
      }

      return columns;
    }

    if (entity instanceof type.Column) {
      final type.Column<?> column = (type.Column<?>)entity;
      final Object[][] columns = compile(entities, index + 1, depth + 1);
      final Object[] array = columns[depth];
      array[0] = column;
      array[1] = -1;
      return columns;
    }

    if (entity instanceof Keyword) {
      final untyped.SELECT<?> select = (untyped.SELECT<?>)entity;
      if (select.entities.length != 1)
        throw new IllegalStateException("Expected 1 entity, but got " + select.entities.length);

      final type.Entity<?> selectEntity = select.entities[0];
      if (!(selectEntity instanceof data.Column))
        throw new IllegalStateException("Expected dat.Column, but got: " + selectEntity.getClass().getName());

      final Object[][] columns = compile(entities, index + 1, depth + 1);
      final Object[] array = columns[depth];
      array[0] = selectEntity;
      array[1] = -1;
      return columns;
    }

    throw new IllegalStateException("Unknown entity type: " + entity.getClass().getName());
  }

  public static class untyped {
    abstract static class SELECT<D extends data.Entity<?>> extends Command<D> implements Select.untyped._SELECT<D>, Select.untyped.FROM<D>, Select.untyped.GROUP_BY<D>, Select.untyped.HAVING<D>, Select.untyped.UNION<D>, Select.untyped.JOIN<D>, Select.untyped.ADV_JOIN<D>, Select.untyped.ON<D>, Select.untyped.ORDER_BY<D>, Select.untyped.LIMIT<D>, Select.untyped.OFFSET<D>, Select.untyped.FOR<D>, Select.untyped.NOWAIT<D>, Select.untyped.SKIP_LOCKED<D>, Select.untyped.WHERE<D> {
      enum LockStrength {
        SHARE,
        UPDATE
      }

      enum LockOption {
        NOWAIT("NOWAIT"),
        SKIP_LOCKED("SKIP LOCKED");

        private final String name;

        private LockOption(final String name) {
          this.name = name;
        }

        @Override
        public String toString() {
          return name;
        }
      }

      enum JoinKind {
        INNER(""),
        CROSS(" CROSS"),
        NATURAL(" NATURAL"),
        LEFT(" LEFT OUTER"),
        RIGHT(" RIGHT OUTER"),
        FULL(" FULL OUTER");

        private final String keyword;

        private JoinKind(final String keyword) {
          this.keyword = keyword;
        }

        @Override
        public String toString() {
          return keyword;
        }
      }

      private boolean tableMutex;
      private data.Table table;

      final boolean distinct;
      final type.Entity<?>[] entities;

      private boolean fromMutex;
      private data.Table[] from;

      List<Object> joins;
      List<Condition<?>> on;

      type.Entity<?>[] groupBy;
      Condition<?> having;

      List<Object> unions;

      data.Column<?>[] orderBy;
      int[] orderByIndexes;

      int limit = -1;
      int offset = -1;

      LockStrength forLockStrength;
      data.Entity<?>[] forSubjects;
      LockOption forLockOption;

      private boolean isObjectQuery;
      private boolean whereMutex;
      private Condition<?> where;

      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        if (entities.length < 1)
          throw new IllegalArgumentException("entities.length < 1");

        for (final type.Entity<?> entity : entities)
          Assertions.assertNotNull(entity, "Argument to SELECT cannot be null (use type.?.NULL instead)");

        this.entities = entities;
        this.distinct = distinct;
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        this.from = from;
        fromMutex = true;
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        return JOIN(JoinKind.CROSS, table);
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.CROSS, select);
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        return JOIN(JoinKind.NATURAL, table);
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.NATURAL, select);
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        return JOIN(JoinKind.LEFT, table);
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.LEFT, select);
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        return JOIN(JoinKind.RIGHT, table);
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.RIGHT, select);
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        return JOIN(JoinKind.FULL, table);
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.FULL, select);
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        return JOIN(JoinKind.INNER, table);
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        return JOIN(JoinKind.INNER, select);
      }

      private SELECT<D> JOIN(final JoinKind kind, final Object join) {
        if (this.joins == null)
          this.joins = new ArrayList<>();

        this.joins.add(kind);
        this.joins.add(join);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        if (this.on == null)
          this.on = new ArrayList<>();

        // Since ON is optional, for each JOIN without ON, add a null to this.on
        for (int i = 0, joinsSize = this.joins.size(), onSize = this.on.size(); i < joinsSize / 2 - onSize - 1; ++i)
          this.on.add(null);

        this.on.add(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        this.groupBy = groupBy;
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        this.having = having;
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.untyped.SELECT<D> select) {
        UNION(Boolean.FALSE, select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.untyped.SELECT<D> select) {
        UNION(Boolean.TRUE, select);
        return this;
      }

      private SELECT<D> UNION(final Boolean all, final Select.untyped.SELECT<D> select) {
        if (this.unions == null)
          this.unions = new ArrayList<>();

        this.unions.add(all);
        this.unions.add(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        this.orderBy = columns;
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        this.orderByIndexes = columnNumbers;
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        this.limit = rows;
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        this.offset = rows;
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        return FOR(LockStrength.SHARE, subjects);
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        return FOR(LockStrength.UPDATE, subjects);
      }

      private SELECT<D> FOR(final LockStrength lockStrength, final data.Entity<?> ... subjects) {
        this.forLockStrength = lockStrength;
        this.forSubjects = subjects;
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        this.forLockOption = LockOption.NOWAIT;
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        this.forLockOption = LockOption.SKIP_LOCKED;
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        this.where = where;
        return this;
      }

      type.Entity<?>[] getEntitiesWithOwners() {
        // FIXME: Do this via recursive array builder
        return Arrays.stream(entities).filter(entitiesWithOwnerPredicate).toArray(type.Entity<?>[]::new);
      }

      @SuppressWarnings("unchecked")
      private RowIterator<D> execute(final Transaction transaction, final String dataSourceId, final QueryConfig config) throws IOException, SQLException {
        Connection connection = null;
        Statement statement = null;
        try {
          final boolean isPrepared;
          final Connection finalConnection;
          if (transaction != null) {
            connection = finalConnection = transaction.getConnection();
            isPrepared = transaction.isPrepared();
          }
          else {
            final Connector connector = Database.getConnector(schema(), dataSourceId);
            connection = finalConnection = connector.getConnection();
            connection.setAutoCommit(true);
            isPrepared = connector.isPrepared();
          }

          try (final Compilation compilation = new Compilation(this, DBVendor.valueOf(finalConnection.getMetaData()), isPrepared)) {
            compile(compilation, false);

            final Object[][] protoSubjectIndexes = SelectImpl.compile(entities, 0, 0);

            final int columnOffset = compilation.skipFirstColumn() ? 2 : 1;
            final ResultSet resultSet = compilation.executeQuery(finalConnection, config);
            final Statement finalStatement = statement = resultSet.getStatement();
            final int noColumns = resultSet.getMetaData().getColumnCount() + 1 - columnOffset;
            return new RowIterator<D>(resultSet, config) {
              private final HashMap<Class<? extends data.Table>,data.Table> prototypes = new HashMap<>();
              private final HashMap<data.Table,data.Table> cache = new HashMap<>();
              private data.Table currentTable;

              @Override
              @SuppressWarnings("null")
              public boolean nextRow() throws SQLException {
                if (super.nextRow())
                  return true;

                if (endReached)
                  return false;

                final Subject[] row;
                int index = 0;
                data.Table table;
                try {
                  if (endReached = !resultSet.next()) {
                    suppressed = Throwables.addSuppressed(suppressed, ResultSets.close(resultSet));
                    return false;
                  }

                  row = new data.Entity[entities.length];
                  table = null;
                  for (int i = 0; i < noColumns; ++i) {
                    final Object[] protoSubjectIndex = protoSubjectIndexes[i];
                    final Subject protoSubject = (Subject)protoSubjectIndex[0];
                    final Integer protoIndex = (Integer)protoSubjectIndex[1];
                    final data.Column<?> column;
                    if (currentTable != null && (currentTable != protoSubject.table() || protoIndex == -1)) {
                      final data.Table cached = cache.get(table);
                      if (cached != null) {
                        row[index++] = cached;
                      }
                      else {
                        row[index++] = table;
                        cache.put(table, table);
                        prototypes.put(table.getClass(), table.newInstance());
                      }
                    }

                    if (protoIndex != -1) {
                      currentTable = protoSubject.table();
                      if (currentTable._mutable$) {
                        table = currentTable;
                      }
                      else {
                        table = prototypes.get(currentTable.getClass());
                        if (table == null)
                          prototypes.put(currentTable.getClass(), table = currentTable.newInstance());
                      }

                      column = table._column$[protoIndex];
                    }
                    else {
                      table = null;
                      currentTable = null;
                      if (protoSubject instanceof data.Column) {
                        final data.Column<?> col = (data.Column<?>)protoSubject;
                        column = col._mutable$ ? col : col.clone();
                      }
                      else {
                        column = protoSubject.column().clone();
                      }

                      row[index++] = column;
                    }

                    column.set(resultSet, i + columnOffset);
                  }
                }
                catch (SQLException e) {
                  e = Throwables.addSuppressed(e, suppressed);
                  suppressed = null;
                  throw SQLExceptions.toStrongType(e);
                }

                if (table != null) {
                  final data.Table cached = cache.get(table);
                  row[index++] = cached != null ? cached : table;
                }

                rows.add((D[])row);
                ++rowIndex;
                resetEntities();
                prototypes.clear();
                currentTable = null;
                return true;
              }

              @Override
              public void close() throws SQLException {
                SQLException e = Throwables.addSuppressed(suppressed, ResultSets.close(resultSet));
                e = Throwables.addSuppressed(e, AuditStatement.close(finalStatement));
                if (transaction == null)
                  e = Throwables.addSuppressed(e, AuditConnection.close(finalConnection));

                prototypes.clear();
                cache.clear();
                currentTable = null;
                rows.clear();
                if (e != null)
                  throw SQLExceptions.toStrongType(e);
              }
            };
          }
        }
        catch (SQLException e) {
          if (statement != null)
            e = Throwables.addSuppressed(e, AuditStatement.close(statement));

          if (transaction == null && connection != null)
            e = Throwables.addSuppressed(e, AuditConnection.close(connection));

          throw SQLExceptions.toStrongType(e);
        }
      }

      @Override
      public final RowIterator<D> execute(final String dataSourceId) throws IOException, SQLException {
        return execute(null, dataSourceId, null);
      }

      @Override
      public final RowIterator<D> execute(final Transaction transaction) throws IOException, SQLException {
        return execute(transaction, transaction != null ? transaction.getDataSourceId() : null, null);
      }

      @Override
      public RowIterator<D> execute() throws IOException, SQLException {
        return execute(null, null, null);
      }

      @Override
      public final RowIterator<D> execute(final String dataSourceId, final QueryConfig config) throws IOException, SQLException {
        return execute(null, dataSourceId, config);
      }

      @Override
      public final RowIterator<D> execute(final Transaction transaction, final QueryConfig config) throws IOException, SQLException {
        return execute(transaction, transaction != null ? transaction.getDataSourceId() : null, config);
      }

      @Override
      public RowIterator<D> execute(final QueryConfig config) throws IOException, SQLException {
        return execute(null, null, config);
      }

      @Override
      final data.Table table() {
        if (tableMutex)
          return table;

        tableMutex = true;
        // FIXME: Note that this returns the 1st table only! Is this what we want?!
        if (entities[0] instanceof SelectImpl.untyped.SELECT)
          return table = ((SelectImpl.untyped.SELECT<?>)entities[0]).table();

        return from() != null ? table = from()[0] : null;
      }

      @Override
      data.Column<?> column() {
        if (entities.length == 1)
          return ((Subject)entities[0]).column();

        throw new UnsupportedOperationException();
      }

      // FIXME: What is translateTypes for? Looks unlinked to me!
      Map<Integer,data.ENUM<?>> translateTypes;

      data.Table[] from() {
        if (fromMutex)
          return from;

        fromMutex = true;
        from = getTables(entities, 0, 0);
        if ((isObjectQuery = where == null) || from == null)
          for (final type.Entity<?> entity : entities)
            isObjectQuery &= entity instanceof data.Table;

        return from;
      }

      private data.Table[] getTables(final type.Entity<?>[] entities, final int index, final int depth) {
        if (index == entities.length)
          return depth == 0 ? null : new data.Table[depth];

        final Subject subject = (Subject)entities[index];
        final data.Table table = subject.table();
        final data.Table[] tables = getTables(entities, index + 1, table != null ? depth + 1 : depth);
        if (table != null)
          tables[depth] = table;

        return tables;
      }

      Condition<?> where() {
        if (whereMutex)
          return where;

        whereMutex = true;
        if (isObjectQuery)
          where = createCondition(entities);

        return where;
      }

      private Condition<?> createCondition(final type.Entity<?>[] entities) {
        final Condition<?>[] conditions = createConditions(entities, 0, 0);
        return conditions == null ? null : conditions.length == 1 ? conditions[0] : DML.AND(conditions);
      }

      private Condition<?>[] createConditions(final type.Entity<?>[] entities, final int index, final int depth) {
        if (index == entities.length)
          return depth == 0 ? null : new Condition[depth];

        final type.Entity<?> entity = entities[index];
        final Condition<?> condition;
        if (entity instanceof data.Table)
          condition = createCondition(((data.Table)entity)._column$);
        else if (entity instanceof SelectImpl.untyped.SELECT)
          condition = null;
        else
          throw new UnsupportedOperationException("Unsupported entity of object query: " + entity.getClass().getName());

        final Condition<?>[] conditions = createConditions(entities, index + 1, condition != null ? depth + 1 : depth);
        if (condition != null)
          conditions[depth] = condition;

        return conditions;
      }

      private Condition<?> createCondition(final data.Column<?>[] columns) {
        final Condition<?>[] conditions = createConditions(columns, 0, 0);
        return conditions == null ? null : conditions.length == 1 ? conditions[0] : DML.AND(conditions);
      }

      private Condition<?>[] createConditions(final data.Column<?>[] columns, final int index, final int depth) {
        if (index == columns.length)
          return depth == 0 ? null : new Condition[depth];

        final data.Column<?> column = columns[index];
        final Condition<?>[] cinditions = createConditions(columns, index + 1, column.wasSet() ? depth + 1 : depth);
        if (column.wasSet())
          cinditions[depth] = DML.EQ(column, column.get());

        return cinditions;
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        final Compiler compiler = compilation.compiler;
        final boolean isForUpdate = forLockStrength != null && forSubjects != null && forSubjects.length > 0;
        final boolean useAliases = !isForUpdate || compiler.aliasInForUpdate();

        compiler.assignAliases(from(), joins, compilation);
        compiler.compileSelect(this, useAliases, compilation);
        compiler.compileFrom(this, useAliases, compilation);
        if (joins != null)
          for (int i = 0, j = 0, len = joins.size(); i < len; j = i / 2)
            compiler.compileJoin((JoinKind)joins.get(i++), joins.get(i++), on != null && j < on.size() ? on.get(j) : null, compilation);

        compiler.compileWhere(this, compilation);
        compiler.compileGroupByHaving(this, useAliases, compilation);
        compiler.compileUnion(this, compilation);
        compiler.compileOrderBy(this, compilation);
        compiler.compileLimitOffset(this, compilation);
        if (forLockStrength != null)
          compiler.compileFor(this, compilation);
      }
    }
  }

  public static class ARRAY {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.ARRAY._SELECT<D>, Select.ARRAY.FROM<D>, Select.ARRAY.GROUP_BY<D>, Select.ARRAY.HAVING<D>, Select.ARRAY.UNION<D>, Select.ARRAY.JOIN<D>, Select.ARRAY.ADV_JOIN<D>, Select.ARRAY.ON<D>, Select.ARRAY.ORDER_BY<D>, Select.ARRAY.LIMIT<D>, Select.ARRAY.OFFSET<D>, Select.ARRAY.FOR<D>, Select.ARRAY.NOWAIT<D>, Select.ARRAY.SKIP_LOCKED<D>, Select.ARRAY.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.ARRAY.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.ARRAY.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class BIGINT {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.BIGINT._SELECT<D>, Select.BIGINT.FROM<D>, Select.BIGINT.GROUP_BY<D>, Select.BIGINT.HAVING<D>, Select.BIGINT.UNION<D>, Select.BIGINT.JOIN<D>, Select.BIGINT.ADV_JOIN<D>, Select.BIGINT.ON<D>, Select.BIGINT.ORDER_BY<D>, Select.BIGINT.LIMIT<D>, Select.BIGINT.OFFSET<D>, Select.BIGINT.FOR<D>, Select.BIGINT.NOWAIT<D>, Select.BIGINT.SKIP_LOCKED<D>, Select.BIGINT.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.BIGINT.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.BIGINT.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class BINARY {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.BINARY._SELECT<D>, Select.BINARY.FROM<D>, Select.BINARY.GROUP_BY<D>, Select.BINARY.HAVING<D>, Select.BINARY.UNION<D>, Select.BINARY.JOIN<D>, Select.BINARY.ADV_JOIN<D>, Select.BINARY.ON<D>, Select.BINARY.ORDER_BY<D>, Select.BINARY.LIMIT<D>, Select.BINARY.OFFSET<D>, Select.BINARY.FOR<D>, Select.BINARY.NOWAIT<D>, Select.BINARY.SKIP_LOCKED<D>, Select.BINARY.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.BINARY.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.BINARY.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class BLOB {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.BLOB._SELECT<D>, Select.BLOB.FROM<D>, Select.BLOB.GROUP_BY<D>, Select.BLOB.HAVING<D>, Select.BLOB.UNION<D>, Select.BLOB.JOIN<D>, Select.BLOB.ADV_JOIN<D>, Select.BLOB.ON<D>, Select.BLOB.ORDER_BY<D>, Select.BLOB.LIMIT<D>, Select.BLOB.OFFSET<D>, Select.BLOB.FOR<D>, Select.BLOB.NOWAIT<D>, Select.BLOB.SKIP_LOCKED<D>, Select.BLOB.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.BLOB.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.BLOB.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class BOOLEAN {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.BOOLEAN._SELECT<D>, Select.BOOLEAN.FROM<D>, Select.BOOLEAN.GROUP_BY<D>, Select.BOOLEAN.HAVING<D>, Select.BOOLEAN.UNION<D>, Select.BOOLEAN.JOIN<D>, Select.BOOLEAN.ADV_JOIN<D>, Select.BOOLEAN.ON<D>, Select.BOOLEAN.ORDER_BY<D>, Select.BOOLEAN.LIMIT<D>, Select.BOOLEAN.OFFSET<D>, Select.BOOLEAN.FOR<D>, Select.BOOLEAN.NOWAIT<D>, Select.BOOLEAN.SKIP_LOCKED<D>, Select.BOOLEAN.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.BOOLEAN.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.BOOLEAN.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class CHAR {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.CHAR._SELECT<D>, Select.CHAR.FROM<D>, Select.CHAR.GROUP_BY<D>, Select.CHAR.HAVING<D>, Select.CHAR.UNION<D>, Select.CHAR.JOIN<D>, Select.CHAR.ADV_JOIN<D>, Select.CHAR.ON<D>, Select.CHAR.ORDER_BY<D>, Select.CHAR.LIMIT<D>, Select.CHAR.OFFSET<D>, Select.CHAR.FOR<D>, Select.CHAR.NOWAIT<D>, Select.CHAR.SKIP_LOCKED<D>, Select.CHAR.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.CHAR.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.CHAR.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class CLOB {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.CLOB._SELECT<D>, Select.CLOB.FROM<D>, Select.CLOB.GROUP_BY<D>, Select.CLOB.HAVING<D>, Select.CLOB.UNION<D>, Select.CLOB.JOIN<D>, Select.CLOB.ADV_JOIN<D>, Select.CLOB.ON<D>, Select.CLOB.ORDER_BY<D>, Select.CLOB.LIMIT<D>, Select.CLOB.OFFSET<D>, Select.CLOB.FOR<D>, Select.CLOB.NOWAIT<D>, Select.CLOB.SKIP_LOCKED<D>, Select.CLOB.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.CLOB.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.CLOB.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class Column {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.Column._SELECT<D>, Select.Column.FROM<D>, Select.Column.GROUP_BY<D>, Select.Column.HAVING<D>, Select.Column.UNION<D>, Select.Column.JOIN<D>, Select.Column.ADV_JOIN<D>, Select.Column.ON<D>, Select.Column.ORDER_BY<D>, Select.Column.LIMIT<D>, Select.Column.OFFSET<D>, Select.Column.FOR<D>, Select.Column.NOWAIT<D>, Select.Column.SKIP_LOCKED<D>, Select.Column.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.Column.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.Column.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class DATE {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.DATE._SELECT<D>, Select.DATE.FROM<D>, Select.DATE.GROUP_BY<D>, Select.DATE.HAVING<D>, Select.DATE.UNION<D>, Select.DATE.JOIN<D>, Select.DATE.ADV_JOIN<D>, Select.DATE.ON<D>, Select.DATE.ORDER_BY<D>, Select.DATE.LIMIT<D>, Select.DATE.OFFSET<D>, Select.DATE.FOR<D>, Select.DATE.NOWAIT<D>, Select.DATE.SKIP_LOCKED<D>, Select.DATE.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.DATE.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.DATE.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class DATETIME {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.DATETIME._SELECT<D>, Select.DATETIME.FROM<D>, Select.DATETIME.GROUP_BY<D>, Select.DATETIME.HAVING<D>, Select.DATETIME.UNION<D>, Select.DATETIME.JOIN<D>, Select.DATETIME.ADV_JOIN<D>, Select.DATETIME.ON<D>, Select.DATETIME.ORDER_BY<D>, Select.DATETIME.LIMIT<D>, Select.DATETIME.OFFSET<D>, Select.DATETIME.FOR<D>, Select.DATETIME.NOWAIT<D>, Select.DATETIME.SKIP_LOCKED<D>, Select.DATETIME.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.DATETIME.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.DATETIME.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class DECIMAL {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.DECIMAL._SELECT<D>, Select.DECIMAL.FROM<D>, Select.DECIMAL.GROUP_BY<D>, Select.DECIMAL.HAVING<D>, Select.DECIMAL.UNION<D>, Select.DECIMAL.JOIN<D>, Select.DECIMAL.ADV_JOIN<D>, Select.DECIMAL.ON<D>, Select.DECIMAL.ORDER_BY<D>, Select.DECIMAL.LIMIT<D>, Select.DECIMAL.OFFSET<D>, Select.DECIMAL.FOR<D>, Select.DECIMAL.NOWAIT<D>, Select.DECIMAL.SKIP_LOCKED<D>, Select.DECIMAL.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.DECIMAL.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.DECIMAL.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class DOUBLE {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.DOUBLE._SELECT<D>, Select.DOUBLE.FROM<D>, Select.DOUBLE.GROUP_BY<D>, Select.DOUBLE.HAVING<D>, Select.DOUBLE.UNION<D>, Select.DOUBLE.JOIN<D>, Select.DOUBLE.ADV_JOIN<D>, Select.DOUBLE.ON<D>, Select.DOUBLE.ORDER_BY<D>, Select.DOUBLE.LIMIT<D>, Select.DOUBLE.OFFSET<D>, Select.DOUBLE.FOR<D>, Select.DOUBLE.NOWAIT<D>, Select.DOUBLE.SKIP_LOCKED<D>, Select.DOUBLE.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.DOUBLE.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.DOUBLE.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class Entity {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.Entity._SELECT<D>, Select.Entity.FROM<D>, Select.Entity.GROUP_BY<D>, Select.Entity.HAVING<D>, Select.Entity.UNION<D>, Select.Entity.JOIN<D>, Select.Entity.ADV_JOIN<D>, Select.Entity.ON<D>, Select.Entity.ORDER_BY<D>, Select.Entity.LIMIT<D>, Select.Entity.OFFSET<D>, Select.Entity.FOR<D>, Select.Entity.NOWAIT<D>, Select.Entity.SKIP_LOCKED<D>, Select.Entity.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.Entity.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.Entity.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class ENUM {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.ENUM._SELECT<D>, Select.ENUM.FROM<D>, Select.ENUM.GROUP_BY<D>, Select.ENUM.HAVING<D>, Select.ENUM.UNION<D>, Select.ENUM.JOIN<D>, Select.ENUM.ADV_JOIN<D>, Select.ENUM.ON<D>, Select.ENUM.ORDER_BY<D>, Select.ENUM.LIMIT<D>, Select.ENUM.OFFSET<D>, Select.ENUM.FOR<D>, Select.ENUM.NOWAIT<D>, Select.ENUM.SKIP_LOCKED<D>, Select.ENUM.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.ENUM.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.ENUM.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class FLOAT {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.FLOAT._SELECT<D>, Select.FLOAT.FROM<D>, Select.FLOAT.GROUP_BY<D>, Select.FLOAT.HAVING<D>, Select.FLOAT.UNION<D>, Select.FLOAT.JOIN<D>, Select.FLOAT.ADV_JOIN<D>, Select.FLOAT.ON<D>, Select.FLOAT.ORDER_BY<D>, Select.FLOAT.LIMIT<D>, Select.FLOAT.OFFSET<D>, Select.FLOAT.FOR<D>, Select.FLOAT.NOWAIT<D>, Select.FLOAT.SKIP_LOCKED<D>, Select.FLOAT.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.FLOAT.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.FLOAT.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class INT {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.INT._SELECT<D>, Select.INT.FROM<D>, Select.INT.GROUP_BY<D>, Select.INT.HAVING<D>, Select.INT.UNION<D>, Select.INT.JOIN<D>, Select.INT.ADV_JOIN<D>, Select.INT.ON<D>, Select.INT.ORDER_BY<D>, Select.INT.LIMIT<D>, Select.INT.OFFSET<D>, Select.INT.FOR<D>, Select.INT.NOWAIT<D>, Select.INT.SKIP_LOCKED<D>, Select.INT.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.INT.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.INT.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class LargeObject {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.LargeObject._SELECT<D>, Select.LargeObject.FROM<D>, Select.LargeObject.GROUP_BY<D>, Select.LargeObject.HAVING<D>, Select.LargeObject.UNION<D>, Select.LargeObject.JOIN<D>, Select.LargeObject.ADV_JOIN<D>, Select.LargeObject.ON<D>, Select.LargeObject.ORDER_BY<D>, Select.LargeObject.LIMIT<D>, Select.LargeObject.OFFSET<D>, Select.LargeObject.FOR<D>, Select.LargeObject.NOWAIT<D>, Select.LargeObject.SKIP_LOCKED<D>, Select.LargeObject.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.LargeObject.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.LargeObject.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class Numeric {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.Numeric._SELECT<D>, Select.Numeric.FROM<D>, Select.Numeric.GROUP_BY<D>, Select.Numeric.HAVING<D>, Select.Numeric.UNION<D>, Select.Numeric.JOIN<D>, Select.Numeric.ADV_JOIN<D>, Select.Numeric.ON<D>, Select.Numeric.ORDER_BY<D>, Select.Numeric.LIMIT<D>, Select.Numeric.OFFSET<D>, Select.Numeric.FOR<D>, Select.Numeric.NOWAIT<D>, Select.Numeric.SKIP_LOCKED<D>, Select.Numeric.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.Numeric.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.Numeric.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class SMALLINT {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.SMALLINT._SELECT<D>, Select.SMALLINT.FROM<D>, Select.SMALLINT.GROUP_BY<D>, Select.SMALLINT.HAVING<D>, Select.SMALLINT.UNION<D>, Select.SMALLINT.JOIN<D>, Select.SMALLINT.ADV_JOIN<D>, Select.SMALLINT.ON<D>, Select.SMALLINT.ORDER_BY<D>, Select.SMALLINT.LIMIT<D>, Select.SMALLINT.OFFSET<D>, Select.SMALLINT.FOR<D>, Select.SMALLINT.NOWAIT<D>, Select.SMALLINT.SKIP_LOCKED<D>, Select.SMALLINT.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.SMALLINT.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.SMALLINT.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class Temporal {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.Temporal._SELECT<D>, Select.Temporal.FROM<D>, Select.Temporal.GROUP_BY<D>, Select.Temporal.HAVING<D>, Select.Temporal.UNION<D>, Select.Temporal.JOIN<D>, Select.Temporal.ADV_JOIN<D>, Select.Temporal.ON<D>, Select.Temporal.ORDER_BY<D>, Select.Temporal.LIMIT<D>, Select.Temporal.OFFSET<D>, Select.Temporal.FOR<D>, Select.Temporal.NOWAIT<D>, Select.Temporal.SKIP_LOCKED<D>, Select.Temporal.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.Temporal.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.Temporal.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class Textual {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.Textual._SELECT<D>, Select.Textual.FROM<D>, Select.Textual.GROUP_BY<D>, Select.Textual.HAVING<D>, Select.Textual.UNION<D>, Select.Textual.JOIN<D>, Select.Textual.ADV_JOIN<D>, Select.Textual.ON<D>, Select.Textual.ORDER_BY<D>, Select.Textual.LIMIT<D>, Select.Textual.OFFSET<D>, Select.Textual.FOR<D>, Select.Textual.NOWAIT<D>, Select.Textual.SKIP_LOCKED<D>, Select.Textual.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.Textual.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.Textual.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class TIME {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.TIME._SELECT<D>, Select.TIME.FROM<D>, Select.TIME.GROUP_BY<D>, Select.TIME.HAVING<D>, Select.TIME.UNION<D>, Select.TIME.JOIN<D>, Select.TIME.ADV_JOIN<D>, Select.TIME.ON<D>, Select.TIME.ORDER_BY<D>, Select.TIME.LIMIT<D>, Select.TIME.OFFSET<D>, Select.TIME.FOR<D>, Select.TIME.NOWAIT<D>, Select.TIME.SKIP_LOCKED<D>, Select.TIME.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.TIME.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.TIME.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  public static class TINYINT {
    static class SELECT<D extends data.Entity<?>> extends untyped.SELECT<D> implements Select.TINYINT._SELECT<D>, Select.TINYINT.FROM<D>, Select.TINYINT.GROUP_BY<D>, Select.TINYINT.HAVING<D>, Select.TINYINT.UNION<D>, Select.TINYINT.JOIN<D>, Select.TINYINT.ADV_JOIN<D>, Select.TINYINT.ON<D>, Select.TINYINT.ORDER_BY<D>, Select.TINYINT.LIMIT<D>, Select.TINYINT.OFFSET<D>, Select.TINYINT.FOR<D>, Select.TINYINT.NOWAIT<D>, Select.TINYINT.SKIP_LOCKED<D>, Select.TINYINT.WHERE<D> {
      SELECT(final boolean distinct, final type.Entity<?>[] entities) {
        super(distinct, entities);
      }

      @Override
      public D AS(final D as) {
        as.wrap(new As<D>(this, as, true));
        return as;
      }

      @Override
      public SELECT<D> FROM(final data.Table ... from) {
        super.FROM(from);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final data.Table table) {
        super.CROSS_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> CROSS_JOIN(final Select.untyped.SELECT<?> select) {
        super.CROSS_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final data.Table table) {
        super.NATURAL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> NATURAL_JOIN(final Select.untyped.SELECT<?> select) {
        super.NATURAL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final data.Table table) {
        super.LEFT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> LEFT_JOIN(final Select.untyped.SELECT<?> select) {
        super.LEFT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final data.Table table) {
        super.RIGHT_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> RIGHT_JOIN(final Select.untyped.SELECT<?> select) {
        super.RIGHT_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final data.Table table) {
        super.FULL_JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> FULL_JOIN(final Select.untyped.SELECT<?> select) {
        super.FULL_JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final data.Table table) {
        super.JOIN(table);
        return this;
      }

      @Override
      public SELECT<D> JOIN(final Select.untyped.SELECT<?> select) {
        super.JOIN(select);
        return this;
      }

      @Override
      public SELECT<D> ON(final Condition<?> on) {
        super.ON(on);
        return this;
      }

      @Override
      public SELECT<D> GROUP_BY(final type.Entity<?> ... groupBy) {
        super.GROUP_BY(groupBy);
        return this;
      }

      @Override
      public SELECT<D> HAVING(final Condition<?> having) {
        super.HAVING(having);
        return this;
      }

      @Override
      public SELECT<D> UNION(final Select.TINYINT.SELECT<D> select) {
        super.UNION(select);
        return this;
      }

      @Override
      public SELECT<D> UNION_ALL(final Select.TINYINT.SELECT<D> select) {
        super.UNION_ALL(select);
        return this;
      }

      @Override
      public SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
        super.ORDER_BY(columns);
        return this;
      }

      private SELECT<D> ORDER_BY(final int ... columnNumbers) {
        super.ORDER_BY(columnNumbers);
        return this;
      }

      @Override
      public SELECT<D> LIMIT(final int rows) {
        super.LIMIT(rows);
        return this;
      }

      @Override
      public SELECT<D> OFFSET(final int rows) {
        super.OFFSET(rows);
        return this;
      }

      @Override
      public SELECT<D> FOR_SHARE(final data.Entity<?> ... subjects) {
        super.FOR_SHARE(subjects);
        return this;
      }

      @Override
      public SELECT<D> FOR_UPDATE(final data.Entity<?> ... subjects) {
        super.FOR_UPDATE(subjects);
        return this;
      }

      @Override
      public SELECT<D> NOWAIT() {
        super.NOWAIT();
        return this;
      }

      @Override
      public SELECT<D> SKIP_LOCKED() {
        super.SKIP_LOCKED();
        return this;
      }

      @Override
      public SELECT<D> WHERE(final Condition<?> where) {
        super.WHERE(where);
        return this;
      }
    }
  }

  private SelectImpl() {
  }
}