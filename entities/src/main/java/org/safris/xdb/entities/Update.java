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

import org.safris.xdb.entities.exception.SQLExceptionCatalog;
import org.safris.xdb.entities.model.case_;
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
      final UpdateCommand command = (UpdateCommand)normalize();

      final Class<? extends Schema> schema = command.update().entities[0].schema();
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        final DBVendor vendor = Schema.getDBVendor(connection);

        final Serialization serialization = new Serialization(command, vendor, EntityRegistry.getStatementType(schema));
        command.serialize(serialization);
        final int[] count = serialization.execute(connection);
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
    public final <T>SET SET(final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
      return new SET(this, column, to);
    }

    @Override
    public final <T>SET SET(final type.DataType<T> column, final T to) {
      final type.DataType<T> wrap = type.DataType.wrap(to);
      return new SET(this, column, wrap);
    }
  }

  protected static final class UPDATE extends UPDATE_SET implements update.UPDATE_SET {
    protected final Entity[] entities;

    protected UPDATE(final Entity ... entities) {
      super(null);
      this.entities = entities;
    }

    @Override
    protected final Command normalize() {
      return new UpdateCommand(this);
    }
  }

  protected static final class SET extends UPDATE_SET implements update.SET {
    protected final type.DataType<?> column;
    protected final Serializable to;

    protected <T>SET(final Keyword<type.DataType<?>> parent, final type.DataType<? extends T> column, final case_.CASE<? extends T> to) {
      super(parent);
      this.column = column;
      this.to = (Provision)to;
    }

    protected <T>SET(final Keyword<type.DataType<?>> parent, final type.DataType<? extends T> column, final type.DataType<? extends T> to) {
      super(parent);
      this.column = column;
      this.to = to;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
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