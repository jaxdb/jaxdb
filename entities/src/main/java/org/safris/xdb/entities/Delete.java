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
import org.safris.xdb.entities.spec.delete;
import org.safris.xdb.schema.DBVendor;

final class Delete extends SQLStatement {
  private static abstract class Execute extends Keyword<DataType<?>> implements delete.DELETE {
    protected Execute(final Keyword<DataType<?>> parent) {
      super(parent);
    }

    @Override
    public int[] execute(final Transaction transaction) throws IOException, SQLException {
      final Keyword<?> delete = getParentRoot(this);
      final Class<? extends Schema> schema = (((DELETE)delete).entity).schema();
      DBVendor vendor = null;
      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        vendor = Schema.getDBVendor(connection);
        final Serialization serialization = null;
        final Serializer serializer = Serializer.getSerializer(serialization.vendor);
        final DeleteCommand command = (DeleteCommand)normalize();
        serializer.serialize(command.delete(), serialization);
        serializer.serialize(command.where(), serialization);

//        final Serialization serialization = new Serialization(Delete.class, vendor, EntityRegistry.getStatementType(schema));
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

  protected static final class WHERE extends Execute implements delete.DELETE {
    protected final Condition<?> condition;

    protected WHERE(final Keyword<DataType<?>> parent, final Condition<?> condition) {
      super(parent);
      this.condition = condition;
    }

    @Override
    protected final Command normalize() {
      final DeleteCommand command = (DeleteCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    protected WHERE clone(final Keyword<DataType<?>> parent) {
      return new WHERE(parent, condition);
    }
  }

  protected static final class DELETE extends Execute implements delete.DELETE_WHERE {
    protected final Entity entity;

    protected DELETE(final Entity entity) {
      super(null);
      this.entity = entity;
    }

    @Override
    public WHERE WHERE(final Condition<?> condition) {
      return new WHERE(this, condition);
    }

    @Override
    protected final Command normalize() {
      final DeleteCommand command = (DeleteCommand)parent().normalize();
      command.add(this);
      return command;
    }

    @Override
    protected DELETE clone(final Keyword<DataType<?>> parent) {
      return new DELETE(entity);
    }
  }
}