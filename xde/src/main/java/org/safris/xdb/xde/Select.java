/* Copyright (c) 2015 Seva Safris
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

package org.safris.xdb.xde;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.safris.commons.lang.Pair;
import org.safris.xdb.xde.DML.ALL;
import org.safris.xdb.xde.DML.DISTINCT;
import org.safris.xdb.xde.DML.Direction;
import org.safris.xdb.xde.DML.NATURAL;
import org.safris.xdb.xde.DML.TYPE;
import org.safris.xdb.xde.csql.Entity;
import org.safris.xdb.xdl.DBVendor;

class Select {
  private static void serialize(final List<Pair<Column<?>,Integer>> columns, final Entity entity) {
    if (entity instanceof Table) {
      final Table table = (Table)entity;
      for (int i = 0; i < table.column().length; i++)
        columns.add(new Pair<Column<?>,Integer>(table.column()[i], i));
    }
    else if (entity instanceof Aggregate<?>) {
      final Aggregate<?> aggregate = (Aggregate<?>)entity;
      columns.add(new Pair<Column<?>,Integer>((Column<?>)aggregate.parent(), -1));
    }
    else if (entity instanceof Column<?>) {
      final Column<?> column = (Column<?>)entity;
      columns.add(new Pair<Column<?>,Integer>(column, -1));
    }
    else {
      throw new UnsupportedOperationException("Unknown entity type: " + entity.getClass().getName());
    }
  }

  private static <B extends Entity>RowIterator<B> parseResultSet(final DBVendor vendor, final Connection connection, final Statement statement, final ResultSet resultSet, final SELECT<?> select) throws SQLException {
    final List<Pair<Column<?>,Integer>> columns = new ArrayList<Pair<Column<?>,Integer>>();
    for (final Entity selectable : select.entities)
      Select.serialize(columns, selectable);

    final ResultSetMetaData metaData = resultSet.getMetaData();
    final int noColumns = metaData.getColumnCount();

    return new RowIterator<B>() {
      private final Map<Class<? extends Table>,Table> prototypes = new HashMap<Class<? extends Table>,Table>();
      private final Map<Table,Table> cache = new HashMap<Table,Table>();
      private Table currentTable = null;

      public boolean nextRow() throws XDEException {
        if (rowIndex + 1 < rows.size()) {
          ++rowIndex;
          resetEntities();
          return true;
        }

        Entity[] row;
        int index;
        Table table;
        try {
          if (!resultSet.next())
            return false;

          row = new Entity[select.entities.length];
          index = 0;
          table = null;
          for (int i = 0; i < noColumns; i++) {
            final Pair<Column<?>,Integer> columnPrototype = columns.get(i);
            final Column column;
            if (columnPrototype.b == -1) {
              column = columnPrototype.a.clone();
              row[index++] = column;
            }
            else {
              if (currentTable != null && currentTable != columnPrototype.a.owner) {
                final Table cached = cache.get(table);
                if (cached != null) {
                  row[index++] = cached;
                }
                else {
                  row[index++] = table;
                  cache.put(table, table);
                  prototypes.put(table.getClass(), table.newInstance());
                }
              }

              currentTable = columnPrototype.a.owner;
              table = prototypes.get(currentTable.getClass());
              if (table == null)
                prototypes.put(currentTable.getClass(), table = currentTable.newInstance());

              column = table.column()[columnPrototype.b];
            }

            column.set(column.get(resultSet, i + 1));
          }
        }
        catch (final SQLException e) {
          throw XDEException.lookup(e, vendor);
        }

        if (table != null) {
          final Table cached = cache.get(table);
          row[index++] = cached != null ? cached : table;
        }

        rows.add((B[])row);
        ++rowIndex;
        resetEntities();
        currentTable = null;
        return true;
      }

      public void close() throws XDEException {
        try {
          resultSet.close();
          statement.close();
          connection.close();
        }
        catch (final SQLException e) {
          throw XDEException.lookup(e, vendor);
        }
      }
    };
  }

  private static abstract class Execute<T extends Entity> extends cSQL<T> {
    public final <B extends Entity>RowIterator<B> execute() throws XDEException {
      final SELECT<?> select = (SELECT<?>)getParentRoot(this);
      final Class<? extends Schema> schema = select.from().tables[0].schema();
      DBVendor vendor = null;
      try {
        final Connection connection = Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));

        serialize(serialization);
        clearAliases();

        if (serialization.statementType == PreparedStatement.class) {
          final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString());
          serialization.set(statement);
          final ResultSet resultSet = statement.executeQuery();
          return parseResultSet(serialization.vendor, connection, statement, resultSet, select);
        }

        if (serialization.statementType == Statement.class) {
          final Statement statement = connection.createStatement();
          final ResultSet resultSet = statement.executeQuery(serialization.sql.toString());
          return parseResultSet(serialization.vendor, connection, statement, resultSet, select);
        }

        throw new UnsupportedOperationException("Unsupported Statement prototype class: " + serialization.statementType.getName());
      }
      catch (final SQLException e) {
        throw XDEException.lookup(e, vendor);
      }
    }
  }

  protected static abstract class FROM_JOIN_ON<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.FROM<T> {
    protected final cSQL<?> parent;

    protected FROM_JOIN_ON(final cSQL<?> parent) {
      this.parent = parent;
    }

    public final <B extends Entity>WHERE<B> WHERE(final Condition<?> condition) {
      return new WHERE<B>(this, condition);
    }

    public final <B extends Entity>JOIN<B> JOIN(final Table table) {
      return new JOIN<B>(this, null, null, table);
    }

    public final <B extends Entity>JOIN<B> JOIN(final TYPE type, final Table table) {
      return new JOIN<B>(this, null, type, table);
    }

    public final <B extends Entity>JOIN<B> JOIN(final NATURAL natural, final Table table) {
      return new JOIN<B>(this, natural, null, table);
    }

    public final <B extends Entity>JOIN<B> JOIN(final NATURAL natural, final TYPE type, final Table table) {
      return new JOIN<B>(this, natural, type, table);
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }
  }

  protected final static class FROM<T extends Entity> extends FROM_JOIN_ON<T> implements org.safris.xdb.xde.csql.select.FROM<T> {
    private final Table[] tables;

    protected FROM(final cSQL<?> parent, final Table ... tables) {
      super(parent);
      this.tables = tables;
    }

    public GROUP_BY<T> GROUP_BY(final org.safris.xdb.xde.Column<?> column) {
      return new GROUP_BY<T>(this, column);
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... columns) {
      return new ORDER_BY<B>(this, columns);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" FROM ");

        // FIXME: If FROM is followed by a JOIN, then we must see what table the ON clause is
        // FIXME: referring to, because this table must be the last in the table order here
        for (int i = 0; i < tables.length; i++) {
          if (i > 0)
            serialization.sql.append(", ");

          serialization.sql.append(tableName(tables[i], serialization)).append(" ").append(tableAlias(tables[i], true));
        }
      }
      else {
        throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
      }
    }
  }

  protected final static class GROUP_BY<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.GROUP_BY<T> {
    private final cSQL<?> parent;
    private final org.safris.xdb.xde.Column<?> column;

    protected GROUP_BY(final cSQL<?> parent, final org.safris.xdb.xde.Column<?> column) {
      this.parent = parent;
      this.column = column;
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... columns) {
      return new ORDER_BY<B>(this, columns);
    }

    public HAVING<T> HAVING(final Condition<?> condition) {
      return new HAVING<T>(this, condition);
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" GROUP BY ").append(columnRef(column));
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class HAVING<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.HAVING<T> {
    private final cSQL<?> parent;
    private final Condition<?> condition;

    protected HAVING(final cSQL<?> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... column) {
      return new ORDER_BY<B>(this, column);
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" HAVING ");
        condition.serialize(serialization);
        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class JOIN<T extends Entity> extends FROM_JOIN_ON<T> implements org.safris.xdb.xde.csql.select.JOIN<T> {
    private final NATURAL natural;
    private final TYPE type;
    private final Table table;

    protected JOIN(final cSQL<?> parent, final NATURAL natural, final TYPE type, final Table table) {
      super(parent);
      this.natural = natural;
      this.type = type;
      this.table = table;
    }

    public <B extends Entity>ON<B> ON(final Condition<?> condition) {
      return new ON<B>(this, condition);
    }

    public GROUP_BY<T> GROUP_BY(final org.safris.xdb.xde.Column<?> column) {
      return new GROUP_BY<T>(this, column);
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... columns) {
      return new ORDER_BY<B>(this, columns);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(natural != null ? " NATURAL" : "");
        if (type != null) {
          serialization.sql.append(" ");
          type.serialize(serialization);
        }

        serialization.sql.append(" JOIN ").append(tableName(table, serialization)).append(" ").append(tableAlias(table, true));
        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class ON<T extends Entity> extends FROM_JOIN_ON<T> implements org.safris.xdb.xde.csql.select.ON<T> {
    private final Condition<?> condition;

    protected ON(final cSQL<?> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    public GROUP_BY<T> GROUP_BY(final org.safris.xdb.xde.Column<?> column) {
      return new GROUP_BY<T>(this, column);
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... columns) {
      return new ORDER_BY<B>(this, columns);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" ON (");
        condition.serialize(serialization);
        serialization.sql.append(")");
        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class ORDER_BY<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.ORDER_BY<T> {
    private final cSQL<?> parent;
    private final ORDER_BY.Column<?>[] columns;

    protected ORDER_BY(final cSQL<?> parent, final ORDER_BY.Column<?> ... columns) {
      this.parent = parent;
      this.columns = columns;
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" ORDER BY ");
        for (int i = 0; i < columns.length; i++) {
          final Column<?> column = columns[i];
          if (i > 0)
            serialization.sql.append(", ");

          if (column instanceof org.safris.xdb.xde.Column<?>) {
            final org.safris.xdb.xde.Column<?> col = (org.safris.xdb.xde.Column<?>)column;
            tableAlias(col.owner, true);
            col.serialize(serialization);
            serialization.sql.append(" ASC");
          }
          else if (column instanceof Direction<?>) {
            ((Direction<?>)column).serialize(serialization);
          }
          else {
            throw new Error("Unknown column type: " + column.getClass().getName());
          }
        }

        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class LIMIT<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.LIMIT<T> {
    private final cSQL<?> parent;
    private final int limit;

    protected LIMIT(final cSQL<?> parent, final int limit) {
      this.parent = parent;
      this.limit = limit;
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" LIMIT " + limit);
        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }
  }

  protected final static class SELECT<T extends Entity> extends cSQL<T> implements org.safris.xdb.xde.csql.select._SELECT<T> {
    private final ALL all;
    private final DISTINCT distinct;
    protected final Entity[] entities;
    private FROM<T> from;

    @SafeVarargs
    public SELECT(final ALL all, final DISTINCT distinct, final Entity ... entities) {
      this.all = all;
      this.distinct = distinct;
      this.entities = entities;
    }

    public FROM<T> FROM(final Table ... table) {
      if (from != null)
        throw new IllegalStateException("FROM() has already been called for this SELECT object.");

      return from = new FROM<T>(this, table);
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }

    protected cSQL<?> parent() {
      return null;
    }

    protected FROM<T> from() {
      return from;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        serialization.sql.append("SELECT ");
        if (all != null) {
          all.serialize(serialization);
          serialization.sql.append(" ");
        }

        if (distinct != null) {
          distinct.serialize(serialization);
          serialization.sql.append(" ");
        }

        for (int i = 0; i < entities.length; i++) {
          final cSQL<?> csql = (cSQL<?>)entities[i];
          if (i > 0)
            serialization.sql.append(", ");

          if (csql instanceof Table) {
            final Table table = (Table)csql;
            final String alias = tableAlias(table, true);
            final org.safris.xdb.xde.Column<?>[] columns = table.column();
            for (int j = 0; j < columns.length; j++) {
              final org.safris.xdb.xde.Column<?> column = columns[j];
              if (j > 0)
                serialization.sql.append(", ");

              serialization.sql.append(alias).append(".").append(column.name);
            }
          }
          else if (csql instanceof org.safris.xdb.xde.Column<?>) {
            tableAlias(((org.safris.xdb.xde.Column<?>)csql).owner, true);
            final org.safris.xdb.xde.Column<?> column = (org.safris.xdb.xde.Column<?>)csql;
            column.serialize(serialization);
          }
          else if (csql instanceof Aggregate<?>) {
            final Aggregate<?> aggregate = (Aggregate<?>)csql;
            aggregate.serialize(serialization);
          }
        }

        return;
      }

      throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
    }

    public <B extends Entity>RowIterator<B> execute() throws XDEException {
      if (entities.length == 1) {
        final Table table = (Table)this.entities[0];
        final Table out = table.newInstance();
        final org.safris.xdb.xde.Column<?>[] columns = table.column();
        String sql = "SELECT ";
        String select = "";
        String where = "";
        for (final org.safris.xdb.xde.Column<?> column : columns) {
          if (column.primary)
            where += " AND " + column.name + " = ?";
          else
            select += ", " + column.name;
        }

        sql += select.substring(2) + " FROM " + table.name() + " WHERE " + where.substring(5);
        DBVendor vendor = null;
        try {
          final Connection connection = Schema.getConnection(table.schema());
          vendor = Schema.getDBVendor(connection);
          final DBVendor finalVendor = vendor;
          final PreparedStatement statement = connection.prepareStatement(sql);
          int index = 0;
          for (final org.safris.xdb.xde.Column<?> column : columns)
            if (column.primary)
              column.set(statement, ++index);

          System.err.println(statement.toString());
          try (final ResultSet resultSet = statement.executeQuery()) {
            new RowIterator<B>() {
              public boolean nextRow() throws XDEException {
                if (rowIndex + 1 < rows.size()) {
                  ++rowIndex;
                  resetEntities();
                  return true;
                }

                try {
                  if (!resultSet.next())
                    return false;

                  int index = 0;
                  for (final org.safris.xdb.xde.Column column : out.column())
                    column.set(column.get(resultSet, ++index));
                }
                catch (final SQLException e) {
                  throw XDEException.lookup(e, finalVendor);
                }

                rows.add((B[])new Table[] {out});
                ++rowIndex;
                resetEntities();
                return true;
              }

              public void close() throws XDEException {
                try {
                  resultSet.close();
                  statement.close();
                  connection.close();
                }
                catch (final SQLException e) {
                  throw XDEException.lookup(e, finalVendor);
                }
              }
            };
          }
        }
        catch (final SQLException e) {
          throw XDEException.lookup(e, vendor);
        }
      }

      clearAliases();
      return null;
    }
  }

  protected final static class WHERE<T extends Entity> extends Execute<T> implements org.safris.xdb.xde.csql.select.WHERE<T> {
    private final cSQL<?> parent;
    private final Condition<?> condition;

    protected WHERE(final cSQL<?> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    public <B extends Entity>ORDER_BY<B> ORDER_BY(final ORDER_BY.Column<?> ... columns) {
      return new ORDER_BY<B>(this, columns);
    }

    public GROUP_BY<T> GROUP_BY(final org.safris.xdb.xde.Column<?> column) {
      return new GROUP_BY<T>(this, column);
    }

    public <B extends Entity>LIMIT<B> LIMIT(final int limit) {
      return new LIMIT<B>(this, limit);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      if (serialization.vendor == DBVendor.MY_SQL || serialization.vendor == DBVendor.POSTGRE_SQL) {
        parent.serialize(serialization);
        serialization.sql.append(" WHERE ");
        condition.serialize(serialization);
      }
      else {
        throw new UnsupportedOperationException(serialization.vendor + " DBVendor is not supported.");
      }
    }
  }
}