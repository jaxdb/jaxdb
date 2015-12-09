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
import java.sql.SQLException;
import java.sql.Statement;

import org.safris.xdb.xde.csql.expression.CASE;

class Update {
  private static abstract class Execute<T> extends cSQL<T> {
    public int execute() throws SQLException {
      final cSQL<?> update = getParentRoot(this);
      final Class<? extends Schema> schema = (((UPDATE<?>)update).table).schema();
      try (final Connection connection = Schema.getConnection(schema)) {
        final Serialization serialization = new Serialization(Schema.getDBVendor(connection), EntityDataSources.getPrototype(schema));
        serialize(serialization);
        clearAliases();
        if (serialization.prototype == PreparedStatement.class) {
          final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString());
          serialization.set(statement);
          return statement.executeUpdate();
        }
        else if (serialization.prototype == Statement.class) {
          final Statement statement = connection.createStatement();
          return statement.executeUpdate(serialization.sql.toString());
        }
        else {
          throw new UnsupportedOperationException("Unsupported Statement prototype class: " + serialization.prototype.getName());
        }
      }
    }
  }

  private abstract static class UPDATE_SET<T> extends Execute<T> implements org.safris.xdb.xde.csql.update.UPDATE_SET<T> {
    public final <B>SET<T,B> SET(final Column<B> set, final Function<B> to) {
      return new SET<T,B>(this, set, to);
    }

    public final <B>SET<T,B> SET(final Column<B> set, final Aggregate<B> to) {
      return new SET<T,B>(this, set, to);
    }

    public final <B>SET<T,B> SET(final Column<B> set, final CASE<B> to) {
      return new SET<T,B>(this, set, to);
    }

    public final <B>SET<T,B> SET(final Column<B> set, final Column<B> to) {
      return new SET<T,B>(this, set, to);
    }

    public final <B>SET<T,B> SET(final Column<B> set, final B to) {
      return new SET<T,B>(this, set, cSQL.valueOf(to));
    }
  }

  protected final static class WHERE<T> extends Execute<T> implements org.safris.xdb.xde.csql.update.UPDATE<T> {
    private final cSQL<T> parent;
    private final Condition<?> condition;

    protected WHERE(final cSQL<T> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      parent.serialize(serialization);
      serialization.sql.append(" WHERE ");
      condition.serialize(serialization);
    }
  }

  protected final static class UPDATE<T> extends UPDATE_SET<T> implements org.safris.xdb.xde.csql.update.UPDATE_SET<T> {
    protected final Table table;

    protected UPDATE(final Table table) {
      this.table = table;
    }

    protected cSQL<?> parent() {
      return null;
    }

    protected void serialize(final Serialization serialization) {
      serialization.sql.append("UPDATE ");
      serialize(table, serialization);
    }

    protected String encodeSingle(final Serialization serialization) {
      if (getClass() != UPDATE.class) // means that there are subsequent clauses
        throw new Error("Need to override this");

      String sql = "UPDATE " + table.name() + " SET ";
      String columns = "";
      String where = "";
      for (final Column<?> column : table.column())
        if (column.primary)
          where += " AND " + column.name + " = ?";
        else
          columns += ", " + column.name + " = ?";

      sql += columns.substring(2) + " WHERE " + where.substring(5);
      System.out.println(sql);
      return sql;
    }

    public int execute() throws SQLException {
      if (false) {
        final cSQL<?> table = getParentRoot(this);
        final Class<? extends Schema> schema = ((Table)table).schema();
        final Connection connection = Schema.getConnection(schema);
        final Serialization serialization = new Serialization(Schema.getDBVendor(connection), EntityDataSources.getPrototype(schema));
        final String sql = encodeSingle(serialization);
        System.out.println(sql);
        if (true)
          return 0;

        final PreparedStatement statement = Schema.getConnection(((Table)table).schema()).prepareStatement(sql);
        // set the updated columns first
        int index = 0;
        for (final Column<?> column : ((Table)table).column())
          if (!column.primary)
            column.set(statement, ++index);

        // then the conditional columns
        for (final Column<?> column : ((Table)table).column())
          if (column.primary)
            column.set(statement, ++index);

        System.err.println(statement.toString());
        return statement.executeUpdate();
      }

      clearAliases();
      return super.execute();
    }
  }

  protected final static class SET<T,B> extends UPDATE_SET<T> implements org.safris.xdb.xde.csql.update.SET<T,B> {
    private final cSQL<T> parent;
    private final Column<B> set;
    private final org.safris.xdb.xde.csql.cSQL<B> to;

    protected SET(final cSQL<T> parent, final Column<B> set, final Function<B> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected SET(final cSQL<T> parent, final Column<B> set, final Aggregate<B> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected SET(final cSQL<T> parent, final Column<B> set, final CASE<B> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected SET(final cSQL<T> parent, final Column<B> set, final Column<B> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected SET(final cSQL<T> parent, final Column<B> set, final cSQL<B> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    public WHERE<T> WHERE(final Condition<?> condition) {
      return new WHERE<T>(this, condition);
    }

    protected cSQL<?> parent() {
      return parent;
    }

    protected void serialize(final Serialization serialization) {
      serialize(parent, serialization);
      serialization.sql.append(" SET ");
      serialize(set, serialization);
      serialization.sql.append(" = ");
      serialize(to, serialization);
    }
  }
}