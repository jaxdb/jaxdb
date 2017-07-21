/* Copyright (c) 2015 lib4j
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

package org.libx4j.rdb.jsql;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.libx4j.rdb.jsql.exception.SQLExceptionCatalog;
import org.libx4j.rdb.jsql.model.ExecuteUpdate;
import org.libx4j.rdb.jsql.model.insert;
import org.libx4j.rdb.jsql.model.select;
import org.libx4j.rdb.vendor.DBVendor;

final class Insert {
  protected static abstract class Execute<T extends Subject<?>> extends Keyword<T> implements ExecuteUpdate {
    protected Execute(final Keyword<T> parent) {
      super(parent);
    }

    @Override
    public final int[] execute(final Transaction transaction) throws IOException, SQLException {
      final InsertCommand command = (InsertCommand)normalize();

      final Class<? extends Schema> schema;
      if (command.insert().entities != null)
        schema = command.insert().entities[0].schema();
      else if (command.insert().columns != null)
        schema = command.insert().columns[0].owner.schema();
      else
        throw new UnsupportedOperationException("Expected insert.entities != null || insert.select != null");

      try {
        final Connection connection = transaction != null ? transaction.getConnection() : Schema.getConnection(schema);
        final DBVendor vendor = Schema.getDBVendor(connection);

        final Compilation compilation = new Compilation(command, vendor, DBRegistry.isPrepared(schema), DBRegistry.isBatching(schema));
        command.compile(compilation);
        final int[] count = compilation.execute(connection);
        if (transaction == null)
          connection.close();

        return count;
      }
      catch (final SQLException e) {
        throw SQLExceptionCatalog.lookup(e);
      }
    }

    @Override
    public final int[] execute() throws IOException, SQLException {
      return execute(null);
    }
  }

  protected static final class VALUES<T extends Subject<?>> extends Execute<T> implements insert.VALUES<T> {
    protected final select.SELECT<?> select;

    protected VALUES(final Keyword<T> parent, final select.SELECT<?> select) {
      super(parent);
      this.select = select;
    }

    @Override
    protected Command normalize() {
      final InsertCommand command = (InsertCommand)parent().normalize();
      command.add(this);
      return command;
    }
  }

  protected static final class INSERT<T extends Subject<?>> extends Execute<T> implements insert.INSERT_VALUES<T> {
    protected final Entity[] entities;
    protected final type.DataType<?>[] columns;

    @SafeVarargs
    protected INSERT(final Entity ... entities) {
      super(null);
      this.entities = entities;
      this.columns = null;
    }

    @SafeVarargs
    protected INSERT(final type.DataType<?> ... columns) {
      super(null);
      this.entities = null;
      this.columns = columns;
      Entity entity = columns[0].owner;
      if (entity == null)
        throw new IllegalArgumentException("DataType must belong to an Entity");

      for (int i = 1; i < columns.length; i++)
        if (!columns[i].owner.equals(entity))
          throw new IllegalArgumentException("All columns must belong to the same Entity");
    }

    @Override
    protected final Command normalize() {
      return new InsertCommand(this);
    }

    @Override
    public insert.VALUES<T> VALUES(final select.SELECT<? super T> select) {
      return new VALUES<T>(this, select);
    }
  }
}