/* Copyright (c) 2021 JAX-DB
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

import static org.libj.lang.Assertions.*;

import java.io.Closeable;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.jaxdb.jsql.Callbacks.OnCommit;
import org.jaxdb.jsql.Callbacks.OnExecute;
import org.jaxdb.jsql.Callbacks.OnNotify;
import org.jaxdb.jsql.Callbacks.OnRollback;
import org.jaxdb.jsql.keyword.Case;
import org.jaxdb.jsql.keyword.Delete.DELETE;
import org.jaxdb.jsql.keyword.Delete._DELETE;
import org.jaxdb.jsql.keyword.Insert.INSERT;
import org.jaxdb.jsql.keyword.Insert._INSERT;
import org.jaxdb.jsql.keyword.Keyword;
import org.jaxdb.jsql.keyword.Update.SET;
import org.jaxdb.jsql.keyword.Update.UPDATE;
import org.jaxdb.vendor.DbVendor;
import org.libj.lang.Throwables;
import org.libj.lang.UUIDs;
import org.libj.sql.AuditConnection;
import org.libj.sql.AuditStatement;
import org.libj.sql.ResultSets;
import org.libj.sql.exception.SQLExceptions;
import org.libj.util.ArrayUtil;
import org.libj.util.function.ToBooleanFunction;

abstract class Command<E> extends Keyword implements Closeable {
  @SuppressWarnings("unchecked")
  public final E onExecute(final OnExecute onExecute) {
    getCallbacks().addOnExecute(onExecute);
    return (E)this;
  }

  private boolean closed;
  Callbacks callbacks;

  Callbacks getCallbacks() {
    return callbacks == null ? callbacks = new Callbacks() : callbacks;
  }

  void assertNotClosed() {
    if (closed)
      throw new IllegalStateException("statement is closed");
  }

  @Override
  public void close() {
    closed = true;
  }

  abstract static class Modification<E,C,R> extends Command<E> {
    data.Table entity;

    private Modification(final data.Table entity) {
      this.entity = entity;
    }

    final void revertEntity() {
      if (entity != null)
        entity.revert();
    }

    @Override
    public final void close() {
      super.close();
      if (entity != null)
        entity._commitEntity$();
    }

    String sessionId;

    @SuppressWarnings("unchecked")
    public final C onCommit(final OnCommit onCommit) {
      getCallbacks().addOnCommit(onCommit);
      return (C)this;
    }

    @SuppressWarnings("unchecked")
    public final R onRollback(final OnRollback onRollback) {
      getCallbacks().addOnRollback(onRollback);
      return (R)this;
    }
  }

  private static final data.Column<?>[] emptyColumns = {};

  private static data.Column<?>[] recurseColumns(final data.Column<?>[] columns, final ToBooleanFunction<data.Column<?>> predicate, final int length, final int index, final int depth) {
    if (index == length)
      return depth == 0 ? emptyColumns : new data.Column<?>[depth];

    final data.Column<?> column = columns[index];
    final boolean include = predicate.applyAsBoolean(column);
    if (!include)
      return recurseColumns(columns, predicate, length, index + 1, depth);

    final data.Column<?>[] results = recurseColumns(columns, predicate, length, index + 1, depth + 1);
    results[depth] = column;
    return results;
  }

  static final class Insert<D extends data.Table> extends Command.Modification<keyword.Insert.CONFLICT_ACTION_EXECUTE,keyword.Insert.CONFLICT_ACTION_COMMIT,keyword.Insert.CONFLICT_ACTION_ROLLBACK> implements _INSERT<D>, keyword.Insert.CONFLICT_ACTION_NOTIFY, keyword.Insert.ON_CONFLICT {
    private data.Column<?>[] columns;
    private data.Column<?>[] primaries;
    final data.Column<?>[] autos;
    private keyword.Select.untyped.SELECT<?> select;
    private boolean isOnConflict;
    private data.Column<?>[] onConflictColumns;
    private boolean doUpdate;

    Insert(final data.Table entity) {
      super(entity);
      this.columns = null;
      this.autos = recurseColumns(entity._auto$, c -> c.setByCur != data.Column.SetBy.USER, entity._auto$.length, 0, 0);
    }

    Insert(final data.Column<?> column, final data.Column<?>[] columns) {
      super(null);
      this.columns = ArrayUtil.splice(columns, 0, 0, column);
      final data.Table table = this.columns[0].getTable();
      for (int i = 1, i$ = this.columns.length; i < i$; ++i) // [A]
        if (!this.columns[i].getTable().equals(table))
          throw new IllegalArgumentException("All columns must belong to the same Table");

      this.primaries = recurseColumns(this.columns, c -> c.primaryIndexType != null, this.columns.length, 0, 0);
      this.autos = recurseColumns(this.columns, c -> c.setByCur != data.Column.SetBy.USER && c.generateOnInsert == GenerateOn.AUTO_GENERATED, this.columns.length, 0, 0);
    }

    @Override
    public keyword.Insert.CONFLICT_ACTION_NOTIFY onNotify(final OnNotify onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    @Override
    public keyword.Insert.CONFLICT_ACTION_NOTIFY onNotify(final boolean onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    @Override
    public INSERT<D> VALUES(final keyword.Select.untyped.SELECT<?> select) {
      this.select = select;
      return this;
    }

    @Override
    public keyword.Insert.ON_CONFLICT ON_CONFLICT() {
      isOnConflict = true;
      if (entity != null)
        onConflictColumns = entity._primary$;
      else if (primaries != null)
        onConflictColumns = primaries;

      return this;
    }

    @Override
    public keyword.Insert.CONFLICT_ACTION DO_UPDATE() {
      doUpdate = true;
      return this;
    }

    @Override
    public keyword.Insert.CONFLICT_ACTION DO_NOTHING() {
      doUpdate = false;
      return this;
    }

    @Override
    final data.Table getTable() {
      if (entity != null)
        return entity;

      if (columns != null)
        return entity = columns[0].getTable();

      throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");
    }

    @Override
    final data.Column<?> getColumn() {
      throw new UnsupportedOperationException();
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final data.Column<?>[] columns = this.columns != null ? this.columns : entity._column$;
      final Compiler compiler = compilation.compiler;
      if (isOnConflict)
        compiler.compileInsertOnConflict(columns, select, onConflictColumns, doUpdate, compilation);
      else if (select != null)
        compiler.compileInsertSelect(columns, select, false, compilation);
      else
        compiler.compileInsert(columns, false, compilation);
    }
  }

  static final class Update extends Command.Modification<keyword.Update.UPDATE_EXECUTE,keyword.Update.UPDATE_COMMIT,keyword.Update.UPDATE_ROLLBACK> implements SET, keyword.Update.UPDATE_NOTIFY {
    private ArrayList<Subject> sets;
    private Condition<?> where;

    Update(final data.Table entity) {
      super(entity);
    }

    @Override
    public keyword.Update.UPDATE_NOTIFY onNotify(final OnNotify onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    @Override
    public keyword.Update.UPDATE_NOTIFY onNotify(final boolean onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    private void initSets() {
      if (sets == null)
        sets = new ArrayList<>();
    }

    @Override
    public <T> SET SET(final data.Column<? extends T> column, final type.Column<? extends T> to) {
      initSets();
      sets.add(column);
      sets.add((Subject)to);
      return this;
    }

    @Override
    public <T> SET SET(final data.Column<T> column, final T to) {
      initSets();
      sets.add(column);
      // FIXME: data.ENUM.NULL
      sets.add(to == null ? null : data.wrap(to));
      return this;
    }

    @Override
    public UPDATE WHERE(final Condition<?> condition) {
      this.where = condition;
      return this;
    }

    @Override
    final data.Table getTable() {
      return entity;
    }

    @Override
    final data.Column<?> getColumn() {
      throw new UnsupportedOperationException();
    }

    @Override
    final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Compiler compiler = compilation.compiler;
      if (sets != null)
        compiler.compileUpdate(entity, sets, where, compilation);
      else
        compiler.compileUpdate(entity, compilation);
    }
  }

  static final class Delete extends Command.Modification<keyword.Delete.DELETE_EXECUTE,keyword.Delete.DELETE_COMMIT,keyword.Delete.DELETE_ROLLBACK> implements _DELETE, keyword.Delete.DELETE_NOTIFY {
    private Condition<?> where;

    Delete(final data.Table entity) {
      super(entity);
    }

    @Override
    public keyword.Delete.DELETE_NOTIFY onNotify(final OnNotify onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    @Override
    public keyword.Delete.DELETE_NOTIFY onNotify(final boolean onNotify) {
      if (sessionId == null)
        sessionId = UUIDs.toString32(UUID.randomUUID());

      getCallbacks().addOnNotify(sessionId, onNotify);
      return this;
    }

    @Override
    public DELETE WHERE(final Condition<?> where) {
      this.where = where;
      return this;
    }

    @Override
    final data.Table getTable() {
      return entity;
    }

    @Override
    final data.Column<?> getColumn() {
      return where;
    }

    @Override
    void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
      final Compiler compiler = compilation.compiler;
      if (where != null)
        compiler.compileDelete(entity, where, compilation);
      else
        compiler.compileDelete(entity, compilation);
    }
  }

  static final class Select {
    public static class untyped {
      abstract static class SELECT<D extends type.Entity> extends Command<statement.Query<type.Entity>> implements keyword.Select.untyped._SELECT<D>, keyword.Select.untyped.FROM<D>, keyword.Select.untyped.GROUP_BY<D>, keyword.Select.untyped.HAVING<D>, keyword.Select.untyped.UNION<D>, keyword.Select.untyped.JOIN<D>, keyword.Select.untyped.ADV_JOIN<D>, keyword.Select.untyped.ON<D>, keyword.Select.untyped.ORDER_BY<D>, keyword.Select.untyped.LIMIT<D>, keyword.Select.untyped.OFFSET<D>, keyword.Select.untyped.FOR<D>, keyword.Select.untyped.NOWAIT<D>, keyword.Select.untyped.SKIP_LOCKED<D>, keyword.Select.untyped.WHERE<D> {
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

        private boolean tableCalled;
        private data.Table table;

        final boolean distinct;
        final type.Entity[] entities;

        private boolean fromCalled;
        private data.Table[] from;

        ArrayList<Object> joins;
        ArrayList<Condition<?>> on;

        data.Entity[] groupBy;
        Condition<?> having;

        ArrayList<Object> unions;

        data.Column<?>[] orderBy;
        int[] orderByIndexes;

        int limit = -1;
        int offset = -1;

        LockStrength forLockStrength;
        Subject[] forSubjects;
        LockOption forLockOption;

        private boolean isObjectQuery;
        private boolean whereCalled;
        private Condition<?> where;

        boolean isEntityOnlySelect;
        boolean isConditionalSelect = false;

        SELECT(final boolean distinct, final type.Entity[] entities) {
          if (entities.length < 1)
            throw new IllegalArgumentException("entities.length < 1");

          assertNotNullArray(entities, "Argument to SELECT cannot be null (use type.?.NULL instead)");

          this.entities = entities;
          this.distinct = distinct;
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public SELECT<D> FROM(final data.Table ... from) {
          this.from = from;
          fromCalled = true;
          return this;
        }

        @Override
        public SELECT<D> CROSS_JOIN(final data.Table table) {
          return JOIN(JoinKind.CROSS, table);
        }

        @Override
        public SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.CROSS, select);
        }

        @Override
        public SELECT<D> NATURAL_JOIN(final data.Table table) {
          return JOIN(JoinKind.NATURAL, table);
        }

        @Override
        public SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.NATURAL, select);
        }

        @Override
        public SELECT<D> LEFT_JOIN(final data.Table table) {
          return JOIN(JoinKind.LEFT, table);
        }

        @Override
        public SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.LEFT, select);
        }

        @Override
        public SELECT<D> RIGHT_JOIN(final data.Table table) {
          return JOIN(JoinKind.RIGHT, table);
        }

        @Override
        public SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.RIGHT, select);
        }

        @Override
        public SELECT<D> FULL_JOIN(final data.Table table) {
          return JOIN(JoinKind.FULL, table);
        }

        @Override
        public SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.FULL, select);
        }

        @Override
        public SELECT<D> JOIN(final data.Table table) {
          return JOIN(JoinKind.INNER, table);
        }

        @Override
        public SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          return JOIN(JoinKind.INNER, select);
        }

        private SELECT<D> JOIN(final JoinKind kind, final Object join) {
          ArrayList<Object> joins = this.joins;
          if (joins == null)
            joins = this.joins = new ArrayList<>();

          joins.add(kind);
          joins.add(join);
          return this;
        }

        @Override
        public SELECT<D> ON(final Condition<?> on) {
          if (this.on == null)
            this.on = new ArrayList<>();

          // Since ON is optional, for each JOIN without ON, add a null to this.on
          for (int i = 0, joinsSize = this.joins.size(), onSize = this.on.size(); i < joinsSize / 2 - onSize - 1; ++i) // [N]
            this.on.add(null);

          this.on.add(on);
          return this;
        }

        @Override
        public SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          this.groupBy = groupBy;
          return this;
        }

        @Override
        public SELECT<D> HAVING(final Condition<?> having) {
          this.having = having;
          return this;
        }

        @Override
        public SELECT<D> UNION(final keyword.Select.untyped.SELECT<D> select) {
          UNION(Boolean.FALSE, select);
          return this;
        }

        @Override
        public SELECT<D> UNION_ALL(final keyword.Select.untyped.SELECT<D> select) {
          UNION(Boolean.TRUE, select);
          return this;
        }

        private SELECT<D> UNION(final Boolean all, final keyword.Select.untyped.SELECT<D> select) {
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
        public SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          return FOR(LockStrength.SHARE, subjects);
        }

        @Override
        public SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          return FOR(LockStrength.UPDATE, subjects);
        }

        private SELECT<D> FOR(final LockStrength lockStrength, final data.Entity ... subjects) {
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

        data.Column<?>[] getPrimaryColumnsFromCondition(final Condition<?> condition) {
          final ArrayList<data.Column<?>> columns = new ArrayList<>(); // FIXME: Is there a way to do this without an ArrayList?
          condition.collectColumns(columns);
          return columns.toArray(new data.Column<?>[columns.size()]);
        }

        private Object[][] compile(final type.Entity[] entities, final int length, final int index, final int depth) {
          if (index == length)
            return new Object[depth][2];

          final type.Entity entity = entities[index];
          if (entity instanceof data.Table) {
            final data.Table table = (data.Table)entity;
            final int noColumns = table._column$.length;
            final Object[][] protoSubjectIndexes = compile(entities, length, index + 1, depth + noColumns);
            for (int i = 0; i < noColumns; ++i) { // [A]
              final Object[] array = protoSubjectIndexes[depth + i];
              array[0] = table._column$[i];
              array[1] = i;
            }

            return protoSubjectIndexes;
          }

          if (entity instanceof type.Column) {
            isEntityOnlySelect = false;
            final type.Column<?> column = (type.Column<?>)entity;
            final Object[][] protoSubjectIndexes = compile(entities, length, index + 1, depth + 1);
            final Object[] array = protoSubjectIndexes[depth];
            array[0] = column;
            array[1] = -1;
            return protoSubjectIndexes;
          }

          if (entity instanceof untyped.SELECT) {
            isEntityOnlySelect = false;
            final type.Entity[] selectEntities = ((untyped.SELECT<?>)entity).entities;
            if (selectEntities.length != 1)
              throw new IllegalStateException("Expected 1 entity, but got " + selectEntities.length);

            final type.Entity selectEntity = selectEntities[0];
            if (!(selectEntity instanceof data.Column))
              throw new IllegalStateException("Expected data.Column, but got: " + selectEntity.getClass().getName());

            final Object[][] protoSubjectIndexes = compile(entities, length, index + 1, depth + 1);
            final Object[] array = protoSubjectIndexes[depth];
            array[0] = selectEntity;
            array[1] = -1;
            return protoSubjectIndexes;
          }

          throw new IllegalStateException("Unknown entity type: " + entity.getClass().getName());
        }

        void assertRowIteratorConsumed(final boolean endReached, final boolean isCacheable, final SQLException e, final boolean isCacheableRowIteratorFullConsume) throws SQLException {
          if (!endReached && isCacheable && isCacheableRowIteratorFullConsume) {
            final IllegalStateException ie = new IllegalStateException("RowIterator end not reached for cacheableRowIteratorFullConsume=true");
            if (e != null)
              ie.addSuppressed(e);

            throw ie;
          }

          if (e != null)
            throw SQLExceptions.toStrongType(e);
        }

        @SuppressWarnings("unchecked")
        RowIterator<D> execute(final Schema schema, final Transaction transaction, Connector connector, Connection connection, boolean isPrepared, final Transaction.Isolation isolation, final QueryConfig contextQueryConfig) throws IOException, SQLException {
          assertNotClosed();

          Statement statement = null;
          try {
            if (transaction != null) {
              isPrepared = transaction.isPrepared();
              connection = transaction.getConnection();
            }
            else if (connection == null) {
              if (connector == null)
                connector = schema.getConnector();

              isPrepared = connector.isPrepared();
              connection = connector.getConnection(isolation);
              connection.setAutoCommit(true);
            }

            final DbVendor vendor = DbVendor.valueOf(connection.getMetaData());
            try (final Compilation compilation = new Compilation(this, vendor, isPrepared)) {
              final QueryConfig defaultQueryConfig = schema.defaultQueryConfig;

              isEntityOnlySelect = true;
              final Object[][] protoSubjectIndexes = compile(entities, entities.length, 0, 0);
              final boolean cacheSelectEntity = QueryConfig.getCacheSelectEntity(contextQueryConfig, defaultQueryConfig);
              compile(compilation, false, cacheSelectEntity);
              final Notifier<?> notifier;
              final boolean isSelectAll;
              if (cacheSelectEntity) {
                notifier = schema.getCacheNotifier();
                isSelectAll = isEntityOnlySelect && !isConditionalSelect;
              }
              else {
                notifier = null;
                isSelectAll = false;
              }

              final int columnOffset = compilation.skipFirstColumn() ? 2 : 1;
              final Compiler compiler = compilation.compiler;

              final ResultSet resultSet = QueryConfig.executeQuery(contextQueryConfig, defaultQueryConfig, compilation, connection);
              if (callbacks != null)
                callbacks.onExecute(Statement.SUCCESS_NO_INFO);

              final Connector connectorFinal = connector;
              final Connection connectionFinal = connection;
              final Statement statementFinal = statement = resultSet.getStatement();
              final int noColumns = resultSet.getMetaData().getColumnCount() + 1 - columnOffset;
              return new RowIterator<D>(resultSet, contextQueryConfig, defaultQueryConfig) {
                private final boolean isCacheableRowIteratorFullConsume = QueryConfig.getCacheableRowIteratorFullConsume(contextQueryConfig, defaultQueryConfig);
                private HashMap<Class<?>,data.Table> prototypes = new HashMap<>();
                private HashMap<data.Table,data.Table> cachedTables = new HashMap<>();
                private data.Table currentTable;
                private boolean mustFetchRow = false;

                @Override
                public boolean nextRow() throws SQLException {
                  if (endReached)
                    return false;

                  try {
                    if (endReached = !resultSet.next()) {
                      if (isSelectAll)
                        table._commitSelectAll$();

                      return false;
                    }

                    mustFetchRow = true;
                  }
                  catch (SQLException e) {
                    e = Throwables.addSuppressed(e, suppressed);
                    suppressed = null;
                    throw SQLExceptions.toStrongType(e);
                  }

                  return true;
                }

                @Override
                public D nextEntity() throws SQLException {
                  fetchRow();
                  return super.nextEntity();
                }

                private void onSelect(final data.Table row) {
                  if (notifier != null && row.getCacheSelectEntity())
                    notifier.onSelect(row);
                }

                @SuppressWarnings("null")
                private void fetchRow() throws SQLException {
                  if (!mustFetchRow)
                    return;

                  final type.Entity[] row;
                  int index = 0;
                  data.Table table;
                  try {
                    row = new type.Entity[entities.length];
                    table = null;
                    for (int i = 0; i < noColumns; ++i) { // [A]
                      final Object[] protoSubjectIndex = protoSubjectIndexes[i];
                      final Subject protoSubject = (Subject)protoSubjectIndex[0];
                      final Integer protoIndex = (Integer)protoSubjectIndex[1];
                      if (currentTable != null && (currentTable != protoSubject.getTable() || protoIndex == -1)) {
                        data.Table cachedTable = cachedTables.get(table);
                        if (cachedTable == null) {
                          cachedTables.put(table, cachedTable = table);
                          prototypes.put(table.getClass(), table.newInstance());
                        }

                        row[index++] = cachedTable;
                        onSelect(cachedTable);
                      }

                      final data.Column<?> column;
                      if (protoIndex != -1) {
                        currentTable = protoSubject.getTable();
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
                          column = protoSubject.getColumn().clone();
                        }

                        row[index++] = column;
                      }

                      column.read(compiler, resultSet, i + columnOffset);
                    }
                  }
                  catch (SQLException e) {
                    e = Throwables.addSuppressed(e, suppressed);
                    suppressed = null;
                    throw SQLExceptions.toStrongType(e);
                  }

                  if (table != null) {
                    final data.Table cachedTable = cachedTables.getOrDefault(table, table);
                    row[index++] = cachedTable;
                    onSelect(cachedTable);
                  }

                  setRow((D[])row);
                  prototypes.clear();
                  currentTable = null;
                  mustFetchRow = false;
                }

                @Override
                public void close() throws SQLException {
                  SQLException e = Throwables.addSuppressed(suppressed, ResultSets.close(resultSet));
                  e = Throwables.addSuppressed(e, AuditStatement.close(statementFinal));
                  if (connectorFinal != null)
                    e = Throwables.addSuppressed(e, AuditConnection.close(connectionFinal));

                  prototypes = null;
                  cachedTables = null;
                  currentTable = null;

                  assertRowIteratorConsumed(endReached, isEntityOnlySelect, e, isCacheableRowIteratorFullConsume);
                }
              };
            }
          }
          catch (SQLException e) {
            if (statement != null)
              e = Throwables.addSuppressed(e, AuditStatement.close(statement));

            if (connector != null)
              e = Throwables.addSuppressed(e, AuditConnection.close(connection));

            throw SQLExceptions.toStrongType(e);
          }
          finally {
            close();
          }
        }

        @Override
        public final RowIterator<D> execute(final Transaction transaction) throws IOException, SQLException {
          return execute(getSchema(), transaction, null, null, false, null, null);
        }

        @Override
        public final RowIterator<D> execute(final Connector connector, final Transaction.Isolation isolation) throws IOException, SQLException {
          return execute(getSchema(), null, connector, null, false, isolation, null);
        }

        @Override
        public final RowIterator<D> execute(final Connector connector) throws IOException, SQLException {
          return execute(getSchema(), null, connector, null, false, null, null);
        }

        @Override
        public final RowIterator<D> execute(final Connection connection, final boolean isPrepared) throws IOException, SQLException {
          return execute(getSchema(), null, null, assertNotNull(connection), isPrepared, null, null);
        }

        @Override
        public final RowIterator<D> execute(final Transaction.Isolation isolation) throws IOException, SQLException {
          final Schema schema = getSchema();
          return execute(schema, null, null, null, false, isolation, null);
        }

        @Override
        public RowIterator<D> execute() throws IOException, SQLException {
          final Schema schema = getSchema();
          return execute(schema, null, null, null, false, null, null);
        }

        @Override
        public final RowIterator<D> execute(final Transaction transaction, final QueryConfig config) throws IOException, SQLException {
          return execute(getSchema(), transaction, null, null, false, null, config);
        }

        @Override
        public final RowIterator<D> execute(final Connector connector, final Transaction.Isolation isolation, final QueryConfig config) throws IOException, SQLException {
          return execute(getSchema(), null, connector, null, false, isolation, config);
        }

        @Override
        public final RowIterator<D> execute(final Connector connector, final QueryConfig config) throws IOException, SQLException {
          return execute(getSchema(), null, connector, null, false, null, config);
        }

        @Override
        public final RowIterator<D> execute(final Connection connection, final boolean isPrepared, final QueryConfig config) throws IOException, SQLException {
          return execute(getSchema(), null, null, assertNotNull(connection), isPrepared, null, null);
        }

        @Override
        public final RowIterator<D> execute(final Transaction.Isolation isolation, final QueryConfig config) throws IOException, SQLException {
          final Schema schema = getSchema();
          return execute(schema, null, null, null, false, isolation, config);
        }

        @Override
        public RowIterator<D> execute(final QueryConfig config) throws IOException, SQLException {
          final Schema schema = getSchema();
          return execute(schema, null, null, null, false, null, null);
        }

        @Override
        final data.Table getTable() {
          if (tableCalled)
            return table;

          tableCalled = true;
          // FIXME: Note that this returns the 1st table only! Is this what we want?!
          if (entities[0] instanceof Select.untyped.SELECT)
            return table = ((Select.untyped.SELECT<?>)entities[0]).getTable();

          final data.Table[] from = from();
          return from != null ? table = from[0] : null;
        }

        @Override
        final data.Column<?> getColumn() {
          if (entities.length == 1)
            return ((Subject)entities[0]).getColumn();

          throw new UnsupportedOperationException();
        }

        // FIXME: What is translateTypes for again?
        HashMap<Integer,data.ENUM<?>> translateTypes;

        data.Table[] from() {
          if (fromCalled)
            return from;

          fromCalled = true;
          from = getTables(entities, entities.length, 0, 0);
          if ((isObjectQuery = where == null) || from == null)
            for (final type.Entity entity : entities) // [A]
              isObjectQuery &= entity instanceof data.Table;

          return from;
        }

        private static data.Table[] getTables(final type.Entity[] subjects, final int length, final int index, final int depth) {
          if (index == length)
            return depth == 0 ? null : new data.Table[depth];

          final type.Entity entity = subjects[index];
          final data.Table table = ((Subject)entity).getTable();
          if (table == null)
            return getTables(subjects, length, index + 1, depth);

          final data.Table[] tables = getTables(subjects, length, index + 1, depth + 1);
          tables[depth] = table;
          return tables;
        }

        Condition<?> where() {
          if (whereCalled)
            return where;

          whereCalled = true;
          if (isObjectQuery) {
            final Condition<?>[] conditions = createObjectQueryConditions(entities, entities.length, 0, 0);
            if (conditions != null)
              where = conditions.length == 1 ? conditions[0] : DML.AND(conditions);
          }

          isConditionalSelect = where != null;
          return where;
        }

        private static Condition<?>[] createObjectQueryConditions(final type.Entity[] entities, final int length, final int index, final int depth) {
          if (index == length)
            return depth == 0 ? null : new Condition[depth];

          final type.Entity entity = entities[index];
          if (entity instanceof data.Table) {
            final data.Column<?>[] columns = ((data.Table)entity)._column$;
            final Condition<?>[] columnConditions = createObjectQueryConditions(columns, columns.length, 0, 0);
            if (columnConditions == null)
              return createObjectQueryConditions(entities, length, index + 1, depth);

            final Condition<?>[] entityConditions = createObjectQueryConditions(entities, length, index + 1, depth + 1);
            entityConditions[depth] = columnConditions.length == 1 ? columnConditions[0] : DML.AND(columnConditions);
            return entityConditions;
          }
          else if (entity instanceof Select.untyped.SELECT) {
            return createObjectQueryConditions(entities, length, index + 1, depth);
          }

          throw new UnsupportedOperationException("Unsupported entity of object query: " + entity.getClass().getName());
        }

        private static Condition<?>[] createObjectQueryConditions(final data.Column<?>[] columns, final int length, final int index, final int depth) {
          if (index == length)
            return depth == 0 ? null : new Condition[depth];

          final data.Column<?> column = columns[index];
          final boolean wasSet = column.setByCur == data.Column.SetBy.USER || (column.primaryIndexType != null || column.isKeyForUpdate) && column.setByCur == data.Column.SetBy.SYSTEM;
          if (!wasSet)
            return createObjectQueryConditions(columns, length, index + 1, depth);

          final Condition<?>[] conditions = createObjectQueryConditions(columns, length, index + 1, depth + 1);
          conditions[depth] = new ComparisonPredicate.Eq<>(column, column.get());
          return conditions;
        }

        @Override
        final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          final Compiler compiler = compilation.compiler;
          final boolean useAliases = forLockStrength == null || forSubjects == null || forSubjects.length == 0 || compiler.aliasInForUpdate();
          compiler.assignAliases(from(), joins, compilation);
          compiler.compileSelect(this, useAliases, compilation);
          compiler.compileFrom(from, useAliases, compilation);
          if (joins != null)
            for (int i = 0, j = 0, i$ = joins.size(), j$ = on == null ? Integer.MIN_VALUE : on.size(); i < i$; j = i / 2) // [RA]
              compiler.compileJoin((JoinKind)joins.get(i++), joins.get(i++), j < j$ ? on.get(j) : null, compilation);

          compiler.compileWhere(where(), compilation);
          compiler.compileGroupByHaving(this, useAliases, compilation);
          compiler.compileUnion(this, compilation);
          compiler.compileOrderBy(this, compilation);
          compiler.compileLimitOffset(this, compilation);
          if (forLockStrength != null)
            compiler.compileFor(this, compilation);
        }

        void compile(final Compilation compilation, final boolean isExpression, final boolean cacheSelectEntity) throws IOException, SQLException {
          compile(compilation, isExpression);
          if (cacheSelectEntity && !isEntityOnlySelect)
            throw new IllegalStateException("QueryConfig.cacheSelectEntity=true can only be fulfilled for queries that exclusively select entities instead of individual columns");
        }
      }
    }

    public static class ARRAY {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.ARRAY._SELECT<D>, keyword.Select.ARRAY.FROM<D>, keyword.Select.ARRAY.GROUP_BY<D>, keyword.Select.ARRAY.HAVING<D>, keyword.Select.ARRAY.UNION<D>, keyword.Select.ARRAY.JOIN<D>, keyword.Select.ARRAY.ADV_JOIN<D>, keyword.Select.ARRAY.ON<D>, keyword.Select.ARRAY.ORDER_BY<D>, keyword.Select.ARRAY.LIMIT<D>, keyword.Select.ARRAY.OFFSET<D>, keyword.Select.ARRAY.FOR<D>, keyword.Select.ARRAY.NOWAIT<D>, keyword.Select.ARRAY.SKIP_LOCKED<D>, keyword.Select.ARRAY.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.ARRAY.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.ARRAY.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class BIGINT {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.BIGINT._SELECT<D>, keyword.Select.BIGINT.FROM<D>, keyword.Select.BIGINT.GROUP_BY<D>, keyword.Select.BIGINT.HAVING<D>, keyword.Select.BIGINT.UNION<D>, keyword.Select.BIGINT.JOIN<D>, keyword.Select.BIGINT.ADV_JOIN<D>, keyword.Select.BIGINT.ON<D>, keyword.Select.BIGINT.ORDER_BY<D>, keyword.Select.BIGINT.LIMIT<D>, keyword.Select.BIGINT.OFFSET<D>, keyword.Select.BIGINT.FOR<D>, keyword.Select.BIGINT.NOWAIT<D>, keyword.Select.BIGINT.SKIP_LOCKED<D>, keyword.Select.BIGINT.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.BIGINT.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.BIGINT.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class BINARY {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.BINARY._SELECT<D>, keyword.Select.BINARY.FROM<D>, keyword.Select.BINARY.GROUP_BY<D>, keyword.Select.BINARY.HAVING<D>, keyword.Select.BINARY.UNION<D>, keyword.Select.BINARY.JOIN<D>, keyword.Select.BINARY.ADV_JOIN<D>, keyword.Select.BINARY.ON<D>, keyword.Select.BINARY.ORDER_BY<D>, keyword.Select.BINARY.LIMIT<D>, keyword.Select.BINARY.OFFSET<D>, keyword.Select.BINARY.FOR<D>, keyword.Select.BINARY.NOWAIT<D>, keyword.Select.BINARY.SKIP_LOCKED<D>, keyword.Select.BINARY.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.BINARY.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.BINARY.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class BLOB {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.BLOB._SELECT<D>, keyword.Select.BLOB.FROM<D>, keyword.Select.BLOB.GROUP_BY<D>, keyword.Select.BLOB.HAVING<D>, keyword.Select.BLOB.UNION<D>, keyword.Select.BLOB.JOIN<D>, keyword.Select.BLOB.ADV_JOIN<D>, keyword.Select.BLOB.ON<D>, keyword.Select.BLOB.ORDER_BY<D>, keyword.Select.BLOB.LIMIT<D>, keyword.Select.BLOB.OFFSET<D>, keyword.Select.BLOB.FOR<D>, keyword.Select.BLOB.NOWAIT<D>, keyword.Select.BLOB.SKIP_LOCKED<D>, keyword.Select.BLOB.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.BLOB.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.BLOB.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class BOOLEAN {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.BOOLEAN._SELECT<D>, keyword.Select.BOOLEAN.FROM<D>, keyword.Select.BOOLEAN.GROUP_BY<D>, keyword.Select.BOOLEAN.HAVING<D>, keyword.Select.BOOLEAN.UNION<D>, keyword.Select.BOOLEAN.JOIN<D>, keyword.Select.BOOLEAN.ADV_JOIN<D>, keyword.Select.BOOLEAN.ON<D>, keyword.Select.BOOLEAN.ORDER_BY<D>, keyword.Select.BOOLEAN.LIMIT<D>, keyword.Select.BOOLEAN.OFFSET<D>, keyword.Select.BOOLEAN.FOR<D>, keyword.Select.BOOLEAN.NOWAIT<D>, keyword.Select.BOOLEAN.SKIP_LOCKED<D>, keyword.Select.BOOLEAN.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.BOOLEAN.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.BOOLEAN.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class CHAR {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.CHAR._SELECT<D>, keyword.Select.CHAR.FROM<D>, keyword.Select.CHAR.GROUP_BY<D>, keyword.Select.CHAR.HAVING<D>, keyword.Select.CHAR.UNION<D>, keyword.Select.CHAR.JOIN<D>, keyword.Select.CHAR.ADV_JOIN<D>, keyword.Select.CHAR.ON<D>, keyword.Select.CHAR.ORDER_BY<D>, keyword.Select.CHAR.LIMIT<D>, keyword.Select.CHAR.OFFSET<D>, keyword.Select.CHAR.FOR<D>, keyword.Select.CHAR.NOWAIT<D>, keyword.Select.CHAR.SKIP_LOCKED<D>, keyword.Select.CHAR.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.CHAR.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.CHAR.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class CLOB {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.CLOB._SELECT<D>, keyword.Select.CLOB.FROM<D>, keyword.Select.CLOB.GROUP_BY<D>, keyword.Select.CLOB.HAVING<D>, keyword.Select.CLOB.UNION<D>, keyword.Select.CLOB.JOIN<D>, keyword.Select.CLOB.ADV_JOIN<D>, keyword.Select.CLOB.ON<D>, keyword.Select.CLOB.ORDER_BY<D>, keyword.Select.CLOB.LIMIT<D>, keyword.Select.CLOB.OFFSET<D>, keyword.Select.CLOB.FOR<D>, keyword.Select.CLOB.NOWAIT<D>, keyword.Select.CLOB.SKIP_LOCKED<D>, keyword.Select.CLOB.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.CLOB.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.CLOB.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class Column {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.Column._SELECT<D>, keyword.Select.Column.FROM<D>, keyword.Select.Column.GROUP_BY<D>, keyword.Select.Column.HAVING<D>, keyword.Select.Column.UNION<D>, keyword.Select.Column.JOIN<D>, keyword.Select.Column.ADV_JOIN<D>, keyword.Select.Column.ON<D>, keyword.Select.Column.ORDER_BY<D>, keyword.Select.Column.LIMIT<D>, keyword.Select.Column.OFFSET<D>, keyword.Select.Column.FOR<D>, keyword.Select.Column.NOWAIT<D>, keyword.Select.Column.SKIP_LOCKED<D>, keyword.Select.Column.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.Column.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.Column.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class DATE {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.DATE._SELECT<D>, keyword.Select.DATE.FROM<D>, keyword.Select.DATE.GROUP_BY<D>, keyword.Select.DATE.HAVING<D>, keyword.Select.DATE.UNION<D>, keyword.Select.DATE.JOIN<D>, keyword.Select.DATE.ADV_JOIN<D>, keyword.Select.DATE.ON<D>, keyword.Select.DATE.ORDER_BY<D>, keyword.Select.DATE.LIMIT<D>, keyword.Select.DATE.OFFSET<D>, keyword.Select.DATE.FOR<D>, keyword.Select.DATE.NOWAIT<D>, keyword.Select.DATE.SKIP_LOCKED<D>, keyword.Select.DATE.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.DATE.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.DATE.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class DATETIME {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.DATETIME._SELECT<D>, keyword.Select.DATETIME.FROM<D>, keyword.Select.DATETIME.GROUP_BY<D>, keyword.Select.DATETIME.HAVING<D>, keyword.Select.DATETIME.UNION<D>, keyword.Select.DATETIME.JOIN<D>, keyword.Select.DATETIME.ADV_JOIN<D>, keyword.Select.DATETIME.ON<D>, keyword.Select.DATETIME.ORDER_BY<D>, keyword.Select.DATETIME.LIMIT<D>, keyword.Select.DATETIME.OFFSET<D>, keyword.Select.DATETIME.FOR<D>, keyword.Select.DATETIME.NOWAIT<D>, keyword.Select.DATETIME.SKIP_LOCKED<D>, keyword.Select.DATETIME.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.DATETIME.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.DATETIME.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class DECIMAL {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.DECIMAL._SELECT<D>, keyword.Select.DECIMAL.FROM<D>, keyword.Select.DECIMAL.GROUP_BY<D>, keyword.Select.DECIMAL.HAVING<D>, keyword.Select.DECIMAL.UNION<D>, keyword.Select.DECIMAL.JOIN<D>, keyword.Select.DECIMAL.ADV_JOIN<D>, keyword.Select.DECIMAL.ON<D>, keyword.Select.DECIMAL.ORDER_BY<D>, keyword.Select.DECIMAL.LIMIT<D>, keyword.Select.DECIMAL.OFFSET<D>, keyword.Select.DECIMAL.FOR<D>, keyword.Select.DECIMAL.NOWAIT<D>, keyword.Select.DECIMAL.SKIP_LOCKED<D>, keyword.Select.DECIMAL.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.DECIMAL.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.DECIMAL.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class DOUBLE {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.DOUBLE._SELECT<D>, keyword.Select.DOUBLE.FROM<D>, keyword.Select.DOUBLE.GROUP_BY<D>, keyword.Select.DOUBLE.HAVING<D>, keyword.Select.DOUBLE.UNION<D>, keyword.Select.DOUBLE.JOIN<D>, keyword.Select.DOUBLE.ADV_JOIN<D>, keyword.Select.DOUBLE.ON<D>, keyword.Select.DOUBLE.ORDER_BY<D>, keyword.Select.DOUBLE.LIMIT<D>, keyword.Select.DOUBLE.OFFSET<D>, keyword.Select.DOUBLE.FOR<D>, keyword.Select.DOUBLE.NOWAIT<D>, keyword.Select.DOUBLE.SKIP_LOCKED<D>, keyword.Select.DOUBLE.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.DOUBLE.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.DOUBLE.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class Entity {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.Entity._SELECT<D>, keyword.Select.Entity.FROM<D>, keyword.Select.Entity.GROUP_BY<D>, keyword.Select.Entity.HAVING<D>, keyword.Select.Entity.UNION<D>, keyword.Select.Entity.JOIN<D>, keyword.Select.Entity.ADV_JOIN<D>, keyword.Select.Entity.ON<D>, keyword.Select.Entity.ORDER_BY<D>, keyword.Select.Entity.LIMIT<D>, keyword.Select.Entity.OFFSET<D>, keyword.Select.Entity.FOR<D>, keyword.Select.Entity.NOWAIT<D>, keyword.Select.Entity.SKIP_LOCKED<D>, keyword.Select.Entity.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        SELECT(final boolean distinct, final data.Table[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.Entity.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.Entity.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class ENUM {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.ENUM._SELECT<D>, keyword.Select.ENUM.FROM<D>, keyword.Select.ENUM.GROUP_BY<D>, keyword.Select.ENUM.HAVING<D>, keyword.Select.ENUM.UNION<D>, keyword.Select.ENUM.JOIN<D>, keyword.Select.ENUM.ADV_JOIN<D>, keyword.Select.ENUM.ON<D>, keyword.Select.ENUM.ORDER_BY<D>, keyword.Select.ENUM.LIMIT<D>, keyword.Select.ENUM.OFFSET<D>, keyword.Select.ENUM.FOR<D>, keyword.Select.ENUM.NOWAIT<D>, keyword.Select.ENUM.SKIP_LOCKED<D>, keyword.Select.ENUM.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.ENUM.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.ENUM.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class FLOAT {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.FLOAT._SELECT<D>, keyword.Select.FLOAT.FROM<D>, keyword.Select.FLOAT.GROUP_BY<D>, keyword.Select.FLOAT.HAVING<D>, keyword.Select.FLOAT.UNION<D>, keyword.Select.FLOAT.JOIN<D>, keyword.Select.FLOAT.ADV_JOIN<D>, keyword.Select.FLOAT.ON<D>, keyword.Select.FLOAT.ORDER_BY<D>, keyword.Select.FLOAT.LIMIT<D>, keyword.Select.FLOAT.OFFSET<D>, keyword.Select.FLOAT.FOR<D>, keyword.Select.FLOAT.NOWAIT<D>, keyword.Select.FLOAT.SKIP_LOCKED<D>, keyword.Select.FLOAT.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.FLOAT.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.FLOAT.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class INT {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.INT._SELECT<D>, keyword.Select.INT.FROM<D>, keyword.Select.INT.GROUP_BY<D>, keyword.Select.INT.HAVING<D>, keyword.Select.INT.UNION<D>, keyword.Select.INT.JOIN<D>, keyword.Select.INT.ADV_JOIN<D>, keyword.Select.INT.ON<D>, keyword.Select.INT.ORDER_BY<D>, keyword.Select.INT.LIMIT<D>, keyword.Select.INT.OFFSET<D>, keyword.Select.INT.FOR<D>, keyword.Select.INT.NOWAIT<D>, keyword.Select.INT.SKIP_LOCKED<D>, keyword.Select.INT.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.INT.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.INT.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class LargeObject {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.LargeObject._SELECT<D>, keyword.Select.LargeObject.FROM<D>, keyword.Select.LargeObject.GROUP_BY<D>, keyword.Select.LargeObject.HAVING<D>, keyword.Select.LargeObject.UNION<D>, keyword.Select.LargeObject.JOIN<D>, keyword.Select.LargeObject.ADV_JOIN<D>, keyword.Select.LargeObject.ON<D>, keyword.Select.LargeObject.ORDER_BY<D>, keyword.Select.LargeObject.LIMIT<D>, keyword.Select.LargeObject.OFFSET<D>, keyword.Select.LargeObject.FOR<D>, keyword.Select.LargeObject.NOWAIT<D>, keyword.Select.LargeObject.SKIP_LOCKED<D>, keyword.Select.LargeObject.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.LargeObject.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.LargeObject.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class Numeric {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.Numeric._SELECT<D>, keyword.Select.Numeric.FROM<D>, keyword.Select.Numeric.GROUP_BY<D>, keyword.Select.Numeric.HAVING<D>, keyword.Select.Numeric.UNION<D>, keyword.Select.Numeric.JOIN<D>, keyword.Select.Numeric.ADV_JOIN<D>, keyword.Select.Numeric.ON<D>, keyword.Select.Numeric.ORDER_BY<D>, keyword.Select.Numeric.LIMIT<D>, keyword.Select.Numeric.OFFSET<D>, keyword.Select.Numeric.FOR<D>, keyword.Select.Numeric.NOWAIT<D>, keyword.Select.Numeric.SKIP_LOCKED<D>, keyword.Select.Numeric.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.Numeric.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.Numeric.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class SMALLINT {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.SMALLINT._SELECT<D>, keyword.Select.SMALLINT.FROM<D>, keyword.Select.SMALLINT.GROUP_BY<D>, keyword.Select.SMALLINT.HAVING<D>, keyword.Select.SMALLINT.UNION<D>, keyword.Select.SMALLINT.JOIN<D>, keyword.Select.SMALLINT.ADV_JOIN<D>, keyword.Select.SMALLINT.ON<D>, keyword.Select.SMALLINT.ORDER_BY<D>, keyword.Select.SMALLINT.LIMIT<D>, keyword.Select.SMALLINT.OFFSET<D>, keyword.Select.SMALLINT.FOR<D>, keyword.Select.SMALLINT.NOWAIT<D>, keyword.Select.SMALLINT.SKIP_LOCKED<D>, keyword.Select.SMALLINT.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.SMALLINT.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.SMALLINT.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class Temporal {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.Temporal._SELECT<D>, keyword.Select.Temporal.FROM<D>, keyword.Select.Temporal.GROUP_BY<D>, keyword.Select.Temporal.HAVING<D>, keyword.Select.Temporal.UNION<D>, keyword.Select.Temporal.JOIN<D>, keyword.Select.Temporal.ADV_JOIN<D>, keyword.Select.Temporal.ON<D>, keyword.Select.Temporal.ORDER_BY<D>, keyword.Select.Temporal.LIMIT<D>, keyword.Select.Temporal.OFFSET<D>, keyword.Select.Temporal.FOR<D>, keyword.Select.Temporal.NOWAIT<D>, keyword.Select.Temporal.SKIP_LOCKED<D>, keyword.Select.Temporal.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.Temporal.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.Temporal.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class Textual {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.Textual._SELECT<D>, keyword.Select.Textual.FROM<D>, keyword.Select.Textual.GROUP_BY<D>, keyword.Select.Textual.HAVING<D>, keyword.Select.Textual.UNION<D>, keyword.Select.Textual.JOIN<D>, keyword.Select.Textual.ADV_JOIN<D>, keyword.Select.Textual.ON<D>, keyword.Select.Textual.ORDER_BY<D>, keyword.Select.Textual.LIMIT<D>, keyword.Select.Textual.OFFSET<D>, keyword.Select.Textual.FOR<D>, keyword.Select.Textual.NOWAIT<D>, keyword.Select.Textual.SKIP_LOCKED<D>, keyword.Select.Textual.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.Textual.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.Textual.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class TIME {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.TIME._SELECT<D>, keyword.Select.TIME.FROM<D>, keyword.Select.TIME.GROUP_BY<D>, keyword.Select.TIME.HAVING<D>, keyword.Select.TIME.UNION<D>, keyword.Select.TIME.JOIN<D>, keyword.Select.TIME.ADV_JOIN<D>, keyword.Select.TIME.ON<D>, keyword.Select.TIME.ORDER_BY<D>, keyword.Select.TIME.LIMIT<D>, keyword.Select.TIME.OFFSET<D>, keyword.Select.TIME.FOR<D>, keyword.Select.TIME.NOWAIT<D>, keyword.Select.TIME.SKIP_LOCKED<D>, keyword.Select.TIME.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.TIME.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.TIME.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    public static class TINYINT {
      static class SELECT<D extends type.Entity> extends untyped.SELECT<D> implements keyword.Select.TINYINT._SELECT<D>, keyword.Select.TINYINT.FROM<D>, keyword.Select.TINYINT.GROUP_BY<D>, keyword.Select.TINYINT.HAVING<D>, keyword.Select.TINYINT.UNION<D>, keyword.Select.TINYINT.JOIN<D>, keyword.Select.TINYINT.ADV_JOIN<D>, keyword.Select.TINYINT.ON<D>, keyword.Select.TINYINT.ORDER_BY<D>, keyword.Select.TINYINT.LIMIT<D>, keyword.Select.TINYINT.OFFSET<D>, keyword.Select.TINYINT.FOR<D>, keyword.Select.TINYINT.NOWAIT<D>, keyword.Select.TINYINT.SKIP_LOCKED<D>, keyword.Select.TINYINT.WHERE<D> {
        SELECT(final boolean distinct, final type.Entity[] entities) {
          super(distinct, entities);
        }

        @Override
        public D AS(final D as) {
          ((data.Entity)as).wrap(new As<D>(this, as, true));
          return as;
        }

        @Override
        public final SELECT<D> FROM(final data.Table ... from) {
          super.FROM(from);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final data.Table table) {
          super.CROSS_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> CROSS_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.CROSS_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final data.Table table) {
          super.NATURAL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> NATURAL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.NATURAL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final data.Table table) {
          super.LEFT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> LEFT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.LEFT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final data.Table table) {
          super.RIGHT_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> RIGHT_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.RIGHT_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final data.Table table) {
          super.FULL_JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> FULL_JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.FULL_JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final data.Table table) {
          super.JOIN(table);
          return this;
        }

        @Override
        public final SELECT<D> JOIN(final keyword.Select.untyped.SELECT<?> select) {
          super.JOIN(select);
          return this;
        }

        @Override
        public final SELECT<D> ON(final Condition<?> on) {
          super.ON(on);
          return this;
        }

        @Override
        public final SELECT<D> GROUP_BY(final data.Entity ... groupBy) {
          super.GROUP_BY(groupBy);
          return this;
        }

        @Override
        public final SELECT<D> HAVING(final Condition<?> having) {
          super.HAVING(having);
          return this;
        }

        @Override
        public final SELECT<D> UNION(final keyword.Select.TINYINT.SELECT<D> select) {
          super.UNION(select);
          return this;
        }

        @Override
        public final SELECT<D> UNION_ALL(final keyword.Select.TINYINT.SELECT<D> select) {
          super.UNION_ALL(select);
          return this;
        }

        @Override
        public final SELECT<D> ORDER_BY(final data.Column<?> ... columns) {
          super.ORDER_BY(columns);
          return this;
        }

        @Override
        public final SELECT<D> LIMIT(final int rows) {
          super.LIMIT(rows);
          return this;
        }

        @Override
        public final SELECT<D> OFFSET(final int rows) {
          super.OFFSET(rows);
          return this;
        }

        @Override
        public final SELECT<D> FOR_SHARE(final data.Entity ... subjects) {
          super.FOR_SHARE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> FOR_UPDATE(final data.Entity ... subjects) {
          super.FOR_UPDATE(subjects);
          return this;
        }

        @Override
        public final SELECT<D> NOWAIT() {
          super.NOWAIT();
          return this;
        }

        @Override
        public final SELECT<D> SKIP_LOCKED() {
          super.SKIP_LOCKED();
          return this;
        }

        @Override
        public final SELECT<D> WHERE(final Condition<?> where) {
          super.WHERE(where);
          return this;
        }
      }
    }

    private Select() {
    }
  }

  static final class CaseImpl implements Case {
    private abstract static class ChainedKeyword extends Keyword {
      final ChainedKeyword root;
      final ChainedKeyword parent;
      ArrayList<data.Column<?>> whenThen;
      data.Column<?> _else;

      ChainedKeyword(final ChainedKeyword root, final ChainedKeyword parent) {
        this.root = assertNotNull(root);
        this.parent = parent;
      }

      ChainedKeyword() {
        this.root = this;
        this.parent = null;
      }

      @Override
      final data.Table getTable() {
        if (root != this)
          return root.getTable();

        for (int i = 0, i$ = whenThen.size(); i < i$; ++i) { // [RA]
          final data.Column<?> column = whenThen.get(i);
          if (column.getTable() != null)
            return column.getTable();
        }

        return null;
      }

      final void WHEN(final data.Column<?> when) {
        if (parent != null) {
          parent.WHEN(when);
        }
        else {
          if (whenThen == null)
            whenThen = new ArrayList<>();

          whenThen.add(when);
        }
      }

      final void THEN(final data.Column<?> then) {
        if (parent != null)
          parent.THEN(then);
        else
          whenThen.add(then);
      }

      final void ELSE(final data.Column<?> _else) {
        if (parent != null)
          parent.ELSE(_else);
        else
          this._else = _else;
      }

      @Override
      void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        if (whenThen != null)
          compilation.compiler.compileWhenThenElse(whenThen, _else, compilation);
      }
    }

    private abstract static class CASE_THEN extends ChainedKeyword {
      CASE_THEN(final ChainedKeyword root, final ChainedKeyword parent) {
        super(root, parent);
      }

      CASE_THEN() {
        super();
      }
    }

    abstract static class CASE extends CASE_THEN {
      CASE() {
        super();
      }

      @Override
      final data.Column<?> getColumn() {
        return null;
      }
    }

    abstract static class WHEN<T> extends ChainedKeyword {
      WHEN(final ChainedKeyword root, final CASE_THEN parent, final data.Column<T> when) {
        super(root, parent);
        WHEN(when);
      }

      WHEN(final data.Column<T> when) {
        super();
        WHEN(when);
      }

      @Override
      final data.Column<?> getColumn() {
        return parent == null ? null : ((CASE_THEN)parent).getColumn();
      }

      @Override
      final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        compilation.compiler.compileWhen((Search.WHEN<?>)this, compilation);
        super.compile(compilation, isExpression);
      }
    }

    private abstract static class THEN_ELSE<D extends data.Column<?>> extends CASE_THEN {
      final D value;

      THEN_ELSE(final ChainedKeyword root, final ChainedKeyword parent, final D value) {
        super(root, parent);
        this.value = value;
      }

      @Override
      final data.Column<?> getColumn() {
        final data.Column<?> column = parent.getColumn();
        return column != null ? column.scaleTo(value) : value;
      }

      @Override
      final void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
        root.compile(compilation, isExpression);
      }
    }

    abstract static class THEN<T,D extends data.Column<?>> extends THEN_ELSE<D> {
      THEN(final ChainedKeyword root, final WHEN<?> parent, final D then) {
        super(root, parent, then);
        THEN(then);
      }
    }

    abstract static class ELSE<D extends data.Column<?>> extends THEN_ELSE<D> {
      ELSE(final ChainedKeyword root, final THEN_ELSE<?> parent, final D _else) {
        super(root, parent, _else);
        ELSE(_else);
      }
    }

    static final class Simple implements simple {
      static final class CASE<T,D extends data.Column<?>> extends CaseImpl.CASE implements simple.CASE<T> {
        final data.Column<T> variable;

        CASE(final data.Column<T> variable) {
          super();
          this.variable = variable;
        }

        @Override
        public final simple.WHEN<T> WHEN(final T when) {
          return new WHEN<T,D>(this, data.wrap(when));
        }

        @Override
        void compile(final Compilation compilation, final boolean isExpression) throws IOException, SQLException {
          final Compiler compiler = compilation.compiler;
          compiler.compileCaseElse(variable, _else, compilation);
          super.compile(compilation, isExpression);
        }
      }

      static final class WHEN<T,D extends data.Column<?>> extends CaseImpl.WHEN<T> implements simple.WHEN<T> {
        WHEN(final CaseImpl.CASE parent, final data.Column<T> when) {
          super(parent, parent, when);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool) {
          return new BOOLEAN.Simple.THEN<>(this, bool);
        }

        @Override
        public final Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
          return new TINYINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
          return new INT.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Simple.THEN<>(this, numeric);
        }

        @Override
        public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
          return new BOOLEAN.Simple.THEN<>(this, data.wrap(bool));
        }

        @Override
        public final Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
          return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.INT.simple.THEN<T> THEN(final int numeric) {
          return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
          return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary) {
          return new BINARY.Simple.THEN<>(this, binary);
        }

        @Override
        public final Case.DATE.simple.THEN<T> THEN(final data.DATE date) {
          return new DATE.Simple.THEN<>(this, date);
        }

        @Override
        public final Case.TIME.simple.THEN<T> THEN(final data.TIME time) {
          return new TIME.Simple.THEN<>(this, time);
        }

        @Override
        public final Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime) {
          return new DATETIME.Simple.THEN<>(this, dateTime);
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> text) {
          return new ENUM.Simple.THEN<>(this, text);
        }

        @Override
        public final Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Simple.THEN<>(this, data.wrap(binary));
        }

        @Override
        public final Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
          return new DATE.Simple.THEN<>(this, data.wrap(date));
        }

        @Override
        public final Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
          return new TIME.Simple.THEN<>(this, data.wrap(time));
        }

        @Override
        public final Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Simple.THEN<>(this, data.wrap(dateTime));
        }

        @Override
        public final Case.CHAR.simple.THEN<T> THEN(final String text) {
          return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.wrap(text));
        }

        @Override
        public final Case.ENUM.simple.THEN<T> THEN(final EntityEnum text) {
          return new ENUM.Simple.THEN<>(this, (data.ENUM<?>)data.wrap(text));
        }
      }

      private Simple() {
      }
    }

    static final class Search implements search {
      static final class WHEN<T> extends CaseImpl.WHEN<T> implements search.WHEN<T> {
        WHEN(final data.Column<T> when) {
          super(when);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool) {
          return new BOOLEAN.Search.THEN<>(this, bool);
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
          return new FLOAT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
          return new DOUBLE.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
          return new DECIMAL.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric) {
          return new TINYINT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
          return new SMALLINT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.INT.search.THEN<T> THEN(final data.INT numeric) {
          return new INT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
          return new BIGINT.Search.THEN<>(this, numeric);
        }

        @Override
        public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
          return new BOOLEAN.Search.THEN<>(this, data.wrap(bool));
        }

        @Override
        public final Case.FLOAT.search.THEN<T> THEN(final float numeric) {
          return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
          return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
          return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
          return new TINYINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
          return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.INT.search.THEN<T> THEN(final int numeric) {
          return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.BIGINT.search.THEN<T> THEN(final long numeric) {
          return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final data.BINARY binary) {
          return new BINARY.Search.THEN<>(this, binary);
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final data.DATE date) {
          return new DATE.Search.THEN<>(this, date);
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final data.TIME time) {
          return new TIME.Search.THEN<>(this, time);
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime) {
          return new DATETIME.Search.THEN<>(this, dateTime);
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
          return new CHAR.Search.THEN<>(this, text);
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> text) {
          return new ENUM.Search.THEN<>(this, text);
        }

        @Override
        public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
          return new BINARY.Search.THEN<>(this, data.wrap(binary));
        }

        @Override
        public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
          return new DATE.Search.THEN<>(this, data.wrap(date));
        }

        @Override
        public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
          return new TIME.Search.THEN<>(this, data.wrap(time));
        }

        @Override
        public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
          return new DATETIME.Search.THEN<>(this, data.wrap(dateTime));
        }

        @Override
        public final Case.CHAR.search.THEN<T> THEN(final String text) {
          return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.wrap(text));
        }

        @Override
        public final Case.ENUM.search.THEN<T> THEN(final EntityEnum text) {
          return new ENUM.Search.THEN<>(this, (data.ENUM<?>)data.wrap(text));
        }
      }

      private Search() {
      }
    }

    static final class BOOLEAN {
      static final class Simple implements Case.BOOLEAN.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.simple.WHEN {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.BOOLEAN.simple.THEN THEN(final data.BOOLEAN bool) {
            return new BOOLEAN.Simple.THEN<>(this, bool);
          }

          @Override
          public final Case.BOOLEAN.simple.THEN THEN(final boolean bool) {
            return new BOOLEAN.Simple.THEN<>(this, data.wrap(bool));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.BOOLEAN> implements Case.BOOLEAN.simple.THEN {
          THEN(final CaseImpl.WHEN<T> parent, final data.BOOLEAN value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.BOOLEAN.ELSE ELSE(final data.BOOLEAN bool) {
            return new BOOLEAN.ELSE(this, bool);
          }

          @Override
          public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
            return new BOOLEAN.ELSE(this, data.wrap(bool));
          }

          @Override
          @SuppressWarnings("unchecked")
          public final Case.BOOLEAN.simple.WHEN WHEN(final boolean condition) {
            return new BOOLEAN.Simple.WHEN<>(this, (data.Column<T>)data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.BOOLEAN.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BOOLEAN.search.CASE<T>, Case.BOOLEAN.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.BOOLEAN.search.THEN<T> THEN(final data.BOOLEAN bool) {
            return new BOOLEAN.Search.THEN<>(this, bool);
          }

          @Override
          public final Case.BOOLEAN.search.THEN<T> THEN(final boolean bool) {
            return new BOOLEAN.Search.THEN<>(this, data.wrap(bool));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.BOOLEAN> implements Case.BOOLEAN.search.THEN<T> {
          THEN(final CaseImpl.WHEN<?> parent, final data.BOOLEAN value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.BOOLEAN.ELSE ELSE(final data.BOOLEAN bool) {
            return new BOOLEAN.ELSE(this, bool);
          }

          @Override
          public final Case.BOOLEAN.ELSE ELSE(final boolean bool) {
            return new BOOLEAN.ELSE(this, data.wrap(bool));
          }

          @Override
          public final Case.BOOLEAN.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new BOOLEAN.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.BOOLEAN> implements Case.BOOLEAN.ELSE {
        ELSE(final THEN_ELSE<data.BOOLEAN> parent, final data.BOOLEAN value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.BOOLEAN END() {
          return new data.BOOLEAN().wrap(this);
        }
      }
    }

    static final class FLOAT {
      static final class Simple implements Case.FLOAT.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final byte numeric) {
            return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.FLOAT.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final byte numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.FLOAT.simple.WHEN<T> WHEN(final T condition) {
            return new FLOAT.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.FLOAT.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.FLOAT.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final byte numeric) {
            return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.FLOAT.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final data.TINYINT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final byte numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.FLOAT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new FLOAT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.FLOAT.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.FLOAT END() {
          return new data.FLOAT().wrap(this);
        }
      }
    }

    static final class DOUBLE {
      static final class Simple implements Case.DOUBLE.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.INT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final byte numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DOUBLE.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final byte numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.DOUBLE.simple.WHEN<T> WHEN(final T condition) {
            return new DOUBLE.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DOUBLE.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DOUBLE.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.INT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final byte numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final short numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final int numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final long numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DOUBLE.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.TINYINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.SMALLINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.INT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.BIGINT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final byte numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final short numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final int numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final long numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.DOUBLE.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DOUBLE.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.DOUBLE.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.DOUBLE END() {
          return new data.DOUBLE().wrap(this);
        }
      }
    }

    static final class TINYINT {
      static final class Simple implements Case.TINYINT.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new TINYINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.TINYINT.simple.THEN<T> THEN(final byte numeric) {
            return new TINYINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.TINYINT.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric) {
            return new TINYINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final byte numeric) {
            return new TINYINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.TINYINT.simple.WHEN<T> WHEN(final T condition) {
            return new TINYINT.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.TINYINT.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TINYINT.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.TINYINT.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new TINYINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.TINYINT.search.THEN<T> THEN(final byte numeric) {
            return new TINYINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.TINYINT.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final data.TINYINT numeric) {
            return new TINYINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.TINYINT.ELSE ELSE(final byte numeric) {
            return new TINYINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.TINYINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new TINYINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.TINYINT.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.TINYINT END() {
          return new data.TINYINT().wrap(this);
        }
      }
    }

    static final class SMALLINT {
      static final class Simple implements Case.SMALLINT.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new SMALLINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.simple.THEN<T> THEN(final float numeric) {
            return new FLOAT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final byte numeric) {
            return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.simple.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.SMALLINT.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final byte numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.SMALLINT.simple.WHEN<T> WHEN(final T condition) {
            return new SMALLINT.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.SMALLINT.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.SMALLINT.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.FLOAT.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new FLOAT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new SMALLINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.FLOAT.search.THEN<T> THEN(final float numeric) {
            return new FLOAT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final byte numeric) {
            return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.search.THEN<T> THEN(final short numeric) {
            return new SMALLINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.SMALLINT.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.FLOAT.ELSE ELSE(final data.FLOAT numeric) {
            return new FLOAT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.TINYINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new SMALLINT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.FLOAT.ELSE ELSE(final float numeric) {
            return new FLOAT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final byte numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.SMALLINT.ELSE ELSE(final short numeric) {
            return new SMALLINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.SMALLINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new SMALLINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.SMALLINT.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.SMALLINT END() {
          final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).getColumn().clone();
          final data.SMALLINT column = numeric instanceof data.SMALLINT ? (data.SMALLINT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.SMALLINT(((data.ExactNumeric<?>)numeric).precision()) : new data.SMALLINT();
          return column.wrap(this);
        }
      }
    }

    static final class INT {
      static final class Simple implements Case.INT.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final data.INT numeric) {
            return new INT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final byte numeric) {
            return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final short numeric) {
            return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.simple.THEN<T> THEN(final int numeric) {
            return new INT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.INT.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.TINYINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.SMALLINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final byte numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final short numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.INT.simple.WHEN<T> WHEN(final T condition) {
            return new INT.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.INT.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.INT.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final data.INT numeric) {
            return new INT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final byte numeric) {
            return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final short numeric) {
            return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.search.THEN<T> THEN(final int numeric) {
            return new INT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.INT.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.TINYINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.SMALLINT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.INT.ELSE ELSE(final data.INT numeric) {
            return new INT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final byte numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final short numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.INT.ELSE ELSE(final int numeric) {
            return new INT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.INT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new INT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.INT.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.INT END() {
          final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).getColumn().clone();
          final data.INT column = numeric instanceof data.INT ? (data.INT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.INT(((data.ExactNumeric<?>)numeric).precision()) : new data.INT();
          return column.wrap(this);
        }
      }
    }

    static final class BIGINT {
      static final class Simple implements Case.BIGINT.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.INT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.simple.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final byte numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final short numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final int numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.simple.THEN<T> THEN(final long numeric) {
            return new BIGINT.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.BIGINT.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.INT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final byte numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final short numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final int numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.BIGINT.simple.WHEN<T> WHEN(final T condition) {
            return new BIGINT.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.BIGINT.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BIGINT.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DOUBLE.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DOUBLE.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.INT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new BIGINT.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final float numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.search.THEN<T> THEN(final double numeric) {
            return new DOUBLE.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final byte numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final short numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final int numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.search.THEN<T> THEN(final long numeric) {
            return new BIGINT.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.BIGINT.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DOUBLE.ELSE ELSE(final data.FLOAT numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final data.DOUBLE numeric) {
            return new DOUBLE.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.TINYINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.SMALLINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.INT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final data.BIGINT numeric) {
            return new BIGINT.ELSE(this, numeric);
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final float numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DOUBLE.ELSE ELSE(final double numeric) {
            return new DOUBLE.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final byte numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final short numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final int numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.BIGINT.ELSE ELSE(final long numeric) {
            return new BIGINT.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.BIGINT.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new BIGINT.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.BIGINT.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.BIGINT END() {
          final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).getColumn().clone();
          final data.BIGINT column = numeric instanceof data.BIGINT ? (data.BIGINT)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.BIGINT(((data.ExactNumeric<?>)numeric).precision()) : new data.BIGINT();
          return column.wrap(this);
        }
      }
    }

    static final class DECIMAL {
      static final class Simple implements Case.DECIMAL.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.FLOAT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.TINYINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.INT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final data.BIGINT numeric) {
            return new DECIMAL.Simple.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final float numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final double numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final byte numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final short numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final int numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.simple.THEN<T> THEN(final long numeric) {
            return new DECIMAL.Simple.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DECIMAL.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.INT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final float numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final double numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final byte numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final short numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final int numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final long numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.DECIMAL.simple.WHEN<T> WHEN(final T condition) {
            return new DECIMAL.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DECIMAL.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DECIMAL.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DECIMAL.search.THEN<T> THEN(final data.FLOAT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DOUBLE numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.DECIMAL numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.TINYINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.SMALLINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.INT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final data.BIGINT numeric) {
            return new DECIMAL.Search.THEN<>(this, numeric);
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final float numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final double numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final BigDecimal numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final byte numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final short numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final int numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.search.THEN<T> THEN(final long numeric) {
            return new DECIMAL.Search.THEN<>(this, (data.Numeric<?>)data.wrap(numeric));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Numeric<?>> implements Case.DECIMAL.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Numeric<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DECIMAL.ELSE ELSE(final data.FLOAT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DOUBLE numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.DECIMAL numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.TINYINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.SMALLINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.INT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final data.BIGINT numeric) {
            return new DECIMAL.ELSE(this, numeric);
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final float numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final double numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final BigDecimal numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final byte numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final short numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final int numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public Case.DECIMAL.ELSE ELSE(final long numeric) {
            return new DECIMAL.ELSE(this, (data.Numeric<?>)data.wrap(numeric));
          }

          @Override
          public final Case.DECIMAL.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DECIMAL.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Numeric<?>> implements Case.DECIMAL.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Numeric<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.DECIMAL END() {
          final data.Numeric<?> numeric = (data.Numeric<?>)((THEN_ELSE<?>)parent).getColumn().clone();
          final data.DECIMAL column = numeric instanceof data.DECIMAL ? (data.DECIMAL)numeric.clone() : numeric instanceof data.ExactNumeric ? new data.DECIMAL(((data.ExactNumeric<?>)numeric).precision(), Integer.valueOf(0)) : new data.DECIMAL();
          return column.wrap(this);
        }
      }
    }

    static final class BINARY {
      static final class Simple implements Case.BINARY.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.BINARY.simple.THEN<T> THEN(final data.BINARY binary) {
            return new BINARY.Simple.THEN<>(this, binary);
          }

          @Override
          public Case.BINARY.simple.THEN<T> THEN(final byte[] binary) {
            return new BINARY.Simple.THEN<>(this, data.wrap(binary));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.BINARY> implements Case.BINARY.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.BINARY value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.BINARY.ELSE ELSE(final data.BINARY binary) {
            return new BINARY.ELSE(this, binary);
          }

          @Override
          public Case.BINARY.ELSE ELSE(final byte[] binary) {
            return new BINARY.ELSE(this, data.wrap(binary));
          }

          @Override
          public final Case.BINARY.simple.WHEN<T> WHEN(final T condition) {
            return new BINARY.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.BINARY.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.BINARY.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.BINARY.search.THEN<T> THEN(final data.BINARY binary) {
            return new BINARY.Search.THEN<>(this, binary);
          }

          @Override
          public final Case.BINARY.search.THEN<T> THEN(final byte[] binary) {
            return new BINARY.Search.THEN<>(this, data.wrap(binary));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.BINARY> implements Case.BINARY.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.BINARY value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.BINARY.ELSE ELSE(final data.BINARY binary) {
            return new BINARY.ELSE(this, binary);
          }

          @Override
          public final Case.BINARY.ELSE ELSE(final byte[] binary) {
            return new BINARY.ELSE(this, data.wrap(binary));
          }

          @Override
          public final Case.BINARY.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new BINARY.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.BINARY> implements Case.BINARY.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.BINARY value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.BINARY END() {
          final data.BINARY column = (data.BINARY)((THEN_ELSE<?>)parent).getColumn().clone();
          return column.wrap(this);
        }
      }
    }

    static final class DATE {
      static final class Simple implements Case.DATE.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DATE.simple.THEN<T> THEN(final data.DATE date) {
            return new DATE.Simple.THEN<>(this, date);
          }

          @Override
          public Case.DATE.simple.THEN<T> THEN(final LocalDate date) {
            return new DATE.Simple.THEN<>(this, data.wrap(date));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.DATE> implements Case.DATE.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.DATE value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DATE.ELSE ELSE(final data.DATE date) {
            return new DATE.ELSE(this, date);
          }

          @Override
          public Case.DATE.ELSE ELSE(final LocalDate date) {
            return new DATE.ELSE(this, data.wrap(date));
          }

          @Override
          public final Case.DATE.simple.WHEN<T> WHEN(final T condition) {
            return new DATE.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DATE.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATE.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DATE.search.THEN<T> THEN(final data.DATE date) {
            return new DATE.Search.THEN<>(this, date);
          }

          @Override
          public final Case.DATE.search.THEN<T> THEN(final LocalDate date) {
            return new DATE.Search.THEN<>(this, data.wrap(date));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.DATE> implements Case.DATE.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.DATE value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DATE.ELSE ELSE(final data.DATE date) {
            return new DATE.ELSE(this, date);
          }

          @Override
          public final Case.DATE.ELSE ELSE(final LocalDate date) {
            return new DATE.ELSE(this, data.wrap(date));
          }

          @Override
          public final Case.DATE.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DATE.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.DATE> implements Case.DATE.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.DATE value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.DATE END() {
          final data.DATE column = (data.DATE)((THEN_ELSE<?>)parent).getColumn().clone();
          return column.wrap(this);
        }
      }
    }

    static final class TIME {
      static final class Simple implements Case.TIME.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.TIME.simple.THEN<T> THEN(final data.TIME time) {
            return new TIME.Simple.THEN<>(this, time);
          }

          @Override
          public Case.TIME.simple.THEN<T> THEN(final LocalTime time) {
            return new TIME.Simple.THEN<>(this, data.wrap(time));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.TIME> implements Case.TIME.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.TIME value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.TIME.ELSE ELSE(final data.TIME time) {
            return new TIME.ELSE(this, time);
          }

          @Override
          public Case.TIME.ELSE ELSE(final LocalTime time) {
            return new TIME.ELSE(this, data.wrap(time));
          }

          @Override
          public final Case.TIME.simple.WHEN<T> WHEN(final T condition) {
            return new TIME.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.TIME.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.TIME.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.TIME.search.THEN<T> THEN(final data.TIME time) {
            return new TIME.Search.THEN<>(this, time);
          }

          @Override
          public final Case.TIME.search.THEN<T> THEN(final LocalTime time) {
            return new TIME.Search.THEN<>(this, data.wrap(time));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.TIME> implements Case.TIME.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.TIME value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.TIME.ELSE ELSE(final data.TIME time) {
            return new TIME.ELSE(this, time);
          }

          @Override
          public final Case.TIME.ELSE ELSE(final LocalTime time) {
            return new TIME.ELSE(this, data.wrap(time));
          }

          @Override
          public final Case.TIME.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new TIME.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.TIME> implements Case.TIME.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.TIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.TIME END() {
          return (data.TIME)((THEN_ELSE<?>)parent).getColumn().clone().wrap(this);
        }
      }
    }

    static final class DATETIME {
      static final class Simple implements Case.DATETIME.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public Case.DATETIME.simple.THEN<T> THEN(final data.DATETIME dateTime) {
            return new DATETIME.Simple.THEN<>(this, dateTime);
          }

          @Override
          public Case.DATETIME.simple.THEN<T> THEN(final LocalDateTime dateTime) {
            return new DATETIME.Simple.THEN<>(this, data.wrap(dateTime));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.DATETIME> implements Case.DATETIME.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.DATETIME value) {
            super(parent.root, parent, value);
          }

          @Override
          public Case.DATETIME.ELSE ELSE(final data.DATETIME dateTime) {
            return new DATETIME.ELSE(this, dateTime);
          }

          @Override
          public Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
            return new DATETIME.ELSE(this, data.wrap(dateTime));
          }

          @Override
          public final Case.DATETIME.simple.WHEN<T> WHEN(final T condition) {
            return new DATETIME.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.DATETIME.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.DATETIME.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.DATETIME.search.THEN<T> THEN(final data.DATETIME dateTime) {
            return new DATETIME.Search.THEN<>(this, dateTime);
          }

          @Override
          public final Case.DATETIME.search.THEN<T> THEN(final LocalDateTime dateTime) {
            return new DATETIME.Search.THEN<>(this, data.wrap(dateTime));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.DATETIME> implements Case.DATETIME.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.DATETIME value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.DATETIME.ELSE ELSE(final data.DATETIME dateTime) {
            return new DATETIME.ELSE(this, dateTime);
          }

          @Override
          public final Case.DATETIME.ELSE ELSE(final LocalDateTime dateTime) {
            return new DATETIME.ELSE(this, data.wrap(dateTime));
          }

          @Override
          public final Case.DATETIME.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new DATETIME.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.DATETIME> implements Case.DATETIME.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.DATETIME value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.DATETIME END() {
          return (data.DATETIME)((THEN_ELSE<?>)parent).getColumn().clone().wrap(this);
        }
      }
    }

    static final class CHAR {
      static final class Simple implements Case.CHAR.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final data.ENUM<?> text) {
            return new CHAR.Simple.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
            return new CHAR.Simple.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final EntityEnum text) {
            return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final String text) {
            return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.CHAR.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.ENUM<?> text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final EntityEnum text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text.toString()));
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final String text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.CHAR.simple.WHEN<T> WHEN(final T condition) {
            return new CHAR.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.CHAR.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.CHAR.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final data.ENUM<?> text) {
            return new CHAR.Search.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
            return new CHAR.Search.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final String text) {
            return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final EntityEnum text) {
            return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.CHAR.search.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.ENUM<?> text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final String text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final EntityEnum text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text.toString()));
          }

          @Override
          public final Case.CHAR.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new CHAR.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Textual<?>> implements Case.CHAR.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.CHAR END() {
          final data.Textual<?> textual = (data.Textual<?>)((THEN_ELSE<?>)parent).getColumn();
          final data.CHAR column = textual instanceof data.CHAR ? (data.CHAR)textual.clone() : new data.CHAR(textual.length());
          return column.wrap(this);
        }
      }
    }

    static final class ENUM {
      static final class Simple implements Case.ENUM.simple {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.simple.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.ENUM.simple.THEN<T> THEN(final data.ENUM<?> text) {
            return new ENUM.Simple.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final data.CHAR text) {
            return new CHAR.Simple.THEN<>(this, text);
          }

          @Override
          public final Case.ENUM.simple.THEN<T> THEN(final EntityEnum text) {
            return new ENUM.Simple.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.CHAR.simple.THEN<T> THEN(final String text) {
            return new CHAR.Simple.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.ENUM.simple.THEN<T> {
          THEN(final CaseImpl.WHEN<T> parent, final data.Textual<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.ENUM.ELSE ELSE(final EntityEnum text) {
            return new ENUM.ELSE(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.ENUM.ELSE ELSE(final data.ENUM<?> text) {
            return new ENUM.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final String text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.ENUM.simple.WHEN<T> WHEN(final T condition) {
            return new ENUM.Simple.WHEN<>(this, data.wrap(condition));
          }
        }
      }

      static final class Search implements Case.ENUM.search {
        static final class WHEN<T> extends CaseImpl.WHEN<T> implements Case.ENUM.search.WHEN<T> {
          WHEN(final CaseImpl.CASE_THEN parent, final data.Column<T> when) {
            super(parent, parent, when);
          }

          @Override
          public final Case.ENUM.search.THEN<T> THEN(final data.ENUM<?> text) {
            return new ENUM.Search.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final data.CHAR text) {
            return new CHAR.Search.THEN<>(this, text);
          }

          @Override
          public final Case.CHAR.search.THEN<T> THEN(final String text) {
            return new CHAR.Search.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.ENUM.search.THEN<T> THEN(final EntityEnum text) {
            return new ENUM.Search.THEN<>(this, (data.Textual<?>)data.wrap(text));
          }
        }

        static final class THEN<T> extends CaseImpl.THEN<T,data.Textual<?>> implements Case.ENUM.search.THEN<T> {
          THEN(final CaseImpl.WHEN<?> parent, final data.Textual<?> value) {
            super(parent.root, parent, value);
          }

          @Override
          public final Case.ENUM.ELSE ELSE(final data.ENUM<?> text) {
            return new ENUM.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final data.CHAR text) {
            return new CHAR.ELSE(this, text);
          }

          @Override
          public final Case.CHAR.ELSE ELSE(final String text) {
            return new CHAR.ELSE(this, (data.Textual<?>)data.wrap(text));
          }

          @Override
          public final Case.ENUM.ELSE ELSE(final EntityEnum text) {
            return new ENUM.ELSE(this, (data.ENUM<?>)data.wrap(text));
          }

          @Override
          public final Case.ENUM.search.WHEN<T> WHEN(final Condition<T> condition) {
            return new ENUM.Search.WHEN<>(this, condition);
          }
        }
      }

      static final class ELSE extends CaseImpl.ELSE<data.Textual<?>> implements Case.ENUM.ELSE {
        ELSE(final THEN_ELSE<?> parent, final data.Textual<?> value) {
          super(parent.root, parent, value);
        }

        @Override
        public final data.ENUM<?> END() {
          return (data.ENUM<?>)((THEN_ELSE<?>)parent).getColumn().clone().wrap(this);
        }
      }
    }

    private CaseImpl() {
    }
  }
}