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

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Set;

import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.model.expression;
import org.safris.xdb.entities.model.select;
import org.safris.xdb.entities.model.update;
import org.safris.xdb.schema.DBVendor;

final class Update extends SQLStatement {
  private static abstract class Execute extends Keyword<type.DataType<?>> implements update.UPDATE {
    protected Execute(final Keyword<type.DataType<?>> parent) {
      super(parent);
    }

    /**
     * Executes the SQL statement in this <code>XDE</code> object.
     *
     * @return the row modification count
     * @exception SQLException if a database access error occurs
     */
    @Override
    public int[] execute(final Transaction transaction) throws IOException, SQLException {
      final Keyword<?> update = getParentRoot(this);
      final Class<? extends Schema> schema = (((UPDATE)update).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = null;
        final Serializer serializer = Serializer.getSerializer(serialization.vendor);
        final UpdateCommand command = (UpdateCommand)normalize();
        serializer.serialize(command.update(), serialization);
        serializer.serialize(command.set(), serialization);
        serializer.serialize(command.where(), serialization);
//        final Serialization serialization = new Serialization(Update.class, vendor, EntityRegistry.getStatementType(schema));
//        serialize(serialization);
        final int[] count = null;//serialization.executeUpdate(connection);
        if (transaction == null)
          connection.close();

        return count;
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }

    @Override
    public int[] execute() throws IOException, SQLException {
      return execute(null);
    }
  }

  private abstract static class UPDATE_SET extends Execute implements update.UPDATE_SET {
    protected UPDATE_SET(final Keyword<type.DataType<?>> parent) {
      super(parent);
    }

    @Override
    public final <T>SET SET(final type.DataType<T> set, final expression.CASE<T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final type.DataType<? extends T> set, final type.DataType<? extends T> to) {
      return new SET(this, set, to);
    }

    @Override
    public final <T>SET SET(final type.DataType<T> set, final T to) {
      final type.DataType<T> wrap = type.DataType.wrap(to);
      return new SET(this, set, wrap);
    }

    @Override
    public <T extends Subject<?>>SET SET(final T set, final select.SELECT<T> to) {
      // TODO:
      throw new UnsupportedOperationException();
    }
  }

  protected static final class UPDATE extends UPDATE_SET implements update.UPDATE_SET {
    protected final Entity entity;

    protected UPDATE(final Entity entity) {
      super(null);
      this.entity = entity;
    }

    @Override
    protected final Command normalize() {
      final UpdateCommand command = (UpdateCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class SET extends UPDATE_SET implements update.SET {
    protected final type.DataType<?> set;
    protected final Serializable to;

    @SuppressWarnings("unchecked")
    protected <T>SET(final Keyword<type.DataType<?>> parent, final type.DataType<? extends T> set, final expression.CASE<? extends T> to) {
      super(parent);
      this.set = set;
      this.to = (Provision<type.DataType<T>>)to;
    }

    protected <T>SET(final Keyword<type.DataType<?>> parent, final type.DataType<? extends T> set, final type.DataType<? extends T> to) {
      super(parent);
      this.set = set;
      this.to = to;
    }

    @Override
    public <T extends Subject<?>>SET SET(final T set, final select.SELECT<T> to) {
      // TODO:
      throw new UnsupportedOperationException();
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    protected Entity getSetColumns(final Set<type.DataType<?>> columns) {
      columns.add(set);
      if (parent() instanceof Update.SET)
        return ((Update.SET)parent()).getSetColumns(columns);

      if (parent() instanceof Update.UPDATE)
        return ((Update.UPDATE)parent()).entity;

      throw new Error("This should not happen, as UPDATE is always followed by SET.");
    }

    @Override
    protected final Command normalize() {
      final UpdateCommand command = (UpdateCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class WHERE extends Execute implements update.UPDATE {
    protected final Condition<?> condition;

    protected WHERE(final Keyword<type.DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    protected final Command normalize() {
      final UpdateCommand command = (UpdateCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }
}