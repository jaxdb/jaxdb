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

package org.safris.xdb.entities;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.spec.expression;
import org.safris.xdb.entities.spec.select;
import org.safris.xdb.entities.spec.update;
import org.safris.xdb.schema.DBVendor;

final class Update {
  private static abstract class Execute extends Keyword<DataType<?>> implements update.UPDATE {
    protected Execute(final Keyword<DataType<?>> parent) {
      super(parent);
    }

    /**
     * Executes the SQL statement in this <code>XDE</code> object.
     *
     * @return the row modification count
     * @exception SQLException if a database access error occurs
     */
    @Override
    public int[] execute(final Transaction transaction) throws SQLException {
      final Keyword<?> update = getParentRoot(this);
      final Class<? extends Schema> schema = (((UPDATE)update).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = new Serialization(Update.class, vendor, EntityRegistry.getStatementType(schema));
        serialize(serialization);
        Subject.clearAliases();
        final int[] count = serialization.executeUpdate(connection);
        if (transaction == null)
          connection.close();

        return count;
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }

    @Override
    public int[] execute() throws SQLException {
      return execute(null);
    }
  }

  private abstract static class UPDATE_SET extends Execute implements update.UPDATE_SET {
    protected UPDATE_SET(final Keyword<DataType<?>> parent) {
      super(parent);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final expression.CASE<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final Variable<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final DataType<T> set, final T to) {
      return new SET(this, set, Variable.valueOf(to));
    }

    @Override
    public <T extends Subject<?>>SET SET(final T set, final select.SELECT<T> to) {
      throw new UnsupportedOperationException();
    }
  }

  protected static final class WHERE extends Execute implements update.UPDATE {
    protected final Condition<?> condition;

    protected WHERE(final Keyword<DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }
  }

  protected static final class UPDATE extends UPDATE_SET implements update.UPDATE_SET {
    protected final Entity entity;

    protected UPDATE(final Entity entity) {
      super(null);
      this.entity = entity;
    }
  }

  protected static final class SET extends UPDATE_SET implements update.SET {
    protected final DataType<?> set;
    protected final Serializable to;

    @SuppressWarnings("unchecked")
    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final expression.CASE<T> to) {
      super(parent);
      this.set = set;
      this.to = (Provision<Variable<T>>)to;
    }

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final Variable<T> to) {
      super(parent);
      this.set = set;
      this.to = to;
    }

    protected <T>SET(final Keyword<DataType<?>> parent, final DataType<T> set, final T to) {
      super(parent);
      this.set = set;
      this.to = Variable.valueOf(to);
    }

    @Override
    public <T extends Subject<?>>SET SET(final T set, final select.SELECT<T> to) {
      throw new UnsupportedOperationException();
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    protected Entity getSetColumns(final Set<DataType<?>> columns) {
      columns.add(set);
      if (parent() instanceof Update.SET)
        return ((Update.SET)parent()).getSetColumns(columns);

      if (parent() instanceof Update.UPDATE)
        return ((Update.UPDATE)parent()).entity;

      throw new Error("This should not happen, as UPDATE is always followed by SET.");
    }
  }
}