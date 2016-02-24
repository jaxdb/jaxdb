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

import org.safris.commons.sql.ConnectionProxy;
import org.safris.commons.sql.StatementProxy;
import org.safris.xdb.xde.csql.expression.CASE;
import org.safris.xdb.xdl.DBVendor;

class Update {
  private static abstract class Execute extends Keyword<DataType<?>> {
    public int execute(final Transaction transaction) throws XDEException {
      final Keyword<?> update = getParentRoot(this);
      final Class<? extends Schema> schema = (((UPDATE)update).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));
        serialize(serialization);
        Entity.clearAliases();
        if (serialization.statementType == PreparedStatement.class) {
          final PreparedStatement statement = connection.prepareStatement(serialization.sql.toString());
          serialization.set(statement);
          final int count = statement.executeUpdate();
          StatementProxy.close(statement);
          if (transaction == null)
            ConnectionProxy.close(connection);

          return count;
        }

        if (serialization.statementType == Statement.class) {
          final Statement statement = connection.createStatement();
          final int count = statement.executeUpdate(serialization.sql.toString());
          StatementProxy.close(statement);
          if (transaction == null)
            ConnectionProxy.close(connection);

          return count;
        }

        throw new UnsupportedOperationException("Unsupported statement type: " + serialization.statementType.getName());
      }
      catch (final SQLException e) {
        throw XDEException.lookup(e, vendor);
      }
    }

    public int execute() throws XDEException {
      return execute(null);
    }
  }

  private abstract static class UPDATE_SET extends Execute implements org.safris.xdb.xde.csql.update.UPDATE_SET {
    @Override
    public final <T>SET SET(final DataType<T> set, final Function<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final CASE<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final Field<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final T to) {
      return new SET(this, set, Field.valueOf(to));
    }
  }

  protected final static class WHERE extends Execute implements org.safris.xdb.xde.csql.update.UPDATE {
    private final Keyword<DataType<?>> parent;
    private final Condition<?> condition;

    protected WHERE(final Keyword<DataType<?>> parent, final Condition<?> condition) {
      this.parent = parent;
      this.condition = condition;
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return parent;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      parent.serialize(serialization);
      serialization.sql.append(" WHERE ");
      condition.serialize(serialization);
    }
  }

  protected final static class UPDATE extends UPDATE_SET implements org.safris.xdb.xde.csql.update.UPDATE_SET {
    protected final Entity entity;

    protected UPDATE(final Entity entity) {
      this.entity = entity;
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      serialization.sql.append("UPDATE ");
      entity.serialize(serialization);
    }

    protected String encodeSingle(final Serialization serialization) {
      if (getClass() != UPDATE.class) // means that there are subsequent clauses
        throw new Error("Need to override this");

      String sql = "UPDATE " + entity.name() + " SET ";
      String columns = "";
      String where = "";
      for (final DataType<?> dataType : entity.column())
        if (dataType.primary)
          where += " AND " + dataType.name + " = ?";
        else
          columns += ", " + dataType.name + " = ?";

      sql += columns.substring(2) + " WHERE " + where.substring(5);
      System.out.println(sql);
      return sql;
    }

    @Override
    public int execute() throws XDEException {
      if (false) {
        final UPDATE update = (UPDATE)getParentRoot(this);
        final Class<? extends Schema> schema = update.entity.schema();
        DBVendor vendor = null;
        try {
          try (final Connection connection = Schema.getConnection(schema)) {
            vendor = Schema.getDBVendor(connection);
            final Serialization serialization = new Serialization(vendor, XDERegistry.getStatementType(schema));
            final String sql = encodeSingle(serialization);
            System.out.println(sql);
            if (true)
              return 0;

            try (final PreparedStatement statement = connection.prepareStatement(sql)) {
              // set the updated columns first
              int index = 0;
              for (final DataType<?> dataType : update.entity.column())
                if (!dataType.primary)
                  dataType.set(statement, ++index);

              // then the conditional columns
              for (final DataType<?> dataType : update.entity.column())
                if (dataType.primary)
                  dataType.set(statement, ++index);

              System.err.println(statement.toString());
              return statement.executeUpdate();
            }
          }
        }
        catch (final SQLException e) {
          throw XDEException.lookup(e, vendor);
        }
      }

      Entity.clearAliases();
      return super.execute();
    }
  }

  protected final static class SET extends UPDATE_SET implements org.safris.xdb.xde.csql.update.SET {
    private final Keyword<? extends DataType<?>> parent;
    private final DataType<?> set;
    private final Serializable to;

    protected <T>SET(final Keyword<? extends DataType<?>> parent, final DataType<T> set, final Function<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected <T>SET(final Keyword<? extends DataType<?>> parent, final DataType<T> set, final Aggregate<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    @SuppressWarnings("unchecked")
    protected <T>SET(final Keyword<? extends DataType<?>> parent, final DataType<T> set, final CASE<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = (Expression<Field<T>>)to;
    }

    protected <T>SET(final Keyword<? extends DataType<?>> parent, final DataType<T> set, final Field<T> to) {
      this.parent = parent;
      this.set = set;
      this.to = to;
    }

    protected <T>SET(final Keyword<? extends DataType<?>> parent, final DataType<T> set, final T to) {
      this.parent = parent;
      this.set = set;
      this.to = FieldWrapper.valueOf(to);
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected Keyword<DataType<?>> parent() {
      return null;
    }

    @Override
    protected void serialize(final Serialization serialization) {
      parent.serialize(serialization);
      serialization.sql.append(" SET ");
      set.serialize(serialization);
      serialization.sql.append(" = ");
      to.serialize(serialization);
    }
  }
}